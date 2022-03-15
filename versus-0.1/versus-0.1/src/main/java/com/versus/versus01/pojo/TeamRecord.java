package com.versus.versus01.pojo;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRecord {

	private int year;

	private String team;

	private String conference = "SEC";

	private String division;

	private int expectedWins;

	private Total total;

	private ConferenceGames conferenceGames;

	// private HomeGame homeGames;

	// private AwayGame awayGames;

}
