package com.rbruno.trench.game;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EngineTeam {

	public Team team;
	public Player flagHolder;
	public Score score;
	public ChatColor color;
	public Location flag;
	public Location spawn;
	
	public ItemStack wool;

	public EngineTeam(String name, ChatColor color, int woolMeta, Location flag, Location spawn, Scoreboard scoreBoard, Objective objective) {
		score = objective.getScore(color + name);
		score.setScore(0);
		
		this.wool = new ItemStack(Material.WOOL, 1, (byte) woolMeta);
		
		this.color = color;
		this.flag = flag;
		this.spawn = spawn;

		team = scoreBoard.registerNewTeam(name);
		team.setAllowFriendlyFire(false);
		team.setCanSeeFriendlyInvisibles(true);
		team.setPrefix(color + "");
		team.setDisplayName(name);
		
	}
}
