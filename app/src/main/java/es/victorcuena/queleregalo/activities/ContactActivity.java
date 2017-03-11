package es.victorcuena.queleregalo.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.utils.OnImageFindTouchListener;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView linkedinIcon = (ImageView) findViewById(R.id.linkedin_icon);
        TextView text2 = (TextView) findViewById(R.id.who_am_I_text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        //Toolbar management
        toolbar.setTitle(getString(R.string.contact_title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setting typefaces
        text2.setTypeface(Typeface.createFromAsset(getAssets(), getString(R.string.font_roboto_thin)));



        linkedinIcon.setOnTouchListener(new OnImageFindTouchListener(getApplicationContext()));
        linkedinIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Redirect to my website in linkedin
                Uri uri = Uri.parse(getString(R.string.contact_linkedin_link));
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On click start intent to send me an email
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.contact_my_email), null));
                startActivity(Intent.createChooser(intent, getString(R.string.contact_choose_client)));


            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
