package com.schedario.bean;

public class RefregeratorSubBean {

	public String refregerator_id, ref_temp;

	public RefregeratorSubBean(String refregerator_id, String ref_temp) {
		super();
		this.refregerator_id = refregerator_id;
		this.ref_temp = ref_temp;
	}

	public String getRefregerator_id() {
		return refregerator_id;
	}

	public void setRefregerator_id(String refregerator_id) {
		this.refregerator_id = refregerator_id;
	}

	public String getRef_temp() {
		return ref_temp;
	}

	public void setRef_temp(String ref_temp) {
		this.ref_temp = ref_temp;
	}

}
