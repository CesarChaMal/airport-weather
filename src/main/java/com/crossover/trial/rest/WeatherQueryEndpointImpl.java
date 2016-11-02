package com.crossover.trial.rest;

import com.crossover.trial.airport.context.AirportData;
import com.crossover.trial.airport.context.AirportDataContext;
import com.crossover.trial.airport.context.AirportLoader;
import com.crossover.trial.airport.context.AtmosphericInformation;
import com.crossover.trial.airport.context.services.AirportDataService;
import com.crossover.trial.rest.helpers.WeatherQueryEndpoint;
import com.crossover.trial.util.AirportDataUtilities;
import com.crossover.trial.util.CloseableContext;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.util.emDataPointType;
import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class WeatherQueryEndpointImpl implements WeatherQueryEndpoint {

	private static AirportDataService airportDataService;

	/** shared gson json to object factory */
	private final static Gson gson = new Gson();
	
//    private final static Logger logger = Logger.getLogger(RestWeatherQueryEndpoint.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	{
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
        init();
    }

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
		LOGGER.info("web service ping");
//		System.out.println("web service ping");
		
        Map<String, Object> retval = new HashMap<>();
    	Map<String, AirportDataContext> map = airportDataService.getAirportData();
    	int datasize = 0;
        Map<String, Double> freq = new HashMap<>();

		for (Map.Entry<String, AirportDataContext> context : map.entrySet())
		{
//		    System.out.println(context.getKey() + "/" + context.getValue());
//		    LOGGER.info(context.getKey() + "/" + context.getValue());
            
		    AtmosphericInformation atmosphericInformation = context.getValue().getAtmosphericInformation();
            
            if (atmosphericInformation.get(emDataPointType.CLOUDCOVER) != null || atmosphericInformation.get(emDataPointType.HUMIDITY) != null
                    || atmosphericInformation.get(emDataPointType.PRECIPITATION) != null || atmosphericInformation.get(emDataPointType.PRESSURE) != null
                    || atmosphericInformation.get(emDataPointType.TEMPERATURE) != null || atmosphericInformation.get(emDataPointType.WIND) != null) {
                // updated in the last day
                if (atmosphericInformation.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                    datasize++;
                }
            }
            
            // fraction of queries
            AirportData airportData = context.getValue().getAirportData();            
            double frac = (double) AirportDataService.requestFrequency.getOrDefault(airportData, 0) / AirportDataService.requestFrequency.size();
//          frac = Double.isNaN(frac) ? 0 : frac;
			if (Double.isNaN(frac)) {
				frac = 0.0;
			}
			freq.put(airportData.getIata(), frac);
		}		
        retval.put("datasize", datasize);
        retval.put("iata_freq", freq);

        int m = AirportDataService.radiusFreq.keySet().stream()
                .max(Double::compare)
                .orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : AirportDataService.radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        retval.put("radius_freq", hist);

        return gson.toJson(retval);
    }

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information and
     * return a list of matching atmosphere information.
     *
     * @param iata the iataCode
     * @param radiusString the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        airportDataService.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        AirportDataContext context = airportDataService.getAirportDataContext(iata);
        Map<String, AirportDataContext> map = airportDataService.getAirportData();
        AirportData ad = context.getAirportData();
        
        if (radius == 0) {
            retval.add(context.getAtmosphericInformation());
        } else {

        	for (Map.Entry<String, AirportDataContext> entry : map.entrySet())
    		{
//    		    System.out.println(entry.getKey() + "/" + entry.getValue());

        		AirportData airportData = entry.getValue().getAirportData();            
    		    AtmosphericInformation ai = entry.getValue().getAtmosphericInformation();

                if (AirportDataUtilities.calculateDistance(ad, airportData) <= radius){
                    if (ai.get(emDataPointType.CLOUDCOVER) != null || ai.get(emDataPointType.HUMIDITY) != null
                            || ai.get(emDataPointType.PRECIPITATION) != null || ai.get(emDataPointType.PRESSURE) != null
                            || ai.get(emDataPointType.TEMPERATURE) != null || ai.get(emDataPointType.WIND) != null) {
                        retval.add(ai);
                    }
                }
    		    
    		}		
        	
        }
        return Response.status(Response.Status.OK).entity(retval).build();
    }

    /**
     * A dummy init method that loads hard coded data
     */
    protected static void init() {
//    	System.out.println("Init");
    	LOGGER.info("Init");
    	
        airportDataService = AirportDataService.getInstance();
        airportDataService.uploadAirportLoader();
    }
}
