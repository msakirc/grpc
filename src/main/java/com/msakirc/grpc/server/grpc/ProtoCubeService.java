package com.msakirc.grpc.server.grpc;

import com.msakirc.grpc.server.data.Cube;
import com.msakirc.grpc.server.data.CubeRepository;
import com.msakirc.grpc.server.proto.CubeRequestProto;
import com.msakirc.grpc.server.proto.CubeShareServiceProtoGrpc.CubeShareServiceProtoImplBase;
import com.msakirc.grpc.server.proto.OlapCubeProto;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class ProtoCubeService extends CubeShareServiceProtoImplBase {
  
  private CubeRepository repository;
  private MongoOperations mongoOperations;
  
  @Autowired
  public ProtoCubeService ( CubeRepository repository ) {
    this.repository = repository;
  }
  
  private Cube findCubeFromSomewhere ( CubeRequestProto request ) {
    Cube eg = new Cube();
    eg.setId( request.getCubeId() );
    Example<Cube> example = Example.of( eg );
    return repository.findOne( example ).orElse( new Cube( "-1", "-1" ) );
  }
  
  @Override
  public void getMondrianCubeProto ( CubeRequestProto request, StreamObserver<OlapCubeProto> responseObserver ) {
    
    Cube cube = findCubeFromSomewhere( request );
    
    OlapCubeProto olapCubeProto = OlapCubeProto.newBuilder()
                                               .setCubeId( cube.getId() )
                                               .setUserId( cube.getUserId() )
                                               .setCaption( cube.getCaption() )
                                               .build();
    
    responseObserver.onNext( olapCubeProto );
    responseObserver.onCompleted();
    
  }
}
