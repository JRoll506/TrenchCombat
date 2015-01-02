package com.rbruno.TrenchWarfare;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
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

	Player redFlagHolder = null;
	Player blueFlagHolder = null;

	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	Objective objective = board.registerNewObjective("score", "dummy");
	public Score[] score = { objective.getScore(ChatColor.BLUE + "Blue"), objective.getScore(ChatColor.RED + "Red") };
	
	public HashMap<Player, Integer> kills = new HashMap<Player, Integer>();

	public ArrayList<Player> cooldownShotgun = new ArrayList<Player>();

	public ArrayList<Player> cooldownGunner = new ArrayList<Player>();
	public ArrayList<Player> cooldownTeamSwitch = new ArrayList<Player>();
	
    ItemStack redwool = new ItemStack(Material.WOOL, 1, (byte)14);
    ItemStack bluewool = new ItemStack(Material.WOOL, 1, (byte)11);


	@SuppressWarnings("deprecation")
	public void pickTeams() {
		int players = 0;
		Player[] onlinePlayers = (Bukkit.getOnlinePlayers());
		if ((onlinePlayers.length % 2) == 1) {
			players = onlinePlayers.length - 1;
			addRed(onlinePlayers[onlinePlayers.length - 1]);
		}
		if (onlinePlayers.length == 1) return;
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
		Main.messagePlayer(player, "You have joined the " + ChatColor.RED + "Red" + ChatColor.RED + " Team!");

	}

	public void addBlue(Player player) {
		blueTeam.add(player);
		Main.messagePlayer(player, "You have joined the " + ChatColor.BLUE + "Blue" + ChatColor.RED + " Team!");
	}

	public void giveItems(final Player player) {
		//TODO: set up configurable kits
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta chestplateMata = (LeatherArmorMeta) chestplate.getItemMeta();
		if (Main.game.redTeam.contains(player)) {
			player.getInventory().setHelmet(redwool);
			chestplateMata.setColor(Color.fromRGB(184, 0, 0));
		} else {
			player.getInventory().setHelmet(bluewool);
			chestplateMata.setColor(Color.fromRGB(0, 255, 255));
		}
		chestplate.setItemMeta(chestplateMata);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setItem(8, chestplate);
		ItemStack[] gunner = { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW) };
		ItemMeta machineGun=gunner[1].getItemMeta();
		machineGun.setDisplayName("Machine gun");
		gunner[1].setItemMeta(machineGun);
		ItemStack[] scout = { new ItemStack(Material.DIAMOND_SWORD) };
		ItemStack[] shotGun = { new ItemStack(Material.IRON_SWORD),new ItemStack(Material.BONE) };
		ItemMeta shotGunMeta=shotGun[1].getItemMeta();
		shotGunMeta.setDisplayName("Shotgun");
		shotGun[1].setItemMeta(shotGunMeta);
		ItemStack[] brute = { new ItemStack(Material.IRON_SWORD)};
		ItemMeta bruteMeta=brute[0].getItemMeta();
		bruteMeta.setDisplayName(ChatColor.BOLD+"Right Click to activate sheild");
		brute[0].setItemMeta(bruteMeta);
		if (!(Main.classMap.containsKey(player))) {
			Main.classMap.put(player, "Gunner");
		}
		if (Main.classMap.get(player).equals("Gunner")) {
			player.getInventory().addItem(gunner);
		}
		if (Main.classMap.get(player).equals("Scout")) {
			player.getInventory().addItem(scout);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
				@Override
				public void run() {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.tick*20, 0));
				}
			}, 5L);

		}
		if (Main.classMap.get(player).equals("Shotgun")) {
			player.getInventory().addItem(shotGun);
		}
		if (Main.classMap.get(player).equals("Brute")) {
			player.getInventory().addItem(brute);
		}
		ItemStack smoke=new ItemStack(Material.SULPHUR, 1);
		ItemMeta smokeMeta=smoke.getItemMeta();
		smokeMeta.setDisplayName("Smoke Bomb!");
		smoke.setItemMeta(smokeMeta);
		player.getInventory().addItem(smoke);
	}

	public void giveItems(Player[] players) {
		//TODO: set up configurable kits

		ItemStack[] gunner = { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW) };
		ItemMeta machineGun=gunner[1].getItemMeta();
		machineGun.setDisplayName("Machine gun");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Click to fire");
		machineGun.setLore(lore);
		gunner[1].setItemMeta(machineGun);
		ItemStack[] scout = { new ItemStack(Material.DIAMOND_SWORD) };
		ItemStack[] shotGun = { new ItemStack(Material.IRON_SWORD),new ItemStack(Material.BONE) };
		ItemMeta shotGunMeta=shotGun[1].getItemMeta();
		shotGunMeta.setDisplayName("Shotgun");
		shotGunMeta.setLore(lore);
		shotGun[1].setItemMeta(shotGunMeta);
		for (int i = 0; i < players.length; i++) {
			players[i].getInventory().setArmorContents(null);
			players[i].getInventory().clear();
			players[i].removePotionEffect(PotionEffectType.JUMP);
			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta chestplateMata = (LeatherArmorMeta) chestplate.getItemMeta();
			if (Main.game.redTeam.contains(players[i])) {
				chestplateMata.setColor(Color.fromRGB(184, 0, 0));
				players[i].getInventory().setHelmet(redwool);

			} else {
				players[i].getInventory().setHelmet(bluewool);
				chestplateMata.setColor(Color.fromRGB(0, 255, 255));
			}
			chestplate.setItemMeta(chestplateMata);
			players[i].getInventory().setChestplate(chestplate);
			players[i].getInventory().setItem(8, chestplate);
			if (!(Main.classMap.containsKey(players[i]))) {
				Main.classMap.put(players[i], "Gunner");
			}
			if (Main.classMap.get(players[i]).equals("Gunner")) {
				players[i].getInventory().addItem(gunner);
			}
			if (Main.classMap.get(players[i]).equals("Scout")) {
				players[i].getInventory().addItem(scout);
				players[i].addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.trenchConfig.getGameClock() * 60 * 20, 0));
			}
			if (Main.classMap.get(players[i]).equals("Shotgun")) {
				players[i].getInventory().addItem(shotGun);
			}
			ItemStack smoke=new ItemStack(Material.SULPHUR, 1);
			ItemMeta smokeMeta=smoke.getItemMeta();
			smokeMeta.setDisplayName("Smoke Bomb!");
			smoke.setItemMeta(smokeMeta);
			players[i].getInventory().addItem(smoke);
		}
	}

	public void tpPlayers() {
		Player[] redTeamArray = redTeam.toArray(new Player[redTeam.size()]);
		Player[] blueTeamArray = blueTeam.toArray(new Player[blueTeam.size()]);
		for (int i = 0; i < redTeamArray.length; i++) {
			redTeamArray[i].setFallDistance(0F);
			redTeamArray[i].teleport(redSpawn);
		}
		if (!(blueTeamArray.length == 0)) {
			for (int i = 0; i < blueTeamArray.length; i++) {
				blueTeamArray[i].setFallDistance(0F);
				blueTeamArray[i].teleport(blueSpawn);
			}
		}
	}

	public void addPlayer(Player player) {
		kills.put(player, 0);
		score[0].setScore(blueScore);
		score[1].setScore(redScore);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String second = Main.tick % 60 + "";
		if (Main.tick % 60 <= 9) second = "0" + second;
		String time = (Main.tick - (Main.tick % 60)) / 60 + ":" + second;
		objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);
		player.setScoreboard(board);

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

	@SuppressWarnings("deprecation")
	public void setScoreBoard() {
		score[0].setScore(blueScore);
		score[1].setScore(redScore);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		String second = Main.tick % 60 + "";
		if (Main.tick % 60 <= 9) second = "0" + second;
		String time = (Main.tick - (Main.tick % 60)) / 60 + ":" + second;
		objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);

		Player[] onlinePlayers = (Bukkit.getOnlinePlayers());
		for (int i = 0; i < onlinePlayers.length; i++) {
			onlinePlayers[i].setScoreboard(board);
		}

	}
}
