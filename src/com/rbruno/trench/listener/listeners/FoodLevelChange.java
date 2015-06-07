package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.rbruno.trench.listener.EngineListner;

public class FoodLevelChange extends EngineListner implements Listener{
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

}
