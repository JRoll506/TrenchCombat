package com.rbruno.trench;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rbruno.trench.classes.ClassManager;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.game.EngineGame;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.Clock;
import com.rbruno.trench.timer.GameState;

public class Main extends JavaPlugin {

	private GameState gameState = GameState.WAITING;

	public Clock clock;
	public ClassManager classManager;
	public EngineGame game;
	public TrenchConfig trenchConfig;
	
	private HashMap<Player, ColorTeam> teamQueue = new HashMap<Player, ColorTeam>();	

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
	    saveConfig();
	    
		trenchConfig = new TrenchConfig(this);
		classManager = new ClassManager(this);
		clock = new Clock(this);
		
		EngineListner.register(this);
		
		getLogger().info(this.getDescription().getName() + " made by " + this.getDescription().getAuthors());
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

	public EngineGame getGame() {
		return game;
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
