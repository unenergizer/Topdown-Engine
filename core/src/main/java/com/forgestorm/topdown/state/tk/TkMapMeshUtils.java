package com.forgestorm.topdown.state.tk;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TkMapMeshUtils {

    public static float[] getTopTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Top
                x         ,distort(y),distort(z               ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                x+tileSize,distort(y),distort(z               ),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                x+tileSize,distort(y),distort(z-(tileSize)),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                x+tileSize,distort(y),distort(z-(tileSize)),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //6
                x         ,distort(y),distort(z-(tileSize)),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //7
                x         ,distort(y),distort(z               ),tex.getU() ,tex.getV() ,  0 , 0 , 0 ,  //4
        };
    }

    public static float[] getTopTriangleTileVertices(int id, float x, float y, float z, int tileSize, TextureRegion tex) {
        switch (id) {
            case 0: {
                return new float[] {
                        //BottomLeft
                        x+tileSize,distort(y),distort(z             ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                        x         ,distort(y),distort(z-tileSize),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                        x         ,distort(y),distort(z             ),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                };
            }
            case 1: {
                return new float[] {
                        //bottomRight
                        x         ,distort(y),distort(z               ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                        x+tileSize,distort(y),distort(z               ),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                        x+tileSize,distort(y),distort(z-(tileSize)),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                };
            }
            case 2: {
                return new float[] {
                        //TopRight
                        x+tileSize,distort(y),distort(z             ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                        x+tileSize,distort(y),distort(z-tileSize),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                        x         ,distort(y),distort(z-tileSize),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                };
            }
            case 3: {
                return new float[] {
                        //TopLeft
                        x+tileSize,distort(y),distort(z-(tileSize)),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //6
                        x         ,distort(y),distort(z-(tileSize)),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //7
                        x         ,distort(y),distort(z               ),tex.getU() ,tex.getV() ,  0 , 0 , 0 ,  //4
                };
            }
            default: {
                return null;
            }
        }
    }

    public static float[] getTopLeftRampTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Top
                x         ,distort(y             ),distort(z               ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                x+tileSize,distort(y+tileSize),distort(z               ),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                x+tileSize,distort(y+tileSize),distort(z-(tileSize)),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                x+tileSize,distort(y+tileSize),distort(z-(tileSize)),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //6
                x         ,distort(y             ),distort(z-(tileSize)),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //7
                x         ,distort(y             ),distort(z               ),tex.getU() ,tex.getV() ,  0 , 0 , 0 ,  //4
        };
    }

    public static float[] getTopRightRampTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Top
                x         ,distort(y+tileSize),distort(z               ),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //4
                x+tileSize,distort(y             ),distort(z               ),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //5
                x+tileSize,distort(y             ),distort(z-(tileSize)),tex.getU2(),tex.getV() ,  0 , 0 , 0 ,  //6
                x+tileSize,distort(y             ),distort(z-(tileSize)),tex.getU2(),tex.getV2(),  0 , 0 , 0 ,  //6
                x         ,distort(y+tileSize),distort(z-(tileSize)),tex.getU() ,tex.getV2(),  0 , 0 , 0 ,  //7
                x         ,distort(y+tileSize),distort(z               ),tex.getU() ,tex.getV() ,  0 , 0 , 0 ,  //4
        };
    }

    public static float[] getFrontTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Front
                x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                x+tileSize,distort(y)             ,distort(z),tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                x+tileSize,distort(y+tileSize),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x+tileSize,distort(y+tileSize),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x         ,distort(y+tileSize),distort(z),tex.getU() ,tex.getV() ,  0 , 0 , 0 , //4
                x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
        };
    }

    public static float[] getFrontTriangleTileVertices(int id, float x, float y, float z, int tileSize, TextureRegion tex) {
        switch (id) {
            case 0: {
                return new float[] {
                        //BottomLeft
                        x+tileSize,distort(y             ),distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                        x         ,distort(y+tileSize),distort(z),tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                        x         ,distort(y             ),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                };
            }
            case 1: {
                return new float[] {
                        //bottomRight
                        x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                        x+tileSize,distort(y)             ,distort(z),tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                        x+tileSize,distort(y+tileSize),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                };
            }
            case 2: {
                return new float[] {
                        //TopRight
                        x+tileSize,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                        x+tileSize,distort(y+tileSize),distort(z),tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                        x         ,distort(y+tileSize),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                };
            }
            case 3: {
                return new float[] {
                        //TopLeft
                        x+tileSize,distort(y+tileSize),distort(z),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                        x         ,distort(y+tileSize),distort(z),tex.getU() ,tex.getV() ,  0 , 0 , 0 , //4
                        x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                };
            }
            default: {
                return null;
            }
        }
    }

    public static float[] getFrontLeftDiagonalTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Front
                x         ,distort(y)             ,distort(z-tileSize),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                x+tileSize,distort(y)             ,distort(z)             ,tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                x+tileSize,distort(y+tileSize),distort(z)             ,tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x+tileSize,distort(y+tileSize),distort(z)             ,tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x         ,distort(y+tileSize),distort(z-tileSize),tex.getU() ,tex.getV() ,  0 , 0 , 0 , //4
                x         ,distort(y)             ,distort(z-tileSize),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
        };
    }

    public static float[] getFrontRightDiagonalTileVertices(float x, float y, float z, int tileSize, TextureRegion tex) {
        return new float[] {
                //Front
                x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
                x+tileSize,distort(y)             ,distort(z-tileSize),tex.getU2(),tex.getV2(),  0 , 0 , 0 , //1
                x+tileSize,distort(y+tileSize),distort(z-tileSize),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x+tileSize,distort(y+tileSize),distort(z-tileSize),tex.getU2(),tex.getV() ,  0 , 0 , 0 , //5
                x         ,distort(y+tileSize),distort(z),tex.getU() ,tex.getV() ,  0 , 0 , 0 , //4
                x         ,distort(y)             ,distort(z),tex.getU() ,tex.getV2(),  0 , 0 , 0 , //0
        };
    }

    public static float distort(float val) {
        return val + (val * ((float) Math.sqrt(2) - 1f));
    }
}
