package com.example.demo.ui.discuss;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

public class HtmlTextView extends androidx.appcompat.widget.AppCompatTextView {


    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setHtmlFromString(String html, boolean useLocalDrawables) {
        Html.ImageGetter imgGetter;
        if (useLocalDrawables) {
            imgGetter = new LocalImageGetter(getContext());
        } else {
            imgGetter = new UrlImageGetter(this, getContext());
        }
        setText(Html.fromHtml(html, imgGetter, new HtmlTagHandler()));

        setMovementMethod(LinkMovementMethod.getInstance());


    }

}
