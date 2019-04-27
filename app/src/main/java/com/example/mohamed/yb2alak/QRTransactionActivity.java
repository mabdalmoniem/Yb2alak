package com.example.mohamed.yb2alak;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mohamed.yb2alak.api.Yb2alkApiClient;
import com.example.mohamed.yb2alak.api.Yb2alkApiInterface;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.api.mapping.user.models.UserQR;
import com.example.mohamed.yb2alak.api.mapping.user.requests.QueryQRRequest;
import com.example.mohamed.yb2alak.api.mapping.user.responses.QueryQRResponse;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class QRTransactionActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    User user;
    Yb2alkApiInterface yb2alkAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(QRTransactionActivity.this);
        user = LocalStorageHelper.getAuthUser(QRTransactionActivity.this);
        if (user.getToken().isEmpty() || user.getToken() == null || user.getToken().length() == 0 || !isLoggedIn) {
            Intent i = new Intent(QRTransactionActivity.this, LoginActivity.class);
            startActivity(i);
            return;
        }

        scannerView = new ZXingScannerView(this);
//        setContentView(R.layout.activity_qr_transaction);
        setContentView(scannerView);

        yb2alkAPI = Yb2alkApiClient.getClient().create(Yb2alkApiInterface.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission()) {
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions();
            }
        }
    }

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText();

        Call<QueryQRResponse> call = yb2alkAPI.queryQR("bearer " + user.getToken(), new QueryQRRequest(scanResult));
        call.enqueue(new Callback<QueryQRResponse>() {
            @Override
            public void onResponse(Call<QueryQRResponse> call, Response<QueryQRResponse> response) {
                switch (response.code()) {
                    case 200:
                        UserQR userQR = response.body().getUserQR();
                        Log.e("user =>>", userQR.getEmail());
                        Intent i = new Intent(QRTransactionActivity.this, AddTransactionActivity.class);
                        i.putExtra("userQR", userQR);
                        startActivity(i);
                        break;

                    default:
                        displayQRResultError("User isn't found");
                        Log.e("user :(( ", "user not found!!");
                        break;
                }
            }

            @Override
            public void onFailure(Call<QueryQRResponse> call, Throwable t) {
                displayQRResultError("Couldn't send request");
                Log.e("QR onFailure :(( ", t.getMessage());
            }
        });
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(QRTransactionActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(QRTransactionActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(QRTransactionActivity.this, "Permission is denied", Toast.LENGTH_LONG).show();

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow access for permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[] {CAMERA}, REQUEST_CAMERA);
                                        }
                                    }
                                });

                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(QRTransactionActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }

                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        scannerView.stopCamera();
    }

    public void displayQRResultError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(QRTransactionActivity.this);
            }
        });

        builder.setMessage(message);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
