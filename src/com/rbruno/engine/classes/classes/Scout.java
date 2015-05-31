package com.rbruno.engine.classes.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.engine.Main;
import com.rbruno.engine.classes.EngineClass;

public class Scout extends EngineClass {

	public Scout() {
		super(new ItemStack[] { new ItemStack(Material.DIAMOND_SWORD) , Main.getPlugin().getGranade(), Main.getPlugin().getSmoke()}, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2), "Scout");
	}
}
