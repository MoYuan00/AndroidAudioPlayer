package com.zq.end_test_video_player;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    private static final String path = "/data/data/com.zq.end_test_video_player/music";
    public static List<Song> getAllMusics(Context context) {
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        List<Song> songList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Song song = getSong(path);
                songList.add(song);
            }
        }
        //扫描自定义目录下的文件
        File file = new File(path);
        for(File f : file.listFiles()){
            String path = f.getAbsolutePath();
            Log.i(TAG, "getAllMusics: " + path);
            Song song = getSong(path);
            songList.add(song);
        }
        return songList;
    }

    private static final String TAG = "MusicUtils";
    private static final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public static Song getSong(String path){
        Song song = new Song();
        song.setPath(path);
        // 获取音乐相关信息
        mmr.setDataSource(path);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        song.setTitle(title);
        String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        song.setAlbum(album);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        song.setArtist(artist);
        int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)); // 播放时长单位为毫秒  
        song.setDuration(duration);
        byte[] image = mmr.getEmbeddedPicture();// 图片，可以通过BitmapFactory.decodeByteArray转换为bitmap图片
//        BitmapFactory.decodeByteArray(image, 0, image.length);
        song.setImage(image);
        return song;
    }

}
