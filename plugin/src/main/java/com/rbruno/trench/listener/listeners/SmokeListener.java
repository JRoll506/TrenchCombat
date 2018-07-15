package com.rbruno.trench.listener.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.Smoke;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class SmokeListener extends EngineListner implements Listener {
	
	public static ArrayList<Smoke> smokes = new ArrayList<Smoke>();
	
	
	public SmokeListener(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME)) return;

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
			smokes.add(new Smoke(smoke, 120));
		}
	}
	
	public static void handleSmoke() {
		ArrayList<Smoke> done = new ArrayList<Smoke>();
		for (Smoke smoke : smokes) {
			if(smoke.tick() == false) done.add(smoke);
		}
		for (Smoke smoke : done) {
			smokes.remove(smoke);
		}
	}

}
