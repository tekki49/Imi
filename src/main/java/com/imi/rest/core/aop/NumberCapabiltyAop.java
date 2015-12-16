package com.imi.rest.core.aop;

import com.imi.rest.constants.ServiceConstants;

public class NumberCapabiltyAop {

	public static String generateTwilioCapabilities(ServiceConstants serviceTypeEnum) {
		String servicesString = null;
		switch (serviceTypeEnum) {
		case SMS:
			servicesString = "SmsEnabled=true&VoiceEnabled=false";
			break;
		case VOICE:
			servicesString = "VoiceEnabled=true&SmsEnabled=false";
			break;
		case BOTH:
			servicesString = "SmsEnabled=true&VoiceEnabled=true";
			break;
		case ANY:
			servicesString="";
			break;
		default:
			break;
		}
		return servicesString;
	}
}
