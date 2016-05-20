/**
 * @author: Jose Ortiz Costa
 * date:    05/16/2016
 * This class provides check credentials for accessing to
 * the server interface
 */
package Core;

public class FlareMediaServerAuthentificator {

    // Default credentials
    public static final String ADMIN = "admin";
    public static final String PASSWORD = "flarePassword";

    /**
     * Static method that check for server administrator authentication
     *
     * @param admin user name
     * @param password password
     * @return true if the credentials checked were correct. Otherwise returns
     * false.
     */
    public static boolean serverAuthentification(String admin, String password) {
        if (ADMIN.equals(admin) && PASSWORD.equals(password)) {
            return true;
        }
        return false;
    }

}
