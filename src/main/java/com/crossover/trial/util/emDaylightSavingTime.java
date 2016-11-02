package com.crossover.trial.util;

/**
 * Daylight saving time zones
 * 
 * @author Cesar Chavez
 *
 */
public enum emDaylightSavingTime {
	
	E ("Europe"), 
	A ("US/Canada"), 
	S ("South America"), 
	O ("Australia"), 
	Z ("New Zealand"), 
	N ("None"), 
	U("Unknown");

	private String name;

	private emDaylightSavingTime(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * @param name DST name
	 * @return corresponding enum or Unknown (DST.U)
	 */
	public emDaylightSavingTime fromName(String name) {
		for (emDaylightSavingTime zone : values()) {
			if (zone.name.equalsIgnoreCase(name))
				return zone;
		}
		return emDaylightSavingTime.U;
	}
}