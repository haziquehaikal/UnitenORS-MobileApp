package haziqhaikal.picotech.unitenors.helper;

/**
 * Created by haziqhaikal on 12/1/2017.
 */

public class Report {

    //public String sid;
    public String id;
    public String title;
    public String stat;
    public String des;
    public String date;
    public String comment;
    public String subj;
    public String imagedta;
    public String doneby;

    public Report(String des, String stat , String date , String rid , String comment,String subj,String imagedta,String doneby) {
        //this.title = title;
        this.id = rid;
        //this.sid = sid;
        this.stat = stat;
        this.des = des;
        this.date = date;
        this.comment = comment;
        this.subj = subj;
        this.imagedta = imagedta;
        this.doneby = doneby;
    }
}