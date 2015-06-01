package com.rbruno.engine.listener;

import java.util.ArrayList;

import com.rbruno.engine.Main;
import com.rbruno.engine.listener.listeners.*;

public class ListenerManager {

	public ArrayList<EngineListner> listners = new ArrayList<EngineListner>();

	public ListenerManager() {
		this.listners.add(new InventoryClick());
		this.listners.add(new DropItem());
		this.listners.add(new EntityDamage());
		this.listners.add(new FoodLevelChange());
		this.listners.add(new BlockBreak());
		this.listners.add(new BlockPlace());
		this.listners.add(new EntityDamageByEntity());
		this.listners.add(new PlayerDeath());
		this.listners.add(new CannonListener());
		this.listners.add(new PlayerJoin());
		this.listners.add(new PlayerMove());
		this.listners.add(new PlayerQuit());
		this.listners.add(new PlayerRespawn());
		this.listners.add(new SignCreation());
		this.listners.add(new PlayerPickUpItem());
		this.listners.add(new SignInteractEvent());
		this.listners.add(new GrenadeListener());
		this.listners.add(new SmokeListener());

		for (EngineListner listner : listners)
			Main.getPlugin().getServer().getPluginManager().registerEvents(listner, Main.getPlugin());
	}

}
