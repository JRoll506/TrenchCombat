package com.rbruno.trench.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.trench.lobby.Lobby;

public class Clock {

	private int clock = Lobby.trenchConfig.getPregameCountdown();

	public Clock() {
		Lobby.getPlugin().setGameState(LobbyState.WAITING);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(Lobby.getPlugin(), new Runnable() {
			@Override
			public void run() {
				tick();
			}
		}, 0L, 20L);
	}

	private void tick() {
		switch (Lobby.getPlugin().getLobbyState()) {
		case WAITING:
			if (Bukkit.getOnlinePlayers().size() >= Lobby.trenchConfig.getMinPlayer()) Lobby.getPlugin().setGameState(LobbyState.COUNTING);
			break;
		case COUNTING:
			if (clock % 10 == 0 && !(clock == 0)) Lobby.broadcast(ChatColor.YELLOW + "" + clock + ChatColor.WHITE + " second(s) till the game starts!");
			if (clock <= 5 && clock != 0) Lobby.broadcast(ChatColor.YELLOW + "" + clock + ChatColor.WHITE + " second(s) till the game starts!");
			if (clock == 0) startGame(); // start game
			if (clock > 0) clock--;
			break;
		case MOVING:
			if (Bukkit.getOnlinePlayers().isEmpty()) {
				Lobby.broadcast("Players moved successfully!"); // Shutdown
			} else {
				if (clock > 10) {
					Lobby.broadcast("There was an error moving you to the game server!");
				}
				clock++;
			}
			break;
		default:
			break;
		
		}
		
	}

	public void startGame() {
		Lobby.getPlugin().setGameState(LobbyState.MOVING);
		Lobby.broadcast("Moving players!");
		for (Player player : Bukkit.getOnlinePlayers()) {
			// Transfer Players
		}

	}

}
