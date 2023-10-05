package guru.qa.niffler.db.model;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Stream;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table(name = "users")
public class UserDataUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyValues currency;

    @Column()
    private String firstname;

    @Column()
    private String surname;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "friend", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> invites = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public UserDataUserEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDataUserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public UserDataUserEntity setCurrency(CurrencyValues currency) {
        this.currency = currency;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserDataUserEntity setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public UserDataUserEntity setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public UserDataUserEntity setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public List<FriendsEntity> getFriends() {
        return friends;
    }

    public UserDataUserEntity setFriends(List<FriendsEntity> friends) {
        this.friends = friends;
        return this;
    }

    public List<FriendsEntity> getInvites() {
        return invites;
    }

    public UserDataUserEntity setInvites(List<FriendsEntity> invites) {
        this.invites = invites;
        return this;
    }

    public void addFriends(boolean pending, UserDataUserEntity... friends) {
        List<FriendsEntity> friendsEntities = Stream.of(friends)
            .map(f -> {
                return new FriendsEntity()
                    .setUser(this)
                    .setFriend(f)
                    .setPending(pending);
            }).toList();

        this.friends.addAll(friendsEntities);
    }

    public void removeFriends(UserDataUserEntity... friends) {
        for (UserDataUserEntity friend : friends) {
            getFriends().removeIf(f -> f.getFriend().getId().equals(friend.getId()));
        }
    }

    public void removeInvites(UserDataUserEntity... invitations) {
        for (UserDataUserEntity invite : invitations) {
            getInvites().removeIf(i -> i.getUser().getId().equals(invite.getId()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataUserEntity that = (UserDataUserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && currency == that.currency && Objects.equals(firstname, that.firstname) && Objects.equals(surname, that.surname) && Arrays.equals(photo, that.photo) && Objects.equals(friends, that.friends) && Objects.equals(invites, that.invites);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, username, currency, firstname, surname, friends, invites);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
