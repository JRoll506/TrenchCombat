package com.rbruno.trench.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.rbruno.trench.classes.classes.*;

public class ClassManager {

	private ArrayList<EngineClass> classes = new ArrayList<EngineClass>();

	private HashMap<Player, String> classMap = new HashMap<Player, String>();

	public ClassManager() {
		this.classes.add(new Gunner());
		this.classes.add(new Shotgun());
		this.classes.add(new Scout());
	}

	public String getClass(Player player) {
		return classMap.get(player);
	}

	public EngineClass getEngineClass(String name) {
		for (EngineClass engineClass : classes) {
			if (engineClass.getName().equalsIgnoreCase(name)) return engineClass;
		}
		return null;
	}

	public void setClass(Player player, String string) {
		classMap.put(player, string);
	}
	
	public HashMap<Player, String> getClassMap(){
		return classMap;
	}

}
