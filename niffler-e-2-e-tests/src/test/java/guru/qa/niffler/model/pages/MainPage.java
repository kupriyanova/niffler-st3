package guru.qa.niffler.model.pages;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.UserJson;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

  public MainPage logIn(UserJson user) {
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();
    return this;
  }

  public MainPage logIn() {
    $("a[href*='redirect']").click();
//    $("input[name='username']").setValue(user.getUsername());
//    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();
    return this;
  }

  public void openFriendsPage() {
    $("li[data-tooltip-content=\"Friends\"]").click();
  }
  public ElementsCollection getListFriends() {
    return $$("table tr td:nth-child(2)");
  }

  public void openPeoplePage() {
    $("li[data-tooltip-content=\"All people\"]").click();
  }
  public ElementsCollection getActiveStatuses() {
    return $$(".abstract-table__buttons");
  }
}
