package com.game.event;

import java.awt.*;

/**
 * A subclass of the "Rectangle" class, but with advanced features for better collision control.
 */
public class EventRect extends Rectangle {
    int eventRectDefaultX, eventRectDefaultY;
    /**
     * Check if an event has occurred so that you don't have to perform it again.
     * (Gives the ability to create a one-time event)
     */
    boolean eventDone = false;
}
