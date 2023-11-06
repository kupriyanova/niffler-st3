package guru.qa.niffler.db.mapper;

import guru.qa.niffler.db.model.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();
  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new UserEntity()
      .setId(rs.getObject("id", UUID.class))
      .setUsername(rs.getString("username"))
      .setFirstname(rs.getString("firstname"))
      .setSurname(rs.getString("surname"));
  }
}
