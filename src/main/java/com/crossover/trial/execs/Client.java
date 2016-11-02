package com.crossover.trial.execs;

import java.io.IOException;
import java.util.logging.Logger;

import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.weather.context.WeatherClient;

/**
 * A reference implementation for the weather client. Consumers of the REST API can look at WeatherClient
 * to understand API semantics. This existing client populates the REST endpoint with dummy data useful for
 * testing.
 *
 * @author code test administrator
 */
public class Client {

//    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String BASE_URI = CommonConstants.BASE_URI;
    
    static {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
    }

    public static void main(String[] args) {
//        System.out.println("Starting Weather App local testing client: " + BASE_URI);
		LOGGER.info("Starting Weather App local testing client: " + BASE_URI);

        WeatherClient wc = new WeatherClient(BASE_URI);
        wc.pingCollect();
        wc.populate("wind", 4.0, 0, 4, 10, 20);
        wc.populate("temperature", 0, 20, 6, 4, 20);
        wc.populate("humidity", 1, 30, 6, 4, 20);

        wc.query("BOS");
        wc.query("JFK");
        wc.query("EWR");
        wc.query("LGA");
        wc.query("MMU");

        wc.pingQuery();
        wc.exit();	
//        System.out.print("complete");
		LOGGER.info("complete");
        System.exit(0);
    }
}
