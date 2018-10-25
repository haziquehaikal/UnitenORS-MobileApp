package haziqhaikal.picotech.unitenors.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by haziqhaikal on 11/27/2017.
 */

    public class SessionManager {

            // LogCat tag
            private static String TAG = SessionManager.class.getSimpleName();

            // Shared Preferences
            SharedPreferences pref;

            SharedPreferences.Editor editor;
            Context _context;

            // Shared pref mode
            int PRIVATE_MODE = 0;

            // Shared preferences file name
            private static final String PREF_NAME = "UniteORSLog";

            private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
            public static final String KEY_STUID = "id";
            public static final String PEGI_MANA = "id";

            public SessionManager(Context context) {
                this._context = context;
                pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
                editor = pref.edit();
            }

            public HashMap<String,String> getDetails()
            {
                HashMap<String,String> user = new HashMap<>();
                user.put(PEGI_MANA , pref.getString(KEY_STUID,null));
                return user;
            }

            public void setDetails(String id)
            {
                editor.putString(KEY_STUID , id);
                editor.commit();
            }

            public void setLogin(boolean isLoggedIn) {


                editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);


                // commit changes
                editor.commit();

                Log.d(TAG, "User login session modified!");
            }

            public boolean isLoggedIn(){
                return pref.getBoolean(KEY_IS_LOGGED_IN, false);
            }
    }
