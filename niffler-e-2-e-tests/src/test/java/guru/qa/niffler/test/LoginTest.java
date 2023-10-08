package guru.qa.niffler.test;

import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.jupiter.dbUser.DBUser;
import guru.qa.niffler.jupiter.web.BaseWebTest;
import guru.qa.niffler.model.pages.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class LoginTest extends BaseWebTest {

  MainPage mainPage;

  @DBUser(
      username = "capets4",
      password = "12345"
  )
  @AllureId("106")
  @Test
  void mainPageShouldBeVisibleAfterLogin(AuthUserEntity createdUser) {
    mainPage = startApplication();
    mainPage.logIn(createdUser);
  }
}
