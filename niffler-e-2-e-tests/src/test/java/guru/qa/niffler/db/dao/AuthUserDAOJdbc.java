package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.model.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO, UserDataUserDAO {

  private static final DataSource authDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
  private static final DataSource userDataDs = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA);

  @Override
  public void createUser(AuthUserEntity user) {
    try(Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO authorities (user_id, authority) " +
                   "VALUES (?,?)"))
      {
        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());
        UUID generatedUserId;
        try (ResultSet generatedKeys = userPs.getGeneratedKeys()) {
          if(generatedKeys.next()) {
            generatedUserId = UUID.fromString(generatedKeys.getString("id"));
          } else throw new IllegalArgumentException("Can't obtain id from given ResultSet");
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, generatedUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }
        authorityPs.executeBatch();
        user.setId(generatedUserId);
        conn.commit();
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthUserEntity updateUser(AuthUserEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement usersPs = conn.prepareStatement(
          "UPDATE users SET " +
              "password = ?, " +
              "enabled = ?, " +
              "account_non_expired = ?, " +
              "account_non_locked = ?, " +
              "credentials_non_expired = ? " +
              "WHERE id = ? ");

           PreparedStatement clearAuthorityPs = conn.prepareStatement("DELETE FROM authorities WHERE user_id = ?");

           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO authorities (user_id, authority) " +
                   "VALUES (?, ?)")) {

        clearAuthorityPs.setObject(1, user.getId());
        clearAuthorityPs.executeUpdate();

        for (AuthorityEntity authority : user.getAuthorities()) {
          authorityPs.setObject(1, user.getId());
          authorityPs.setString(2, authority.getAuthority().name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }
        authorityPs.executeBatch();

        usersPs.setString(1, pe.encode(user.getPassword()));
        usersPs.setBoolean(2, user.getEnabled());
        usersPs.setBoolean(3, user.getAccountNonExpired());
        usersPs.setBoolean(4, user.getAccountNonLocked());
        usersPs.setBoolean(5, user.getCredentialsNonExpired());
        usersPs.setObject(6, user.getId());
        usersPs.executeUpdate();

        conn.commit();
        conn.setAutoCommit(true);

        return getUserById(user.getId());
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
        throw e;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteUser(UUID userId) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement usersPs = conn.prepareStatement(
          "DELETE FROM users WHERE id = ?");

           PreparedStatement authorityPs = conn.prepareStatement(
               "DELETE FROM authorities WHERE user_id = ?")) {

        authorityPs.setObject(1, userId);
        authorityPs.executeUpdate();

        usersPs.setObject(1, userId);
        usersPs.executeUpdate();

        conn.commit();
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthUserEntity getUserById(UUID userId) {
    AuthUserEntity user = new AuthUserEntity();
    try (Connection conn = authDs.getConnection()) {
      try (PreparedStatement usersPs = conn.prepareStatement(
          "SELECT * FROM users " +
              "WHERE id = ? ")) {

        usersPs.setObject(1, userId);

        usersPs.execute();
        ResultSet rs = usersPs.getResultSet();

        while (rs.next()) {
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        }
      }
      try (PreparedStatement authPs = conn.prepareStatement(
          "select * from authorities a " +
              "where user_id = ?")) {
        if (Objects.nonNull(user.getId())) {
          authPs.setObject(1, user.getId());
          authPs.execute();
          ResultSet rs = authPs.getResultSet();

          List<AuthorityEntity> authorityEntityList = new ArrayList<>();
          while (rs.next()) {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setId(rs.getObject("id", UUID.class));
            ae.setAuthority(Authority.valueOf(rs.getString("authority")));
            authorityEntityList.add(ae);
          }

          user.setAuthorities(authorityEntityList);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }


  @Override
  public void createUserInUserData(UserDataUserEntity user) {
    try(Connection conn = userDataDs.getConnection()) {
      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO users (username, currency) " +
              "VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS))
      {
        userPs.setString(1, user.getUsername());
        userPs.setString(2, CurrencyValues.RUB.name());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserDataUserEntity updateUserInUserData(UserDataUserEntity user) {
    try (Connection conn = userDataDs.getConnection()) {
      try (PreparedStatement usersPs = conn.prepareStatement(
          "UPDATE  users " +
              "SET id = ?, username = ?, currency = ?, firstname = ?, surname = ?, photo = ? " +
              "WHERE id = ? ")) {

        usersPs.setObject(1, user.getId());
        usersPs.setString(2, user.getUsername());
        usersPs.setObject(3, user.getCurrency().name());
        usersPs.setString(4, user.getFirstname());
        usersPs.setString(5, user.getSurname());
        usersPs.setObject(6, user.getPhoto());
        usersPs.setObject(7, user.getId());

        usersPs.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public void deleteUserInUserData(String userName) {
    try (Connection conn = userDataDs.getConnection()) {
      try (PreparedStatement usersPs = conn.prepareStatement(
          "DELETE FROM users " +
              "WHERE username = ? ")) {

        usersPs.setString(1, userName);

        usersPs.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserDataUserEntity getUserByIdInUserData(UUID userId) {
    UserDataUserEntity user = new UserDataUserEntity();
    try (Connection conn = userDataDs.getConnection()) {
      try (PreparedStatement usersPs = conn.prepareStatement(
          "SELECT * FROM users " +
              "WHERE id = ? ")) {

        usersPs.setObject(1, userId);

        usersPs.execute();
        ResultSet rs = usersPs.getResultSet();

        while (rs.next()) {
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          user.setFirstname(rs.getString("firstname"));
          user.setSurname(rs.getString("surname"));
          String photo = rs.getString("photo");
          user.setPhoto(photo != null ? photo.getBytes() : null);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }
}
