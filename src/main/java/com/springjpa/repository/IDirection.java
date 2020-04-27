package com.springjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springjpa.entity.Direction;

@Repository
public interface IDirection extends JpaRepository<Direction, Long>{

}
