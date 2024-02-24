package com.forgestorm.topdown.world;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlockType {
    AIR(false, false, false, false, false, false),
    BLOCK(true, true, true,true, true, true),
    //   |   /
    //   |  /
    //   | /
    //   +------
    TRIANGULAR_PRISM_45(true, true, true, true, false, true),
    //     \   |
    //      \  |
    //       \ |
    //   ------+
    TRIANGULAR_PRISM_135(true, true, false, false, false, false),
    //   ------+
    //       / |
    //      /  |
    //     /   |
    TRIANGULAR_PRISM_255(true, true, false, false, false, false),
    //   +------
    //   | \
    //   |  \
    //   |   \
    TRIANGULAR_PRISM_315(true, true, false, true, false, true);

    private final boolean visibleTop;
    private final boolean visibleBottom;
    private final boolean visibleLeft;
    private final boolean visibleRight;
    private final boolean visibleFront;
    private final boolean visibleBack;
}
