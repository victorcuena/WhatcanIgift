package es.victorcuena.queleregalo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import es.victorcuena.queleregalo.R;

/**
 * Created by victorcuenagarcia on 04/02/2017.
 */

public class DoubleCircleView extends TextView {


    private Paint paint;
    private int color;

    public DoubleCircleView(Context context) {
        super(context);
        init(context, null);
    }

    public DoubleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);

        final String xmlns = "http://schemas.android.com/apk/res/android";
        int background = attrs.getAttributeResourceValue(xmlns, "backgroundTint", -1);
        if (background != -1)
            color = ContextCompat.getColor(getContext(), background);


    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int h = this.getHeight();
        int w = this.getWidth();
        float strokeWidth = 2f;

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;


        this.setHeight(diameter);
        this.setWidth(diameter);

        // smooths
        paint.setAntiAlias(true);
        paint.setColor(color);

        //Paint the external circle
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(radius, radius, radius - strokeWidth, paint);

        //Paint the internall circle
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(radius, radius, (radius - strokeWidth) * 0.95f, paint);



        //Paint the text
        paint = new Paint();
        paint.setColor(Color.WHITE);


        String text = getText().toString();

        if(text.length() > 4)
            paint.setTextSize(getResources().getDimension(R.dimen.heading_2));
        else
            paint.setTextSize(getResources().getDimension(R.dimen.heading));


        Rect bounds = new Rect();
        paint.getTextBounds(getText().toString(), 0, getText().length(), bounds);
        int x = (canvas.getWidth() / 2) - (bounds.width() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(getText().toString(), x, yPos, paint);
        //Paint the coin


        paint.setColor(Color.RED);
        canvas.drawRoundRect(new RectF(1, 2, 3, 4), 0, 0, paint);


    }


}
