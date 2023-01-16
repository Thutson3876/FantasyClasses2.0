package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Lightfoot extends AbstractAbility {

	public Lightfoot(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Lightfoot";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.RABBIT_FOOT);
	}

	@Override
	public String getInstructions() {
		return "Re-activate &6Windwalker &5while &5it's on cooldown";
	}

	@Override
	public String getDescription() {
		return "While you're mid-air, after using &6Windwalker&r, you may re-activate it to gain Slow Fall";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

	@Override
	public void deInit() {
		if(fplayer == null)
			return;
		
		Ability abil = fplayer.getClassAbility(WindWalker.class);
		if(abil == null)
			return;
		
		WindWalker windwalker = (WindWalker)abil;
		
		windwalker.setDoubleJump(false);
	}
	
	@Override
	protected void init() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(WindWalker.class);
		if(abil == null)
			return;
		
		WindWalker windwalker = (WindWalker)abil;
		
		windwalker.setDoubleJump(true);
	}
}
