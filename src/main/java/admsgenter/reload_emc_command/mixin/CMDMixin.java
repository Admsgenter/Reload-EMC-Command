package admsgenter.reload_emc_command.mixin;

import admsgenter.reload_emc_command.ReloadEMCCommand;
import moze_intel.projecte.network.commands.RemoveEmcCMD;
import moze_intel.projecte.network.commands.ResetEmcCMD;
import moze_intel.projecte.network.commands.SetEmcCMD;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin({RemoveEmcCMD.class, ResetEmcCMD.class, SetEmcCMD.class})
public class CMDMixin {
    @ModifyArg(
        at = @At(ordinal = 1, target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Ljava/util/function/Supplier;Z)V", value = "INVOKE"),
        method = {
            "removeEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "resetEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "setEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;J)I"
        }
    )
    private static Supplier<Component> sendSuccess(Supplier<Component> 文本) { return ReloadEMCCommand.重载提示; }
}