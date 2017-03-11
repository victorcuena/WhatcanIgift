package es.victorcuena.queleregalo.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


/**
 * Created by victorcuenagarcia on 02/02/2017.
 */

public class Utils {


    public static int colorWithAlpha(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int alpha = Math.round(percent * 255);

        return Color.argb(alpha, r, g, b);
    }


    public static TransitionDrawable transitionToImage(ImageView imgSrc, Drawable dst) {

        Drawable backgrounds[] = new Drawable[2];
        backgrounds[0] = imgSrc.getDrawable();
        backgrounds[1] = dst;

        TransitionDrawable trans = new TransitionDrawable(backgrounds);
        trans.setCrossFadeEnabled(true);//Otherwise with less alpha we can see both images overlap

        imgSrc.setImageDrawable(trans);

        return trans;

    }


    public static ValueAnimator colorTransition(int from, int to, long duration, ValueAnimator.AnimatorUpdateListener callback) {


        ValueAnimator anim = ValueAnimator.ofInt(from, to);


        anim.setDuration(duration);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setInterpolator(new DecelerateInterpolator(2));
        anim.addUpdateListener(callback);

        return anim;
    }


    public static String firstLetterToUpperCase(String text) {


        //not empty
        if (text.length() != 0) {

            //Get first letter
            Character c = text.charAt(0);

            if (c != null && Character.isLowerCase(c)) {
                c = Character.toUpperCase(c);
                text = c + text.substring(1);
            }
        }

        return text;
    }


}