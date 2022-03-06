package info.devexchanges.googledrive;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

public class CreateFolderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "CreateFolderActivity";
    private GoogleApiClient googleApiClient;
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this, this,this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .build();
        }
        if (!googleApiClient.isConnecting() && !googleApiClient.isConnected())
        {
            googleApiClient.connect();
        }
    }

    /**
     * Handles resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            googleApiClient.connect();
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected", "===================onConnected==========");
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("New folder").build();
        DriveFolder test = Drive.DriveApi.getRootFolder(googleApiClient);
        test.listChildren(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        int e = connectionResult.getErrorCode();
        Log.d("errcode==", String.valueOf(e));
        Log.d("onConnectionFailed", connectionResult.getErrorMessage()  != null ? connectionResult.getErrorMessage() : "failed....");

        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());
        if (!connectionResult.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }
        try {
            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e2) {
            Log.e(TAG, "Exception while starting resolution activity", e2);
        }
    }

    @Override
    protected void onPause() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this, this,this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .build();
        }
        if (!googleApiClient.isConnecting() && !googleApiClient.isConnected())
        {
            googleApiClient.connect();
        }
    }
}
