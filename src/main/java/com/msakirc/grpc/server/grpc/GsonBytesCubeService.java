package com.msakirc.grpc.server.grpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.msakirc.grpc.server.data.Cube;
import com.msakirc.grpc.server.data.CubeRepository;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.MethodDescriptor.Marshaller;
import io.grpc.MethodDescriptor.MethodType;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

public class GsonBytesCubeService {
  
  public static final String SERVICE_NAME = "CubeShareServiceGson";
  public static final String METHOD_NAME = "getOlapCubeGson";
  
  @Data
  public static final class CubeRequestGson {
    
    public String user_id;
    public String cube_id;
  }
  
  public static final class OlapCubeGsonByte {
    
    public byte[] value;
  }
  
  public static final MethodDescriptor<CubeRequestGson, OlapCubeGsonByte> SHARE_SERVICE =
      MethodDescriptor.newBuilder(
          marshallerFor( CubeRequestGson.class ),
          marshallerFor( OlapCubeGsonByte.class ) )
                      .setFullMethodName( MethodDescriptor.generateFullMethodName( SERVICE_NAME, METHOD_NAME ) )
                      .setType( MethodType.UNARY )
                      .setSampledToLocalTracing( true )
                      .build();
  
  public static abstract class CubeShareServiceGsonImplBase implements BindableService {
    
    public abstract void getOlapCubeGson ( CubeRequestGson request, StreamObserver<OlapCubeGsonByte> responseObserver );
    
    @Override
    public final ServerServiceDefinition bindService () {
      ServerServiceDefinition.Builder ssd = ServerServiceDefinition.builder( SERVICE_NAME );
      ssd.addMethod( SHARE_SERVICE, ServerCalls.asyncUnaryCall( this::getOlapCubeGson ) );
      return ssd.build();
    }
  }
  
  public static final Gson gson =
      new GsonBuilder().registerTypeAdapter( byte[].class, new TypeAdapter<byte[]>() {
        @Override
        public void write ( JsonWriter out, byte[] value ) throws IOException {
          out.value( Base64.getEncoder().encodeToString( value ) );
        }
        
        @Override
        public byte[] read ( JsonReader in ) throws IOException {
          return Base64.getDecoder().decode( in.nextString() );
        }
      } ).create();
  
  public static <T> Marshaller<T> marshallerFor ( Class<T> clz ) {
    return new Marshaller<>() {
      @Override
      public InputStream stream ( T value ) {
        return new ByteArrayInputStream( gson.toJson( value, clz ).getBytes( StandardCharsets.UTF_8 ) );
      }
      
      @Override
      public T parse ( InputStream stream ) {
        return gson.fromJson( new InputStreamReader( stream, StandardCharsets.UTF_8 ), clz );
      }
    };
  }
  
  @Service
  public static class GsonBytesCubeServiceImpl extends CubeShareServiceGsonImplBase {
    
    private CubeRepository repository;
    private MongoOperations mongoOperations;
    private final Marshaller<Cube> cubeMarshaller;
    
    @Autowired
    public GsonBytesCubeServiceImpl ( CubeRepository repository ) {
      this.repository = repository;
      cubeMarshaller = GsonBytesCubeService.marshallerFor( Cube.class );
    }
    
    private Cube findCubeFromSomewhere ( CubeRequestGson request ) {
      Cube eg = new Cube();
      eg.setId( request.getCube_id() );
      Example<Cube> example = Example.of( eg );
      return repository.findOne( example ).orElse( new Cube( "-1", "-1" ) );
    }
    
    @Override
    public void getOlapCubeGson ( CubeRequestGson request, StreamObserver<OlapCubeGsonByte> responseObserver ) {
      
      try {
        InputStream stream = cubeMarshaller.stream( findCubeFromSomewhere( request ) );
        byte[] data = stream.readAllBytes();
        
        OlapCubeGsonByte response = new OlapCubeGsonByte();
        response.value = data;
        
        responseObserver.onNext( response );
        responseObserver.onCompleted();
      }
      catch ( IOException e ) {
        e.printStackTrace();
      }
    }
  }
}
