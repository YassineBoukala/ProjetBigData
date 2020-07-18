package org.sid.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.sid.dao.WeatherRepository;
import org.sid.entities.Weather;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.connection.Stream;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@CrossOrigin("*")
@Controller

public class WeatherController {
	   @Autowired
	  private MongoTemplate mongoTemplate;
@Autowired
WeatherRepository weatherRepository;
@GetMapping(path="/WeathersApi")
public String intefaceapi(){
	return "/weatherapi";
}
//recherche par location(Ville)
@GetMapping(path="/Weathersp")
public ResponseEntity<List<Weather>> getWeathers(Model model,@RequestParam(required = false) String location){
	try {
		
		List<Weather> weathers = new ArrayList<Weather>();
	    if (location == null)
	      weatherRepository.findAll().forEach(weathers::add);
	    else {
	     weatherRepository.findBylocationContains(location).forEach(weathers::add);
	   
	    }if (weathers.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<>(weathers, HttpStatus.OK);
	  } catch (Exception e) {
	    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	
}




 

 
//recherche par temperature
  @GetMapping(path="/weathers") 
  public ResponseEntity<List<Weather>> getTemperature(Model model,@RequestParam(
  name="temperature",defaultValue = "0.0") double temperature){ 
	  try {
  List<Weather> weathers = new ArrayList<Weather>();
   if (temperature == 0) {
	   weatherRepository.findAll().forEach(weathers::add);
	  } else {
		  model.addAttribute("temperature", temperature);	
		weathers=weatherRepository.findBytemperature(temperature);
		   
   }
  if (weathers.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  return new ResponseEntity<>(weathers, HttpStatus.OK); } catch (Exception e) {
  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); } }
  
  
  
  
  
  //rechercher par conditions
  @GetMapping(path="/weatherCo") 
  public ResponseEntity<List<Weather>> getConditions(Model model,@RequestParam(
  name="condition",defaultValue = "") String condition){ 
	  try {
  List<Weather> weathers = new ArrayList<Weather>();
   if (condition.equals("")) {
	   weatherRepository.findAll().forEach(weathers::add);
	  } else {
		  model.addAttribute("condition",condition);	
		   weatherRepository.findByconditions(condition).forEach(weathers::add);
		   
   }
  if (weathers.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  
  return new ResponseEntity<>(weathers, HttpStatus.OK); } catch (Exception e) {
  return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); } }
  
/*
 * @PostMapping("/upload") // //new annotation since 4.3 public String
 * singleFileUpload(Model model,@RequestParam(name="file") MultipartFile file,
 * RedirectAttributes redirectAttributes,@RequestParam(name = "db") String db) {
 * 
 * if (file.isEmpty()) { redirectAttributes.addFlashAttribute("message",
 * "Please select a file to upload"); return "Error"; }
 * 
 * try { String str="C:\\Program Files\\MongoDB\\Server4.2\\bin"; StringBuilder
 * filename=new StringBuilder(); Path
 * filen=Paths.get(str,file.getOriginalFilename());
 * filename.append(file.getOriginalFilename()); Files.write(filen,
 * file.getBytes());
 * 
 * model.addAttribute("db", db); final Log log =
 * LogFactory.getLog(Weather.class); model.addAttribute("file", file); // Get
 * the file and save it somewhere byte[] bytes = file.getBytes();
 * ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
 * BufferedReader br = new BufferedReader(new InputStreamReader(inputFilestream
 * )); String line = ""; MongoOperations mongoOps = new
 * MongoTemplate(MongoClients.create(), db); List<Weather> weathers=new
 * ArrayList<Weather>(); Weather p=new Weather();
 * 
 * while ((line = br.readLine()) != null) { // Insert is used to initially store
 * the object into the database. System.out.println(line); }
 * mongoOps.save(file.getBytes()); br.close();
 * 
 * 
 * 
 * } catch (IOException e) { e.printStackTrace(); } return "weatherapi"; }
 */
  
  //upload file 
  @PostMapping("/upload-csv-file")
  public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

      // validate file
      if (file.isEmpty()) {
    	  System.out.println("empty file");
    	 
      } else {

          // parse CSV file to create a list of `User` objects
          try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

              // create csv bean reader
              CsvToBean<Weather> csvToBean = new CsvToBeanBuilder<Weather>(reader)
                      .withType(Weather.class)
                      .withIgnoreLeadingWhiteSpace(true)
                      .build();

              // convert `CsvToBean` object to list of users
              List<Weather> weather = csvToBean.parse();

              // TODO: save users in DB?
              weatherRepository.deleteAll();
                  weatherRepository.saveAll(weather);
                  model.addAttribute("weather",weather);
                  model.addAttribute("status", true);
          } catch (Exception ex) {
        	  System.out.println("dadasas");
          }
      }
      return "weatherapi";
  }
  
  
  
  
  
 
 


}
