package com.springjpa.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserControl extends User {

	private static final long serialVersionUID = 1L;

	//private User user;
	
	private List<String> messageErrorList = new ArrayList<String>();

	private List<Integer> numerErrorList = new ArrayList<Integer>();
	
	//****
	
	public UserControl() {
		
	}

	/*
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
*/
	
	public List<String> getMessageErrorList() {
		return messageErrorList;
	}

	public void setMessageErrorList(List<String> messageErrorList) {
		this.messageErrorList = messageErrorList;
	}

	public List<Integer> getNumerErrorList() {
		return numerErrorList;
	}

	public void setNumerErrorList(List<Integer> numerErrorList) {
		this.numerErrorList = numerErrorList;
	}

}
