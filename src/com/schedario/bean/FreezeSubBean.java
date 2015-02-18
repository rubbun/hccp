package com.schedario.bean;

public class FreezeSubBean {

	public String freeze_id, freez_temp;

	public FreezeSubBean(String refregerator_id, String ref_temp) {
		super();
		this.freeze_id = refregerator_id;
		this.freez_temp = ref_temp;
	}

	public String getRefregerator_id() {
		return freeze_id;
	}

	public void setRefregerator_id(String refregerator_id) {
		this.freeze_id = refregerator_id;
	}

	public String getRef_temp() {
		return freez_temp;
	}

	public void setRef_temp(String ref_temp) {
		this.freez_temp = ref_temp;
	}

}
