package com.game.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    public boolean upPressed, downPressed;
    public boolean leftPressed, rightPressed;
    public boolean debugPressed, interactionPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(Game.state == State.PLAY) {
            if(key == KeyEvent.VK_W) {
                upPressed = true;
            }
            if(key == KeyEvent.VK_S) {
                downPressed = true;
            }
            if(key == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if(key == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if(key == KeyEvent.VK_SPACE) {
                if(!Game.player.attacking) {
                    Game.player.spriteCounter = 0;
                    Game.player.attacking = true;
                    Game.playSoundEffect(5);
                }
            }
            if(key == KeyEvent.VK_E) {
                interactionPressed = true;
            }
            if(key == KeyEvent.VK_C) {
                Game.state = State.CHARACTER;
            }
            if(key == KeyEvent.VK_P){
                Game.state = State.PAUSE;
            }
            if(key == KeyEvent.VK_T) {
                debugPressed = !debugPressed;
            }
        }else if(Game.state == State.PAUSE) {
            if(key == KeyEvent.VK_P){
                Game.state = State.PLAY;
            }
        }else if(Game.state == State.DIALOGUE) {
            if(key == KeyEvent.VK_ESCAPE) {
                Game.state = State.PLAY;
            }
        }else if(Game.state == State.CHARACTER) {
            if(key == KeyEvent.VK_C) {
                Game.state = State.PLAY;
            }
            if(key == KeyEvent.VK_ESCAPE) {
                Game.state = State.PLAY;
            }
        }else if(Game.state == State.GAMEOVER) {
            if(key == KeyEvent.VK_W) {
                Game.guiManager.commandNum--;
                if(Game.guiManager.commandNum < 0) {
                    Game.guiManager.commandNum = 1;
                }
            }

            if(key == KeyEvent.VK_S) {
                Game.guiManager.commandNum++;
                if(Game.guiManager.commandNum > 1) {
                    Game.guiManager.commandNum = 0;
                }
            }
            if(key == KeyEvent.VK_ENTER) {
                if(Game.guiManager.commandNum == 0) {
                    Game.state = State.PLAY;
                    Game.retry();
                    Game.playMusic(0);
                }else if(Game.guiManager.commandNum == 1) {
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(key == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if(key == KeyEvent.VK_E) {
            interactionPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
