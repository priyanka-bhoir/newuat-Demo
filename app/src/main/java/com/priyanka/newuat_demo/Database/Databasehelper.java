package com.priyanka.newuat_demo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.priyanka.newuat_demo.Models.Dome;
import com.priyanka.newuat_demo.Models.GetEntry;
import com.priyanka.newuat_demo.Models.MobileLayout;
import com.priyanka.newuat_demo.Models.TeamData;
import com.priyanka.newuat_demo.Models.User;
import com.priyanka.newuat_demo.Models.module_pojo;

import java.net.PortUnreachableException;
import java.util.ArrayList;

public class Databasehelper extends SQLiteOpenHelper {

    public static final String Database_name="newuat";
    public static final int version = 11;
    public static final String Table_LOGIN="login";
    public static final String Table_MODULE_LIST="modules";
    public static final String Table_MOBILE_LAYOUT="mobile";
    public static final String Table_GETENTRY_LIST="getentry";
    public static final String LOGIN_JSON="logindata";
    public static final String Table_TEAM="team";
    public static final String Table_mobileLayout_Dome="dom_list";
    public static final String Table_User="user";

    //for data members of table module
    public static final String ID="id";
    public static final String MODULENAME="module_name";
    public static final String MODULESINGLUAR="module_singular";
    public static final String MODULEPLURAL="module_plural";

    //for data members table mobile layout
    public static final String MODULE_NAME="module_name";
    public static final String MODULE_LABEL="module_label";
    public static final String LAYOUT_DEFS="layoutdefs";
    public static final String FIELDDEFS="fielddefs";

    //for data members of table getEntrylist
    public static final String ENTRY_DATA="data";
    public static final String ENTRY_LINK="link";
    public static final String ENTRY_META="meta";

    // for team table
    public static final String ID_TEAM="team_id";
    public static final String CREATED_AT="created_at";
    public static final String UPDATED_AT="updated_at";
    public static final String DELETED_AT="deleted_at";
    public static final String NAME="name";
    public static final String DESCRIPTION="description";

    //for Storing dome value of mobile layout for spinner
    public static final String DOM_NAME="dom_name";
    public static final String DOM_VALUE="dom_values";

    //  for Storing User Data
    public static final String ID_user="user_id";
    public static final String ASSIGNED_USER_ID="assigned_user_id";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String EMAIL="email";
    public static final String USER_TYPE="user_type";
    public static final String DESIGNATION="designation";
    public static final String DEPARMENT="department";
    public static final String PHONE="phone";
    public static final String ADDRESS="address";

    module_pojo data;
    MobileLayout mobileLayout;
    String TAG="Dabasehelper.class";


