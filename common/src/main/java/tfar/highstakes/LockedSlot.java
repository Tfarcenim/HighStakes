package tfar.highstakes;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class LockedSlot extends Slot {
    public LockedSlot(Container $$0, int $$1, int $$2, int $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public boolean mayPickup(Player $$0) {
        return false;
    }
}
