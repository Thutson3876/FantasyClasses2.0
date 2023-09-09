package me.thutson3876.fantasyclasses.util.particles.vanity.wings;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import me.thutson3876.fantasyclasses.util.geometry.VectorUtils;
import me.thutson3876.fantasyclasses.util.particles.CustomParticle;
import me.thutson3876.fantasyclasses.util.particles.vanity.Vanity;

public class Wings extends Vanity {

	private static final double BUG_STEP = 0.1D;
	private static final double BUTTERFLY_STEP = 0.2D;
	private static final double HANGING1_STEP = 0.2D;
	private static final double HANGING2_STEP = 0.2D;

	protected CustomParticle particle;

	private WingType type = WingType.BUTTERFLY;

	public Wings(WingType type, CustomParticle particle, int tickRate) {
		super(tickRate, 1.0, 10000);
		this.particle = particle;
		this.type = type;
	}

	@Override
	protected void tickDefault(UUID key, LivingEntity ent, int currentTick) {
		animation(ent, false);
	}

	@Override
	protected void tickWhileCrouching(UUID key, LivingEntity ent, int currentTick) {
		tickDefault(key, ent, currentTick);
	}

	@Override
	protected void tickWhileGliding(UUID key, LivingEntity ent, int currentTick) {
		animation(ent, true);
	}

	//rotate when gliding and crouching
	private void animation(LivingEntity ent, boolean isGliding) {
		double i;
		Location loc = ent.getEyeLocation().subtract(0.0D, 0.3D, 0.0D);
		loc.setPitch(0.0F);
		loc.setYaw(ent.getEyeLocation().getYaw());
		
		if(isGliding) {
			loc.add(0, 0.9, 0);
			loc.setPitch(90.0f);
		}
		
		Vector v1 = loc.getDirection().normalize().multiply(-0.4D);
		v1.setY(0);
		loc.add(v1);
		switch (this.type) {
		case BUG:
			for (i = -8.32D; i < -4.26D; i += BUG_STEP) {
				double var = Math.sin(i / 12.0D);
				double x = Math.sin(i * 0.5D)
						* (Math.exp(Math.cos(i * 7.0D)) - 1.0D * Math.cos(3.0D * i) - Math.pow(var, 5.0D)) * 0.8D;
				double z = Math.cos(i * 0.5D)
						* (Math.exp(Math.cos(i * 7.0D)) - 1.0D * Math.cos(3.0D * i) - Math.pow(var, 5.0D)) * 0.6D;
				Vector v = new Vector(x, 0.0D, -z);
				VectorUtils.rotateAroundAxisX(v, Math.max(loc.getPitch() * 2 / 45.0f, 1) * 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case BUTTERFLY:
			for (i = -10.0D; i < 6.2D; i += BUTTERFLY_STEP) {
				double var = Math.sin(i / 12.0D);
				double x = Math.sin(i) * (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D))
						/ 2.0D;
				double z = Math.cos(i) * (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D))
						/ 2.0D;
				Vector v = new Vector(-x, 0.0D, -z);
				VectorUtils.rotateAroundAxisX(v, ((loc.getPitch() + 90.0F) * 0.017453292F));
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case HANGING1:
			for (i = -20.2D; i < 5.0D; i += HANGING1_STEP) {
				double x = 1.75D * (1.0D - Math.sin(i)) * Math.cos(i * 0.5D) / 2.0D;
				double z = 2.0D * (Math.sin(i) - 1.0D) / 2.0D;
				z *= -1.0D;
				Vector v = new Vector(x, 0.0D, z);
				VectorUtils.rotateAroundAxisX(v, Math.max(loc.getPitch() * 2 / 45.0f, 1) * 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case HANGING2:
			for (i = -20.2D; i < 5.0D; i += HANGING2_STEP) {
				double x = 1.75D * (1.0D - Math.sin(i)) * Math.cos(i * 0.25D) / 2.0D;
				double z = 2.0D * (Math.sin(i) - 1.0D) / 2.0D;
				z *= -1.0D;
				Vector v = new Vector(x, 0.0D, z);
				VectorUtils.rotateAroundAxisX(v, Math.max(loc.getPitch() * 2 / 45.0f, 1) * 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		}
	}
	
	//unchanged
	/*private void animation(LivingEntity ent) {
		double i;
		Location loc = ent.getEyeLocation().subtract(0.0D, 0.3D, 0.0D);
		loc.setPitch(0.0F);
		loc.setYaw(ent.getEyeLocation().getYaw());
		
		Vector v1 = loc.getDirection().normalize().multiply(-0.3D);
		v1.setY(0);
		loc.add(v1);
		switch (this.type) {
		case BUG:
			for (i = -8.32D; i < -4.26D; i += BUG_STEP) {
				double var = Math.sin(i / 12.0D);
				double x = Math.sin(i * 0.5D)
						* (Math.exp(Math.cos(i * 7.0D)) - 1.0D * Math.cos(3.0D * i) - Math.pow(var, 5.0D)) * 0.8D;
				double z = Math.cos(i * 0.5D)
						* (Math.exp(Math.cos(i * 7.0D)) - 1.0D * Math.cos(3.0D * i) - Math.pow(var, 5.0D)) * 0.6D;
				Vector v = new Vector(x, 0.0D, -z);
				VectorUtils.rotateAroundAxisX(v, 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case BUTTERFLY:
			for (i = -10.0D; i < 6.2D; i += BUTTERFLY_STEP) {
				double var = Math.sin(i / 12.0D);
				double x = Math.sin(i) * (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D))
						/ 2.0D;
				double z = Math.cos(i) * (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D))
						/ 2.0D;
				Vector v = new Vector(-x, 0.0D, -z);
				VectorUtils.rotateAroundAxisX(v, ((loc.getPitch() + 90.0F) * 0.017453292F));
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case HANGING1:
			for (i = -20.2D; i < 5.0D; i += HANGING1_STEP) {
				double x = 1.75D * (1.0D - Math.sin(i)) * Math.cos(i * 0.5D) / 2.0D;
				double z = 2.0D * (Math.sin(i) - 1.0D) / 2.0D;
				z *= -1.0D;
				Vector v = new Vector(x, 0.0D, z);
				VectorUtils.rotateAroundAxisX(v, 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		case HANGING2:
			for (i = -20.2D; i < 5.0D; i += HANGING2_STEP) {
				double x = 1.75D * (1.0D - Math.sin(i)) * Math.cos(i * 0.25D) / 2.0D;
				double z = 2.0D * (Math.sin(i) - 1.0D) / 2.0D;
				z *= -1.0D;
				Vector v = new Vector(x, 0.0D, z);
				VectorUtils.rotateAroundAxisX(v, 2.0071287155151367D);
				VectorUtils.rotateAroundAxisY(v, (-loc.getYaw() * 0.017453292F));
				particle.spawn(loc.clone().add(v));
			}
			break;
		}
	}*/

}
