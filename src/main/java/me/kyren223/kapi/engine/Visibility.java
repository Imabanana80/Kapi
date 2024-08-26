/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package me.kyren223.kapi.engine;

import me.kyren223.kapi.annotations.Kapi;

/**
 * Determines how to calculate a {@link Object3D}'s visibility.
 */
@Kapi
public enum Visibility {
    /**
     * The object is always visible.
     */
    VISIBLE,
    /**
     * The object is always hidden.
     */
    HIDDEN,
    /**
     * The object is visible if its parent is visible and hidden if its parent is hidden.<br>
     * If the object has no parent, it is always visible.
     */
    INHERIT,
}
