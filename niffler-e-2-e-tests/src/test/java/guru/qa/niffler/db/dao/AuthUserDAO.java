package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface AuthUserDAO {
  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  void createUser(AuthUserEntity user);
  AuthUserEntity updateUser(AuthUserEntity user);
  void deleteUser(UUID userId);
  AuthUserEntity getUserById(UUID userId);
}
