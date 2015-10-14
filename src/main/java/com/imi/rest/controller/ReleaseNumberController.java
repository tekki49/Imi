package com.imi.rest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imi.rest.util.ImiJsonUtil;

@RestController
public class ReleaseNumberController {

	
	@RequestMapping(value="/release/{number}", method=RequestMethod.DELETE)
	public String releaseNumber(@PathVariable("number") String number, @RequestHeader("provider") String provideId ) throws JsonProcessingException{
		return ImiJsonUtil.getJSONString(number, "released");	
	}
	
}
