package com.forgestorm.topdown.world;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlockType {
    AIR(),
    BLOCK(),
    TRIANGULAR_PRISM_NE(),
    TRIANGULAR_PRISM_SE(),
    TRIANGULAR_PRISM_SW(),
    TRIANGULAR_PRISM_NW(),
    RAMP_N(),
    RAMP_E(),
    RAMP_S(),
    RAMP_W();
}
