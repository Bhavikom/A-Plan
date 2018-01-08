package de.smacsoftwares.aplanapp.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// this class is for declaring static and global variable to use in whole application and also declare global methods
public class GlobalClass
{
	public static String CONTROL_PATH="file:///android_asset/index.html?profile=";
	public static String strAccessToken = "";

	/* to handle font type*/
	public static Typeface fontRegular;
	public static Typeface fontLight;
	public static Typeface fontBold;
	public static Typeface fontMedium;
	public static String fontPath="fonts/Concord W00 Regular.ttf";
	public static String fontPathBold="fonts/Concord W00 Bold.ttf";
	public static String fontPathLight="fonts/Concord W00 Light.ttf";
	public static String fontPathMedium="fonts/Concord W00 Medium.ttf";

	private static interfaceDateSelected callerActivity;
	public static SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
	public static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	public static boolean isEditMode=false;

	public static String MANAGEMENT="/management/";
	//public static String DEMO_URL="http://13.93.124.247:9999/management/";
	//public static String DEMO_URL="http://braintool-demo.smacsoftwares.de:9999/management/";
	public static String DEMO_URL="https://braintool-demo.smacsoftwares.de:9998/management/";

	public static String GET_URL="";
	public static String SERVICE_URL="";
	//public static String GET_USERT_DETAIL_URL = "http://138.201.245.106/ssoftDev/management/Add/";
	//public static String GET_USERT_DETAIL_URL = "http://braintool-admin.smacsoftwares.de:9999/management/Add/";
	public static String GET_USERT_DETAIL_URL = "https://braintool-admin.smacsoftwares.de:9998/management/Add/";

	public static Context context;
	public static int SCREEN_HEIGHT;
	public static int SCREEN_WIDTH;	
	public static ProgressDialog progressDialog;
	public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	public static Pattern pattern;
	public static Matcher matcher;
	public static String[] arrayDate;
	public static String[] arrayStatus;
	public static String[] arrayTraffic;

	// Database Name
	public static final String DATABASE_NAME = "AplanDb.sqlite";
	public GlobalClass(){
	}
	public GlobalClass(interfaceDateSelected activity) {
		callerActivity = (interfaceDateSelected) activity;
	}
	public GlobalClass(Context context)
	{
		GlobalClass.context = context;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		SCREEN_HEIGHT = displaymetrics.heightPixels;
		SCREEN_WIDTH = displaymetrics.widthPixels;
	}
//	 return screenWidth
	public static int screenWidth(Context con){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) con).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		SCREEN_WIDTH = displaymetrics.widthPixels;
		return SCREEN_WIDTH;
	}
//	 return screnHeight
	public static int screenHeight(Context con){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) con).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		SCREEN_HEIGHT = displaymetrics.heightPixels;
		return SCREEN_HEIGHT;
	}
//	check that email is valid or not
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
//	check that network is available or not
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
//	 show toast from anywhere in application
	/*public static void showToast(Activity activity, String text)
	{
		Toast.makeText(activity,text, Toast.LENGTH_SHORT).show();
	}
//	 show internet not available toast in application
	public static void showToastInternet(Activity activity)
	{
		Toast.makeText(activity,activity.getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();
	}
//	show valid email notice to user
	public static void showToastEmail(Activity activity)
	{
		Toast.makeText(activity,activity.getString(R.string.please_enter_valid_email_address), Toast.LENGTH_SHORT).show();
	}
//	show toast to fill all field requiring
	public static void showToastField(Activity activity)
	{
		Toast.makeText(activity,activity.getString(R.string.please_fill_all_required_field), Toast.LENGTH_SHORT).show();
	}*/
//	return current epochTime
	public static String epochTimeToDate(String time){
		String date="";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		date=sdf.format(new Date(Long.parseLong(time)));
		return  date;
	}
//	convert epoch time to string
	public static  String epochFormatetoString(String time){
		String date="";
		if(!TextUtils.isEmpty(time)){
			String splited = time.substring(6);
			String finalString = splited.substring(0,splited.length()-2);
			String[] Splite = finalString.split("\\+");

			date=Splite[0];
		}

		return date;
	}
