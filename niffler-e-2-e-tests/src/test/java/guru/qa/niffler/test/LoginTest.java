package guru.qa.niffler.test;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.Dao;
import guru.qa.niffler.jupiter.DaoExtension;
import guru.qa.niffler.jupiter.web.BaseWebTest;
import guru.qa.niffler.model.pages.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

@ExtendWith(DaoExtension.class)
public class LoginTest extends BaseWebTest {

  @Dao
  private AuthUserDAO authUserDAO;
  private UserEntity user;
  MainPage mainPage;
  @BeforeEach
  void createUser() {
    user = new UserEntity()
        .setUsername("valentin_0")
        .setPassword("12345")
        .setEnabled(true)
        .setAccountNonExpired(true)
        .setAccountNonLocked(true)
        .setCredentialsNonExpired(true)
        .setAuthorities(Arrays.stream(Authority.values())
            .map(a -> new AuthorityEntity().setAuthority(a)).toList());
    authUserDAO.createUser(user);
  }
  @AfterEach
  void deleteUser() {
    authUserDAO.deleteUserById(user.getId());
  }

  @Test
  void mainPageShouldBeVisibleAfterLogin() {
    mainPage = startApplication();
    mainPage.logIn(user);
  }
}
