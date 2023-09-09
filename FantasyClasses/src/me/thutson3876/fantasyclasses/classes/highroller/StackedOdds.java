package me.thutson3876.fantasyclasses.classes.highroller;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Blindsided;
import me.thutson3876.fantasyclasses.classes.highroller.statuses.Broadsided;
import me.thutson3876.fantasyclasses.events.RemoveStatusEvent;
import me.thutson3876.fantasyclasses.status.RemoveCause;
import me.thutson3876.fantasyclasses.status.StatusType;

public class StackedOdds extends AbstractAbility {

	public StackedOdds(Player p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setDefaults() {
		// TODO Auto-generated method stub
		
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
			
			ItemStack offhand = player.getInventory().getItemInOffHand();
			if(!offhand.getType().equals(Material.CROSSBOW))
				return;
			CrossbowMeta meta = (CrossbowMeta) offhand.getItemMeta();
			if(meta.hasChargedProjectiles())
				return;
			
			meta.addChargedProjectile(new ItemStack(Material.ARROW));
		}
	}
	
	@Override
	public String getInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getDealsDamage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		// TODO Auto-generated method stub
		
	}

}
