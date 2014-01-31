package controllers;

import com.atlassian.connect.play.java.controllers.AcController;
import com.google.common.base.Supplier;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.OmniResponse;
import models.UserLogin;
import models.WikiUser;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Application controller. Code given by Atlassian Connect from: http://localhost:9000/@connect/descriptor.
 *
 * Date: 28/01/14
 * Time: 11:14
 *
 * @author      Sav Balac
 * @version     1.0
 */
public class Application extends Controller {


    /**
     * Renders the Atlassian Connect module home page and returns the "descriptor" -
     * the contents of /atlassian-connect.json, currently set to "baseUrl": "http://localhost:9000".
     *
     * @return Result  The home page.
     */
    public static Result index() {
        testGeoIP();
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


    public static void testGeoIP() {
        // This creates a WebServiceClient object that can be reused across requests.
        // Replace "42" with your user ID and "license_key" with your license key.
        try {

            // Test wiki user model
            WikiUser wikiUser = WikiUser.find.byId(1L);
            System.out.println("WikiUser: " + wikiUser.id + ", " + wikiUser.username + ", " + wikiUser.email + ", " +
                               wikiUser.password + ", " + wikiUser.forename + ", " + wikiUser.surname);

            // Test user login model
            UserLogin userLogin = UserLogin.find.byId(1L);
            System.out.println("UserLogin: " + userLogin.id + ", " + userLogin.username + ", " +
                               userLogin.login_timestamp + ", " + userLogin.ip_address);

            // Demo user ID and license key
            WebServiceClient client = new WebServiceClient.Builder(85883, "DCbc8uukhWNg").build();

            // Replace "omni" with the method corresponding to the web service that
            // you are using, e.g., "country", "cityIspOrg", "city".
            //OmniResponse response = client.omni(InetAddress.getByName("128.101.101.101"));
            OmniResponse response = client.omni(InetAddress.getByName("86.169.215.193"));

            System.out.println("response = \n" + response);


            System.out.println("Continent Code:      " + response.getContinent().getCode());
            System.out.println("Continent GeoNameId: " + response.getContinent().getGeoNameId());
            System.out.println("Continent Name:      " + response.getContinent().getName());

            System.out.println("Country Confidence: " + response.getCountry().getConfidence());
            System.out.println("Country IsoCode:    " + response.getCountry().getIsoCode()); // 'GB'
            System.out.println("Country GeoNameId:  " + response.getCountry().getGeoNameId());
            System.out.println("Country Name:       " + response.getCountry().getName()); // 'United Kingdom'

            System.out.println("Registered Country IsoCode:   " + response.getRegisteredCountry().getIsoCode());
            System.out.println("Registered Country GeoNameId: " + response.getRegisteredCountry().getGeoNameId());
            System.out.println("Registered Country Name:      " + response.getRegisteredCountry().getName());

            System.out.println("Represented Country IsoCode:   " + response.getRepresentedCountry().getIsoCode());
            System.out.println("Represented Country GeoNameId: " + response.getRepresentedCountry().getGeoNameId());
            System.out.println("Represented Country Name:      " + response.getRepresentedCountry().getName());
            System.out.println("Represented Country Type:      " + response.getRepresentedCountry().getType());

            System.out.println("City Confidence: " + response.getCity().getConfidence());
            System.out.println("City GeoNameId:  " + response.getCity().getGeoNameId());
            System.out.println("City Name:       " + response.getCity().getName()); // 'MK'

            System.out.println("Postal Code:       " + response.getPostal().getCode()); // 'null'
            System.out.println("Postal Confidence: " + response.getPostal().getConfidence());

            System.out.println("Location AccuracyRadius: " + response.getLocation().getAccuracyRadius());
            System.out.println("Location Latitude:       " + response.getLocation().getLatitude()); // 52.0333
            System.out.println("Location Longitude:      " + response.getLocation().getLongitude()); // -0.7
            System.out.println("Location MetroCode:      " + response.getLocation().getMetroCode());
            System.out.println("Location TimeZone:       " + response.getLocation().getTimeZone());

            System.out.println("MostSpecificSubdivision Confidence: " + response.getMostSpecificSubdivision().getConfidence());
            System.out.println("MostSpecificSubdivision GeoNameId:  " + response.getMostSpecificSubdivision().getGeoNameId());
            System.out.println("MostSpecificSubdivision IsoCode:    " + response.getMostSpecificSubdivision().getIsoCode()); // 'MIK'
            System.out.println("MostSpecificSubdivision Name:       " + response.getMostSpecificSubdivision().getName()); // 'Milton Keynes'

            System.out.println("Traits AutonomousSystemNumber:       " + response.getTraits().getAutonomousSystemNumber());
            System.out.println("Traits AutonomousSystemOrganization: " + response.getTraits().getAutonomousSystemOrganization());
            System.out.println("Traits Domain:                       " + response.getTraits().getDomain());
            System.out.println("Traits IpAddress:                    " + response.getTraits().getIpAddress());
            System.out.println("Traits isAnonymousProxy:             " + response.getTraits().isAnonymousProxy());
            System.out.println("Traits isSatelliteProvider:          " + response.getTraits().isSatelliteProvider());
            System.out.println("Traits Isp:                          " + response.getTraits().getIsp());
            System.out.println("Traits Organization:                 " + response.getTraits().getOrganization());
            System.out.println("Traits UserType:                     " + response.getTraits().getUserType());


        } catch (GeoIp2Exception e) {
            System.out.println("GeoIp2Exception: " + e);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + "128.101.101.101" + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }


}
