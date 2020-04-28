package com.msakirc.grpc.server.grpc;

import com.google.gson.Gson;
import com.msakirc.grpc.server.data.Cube;
import com.msakirc.grpc.server.data.CubeRepository;
import com.msakirc.grpc.server.proto.CubeRequestJson;
import com.msakirc.grpc.server.proto.CubeShareServiceJsonGrpc.CubeShareServiceJsonImplBase;
import com.msakirc.grpc.server.proto.OlapCubeJson;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class JsonCubeService extends CubeShareServiceJsonImplBase {
  
  private CubeRepository repository;
  private MongoOperations mongoOperations;
  
  @Autowired
  public JsonCubeService ( CubeRepository repository ) {
    this.repository = repository;
  }
  
  private Cube findCubeFromSomewhere ( CubeRequestJson request ) {
    Cube eg = new Cube();
    eg.setId( request.getCubeId() );
    Example<Cube> example = Example.of( eg );
    return repository.findOne( example ).orElse( new Cube( "-1", "-1" ) );
  }
  
  @Override
  public void getOlapCubeJson ( CubeRequestJson request, StreamObserver<OlapCubeJson> responseObserver ) {
    Cube cube = findCubeFromSomewhere( request );
    
    String json = new Gson().toJson( cube );
    
    OlapCubeJson olapCubeJson = OlapCubeJson.newBuilder().setCube( json ).build();
    responseObserver.onNext( olapCubeJson );
    responseObserver.onCompleted();
  }
}
