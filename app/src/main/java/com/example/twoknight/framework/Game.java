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
 * The role of a single TwoKnight game, allowing clients to
 * take game actions and then the game takes care of it.
 */
public interface Game extends Observable {
    // === Accessors for Game state

    /**
     * Get the enemy hero.
     *
     * @return the hero
     */
    Hero getHero();

    /**
     * Get Game state.
     *
     * @return the state of the game.
     */
    GameState getGameState();

    /**
     * Get the number of turns played. Starts at turn 0.
     *
     * @return the turn number of the game progress
     */
    int getTurnNumber();

    /**
     * Get the current Level. Starts at 0.
     *
     * @return the level integer
     */
    int getLevel();

    /**
     * Get the playing field.
     *
     * @return an array of Tiles
     */
    Tile[][] getField();

    // === Mutators

    /**
     * Perform end of turn, i.e. moving the tiles
     * and handling everything after.
     */
    void endTurn(int e);

    /**
     * Get the current number of skills.
     *
     * @return array with numbers representing "ammo" for each skill
     */
    int[] getBought();

    /**
     * A way to get laser data to GUI
     *
     * @return a laser data class
     */
    Laser getLaserState();

    int getPower();
}
