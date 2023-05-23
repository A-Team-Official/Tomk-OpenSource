package net.ccbluex.liquidbounce.features.module.modules.tomk;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;


@ModuleInfo(name = "ItemPhysics",description = "物理掉落",category = ModuleCategory.RENDER)
public class ItemPhysics extends Module {

    private final Random random;

    public static void handleCameraTransforms(final IBakedModel model, final ItemCameraTransforms.TransformType cameraTransformType) {
     model.getItemCameraTransforms().applyTransform(cameraTransformType);
    }
    public ItemPhysics() {
        random = new Random();
    }

    public ResourceLocation getEntityTexture() {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public void setPositionAndRotation2(final EntityItem item, final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        item.setPosition(x, y, z);
    }

    public void doRender(final TextureManager renderer, final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        double rotation = 2D;
        final EntityItem item = (EntityItem) entity;
        final ItemStack itemstack = item.getItem();
        int i;
        if (itemstack != null && itemstack.getItem() != null) {
            i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        } else {
            i = 187;
        }
        this.random.setSeed(i);
        renderer.bindTexture(this.getEntityTexture());
        renderer.getTexture(this.getEntityTexture()).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemstack);
        final boolean flag2 = ibakedmodel.isGui3d();
        final boolean is3D = ibakedmodel.isGui3d();
        final int j = this.getModelCount(itemstack);
        GlStateManager.translate((float) x, (float) y, (float) z);
        if (ibakedmodel.isGui3d()) {
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
        }
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(item.rotationYaw, 0.0f, 0.0f, 1.0f);
        if (is3D) {
            GlStateManager.translate(0.0, 0.0, -0.08);
        } else {
            GlStateManager.translate(0.0, 0.0, -0.04);
        }
        if (is3D || mc2.getRenderManager().options != null) {
            if (is3D) {
                if (!item.onGround) {
                    item.rotationPitch += (float) rotation;
                }
            } else if (!Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(item.posZ) && item.world != null) {
                if (item.onGround) {
                    item.rotationPitch = 0.0f;
                } else {
                    rotation *= 2.0;
                    item.rotationPitch += (float) rotation;
                }
            }
            GlStateManager.rotate(item.rotationPitch, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int k = 0; k < j; ++k) {
            if (flag2) {
                GlStateManager.pushMatrix();
                if (k > 0) {
                    final float f4 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f5 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f6 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f4, f5, f6);
                }
                handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                mc2.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                if (k > 0) {
                }
                handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                Minecraft.getMinecraft().getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.05375f);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        renderer.bindTexture(this.getEntityTexture());
        renderer.getTexture(this.getEntityTexture()).restoreLastBlurMipmap();
    }

    public int getModelCount(final ItemStack stack) {
        int i = 1;
        if (stack.stackSize > 48) {
            i = 5;
        } else if (stack.stackSize > 32) {
            i = 4;
        } else if (stack.stackSize > 16) {
            i = 3;
        } else if (stack.stackSize > 1) {
            i = 2;
        }
        return i;
    }

    enum PhysicModes {
        Realistic,
        Alpha,
        Simple;
    }
}
