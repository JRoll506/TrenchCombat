package com.rbruno.TrenchWarfare;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;

public class listeners implements Listener {

	public static ArrayList<Player> cooldown = new ArrayList<Player>();

	Location spawn = Main.trenchConfig.getSpawn();
	Location redSpawn = Main.trenchConfig.getRed();
	Location blueSpawn = Main.trenchConfig.getBlue();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!(player.isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (Main.gameState == 0) return;
		Player player = event.getPlayer();
		Location location = player.getLocation();
		if (Main.game.blueTeam.contains(player) && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			if (!(Main.game.redFlagHolder == null)) return;
			Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag", true);
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.redFlagHolder = player;
		}
		if (Main.game.redTeam.contains(player) && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			if (!(Main.game.blueFlagHolder == null)) return;
			Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag", true);
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.blueFlagHolder = player;
		}

		if (Main.game.redFlagHolder == player && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			Main.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue", true);
			Main.endGame(true);
		}

		if (Main.game.blueFlagHolder == player && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			Main.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red", true);
			Main.endGame(true);
		}
		if (Main.gameState == 0 || player.isOp()) return;

		if (location.getBlockX() >= Main.trenchConfig.maxX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.maxX - 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockX() <= Main.trenchConfig.minX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.minX + 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() <= Main.trenchConfig.minZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.minZ + 1, location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() >= Main.trenchConfig.maxZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.maxZ - 1, location.getYaw(), location.getPitch()));
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Main.gameState == 0) {
			ItemStack[] kit = { new ItemStack(Material.COOKED_BEEF) };
			kit[0].setAmount(64);
			event.getPlayer().getInventory().clear();
			event.getPlayer().getInventory().addItem(kit);
			event.getPlayer().teleport(spawn);
			Main.messagePlayer(event.getPlayer(), "The game will begin shortly!");
		} else if (Main.gameState == 1) {
			Main.addPlayer(event.getPlayer());
		}
	}

	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.game.redTeam.contains(player)) {
			Main.game.redTeam.remove(player);
		} else {
			Main.game.blueTeam.remove(player);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (Main.gameState == 1) {
			if (Main.game.redTeam.contains(player)) {
				if (Main.game.blueFlagHolder.equals(player)) {
					Main.game.blueFlagHolder = null;
					Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "blue " + ChatColor.WHITE + "flag", true);
				}
				Main.game.blueScore = Main.game.blueScore + 10;
				Main.game.score[0].setScore(Main.game.blueScore);
			} else {
				if (Main.game.blueFlagHolder.equals(player)) {
					Main.game.redFlagHolder = null;
					Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "red " + ChatColor.WHITE + "flag", true);
				}
				Main.game.redScore = Main.game.redScore + 10;
				Main.game.score[1].setScore(Main.game.redScore);
			}
		}
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.gameState == 1) {
			if (Main.game.redTeam.contains(player)) {
				ItemStack lhelmet = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
				LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
				lam.setColor(Color.fromRGB(184, 0, 0));
				lhelmet.setItemMeta(lam);
				player.getInventory().setChestplate(lhelmet);
				event.setRespawnLocation(redSpawn);
				Main.game.giveItems(player);
			} else {
				ItemStack lhelmet = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
				LeatherArmorMeta lam = (LeatherArmorMeta) lhelmet.getItemMeta();
				lam.setColor(Color.fromRGB(0, 255, 255));
				lhelmet.setItemMeta(lam);
				player.getInventory().setChestplate(lhelmet);
				event.setRespawnLocation(blueSpawn);
				Main.game.giveItems(player);
			}
		} else {
			event.setRespawnLocation(spawn);
			ItemStack[] kit = { new ItemStack(Material.COOKED_BEEF) };
			kit[0].setAmount(64);
			player.getInventory().clear();
			player.getInventory().addItem(kit);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(event.getDamager())) {
				// attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					return;
				} else {
					// Victim is blue
					event.setCancelled(true);
				}
			}
		} else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			Player damager = (Player) arrow.getShooter();
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(damager)) {
				// attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					return;
				} else {
					// Victim is blue
					event.setCancelled(true);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (Main.gameState == 0) return;
		final Player player = (Player) event.getPlayer();
		Location location = player.getLocation();
		Location d = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD") {
			if (d.getBlock().getType() == Material.SPONGE) {
				if (Main.game.cooldown.toArray().length == 0) {
					Main.game.cooldown.add(player);
					fireCannon(player);
				} else {
					if (!(Main.game.cooldown.contains(player))) {
						Main.game.cooldown.add(player);
						fireCannon(player);
					} else {

						Main.messagePlayer(player, "Cannon is reloading!");
					}
				}
			} else {
				if (Main.game.redTeam.contains(player) && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.blueTeam.contains(player) && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}

			}
		} else if (event.getMaterial().name() == "ARROW") {
			event.setCancelled(true);
			player.launchProjectile(Arrow.class);

		}
	}

	public void fireCannon(final Player player) {
		final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		tnt.setVelocity(player.getLocation().getDirection().multiply(3));
		tnt.setYield(0);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {

				if (Main.game.redTeam.contains(player)) {
					// red
					if (tnt.getLocation().getBlockX() < Main.trenchConfig.trenchLocationRed) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
						List<Entity> players = tnt.getNearbyEntities(5, 5, 5);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player player = (Player) players.get(i);
								player.damage(15F);
							}

						}
					}
				} else {
					// blue
					if (tnt.getLocation().getBlockX() > Main.trenchConfig.trenchLocationBlue) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
						List<Entity> players = tnt.getNearbyEntities(5, 5, 5);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player player = (Player) players.get(i);
								player.damage(15F);
							}

						}
					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			public void run() {
				Main.game.cooldown.remove(player);
			}
		}, 60L);
	}
}
