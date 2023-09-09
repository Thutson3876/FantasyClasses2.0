package me.thutson3876.fantasyclasses.util.geometry;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class LocationList {

	public static List<Location> generateLine(Location from, Location to){
		List<Location> list = new ArrayList<>();
		Vector direction = AbilityUtils.getDifferentialVector(from, to);
		double distance = from.distance(to);
		Location loc = from.add(direction);
		for(int i = 0; i < distance; i++) {
			list.add(loc);
			loc.add(direction);
		}
		
		return list;
	}
	
	public static List<Location> generateLine(Location from, Vector direction, int length){
		List<Location> list = new ArrayList<>();
		Location loc = from.add(direction);
		for(int i = 0; i < length; i++) {
			list.add(loc);
			loc.add(direction);
		}
		
		return list;
	}
	
}
