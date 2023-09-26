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

package com.example.twoknight.standard;

/**
 * Placeholder class for a set of HotStone domain wide constants.
 * Note some of these constants are actually variant specific, and
 * could beneficially be moved out of this more general purpose
 * constant container.
 */

public class GameConstants {
    public static final int HERO_MAX_HEALTH = 128;
    public static final String BABY_HERO_TYPE = "Baby";
    public static final String ARMOR_HERO_TYPE = "Amoré";
    public static final String SHIELD_HERO_TYPE = "Shelly";
    public static final String BUILDER_HERO_TYPE = "Bob";
    public static final String LAZER_HERO_TYPE = "Shoop";

    public static final int side0 = 4;
    public static final int side1 = 4;

    public static final String STANDARD_HERO_TYPE = "Hero";
    private static final String powerOne = "Add big tile";
    private static final String powerTwo = "WOW double a tile!";
    private static final String powerThree = "Swap two tiles";
    private static final String powerFour = "Remove a tile";
    private static final String powerFive = "Add THE GOLDEN TILE"; //TODO: double every ten turns?
    private static final String powerSix = "Remove all grey tiles";
    private static final String powerSeven = "Mystery power";
    private static final String powerEight = "Remove shield";
    private static final String powerNine = "Skip Laser";
    private static final String powerZero = "Vulnerable";
    public static final String[] powerDescription = new String[]{powerZero, powerOne, powerTwo, powerThree, powerFour, powerFive, powerSix, powerSeven, powerEight, powerNine};
    public static int[] powerCost = new int[]{1, 3, 1, 2, 1, 4, 1, 2, 1, 3};
    public static int[] maxItems = new int[]{90, 3, 1, 2, 1, 4, 1, 2, 1, 3};
}
