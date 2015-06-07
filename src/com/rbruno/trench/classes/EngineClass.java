package com.rbruno.trench.classes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EngineClass implements Listener {
	private String name;
	private String premission;

	private ItemStack[] items;

	private PotionEffect effect = new PotionEffect(PotionEffectType.CONFUSION, 0, 0);
	
	private String[] description;

	public EngineClass(ItemStack[] items, PotionEffect effect, String name, String permssion) {
		this.setItems(items);
		this.setEffect(effect);
		this.setName(name);
		this.setPremission(permssion);
	}
	
	public EngineClass(ItemStack[] items, PotionEffect effect, String name) {
		this.setItems(items);
		this.setEffect(effect);
		this.setName(name);
	}

	public EngineClass(ItemStack[] items, String name, String permssion) {
		this.setItems(items);
		this.setName(name);
		this.setPremission(permssion);
	}

	public EngineClass(ItemStack[] items, String name) {
		this.setItems(items);
		this.setName(name);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){	
	}

	public String getName() {
		return name;
	}

	public String getPremission() {
		return premission;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setPremission(String premission) {
		this.premission = premission;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public void setEffect(PotionEffect effect) {
		this.effect = effect;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}
}
