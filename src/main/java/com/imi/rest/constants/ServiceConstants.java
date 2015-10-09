package com.imi.rest.constants;

public enum ServiceConstants {

	VOICE("voice"), VOICE_SMS("voice,sms"), SMS("sms");

	private final String serviceType;

	private ServiceConstants(String serviceType) {
		this.serviceType = serviceType;
	}

	public static ServiceConstants evaluate(String value) {
		ServiceConstants serviceType = null;
		if (value.equalsIgnoreCase(VOICE.toString())) {
			serviceType = VOICE;
		}
		if (value.equalsIgnoreCase(VOICE_SMS.toString())) {
			serviceType = VOICE_SMS;
		}
		if (value.equalsIgnoreCase(SMS.toString())) {
			serviceType = SMS;
		}
		return serviceType;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : serviceType.equals(otherName);
	}

	public String toString() {
		return this.serviceType;
	}

}
