package com.rbruno.engine.classes.classes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.classes.EngineClass;

public class Gunner extends EngineClass {

	private ArrayList<Player> cooldown = new ArrayList<Player>();

	public Gunner() {
		super(new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW), Main.getPlugin().getGrenade() }, "Gunner");
		String[] description = { 
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 
				"&f&lGunner Class",
				"&7Fully-automatic machine gun.",
				"",
				"&f&lMachine Gun",
				"&eRight-Click &7to use gun.",
				"&7Equipped with &aIron Sword &7and &a3 grenades",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
			};
		this.setDescription(description);
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getMaterial().name() == "ARROW") {
			event.setCancelled(true);
			if (cooldown.toArray().length == 0) {
				cooldown.add(player);
				player.launchProjectile(Arrow.class);
			} else {
				if (!(cooldown.contains(player))) {
					cooldown.add(player);
					player.launchProjectile(Arrow.class);
				}
			}
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				public void run() {
					cooldown.remove(player);
				}
			}, 2L);

		}
	}

}
