package top.gregtao.iconrenderer.utils;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public class FrameHelper {
    public RenderTarget framebuffer;
    private Matrix4fStack matrixStack;
    private PoseStack modelStack;

    public FrameHelper(int size, ItemStack itemStack) {
        this.framebuffer = new TextureTarget(size, size, true, Minecraft.ON_OSX);
        this.startRecord();
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        this.renderGuiItemIcon(itemStack, 0, 0, renderer);
        this.endRecord();
    }

    public FrameHelper(int size, Entity entity) {
        this.framebuffer = new TextureTarget(size, size, true, Minecraft.ON_OSX);
        this.startRecord();
        this.renderEntity(entity);
        this.endRecord();
    }

    public void startRecord() {
        this.matrixStack = RenderSystem.getModelViewStack();
        this.matrixStack.pushMatrix();
        RenderSystem.applyModelViewMatrix();
        this.modelStack = new PoseStack();

        RenderSystem.backupProjectionMatrix();
        Matrix4f p = new Matrix4f().setOrtho(0, 16, 16, 0, -150, 150);
        RenderSystem.setProjectionMatrix(p, VertexSorting.ORTHOGRAPHIC_Z);

        this.framebuffer.bindWrite(true);
        this.framebuffer.bindRead();
    }

    public void endRecord() {
        RenderSystem.restoreProjectionMatrix();
        this.matrixStack.popMatrix();

        this.framebuffer.unbindWrite();
        this.framebuffer.unbindRead();
    }

    public void renderEntity(Entity spawnEntity) {
        Minecraft client = Minecraft.getInstance();
        MultiBufferSource.BufferSource immediate = client.renderBuffers().bufferSource();

        this.modelStack.mulPose(Axis.XP.rotationDegrees(112.5f));
        this.modelStack.scale(2.5f, -2.5f, -2.5f);
        this.modelStack.translate(0.75f, 1f, 1f);
        this.modelStack.mulPose(Axis.ZP.rotationDegrees(45f));
        this.modelStack.translate(-0.75f, 0, 0);
        this.modelStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        this.modelStack.mulPose(Axis.ZN.rotationDegrees(22.5f));
        this.modelStack.translate(0.75f, 0, 0);

        if (!(client.player == null)) {
            spawnEntity.setPosRaw(client.player.getX(), client.player.getY(), client.player.getZ());
        }

        try {
            client.getEntityRenderDispatcher().render(spawnEntity, 0, 0, 0, 0,
                    DeltaTracker.ZERO.getGameTimeDeltaPartialTick(false), this.modelStack, immediate, 15728880);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renderGuiItemIcon(ItemStack stack, int x, int y, ItemRenderer renderer) {
        this.renderGuiItemModel(stack, x, y, renderer.getModel(stack, null, null, 0), renderer);
    }

    protected void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model, ItemRenderer renderer) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.modelStack.translate(x + 8, y + 8, 100.0F);
        this.modelStack.scale(16.0F, -16.0F, 16.0F);
        MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean bl = !model.usesBlockLight();
        if (bl) {
            Lighting.setupForFlatItems();
        }

        renderer.render(stack, ItemDisplayContext.GUI, false, this.modelStack, immediate, 0xf000f0, OverlayTexture.NO_OVERLAY, model);
        RenderSystem.disableDepthTest();
        immediate.endBatch();
        RenderSystem.enableDepthTest();
        if (bl) {
            Lighting.setupFor3DItems();
        }
    }
}
