package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.jupiter.user.User;

import java.util.List;
import java.util.UUID;

public class UserJson {
  @JsonProperty("id")
  private UUID id;
  @JsonProperty("username")
  private String username;
  @JsonProperty("firstname")
  private String firstname;
  @JsonProperty("surname")
  private String surname;
  @JsonProperty("currency")
  private CurrencyValues currency;
  @JsonProperty("photo")
  private String photo;
  @JsonProperty("friendState")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private FriendState friendState;

  transient String password;
  transient User.UserType userType;
  transient List<String> friendsUserName;

  public UserJson() {
  }

  public UUID getId() {
    return id;
  }

  public UserJson setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public UserJson setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getFirstname() {
    return firstname;
  }

  public UserJson setFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public String getSurname() {
    return surname;
  }

  public UserJson setSurname(String surname) {
    this.surname = surname;
    return this;
  }

  public CurrencyValues getCurrency() {
    return currency;
  }

  public UserJson setCurrency(CurrencyValues currency) {
    this.currency = currency;
    return this;
  }

  public String getPhoto() {
    return photo;
  }

  public UserJson setPhoto(String photo) {
    this.photo = photo;
    return this;
  }

  public FriendState getFriendState() {
    return friendState;
  }

  public UserJson setFriendState(FriendState friendState) {
    this.friendState = friendState;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserJson setPassword(String password) {
    this.password = password;
    return this;
  }

  public User.UserType getUserType() {
    return userType;
  }

  public UserJson setUserType(User.UserType userType) {
    this.userType = userType;
    return this;
  }

  public List<String> getFriendsUserName() {
    return friendsUserName;
  }

  public UserJson setFriendsUserName(List<String> friendsUserName) {
    this.friendsUserName = friendsUserName;
    return this;
  }
}
