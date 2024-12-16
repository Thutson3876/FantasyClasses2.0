package me.thutson3876.fantasyclasses.classes.witch;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.joml.Random;

import me.thutson3876.fantasyclasses.abilities.AbstractAbility;
import me.thutson3876.fantasyclasses.abilities.Priority;
import me.thutson3876.fantasyclasses.util.chat.ChatUtils;

public class TasteTest extends AbstractAbility {
	
	public TasteTest(Player p) {
		super(p, Priority.LOW);
	}
	
	@Override
	public void setDefaults() {
		this.coolDowninTicks = 0;
		this.displayName = "Taste Test";
		this.skillPointCost = 1;
		this.maximumLevel = 1;

		this.createItemStack(Material.SUSPICIOUS_STEW);	
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(!e.getPlayer().equals(player))
			return;
		
		if(!e.getItem().getType().equals(Material.SUSPICIOUS_STEW))
			return;
		
		Random rng = new Random();
		WitchBrewRecipe recipe = WitchBrewRecipe.values()[rng.nextInt(WitchBrewRecipe.values().length)];
		
		String message = "&dYou're sensing... ";
				
		for(Material mat : recipe.getIngredients()) {
			message += "&6" + mat.toString().toLowerCase() + "&d... ";
		}
		player.sendMessage(ChatUtils.chat(message));
		
		this.onTrigger(true);
	}

	@Override
	public String getInstructions() {
		return "Eat suspicious stew";
	}

	@Override
	public String getDescription() {
		return "Whenever you consume &6Suspicious Stew &r, you gain insight into a random witch's brew recipe";
	}

	@Override
	public boolean getDealsDamage() {
		return false;
	}

	@Override
	public void applyLevelModifiers() {
		
	}

}
