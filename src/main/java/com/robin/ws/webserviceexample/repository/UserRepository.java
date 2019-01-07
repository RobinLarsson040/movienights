package com.robin.ws.webserviceexample.repository;

import com.robin.ws.webserviceexample.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);
    UserEntity findByUserId(String id);
}
