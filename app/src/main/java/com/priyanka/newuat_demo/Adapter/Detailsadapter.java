package com.priyanka.newuat_demo.Adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.priyanka.newuat_demo.Database.Databasehelper;
import com.priyanka.newuat_demo.R;
import com.priyanka.newuat_demo.fragment.Details_frag;
import com.priyanka.newuat_demo.singletone.MySpannable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import static android.R.color.darker_gray;
import static com.priyanka.newuat_demo.R.color.design_default_color_background;

public class Detailsadapter extends ArrayAdapter<ArrayList<String>>{

    private static final String TAG = "Detailsadapter";
    Context context;
    ArrayList<String> keyset;
    ArrayList<String> values;
    String module;
    Databasehelper db;
//    Details_frag.NameValue nameValue;
    String value;
    int maxliens;


    public Detailsadapter(String module,@NonNull Context context, int resource, ArrayList<String> keySet, ArrayList<String> listOfValues) {
        super(context, resource);
        this.context=context;
//        this.hashMapArrayList=map;
        this.keyset=keySet;
        this.values=listOfValues;
        this.module=module;
//        Log.e(TAG, "Detailsadapter: "+"Details_Adapter created" );
    }

    @Override
    public int getCount() {
//        Log.e(TAG, "getCount:keysetsize==> "+keyset.size() +"||valueset||"+values.size());
        return keyset.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        Log.e(TAG, "getView: ");
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        convertView=layoutInflater.inflate(R.layout.details_list_holder,parent,false);
        ItemViewHlder viewHlder=new ItemViewHlder(convertView);
//        db=new Databasehelper(context);
//        String map=hashMapArrayList.get("name");
//        Set<String> keySet = hashMapArrayList.keySet();
//        Log.e(TAG, "getView: map===> "+hashMapArrayList);




//        Log.e(TAG, "getView:position==> "+position );
        //            key = getDisplayNames(keyset.get(position));
        String key=keyset.get(position);
//            Log.e(TAG, "getView: this is the kay "+key );
        if (key==null){
//                Log.e(TAG, "getView: you got a null value for key " );
        }else if (key.equals("Website")){
            viewHlder.key.setText(key);
            viewHlder.value.setText(values.get(position));
            Linkify.addLinks(viewHlder.value,Linkify.WEB_URLS);
            viewHlder.value.setMovementMethod(LinkMovementMethod.getInstance());
        }else if (key.equals("Email")){
            viewHlder.key.setText(key);
            viewHlder.value.setText(values.get(position));
            Linkify.addLinks(viewHlder.value,Linkify.EMAIL_ADDRESSES);
            viewHlder.value.setMovementMethod(LinkMovementMethod.getInstance());
        }else if (key.equals("Phone")){
            viewHlder.key.setText(key);
            viewHlder.value.setText(values.get(position));
            Linkify.addLinks(viewHlder.value,Linkify.PHONE_NUMBERS);
            viewHlder.value.setMovementMethod(LinkMovementMethod.getInstance());
        }else if (key.equals("Address")){
            viewHlder.key.setText(key);
//              Log.e(TAG, "getView: hey its me your address i am called(" );
            Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
            viewHlder.value.setText(values.get(position));
            Linkify.addLinks(viewHlder.value, pattern, "geo:0,0?q=");
            viewHlder.value.setMovementMethod(LinkMovementMethod.getInstance());
        }else if (key.equals("Updated At")||key.equals("Created At")){
            viewHlder.key.setText(key);
            String dateTime=formatDateTime(values.get(position));
            viewHlder.value.setText(dateTime);
        }else if (key.equals("Description")) {
            viewHlder.key.setText(key);
            viewHlder.value.setText(values.get(position));
            value=values.get(position);
            maxliens=viewHlder.value.getMaxLines();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                maxliens=viewHlder.value.getAutoSizeMaxTextSize();
            }
            Log.e(TAG, "getView: this is the original data =>"+values.get(position) );
            Log.e(TAG, "getView: this is the vale of value==>"+value);
            viewHlder.value.post(new Runnable() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void run() {
                    int MAX_LINES = 3;
                    if (viewHlder.value.getLineCount() > MAX_LINES) {
                        int lastCharShown = viewHlder.value.getLayout().getLineVisibleEnd(MAX_LINES - 1);

                        viewHlder.value.setMaxLines(MAX_LINES);
                        String moreString = "Read More";
                        String suffix = "  " + moreString;

                        // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                        String actionDisplayText = values.get(position).substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;
                        SpannableStringBuilder truncatedSpannableString = new SpannableStringBuilder(actionDisplayText);
                        int startIndex = actionDisplayText.indexOf(moreString);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            truncatedSpannableString.setSpan(new ForegroundColorSpan(R.color.purple_200),
                                    startIndex, startIndex + moreString.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        viewHlder.value.setText(truncatedSpannableString);
                    }
                }
            });
            viewHlder.value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: this is value that get displayed on read more =>"+ value);
//                        viewHlder.value.setMaxLines(maxliens);
                    Log.e(TAG, "onClick: this is the size of my text view"+viewHlder.value.getMaxLines());
                    viewHlder.value.setMaxLines(maxliens);
                    viewHlder.value.setText(value);
                }
            });
        }
        else if (key!=null){
            viewHlder.key.setText(key);
            viewHlder.value.setText(values.get(position));
    }

        return convertView;
    }

    private String formatDateTime(String s) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(s);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private class ItemViewHlder extends View{
        TextView key;
        TextView value;

        public ItemViewHlder(@NonNull View itemView) {
            super(context);
            key=itemView.findViewById(R.id.card_key);
            value=itemView.findViewById(R.id.card_value);
        }
    }
    private String getDisplayNames(String key) throws JSONException {
        String fielddefs=db.getFielddefs(module);
        String displayname = null;
//        Log.e(TAG, "getDisplayNames:===> "+fielddefs );
        JSONArray jsonArray=new JSONArray(fielddefs);
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.get("name").equals(key)) {
//                Log.e(TAG, "getDisplayNames:key "+key );
//                Log.e(TAG, "getDisplayNames:object===> " + object.get("display_label"));
                displayname=object.optString("display_label");
            }
        }
        return displayname;
    }

    // this function is for read more feature
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
//        Log.e(TAG, "makeTextViewResizable:  i am called" +tv.getText());

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}
