package org.sid.dao;

import org.sid.entities.Category;
import org.sid.entities.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category,String> {
    
}
