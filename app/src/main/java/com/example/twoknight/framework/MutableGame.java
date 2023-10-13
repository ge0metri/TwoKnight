/*
 * Copyright (C) 2022. Henrik Bærbak Christensen, Aarhus University.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.twoknight.framework;

import com.example.twoknight.standard.Laser;

/**
 * The role of this interface, is to let certain parties mutate, while other dont.
 */
public interface MutableGame extends Game {
    int checkTiles();

    // === Accessors for Game state
    void setWinner(GameState who);

    void addRandomTile();

    void addTile(Tile tile);

    void beginLaser(int i, int j);

    void fireLaser(int i, int j);

    Laser getLaserState();

    boolean moveUp();

    boolean moveDown();

    boolean moveLeft();

    boolean moveRight();

    void clearMerged();

    void skipLaser();
}
