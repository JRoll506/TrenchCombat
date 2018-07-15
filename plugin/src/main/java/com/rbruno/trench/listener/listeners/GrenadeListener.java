package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class GrenadeListener extends EngineListner implements Listener {

	public GrenadeListener(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME))
			return;

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
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				@Override
				public void run() {
					grenade.remove();
					if (grenade.getLocation().getX() <= main.trenchConfig.fortRed || grenade.getLocation().getX() >= main.trenchConfig.fortBlue)
						return;
					grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);

				}
			}, 40L);
		}
	}

}
