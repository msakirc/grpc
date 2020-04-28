package com.msakirc.grpc.server;

import com.msakirc.grpc.server.grpc.ExtendedProtoCubeService;
import com.msakirc.grpc.server.grpc.GsonBytesCubeService.GsonBytesCubeServiceImpl;
import com.msakirc.grpc.server.grpc.JacksonBytesCubeService;
import com.msakirc.grpc.server.grpc.JsonCubeService;
import com.msakirc.grpc.server.grpc.ProtoCubeService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcServer {
  
  private ExtendedProtoCubeService extendedProtoCubeService;
  private ProtoCubeService protoCubeService;
  private GsonBytesCubeServiceImpl gsonBytesCubeService;
  private JacksonBytesCubeService jacksonBytesCubeService;
  private JsonCubeService jsonCubeService;
  
  @Autowired
  public GrpcServer ( ExtendedProtoCubeService extendedProtoCubeService, ProtoCubeService protoCubeService, GsonBytesCubeServiceImpl gsonBytesCubeService,
      JacksonBytesCubeService jacksonBytesCubeService, JsonCubeService jsonCubeService ) {
    this.extendedProtoCubeService = extendedProtoCubeService;
    this.protoCubeService = protoCubeService;
    this.gsonBytesCubeService = gsonBytesCubeService;
    this.jacksonBytesCubeService = jacksonBytesCubeService;
    this.jsonCubeService = jsonCubeService;
  }
  
  @PostConstruct
  private void construct () throws IOException {
    Server server = ServerBuilder.forPort( 8080 )
                                 .addService( extendedProtoCubeService )
                                 .addService( protoCubeService )
                                 .addService( gsonBytesCubeService )
                                 .addService( jacksonBytesCubeService )
                                 .addService( jsonCubeService )
                                 .build();
    
    System.out.println( "Starting server..." );
    server.start();
    System.out.println( "Server started!" );
    // server.awaitTermination();
  }
  
}
