package org.example.controllers;

import org.example.models.User;
import org.example.daos.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller for users.
 * This class is responsible for handling all HTTP requests related to users.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {
    /**
     * The user data access object.
     */
    private final UserDao userDao;

    /**
     * Creates a new user controller.
     *
     * @param userDao The user data access object.
     */
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Gets all users.
     *
     * @return A list of all users.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<User> getAll() {
        return userDao.getUsers();
    }

    /**
     * Gets a user by their username.
     *
     * @param username The username of the user.
     * @return The user with the given username.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{username}")
    public User get(@PathVariable String username) {
        return userDao.getUserByUsername(username);
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created user.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users")
    public User create(@RequestBody User user) {
        return userDao.createUser(user);
    }

    /**
     * Updates a specific user's password.
     *
     * @param password The new password.
     * @param username The username of the user.
     * @return The updated user.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(path = "users/{username}/password")
    public User updatePassword(@RequestBody String password, @PathVariable String username) {
        User user = userDao.getUserByUsername(username);
        user.setPassword(password);
        return userDao.updatePassword(user);
    }

    /**
     * Deletes a user.
     *
     * @param username The username of the user to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "users/{username}")
    public void delete(@PathVariable String username) {
        userDao.deleteUser(username);
    }

    /**
     * Gets all roles for a user.
     *
     * @return A list of all roles for the user.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/roles")
    public List<String> getRoles(Principal principal) {
        return userDao.getRoles(principal.getName());
    }

    /**
     * Adds a role to a user.
     *
     * @param username The username of the user.
     * @param role The role to add.
     * @return A list of all roles for the user.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "users/{username}/roles")
    public List<String> addRole(@PathVariable String username, @RequestBody String role) {
        return userDao.addRole(username, role.toUpperCase());
    }

    /**
     * Deletes a role from a user.
     *
     * @param username The username of the user.
     * @param role The role to delete.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "users/{username}/roles/{role}")
    public void deleteRole(@PathVariable String username, @PathVariable String role) {
        userDao.deleteRole(username, role.toUpperCase());
    }
}
