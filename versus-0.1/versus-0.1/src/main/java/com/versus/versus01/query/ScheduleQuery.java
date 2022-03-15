package com.versus.versus01.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.versus.versus01.pojo.Schedule;
import com.versus.versus01.utils.EncodeURL;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Data
public class ScheduleQuery {

	private String apiKey;

	private String team;

	private int year;

	private ArrayList<Schedule> schedule;

	public ScheduleQuery(String team, int year, String _apiKey) throws Exception {
		this.apiKey = _apiKey;
		this.year = year;
		this.team = team;
//		if (this.team.equals("Texas%20A&M")) {
//			this.team = "Texas%20A%26M";
//		}
		initSchedule();
	}

	private void initSchedule() throws Exception {

		// this.team = replaceSpaceIfPresent(this.team);

		String baseURL = "https://api.collegefootballdata.com/games/media?";

		String[] keys = { "year", "team", "conference" };

		String[] paramValues = { Integer.toString(this.year), this.team, "SEC" };

		EncodeURL encoder = new EncodeURL();

		String request = encoder.encodeURL(baseURL, keys, paramValues);

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

			// convert JSON array (in response) to array list of Schedule objects
			Schedule[] scheduleArray = mapper.readValue(response.toString(), Schedule[].class);

			ArrayList<Schedule> completeSchedule = new ArrayList<>(Arrays.asList(scheduleArray));

			// System.out.println(completeSchedule);

			// parse all games where both teams are in the SEC
			this.schedule = parseSECGames(
					s -> s.getHomeConference() != null && s.getAwayConference() != null
							&& s.getAwayConference().equals("SEC") && s.getHomeConference().equals("SEC"),
					completeSchedule);

		} catch (IOException e) {

			System.out.println(">>>>>>>>>>>>>>>>> HTTP connection error in initSchedule()");

			e.printStackTrace();
		}

		// for testing
		// System.out.println(this.schedule.toString());

	}

	// use Stream to filter the SEC conference games out of the complete schedule
	private ArrayList<Schedule> parseSECGames(Predicate<Schedule> schedulePredicate,
			ArrayList<Schedule> completeSchedule) {

		ArrayList<Schedule> toReturn = new ArrayList<>();

		for (Schedule s : completeSchedule.stream().filter(schedulePredicate).toArray(Schedule[]::new)) {
			if (!toReturn.contains(s))
				toReturn.add(s);
		}

		return toReturn;

	}

}
