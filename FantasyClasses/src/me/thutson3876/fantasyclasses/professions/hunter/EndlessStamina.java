package me.thutson3876.fantasyclasses.professions.hunter;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason;
import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class EndlessStamina extends AbstractAbility {

	List<ExhaustionReason> validReasons = Arrays.asList(ExhaustionReason.JUMP, ExhaustionReason.SPRINT, ExhaustionReason.JUMP_SPRINT);
	
	public EndlessStamina(Player p) {
		super(p);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Endless Stamina";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.RABBIT_FOOT);
	}

	@EventHandler
	public void onEntityExhaustionEvent(EntityExhaustionEvent e) {
		if(!e.getEntity().equals(player))
			return;
		if(!validReasons.contains(e.getExhaustionReason()))
			return;
		
		e.setExhaustion(0);
		
		this.onTrigger(false);
	}

	@Override
	public String getInstructions() {
		return "Sprint and jump";
	}

	@Override
	public String getDescription() {
		return "Sprinting and jumping no longer apply exhaustion";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
