package guru.qa.niffler.db.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "friends")
@IdClass(FriendsId.class)
public class FriendsEntity {
  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserDataUserEntity user;

  @Id
  @ManyToOne
  @JoinColumn(name = "friend_id", referencedColumnName = "id")
  private UserDataUserEntity friend;

  @Column(name = "pending")
  private boolean pending;

  public UserDataUserEntity getUser() {
    return user;
  }

  public FriendsEntity setUser(UserDataUserEntity user) {
    this.user = user;
    return this;
  }

  public UserDataUserEntity getFriend() {
    return friend;
  }

  public FriendsEntity setFriend(UserDataUserEntity friend) {
    this.friend = friend;
    return this;
  }

  public boolean isPending() {
    return pending;
  }

  public FriendsEntity setPending(boolean pending) {
    this.pending = pending;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FriendsEntity that = (FriendsEntity) o;
    return pending == that.pending && Objects.equals(user, that.user) && Objects.equals(friend, that.friend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, friend, pending);
  }
}
