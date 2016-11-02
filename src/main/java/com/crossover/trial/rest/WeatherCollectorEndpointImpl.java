package com.crossover.trial.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.airport.context.AirportData;
import com.crossover.trial.airport.context.AirportDataContext;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.airport.context.services.AirportDataService;
import com.crossover.trial.rest.helpers.WeatherCollectorEndpoint;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.weather.context.exceptions.WeatherException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class WeatherCollectorEndpointImpl implements WeatherCollectorEndpoint {

	private static AirportDataService airportDataService;

	/** shared gson json to object factory */
	private final static Gson gson = new Gson();
	
//    private final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	{
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
		// temporary for testing
        init();
    }

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("1").build();
    }

    @POST
    @Path("/weather/{iata}/{pointType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWeather(@PathParam("iata") String iataCode,
    		@PathParam("pointType") String pointType,
    		String datapointJson) {
    	System.out.println("datapointJson" + datapointJson);
        try {
        	AirportDataContext adContext = airportDataService.getAirportDataContext(iataCode);
            if (adContext != null) {
            	adContext.addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
                return Response.status(Response.Status.OK).build();
            } else
            	throw new NoSuchElementException ("Non existing airport");
        } catch (JsonSyntaxException | WeatherException | NoSuchElementException e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
            return Response.status(422).build();
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Override
    public Response getAirports() {
    	Set<String> retval = new HashSet<>();

    	Map<String, AirportDataContext> map = airportDataService.getAirportData();

		for (Map.Entry<String, AirportDataContext> context : map.entrySet())
		{
            retval.add(context.getKey());
		}		
        return Response.status(Response.Status.OK).entity(retval).build();
    }

    @GET
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirport(@PathParam("iata") String iata) {
		AirportData ad = airportDataService.findAirportData(iata);
        if (ad != null){
            return Response.status(Response.Status.OK).entity(ad).build();
        } else {
        	return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/airport/{iata}/{lat}/{long}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {
        AirportData ad = airportDataService.findAirportData(iata);
        if (ad == null){
    		AirportData airport = new AirportData.Builder()
					.withIata(iata)
					.withLatitude(Double.valueOf(latString))
					.withLongitude(Double.valueOf(longString))
				.build();

        	airportDataService.addAirport(airport);
        	return Response.status(Response.Status.CREATED).build();
        } else {
        	return Response.status(Response.Status.FOUND).build();
        }
    }


    @POST
    @Path("/airport")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAirport(String airportDataJson) {
    	AirportData ad = gson.fromJson(airportDataJson, AirportData.class); 

    	if (airportDataService.findAirportData(ad.getIata()) == null){
    		airportDataService.addAirport(ad);
    		return Response.status(Response.Status.CREATED).build();
    	} else {
    		return Response.status(Response.Status.FOUND).build();
    	}
    }
    
    
    @DELETE
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAirport(@PathParam("iata") String iata) {
        AirportData ad = airportDataService.findAirportData(iata);
        if (ad != null){
        	airportDataService.delete(iata);
        	return Response.status(Response.Status.OK).build();
        } else {
        	return Response.status(Response.Status.NOT_FOUND).build();
        }
//        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
    //
    // Internal support methods
    //
    
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
