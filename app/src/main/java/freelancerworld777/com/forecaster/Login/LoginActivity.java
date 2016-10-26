package freelancerworld777.com.forecaster.Login;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import freelancerworld777.com.forecaster.Home.MainActivity;
import freelancerworld777.com.forecaster.R;
import freelancerworld777.com.forecaster.utils.Constants;
import freelancerworld777.com.forecaster.utils.PermissionHelper;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    Button guest_button;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean isLocationPermsionGranted = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        guest_button = (Button)findViewById(R.id.guest_button);
        guest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionHelper.requestLocationAccess(LoginActivity.this)) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(Constants.LOCATION_UPDATION_KEY ,"key");
                    startActivity(i);
                    finish();
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if(PermissionHelper.requestLocationAccess(LoginActivity.this)) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra(Constants.LOCATION_UPDATION_KEY ,"key");
                startActivity(i);
                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

            if (permissions.length==2 && permissions[0].equalsIgnoreCase(PermissionHelper.LOCATION_FINE) ||
                    permissions[1].equalsIgnoreCase(PermissionHelper.LOCATION_COARSE)) {


                for (int result : grantResults) {
                    if (result != PermissionHelper.PERMISSION_GRANTED) {
                        isLocationPermsionGranted = false;

                        break;
                    }
                }
                if (isLocationPermsionGranted)
                {
                    Intent locationTrack = new Intent(this, MainActivity.class);
                    locationTrack.putExtra(Constants.LOCATION_UPDATION_KEY ,"key");
                    startActivity(locationTrack);
                }
                else {
                    Intent locationTrack = new Intent(this, MainActivity.class);
                    locationTrack.putExtra(Constants.LOCATION_UPDATION_KEY ,"default");
                    startActivity(locationTrack);
                }
            }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
