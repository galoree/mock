package org.lanit.models;

public class Delete{
	private String tickerName;
	private int alertIndex;

	public void setTickerName(String tickerName){
		this.tickerName = tickerName;
	}

	public String getTickerName(){
		return tickerName;
	}

	public void setAlertIndex(int alertIndex){
		this.alertIndex = alertIndex;
	}

	public int getAlertIndex(){
		return alertIndex;
	}
}
