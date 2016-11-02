package com.crossover.trial.weather.context;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.crossover.trial.airport.context.DataPoint;
import com.crossover.trial.rest.WeatherQueryEndpointImpl;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;

/**
 * A reference implementation for the weather client. Consumers of the REST API can look at WeatherClient
 * to understand API semantics. This existing client populates the REST endpoint with dummy data useful for
 * testing.
 *
 * @author code test administrator
 */
public class WeatherClient {

//    private final static Logger LOGGER = Logger.getLogger(WeatherClient.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String BASE_URI = CommonConstants.BASE_URI;

	/** end point for read queries */
    private WebTarget query;

    /** end point to supply updates */
    private WebTarget collect;

    private Client client;
    
	{
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
    }

    public WeatherClient() {
    	this.client = ClientBuilder.newClient();
    	this.query = this.client.target(this.BASE_URI + CommonConstants.QUERY_ENDPOINT);
    	this.collect = this.client.target(this.BASE_URI + CommonConstants.COLLECT_ENDPOINT);
    }

    public WeatherClient(String base_uri) {
    	this.client = ClientBuilder.newClient();
    	this.BASE_URI = base_uri;
    	this.query = this.client.target(base_uri + CommonConstants.QUERY_ENDPOINT);
    	this.collect = this.client.target(base_uri + CommonConstants.COLLECT_ENDPOINT);
    }
    
    public void pingCollect() {
        WebTarget path = collect.path("/ping");
        Response response = path.request().get();
//        System.out.print("collect.ping: " + response.readEntity(String.class) + "\n");
		LOGGER.info("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    public void query(String iata) {
        WebTarget path = query.path("/weather/" + iata + "/0");
        Response response = path.request().get();
//        System.out.println("query." + iata + ".0: " + response.readEntity(String.class));
		LOGGER.info("query." + iata + ".0: " + response.readEntity(String.class));
    }

    public void pingQuery() {
        WebTarget path = query.path("/ping");
        Response response = path.request().get();
//        System.out.println("query.ping: " + response.readEntity(String.class));
		LOGGER.info("query.ping: " + response.readEntity(String.class));
    }

    public void populate(String pointType, double mean, int first, int median, int last, int count) {
        WebTarget path = collect.path("/weather/BOS/" + pointType);
        DataPoint dp = new DataPoint.Builder()
        				.withMean(mean)
                		.withFirst(first)
                		.withMedian(median)
                		.withLast(last)
                		.withCount(count)
                	.build();
        Response post = path.request().post(Entity.entity(dp, "application/json"));
		LOGGER.info("collect.weather.pointype: " + post.readEntity(String.class));
    }

    public void exit() {
        try {
            collect.path("/exit").request().get();
        } catch (Throwable t) {
            // swallow
        }
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
	
}
