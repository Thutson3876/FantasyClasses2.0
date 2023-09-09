package me.thutson3876.fantasyclasses.status;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.thutson3876.fantasyclasses.classes.berserker.statuses.Enraged;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.AdrenalineRush;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Blindsided;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Dreadblades;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Keelhaul;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.RideTheWaves;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Ruthlessness;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.TrueBearing;
import me.thutson3876.fantasyclasses.status.general.Leech;
import me.thutson3876.fantasyclasses.status.general.Stealth;
import me.thutson3876.fantasyclasses.status.general.Strider;
import me.thutson3876.fantasyclasses.status.general.Vulnerable;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;
import me.thutson3876.fantasyclasses.util.chat.ColorCode;
import me.thutson3876.fantasyclasses.util.math.MathUtils;

public enum DefaultStatusType {

	//General
	LEECH(new Leech(), "Heal for &6" + MathUtils.convertToPercent(Leech.getLeechPercent(), 1) + "% &rof the damage you deal, per stack", Material.REDSTONE),
	STRIDER(new Strider(), "Gain &6Speed " + (Strider.getDefaultAmp() + 1) + " &rand &6Jump &6Boost &6" + (Strider.getDefaultAmp() + 1), Material.RABBIT_FOOT),
	STEALTH(new Stealth(), "Gain &6Speed " + (Stealth.getDefaultAmp() + 1) + " &rand &6Invisibility&r. Taking or dealing damage removes this effect.", Material.OAK_LEAVES),
	VULNERABLE(new Vulnerable(), "Increases damage taken per stack by" + MathUtils.convertToPercent(Vulnerable.getDefaultMod(), 1) + "%", Material.IRON_SWORD),
	//Berserker
	ENRAGED(new Enraged(), "Gain &6Resistance 1&r. Interacts with various &6Berserker &rabilities", Material.CRACKED_STONE_BRICKS),
	//Highroller
	BLINDSIDED(new Blindsided(), "Take " + MathUtils.convertToPercent(Blindsided.getBaseDamageMod(), 1) + "% &rmore damage from the next arrow shot from whoever applied this debuff (increases based on stacks). Interacts with various &6Highroller &rabilities", Material.IRON_SWORD),
	BROADSIDED(new Broadsided(), "Take " + MathUtils.convertToPercent(Broadsided.getDmgMod(), 1) + "% &rmore damage from the next melee attack from whoever applied this debuff (increases based on stacks). Interacts with various &6Highroller &rabilities", Material.CROSSBOW),
	ADRENALINE_RUSH(new AdrenalineRush(), "Gain &6Speed 1&r. Consuming a &dBlindsided &ror &dBroadsided &ron an enemy reduces the cooldown of your abilities by 1 second. &bRoll the &bBones Status", Material.HONEY_BOTTLE),
	DREADBLADES(new Dreadblades(), "Gain &6Haste 4&r and &dLeech. &bRoll the &bBones Status", Material.NETHERITE_SWORD),
	KEELHAUL(new Keelhaul(), "Crossbow shots that consume &dBlindsided &rcause you to launch TNT at the target. The explosion applies &dBlindsided to all targets hit. &bRoll the &bBones Status", Material.TNT),
	RIDE_THE_WAVES(new RideTheWaves(), "Gain &dStrider&r. Damage dealt while above your target is doubled. &bRoll the &bBones Status", Material.RABBIT_FOOT),
	RUTHLESSNESS(new Ruthlessness(), "Gain &6Resistance 1&r. Consuming a &dBlindsided &ror &dBroadsided &ron an enemy causes the next consumption of its opposite to deal 10% more bonus damage (stacking). &bRoll the &bBones Status", Material.TARGET),
	TRUE_BEARING(new TrueBearing(), "Gain 1 hunger and saturation every second. Consuming a &dBlindsided &ror &dBroadsided &ron an enemy causes you to gain &dStealth. &bRoll the &bBones Status", Material.COMPASS),

	//Ranger
	NOT_ENRAGED(new Enraged(), "Gain &6Speed 1&r. Consuming a &dBlindsided &ror &dBroadsided &ron an enemy causes the next consumption of its opposite to deal 10% more bonus damage (stacking). &bRoll the &bBones Status", Material.CRACKED_STONE_BRICKS);
	//
	
	final StatusType type;
	final String description;
	final ItemStack item;
	
	DefaultStatusType(StatusType type, String description, Material mat) {
		this.type = type;
		this.description = description;
		
		this.item = generateItem(type, description, mat);
	}
	
	DefaultStatusType(StatusType type, String description){
		this(type, description, Material.POTATO);
	}
	
	ItemStack generateItem(StatusType type, String description, Material mat) {
		ItemStack item = new ItemStack(mat);
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(ChatUtils.chat(description));
		meta.setLore(ChatUtils.splitStringAtLength(lore, ChatUtils.getDefaultSplitLength()));
		
		meta.setDisplayName(ChatUtils.chat(ColorCode.STATUS + this.toString()));
		
		item.setItemMeta(meta);
		
		return item;
	}

	public StatusType getType() {
		return type;
	}

	public ItemStack getItem() {
		return item;
	}
	
}
