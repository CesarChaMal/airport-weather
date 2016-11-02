package com.crossover.trial.execs;

import java.io.IOException;
import java.util.logging.Logger;

import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.weather.context.WeatherServer;

/**
 * This main method will be use by the automated functional grader. You shouldn't move this class or remove the
 * main method. You may change the implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class Server {

//    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String BASE_URL = CommonConstants.BASE_URL;

    static {
		try {
			MyLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}
    }

    public static void main(String[] args) {
//		System.out.println("Starting Weather App local testing server: " + BASE_URL);
//		LOGGER.log(Level.INFO, "Starting Weather App local testing server: " + BASE_URL);
		LOGGER.info("Starting Weather App local testing server: " + BASE_URL);

		WeatherServer server = new WeatherServer(BASE_URL);
    }

}
