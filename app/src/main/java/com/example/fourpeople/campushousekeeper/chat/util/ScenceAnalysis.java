package com.example.fourpeople.campushousekeeper.chat.util;

import java.util.ArrayList;
import java.util.List;

public class ScenceAnalysis {
	public List<String> ScenceData = new ArrayList<String>();
	public String conversation = null;

	public ScenceAnalysis(List<String> ScenceData) {
		// TODO Auto-generated constructor stub
		this.ScenceData = ScenceData;
	}

	public String getConversation(int index) {
		conversation = ScenceData.get(index).toString();
		return conversation;
	}

	public String getScenceBackground() {
		return ScenceData.get(0).toString();
	}
	
}
