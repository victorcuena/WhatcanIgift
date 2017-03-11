package es.victorcuena.queleregalo.models;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import es.victorcuena.queleregalo.R;


public class Present implements Serializable {

    private String id;
    private String name;
    private String description;
    private String url;
    private int gender;
    private int age;
    private int fromPrice;

    private BitmapDrawable imgLoaded;

    public Present() {

    }

    public Present(String nId, String nName, String nDescription, int nPrice, String pic) {
        this.id = nId;
        this.name = nName;
        this.description = nDescription;
        this.fromPrice = nPrice;
        this.url = pic;
    }

    public static Present fillPresent(JSONObject pObj) {

        Present p = new Present();


        try {

            p.setId(pObj.getInt("id_present") + "");
            p.setName(pObj.getString("name"));
            p.setUrl(pObj.getString("imageUrl"));
            p.setDescription(pObj.getString("description"));
            p.setGender(pObj.getInt("id_gender"));
            p.setAge(pObj.getInt("id_age"));
            p.setFromPrice(pObj.getInt("id_price"));

        } catch (JSONException e) {
            e.printStackTrace();
            p = null;
        }
        return p;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

        if (age > 4)
            age = 4;

        this.age = age;
    }

    public int getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(int fromPrice) {

        if (fromPrice > 4)
            fromPrice = 1;

        this.fromPrice = fromPrice;
    }

    @Override
    public String toString() {
        return "Present{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", fromPrice=" + fromPrice +
                '}';
    }

    public static class Age {

        public static final int PRESCHOOL = 0;
        public static final int CHILDREN = 1;
        public static final int TEENAGE = 2;
        public static final int ADULT = 3;
        public static final int ALL = 4;


        /**
         * Returns the string value of the age specified by parameter
         */
        public static String getStringValueFromAge(Context context, int age) {

            String res = null;

            switch (age) {
                case PRESCHOOL:
                    res = context.getString(R.string.age_preschool);
                    break;
                case CHILDREN:
                    res = context.getString(R.string.age_children);
                    break;
                case TEENAGE:
                    res = context.getString(R.string.age_teenage);
                    break;
                case ADULT:
                    res = context.getString(R.string.age_adult);
                    break;
                default:
                    res = context.getString(R.string.age_all);
                    break;


            }

            return res;

        }

        public static Drawable getDrawableFromAgeCode(Context context, int ageCode) {

            Drawable res;

            switch (ageCode) {
                case PRESCHOOL:
                    res = ContextCompat.getDrawable(context, R.drawable.age_preschool);
                    break;
                case CHILDREN:
                    res = ContextCompat.getDrawable(context, R.drawable.age_children);
                    break;
                case TEENAGE:
                    res = ContextCompat.getDrawable(context, R.drawable.age_teen);
                    break;
                case ADULT:
                    res = ContextCompat.getDrawable(context, R.drawable.age_adult);
                    break;
                default:
                    res = ContextCompat.getDrawable(context, R.drawable.age_all);
                    break;

            }


            return res;

        }


    }

    public static class Price {

        public static final int FREE = 0;
        public static final int FROM_10 = 1;
        public static final int FROM_20 = 2;
        public static final int FROM_50 = 3;
        public static final int FROM_100 = 4;


        /**
         * Returns the string value of the age specified by parameter
         */
        public static String getStringValueFromPrice(Context context, int price) {

            String res = null;

            switch (price) {
                case FREE:
                    res = context.getString(R.string.price_free);
                    break;
                default:
                    res = context.getString(R.string.price_from_10);
                    break;
                case FROM_20:
                    res = context.getString(R.string.price_from_20);
                    break;
                case FROM_50:
                    res = context.getString(R.string.price_from_50);
                    break;
                case FROM_100:
                    res = context.getString(R.string.price_from_100);
                    break;

            }

            return res;

        }

        public static int getColorFromPrice(Context context, int price) {

            int res = -1;

            switch (price) {
                case FREE:
                    res = ContextCompat.getColor(context, R.color.price_free);
                    break;
                default:
                    res = ContextCompat.getColor(context, R.color.price_10);
                    break;
                case FROM_20:
                    res = ContextCompat.getColor(context, R.color.price_20);
                    break;
                case FROM_50:
                    res = ContextCompat.getColor(context, R.color.price_50);
                    break;
                case FROM_100:
                    res = ContextCompat.getColor(context, R.color.price_100);
                    break;

            }

            return res;

        }

    }

    public static class Gender {

        public static final int MEN = 0;
        public static final int WOMEN = 1;
        public static final int UNISEX = 2;

        /**
         * Returns the string value of the gender specified by parameter
         */
        public static String getStringValueFromGender(Context context, int gender) {

            String res = null;

            switch (gender) {
                case MEN:
                    res = context.getString(R.string.gender_men);
                    break;
                case WOMEN:
                    res = context.getString(R.string.gender_women);
                    break;
                case UNISEX:
                    res = context.getString(R.string.gender_unisex);
                    break;

            }

            return res;

        }


        public static Drawable getDrawableFromGenderCode(Context context, int genderCode) {

            Drawable res = null;

            switch (genderCode) {
                case MEN:
                    res = ContextCompat.getDrawable(context, R.drawable.men_1);
                    break;
                case WOMEN:
                    res = ContextCompat.getDrawable(context, R.drawable.women_1);
                    break;
                case UNISEX:
                    res = ContextCompat.getDrawable(context, R.drawable.unisex);
                    break;

            }

            return res;

        }


    }


}
