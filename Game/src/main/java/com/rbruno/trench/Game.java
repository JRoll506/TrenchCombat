package com.rbruno.trench;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.EngineGame;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.map.EngineMap;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.GameState;

public class Game extends JavaPlugin {

	private static Game plugin;

	public static TrenchConfig trenchConfig;
	private GameState gameState = GameState.LOADING;
	public static ClassManager classManager;
	public static EngineGame game;
	public static Clock clock;
	private static EngineMap map;
	private static Location spawn;

	PluginDescriptionFile pdf = this.getDescription();

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getConfig().options().copyDefaults(true);
	    saveConfig();
		trenchConfig = new TrenchConfig();
		map = new EngineMap("Map", trenchConfig.redSpawn, trenchConfig.blueSpawn);
		spawn = trenchConfig.spawn;
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
		new ListenerManager();
		classManager = new ClassManager();
		clock = new Clock();
	}

	@Override
	public void onDisable() {
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
	}

	public static void broadcast(String message) {
		getPlugin().getServer().broadcastMessage(message);
	}
	
	public static void sendToLobby(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(System.getenv("TRENCH_NAME") + "-Lobby");

		player.sendPluginMessage(Game.getPlugin(), "BungeeCord", out.toByteArray());	
	}

	public static ClassManager getClassManager() {
		return classManager;
	}

	public GameState getGameState() {
		return gameState;
	}
	

	public static EngineGame getGame() {
		return game;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public ItemStack getGrenade() {
		ItemStack granade = new ItemStack(Material.SLIME_BALL);
		granade.setAmount(3);
		ItemMeta meta = granade.getItemMeta();
		meta.setDisplayName("Granade (Right Click)");
		granade.setItemMeta(meta);
		return granade;
	}

	public ItemStack getSmoke() {
		ItemStack smoke = new ItemStack(Material.SULPHUR);
		smoke.setAmount(1);
		ItemMeta meta = smoke.getItemMeta();
		meta.setDisplayName("Smoke (Right Click)");
		smoke.setItemMeta(meta);
		return smoke;
	}

	public static Location getSpawn() {
		return spawn;
	}

	public static EngineMap getMap() {
		return map;
	}

	public static Game getPlugin() {
		return plugin;
	}
}
