package com.crossover.trial.execs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.crossover.trial.airport.context.AirportLoader;
import com.crossover.trial.airport.context.services.AirportDataService;
import com.crossover.trial.util.CommonConstants;
import com.crossover.trial.util.MyLogger;
import com.crossover.trial.weather.context.WeatherClient;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class Loader {

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

    public static void main(String args[]) throws IOException{
        File airportDataFile = new File(args[0]);
//        LOGGER.info("File:" + args[0]);
        
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            System.err.println(airportDataFile + " is not a valid input");
            System.exit(1);
        }

        // this test will only work with the server down 
        AirportLoader al = new AirportLoader(BASE_URI);
        al.upload(airportDataFile);
        
        // this test will only work with the server up 
        AirportDataService airportDataService = AirportDataService.getInstance();
        al.upload(airportDataService, airportDataFile);
        al.pingCollect();
        
        System.exit(0);
    }
}
