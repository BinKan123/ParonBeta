package com.peppypals.paronbeta;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.peppypals.paronbeta.Utils.HideKeyboard.hideSoftKeyboard;


public class LoginActivity extends AppCompatActivity {
    //firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;

    //facebook signin variables
    private CallbackManager callbackManager;
    private AccessToken facebookAccessToken;
    private LoginButton facebookLogin;
    private static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String PROFILE = "profile";
    private String fbName;
    private String fbEmail;

    //google signin varialbles
    private Button googleLogin;
    private static final int RC_SIGN_IN =1;
    private GoogleApiClient googleApiClient;
    private String gName;
    private String gMail;

    //email signup varialbles
    private Button registerBtn;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;

    private final String FILL_ALL_FIELDS = "Vänligen fyll i alla fälten";
    private final String EMAIL_FORMAT = "Fel email format";
    private final String EXIST_EMAIL = "Denna email är redan registerat";
    private final String UNSECURE_PASSWORD = "Detta lösenord är inte tillräckligt säkert";
    private TextView showError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView loginText = (TextView) findViewById(R.id.loginText);
        loginText.setText(Html.fromHtml(getString(R.string.login_page_text)));

        showError = (TextView) findViewById(R.id.showError);

        //firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, InfoActivity.class));
                }
            }
        };

        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        facebookLogin = (LoginButton) findViewById(R.id.fbBtn);

        initializeFacebookLogin();

        //google

        googleLogin = (Button) findViewById(R.id.gmailBtn);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "got an error", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                googleSignIn();
            }
        });


        //email
        firstNameInput = (EditText) findViewById(R.id.firstName);
        lastNameInput = (EditText) findViewById(R.id.lastName);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        registerBtn =(Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                emailSignUp();
            }
        });


        //dismiss keyboard
        dismissKeyboard(findViewById(R.id.parent));
    }

    // check if the user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        if (currentUser!=null){
            //change to nextActivity()
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }
    }

    //initialize facebook login button
    public void initializeFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions("email", "public_profile");
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess" + loginResult);
                facebookAccessToken = loginResult.getAccessToken();
                handleFacebookAccessToken(facebookAccessToken);
                Profile profile = Profile.getCurrentProfile();

                // Facebook Emailaddress
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    fbName = object.getString("name");

                                    fbEmail = object.getString("email");
                                    Log.v("Email = ", " " + fbEmail);
                                    Toast.makeText(getApplicationContext(), "Name: " + fbName+"& Email: "+ fbEmail, Toast.LENGTH_LONG).show();

                                    Map<String, Object> fbUser = new HashMap<>();
                                    fbUser.put("name", fbName);
                                    fbUser.put("email", fbEmail);
                                    firebaseFirestore.collection("users")
                                            .add(fbUser)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(LoginActivity.this, "Authentication failed with error" + e,
                                                           Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

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
                Toast.makeText(LoginActivity.this, "Google Authentication failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    //authenticate with Firebase using the Firebase credential
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


                        } else {
                            // If sign in fails, display a message to the user.

                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    //Google signin
    public void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        currentUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
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

            gName = acct.getDisplayName();
            gMail = acct.getEmail();

            Log.e(TAG, "Name: " + gName + ", email: " + gMail);
            Toast.makeText(getApplicationContext(), "Name: " + gName+"& Email: "+ gMail, Toast.LENGTH_LONG).show();


            Map<String, Object> gUser = new HashMap<>();
            gUser.put("name", gName);
            gUser.put("email", gMail);

                firebaseFirestore.collection("users")
                    .add(gUser)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(LoginActivity.this, "Authentication failed with error" + e,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "failed with error", Toast.LENGTH_LONG).show();

        }
    }

    public void emailSignUp() {
        final String firstName = firstNameInput.getText().toString().toLowerCase().trim();
        final String lastName = lastNameInput.getText().toString().toLowerCase().trim();
        final String email = emailInput.getText().toString().toLowerCase().trim();
        final String password = passwordInput.getText().toString().trim();

        //all fields should be filled
        if(!firstName.isEmpty()&& !lastName.isEmpty()&& !email.isEmpty()&& !password.isEmpty()) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                            /*FirebaseUser user = firebaseAuth.getCurrentUser();
                            mDatabase.child("users").child(user.getUid()).child("email").setValue(email);
                            mDatabase.child("users").child(user.getUid()).child("password").setValue(password);
                            mDatabase.child("users").child(user.getUid()).child("firstName").setValue(firstName);
                            mDatabase.child("users").child(user.getUid()).child("lastName").setValue(lastName);*/

                                // Create a new user with a first and last name
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstName", firstName);
                                user.put("lastName", lastName);
                                user.put("email", email);
                                user.put("password", password);

                                // Add a new document with a generated ID
                                firebaseFirestore.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                                Toast.makeText(LoginActivity.this, "Authentication failed with error" + e,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                               // showError.setText(task.getException().getMessage());
                                // If sign in fails, display a message to the user.
                                try {
                                    throw task.getException();
                                }
                                // if user enters a valid email.
                                catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Log.d(TAG, "onComplete: invalid_email");

                                    showError.setText(EMAIL_FORMAT);
                                }
                                // if user enters exist email.
                                catch (FirebaseAuthUserCollisionException existEmail) {
                                    Log.d(TAG, "onComplete: exist_email");

                                    showError.setText(EXIST_EMAIL);
                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthWeakPasswordException weakPassword)
                                {
                                    Log.d(TAG, "onComplete: weak_password");

                                    showError.setText(UNSECURE_PASSWORD);
                                }
                                catch (Exception e) {
                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            }
            else{
            showError.setText(FILL_ALL_FIELDS);
        }
    }


    public void dismissKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }
    }

    //ensure not back to inloged status
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EmailLoginActivity.class);
        startActivity(intent);

        return;
    }
}
