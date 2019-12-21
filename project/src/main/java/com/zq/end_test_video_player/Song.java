package com.zq.end_test_video_player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.Objects;

public class Song implements Serializable {
    /**
     * 歌手
     */
    private String artist;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 图片
     */
    private byte[] image;
    /**
     * 歌曲的地址
     */
    private String path;
    private String album;
    /**
     * 歌曲长度
     */
    private int duration;
    /**
     * 歌曲的大小
     */
    private long size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return path.equals(song.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }

    public String getAlbum() {
        if(album != null)
            return album;
        return "未知";
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        if(artist != null)
            return artist;
        return "未知";
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        if (title != null)
            return title;
        return "未知";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Deprecated
    public byte[] getImage() {
        return image;
    }

    public Bitmap getBitmap() {
        if (image != null)
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        return null;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
