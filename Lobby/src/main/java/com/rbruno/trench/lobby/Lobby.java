package com.rbruno.trench.lobby;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.LobbyState;

public class Lobby extends JavaPlugin {

	private static Lobby plugin;

	public static TrenchConfig trenchConfig;
	private static Location spawn;
	private LobbyState lobbyState;
	public static ClassManager classManager;
	
	private HashMap<Player, ColorTeam> teamQueue = new HashMap<Player, ColorTeam>();

	
	public static ArrayList<Player> parkour = new ArrayList<Player>();

	PluginDescriptionFile pdf = this.getDescription();

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getConfig().options().copyDefaults(true);
		saveConfig();
		trenchConfig = new TrenchConfig();
		spawn = trenchConfig.spawn;
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
		new ListenerManager();
		classManager = new ClassManager();
		new Clock();
	}

	@Override
	public void onDisable() {
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
	}

	public static void broadcast(String message) {
		getPlugin().getServer().broadcastMessage(message);
	}
	
	public static ClassManager getClassManager() {
		return classManager;
	}
	
	public static Location getSpawn() {
		return spawn;
	}

	public LobbyState getLobbyState() {
		return lobbyState;
	}
	
	public HashMap<Player, ColorTeam> getTeamQueue() {
		return teamQueue;
	}

	public void setGameState(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	public static Lobby getPlugin() {
		return plugin;
	}

}
