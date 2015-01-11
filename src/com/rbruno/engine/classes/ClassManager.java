package com.rbruno.engine.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.rbruno.engine.classes.classes.Gunner;

public class ClassManager {

	private ArrayList<EngineClass> classes = new ArrayList<EngineClass>();

	private HashMap<Player, String> classMap = new HashMap<Player, String>();

	public ClassManager() {
		this.classes.add(new Gunner());
	}

	public String getClass(Player player) {
		return classMap.get(player);
	}

	public EngineClass getEngineClass(String name) {
		for (EngineClass engineClass : classes) {
			if (engineClass.getName().equals(name)) return engineClass;
		}
		return null;
	}

	public void setClass(Player player, String string) {
		classMap.put(player, string);
	}

}
