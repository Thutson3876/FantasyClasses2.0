package me.thutson3876.fantasyclasses.cooldowns;

import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;

public class CooldownContainer {
	private final Player player;

	private final Ability ability;

	private double cooldownTime;
	
	private double reductionPerTick = 1.00;

	public CooldownContainer(Player player, Ability ability, double time) {
		this.player = player;
		this.ability = ability;
		this.cooldownTime = time;
	}
	
	public CooldownContainer(Player player, Ability ability, double cooldown, double reductionPerTick) {
		this.player = player;
		this.ability = ability;
		this.cooldownTime = cooldown;
		this.reductionPerTick = reductionPerTick;
	}

	public void tick() {
		this.cooldownTime -= reductionPerTick;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Ability getAbility() {
		return this.ability;
	}

	public double getCooldownTime() {
		return this.cooldownTime;
	}
	
	public double getReductionPerTick() {
		return this.reductionPerTick;
	}
	
	public void setReductionPerTick(double reductionPerTick) {
		this.reductionPerTick = reductionPerTick;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CooldownContainer other = (CooldownContainer) obj;
		if (this.ability.getName() == null) {
			if (other.ability.getName() != null)
				return false;
		} else if (!this.ability.equals(other.ability)) {
			return false;
		}
		if (this.cooldownTime != other.cooldownTime)
			return false;
		if (this.player == null) {
			if (other.player != null)
				return false;
		} else if (!this.player.equals(other.player)) {
			return false;
		}
		return true;
	}

	public String toString() {
		return this.player.getDisplayName() + " has: " + this.cooldownTime + " on: " + this.ability.getName();
	}
}
