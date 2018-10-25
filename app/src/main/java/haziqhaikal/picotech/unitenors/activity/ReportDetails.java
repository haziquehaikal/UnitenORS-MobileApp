package haziqhaikal.picotech.unitenors.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import haziqhaikal.picotech.unitenors.R;
import haziqhaikal.picotech.unitenors.app.AppConfig;
import haziqhaikal.picotech.unitenors.app.AppController;
import haziqhaikal.picotech.unitenors.helper.SessionManager;

/**
 * Created by user on 12/8/2017.
 */

public class ReportDetails extends AppCompatActivity {

    private static final String TAG = ReportDetails.class.getSimpleName();
    SessionManager session;
    //SQLiteHandler db;
    EditText inputEmail,inputPassword;
    Button btnSolve , btnUnresolve;
    TextView stuid , date ,stat , desc, subj,comt,buatoleh;
    ProgressDialog pDialog;
    ImageView image;
    Bitmap imgg;
    ImageView imageView;
    LinearLayout testo;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
       // db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //get data from listview
        Bundle b;
        b = getIntent().getExtras();
        //String sid = b.getString("sid");
        final String rid = b.getString("id");
        final String infodes = b.getString("desc");
        final String stats = b.getString("stat");
        final String rdate = b.getString("date");
        final String comm = b.getString("comment");
        final String sub = b.getString("subj");
        final String imgg = b.getString("image");
        final String doneby = b.getString("doneby");

        String wutoh =  imgg.replaceAll("\n", "");
        byte[] decodedString = Base64.decode(wutoh, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView image = this.findViewById(R.id.imageButton);
        image.setImageBitmap(decodedByte);

        setTitle("Report Details for "  + rid);

        //assign the retrived data to the textView
        subj = findViewById(R.id.subj);
        stuid = findViewById(R.id.doneby);
        date = findViewById(R.id.date);
        stat = findViewById(R.id.stat);
        desc = findViewById(R.id.desc);
        subj = findViewById(R.id.subj);
        comt = findViewById(R.id.comment);
        buatoleh = findViewById(R.id.doneby);
        //image = findViewById(R.id.imageButton);
            date.setText(rdate);
        stat.setText(stats);
        desc.setText(infodes);
        //stuid.setText(sid);
        comt.setText(comm);
        subj.setText(sub);
        buatoleh.setText(doneby);

        imageView = findViewById(R.id.imageButton);
        testo = (LinearLayout) findViewById(R.id.SubT);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testo.isShown()) {
                    testo.setVisibility(View.INVISIBLE);
                }
                else {
                    testo.setVisibility(View.VISIBLE);
                }
            }
        });

        //button for update


        btnSolve = findViewById(R.id.solved);
        String ayam = "SOLVED";
        if(stats.equals(ayam))
        {
            btnSolve.setEnabled(false);
        }
        else
        {
            btnSolve.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    AlertDialog.Builder out = new AlertDialog.Builder(ReportDetails.this);

                    String ayatKeluar = "Are you sure to cancel report " + rid + " ? ";
                    out.setTitle("Confirm Update ?");
                    out.setMessage(ayatKeluar);

                    out.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            deleteRep("1",rid);
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });

                    out.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = out.create();
                    alert.show();
                }

            });


        }

    }



    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
        return super.onOptionsItemSelected(item);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    public void onBackPressed() {
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }


    }


    public void deleteRep(final String val,final String rid)
    {
        pDialog.setMessage("Updating....");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG , "RESPONSE_UPDATE" + response.toString());
                hideDialog();

                try
                {
                    JSONObject jobj = new JSONObject(response);
                    boolean error  = jobj.getBoolean("error_flag");
                    if(!error)
                    {
                        Toast.makeText(getApplicationContext(), "Report successfully canceled", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to delete report", Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e)
                {
                    e.printStackTrace();

                }

            }

        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("update_val",val);
                params.put("repID",rid);
                return params;
            }
        };

        String tag_string_req = "update_date_push";
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }


}
