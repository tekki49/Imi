package com.imi.rest.core.aop;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.model.Number;

@Component
public class ServiceTypeAop {

	public void setTwilioServiceType(Number number) {
		Map<String, Boolean> capabilties = number.getCapabilities();
		if (capabilties.get("voice") && capabilties.get("SMS")) {
			number.setServiceType(ServiceConstants.BOTH.name());
			number.setSmsEnabled(true);
			number.setVoiceEnabled(true);
		} else if (capabilties.get("SMS")) {
			number.setServiceType(ServiceConstants.SMS.name());
			number.setSmsEnabled(true);
		} else if (capabilties.get("voice")) {
			number.setServiceType(ServiceConstants.VOICE.name());
			number.setVoiceEnabled(true);
		}
	}

	public void setNexmoServiceType(Number number) {
		List<String> features = number.getFeatures();
		if (number == null || features == null)
			return;
		if (features.contains(ServiceConstants.SMS.name()) && features.contains(ServiceConstants.VOICE.name())) {
			number.setServiceType(ServiceConstants.BOTH.name());
			number.setSmsEnabled(true);
			number.setVoiceEnabled(true);
		} else if (features.contains(ServiceConstants.SMS.name())) {
			number.setServiceType(ServiceConstants.SMS.name());
			number.setSmsEnabled(true);
		} else {
			number.setServiceType(ServiceConstants.VOICE.name());
			number.setVoiceEnabled(true);
		}
	}

	public void setPlivoServiceType(Number number) {
		if (number.isSmsEnabled() && number.isVoiceEnabled()) {
			number.setServiceType(ServiceConstants.BOTH.name());
		} else if (number.isSmsEnabled()) {
			number.setServiceType(ServiceConstants.SMS.name());
		} else {
			number.setServiceType(ServiceConstants.VOICE.name());
		}
	}

}
