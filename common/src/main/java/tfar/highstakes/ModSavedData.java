package tfar.highstakes;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModSavedData extends SavedData {

    private HashSet<UUID> joined = new HashSet<>();

    public ModSavedData(ServerLevel serverLevel) {

    }

    @Nullable
    public static ModSavedData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    @Nullable
    public static ModSavedData getDefaultInstance(MinecraftServer server) {
        return getInstance(server.overworld());
    }

    public static ModSavedData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new ModSavedData(serverLevel),name(serverLevel));
    }

    private static String name(ServerLevel level) {
        return  HighStakes.MOD_ID+"_"+level.dimension().location().toString().replace(':','.');
    }

    public static ModSavedData getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    public static ModSavedData loadStatic(CompoundTag compoundTag,ServerLevel level) {
        ModSavedData id = new ModSavedData(level);
        id.load(compoundTag,level);
        return id;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        Tag tag2 = UUIDUtil.CODEC.listOf().xmap(HashSet::new,ArrayList::new).encodeStart(NbtOps.INSTANCE,joined).resultOrPartial(HighStakes.LOG::error).orElseThrow();
        compoundTag.put("joined_before",tag2);
        return compoundTag;
    }

    public void load(CompoundTag tag,ServerLevel level) {
        joined = UUIDUtil.CODEC.listOf().xmap(HashSet::new,ArrayList::new).parse(new Dynamic<>(NbtOps.INSTANCE,tag.get("joined_before"))).resultOrPartial(HighStakes.LOG::error).orElseThrow();
    }

    public boolean hasJoinedBefore(ServerPlayer player) {
        return joined.contains(player.getUUID());
    }

    public void addPlayer(ServerPlayer player) {
        joined.add(player.getUUID());
        setDirty();
    }
}
