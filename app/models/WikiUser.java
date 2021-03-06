package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder; // Import Finder as sometimes Play! shows compilation error "not found: type Finder"
import utils.Utils;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

/**
 * Model class that maps to table wiki_user. This is the user of the service. Login requires authentication.
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
    @Constraints.Required // A required constraint will ensure fields have values
    @SequenceGenerator(name="seq_gen_name", sequenceName="wiki_user_id_seq") // Required for a Postgres database
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen_name")
    @Id
    public Long                     id;

    @Constraints.Required
    @Formats.NonEmpty
    public String                   username; // A non-empty format will convert spaces to null and ensure validation

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


    /**
     * Checks the wiki user's password. The hashed password is 64 characters long.
     *
     * @param  username  Username.
     * @param  password  Password to be hashed before checking.
     * @return User      The authenticated user.
     * @throws NoSuchAlgorithmException    If the hashing algorithm doesn't exist.
     */
    public static WikiUser authenticate(String username, String password) throws NoSuchAlgorithmException {
        return find.where().eq("username", username)
                           .eq("password", Utils.hashString(password)).findUnique();
    }


}