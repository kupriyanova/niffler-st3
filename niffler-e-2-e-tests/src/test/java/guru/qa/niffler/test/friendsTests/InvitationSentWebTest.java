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

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.jupiter.user.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvitationSentWebTest extends BaseWebTest {
  MainPage mainPage;
  @BeforeEach
  void beforeTestMethod() {
    mainPage = startApplication();
    mainPage.logIn();
    mainPage.openFriendsPage();
  }
  @Test
  @AllureId("101")
  void friendShouldBeDisplayedInTable(@User(userType = INVITATION_SENT) UserJson userForTest) {

    assertFalse($$("table tr td:nth-child(2)").isEmpty(),
        "У этого пользователя есть принятые друзья, список не должен быть пуст.");
    List<String> friendsUserName = userForTest.getFriendsUserName();
    $$("table tr td:nth-child(2)").shouldHave(CollectionCondition.texts(friendsUserName));
    mainPage.getActiveStatuses().shouldHave(CollectionCondition.texts("You are friends"));

    mainPage.openPeoplePage();
    mainPage.getActiveStatuses().shouldHave(CollectionCondition.texts("You are friends"));
  }


}
