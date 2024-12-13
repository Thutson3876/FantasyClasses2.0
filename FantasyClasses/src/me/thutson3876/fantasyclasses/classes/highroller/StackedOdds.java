package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Blindsided;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.util.MaterialLists;

public class StackedOdds extends AbstractAbility {

	public StackedOdds(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Stacked Odds";
		this.skillPointCost = 2;
		this.maximumLevel = 1;

		this.createItemStack(Material.ARROW);
	}

	@EventHandler
	public void onStatusRemoveEvent(RemoveStatusEvent e) {
		StatusType statusType = e.getStatus().getType();
		
		if(!e.getCause().equals(RemoveCause.ABILITY_PLAYER))
			return;
		
		if (!(e.getDispeller() instanceof LivingEntity))
			return;

		LivingEntity leHost = (LivingEntity) e.getDispeller();
		
		if(!this.player.equals(leHost))
			return;
		
		if(statusType instanceof Blindsided || statusType instanceof Broadsided) {
			Player player = (Player) leHost;
			PlayerInventory inv = player.getInventory();
			//player.sendMessage("Cleared " + statusType.getName());
			
			ItemStack offhand = inv.getItemInOffHand();
			if(!offhand.getType().equals(Material.CROSSBOW))
				return;
			CrossbowMeta meta = (CrossbowMeta) offhand.getItemMeta();
			if(meta.hasChargedProjectiles())
				return;
			
			Material arrowType = Material.ACACIA_BOAT;
			for(Material mat : MaterialLists.ARROW.getMaterials())
				if(inv.contains(mat)) {
					arrowType = mat;
					break;
				}
			
			
			if(arrowType == Material.ACACIA_BOAT)
				return;
			
			int idx = inv.first(arrowType);
			ItemStack arrows = inv.getItem(idx);
			int newAmt = arrows.getAmount() - 1;
			
			if(newAmt <= 0)
				arrows = null;
			else
				arrows.setAmount(newAmt);
			
			inv.setItem(idx, arrows);
			
			meta.addChargedProjectile(new ItemStack(Material.ARROW));
			offhand.setItemMeta(meta);
		}
	}
	
	@Override
	public String getInstructions() {
		return "Activate &dBroadsided &ror &dBlindsided";
	}

	@Override
	public String getDescription() {
		return "Instantly refill a crossbow in your offhand with an arrow, if you have any in your inventory";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
