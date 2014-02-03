package models;

import org.joda.time.DateTime;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder; // Import Finder as sometimes Play! shows compilation error "not found: type Finder"
import javax.persistence.*;

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


}