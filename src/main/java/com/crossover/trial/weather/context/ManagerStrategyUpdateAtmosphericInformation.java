package com.crossover.trial.weather.context;

import com.crossover.trial.airport.context.AtmosphericInformation;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.weather.context.exceptions.WeatherException;

/**
 * @author Cesar Chavez
 *
 */
public class ManagerStrategyUpdateAtmosphericInformation implements StrategyUpdateAtmosphericInformation {

	private StrategyUpdateAtmosphericInformation strategy;
	
	private boolean updated = false;

	public void setStrategy(StrategyUpdateAtmosphericInformation strategy){
		this.strategy = strategy;
	}

	@Override
	public void update(AtmosphericInformation ai, String pointType, DataPoint dp) throws WeatherException {
		this.strategy.update(ai, pointType, dp);
		this.setUpdated(this.strategy.isUpdated());
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

}
