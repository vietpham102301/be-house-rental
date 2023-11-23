package com.example.houserental.internal.repositories.user;

import com.example.houserental.internal.models.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByUsernameOrEmailOrPhoneOrIdNumber(String username, String email, String phone, String idNumber);

    @Modifying
    @Query("UPDATE User u SET u.status = 'Inactive' WHERE u.id = ?1")
    int updateUserStatusById(Integer userId);

    @Modifying
    @Query(value="UPDATE users u SET u.birthdate = :birthDate, u.gender = :gender, " +
            "u.email = :email, u.phone = :phone, u.id_number = :idNumber, " +
            "u.name = :name, " +
            "u.role = :role, " +
            "u.username = :username, " +
            "u.started_date = :startedDate, u.status = :status, " +
            "u.description = :description, u.password = :password WHERE u.id = :userId", nativeQuery = true)
    int updateUserById(@Param("userId") Integer userId, @Param("birthDate") Date birthDate,
                       @Param("gender") String gender, @Param("email") String email,
                       @Param("phone") String phone, @Param("idNumber") String idNumber,
                       @Param("startedDate") Date startedDate, @Param("status") String status,
                       @Param("description") String description, @Param("password") String password,
                       @Param("name") String name, @Param("role") String role, @Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.phone LIKE %?1%"
            + " OR u.idNumber LIKE %?1%"
            + " OR u.description LIKE %?1%")
    List<User> searchUsers(String keywords);


    List<User> findAllByStatus(String status, Pageable page);

    User findUserById(int id);


}
