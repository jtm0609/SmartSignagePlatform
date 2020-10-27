package com.jtmcompany.smartadvertisingboard.VideoEdit.VO;

import android.graphics.drawable.Drawable;

public class EditorMenu_VO {
    private Drawable image;
    private String text;

    public EditorMenu_VO(Drawable image, String text) {
        this.image = image;
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
