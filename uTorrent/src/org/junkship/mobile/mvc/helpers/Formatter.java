package org.junkship.mobile.mvc.helpers;

public class Formatter {
	
	public static String printDouble(double value){
		Double nD = new Double(value);
		double temp = value-nD.intValue();
		Double fD = new Double(temp*10);
		return nD.intValue()+"."+fD.intValue();
	}
	
	public static String printTimeSpan(long seconds) {
		
		if (seconds<0) {
			return "INF";
		}
		long weeks = seconds / 604800;
		if (weeks > 0) seconds %= 604800;
		long days = seconds / 86400;
		if (days > 0) seconds %= 86400;
		long hours = seconds / 3600;
		if (hours > 0) seconds %= 3600;
		long minutes = seconds / 60;
		if (minutes > 0) seconds %= 60;
		
		String result = weeks > 0 ? (Long.toString(weeks) + "w ") : "";
		result += days > 0 ? (Long.toString(days) + "d ") : "";
		result+=Long.toString(hours)+":"+Long.toString(minutes)+":"+Long.toString(seconds);
		
		return result;
	}

}
