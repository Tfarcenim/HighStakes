package tfar.highstakes.mixin;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.highstakes.HighStakes;

@Mixin(InventoryMenu.class)
@Debug(export = true)
public class InventoryMenuMixin {

    //3,4
    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",ordinal = 3))
    private Slot fixSlot(Slot par1) {
        return HighStakes.getLockedSlot(par1);
    }

    //3,4
    @ModifyArg(method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/InventoryMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",ordinal = 4))
    private Slot fixSlot4(Slot par1) {
        return HighStakes.getLockedSlot(par1);
    }
}
