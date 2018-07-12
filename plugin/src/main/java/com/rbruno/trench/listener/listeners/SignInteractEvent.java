package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;

public class SignInteractEvent extends EngineListner {

	public SignInteractEvent(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Parkour]")) {
					Bukkit.getServer().broadcastMessage(player.getName() + " knows how to use the spacebar!");
					player.setVelocity(new Vector((Math.random() - .5) * 8, 2, (Math.random() - .5) * 8));
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Bukkit.getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Bukkit.getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}
	}
}
