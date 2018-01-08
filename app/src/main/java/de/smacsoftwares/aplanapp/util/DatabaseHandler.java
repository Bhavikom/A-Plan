package de.smacsoftwares.aplanapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.Model.ProfileModelClass;
import de.smacsoftwares.aplanapp.Model.RibbonFilterDataSet;
import de.smacsoftwares.aplanapp.Model.TextFilterKeyValueDataset;

/**
 * Created by SSoft-13 on 09-08-2016.
 */
// this class in for creating Database and Tables,updaing,getting recordds from table in whole application
public class DatabaseHandler extends SQLiteOpenHelper
{
    private final Context applicationContext;
    private static final int DATABASE_VERSION = 1;

    // 111 USER PROFILES TABLE(MULTIPLE PROFILES WILL BE SAVE WHILE LOGIN)
    private static final String TABLE_PROFILE_MASTER = "table_profile_master";

    private static final String PROFILE_KEY = "profile_key";
    private static final String PROFILE_ID = "profile_id";
    private static final String PROFILE_NAME = "profile_name";
    private static final String PROFILE_COL_DETAIL = "profile_col_detail";
    private static final String PROFILE_USER_ID = "profile_user_id";
    private static final String PROFILE_EXPANDED_ID = "profile_expanded_id";
    private static final String PROFILE_USERID_TEMP = "profile_user_id_temp";
    private static final String PROFILE_LANGUAGE = "language";
    private static final String PROFILE_RESOLUTION = "resolution";
    private static final String PROFILE_ATTRIBUTE_KEY = "profile_attribute_key";
    private static final String PROFILE_ATTRIBUTE_VALUE = "profile_attribute_value";
    private static final String PROFILE_ATTRIBUTE_KEY_LOCAL = "profile_attribute_key_local";
    private static final String PROFILE_ATTRIBUTE_VALUE_LOCAL = "profile_attribute_value_local";

    // 222 GENERAL FILTER MASTER TABLE(ALL GENERAL FILTER DATA WILL BE SAVED HERE )
    private static final String TABLE_GENERAL_FILTER_MASTER = "general_filter_master";

    private static final String GENERAL_FILTER_PID = "general_filter_pid";
    private static final String GENERAL_FILTER_KEY = "general_filter_key";
    private static final String GENERAL_FILTER_VALUE = "general_filter_value";
    private static final String GENERAL_FILTER_LANGUAGE = "general_filter_language";
    private static final String GENERAL_FILTER_TYPE = "general_filter_type";
    private static final String GENERAL_FILTER_UID = "general_filter_uid";
    private static final String GENERAL_FILTER_VALUE_LOCAL = "general_filter_value_local";

    // 333 RIBBON FILTER MASTER TABLE(ALL RIBBON FILTER DATA WILL BE SAVED HERE )
    private static final String TABLE_RIBBON_FILTER_MASTER = "ribbon_filter_master";

    private static final String RIBBON_FILTER_PID = "ribbon_filter_pid";
    private static final String RIBBON_FILTER_UID = "ribbon_filter_uid";
    private static final String RIBBON_FILTER_LANGUAGE = "ribbon_filter_language";
    private static final String RIBBON_FILTER_KEY = "ribbon_filter_key";
    private static final String RIBBON_FILTER_VALUE = "ribbon_filter_value";
    private static final String RIBBON_FILTER_VALUE_LOCAL = "ribbon_filter_value_local";
    private static final String RIBBON_RESOLUTION = "resolution";

    /// 444 table for date filter data
    private static final String TABLE_DATE_FILTER = "table_date_filter";

    private static final String DATE_KEY = "date_key";
    private static final String DATE_VALUE = "date_value";
    private static final String DATE_USERID = "date_user_id";
    private static final String DATE_PROFILEID = "date_profile_id";
    private static final String DATE_FILTER_TYPE = "date_filter_type";

