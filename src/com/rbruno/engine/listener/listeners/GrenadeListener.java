package com.rbruno.engine.listener.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

								if (Main.game.getColorTeam(player) == ColorTeam.BLUE) {
									if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
										if (Main.game.getRedTeam().getFlagHolder() == victum) {
											Main.game.getRedTeam().setFlagHolder(null);
											Main.broadcast(ChatColor.BLUE + victumPlayer.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
										}
									}
									player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.WHITE + " with your grenade!");
									victum.sendMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has killed you with their grenade!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victumPlayer.damage(20F);
								}

							} else {
								if (Main.game.getColorTeam(victumPlayer) == ColorTeam.RED) {
									if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
										if (Main.game.getBlueTeam().getFlagHolder() == victum) {
											Main.game.getBlueTeam().setFlagHolder(null);
											Main.broadcast(ChatColor.RED + victumPlayer.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
										}
									}
									player.sendMessage("You have killed " + ChatColor.RED + victum.getName() + ChatColor.WHITE + " with your cannon!");
									victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has killed you with their grenade!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victumPlayer.damage(20F);
								}
							}
						}
					}

				}
			}, 40L);
		}
	}

}
