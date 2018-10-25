package haziqhaikal.picotech.unitenors.app;

/**
 * Created by haziqhaikal on 11/28/2017.
 */

public class AppConfig {

    //

    ////*****************dev usage only*********************

    ////login

    public static String URL_LOGIN = "http://192.168.43.135/ors/login/login.php";

    //

    ////upload

    public static String URL_DATA = "http://192.168.43.135/ors/report/uploadd.php";

    //

    ////view report

    public static String URL_VIEW = "http://192.168.43.135/ors/report/viewReport.php?stuid=";

    ////*******************************************************

    //

    /*****************live usage only*********************/

    //login


    //public static String URL_LOGIN = "https://sufian.hakimco.ml/login/login.php";

    //upload

   // public static String URL_DATA = "https://sufian.hakimco.ml/report/uploadd.php";

    //view report

    //public static String URL_VIEW = "https://sufian.hakimco.ml/report/viewReport.php?stuid=";

    public static String URL_DELETE = "https://192.168.43.135/ors/report/delete.php";

    //notofy


    /*******************************************************/


    /*************************************************************************/

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


}