package org.sid.dao;

import org.sid.entities.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository  extends MongoRepository<Weather,String> {

}
