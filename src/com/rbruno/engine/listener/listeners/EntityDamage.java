package com.rbruno.engine.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class EntityDamage extends EngineListner implements Listener{
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(Main.getGameState() == GameState.LOBBY){
			event.setCancelled(true);
		}
	}

}
