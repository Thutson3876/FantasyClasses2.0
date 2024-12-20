package me.thutson3876.fantasyclasses.professions.hunter;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;

public class FightDressing extends AbstractAbility {

	int dropAmt = 1;
	
	public FightDressing(Player p) {
		super(p);
	}

	@Override
	public void setDefaults() {
		this.coolDowninTicks = 30;
		this.displayName = "Fight Dressing";
		this.skillPointCost = 1;
		this.maximumLevel = 2;

		this.createItemStack(Material.ARROW);
	}

	@EventHandler
	public void EntityDeathEvent(EntityDeathEvent e) {

		LivingEntity ent = e.getEntity();

		if (ent.getKiller() == null)
			return;

		if (!ent.getKiller().equals(player))
			return;

		if (!(ent instanceof Monster))
			return;
		
		if(ent.hasMetadata("noexpdrop"))
			return;
		
		Collection<ItemStack> drops = e.getDrops();
		if (drops == null)
			drops = new ArrayList<>();

		if (!drops.isEmpty()) {
			for(int i = 0; i < dropAmt; i++)
				drops.addAll(drops);
		}

		e.setDroppedExp(e.getDroppedExp() * (dropAmt + 1));

		// player.getWorld().playSound(player.getLocation(), Sound., 0.7f, 1.2f);
	}

	@Override
	public String getInstructions() {
		return "Kill a monster";
	}

	@Override
	public String getDescription() {
		return "Wild monster drops are increased by &6" + (dropAmt + 1) + " &rtimes";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		dropAmt = currentLevel;
	}

}
