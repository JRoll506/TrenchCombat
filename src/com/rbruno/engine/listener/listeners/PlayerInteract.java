package com.rbruno.engine.listener.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.ParticleEffect;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class PlayerInteract extends EngineListner implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (Main.getGameState() == GameState.LOBBY)
			return;

		Location location = player.getLocation();
		Location d = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD" || event.getMaterial().name() == "DIAMOND_SWORD") {
			if (d.getBlock().getType() == Material.SPONGE) {
				if (Main.game.cooldown.toArray().length == 0) {
					Main.game.cooldown.add(player);
					if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
						fireCannon(player, true);
					} else {
						fireCannon(player, false);
					}
					player.sendMessage("Reloading cannon...");
				} else {
					if (!(Main.game.cooldown.contains(player))) {
						Main.game.cooldown.add(player);
						if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
							fireCannon(player, true);
						} else {
							fireCannon(player, false);
						}
						player.sendMessage("Reloading cannon...");
					}
				}
			} else {
				if (Main.game.getColorTeam(player) == ColorTeam.RED && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.getColorTeam(player) == ColorTeam.BLUE && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}

			}
		} else if (event.getMaterial().name() == "SULPHUR") {
			player.getInventory().remove(new ItemStack(Material.SLIME_BALL, 1));
			final Item smoke = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SULPHUR, 1));
			smoke.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.runTaskTimer(Main.getPlugin(), new Runnable() {
				int i = 0;

				@Override
				public void run() {
					i++;
					if (i >= 40)
						smoke.remove();
					if (i < 40) {
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
					}
				}
			}, 20L, 5L);

		} else if (event.getMaterial().name() == "SLIME_BALL") {
			player.getInventory().remove(new ItemStack(Material.SLIME_BALL, 1));
			final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SLIME_BALL, 1));
			grenade.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					grenade.remove();
					grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);
					if (grenade.getLocation().getX() <= Main.trenchConfig.fortRed || grenade.getLocation().getX() >= Main.trenchConfig.fortBlue)
						return;
					if (Main.game.getColorTeam(player) == ColorTeam.RED) {
						grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);
						List<Entity> players;
						players = grenade.getNearbyEntities(2, 2, 2);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player victum = (Player) players.get(i);
								if (Main.game.getColorTeam(victum) == ColorTeam.BLUE) {
									if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
										if (Main.game.getRedTeam().getFlagHolder() == victum) {
											Main.game.getRedTeam().setFlagHolder(null);
											Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
										}
									}
									player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.WHITE + " with your grenade!");
									victum.sendMessage(ChatColor.RED + player.getName() + ChatColor.WHITE +" has killed you with their grenade!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victum.damage(20F);
								}

							}

						}
					} else {
						grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);
						List<Entity> players;
						players = grenade.getNearbyEntities(2, 2, 2);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player victum = (Player) players.get(i);
								if (Main.game.getColorTeam(victum) == ColorTeam.RED) {
									if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
										if (Main.game.getBlueTeam().getFlagHolder() == victum) {
											Main.game.getBlueTeam().setFlagHolder(null);
											Main.broadcast(ChatColor.RED + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
										}
									}
									player.sendMessage("You have killed " + ChatColor.RED + victum.getName() + ChatColor.WHITE + " with your cannon!");
									victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has killed you with their grenade!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victum.damage(20F);
								}
							}

						}
					}

				}
			}, 40L);
		}
	}

	public void fireCannon(final Player player, final boolean rightClick) {
		final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		if (rightClick) {
			tnt.setVelocity(player.getLocation().getDirection().multiply(1.5));
		} else {
			tnt.setVelocity(player.getLocation().getDirection().multiply(3));
		}
		tnt.setYield(0);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (tnt.getLocation().getX() <= Main.trenchConfig.fortRed || tnt.getLocation().getX() >= Main.trenchConfig.fortBlue)
					return;
				if (Main.game.getColorTeam(player) == ColorTeam.RED) {
					// red
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(4, 4, 4);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (Main.game.getColorTeam(victum) == ColorTeam.RED) {
								if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
									if (Main.game.getRedTeam().getFlagHolder().equals(victum)) {
										Main.game.getRedTeam().setFlagHolder(null);
										Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
									}
								}
								player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.RED + " with your cannon!");
								victum.sendMessage(player.getName() + " has killed you with his cannon!");
								// Main.game.kills.put(player,
								// Main.game.kills.get(player) + 1);
								// player.setExp(Main.game.kills.get(player));
								victum.damage(20F);
							}

						}

					}
				} else {
					// blue
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(4, 4, 4);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (Main.game.getColorTeam(victum) == ColorTeam.RED) {
								if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
									if (Main.game.getBlueTeam().getFlagHolder() == victum) {
										Main.game.getBlueTeam().setFlagHolder(null);
										Main.broadcast(ChatColor.RED + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
									}
								}
								player.sendMessage("You have killed " + victum.getName() + " with your cannon!");
								victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " has killed you with his cannon!");
								// Main.game.kills.put(player,
								// Main.game.kills.get(player) + 1);
								// player.setExp(Main.game.kills.get(player));
								victum.damage(20F);
							}
						}

					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Main.game.cooldown.remove(player);
			}
		}, 60L);
	}
}
