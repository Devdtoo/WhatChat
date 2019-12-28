package com.devdtoo.whatchat.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.devdtoo.whatchat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Commonhelper {
    private static Context _ctx;
    private SharedPreferences Shpref;
    private SharedPreferences.Editor editShpref;
    private ArrayList<HashMap<String, String>> hasharrlist;
    private ArrayList<String> arrlist;
    private Dialog dialog;
    private final Calendar myCalendar = Calendar.getInstance();
    private DataPickerListener datepickerListner;


    public interface DataPickerListener {
        public void onSetDataPicker(String datepicker_for, int year, int month, int day);
    }

    public Commonhelper(Context context) {
        this._ctx = context;
        Shpref = _ctx.getSharedPreferences(Shared.sp_filename,
                _ctx.MODE_PRIVATE);
        editShpref = Shpref.edit();
//        dialog = new ProgressDialog(context);
        /*dialog.setMessage("please wait...");*/

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setProgressStyle();




    }

    public void ShowMesseage(String str) {
        Toast.makeText(_ctx, str, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {

        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean setSharedPreferences(String obj, String val) {
        try {
            editShpref.putString(obj, val);
            editShpref.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSharedPreferences(String obj, String defval) {
        String a = Shpref.getString(obj, defval);
        return a;

    }

    public boolean ClearSharedPreference() {
        try {
            editShpref.clear();
            editShpref.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final void callintent(Context context, Class newc) {
        Intent i = new Intent(context, newc);
        context.startActivity(i);
    }


    public boolean isNullOrEmpaty(String param) {
        if (isNull(param) || param.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNull(String str) {
        return str == null ? true : false;
    }

    public JSONArray CnvrtToJsonArray(String stringjson) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(stringjson);
        } catch (JSONException e) {
            jsonArray = null;
        }
        return jsonArray;
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) _ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isCameraPermissionGranted(Activity _activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (_activity.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                //     Log.v(TAG, "Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }


    }

    public boolean isStoragePermissionGranted(Activity _activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (_activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //     Log.v(TAG, "Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");
            return true;
        }


    }

    public static boolean isStatus(JSONArray jsonArray) {
        boolean check = false;
        try {
            JSONObject jsobj = jsonArray.getJSONObject(0);
            check = jsobj.getBoolean("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return check;
    }

    public static String isMessage(JSONArray jsonArray) {
        String str = "";
        try {
            JSONObject jsobj = jsonArray.getJSONObject(0);
            str = jsobj.getString("Message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return str;
    }

    public void ShowLoader() {
        this.dialog.show();
    }

    public void HideLoader() {
        this.dialog.dismiss();
    }

    public void RateApp() {
        Uri uri = Uri.parse("market://details?id=" + _ctx.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            _ctx.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            _ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + _ctx.getPackageName())));
        }
    }

    public void showdatepicker(DataPickerListener listener, final String datepicker_for) {
        this.datepickerListner = listener;

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                datepickerListner.onSetDataPicker(datepicker_for, year, monthOfYear + 1, dayOfMonth);
            }


        };
         new DatePickerDialog(_ctx, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();



    }

}




