package tfar.highstakes;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.highstakes.datagen.ModDatagen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(HighStakes.MOD_ID)
public class HighStakesForge {

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<Object>>>> registerLater = new HashMap<>();


    public HighStakesForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        bus.addListener(this::onInitialize);
        bus.addListener(this::registerObjs);
        bus.addListener(ModDatagen::gather);
        // Use Forge to bootstrap the Common mod.
        HighStakes.init();
        MinecraftForge.EVENT_BUS.addListener(this::respawn);
        MinecraftForge.EVENT_BUS.addListener(this::login);
    }

    void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.isEndConquered()) {
            blockSlots((ServerPlayer) event.getEntity());
        }
    }

    static void blockSlots(ServerPlayer player) {
        ItemStack barrier = barrier(player.serverLevel());
        for (int i = 1; i < player.getInventory().items.size();i++) {
            player.getInventory().items.set(i,barrier.copy());
        }
    }

    void login(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ModSavedData modSavedData = ModSavedData.getOrCreateDefaultInstance(player.server);
        if (!modSavedData.hasJoinedBefore(player)) {
            modSavedData.addPlayer(player);
            blockSlots(player);
        }
    }

    static ItemStack barrier(Level level) {
        ItemStack bingoCard = Items.BARRIER.getDefaultInstance();
        bingoCard.enchant(Enchantments.VANISHING_CURSE,1);
        bingoCard.hideTooltipPart(ItemStack.TooltipPart.ENCHANTMENTS);
        bingoCard.setHoverName(Component.literal("Blocked").withStyle(Style.EMPTY.withItalic(false)));
        return bingoCard;
    }

    public void registerObjs(RegisterEvent event) {
        Registry<?> registry = event.getVanillaRegistry();
        List<Pair<ResourceLocation, Supplier<Object>>> toRegister = registerLater.get(registry);
        if (toRegister != null) {
            for (Pair<ResourceLocation,Supplier<Object>> pair : toRegister) {
                event.register((ResourceKey<Registry<Object>>) registry.key(), pair.getLeft(), pair.getValue());
            }
        }
    }

    public void onInitialize(FMLCommonSetupEvent e) {
        registerLater.clear();
    }

}