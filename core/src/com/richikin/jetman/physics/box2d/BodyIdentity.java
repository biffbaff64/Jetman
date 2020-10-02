/*
 *  Copyright 07/05/2018 Red7Projects.
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

package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.entities.rootobjects.GameEntity;
import com.richikin.jetman.graphics.GraphicID;

public class BodyIdentity
{
    public final GraphicID gid;
    public final GraphicID type;
    public final GameEntity entity;

    BodyIdentity(GameEntity _entity, GraphicID _gid, GraphicID _type)
    {
        this.entity = _entity;
        this.gid = _gid;
        this.type = _type;
    }
}
