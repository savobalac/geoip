package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder; // Import Finder as sometimes Play! shows compilation error "not found: type Finder"
import play.libs.Json;
import utils.Utils;

import javax.persistence.*;
import java.util.List;

/**
 * Model class that maps to table user_login. This is the Confluence user with details of their login and location.
 *
 * Date: 31/01/14
 * Time: 11:18
 *
 * @author Sav Balac
 * @version 1.0
 */
@Entity
@Table(name="user_login")
public class UserLogin extends Model {

    // Instance variables
    @Constraints.Required // A required constraint will ensure fields have values
    @SequenceGenerator(name="seq_gen_name", sequenceName="user_login_id_seq") // Required for a Postgres database
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen_name")
    @Id
    public Long                     id;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   username; // A non-empty format will convert spaces to null and ensure validation

    @Constraints.Required
    @Formats.NonEmpty
    public DateTime                 loginTimestamp;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   ipAddress;

    // The remaining field may not necessarily have values
    public String                   continentCode;
    public Integer                  continentGeonameId;
    public String                   continentName;

    public Integer                  countryConfidence;
    public String                   countryIsoCode;
    public Integer                  countryGeonameId;
    public String                   countryName;

    public String                   registeredCountryIsoCode;
    public Integer                  registeredCountryGeonameId;
    public String                   registeredCountryName;

    public String                   representedCountryIsoCode;
    public Integer                  representedCountryGeonameId;
    public String                   representedCountryName;
    public String                   representedCountryType;

    public Integer                  cityConfidence;
    public Integer                  cityGeonameId;
    public String                   cityName;

    public String                   postalCode;
    public Integer                  postalConfidence;

    public Integer                  locationAccuracyRadius;
    public Double                   locationLatitude;
    public Double                   locationLongitude;
    public Integer                  locationMetroCode;
    public String                   locationTimeZone;

    public Integer                  mostSpecificSubdivisionConfidence;
    public Integer                  mostSpecificSubdivisionGeonameId;
    public String                   mostSpecificSubdivisionIsoCode;
    public String                   mostSpecificSubdivisionName;

    public Integer                  traitsAutonomousSystemNumber;
    public String                   traitsAutonomousSystemOrganization;
    public String                   traitsDomain;
    public String                   traitsIpAddress;
    public Boolean                  traitsIsAnonymousProxy;
    public Boolean                  traitsIsSatelliteProvider;
    public String                   traitsIsp;
    public String                   traitsOrganization;
    public String                   traitsUserType;


    /**
     * Generic query helper for entity UserLogin.
     */
    public static Finder<Long, UserLogin> find = new Finder<Long, UserLogin>(Long.class, UserLogin.class);


    /**
     * Gets all user login differences as JSON.
     *
     * @return ObjectNode  The users as a JSON object node.
     */
    public static ObjectNode getAllDiffsAsJson() {
        List<UserLogin> userLogins = getAllDiffs();
        ObjectNode result = Json.newObject();
        ArrayNode userLoginNodes = result.arrayNode();
        for (UserLogin userLogin : userLogins) {
            ObjectNode userLoginResult = userLogin.toJson();
            userLoginNodes.add(userLoginResult);
        }
        result.put("userLogins", userLoginNodes);
        return result;
    }

    /**
     * Gets login differences for the user (if any) as JSON.
     *
     * @param userName  User name.
     * @return ObjectNode  The users as a JSON object node.
     */
    public static ObjectNode getUserDiffsAsJson(String userName) {
        List<UserLogin> userLogins = getUserDiffs(userName);
        ObjectNode result = Json.newObject();
        ArrayNode userLoginNodes = result.arrayNode();
        for (UserLogin userLogin : userLogins) {
            ObjectNode userLoginResult = userLogin.toJson();
            userLoginNodes.add(userLoginResult);
        }
        result.put("userLogins", userLoginNodes);
        return result;
    }


