package top.gregtao.iconrenderer.utils;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;

import java.io.IOException;
import java.util.Base64;

public class ImageHelper {
    public FrameHelper frameHelper1, frameHelper2;
    public JsonMeta jsonMeta;
    public EntityJsonMeta entityJsonMeta;

    public ImageHelper(JsonMeta jsonMeta) {
        this.jsonMeta = jsonMeta;
        this.frameHelper1 = new FrameHelper(128, jsonMeta.itemStack);
        this.frameHelper2 = new FrameHelper(32, jsonMeta.itemStack);

        try (NativeImage image = fromFrame(this.frameHelper1.framebuffer)) {
            this.jsonMeta.largeIcon = Base64.getEncoder().encodeToString(image.asByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (NativeImage image = fromFrame(this.frameHelper2.framebuffer)) {
            this.jsonMeta.smallIcon = Base64.getEncoder().encodeToString(image.asByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageHelper(EntityJsonMeta jsonMeta) {
        this.entityJsonMeta = jsonMeta;
        this.frameHelper1 = new FrameHelper(128, jsonMeta.entity);
        try (NativeImage image = fromFrame(this.frameHelper1.framebuffer)) {
            this.entityJsonMeta.icon = Base64.getEncoder().encodeToString(image.asByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NativeImage fromFrame(RenderTarget frame) {
        NativeImage img = new NativeImage(frame.width, frame.height, false);
        RenderSystem.bindTexture(frame.getColorTextureId());
        img.downloadTexture(0, false);
        img.flipY();
        return img;
    }

}
