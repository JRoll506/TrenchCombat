package com.rbruno.trench.classes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.rbruno.trench.Main;

public class EngineClass implements Listener {
	
	public Main main;
	
	private String name;

	private ItemStack[] items;

	private PotionEffect effect;
	
	private String[] description = {};

	public EngineClass(ItemStack[] items, PotionEffect effect, String name, Main main) {
		this.main = main;
		this.items = items;
		this.effect = effect;
		this.name = name;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){	
	}

	public String getName() {
		return name;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public PotionEffect getEffect() {
		return effect;
	}
	
	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}
}
