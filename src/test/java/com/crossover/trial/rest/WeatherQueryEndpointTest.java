package com.crossover.trial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.crossover.trial.airport.context.AirportData;
import com.crossover.trial.util.CommonConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Cesar Chavez
 * 
 * This class requires the server to be up, test cases will failed otherwise
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherQueryEndpointTest {

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
        WebTarget path = query.path("ping");
        Response response = path.request().get();
//        String[] res = response.readEntity(String[].class);
        String res = response.readEntity(String.class);
        
        assertNotNull(res);
    }

//    @Test
    public void testWeather() throws Exception {
        WebTarget path = query.path("/weather/BOS/" + 1.1);
    	Response response = path.request().get();
    	
    	// does not work with local inner class
//    	class JsonInner {public String lastUpdateTime;};
    	
		Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        String json = response.readEntity(String.class).replace("[", "").replace("]","");
        
//		JsonInner jsonClass = _gson.fromJson(json, JsonInner.class);
		
        Json jsonClass = _gson.fromJson(json, Json.class);
    	
    	assertNotNull(jsonClass.lastUpdateTime);
    	assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    public class JSON_STUBS {}
    public class Json extends JSON_STUBS {public String lastUpdateTime;};
}