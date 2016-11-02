package com.crossover.trial.airport.context;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.crossover.trial.airport.context.DataPoint.Builder;
import com.crossover.trial.util.AirportDataUtilities;
import com.crossover.trial.util.emDaylightSavingTime;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {


//	1,"General Edward Lawrence Logan Intl","Boston","United States","BOS","KBOS",42.364347,-71.005181,19,-5,"A"
	
    /** Name of the airport */
    private String name;

	/** City of the airport */
	private String city;
	
	/** Country of the airport */
	private String country;
	
    /** the three letter IATA code */
    private String iata;
    
    /** the four letter ICAO code */
    private String icao;
    
    /** latitude value in degrees */
    private double latitude;

    /** longitude value in degrees */
    private double longitude;

    /** altitude value in feets */
    private double altitude;
    
	/** Hours offset from UTC. */
	private double timezone;

	/** Day light saving time of airport */
	private emDaylightSavingTime dst = emDaylightSavingTime.U;

    /** private constructor, use the builder to create this object */
    /**
     * @param builder
     */
    private AirportData(Builder builder){
		this.setName(builder.name);
        this.setCity(builder.city);
        this.setCountry(builder.country);
        this.setIata(builder.iata);
        this.setIcao(builder.icao);
        this.setLatitude(builder.latitude);
        this.setLongitude(builder.longitude);
        this.setAltitude(builder.altitude);
        this.setTimezone(builder.timezone);
        this.setDST(builder.dst);
	}

	public static class Builder {
		
	    private String name;
		private String city;
		private String country;
	    private String iata;
	    private String icao;
	    private double latitude;
	    private double longitude;
	    private double altitude;
		private double timezone;
		private emDaylightSavingTime dst;
		
        public Builder() { }

        public Builder withName(String name) {
        	this.name = name;
        	return this;
        }

        public Builder withCity(String city) {
        	this.city = city;
        	return this;
        }
        
        public Builder withCountry(String country) {
        	this.country = country;
        	return this;
        }
        
        public Builder withIata(String iata) {
        	this.iata = AirportDataUtilities.checkIata(iata);
        	return this;
        }
        
        public Builder withIcao(String icao) {
        	this.icao = AirportDataUtilities.checkIata(icao);
        	return this;
        }
        
        public Builder withLatitude(double latitude) {
            if (AirportDataUtilities.isValidLatitude(latitude)) {
                this.latitude = latitude;
            } else {
                throw new IllegalArgumentException("Invalid latitude");
            }
            return this;
        }

        public Builder withLongitude(double longitude) {
            if (AirportDataUtilities.isValidLatitude(longitude)) {
                this.longitude = longitude;
            } else {
                throw new IllegalArgumentException("Invalid longitude");
            }
        	return this;
        }
        
        public Builder withAltitude(double altitude) {
        	this.altitude = altitude;
        	return this;
        }
        
        public Builder withTimezone(double timezone) {
        	this.timezone = timezone;
        	return this;
        }
        
        public Builder withDST(emDaylightSavingTime dst) {
        	this.dst = dst;
        	return this;
        }
        
        public AirportData build() {
            return new AirportData(this);
        }
	}

	public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getTimezone() {
		return timezone;
	}

	public void setTimezone(double timezone) {
		this.timezone = timezone;
	}

	public emDaylightSavingTime getDST() {
		return dst;
	}

	public void setDST(emDaylightSavingTime dst) {
		this.dst = dst;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object other) {
        if (other instanceof AirportData) {
            return ((AirportData)other).getIata().equals(this.getIata());
        }

        return false;
    }
}
