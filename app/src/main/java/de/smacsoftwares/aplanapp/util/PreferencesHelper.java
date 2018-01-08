package de.smacsoftwares.aplanapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// this preferences class to handle store retrive method from anywhere in application
public class PreferencesHelper
{
	SharedPreferences sharedPreferences;
	Editor editor;
	private static final String PREF_NAME = "APLAN-PREFERENCES";
	// Context
	Context context;
	// Shared pref mode
	int PRIVATE_MODE = 0;
	// Constructor
	public PreferencesHelper(Context context) {
			this.context = context;
			sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = sharedPreferences.edit();
	}
	/*public PreferencesHelper(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
		editor = sharedPreferences.edit();
	}*/
	// save form where user has come from
	public void saveUserID(String id)
	{
			editor.putString("id",id);
			editor.commit();
	}
	public String getUserID()
	{
		return sharedPreferences.getString("id","");
	}
	public void saveEmail(String email){
		editor.putString("email",email);
		editor.commit();
	}
	public String getEmail(){
		return sharedPreferences.getString("email","");
	}
	public void saveFullName(String name){
		editor.putString("fullname",name);
		editor.commit();
	}
	public String getFullName(){
		return sharedPreferences.getString("fullname","");
	}
	public void saveDomain(String mobile){
		editor.putString("domain",mobile);
		editor.commit();
	}
	public String getDomain(){
		return sharedPreferences.getString("domain","");
	}
	public void saveOrgUser(String mobile){
		editor.putString("org_user",mobile);
		editor.commit();
	}
	public String getOrgUser(){
		return sharedPreferences.getString("org_user","");
	}
	public void saveUserIdTemp(String user){
		editor.putString("user_temp",user);
		editor.commit();
	}
	public String getUserIdTemp(){
		return sharedPreferences.getString("user_temp","");
	}

	public void saveLanguage(String lan){
		editor.putString("language",lan);
		editor.commit();
	}
	public String getLanguage(){
		return sharedPreferences.getString("language","");
	}
	public void saveProfileId(String id){
		editor.putString("profile_id",id);
		editor.commit();
	}
	public String getProfileId(){
		return sharedPreferences.getString("profile_id","");
	}

	public void saveUserName(String designation){
		editor.putString("username",designation);
		editor.commit();
	}
	public String getUserName(){
		return sharedPreferences.getString("username","");
	}

	public void saveUserProfile(String json){
		editor.putString("profile",json);
		editor.commit();
	}
	public String getUserProfile(){
		return sharedPreferences.getString("profile","");
	}
	public void clearUserProfile(){
		sharedPreferences.edit().remove("profile").commit();
	}
	public void saveUserLogged(boolean flag){
		editor.putBoolean("logged",flag);
		editor.commit();
	}
	public Boolean isUserLooged()
	{
		return sharedPreferences.getBoolean("logged",false);
	}
	public void saveIsTutorialShow(boolean flag){
		editor.putBoolean("toggle",flag);
		editor.commit();

	}
	public Boolean isTutorialShow(){
		return  sharedPreferences.getBoolean("toggle",true);
	}

	public void saveIsAdminScreenShow(boolean flag)
	{
		editor.putBoolean("is_admin_show",flag);
		editor.commit();
	}
	public Boolean isAdminShow()
	{
		return  sharedPreferences.getBoolean("is_admin_show",true);
	}
	public void clearAdminshow(){
		sharedPreferences.edit().remove("is_admin_show").commit();
	}


	public void clearLoginData()
	{
		sharedPreferences.edit().remove("id").commit();
		sharedPreferences.edit().remove("email").commit();
		sharedPreferences.edit().remove("name").commit();
		sharedPreferences.edit().remove("designation").commit();

	}
	public void saveIsfirstTime(boolean flag)
	{
		editor.putBoolean("first_time",flag);
		editor.commit();
	}
	public Boolean isFirstTime(){
		return  sharedPreferences.getBoolean("first_time",true);
	}
	public void saveServerUrl(String Url)
	{
		editor.putString("server_url",Url);
		editor.commit();
	}
	public String getServerUrl()
	{
		return  sharedPreferences.getString("server_url","");
	}

	public void saveServerUrlUser(String Url)
	{
		editor.putString("server_url_user",Url);
		editor.commit();
	}
	public String getServerUrlUser()
	{
		return  sharedPreferences.getString("server_url_user","");
	}

