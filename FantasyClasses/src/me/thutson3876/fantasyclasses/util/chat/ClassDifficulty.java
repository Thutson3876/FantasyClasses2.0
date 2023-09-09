package me.thutson3876.fantasyclasses.util.chat;

public enum ClassDifficulty {

	EASY(ColorCode.GREEN),
	MEDIUM(ColorCode.YELLOW),
	HARD(ColorCode.RED);
	
	final ColorCode color;
	
	ClassDifficulty(ColorCode color) {
		this.color = color;
	}

	public ColorCode getColor() {
		return color;
	}
	
}
