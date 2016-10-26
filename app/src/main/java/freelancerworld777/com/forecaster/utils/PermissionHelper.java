package freelancerworld777.com.forecaster.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import freelancerworld777.com.forecaster.R;

/**
 * Created by HP on 26-10-2016.
 */

public class PermissionHelper {
    public static boolean isMarshMallow(){
        return(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M);
    }

    /**Manifest import used here is android and not app **/

    public static final String LOCATION_FINE= Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int REQUEST_PERMISSIONS = 165;
    public static final String LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;


    @TargetApi(Build.VERSION_CODES.M)
    public static Boolean requestLocationAccess(Activity mActivity) {

        if (isMarshMallow()) {

            //Boolean to check whether "Never show this again" is checked in permission dialog
            Boolean isAccessDenied = false;
            ArrayList<String> permissions = new ArrayList<>();
            if ((ContextCompat.checkSelfPermission(mActivity,
                    PermissionHelper.LOCATION_FINE)
                    != PackageManager.PERMISSION_GRANTED)) {
                permissions.add(PermissionHelper.LOCATION_FINE);
                if (mActivity.shouldShowRequestPermissionRationale(PermissionHelper.LOCATION_FINE)) {
                    isAccessDenied = true;
                }
            }

            if ((ContextCompat.checkSelfPermission(mActivity,
                    PermissionHelper.LOCATION_COARSE)
                    != PackageManager.PERMISSION_GRANTED)) {
                permissions.add(PermissionHelper.LOCATION_COARSE);
                if (mActivity.shouldShowRequestPermissionRationale(PermissionHelper.LOCATION_COARSE)) {
                    isAccessDenied = true;
                }
            }
            if (!isAccessDenied) {
                if (permissions.size() == 0)
                    return true;
                else {
                    String[] mStringArray = new String[permissions.size()];
                    mStringArray = permissions.toArray(mStringArray);
                    ActivityCompat.requestPermissions(mActivity, mStringArray,
                            PermissionHelper.REQUEST_PERMISSIONS);
                    return false;
                }
            } else {

                //   TODO: Needed for mandatory permissions
                PermissionHelper.showSettingsSnackbar(mActivity,mActivity.getString(R.string.settings_permission));
                return false;
            }

        } else {
            return true;
        }
    }

    public static void showSettingsSnackbar(final Activity mActivity, final String msg) {
        final int REQUEST_PERMISSION_SETTING = 101;


        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View parentLayout = mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar mSnackbar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //on action click code
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                                intent.setData(uri);
                                mActivity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            }
                        })
                        .setAction("Not Now", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setDuration(Snackbar.LENGTH_LONG);
                mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(mActivity, R.color.cardview_dark_background));
                mSnackbar.setActionTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
                TextView textView = ((TextView) mSnackbar.getView().findViewById(android.support.design.R.id.snackbar_text));
                textView.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
                textView.setMaxLines(5);
                mSnackbar.show();
            }
        });
    }

}
