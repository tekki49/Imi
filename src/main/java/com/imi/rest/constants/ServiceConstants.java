package com.imi.rest.constants;

public enum ServiceConstants {

	VOICE("voice"), BOTH("voice,sms"), SMS("sms"),ANY("any");

	private final String serviceType;

	private ServiceConstants(String serviceType) {
		this.serviceType = serviceType;
	}

	public static ServiceConstants evaluate(String value) {
		ServiceConstants serviceType = null;
		if (value.equalsIgnoreCase(VOICE.name())) {
			serviceType = VOICE;
		}
		if (value.equalsIgnoreCase(BOTH.name())) {
			serviceType = BOTH;
		}
		if (value.equalsIgnoreCase(SMS.name())) {
			serviceType = SMS;
		}
		if (value.equalsIgnoreCase(ANY.name())) {
			serviceType = ANY;
		}
		return serviceType;
	}

	public String toString() {
		return this.serviceType;
	}

}
