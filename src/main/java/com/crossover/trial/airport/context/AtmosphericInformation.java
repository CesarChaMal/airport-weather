package com.crossover.trial.airport.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.crossover.trial.util.emDataPointType;
import com.crossover.trial.weather.context.exceptions.WeatherException;


/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

    final Map<emDataPointType, DataPoint> data = new ConcurrentHashMap<emDataPointType, DataPoint>();

    /** the last time this data was updated, in milliseconds since UTC epoch */
    private long lastUpdateTime;

    public AtmosphericInformation() {
        lastUpdateTime = System.currentTimeMillis();
    }

    public DataPoint update(emDataPointType key, DataPoint value) throws WeatherException {
        lastUpdateTime = System.currentTimeMillis();
        return data.put(key, value);
    }

    public DataPoint get(emDataPointType key) {
        return data.get(key);
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    
}
