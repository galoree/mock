package org.lanit.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeFrame", "percent" })
public class AlertsItem{
	private int percent;
	private int timeFrame;

	public void setPercent(int percent){
		this.percent = percent;
	}

	public int getPercent(){
		return percent;
	}

	public void setTimeFrame(int timeFrame){
		this.timeFrame = timeFrame;
	}

	public int getTimeFrame(){
		return timeFrame;
	}
}
