package com.rbruno.trench.classes.classes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.trench.Main;
import com.rbruno.trench.classes.EngineClass;

public class Gunner extends EngineClass {

	private ArrayList<Player> cooldown = new ArrayList<Player>();

	public Gunner(Main main) {
		super(new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.ARROW), main.getGrenade() },
				"Gunner", main);
		String[] description = { "&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", "&f&lGunner Class",
				"&7Fully-automatic machine gun.", "", "&f&lMachine Gun", "&eRight-Click &7to use gun.",
				"&7Equipped with &aIron Sword &7and &a3 grenades",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=" };
		this.setDescription(description);
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getMaterial().name() == "ARROW") {
			event.setCancelled(true);
			if (!(cooldown.contains(player))) {
				cooldown.add(player);
				player.launchProjectile(Arrow.class);
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						cooldown.remove(player);
					}
				}, 2L);
			}

		}
	}

}
