package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.AuthUserEntity;

import java.util.UUID;

public class AuthUserDAOHibernate implements AuthUserDAO {
  @Override
  public void createUser(AuthUserEntity user) {
  }

  @Override
  public AuthUserEntity updateUser(AuthUserEntity user) {
    return null;
  }

  @Override
  public void deleteUser(UUID userId) {

  }

  @Override
  public AuthUserEntity getUserById(UUID userId) {
    return null;
  }

}
