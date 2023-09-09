package me.thutson3876.fantasyclasses.classes.highroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Bindable;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.AdrenalineRush;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Dreadblades;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.HighRollerStatus;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Keelhaul;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.RideTheWaves;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Ruthlessness;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.TrueBearing;
import me.thutson3876.fantasyclasses.events.AbilityTriggerEvent;
import me.thutson3876.fantasyclasses.events.ApplyStatusEvent;
import me.thutson3876.fantasyclasses.status.ApplyCause;
import me.thutson3876.fantasyclasses.status.Status;
import me.thutson3876.fantasyclasses.util.AbilityUtils;
import me.thutson3876.fantasyclasses.util.Aura;

public class RollTheBones extends AbstractAbility implements Bindable {

	private static final List<HighRollerStatus> BUFFS_LIST;
	
	private Material type = null;

	private static int duration = 12 * 20;
	
	private BuffAura aura;

	static {
		List<HighRollerStatus> buffs = new ArrayList<>();
		
		buffs.add(new RideTheWaves());
		buffs.add(new Dreadblades());
		buffs.add(new AdrenalineRush());
		buffs.add(new Keelhaul());
		buffs.add(new Ruthlessness());
		buffs.add(new TrueBearing());
		BUFFS_LIST = buffs;
	}
	
	public RollTheBones(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 16 * 20;
		this.displayName = "Roll The Bones";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SKELETON_SKULL);
	}

	@EventHandler
	public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
		if (isOnCooldown())
			return;

		if (!e.getPlayer().equals(player))
			return;

		if(player.isSneaking())
			return;
		
		boolean correctType = false;

		if (e.getMainHandItem() != null && e.getMainHandItem().getType().equals(type)) {
			correctType = true;
		}
		if (e.getOffHandItem() != null && e.getOffHandItem().getType().equals(type)) {
			correctType = true;
		}

		if (!correctType)
			return;

		AbilityTriggerEvent thisEvent = this.callEvent();

		if (thisEvent.isCancelled())
			return;

		e.setCancelled(true);
		
		for(HighRollerStatus type : roll()) {
			type.apply(player, player, 1, duration, ApplyCause.PLAYER_ABILITY);
		}
		
		
		player.getWorld().playSound(player, Sound.ENTITY_SKELETON_AMBIENT, 1.5f, 0.7f);
		this.triggerCooldown(thisEvent.getCooldown(), thisEvent.getCooldownReductionPerTick());
	}
	
	@EventHandler
	public void onApplyBuffEvent(ApplyStatusEvent e) {
		boolean isRollTheBones = false;
		Status eventStatus = e.getStatus();
		
		for(HighRollerStatus rollerStatus : BUFFS_LIST) {
			if(eventStatus.getType().equals(rollerStatus)) {
				isRollTheBones = true;
				break;
			}
		}
		
		if(!isRollTheBones)
			return;
		
		double longestDuration = eventStatus.getRemainingDuration();
		for(Status s : plugin.getStatusManager().getAll(player)) {
			if(BUFFS_LIST.contains(s.getType()) && s.getRemainingDuration() > longestDuration)
				longestDuration = s.getRemainingDuration();
		}
		
		if(aura.isOn())
			aura.toggleAura();
		
		aura = new BuffAura(player, 1, displayName, BarColor.BLUE, 20, (int) longestDuration);
	}

	public List<HighRollerStatus> roll() {
		int buffsListSize = BUFFS_LIST.size();
		
		Random rng = new Random();
		List<HighRollerStatus> buffs = new ArrayList<>();
		int[] numOfMatchesPerIndex = new int[buffsListSize];
		
		int[] rolls = new int[buffsListSize];
		
		for(int i = 0; i < buffsListSize; i++) {
			rolls[i] = rng.nextInt(buffsListSize);
		}
		
		for(int i : rolls) {
			numOfMatchesPerIndex[i]++;
		}
		
		for(Integer i : getHighestIndicies(numOfMatchesPerIndex))
			buffs.add(BUFFS_LIST.get(i));
		
		return buffs;
	}
	
	private List<Integer> getHighestIndicies(int[] arr){
		int highest = -1;
		List<Integer> highestIndicies = new ArrayList<>();
		
		for(int i : arr) {
			if(i > highest)
				highest = i;
		}
		
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == highest)
				highestIndicies.add(i);
		}
		
		return highestIndicies;
	}
	
	@Override
	public String getInstructions() {
		return "Swap hands with bound item type while not crouching";
	}

	@Override
	public String getDescription() {
		return "Grant yourself a random power boost for &6" + AbilityUtils.doubleRoundToXDecimals(((double)duration)/20.0, 1) + " &rseconds";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {

	}

	@Override
	public Material getBoundType() {
		return type;
	}

	@Override
	public void setBoundType(Material type) {
		this.type = type;
	}
	
	public static List<HighRollerStatus> getBuffsList(){
		return BUFFS_LIST;
	}
	
	private class BuffAura extends Aura {

		public BuffAura(Player p, double range, String name, BarColor color, int tickRate, long duration) {
			super(p, range, name, color, tickRate, duration);
		}

		@Override
		public void run() {
			
		}
		
	}
}
