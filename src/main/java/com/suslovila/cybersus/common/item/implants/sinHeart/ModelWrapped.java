package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.ModelWrapperDisplayList;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.util.ResourceLocation;

public class ModelWrapped {
    ModelWrapperDisplayList model;
    ResourceLocation texture;

    public ModelWrapped(
            ModelWrapperDisplayList model,
            ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }
    public ModelWrapped(
            ResourceLocation modelLocation,
            ResourceLocation texture) {
        this.model = new ModelWrapperDisplayList(modelLocation);
        this.texture = texture;
    }

    public ModelWrapped(
            String modelLocationString,
            String textureString) {
        this.model = new ModelWrapperDisplayList(modelLocationString);
        this.texture = new ResourceLocation(Cybersus.MOD_ID, textureString);
    }
    public ModelWrapped(
            String modelLocationString
            ) {
        this.model = new ModelWrapperDisplayList(modelLocationString);
        this.texture = SusGraphicHelper.whiteBlank;
    }
    public void render() {
        SusGraphicHelper.bindTexture(texture);
        model.renderAll();
    }

}
