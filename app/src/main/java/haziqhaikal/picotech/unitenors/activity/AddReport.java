package haziqhaikal.picotech.unitenors.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import haziqhaikal.picotech.unitenors.R;
import haziqhaikal.picotech.unitenors.app.AppConfig;
import haziqhaikal.picotech.unitenors.helper.SessionManager;

import static android.app.Activity.RESULT_OK;
import static haziqhaikal.picotech.unitenors.app.AppController.TAG;

/**
 * Created by haziqhaikal on 11/27/2017.
 */

public class AddReport extends Fragment implements View.OnClickListener ,  AdapterView.OnItemSelectedListener {

    //location


    /**********************************************/

    int PICK_IMAGE_REQUEST = 111;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private View rootView;


    Button snappic, gallpic, uploadpic;
    EditText des, subj;
    TextView repid;
    ImageView image;
    private  Bitmap bitmap,asd;
    SessionManager session;
    String deviceid;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Spinner lokasi,type;
    String item_type,item_loc;
    String imageString;


    ProgressDialog progressDialog;

    public static AddReport newInstance() {
        AddReport fragment = new AddReport();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//reserve memory (to fix memory overflow)-- accumulated memory
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        //layout
        //initialize fragment
        rootView =      inflater.inflate
                (R.layout.add_report, container, false);

        session = new SessionManager(getActivity());
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            session.setLogin(false);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }


        //location detection -- geolocation
        ((MainActivity) getActivity()).displayLocation();


        //button
        snappic = rootView.findViewById(R.id.takecam);
        gallpic = rootView.findViewById(R.id.takegal);
        uploadpic = rootView.findViewById(R.id.uploadData);
        image = rootView.findViewById(R.id.imageView);

        snappic.setOnClickListener(this);
        gallpic.setOnClickListener(this);
        uploadpic.setOnClickListener(this);


        //input form
        des = rootView.findViewById(R.id.des);
        subj = rootView.findViewById(R.id.prob_sub);
        HashMap<String, String> user = session.getDetails(); //get ID user from session
        final String id = user.get(SessionManager.PEGI_MANA);
        repid = rootView.findViewById(R.id.repid);
        repid.setText(id);

        //SPINNER
        lokasi = rootView.findViewById(R.id.prob_loc);
        type = rootView.findViewById(R.id.prob_type);
        lokasi.setOnItemSelectedListener(this);
        type.setOnItemSelectedListener(this);
        //type

        //session -- login
        session = new SessionManager(getActivity());


        //FIREBASE NOTIFICATIONS
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);

                    ((MainActivity) getActivity()).displayFirebaseRegId();//display device id

                } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getActivity(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };


        //GET DEVICE ID FOR fb notifications  TO SERVER
        deviceid = ((MainActivity) getActivity()).displayFirebaseRegId();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takegal:
                selectImage();
                break;
            case R.id.takecam:
                takeImage();
                break;
            case R.id.uploadData:
                submitReport();
                break;
            default:
                break;
        }

    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult
                (Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void takeImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }


    public void submitReport() {

        //KITE NK DPAT user details !
        HashMap<String, String> user = session.getDetails(); //get ID user from session

        //int variable id untuk dpat student id
        final String id = user.get(SessionManager.PEGI_MANA);

        //loding figure
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Submitting data...");
        progressDialog.show();


        //Convert image into base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//declare obj
        if(bitmap == null)
        {
            asd.compress(Bitmap.CompressFormat.JPEG, 100, baos);//convert binary jadi jpeg
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);//convert jadi byte array -- base 64
        }
        else
        {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//convert binary jadi jpeg
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);//convert jadi byte array -- base 64
        }



        //user form input
        final String desc = des.getText().toString().trim();
        final String subject = subj.getText().toString().trim();

        //init volley data transfer -- sebab nak hantar obj as string
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());//nak dapat response server -- success ke tak
                progressDialog.dismiss();//hilang loading -- sebab dah dapat response server

                try {

                    JSONObject jObj = new JSONObject(response);//json obj
                    String error = jObj.getString("message");
                    Log.d(TAG, "server_response: " + error);
                    if (Objects.equals(error, "OK")) {
                        Toast.makeText(getActivity(), "UPLOAD SUCCESS", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "UPLOAD ERROR", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {//json error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {//web server error
                Log.e(TAG, "Upload Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }


        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString);
                params.put("desc", desc);
                params.put("subj", subject);
                params.put("stuid", id);
                params.put("type",item_type);
                params.put("loc",item_loc);
                params.put("lat", String.valueOf(((MainActivity) getActivity()).getlat()));
                params.put("lon", String.valueOf(((MainActivity) getActivity()).getlon()));
                params.put("deviceid", deviceid);
                return params;
            }

        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                //setting image to imageview
                image.setImageBitmap(bitmap);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                //getting image from camera
                asd = (Bitmap) data.getExtras().get("data");

                //setting image to imageview
                image.setImageBitmap(asd);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

        String data_prob [] = getResources().getStringArray(R.array.prob_loc);
        String data_type [] =  getResources().getStringArray(R.array.prob_type);

        Spinner spinner = (Spinner) parent;
        switch(parent.getId())
        {
            case (R.id.prob_loc):
                 item_loc = data_prob [position];
                Toast.makeText(parent.getContext(), "Selected: " + item_loc
                        , Toast.LENGTH_LONG).show();
                break;

            case (R.id.prob_type):
                 item_type = data_type [position];
                Toast.makeText(parent.getContext(), "Selected: " + item_type, Toast.LENGTH_LONG).show();
                break;


        }
        //Toast.makeText(parent.getContext(), "Selected: " + (position+1), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
