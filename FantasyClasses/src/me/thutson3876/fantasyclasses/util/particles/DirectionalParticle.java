package me.thutson3876.fantasyclasses.util.particles;

import org.bukkit.Particle;

public enum DirectionalParticle {

	BUBBLE_COLUMN_UP(Particle.BUBBLE_COLUMN_UP),
	BUBBLE_POP(Particle.BUBBLE_POP),
	CAMPFIRE_COZY_SMOKE(Particle.CAMPFIRE_COSY_SMOKE),
	CAMPFIRE_SIGNAL_SMOKE(Particle.CAMPFIRE_SIGNAL_SMOKE),
	CLOUD(Particle.CLOUD),
	CRIT(Particle.CRIT),
	CRIT_MAGIC(Particle.ENCHANTED_HIT),
	DAMAGE_INDICATOR(Particle.DAMAGE_INDICATOR),
	DRAGON_BREATH(Particle.DRAGON_BREATH),
	ELECTRIC_SPARK(Particle.ELECTRIC_SPARK),
	ENCHANTMENT_TABLE(Particle.ENCHANT),
	END_ROD(Particle.END_ROD),
	EXPLOSION_NORMAL(Particle.EXPLOSION),
	FIREWORKS_SPARK(Particle.FIREWORK),
	FLAME(Particle.FLAME),
	NAUTILUS(Particle.NAUTILUS),
	PORTAL(Particle.PORTAL),
	REVERSE_PORTAL(Particle.REVERSE_PORTAL),
	SCRAPE(Particle.SCRAPE),
	SCULK_CHARGE(Particle.SCULK_CHARGE),
	SCULK_CHARGE_POP(Particle.SCULK_CHARGE_POP),
	SCULK_SOUL(Particle.SCULK_SOUL),
	SMALL_FLAME(Particle.SMALL_FLAME),
	SMOKE_LARGE(Particle.LARGE_SMOKE),
	SMOKE_NORMAL(Particle.SMOKE),
	SOUL(Particle.SOUL),
	SOUL_FIRE_FLAME(Particle.SOUL_FIRE_FLAME),
	SPIT(Particle.SPIT),
	SQUID_INK(Particle.SQUID_INK),
	TOTEM(Particle.TOTEM_OF_UNDYING),
	WATER_BUBBLE(Particle.BUBBLE),
	WATER_WAKE(Particle.UNDERWATER),
	WAX_OFF(Particle.WAX_OFF),
	WAX_ON(Particle.WAX_ON);
	
	private final Particle particle;
	
	private DirectionalParticle(Particle particle) {
		this.particle = particle;
	}
	
	public Particle toParticle() {
		return this.particle;
	}
	
	public static boolean contains(Particle particle) {
		boolean isFound = false;
		for(DirectionalParticle p : values()) {
			if(p.toParticle().equals(particle)) {
				isFound = true;
				break;
			}
		}
		
		return isFound;
	}
}
