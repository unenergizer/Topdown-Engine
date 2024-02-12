package com.forgestorm.topdown.demoUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import lombok.Getter;

import java.util.HashMap;

public class RenderUtils {

    public AssetManager manager = new AssetManager();
    public TextureAtlas textureAtlas;
    public String textureAtlasFilename = "textureAtlas/atlas.atlas";

    @Getter
    public BitmapFont font;

    private boolean fullyLoaded = false;
    private Runnable onLoad;

    public RenderUtils(Runnable finishedLoadingRunnable) {
        TextureRegion temptemp =  new TextureRegion(new Texture(Gdx.files.internal("Fonts/PixPrompt.png")));
        temptemp.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font = new BitmapFont(Gdx.files.internal("Fonts/PixPrompt.fnt"), temptemp);
        font.getData().markupEnabled = true;

        // load the texture atlas
        manager.load(textureAtlasFilename, TextureAtlas.class);
        onLoad = finishedLoadingRunnable;
    }

    public boolean doneLoading() {
        if (fullyLoaded)
            return true;

        manager.update();

        if (manager.isFinished()) {
            if (textureAtlas == null && manager.contains(textureAtlasFilename, TextureAtlas.class)) // Retrieve the loaded texture atlas
                textureAtlas = manager.get(textureAtlasFilename, TextureAtlas.class);
            if (onLoad != null) {
                onLoad.run();
                onLoad = null;
            }
            fullyLoaded = true;
        }

        return manager.isFinished();
    }

    private HashMap<String, TextureAtlas.AtlasRegion> cachedTextures = new HashMap<>();
    public TextureAtlas.AtlasRegion getTexture(String name) {
        TextureAtlas.AtlasRegion texture = cachedTextures.get(name);

        if (texture != null)
            return texture;
        else {
            texture = textureAtlas.findRegion(name);
            if (texture == null)
                throw new RuntimeException("Region not found on atlas, name: "+ name + "\nAll regions: " + getRegionsNames());

            cachedTextures.put(name, texture);
            return texture;
        }
    }

    private HashMap<String, Array<TextureAtlas.AtlasRegion>> cachedTextureGroups = new HashMap<>();
    public Array<TextureAtlas.AtlasRegion> getTextures(String name) {
        Array<TextureAtlas.AtlasRegion> textures = cachedTextureGroups.get(name);
        if (textures != null)
            return textures;
        else {
            textures = textureAtlas.findRegions(name);
            if (textures.size == 0)
                throw new RuntimeException("Group of regions ( .findRegions() ) not found on atlas, name: " + name);

            cachedTextureGroups.put(name, textures);
            return textures;
        }
    }

    public boolean hasTextures(String name) {
        Array<TextureAtlas.AtlasRegion> textures = textureAtlas.findRegions(name);
        if (textures.size == 0)
            return false;

        return true;
    }

    private String getRegionsNames() {
        ObjectSet<String> names = new ObjectSet<>();
        for (TextureAtlas.AtlasRegion region : textureAtlas.getRegions()) {
            names.add(region.name);
        }

        return names.toString("; ");
    }

    public void dispose() {
        manager.dispose();
        textureAtlas.dispose();
        font.dispose();
    }

}
