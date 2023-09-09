package me.thutson3876.fantasyclasses.util.geometry;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Sphere {

	public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {

		List<Location> circleBlocks = new ArrayList<Location>();

		int bx = centerBlock.getBlockX();
		int by = centerBlock.getBlockY();
		int bz = centerBlock.getBlockZ();

		for (int x = bx - radius; x <= bx + radius; x++) {
			for (int y = by - radius; y <= by + radius; y++) {
				for (int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

					if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(centerBlock.getWorld(), x, y, z);

						// checks if air
						if (l.getBlock().isPassable()) {
							circleBlocks.add(l);
						}

					}
				}
			}
		}

		return circleBlocks;
	}
	
	public static List<Location> generateDome(Location centerBlock, int radius, boolean hollow){

		List<Location> circleBlocks = new ArrayList<Location>();

		int bx = centerBlock.getBlockX();
		int by = centerBlock.getBlockY();
		int bz = centerBlock.getBlockZ();

		for (int x = bx - radius; x <= bx + radius; x++) {
			for (int y = by; y <= by + radius; y++) {
				for (int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

					if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(centerBlock.getWorld(), x, y, z);

						// checks if air
						if (l.getBlock().isPassable()) {
							circleBlocks.add(l);
						}

					}
				}
			}
		}

		return circleBlocks;
	}
	
	public static List<Location> generateBowl(Location centerBlock, int radius, boolean hollow){

		List<Location> circleBlocks = new ArrayList<Location>();

		int bx = centerBlock.getBlockX();
		int by = centerBlock.getBlockY();
		int bz = centerBlock.getBlockZ();

		for (int x = bx - radius; x <= bx + radius; x++) {
			for (int y = by - radius; y <= by; y++) {
				for (int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

					if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(centerBlock.getWorld(), x, y, z);

						// checks if air
						if (l.getBlock().isPassable()) {
							circleBlocks.add(l);
						}

					}
				}
			}
		}

		return circleBlocks;
	}
	
	public static List<Location> generateSphereIncludingBlocks(Location centerBlock, int radius, boolean hollow) {

		List<Location> circleBlocks = new ArrayList<Location>();

		int bx = centerBlock.getBlockX();
		int by = centerBlock.getBlockY();
		int bz = centerBlock.getBlockZ();

		for (int x = bx - radius; x <= bx + radius; x++) {
			for (int y = by - radius; y <= by + radius; y++) {
				for (int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

					if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(centerBlock.getWorld(), x, y, z);
						
						circleBlocks.add(l);

					}
				}
			}
		}

		return circleBlocks;
	}
	
	public static List<Location> generateCircle(Location center, int radius, boolean hollow){
		List<Location> circleBlocks = new ArrayList<Location>();

		int bx = center.getBlockX();
		int bz = center.getBlockZ();

		for (int x = bx - radius; x <= bx + radius; x++) {
				for (int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));

					if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(center.getWorld(), x, center.getY(), z);
						
						circleBlocks.add(l);

					}
				}
		}

		return circleBlocks;
	}
	
	public static List<Location> generateCircle(Location center, double radius, double gapSize, boolean hollow){
		List<Location> circleBlocks = new ArrayList<Location>();

		double bx = center.getX();
		double bz = center.getZ();

		for (double x = bx - radius; x <= bx + radius; x += gapSize) {
				for (double z = bz - radius; z <= bz + radius; z += gapSize) {

					double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));

					if (distance < radius * radius && !(hollow && distance < ((radius - gapSize) * (radius - gapSize)))) {

						Location l = new Location(center.getWorld(), x, center.getY(), z);
						
						circleBlocks.add(l);

					}
				}
		}

		return circleBlocks;
	}

	public static Location rotateLocXZ(Location loc, Location axisCenter, double angle) {
	    //angle *= -1; // By default, we use counterclockwise rotations. Uncomment to use clcokwise rotations instead.
	    //angle *= 180 / Math.PI; // By default, angle is in radians. Uncomment to use degrees instead.
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    Vector r1 = new Vector(cos, 0, -sin); // Bukkit vectors need 3 components, so set the y-component to 0
	    Vector r2 = new Vector(sin, 0, cos);
	    Vector v = loc.clone().subtract(axisCenter).toVector();
	    Vector rotated = new Vector(r1.dot(v), 0, r2.dot(v)); // Perform the matrix multiplication
	    return rotated.add(axisCenter.toVector()).toLocation(loc.getWorld());
	}
	
	public static Location rotateLoc(Location loc, Location axisCenter, Vector axis, double angle) {
	    //angle *= -1; // By default, we use counterclockwise rotations. Uncomment to use clcokwise rotations instead.
	    //angle *= 180 / Math.PI; // By default, angle is in radians. Uncomment to use degrees instead.
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    double tan = Math.tan(angle);
	    Vector r1 = new Vector(cos, 0, -sin); // Bukkit vectors need 3 components, so set the y-component to 0
	    Vector r2 = new Vector(sin, 0, cos);
	    Vector r3 = new Vector(0, tan, 0);
	    Vector v = loc.clone().subtract(axisCenter).toVector();
	    Vector rotated = new Vector(r1.dot(v), r3.dot(v), r2.dot(v)); // Perform the matrix multiplication
	    return rotated.add(axisCenter.toVector()).toLocation(loc.getWorld());
	}
	
	/**
	 * Intrinsic matrix rotation for MC coordinate system
	 * @author Michel_0
	 * @param direction The directional vector to be transformed
	 * @param yaw The desired yaw angle for rotation
	 * @param pitch The desired pitch angle for rotation
	 * @param roll The desired roll angle for rotation
	 * @return The transformed directional vector
	 * @see https://en.wikipedia.org/wiki/Euler_angles
	 */
	public static Vector transform(Vector direction, double yaw, double pitch, double roll) {
	    double[] vec = new double[] { direction.getX(), direction.getY(), direction.getZ() };
	    direction.setX(
	            vec[0] * (Math.cos(-yaw) * Math.cos(roll) + Math.sin(-yaw) * Math.sin(pitch) * Math.sin(roll))
	            + vec[1] * (Math.cos(roll) * Math.sin(-yaw) * Math.sin(pitch) - Math.cos(-yaw) * Math.sin(roll))
	            + vec[2] * Math.cos(pitch) * Math.sin(-yaw));
	    direction.setY(
	            vec[0] * Math.cos(pitch) * Math.sin(roll)
	            + vec[1] * Math.cos(pitch) * Math.cos(roll)
	            - vec[2] * Math.sin(pitch));
	    direction.setZ(
	            vec[0] * (Math.cos(-yaw) * Math.sin(pitch) * Math.sin(roll) - Math.cos(roll) * Math.sin(-yaw))
	            + vec[1] * (Math.cos(-yaw) * Math.cos(roll) * Math.sin(pitch) + Math.sin(-yaw) * Math.sin(roll))
	            + vec[2] * Math.cos(-yaw) * Math.cos(pitch));
	    return direction;
	}
}