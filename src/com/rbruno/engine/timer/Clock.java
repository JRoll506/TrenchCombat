package com.rbruno.engine.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.game.Game;
import com.rbruno.engine.game.GameType;

public class Clock {

	private int pregameClock = Main.trenchConfig.getPregameCountdown();
	private int gameClock = Main.trenchConfig.getGameClock();

	private boolean pregameClockIsRunning = false;

	private Player redFlagHolder;
	private Player blueFlagHolder;

	public Clock() {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				tick();
			}
		}, 0L, 20L);
	}

	@SuppressWarnings("deprecation")
	private void tick() {
		if (Main.getGameState() == GameState.LOBBY) {
			if(pregameClock % 10 ==0 && pregameClockIsRunning) Main.broadcast(pregameClock + " second(s) till the game starts!");
			if (pregameClock == 0) startGame();//start game
			if (!pregameClockIsRunning && Bukkit.getOnlinePlayers().length >= Main.trenchConfig.getMinPlayer()) pregameClockIsRunning = true;
			if (pregameClockIsRunning) pregameClock--;
		} else if (Main.getGameState() == GameState.IN_GAME) {
			spawnFirework();
			if (gameClock <= 0) endGame();//end game & rebuild map
			if (gameClock > 0) gameClock--;
			String second = Main.clock.getGameClock() % 60 + "";
			if (Main.clock.getGameClock() % 60 <= 9) second = "0" + second;
			String time = (Main.clock.getGameClock() - (Main.clock.getGameClock() % 60)) / 60 + ":" + second;
			Main.getGame().objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);

		}
	}

	public void startGame() {
		pregameClockIsRunning = false;
		pregameClock = Main.getPlugin().getConfig().getInt("pregameClock");
		Main.setGameState(GameState.IN_GAME);
		Main.game = new Game(GameType.DEATHMATCH);
		Main.getGame().pickTeams();
		Main.getGame().giveItems();
		
	}

	public void endGame() {
		Main.game = null;
		pregameClock = Main.getPlugin().getConfig().getInt("pregameClock");
		Main.setGameState(GameState.LOBBY);
	}
	
	private void spawnFirework(){
		if (!(getFlagHolder(ColorTeam.RED) == null)) {
			Firework firework = (Firework) getFlagHolder(ColorTeam.RED).getWorld().spawnEntity(getFlagHolder(ColorTeam.RED).getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);

		}
		if (!(getFlagHolder(ColorTeam.BLUE) == null)) {
			Firework firework = (Firework) getFlagHolder(ColorTeam.BLUE).getWorld().spawnEntity(getFlagHolder(ColorTeam.BLUE).getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.BLUE).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);

		}
	}

	public Player getFlagHolder(ColorTeam team) {
		switch (team) {
		case RED:
			return redFlagHolder;
		case BLUE:
			return blueFlagHolder;
		default:
			return null;
		}
	}
	
	public int getGameClock(){
		return gameClock;
	}

}
