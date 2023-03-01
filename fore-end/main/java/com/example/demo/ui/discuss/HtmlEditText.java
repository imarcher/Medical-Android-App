package com.example.demo.ui.discuss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.demo.R;

import java.util.List;


public class HtmlEditText extends LinearLayout {


    public HtmlEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public HtmlEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public HtmlEditText(Context context) {
        super(context);
        init(context);
    }

    private Context context;
    private EditText mycontent;
    private int width;

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.myedit, this);
        mycontent= (EditText) this.findViewById(R.id.mycontent);

        ViewTreeObserver vt =mycontent.getViewTreeObserver();
        vt.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = mycontent.getWidth();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public EditText getEditText() {
        return mycontent;
    }

    public void setUploadPath(HtmlFile htmlFile) {
        insertImageSpan(htmlFile);
    }

    public void setUploadPaths(List<HtmlFile> fileList) {
        for (int i = 0; i < fileList.size(); i++) {
            insertImageSpan(fileList.get(i));
        }
    }
    private void insertImageSpan(HtmlFile htmlFile) {

        Bitmap loadedImage = BitmapFactory.decodeFile(htmlFile.getLocalPath());
        float scaleWidth = ((float) width) / (loadedImage.getWidth()*1.5f);

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix,
                true);
        ImageSpan imageSpan = new ImageSpan(context, loadedImage);
        String tempUrl = "<img src=\"" + htmlFile.getUrlPath() + "\" />";
        SpannableString spannableString = new SpannableString(tempUrl);
        spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int index = mycontent.getSelectionStart();
        Editable edit_text = mycontent.getEditableText();
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {
            edit_text.insert(index, spannableString);
        }
        edit_text.insert(index + spannableString.length(), "\n");
    }

}
