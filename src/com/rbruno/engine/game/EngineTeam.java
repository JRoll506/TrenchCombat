package com.rbruno.engine.game;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class EngineTeam {
	
	private String name;
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private Team team;

	public Color color;
	
	private Player flagHolder;

	private int score;
	
	public EngineTeam(String name,boolean canHarm,Color color, ScoreBoardManager scoreBoardManager){
		this.color = color;
		this.name = name;
		team = scoreBoardManager.addTeam(name);
		team.setAllowFriendlyFire(canHarm);
		team.setCanSeeFriendlyInvisibles(true);
		team.setDisplayName(name);
	}
	
	public boolean isPlayer(Player player){
		return players.contains(player);
	}
	
	public void addPlayer(Player player){
		players.add(player);
		team.addEntry(player.getName());
	}
	
	public void removePlayer(Player player){
		players.remove(player);
		team.removeEntry(player.getName());
	}
	
	public String getName(){
		return name;
	}
	
	public Team getTeam(){
		return team;
	}
	
	public void setName(String name){
		this.name = name;
		team.setDisplayName(name);
	}

	public Player getFlagHolder() {
		return flagHolder;
	}

	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public void setFlagHolder(Player flagHolder) {
		this.flagHolder = flagHolder;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		System.out.println("called " + score);
		this.score = score;
	}

}
