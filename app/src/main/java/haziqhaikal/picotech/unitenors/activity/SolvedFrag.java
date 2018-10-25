package haziqhaikal.picotech.unitenors.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import haziqhaikal.picotech.unitenors.R;
import haziqhaikal.picotech.unitenors.app.AppConfig;
import haziqhaikal.picotech.unitenors.app.AppController;
import haziqhaikal.picotech.unitenors.helper.Report;
import haziqhaikal.picotech.unitenors.helper.ReportAdapter;
import haziqhaikal.picotech.unitenors.helper.SessionManager;

/**
 * Created by Haziq Haikal on 12/1/2018.
 */

public class SolvedFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ReportAdapter adapter;
    private List<Report> reportList;
    private SessionManager session;
    private View rootView;

    public SolvedFrag() {

        }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.list_solved, container, false);
        listView = rootView.findViewById(R.id.listView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        session = new SessionManager(getActivity());
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            session.setLogin(false);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }



        reportList = new ArrayList<>();
        adapter = new ReportAdapter(getActivity(), reportList);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchReport();
            }
        });


        //OnClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ReportDetails.class);
                i.putExtra("id",adapter.getRepID(position));
                i.putExtra("desc",adapter.getDesc(position));
                i.putExtra("stat",adapter.getStat(position));
                i.putExtra("date",adapter.getDate(position));
                // i.putExtra("sid",adapter.getStuId(position));
                i.putExtra("comment", adapter.getCom(position));
                i.putExtra("subj",adapter.getSubj(position));
                i.putExtra("image",adapter.getImg(position));
                i.putExtra("doneby",adapter.getDone(position));
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);

                Log.d("testrd", adapter.getRepID(position));

            }
        });

        return rootView;
    }

    @Override
    public void onRefresh() {

        reportList.clear();
        fetchReport();

    }

    private void fetchReport() {
        HashMap<String,String> user = session.getDetails();
        final String id = user.get(SessionManager.KEY_STUID);


        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);


        // appending offset to url
        String url = AppConfig.URL_VIEW + id + "&stat=1";

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {

                                try {
                                    JSONObject res = response.getJSONObject(i);
                                    boolean error = res.getBoolean("error_flag");

                                    if (error == false) {
                                        String des = res.getString("des");
                                        String stat = res.getString("stat");
                                        String subdate = res.getString("sub_date");
                                        String rid = res.getString("id");
                                        String comt = res.getString("comment");
                                        //String sid = res.getString("sid");
                                        String subj = res.getString("subj");
                                        String imagedta = res.getString("image_data");
                                        String bydone = res.getString("job_done");

                                        Report m = new Report(des, stat, subdate, rid , comt,subj,imagedta,bydone);
                                        reportList.add(m);

                                    }
                                    else
                                    {
                                        String errormsg = res.getString("error_msg");
                                        Log.d(TAG, String.valueOf(error));
                                        AlertDialog.Builder out = new AlertDialog.Builder(getActivity());
                                        String ayaterr =  "We can't find your data " +
                                                "\nERROR MSG: " + errormsg;
                                        out.setTitle("Opps! , something goes wrong :(");
                                        out.setMessage(ayaterr);
                                        out.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do nothing but close the dialog
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog alert = out.create();
                                        alert.show();
                                    }

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onDestroyView()
    {

        super.onDestroyView();
        rootView = null;
    }

}
