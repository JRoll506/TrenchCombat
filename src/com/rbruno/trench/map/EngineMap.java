package com.rbruno.trench.map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.rbruno.trench.Main;

public class EngineMap {
	private String name;

	private Location redSpawn;
	private Location blueSpawn;

	public EngineMap(String name, Location redSpawn, Location blueSpawn) {
		this.name = name;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;
	}
	
	public void spawnPlayers(){
		for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) 
			repawnPlayer(player);
	}

	public void repawnPlayer(Player player){
		switch (Main.getGame().getColorTeam(player)) {
		case RED:
			player.teleport(redSpawn);
		case BLUE:
			player.teleport(blueSpawn);
		default:
			break;
		}
	}

	public String getName() {
		return name;
	}

	public Location getRedSpawn() {
		return redSpawn;
	}

	public Location getBlueSpawn() {
		return blueSpawn;
	}
}
