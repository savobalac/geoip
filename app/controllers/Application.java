package controllers;

import com.atlassian.connect.play.java.controllers.AcController;
import com.google.common.base.Supplier;
import models.WikiUser;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;

import java.security.NoSuchAlgorithmException;

import static play.data.Form.form;


/**
 * Application controller. Code given by Atlassian Connect from: http://localhost:9000/@connect/descriptor.
 *
 * Date: 28/01/14
 * Time: 11:14
 *
 * @author      Sav Balac
 * @version     1.0
 */
public class Application extends AbstractController {

    // Constants
    private static final String AUTHENTICATION_ERROR_MSG = "Invalid username or password.";


    /**
     * Renders the Atlassian Connect module home page and returns the "descriptor" -
     * the contents of /atlassian-connect.json, currently set to "baseUrl": "http://localhost:9000".
     *
     * @return Result  The home page.
     */
    @Security.Authenticated(Secured.class) // Will require the API user to be logged in
    public static Result index() {

        System.out.println("Application.index()");

        return AcController.index(
                AcController.home(),    // The HTML home page from the module, as this is our documentation for now
                descriptorSupplier());  // Serve the descriptor when accept header is 'application/json'
                                        //     try 'curl -H "Accept: application/json" http://localhost:9000'
    }


    /**
     * Returns the "descriptor" -
     * the contents of /atlassian-connect.json, currently set to "baseUrl": "http://localhost:9000".
     *
     * @return Result  The descriptor as JSON.
     */
    @Security.Authenticated(Secured.class) // Will require the user to be logged in
    public static Result descriptor() {
        return AcController.descriptor();
    }


    /**
     * Returns the "supplier".
     *
     * @return Supplier  The supplier.
     */
    private static Supplier descriptorSupplier() {
        return new Supplier() {
            public Result get() {
                return descriptor();
            }
        };
    }

    /**
     * Requests the API user to log in.
     *
     * @return Result  Info message as JSON.
     */
    public static Result login() {
        if (request().accepts("application/json") || request().accepts("text/json")) {
            return ok(getInfoAsJson("Please sign in with a valid username and password."));
        } else {
            return badRequest();
        }
    }


    /**
     * Authenticates the user and goes the application index method.
     *
     * @return Result  The home "page" if logged in.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest(); // Get the username and password
        // Check if there are errors
        if (loginForm.hasErrors()) {
            if (request().accepts("application/json") || request().accepts("text/json")) {
                return ok(getErrorAsJson(AUTHENTICATION_ERROR_MSG));
            } else {
                return badRequest();
            }
        } else {
            session().clear();
            String username = loginForm.get().username;
            session("username", username);
            return redirect(controllers.routes.Application.index());
        }
    }


    /**
     * Logs the user out.
     *
     * @return Result  A logout message as JSON.
     */
    public static Result logout() {
        session().clear();
        String msg = "You have signed out.";
        if (request().accepts("application/json") || request().accepts("text/json")) {
            return ok(getSuccessAsJson(msg));
        } else {
            return badRequest();
        }
    }


    /**
     * Inner class that holds the username and password and validates the user.
     */
    public static class Login {

        public String username;
        public String password;

        /**
         * Calls a method to authenticate the user.
         *
         * @return String                       An error message if not authenticated, else null.
         * @throws java.security.NoSuchAlgorithmException     If the algorithm doesn't exist.
         */
        public String validate() throws NoSuchAlgorithmException {

            System.out.println("Login.validate(), username: " + username + ", password: " + password);

            if (username == null || password == null || WikiUser.authenticate(username, password) == null) {
                return AUTHENTICATION_ERROR_MSG;
            }
            return null;
        }

    }


}
