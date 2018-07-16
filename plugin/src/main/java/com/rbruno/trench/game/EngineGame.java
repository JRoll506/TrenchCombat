package com.rbruno.trench.game;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.rbruno.trench.Main;
import com.rbruno.trench.classes.EngineClass;

public class EngineGame {

	public EngineTeam[] teams;

	public Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	public Objective objective = board.registerNewObjective("Score", "dummy");

	private Main main;

	public EngineGame(Main main) {
		this.main = main;

		teams = new EngineTeam[2];
		teams[0] = new EngineTeam("Red", ChatColor.RED, 14, main.trenchConfig.redFlag, main.trenchConfig.redSpawn, board, objective);
		teams[1] = new EngineTeam("Blue", ChatColor.BLUE, 11, main.trenchConfig.blueFlag, main.trenchConfig.blueSpawn, board, objective);

		/* TODO Stats */

		setScoreBoard();
	}

	public void pickTeams() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		
		for (EngineTeam team : teams) {
			for (Player player : main.teamQueue.keySet()) {
				if (team.team.getEntries().size() >= Bukkit.getOnlinePlayers().size() / 2 && main.teamQueue.get(player).equalsIgnoreCase(team.team.getName())) {
					team.team.addEntry(player.getName());
					players.remove(player);
				}
			}
		}
		
		int i = 0;
		for (Player player : players) {
			teams[i % teams.length].team.addEntry(player.getName());
			i++;
		}
	}

	public void tpPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setFallDistance(0);
			player.teleport(getColorTeam(player).spawn);
		}
	}

	public void giveItems() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			giveItems(player);
		}
	}

	public void setScoreBoard() {
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.YELLOW + "The War has begun!");

		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

		for (Player player : onlinePlayers) {
			player.setScoreboard(board);
		}
	}

	public void giveItems(final Player player) {
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());

		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		
		player.getInventory().setHelmet(main.game.getColorTeam(player).wool);

		EngineClass playerClass = main.getClassManager().classMap.get(player);
		
		if (playerClass == null) {
			playerClass = main.getClassManager().classes[0];
		}
		player.getInventory().addItem(playerClass.getItems());
		if (playerClass.getEffect() != null) {
			player.addPotionEffect(playerClass.getEffect());
		}
	}

	public void addPlayer(Player player) {
		player.setScoreboard(board);

		EngineTeam team = teams[0];
		for (EngineTeam target : teams)
			if (target.team.getEntries().size() < team.team.getEntries().size())
				team = target;
		
		team.team.addEntry(player.getName());

		player.teleport(team.spawn);
		giveItems(player);
	}

	public EngineTeam getColorTeam(Player player) {
		for (EngineTeam team : teams) {
			if (team.team.hasEntry(player.getName())) {
				return team;
			}
		}
		return null;
	}
}
