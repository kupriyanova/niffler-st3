package guru.qa.niffler.test.friendsTests;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.niffler.jupiter.user.User;
import guru.qa.niffler.jupiter.web.BaseWebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pages.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.user.User.UserType.*;

public class FriendsWebTest extends BaseWebTest {

  MainPage mainPage;
  @BeforeEach
  void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
    mainPage = startApplication();
    mainPage.logIn(userForTest);
    mainPage.openFriendsPage();
  }
  @Test
  @AllureId("101")
  void friendShouldBeDisplayedInTable(@User(userType = WITH_FRIENDS) UserJson userForTest) {
    List<String> friendsUserName = userForTest.getFriendsUserName();
    $$(byText("You are friends")).shouldHave(CollectionCondition.size(friendsUserName.size()));

    mainPage.openPeoplePage();
    $$(byText("You are friends")).shouldHave(CollectionCondition.size(friendsUserName.size()));
  }
}