    // constructor for class
    public DatabaseHandler(Context context) {
        super(context, GlobalClass.DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE " + TABLE_PROFILE_MASTER + " (" + PROFILE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + PROFILE_ID + " TEXT , "
                    + PROFILE_NAME + " TEXT , "
                    + PROFILE_COL_DETAIL + " TEXT , "
                    + PROFILE_USER_ID + " TEXT , "
                    + PROFILE_EXPANDED_ID + " TEXT , "
                    + PROFILE_USERID_TEMP + " TEXT , "
                    + PROFILE_LANGUAGE + " TEXT , "
                    + PROFILE_RESOLUTION + " TEXT , "
                    + PROFILE_ATTRIBUTE_KEY + " TEXT , "
                    + PROFILE_ATTRIBUTE_VALUE + " TEXT , "
                    + PROFILE_ATTRIBUTE_KEY_LOCAL + " TEXT , "
                    + PROFILE_ATTRIBUTE_VALUE_LOCAL + " TEXT "
                    + ")");

            db.execSQL("CREATE TABLE " + TABLE_GENERAL_FILTER_MASTER + " (" + GENERAL_FILTER_PID + " TEXT , "
                    + GENERAL_FILTER_KEY + " TEXT , "
                    + GENERAL_FILTER_VALUE + " TEXT , "
                    + GENERAL_FILTER_LANGUAGE + " TEXT , "
                    + GENERAL_FILTER_TYPE + " TEXT , "
                    + GENERAL_FILTER_UID + " TEXT , "
                    + GENERAL_FILTER_VALUE_LOCAL + " TEXT "
                    + ")");

            db.execSQL("CREATE TABLE " + TABLE_RIBBON_FILTER_MASTER + " (" + RIBBON_FILTER_PID + " TEXT , "
                    + RIBBON_FILTER_UID + " TEXT , "
                    + RIBBON_FILTER_LANGUAGE + " TEXT , "
                    + RIBBON_FILTER_KEY + " TEXT , "
                    + RIBBON_FILTER_VALUE + " TEXT , "
                    + RIBBON_FILTER_VALUE_LOCAL + " TEXT , "
                    + RIBBON_RESOLUTION + " TEXT "
                    + ")");
            db.execSQL("CREATE TABLE " + TABLE_DATE_FILTER + " (" + DATE_KEY + " TEXT , "
                    + DATE_VALUE + " TEXT , "
                    + DATE_USERID + " TEXT , "
                    + DATE_PROFILEID + " TEXT , "
                    + DATE_FILTER_TYPE + " TEXT "
                    + ")");
        }
        catch (Exception e) {
            LogApp.e(" exception while creating database : ", " in database handler : " + e.toString());
        }
    }
    /////////////////////////////////// 11111 GENERAL FILTER TALBE OPERATION ///////////////////////////////
    /* this method store general filter data while login */
    public long addGeneralFilter(String pId, String key, String value, String lang,String type,String uId,String local)
    {
        long add = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("general_filter_pid", pId);
        values.put("general_filter_key", key);
        values.put("general_filter_value", value);
        values.put("general_filter_language", lang);
        values.put("general_filter_type", type);
        values.put("general_filter_uid", uId);
        values.put("general_filter_value_local", local);
        add = db.insert(TABLE_GENERAL_FILTER_MASTER, null, values);
        LogApp.e("record added in general filter   ", " in database handler : " + add);
        return add;
    }
    public ArrayList<GeneralFilterDataSet> getAllGeneralFilterData(String profile_id, String type, String lang, String uId){
        Cursor cursor = null;
        ArrayList<GeneralFilterDataSet> arraylistFilter = new ArrayList<GeneralFilterDataSet>();
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid ='" +profile_id+
                "' AND general_filter_type = '"+type+"' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
        //String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND profile_id= '"+profileId+"' AND language = '"+language+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    GeneralFilterDataSet userProfile = new GeneralFilterDataSet();
                    userProfile.setProfileId(cursor.getString(cursor.getColumnIndex("general_filter_pid")));
                    userProfile.setKey(cursor.getString(cursor.getColumnIndex("general_filter_key")));
                    userProfile.setValue(cursor.getString(cursor.getColumnIndex("general_filter_value")));
                    userProfile.setLang(cursor.getString(cursor.getColumnIndex("general_filter_language")));
                    userProfile.setType(cursor.getString(cursor.getColumnIndex("general_filter_type")));
                    userProfile.setValueLocal(cursor.getString(cursor.getColumnIndex("general_filter_value_local")));
                    // Adding contact to list
                    arraylistFilter.add(userProfile);
                    LogApp.e(" gettind profile  data for folder filter : ", " in database handler : " + arraylistFilter.size());
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            LogApp.e(" $$$$ ","exception while get profile : "+e.toString());
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // return contact list
        return arraylistFilter;
    }
    public int getCursorCountFilterForGeneralFilter(String profileId, String Type,String lang,String uId)
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid= '" + profileId+
                "' AND general_filter_type = '"+Type+"' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    public int getCursorCountForAnyGeneralFilter(String profileId,String uId,String lang)
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid= '" + profileId+
                "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    public int getCursorCountForGeneralFilterforProfileChange(String profileId,String uId,String lang,String key)
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid= '" + profileId+
                "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"' AND general_filter_key = '"+key+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    public int getCursorCountForFolderFilter(String profileId, String Type,String lang,String uId)
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid= '" + profileId+
                "' AND general_filter_type = '"+Type+"' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    public String getServerStringFromFolderFilter(String profileId, String Type,String lang,String uId)
    {
        Cursor cursor = null;
        String valueServer="";
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid= '" + profileId+
                "' AND general_filter_type = '"+Type+"' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    valueServer = cursor.getString(cursor.getColumnIndex("general_filter_value"));
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            LogApp.e(" $$$$ ","exception while get profile : "+e.toString());
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return valueServer;
    }
    public void updateGeneralFilter(String profileId,String key,String type,String value,String lang,String uId) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            String[] args = new String[]{profileId,key,type};
            ContentValues values = new ContentValues();

