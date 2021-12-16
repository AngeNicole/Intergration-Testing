package rw.ac.rca.termOneExam.utils;

import rw.ac.rca.termOneExam.domain.City;

public class APICustomResponse {

	private boolean status;
	private String message;
	private City data;

	public APICustomResponse(boolean status, String message, City city) {
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public City getData() {
		return data;
	}

	public void setData(City data) {
		this.data = data;
	}
}
