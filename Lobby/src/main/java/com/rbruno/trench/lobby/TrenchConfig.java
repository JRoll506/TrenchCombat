package com.rbruno.trench.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class TrenchConfig {

	private Plugin plugin = Lobby.getPlugin();

	Location spawn = new Location(Bukkit.getServer().getWorld(plugin.getConfig().getString("spawn.world")), plugin.getConfig().getInt("spawn.x"), plugin.getConfig().getInt("spawn.y"), plugin.getConfig().getInt("spawn.z"));

	int minPlayer = plugin.getConfig().getInt("minPlayer");
	int pregameCountdown = plugin.getConfig().getInt("pregameCountdown");
	
	public Location getSpawn() {
		return spawn;
	}

	public int getMinPlayer() {
		return minPlayer;
	}

	public int getPregameCountdown() {
		return pregameCountdown;
	}

}
