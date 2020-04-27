package com.springjpa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long idUser;
	
	@Column(name = "FULL_NAME")
	private String firstAndSecondName;

	@Column(name = "PASSWORD")
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "DIRECTION_ID")
	private Direction direction;
	
	@OneToMany(mappedBy = "user")
	private List<CellPhone> cellPhoneList;
	
	//**********

	public User() {

	}

	public User(Long idUser, String firstAndSecondName, String password, Direction direction,
			List<CellPhone> cellPhoneList) {
		this.idUser = idUser;
		this.firstAndSecondName = firstAndSecondName;
		this.password = password;
		this.direction = direction;
		this.cellPhoneList = cellPhoneList;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getFirstAndSecondName() {
		return firstAndSecondName;
	}

	public void setFirstAndSecondName(String firstAndSecondName) {
		this.firstAndSecondName = firstAndSecondName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public List<CellPhone> getCellPhoneList() {
		return cellPhoneList;
	}

	public void setCellPhoneList(List<CellPhone> cellPhoneList) {
		this.cellPhoneList = cellPhoneList;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", firstAndSecondName=" + firstAndSecondName + ", password=" + password
				+ ", direction=" + direction + ", cellPhoneList=" + cellPhoneList + "]";
	}
}
