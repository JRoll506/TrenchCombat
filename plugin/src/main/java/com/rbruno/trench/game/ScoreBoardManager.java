package com.rbruno.trench.game;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreBoardManager {
	
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	public Scoreboard board = manager.getNewScoreboard();
	
	public Team addTeam(String name){
		return board.registerNewTeam(name);
	}

	public Objective registerNewObjective(String name,String criteria){
		return board.registerNewObjective(name, criteria);
	}
}
