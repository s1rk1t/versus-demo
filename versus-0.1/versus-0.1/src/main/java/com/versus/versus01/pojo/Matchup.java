package com.versus.versus01.pojo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// @JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Matchup {

	private String team1;
	private String team2;
	private int startYear;
	private int endYear;
	private int team1Wins;
	private int team2Wins;
	private int ties;
	private Game[] games;

}
