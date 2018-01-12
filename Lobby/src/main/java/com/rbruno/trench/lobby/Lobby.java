package com.rbruno.trench.lobby;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.LobbyState;

public class Lobby extends JavaPlugin {

	private static Lobby plugin;

	public static TrenchConfig trenchConfig;
	private static Location spawn;
	private LobbyState lobbyState;
	
	public static ArrayList<Player> parkour = new ArrayList<Player>();

	PluginDescriptionFile pdf = this.getDescription();
	
	public String lobbyName;

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
		new Clock();
	}

	@Override
	public void onDisable() {
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
	}
	

	public static void sendToGame(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		broadcast(System.getenv("TRENCH_NAME") + "-Game");
		out.writeUTF(System.getenv("TRENCH_NAME") + "-Game");

		player.sendPluginMessage(Lobby.getPlugin(), "BungeeCord", out.toByteArray());	
	}

	public static void broadcast(String message) {
		getPlugin().getServer().broadcastMessage(message);
	}
	
	public static Location getSpawn() {
		return spawn;
	}

	public LobbyState getLobbyState() {
		return lobbyState;
	}

	public void setGameState(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	public static Lobby getPlugin() {
		return plugin;
	}


}
