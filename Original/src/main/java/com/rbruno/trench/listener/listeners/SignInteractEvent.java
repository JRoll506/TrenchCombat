package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class SignInteractEvent extends EngineListner {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					if (Main.getClassManager().getClassMap().containsKey(player)) {
						Main.getClassManager().getClassMap().remove(player);
					}
					Main.getClassManager().getClassMap().put(player, sign.getLine(1));
					if (Main.getClassManager().getEngineClass(sign.getLine(1)) == null) {
						player.sendMessage(ChatColor.RED + "Class not Found!");
						return;
					}
					player.sendMessage("You have picked the " + sign.getLine(1) + " class");
					for (String line : Main.getClassManager().getEngineClass(sign.getLine(1)).getDescription()) {
						player.sendMessage(line.replace("&", "§"));
					}
				} else if (sign.getLine(0).contains("[Team]")) {
					if (Main.getPlugin().getTeamQueue().containsKey(player)) Main.getPlugin().getTeamQueue().remove(player);
					player.sendMessage("You have queued for the " + sign.getLine(1) + ChatColor.WHITE + " team");
					if (sign.getLine(1).contains("Red")) Main.getPlugin().getTeamQueue().put(player, ColorTeam.RED);
					if (sign.getLine(1).contains("Blue")) Main.getPlugin().getTeamQueue().put(player, ColorTeam.BLUE);
				} else if (sign.getLine(0).contains("[Parkour]")) {
					if (!(Main.parkour.contains(player))) {
						Main.broadcast(player.getName() + " knows how to use the spacebar!");
						Main.parkour.add(player);

					}
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Main.getPlugin().getServer().getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Main.getPlugin().getServer().getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}
	}
}
