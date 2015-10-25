package com.imi.rest.helper;

import java.util.ArrayList;
import java.util.List;

public class ApplicationHelper {

	public static List<String> validatePurchaseRequest(String number,
			String providerId) {
		List<String> errors = new ArrayList<String>();
		if (providerId != null){
			switch (providerId.trim()) {
			case "1":
				break;
			case "2":
				break;
			case "3":
				break;
			default:
				errors.add("ProviderId is invalid");
				break;
			}
		}else{
			errors.add("Please add a valid providerId header");
		}
		return errors;
	}

}
