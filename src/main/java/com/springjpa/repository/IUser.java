package com.springjpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springjpa.entity.User;

@Repository
public interface IUser extends JpaRepository<User, Long>{

	Optional<User> findById(Long idUser);
	
	List<User> findAll();
	
	@Query(value = "select u from User u where u.firstAndSecondName like %?1%")
	List<User> findByFirstnameContaining(String expressionMatch);
	
}