	public void saveServerUrlGet(String Url)
	{
		editor.putString("server_url_get",Url);
		editor.commit();
	}
	public String getServerUrlGet()
	{
		return  sharedPreferences.getString("server_url_get","");
	}
	public void saveServerUrlTask(String Url)
	{
		editor.putString("server_url_task",Url);
		editor.commit();
	}
	public String getActivity()
	{
		return sharedPreferences.getString("activity","");
	}
	public void saveCurrentFiredFilter(String controlType)
	{
		editor.putString("current_fired_filter",controlType);
		editor.commit();
	}
	public String getCurrentFiredFilter(){
		return  sharedPreferences.getString("current_fired_filter","");
	}
	public void clearCurrentFiredFilter(){
		sharedPreferences.edit().remove("current_fired_filter").commit();
	}

	public void saveAlreadyFiredFilter(String controlType)
	{
		editor.putString("already_fired_filter",controlType);
		editor.commit();
	}
	public String getAlreadyFiredFilter(){
		return  sharedPreferences.getString("already_fired_filter","");
	}
	public void isContolFirstTime(String str){
		editor.putString("is_firsttime",str);
		editor.commit();
	}
	public String getIsControlFirstTime(){
		return  sharedPreferences.getString("is_firsttime","");
	}
	public void saveTaskPosition(int pos){
		editor.putInt("task_position",pos);
		editor.commit();
	}
	public void saveIsGeneralFilterDialogshow(String str){
		editor.putString("is_show_dialog",str);
		editor.commit();
	}
	public String getIsGeneralFilterDialogshow(){
		return  sharedPreferences.getString("is_show_dialog","yes");
	}
	public int getTaskPosition(){
		return  sharedPreferences.getInt("task_position",0);
	}
	public void saveAgreegatePosition(int pos){
		editor.putInt("agreegate_position",pos);
		editor.commit();
	}
	public int getAgreegatePosition(){
		return  sharedPreferences.getInt("agreegate_position",0);
	}
	public void saveLastDateFilterString(String str)
	{
		editor.putString("last_date_filter",str);
		editor.commit();
	}
	public String getLastDateFilterString(){
		return sharedPreferences.getString("last_date_filter","");
	}
	public void clearLastDateFilterString(){
		sharedPreferences.edit().remove("last_date_filter").commit();
	}

	public void saveLastTrafficFilterString(String str)
	{
		editor.putString("last_traffic_filter",str);
		editor.commit();
	}
	public String getLastTrafficFilterString(){
		return sharedPreferences.getString("last_traffic_filter","");
	}
	public void clearLastTrafficFilterString(){
		sharedPreferences.edit().remove("last_traffic_filter").commit();
	}

	public void saveLastStatusFilterString(String str)
	{
		editor.putString("last_status_filter",str);
		editor.commit();
	}
	public String getLastStatusilterString(){
		return sharedPreferences.getString("last_status_filter","");
	}
	public void clearLastStatusilterString(){
		sharedPreferences.edit().remove("last_status_filter").commit();
	}

	public void saveIsProgressShow(String str){
		editor.putString("is_progress_show",str);
		editor.commit();
	}
	public String getIsProgressShow(){
		return sharedPreferences.getString("is_progress_show","");
	}
	/*public void saveStaticUserName(String str){
		editor.putString("static_username",str);
		editor.commit();
	}
	public void saveStaticPassword(String str){
		editor.putString("static_password",str);
		editor.commit();
	}
	public String getStaticUserName(){
		return sharedPreferences.getString("static_username","");
	}
	public String getStaticPassword(){
		return sharedPreferences.getString("static_password","");
	}*/

	public void saveClickedAgreegatePosition(String pos){
		editor.putString("clicked_agreegate_position",pos);
		editor.commit();
	}
	public String getClickedAgreegatePosition(){
		return sharedPreferences.getString("clicked_agreegate_position","");
	}
	public void clearClickedAgreegatePosition(){
		sharedPreferences.edit().remove("clicked_agreegate_position").commit();
	}

	public void saveClickedTaskPosition(String pos){
		editor.putString("clicked_task_position",pos);
		editor.commit();
	}
	public void saveLastFolderfilterString(String value){
		editor.putString("last_folder_filter_string",value);
		editor.commit();
	}
	public String getLastFolderfilterString(){
		return sharedPreferences.getString("last_folder_filter_string","");
	}
	public void clearLastFolderfilterString(){
		sharedPreferences.edit().remove("last_folder_filter_string").commit();
	}

	public void saveLastTextFilterString(String value){
		editor.putString("last_text_filter_string",value);
		editor.commit();
	}
	public String getLastTextFilterString(){
		return sharedPreferences.getString("last_text_filter_string","");
	}
	public void clearLastTextFilterString(){
		sharedPreferences.edit().remove("last_text_filter_string").commit();
	}

