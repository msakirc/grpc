package com.msakirc.grpc.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cube {
  
  private String id;
  
  private String userId;
  
  private String caption;
  
  public Cube ( String id, String userId ) {
    this.id = id;
    this.userId = userId;
    this.caption = String.format( "caption_%s_%s", id, userId );
  }
}
