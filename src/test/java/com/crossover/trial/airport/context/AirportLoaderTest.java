package com.crossover.trial.airport.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.crossover.trial.airport.context.services.AirportDataService;
import com.crossover.trial.util.CommonConstants;
import com.google.gson.Gson;

/**
 * @author Cesar Chavez
 * 
 * This class requires the server to be up, test cases will failed otherwise
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AirportLoaderTest {

    private static final String BASE_URI = CommonConstants.BASE_URI;

    /** end point for read queries */
    private static WebTarget query;

    /** end point to supply updates */
    private static WebTarget collect;

    private Gson _gson = new Gson();
    
    @BeforeClass
    public static void setUp() throws Exception {
        Client client = ClientBuilder.newClient();
//        query = client.target(BASE_URI + "/query");
//        collect = client.target(BASE_URI + "/collect");
        query = client.target(BASE_URI).path("query");
        collect = client.target(BASE_URI).path("collect");
    }

    @Test
    public void testUpload() throws Exception {
    	// absolute path
//		File airportDataFile = new File("C://springsource//workspaces//ws_airport-weather//weather-dist//src//main//resources//airports.dat");
//		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
//			System.err.println(airportDataFile + " is not a valid input");
//		}
//	
//		AirportLoader al = new AirportLoader(BASE_URI);
//		assertEquals(0, al.getAirportData().size());
//		assertFalse(al.isUploaded());
//		al.upload(airportDataFile);
//		assertEquals(10, al.getAirportData().size());
//		assertTrue(al.isUploaded());
		
    	// relative path
    	File airportDataFile = new File("src/main/resources/airports.dat");
		if (!airportDataFile.exists() || airportDataFile.length() == 0) {
			System.err.println(airportDataFile + " is not a valid input");
		}
		
		AirportLoader al = new AirportLoader(BASE_URI);
		assertEquals(0, al.getAirportData().size());
		assertFalse(al.isUploaded());
		al.upload(airportDataFile);
		assertEquals(10, al.getAirportData().size());
		assertTrue(al.isUploaded());
    }
    
//    @Test
    public void testUpload2() throws Exception {
    	// absolute path
    	File airportDataFile = new File("C://springsource//workspaces//ws_airport-weather//weather-dist//src//main//resources//airports.dat");
    	if (!airportDataFile.exists() || airportDataFile.length() == 0) {
    		System.err.println(airportDataFile + " is not a valid input");
    	}

    	AirportDataService airportDataService = AirportDataService.getInstance();
    	AirportLoader al = new AirportLoader(BASE_URI);
    	assertEquals(0, airportDataService.getAirportData().size());
    	assertFalse(al.isUploaded());
    	al.upload(airportDataService, airportDataFile);
    	al.pingCollect();
    	assertEquals(10, airportDataService.getAirportData().size());
    	assertTrue(al.isUploaded());
    	
    	// relative path
		airportDataFile = new File("src/main/resources/airports.dat");
    	if (!airportDataFile.exists() || airportDataFile.length() == 0) {
    		System.err.println(airportDataFile + " is not a valid input");
    	}
    	
    	airportDataService = AirportDataService.getInstance();
    	al = new AirportLoader(BASE_URI);
    	assertEquals(10, airportDataService.getAirportData().size());
    	assertFalse(al.isUploaded());
    	al.upload(airportDataService, airportDataFile);
    	al.pingCollect();
    	assertEquals(10, airportDataService.getAirportData().size());
    	assertTrue(al.isUploaded());
    }

}