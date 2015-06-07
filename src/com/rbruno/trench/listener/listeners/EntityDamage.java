package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class EntityDamage extends EngineListner implements Listener{
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(Main.getGameState() == GameState.LOBBY){
			event.setCancelled(true);
		}
	}

}
