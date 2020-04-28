package com.msakirc.grpc.server.grpc;

import com.msakirc.grpc.server.proto.CubeRequestProto;
import com.msakirc.grpc.server.proto.ExtendedCubeShareServiceProtoGrpc.ExtendedCubeShareServiceProtoImplBase;
import com.msakirc.grpc.server.proto.ExtendedHierarchyInfo;
import com.msakirc.grpc.server.proto.ExtendedMondrianCube;
import com.msakirc.grpc.server.proto.ExtendedMondrianDimension;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class ExtendedProtoCubeService extends ExtendedCubeShareServiceProtoImplBase {
  
  public void getMondrianCubeProto ( CubeRequestProto request, StreamObserver<ExtendedMondrianCube> responseObserver ) {
    
    System.out.println( "Received request." );
    
    ExtendedMondrianCube mondrianCube;
    mondrianCube = ExtendedMondrianCube
                       .newBuilder()
                       .setId( "1" )
                       .setCubeName( "cubeName" )
                       .setCaption( "caption" )
                       .addDimensions(
                           ExtendedMondrianDimension
                               .newBuilder()
                               .addHierarchies(
                                   ExtendedHierarchyInfo
                                       .newBuilder()
                                       .addLevels( "level1" )
                                       .addLevels( "level2" )
                                       .build()
                               )
                               .addHierarchies(
                                   ExtendedHierarchyInfo.newBuilder()
                                                        .addLevels( "level3" )
                                                        .addLevels( "level4" )
                                                        .build()
                               )
                               .setDimensionName( "dimensionName" )
                               .build()
                       )
                       .addDimensions(
                           ExtendedMondrianDimension
                               .newBuilder()
                               .addHierarchies(
                                   ExtendedHierarchyInfo.newBuilder()
                                                        .addLevels( "level5" )
                                                        .addLevels( "level6" )
                                                        .build()
                               )
                               .addHierarchies(
                                   ExtendedHierarchyInfo.newBuilder()
                                                        .addLevels( "level7" )
                                                        .addLevels( "level8" )
                                                        .build()
                               )
                               .setDimensionName( "dimensionName2" )
                               .build()
                       )
                       .build();
    
    responseObserver.onNext( mondrianCube );
    responseObserver.onCompleted();
  }
}