	public void saveLastUserDefinedFilterString(String value){
		editor.putString("last_user_defined_filter_string",value);
		editor.commit();
	}
	public String getLastUserDefinedFilterString(){
		return sharedPreferences.getString("last_user_defined_filter_string","");
	}
	public void clearLastUserDefinedFilterString(){
		sharedPreferences.edit().remove("last_user_defined_filter_string").commit();
	}

	public void saveLastUserDefinedFilterString2(String value){
		editor.putString("last_user_defined_filter_string",value);
		editor.commit();
	}
	public String getLastUserDefinedFilterString2(){
		return sharedPreferences.getString("last_user_defined_filter_string","");
	}
	public void clearLastUserDefinedFilterString2(){
		sharedPreferences.edit().remove("last_user_defined_filter_string").commit();
	}

	public void saveUserDefinedType(String type){
		editor.putString("user_defined_type",type);
		editor.commit();
	}
	public String getUserDefinedType(){
		return sharedPreferences.getString("user_defined_type","");
	}
	public void clearUserDefinedType(){
		sharedPreferences.edit().remove("user_defined_type").commit();
	}

	public void saveAgreegateLastFiredFilter(String type){
		editor.putString("agreegate_last_fired_filter",type);
		editor.commit();
	}
	public String getAgreegateLastFiredFilter(){
		return sharedPreferences.getString("agreegate_last_fired_filter","");
	}
	public void clearAgreegateLastFiredFilter(){
		sharedPreferences.edit().remove("agreegate_last_fired_filter").commit();
	}

	public void saveTaskLastFiredFilter(String type){
		editor.putString("task_last_fired_filter",type);
		editor.commit();
	}
	public String getTaskLastFiredFilter(){
		return sharedPreferences.getString("task_last_fired_filter","");
	}
	public void clearTaskLastFiredFilter(){
		sharedPreferences.edit().remove("task_last_fired_filter").commit();
	}

	public void saveIsFirsttimeToLoadControl(String value){
		editor.putString("is_first_time_to_load_control",value);
		editor.commit();
	}
	public String getIsFirsttimeToLoadControl(){
		return sharedPreferences.getString("is_first_time_to_load_control","");
	}
	public void clearIsFirsttimeToLoadControl(){
		sharedPreferences.edit().remove("is_first_time_to_load_control").commit();
	}

	public void saveLastDashboardFiredFilter(String value){
		editor.putString("last_dashboard_fire_filter",value);
		editor.commit();
	}
	public String getLastDashboardFiredFilter(){
		return sharedPreferences.getString("last_dashboard_fire_filter","");
	}
	public void clearLastDashboardFiredFilter(){
		sharedPreferences.edit().remove("last_dashboard_fire_filter").commit();
	}

	public void saveResourcefilter(String value){
		editor.putString("is_resource_filter",value);
		editor.commit();
	}
	public String getResourcefilter(){
		return sharedPreferences.getString("is_resource_filter","");
	}
	public void clearResourcefilter(){
		sharedPreferences.edit().remove("is_resource_filter").commit();
	}

	public void saveFireResourcefilter(String value){
		editor.putString("current_resource_filter",value);
		editor.commit();
	}
	public String getFireResourcefilter(){
		return sharedPreferences.getString("current_resource_filter","");
	}
	public void clearFireResourcefilter(){
		sharedPreferences.edit().remove("current_resource_filter").commit();
	}

	public void saveLastFiredFilterAll(String value){
		editor.putString("save_last_firefilter",value);
		editor.commit();
	}
	public String getLastFiredFilterAll(){
		return sharedPreferences.getString("save_last_firefilter","");
	}
	public void clearLastFiredFilterAll(){
		sharedPreferences.edit().remove("save_last_firefilter").commit();
	}


	public String getClickedTaskPosition(){
		return sharedPreferences.getString("clicked_task_position","");
	}
	public void clearClickedTaskPosition(){
		sharedPreferences.edit().remove("clicked_task_position").commit();
	}
	public boolean isDisableApp(){
		return sharedPreferences.getBoolean("is_disable",false);
	}
	public void saveAppDisalbe(boolean flag){
		editor.putBoolean("is_disable",flag);
		editor.commit();
	}
	public boolean isLogoutApp(){
		return sharedPreferences.getBoolean("is_logout",false);
	}
	public void saveAppLogout(boolean flag){
		editor.putBoolean("is_logout",flag);
		editor.commit();
	}
    public void saveDeviceToken(String value){
        editor.putString("device_token",value);
        editor.commit();
    }
    public String getDeviceToken(){
        return sharedPreferences.getString("device_token","1234");
    }
	public void saveAccessToken(String value){
		editor.putString("access_token",value);
		editor.commit();
	}
	public String getAccessToken(){
		return sharedPreferences.getString("access_token","");
	}

