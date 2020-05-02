/**
 * Author: Tony Crespo, tonycrespo@outlook.com
 */
package com.springjpa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springjpa.entity.Direction;
import com.springjpa.entity.StatusTransaction;
import com.springjpa.entity.CellPhone;
import com.springjpa.entity.User;
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
	private StatusTransaction statusTrans;
	
	private Map<Integer, String> resultTransaction = new HashMap<Integer, String>();
	
	//****
	

	@Transactional
	//Method for adding new users. Return certain code errors. See validate method for details.
	public StatusTransaction addUser(User user) {
		
		System.out.println("User : " + user);
		
		//It is validated first
		if (validateUser(user, 0)) {
			
			return statusTrans;
		} 
		
		if (user.getIdUser() !=null) {
			
			resultTransaction.put(1, "Se ha recibido el Id de Usuario: " + user.getIdUser() + " inválido. No debe ser especificado para operaciones de Alta");
			
		}
		
		//To be sure after validation that both Ids are zero for new User and Direction
		Direction direction = new Direction();
		
		direction = user.getDirection();
		
		direction.setIdDirection(0L);
		
		user.setIdUser(0L);
		user.setDirection(direction); 
		Optional <User> optUser = Optional.of(userDao.save(user));
			
		if (optUser.isPresent()) {
				
			resultTransaction.put(0, "Alta del Usuario realizada satisfactoriamente");
				
			statusTrans.setGenericObject(optUser.get());
			statusTrans.setResultTransaction(resultTransaction);
				
			return statusTrans;
				
		}else {
				
			resultTransaction.put(12, "Ha habido un fallo durante el proceso de persistencia del nuevo Usuario. Favor reintentar o contactar a Soporte Técnico");
				
		}

		statusTrans.setResultTransaction(resultTransaction);
		statusTrans.setGenericObject(user);
		
		return statusTrans;
	}
	
	/**
	 * DELETE User
	 * @param user
	 * @return
	 */
	@Transactional
	public StatusTransaction deleteUser(String dni) {
		
		resultTransaction.clear();
		
		Optional<User> optUser = userDao.findByDni(dni);
		
		if (optUser.isPresent()){
			
			userDao.deleteByDni(dni);
			
			resultTransaction.put(0, "EL Usuario con DNI: " + optUser.get().getDni() + " a sido eliminado correctamente.");
			
			statusTrans.setGenericObject(optUser.get());
			statusTrans.setResultTransaction(resultTransaction);
			
		}else {
		
			resultTransaction.put(1, "EL Usuario con DNI: " + dni + " NO EXISTE, favor confirmar el documento de identidad.");
		
			statusTrans.setResultTransaction(resultTransaction);
		}
		
		return statusTrans;
	}
	
	
	//UPDATE User
	@Transactional
	public StatusTransaction updateUser(User user) {
		
		resultTransaction.clear();
		
		if (validateUser(user, 1)) {
			
			return statusTrans;
		}
		
		Optional<User> optUser = Optional.of(userDao.save(user));
		
		if (optUser.isPresent()) {
			
			resultTransaction.put(0, "Los datos del Usuario han sido actualizados correctamente.");
			statusTrans.setGenericObject(optUser.get());
			
		}else {
			
			resultTransaction.put(13, "Ha habido un fallo intentando actualizar los datos del Usuario.");
		}
		
		statusTrans.setResultTransaction(resultTransaction);
		
		return statusTrans; 
	}
	
	/**
	 * List All User
	 * @return List of object users
	 */
	public List<User> listUsers(){
		
		return userDao.findByJoinUserDirection();
	}
	
	
	//Find By DNI
	public StatusTransaction findUser(String dni) {
	
		if (dni != null) {
		
			Optional<User> optUser = userDao.findByDni(dni);
			
			if (optUser.isPresent()){
				
				statusTrans.setGenericObject(optUser.get());
				
				resultTransaction.put(0, "Búsqueda exitosa.");
				
				statusTrans.setResultTransaction(resultTransaction);
				
			}else {
			
				resultTransaction.put(1, "EL Usuario con DNI: " + dni + " NO EXISTE, favor confirmar el documento de identidad.");
			
				statusTrans.setResultTransaction(resultTransaction);
			}
		}else {
			
			resultTransaction.put(2, "No ha especificado el DNI para ubicar los datos del Usuario");
			
			statusTrans.setResultTransaction(resultTransaction);	
			
		}
		
		return statusTrans;
	}

	//If we find some error, the userControl Object store every message to notify the issue
	//Param: Object User and type transaction new = 0 or update = 1.
	//Return code error 2 for not fullname defined, 3 for wrong password, 4 there is not Id direction associated
	//5 for wrong Id direction 
	private boolean validateUser(User user, int typeTransaction) {
		
		boolean errorStatus = false;
		
		resultTransaction.clear();
		
		//Attempting to create a new User
		if (typeTransaction == 0) {
			
			try {
				if (user.getDni().equals(null)) {
	
					resultTransaction.put(6, "El Documento de Identidad DNI: " + user.getDni() + " es inválido.");
					
					errorStatus = true;
					
				}else {
					
					Optional<User> optUser = userDao.findByDni(user.getDni());
	
					if (optUser.isPresent()) {
	
						resultTransaction.put(7, "Ya existe el Documento de Identidad DNI: " + user.getDni() + " suministrado");
						
						errorStatus = true;
					}
					
				}
			}catch (NullPointerException NPE){
				
				System.out.println("DNI no especificado durante un proceso de Create/Update User: " + NPE);
				
			}
		}
			
		if (user.getFirstAndSecondName().isEmpty()) {
			
			resultTransaction.put(2, "No se ha indicado un nombre de Usuario");
			
			errorStatus = true;
		}
		
		if (user.getPassword().isEmpty()) {
			
			resultTransaction.put(3, "Por seguridad debe proporcionarse una contraseña");

			errorStatus = true;
		}
		
		//OneToOne relations between User and Direction. This data has to be sent for adding or updating users.
		//If it is for create a new user we have to receive every Direction attribute from client.
		//For updating we validate if the id Direction is already exist
		if (typeTransaction == 1) {
			
			if (user.getDirection().getIdDirection() != null) {
				
				Optional<Direction> optDirection = directionDao.findById(user.getDirection().getIdDirection());
				
				//Retrieving and Direction object for update
				if (optDirection.isPresent()){
					
					user.setDirection(optDirection.get());
					
				}else {
					
					//If we trying to add or update a new user, we have to identify which is of both of process to make insertion or updating direction object attribute to persist it
					//As a consequence id direction was not DDBB found is a new user with its new direction. We validate every direction attribute
					if (user.getDirection().getLocation().isEmpty()) {
	
						resultTransaction.put(8, "La Localidad de la Dirección es inválida");
	
						errorStatus = true;
	
					}
					
					if (optDirection.get().getNumber().isEmpty()) {
								
						resultTransaction.put(9, "El Número de la Dirección es inválido");
	
						errorStatus = true;
	
					}
								
					if (optDirection.get().getStreet().isEmpty()) {
					
						resultTransaction.put(10, "La Calle/Avenida de la Dirección es inválida");
	
						errorStatus = true;
	
					}
				
				if (optDirection.get().getZipCode().isEmpty()) {
					
					resultTransaction.put(11, "El Código Postal de la Dirección es inválido");
	
					errorStatus = true;
	
					}
				}
			}else {
				
				resultTransaction.put(4, "Para actualizar los datos del Usuario se requiere proporcionar el Id de su Dirección");
				
				errorStatus = true;
				
			}
			
		}else {
			
			try {
				
				if (user.getDirection().getIdDirection() != null) {
					
					resultTransaction.put(12, "Para añadir un nuevo Usuario no debe definir el Id de la Dirección");
					
					errorStatus = true;
				}
				
			} catch (Exception e) {
				
				System.out.println("Ok para evitar un Null Pointer Exception. Id Direccion nulo al intentar crear un Usuario");
			}
			
		}
				
		statusTrans.setResultTransaction(resultTransaction);
		statusTrans.setGenericObject(user);
		
		return errorStatus;
	}
}
