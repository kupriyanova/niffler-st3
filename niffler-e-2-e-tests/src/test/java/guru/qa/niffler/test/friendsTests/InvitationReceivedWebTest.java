package guru.qa.niffler.test.friendsTests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.user.User;
import guru.qa.niffler.jupiter.web.BaseWebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.pages.MainPage;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.user.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvitationReceivedWebTest extends BaseWebTest {

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
  }
  @Test
  @AllureId("103")
  void friendShouldBeDisplayedInTable(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
    MainPage mainPage = new MainPage();
    mainPage.logIn(userForTest).openFriendsPage();
    assertFalse(mainPage.getListFriends().isEmpty(),
        "У этого пользователя есть приглашения друзей, список не должен быть пуст.");

    mainPage.openPeoplePage();
    mainPage.getActiveStatuses().shouldHave(CollectionCondition.texts("You are friends"));
  }

}
