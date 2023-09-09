package me.thutson3876.fantasyclasses.util.geometry;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class VectorUtils {

	//When used for player, angle = pitch
	public static Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

	//When used for player, angle = yaw
    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
    
    public static double getUseablePitch(LivingEntity ent) {
    	return (ent.getLocation().getPitch() + 90.0F) * 0.017453292F;
    }
    
    public static double getUseableYaw(LivingEntity ent) {
    	return -ent.getLocation().getYaw() * 0.017453292F;
    }
}
