package tfar.highstakes.mixin;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow public int selected;

    @Overwrite
    public void swapPaint(double pDirection){
        selected = 0;
    }
}
