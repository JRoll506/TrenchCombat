package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		if (main.getGameState() == GameState.IN_GAME) {
			for (EngineTeam targetTeam : main.game.teams)
				if (targetTeam.flagHolder == event.getPlayer())
					targetTeam.flag = null;
			main.game.getColorTeam(event.getPlayer()).team.removeEntry(event.getPlayer().getName());
		} else {
			if (Bukkit.getOnlinePlayers().size() - 1 < main.getConfig().getInt("minPlayer")) {
				main.setGameState(GameState.WAITING);
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Wating for more players.");
			}
		}
	}

}
