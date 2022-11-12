package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUserId(int userId);
    boolean existsByEmail(String email);
    boolean existsByEmailAndUserIdIsNot(String email,int userId);



}
