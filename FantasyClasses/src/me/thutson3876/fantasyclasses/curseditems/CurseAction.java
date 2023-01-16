package me.thutson3876.fantasyclasses.curseditems;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

public interface CurseAction {

	public void action(Event e, LivingEntity owner);
}
