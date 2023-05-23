package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name = "CustomFont", description = "CustomFont", category = ModuleCategory.RENDER)
public class CustomFont extends Module {
    public static final ListValue shadowValue = new ListValue("ShadowMode", new String[]{"LiquidBounce", "Outline", "Default", "Custom"}, "Outline");
    public static final IntegerValue fontWidthValue = new IntegerValue("FontWidth", 7, 5, 10);
    public static final FloatValue shadowstrenge = new FloatValue("ShadowStrengh", 0.1f, 0.1f, 1f);
}
