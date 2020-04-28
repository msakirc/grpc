package com.msakirc.grpc.server.data;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CubeGenerator {
  
  private Random random;
  private int count;
  private CubeController cubeController;
  
  @Autowired
  public CubeGenerator ( CubeController cubeController ) {
    this.cubeController = cubeController;
    this.random = new Random();
    this.count = 0;
  }
  
  private int getUserId () {
    return random.nextInt( 20 );
  }
  
  @GetMapping( "/cube" )
  public Cube createCube () {
    return cubeController.createCube( String.valueOf( count++ ), String.valueOf( getUserId() ) );
  }
  
  
}
