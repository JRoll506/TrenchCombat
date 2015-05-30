package com.rbruno.engine.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.rbruno.engine.Main;

public class Game {

	private final GameType GAME_TYPE;

	EngineTeam redTeam;
	EngineTeam blueTeam;

	public ScoreBoardManager scoreBoardManager = new ScoreBoardManager();

	public Objective objective = scoreBoardManager.registerNewObjective("Score", "dummy");

	public Score[] score = { objective.getScore(ChatColor.BLUE + "Blue"), objective.getScore(ChatColor.RED + "Red") };

	public HashMap<Player, Integer> kills = new HashMap<Player, Integer>();
	public ArrayList<Player> cooldownShotgun = new ArrayList<Player>();
	public ArrayList<Player> cooldownGunner = new ArrayList<Player>();
	public ArrayList<Player> cooldownTeamSwitch = new ArrayList<Player>();
	public ArrayList<Player> cooldown = new ArrayList<Player>();

	public Game(GameType gameType) {
		this.GAME_TYPE = gameType;
		redTeam = new EngineTeam("Red", false, Color.RED, scoreBoardManager);
		blueTeam = new EngineTeam("Blue", false, Color.BLUE, scoreBoardManager);
		setScoreBoard();
	}

	public void pickTeams() {
		int players = 0;
		Collection<? extends Player> onlinePlayers = (Bukkit.getOnlinePlayers());
		Player[] onlinePlayersConverted = new Player[onlinePlayers.size()];
		int ia = 0;
		for (Player player : onlinePlayers) {
			onlinePlayersConverted[ia] = player;
			ia++;
		}
		if ((onlinePlayersConverted.length % 2) == 1) {
			players = onlinePlayersConverted.length - 1;
			redTeam.addPlayer(onlinePlayersConverted[onlinePlayers.size() - 1]);
		}
		if (onlinePlayersConverted.length == 1) return;
		players = onlinePlayersConverted.length;
		for (int i = 0; i < players; i++) {
			if (i >= players / 2) {
				redTeam.addPlayer(onlinePlayersConverted[i]);

			} else {
				blueTeam.addPlayer(onlinePlayersConverted[i]);
			}
		}
	}

	public void tpPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (getColorTeam(player) == ColorTeam.RED) {
				player.teleport(Main.trenchConfig.getRed());
			} else {
				player.teleport(Main.trenchConfig.getBlue());
			}
		}
	}

	public void giveItems() {
		for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
			player.getInventory().clear();
			if (Main.getClassManager().getClass(player) == null) {
				player.getInventory().addItem(Main.getClassManager().getEngineClass("Gunner").getItems());
				continue;
			}
			player.getInventory().addItem(Main.getClassManager().getEngineClass(Main.getClassManager().getClass(player)).getItems());
		}
	}

	public void setScoreBoard() {
		score[0].setScore(0);
		score[1].setScore(0);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String second = Main.clock.getGameClock() % 60 + "";
		if (Main.clock.getGameClock() % 60 <= 9) second = "0" + second;
		String time = (Main.clock.getGameClock() - (Main.clock.getGameClock() % 60)) / 60 + ":" + second;
		objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);

		Collection<? extends Player> onlinePlayers = (Bukkit.getOnlinePlayers());
		Player[] onlinePlayersConverted = new Player[onlinePlayers.size()];
		int ia = 0;
		for (Player player : onlinePlayers) {
			onlinePlayersConverted[ia] = player;
			ia++;
		}
		for (int i = 0; i < onlinePlayersConverted.length; i++) {
			onlinePlayersConverted[i].setScoreboard(scoreBoardManager.board);
		}

	}

	public void giveItems(Player player) {
		player.getInventory().clear();
		if (Main.getClassManager().getClass(player) == null) {
			player.getInventory().addItem(Main.getClassManager().getEngineClass("Gunner").getItems());
			return;
		}
		player.getInventory().addItem(Main.getClassManager().getEngineClass(Main.getClassManager().getClass(player)).getItems());
	}

	public void addPlayer(Player player) {
		player.setScoreboard(scoreBoardManager.board);
		if (redTeam.getPlayers().toArray().length == blueTeam.getPlayers().toArray().length) {
			player.teleport(Main.getMap().getRedSpawn());
			redTeam.addPlayer(player);
			giveItems(player);
		} else if (redTeam.getPlayers().toArray().length < blueTeam.getPlayers().toArray().length) {
			player.teleport(Main.getMap().getRedSpawn());
			redTeam.addPlayer(player);
			giveItems(player);
		} else {
			player.teleport(Main.getMap().getBlueSpawn());
			giveItems(player);
			blueTeam.addPlayer(player);
		}
	}

	public ColorTeam getColorTeam(Player player) {
		if (redTeam.isPlayer(player)) return ColorTeam.RED;
		if (blueTeam.isPlayer(player)) return ColorTeam.BLUE;
		return ColorTeam.NONE;
	}

	public GameType getGameType() {
		return GAME_TYPE;
	}

	public EngineTeam getRedTeam() {
		return redTeam;
	}

	public EngineTeam getBlueTeam() {
		return blueTeam;
	}
}
