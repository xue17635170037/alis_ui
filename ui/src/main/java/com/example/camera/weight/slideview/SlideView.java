package com.example.camera.weight.slideview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.camera.R;

import static com.example.camera.weight.slideview.SlideUtil.spToPx;



/**
 * @author Kizito Nwose
 */

public class SlideView extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {

    protected Slider slider;
    protected Drawable slideBackground;
    protected Drawable buttonBackground;
    protected Drawable buttonImage;
    protected Drawable buttonImageDisabled;
    protected TextView slideTextView;
    protected LayerDrawable buttonLayers;
    protected ColorStateList slideBackgroundColor;
    protected ColorStateList buttonBackgroundColor;
    protected boolean animateSlideText;
    /**
     * 滑动监听百分比
     */
    protected int slideCompletePercent = 70;
    /**
     * 滑动按钮在滑动后是否归位(默认归位)
     */
    protected boolean isReset = true;
    protected String slideText;
    protected String slideCompleteText = "完成";

    public SlideView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }
    @SuppressWarnings("ResourceType")
    void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.widget_slide_view, this);
        if (getBackground() == null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_slide_view));
            } else {
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_slide_view));
            }
        }
        slideTextView = (TextView) findViewById(R.id.slideText);
        slider = (Slider) findViewById(R.id.slider);
        slider.setOnSeekBarChangeListener(this);
        slideBackground = getBackground();
        buttonLayers = (LayerDrawable) slider.getThumb();
        buttonBackground = buttonLayers.findDrawableByLayerId(R.id.buttonBackground);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SlideView,
                defStyle, defStyle);

        int strokeColor;
        float slideTextSize = spToPx(16, getContext());
        boolean reverseSlide;
        ColorStateList sliderTextColor;
        try {
            animateSlideText = a.getBoolean(R.styleable.SlideView_animateSlideText, true);
            reverseSlide = a.getBoolean(R.styleable.SlideView_reverseSlide, false);
            strokeColor = a.getColor(R.styleable.SlideView_strokeColor, ContextCompat.
                    getColor(getContext(), R.color.color_default_slide_stroke));


            slideText = a.getString(R.styleable.SlideView_slideText);
            sliderTextColor = a.getColorStateList(R.styleable.SlideView_slideTextColor);

            slideTextSize = a.getDimension(R.styleable.SlideView_slideTextSize, slideTextSize);
            slideTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, slideTextSize);

            setText(slideText);
            setTextColor(sliderTextColor == null ? slideTextView.getTextColors() : sliderTextColor);

            int buttonImageId = a.getResourceId(R.styleable.SlideView_buttonImage, R.drawable.ic_slide_btn_arrow);
            setButtonImage(ContextCompat.getDrawable(getContext(), buttonImageId));
            setButtonImageDisabled(ContextCompat.getDrawable(getContext(), a.getResourceId
                    (R.styleable.SlideView_buttonImageDisabled, buttonImageId)));

            setButtonBackgroundColor(a.getColorStateList(R.styleable.SlideView_buttonBackgroundColor));
            setSlideBackgroundColor(a.getColorStateList(R.styleable.SlideView_slideBackgroundColor));

            if (a.hasValue(R.styleable.SlideView_strokeColor)) {
                SlideUtil.setDrawableStroke(slideBackground, strokeColor);
            }
            if (reverseSlide) {
                slider.setRotation(180);
                LayoutParams params = ((LayoutParams) slideTextView.getLayoutParams());
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                }
                slideTextView.setLayoutParams(params);
            }
        } finally {
            a.recycle();
        }
    }

    public boolean isReset() {
        return isReset;
    }

    public SlideView setReset(boolean reset) {
        isReset = reset;
        if (reset){
            slider.setEnabled(true);
            slider.setProgress(0);
        }
        return this;
    }

    public int getSlideCompletePercent() {
        return slideCompletePercent;
    }

    public SlideView setSlideCompletePercent(int slideCompletePercent) {
        this.slideCompletePercent = slideCompletePercent;
        return this;
    }

    public String getSlideCompleteText() {
        return slideCompleteText;
    }

    public SlideView setSlideCompleteText(String slideCompleteText) {
        this.slideCompleteText = slideCompleteText;
        return this;
    }

    public SlideView setTextColor(@ColorInt int color) {
        slideTextView.setTextColor(color);
        return this;
    }

    public SlideView setTextColor(ColorStateList colors) {
        slideTextView.setTextColor(colors);
        return this;
    }

    public SlideView setText(CharSequence text) {
        slideTextView.setText(text);
        this.slideText = (String) text;
        return this;
    }

    public String getText(){
        return slideText;
    }

    public SlideView setTextSize(int size) {
        slideTextView.setTextSize(size);
        return this;
    }

    public TextView getTextView() {
        return slideTextView;
    }

    public SlideView setButtonImage(Drawable image) {
        buttonImage = image;
        buttonLayers.setDrawableByLayerId(R.id.buttonImage, image);
        return this;
    }

    public SlideView setButtonImageDisabled(Drawable image) {
        buttonImageDisabled = image;
        return this;
    }


    public SlideView setButtonBackgroundColor(ColorStateList color) {
        if (color != null) {
            buttonBackgroundColor = color;
            SlideUtil.setDrawableColor(buttonBackground, color.getDefaultColor());
        }
        return this;
    }


    public SlideView setSlideBackgroundColor(ColorStateList color) {
        if (color != null) {
            slideBackgroundColor = color;
            SlideUtil.setDrawableColor(slideBackground, color.getDefaultColor());
        }
        return this;
    }

    public Slider getSlider() {
        return slider;
    }

    public void setOnSlideCompleteListener(OnSlideCompleteListener listener) {
        slider.setOnSlideCompleteListenerInternal(listener, this);
    }


    public interface OnSlideCompleteListener {
        void onSlideComplete(SlideView slideView);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
        buttonLayers.setDrawableByLayerId(R.id.buttonImage, enabled ? buttonImage :
                buttonImageDisabled == null ? buttonImage : buttonImageDisabled);
        SlideUtil.setDrawableColor(buttonBackground, buttonBackgroundColor.getColorForState(
                enabled ? new int[]{android.R.attr.state_enabled} : new int[]{-android.R.attr.state_enabled}
                , ContextCompat.getColor(getContext(), R.color.color_default_slide_button)));
        SlideUtil.setDrawableColor(slideBackground, slideBackgroundColor.getColorForState(
                enabled ? new int[]{android.R.attr.state_enabled} : new int[]{-android.R.attr.state_enabled}
                , ContextCompat.getColor(getContext(), R.color.color_default_slide_button)));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (progress > slideCompletePercent) {
            if (!TextUtils.isEmpty(slideCompleteText))
                slideTextView.setText(slideCompleteText);
            if (!isReset) {
                slider.setAlpha(1 - (progress / 100f));
            } else {
                if (animateSlideText) {
                    slideTextView.setAlpha(1 - (progress / 100f));
                }
            }
        } else {
            if (isReset){
                if (animateSlideText) {
                    slideTextView.setAlpha(1 - (progress / 100f));
                }
            }
            slider.setAlpha(1 - (progress / 100f));
            slideTextView.setText(slideText);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
