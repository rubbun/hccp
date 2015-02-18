package com.schedario.bean;

public class LabBean {

	String id, result, sample, responsible_food_safety, added_date;

	public LabBean(String id, String result, String sample,
			String responsible_food_safety, String added_date) {
		super();
		this.id = id;
		this.result = result;
		this.sample = sample;
		this.responsible_food_safety = responsible_food_safety;
		this.added_date = added_date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public String getResponsible_food_safety() {
		return responsible_food_safety;
	}

	public void setResponsible_food_safety(String responsible_food_safety) {
		this.responsible_food_safety = responsible_food_safety;
	}

	public String getAdded_date() {
		return added_date;
	}

	public void setAdded_date(String added_date) {
		this.added_date = added_date;
	}

}
