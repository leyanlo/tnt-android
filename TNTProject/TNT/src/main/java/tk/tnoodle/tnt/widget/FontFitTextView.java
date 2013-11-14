package tk.tnoodle.tnt.widget;

// from http://stackoverflow.com/a/7875656/759772

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

@SuppressWarnings("unused")
public class FontFitTextView extends TextView {

    public FontFitTextView(Context context) {
        this(context, null);
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
        mMaxSize = getTextSize();

        TypedArray a = context.obtainStyledAttributes(attrs, new int[] { android.R.attr.maxLines });
        mLineCount = a.getInt(0, 1);
    }

    /* Re size the font so the specified text fits in the text box
    * assuming the text box is the specified width.
    */
    private void refitText(String text, int textWidth, int textHeight)
    {
        if (textWidth <= 0 || textHeight <= 0)
            return;
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        int targetHeight = textHeight - this.getPaddingTop() - this.getPaddingBottom();
        float hi = mMaxSize;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            mTestPaint.setTextSize(size);
            if(mTestPaint.measureText(text) >= targetWidth * mLineCount ||
                    mTestPaint.getFontSpacing() * mLineCount >= targetHeight)
                hi = size; // too big
            else
                lo = size; // too small
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, Math.round(lo));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth, parentHeight);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w, h);
        }
    }

    //Attributes
    private Paint mTestPaint;
    private float mMaxSize;
    private int mLineCount;
}
