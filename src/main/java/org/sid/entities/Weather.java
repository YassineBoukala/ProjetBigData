package org.sid.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Document(collection = "weathers3")
@Data @AllArgsConstructor  @NoArgsConstructor @ToString
public class Weather {
	@Id
	private String id;
	@Field("Location")
	private String location;
	/*
	 * @Field("Date time") private Date dateTime;
	 * 
	 * 
	 * 
	 * @Field("Minimum Temperature") private int minimum;
	 */
	@Field("Maximum Temperature") private double maximum;
	@Field("Temperature")	
	private double temperature;
	/*
	 * @Field("Wind Speed") private double windSpeed;
	 * 
	 * @Field("Cloud Cover") private double cloudCover;
	 * 
	 * @Field("Relative Humidity") private double relativeHumidity;
	 */
	
	 @Field("Conditions") 
	 private String conditions;
		/*
		 * @Field("Address") private String Address;
		 * 
		 * @Field("Wind CHill") private String WindChill;
		 * 
		 * @Field("Heat Index") private String HeatIndex;
		 * 
		 * @Field("Precipitation") private int Precipitation;
		 * 
		 * @Field("Snow Depth") private String SnowDepth;
		 * 
		 * @Field("Wind Gust") private String WindGust;
		 */
	 

	
}
