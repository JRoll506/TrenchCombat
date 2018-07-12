package com.rbruno.trench;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class TrenchConfig {

	private Plugin plugin;

	public int minX;
	public int maxX;
	public int minZ;
	public int maxZ;

	public int trenchLocationRed;
	public int trenchLocationBlue;

	Location spawn;
	Location redSpawn;
	Location blueSpawn;

	int minPlayer;
	int pregameCountdown;
	int gameClock;

	public Location redFlag;
	public Location blueFlag;

	public int fortRed;
	public int fortBlue;

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

	public int getGameClock() {
		return gameClock;
	}

	public TrenchConfig(Main main) {
		this.plugin = main;
		minX = plugin.getConfig().getInt("mapBounderies.minX");
		maxX = plugin.getConfig().getInt("mapBounderies.maxX");
		minZ = plugin.getConfig().getInt("mapBounderies.minZ");
		maxZ = plugin.getConfig().getInt("mapBounderies.maxZ");

		trenchLocationRed = plugin.getConfig().getInt("trenchLocation.red");
		trenchLocationBlue = plugin.getConfig().getInt("trenchLocation.blue");

		spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")),
				plugin.getConfig().getInt("spawn.x"), plugin.getConfig().getInt("spawn.y"),
				plugin.getConfig().getInt("spawn.z"));
		redSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("red.world")),
				plugin.getConfig().getInt("red.x"), plugin.getConfig().getInt("red.y"),
				plugin.getConfig().getInt("red.z"), -90F, 0F);
		blueSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("blue.world")),
				plugin.getConfig().getInt("blue.x"), plugin.getConfig().getInt("blue.y"),
				plugin.getConfig().getInt("blue.z"), 90F, 0F);

		minPlayer = plugin.getConfig().getInt("minPlayer");
		pregameCountdown = plugin.getConfig().getInt("pregameCountdown");
		gameClock = plugin.getConfig().getInt("gameClock");

		redFlag = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")),
				plugin.getConfig().getInt("flag.red.x"), plugin.getConfig().getInt("flag.red.y"),
				plugin.getConfig().getInt("flag.red.z"));

		blueFlag = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")),
				plugin.getConfig().getInt("flag.blue.x"), plugin.getConfig().getInt("flag.blue.y"),
				plugin.getConfig().getInt("flag.blue.z"));

		fortRed = plugin.getConfig().getInt("fort.red");
		fortBlue = plugin.getConfig().getInt("fort.blue");
	}
}
