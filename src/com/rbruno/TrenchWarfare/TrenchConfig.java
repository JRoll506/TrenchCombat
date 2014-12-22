package com.rbruno.TrenchWarfare;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class TrenchConfig {

	private Plugin plugin = Main.getInstance();

	public int minX = plugin.getConfig().getInt("mapBounderies.minX");
	public int maxX = plugin.getConfig().getInt("mapBounderies.maxX");
	public int minZ = plugin.getConfig().getInt("mapBounderies.minZ");
	public int maxZ = plugin.getConfig().getInt("mapBounderies.maxZ");

	public int trenchLocationRed = plugin.getConfig().getInt("trenchLocation.red");
	public int trenchLocationBlue = plugin.getConfig().getInt("trenchLocation.blue");

	Location spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")), plugin.getConfig().getInt("spawn.x"), plugin.getConfig().getInt("spawn.y"), plugin.getConfig().getInt("spawn.z"));
	Location redSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("red.world")), plugin.getConfig().getInt("red.x"), plugin.getConfig().getInt("red.y"), plugin.getConfig().getInt("red.z"), -90F, 0F);
	Location blueSpawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("blue.world")), plugin.getConfig().getInt("blue.x"), plugin.getConfig().getInt("blue.y"), plugin.getConfig().getInt("blue.z"), 90F, 0F);

	int minPlayer = plugin.getConfig().getInt("minPlayer");
	int pregameCountdown = plugin.getConfig().getInt("pregameCountdown");
	int gameClock = plugin.getConfig().getInt("gameClock");

	public int redFlagX = plugin.getConfig().getInt("flag.red.x");
	public int redFlagY = plugin.getConfig().getInt("flag.red.y");
	public int redFlagZ = plugin.getConfig().getInt("flag.red.z");

	public int blueFlagX = plugin.getConfig().getInt("flag.blue.x");
	public int blueFlagY = plugin.getConfig().getInt("flag.blue.y");
	public int blueFlagZ = plugin.getConfig().getInt("flag.blue.z");

	public int fortRed = plugin.getConfig().getInt("fort.red");
	public int fortBlue = plugin.getConfig().getInt("fort.blue");
	
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

}
