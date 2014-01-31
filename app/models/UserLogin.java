package models;

import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder; // Import Finder as sometimes Play! shows compilation error "not found: type Finder"

import javax.persistence.*;
import java.sql.Timestamp;

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
    // A required constraint will ensure fields have values
    @Constraints.Required
    @SequenceGenerator(name="seq_gen_name", sequenceName="user_login_id_seq") // Required for a Postgres database
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen_name")
    @Id
    public Long                     id;

    @Constraints.Required
    @Formats.NonEmpty                         // A non-empty format will convert spaces to null and ensure validation
    public String                   username;

    @Constraints.Required
    @Formats.NonEmpty
    public DateTime                 login_timestamp;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   ip_address;

    public String                   continent_code;
    public Integer                  continent_geoname_id;
    public String                   continent_name;

    public Integer                  country_confidence;
    public String                   country_iso_code;
    public Integer                  country_geoname_id;
    public String                   country_name;

    public String                   registered_country_iso_code;
    public Integer                  registered_country_geoname_id;
    public String                   registered_country_name;

    public String                   represented_country_iso_code;
    public Integer                  represented_country_geoname_id;
    public String                   represented_country_name;
    public String                   represented_country_type;

    public Integer                  city_confidence;
    public Integer                  city_geoname_id;
    public String                   city_name;

    public String                   postal_code;
    public Integer                  postal_confidence;

    public Integer                  location_accuracy_radius;
    public Double                   location_latitude;
    public Double                   location_longitude;
    public Integer                  location_metro_code;
    public String                   location_time_zone;

    public Integer                  most_specific_subdivision_confidence;
    public Integer                  most_specific_subdivision_geoname_id;
    public String                   most_specific_subdivision_iso_code;
    public String                   most_specific_subdivision_name;

    public Integer                  traits_autonomous_system_number;
    public String                   traits_autonomous_system_organization;
    public String                   traits_domain;
    public String                   traits_ip_address;
    public Boolean                  traits_is_anonymous_proxy;
    public Boolean                  traits_is_satellite_provider;
    public String                   traits_isp;
    public String                   traits_organization;
    public String                   traits_user_type;


    /**
     * Generic query helper for entity WikiUser.
     */
    public static Finder<Long, UserLogin> find = new Finder<Long, UserLogin>(Long.class, UserLogin.class);

}