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

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.jupiter.user.User.UserType.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvitationReceivedWebTest extends BaseWebTest {

  MainPage mainPage;
  private UserJson userWithInvitationReceived;

  @BeforeEach
  void doLogin(@User(userType = INVITATION_RECEIVED) UserJson user) {
    userWithInvitationReceived = user;
    mainPage = startApplication();
    mainPage.logIn(userWithInvitationReceived);
    mainPage.openFriendsPage();
  }
  @Test
  @AllureId("103")
  void invitationReceivedInTable() {
    List<String> receivedUserName = userWithInvitationReceived.getFriendsUserName();
    $$("[data-tooltip-id=\"submit-invitation\"] button")
        .shouldHave(CollectionCondition.size(receivedUserName.size()));

    mainPage.openPeoplePage();
    $$("[data-tooltip-id=\"submit-invitation\"] button")
        .shouldHave(CollectionCondition.size(receivedUserName.size()));
  }

}
