package es.victorcuena.queleregalo.activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import es.victorcuena.queleregalo.R;
import es.victorcuena.queleregalo.fragments.AmazonRecommendationsFragment;
import es.victorcuena.queleregalo.fragments.DialogAddPresentDiary;
import es.victorcuena.queleregalo.models.Present;
import es.victorcuena.queleregalo.utils.Routes;
import es.victorcuena.queleregalo.utils.VolleySingleton;
import es.victorcuena.queleregalo.views.DoubleCircleView;

import static es.victorcuena.queleregalo.fragments.RecyclerViewPresentsFragment.ID_EXTRA;
import static es.victorcuena.queleregalo.utils.Constants.OK_CODE;

public class DetailActivity extends AppCompatActivity {


    private LinearLayout errorLayout;
    private View contentDetail;

    private FloatingActionButton fab;
    private Toolbar toolbar;

    private TextView name;
    private DoubleCircleView priceImage;
    private ImageView genderImage;
    private TextView gender;
    private ImageView ageImage;
    private TextView age;
    private TextView description;

    private Present p = new Present();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();

        //Loading data remaining
        String id = extras.getString(ID_EXTRA);
        p.setId(String.valueOf(id));


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        contentDetail = findViewById(R.id.content_detail);

        priceImage = (DoubleCircleView) findViewById(R.id.detail_price_icon);
        name = (TextView) findViewById(R.id.detail_name);

        description = (TextView) findViewById(R.id.detail_description);

        genderImage = (ImageView) findViewById(R.id.detail_gender_icon);
        gender = (TextView) findViewById(R.id.detail_gender_text);
        ageImage = (ImageView) findViewById(R.id.detail_age_icon);
        age = (TextView) findViewById(R.id.detail_age_text);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogAddPresentDiary diag = DialogAddPresentDiary.newInstance(p);
                diag.show(getFragmentManager(), "dialog");


            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        errorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });


        load();

    }

    private void loadPresent(String id) {


        VolleySingleton.
                getInstance(getApplicationContext()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                Routes.PRESENT_RESOURCE + "/" + id,
                                (String) null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {


                                            int status = response.getInt("status");

                                            switch (status) {
                                                case OK_CODE:

                                                    JSONObject pObj = response.getJSONObject("data");

                                                    p = Present.fillPresent(pObj);

                                                    loadPresentImage(p.getUrl());
                                                    name.setText(p.getName());
                                                    toolbar.setTitle(p.getName());

                                                    description.setText(p.getDescription());

                                                    //Gender
                                                    int genderCode = p.getGender();
                                                    int priceCode = p.getFromPrice();
                                                    int ageCode = p.getAge();


                                                    gender.setText(Present.Gender.getStringValueFromGender(getApplicationContext(), genderCode));
                                                    genderImage.setImageDrawable(Present.Gender.getDrawableFromGenderCode(getApplicationContext(), genderCode));

                                                    age.setText(Present.Age.getStringValueFromAge(getApplicationContext(), ageCode));
                                                    ageImage.setImageDrawable(Present.Age.getDrawableFromAgeCode(getApplicationContext(), ageCode));

                                                    priceImage.setText(Present.Price.getStringValueFromPrice(getApplicationContext(), priceCode));
                                                    priceImage.setColor(Present.Price.getColorFromPrice(getApplicationContext(), priceCode));


                                                    contentDetail.setVisibility(View.VISIBLE);

                                                    //Load amazon results if it is not free

                                                    if (priceCode != 0) {

                                                        String price = "0";

                                                        if (p.getFromPrice() != 0)
                                                            price = Present.Price.getStringValueFromPrice(getApplicationContext(), p.getFromPrice());

                                                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                                        ft.replace(R.id.amazon_layout, AmazonRecommendationsFragment.newInstance(p.getName(), price));
                                                        ft.commit();
                                                    }

                                                    break;
                                                default:

                                                    errorLayout.setVisibility(View.VISIBLE);
                                                    contentDetail.setVisibility(View.GONE);

                                            }

                                        } catch (JSONException e) {

                                            Log.e("Error JSON", "Error");
                                            e.printStackTrace();
                                        }

                                        findViewById(R.id.loading).setVisibility(View.GONE);

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("VOLLEY", "Error Volley: " + error.getMessage());

                                        errorLayout.setVisibility(View.VISIBLE);
                                        contentDetail.setVisibility(View.GONE);
                                        findViewById(R.id.loading).setVisibility(View.GONE);
                                    }
                                }

                        )
                );

    }

    private void loadPresentImage(String url) {

        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        //work with the palette here

                        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

                        int colorScrim = palette.getVibrantColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryMen));
                        int colorFab = palette.getLightMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                        collapsingToolbarLayout.setContentScrimColor(colorScrim);

                        fab.setBackgroundTintList(ColorStateList.valueOf(colorFab));

                        ImageView img = (ImageView) findViewById(R.id.detail_image);

                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                            img.setBackground(bitmapDrawable);
                        else
                            img.setBackgroundDrawable(bitmapDrawable);

                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });

    }

    private void load() {

        errorLayout.setVisibility(View.GONE);
        contentDetail.setVisibility(View.GONE);
        loadPresent(p.getId());

    }


}
