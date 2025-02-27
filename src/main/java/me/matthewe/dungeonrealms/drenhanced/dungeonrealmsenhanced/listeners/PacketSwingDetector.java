package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class PacketSwingDetector  {
    private static final String HANDLER_NAME = "noswing_blocker";


    @SubscribeEvent
    public void onPlayerJoin(DungeonRealmsJoinEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.getConnection() == null) {
            return;
        }
        if (!DREnhanced.isDeveloper()) return;

        NetHandlerPlayClient connection = mc.getConnection();
        ChannelPipeline pipeline = connection.getNetworkManager().channel().pipeline();

        // Prevent duplicate injection
        if (pipeline.get(HANDLER_NAME) == null) {
            pipeline.addBefore("packet_handler", HANDLER_NAME, new ChannelDuplexHandler() {
                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    if (mc.player == null || mc.getConnection() == null) {
                        super.write(ctx, msg, promise);
                        return;
                    }
                    if (msg instanceof CPacketAnimation || msg instanceof CPacketUseEntity) {
                        float experience = mc.player.experience;
                        if (experience < 0.15f) {
                            return;
                        }
                    }
                    super.write(ctx, msg, promise);
                }
            });
        }
    }

}