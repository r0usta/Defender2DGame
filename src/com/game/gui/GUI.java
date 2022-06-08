package com.game.gui;

import com.game.main.UtilityTool;

import java.awt.*;

public interface GUI {
   Font maruMonica = UtilityTool.loadFont("/assets/font/maru_monica.ttf");

   void draw(Graphics2D g2);
}

