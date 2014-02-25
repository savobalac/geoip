package controllers;

import com.atlassian.connect.play.java.AC;
import com.atlassian.connect.play.java.auth.jwt.AuthenticateJwtRequest;

import com.atlassian.connect.play.java.token.CheckValidToken;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.OmniResponse;

import models.UserLogin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;

import utils.Utils;
import views.html.*;

import static play.data.Form.form;

/**
 * The controller that requests location data for a user at an IP address.
 *
 * Date: 31/01/14
 * Time: 12:16
 *
 * @author Sav Balac
 * @version 1.0
 */
public class GeoIp extends AbstractController {

    // Class variables
    private static WebServiceClient client;
    private static OmniResponse response;


    /**
     * Gets GeoIP2 location data for the requested user at the IP address and saves it to the database.
     *
     * @return Result  The output of GeoIP2 as a string.
     */
    @AuthenticateJwtRequest // Authentication is done by the AC plugin using JWT
    public static Result getLocation() throws Exception {

        String user = null;
        String remoteIP = null;
        String remoteHost = null;
        String timestamp = null;
        try {

            Form<User> userForm = form(User.class).bindFromRequest(); // Get the posted data
            user        = userForm.get().user;
            remoteIP    = userForm.get().remoteIP;
            remoteHost  = userForm.get().remoteHost;
            timestamp   = userForm.get().timestamp;

            // Debug as testing locally gives an IP address of 0:0:0:0:0:0:0:1
            remoteIP = "6.7.8.9";

            System.out.println("***** in method getLocation(), user = " + user + ", remoteIP = " + remoteIP +
                               ", remoteHost = " + remoteHost + ", timestamp = " + timestamp);

            // Create a WebServiceClient object using the demo user ID and license key
            int    geoId  = Play.application().configuration().getInt("geoip2.id");
            String geoKey = Play.application().configuration().getString("geoip2.key");

            client = new WebServiceClient.Builder(geoId, geoKey).build();

            System.out.println("***** Built the WebServiceClient");
            System.out.println("***** Not calling GeoIP as we've reached our free query limit");

            // Get the data
            /*response = client.omni(InetAddress.getByName(remoteIP));

            System.out.println("***** Got the response from client.omni");

            // Create a user login object
            UserLogin userLogin = new UserLogin();

            System.out.println("***** Created a new UserLogin");

            userLogin.username = user;
            userLogin.loginTimestamp = Utils.getCurrentDateTime();
            userLogin.ipAddress = remoteIP;

            userLogin.continentCode = response.getContinent().getCode();
            userLogin.continentGeonameId = response.getContinent().getGeoNameId();
            userLogin.continentName = response.getContinent().getName();

            userLogin.countryConfidence = response.getCountry().getConfidence();
            userLogin.countryIsoCode = response.getCountry().getIsoCode();
            userLogin.countryGeonameId = response.getCountry().getGeoNameId();
            userLogin.countryName = response.getCountry().getName();

            userLogin.registeredCountryIsoCode = response.getRegisteredCountry().getIsoCode();
            userLogin.registeredCountryGeonameId = response.getRegisteredCountry().getGeoNameId();
            userLogin.registeredCountryName = response.getRegisteredCountry().getName();

            userLogin.representedCountryIsoCode = response.getRepresentedCountry().getIsoCode();
            userLogin.representedCountryGeonameId = response.getRepresentedCountry().getGeoNameId();
            userLogin.representedCountryName = response.getRepresentedCountry().getName();
            userLogin.representedCountryType = response.getRepresentedCountry().getType();

            userLogin.cityConfidence = response.getCity().getConfidence();
            userLogin.cityGeonameId = response.getCity().getGeoNameId();
            userLogin.cityName = response.getCity().getName();

            userLogin.postalCode = response.getPostal().getCode();
            userLogin.postalConfidence = response.getPostal().getConfidence();

            userLogin.locationAccuracyRadius = response.getLocation().getAccuracyRadius();
            userLogin.locationLatitude = response.getLocation().getLatitude();
            userLogin.locationLongitude = response.getLocation().getLongitude();
            userLogin.locationMetroCode = response.getLocation().getMetroCode();
            userLogin.locationTimeZone = response.getLocation().getTimeZone();

            userLogin.mostSpecificSubdivisionConfidence = response.getMostSpecificSubdivision().getConfidence();
            userLogin.mostSpecificSubdivisionGeonameId = response.getMostSpecificSubdivision().getGeoNameId();
            userLogin.mostSpecificSubdivisionIsoCode = response.getMostSpecificSubdivision().getIsoCode();
            userLogin.mostSpecificSubdivisionName = response.getMostSpecificSubdivision().getName();

            userLogin.traitsAutonomousSystemNumber = response.getTraits().getAutonomousSystemNumber();
            userLogin.traitsAutonomousSystemOrganization = response.getTraits().getAutonomousSystemOrganization();
            userLogin.traitsDomain = response.getTraits().getDomain();
            userLogin.traitsIpAddress = response.getTraits().getIpAddress();
            userLogin.traitsIsAnonymousProxy = response.getTraits().isAnonymousProxy();
            userLogin.traitsIsSatelliteProvider = response.getTraits().isSatelliteProvider();
            userLogin.traitsIsp = response.getTraits().getIsp();
            userLogin.traitsOrganization = response.getTraits().getOrganization();
            userLogin.traitsUserType = response.getTraits().getUserType();

            System.out.println("***** About to save the user login");

            // Save the user login
            userLogin.save();

            System.out.println("***** Saved the user login");

            // Return the response as JSON
            if (request().accepts("application/json") || request().accepts("text/json")) {
                return ok(getSuccessAsJson(response.toString()));
            } else {
                return badRequest();
            }*/
            return ok("ok");

        /*} catch (GeoIp2Exception e) {
            return locationError(user, remoteIP, e);
        } catch (UnknownHostException e) {
            return locationError(user, remoteIP, e);
        } catch (IOException e) {
            return locationError(user, remoteIP, e);*/
        } catch (Exception e) {
            return locationError(user, remoteIP, e);
        }

    }


