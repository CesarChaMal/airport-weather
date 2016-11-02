package com.crossover.trial.util;

/**
 * The various types of data points we can collect.
 *
 * @author Cesar Chavez
 *
 */
public enum emDataPointType {
	
    WIND ("wind"),
    TEMPERATURE ("temperature"),
    HUMIDITY ("humidity"),
    PRESSURE ("pressure"),
    CLOUDCOVER ("cloudcover"),
    PRECIPITATION ("precipitation");
	
	emDataPointType(String dataPointType) {
		this.dataPointType = dataPointType;
	}
	
    private final String dataPointType;
    
    /**
     * return the method string value for the entity type.
     * @return type of data point
     */
    public String getDataPointType()   { return dataPointType; }
}
