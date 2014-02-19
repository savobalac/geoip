package controllers;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.OmniResponse;

import models.UserLogin;
import models.WikiUser;

import play.Play;
import play.data.Form;
import play.mvc.Result;

import play.mvc.Security;
import utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
@Security.Authenticated(Secured.class) // All methods will require the API user to be logged in
public class GeoIp extends AbstractController {

    // Class variables
    private static WebServiceClient client;
    private static OmniResponse response;


    /**
     * Gets GeoIP2 location data for the requested user at the IP address and saves it to the database.
     *
     * @return Result  The output of GeoIP2 as a string.
     */
    public static Result getLocation() {
        String userName = null;
        String ipAddress = null;
        try {

            Form<User> userForm = form(User.class).bindFromRequest(); // Get the username and IP address
            userName = userForm.get().user;
            ipAddress = userForm.get().remoteIP;

            System.out.println("***** in method getLocation(), userName = " + userName + ", IP address = " + ipAddress);

            // Check the user exists and return if not
            if (WikiUser.find.where().eq("username", userName).findUnique() == null) {
                return noWikiUser(userName);
            }

            // Create a WebServiceClient object using the demo user ID and license key
            int    geoId  = Play.application().configuration().getInt("geoip2.id");
            String geoKey = Play.application().configuration().getString("geoip2.key");

            client = new WebServiceClient.Builder(geoId, geoKey).build();

            // Get the data
            response = client.omni(InetAddress.getByName(ipAddress));

            // Create a user login object
            UserLogin userLogin = new UserLogin();

            userLogin.username = userName;
            userLogin.loginTimestamp = Utils.getCurrentDateTime();
            userLogin.ipAddress = ipAddress;

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

            // Save the user login
            userLogin.save();

            // Return the response as JSON
            if (request().accepts("application/json") || request().accepts("text/json")) {
                return ok(getSuccessAsJson(response.toString()));
            } else {
                return badRequest();
            }

        } catch (GeoIp2Exception e) {
            return locationError(userName, ipAddress, e);
        } catch (UnknownHostException e) {
            return locationError(userName, ipAddress, e);
        } catch (IOException e) {
            return locationError(userName, ipAddress, e);
        } catch (Exception e) {
            return locationError(userName, ipAddress, e);
        }

    }


    /**
     * Returns a message when the Wiki user doesn't exist.
     *
     * @param  userName  Username.
     * @return Result  A message as JSON.
     */
    private static Result noWikiUser(String userName) {

        System.out.println("***** in method noWikiUser()");

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
    public static Result getLoginDiff() {
        if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
            return ok(UserLogin.getAllDiffsAsJson());
        } else {
            return badRequest();
        }
    }


    /**
     * Returns a login report for the user (returns rows if they have logged in from different locations).
     *
     * @param  userName  Username.
     * @return Result  Login report as JSON.
     */
    public static Result getUserLoginDiff(String userName) {
        if (request().accepts("application/json") || request().accepts("text/json")) { // Return data as JSON
            return ok(UserLogin.getUserDiffsAsJson(userName));
        } else {
            return badRequest();
        }
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