    /**
     * Returns a list of all user logins where there are differences in location data.
     *
     * @return List<UserLogin>  List of all user logins.
     */
    public static List<UserLogin> getAllDiffs() {

        String sql =
            "SELECT DISTINCT " +
            " ul1.id, ul1.username, ul1.login_timestamp, ul1.ip_address, ul1.continent_name, ul1.country_name, " +
            " ul1.registered_country_name, ul1.represented_country_name, ul1.city_name, ul1.postal_code, " +
            " ul1.location_time_zone, ul1.most_specific_subdivision_name, ul1.traits_ip_address " +
            "FROM " +
            "  user_login ul1, user_login ul2 " +
            "WHERE ul1.username = ul2.username " +
            "AND " +
            " (   ul1.continent_name                    != ul2.continent_name"                  +
            "  OR ul1.country_name                      != ul2.country_name "                   +
            "  OR ul1.registered_country_name           != ul2.registered_country_name"         +
            "  OR ul1.represented_country_name          != ul2.represented_country_name"        +
            "  OR ul1.city_name                         != ul2.city_name"                       +
            "  OR ul1.postal_code                       != ul2.postal_code"                     +
            "  OR ul1.location_time_zone                != ul2.location_time_zone"              +
            "  OR ul1.most_specific_subdivision_name    != ul2.most_specific_subdivision_name"  +
            "  OR ul1.traits_ip_address                 != ul2.traits_ip_address"               +
            " )" +
            "ORDER BY ul1.username, ul1.login_timestamp";

        RawSql rawSql =
            RawSqlBuilder.unparsed(sql)
                .columnMapping("ul1.id", "id")
                .columnMapping("ul1.username", "username")
                .columnMapping("ul1.login_timestamp", "loginTimestamp")
                .columnMapping("ul1.ip_address", "ipAddress")
                .columnMapping("ul1.continent_name", "continentName")
                .columnMapping("ul1.country_name", "countryName")
                .columnMapping("ul1.registered_country_name", "registeredCountryName")
                .columnMapping("ul1.represented_country_name", "representedCountryName")
                .columnMapping("ul1.city_name", "cityName")
                .columnMapping("ul1.postal_code", "postalCode")
                .columnMapping("ul1.location_time_zone", "locationTimeZone")
                .columnMapping("ul1.most_specific_subdivision_name", "mostSpecificSubdivisionName")
                .columnMapping("ul1.traits_ip_address", "traitsIpAddress")
                .create();

        Query<UserLogin> query = Ebean.find(UserLogin.class);
        query.setRawSql(rawSql);
        List<UserLogin> list = query.findList();
        return list;
    }

    /**
     * Returns a list of all user logins where there are differences in location data.
     *
     * @return List<UserLogin>  List of all user logins.
     */
    public static List<UserLogin> getUserDiffs(String userName) {

        String sql =
                "SELECT DISTINCT " +
                        " ul1.id, ul1.username, ul1.login_timestamp, ul1.ip_address, ul1.continent_name, ul1.country_name, " +
                        " ul1.registered_country_name, ul1.represented_country_name, ul1.city_name, ul1.postal_code, " +
                        " ul1.location_time_zone, ul1.most_specific_subdivision_name, ul1.traits_ip_address " +
                        "FROM " +
                        "  user_login ul1, user_login ul2 " +
                        "WHERE ul1.username = ul2.username " +
                        "AND ul1.username = '" + userName + "' " +
                        "AND " +
                        " (   ul1.continent_name                    != ul2.continent_name"                  +
                        "  OR ul1.country_name                      != ul2.country_name "                   +
                        "  OR ul1.registered_country_name           != ul2.registered_country_name"         +
                        "  OR ul1.represented_country_name          != ul2.represented_country_name"        +
                        "  OR ul1.city_name                         != ul2.city_name"                       +
                        "  OR ul1.postal_code                       != ul2.postal_code"                     +
                        "  OR ul1.location_time_zone                != ul2.location_time_zone"              +
                        "  OR ul1.most_specific_subdivision_name    != ul2.most_specific_subdivision_name"  +
                        "  OR ul1.traits_ip_address                 != ul2.traits_ip_address"               +
                        " )" +
                        "ORDER BY ul1.username, ul1.login_timestamp";

        RawSql rawSql =
                RawSqlBuilder.unparsed(sql)
                        .columnMapping("ul1.id", "id")
                        .columnMapping("ul1.username", "username")
                        .columnMapping("ul1.login_timestamp", "loginTimestamp")
                        .columnMapping("ul1.ip_address", "ipAddress")
                        .columnMapping("ul1.continent_name", "continentName")
                        .columnMapping("ul1.country_name", "countryName")
                        .columnMapping("ul1.registered_country_name", "registeredCountryName")
                        .columnMapping("ul1.represented_country_name", "representedCountryName")
                        .columnMapping("ul1.city_name", "cityName")
                        .columnMapping("ul1.postal_code", "postalCode")
                        .columnMapping("ul1.location_time_zone", "locationTimeZone")
                        .columnMapping("ul1.most_specific_subdivision_name", "mostSpecificSubdivisionName")
                        .columnMapping("ul1.traits_ip_address", "traitsIpAddress")
                        .create();

        Query<UserLogin> query = Ebean.find(UserLogin.class);
        query.setRawSql(rawSql);
        List<UserLogin> list = query.findList();
        return list;
    }



