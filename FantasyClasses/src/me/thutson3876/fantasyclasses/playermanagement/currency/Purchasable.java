package me.thutson3876.fantasyclasses.playermanagement.currency;

import org.bukkit.Material;

public class Purchasable {
	
	private final Material material;
	private final int price;
	
	public Purchasable(Material mat, int price) {
		this.material = mat;
		this.price = price;
	}

	public Material getMaterial() {
		return material;
	}

	public int getPrice() {
		return price;
	}

}
