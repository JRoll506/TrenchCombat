package com.rbruno.trench;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.game.Game;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.map.EngineMap;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.GameState;

public class Main extends JavaPlugin {

	private static Main plugin;

	PluginDescriptionFile pdf = this.getDescription();

	private static GameState gameState;

	private static EngineMap map;

	private static Location spawn;

	public static Clock clock;

	ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	public static ClassManager classManager;
	
	private HashMap<Player, ColorTeam> teamQue = new HashMap<Player, ColorTeam>();

	public static Game game;
	public static TrenchConfig trenchConfig;
	
	public static ArrayList<Player> parkour = new ArrayList<Player>();

	@Override
	public void onEnable() {
		plugin = this;
		gameState = GameState.LOBBY;
		getConfig().options().copyDefaults(true);
	    saveConfig();
		trenchConfig = new TrenchConfig();
		map = new EngineMap("Map", trenchConfig.redSpawn, trenchConfig.blueSpawn);
		spawn = trenchConfig.spawn;
		getLogger().info(pdf.getName() + "[" + pdf.getVersion() + "]" + " is enabled");
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
		new ListenerManager();
		classManager = new ClassManager();
		clock = new Clock();
	}

	@Override
	public void onDisable() {
		getLogger().info(pdf.getName() + "[" + pdf.getVersion() + "]" + " is Disabled");
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
	}

	public static void broadcast(String message) {
		Main.getPlugin().getServer().broadcastMessage(message);
	}

	public static ClassManager getClassManager() {
		return classManager;
	}

	public static GameState getGameState() {
		return gameState;
	}

	public static Main getPlugin() {
		return plugin;
	}

	public static EngineMap getMap() {
		return map;
	}

	public static Game getGame() {
		return game;
	}

	public static Location getSpawn() {
		return spawn;
	}

	public static void setGameState(GameState gameState) {
		Main.gameState = gameState;
	}

	public HashMap<Player, ColorTeam> getTeamQue() {
		return teamQue;
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

}
