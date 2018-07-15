package com.rbruno.trench.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.particalLib.ParticleEffect;

public class Smoke {

	public Item smoke;
	public int duration;

	public Smoke(Item smoke, int duration) {
		this.smoke = smoke;
		this.duration = duration;
	}

	public boolean tick() {
		if (duration <= 0) {
			smoke.remove();
			return false;
		} else {
			ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 1000);
			ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 1000);
			for (Player player : Bukkit.getOnlinePlayers())
				if (player.getLocation().distance(smoke.getLocation()) <= 5)
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 1), true);
			duration--;
			return true;
		}
	}

}
