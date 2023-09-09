package me.thutson3876.fantasyclasses.classes.highroller;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.HighRollerStatus;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class RollToStarboard extends AbstractAbility {

	private static Random rng = new Random();
	
	private static final double CHANCE_PER_LEVEL = 0.1;
	private double chance = CHANCE_PER_LEVEL;
	private int duration = 6 * 20;
	
	public RollToStarboard(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0 * 20;
		this.displayName = "Roll To Starboard";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.DARK_OAK_BOAT);
	}

	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();
		
		if(!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;
		
		if(!player.equals(e.getDispeller()))
			return;
		
		if(statusType instanceof Broadsided) {
			roll();
			return;
		}
	}
	
	private boolean roll() {
		if(rng.nextDouble() < chance) {
			List<HighRollerStatus> buffs = RollTheBones.getBuffsList();
			
			HighRollerStatus buff = buffs.get(rng.nextInt(buffs.size()));
			
			Status status = buff.apply(player, player, 1, duration, ApplyCause.PLAYER_ABILITY);
			status.setRemainingDuration(duration);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getInstructions() {
		return "Consume a stack of &dBroadsided";
	}

	@Override
	public String getDescription() {
		return "When you consume a stack of &dBroadsided&r, you have a &6" + AbilityUtils.doubleRoundToXDecimals(chance * 100, 1)
		+ "% &rchance to gain a random Roll the Bones buff for &6" + AbilityUtils.doubleRoundToXDecimals(((double)duration)/20.0, 1) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		chance = CHANCE_PER_LEVEL * this.currentLevel;
	}

}
