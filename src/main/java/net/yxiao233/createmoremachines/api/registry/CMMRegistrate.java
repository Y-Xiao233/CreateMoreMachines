package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.api.registry.registrate.SimpleBuilder;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class CMMRegistrate extends AbstractRegistrate<CMMRegistrate> {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected CMMRegistrate(String modid) {
        super(modid);
    }
    protected @Nullable Function<Item, TooltipModifier> currentTooltipModifierFactory;

    public static CMMRegistrate create(String modid) {
        return new CMMRegistrate(modid);
    }

    public <T extends BlockEntity, P> @NotNull CreateBlockEntityBuilder<T, P> blockEntity(@NotNull P parent, @NotNull String name, BlockEntityBuilder.@NotNull BlockEntityFactory<T> factory) {
        return (CreateBlockEntityBuilder<T,P>)this.entry(name, (callback) -> {
            return CreateBlockEntityBuilder.create(this, parent, name, callback, factory);
        });
    }

    public <T extends BlockEntity> @NotNull CreateBlockEntityBuilder<T, CMMRegistrate> blockEntity(@NotNull String name, BlockEntityBuilder.@NotNull BlockEntityFactory<T> factory) {
        return this.blockEntity(this.self(), name, factory);
    }

    public CMMRegistrate setTooltipModifierFactory(@Nullable Function<Item, TooltipModifier> factory) {
        this.currentTooltipModifierFactory = factory;
        return this.self();
    }

    public @Nullable Function<Item, TooltipModifier> getTooltipModifierFactory() {
        return this.currentTooltipModifierFactory;
    }

    public <T extends MountedItemStorageType<?>> SimpleBuilder<MountedItemStorageType<?>, T, CMMRegistrate> mountedItemStorage(String name, Supplier<T> supplier) {
        return this.entry(name, (callback) -> {
            return (new SimpleBuilder<>(this, this, name, callback, CreateRegistries.MOUNTED_ITEM_STORAGE_TYPE, supplier)).byBlock(MountedItemStorageType.REGISTRY);
        });
    }
}
