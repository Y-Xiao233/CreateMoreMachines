package net.yxiao233.createmoremachines.api.content.depot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.contraption.storage.SyncedMountedStorage;
import com.simibubi.create.api.contraption.storage.item.WrapperMountedItemStorage;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class CMMDepotMountedStorage extends WrapperMountedItemStorage<CMMDepotMountedStorage.Handler> implements SyncedMountedStorage {
    public static final MapCodec<CMMDepotMountedStorage> CODEC;
    private boolean dirty;
    private final CMMTier tier;

    protected CMMDepotMountedStorage(ResourceLocation id, ItemStack stack) {
        super(CMMRegistryEntry.getAllDepotStorageTypes().get(id).get(), new CMMDepotMountedStorage.Handler(stack));
        this.wrapped.onChange = () -> {
            this.dirty = true;
        };
        this.tier = CMMTier.getTiers().get(id);
    }

    public void unmount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof DepotBlockEntity depot) {
            depot.setHeldItem(this.getStackInSlot(0));
        }

    }

    public boolean handleInteraction(ServerPlayer player, Contraption contraption, StructureTemplate.StructureBlockInfo info) {
        return false;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void markClean() {
        this.dirty = false;
    }

    public void afterSync(Contraption contraption, BlockPos localPos) {
        BlockEntity be = contraption.getBlockEntityClientSide(localPos);
        if (be instanceof DepotBlockEntity depot) {
            depot.setHeldItem(this.getItem());
        }
    }

    public void setItem(ItemStack stack) {
        this.setStackInSlot(0, stack);
    }

    public ItemStack getItem() {
        return this.getStackInSlot(0);
    }

    public CMMTier getTier() {
        return tier;
    }
    public ResourceLocation getId() {
        return tier.getId();
    }

    public static CMMDepotMountedStorage fromDepot(ResourceLocation id, DepotBlockEntity depot) {
        ItemStack held = depot.getHeldItem();
        return new CMMDepotMountedStorage(id,held.copy());
    }

    @SuppressWarnings("unused")
    public static CMMDepotMountedStorage fromLegacy(ResourceLocation id, HolderLookup.Provider registries, CompoundTag nbt) {
        ItemStackHandler handler = new ItemStackHandler();
        handler.deserializeNBT(registries, nbt);
        if (handler.getSlots() == 1) {
            ItemStack stack = handler.getStackInSlot(0);
            return new CMMDepotMountedStorage(id,stack);
        } else {
            return new CMMDepotMountedStorage(id,ItemStack.EMPTY);
        }
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        ResourceLocation.CODEC.fieldOf("id").forGetter(CMMDepotMountedStorage::getId),
                        ItemStack.OPTIONAL_CODEC.fieldOf("value").forGetter(CMMDepotMountedStorage::getItem)
                ).apply(builder, CMMDepotMountedStorage::new)
        );
    }
    public static final class Handler extends ItemStackHandler {
        private Runnable onChange = () -> {
        };

        private Handler(ItemStack stack) {
            super(1);
            this.setStackInSlot(0, stack);
        }

        protected void onContentsChanged(int slot) {
            this.onChange.run();
        }
    }
}
