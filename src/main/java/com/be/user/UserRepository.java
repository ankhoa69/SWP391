package com.be.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE role = ?", nativeQuery = true)
    List<UserEntity> findByRole(String role);

    UserEntity findById(long id);


}
