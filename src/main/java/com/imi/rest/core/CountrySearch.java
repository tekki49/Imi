package com.imi.rest.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.model.Country;

public interface CountrySearch {
    public Set<Country> importCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws FileNotFoundException, JsonParseException,
            JsonMappingException, IOException;
}
