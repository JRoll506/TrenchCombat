package com.rbruno.trench.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EngineTeam {

	public Team team;
	public Player flagHolder;
	public Score score;

	public EngineTeam(String name, ChatColor color, Scoreboard scoreBoard, Objective objective) {
		score = objective.getScore(color + name);
		
		team = scoreBoard.registerNewTeam(name);
		team.setAllowFriendlyFire(false);
		team.setCanSeeFriendlyInvisibles(true);
		team.setPrefix(color + "");
		team.setDisplayName(name);
		
	}
}
