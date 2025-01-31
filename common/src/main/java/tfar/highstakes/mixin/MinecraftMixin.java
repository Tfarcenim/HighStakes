package tfar.highstakes.mixin;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import tfar.highstakes.HighStakes;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Final
    public Options options;

    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void init(CallbackInfo info) {
        for (int i = 0; i < 9; ++i) {
            this.options.keyHotbarSlots[i].consumeClick();
        }
    }
}