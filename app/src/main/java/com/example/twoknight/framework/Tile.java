/*
 * Copyright (C) 2022. Henrik Bærbak Christensen, Aarhus University.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.twoknight.framework;

/**
 * Role of a Tile in TwoKnight. The present interface presents a
 * Tile, that can be seen and merged. It did not seem worthy to
 * introduce read only / mutable
 */

public interface Tile {
    /**
     * Get the value of the tile.
     *
     * @return the value
     */
    int getValue();

    /**
     * Set merge state
     */
    void setMerged(boolean m);

    /**
     * Determines if a given Tile other can be merged.
     *
     * @param other tile to be merged with
     * @return if it can be merged
     */
    boolean canMergeWith(Tile other);

    /**
     * Merges with other Tile.
     *
     * @param other tile to be merged with.
     * @return resulting tile number
     */
    int mergeWith(Tile other);

    /**
     * Sees if the tile has been merged in this turn
     *
     * @return true if Tile has been merged this turn.
     */
    boolean getMerged();
}
