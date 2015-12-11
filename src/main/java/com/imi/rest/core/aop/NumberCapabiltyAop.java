package com.imi.rest.core.aop;

import com.imi.rest.constants.ServiceConstants;

public class NumberCapabiltyAop {

    public static String generateTwilioCapabilities(
            ServiceConstants serviceTypeEnum) {
        String servicesString = null;
        switch (serviceTypeEnum) {
        case SMS:
            servicesString = "SmsEnabled=true";
            break;
        case VOICE:
            servicesString = "VoiceEnabled=true";
            break;
        case BOTH:
            servicesString = "SmsEnabled=true&VoiceEnabled=true";
            break;
        default:
            break;
        }
        return servicesString;
    }
}
