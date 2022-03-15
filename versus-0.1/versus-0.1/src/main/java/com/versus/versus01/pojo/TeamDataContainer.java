package com.versus.versus01.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDataContainer {

	private TeamRecord teamRecord;

	private Schedule[] schedule;

	private Matchup[] matchups;

	private Game[] games;

}
