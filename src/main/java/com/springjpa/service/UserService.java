package com.springjpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springjpa.entity.Direction;
import com.springjpa.entity.CellPhone;
import com.springjpa.entity.User;
import com.springjpa.entity.UserControl;
import com.springjpa.repository.IDirection;
import com.springjpa.repository.ICellPhone;
import com.springjpa.repository.IUser;

@Service
public class UserService {

	@Autowired
	private IUser userDao;
	
	@Autowired
	private IDirection directionDao;
	
	@Autowired
	private ICellPhone cellPhoneDao;
	
	@Autowired
	private UserControl userControl;
	
	private List<Integer> errorNumber = new ArrayList<Integer>();
	
	private List<String> messageError = new ArrayList<String>();
	
	//****
	
	//Return user object recovered or validation errors
	@Transactional
	public UserControl addUser(User user) {
		
		//It is validated first
		if (validateUser(user)) {
			
			return userControl;
		}
		
		//For adding User this value must be 0
		if (user.getIdUser() == 0) {
			
			Optional <User> optUser = Optional.of(userDao.save(user));
			
			if (optUser.isPresent()) {
				
				errorNumber.add(0);
				messageError.add("Alta realizada satisfactoriamente");
				
				userControl.setIdUser(optUser.get().getIdUser());
				userControl.setFirstAndSecondName(user.getFirstAndSecondName());
				userControl.setPassword(user.getPassword());
				userControl.setDirection(user.getDirection());
				userControl.setCellPhoneList(user.getCellPhoneList());

			}else {
				
				errorNumber.add(6);
				messageError.add("Ha habido un fallo durante el proceso de persistencia del nuevo Usuario. Favor reintentar o contactar a Soporte Técnico");
				
			}
			
		}else {
			
			errorNumber.add(1);
			messageError.add("Se ha recibido un Id de Usuario inválido. No debe especificarse para operaciones de Alta");
			userControl.setNumerErrorList(errorNumber);
			userControl.setMessageErrorList(messageError);
			
		}
		
		return userControl;
	}

	//If we find some error, the userControl Object store every message to notify the issue
	//Return 
	private boolean validateUser(User user) {
		
		boolean errorStatus = false;
		
		if (user.getFirstAndSecondName().isEmpty()) {
			
			errorNumber.add(2);
			messageError.add("No se ha indicado un nombre de Usuario");
			
			errorStatus = true;
		}
		
		if (user.getPassword().isEmpty()) {
			
			errorNumber.add(3);
			messageError.add("Por seguridad debe proporcionarse una contraseña");
			
			errorStatus = true;
		}
		
		if (user.getDirection().getIdDirection() == 0) {
			
			errorNumber.add(4);
			messageError.add("No se ha especificado una Dirección para el Usuario");
			
			errorStatus = true;
			
		}else {

			Optional<Direction> optDirection = directionDao.findById(user.getDirection().getIdDirection());
			
			if (optDirection.isPresent()){
				
				userControl.setDirection(optDirection.get());
				
			}else {
				
				errorNumber.add(4);
				messageError.add("Se ha especificado una Dirección inválida para el Usuario");
				
				errorStatus = true;
				
			}
		}
		
		//Verifying if there is at leat one cellular phone specified
		if (user.getCellPhoneList().isEmpty()) {
			
			errorNumber.add(5);
			messageError.add("Se debe indicar al menos un teléfono móvil al Usuario");
			
			errorStatus = true;
			
		}else {
				
			//Comparing Cell Phone Id into DB with Cell Phone Id vía API Rest Client
			for (CellPhone cp : user.getCellPhoneList()) {
				
				Optional<CellPhone> optCellPhone = cellPhoneDao.findById(cp.getIdCellPhone());
				
				if (optCellPhone.isPresent()) {
					
					userControl.getCellPhoneList().add(cp);
					
				}else {
					
					errorNumber.add(5);
					messageError.add("El Teléfono Móvil especificado no existe");
					errorStatus = true;
				}
			}
		}
			
		userControl.setNumerErrorList(errorNumber);
		userControl.setMessageErrorList(messageError);
		
		return errorStatus;
	}
}
