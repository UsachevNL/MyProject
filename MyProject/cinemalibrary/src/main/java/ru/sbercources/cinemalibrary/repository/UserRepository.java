package ru.sbercources.cinemalibrary.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sbercources.cinemalibrary.model.User;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User> {

   List<User> findAllByFirstName(String firstName);
   List<User> findAllByFirstNameAndMiddleName(String firstName, String middleName);
   @Query(nativeQuery = true, value = """
    select * from users where created_by = :createdBy
  """)
   List<User> findAllByCreatedBy(@Param(value = "createdBy") String createdBy);

   @Query(nativeQuery = true, value = """
    select * from users where login = :login and is_deleted = false
  """)
   User findUserByLoginAndDeletedFalse(@Param(value = "login") String login);

   User findByEmail(String email);
}
