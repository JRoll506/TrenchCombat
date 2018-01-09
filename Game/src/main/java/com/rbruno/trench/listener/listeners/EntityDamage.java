package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class EntityDamage extends EngineListner implements Listener {
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (Game.getPlugin().getGameState() == GameState.PLAYING)
			return;
		event.setCancelled(true);
	}

}
