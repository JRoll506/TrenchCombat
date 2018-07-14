package com.rbruno.trench.game;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.rbruno.trench.Main;

public class EngineGame {
	
	public EngineTeam[] teams;

	public Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

	public Objective objective = board.registerNewObjective("Score", "dummy");
	public Score[] score = { objective.getScore(ChatColor.BLUE + "Blue"), objective.getScore(ChatColor.RED + "Red") };

	public Stats stats;

	private ItemStack redwool = new ItemStack(Material.WOOL, 1, (byte) 14);
	private ItemStack bluewool = new ItemStack(Material.WOOL, 1, (byte) 11);

	private Main main;

	public EngineGame(Main main) {
		this.main = main;

		teams = new EngineTeam[2];
		teams[0] = new EngineTeam("Red", ChatColor.RED, board, objective);
		teams[1] = new EngineTeam("Blue", ChatColor.BLUE, board, objective);

		/* TODO */
		stats = new Stats();

		setScoreBoard();
	}

	public void pickTeams() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		int i = 0;
		for (Player player : players) {
			if ((i % 2) == 0)
				blueTeam.team.addEntry(player.getName());
			else
				redTeam.team.addEntry(player.getName());
			i++;
		}
	}

	public void tpPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setFallDistance(0);
			player.teleport(getColorTeam(player).team.getName().equals("Red") ? main.trenchConfig.getRed() : main.trenchConfig.getBlue());
		}
	}

	public void giveItems() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().clear();
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			if (main.game.getColorTeam(player).team.getName().equals("Red")) {
				player.getInventory().setHelmet(redwool);
			} else {
				player.getInventory().setHelmet(bluewool);
			}
			if (main.getClassManager().getClass(player) == null) {
				player.getInventory().addItem(main.getClassManager().getEngineClass("Gunner").getItems());
				player.addPotionEffect(main.getClassManager().getEngineClass("Gunner").getEffect());
				continue;
			}
			player.getInventory().addItem(main.getClassManager().getEngineClass(main.getClassManager().getClass(player)).getItems());
			player.addPotionEffect(main.getClassManager().getEngineClass(main.getClassManager().getClass(player)).getEffect());
		}
	}

	public void setScoreBoard() {
		score[0].setScore(0);
		score[1].setScore(0);

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

		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, main.game.getColorTeam(player).team.getName().equals("Red") ? (byte) 14 : (byte) 11));

		if (main.getClassManager().getClass(player) == null) {
			player.getInventory().addItem(main.getClassManager().getEngineClass("Gunner").getItems());
			player.addPotionEffect(main.getClassManager().getEngineClass("Gunner").getEffect());
			return;
		}
		player.getInventory().addItem(main.getClassManager().getEngineClass(main.getClassManager().getClass(player)).getItems());
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				player.addPotionEffect(main.getClassManager().getEngineClass(main.getClassManager().getClass(player)).getEffect());
			}
		}, 20);
	}

	public void addPlayer(Player player) {
		player.setScoreboard(board);

		
		EngineTeam team = teams[(int) Math.round(Math.random() * teams.length)];
		team.team.addEntry(player.getName());
		
		player.teleport(main.trenchConfig.blueSpawn);
		giveItems(player);
	}

	public EngineTeam getColorTeam(Player player) {
		for (EngineTeam team : teams) {
			if (board.getEntryTeam(player.getName()) == team.team) {
				return team;
			}
		}
		return null;
	}
}
