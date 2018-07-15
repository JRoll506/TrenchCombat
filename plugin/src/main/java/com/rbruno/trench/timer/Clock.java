package com.rbruno.trench.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.EngineGame;
import com.rbruno.trench.game.EngineTeam;
import com.rbruno.trench.listener.listeners.SmokeListener;

public class Clock {
	
	private Main main;

	private int pregameClock;
	private int gameClock;

	public Clock(Main main) {
		this.main = main;
		
		pregameClock = main.getConfig().getInt("pregameCountdown");
		gameClock = main.getConfig().getInt("gameClock") * 60;
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				tick();
			}
		}, 0L, 20L);
		scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				SmokeListener.handleSmoke();
			}
		}, 0L, 5L);
	}

	private void tick() {
		if (main.getGameState() == GameState.WAITING) {
			/* Start Counting if minPlayers is reached */
			if (Bukkit.getOnlinePlayers().size() >= main.getConfig().getInt("minPlayer")) main.setGameState(GameState.COUNTING);
		} else if (main.getGameState() == GameState.COUNTING) {
			/* Broadcast Time */
			if (pregameClock % 10 == 0 && !(pregameClock == 0)) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			if (pregameClock <= 5 && pregameClock != 0) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			/* Decrement Time */
			pregameClock--;
			/* Start Game */
			if (pregameClock == 0) startGame();
		} else if (main.getGameState() == GameState.IN_GAME) {
			spawnFirework();
			/* Decrement Time */
			if (gameClock > 0) gameClock--;
			/* Update scoreboard and broadcast time */
			String second = gameClock % 60 + "";
			if (gameClock % 60 <= 9) second = "0" + second;
			String time = (gameClock - (gameClock % 60)) / 60 + ":" + second;
			if (main.getGame() != null) main.getGame().objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);
			if (gameClock % 60 == 0 && gameClock != 0) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + (gameClock / 60) + ChatColor.WHITE + " minutes left in game!");
			/* End game */
			if (gameClock <= 0) endGame();
		}
	}

	public void startGame() {		
		pregameClock = main.getConfig().getInt("pregameCountdown");
		
		main.game = new EngineGame(main);
		main.getGame().pickTeams();
		main.getGame().tpPlayers();
		main.getGame().giveItems();
		
		Bukkit.getServer().broadcastMessage("The war has begun!");
		main.setGameState(GameState.IN_GAME);
	}

	public void endGame() {
		gameClock = main.getConfig().getInt("gameClock") * 60;
		EngineTeam team = null;
		
		for (EngineTeam targetTeam : main.game.teams) {
			if (team == null || targetTeam.score.getScore() >= team.score.getScore()) 
				team = targetTeam;
		}
		
		if (team == null) {
			Bukkit.getServer().broadcastMessage("The game ended in a " + ChatColor.YELLOW + "Tie" + ChatColor.RESET + "!");
		} else {
			Bukkit.getServer().broadcastMessage("The game had ended and the " + team.color + team.team.getName() + ChatColor.RESET + " team won!");
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			player.teleport(main.trenchConfig.getSpawn());
		}

		main.setGameState(GameState.WAITING);
		main.game = null;
		main.classManager.classMap.clear();
	}

	private void spawnFirework() {
		for (EngineTeam targetTeam : main.game.teams) {
			if (targetTeam.flagHolder != null) {
			Firework firework = (Firework) targetTeam.flagHolder.getLocation().getWorld().spawnEntity(targetTeam.flagHolder.getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			// TODO
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);
		}
		}
	}
}
