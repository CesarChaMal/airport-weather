package com.crossover.trial.airport.context.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import com.crossover.trial.airport.context.AirportData;
import com.crossover.trial.airport.context.AirportDataContext;
import com.crossover.trial.airport.context.AirportLoader;
import com.crossover.trial.airport.context.AtmosphericInformation;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.util.CloseableContext;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.weather.context.exceptions.WeatherException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author Cesar Chavez
 *
 */
final public class AirportDataService {

	private volatile static AirportDataService INSTANCE;

	// private List<AirportDataContext> airportData =
	// Collections.synchronizedList(new ArrayList<>());
	private static Map<String, AirportDataContext> airportDataContext = new ConcurrentHashMap<>();

	private AirportDataService() {
	}

	public static Map<String, AirportDataContext> getAirportData() {
		return airportDataContext;
	}

	public static void setAirportData(Map<String, AirportDataContext> airportData) {
		AirportDataService.airportDataContext = airportData;
	}

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/** shared gson json to object factory */
	private final static Gson gson = new Gson();

//	/** all known airports */
//    protected static List<AirportData> airportData = new ArrayList<>();
//
//    /** atmospheric information for each airport, idx corresponds with airportData */
//    protected static List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

	public static AirportDataService getInstance() {
		// Check 1 of the double-checked lock
		if (INSTANCE == null) {
			synchronized (AirportDataService.class) {
				// Check 2 of the double-checked lock
				if (INSTANCE == null) {
					INSTANCE = new AirportDataService();
					init();
				}
			}
		}
		return INSTANCE;
	}

	private static void init() {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
	}

	/**
	 * Internal performance counter to better understand most requested
	 * information, this map can be improved but for now provides the basis for
	 * future performance optimizations. Due to the stateless deployment
	 * architecture we don't want to write this to disk, but will pull it off
	 * using a REST request and aggregate with other performance metrics
	 */
	public static Map<AirportData, Integer> requestFrequency = new ConcurrentHashMap<AirportData, Integer>();

	public static Map<Double, Integer> radiusFreq = new ConcurrentHashMap<Double, Integer>();
	
	private volatile AirportLoader airportLoader = new AirportLoader();

	public synchronized AirportLoader getAirportLoader() {
		return airportLoader;
	}

	public synchronized void setAirportLoader(AirportLoader airportLoader) {
		this.airportLoader = airportLoader;
	}

	/**
	 * Delete a known airport to our list.
	 *
	 * @param iata
	 *            3 letter code
	 */
	public synchronized void delete(String iata) {
		airportDataContext.remove(iata);
	}

	public synchronized AirportDataContext getAirportDataContext(String iata) {
		return airportDataContext.get(iata);
	}

	/**
	 * Add a new known airport to our list.
	 *
	 * @param airport AirportData type
	 *
	 * @return the added airport
	 */
	public synchronized AirportData addAirport(AirportData airport) {
//		AirportData ad = new AirportData.Builder()
//					.withName(airport.getName())
//					.withCity(airport.getCity())
//					.withCountry(airport.getCountry())
//					.withIata(airport.getIata())
//					.withIcao(airport.getIcao())
//					.withLatitude(airport.getLatitude())
//					.withLongitude(airport.getLongitude())
//					.withAltitude(airport.getAltitude())
//					.withTimezone(airport.getTimezone())
//					.withDST(airport.getDST())
//				.build();
//
		AtmosphericInformation atmosphericInformation = new AtmosphericInformation();

		AirportDataContext dataContext = new AirportDataContext();
		dataContext.setAirportData(airport);
		dataContext.setAtmosphericInformation(atmosphericInformation);
		
		airportDataContext.put(airport.getIata(), dataContext);
		
		return airport;
	}

	/**
	 * Records information about how often requests are made
	 *
	 * @param iata
	 *            an iata code
	 * @param radius
	 *            query radius
	 */
	public synchronized void updateRequestFrequency(String iata, Double radius) {
		AirportData data = findAirportData(iata);
		if (data != null){
			requestFrequency.put(data, requestFrequency.getOrDefault(data, 0) + 1);
			radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0) + 1);
		} else {
			throw new NoSuchElementException("Non Existing airport");
		}
	}

	/**
	 * Given an iataCode find the airport data
	 *
	 * @param iata
	 *            as a string
	 * @return airport data or null if not found
	 */
	public synchronized AirportData findAirportData(String iata) {
		AirportDataContext dataContext = (AirportDataContext) getAirportDataContext(iata);
		return dataContext != null ? dataContext.getAirportData() : null;
//		return dataContext.stream().filter(ap -> ap.getIata().equals(iata)).findFirst().orElse(null);
	}
	
    public synchronized void uploadAirportLoader() {
//    	System.out.println("uploadAirportLoader");
    	LOGGER.info("uploadAirportLoader");
    	
//    	airportDataContext.clear();
//        requestFrequency.clear();
    	
//        airportData.clear();
//        atmosphericInformation.clear();

		File airportDataFile = new File(CommonConstants.FILE);
    	  
//		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
//			try
//			{
//		        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		        URL u = classLoader.getResource(CommonConstants.FILENAME);
//		        airportDataFile = new java.io.File(u.toURI());
//		        
//		        if (!airportDataFile.exists() || airportDataFile.length() == 0){
//					System.err.println(airportDataFile + " is not a valid input");
//					System.exit(1);
//				}
//			} 
//			catch (URISyntaxException e) 
//			{							
//				LOGGER.severe(e.getMessage());
//				e.printStackTrace();
//			}
//		}

		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
			try (CloseableContext classLoader = CloseableContext.contextClassLoader(Thread.currentThread().getContextClassLoader()))
			{
		        URL u = classLoader.getResource(CommonConstants.FILENAME);
		        try {
					airportDataFile = new java.io.File(u.toURI());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
		        
		        if (!airportDataFile.exists() || airportDataFile.length() == 0){
					System.err.println(airportDataFile + " is not a valid input");
					System.exit(1);
				}
			} 
		}

		try {
			if (!airportLoader.isUploaded()){
//				airportLoader.upload(airportDataFile);
				airportLoader.upload(this, airportDataFile);
//				airportData = airportLoader.getAirportData();
//				atmosphericInformation = airportLoader.getAtmosphericInformation();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
}
