package com.msakirc.grpc.server.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CubeController {
  
  private CubeRepository repository;
  
  @Autowired
  public CubeController ( CubeRepository repository ) {
    this.repository = repository;
  }
  
  
  // @GetMapping( "/cube" )
  public Cube createCube ( @RequestParam String cubeId, @RequestParam String userId ) {
    return repository.save( new Cube( cubeId, userId ) );
  }
  
}
