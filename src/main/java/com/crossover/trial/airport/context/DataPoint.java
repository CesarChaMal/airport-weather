package com.crossover.trial.airport.context;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    /** the mean of the observations */
    private double mean = 0.0;
    /** 1st quartile -- useful as a lower bound */
    private int first = 0;
    /** 2nd quartile -- median value */
    private int median = 0;
    /** 3rd quartile value -- less noisy upper value */
    private int last = 0;
    /** the total number of measurements */
    private int count = 0;

    /** private constructor, use the builder to create this object */
    private DataPoint(Builder builder){
    	this.setMean(builder.mean);
        this.setFirst(builder.first);
        this.setMedian(builder.median);
        this.setLast(builder.last);
        this.setCount(builder.count);
	}
	
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) { 
    	this.mean = mean; 
    }

    public int getFirst() {
    	return this.first;
    }
    
    protected void setFirst(int first) {
    	this.first = first;
    }

    public int getMedian() {
    	return this.median;
    }
    
    public void setMedian(int median) { 
    	this.median = median; 
    }
    
    public int getLast() {
        return this.last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

	public static class Builder {
		
		private double mean;
	    private int first;
	    private int median;
	    private int last;
	    private int count;
		
        public Builder() { }

        public Builder withMean(double mean) {
        	this.mean = mean;
        	return this;
        }

        public Builder withFirst(int first) {
        	this.first= first;
        	return this;
        }
        
        public Builder withMedian(int median) {
        	this.median= median;
        	return this;
        }
        
        public Builder withLast(int last) {
        	this.last= last;
        	return this;
        }
        
        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }
	}

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object that) {
        return this.toString().equals(that.toString());
    }

}
