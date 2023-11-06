package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {
  @Override
  public void createUser(UserEntity user) {
  }

  @Override
  public AuthUserEntity updateUser(AuthUserEntity user) {
    return null;
  }

  @Override
  public void deleteUser(UUID userId) {

  }
  @Override
  public void deleteUser(String userName) {
  }

  @Override
  public AuthUserEntity getUserById(UUID userId) {
    return null;
  }

}
