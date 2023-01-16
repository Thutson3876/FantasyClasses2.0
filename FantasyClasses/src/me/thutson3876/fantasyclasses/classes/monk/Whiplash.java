package me.thutson3876.fantasyclasses.classes.monk;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.thutson3876.fantasyclasses.abilities.Ability;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class Whiplash extends AbstractAbility {

	public Whiplash(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Whiplash";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@Override
	public String getInstructions() {
		return "Hit an entity with &6Kyoketsu-Shoge";
	}

	@Override
	public String getDescription() {
		return "Your &6Kyoketsu-Shoge &r applies Bad Luck, increasing the damage the target takes";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void deInit() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(KyoketsuShoge.class);
		if (abil == null)
			return;

		KyoketsuShoge kyo = (KyoketsuShoge) abil;
		
		kyo.setApplyWhiplash(false);
	}
	
	@Override
	public void applyLevelModifiers() {
		if(fplayer == null || fplayer.getChosenClass() == null)
			return;
		
		Ability abil = fplayer.getClassAbility(KyoketsuShoge.class);
		if (abil == null)
			return;

		KyoketsuShoge kyo = (KyoketsuShoge) abil;
		
		kyo.setApplyWhiplash(true);
	}

}
