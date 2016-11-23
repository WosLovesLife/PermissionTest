package com.woslovelife.permissiontest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRxPermissions = new RxPermissions(this);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!checkPermission(Manifest.permission.CAMERA)) {
//                    requestPermission(Manifest.permission.CAMERA);
//                }
//                mRxPermissions
//                        .request(Manifest.permission.CAMERA)
//                        .subscribe(new Action1<Boolean>() {
//                            @Override
//                            public void call(Boolean granted) {
//                                if (granted) { // Always true pre-M
//                                    // I can control the camera now
//                                } else {
//                                    // Oups permission denied
//                                }
//                            }
//                        });

                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {
                                if (permission.granted) {
                                    // `permission.name` is granted !
                                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // Denied permission without ask never again
                                    Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Denied permission with ask never again
                                    // Need to go to the settings
                                    Toast.makeText(MainActivity.this, "不再提示", Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("提示")
                                            .setMessage("申请权限")
                                            .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    toDetailPage();
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                            }
                        });
            }
        });
    }

    boolean checkPermission(String permission) {
        // Assume thisActivity is the current activity
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    void requestPermission(final String permission) {
        // Should we show an explanation?
        /* 如果这个权限需要解释,就弹出一个自定义框来申请, 如果不需要 就直接申请 */
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请求照相权限")
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    })
                    .create()
                    .show();

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{permission}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

//    void checkAndRequest(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            //申请WRITE_EXTERNAL_STORAGE权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void toDetailPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }
}
