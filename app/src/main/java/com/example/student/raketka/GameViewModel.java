package com.example.student.raketka;


import android.arch.lifecycle.ViewModel;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {
    public boolean addEnemy1 = true;
    public boolean addEnemy2 = true;
    public boolean addEnemy3 = true;
    public boolean addEnemy4 = true;
    public Rocket rocket;

    public MediaPlayer mp2;
    boolean gameOver = true;
    public int counter = 0;
    public List<Enemy> enemies = new ArrayList<>();
    public List<Fuel> fuels = new ArrayList<>();
}