package es.victorcuena.queleregalo.utils;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import es.victorcuena.queleregalo.R;

/**
 * Created by victorcuenagarcia on 12/02/2017.
 */

public class OnImageFindTouchListener implements View.OnTouchListener {

    private Context context;

    public OnImageFindTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().translationZ(context.getResources().getDimensionPixelSize(R.dimen.translation_z_imageview_find));
                    break;
                case MotionEvent.ACTION_UP:
                    v.animate().translationZ(0);
                    break;

            }
        }


        return false;
    }
}
