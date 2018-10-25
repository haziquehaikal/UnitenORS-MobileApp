package haziqhaikal.picotech.unitenors.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;

import haziqhaikal.picotech.unitenors.R;
import haziqhaikal.picotech.unitenors.helper.SessionManager;

/**
 * Created by haziqhaikal on 11/28/2017.
 */


public class AboutReport extends Fragment implements View.OnClickListener {

    public Button btn_logout;
    private SessionManager session;

    private View rootView;
    public static AboutReport newInstance(){
        AboutReport fragment = new AboutReport();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState)
    {

        rootView = inflater.inflate
                (R.layout.about_report, container, false);
        session = new SessionManager(getActivity());
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            session.setLogin(false);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        }




        btn_logout = rootView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View view) {

        HashMap<String,String> user = session.getDetails();

        final String saja = user.get(SessionManager.PEGI_MANA);

        switch (view.getId())
        {
            case R.id.btn_logout:
                AlertDialog.Builder out = new AlertDialog.Builder(getActivity());

                String ayatKeluar = "Are you sure? , " + saja;
                out.setTitle(ayatKeluar);


                out.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        logout();
                        dialog.dismiss();
                    }
                });

                out.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = out.create();
                alert.show();
                break;
            default:
                break;
        }


    }

    public void logout()
    {
        session.setLogin(false);
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }


    //-UI memory leak issue fix
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }
}