    /**
     * Converts the user and its groups to JSON. User-group is a many-many relationship.
     * Using Play's static toJson method results in a StackOverflow error (infinite recursion).
     *
     * @return ObjectNode  The user as a JSON object node.
     */
    public ObjectNode toJson() {
        ObjectNode userNode = Json.newObject();
        if (id == null) {
            return userNode;
        }
        userNode.put("id", id.toString());
        userNode.put("username", username);
        userNode.put("loginTimestamp", Utils.formatTimestamp(loginTimestamp));
        userNode.put("ipAddress", ipAddress);

        userNode.put("continentCode", continentCode);
        if (continentGeonameId != null) {
            userNode.put("continentGeonameId", continentGeonameId.toString());
        }
        userNode.put("continentName", continentName);

        if (countryConfidence != null) {
            userNode.put("countryConfidence", countryConfidence.toString());
        }
        userNode.put("countryIsoCode", countryIsoCode);
        if (countryGeonameId != null) {
            userNode.put("countryGeonameId", countryGeonameId.toString());
        }
        userNode.put("countryName", countryName);

        userNode.put("registeredCountryIsoCode", registeredCountryIsoCode);
        if (registeredCountryGeonameId != null) {
            userNode.put("registeredCountryGeonameId", registeredCountryGeonameId.toString());
        }
        userNode.put("registeredCountryName", registeredCountryName);

        userNode.put("representedCountryIsoCode", representedCountryIsoCode);
        if (representedCountryGeonameId != null) {
            userNode.put("representedCountryGeonameId", representedCountryGeonameId.toString());
        }
        userNode.put("representedCountryName", representedCountryName);
        userNode.put("representedCountryType", representedCountryType);

        if (cityConfidence != null) {
            userNode.put("cityConfidence", cityConfidence.toString());
        }
        if (cityGeonameId != null) {
            userNode.put("cityGeonameId", cityGeonameId.toString());
        }
        userNode.put("cityName", cityName);

        userNode.put("postalCode", postalCode);
        if (postalConfidence != null) {
            userNode.put("postalConfidence", postalConfidence.toString());
        }

        if (locationAccuracyRadius != null) {
            userNode.put("locationAccuracyRadius", locationAccuracyRadius.toString());
        }
        if (locationLatitude != null) {
            userNode.put("locationLatitude", locationLatitude.toString());
        }
        if (locationLongitude != null) {
            userNode.put("locationLongitude", locationLongitude.toString());
        }
        if (locationMetroCode != null) {
            userNode.put("locationMetroCode", locationMetroCode.toString());
        }
        userNode.put("locationTimeZone", locationTimeZone);

        if (mostSpecificSubdivisionConfidence != null) {
            userNode.put("mostSpecificSubdivisionConfidence", mostSpecificSubdivisionConfidence.toString());
        }
        if (mostSpecificSubdivisionGeonameId != null) {
            userNode.put("mostSpecificSubdivisionGeonameId", mostSpecificSubdivisionGeonameId.toString());
        }
        userNode.put("mostSpecificSubdivisionIsoCode", mostSpecificSubdivisionIsoCode);
        userNode.put("mostSpecificSubdivisionName", mostSpecificSubdivisionName);

        if (traitsAutonomousSystemNumber != null) {
            userNode.put("traitsAutonomousSystemNumber", traitsAutonomousSystemNumber.toString());
        }
        userNode.put("traitsAutonomousSystemOrganization", traitsAutonomousSystemOrganization);
        userNode.put("traitsDomain", traitsDomain);
        userNode.put("traitsIpAddress", traitsIpAddress);
        if (traitsIsAnonymousProxy != null) {
            userNode.put("traitsIsAnonymousProxy", traitsIsAnonymousProxy.toString());
        }
        if (traitsIsSatelliteProvider != null) {
            userNode.put("traitsIsSatelliteProvider", traitsIsSatelliteProvider.toString());
        }
        userNode.put("traitsIsp", traitsIsp);
        userNode.put("traitsOrganization", traitsOrganization);
        userNode.put("traitsUserType", traitsUserType);

        return userNode;
    }


}