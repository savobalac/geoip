package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import utils.Utils;

import java.security.NoSuchAlgorithmException;

/**
 * Ensures requests have an authenticated user.
 * Contains methods to get the username and redirect if not authorised.
 *
 * Date: 03/02/14
 * Time: 12:36
 *
 * @author      Sav Balac
 * @version     1.0
 */
public class Secured extends Security.Authenticator {


    /**
     * Gets the username.
     *
     * @param ctx  The context object.
     * @return String  The username.
     */
    @Override
    public String getUsername(Context ctx) {

        // Get the username from the cookie
        // Requests from the JSON API may include the username and password to avoid logging in first
        if (ctx.session().get("username") != null) {
            return ctx.session().get("username");
        } else {
            if (ctx.request().accepts("application/json") || ctx.request().accepts("text/json")) {
                String username = ctx.request().getHeader("username");
                String password = ctx.request().getHeader("password");
                Application.Login login = new Application.Login();
                login.username = username;
                login.password = password;
                try {
                    if (login.validate() == null) {
                        return username;
                    } else {
                        return null;
                    }
                } catch (NoSuchAlgorithmException ex) {
                    // Log an error
                    Utils.eHandler("Secured.getUsername()", ex);
                    return null;
                }
            } else {
                return null;
            }
        }
    }


    /**
     * Redirects to the application login method if unauthorised.
     *
     * @param ctx  The context object.
     * @return Result  The login "page".
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(controllers.routes.Application.login());
    }


}