            values.put("general_filter_value", value);

            //db.update(TABLE_GENERAL_FILTER_MASTER, values, "general_filter_pid=? AND general_filter_key=? AND general_filter_type=?", args);

            String strUpdate = "UPDATE " + TABLE_GENERAL_FILTER_MASTER + " SET general_filter_value_local = '"+ value +"' " +
                    " WHERE general_filter_pid = '"+profileId+"' AND general_filter_key = '"+key+"' AND general_filter_type = '"+type+
                    "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strUpdate);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public void updateGeneralFilter2(String profileId,String key,String type,String value,String lang,String uId) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            String[] args = new String[]{profileId,key,type};
            ContentValues values = new ContentValues();

            values.put("general_filter_value", value);

            //db.update(TABLE_GENERAL_FILTER_MASTER, values, "general_filter_pid=? AND general_filter_key=? AND general_filter_type=?", args);

            String strUpdate = "UPDATE " + TABLE_GENERAL_FILTER_MASTER + " SET general_filter_value_local = '"+ value +"' " +
                    " WHERE general_filter_pid = '"+profileId+"' AND general_filter_key = '"+key+"' AND general_filter_type = '"+type+
                    "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strUpdate);
            String strUpdate2 = "UPDATE " + TABLE_GENERAL_FILTER_MASTER + " SET general_filter_value = '"+ value +"' " +
                    " WHERE general_filter_pid = '"+profileId+"' AND general_filter_key = '"+key+"' AND general_filter_type = '"+type+
                    "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strUpdate2);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public void updateGeneralFilterToDelete(String profileId,String type,String value,String lang,String uId) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            //String[] args = new String[]{profileId,key,type};
            ContentValues values = new ContentValues();

            values.put("general_filter_value", value);

            //db.update(TABLE_GENERAL_FILTER_MASTER, values, "general_filter_pid=? AND general_filter_key=? AND general_filter_type=?", args);

            String strUpdate = "UPDATE " + TABLE_GENERAL_FILTER_MASTER + " SET general_filter_value_local = '"+ value +"' " +
                    " WHERE general_filter_pid = '"+profileId+"' AND general_filter_type = '"+type+
                    "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strUpdate);

