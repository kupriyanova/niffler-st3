package guru.qa.niffler.db;

import guru.qa.niffler.config.Config;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DataSourceProvider {

  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<ServiceDB, DataSource> dataSourceStore = new ConcurrentHashMap<>();
  public DataSource getDataSource(ServiceDB db) {
    return dataSourceStore.computeIfAbsent(db, key -> {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      ds.setURL(key.getUrl());
      ds.setUser(cfg.databaseUser());
      ds.setPassword(cfg.databasePassword());
      return ds;
    });
  }
}
