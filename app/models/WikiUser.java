package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder; // Import Finder as sometimes Play! shows compilation error "not found: type Finder"

import javax.persistence.*;

/**
 * Model class that maps to table wiki_user. This is the user of the service.
 *
 * Date: 31/01/14
 * Time: 11:19
 *
 * @author Sav Balac
 * @version 1.0
 */
@Entity
@Table(name="wiki_user")
public class WikiUser extends Model {

    // Instance variables
    @Constraints.Required                     // A required constraint will ensure fields have values
    @SequenceGenerator(name="seq_gen_name", sequenceName="wiki_user_id_seq") // Required for a Postgres database
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen_name")
    @Id
    public Long                     id;

    @Constraints.Required
    @Formats.NonEmpty                         // A non-empty format will convert spaces to null and ensure validation
    public String                   username;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   password;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   email;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   forename;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   surname;



    /**
     * Generic query helper for entity WikiUser.
     */
    public static Finder<Long, WikiUser> find = new Finder<Long, WikiUser>(Long.class, WikiUser.class);

}