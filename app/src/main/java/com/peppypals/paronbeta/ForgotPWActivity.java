package com.peppypals.paronbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import static com.peppypals.paronbeta.Utils.HideKeyboard.hideSoftKeyboard;

public class ForgotPWActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPWActivity";
    private EditText emailForPW;
    private final String EMAIL_HINT = "EMAIL";
    private FirebaseAuth mAuth;

    private final String INVALID_EMAIL = "Denna email är inte registrerat";
    private final String FILL_ERROR = "Email fälten bör fyllas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        TextView forgotPWText = (TextView) findViewById(R.id.forgotPWText);
        forgotPWText.setText(Html.fromHtml(getString(R.string.forgotPWInfo)));

        TextView enterEmailInfo = (TextView) findViewById(R.id.enterEmailText);
        enterEmailInfo.setText(Html.fromHtml(getString(R.string.enterEmailInfo)));

        final EditText emailForPW = (EditText) findViewById(R.id.emailAdd);

        //show email if already exists in EmailLoginActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String emailValue = getIntent().getExtras().getString("emailInput");
            emailForPW.setText(emailValue);
        }

        Button sendNewPWBtn = (Button) findViewById(R.id.newPWBtn);
        sendNewPWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailForPW.getText().toString().toLowerCase().trim();
                final TextView emailMsg = (TextView) findViewById(R.id.emailMsg);
                mAuth = FirebaseAuth.getInstance();
                if (!email.isEmpty()) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        emailForPW.setHint(EMAIL_HINT);
                                        Intent intent = new Intent(getApplication(), EmailSentActivity.class);
                                        startActivity(intent);
                                    }else {

                                    try {
                                        throw task.getException();
                                    }
                                    // if user enters a valid email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail) {
                                        Log.d(TAG, "onComplete: invalid_email");

                                        emailMsg.setText(INVALID_EMAIL);
                                    }
                                    catch (Exception e) {
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                    }
                                }
                            });
                } else {
                    emailMsg.setText(FILL_ERROR);
                }
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), EmailLoginActivity.class);
                startActivity(intent);
            }
        });

        //dismiss keyboard
        dismissKeyboard(findViewById(R.id.forgotPWLayout));

    }

    public void dismissKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ForgotPWActivity.this);
                    return false;
                }
            });
        }
    }
}