	public void saveOrganizationId(String value){
		editor.putString("organization_id",value);
		editor.commit();
	}
	public String getOrganizationId(){
		return sharedPreferences.getString("organization_id","");
	}
	public void saveOrganizationName(String value){
		editor.putString("organization_name",value);
		editor.commit();
	}
	public String getOrganizationName(){
		return sharedPreferences.getString("organization_name","");
	}
	public void saveTokenforControl(String value){
		editor.putString("token_control",value);
		editor.commit();
	}
	public String getTokenforControl(){
		return sharedPreferences.getString("token_control","");
	}

	public void saveShowToday(boolean flag){
		editor.putBoolean("show_today",flag);
		editor.commit();

	}
	public boolean isShowToday(){
		return sharedPreferences.getBoolean("show_today",true);
	}
	public void saveIsLicence(boolean flag){
		editor.putBoolean("is_licence",flag);
		editor.commit();
	}
	public boolean hasLicence(){
		return sharedPreferences.getBoolean("is_licence",false);
	}
	public void saveIsFilemissing(boolean flag){
		editor.putBoolean("file_missing",flag);
		editor.commit();
	}
	public boolean isFileMissing(){
		return sharedPreferences.getBoolean("file_missing",false);
	}
    public void saveIsRightRevoked(boolean flag){
        editor.putBoolean("right_revoked",flag);
        editor.commit();
    }
    public boolean isRightRevoked(){
        return sharedPreferences.getBoolean("right_revoked",false);
    }

    public void saveIsLicenceExpire(boolean flag){
        editor.putBoolean("licence_expire",flag);
        editor.commit();
    }
    public boolean isLicenceExpire(){
        return sharedPreferences.getBoolean("licence_expire",false);
    }

	public void saveIsUserDisabled(boolean flag){
		editor.putBoolean("user_disabled",flag);
		editor.commit();
	}
	public boolean isUserDisabled(){
		return sharedPreferences.getBoolean("user_disabled",false);
	}

	public void saveIsOrgUserDisabled(boolean flag){
		editor.putBoolean("org_user_disabled",flag);
		editor.commit();
	}
	public boolean isOrgUserDisabled(){
		return sharedPreferences.getBoolean("org_user_disabled",false);
	}


	public void save404Count(int value){
		editor.putInt("404_count",value);
		editor.commit();
	}
	public int get404Count(){
		return sharedPreferences.getInt("404_count",0);
	}
	public void  clear404Count(){
		sharedPreferences.edit().remove("404_count").commit();
	}
	public void saveUrl(String url){
		editor.putString("service_url",url);
		editor.commit();
	}
	public String getUrl(){
		return sharedPreferences.getString("service_url","");
	}
	public void clearServiceUrl(){
		sharedPreferences.edit().remove("service_url").commit();
	}

	public void saveNotificationBody(String body){
		editor.putString("noti_body",body);
		editor.commit();
	}
	public String getNotificationBody(){
		return sharedPreferences.getString("noti_body","");
	}

	public void saveNotificationTitle(String title){
		editor.putString("noti_title",title);
		editor.commit();
	}
	public String getNotificationTitle(){
		return sharedPreferences.getString("noti_title","");
	}

	public void saveNotificationActionType(String title){
		editor.putString("action_type",title);
		editor.commit();
	}
	public String getNotificationActionType(){
		return sharedPreferences.getString("action_type","");
	}


	public void saveShowActiveDialog(String str){
		editor.putString("is_show_active",str);
		editor.commit();
	}
	public String getActiveDialog(){
		return sharedPreferences.getString("is_show_active","");
	}

	public void saveShowInActiveDialog(String str){
		editor.putString("is_show_inactive",str);
		editor.commit();
	}
	public String getInActiveDialog(){
		return sharedPreferences.getString("is_show_inactive","");
	}

	public void saveShowDialogType(String str){
		editor.putString("show_dialog_type",str);
		editor.commit();
	}
	public String getShowDialogType(){
		return sharedPreferences.getString("show_dialog_type","");
	}
	public void saveIsDemoUser(boolean flag){
		editor.putBoolean("demo_user",flag);
		editor.commit();

	}
	public boolean isDemoUser(){
		return sharedPreferences.getBoolean("demo_user",false);
	}
}
