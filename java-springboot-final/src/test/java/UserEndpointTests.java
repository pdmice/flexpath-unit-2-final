import org.example.SpringBootApplication;
import org.example.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import support.FinalTestConfiguration;
import support.WebStoreTest;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the user endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootApplication.class)
@Import(FinalTestConfiguration.class)
public class UserEndpointTests extends WebStoreTest {
    /**
     * Tests that getting users fails if not authorized.
     */
    @Test
    @DisplayName("GET /api/users should return a 401 if not authorized")
    public void getUsersShouldFailIfNotAuthorized() {
        var result = this.restTemplate.getForEntity(getBaseUrl() + "/api/users", String.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.UNAUTHORIZED, responseCode);
    }

    @Test
    @DisplayName("GET /api/users should return a 403 if not an admin")
    public void getUsersShouldFailIfUserNotAdmin() throws SQLException {
        jdbcTemplate.update(
            "insert into users (username, password) values ('user', ?);",
            passwordEncoder.encode("user")
        );
        var requestEntity = GetAuthEntity("user", "user");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/users", HttpMethod.GET, requestEntity, String.class);
        var responseCode = result.getStatusCode();
        assertEquals(HttpStatus.FORBIDDEN, responseCode);
    }

    /**
     * Tests that getting users succeeds if authorized.
     */
    @Test
    @DisplayName("GET /api/users should return a 200 and a list of one user if authorized")
    public void getUserShouldSucceedIfAuthorized() {
        var requestEntity = GetAuthEntity("test-admin", "admin");
        var result = this.restTemplate.exchange(getBaseUrl() + "/api/users", HttpMethod.GET, requestEntity, User[].class);
        var responseCode = result.getStatusCode();
        var users = result.getBody();
        assertEquals(HttpStatus.OK, responseCode);
        assertEquals(1, users.length);
        assertEquals("test-admin", users[0].getUsername());
    }
}
