package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.db.model.UserDataUserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

  void createUserInUserData(UserDataUserEntity user);
  UserDataUserEntity updateUserInUserData(UserDataUserEntity user);
  void deleteUserInUserData(String userName);
  UserDataUserEntity getUserByIdInUserData(UUID userId);
}
