package com.rbruno.trench;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.game.EngineGame;
import com.rbruno.trench.listener.ListenerManager;
import com.rbruno.trench.map.EngineMap;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.GameState;

public class Main extends JavaPlugin {

	private GameState gameState;

	private EngineMap map;

	private Location spawn;

	public Clock clock;

	ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	public ClassManager classManager;
	
	private HashMap<Player, ColorTeam> teamQueue = new HashMap<Player, ColorTeam>();

	public EngineGame game;
	public TrenchConfig trenchConfig;
	
	public ArrayList<Player> parkour = new ArrayList<Player>();

	@Override
	public void onEnable() {
		gameState = GameState.WAITING;
		getConfig().options().copyDefaults(true);
	    saveConfig();
		trenchConfig = new TrenchConfig(this);
		map = new EngineMap("Map", trenchConfig.redSpawn, trenchConfig.blueSpawn, this);
		spawn = trenchConfig.spawn;
		getLogger().info(this.getDescription().getName() + " made by " + this.getDescription().getAuthors());
		new ListenerManager(this);
		classManager = new ClassManager(this);
		clock = new Clock(this);
	}

	@Override
	public void onDisable() {
		getLogger().info(this.getDescription().getName() + " made by " + this.getDescription().getAuthors());
	}
	
	public ClassManager getClassManager() {
		return classManager;
	}

	public GameState getGameState() {
		return gameState;
	}

	public EngineMap getMap() {
		return map;
	}

	public EngineGame getGame() {
		return game;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public HashMap<Player, ColorTeam> getTeamQueue() {
		return teamQueue;
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
