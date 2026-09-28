package admsgenter.reload_emc;

import com.mojang.brigadier.Command;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.nss.AbstractNSSTag;
import moze_intel.projecte.config.CustomEMCParser;
import moze_intel.projecte.emc.EMCMappingHandler;
import moze_intel.projecte.network.PacketHandler;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("reload_emc")
@Mod.EventBusSubscriber
public class ReloadEMC {
    public static final Component 重载提示 = Component.translatable("command.reload_emc.notice");
    private static final Component 正在重载 = Component.translatable("command.reload_emc.started");
    private static final Component 完成重载 = Component.translatable("command.reload_emc.success");

    @SubscribeEvent
    public static void 指令(final RegisterCommandsEvent 事件) {
        事件.getDispatcher().register(Commands.literal("projecte").then(Commands.literal("reloademc").requires(执行者 -> 执行者.hasPermission(Commands.LEVEL_GAMEMASTERS)).executes(指令 -> {
            指令.getSource().sendSuccess(正在重载, true);
            long 时间 = System.currentTimeMillis();
            AbstractNSSTag.clearCreatedTags();
            CustomEMCParser.init();
            MinecraftServer 服务器 = 指令.getSource().getServer();
            try {
                EMCMappingHandler.map(服务器.getServerResources().managers(), 服务器.getResourceManager());
                PECore.LOGGER.info("Registered {} EMC values. (took {} ms)", EMCMappingHandler.getEmcMapSize(), System.currentTimeMillis()-时间);
                PacketHandler.sendFragmentedEmcPacketToAll();
            } catch(Throwable 异常) { PECore.LOGGER.error("Error calculating EMC values", 异常); }
            指令.getSource().sendSuccess(完成重载, true);
            return Command.SINGLE_SUCCESS;
        })));
    }
}