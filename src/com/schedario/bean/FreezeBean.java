package com.schedario.bean;

import java.util.ArrayList;

public class FreezeBean {

	public String date;
	public ArrayList<FreezeSubBean> list = new ArrayList<FreezeSubBean>();

	public FreezeBean(String date, ArrayList<FreezeSubBean> list) {
		super();
		this.date = date;
		this.list = list;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<FreezeSubBean> getList() {
		return list;
	}

	public void setList(ArrayList<FreezeSubBean> list) {
		this.list = list;
	}
}
