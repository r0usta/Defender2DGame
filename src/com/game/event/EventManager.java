package com.game.event;

import com.game.entity.Direction;
import com.game.main.Game;

public class EventManager {
    /**
     * By creating a two-dimensional array. It is possible to control each event in your own way.
     */
    EventRect[][] eventRect;
    /**
     * Event coordinate variables, which are written after a
     * successfully executed condition in the "hit" method.
     * They are used to calculate the distance between the executed event and the player.
     */
    private int previousEventX;
    private int previousEventY;
    /**
     * Can a player re-touch an event
     */
    public boolean canTouchEvent = true;

    public EventManager() {
        eventRect = new EventRect[Game.maxMapRow][Game.maxMapCol];

        for(int row = 0; row < Game.maxMapRow; row++) {
            for(int col = 0; col < Game.maxMapCol; col++) {
                eventRect[row][col] = new EventRect();
                eventRect[row][col].eventRectDefaultX = eventRect[row][col].x = 23;
                eventRect[row][col].eventRectDefaultY = eventRect[row][col].y = 23;
                /*
                The size of the area for the event will be a small 2x2 pixels.
                To avoid strange situations. For example, to be able to teleport,
                the player will need to stand in the center of the tile.
                But because of the large area it will be enough for him to
                approach the edge of this tile (and we don't want that!).
                 */
                eventRect[row][col].width = 2;
                eventRect[row][col].height = 2;
            }
        }
    }

    /**
     * Every frame will check the event.
     * Called in the "Player" class in the "update()" method
     * @see com.game.entity.Player
     */
    public void eventCheck() {
        int xDistance = Math.abs(Game.player.mapPosX - previousEventX);
        int yDistance = Math.abs(Game.player.mapPosY - previousEventY);
        int distance = Math.max(xDistance, yDistance); // Selects the biggest integer
        if(distance > Game.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent) {
            if(hit(4,10)) Event.damagePit();
            else if(hit(5,24)) Event.damagePit();
            else if(hit(19,29)) Event.damagePit();
            else if(hit(9,31)) Event.damagePit();
            else if(hit(20,12, Direction.RIGHT)) Event.healingPlace();
            else if(hit(21,13, Direction.UP)) Event.healingPlace();
            else if(hit(7,13, Direction.DOWN)) Event.woodenPlate();
            else if(hit(8,14, Direction.LEFT)) Event.woodenPlate();
            else if(hit(7,15, Direction.UP)) Event.woodenPlate();
        }
    }

    /**
     * Checks if the player "area" has collided with the event "area" in the specified location
     * @param col The place where you need to check the collision. Axis X
     * @param row The place where you need to check the collision. Axis Y
     * @return Whether there was a collision
     */
    public boolean hit(int row, int col) {
        boolean isHit = false;

        Game.player.collisionArea.x = Game.player.mapPosX + Game.player.collisionArea.x;
        Game.player.collisionArea.y = Game.player.mapPosY + Game.player.collisionArea.y;
        eventRect[row][col].x += row * Game.tileSize;
        eventRect[row][col].y += col * Game.tileSize;

        if(Game.player.collisionArea.intersects(eventRect[row][col]) && !eventRect[row][col].eventDone) {
            isHit = true;
            previousEventX = Game.player.mapPosX;
            previousEventY = Game.player.mapPosY;
        }

        Game.player.collisionArea.x = Game.player.collisionAreaDefaultX;
        Game.player.collisionArea.y = Game.player.collisionAreaDefaultY;
        eventRect[row][col].x = eventRect[row][col].eventRectDefaultX;
        eventRect[row][col].y = eventRect[row][col].eventRectDefaultY;

        return isHit;
    }

    /**
     * Checks if the player "area" has collided with the event "area" in the specified location
     * @param col The place where you need to check the collision. Axis X
     * @param row The place where you need to check the collision. Axis Y
     * @param reqDirection where the player should be directed to record the collision
     *                     (To avoid situations where the player will not be
     *                     able to get out of the event, because regardless
     *                     of the direction will constantly be fixed collision)
     * @return Whether there was a collision
     */
    public boolean hit(int row, int col, Direction reqDirection) {
        boolean isHit = false;

        Game.player.collisionArea.x = Game.player.mapPosX + Game.player.collisionArea.x;
        Game.player.collisionArea.y = Game.player.mapPosY + Game.player.collisionArea.y;
        eventRect[row][col].x += row * Game.tileSize;
        eventRect[row][col].y += col * Game.tileSize;

        if(Game.player.collisionArea.intersects(eventRect[row][col]) && !eventRect[row][col].eventDone) {
            if(Game.player.direction == reqDirection) {
                isHit = true;
                previousEventX = Game.player.mapPosX;
                previousEventY = Game.player.mapPosY;
            }
        }

        Game.player.collisionArea.x = Game.player.collisionAreaDefaultX;
        Game.player.collisionArea.y = Game.player.collisionAreaDefaultY;
        eventRect[row][col].x = eventRect[row][col].eventRectDefaultX;
        eventRect[row][col].y = eventRect[row][col].eventRectDefaultY;

        return isHit;
    }
}
