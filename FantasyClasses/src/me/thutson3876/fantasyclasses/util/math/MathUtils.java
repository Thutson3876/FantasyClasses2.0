package me.thutson3876.fantasyclasses.util.math;

import me.thutson3876.fantasyclasses.util.AbilityUtils;

public class MathUtils {

	public static double convertToPercent(double num, int decimalCount) {
		return AbilityUtils.doubleRoundToXDecimals(num * 100.0, decimalCount);
	}
	
	public static double convertToDurationInSeconds(int num, int decimalCount) {
		return AbilityUtils.doubleRoundToXDecimals(((double)num) / 20.0, decimalCount);
	}
}