            String strUpdate2 = "UPDATE " + TABLE_GENERAL_FILTER_MASTER + " SET general_filter_value = '"+ value +"' " +
                    " WHERE general_filter_pid = '"+profileId+"' AND general_filter_type = '"+type+
                    "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strUpdate2);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public void deleteGeneralFilterData(String profileId,String type,String lang,String uId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid = '" +profileId+ "' " +
                    " AND general_filter_type = '"+type+ "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }


    }
    public void deleteGeneralFilterDataWhileProfileChange(String profileId,String type,String lang,String uId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid = '" +profileId+ "' " +
                    " AND general_filter_type = '"+type+ "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }


    }
    public void deleteGeneralFilterDataWhileRefresh(String profileId, String type, String lang, String uId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_GENERAL_FILTER_MASTER + " WHERE general_filter_pid = '" +profileId+ "' " +
                    " AND general_filter_type = '"+type+ "' AND general_filter_language = '"+lang+"' AND general_filter_uid = '"+uId+"'";
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }


    }
    //////////////////////////////////////// RIBBON FILTER ALL OPERATION //////////////////////////
    /* this method store ribbon filter data while login */
    public long addRibbonFilter(String pId,String uId,String lang,String key, String value,String local, String resolution)
    {
        long add = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ribbon_filter_pid", pId);
        values.put("ribbon_filter_uid", uId);
        values.put("ribbon_filter_language", lang);
        values.put("ribbon_filter_key", key);
        values.put("ribbon_filter_value", value);
        values.put("ribbon_filter_value_local", local);
        values.put("resolution", resolution);
        add = db.insert(TABLE_RIBBON_FILTER_MASTER, null, values);
        LogApp.e("record added in general filter   ", " in database handler : " + add);
        return add;
    }
    // this method return all the project listing
    public ArrayList<RibbonFilterDataSet> getRibbonFilter(String profileId, String userId, String language)
    {
        Cursor cursor = null;
        ArrayList<RibbonFilterDataSet> arraylistRibbonFilter = new ArrayList<RibbonFilterDataSet>();
        String selectQuery = "SELECT  * FROM " + TABLE_RIBBON_FILTER_MASTER + " WHERE ribbon_filter_pid ='" +profileId
                + "' AND ribbon_filter_uid= '"+userId+"' AND ribbon_filter_language= '"+language+"'";
        //String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND profile_id= '"+profileId+"' AND language = '"+language+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst())
            {
                do
                {
                    RibbonFilterDataSet ribbonFilter = new RibbonFilterDataSet();
                    ribbonFilter.setpId(cursor.getString(cursor.getColumnIndex("ribbon_filter_pid")));
                    ribbonFilter.setuId(cursor.getString(cursor.getColumnIndex("ribbon_filter_uid")));
                    ribbonFilter.setLang(cursor.getString(cursor.getColumnIndex("ribbon_filter_language")));
                    ribbonFilter.setKey(cursor.getString(cursor.getColumnIndex("ribbon_filter_key")));
                    ribbonFilter.setValue(cursor.getString(cursor.getColumnIndex("ribbon_filter_value")));
                    ribbonFilter.setValueLocal(cursor.getString(cursor.getColumnIndex("ribbon_filter_value_local")));
                    ribbonFilter.setResolution(cursor.getString(cursor.getColumnIndex("resolution")));
                    // Adding contact to list
                    arraylistRibbonFilter.add(ribbonFilter);
                    LogApp.e(" gettind profile  data for folder filter : ", " in database handler : " + arraylistRibbonFilter.size());
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            LogApp.e(" $$$$ ","exception while get profile : "+e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        // return contact list
        return arraylistRibbonFilter;
    }
    public void updateRibbonFilterFromServerSide(String profileId, String uId, String lang, String value) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            //String[] args = new String[]{profileId,key,type};
            ContentValues values = new ContentValues();

            values.put("general_filter_value", value);

            //db.update(TABLE_GENERAL_FILTER_MASTER, values, "general_filter_pid=? AND general_filter_key=? AND general_filter_type=?", args);

            String strUpdate = "UPDATE " + TABLE_RIBBON_FILTER_MASTER + " SET ribbon_filter_value = '"+ value +"' " +
                    " WHERE ribbon_filter_pid = '"+profileId+"' AND ribbon_filter_uid = '"+uId+"' AND ribbon_filter_language = '"+lang+
                    "'";
            db.execSQL(strUpdate);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public void updateRibbonFilterFromLocalSide(String profileId, String uId, String lang, String value) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            //String[] args = new String[]{profileId,key,type};
            ContentValues values = new ContentValues();

            values.put("general_filter_value", value);

            //db.update(TABLE_GENERAL_FILTER_MASTER, values, "general_filter_pid=? AND general_filter_key=? AND general_filter_type=?", args);

            String strUpdate = "UPDATE " + TABLE_RIBBON_FILTER_MASTER + " SET ribbon_filter_value_local = '"+ value +"' " +
                    " WHERE ribbon_filter_pid = '"+profileId+"' AND ribbon_filter_uid = '"+uId+"' AND ribbon_filter_language = '"+lang+
                    "'";
            db.execSQL(strUpdate);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public int getCursorCountForRibbonFilter(String profileId,String uId,String lang)
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_RIBBON_FILTER_MASTER + " WHERE ribbon_filter_pid= '" + profileId+
                "' AND ribbon_filter_uid = '"+uId+"' AND ribbon_filter_language = '"+lang+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    /////////////////////////////////// ALL PROFILE RELATED OPERATION ////////////////////////////////////
    /* this method adding data in user profile while login  */
    public long addAllProfileToDB(String id, String name, String colDetail, String userID, String expandId, String userIdTemp,
                                  String language, String resolution, String key, String value, String keyLocal, String valueLocal)
    {
        long add = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("profile_id", id);
        values.put("profile_name", name);
        values.put("profile_col_detail", colDetail);
        values.put("profile_user_id", userID);
        values.put("profile_expanded_id", expandId);
        values.put("profile_user_id_temp", userIdTemp);
        values.put("language", language);
        values.put("resolution", resolution);
        values.put("profile_attribute_key", key);
        values.put("profile_attribute_value", value);
        values.put("profile_attribute_key_local", keyLocal);
        values.put("profile_attribute_value_local", valueLocal);
        add = db.insert(TABLE_PROFILE_MASTER, null, values);
        LogApp.e("record added in user profile  ", " in database handler : " + add);
        return add;
    }
    public void updateProfileAttribute(String value,String profileId) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            String strUpdate = "UPDATE " + TABLE_PROFILE_MASTER + " SET profile_attribute_value_local = '"+ value +"' WHERE profile_id = '"+profileId+"'";
            db.execSQL(strUpdate);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    public void updateProfile(String resolution,String profileId) {
        SQLiteDatabase db = null;
        try
        {
            db = this.getReadableDatabase();
            String strUpdate = "UPDATE " + TABLE_PROFILE_MASTER + " SET resolution = '"+ resolution +"' WHERE profile_id = '"+profileId+"'";
            db.execSQL(strUpdate);
        }
        catch (Exception e){
            LogApp.e(""," exception while updating : "+e.toString());
            db.close();
        }

    }
    // this method return all the project listing
    public ArrayList<ProfileModelClass> getSingleUserProfileForTableScreen(String userId, String profileId, String language)
    {
        Cursor cursor = null;
        ArrayList<ProfileModelClass> arraylistUserProfile = new ArrayList<ProfileModelClass>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND language= '"+language+"' AND profile_id= '"+profileId+"'";
        //String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND profile_id= '"+profileId+"' AND language = '"+language+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst())
            {
                do
                {
                    ProfileModelClass userProfile = new ProfileModelClass();
                    userProfile.setProfileId(cursor.getString(cursor.getColumnIndex("profile_id")));
                    userProfile.setProfileName(cursor.getString(cursor.getColumnIndex("profile_name")));
                    userProfile.setColDeTail(cursor.getString(cursor.getColumnIndex("profile_col_detail")));
                    userProfile.setProfileUserId(cursor.getString(cursor.getColumnIndex("profile_user_id")));
                    userProfile.setExpandedId(cursor.getString(cursor.getColumnIndex("profile_expanded_id")));
                    userProfile.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
                    userProfile.setResolution(cursor.getString(cursor.getColumnIndex("resolution")));
                    // Adding contact to list
                    arraylistUserProfile.add(userProfile);
                    LogApp.e(" gettind profile  data for folder filter : ", " in database handler : " + arraylistUserProfile.size());
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            LogApp.e(" $$$$ ","exception while get profile : "+e.toString());
        }
        /*finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }*/
        // return contact list
        return arraylistUserProfile;
    }
    // this method return all the project listing
    public ArrayList<ProfileModelClass> getAllUserProfile(String userId, String profileId, String language)
    {
        Cursor cursor = null;
        ArrayList<ProfileModelClass> arraylistUserProfile = new ArrayList<ProfileModelClass>();
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND language= '"+language+"'";
        //String selectQuery = "SELECT  * FROM " + TABLE_PROFILE_MASTER + " WHERE profile_user_id ='" +userId+ "' AND profile_id= '"+profileId+"' AND language = '"+language+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst())
            {
                do
                {
                    ProfileModelClass userProfile = new ProfileModelClass();
                    userProfile.setProfileId(cursor.getString(cursor.getColumnIndex("profile_id")));
                    userProfile.setProfileName(cursor.getString(cursor.getColumnIndex("profile_name")));
                    userProfile.setColDeTail(cursor.getString(cursor.getColumnIndex("profile_col_detail")));
                    userProfile.setProfileUserId(cursor.getString(cursor.getColumnIndex("profile_user_id")));
                    userProfile.setExpandedId(cursor.getString(cursor.getColumnIndex("profile_expanded_id")));
                    userProfile.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
                    userProfile.setResolution(cursor.getString(cursor.getColumnIndex("resolution")));
                    userProfile.setValue(cursor.getString(cursor.getColumnIndex("profile_attribute_value")));
                    userProfile.setKey(cursor.getString(cursor.getColumnIndex("profile_attribute_key")));
                    userProfile.setKeyLocal(cursor.getString(cursor.getColumnIndex("profile_attribute_key_local")));
                    userProfile.setValueLocal(cursor.getString(cursor.getColumnIndex("profile_attribute_value_local")));
                    // Adding contact to list
                    arraylistUserProfile.add(userProfile);
                    LogApp.e(" gettind profile  data for folder filter : ", " in database handler : " + arraylistUserProfile.size());
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            LogApp.e(" $$$$ ","exception while get profile : "+e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        // return contact list
        return arraylistUserProfile;
    }
    public void deleteAllUserProfile(){
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_PROFILE_MASTER;
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }
    }
    public void deleteSingleUserProfile(String userId, String profileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_PROFILE_MASTER + " WHERE profile_user_id = '" +userId +"' AND profile_id = '"+profileId+"'";
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }


    }
    public void deleteAlluserProfile()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_PROFILE_MASTER;
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }
    }
    /////////////////////////////// END FOR ALL RPOFILE RELATED OPERATION ///////////////////////////////

    //////////////////////////////  ONLY DATE FILTER OEPATION /////////////////////////////////////////////
    /* this method adding only date filter for creating json string for control  */
    public long addDateFilter(String key, String value,String userId,String profileId,String filterType)
    {
        long add = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_key", key);
        values.put("date_value", value);
        values.put("date_user_id", userId);
        values.put("date_profile_id", profileId);
        values.put("date_filter_type", filterType);
        add = db.insert(TABLE_DATE_FILTER, null, values);
        LogApp.e("record added in filter info ", " in database handler : " + add);
        return add;
    }
    public void deleteDateFilter(String userId,String profileId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            String strDelete = "DELETE from " + TABLE_DATE_FILTER + " WHERE date_user_id = '" +userId+ "' AND date_profile_id= '"+profileId+"'";
            db.execSQL(strDelete);
        }
        catch (Exception e){
            LogApp.v(" in catch while deleting from database : "," in database handler : "+e.toString());
        }


    }
    public int getCursorCountforDateFilter()
    {
        Cursor cursor = null;
        int count = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_DATE_FILTER;
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            cursor = db.rawQuery(selectQuery, null);
            count = cursor.getCount();
        }
        catch (Exception e)
        {
            LogApp.e(" exception while getting cursor count for folder ", " in database handler : " +e.toString());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return count;
    }
    // this method return all text filter data
    public ArrayList<TextFilterKeyValueDataset> getDateFilterData(String userId, String profileId, String filterType)
    {
        Cursor cursor = null;
        ArrayList<TextFilterKeyValueDataset> arraylistFilterdata = new ArrayList<TextFilterKeyValueDataset>();
        String selectQuery = "SELECT  * FROM " + TABLE_DATE_FILTER + " WHERE date_user_id = '" +userId+ "' AND date_profile_id= '"+profileId+"' AND date_filter_type= '"+filterType+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst())
            {
                do {
                    TextFilterKeyValueDataset filter = new TextFilterKeyValueDataset();
                    filter.setKey(cursor.getString(cursor.getColumnIndex("date_key")));
                    filter.setValue(cursor.getString(cursor.getColumnIndex("date_value")));

                    // Adding contact to list
                    arraylistFilterdata.add(filter);
                    LogApp.e(" gettind filter data for folder filter : ", " in database handler : " + arraylistFilterdata.size());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // return contact list
        return arraylistFilterdata;
    }
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db != null) {
            String clearProfileTable = "DELETE FROM " + TABLE_PROFILE_MASTER;
            db.execSQL(clearProfileTable);

            String clearGeneralFilter = "DELETE FROM " + TABLE_GENERAL_FILTER_MASTER;
            db.execSQL(clearGeneralFilter);
            String clearRibbonFilter = "DELETE FROM " + TABLE_RIBBON_FILTER_MASTER;
            db.execSQL(clearRibbonFilter);
            String clearDateFilter = "DELETE FROM " + TABLE_DATE_FILTER;
            db.execSQL(clearDateFilter);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