    /**
     * Returns a message when the Wiki user doesn't exist.
     *
     * @param  userName  Username.
     * @return Result  A message as JSON.
     */
    private static Result noWikiUser(String userName) {
        String message = "Wiki user: " + userName + " does not exist.";
        if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
            return ok(getErrorAsJson(message));
        } else {
            return badRequest();
        }
    }


    /**
     * Returns a message if an exception occurred getting the GeoIP data.
     *
     * @param  userName  Username.
     * @return Result  A message as JSON.
     */
    private static Result locationError(String userName, String ipAddress, Exception e) {
        String message = "Error getting location data for Wiki user: " + userName +
                         ", at IP address: " + ipAddress + ", error: " + e;
        System.out.println(message);
        if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
            return ok(getErrorAsJson(message));
        } else {
            return badRequest();
        }
    }


    /**
     * Returns a report of users what have logged in from different locations.
     *
     * @return Result  Login report as JSON.
     */
    @AuthenticateJwtRequest
    public static Result loginReport() {

        // Return data in HTML or JSON as requested
        //if (request().accepts("text/html")) {

            // Get the list of differences and render the list page
            List<UserLogin> userLogins = UserLogin.getAllDiffs();
            return ok(listUserLogins.render(userLogins));

        //} else if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
        //    return ok(UserLogin.getAllDiffsAsJson());
        //} else {
        //    return badRequest();
        //}
    }


    /**
     * Returns a login report for the user (returns rows if they have logged in from different locations).
     *
     * @param  userName  Username.
     * @return Result  Login report as JSON.
     */
    //@AuthenticateJwtRequest
    public static Result userLoginReport(String userName) {

        // Return data in HTML or JSON as requested
        if (request().accepts("text/html")) {

            // Get the list of differences and render the list page
            List<UserLogin> userLogins = UserLogin.getUserDiffs(userName);
            return ok(listUserLogins.render(userLogins));

        } else if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
            return ok(UserLogin.getUserDiffsAsJson(userName));
        } else {
            return badRequest();
        }
    }


    /**
     * User logged out from Confluence.
     *
     * @return  Result  A logout message.
     */
    @AuthenticateJwtRequest
    public static Result logout() {
        System.out.println("User logged out from Confluence");
        return ok("User logged out from Confluence");
    }


    /**
     * Inner class that holds the username and IP address of the user (login).
     */
    public static class User {

        public String user;
        public String remoteIP;
        public String remoteHost;
        public String timestamp;

    }


}
