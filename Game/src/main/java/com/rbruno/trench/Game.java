package com.rbruno.trench;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.game.EngineGame;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.map.EngineMap;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.GameState;

public class Game extends JavaPlugin {

	private static Game plugin;

	public static TrenchConfig trenchConfig;
	private GameState gameState = GameState.PLAYING;
	public static ClassManager classManager;
	public static EngineGame game;
	public static Clock clock;
	private static EngineMap map;

	private HashMap<Player, ColorTeam> teamQueue = new HashMap<Player, ColorTeam>();

	PluginDescriptionFile pdf = this.getDescription();

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getConfig().options().copyDefaults(true);
	    saveConfig();
		trenchConfig = new TrenchConfig();
		map = new EngineMap("Map", trenchConfig.redSpawn, trenchConfig.blueSpawn);
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

	public static ClassManager getClassManager() {
		return classManager;
	}

	public GameState getGameState() {
		return gameState;
	}
	

	public static EngineGame getGame() {
		return game;
	}

	public HashMap<Player, ColorTeam> getTeamQueue() {
		return teamQueue;
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

	public static EngineMap getMap() {
		return map;
	}

	public static Game getPlugin() {
		return plugin;
	}
}
