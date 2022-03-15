package com.versus.versus01.pojo;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {

	// year
	private int season;

	private int week;

	private String seasonType;

	private String homeTeam;

	private String awayTeam;

	private String homeConference;

	private String awayConference;

}
