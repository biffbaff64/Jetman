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

package com.richikin.jetman.characters.hero;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.physics.Movement;

public class ButtonInputHandler implements Disposable
{
    private float slowDown;

    public ButtonInputHandler()
    {
    }

    public void checkButtons()
    {
        if (App.getHud().getJoystick() != null)
        {
            //
            // Updates button presses depending
            // upon joystick knob position
            App.getHud().getJoystick().update();
        }

        //
        // FIRE button - shoot lasers at the bad guys
        if (App.getHud().buttonAttack.isPressed())
        {
            if (App.getPlayer().isRidingRover)
            {
                App.getGun().startShooting();
            }
            else
            {
                if (!App.getPlayer().isShooting)
                {
                    App.getPlayer().isShooting = true;
                    App.getPlayer().shootCount = 0;
                    App.getPlayer().shootRate = 0;
                }
            }
        }

        //
        // ACTION Button, used for picking up bombs or bridge sections,
        // entering transporters, or entering the Rover
        if (App.getHud().buttonAction.isPressed())
        {
            if (App.getPlayer().collision.isTeleporterPresent())
            {
                App.getPlayer().actionButton.setAction();
            }
            else if (App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BOMB_CARRY)
            {
                App.getHud().buttonAction.release();

                if (App.getPlayer().isCarrying)
                {
                    App.getPlayer().actionButton.removeAction();
                    App.getPlayer().isCarrying = false;

                    App.getBomb().isAttachedToPlayer = false;
                    App.getBomb().releaseXY.set(App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY());

                    App.getBomb().setAction(ActionStates._FALLING);
                }
            }
            else if (App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._GUN_CARRY)
            {
                App.getHud().buttonAction.release();

                if (App.getPlayer().isCarrying)
                {
                    App.getPlayer().actionButton.removeAction();
                    App.getPlayer().isCarrying = false;

                    App.getGun().isAttachedToPlayer = false;
                    App.getGun().releaseXY.set(App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY());

                    App.getGun().setAction(ActionStates._FALLING);
                }
            }
            else if (App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._ROVER_RIDE)
            {
//                if (App.getPlayer().isRidingRover)
                {
                    App.getPlayer().isRidingRover = false;
                    App.getPlayer().actionButton.removeAction();
                    App.getPlayer().collision.setOnGround(GraphicID._GROUND);
                    App.getPlayer().sprite.setPosition((App.getPlayerPos().getX() + 130), App.getPlayerPos().getY());
                    App.getRover().setAction(ActionStates._STANDING);
                }

                App.getHud().buttonAction.release();
            }
            else if (App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BRIDGE_CARRY)
            {
                App.getHud().buttonAction.release();

                if (App.getPlayer().isCarrying && App.getPlayer().isOnGround && !App.getPlayer().isOnRoverBack)
                {
                    int x = (int) App.getPlayer().sprite.getX();

                    if (App.getPlayer().lookingAt.getX() == Movement._DIRECTION_RIGHT)
                    {
                        x += App.getPlayer().frameWidth;
                    }
                    else
                    {
                        x -= Gfx.getTileWidth();
                    }

                    App.getPlayer().bridgeManager.layBridge
                        (
                            x / Gfx.getTileWidth(),
                            (int) (App.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1
                        );

                    App.getPlayer().actionButton.removeAction();
                    App.getPlayer().isCarrying = false;
                }
            }
            else
            {
                App.getPlayer().actionButton.setAction();
            }
        }

        boolean directionButtonPressed = false;

        //
        // UP button
        if (App.getHud().buttonUp.isPressed())
        {
            directionButtonPressed = true;

            if (!App.getPlayer().isRidingRover)
            {
                if (App.getHud().getFuelBar().getTotal() > 0)
                {
                    App.getPlayer().isFalling = false;
                    App.getPlayer().isMovingY = true;
                    App.getPlayer().maxMoveSpeed = 24;
                    App.getPlayer().direction.setY(Movement._DIRECTION_UP);
                    App.getPlayer().speed.setY(MainPlayer._PLAYER_Y_SPEED);

                    App.getPlayer().setAction(ActionStates._FLYING);
                }
                else
                {
                    App.getPlayer().isMovingY = false;
                }
            }
        }

        //
        // DOWN button
        if (App.getHud().buttonDown.isPressed() && !App.getPlayer().isOnGround)
        {
            directionButtonPressed = true;

            if (!App.getPlayer().isRidingRover)
            {
                if (App.getPlayer().isInMidAir)
                {
                    App.getPlayer().isFalling = true;
                    App.getPlayer().isMovingY = true;
                    App.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                    App.getPlayer().speed.setY(MainPlayer._PLAYER_Y_SPEED);
                }
                else
                {
                    App.getPlayer().isMovingY = false;
                    App.getPlayer().speed.setY(0);
                }
            }
        }

        //
        // Check the RIGHT button
        if (App.getHud().buttonRight.isPressed())
        {
            directionButtonPressed = true;
            App.getPlayer().isFlippedX = false;

            if (!App.getPlayer().isBlockedRight && (App.getPlayer().getRightEdge() < Gfx.getMapWidth()))
            {
                if (App.getPlayer().isRidingRover)
                {
                    App.getRover().isMovingX = true;
                    App.getRover().direction.setX(Movement._DIRECTION_RIGHT);
                }
                else
                {
                    App.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
                    App.getPlayer().isMovingX = true;
                    App.getPlayer().speed.setX(App.getPlayer().isInMidAir ? MainPlayer._PLAYER_JUMP_X_SPEED : MainPlayer._PLAYER_X_SPEED);

                    if (App.getPlayer().isJumpingCrater)
                    {
                        App.getPlayer().setAction(ActionStates._FLYING);
                    }
                    else
                    {
                        if (!App.getPlayer().isInMidAir)
                        {
                            App.getPlayer().setAction(ActionStates._RUNNING);
                        }
                    }
                }
            }
            else
            {
                App.getPlayer().isMovingX = false;

                App.getHud().buttonRight.release();
                App.getPlayer().direction.setX(Movement._DIRECTION_STILL);
                App.getPlayer().speed.setX(0);
            }
        }
        //
        // Check the LEFT button
        else if (App.getHud().buttonLeft.isPressed())
        {
            directionButtonPressed = true;
            App.getPlayer().isFlippedX = true;

            if (!App.getPlayer().isBlockedLeft && (App.getPlayer().sprite.getX() > 0))
            {
                if (App.getPlayer().isRidingRover)
                {
                    App.getRover().isMovingX = true;
                    App.getRover().direction.setX(Movement._DIRECTION_LEFT);
                }
                else
                {
                    App.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
                    App.getPlayer().isMovingX = true;
                    App.getPlayer().speed.setX(App.getPlayer().isInMidAir ? MainPlayer._PLAYER_JUMP_X_SPEED : MainPlayer._PLAYER_X_SPEED);

                    if (App.getPlayer().isJumpingCrater)
                    {
                        App.getPlayer().setAction(ActionStates._FLYING);
                    }
                    else
                    {
                        if (!App.getPlayer().isInMidAir)
                        {
                            App.getPlayer().setAction(ActionStates._RUNNING);
                        }
                    }
                }
            }
            else
            {
                App.getPlayer().isMovingX = false;

                App.getHud().buttonLeft.release();
                App.getPlayer().direction.setX(Movement._DIRECTION_STILL);
                App.getPlayer().speed.setX(0);
            }
        }

        //
        // No direction buttons pressed
        if (!directionButtonPressed && !App.getPlayer().isTeleporting)
        {
            if (App.getPlayer().isRidingRover)
            {
                App.getRover().isMovingX = false;
                App.getRover().setAction(ActionStates._STANDING);
            }
            else if ((App.getPlayer().getAction() != ActionStates._HURT)
                    && (App.getPlayer().getAction() != ActionStates._FALLING_TO_GROUND))
            {
                App.getPlayer().isMovingX = false;
                App.getPlayer().isMovingY = false;

                if (App.getPlayer().isOnGround && (App.getPlayer().getAction() != ActionStates._HOVERING))
                {
                    App.getPlayer().setAction(ActionStates._STANDING);
                    App.getPlayer().speed.set(0, 0);
                }
                else
                {
                    //
                    // If in mid air and no buttons pressed then
                    // the player must be falling
                    if (App.getPlayer().isInMidAir)
                    {
                        App.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
                        App.getPlayer().setAction(ActionStates._FALLING);
                        App.getPlayer().isMovingY = true;

                        if (App.getPlayer().speed.getY() <= 0)
                        {
                            App.getPlayer().speed.setY(1.0f);
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
