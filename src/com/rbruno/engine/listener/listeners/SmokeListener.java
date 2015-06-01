package com.rbruno.engine.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.ParticleEffect;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class SmokeListener extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (Main.getGameState() == GameState.LOBBY) return;

		final Player player = (Player) event.getPlayer();
		
		if (event.getMaterial().name() == "SULPHUR") {
			// Removes 1 smoke
			ItemStack item = player.getItemInHand();
			player.getInventory().remove(item);
			item.setAmount(item.getAmount() - 1);
			player.getInventory().addItem(item);

			final Item smoke = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SULPHUR, 1));
			// Throws smoke
			smoke.setVelocity(player.getLocation().getDirection().multiply(1.2));
			// Makes smoke
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.runTaskTimer(Main.getPlugin(), new Runnable() {
				int i = 0;

				@Override
				public void run() {
					i++;
					if (i >= 40) smoke.remove();
					if (i < 40) {
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
					}
				}
			}, 20L, 5L);
		}
	}

}
