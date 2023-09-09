package me.thutson3876.fantasyclasses.events;

import java.util.UUID;

import org.bukkit.attribute.AttributeModifier.Operation;

public class DamageModifier {
	private final UUID id;
	private final String name;
	private final Operation operation;
	private final double value;

	public DamageModifier(String name, Operation operation, double value) {
		id = UUID.randomUUID();

		this.name = name;
		this.operation = operation;
		this.value = value;
	}

	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Operation getOperation() {
		return operation;
	}

	public double getValue() {
		return value;
	}

	public double getResult(double valueToModify) {
		switch (operation) {
		case ADD_NUMBER:
			return value;

		case ADD_SCALAR:
			return valueToModify * (value);

		case MULTIPLY_SCALAR_1:
			return valueToModify * (value);

		default:
			return valueToModify;

		}
	}

}
