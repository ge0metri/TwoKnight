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
 * The role of an enemy Hero in TwoKnight. The present interface presents a
 * read-only view of the hero to respect that a client may only alter
 * the internal state of a hero through the Game's mutator methods.
 */
public interface Hero extends ImmovableTile {

    /**
     * Get the value of health of this hero.
     *
     * @return the value of health
     */
    int getHealth();

    /**
     * Get the type of the hero. Type is a
     * string value to be open for new hero types.
     * Default hero types are defined in GameConstants.
     *
     * @return the type of hero
     */
    String getType();

    /**
     * Get current armor value.
     *
     * @return armor value
     */
    int getArmor();

    /**
     * Get current shield value.
     *
     * @return shield value
     */
    int getShield();

    /**
     * Get current vulnerable state.
     *
     * @return true if vulnerable
     */
    boolean isVulnerable();
}
