package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(
    name = "PlayerSize",
    fakeName = "Player Size",
    description = "Edit the player's size",
    category = ModuleCategory.TOMK)
class PlayerSize : Module() {
    val playerSizeValue = FloatValue("PlayerSize", 0.5F, 0.01F, 5F)
}