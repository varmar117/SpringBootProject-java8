package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.resources.exceptions.DataBaseException;
import com.example.demo.services.exceptions.ResourceNotFoundException;


@Service
public class UserService {
	@Autowired
	private UserRepository repository;
	
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	
	public User findById(long id) {
	Optional<User> obj =repository.findById(id);
	return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public User insert (User obj) {
		
		return repository.save(obj);
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException(e.getMessage());
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User update(Long id, User obj) {
		
		User entity = repository.getOne(id);
		updtateData(entity, obj);
		return repository.save(entity);
	}

	private void updtateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
		
	}
	

}
