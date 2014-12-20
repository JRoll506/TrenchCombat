package com.rbruno.TrenchWarfare;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Game {
	public ArrayList<Player> cooldown = new ArrayList<Player>();

	ArrayList<Player> redTeam = new ArrayList<Player>();
	ArrayList<Player> blueTeam = new ArrayList<Player>();

	public int redScore = 0;
	public int blueScore = 0;

	Location redSpawn = Main.trenchConfig.getRed();
	Location blueSpawn = Main.trenchConfig.getBlue();

	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Objective objective = board.registerNewObjective("score", "dummy");
	public Score[] score = { 
			objective.getScore(ChatColor.BLUE + "Blue"), 
			objective.getScore(ChatColor.RED + "Red"), 
		};

	public void pickTeams() {
		int players = 0;
		Player[] onlinePlayers = (Bukkit.getOnlinePlayers());
		if ((onlinePlayers.length % 2) == 1) {
			players = onlinePlayers.length - 1;
			addRed(onlinePlayers[onlinePlayers.length - 1]);
		}
		players = onlinePlayers.length;
		for (int i = 0; i < players; i++) {
			if (i >= players / 2) {
				addRed(onlinePlayers[i]);

			} else {
				addBlue(onlinePlayers[i]);
			}
		}
	}

	public void addRed(Player player) {
		redTeam.add(player);
		Main.messagePlayer(player, "You have joined the " + ChatColor.RED + "red" + ChatColor.RED + " Team!");

	}

	public void addBlue(Player player) {
		blueTeam.add(player);
		Main.messagePlayer(player, "You have joined the " + ChatColor.BLUE + "blue" + ChatColor.RED + " Team!");
	}

	public void giveItems(Player player) {
		//TODO: set up configurable kits
		ItemStack[] kit = { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW), new ItemStack(Material.COOKED_BEEF) };
		kit[2].setAmount(64);
		player.getInventory().clear();
		player.getInventory().addItem(kit);
		
	}

	public void giveItems(Player[] players) {
		//TODO: set up configurable kits
		ItemStack[] kit = { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW), new ItemStack(Material.COOKED_BEEF) };
		kit[2].setAmount(64);
		for (int i = 0; i < players.length; i++) {
			players[i].getInventory().clear();
			players[i].getInventory().addItem(kit);
		}
	}

	public void tpPlayers() {
		Player[] redTeamArray = redTeam.toArray(new Player[redTeam.size()]);
		Player[] blueTeamArray = blueTeam.toArray(new Player[blueTeam.size()]);
		for (int i = 0; i < redTeamArray.length; i++) {
			redTeamArray[i].teleport(redSpawn);
		}
		if (!(blueTeamArray.length == 0)) {
			for (int i = 0; i < blueTeamArray.length; i++) {
				blueTeamArray[i].teleport(blueSpawn);
			}
		}
	}

	public void addPlayer(Player player) {
		Player[] redTeamArray = redTeam.toArray(new Player[redTeam.size()]);
		Player[] blueTeamArray = blueTeam.toArray(new Player[blueTeam.size()]);
		if (redTeamArray.length == blueTeamArray.length) {
			player.teleport(redSpawn);
			addRed(player);
			giveItems(player);
		} else if (redTeamArray.length < blueTeamArray.length) {
			player.teleport(redSpawn);
			addRed(player);
			giveItems(player);
		} else if (redTeamArray.length > blueTeamArray.length) {
			player.teleport(blueSpawn);
			giveItems(player);
			addBlue(player);
		}
	}

	public void setScoreBoard() {
		score[0].setScore(blueScore);
		score[1].setScore(redScore);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String time = Main.tick - Main.tick%60 + ":" + Main.tick%60;
		objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);

		Player[] onlinePlayers = (Bukkit.getOnlinePlayers());
		for (int i = 0; i < onlinePlayers.length; i++) {
			onlinePlayers[i].setScoreboard(board);
		}

	}
}
