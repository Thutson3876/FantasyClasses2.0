package me.thutson3876.fantasyclasses.util.metadatavalue;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class NoExpDrop implements MetadataValue {

	@Override
	public boolean asBoolean() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public byte asByte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double asDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float asFloat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int asInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long asLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short asShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Plugin getOwningPlugin() {
		// TODO Auto-generated method stub
		return FantasyClasses.getPlugin();
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object value() {
		// TODO Auto-generated method stub
		return null;
	}

}
