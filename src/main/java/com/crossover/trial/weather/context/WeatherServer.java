package com.crossover.trial.weather.context;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.crossover.trial.rest.WeatherCollectorEndpointImpl;
import com.crossover.trial.rest.WeatherQueryEndpointImpl;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;

/**
 * Jetty Server class
 * 
 * @author Cesar Chavez
 *
 */
public class WeatherServer {

//    private final static Logger LOGGER = Logger.getLogger(WeatherServer.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String BASE_URL = CommonConstants.BASE_URL;

	private HttpServer server;
	
	{
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
    }

	private HttpServer createHttpServer() throws IOException {
		final ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(WeatherCollectorEndpointImpl.class);
		resourceConfig.register(WeatherQueryEndpointImpl.class);

		return GrizzlyHttpServerFactory.createHttpServer(URI.create(getBASE_URL()), resourceConfig, false);
	}
	
    public WeatherServer(String base_url) {
    	this.BASE_URL = base_url;
		try {
			server = createHttpServer();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				server.shutdownNow();
			}));
			
			HttpServerProbe probe = new HttpServerProbe.Adapter() {
				public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
//					System.out.println(request.getRequestURI());
//					LOGGER.log(Level.INFO, request.getRequestURI());
					LOGGER.info(request.getRequestURI());
				}
			};
			server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);

			// the autograder waits for this output before running automated
			// tests, please don't remove it
			server.start();
//			System.out.println(format("Weather Server started.\n url=%s\n", base_url));
//			LOGGER.log(Level.INFO, format("Weather Server started.\n url=%s\n", base_url));
			LOGGER.info(format("Weather Server started.\n url=%s\n", base_url));

			// blocks until the process is terminated
			Thread.currentThread().join();
			server.shutdown();
		} catch (IOException | InterruptedException ex) {
//			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			LOGGER.severe(ex.getMessage());
		}
		
	}

	public String getBASE_URL() {
		return BASE_URL;
	}

	public void setBASE_URL(String BASE_URL) {
		this.BASE_URL = BASE_URL;
	}

	public HttpServer getServer() {
		return server;
	}
	
	public void setServer(HttpServer server) {
		this.server = server;
	}
	
}
