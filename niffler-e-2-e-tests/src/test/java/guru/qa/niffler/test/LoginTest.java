package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.Dao;
import guru.qa.niffler.jupiter.dbUser.DBUser;
import guru.qa.niffler.jupiter.web.BaseWebTest;
import guru.qa.niffler.model.pages.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class LoginTest extends BaseWebTest {

  @Dao
  private AuthUserDAO authUserDao;
  @Dao
  private UserDataUserDAO userDataUserDAO;
  private UserEntity user;

  private MainPage mainPage;

  @BeforeEach
  void createUser() {
    user = new UserEntity()
        .setUsername("valentin_1")
        .setPassword("12345")
        .setEnabled(true)
        .setAccountNonExpired(true)
        .setAccountNonLocked(true)
        .setCredentialsNonExpired(true)
        .setAuthorities(Arrays.stream(Authority.values())
            .map(a-> new AuthorityEntity().setAuthority(a)).toList());
    authUserDao.createUser(user);
    userDataUserDAO.createUserInUserData(user);
  }

  @AfterEach
  void deleteUser() {
    userDataUserDAO.deleteUserInUserData(user.getUsername());
    authUserDao.deleteUser(user.getId());
  }

  @AllureId("106")
  @Test
  void mainPageShouldBeVisibleAfterLogin(AuthUserEntity createdUser) {
    mainPage = startApplication();
    mainPage.logIn(createdUser);
  }
}
