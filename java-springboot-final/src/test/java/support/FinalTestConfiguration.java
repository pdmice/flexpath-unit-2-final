package support;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;

/**
 * Test configuration class for overriding the data source bean
 * to use an embedded MariaDB instance.
 */
@TestConfiguration
public class FinalTestConfiguration {
    /**
     * Creates a data source bean that uses an embedded MariaDB instance.
     *
     * @return The data source bean.
     * @throws ManagedProcessException If an error occurs while starting the embedded MariaDB instance.
     */
    @Bean
    public DataSource dataSource() throws ManagedProcessException {
        var configBuilder = DBConfigurationBuilder.newBuilder();
        configBuilder.setPort(0);
        var db = DB.newEmbeddedDB(configBuilder.build());
        db.start();

        return DataSourceBuilder.create()
            .url("jdbc:mariadb://localhost:" + db.getConfiguration().getPort() + "/test")
            .username("root")
            .build();
    }
}
