package me.thutson3876.fantasyclasses.status;

public class StatusData {

	//stores any and all data needs for any status effect
	private int[] intArr = new int[3];
	private double[] doubleArr = new double[3];
	
	public StatusData() {
		
	}
	
	public StatusData(int[] intArr, double[] doubleArr) {
		this.intArr = intArr;
		this.doubleArr = doubleArr;
	}

	public int[] getIntArr() {
		return intArr;
	}

	public void setIntArr(int[] intArr) {
		this.intArr = intArr;
	}

	public double[] getDoubleArr() {
		return doubleArr;
	}

	public void setDoubleArr(double[] doubleArr) {
		this.doubleArr = doubleArr;
	}
	
	public int getInt(int index) {
		if(index < 0 || index > intArr.length)
			return -1;
		
		return intArr[index];
	}
	
	public void setInt(int index, int value) {
		if(index < 0 || index > intArr.length)
			return;
		
		intArr[index] = value;
	}
	
	public double getDouble(int index) {
		if(index < 0 || index > doubleArr.length)
			return -1;
		
		return doubleArr[index];
	}
	
	public void setDouble(int index, double value) {
		if(index < 0 || index > doubleArr.length)
			return;
		
		doubleArr[index] = value;
	}
}
