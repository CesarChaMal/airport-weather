package com.crossover.trial.weather.context;

import com.crossover.trial.airport.context.AtmosphericInformation;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.util.emDataPointType;
import com.crossover.trial.weather.context.exceptions.WeatherException;

/**
 * @author Cesar Chavez
 *
 */
public class StrategyUpdatePressure implements StrategyUpdateAtmosphericInformation {

	private boolean updated = false;

	@Override
	public void update(AtmosphericInformation ai, String pointType, DataPoint dp) throws WeatherException {
       	if (pointType.equalsIgnoreCase(emDataPointType.PRESSURE.getDataPointType())) {
            if (dp.getMean() >= 650 && dp.getMean() < 800) {
            	updated = true;
            	ai.update(emDataPointType.PRESSURE, dp);
                return;
            }
        }
	}
	
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
