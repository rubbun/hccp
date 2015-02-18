package com.schedario.bean;

import java.util.ArrayList;

public class RefregeratorBean {

	public String date;
	public ArrayList<RefregeratorSubBean> list = new ArrayList<RefregeratorSubBean>();

	public RefregeratorBean(String date, ArrayList<RefregeratorSubBean> list) {
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

	public ArrayList<RefregeratorSubBean> getList() {
		return list;
	}

	public void setList(ArrayList<RefregeratorSubBean> list) {
		this.list = list;
	}
}
