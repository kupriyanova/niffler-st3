package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

  void createUserInUserData(UserEntity user);
  UserEntity updateUserInUserData(UserEntity user);
  void deleteUserInUserData(String userName);
  UserEntity getUserByIdInUserData(UUID userId);
}
