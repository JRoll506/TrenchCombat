package com.rbruno.engine.classes.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.engine.Main;
import com.rbruno.engine.classes.EngineClass;

public class Scout extends EngineClass {

	public Scout() {
		super(new ItemStack[] { new ItemStack(Material.DIAMOND_SWORD) , Main.getPlugin().getGranade(), Main.getPlugin().getSmoke()}, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), "Scout");
		String[] description = { 
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 
				"&f&lScout Class",
				"&7Speedy one that hurts from close.",
				"",
				"&f&lSpeed",
				"&7Permanent Speed &a2&7.",
				"&7Equipped with &aIron Sword&7, &a3 granades &7and &aa smoke&7.",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
			};
		this.setDescription(description);
	}
}
