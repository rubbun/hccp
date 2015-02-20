package com.schedario.bean;

public class FirstImageBean {

	String user_id, id, image_name;

	public FirstImageBean(String id, String image_name) {
		super();
		this.id = id;
		this.image_name = image_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

}
