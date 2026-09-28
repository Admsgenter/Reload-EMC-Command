package admsgenter.reload_emc;

import com.mojang.brigadier.Command;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.nss.AbstractNSSTag;
import moze_intel.projecte.config.CustomEMCParser;
import moze_intel.projecte.emc.EMCMappingHandler;
import moze_intel.projecte.network.PacketHandler;
import net.minecraft.command.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod("reload_emc")
@Mod.EventBusSubscriber
public class ReloadEMC {
    public static final ITextComponent 重载提示 = new TranslationTextComponent("command.reload_emc.notice");
    private static final ITextComponent 正在重载 = new TranslationTextComponent("command.reload_emc.started");
    private static final ITextComponent 完成重载 = new TranslationTextComponent("command.reload_emc.success");

    @SubscribeEvent
    public static void 指令(final RegisterCommandsEvent 事件) {
        事件.getDispatcher().register(Commands.literal("projecte").then(Commands.literal("reloademc").requires(执行者 -> 执行者.hasPermission(2)).executes(指令 -> {
            指令.getSource().sendSuccess(正在重载, true);
            long 时间 = System.currentTimeMillis();
            AbstractNSSTag.clearCreatedTags();
            CustomEMCParser.init();
            MinecraftServer 服务器 = ServerLifecycleHooks.getCurrentServer();
            try {
                EMCMappingHandler.map(服务器.getDataPackRegistries(), 服务器.getDataPackRegistries().getResourceManager());
                PECore.LOGGER.info("Registered {} EMC values. (took {} ms)", EMCMappingHandler.getEmcMapSize(), System.currentTimeMillis()-时间);
                PacketHandler.sendFragmentedEmcPacketToAll();
            } catch(Throwable t) { PECore.LOGGER.error("Error calculating EMC values", t); }
            指令.getSource().sendSuccess(完成重载, true);
            return Command.SINGLE_SUCCESS;
        })));
    }
}