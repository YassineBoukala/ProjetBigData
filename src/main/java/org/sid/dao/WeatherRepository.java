package org.sid.dao;


import java.util.List;

import org.sid.entities.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
@Repository
public interface WeatherRepository extends MongoRepository<Weather,String> {
	List<Weather> findBylocationContains(String location);
	List<Weather> findBytemperatureContains(double temperature);
	List<Weather> findBytemperature(double temperature);
	List<Weather> findByconditions(String condition);
}
