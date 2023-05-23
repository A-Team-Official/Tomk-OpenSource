package tomk;


//import net.ccbluex.liquidbounce.utils.math.MathUtils;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IAxisAlignedBB;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import tomk.blur.MathUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.classProvider;
import static org.lwjgl.opengl.GL11.*;
import static tomk.StencilUtil.mc;

public class ESPUtil {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);

    public static boolean isInView(Entity ent) {
        frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) || ent.ignoreFrustumCheck;
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer1);
        glGetFloat(GL_PROJECTION_MATRIX, floatBuffer2);
        glGetInteger(GL_VIEWPORT, intBuffer);
        if (GLU.gluProject(x, y, z, floatBuffer1, floatBuffer2, intBuffer, windPos)) {
            return new Vector3f(windPos.get(0) / scaleFactor, (mc.displayHeight - windPos.get(1)) / scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static double[] getInterpolatedPos(IEntity entity) {
        float ticks = mc.timer.renderPartialTicks;
        return new double[]{
                MathUtils.interpolate(entity.getLastTickPosX(), entity.getPosX(), ticks) - mc.getRenderManager().viewerPosX,
                MathUtils.interpolate(entity.getLastTickPosY(), entity.getPosY(), ticks) - mc.getRenderManager().viewerPosY,
                MathUtils.interpolate(entity.getLastTickPosZ(), entity.getPosZ(), ticks) - mc.getRenderManager().viewerPosZ
        };
    }

    public static Vector4f getEntityPositionsOn2D(IEntity entity) {
        final double[] renderingEntityPos = getInterpolatedPos(entity);
        final double entityRenderWidth = entity.getWidth() / 1.5;
        final IAxisAlignedBB bb = classProvider.createAxisAlignedBB(renderingEntityPos[0] - entityRenderWidth,
                renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth,
                renderingEntityPos[1] + entity.getHeight() + (entity.isSneaking() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);

        final List<Vector3f> vectors = Arrays.asList(
                new Vector3f((float) bb.getMinX(), (float) bb.getMinY(), (float) bb.getMinZ()),
                new Vector3f((float) bb.getMinX(), (float) bb.getMaxY(), (float) bb.getMinZ()),
                new Vector3f((float) bb.getMaxX(), (float) bb.getMinY(), (float) bb.getMinZ()),
                new Vector3f((float) bb.getMaxX(), (float) bb.getMaxY(), (float) bb.getMinZ()),
                new Vector3f((float) bb.getMinX(), (float) bb.getMinY(), (float) bb.getMaxZ()),
                new Vector3f((float) bb.getMinX(), (float) bb.getMaxY(), (float) bb.getMaxZ()),
                new Vector3f((float) bb.getMaxX(), (float) bb.getMinY(), (float) bb.getMaxZ()),
                new Vector3f((float) bb.getMaxX(), (float) bb.getMaxY(), (float) bb.getMaxZ()));

        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(mc);
        for (Vector3f vector3f : vectors) {
            vector3f = projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.getScaleFactor());
            if (vector3f != null && vector3f.z >= 0.0 && vector3f.z < 1.0) {
                entityPos.x = Math.min(vector3f.x, entityPos.x);
                entityPos.y = Math.min(vector3f.y, entityPos.y);
                entityPos.z = Math.max(vector3f.x, entityPos.z);
                entityPos.w = Math.max(vector3f.y, entityPos.w);
            }
        }
        return entityPos;
    }



}
