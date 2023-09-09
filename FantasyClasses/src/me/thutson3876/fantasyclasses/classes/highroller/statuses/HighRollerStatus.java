package me.thutson3876.fantasyclasses.classes.highroller.statuses;

import me.thutson3876.fantasyclasses.status.StatusType;
import me.thutson3876.fantasyclasses.status.StatusEffect;

public class HighRollerStatus extends StatusType {

	protected HighRollerStatus(String name, double tickRate, 
			StatusEffect tickEffect, StatusEffect applicationEffect) {
		super(name, tickRate, 1, tickEffect, applicationEffect);
	}
	
	

}
