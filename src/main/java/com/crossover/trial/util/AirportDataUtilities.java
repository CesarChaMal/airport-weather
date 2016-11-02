package com.crossover.trial.util;

import com.crossover.trial.airport.context.AirportData;

/**
 * @author Cesar Chavez
 *
 */
public class AirportDataUtilities {

	/** earth radius in KM */
    public static final double R = 6372.8;
    public static int IATA_CODE_LENGTH = 3;
	public static int ICAO_CODE_LENGTH = 4;
	public static double MINIMUM_LATITUDE = -90.0;
	public static double MAXIMUM_LATITUDE = 90.0;
	public static double MINIMUM_LONGITUDE = -180.0;
	public static double MAXIMUM_LONGITUDE = 180.0;

	/**
     *  Latitude validation
     *
     * @param latitude latitude to check
     *
     * @return true if the latitude is between the allowed values
     */
    public static boolean isValidLatitude(double latitude) {
        return (latitude >= MINIMUM_LATITUDE && latitude <= MAXIMUM_LATITUDE);
    }

    /**
     *  Longitude validation
     *
     * @param longitude longitude to check
     *
     * @return true if the latitude is between the allowed values
     */
    public static boolean isValidLongitude(double longitude) {
        return (longitude >= MINIMUM_LONGITUDE && longitude <= MAXIMUM_LONGITUDE);
    }

    /**
     *  IATA validation
     *
     * @param iata iata to check
     *
     * @return String with the right value
     */
    public static String checkIata(String iata) {
    	int maxLength = (iata.length() < IATA_CODE_LENGTH) ? iata.length() : IATA_CODE_LENGTH;
    	iata = iata.substring(0, maxLength);
    	return iata;
    }
    
    /**
     *  ICAO validation
     *
     * @param icao icao to check
     *
     * @return String with the right value
     */
    public static String checkIcao(String icao) {
    	int maxLength = (icao.length() < ICAO_CODE_LENGTH) ? icao.length() : ICAO_CODE_LENGTH;
    	icao = icao.substring(0, maxLength);
    	return icao;
    }
    
    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    static public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a =  Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
