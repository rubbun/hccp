package com.schedario.bean;

public class MeasureBean {

	String id, name, description, added_hour, added_date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdded_hour() {
		return added_hour;
	}

	public void setAdded_hour(String added_hour) {
		this.added_hour = added_hour;
	}

	public String getAdded_date() {
		return added_date;
	}

	public void setAdded_date(String added_date) {
		this.added_date = added_date;
	}

	public MeasureBean(String id, String result, String sample,
			String responsible_food_safety, String added_date) {
		super();
		this.id = id;
		this.name = result;
		this.description = sample;
		this.added_hour = responsible_food_safety;
		this.added_date = added_date;
	}

	

}
