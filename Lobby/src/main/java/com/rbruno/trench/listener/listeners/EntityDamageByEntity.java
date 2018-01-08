package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.rbruno.trench.listener.EngineListner;

public class EntityDamageByEntity extends EngineListner implements Listener{
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}
}
