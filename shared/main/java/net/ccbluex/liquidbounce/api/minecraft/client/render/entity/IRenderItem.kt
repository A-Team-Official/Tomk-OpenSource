/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.api.minecraft.client.render.entity

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer

interface IRenderItem {
    var zLevel: Float

    fun renderItemAndEffectIntoGUI(stack: IItemStack, x: Int, y: Int)
    fun renderItemIntoGUI(stack: IItemStack, x: Int, y: Int)
    fun renderItemOverlays(fontRenderer: GameFontRenderer, stack: IItemStack, x: Int, y: Int)
}