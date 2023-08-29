package guru.qa.niffler.jupiter.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.pages.MainPage;

@WebTest
public abstract class BaseWebTest {

  static {
    Configuration.browser = "chrome";
    Configuration.browserVersion = "114.0.5696.0";
    Configuration.browserSize = "1980x1024";
  }

  protected MainPage startApplication() {
    Selenide.open("http://127.0.0.1:3000/main");
    return new MainPage();
  }
}
