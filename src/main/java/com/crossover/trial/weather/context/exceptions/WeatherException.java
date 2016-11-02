package com.crossover.trial.weather.context.exceptions;

import com.crossover.trial.util.emDataPointType;

/**
 * 
 * @author Cesar Chavez
 *
 */
public class WeatherException extends Exception {
    private static final long serialVersionUID = -687991492884005033L;

    private final emDataPointType dataPointType;

    public WeatherException(emDataPointType dataPointType, String message) {
        super(message);
        this.dataPointType = dataPointType;
    }

    public WeatherException(emDataPointType dataPointType, String message, Throwable cause) {
        super(message, cause);
        this.dataPointType = dataPointType;
    }

    public emDataPointType getDataPointType() {
        return dataPointType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " for data point type :" + dataPointType.name();
    }

}