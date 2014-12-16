package com.rbruno.TrenchWarfare;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Game {
	public ArrayList<Player> cooldown = new ArrayList<Player>();

	ArrayList<Player> redTeam = new ArrayList<Player>();
	ArrayList<Player> blueTeam = new ArrayList<Player>();

	public int redScore;
	public int blueScore;
	
	Location redSpawn = Main.trenchConfig.getRed();
	Location blueSpawn = Main.trenchConfig.getBlue();

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
		ItemStack lhelmet = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
		lam.setColor(Color.fromRGB(184, 0, 0));
		lhelmet.setItemMeta(lam);
		player.getInventory().setChestplate(lhelmet);
		redTeam.add(player);
		Main.messagePlayer(player, "You have joined the " + ChatColor.RED + "red" + ChatColor.RED + " Team!");

	}

	public void addBlue(Player player) {
		ItemStack lhelmet = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
		lam.setColor(Color.fromRGB(0, 255, 255));
		lhelmet.setItemMeta(lam);
		player.getInventory().setChestplate(lhelmet);
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
}
