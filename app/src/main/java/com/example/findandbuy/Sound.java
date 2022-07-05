package com.example.findandbuy;

import android.media.MediaPlayer;

import androidx.fragment.app.FragmentActivity;

public class Sound {
    private String title;
    private MediaPlayer source;

    public Sound(String title, MediaPlayer source) {
        this.title = title;
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MediaPlayer getSource() {
        return source;
    }

    public void setSource(MediaPlayer source) {
        this.source = source;
    }
}
