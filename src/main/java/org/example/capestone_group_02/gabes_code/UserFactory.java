package org.example.capestone_group_02.gabes_code;
/**
 * The UserFactory class provides a method for creating User objects.
 */

public class UserFactory {
	
	/**
     * Creates a new User object with the specified attributes.
     *
     * @param id       the ID of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email    the email of the user
     * @return the newly created User object
     */
	
    public static User createUser(int id, String username, String password, String email) {
        return new User(id, username, password, email);
    }
}
