package utils;

import org.joda.time.DateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contains utility methods and constants.
 *
 * Date: 16/10/13
 * Time: 13:23
 *
 * @author      Sav Balac
 * @version     1.2
 */
public class Utils {

    // Constants
    public static final int TEXT_BYTES = 65535; // A text column is 2^16 bytes

    // Maximum upload file size of 10 Mb
    public static final int    MAX_FILE_SIZE = 10485760; // In bytes
    public static final String MAX_FILE_SIZE_STRING = MAX_FILE_SIZE / 1048576 + "Mb";

    public static final int LOG_LEVEL_NORMAL  = 0;
    public static final int LOG_LEVEL_DEBUG   = 1; // Not yet implemented
    public static final int LOG_LEVEL_VERBOSE = 2; // Not yet implemented

    public static final int LOG_TYPE_ERROR   = -1;
    public static final int LOG_TYPE_GENERAL =  0;

    public static final String KEY_INFO    = "info";
    public static final String KEY_ERROR   = "error";
    public static final String KEY_SUCCESS = "success";

    public static final String DATETIME_FORMAT = "dd-MMM-yyyy HH:mm";


    /**
     * Logs caught exceptions.
     *
     * @param module  The module the exception occurred in.
     * @param e       The exception thrown.
     */
    public static void eHandler(String module, Exception e) {
        String msg = "ERROR in " + module + ": " + e.getMessage() + " (" + e.getClass() + ")";
        addLogMsg(LOG_LEVEL_NORMAL, LOG_TYPE_ERROR, msg);
    }


    /**
     * Logs application errors.
     *
     * @param module   The module the error occurred in.
     * @param message  The error message.
     */
    public static void errorHandler(String module, String message) {
        String msg = "ERROR in " + module + ": " + message;
        addLogMsg(LOG_LEVEL_NORMAL, LOG_TYPE_ERROR, msg);
    }


    /**
     * Logs application information.
     *
     * @param module   The module the event occurred in.
     * @param message  The information message.
     */
    public static void logEvent(String module, String message) {
        String msg = module + ": " + message;
        addLogMsg(LOG_LEVEL_NORMAL, LOG_TYPE_GENERAL, msg);
    }


    /**
     * Logs a message. Log level and type to be implemented.
     *
     * @param logLevel   0 = Normal, 1 = Debug, 2 = Verbose.
     * @param logType   -1 = Error, 0 = General.
     * @param msg       Description of the message.
     */
    public static void addLogMsg(int logLevel, int logType, String msg) {
        System.out.println("");
        System.out.println(Calendar.getInstance().getTime().toString() +   ": " + msg);
        System.out.println("");
    }


    /**
     * Returns a list of Integers based on start and end values.
     *
     * @param start    Start value.
     * @param end      End value.
     * @return List<Integer>  A list of integers.
     */
    public static List<Integer> getIntegers(int start, int end) {
        int numIntegers = end - start + 1;
        List<Integer> list = new ArrayList<Integer>(numIntegers);
        for (int idx = 0; idx < numIntegers; idx++) {
            int value = start + idx;
            list.add(idx, value);
        }
        return list;
    }


    /**
     * Returns titles typically used in a select.
     *
     * @return Map<String,String>  The titles (key and value are the same).
     */
    public static Map<String,String> getTitles() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();

        options.put(null, "Select a value");
        options.put("Mr.","Mr.");
        options.put("Mrs.","Mrs.");
        options.put("Miss","Miss");
        options.put("Ms.","Ms.");
        options.put("Dr.","Dr.");
        options.put("Prof.","Prof.");
        options.put("Rev.","Rev.");
        options.put("Other","Other");

        return options;
    }


    /**
     * Returns a friendly "Created on <date> at <time>" string.
     *
     * @param datetime  Input Timestamp.
     * @return String   The formatted string.
     */
    public static String formatCreatedTimestamp(Timestamp datetime) {
        return "Created on " + formatTimestampVerbose(datetime);
    }


    /**
     * Returns a friendly "Created on <date> at <time>" string.
     *
     * @param datetime  Input DateTime.
     * @return String   The formatted string.
     */
    public static String formatCreatedTimestamp(DateTime datetime) {
        return "Created on " + formatTimestampVerbose(datetime);
    }


    /**
     * Returns a friendly "Last updated on <date> at <time>" string.
     *
     * @param datetime  Input DateTime.
     * @return String   The formatted string.
     */
    public static String formatUpdatedTimestamp(DateTime datetime) {
        return "Last updated on " + formatTimestampVerbose(datetime);
    }


    /**
     * Formats a timestamp as "<date> at <time>", e.g. Monday 21-Oct-2013 at 12:34.
     *
     * @param datetime  The input Timestamp.
     * @return String   The formatted timestamp.
     */
    public static String formatTimestampVerbose(Timestamp datetime) {
        return new SimpleDateFormat("EEEE dd-MMM-yyyy").format(datetime) + " at " +
                "" + new SimpleDateFormat("HH:mm").format(datetime);
    }


    /**
     * Formats a datetime as "<date> at <time>", e.g. Monday 21-Oct-2013 at 12:34.
     *
     * @param datetime  The input DateTime.
     * @return String   The formatted datetime.
     */
    public static String formatTimestampVerbose(DateTime datetime) {
        return new SimpleDateFormat("EEEE dd-MMM-yyyy").format(datetime.toDate()) + " at " +
                   "" + new SimpleDateFormat("HH:mm").format(datetime.toDate());
    }


    /**
     * Formats a timestamp as "<date> <time>", e.g. 21-Oct-2013 12:34.
     *
     * @param datetime  The input Timestamp.
     * @return String   The timestamp as a string.
     */
    public static String formatTimestamp(Timestamp datetime) {
        return new SimpleDateFormat(DATETIME_FORMAT).format(datetime);
    }


    /**
     * Formats a datetime as "<date> <time>", e.g. 21-Oct-2013 12:34.
     *
     * @param datetime  The input DateTime.
     * @return String   The datetime as a string.
     */
    public static String formatTimestamp(DateTime datetime) {
        return new SimpleDateFormat(DATETIME_FORMAT).format(datetime.toDate());
    }


    /**
     * Returns the current date time as a DateTime.
     *
     * @return DateTime  The current date and time.
     */
    public static DateTime getCurrentDateTime() {
        return new DateTime();
    }


    /**
     * Hashes a string using the SHA-256 hashing algorithm.
     * This doesn't generate unique values - there is a very small chance that 2 input strings hash to the same value.
     *
     * @param  input   Input string.
     * @return String  The hashed string.
     * @throws java.security.NoSuchAlgorithmException  If the algorithm doesn't exist.
     */
    public static String hashString(String input) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        byte byteData[] = md.digest();

        // Convert the bytes to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }


}
