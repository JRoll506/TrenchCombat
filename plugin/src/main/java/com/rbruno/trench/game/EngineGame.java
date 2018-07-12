package com.rbruno.trench.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.rbruno.trench.Main;

public class EngineGame {

	private EngineTeam redTeam;
	private EngineTeam blueTeam;

	public ScoreBoardManager scoreBoardManager = new ScoreBoardManager();

	public Objective objective = scoreBoardManager.registerNewObjective("Score", "dummy");

	public Score[] score = { objective.getScore(ChatColor.BLUE + "Blue"), objective.getScore(ChatColor.RED + "Red") };

	public HashMap<Player, Integer> kills = new HashMap<Player, Integer>();
	public ArrayList<Player> cooldownShotgun = new ArrayList<Player>();
	public ArrayList<Player> cooldownGunner = new ArrayList<Player>();
	public ArrayList<Player> cooldownTeamSwitch = new ArrayList<Player>();
	public ArrayList<Player> cooldownCannon = new ArrayList<Player>();

	private ItemStack redwool = new ItemStack(Material.WOOL, 1, (byte) 14);
	private ItemStack bluewool = new ItemStack(Material.WOOL, 1, (byte) 11);

	private Main main;

	public EngineGame(Main main) {
		this.main = main;
		redTeam = new EngineTeam("Red", false, Color.RED, scoreBoardManager);
		blueTeam = new EngineTeam("Blue", false, Color.BLUE, scoreBoardManager);
		setScoreBoard();
	}

	public void pickTeams() {
		Collection<? extends Player> onlinePlayers = (Bukkit.getOnlinePlayers());
		Set<Player> playerSet = main.getTeamQueue().keySet();
		for (Player player : playerSet) {
			ColorTeam team = main.getTeamQueue().get(player);
			if (team.equals(ColorTeam.BLUE)) {
				if (blueTeam.getPlayers().size() < onlinePlayers.size() / 2) {
					blueTeam.addPlayer(player);
				}
			}
			if (team.equals(ColorTeam.RED)) {
				if (redTeam.getPlayers().size() < onlinePlayers.size() / 2) {
					redTeam.addPlayer(player);
				}
			}
		}
		for (Player player : onlinePlayers) {
			if (!blueTeam.isPlayer(player) && !redTeam.isPlayer(player)) {
				if (blueTeam.getPlayers().size() < redTeam.getPlayers().size()) {
					blueTeam.addPlayer(player);
				} else if (redTeam.getPlayers().size() < blueTeam.getPlayers().size()) {
					redTeam.addPlayer(player);
				} else {
					Random random = new Random();
					switch (random.nextInt(2)) {
					case 1:
						redTeam.addPlayer(player);
						continue;
					case 0:
						blueTeam.addPlayer(player);
						continue;
					}
				}
			}
		}
	}

	public void tpPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (getColorTeam(player) == ColorTeam.RED) {
				player.teleport(main.trenchConfig.getRed());
			} else {
				player.teleport(main.trenchConfig.getBlue());
			}
		}
	}

	public void giveItems() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().clear();
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			if (main.game.getColorTeam(player) == ColorTeam.RED) {
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
		String second = main.clock.getGameClock() % 60 + "";
		if (main.clock.getGameClock() % 60 <= 9)
			second = "0" + second;
		String time = (main.clock.getGameClock() - (main.clock.getGameClock() % 60)) / 60 + ":" + second;
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

	public void giveItems(final Player player) {
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		if (main.game.getColorTeam(player) == ColorTeam.RED) {
			player.getInventory().setHelmet(redwool);
		} else {
			player.getInventory().setHelmet(bluewool);
		}
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
		player.setScoreboard(scoreBoardManager.board);
		if (redTeam.getPlayers().toArray().length == blueTeam.getPlayers().toArray().length) {
			Random random = new Random();
			switch (random.nextInt(2)) {
			case 0:
				player.teleport(main.getMap().getBlueSpawn());
				giveItems(player);
				blueTeam.addPlayer(player);
				return;
			case 1:
				player.teleport(main.getMap().getRedSpawn());
				redTeam.addPlayer(player);
				giveItems(player);
				return;
			}
			player.teleport(main.getMap().getRedSpawn());
			redTeam.addPlayer(player);
			giveItems(player);
		} else if (redTeam.getPlayers().toArray().length < blueTeam.getPlayers().toArray().length) {
			player.teleport(main.getMap().getRedSpawn());
			redTeam.addPlayer(player);
			giveItems(player);
		} else {
			player.teleport(main.getMap().getBlueSpawn());
			giveItems(player);
			blueTeam.addPlayer(player);
		}
	}

	public ColorTeam getColorTeam(Player player) {
		if (redTeam.isPlayer(player))
			return ColorTeam.RED;
		if (blueTeam.isPlayer(player))
			return ColorTeam.BLUE;
		return ColorTeam.NONE;
	}
	
	public EngineTeam getTeam(Player player) {
		if (redTeam.isPlayer(player)) {
			return getRedTeam();
		}
		if (blueTeam.isPlayer(player)){
			return getBlueTeam();
		}
		return null;
	}

	public EngineTeam getRedTeam() {
		return redTeam;
	}

	public EngineTeam getBlueTeam() {
		return blueTeam;
	}
}
