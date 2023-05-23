
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot
import net.ccbluex.liquidbounce.features.module.modules.tomk.Teams
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer.Companion.getColorIndex
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils.color
import net.ccbluex.liquidbounce.utils.render.WorldToScreen
import net.ccbluex.liquidbounce.utils.render.shader.FramebufferShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.GlowShader
import net.ccbluex.liquidbounce.utils.render.shader.shaders.OutlineShader

import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11
import java.awt.Color
import org.lwjgl.util.vector.Vector3f

import java.text.DecimalFormat

@ModuleInfo(name = "ESP", description = "FDPESP", category = ModuleCategory.RENDER)
class ESP : Module() {
    val modeValue = ListValue(
        "Mode",
        arrayOf("Box", "OtherBox", "WireFrame", "2D", "Real2D", "CSGO", "CSGO-Old", "Outline", "ShaderOutline", "ShaderGlow", "Jello","Frame"),
        "CSGO"
    )
    private val outlineWidthValue = FloatValue("Outline-Width", 3f, 0.5f, 5f)
    val wireframeWidthValue = FloatValue("WireFrame-Width", 2f, 0.5f, 5f)
    private val shaderOutlineRadiusValue = FloatValue("ShaderOutline-Radius", 1.35f, 1f, 2f)
    private val shaderGlowRadiusValue = FloatValue("ShaderGlow-Radius", 2.3f, 2f, 3f)
    private val csgoDirectLineValue = BoolValue("CSGO-DirectLine", false)
    private val csgoShowHealthValue = BoolValue("CSGO-ShowHealth", true)
    private val csgoShowHeldItemValue = BoolValue("CSGO-ShowHeldItem", true)
    private val csgoShowNameValue = BoolValue("CSGO-ShowName", true)
    private val csgoWidthValue = FloatValue("CSGOOld-Width", 2f, 0.5f, 5f)
    private val colorModeValue = ListValue("ColorMode", arrayOf("Name", "Armor", "OFF"), "Name")
    private val colorRedValue = IntegerValue("R", 255, 0, 255)
    private val colorGreenValue = IntegerValue("G", 255, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val colorRainbowValue = BoolValue("Rainbow", false)
    val invisible = BoolValue("Invisible", false)

    private val decimalFormat = DecimalFormat("0.0")

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        val mode = modeValue.get().toLowerCase()
        val mvMatrix = WorldToScreen.getMatrix(GL11.GL_MODELVIEW_MATRIX)
        val projectionMatrix = WorldToScreen.getMatrix(GL11.GL_PROJECTION_MATRIX)

        val need2dTranslate = mode == "csgo" || mode == "real2d" || mode == "csgo-old"
        if (need2dTranslate) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glMatrixMode(GL11.GL_PROJECTION)
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
            GL11.glOrtho(0.0, mc.displayWidth.toDouble(), mc.displayHeight.toDouble(), 0.0, -1.0, 1.0)
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glPushMatrix()
            GL11.glLoadIdentity()
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GlStateManager.enableTexture2D()
            GlStateManager.depthMask(true)
            GL11.glLineWidth(1.0f)
        }

