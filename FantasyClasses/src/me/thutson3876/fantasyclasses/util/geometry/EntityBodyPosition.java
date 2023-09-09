package me.thutson3876.fantasyclasses.util.geometry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public enum EntityBodyPosition {

	TOP, MID, BOTTOM;

	public Location getLocation(Entity ent) {
		Location returnLoc = ent.getLocation();
		
		switch (this) {
		case TOP:
			returnLoc = returnLoc.add(0, ent.getHeight(), 0);
		case MID:
			returnLoc = returnLoc.add(0, ent.getHeight() / 2.0, 0);
		case BOTTOM:
			
		}
		
		return returnLoc;
	}

}
