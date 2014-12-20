package com.rbruno.TrenchWarfare;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
		Player player = event.getPlayer();
		if (Main.gameState == 0 || player.isOp()) return;

		Location location = player.getLocation();
		if (location.getBlockX() >= 692){
			player.teleport(new Location(location.getWorld(),691, location.getBlockY(),location.getZ(),location.getYaw(),location.getPitch()));
		}
		if (location.getBlockX() <= 511){
			player.teleport(new Location(location.getWorld(),512, location.getBlockY(),location.getZ(),location.getYaw(),location.getPitch()));
		}
		if (location.getBlockZ() <= 86){
			player.teleport(new Location(location.getWorld(),location.getX(), location.getBlockY(),87,location.getYaw(),location.getPitch()));
		}
		if (location.getBlockZ() >= 120){
			player.teleport(new Location(location.getWorld(),location.getX(), location.getBlockY(),119,location.getYaw(),location.getPitch()));
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
				Main.game.blueScore = Main.game.blueScore + 10;
				Main.game.redScore = Main.game.redScore - 5;
				Main.game.score[0].setScore(Main.game.blueScore);
				Main.game.score[1].setScore(Main.game.redScore);
			} else {
				Main.game.redScore = Main.game.redScore + 10;
				Main.game.blueScore = Main.game.blueScore - 5;
				Main.game.score[0].setScore(Main.game.blueScore);
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
				//attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					event.setCancelled(true);
				} else {
					//Victim is blue
					return;
				}
			} else {
				//attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					return;
				} else {
					//Victim is blue
					event.setCancelled(true);
				}
			}
		} else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			Player damager = (Player) arrow.getShooter();
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(damager)) {
				//attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					event.setCancelled(true);
				} else {
					//Victim is blue
					return;
				}
			} else {
				//attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					return;
				} else {
					//Victim is blue
					event.setCancelled(true);
				}
			}
		}

	}

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
			} else if (player.getLocation().getBlockX() == 592 || player.getLocation().getBlockX() == 611) {
				if (Main.game.redTeam.contains(player) && player.getLocation().getBlockX() == 592) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.blueTeam.contains(player) && player.getLocation().getBlockX() == 611) {
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
					//red
					if (tnt.getLocation().getBlockX() < 593) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
						List<Entity> players = tnt.getNearbyEntities(5, 5, 5);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player player = (Player) players.get(i);
								player.setFireTicks(100);
								player.damage(15F);
							}

						}
					}
				} else {
					//blue
					if (tnt.getLocation().getBlockX() > 610) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
						List<Entity> players = tnt.getNearbyEntities(5, 5, 5);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player player = (Player) players.get(i);
								player.setFireTicks(100);
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
