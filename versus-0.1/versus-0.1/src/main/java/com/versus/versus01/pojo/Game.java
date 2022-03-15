package com.versus.versus01.pojo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Game {

	// year
	private int season;

	private int week;

	private String seasonType;

	private String date;

	private boolean neutralSite;

	private String venue;

	private String homeTeam;

	private int homeScore;

	private String awayTeam;

	private int awayScore;

	private String winner;
}
