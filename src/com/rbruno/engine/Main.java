package com.rbruno.engine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import com.rbruno.engine.game.Game;
import com.rbruno.engine.listener.ListenerManager;
import com.rbruno.engine.map.EngineMap;
import com.rbruno.engine.timer.Clock;
import com.rbruno.engine.timer.GameState;

public class Main extends JavaPlugin {

	private static Plugin plugin;

	PluginDescriptionFile pdf = this.getDescription();

	private static GameState gameState;

	private static EngineMap map;

	private static Location spawn;

	public static Clock clock;

	ScoreboardManager manager = Bukkit.getScoreboardManager();

	public static Game game;
	public static TrenchConfig trenchConfig;

	@Override
	public void onEnable() {
		plugin = this;
		gameState = GameState.LOBBY;
		trenchConfig = new TrenchConfig();
		map = new EngineMap("Map", trenchConfig.redSpawn, trenchConfig.blueSpawn);
		spawn = trenchConfig.spawn;
		getLogger().info(pdf.getName() + "[" + pdf.getVersion() + "]" + " is enabled");
		getLogger().info(pdf.getName() + " made by " + pdf.getAuthors());
		new ListenerManager();

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

	public static GameState getGameState() {
		return gameState;
	}

	public static Plugin getPlugin() {
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

}
