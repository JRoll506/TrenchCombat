package com.rbruno.TrenchWarfare;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class TrenchConfig {
	
	private Plugin plugin = Main.getInstance();
	
	Location spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")), plugin.getConfig().getInt("spawn.x"), plugin.getConfig().getInt("spawn.y"), plugin.getConfig().getInt("spawn.z"));
	Location redSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("red.world")), plugin.getConfig().getInt("red.x"), plugin.getConfig().getInt("red.y"), plugin.getConfig().getInt("red.z"));
	Location blueSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("blue.world")), plugin.getConfig().getInt("blue.x"), plugin.getConfig().getInt("blue.y"), plugin.getConfig().getInt("blue.z"));
	
	int minPlayer = plugin.getConfig().getInt("minPlayer");
	int pregameCountdown = plugin.getConfig().getInt("pregameCountdown");


	public Location getSpawn() {
		return spawn;
	}
	public Location getRed() {
		return redSpawn;
	}
	public Location getBlue() {
		return blueSpawn;
	}
	public int getMinPlayer() {
		return minPlayer;
	}
	public int getPregameCountdown() {
		return pregameCountdown;
	}

}
