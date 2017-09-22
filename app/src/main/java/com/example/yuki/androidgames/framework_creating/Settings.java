package com.example.yuki.androidgames.framework_creating;

import android.os.Environment;

import com.example.yuki.androidgames.framework.FileIO;
import com.example.yuki.androidgames.framework.impl.AndroidFileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Yuki on 2017/09/14.
 */

public class Settings {
    public static final String SD_FILE_NAME = "samplesnake.txt";
    public static boolean soundEnabled = true;
    public static int[] highscores = {0, 0, 0, 0, 0};

    public static void load(FileIO files){
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(files.readFile(SD_FILE_NAME)));
            System.out.println("[load success] path:"+
                    Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+
                    SD_FILE_NAME);
            soundEnabled = Boolean.parseBoolean(in.readLine());
            System.out.println("[load success] sound:"+soundEnabled);
            for (int i=0;i<5;i++){
                highscores[i] = Integer.parseInt(in.readLine());
            }
            System.out.println("[load success] highscore:"+highscores[0]);
        } catch (IOException e) {
            System.out.println("[file load error] Settings.load -> IOException");
        } catch (NumberFormatException e) {
            System.out.println("[file load error] Settings.load -> NumberFormatException");
        } finally {
            try{
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("[file load close error] Settings.load -> IOException");
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(SD_FILE_NAME)));
            System.out.println("[save success] path:"+
                    Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+
                    SD_FILE_NAME);
            out.write(Boolean.toString(soundEnabled)+"\n");
            System.out.println("[save success] sound:"+soundEnabled);
            for(int i=0;i<5;i++){
                out.write(Integer.toString(highscores[i])+"\n");
            }
            System.out.println("[save success] highscore:"+highscores[0]);
        } catch (IOException e) {
            System.out.println("[file save error] Settings.save -> IOException");
        } finally {
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
                System.out.println("[save complete]");
            } catch (IOException e){
                System.out.println("[file save close error] Settings.save -> IOException");
            }
        }
    }

    public static void addScore(int score) {
        for (int i=0; i< 5; i++) {
            if(highscores[i] < score) {
                for (int j = 4; j > i; j--) {
                    highscores[j] = highscores[j - 1];
                }
                highscores[i] = score;
                break;
            }
        }
    }
}
