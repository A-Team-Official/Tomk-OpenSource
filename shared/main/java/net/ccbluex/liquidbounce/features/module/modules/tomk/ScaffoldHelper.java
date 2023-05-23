package net.ccbluex.liquidbounce.features.module.modules.tomk;

import net.ccbluex.liquidbounce.features.module.*;
import org.jetbrains.annotations.*;
import net.ccbluex.liquidbounce.*;
import kotlin.*;
import net.ccbluex.liquidbounce.features.module.modules.world.*;
import net.ccbluex.liquidbounce.utils.*;
import kotlin.jvm.internal.*;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.*;
import net.ccbluex.liquidbounce.event.*;

@ModuleInfo(name = "ScffoldHelper", description = "ScffoldHelper", category = ModuleCategory.WORLD)
public final class ScaffoldHelper extends Module
{
    @Override
    public void onDisable() {
    }
    
    @EventTarget
    public final void onUpdate(@Nullable final UpdateEvent event) {
        final Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Scaffold.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.world.Scaffold1");
        }
        final Scaffold scaffoldmodule = (Scaffold)module;
        final Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(Timer.class);
        if (module2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.world.Timer");
        }
        final Timer timermodule = (Timer)module2;
        final IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (thePlayer == null) {
            Intrinsics.throwNpe();
        }
        if (!thePlayer.isSneaking()) {
            final IEntityPlayerSP thePlayer2 = MinecraftInstance.mc.getThePlayer();
            if (thePlayer2 == null) {
                Intrinsics.throwNpe();
            }
            if (thePlayer2.getOnGround()) {
                scaffoldmodule.setState(false);
                timermodule.setState(false);
            }
            else {
                scaffoldmodule.setState(true);
                timermodule.setState(true);
            }
        }
    }
}
