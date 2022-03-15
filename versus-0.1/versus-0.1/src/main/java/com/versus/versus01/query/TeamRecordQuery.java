package com.versus.versus01.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.versus.versus01.pojo.TeamRecord;
import com.versus.versus01.utils.EncodeURL;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class TeamRecordQuery {

	private String jsonResponse;

	private String apiKey;

	@Autowired
	private TeamRecord teamRecord;

	public TeamRecordQuery(String team, int year, String _apiKey) throws Exception {

		this.apiKey = _apiKey;

		initTeamRecord(team, year);

	}

	private void initTeamRecord(String team, int year) throws Exception {

		String baseURL = "https://api.collegefootballdata.com/records?";

		String[] keys = { "year", "team", "conference" };

		String[] paramValues = { Integer.toString(year), team, "SEC" };

		EncodeURL encoder = new EncodeURL();

		String request = encoder.encodeURL(baseURL, keys, paramValues);

		// testing URL encoder
		// System.out.println("request URL: " + request);

		URL requestURL = null;

		try {

			requestURL = new URL(request);

		} catch (MalformedURLException e) {

			System.out.println(">>>>>>>>>>>>>>>>> Error forming URL in initTeamRecord(): " + request);

			e.printStackTrace();
		}

		try {

			HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

			conn.setRequestProperty("Authorization", "Bearer " + this.apiKey);

			conn.setRequestProperty("Content-Type", "application/json");

			conn.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String output = "";

			StringBuffer response = new StringBuffer();

			while ((output = reader.readLine()) != null) {

				response.append(output);

			}

			reader.close();

			ObjectMapper mapper = new ObjectMapper();

			this.teamRecord = mapper.readValue(response.toString(), TeamRecord[].class)[0];

		} catch (IOException e) {

			System.out.println(">>>>>>>>>>>>>>>>> HTTP connection error in initTeamRecord()");

			e.printStackTrace();
		}

		// for testing
		// System.out.println(this.teamRecord.toString());

	}

}
