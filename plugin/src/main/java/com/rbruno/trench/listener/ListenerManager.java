package com.rbruno.trench.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.listeners.*;

public class ListenerManager {

	public ArrayList<EngineListner> listners = new ArrayList<EngineListner>();

	public ListenerManager(Main main) {
		this.listners.add(new InventoryClick());
		this.listners.add(new DropItem());
		this.listners.add(new EntityDamage(main));
		this.listners.add(new FoodLevelChange());
		this.listners.add(new BlockBreak());
		this.listners.add(new BlockPlace());
		this.listners.add(new EntityDamageByEntity(main));
		this.listners.add(new PlayerDeath(main));
		this.listners.add(new CannonListener(main));
		this.listners.add(new PlayerJoin(main));
		this.listners.add(new PlayerMove(main));
		this.listners.add(new PlayerQuit(main));
		this.listners.add(new PlayerRespawn(main));
		this.listners.add(new SignCreation());
		this.listners.add(new PlayerPickUpItem());
		this.listners.add(new SignInteractEvent(main));
		this.listners.add(new GrenadeListener(main));
		this.listners.add(new SmokeListener(main));
		this.listners.add(new ClassSignInteract(main));
		this.listners.add(new TeamSignInteract(main));

		for (EngineListner listner : listners)
			Bukkit.getPluginManager().registerEvents(listner, main);
	}

}