    public Databasehelper(@Nullable Context context) {
        super(context, Database_name, null, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //Login table
        db.execSQL("create table "+Table_LOGIN+"("+LOGIN_JSON+")");

        //for Module-list
        db.execSQL("create table "+ Table_MODULE_LIST+"("+ID+" integer primary key,"+MODULENAME+" text,"+MODULESINGLUAR+" text,"+MODULEPLURAL+" text)");

        //for mobile-layout
        db.execSQL("create table "+ Table_MOBILE_LAYOUT+"("+ID+" integer primary key, "+MODULE_NAME+" text, "+MODULE_LABEL+" text,"+LAYOUT_DEFS+" text,"+FIELDDEFS+" text)");

        //for getEntry
        db.execSQL("create table "+Table_GETENTRY_LIST+"("+ID+" integer primary key, "+MODULENAME+" text, "+ENTRY_DATA+" text,"+ENTRY_LINK+" text,"+ENTRY_META+" text)");

        // this is for Team
        db.execSQL("create table "+Table_TEAM+"("+ID+" integer primary key, "+ID_TEAM+" text,"+CREATED_AT+" text,"+UPDATED_AT+" text,"+DELETED_AT+" text,"+NAME+" text,"+DESCRIPTION+" text)");

        //this is for Dom value of mobile layout
        db.execSQL("create table "+Table_mobileLayout_Dome+"("+ID+" integer primary key, "+DOM_NAME+" text,"+DOM_VALUE+" text)");

        // this is user table
        db.execSQL("create table "+Table_User+"("+ID+" integer primary key, "+ID_user+" text, "+CREATED_AT+" text, "+UPDATED_AT+" text,"+DELETED_AT+" text,"+ASSIGNED_USER_ID+" text, "+NAME+" text, "+FIRST_NAME+" text, "+LAST_NAME+" text,"+ EMAIL+" text, "+USER_TYPE+" text, "+DESIGNATION+" text,"+DEPARMENT+" text, "+PHONE+" text,"+ADDRESS+" text )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS "+Table_MODULE_LIST);
        db.execSQL("DROP TABLE IF EXISTS "+Table_MOBILE_LAYOUT);
        db.execSQL("DROP TABLE IF EXISTS "+Table_GETENTRY_LIST);
        db.execSQL("DROP TABLE IF EXISTS "+Table_TEAM);
        db.execSQL("DROP TABLE IF EXISTS "+Table_mobileLayout_Dome);
        db.execSQL("DROP TABLE IF EXISTS " + Table_User);
        onCreate(db);
    }

    public void insertLogin(String data){

        // for inserting login api data
        ContentValues values=new ContentValues();
        values.put(LOGIN_JSON,data);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(Table_LOGIN,null,values);
        Log.e(TAG, "insertLogin: "+"Data Inserted" );
    }

    public void insertModules(module_pojo data){

        //for inserting modules list api data
        ContentValues values=new ContentValues();
        Log.e(TAG, "insertModules:Name "+data.getName() );
        values.put(MODULENAME,data.getName());
        values.put(MODULESINGLUAR,data.getSingular());
        values.put(MODULEPLURAL,data.getPlural());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_MODULE_LIST,null,values);
        Log.e(TAG, "insertModules: Data inserted" );
    }

    public void insertMobileLayout(MobileLayout mobileLayout1){

        //for inserting mobilelayout api data
        ContentValues values=new ContentValues();
        values.put(MODULE_NAME,mobileLayout1.getModulename());
        values.put(MODULE_LABEL,mobileLayout1.getModulelabel());
        values.put(LAYOUT_DEFS,mobileLayout1.getLayoutdefs());
        values.put(FIELDDEFS,mobileLayout1.getFields());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_MOBILE_LAYOUT,null,values);
        Log.e(TAG, "insertMobileLayout: Data inserted" );
    }

    public void insertTeamMember(TeamData teamData){
        ContentValues values=new ContentValues();
        values.put(ID_TEAM,teamData
                .getId());
        values.put(CREATED_AT,teamData.getCreated_at());
        values.put(UPDATED_AT,teamData.getUpdated_at());
        values.put(DELETED_AT,teamData.getDeleted_at());
        values.put(NAME,teamData.getName());
        values.put(DESCRIPTION,teamData.getDescription());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_TEAM,null,values);
        Log.e(TAG, "insertTeamMember: Data inserted" );
    }

    public void insertGetEntryList(GetEntry getEntry){
        ContentValues values=new ContentValues();
        values.put(ENTRY_DATA,getEntry.getData());
        values.put(ENTRY_LINK,getEntry.getLinks());
        values.put(ENTRY_META,getEntry.getMeta());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_GETENTRY_LIST,null,values);
        Log.e(TAG, "insertGetEntryList: Data inserted" );

    }

    public void inserUserData(User user){
        ContentValues values=new ContentValues();
        values.put(ID_user,user.getId());
        values.put(NAME,user.getName());
        values.put(CREATED_AT,user.getCreated_at());
        values.put(UPDATED_AT,user.getUpdated_at());
        values.put(DELETED_AT,user.getDeleted_at());
        values.put(ASSIGNED_USER_ID,user.getAssigned_user_id());
        values.put(FIRST_NAME,user.getFirst_name());
        values.put(LAST_NAME,user.getLast_name());
        values.put(USER_TYPE,user.getUser_type());
        values.put(DESIGNATION,user.getDesignation());
        values.put(DEPARMENT,user.getDepartment());
        values.put(PHONE,user.getPhone());
        values.put(ADDRESS,user.getAddress());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_User,null,values);
        Log.e(TAG, "inserUserData: Data Inserted");
    }

    public void insertDomData(Dome dome){
        ContentValues values=new ContentValues();
        values.put(DOM_NAME,dome.getDomName());
        values.put(DOM_VALUE,dome.getDomValue());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(Table_mobileLayout_Dome,null,values);
        Log.e(TAG, "insertDomData: Data inserted" );
    }
    public boolean getValidation(String Table_name){

        //validation function
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * from "+Table_name,null);
        Log.e(TAG, "(getValidation):cursor.getCount() "+cursor.getCount() );
        if(cursor.getCount()>0){
            Log.e(TAG, "getValidation: returns true");

            return true;
        }
        Log.e(TAG, "getValidation: returns false");

        return false;
    }

    public boolean getLogin() {
       return getValidation(Table_LOGIN);
    }

