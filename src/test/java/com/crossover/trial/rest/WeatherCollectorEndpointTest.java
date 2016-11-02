package com.crossover.trial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.crossover.trial.airport.context.AirportData;
import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.emDaylightSavingTime;
import com.google.gson.Gson;

/**
 * @author Cesar Chavez
 * 
 * This class requires the server to be up, test cases will failed otherwise
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherCollectorEndpointTest {

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

//    @Test
    public void testPing() throws Exception {
    	WebTarget path = collect.path("ping");
    	Response response = path.request().get();
        String res = response.readEntity(String.class);
    	
    	assertEquals("1", res);
    }
    
//    @Test
    public void testUpdateWeather() throws Exception {
    	WebTarget path = collect.path("weather/BOS/wind");
    	DataPoint dp = new DataPoint.Builder()
        		.withMean(10.0)
        		.withFirst(4)
        		.withMedian(2)
        		.withLast(3)
        		.withCount(50)
        	.build();
        
    	Response response = path.request().post((Entity.entity(dp, "application/json")));
//        String res = response.readEntity(String.class);
    	assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

//    @Test
    public void testGetAirports() throws Exception {
        WebTarget path = collect.path("airports");
        Response response = path.request().get();
        String[] res = response.readEntity(String[].class);
//        String res = response.readEntity(String.class);

        assertEquals(11, res.length);
    }

//    @Test
    public void testGetAirport() throws Exception {
    	WebTarget path = collect.path("airport/BOS");
    	Response response = path.request().get();
//    	String[] res = response.readEntity(String[].class);
        String res = response.readEntity(String.class);
        
        AirportData data = _gson.fromJson(res, AirportData.class);
    	
    	assertNotNull(data);
    	assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    	
    	path = collect.path("airport/CAS");
    	response = path.request().get();
//    	String[] res = response.readEntity(String[].class);
    	res = response.readEntity(String.class);
    	
    	data = _gson.fromJson(res, AirportData.class);
    	
    	assertNull(data);
    	assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

//    @Test
    public void testAddAirport() throws Exception {
    	WebTarget path = collect.path("airport/YUC/42.639751/-76.778925");
    	Response response = path.request().post((Entity.entity(null, "application/json")));
    	String res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

    	path = collect.path("airport/BOS/41.21151/-96.778925");
    	response = path.request().post((Entity.entity(null, "application/json")));
    	res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.FOUND.getStatusCode(), response.getStatus());
    }
    
//    @Test
    public void testAddAirport2() throws Exception {
    	WebTarget path = collect.path("airport");

    	AirportData ad = new AirportData.Builder()
    			.withName("Houston Airport")
    			.withCity("Houston")
    			.withCountry("US")
    			.withIata("HOU")
    			.withIcao("HOUS")
				.withLatitude(40.0000)
				.withLongitude(-70.000)
				.withAltitude(900)
				.withTimezone(-6)
				.withDST(emDaylightSavingTime.A)
			.build();

    	Response response = path.request().post((Entity.entity(ad, "application/json")));
    	String res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    	
    	path = collect.path("airport");
    	
    	ad = new AirportData.Builder()
    			.withName("Roberto Ascencio")
    			.withCity("Merida")
    			.withCountry("Mexico")
    			.withIata("YUC")
    			.withIcao("YUCA")
    			.withLatitude(40.0000)
    			.withLongitude(-75.000)
    			.withAltitude(100)
    			.withTimezone(-6)
    			.withDST(emDaylightSavingTime.A)
    			.build();
    	
    	response = path.request().post((Entity.entity(ad, "application/json")));
    	res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.FOUND.getStatusCode(), response.getStatus());
    }
    
//    @Test
    public void testDeleteAirport() throws Exception {
    	WebTarget path = collect.path("airport/JFK");
    	Response response = path.request().delete();
    	String res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    	path = collect.path("airport/CAS");
    	response = path.request().delete();
    	res = response.readEntity(String.class);
    	
    	assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // this test case stop the server 
//    @Test
//    public void testExit() throws Exception {
//    	WebTarget path = collect.path("exit");
//    	Response response = path.request().get();
//    	String res = response.readEntity(String.class);
//    	
//    	assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
//    }
    
}