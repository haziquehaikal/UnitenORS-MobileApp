package haziqhaikal.picotech.unitenors.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import haziqhaikal.picotech.unitenors.R;

/**
 * Created by haziqhaikal on 12/1/2017.
 */

public class ReportAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Report> reportList;

    public ReportAdapter(Activity activity, List<Report> movieList) {
        this.activity = activity;
        this.reportList = movieList;
    }

    @Override
    public int getCount() {
        return reportList.size();
    }

    @Override
    public Object getItem(int location) {
        return reportList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getRepID(int position)
    {
        return reportList.get(position).id;
    }

    public String getDesc(int position)
    {
        return reportList.get(position).des;
    }

    public String getStat(int position)
    {
        return reportList.get(position).stat;
    }

    public String getDate(int position)
    {
        return reportList.get(position).date;
    }

    public String getCom(int position)
    {
        return reportList.get(position).comment;
    }

    public String getSubj(int position)
    {
        return reportList.get(position).subj;
    }
    public String getImg(int position)
    {
        return reportList.get(position).imagedta;
    }
    public String getDone(int position)
    {
        return reportList.get(position).doneby;
    }
   /* public String getStuId(int position)
    {
        return reportList.get(position).sid;
    }*/



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView serial = convertView.findViewById(R.id.serial);
        TextView title = convertView.findViewById(R.id.title);
        TextView subdate = convertView.findViewById(R.id.date);

        serial.setText(String.valueOf(reportList.get(position).stat));
        subdate.setText(String.valueOf(reportList.get(position).date));
        title.setText(reportList.get(position).subj);

        // String color = bgColors[position % bgColors.length];
        //title.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}