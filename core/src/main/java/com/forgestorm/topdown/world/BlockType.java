package com.forgestorm.topdown.world;

public enum BlockType {
    AIR,
    BLOCK,
    //   -------
    //   |   / |
    //   |  /  |
    //   | /   |
    //   +------
    DIAGONAL_45,
    //   -------
    //   | \   |
    //   |  \  |
    //   |   \ |
    //   ------+
    DIAGONAL_135,
    //   ------+
    //   |   / |
    //   |  /  |
    //   | /   |
    //   -------
    DIAGONAL_255,
    //   +------
    //   | \   |
    //   |  \  |
    //   |   \ |
    //   -------
    DIAGONAL_315
}
