package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.db.model.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {
  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  void createUser(UserEntity user);
  AuthUserEntity updateUser(AuthUserEntity user);
  void deleteUser(UUID userId);
  void deleteUser(String userName);
  AuthUserEntity getUserById(UUID userId);
}
