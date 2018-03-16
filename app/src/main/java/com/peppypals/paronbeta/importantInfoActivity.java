package com.peppypals.paronbeta;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.peppypals.paronbeta.CategoryList.CategoryActivity;


public class ImportantInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_info);

        TextView importantText = (TextView) findViewById(R.id.importantText);
        importantText.setText(Html.fromHtml(getString(R.string.importantInfo)));

        //set first word to pink
        SpannableString spannableString = new SpannableString(Html.fromHtml(getString(R.string.importantInfo)));
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        spannableString.setSpan(foregroundSpan, 0,8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        importantText.setText(spannableString);

        Button understandBtn = (Button)findViewById(R.id.understandBtn);
        understandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImportantInfoActivity.this, CategoryActivity.class);
                startActivity(intent);

            }
        });

        Button outBtn = (Button) findViewById(R.id.outBtn);
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ImportantInfoActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });



    }

}
