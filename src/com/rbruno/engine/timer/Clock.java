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
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.Game;
import com.rbruno.engine.game.GameType;

public class Clock {

	private int pregameClock = Main.trenchConfig.getPregameCountdown();
	private int gameClock = Main.trenchConfig.getGameClock() * 60;

	private boolean pregameClockIsRunning = false;

	public Clock() {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				tick();
			}
		}, 0L, 20L);
	}

	private void tick() {
		if (Main.getGameState() == GameState.LOBBY) {
			if (!pregameClockIsRunning && Bukkit.getOnlinePlayers().size() >= Main.trenchConfig.getMinPlayer()) pregameClockIsRunning = true;
			if (pregameClock % 10 == 0 && pregameClockIsRunning && !(pregameClock == 0)) Main.broadcast(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			if (pregameClock <= 5 && pregameClock != 0) Main.broadcast(ChatColor.YELLOW + "" + pregameClock + ChatColor.WHITE + " second(s) till the game starts!");
			if (pregameClock == 0) startGame();// start game
			if (pregameClockIsRunning) pregameClock--;
		} else if (Main.getGameState() == GameState.IN_GAME) {
			spawnFirework();
			// end game & rebuild map
			if (gameClock > 0) gameClock--;
			String second = Main.clock.getGameClock() % 60 + "";
			if (Main.clock.getGameClock() % 60 <= 9) second = "0" + second;
			String time = (Main.clock.getGameClock() - (Main.clock.getGameClock() % 60)) / 60 + ":" + second;
			if (Main.getGame() != null) Main.getGame().objective.setDisplayName(ChatColor.YELLOW + "Time: " + ChatColor.WHITE + time);
			if (gameClock % 60 == 0 && gameClock != 0) Main.broadcast(ChatColor.YELLOW + "" + (gameClock / 60) + ChatColor.WHITE + " minutes left in game!");
			if (gameClock <= 0) endGame();
		}
	}

	public void startGame() {
		gameClock = Main.trenchConfig.getGameClock() * 60;
		pregameClock = Main.trenchConfig.getPregameCountdown();
		pregameClockIsRunning = false;
		Main.parkour.clear();
		pregameClock = Main.getPlugin().getConfig().getInt("pregameClock");
		Main.game = new Game(GameType.DEATHMATCH);
		Main.getGame().pickTeams();
		Main.getGame().tpPlayers();
		Main.getGame().giveItems();
		Main.broadcast("The war has begun!");
		Main.setGameState(GameState.IN_GAME);
		for (Player player : Bukkit.getOnlinePlayers()) {
			Main.game.kills.put(player, 0);
		}

	}

	public void endGame() {
		gameClock = Main.trenchConfig.getGameClock() * 60;
		pregameClock = Main.trenchConfig.getPregameCountdown();
		if (Main.getGame().getBlueTeam().getScore() == Main.getGame().getRedTeam().getScore()) {
			Main.broadcast("The game ended in a " + ChatColor.YELLOW + "Tie" + ChatColor.WHITE + "!");
		} else if (Main.getGame().getBlueTeam().getScore() > Main.getGame().getRedTeam().getScore()) {
			Main.broadcast("The game had ended and the  " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team won!");
		} else {
			Main.broadcast("The game had ended and the  " + ChatColor.RED + "Red " + ChatColor.WHITE + "team won!");
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage("You got " + ChatColor.RED + (Main.getGame().kills.get(player) == null ? "0" : Main.getGame().kills.get(player)) + ChatColor.WHITE + " Kills!");
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			player.teleport(Main.getSpawn());
		}

		Main.setGameState(GameState.LOBBY);
		Main.game = null;
		Main.classManager.getClassMap().clear();
	}

	private void spawnFirework() {
		if (!(Main.getGame().getRedTeam().getFlagHolder() == null)) {
			Firework firework = (Firework) Main.getGame().getRedTeam().getFlagHolder().getWorld().spawnEntity(Main.getGame().getRedTeam().getFlagHolder().getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkMeta = firework.getFireworkMeta();
			FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.RED).with(Type.BALL_LARGE).trail(true).build();
			fireworkMeta.addEffect(effect);
			fireworkMeta.setPower(1);
			firework.setFireworkMeta(fireworkMeta);

		}
		if (!(Main.getGame().getBlueTeam().getFlagHolder() == null)) {
			Firework firework = (Firework) Main.getGame().getBlueTeam().getFlagHolder().getWorld().spawnEntity(Main.getGame().getBlueTeam().getFlagHolder().getLocation(), EntityType.FIREWORK);
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
