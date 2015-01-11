package com.rbruno.engine.classes;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class EngineClass {
	private String name;
	private String premission;

	private ItemStack[] items;

	private PotionEffect effect;
	
	private String[] description;

	public EngineClass(ItemStack[] items, PotionEffect effect, String name, String permssion) {
		this.setItems(items);
		this.setEffect(effect);
		this.setName(name);
		this.setPremission(permssion);
		decription();
	}

	public EngineClass(ItemStack[] items, String name, String permssion) {
		this.setItems(items);
		this.setName(name);
		this.setPremission(permssion);
		decription();
	}

	public EngineClass(ItemStack[] items, String name) {
		this.setItems(items);
		this.setName(name);
		decription();
	}
	
	private String[] decription(){
		String string = "The "+ getName() + " class has "; 
		for (ItemStack name: items){
			string += name.getItemMeta().getDisplayName() + " ";
		}
		return new String[]{string};
	}
	
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