//    public boolean setLogin(){
//    }

    public boolean getModule() { return getValidation(Table_MODULE_LIST); }

    public  boolean getMobileList() {
      return getValidation(Table_MOBILE_LAYOUT);
    }

    public boolean getEntryList(String mParam1){
        SQLiteDatabase db=this.getReadableDatabase();
        String selection=MODULENAME+"=?";
        Cursor cursor=db.query(Table_GETENTRY_LIST,new String[]{MODULENAME},selection,new String[]{mParam1},null,null,null);
        if (cursor.getCount()>0){
            Log.e(TAG, "getEntryList:  returns true");
            return true;

        }
        Log.e(TAG, "getEntryList:  returns false");
        return false;
    }

    public ArrayList<module_pojo> getModuleData() {

        //fetching data from module list tabl
        String query="select * FROM "+Table_MODULE_LIST;
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<module_pojo> storeData= new ArrayList<>();

        Cursor cursor=db.rawQuery(query,null);
        Log.e(TAG, "getModuleData:cursor-->> "+cursor.getCount() );

        if (cursor.moveToFirst()){
            do{
                String id=cursor.getString(cursor.getColumnIndex(ID));
                String name=cursor.getString(cursor.getColumnIndex(MODULENAME));
                String singular=cursor.getString(cursor.getColumnIndex(MODULESINGLUAR));
                String plural=cursor.getString(cursor.getColumnIndex(MODULEPLURAL));
                Log.e(TAG, "getModuleListData:id "+id+"name:"+name );
                storeData.add(new module_pojo(name,singular,plural));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;
    }

    public ArrayList<MobileLayout> getMobileListData() {
        String query="select * FROM "+Table_MOBILE_LAYOUT;
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<MobileLayout> storeData= new ArrayList<>();

        Cursor cursor=db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do{
                String id=cursor.getString(cursor.getColumnIndex(ID));
                String name=cursor.getString(cursor.getColumnIndex(MODULE_NAME));
                String label=cursor.getString(cursor.getColumnIndex(MODULE_LABEL));
                String defs=cursor.getString(cursor.getColumnIndex(LAYOUT_DEFS));
                String fields=cursor.getString(cursor.getColumnIndex(FIELDDEFS));
                Log.e(TAG, "getMobileListData: "+name );
                storeData.add(new MobileLayout(name,label,defs,fields));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;
    }

    public ArrayList<GetEntry> getEntryListData() {
        String query="select * from "+ Table_GETENTRY_LIST;
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<GetEntry> storeData= new ArrayList<>();

        Cursor cursor=db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            while (cursor.moveToNext()){
                String id=cursor.getString(0);
                String data=cursor.getString(1);
                String link=cursor.getString(2);
                String meta=cursor.getString(3);
                storeData.add(new GetEntry(data,link,meta));
            }
        }
        cursor.close();
        return storeData;
    }

    public String getBackendname(String mParam1) {
        String string = null;
        SQLiteDatabase db=getReadableDatabase();
        String selection=MODULEPLURAL+"=?";
        Cursor cursor=db.query(Table_MODULE_LIST,new String[]{MODULENAME},selection,new String[]{mParam1},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            string=cursor.getString(0);
            Log.e(TAG,"getBackendname:"+string);
        }
        return string;
    }

    public String getFrontEndname(String string){
        String data=null;
        SQLiteDatabase db=getReadableDatabase();
        String selection=MODULENAME+"=?";
        Cursor cursor=db.query(Table_MODULE_LIST,new String[]{MODULEPLURAL},selection,new String[]{string},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            data=cursor.getString(0);
//            Log.e(TAG, "getFrontEndname: "+data );
        }
        return data;
    }

    public String getdisplaylabel(String name){
        //these will fetch Display label from mobile layout
        String display_label="";
        SQLiteDatabase db=getReadableDatabase();
        String selection=MODULE_LABEL+"=?";
        Cursor cursor=db.query(Table_MOBILE_LAYOUT,new String[]{FIELDDEFS},selection,new String[]{name},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            display_label=cursor.getString(0);
//            Log.e(TAG, "getd/isplaylabel: "+display_label );
        }
        return display_label;
    }

    public ArrayList<TeamData> fetchAllMemberNames(){
       ArrayList<TeamData> a = new ArrayList<>();
       SQLiteDatabase db=getReadableDatabase();
       String query="select * FROM "+ Table_User;
       Cursor cursor=db.rawQuery(query,null);
//        Log.e(TAG, "fetchAllMemberNames: "+cursor.getCount());
        if (cursor.moveToFirst()){
            do{
                String id=cursor.getString(cursor.getColumnIndex(ID_user));
                String name=cursor.getString(cursor.getColumnIndex(NAME));
                a.add(new TeamData(id,name));
            }while (cursor.moveToNext());
        }
       return a;
    }

    public String fetchUserData(String value) {
        String abc="";

//        String query="select * FROM "+ Table_TEAM;
        SQLiteDatabase db=getReadableDatabase();
//        Log.e(TAG, "fetchTeamName: "+value);
//        Cursor cursor=db.rawQuery(query,null);
//        Log.e(TAG, "fetchTeamName: cursor.getCount()==>"+cursor.getCount() );
        String selection = NAME+ "=?" ;
        Cursor cursor=db.query(Table_TEAM,new String[]{ID_TEAM},selection,new String[]{value},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            abc=cursor.getString(cursor.getColumnIndex(NAME));
//            Log.e(TAG, "getlayoutdefs:cursor "+cursor.getString(cursor.getColumnIndex(NAME)));
        }
//        Log.e(TAG, "fetchTeamName: "+abc );
        return abc;
    }
    public String getdefs(String module,String field){
        String abc="";
//        Log.e(TAG, "getdefs: "+module+":"+field);
        SQLiteDatabase db=getReadableDatabase();
        String selection=MODULE_LABEL+"=?";
        Cursor cursor=db.query(Table_MOBILE_LAYOUT, new String[]{field}, selection, new String[]{module},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            abc=cursor.getString(0);
//            Log.e(TAG, "getlayoutdefs:cursor "+cursor.getString(0));
        }
        return abc;
    }
    public String getlayoutdefs(String modulename) {
        //these will fetch layoutdefs from mobile layout
       return getdefs(modulename,LAYOUT_DEFS);
    }

    public String getFielddefs(String modulename){
        //these will fetch fielddefs from mobile layout
        return getdefs(modulename,FIELDDEFS);
    }
    public String fetchDomeValue(String name){
        String abc="";
        SQLiteDatabase db=getReadableDatabase();
        String selection=DOM_NAME+"=?";
        Cursor cursor=db.query(Table_mobileLayout_Dome,new String[]{DOM_VALUE},selection,new String[]{name},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
            abc=cursor.getString(0);
//            Log.e(TAG, "fetchDomeValue:cursor "+cursor.getString(0));
        }
        return abc;
    }
//    public String fetchDomeName(String name){
//        Log.e(TAG, "fetchDomeName: "+name );
//        String abc="";
//        SQLiteDatabase db=getReadableDatabase();
//        String selection=DOM_VALUE+"=?";
//        Cursor cursor=db.query(Table_mobileLayout_Dome,new String[]{DOM_NAME},selection,new String[]{name},null,null,null);
//        Log.e(TAG, "fetchDomeName:cursor==> "+cursor.getCount());
//        if (cursor!=null){
//            cursor.moveToFirst();
//            abc=cursor.getString(cursor.getColumnIndex(DOM_NAME));
//        }
//        return abc;
//    }
}
