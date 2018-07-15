package com.rbruno.trench.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.listeners.*;

public class EngineListner implements Listener {

	public Main main;

	public static void register(Main main) {
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), main);
		Bukkit.getPluginManager().registerEvents(new DropItem(), main);
		Bukkit.getPluginManager().registerEvents(new EntityDamage(main), main);
		Bukkit.getPluginManager().registerEvents(new FoodLevelChange(), main);
		Bukkit.getPluginManager().registerEvents(new BlockBreak(), main);
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), main);
		//Bukkit.getPluginManager().registerEvents(new EntityDamageByEntity(main), main);
		Bukkit.getPluginManager().registerEvents(new PlayerDeath(main), main);
		Bukkit.getPluginManager().registerEvents(new CannonListener(main), main);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(main), main);
		Bukkit.getPluginManager().registerEvents(new PlayerMove(main), main);
		Bukkit.getPluginManager().registerEvents(new PlayerQuit(main), main);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawn(main), main);
		Bukkit.getPluginManager().registerEvents(new SignCreation(), main);
		Bukkit.getPluginManager().registerEvents(new PlayerPickUpItem(), main);
		Bukkit.getPluginManager().registerEvents(new SignInteractEvent(main), main);
		Bukkit.getPluginManager().registerEvents(new GrenadeListener(main), main);
		Bukkit.getPluginManager().registerEvents(new SmokeListener(main), main);
		Bukkit.getPluginManager().registerEvents(new ClassSignInteract(main), main);
		//Bukkit.getPluginManager().registerEvents(new TeamSignInteract(main), main);
	}

	public EngineListner() {
	}

	public EngineListner(Main main) {
		this.main = main;
	}

}
