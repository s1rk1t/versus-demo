package com.versus.versus01.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.versus.versus01.pojo.Matchup;
import com.versus.versus01.utils.EncodeURL;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
public class MatchupQuery {

	private String jsonResponse;

	private String apiKey;

	private Matchup matchup;

	public MatchupQuery(String team1, String team2, int minYear, int maxYear, String _apiKey) throws Exception {

		this.apiKey = _apiKey;

		initMatchup(team1, team2, minYear, maxYear);

	}

	private void initMatchup(String team1, String team2, int minYear, int maxYear) throws Exception {

		String baseURL = "https://api.collegefootballdata.com/teams/matchup?";

		String[] keys = { "team1", "team2", "minYear", "maxYear" };

		String[] paramValues = { team1, team2, Integer.toString(minYear), Integer.toString(maxYear) };

		EncodeURL encoder = new EncodeURL();

		String request = encoder.encodeURL(baseURL, keys, paramValues);

		URL requestURL = null;

		try {

			requestURL = new URL(request);

		} catch (MalformedURLException e) {

			System.out.println(">>>>>>>>>>>>>>>>> Error forming URL in initMatchup(): " + request);

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

			this.matchup = mapper.readValue(response.toString(), Matchup.class);

		} catch (IOException e) {

			System.out.println(">>>>>>>>>>>>>>>>> HTTP connection error in initMatchup()");

			e.printStackTrace();
		}

		// for testing
		// System.out.println(this.matchup.toString());

	}

}
