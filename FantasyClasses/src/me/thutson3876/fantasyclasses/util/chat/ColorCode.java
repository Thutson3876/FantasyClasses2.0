package me.thutson3876.fantasyclasses.util.chat;

public enum ColorCode {

	DEFAULT("&3"),
	DEFAULT_HIGHLIGHT("&6"),
	DEFAULT_METER("&7"),
	
	ERROR("&c"), 
	SUCCESS("&a"),
	
	ENEMY("&c"),
	ALLY("&a"),
	
	STATUS("&d"), 
	STATUS_APPLIED("&d"), 
	STATUS_RECEIVED("&b"), 
	STATUS_DURATION("&a"),
	STATUS_EXPIRED("&5"),
	STATUS_DISPEL_CAUSE("&e"),
	
	ABILITY_LORE("&r&5"), 
	PROFICIENCY_BONUS("&a"),
	INSTRUCTIONS("&3"),
	BINDABLE("&b"),
	
	BLACK("&0"),
	DARK_BLUE("&1"),
	DARK_GREEN("&2"),
	DARK_AQUA("&3"),
	DARK_RED("&4"),
	DARK_PURPLE("&5"),
	
	GOLD("&6"),
	GRAY("&7"),
	DARK_GRAY("&8"),
	BLUE("&9"),
	
	GREEN("&a"),
	AQUA("&b"),
	RED("&c"),
	LIGHT_PURPLE("&d"),
	YELLOW("&e"),
	WHITE("&f"),
	
	OBFUSCATED("&k"),
	BOLD("&l"),
	STRIKETHROUGH("&m"),
	UNDERLINE("&n"),
	ITALIC("&o"),
	RESET("&r");

	private final String code;

	private ColorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return code;
	}
}
