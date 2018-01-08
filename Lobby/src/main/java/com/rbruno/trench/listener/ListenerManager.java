package com.rbruno.trench.listener;

import java.util.ArrayList;

import com.rbruno.trench.listener.listeners.*;
import com.rbruno.trench.lobby.Lobby;

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
		this.listners.add(new PlayerJoin());
		this.listners.add(new PlayerMove());
		this.listners.add(new PlayerQuit());
		this.listners.add(new PlayerRespawn());
		this.listners.add(new SignCreation());
		this.listners.add(new PlayerPickUpItem());
		this.listners.add(new SignInteractEvent());

		for (EngineListner listner : listners)
			Lobby.getPlugin().getServer().getPluginManager().registerEvents(listner, Lobby.getPlugin());
	}

}
