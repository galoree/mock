package org.lanit.models;

import java.util.List;

public class Info{
	private String userID;
	private List<TickersItem> tickers;

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}

	public void setTickers(List<TickersItem> tickers){
		this.tickers = tickers;
	}

	public List<TickersItem> getTickers(){
		return tickers;
	}
}