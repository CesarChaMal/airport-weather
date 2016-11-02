package com.crossover.trial.airport.context;

import com.crossover.trial.util.emDataPointType;
import com.crossover.trial.weather.context.ManagerStrategyUpdateAtmosphericInformation;
import com.crossover.trial.weather.context.StrategyUpdateCloudcover;
import com.crossover.trial.weather.context.StrategyUpdateHumidity;
import com.crossover.trial.weather.context.StrategyUpdatePrecipitation;
import com.crossover.trial.weather.context.StrategyUpdatePressure;
import com.crossover.trial.weather.context.StrategyUpdateTemperature;
import com.crossover.trial.weather.context.StrategyUpdateWind;
import com.crossover.trial.weather.context.exceptions.WeatherException;

/**
 * @author Cesar Chavez
 *
 */
public class AirportDataContext {
	
	private AirportData airportData;

	private AtmosphericInformation atmosphericInformation;
	
    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link emDataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    public synchronized void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
//        AtmosphericInformation ai = atmosphericInformation.get(airportDataIdx);
        updateAtmosphericInformation(getAtmosphericInformation(), pointType, dp);
    }

    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param ai the atmospheric information object to update
     * @param pointType the data point type as a string
     * @param dp the actual data point
     * @throws WeatherException Exception class to handle weather AtmosphericInformation
     */
    public synchronized void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) throws WeatherException {
        System.out.println("updateAtmosphericInformation");
    	final emDataPointType dptype = emDataPointType.valueOf(pointType.toUpperCase());

       	ManagerStrategyUpdateAtmosphericInformation manager = new ManagerStrategyUpdateAtmosphericInformation();
       	manager.setStrategy(new StrategyUpdateWind());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
//	 	  if (pointType.equalsIgnoreCase(DataPointType.WIND.getDataPointType())) {
//            if (dp.getMean() >= 0) {
//                ai.setWind(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }
		
       	manager.setStrategy(new StrategyUpdateTemperature());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.name())) {
//        if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.getDataPointType())) {
//            if (dp.getMean() >= -50 && dp.getMean() < 100) {
//                ai.setTemperature(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }

       	manager.setStrategy(new StrategyUpdateHumidity());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.HUMIDITY.name())) {
//        if (pointType.equalsIgnoreCase(DataPointType.HUMIDITY.getDataPointType())) {
//            if (dp.getMean() >= 0 && dp.getMean() < 100) {
//                ai.setHumidity(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }

       	manager.setStrategy(new StrategyUpdatePressure());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.PRESSURE.name())) {
//        if (pointType.equalsIgnoreCase(DataPointType.PRESSURE.getDataPointType())) {
//            if (dp.getMean() >= 650 && dp.getMean() < 800) {
//                ai.setPressure(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }

       	manager.setStrategy(new StrategyUpdateCloudcover());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.CLOUDCOVER.name())) {
//        if (pointType.equalsIgnoreCase(DataPointType.CLOUDCOVER.getDataPointType())) {
//            if (dp.getMean() >= 0 && dp.getMean() < 100) {
//                ai.setCloudCover(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }

       	manager.setStrategy(new StrategyUpdatePrecipitation());
       	manager.update(ai, pointType, dp);
       	if (manager.isUpdated())
       		return;
//        if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.name())) {
//        if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.getDataPointType())) {
//            if (dp.getMean() >=0 && dp.getMean() < 100) {
//                ai.setPrecipitation(dp);
//                ai.setLastUpdateTime(System.currentTimeMillis());
//                return;
//            }
//        }

//       	throw new IllegalStateException("couldn't update atmospheric data");
       	throw new WeatherException(dptype,"couldn't update atmospheric data");
    }

	public synchronized AirportData getAirportData() {
		return airportData;
	}

	public synchronized void setAirportData(AirportData airportData) {
		this.airportData = airportData;
	}

	public synchronized AtmosphericInformation getAtmosphericInformation() {
		return atmosphericInformation;
	}

	public synchronized void setAtmosphericInformation(AtmosphericInformation atmosphericInformation) {
		this.atmosphericInformation = atmosphericInformation;
	}

    
}