//	change date formate globally from this method
	public static String dateFormateChanged(Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		String dateString = formatter.format(date);

		return dateString;
	}
//	get ephoch time in milliseconds
	public static long getEpochTime()
	{
		long ephoch;
		long millysec = System.currentTimeMillis();
		ephoch = millysec/1000;
		return ephoch;
	}
//	return sum of all numbers from single strings
	public static int sumFromString(String str)
	{
		int sum = 0;
		for(int i = 0; i < str.length() ; i++){
			if( Character.isDigit(str.charAt(i)) ){
				sum = sum + Character.getNumericValue(str.charAt(i));
			}
		}
		return sum;
	}
//	return random number
	public static int getRandomNumber(){
		Random r = new Random();
		int Low = 10;
		int High = 100;
		int Result = r.nextInt(High-Low) + Low;

		return Result;
	}
//  change date formate
	public static String dateFormateChanged2(Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		return dateString;
	}
	public static void showAlertDialog(Activity activity, String message, String title) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		// set title
		alertDialogBuilder.setTitle(title);
		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)
		.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	public static void alertDialog(Activity activity, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		// set title
		// alertDialogBuilder.setTitle(title);
		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false)

		.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.show();
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	public static void alertDialogInternet(Activity activity) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

		// set title

		// alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage("Please check internet connection.").setCancelable(false)
		.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.show();
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	public static Runnable getTouchDelegateAction(final View parent, final View delegate, final int topPadding, final int bottomPadding, final int leftPadding, final int rightPadding) {
		return new Runnable()
		{
	        @Override
	        public void run() 
	        {
	            
	            //Construct a new Rectangle and let the Delegate set its values
	            Rect touchRect = new Rect();
	            delegate.getHitRect(touchRect);
	            
	            //Modify the dimensions of the Rectangle
	            //Padding values below zero are replaced by zeros
	            touchRect.top-= Math.max(0, topPadding);
	            touchRect.bottom+= Math.max(0, bottomPadding);
	            touchRect.left-= Math.max(0, leftPadding);
	            touchRect.right+= Math.max(0, rightPadding);
	            
	            //Now we are going to construct the TouchDelegate
	            TouchDelegate touchDelegate = new TouchDelegate(touchRect, delegate);
	            
	            //And set it on the parent
	            parent.setTouchDelegate(touchDelegate);
	            
	        }
		};
	}
	public static boolean containsWhiteSpace(final String testCode){
	    if(testCode != null){
	        for(int i = 0; i < testCode.length(); i++){
	            if(Character.isWhitespace(testCode.charAt(i))){
	                return true;
	            }
	        }
	    }
	    return false;
	}
	public static boolean validate(final String password)
	{
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	    	    
	}
	public static boolean isValidPassword(final String password) {

	        Pattern pattern;
	        Matcher matcher;

	        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

	        pattern = Pattern.compile(PASSWORD_PATTERN);
	        matcher = pattern.matcher(password);

	        return matcher.matches();

	}
	public  static boolean isValidEmail(final String email) {
		 Pattern pattern;
	        Matcher matcher;

	        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[a-z])";

	        pattern = Pattern.compile(PASSWORD_PATTERN);
	        matcher = pattern.matcher(email);

	        return matcher.matches();
		}
	 public static interface fragmentInterface
	    {
	    	public void settext(String text);
		}
	public static int getScreenResolution(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;


		return width;
	}
	public static byte[] BitmaptoByteArray(Bitmap bitmap)
	{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] img=bos.toByteArray();
		return  img;
	}
	public static int getXcordinate(ImageView imageview)
	{
		int[] location = new int[2];
		imageview.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];

		return  x;
	}
	public static  int getYCordinate(ImageView imageview)
	{
		int[] location = new int[2];
		imageview.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];

		return  y;
	}
	public static void  setLocked(ImageView v)
	{
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);  //0 means grayscale
		ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
		v.setColorFilter(cf);
		v.setAlpha(200);   // 128 = 0.5
	}
	public static void  setUnlocked(ImageView v)
	{
		v.setColorFilter(null);
		v.setAlpha(255);
	}
	public static void showDatePickerDialog(final Context con){
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		DatePickerDialog datePickerDialog = new DatePickerDialog(con,
				new DatePickerDialog.OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						String selectedDate = dayOfMonth+"."+(monthOfYear+1)+"."+year;
						Date Dateselected = convertStringToDate(selectedDate);
						callerActivity.onDateSelected(Dateselected);
					}
				}, mYear, mMonth, mDay);
		datePickerDialog.show();
	}
	/* this is the interface which will get called when clicked on datepicer ok button */
	public interface  interfaceDateSelected {
		public void onDateSelected(Date start);
	}
	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	public interface TextCallBackListener {
		public void updateText(String val);
	}
	public static String convertDateToString(Date date) {
		String dateStr="";
		try {
			dateStr = dateFormater.format(date);
		}
		catch (Exception e){
		}
		return  dateStr;
	}
	public static Date convertStringToDate(String date) {
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormater.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  convertedDate;
	}
	public static Date convertStringToDate2(String date) {
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormater2.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  convertedDate;
	}
	public static List<Date> getDatesWithWeekend(String dateString1, String dateString2)
	{
		ArrayList<Date> dates = new ArrayList<Date>();
		DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

		Date date1 = null;
		Date date2 = null;

		try {
			date1 = df1 .parse(dateString1);
			date2 = df1 .parse(dateString2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);


		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		while(!cal1.after(cal2))
		{
			dates.add(cal1.getTime());
			cal1.add(Calendar.DATE, 1);
		}
		return dates;
	}
	public static void hideSoftKeyboard(SearchView input,Activity activity) {
		input.setInputType(0);
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}
	public static String arrayToCommaSeparated(String[] array){
		StringBuilder sb = new StringBuilder();
		for (String n : array) {
			if (sb.length() > 0) sb.append(',');
			sb.append("").append(n).append("");
		}
		return sb.toString();
	}
	public static List<String> commaSeparetedtoArraylist(String commaSepareated){
		List<String> items = Arrays.asList(commaSepareated.split("\\s*,\\s*"));
		return items;
	}
	public static  ArrayList<String> stringArraytoArrayList(String[] array){
		return new ArrayList<String>(Arrays.asList(array));
	}
	public static  String removeCommaAttheEnd(String str){
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	public static void setupTypeface(View view, Typeface globalFace) {
		try {
			if (view instanceof EditText) {
				((EditText) view).setTypeface(globalFace);
			} else if (view instanceof CheckBox) {
				((CheckBox) view).setTypeface(globalFace);
			} else if (view instanceof TextView) {
				((TextView) view).setTypeface(globalFace);
				//((TextView) view).setLineSpacing(getPixelsFromDp(1f), 1f);
			} else if (view instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) view;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					setupTypeface(child, globalFace);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getAppversion(Activity activity){
		String version="";
		PackageInfo pInfo = null;
		try {
			pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
			version = pInfo.versionName;

		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return  version;
	}
	public static boolean isTablet(Activity activity){
		boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
		return  tabletSize;
	}
	public static boolean isTablet2(Context activity){
		boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
		return  tabletSize;
	}
	public static boolean isAppIsInBackground(Context context) {
		boolean isInBackground = true;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
			List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					for (String activeProcess : processInfo.pkgList) {
						if (activeProcess.equals(context.getPackageName())) {
							isInBackground = false;
						}
					}
				}
			}
		} else {
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(context.getPackageName())) {
				isInBackground = false;
			}
		}

		return isInBackground;
	}
	public static void hideSoftKeyboard(Activity activity) {
		if(activity.getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}
	/*public static void showSnackBar(Activity activity, boolean isSuccess, String message)
	{
		Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
		TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(isSuccess ? Color.GREEN : Color.RED);
		textView.setMaxLines(3);
		snackbar.show();
	}
	public static void showCustomDialogInternet(Activity activity)
	{
		Toast.makeText(activity,activity.getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show();

		Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),activity.getString(R.string.please_check_internet_connection), Snackbar.LENGTH_SHORT);
		TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.RED);
		textView.setMaxLines(3);
		snackbar.show();
	}
	public static void showCustomDialogEmailValidation(Activity activity)
	{
		Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),activity.getString(R.string.please_enter_valid_email_address), Snackbar.LENGTH_SHORT);
		TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.RED);
		textView.setMaxLines(3);
		snackbar.show();

	}*/
	//	show toast to fill all field requiring
	/*public static void showCustomDialogFieldRequired(Activity activity)
	{
		Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
				activity.getString(R.string.please_fill_all_required_field), Snackbar.LENGTH_SHORT);
		TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.RED);
		textView.setMaxLines(3);
		snackbar.show();
	}*/
	public static String getOsVersion(){
		return String.valueOf(Build.VERSION.SDK_INT);
	}
	public static String getDeviceType(Activity activity){
		boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
		if(tabletSize){
			return "Android/Tablet";
		}
		else {
			return "Android/Phone";
		}
	}
	public static  String getDeviceModel(){
		return String.valueOf(Build.MODEL);
	}
	public static boolean validateUrl(String input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		if (input.matches("((http)[s]?(://).*)")){
			return true;
		}else {
			return false;
		}
	}
	/* getting current screenOrientation of screen */
	public static int getScreenOrientation(Activity activity) {
		/* will return 1 for portrait and 2 for landscape*/
		Display getOrient = activity.getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if(getOrient.getWidth()==getOrient.getHeight()){
			orientation = Configuration.ORIENTATION_SQUARE;
		} else{
			if(getOrient.getWidth() < getOrient.getHeight()){
				orientation = Configuration.ORIENTATION_PORTRAIT;
			}else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}
	public static void showCustomMessageDialog(Context context,String title,String message)
	{
		final Dialog dialog = new Dialog(context);
		View dialogView = View.inflate(context, R.layout.custom_dialog_user_inactive, null);
		dialog.setContentView(dialogView);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		TextView textview_title = (TextView) dialogView.findViewById(R.id.title);
		if(!TextUtils.isEmpty(title)){
			textview_title.setText(title);
		}else {
			textview_title.setVisibility(View.GONE);
		}

		TextView textview_message = (TextView) dialogView.findViewById(R.id.message);
		textview_message.setText(message);
		TextView btnOk = (TextView) dialogView.findViewById(R.id.cancel);
		LinearLayout linearMain = (LinearLayout)dialogView.findViewById(R.id.linear_main);
		GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

		dialog.show();
		if(context.getResources().getBoolean(R.bool.isTablet)){dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			/*if(GlobalClass.getScreenOrientation(context) == 1){
				dialog.getWindow().setLayout(SCREEN_WIDTH / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
			}else {
				dialog.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
			}*/

		}else {
			dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
		}
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}
    public static void showCustomDialogInternet(Activity activity)
    {
        showCustomMessageDialog(activity,activity.getString(R.string.no_internet_title),activity.getString(R.string.please_check_internet_connection));
    }

    public static void showCustomDialogEmailValidation(Activity activity)
    {
        showCustomMessageDialog(activity,activity.getString(R.string.invalid_email_title),activity.getString(R.string.please_enter_valid_email_address));
    }

    //	show toast to fill all field requiring
    public static void showCustomDialogFieldRequired(Activity activity)
    {
        showCustomMessageDialog(activity,"",activity.getString(R.string.please_fill_all_required_field));
    }
	static Spanned Text;
    public  static String getLinkString(Activity activity, TextView textView){
		SpannableString ss = new SpannableString(activity.getString(R.string.my_smsc_link));
		ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
		ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		return ss.toString();
		/*Text = Html.fromHtml("<br />" +
				"<a href='http://www.smacsoftwares.de//'>http://www.smacsoftwares.de/</a>");
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setText(Text);
		return Text.toString();*/
	}
	public static class URLSpanNoUnderline extends URLSpan {
		public URLSpanNoUnderline(String url) {
			super(url);
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(true);
		}
	}
}
