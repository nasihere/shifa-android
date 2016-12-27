package com.shifa.kent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
public class UserCustomAdapter extends ArrayAdapter<User> implements Filterable  {
    Context context;
    int layoutResourceId;
    DBclass db1;
    private ProgressBar pb;
    private boolean gotdata;
    UserHolder holder = null;
    String ShifaInteractiveFlag;
    public ImageManager imageManager;
    boolean filterResult = true;
    final ArrayList<User> data;//new ArrayList<User>();
    ArrayList<User> filterdata = new ArrayList<User>();
    String SmartCategoy = "";
    int iCounterForComment = 0;
    public final Super_Library_AppClass SLAc;
    public UserCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<User> arrdata, String Category) {
        super(context, layoutResourceId, arrdata);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.filterdata = arrdata;
        this.data = arrdata;
        this.SmartCategoy = Category;
        db1 = new DBclass(context);
        imageManager =
                new ImageManager(context.getApplicationContext());
        Log.e("USercustomadapter constructor","Data Populate done");
        this.SLAc = new Super_Library_AppClass(context);






    }
    @Override
    public int getCount() {
        return filterdata.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("runnable","convertView");

        View row = convertView;
        Log.e("row","Start");

        if (row == null) {
            Log.e("cache","false");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            Log.e("cache","3.0.8");
            row = inflater.inflate(layoutResourceId, parent, false);
            Log.e("cache","3.0.9");
            holder = new UserHolder();
            Log.e("cache","3.1.0");
            holder.textName = (TextView) row.findViewById(R.id.txtName_l_r_d);
            Log.e("cache","3.1.1");
            holder.txtCategory = (TextView) row.findViewById(R.id.txtCategory_l_r_d);
            Log.e("cache","3.2");
            holder.txtCounter = (TextView) row.findViewById(R.id.txtCounter_l_r_d);
            Log.e("cache","3.3");
            holder.txtRemedies = (TextView) row.findViewById(R.id.txtRemedies_l_r_d);
            Log.e("cache","3.4");
            holder.chkHolderBox = (CheckBox) row.findViewById(R.id.chckidbox_l_r_d);
            Log.e("cache","3.5");
            holder.btnDelete = (ImageButton) row.findViewById(R.id.imgRem_l_r_d);
            Log.e("cache","3.6");
            holder.imgBtnReportRepertory = (ImageButton) row.findViewById(R.id.imgBtnReportRepertory);



            Log.e("cache","3.1");
            holder.chkHolderBox.setTag(R.string.ListPosition, holder);
            Log.e("cache","4");
            holder.chkHolderBox.setTag(R.string.holder, holder);

            holder.btnDelete.setTag(R.string.RemediesShowHide, holder);
            row.setTag(holder);
            Log.e("cache","5");
        } else {
            Log.e("cache","true");

            holder = (UserHolder) row.getTag();
            Log.e("cache","3.17");
        }

        Log.e("Getview",String.valueOf(position));
        try
        {
            User user = filterdata.get(position);
            Log.e("cache","3.18");
            holder.chkHolderBox.setTag(R.string.ListPosition, position);

            holder.chkHolderBox.setTag(R.string.RemediesShowHide, user.getCounter());
            Log.e("cache","3.19");


            holder.textName.setText(user.getName());
            holder.txtCategory.setText(user.getAddress());
            String sCounter = user.getCounter();
            Log.e("cache","3.20");
            Log.e("Getview","progress 10%");
            holder.btnDelete.setTag(R.string.ListPosition, position);
            //Log.e("getId check ", String.valueOf(user.getRemediesShowHide()));
            int sRemedies = user.getRemediesShowHide();
            Log.e("cache","3.21");
            int iSublevel = user.getSubLevel();
            String[] urlChapter = user.getAddress().split(", ");
            getIconMenu(urlChapter[0].toLowerCase(),holder.btnDelete);
            if (iSublevel == 2)
            {
                sCounter = "";
                //holder.chkHolderBox.setEnabled (false);
                holder.chkHolderBox.setVisibility(View.GONE);

                //holder.btnDelete.setBackgroundResource(R.drawable.ic_launcher);

             //   imageManager.displayImage(, , R.drawable.ic_launcher);
                holder.txtRemedies.setVisibility(View.GONE);
            }
            else if (sRemedies == 1)
            {
					 /*
					 Log.e("counter","ic_cateogry");
					 holder.chkHolderBox.setEnabled(true);
					 holder.btnDelete.setBackgroundResource(R.drawable.ic_cateogry);
					 holder.txtRemedies.setVisibility(View.GONE);
					 */
                Log.e("counter","ic_cateogryminus");
                String HtmlRem = getRemediesColor(user.getremedies(),user.book);
                holder.txtRemedies.setText(Html.fromHtml(HtmlRem), TextView.BufferType.SPANNABLE);
                //holder.chkHolderBox.setEnabled(true);
                holder.chkHolderBox.setVisibility(View.VISIBLE);
                //holder.btnDelete.setBackgroundResource(R.drawable.ic_cateogryminus);
            //    imageManager.displayImage("-"+urlChapter[0].toLowerCase(), holder.btnDelete, R.drawable.ic_launcher);
                holder.txtRemedies.setVisibility(View.VISIBLE);
            }
            else if (sRemedies == 0)
            {

                Log.e("counter","ic_cateogryminus");
                String HtmlRem = getRemediesColor(user.getremedies(),user.book);
                holder.txtRemedies.setText(Html.fromHtml(HtmlRem), TextView.BufferType.SPANNABLE);
                //holder.chkHolderBox.setEnabled(true);
                holder.chkHolderBox.setVisibility(View.VISIBLE);
                //holder.btnDelete.setBackgroundResource(R.drawable.ic_cateogryminus);
            //    imageManager.displayImage("-"+urlChapter[0].toLowerCase(), holder.btnDelete, R.drawable.ic_launcher);
                holder.txtRemedies.setVisibility(View.VISIBLE);

            }
            if (sCounter.equals("")){
                holder.txtCounter.setVisibility(View.GONE);
            }
            else
            {
                holder.txtCounter.setVisibility(View.VISIBLE);
            }
            holder.txtCounter.setText(user.book);
            Log.e("cache","3.23");
            Log.e("Getview","progress 98%");
            Log.e("user get selected value",user.getSelected().toString());
            if (user.getSelected().equals("1"))
            {
                Log.e("holder.chkHolderBox","Setselected trye");
                holder.chkHolderBox.setChecked(true);
            }
            else
            {
                Log.e("holder.chkHolderBox","Setselected false");
                holder.chkHolderBox.setChecked(false);
            }
                holder.chkHolderBox.setTag(R.string.RemediesShowHide, user.getID());
                Log.e("Getview","progress 100%");



                 holder.imgBtnReportRepertory.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, activity_report.class);
                        intent.putExtra("KentRepertory", true);
                        context.startActivity(intent);





                    }
                });

                holder.chkHolderBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        UserHolder holder1 = (UserHolder)buttonView.getTag(R.string.holder);

                        int iTag = (Integer) buttonView.getTag(R.string.ListPosition);
                        Log.e("iTag",String.valueOf(iTag));
                        User us = filterdata.get(iTag);
                        Log.e("us"," user us filled ");
                        int _id = (Integer) buttonView.getTag(R.string.RemediesShowHide);
                        Log.e("_id", String.valueOf(_id));
                        if(isChecked) {

                            us.setSelected("1");
                            Log.e("Check","CHECKED1");
                            db1.KentItemSeleted("1", _id);
                            Log.e("Check","CHECKED");
                            holder1.imgBtnReportRepertory.setVisibility(View.VISIBLE);

                            SLAc.PostWebApi(new String[] {us.book + "~"+ us.title, "http://shifa.in/api/RepertoryService/ExtraSaveHitLike"});
                        } else {

                            holder1.imgBtnReportRepertory.setVisibility(View.GONE);
                            us.setSelected("0");
                            Log.e("Check","UNCHECKED");
                            db1.KentItemSeleted("0", _id);
                            Log.e("Check","UNCHECKED");
                        }
                    }
                });


                return row;
            }
        catch(Exception exx)
        {
            Log.e("error",exx.toString());
            return row;

        }
    }
    /**
     ���������* Implementing the Filterable interface.
     ���������*/
    private String RestoreShifaConnectType()
    {
        SharedPreferences prefs = context.getSharedPreferences("AppNameSettings",0);
        String restoredText = prefs.getString("SaveShifaConnectType", null);
        if (restoredText != null)
        {
            return restoredText;
        }
        return "";

    }
    public String getColorOnRemedies(String r)
    {

        //window.MyCls.log(remedies);
        return "";
    }
    static class UserHolder {
        TextView textName;
        TextView txtCategory;
        TextView txtCounter;
        TextView txtRemedies;
        ImageButton btnDelete;
        CheckBox chkHolderBox;
        ImageButton imgBtnReportRepertory;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<User> results = new ArrayList<User>();

                ArrayList<User> orig = new ArrayList<User>();
                Log.e("orig","empty");

                Log.e("orig","true");
                orig = data;
                Log.e("orig","false");

                Log.e("orig",String.valueOf(orig.size()));
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final User g : orig) {
                            String[] sWords = constraint.toString().split(" ");
                            int iMatched = 0;
                            for (String sWrd : sWords) {

                                Log.e("sWrd ", sWrd);
                                if (g.getName().toLowerCase()
                                        .contains(sWrd.toLowerCase().toString()))
                                {
                                    iMatched++;


                                }
                                else if (g.getAddress().toLowerCase()
                                        .contains(sWrd.toLowerCase().toString()))
                                {
                                    iMatched++;


                                }


                            }
                            Log.e("sWrd.length()", String.valueOf(sWords.length));
                            Log.e("iMatched ", String.valueOf(iMatched));
                            if (iMatched == sWords.length )
                            {

                                results.add(g);

                            }

                        }
                    }
                    oReturn.count = results.size();
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                ArrayList<User> items;
                Log.e("orig","notifyDataSetChanged");

                items = (ArrayList<User>) results.values;

                Log.e("orig","notifyDataS items etChanged");
                notifyDataSetChanged(items);
            }
        };
    }

    public void notifyDataSetChanged(ArrayList<User> performfilter) {
        super.notifyDataSetChanged();
        Log.e("runnable","action");
        this.filterdata = performfilter;
        Log.e("runnable","action end");
        // notifyChanged = true;
    }
    public String getRemediesColor(String r, String book)
    {
        Log.e("getRemediesColor", book);

        if (book.equalsIgnoreCase("Boenninghausens")){
            return getRemediesBoenninghausens(r);
        }
        else
        {
            return getRemediesKent(r);
        }

    }
    private void getIconMenu(String cap, ImageView img){
        cap = cap.replaceAll(" ","_");
        if (cap.equalsIgnoreCase("abdomen"))
            img.setBackgroundResource(R.drawable._abdomen);
        else if (cap.equalsIgnoreCase("abdomen_external"))
            img.setBackgroundResource(R.drawable._abdomen_external);
        else if (cap.equalsIgnoreCase("anus"))
            img.setBackgroundResource(R.drawable._anus);
        else if (cap.equalsIgnoreCase("anus_and_rectum"))
            img.setBackgroundResource(R.drawable._anus_and_rectum);
        else if (cap.equalsIgnoreCase("appetite"))
            img.setBackgroundResource(R.drawable._appetite);
        else if (cap.equalsIgnoreCase("aversions"))
            img.setBackgroundResource(R.drawable._aversions);
        else if (cap.equalsIgnoreCase("back"))
            img.setBackgroundResource(R.drawable._back);
        else if (cap.equalsIgnoreCase("bladder"))
            img.setBackgroundResource(R.drawable._bladder);
        else if (cap.equalsIgnoreCase("blood"))
            img.setBackgroundResource(R.drawable._blood);
        else if (cap.equalsIgnoreCase("bones"))
            img.setBackgroundResource(R.drawable._bones);
        else if (cap.equalsIgnoreCase("brain"))
            img.setBackgroundResource(R.drawable._brain);
        else if (cap.equalsIgnoreCase("chest"))
            img.setBackgroundResource(R.drawable._chest);
        else if (cap.equalsIgnoreCase("chest_and_lungs"))
            img.setBackgroundResource(R.drawable._chest_and_lungs);
        else if (cap.equalsIgnoreCase("chill"))
            img.setBackgroundResource(R.drawable._chill);
        else if (cap.equalsIgnoreCase("circulation"))
            img.setBackgroundResource(R.drawable._circulation);
        else if (cap.equalsIgnoreCase("compound_fever"))
            img.setBackgroundResource(R.drawable._compound_fever);
        else if (cap.equalsIgnoreCase("conditions_of_aggravation_and_amelioration"))
            img.setBackgroundResource(R.drawable._conditions_of_aggravation_and_amelioration);
        else if (cap.equalsIgnoreCase("conditionsa"))
            img.setBackgroundResource(R.drawable._conditionsa);
        else if (cap.equalsIgnoreCase("coryza"))
            img.setBackgroundResource(R.drawable._coryza);
        else if (cap.equalsIgnoreCase("cough"))
            img.setBackgroundResource(R.drawable._cough);
        else if (cap.equalsIgnoreCase("cravings_and_desires"))
            img.setBackgroundResource(R.drawable._cravings_and_desires);
        else if (cap.equalsIgnoreCase("dorsal_region"))
            img.setBackgroundResource(R.drawable._dorsal_region);
        else if (cap.equalsIgnoreCase("dreams"))
            img.setBackgroundResource(R.drawable._dreams);
        else if (cap.equalsIgnoreCase("ear"))
            img.setBackgroundResource(R.drawable._ear);
        else if (cap.equalsIgnoreCase("ears"))
            img.setBackgroundResource(R.drawable._ears);
        else if (cap.equalsIgnoreCase("epigastrium"))
            img.setBackgroundResource(R.drawable._epigastrium);
        else if (cap.equalsIgnoreCase("eructation"))
            img.setBackgroundResource(R.drawable._eructation);
        else if (cap.equalsIgnoreCase("eructations"))
            img.setBackgroundResource(R.drawable._eructations);
        else if (cap.equalsIgnoreCase("expectoration"))
            img.setBackgroundResource(R.drawable._expectoration);
        else if (cap.equalsIgnoreCase("external_abdomen"))
            img.setBackgroundResource(R.drawable._external_abdomen);
        else if (cap.equalsIgnoreCase("external_chest"))
            img.setBackgroundResource(R.drawable._external_chest);
        else if (cap.equalsIgnoreCase("external_head_bones_and_scalp"))
            img.setBackgroundResource(R.drawable._external_head_bones_and_scalp);
        else if (cap.equalsIgnoreCase("external_throat"))
            img.setBackgroundResource(R.drawable._external_throat);
        else if (cap.equalsIgnoreCase("extremities"))
            img.setBackgroundResource(R.drawable._extremities);
        else if (cap.equalsIgnoreCase("eye"))
            img.setBackgroundResource(R.drawable._eye);
        else if (cap.equalsIgnoreCase("eyes"))
            img.setBackgroundResource(R.drawable._eyes);
        else if (cap.equalsIgnoreCase("face"))
            img.setBackgroundResource(R.drawable._face);
        else if (cap.equalsIgnoreCase("female"))
            img.setBackgroundResource(R.drawable._female);
        else if (cap.equalsIgnoreCase("female_organs"))
            img.setBackgroundResource(R.drawable._female_organs);
        else if (cap.equalsIgnoreCase("fever"))
            img.setBackgroundResource(R.drawable._fever);
        else if (cap.equalsIgnoreCase("fever_chill"))
            img.setBackgroundResource(R.drawable._fever_chill);
        else if (cap.equalsIgnoreCase("flatulence"))
            img.setBackgroundResource(R.drawable._flatulence);
        else if (cap.equalsIgnoreCase("general_analysis"))
            img.setBackgroundResource(R.drawable._general_analysis);
        else if (cap.equalsIgnoreCase("generalities"))
            img.setBackgroundResource(R.drawable._generalities);
        else if (cap.equalsIgnoreCase("genitalia"))
            img.setBackgroundResource(R.drawable._genitalia);
        else if (cap.equalsIgnoreCase("genitalia_female"))
            img.setBackgroundResource(R.drawable._genitalia_female);
        else if (cap.equalsIgnoreCase("genitalia_male"))
            img.setBackgroundResource(R.drawable._genitalia_male);
        else if (cap.equalsIgnoreCase("genitals"))
            img.setBackgroundResource(R.drawable._genitals);
        else if (cap.equalsIgnoreCase("glands"))
            img.setBackgroundResource(R.drawable._glands);
        else if (cap.equalsIgnoreCase("groins"))
            img.setBackgroundResource(R.drawable._groins);
        else if (cap.equalsIgnoreCase("gums"))
            img.setBackgroundResource(R.drawable._gums);
        else if (cap.equalsIgnoreCase("hand"))
            img.setBackgroundResource(R.drawable._hand);
        else if (cap.equalsIgnoreCase("head"))
            img.setBackgroundResource(R.drawable._head);
        else if (cap.equalsIgnoreCase("head_external"))
            img.setBackgroundResource(R.drawable._head_external);
        else if (cap.equalsIgnoreCase("head_internal"))
            img.setBackgroundResource(R.drawable._head_internal);
        else if (cap.equalsIgnoreCase("hearing"))
            img.setBackgroundResource(R.drawable._hearing);
        else if (cap.equalsIgnoreCase("heart"))
            img.setBackgroundResource(R.drawable._heart);
        else if (cap.equalsIgnoreCase("heartbun"))
            img.setBackgroundResource(R.drawable._heartbun);
        else if (cap.equalsIgnoreCase("heartburn"))
            img.setBackgroundResource(R.drawable._heartburn);
        else if (cap.equalsIgnoreCase("heat"))
            img.setBackgroundResource(R.drawable._heat);
        else if (cap.equalsIgnoreCase("hiccough"))
            img.setBackgroundResource(R.drawable._hiccough);
        else if (cap.equalsIgnoreCase("hypochondria"))
            img.setBackgroundResource(R.drawable._hypochondria);
        else if (cap.equalsIgnoreCase("hypochondriae"))
            img.setBackgroundResource(R.drawable._hypochondriae);
        else if (cap.equalsIgnoreCase("inguinal"))
            img.setBackgroundResource(R.drawable._inguinal);
        else if (cap.equalsIgnoreCase("intellect"))
            img.setBackgroundResource(R.drawable._intellect);
        else if (cap.equalsIgnoreCase("kidneys"))
            img.setBackgroundResource(R.drawable._kidneys);
        else if (cap.equalsIgnoreCase("larynx"))
            img.setBackgroundResource(R.drawable._larynx);
        else if (cap.equalsIgnoreCase("larynx_and_trachea"))
            img.setBackgroundResource(R.drawable._larynx_and_trachea);
        else if (cap.equalsIgnoreCase("liver"))
            img.setBackgroundResource(R.drawable._liver);
        else if (cap.equalsIgnoreCase("lower_extremities"))
            img.setBackgroundResource(R.drawable._lower_extremities);
        else if (cap.equalsIgnoreCase("lower_limbs"))
            img.setBackgroundResource(R.drawable._lower_limbs);
        else if (cap.equalsIgnoreCase("lumbar_region"))
            img.setBackgroundResource(R.drawable._lumbar_region);
        else if (cap.equalsIgnoreCase("lungs"))
            img.setBackgroundResource(R.drawable._lungs);
        else if (cap.equalsIgnoreCase("male"))
            img.setBackgroundResource(R.drawable._male);
        else if (cap.equalsIgnoreCase("male_organs"))
            img.setBackgroundResource(R.drawable._male_organs);
        else if (cap.equalsIgnoreCase("menstruation"))
            img.setBackgroundResource(R.drawable._menstruation);
        else if (cap.equalsIgnoreCase("micturition"))
            img.setBackgroundResource(R.drawable._micturition);
        else if (cap.equalsIgnoreCase("mind"))
            img.setBackgroundResource(R.drawable._mind);
        else if (cap.equalsIgnoreCase("mouth"))
            img.setBackgroundResource(R.drawable._mouth);
        else if (cap.equalsIgnoreCase("mouth_and_throat"))
            img.setBackgroundResource(R.drawable._mouth_and_throat);
        else if (cap.equalsIgnoreCase("nausea"))
            img.setBackgroundResource(R.drawable._nausea);
        else if (cap.equalsIgnoreCase("neck"))
            img.setBackgroundResource(R.drawable._neck);
        else if (cap.equalsIgnoreCase("neck_nape"))
            img.setBackgroundResource(R.drawable._neck_nape);
        else if (cap.equalsIgnoreCase("nipples"))
            img.setBackgroundResource(R.drawable._nipples);
        else if (cap.equalsIgnoreCase("nose"))
            img.setBackgroundResource(R.drawable._nose);
        else if (cap.equalsIgnoreCase("nose_and_accessory_cavities"))
            img.setBackgroundResource(R.drawable._nose_and_accessory_cavities);
        else if (cap.equalsIgnoreCase("palate"))
            img.setBackgroundResource(R.drawable._palate);
        else if (cap.equalsIgnoreCase("perineum"))
            img.setBackgroundResource(R.drawable._perineum);
        else if (cap.equalsIgnoreCase("perspiration"))
            img.setBackgroundResource(R.drawable._perspiration);
        else if (cap.equalsIgnoreCase("prostate"))
            img.setBackgroundResource(R.drawable._prostate);
        else if (cap.equalsIgnoreCase("prostate_gland"))
            img.setBackgroundResource(R.drawable._prostate_gland);
        else if (cap.equalsIgnoreCase("qualmishness"))
            img.setBackgroundResource(R.drawable._qualmishness);
        else if (cap.equalsIgnoreCase("rectum"))
            img.setBackgroundResource(R.drawable._rectum);
        else if (cap.equalsIgnoreCase("regurgitation"))
            img.setBackgroundResource(R.drawable._regurgitation);
        else if (cap.equalsIgnoreCase("respiration"))
            img.setBackgroundResource(R.drawable._respiration);
        else if (cap.equalsIgnoreCase("retching_and_gagging"))
            img.setBackgroundResource(R.drawable._retching_and_gagging);
        else if (cap.equalsIgnoreCase("retching_and_gaggling"))
            img.setBackgroundResource(R.drawable._retching_and_gaggling);
        else if (cap.equalsIgnoreCase("sacrum"))
            img.setBackgroundResource(R.drawable._sacrum);
        else if (cap.equalsIgnoreCase("saliva"))
            img.setBackgroundResource(R.drawable._saliva);
        else if (cap.equalsIgnoreCase("scapular_region"))
            img.setBackgroundResource(R.drawable._scapular_region);
        else if (cap.equalsIgnoreCase("sediment"))
            img.setBackgroundResource(R.drawable._sediment);
        else if (cap.equalsIgnoreCase("sensationsa"))
            img.setBackgroundResource(R.drawable._sensationsa);
        else if (cap.equalsIgnoreCase("sensationsb"))
            img.setBackgroundResource(R.drawable._sensationsb);
        else if (cap.equalsIgnoreCase("sensationsc"))
            img.setBackgroundResource(R.drawable._sensationsc);
        else if (cap.equalsIgnoreCase("sensationsd"))
            img.setBackgroundResource(R.drawable._sensationsd);
        else if (cap.equalsIgnoreCase("sensationse"))
            img.setBackgroundResource(R.drawable._sensationse);
        else if (cap.equalsIgnoreCase("sensationsf"))
            img.setBackgroundResource(R.drawable._sensationsf);
        else if (cap.equalsIgnoreCase("sensationsg"))
            img.setBackgroundResource(R.drawable._sensationsg);
        else if (cap.equalsIgnoreCase("sensationsh"))
            img.setBackgroundResource(R.drawable._sensationsh);
        else if (cap.equalsIgnoreCase("sensationsi"))
            img.setBackgroundResource(R.drawable._sensationsi);
        else if (cap.equalsIgnoreCase("sensationsj"))
            img.setBackgroundResource(R.drawable._sensationsj);
        else if (cap.equalsIgnoreCase("sensationsk"))
            img.setBackgroundResource(R.drawable._sensationsk);
        else if (cap.equalsIgnoreCase("sensationsl"))
            img.setBackgroundResource(R.drawable._sensationsl);
        else if (cap.equalsIgnoreCase("sensationsm"))
            img.setBackgroundResource(R.drawable._sensationsm);
        else if (cap.equalsIgnoreCase("sensationsn"))
            img.setBackgroundResource(R.drawable._sensationsn);
        else if (cap.equalsIgnoreCase("sensationso"))
            img.setBackgroundResource(R.drawable._sensationso);
        else if (cap.equalsIgnoreCase("sensationsp"))
            img.setBackgroundResource(R.drawable._sensationsp);
        else if (cap.equalsIgnoreCase("sensationsq"))
            img.setBackgroundResource(R.drawable._sensationsq);
        else if (cap.equalsIgnoreCase("sensationsr"))
            img.setBackgroundResource(R.drawable._sensationsr);
        else if (cap.equalsIgnoreCase("sensationss"))
            img.setBackgroundResource(R.drawable._sensationss);
        else if (cap.equalsIgnoreCase("sensationst"))
            img.setBackgroundResource(R.drawable._sensationst);
        else if (cap.equalsIgnoreCase("sensationsu"))
            img.setBackgroundResource(R.drawable._sensationsu);
        else if (cap.equalsIgnoreCase("sensationsv"))
            img.setBackgroundResource(R.drawable._sensationsv);
        else if (cap.equalsIgnoreCase("sensationsw"))
            img.setBackgroundResource(R.drawable._sensationsw);
        else if (cap.equalsIgnoreCase("sensorium"))
            img.setBackgroundResource(R.drawable._sensorium);
        else if (cap.equalsIgnoreCase("sexual"))
            img.setBackgroundResource(R.drawable._sexual);
        else if (cap.equalsIgnoreCase("sexual_impulse"))
            img.setBackgroundResource(R.drawable._sexual_impulse);
        else if (cap.equalsIgnoreCase("skin"))
            img.setBackgroundResource(R.drawable._skin);
        else if (cap.equalsIgnoreCase("sleep"))
            img.setBackgroundResource(R.drawable._sleep);
        else if (cap.equalsIgnoreCase("sperm"))
            img.setBackgroundResource(R.drawable._sperm);
        else if (cap.equalsIgnoreCase("sperms"))
            img.setBackgroundResource(R.drawable._sperms);
        else if (cap.equalsIgnoreCase("spleen"))
            img.setBackgroundResource(R.drawable._spleen);
        else if (cap.equalsIgnoreCase("stomach"))
            img.setBackgroundResource(R.drawable._stomach);
        else if (cap.equalsIgnoreCase("stomach_and_abdomen"))
            img.setBackgroundResource(R.drawable._stomach_and_abdomen);
        else if (cap.equalsIgnoreCase("stool"))
            img.setBackgroundResource(R.drawable._stool);
        else if (cap.equalsIgnoreCase("sweat"))
            img.setBackgroundResource(R.drawable._sweat);
        else if (cap.equalsIgnoreCase("taste"))
            img.setBackgroundResource(R.drawable._taste);
        else if (cap.equalsIgnoreCase("teeth"))
            img.setBackgroundResource(R.drawable._teeth);
        else if (cap.equalsIgnoreCase("thirst"))
            img.setBackgroundResource(R.drawable._thirst);
        else if (cap.equalsIgnoreCase("throat"))
            img.setBackgroundResource(R.drawable._throat);
        else if (cap.equalsIgnoreCase("time"))
            img.setBackgroundResource(R.drawable._time);
        else if (cap.equalsIgnoreCase("tongue"))
            img.setBackgroundResource(R.drawable._tongue);
        else if (cap.equalsIgnoreCase("upper_extremities"))
            img.setBackgroundResource(R.drawable._upper_extremities);
        else if (cap.equalsIgnoreCase("upper_limbs"))
            img.setBackgroundResource(R.drawable._upper_limbs);
        else if (cap.equalsIgnoreCase("urethra"))
            img.setBackgroundResource(R.drawable._urethra);
        else if (cap.equalsIgnoreCase("urinary"))
            img.setBackgroundResource(R.drawable._urinary);
        else if (cap.equalsIgnoreCase("urinary_organs"))
            img.setBackgroundResource(R.drawable._urinary_organs);
        else if (cap.equalsIgnoreCase("urine"))
            img.setBackgroundResource(R.drawable._urine);
        else if (cap.equalsIgnoreCase("vertigo"))
            img.setBackgroundResource(R.drawable._vertigo);
        else if (cap.equalsIgnoreCase("vision"))
            img.setBackgroundResource(R.drawable._vision);
        else if (cap.equalsIgnoreCase("voice"))
            img.setBackgroundResource(R.drawable._voice);
        else if (cap.equalsIgnoreCase("voice_and_speech"))
            img.setBackgroundResource(R.drawable._voice_and_speech);
        else if (cap.equalsIgnoreCase("vomiting"))
            img.setBackgroundResource(R.drawable._vomiting);
        else if (cap.equalsIgnoreCase("waist"))
            img.setBackgroundResource(R.drawable._waist);
        else if (cap.equalsIgnoreCase("waterbrash"))
            img.setBackgroundResource(R.drawable._waterbrash);

        else
            img.setBackgroundResource(R.drawable.ic_launcher);
    }
    public String getRemediesKent(String r)
    {
        try
        {
            if (r == "" || r == null) return "-";
            String[] remS = r.split(":");
            Log.e("remS",String.valueOf(remS.length));
            String remedies = " ";
            //window.MyCls.log("remedies 1 remS.length" + remS.length) ;
            String str = "";
            for(int i=0;i<= remS.length-1;i++)
            {
                String[] spi = remS[i].split(",");
                if (spi[1].equals("1"))
                {
                    str = "<font color='black'>" + spi[0] + "</font>, ";
                }
                else if (spi[1].equals("2"))
                {
                    str = "<font color='blue'>" + spi[0] + "</font>, ";
                }
                else if (spi[1].equals("3"))
                {
                    str = "<font color='red'>" + spi[0] + "</font>, ";
                }
                remedies = remedies + str;

            }
            return remedies;
        }
        catch(Exception ex)
        {
            return "-";
        }
        //window.MyCls.log(remedies);

    }
    public String getRemediesBoenninghausens(String r)
    {
        Log.e("getRemediesBoenninghausens", "I am in");
        try
        {
            if (r == "" || r == null) return "-";
            String[] remS = r.split(":");
            Log.e("remS",String.valueOf(remS.length));
            String remedies = " ";
            //window.MyCls.log("remedies 1 remS.length" + remS.length) ;
            String str = "";
            for(int i=0;i<= remS.length-1;i++)
            {
                String[] spi = remS[i].split(",");
                if (spi[1].equals("1"))
                {
                    str = "<font color='black'>" + spi[0] + "</font>, ";
                }
                else if (spi[1].equals("2"))
                {
                    str = "<font color='#009933'>" + spi[0] + "</font>, ";
                }
                else if (spi[1].equals("3"))
                {
                    str = "<font color='blue'><i>" + spi[0] + "</i></font>, ";
                }
                else if (spi[1].equals("4"))
                {
                    str = "<font color='red'><b>" + spi[0] + "</b></font>, ";
                }
                else if (spi[1].equals("5"))
                {
                    str = "<font color='#000080'><u><b>" + spi[0] + "</b></u></font>, ";
                }
                remedies = remedies + str;

            }
            return remedies;
        }
        catch(Exception ex)
        {
            return "-";
        }
        //window.MyCls.log(remedies);

    }




}



