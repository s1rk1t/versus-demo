package com.versus.versus01.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EncodeURL {

	private String encodeValue(String value) {
		String encodedString = "";
		try {
			encodedString = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedString;
	}

	public String encodeURL(String baseURL, String[] keys, String[] paramValues) throws Exception {
		Map<String, String> requestParams = new HashMap<>();

		for (int i = 0; i < keys.length; i++) {
			requestParams.put(keys[i], paramValues[i]);
		}

		String encodedURL = "";

		try {
			encodedURL = requestParams.keySet().stream().map(key -> key + "=" + encodeValue(requestParams.get(key)))
					.collect(Collectors.joining("&", baseURL, ""));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return encodedURL;
	}

}
