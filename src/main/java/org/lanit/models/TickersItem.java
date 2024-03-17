package org.lanit.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "ticker", "alerts" })
public class TickersItem{
	private List<AlertsItem> alerts;
	private String ticker;

	public void setAlerts(List<AlertsItem> alerts){
		this.alerts = alerts;
	}

	public List<AlertsItem> getAlerts(){
		return alerts;
	}

	public void setTicker(String ticker){
		this.ticker = ticker;
	}

	public String getTicker(){
		return ticker;
	}
}