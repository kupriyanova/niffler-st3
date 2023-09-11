package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.UserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserDAOJdbc implements AuthUserDAO {

  private static DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.AUTH);
  @Override
  public int createUser(UserEntity user) {
    int createdRows = 0;
    try(Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO authorities (user_id, authority) " +
                   "VALUES (?,?)"))
      {
        userPs.setString(1, user.getUsername());
        userPs.setString(2, user.getPassword());
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());
        createdRows = userPs.executeUpdate();
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

        conn.commit();
        conn.setAutoCommit(true);
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return createdRows;
  }

  @Override
  public void deleteUserById(UUID userId) {

  }
}
