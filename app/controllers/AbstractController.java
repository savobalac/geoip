package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.WikiUser;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Top-level controller providing common fields and methods.
 *
 * Date:        21/11/13
 * Time:        12:56
 *
 * @author      Sav Balac
 * @version     1.1
 */
public abstract class AbstractController extends Controller {

    // Constants
    public static final int PAGE_TYPE_LIST = 1;
    public static final int PAGE_TYPE_EDIT = 2;


    /**
     * Returns the logged-in user.
     *
     * @return User  The logged-in user.
     */
    static WikiUser getLoggedInUser() {
        return WikiUser.find.where().eq("username", request().username()).findUnique();
    }


    /**
     * Shows a "Changes not saved" error.
     *
     * @param e  The exception that caused the error.
     */
    static void showSaveError(Exception e) {
        String msg = String.format("%s. Changes not saved.", e.getMessage());
        flash(Utils.KEY_ERROR, msg);
    }


    /**
     * Converts a string response to a result. The content is HTML or JSON as requested.
     *
     * @param result   The response as a string, e.g. "OK" or starting with "ERROR".
     * @return Result  A result containing the response, either as HTML or JSON.
     */
    static Result getAjaxResponse(String result) {
        if (request().accepts("text/html")) {
            return ok(result);
        } else if (request().accepts("application/json") || request().accepts("text/json")) {
            if (result.startsWith("ERROR")) {
                return ok(getErrorAsJson(result));
            } else {
                return ok(getSuccessAsJson(result));
            }
        } else {
            return badRequest();
        }
    }


    /**
     * Gets the form validation errors as JSON with a key of "error", for example, { "error": "password required" }.
     * Using the form's standard errorAsJson method sets the key to the field in error.
     *
     * @param  form  The form.
     * @return ObjectNode  The errors as a JSON object node with a key of "error".
     */
    static ObjectNode getErrorsAsJson(Form form) {

        // Get the form validation errors
        String errors = "";
        Map<String,List<ValidationError>> errorsMap = form.errors();

        // There is one key per field in error
        Set<String> keys = errorsMap.keySet();
        Iterator keyIt = keys.iterator();
        while (keyIt.hasNext()) {
            String key = (String) keyIt.next();

            // Get the validation error (should only be one per field)
            List<ValidationError> valErrors = errorsMap.get(key);
            Iterator entryIt = valErrors.iterator();
            while (entryIt.hasNext()) {
                ValidationError error = (ValidationError) entryIt.next();
                String message;
                if (error.message().startsWith("error.")) {
                    message = error.message().replace("error.", ""); // Remove text "error." from the message
                } else {
                    message = error.message();
                }
                errors += error.key() + " " + message + ". "; // Build one error string
            }
        }
        return getErrorAsJson(errors);
    }



    /**
     * Gets the success message as JSON.
     *
     * @param  msg  The message.
     * @return ObjectNode  The message as a JSON object node.
     */
    static ObjectNode getSuccessAsJson(String msg) {
        return getJson(Utils.KEY_SUCCESS, msg);
    }


    /**
     * Gets the information message as JSON.
     *
     * @param  msg  The message.
     * @return ObjectNode  The message as a JSON object node.
     */
    static ObjectNode getInfoAsJson(String msg) {
        return getJson(Utils.KEY_INFO, msg);
    }


    /**
     * Gets the error message as JSON.
     *
     * @param  msg  The message.
     * @return ObjectNode  The error as a JSON object node.
     */
    public static ObjectNode getErrorAsJson(String msg) {
        return getJson(Utils.KEY_ERROR, msg);
    }


    /**
     * Gets the key-value pair as JSON.
     *
     * @param  key    The key.
     * @param  value  The value.
     * @return ObjectNode  The key-value pair as a JSON object node.
     */
    static ObjectNode getJson(String key, String value) {
        ObjectNode result = Json.newObject();
        result.put(key, value);
        return result;
    }


}
