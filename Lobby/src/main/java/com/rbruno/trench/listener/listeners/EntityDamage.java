package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.rbruno.trench.listener.EngineListner;

public class EntityDamage extends EngineListner implements Listener {
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		event.setCancelled(true);
	}

}
