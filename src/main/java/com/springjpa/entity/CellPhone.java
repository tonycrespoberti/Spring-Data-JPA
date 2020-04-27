package com.springjpa.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CELLPHONE")
//It is assumed each user has at least one cellphone.
public class CellPhone implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CELLPHONE_ID")
	private int idCellPhone;
	
	@Column(name = "MODEL")
	private String model;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;

	//**********
	
	public CellPhone() {
		
	}

	public CellPhone(int idCellPhone, String model, User user) {
		super();
		this.idCellPhone = idCellPhone;
		this.model = model;
		this.user = user;
	}

	public int getIdCellPhone() {
		return idCellPhone;
	}

	public void setIdCellPhone(int idCellPhone) {
		this.idCellPhone = idCellPhone;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CellPhone [idCellPhone=" + idCellPhone + ", model=" + model + ", user=" + user + "]";
	}
}
