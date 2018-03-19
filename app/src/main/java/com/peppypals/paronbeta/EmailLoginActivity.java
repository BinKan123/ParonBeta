package com.peppypals.paronbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.peppypals.paronbeta.Utils.HideKeyboard.hideSoftKeyboard;

public class EmailLoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailLoginActivity";

    //email signup varialbles
    private Button loginBtn;
    private EditText emailText;
    private EditText passwordText;
    private TextView forgotPW;
    private TextView emailErrorMsg;
    private TextView passwordErrorMsg;
    private TextView showErrorMsg;
    private final String INVALID_EMAIL = "Ogiltig email";
    private final String WRONG_PASSWORD = "Fel lösenord";
    private final String FILL_BOTH_ERROR = "Båda fälten bör fyllas";

    //facebook signin variables
    private CallbackManager callbackManager;
    private AccessToken facebookAccessToken;
    private LoginButton loginWithFB;
    private static final String EMAIL = "email";

    //google signin varialbles
    private Button loginWithGmail;
    private static final int RC_SIGN_IN =1;
    private GoogleApiClient googleApiClient;

    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        TextView forgotPWText = (TextView) findViewById(R.id.forgotPw);
        forgotPWText.setText(Html.fromHtml(getString(R.string.login_forgotPW)));

        TextView ellerText = (TextView) findViewById(R.id.eller);
        ellerText.setText(Html.fromHtml(getString(R.string.login_eller)));


        TextView registerText = (TextView) findViewById(R.id.registerHere);

        SpannableString spannableString =
                new SpannableString(getString(R.string.login_register_text));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 20, spannableString.length(), 0);
        registerText.setMovementMethod(LinkMovementMethod.getInstance());
        registerText.setText(spannableString);

        //email
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        loginBtn = (Button) findViewById(R.id.emailLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailSignIn();
            }
        });
        forgotPW = (TextView) findViewById(R.id.forgotPw);
        forgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLoginActivity.this, ForgotPWActivity.class);
                intent.putExtra("emailInput", emailText.getText().toString().toLowerCase().trim());
                startActivity(intent);;
            }
        });

        //dismiss keyboard
        dismissKeyboard(findViewById(R.id.emailLayout));

        //firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser != null) {
                    startActivity(new Intent(getBaseContext(), InfoActivity.class));
                }
            }
        };

        //facebook
        Button fblog = (Button) findViewById(R.id.loginWithFB);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginWithFB = (LoginButton) findViewById(R.id.loginWithFB);

        initializeFacebookLogin();

        //google
        loginWithGmail = (Button) findViewById(R.id.loginWithGmail);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(EmailLoginActivity.this, "got an error", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginWithGmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                googleSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseAuth.addAuthStateListener(mAuthListener);
        if (currentUser!=null){
            //change to nextActivity()
            Intent intent = new Intent(getApplication(), InfoActivity.class);
            startActivity(intent);
    }
    }

    //for both google & facebook sign-in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the GOOGLE SDK

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                handleSignInResult(result);
            } else {
                Toast.makeText(EmailLoginActivity.this, "Google Authentication failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    //email sign-in
    public void emailSignIn() {
        final String email = emailText.getText().toString().toLowerCase().trim();
        final String password = passwordText.getText().toString().trim();
        emailErrorMsg = (TextView) findViewById(R.id.emailError);
        passwordErrorMsg = (TextView) findViewById(R.id.pwError);
        showErrorMsg = (TextView) findViewById(R.id.fillBoth);

        //Both fields should be filled
        if (!email.isEmpty() && !password.isEmpty()) {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {  // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                Intent intent = new Intent(getApplication(), InfoActivity.class);
                                startActivity(intent);

                            } else {
                                try {
                                    throw task.getException();
                                }
                                // if user enters a valid email.
                                catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Log.d(TAG, "onComplete: invalid_email");

                                    emailErrorMsg.setText(INVALID_EMAIL);
                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    Log.d(TAG, "onComplete: wrong_password");

                                    passwordErrorMsg.setText(WRONG_PASSWORD);
                                } catch (Exception e) {
                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                }
                            }
                        }
                    });
            }
            else {
            showErrorMsg.setText(FILL_BOTH_ERROR);
        }
    }

    //Google signin
    public void googleSignIn(){
        Intent signInIntent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent(getApplication(), InfoActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(EmailLoginActivity.this, "Google sign-in failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());

        } else {
            Toast.makeText(getApplicationContext(), "failed with error", Toast.LENGTH_LONG).show();
        }
    }

    //facebook sign-in
    public void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginWithFB.setReadPermissions("email", "public_profile");
        loginWithFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess" + loginResult);
                facebookAccessToken = loginResult.getAccessToken();
                handleFacebookAccessToken(facebookAccessToken);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent = new Intent(getApplication(), InfoActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(EmailLoginActivity.this, "Facebook sign-in failed.",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void dismissKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(EmailLoginActivity.this);
                    return false;
                }
            });
        }
    }

    //ensure not back to inloged status
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        return;
    }
}