        for (entity in mc.theWorld!!.loadedEntityList) {
            if (EntityUtils.isSelected(entity, false)) {
                val entityLiving = entity.asEntityLivingBase()
                val color = getColor(entityLiving)
                when (mode) {
                    "box", "otherbox" -> RenderUtils.drawEntityBox(
                        entity,
                        color,
                        mode != "otherbox",
                        true,
                        outlineWidthValue.get()
                    )
                    "frame"->{
                        doWireFrame()
                    }

                    "outline" -> RenderUtils.drawEntityBox(entity, color, true, false, outlineWidthValue.get())

                    "2d" -> {
                        val renderManager = mc.renderManager
                        val timer = mc.timer
                        val posX =
                            entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX
                        val posY =
                            entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY
                        val posZ =
                            entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
                        RenderUtils.draw2D(entityLiving, posX, posY, posZ, color.rgb, Color.BLACK.rgb)
                    }

                    "csgo", "real2d", "csgo-old" -> {
                        val renderManager = mc.renderManager
                        val timer = mc.timer
                        val bb = entityLiving.entityBoundingBox
                            .offset(-entityLiving.posX, -entityLiving.posY, -entityLiving.posZ)
                            .offset(
                                entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * timer.renderPartialTicks,
                                entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * timer.renderPartialTicks,
                                entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * timer.renderPartialTicks
                            )
                            .offset(-renderManager.renderPosX, -renderManager.renderPosY, -renderManager.renderPosZ)
                        val boxVertices = arrayOf(
                            doubleArrayOf(bb.minX, bb.minY, bb.minZ),
                            doubleArrayOf(bb.minX, bb.maxY, bb.minZ),
                            doubleArrayOf(bb.maxX, bb.maxY, bb.minZ),
                            doubleArrayOf(bb.maxX, bb.minY, bb.minZ),
                            doubleArrayOf(bb.minX, bb.minY, bb.maxZ),
                            doubleArrayOf(bb.minX, bb.maxY, bb.maxZ),
                            doubleArrayOf(bb.maxX, bb.maxY, bb.maxZ),
                            doubleArrayOf(bb.maxX, bb.minY, bb.maxZ)
                        )
                        var minX = mc.displayWidth.toFloat()
                        var minY = mc.displayHeight.toFloat()
                        var maxX = 0f
                        var maxY = 0f
                        for (boxVertex in boxVertices) {
                            val screenPos = WorldToScreen.worldToScreen(
                                Vector3f(
                                    boxVertex[0].toFloat(), boxVertex[1].toFloat(), boxVertex[2].toFloat()
                                ), mvMatrix, projectionMatrix, mc.displayWidth, mc.displayHeight
                            ) ?: continue
                            minX = screenPos.x.coerceAtMost(minX)
                            minY = screenPos.y.coerceAtMost(minY)
                            maxX = screenPos.x.coerceAtLeast(maxX)
                            maxY = screenPos.y.coerceAtLeast(maxY)
                        }

                        // out of screen
                        if (!(minX == mc.displayWidth.toFloat() || minY == mc.displayHeight.toFloat() || maxX == 0f || maxY == 0f)) {
                            if (mode == "csgo") {
                                RenderUtils.glColor(color)
                                if (!csgoDirectLineValue.get()) {
                                    val distX = (maxX - minX) / 3.0f
                                    val distY = (maxY - minY) / 3.0f
                                    GL11.glBegin(GL11.GL_LINE_STRIP)
                                    GL11.glVertex2f(minX, minY + distY)
                                    GL11.glVertex2f(minX, minY)
                                    GL11.glVertex2f(minX + distX, minY)
                                    GL11.glEnd()
                                    GL11.glBegin(GL11.GL_LINE_STRIP)
                                    GL11.glVertex2f(minX, maxY - distY)
                                    GL11.glVertex2f(minX, maxY)
                                    GL11.glVertex2f(minX + distX, maxY)
                                    GL11.glEnd()
                                    GL11.glBegin(GL11.GL_LINE_STRIP)
                                    GL11.glVertex2f(maxX - distX, minY)
                                    GL11.glVertex2f(maxX, minY)
                                    GL11.glVertex2f(maxX, minY + distY)
                                    GL11.glEnd()
                                    GL11.glBegin(GL11.GL_LINE_STRIP)
                                    GL11.glVertex2f(maxX - distX, maxY)
                                    GL11.glVertex2f(maxX, maxY)
                                    GL11.glVertex2f(maxX, maxY - distY)
                                    GL11.glEnd()
                                } else {
                                    GL11.glBegin(GL11.GL_LINE_LOOP)
                                    GL11.glVertex2f(minX, minY)
                                    GL11.glVertex2f(minX, maxY)
                                    GL11.glVertex2f(maxX, maxY)
                                    GL11.glVertex2f(maxX, minY)
                                    GL11.glEnd()
                                }
                                if (csgoShowHealthValue.get()) {
                                    val barHeight =
                                        (maxY - minY) * (1.0f - entityLiving.health / entityLiving.maxHealth)
                                    GL11.glColor4f(0.1f, 1.0f, 0.1f, 1.0f)
                                    GL11.glBegin(GL11.GL_QUADS)
                                    GL11.glVertex2f(maxX + 2.0f, minY + barHeight)
                                    GL11.glVertex2f(maxX + 2.0f, maxY)
                                    GL11.glVertex2f(maxX + 3.0f, maxY)
                                    GL11.glVertex2f(maxX + 3.0f, minY + barHeight)
                                    GL11.glEnd()
                                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                                    mc.fontRendererObj.drawString(
                                        this.decimalFormat.format(entityLiving.health) + "§c❤",
                                        maxX + 4.0f,
                                        minY + barHeight,
                                        ColorUtils.healthColor(entityLiving.health, entityLiving.maxHealth).rgb,
                                        false
                                    )
                                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                                }
                                if (csgoShowHeldItemValue.get() && classProvider.isEntityPlayer(entityLiving)) {
                                    if (entityLiving.asEntityPlayer().heldItem?.displayName != null) {
                                        GL11.glEnable(GL11.GL_TEXTURE_2D)
                                        GL11.glEnable(GL11.GL_DEPTH_TEST)
                                        if (classProvider.isEntityPlayer(entityLiving)) {
                                            mc.fontRendererObj.drawCenteredString(
                                                entityLiving.asEntityPlayer().heldItem!!.displayName,
                                                minX + (maxX - minX) / 2.0f,
                                                maxY + 2.0f,
                                                -1
                                            )
                                        }
                                        GL11.glDisable(GL11.GL_TEXTURE_2D)
                                        GL11.glDisable(GL11.GL_DEPTH_TEST)
                                    }
                                }
                                if (csgoShowNameValue.get()) {
                                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                                    if (classProvider.isEntityPlayer(entityLiving)) {
                                        mc.fontRendererObj.drawCenteredString(
                                            entityLiving.displayName!!.formattedText,
                                            minX + (maxX - minX) / 2.0f,
                                            minY - 12.0f,
                                            -1
                                        )
                                    }
                                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                                }
                            } else if (mode == "real2d") {
                                RenderUtils.drawRect(minX - 1, minY - 1, minX, maxY, color)
                                RenderUtils.drawRect(maxX, minY - 1, maxX + 1, maxY + 1, color)
                                RenderUtils.drawRect(minX - 1, maxY, maxX, maxY + 1, color)
                                RenderUtils.drawRect(minX - 1, minY - 1, maxX, minY, color)
                            } else if (mode == "csgo-old") {
                                val width = csgoWidthValue.get() * ((maxY - minY) / 50)
                                RenderUtils.drawRect(minX - width, minY - width, minX, maxY, color)
                                RenderUtils.drawRect(maxX, minY - width, maxX + width, maxY + width, color)
                                RenderUtils.drawRect(minX - width, maxY, maxX, maxY + width, color)
                                RenderUtils.drawRect(minX - width, minY - width, maxX, minY, color)
                                // hp bar
                                val hpSize = (maxY + width - minY) * (entityLiving.health / entityLiving.maxHealth)
                                RenderUtils.drawRect(
                                    minX - width * 3,
                                    minY - width,
                                    minX - width * 2,
                                    maxY + width,
                                    Color.GRAY
                                )
                                RenderUtils.drawRect(
                                    minX - width * 3,
                                    maxY - hpSize,
                                    minX - width * 2,
                                    maxY + width,
                                    ColorUtils.healthColor(entityLiving.health, entityLiving.maxHealth)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (need2dTranslate) {
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glMatrixMode(GL11.GL_PROJECTION)
            GL11.glPopMatrix()
            GL11.glMatrixMode(GL11.GL_MODELVIEW)
            GL11.glPopMatrix()
            GL11.glPopAttrib()
        }
    }

    fun isValid(entity: IEntityLivingBase): Boolean {
        return entity !== mc.thePlayer && entity.health > 0.0f && entity is EntityPlayer && !AntiBot.isBot(
            entity
        )
    }
    fun doWireFrame() {
        val mvMatrix = WorldToScreen.getMatrix(GL11.GL_MODELVIEW_MATRIX)
        val projectionMatrix = WorldToScreen.getMatrix(GL11.GL_PROJECTION_MATRIX)
        for (entity in mc.theWorld!!.playerEntities) {
            if (entity.asEntityLivingBase().invisible && !invisible.value) {
                return
            }
            if (isValid(entity)) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glMatrixMode(GL11.GL_PROJECTION)
                GL11.glPushMatrix()
                GL11.glLoadIdentity()
                GL11.glOrtho(0.0, mc.displayWidth.toDouble(), mc.displayHeight.toDouble(), 0.0, -1.0, 1.0)
                GL11.glMatrixMode(GL11.GL_MODELVIEW)
                GL11.glPushMatrix()
                GL11.glLoadIdentity()
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                GlStateManager.enableTexture2D()
                GL11.glDepthMask(true)
                GL11.glLineWidth(2.0f)

                val renderManager = mc.renderManager
                val timer = mc.timer
                val bb = entity.entityBoundingBox
                    .offset(-entity.posX, -entity.posY, -entity.posZ)
                    .offset(
                        entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks,
                        entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks,
                        entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                    )
                    .offset(-mc.renderManager.renderPosX, -mc.renderManager.renderPosY, -mc.renderManager.renderPosZ)
                val boxVertices = arrayOf(
                    doubleArrayOf(bb.minX, bb.minY, bb.minZ),
                    doubleArrayOf(bb.minX, bb.maxY, bb.minZ),
                    doubleArrayOf(bb.maxX, bb.maxY, bb.minZ),
                    doubleArrayOf(bb.maxX, bb.minY, bb.minZ),
                    doubleArrayOf(bb.minX, bb.minY, bb.maxZ),
                    doubleArrayOf(bb.minX, bb.maxY, bb.maxZ),
                    doubleArrayOf(bb.maxX, bb.maxY, bb.maxZ),
                    doubleArrayOf(bb.maxX, bb.minY, bb.maxZ)
                )
                var minX = Float.MAX_VALUE
                var minY = Float.MAX_VALUE
                var maxX = -1f
                var maxY = -1f
                for (boxVertex in boxVertices) {
                    val screenPos = WorldToScreen.worldToScreen(
                        Vector3f(
                            boxVertex[0].toFloat(),
                            boxVertex[1].toFloat(),
                            boxVertex[2].toFloat()
                        ), mvMatrix, projectionMatrix, mc.displayWidth, mc.displayHeight
                    )
                        ?: continue
                    minX = Math.min(screenPos.x, minX)
                    minY = Math.min(screenPos.y, minY)
                    maxX = Math.max(screenPos.x, maxX)
                    maxY = Math.max(screenPos.y, maxY)
                }
                val teams = LiquidBounce.moduleManager[Teams::class.java] as Teams
                if (minX > 0 || minY > 0 || maxX <= mc.displayWidth || maxY <= mc.displayWidth) {
                    var espColor:Color = if(entity.totalArmorValue2 * 1 + entity.health * 2 > mc.thePlayer!!.totalArmorValue2 + mc.thePlayer!!.health*2) Color(255,0,0)else Color(255,255,255)
                    if(teams.isInYourTeam(entity))
                        espColor = Color(50,255,50)
                    color(if (entity.hurtTime == 0) espColor.rgb else Color(255,0,0).rgb)
                    GL11.glBegin(GL11.GL_LINE_LOOP)
                    GL11.glVertex2f(minX, minY)
                    GL11.glVertex2f(minX, maxY)
                    GL11.glVertex2f(maxX, maxY)
                    GL11.glVertex2f(maxX, minY)
                    GL11.glEnd()
                }
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glMatrixMode(GL11.GL_PROJECTION)
                GL11.glPopMatrix()
                GL11.glMatrixMode(GL11.GL_MODELVIEW)
                GL11.glPopMatrix()
                GL11.glPopAttrib()
            }
        }
    }
    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val mode = modeValue.get().toLowerCase()
        val partialTicks = event.partialTicks

        if (mode.equals("jello", ignoreCase = true)) {
            val hurtingEntities = ArrayList<IEntityLivingBase>()
            var shader: FramebufferShader = GlowShader.GLOW_SHADER
            var radius = 3f
            var color = Color(120, 120, 120)
            var hurtColor = Color(120, 0, 0)
            var firstRun = true

            for (i in 0..1) {
                shader.startDraw(partialTicks)
                for (entity in mc.theWorld!!.loadedEntityList) {
                    if (EntityUtils.isSelected(entity, false)) {
                        val entityLivingBase = entity.asEntityLivingBase()
                        if (firstRun && entityLivingBase.hurtTime > 0) {
                            hurtingEntities.add(entityLivingBase)
                            continue
                        }
                        mc.renderManager.renderEntityStatic(entity, partialTicks, true)
                    }
                }
                shader.stopDraw(color, radius, 1f)

                // hurt
                if (hurtingEntities.size > 0) {
                    shader.startDraw(partialTicks)
                    for (entity in hurtingEntities) {
                        mc.renderManager.renderEntityStatic(entity, partialTicks, true)
                    }
                    shader.stopDraw(hurtColor, radius, 1f)
                }
                shader = OutlineShader.OUTLINE_SHADER
                radius = 1.2f
                color = Color(255, 255, 255, 170)
                hurtColor = Color(255, 0, 0, 170)
                firstRun = false
            }
            return
        }

        // normal shader esp
        val shader = when (mode) {
            "shaderoutline" -> OutlineShader.OUTLINE_SHADER
            "shaderglow" -> GlowShader.GLOW_SHADER
            else -> return
        }
        val radius = when (mode) {
            "shaderoutline" -> shaderOutlineRadiusValue.get()
            "shaderglow" -> shaderGlowRadiusValue.get()
            else -> 1f
        }

        // search
        val entityMap: MutableMap<Color, ArrayList<IEntityLivingBase>> = HashMap()
        for (entity in mc.theWorld!!.loadedEntityList) {
            if (EntityUtils.isSelected(entity, false)) {
                val entityLiving = entity.asEntityLivingBase()
                val color = getColor(entityLiving)
                if (!entityMap.containsKey(color)) {
                    entityMap[color] = ArrayList()
                }
                entityMap[color]!!.add(entityLiving)
            }
        }

        // draw
        for ((key, value) in entityMap) {
            shader.startDraw(partialTicks)
            for (entity in value) {
                mc.renderManager.renderEntityStatic(entity, partialTicks, true)
            }
            shader.stopDraw(key, radius, 1f)
        }
    }

    override val tag: String
        get() = modeValue.get()

    fun getColor(entity: IEntity): Color {
        if (classProvider.isEntityLivingBase(entity)) {
            if (entity.asEntityLivingBase().hurtTime > 0) return Color.RED
            if (EntityUtils.isFriend(entity)) return Color.BLUE
            if (colorModeValue.get() == "Name") {
                val chars = entity.displayName!!.formattedText.toCharArray()
                for (i in chars.indices) {
                    if (chars[i] != '§' || i + 1 >= chars.size) continue
                    val index = getColorIndex(chars[i + 1])
                    if (index < 0 || index > 15) continue
                    return Color(ColorUtils.hexColors[index])
                }
            } else if (colorModeValue.get() == "Armor") {
                if (entity is IEntityPlayer) {
                    val entityHead = entity.inventory.armorInventory[3] ?: return Color(Int.MAX_VALUE)
                    if (classProvider.isEntityPlayer(entityHead)) {
                        val entityItemArmor = entityHead.item as IItemArmor
                        return Color(entityItemArmor.getColor(entityHead))
                    }
                }
            }
        }
        return if (colorRainbowValue.get()) ColorUtils.rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get())
    }
}
