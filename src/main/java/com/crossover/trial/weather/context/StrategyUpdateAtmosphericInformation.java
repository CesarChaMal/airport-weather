package com.crossover.trial.weather.context;

import com.crossover.trial.airport.context.AtmosphericInformation;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.weather.context.exceptions.WeatherException;

/**
 * @author Cesar Chavez
 *
 */
public interface StrategyUpdateAtmosphericInformation {
	public void update(AtmosphericInformation ai, String pointType, DataPoint dp) throws WeatherException;
	public boolean isUpdated();
	public void setUpdated(boolean updated);
}
