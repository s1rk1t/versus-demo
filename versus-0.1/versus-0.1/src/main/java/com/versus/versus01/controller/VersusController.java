package com.versus.versus01.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.versus.versus01.pojo.Game;
import com.versus.versus01.pojo.Matchup;
import com.versus.versus01.pojo.Schedule;
import com.versus.versus01.pojo.TeamDataContainer;
import com.versus.versus01.pojo.TeamRecord;
import com.versus.versus01.query.MatchupQuery;
import com.versus.versus01.query.ScheduleQuery;
import com.versus.versus01.query.TeamRecordQuery;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin
public class VersusController {

	@Value("${apiKey}")
	private String apiKey;

	@GetMapping("/team/{teamName}/{year}")
	public TeamDataContainer getTeamData(@PathVariable String teamName, @PathVariable int year) throws Exception {

		// get the team's record for that year
		TeamRecordQuery teamRecordQuery = new TeamRecordQuery(teamName, year, apiKey);

		TeamRecord teamRecord = teamRecordQuery.getTeamRecord();

		// get team's schedule (for given year) and parse the most recent four SEC games
		// for display on the Team's home page
		ScheduleQuery schedule = new ScheduleQuery(teamName, year, apiKey);

		ArrayList<Schedule> completeSchedule = schedule.getSchedule();

		Schedule[] lastFourSECGames = completeSchedule.subList(completeSchedule.size() - 4, completeSchedule.size())
				.toArray(new Schedule[4]);

		Matchup[] matchups = new Matchup[lastFourSECGames.length];

		// populate matchups array with the four latest SEC games returned by the
		// schedule query
		for (int i = 3; i > -1; i--) {

			MatchupQuery matchupQuery = new MatchupQuery(lastFourSECGames[i].getHomeTeam(),
					lastFourSECGames[i].getAwayTeam(), year, year, apiKey);

			matchups[i] = matchupQuery.getMatchup();
		}

		matchups = checkForPostSeasonRematchAndRemoveDupes(matchups);

		Game[] sortedGames = sortAndFlattenGames(matchups);

		// add the team's record, the four most recent SEC conference games', and the
		// matchup data for each of those games
		TeamDataContainer allTeamData = new TeamDataContainer(teamRecord, lastFourSECGames, matchups, sortedGames);

		return allTeamData;

	}

	@GetMapping("/team/{teamName}/matches/{year}")
	public Game[] getSortedGames(@PathVariable String teamName, @PathVariable int year) throws Exception {

		// get the schedule information for all SEC games for a given team
		ScheduleQuery query = new ScheduleQuery(teamName, year, this.apiKey);

		ArrayList<Schedule> completeSchedule = query.getSchedule();

		Matchup[] matchups = new Matchup[completeSchedule.size()];

		// get all Matchup data for each SEC game in the given team's schedule
		for (int i = 0; i < completeSchedule.size(); i++) {

			MatchupQuery matchupQuery = new MatchupQuery(completeSchedule.get(i).getHomeTeam(),
					completeSchedule.get(i).getAwayTeam(), year, year, apiKey);

			matchups[i] = matchupQuery.getMatchup();
		}

		// since some game arrays contain more than one value (if two teams play in the
		// regular and post-season), it is necessary to flatten those game arrays and
		// remove duplicate game arrays for the respective Matchup
		matchups = checkForPostSeasonRematchAndRemoveDupes(matchups);

		Game[] sortedGames = sortAndFlattenGames(matchups);

		return sortedGames;
	}

	@GetMapping("/teams")
	public List<String> getAllSECTeams() {

		List<String> teams = new ArrayList<>();

		// return all of the current SEC teams
		teams.add("Georgia");
		teams.add("Alabama");
		teams.add("Florida");
		teams.add("Arkansas");
		teams.add("Kentucky");
		teams.add("Auburn");
		teams.add("Missouri");
		teams.add("LSU");
		teams.add("South Carolina");
		teams.add("Ole Miss");
		teams.add("Tennessee");
		teams.add("Mississippi State");
		teams.add("Vanderbilt");
		teams.add("Texas A&M");

		return teams;
	}

	// check for edge case where two teams played twice in one season, once in
	// regular season and once in post-season/SECCG
	private Matchup[] checkForPostSeasonRematchAndRemoveDupes(Matchup[] _matchups) {

		Game[] regularSeasonGame = new Game[1];
		Game[] postSeasonGame = new Game[1];

		for (int i = 0; i < _matchups.length; i++) {

			if (_matchups[i].getGames().length > 1) {

				regularSeasonGame[0] = _matchups[i].getGames()[0];
				postSeasonGame[0] = _matchups[i].getGames()[1];

				// first, the regular season game is set
				_matchups[i].setGames(regularSeasonGame);

				// last game is set as the post-season rematch
				_matchups[_matchups.length - 1].setGames(postSeasonGame);

				break;
			}

		}
		return _matchups;
	}

	// must be done because of possibility of multiple games in regular/post season
	private Game[] sortAndFlattenGames(Matchup[] _matchups) {

		List<Game> postSeasonGames = new ArrayList<Game>();

		int size = _matchups.length;

		List<Game> games = new ArrayList<Game>();

		for (int i = 0; i < size; i++) {

			games.add(_matchups[i].getGames()[0]);

			if (_matchups[i].getGames().length > 1) {

				Game postSeasonGame = _matchups[i].getGames()[1];

				// add values to postSeason array
				postSeasonGames.add(postSeasonGame);
			} // end if

		} // end for

		// append post season List of Game objects to regular season List of Game
		// objects
		games.addAll(postSeasonGames);

		// reverse list for front end display requirement (most recent first)
		Collections.reverse(games);

		// convert List to array
		Game[] sortedGamesArray = new Game[size];

		games.toArray(sortedGamesArray); // fill the array

		// return the sorted games!
		return sortedGamesArray;
	}
}
