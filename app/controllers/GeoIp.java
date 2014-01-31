package controllers;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.OmniResponse;
import models.UserLogin;
import models.WikiUser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

/**
 * .
 * <p/>
 * Date: 31/01/14
 * Time: 12:16
 *
 * @author Sav Balac
 * @version 1.0
 * @since 1.0
 */
public class GeoIp extends Controller {

    private static OmniResponse response;

    public static Result getLocation(String userName, String ipAddress) {
        // This creates a WebServiceClient object that can be reused across requests.
        // Replace "42" with your user ID and "license_key" with your license key.
        try {

            // Test wiki user model
            //WikiUser wikiUser = WikiUser.find.byId(1L);
            //System.out.println("WikiUser: " + wikiUser.id + ", " + wikiUser.username + ", " + wikiUser.email + ", " +
            //        wikiUser.password + ", " + wikiUser.forename + ", " + wikiUser.surname);

            // Test user login model
            //UserLogin userLogin = UserLogin.find.byId(1L);
            //System.out.println("UserLogin: " + userLogin.id + ", " + userLogin.username + ", " +
            //        userLogin.login_timestamp + ", " + userLogin.ip_address);

            // Demo user ID and license key
            WebServiceClient client = new WebServiceClient.Builder(85883, "DCbc8uukhWNg").build();

            // Replace "omni" with the method corresponding to the web service that
            // you are using, e.g., "country", "cityIspOrg", "city".
            //OmniResponse response = client.omni(InetAddress.getByName("128.101.101.101"));
            response = client.omni(InetAddress.getByName(ipAddress));

            UserLogin userLogin = new UserLogin();
            userLogin.username = userName;
            userLogin.login_timestamp = Utils.getCurrentDateTime();
            userLogin.ip_address = ipAddress;

            userLogin.continent_code = response.getContinent().getCode();
            userLogin.continent_geoname_id = response.getContinent().getGeoNameId();
            userLogin.continent_name = response.getContinent().getName();

            userLogin.country_confidence = response.getCountry().getConfidence();
            userLogin.country_iso_code = response.getCountry().getIsoCode();
            userLogin.country_geoname_id = response.getCountry().getGeoNameId();
            userLogin.country_name = response.getCountry().getName();

            userLogin.registered_country_iso_code = response.getRegisteredCountry().getIsoCode();
            userLogin.registered_country_geoname_id = response.getRegisteredCountry().getGeoNameId();
            userLogin.registered_country_name = response.getRegisteredCountry().getName();

            userLogin.represented_country_iso_code = response.getRepresentedCountry().getIsoCode();
            userLogin.represented_country_geoname_id = response.getRepresentedCountry().getGeoNameId();
            userLogin.represented_country_name = response.getRepresentedCountry().getName();
            userLogin.represented_country_type = response.getRepresentedCountry().getType();

            userLogin.city_confidence = response.getCity().getConfidence();
            userLogin.city_geoname_id = response.getCity().getGeoNameId();
            userLogin.city_name = response.getCity().getName();

            userLogin.postal_code = response.getPostal().getCode();
            userLogin.postal_confidence = response.getPostal().getConfidence();

            userLogin.location_accuracy_radius = response.getLocation().getAccuracyRadius();
            userLogin.location_latitude = response.getLocation().getLatitude();
            userLogin.location_longitude = response.getLocation().getLongitude();
            userLogin.location_metro_code = response.getLocation().getMetroCode();
            userLogin.location_time_zone = response.getLocation().getTimeZone();

            userLogin.most_specific_subdivision_confidence = response.getMostSpecificSubdivision().getConfidence();
            userLogin.most_specific_subdivision_geoname_id = response.getMostSpecificSubdivision().getGeoNameId();
            userLogin.most_specific_subdivision_iso_code = response.getMostSpecificSubdivision().getIsoCode();
            userLogin.most_specific_subdivision_name = response.getMostSpecificSubdivision().getName();

            userLogin.traits_autonomous_system_number = response.getTraits().getAutonomousSystemNumber();
            userLogin.traits_autonomous_system_organization = response.getTraits().getAutonomousSystemOrganization();
            userLogin.traits_domain = response.getTraits().getDomain();
            userLogin.traits_ip_address = response.getTraits().getIpAddress();
            userLogin.traits_is_anonymous_proxy = response.getTraits().isAnonymousProxy();
            userLogin.traits_is_satellite_provider = response.getTraits().isSatelliteProvider();
            userLogin.traits_isp = response.getTraits().getIsp();
            userLogin.traits_organization = response.getTraits().getOrganization();
            userLogin.traits_user_type = response.getTraits().getUserType();

            userLogin.save();

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
            System.out.println("Unknown host: " + ipAddress + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return ok(response.toString());
    }

}
