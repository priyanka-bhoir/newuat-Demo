package com.priyanka.newuat_demo.Adapter;

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

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.color.darker_gray;
import static com.priyanka.newuat_demo.R.color.design_default_color_background;

public class Detailsadapter extends ArrayAdapter<ArrayList<String>>{

    private static final String TAG = "Detailsadapter";
    Context context;
    ArrayList<String> keyset;
    ArrayList<String> values;
    String module;
    Databasehelper db;
    Details_frag.NameValue nameValue;


    public Detailsadapter(String module,@NonNull Context context, int resource, ArrayList<String> keySet, ArrayList<String> listOfValues) {
        super(context, resource);
        this.context=context;
//        this.hashMapArrayList=map;
        this.keyset=keySet;
        this.values=listOfValues;
        this.module=module;
//        Log.e(TAG, "Detailsadapter:you are in these module====> "+module );
        Log.e(TAG, "Detailsadapter: "+"Details_Adapter created" );
    }

    @Override
    public int getCount() {
        Log.e(TAG, "getCount:keysetsize==> "+keyset.size() +"||valueset||"+values.size());
        return keyset.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e(TAG, "getView: ");
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        convertView=layoutInflater.inflate(R.layout.details_list_holder,parent,false);
        ItemViewHlder viewHlder=new ItemViewHlder(convertView);
        db=new Databasehelper(context);
//        String map=hashMapArrayList.get("name");
//        Set<String> keySet = hashMapArrayList.keySet();
//        Log.e(TAG, "getView: map===> "+hashMapArrayList);

        //Sending value to display in card
        if (keyset.get(position).equals("name")){
            //nameValue.getname(values.get(position));
        }


        Log.e(TAG, "getView:position==> "+position );
        String key= null;
        try {
            key = getDisplayNames(keyset.get(position));
            String s=key;
            Log.e(TAG, "getView: this is the kay "+key );
            if (key==null){
                Log.e(TAG, "getView: you got a null value for key " );
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
            }else if (key.equals("Description")) {
                viewHlder.key.setText(key);
                viewHlder.value.setText(values.get(position));
                makeTextViewResizable(viewHlder.value, 3, "See More", true);
            }
            else if (key!=null){
                viewHlder.key.setText(key);
                viewHlder.value.setText(values.get(position));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
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
                Log.e(TAG, "getDisplayNames:key "+key );
                Log.e(TAG, "getDisplayNames:object===> " + object.get("display_label"));

                displayname=object.optString("display_label");
            }
        }
        return displayname;
    }

    // this function is for read more feature
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {
        Log.e(TAG, "makeTextViewResizable:  i am called" );

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
