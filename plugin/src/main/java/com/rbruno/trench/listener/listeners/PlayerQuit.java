package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.EngineTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerQuit extends EngineListner implements Listener {

	public PlayerQuit(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		main.classManager.classMap.remove(player);
		if (main.getGameState() == GameState.IN_GAME) {
			EngineTeam team = main.game.getColorTeam(player);
			for (EngineTeam targetTeam : main.game.teams)
				if (targetTeam.flagHolder == player) {
					targetTeam.flagHolder = null;
					Bukkit.getServer().broadcastMessage(team.color + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + targetTeam.color + targetTeam.team.getName() + ChatColor.WHITE + " flag");
				}
			team.team.removeEntry(player.getName());
		} else {
			if (Bukkit.getOnlinePlayers().size() - 1 < main.getConfig().getInt("minPlayer")) {
				main.setGameState(GameState.WAITING);
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Wating for more players.");
			}
		}
	}

}
