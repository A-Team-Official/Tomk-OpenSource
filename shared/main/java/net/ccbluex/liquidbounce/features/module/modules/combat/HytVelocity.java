package net.ccbluex.liquidbounce.features.module.modules.combat;

import com.sun.org.apache.bcel.internal.generic.I2F;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "HytVelocity", description = "NMSL", category = ModuleCategory.COMBAT)
public class HytVelocity extends Module {
    private ListValue modeValue = new ListValue("Mode", new String[]{"hyt","grimac"},"hyt");
    private double lastX,lastY,lastZ;
    private List<CPacketConfirmTransaction> cPacketConfirmTransactions = new ArrayList<>();
    private boolean attack = false;
    @EventTarget
    private void onPacket(PacketEvent event){
        if (mc.getThePlayer()!=null) {

            if (classProvider.isSPacketEntityVelocity(event.getPacket())) {
                ISPacketEntityVelocity packetEntityVelocity = event.getPacket().asSPacketEntityVelocity();
                if (mc.getThePlayer().getHurtTime() > 0) {

                    lastX = mc.getThePlayer().getMotionX();
                    lastY = mc.getThePlayer().getMotionY();
                    lastZ = mc.getThePlayer().getMotionZ();
                    event.cancelEvent();

                    if (mc.getThePlayer().getHurtTime() > 4 && mc.getThePlayer().getHurtTime() <= 9){
                        cPacketConfirmTransactions.forEach(cPacketConfirmTransaction -> {
                            mc2.getConnection().getNetworkManager().sendPacket(cPacketConfirmTransaction);
                        });
                        mc.getThePlayer().setSpeedInAir(0.018F);
                        packetEntityVelocity.setMotionX(1);
                        packetEntityVelocity.setMotionZ(1);
                    }


                    attack = true;
                }
            }else{
                if (attack){
                    if (classProvider.isCPacketConfirmTransaction(event.getPacket())){
                        cPacketConfirmTransactions.add((CPacketConfirmTransaction) event.getPacket());
                        event.cancelEvent();
                    }
                    mc.getThePlayer().setMotionX(0);
                    mc.getThePlayer().setMotionZ(0);
                    mc.getThePlayer().setSpeedInAir(0.02F);
                    attack = false;
                }
            }
            if (mc.getThePlayer().isDead()){
                attack = false;
                lastX = 0.0;
                lastY = 0.0;
                lastZ = 0.0;
            }
        }
    }
    @EventTarget
    private void onUpdate(UpdateEvent event){

    }
}
