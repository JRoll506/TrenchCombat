package com.rbruno.engine.listener.listeners;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class GrenadeListener extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (Main.getGameState() == GameState.LOBBY) return;

		final Player player = (Player) event.getPlayer();

		if (event.getMaterial().name() == "SLIME_BALL") {
			// Removes 1 grenade
			ItemStack item = player.getItemInHand();
			player.getInventory().remove(item);
			item.setAmount(item.getAmount() - 1);
			player.getInventory().addItem(item);

			final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SLIME_BALL, 1));
			// Throws grenade
			grenade.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					grenade.remove();
					grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);

					if (grenade.getLocation().getX() <= Main.trenchConfig.fortRed || grenade.getLocation().getX() >= Main.trenchConfig.fortBlue) return;

					List<Entity> players;
					players = grenade.getNearbyEntities(2, 2, 2);
					for (Entity victum : players) {
						if (victum instanceof Player) {
							Player victumPlayer = (Player) victum;
							if (Main.game.getColorTeam(player) == ColorTeam.RED) {
								if (Main.game.getColorTeam(victumPlayer) == ColorTeam.BLUE) {
									victumPlayer.damage(20F);
									victumPlayer.setLastDamageCause(new EntityDamageEvent(player, DamageCause.ENTITY_EXPLOSION, new HashMap<EntityDamageEvent.DamageModifier, Double>(), null));
								}

							} else {
								if (Main.game.getColorTeam(victumPlayer) == ColorTeam.RED) {
									victumPlayer.damage(20F);
									victumPlayer.setLastDamageCause(new EntityDamageEvent(player, DamageCause.ENTITY_EXPLOSION, new HashMap<EntityDamageEvent.DamageModifier, Double>(), null));
								}
							}
						}
					}

				}
			}, 40L);
		}
	}

}
