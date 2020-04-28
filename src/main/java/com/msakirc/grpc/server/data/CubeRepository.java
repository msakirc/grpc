package com.msakirc.grpc.server.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CubeRepository extends MongoRepository<Cube, String> {

}
