package com.msakirc.grpc.server.grpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.google.protobuf.ByteString;
import com.msakirc.grpc.server.data.Cube;
import com.msakirc.grpc.server.data.CubeRepository;
import com.msakirc.grpc.server.proto.CubeRequestJackson;
import com.msakirc.grpc.server.proto.CubeShareServiceJacksonGrpc.CubeShareServiceJacksonImplBase;
import com.msakirc.grpc.server.proto.OlapCubeJackson;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class JacksonBytesCubeService extends CubeShareServiceJacksonImplBase {
  
  private CubeRepository repository;
  private MongoOperations mongoOperations;
  private final ProtobufMapper mapper;
  private final ProtobufSchema schemaWrapper;
  
  @Autowired
  public JacksonBytesCubeService ( CubeRepository repository ) throws JsonMappingException {
    this.repository = repository;
    mapper = new ProtobufMapper();
    schemaWrapper = mapper.generateSchemaFor( Cube.class );
  }
  
  private Cube findCubeFromSomewhere ( CubeRequestJackson request ) {
    Cube eg = new Cube();
    eg.setId( request.getCubeId() );
    Example<Cube> example = Example.of( eg );
    return repository.findOne( example ).orElse( new Cube( "-1", "-1" ) );
  }
  
  @Override
  public void getOlapCubeJackson ( CubeRequestJackson request, StreamObserver<OlapCubeJackson> responseObserver ) {
    
    try {
      Cube cube = findCubeFromSomewhere( request );
      byte[] protobufData = mapper.writer( schemaWrapper )
                                  .writeValueAsBytes( cube );
      
      ByteString usableData = ByteString.copyFrom( protobufData );
      
      OlapCubeJackson olapCubeProto = OlapCubeJackson.newBuilder().setCube( usableData ).build();
      responseObserver.onNext( olapCubeProto );
      responseObserver.onCompleted();
    }
    catch ( JsonProcessingException e ) {
      e.printStackTrace();
    }
    
  }
}
