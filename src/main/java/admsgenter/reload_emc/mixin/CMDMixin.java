package admsgenter.reload_emc.mixin;

import admsgenter.reload_emc.ReloadEMC;
import moze_intel.projecte.network.commands.RemoveEmcCMD;
import moze_intel.projecte.network.commands.ResetEmcCMD;
import moze_intel.projecte.network.commands.SetEmcCMD;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({RemoveEmcCMD.class, ResetEmcCMD.class, SetEmcCMD.class})
public class CMDMixin {
    @ModifyArg(
        at = @At(ordinal = 1, target = "Lnet/minecraft/commands/CommandSourceStack;sendSuccess(Lnet/minecraft/network/chat/Component;Z)V", value = "INVOKE"),
        method = {
            "removeEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "resetEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "setEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;J)I"
        }
    )
    private static Component sendSuccess(Component 文本) { return ReloadEMC.重载提示; }
}