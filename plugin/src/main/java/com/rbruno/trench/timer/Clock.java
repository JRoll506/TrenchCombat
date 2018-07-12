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

public class Clock {
	
	private Main main;

	private int pregameClock;
	private int gameClock;

	public Clock(Main main) {
		this.main = main;
		
		pregameClock = main.trenchConfig.getPregameCountdown();
		gameClock = main.trenchConfig.getGameClock() * 60;
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(main, new Runnable() {
			@Override
			public void run() {
				tick();
			}
		}, 0L, 20L);
	}

	private void tick() {
		if (main.getGameState() == GameState.WAITING) {
			if (Bukkit.getOnlinePlayers().size() >= main.trenchConfig.getMinPlayer()) main.setGameState(GameState.COUNTING);
		} else if (main.getGameState() == GameState.COUNTING) {
			if (pregameClock % 10 == 0 && !(pregameClock == 0)) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			if (pregameClock <= 5 && pregameClock != 0) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			pregameClock--;
			if (pregameClock == 0) startGame();// start game
		} else if (main.getGameState() == GameState.IN_GAME) {
			spawnFirework();
			// end game & rebuild map
			if (gameClock > 0) gameClock--;
			String second = main.clock.getGameClock() % 60 + "";
			if (main.clock.getGameClock() % 60 <= 9) second = "0" + second;
			String time = (main.clock.getGameClock() - (main.clock.getGameClock() % 60)) / 60 + ":" + second;
			if (main.getGame() != null) main.getGame().objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);
			if (gameClock % 60 == 0 && gameClock != 0) Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + (gameClock / 60) + ChatColor.WHITE + " minutes left in game!");
			if (gameClock <= 0) endGame();
		}
	}

	public void startGame() {
		gameClock = main.trenchConfig.getGameClock() * 60;
		pregameClock = main.trenchConfig.getPregameCountdown();
		pregameClock = main.getConfig().getInt("pregameClock");
		main.game = new EngineGame(main);
		main.getGame().pickTeams();
		main.getGame().tpPlayers();
		main.getGame().giveItems();
		Bukkit.getServer().broadcastMessage("The war has begun!");
		main.setGameState(GameState.IN_GAME);
		for (Player player : Bukkit.getOnlinePlayers()) {
			main.game.kills.put(player, 0);
		}

	}

	public void endGame() {
		gameClock = main.trenchConfig.getGameClock() * 60;
		pregameClock = main.trenchConfig.getPregameCountdown();
		if (main.getGame().getBlueTeam().getScore() == main.getGame().getRedTeam().getScore()) {
			Bukkit.getServer().broadcastMessage("The game ended in a " + ChatColor.YELLOW + "Tie" + ChatColor.WHITE + "!");
		} else if (main.getGame().getBlueTeam().getScore() > main.getGame().getRedTeam().getScore()) {
			Bukkit.getServer().broadcastMessage("The game had ended and the  " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team won!");
		} else {
			Bukkit.getServer().broadcastMessage("The game had ended and the  " + ChatColor.RED + "Red " + ChatColor.WHITE + "team won!");
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage("You got " + ChatColor.RED + (main.getGame().kills.get(player) == null ? "0" : main.getGame().kills.get(player)) + ChatColor.WHITE + " Kills!");
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			player.teleport(main.trenchConfig.getSpawn());
		}

		main.setGameState(GameState.WAITING);
		main.game = null;
		main.classManager.getClassMap().clear();
	}

	private void spawnFirework() {
		if (!(main.getGame().getRedTeam().getFlagHolder() == null)) {
			Firework firework = (Firework) main.getGame().getRedTeam().getFlagHolder().getWorld().spawnEntity(main.getGame().getRedTeam().getFlagHolder().getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);

		}
		if (!(main.getGame().getBlueTeam().getFlagHolder() == null)) {
			Firework firework = (Firework) main.getGame().getBlueTeam().getFlagHolder().getWorld().spawnEntity(main.getGame().getBlueTeam().getFlagHolder().getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.BLUE).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);

		}
	}

	public int getGameClock() {
		return gameClock;
	}

}
