/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

/**
 *
 * @author josesfval
 */
public class FlareMediaServerAuthentificator 
{
    static final String ADMIN = "admin";
    static final String PASSWORD = "flarePassword";
    
    
    public static boolean serverAuthentification (String admin, String password)
    {
        if (ADMIN.equals(admin) && PASSWORD.equals(password))
            return true;
        return false;
    }
            
}
