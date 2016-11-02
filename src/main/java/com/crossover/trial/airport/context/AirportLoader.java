package com.crossover.trial.airport.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.crossover.trial.airport.context.services.AirportDataService;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.util.emDaylightSavingTime;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /** end point for read queries */
    private WebTarget query;

    /** end point to supply updates */
    private WebTarget collect;

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String BASE_URI = CommonConstants.BASE_URI;

    private Client client;
    
    private volatile boolean uploaded;

    /** all known airports */
    public volatile List<AirportData> airportData = new ArrayList<>();

    /** atmospheric information for each airport, idx corresponds with airportData */
    public volatile List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

	public AirportLoader() {
        this.client = ClientBuilder.newClient();
    	this.query = this.client.target(this.BASE_URI + CommonConstants.QUERY_ENDPOINT);
    	this.collect = this.client.target(this.BASE_URI + CommonConstants.COLLECT_ENDPOINT);
    }

    public synchronized boolean isUploaded() {
		return uploaded;
	}

	public synchronized void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	/**
	 * @param base_uri
	 */
	public AirportLoader(String base_uri) {
    	this.BASE_URI = base_uri;
    	this.client = ClientBuilder.newClient();
    	this.query = this.client.target(base_uri + CommonConstants.QUERY_ENDPOINT);
    	this.collect = this.client.target(base_uri + CommonConstants.COLLECT_ENDPOINT);
    }
    
    {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
    }

    public void pingCollect() {
        WebTarget path = this.collect.path("/airports");
        Response response = path.request().get();
//        System.out.print("collect.ping: " + response.readEntity(String.class) + "\n");
		LOGGER.info("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    /**
     * @param airportDataService AirportDataService class
     * @param file file to upload
     * @throws IOException IO exception for files
     */
    public synchronized void upload(AirportDataService airportDataService, File file) throws IOException{
    	
//    	  Before Java 7 Using Decorators and Scanner
/*
    	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
        	sb.append(line);
            sb.append(System.lineSeparator());
			airportDataService.addAirport(
					new AirportData.Builder()
					.withName(line.split(",")[1].contains("\"") ? line.split(",")[1].split("\"")[1] : line.split(",")[1])
					.withCity(line.split(",")[2].contains("\"") ? line.split(",")[2].split("\"")[1] : line.split(",")[2])
					.withCountry(line.split(",")[3].contains("\"") ? line.split(",")[3].split("\"")[1] : line.split(",")[3])
					.withIata(line.split(",")[4].contains("\"") ? line.split(",")[4].split("\"")[1] : line.split(",")[4])
					.withIcao(line.split(",")[5].contains("\"") ? line.split(",")[5].split("\"")[1] : line.split(",")[5])
					.withLatitude(!(line.split(",")[6].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[6]))
					.withLongitude(!(line.split(",")[7].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[7]))
					.withAltitude(!(line.split(",")[8].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[8]))
					.withTimezone(!(line.split(",")[9].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[9]))
					.withDST(line.split(",")[10].contains("\"") ? emDaylightSavingTime.valueOf(line.split(",")[10].split("\"")[1]) : emDaylightSavingTime.valueOf(line.split(",")[10]))
					.build()
			);
        }
        String everything = sb.toString();
        LOGGER.info(everything);
*/
    	
//  	Java 7 : Try with Resources
/*    	
     	try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())))) {
    		String line = null;
    		StringBuilder sb = new StringBuilder();

    		while ((line = reader.readLine()) != null) {
    			sb.append(line);
    			sb.append(System.lineSeparator());
				airportDataService.addAirport(
						new AirportData.Builder()
						.withName(line.split(",")[1].contains("\"") ? line.split(",")[1].split("\"")[1] : line.split(",")[1])
						.withCity(line.split(",")[2].contains("\"") ? line.split(",")[2].split("\"")[1] : line.split(",")[2])
						.withCountry(line.split(",")[3].contains("\"") ? line.split(",")[3].split("\"")[1] : line.split(",")[3])
						.withIata(line.split(",")[4].contains("\"") ? line.split(",")[4].split("\"")[1] : line.split(",")[4])
						.withIcao(line.split(",")[5].contains("\"") ? line.split(",")[5].split("\"")[1] : line.split(",")[5])
						.withLatitude(!(line.split(",")[6].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[6]))
						.withLongitude(!(line.split(",")[7].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[7]))
						.withAltitude(!(line.split(",")[8].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[8]))
						.withTimezone(!(line.split(",")[9].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[9]))
						.withDST(line.split(",")[10].contains("\"") ? emDaylightSavingTime.valueOf(line.split(",")[10].split("\"")[1]) : emDaylightSavingTime.valueOf(line.split(",")[10]))
						.build()
				);
	        }
    		String everything = sb.toString();
            LOGGER.info(everything);
    	}
*/
    	
//  	Java 8 : Try with Resources and Lambda expression and Streams
/*    	
    	try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())))) {
			Stream<String> stream = reader.lines();
//			stream.filter(line -> line.equals(line)).forEach(System.out::println);
//			stream.filter(line -> line.equals(line)).forEach(LOGGER::info);
//	    	stream.forEach(LOGGER::info);
			stream.forEach(line -> 
				airportDataService.addAirport(
						new AirportData.Builder()
						.withName(line.split(",")[1].contains("\"") ? line.split(",")[1].split("\"")[1] : line.split(",")[1])
						.withCity(line.split(",")[2].contains("\"") ? line.split(",")[2].split("\"")[1] : line.split(",")[2])
						.withCountry(line.split(",")[3].contains("\"") ? line.split(",")[3].split("\"")[1] : line.split(",")[3])
						.withIata(line.split(",")[4].contains("\"") ? line.split(",")[4].split("\"")[1] : line.split(",")[4])
						.withIcao(line.split(",")[5].contains("\"") ? line.split(",")[5].split("\"")[1] : line.split(",")[5])
						.withLatitude(!(line.split(",")[6].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[6]))
						.withLongitude(!(line.split(",")[7].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[7]))
						.withAltitude(!(line.split(",")[8].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[8]))
						.withTimezone(!(line.split(",")[9].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[9]))
						.withDST(line.split(",")[10].contains("\"") ? emDaylightSavingTime.valueOf(line.split(",")[10].split("\"")[1]) : emDaylightSavingTime.valueOf(line.split(",")[10]))
						.build()
				)
			);
    	}
*/    
    	
//  	Java 8 : Try with Resources, Lambda expression and Streams and Files API
    	try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
//			stream.forEach(System.out::println);
//			stream.forEach(LOGGER::info);
			
    		stream
//			.filter(line -> line.contains("\""))
			.forEach(line -> 
				airportDataService.addAirport(
						new AirportData.Builder()
						.withName(line.split(",")[1].contains("\"") ? line.split(",")[1].split("\"")[1] : line.split(",")[1])
						.withCity(line.split(",")[2].contains("\"") ? line.split(",")[2].split("\"")[1] : line.split(",")[2])
						.withCountry(line.split(",")[3].contains("\"") ? line.split(",")[3].split("\"")[1] : line.split(",")[3])
						.withIata(line.split(",")[4].contains("\"") ? line.split(",")[4].split("\"")[1] : line.split(",")[4])
						.withIcao(line.split(",")[5].contains("\"") ? line.split(",")[5].split("\"")[1] : line.split(",")[5])
						.withLatitude(!(line.split(",")[6].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[6]))
						.withLongitude(!(line.split(",")[7].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[7]))
						.withAltitude(!(line.split(",")[8].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[8]))
						.withTimezone(!(line.split(",")[9].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[9]))
						.withDST(line.split(",")[10].contains("\"") ? emDaylightSavingTime.valueOf(line.split(",")[10].split("\"")[1]) : emDaylightSavingTime.valueOf(line.split(",")[10]))
						.build()
				)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    	airportDataService.getAirportData().values().forEach(System.out::println);
    	airportDataService.getAirportData().values().forEach(airport -> System.out.println(airport.getAirportData()));
    	setUploaded(true);
    }
    
    public synchronized void upload(File file) throws IOException{
    	try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()))) {
    		stream
    		.forEach(line -> addAirport(
			    				new AirportData.Builder()
				    				.withName(line.split(",")[1].contains("\"") ? line.split(",")[1].split("\"")[1] : line.split(",")[1])
				    				.withCity(line.split(",")[2].contains("\"") ? line.split(",")[2].split("\"")[1] : line.split(",")[2])
				    				.withCountry(line.split(",")[3].contains("\"") ? line.split(",")[3].split("\"")[1] : line.split(",")[3])
				    				.withIata(line.split(",")[4].contains("\"") ? line.split(",")[4].split("\"")[1] : line.split(",")[4])
				    				.withIcao(line.split(",")[5].contains("\"") ? line.split(",")[5].split("\"")[1] : line.split(",")[5])
				    				.withLatitude(!(line.split(",")[6].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[6]))
				    				.withLongitude(!(line.split(",")[7].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[7]))
				    				.withAltitude(!(line.split(",")[8].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[8]))
				    				.withTimezone(!(line.split(",")[9].matches("^-?[0-9]{1,13}(.[0-9]*)?")) ? 0.0 : Double.parseDouble(line.split(",")[9]))
				    				.withDST(line.split(",")[10].contains("\"") ? emDaylightSavingTime.valueOf(line.split(",")[10].split("\"")[1]) : emDaylightSavingTime.valueOf(line.split(",")[10]))
			    				.build()
    						)
    		);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}    	
    	airportData.forEach(System.out::println);
    	setUploaded(true);
    }
    
    private AirportData addAirport(AirportData airport) {
        airportData.add(airport);

        AtmosphericInformation ai = new AtmosphericInformation();
        atmosphericInformation.add(ai);

        return airport;
    }
    
    public String getBASE_URI() {
		return BASE_URI;
	}

	public void setBASE_URI(String BASE_URI) {
		this.BASE_URI = BASE_URI;
	}

	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
    public synchronized List<AirportData> getAirportData() {
		return airportData;
	}

	public synchronized void setAirportData(List<AirportData> airportData) {
		this.airportData = airportData;
	}

	public synchronized List<AtmosphericInformation> getAtmosphericInformation() {
		return atmosphericInformation;
	}

	public synchronized void setAtmosphericInformation(List<AtmosphericInformation> atmosphericInformation) {
		this.atmosphericInformation = atmosphericInformation;
	}

}
