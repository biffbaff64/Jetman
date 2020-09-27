/*
 *  Copyright 28/04/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.input.UIButtons;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Trace;

public class ButtonInputHandler implements Disposable
{
    private float slowDown;
    private final App app;

    public ButtonInputHandler(App _app)
    {
        this.app = _app;
    }

    public void checkButtons()
    {
        if (app.getHud().getJoystick() != null)
        {
            //
            // Updates button presses depending
            // upon joystick knob position
            app.getHud().getJoystick().update();
        }

        //
        // FIRE button - shoot lasers at the bad guys
        if (app.getHud().buttonB.isPressed())
        {
            if (app.getPlayer().isRidingRover)
            {
                app.getGun().startShooting();
            }
            else
            {
                if (!app.getPlayer().isShooting)
                {
                    app.getPlayer().isShooting = true;
                    app.getPlayer().shootCount = 0;
                    app.getPlayer().shootRate = 0;
                }
            }
        }

        //
        // ACTION Button, used for picking up bombs or bridge sections,
        // entering transporters, or entering the Rover
        if (app.getHud().buttonA.isPressed())
        {
            if (app.getPlayer().collision.isTeleporterPresent())
            {
                app.getPlayer().actionButton.setAction();
            }
            else if (app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BOMB_CARRY)
            {
                app.getHud().buttonA.release();

                if (app.getPlayer().isCarrying)
                {
                    app.getPlayer().actionButton.removeAction();
                    app.getPlayer().isCarrying = false;

                    app.getBomb().isAttachedToPlayer = false;
                    app.getBomb().releaseXY.set(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY());

                    app.getBomb().setAction(Actions._FALLING);
                }
            }
            else if (app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._GUN_CARRY)
            {
                app.getHud().buttonA.release();

                if (app.getPlayer().isCarrying)
                {
                    app.getPlayer().actionButton.removeAction();
                    app.getPlayer().isCarrying = false;

                    app.getGun().isAttachedToPlayer = false;
                    app.getGun().releaseXY.set(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY());

                    app.getGun().setAction(Actions._FALLING);
                }
            }
            else if (app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._ROVER_RIDE)
            {
                if (app.getPlayer().isRidingRover)
                {
                    app.getPlayer().isRidingRover = false;
                    app.getPlayer().actionButton.removeAction();
                    app.getPlayer().sprite.setPosition((app.getRover().sprite.getX() + 96), app.getPlayer().initXYZ.getY());
                    app.getPlayer().setAction(Actions._STANDING);
                    app.getRover().setAction(Actions._STANDING);
                }

                app.getHud().buttonA.release();
            }
            else if (app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BRIDGE_CARRY)
            {
                app.getHud().buttonA.release();

                if (app.getPlayer().isCarrying && app.getPlayer().isOnGround && !app.getPlayer().isOnRoverBack)
                {
                    int x = (int) app.getPlayer().sprite.getX();

                    if (app.getPlayer().lookingAt.getX() == Movement._DIRECTION_RIGHT)
                    {
                        x += app.getPlayer().frameWidth;
                    }
                    else
                    {
                        x -= Gfx.getTileWidth();
                    }

                    app.getPlayer().bridgeManager.layBridge
                        (
                            x / Gfx.getTileWidth(),
                            (int) (app.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1
                        );

                    app.getPlayer().actionButton.removeAction();
                    app.getPlayer().isCarrying = false;
                }
            }
            else
            {
                app.getPlayer().actionButton.setAction();
            }
        }

        boolean directionButtonPressed = false;

        //
        // UP button
        if (app.getHud().buttonUp.isPressed())
        {
            directionButtonPressed = true;

            if (!app.getPlayer().isRidingRover)
            {
                if (app.getHud().getFuelBar().getTotal() > 0)
                {
                    app.getPlayer().isFalling = false;
                    app.getPlayer().isMovingY = true;
                    app.getPlayer().maxMoveSpeed = 24;
                    app.getPlayer().direction.setY(Movement._DIRECTION_UP);
                    app.getPlayer().speed.setY(MainPlayer._PLAYER_Y_SPEED);

                    app.getPlayer().setAction(Actions._FLYING);
                }
                else
                {
                    app.getPlayer().isMovingY = false;
                }
            }
        }

        //
        // DOWN button
        if (app.getHud().buttonDown.isPressed() && !app.getPlayer().isOnGround)
        {
            directionButtonPressed = true;

            if (!app.getPlayer().isRidingRover)
            {
                if (app.getPlayer().isInMidAir)
                {
                    app.getPlayer().isFalling = true;
                    app.getPlayer().isMovingY = true;
                    app.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                    app.getPlayer().speed.setY(MainPlayer._PLAYER_Y_SPEED);
                }
                else
                {
                    app.getPlayer().isMovingY = false;
                    app.getPlayer().speed.setY(0);
                }
            }
        }

        //
        // Check the RIGHT button
        if (app.getHud().buttonRight.isPressed())
        {
            directionButtonPressed = true;
            app.getPlayer().isFlippedX = false;

            if (!app.getPlayer().isBlockedRight && (app.getPlayer().getRightEdge() < Gfx.getMapWidth()))
            {
                if (app.getPlayer().isRidingRover)
                {
                    app.getRover().isMovingX = true;
                    app.getRover().direction.setX(Movement._DIRECTION_RIGHT);
                }
                else
                {
                    app.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
                    app.getPlayer().isMovingX = true;
                    app.getPlayer().speed.setX(app.getPlayer().isInMidAir ? MainPlayer._PLAYER_JUMP_X_SPEED : MainPlayer._PLAYER_X_SPEED);

                    if (app.getPlayer().isJumpingCrater)
                    {
                        app.getPlayer().setAction(Actions._FLYING);
                    }
                    else
                    {
                        if (!app.getPlayer().isInMidAir)
                        {
                            app.getPlayer().setAction(Actions._RUNNING);
                        }
                    }
                }
            }
            else
            {
                app.getPlayer().isMovingX = false;

                app.getHud().buttonRight.release();
                app.getPlayer().direction.setX(Movement._DIRECTION_STILL);
                app.getPlayer().speed.setX(0);
            }
        }
        //
        // Check the LEFT button
        else if (app.getHud().buttonLeft.isPressed())
        {
            directionButtonPressed = true;
            app.getPlayer().isFlippedX = true;

            if (!app.getPlayer().isBlockedLeft && (app.getPlayer().sprite.getX() > 0))
            {
                if (app.getPlayer().isRidingRover)
                {
                    app.getRover().isMovingX = true;
                    app.getRover().direction.setX(Movement._DIRECTION_LEFT);
                }
                else
                {
                    app.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
                    app.getPlayer().isMovingX = true;
                    app.getPlayer().speed.setX(app.getPlayer().isInMidAir ? MainPlayer._PLAYER_JUMP_X_SPEED : MainPlayer._PLAYER_X_SPEED);

                    if (app.getPlayer().isJumpingCrater)
                    {
                        app.getPlayer().setAction(Actions._FLYING);
                    }
                    else
                    {
                        if (!app.getPlayer().isInMidAir)
                        {
                            app.getPlayer().setAction(Actions._RUNNING);
                        }
                    }
                }
            }
            else
            {
                app.getPlayer().isMovingX = false;

                app.getHud().buttonLeft.release();
                app.getPlayer().direction.setX(Movement._DIRECTION_STILL);
                app.getPlayer().speed.setX(0);
            }
        }

        //
        // No direction buttons pressed
        if (!directionButtonPressed && !app.getPlayer().isTeleporting)
        {
            if (app.getPlayer().isRidingRover)
            {
                app.getRover().isMovingX = false;
                app.getRover().setAction(Actions._STANDING);
            }
            else if ((app.getPlayer().getSpriteAction() != Actions._HURT)
                    && (app.getPlayer().getSpriteAction() != Actions._FALLING_TO_GROUND))
            {
                app.getPlayer().isMovingX = false;
                app.getPlayer().isMovingY = false;

                if (app.getPlayer().isOnGround && (app.getPlayer().getSpriteAction() != Actions._HOVERING))
                {
                    app.getPlayer().setAction(Actions._STANDING);
                    app.getPlayer().speed.set(0, 0);
                }
                else
                {
                    //
                    // If in mid air and no buttons pressed then
                    // the player must be falling
                    if (app.getPlayer().isInMidAir)
                    {
                        app.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                        app.getPlayer().setAction(Actions._FALLING);
                        app.getPlayer().isMovingY = true;

                        if (app.getPlayer().speed.getY() <= 0)
                        {
                            app.getPlayer().speed.setY(1.0f);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
