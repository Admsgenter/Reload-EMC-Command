package admsgenter.reload_emc.mixin;

import admsgenter.reload_emc.ReloadEMC;
import moze_intel.projecte.network.commands.RemoveEmcCMD;
import moze_intel.projecte.network.commands.ResetEmcCMD;
import moze_intel.projecte.network.commands.SetEmcCMD;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({RemoveEmcCMD.class, ResetEmcCMD.class, SetEmcCMD.class})
public class CMDMixin {
    @ModifyArg(
        at = @At(ordinal = 1, target = "Lnet/minecraft/command/CommandSource;sendSuccess(Lnet/minecraft/util/text/ITextComponent;Z)V", value = "INVOKE"),
        method = {
            "removeEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "resetEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;)I",
            "setEmc(Lcom/mojang/brigadier/context/CommandContext;Lmoze_intel/projecte/network/commands/parser/NSSItemParser$NSSItemResult;J)I"
        }
    )
    private static ITextComponent sendSuccess(ITextComponent 文本) { return ReloadEMC.重载提示; }
}