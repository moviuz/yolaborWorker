package mx.com.omnius.yolabor.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import mx.com.omnius.yolabor.R;



public class MyFontEdittextView extends EditText {

    private static final String TAG = "TextView";

    private Typeface typeface;

    public MyFontEdittextView(Context context) {
        super(context);
    }

    public MyFontEdittextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHintTextColor(getResources().getColor(R.color.color_hint_light));
        setCustomFont(context, attrs);
    }

    public MyFontEdittextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.app);
        String customFont = a.getString(R.styleable.app_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    private boolean setCustomFont(Context ctx, String asset) {
        try {
            if (typeface == null) {
                // Log.i(TAG, "asset:: " + "fonts/" + asset);
                typeface = Typeface.createFromAsset(ctx.getAssets(),
                        "fonts/OPENSANS-REGULAR.ttf");
//				typeface = Typeface.createFromAsset(ctx.getAssets(),
//						"fonts/SIFONN_BASIC.otf");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }

}