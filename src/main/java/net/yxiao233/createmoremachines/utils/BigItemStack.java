package net.yxiao233.createmoremachines.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import net.yxiao233.createmoremachines.CreateMoreMachines;

import java.util.Optional;

public class BigItemStack {
    private final ItemStack stack;
    public BigItemStack(ItemStack stack){
        this.stack = stack.copy();
    }
    public static BigItemStack of(ItemStack stack){
        return new BigItemStack(stack.copy());
    }
    public static Codec<ItemStack> CODEC = Codec.lazyInitialized(() -> {
        return RecordCodecBuilder.create((p_347288_) -> {
            return p_347288_.group(ItemStack.ITEM_NON_AIR_CODEC.fieldOf("id").forGetter(ItemStack::getItemHolder), ExtraCodecs.intRange(1, Integer.MAX_VALUE).fieldOf("count").orElse(1).forGetter(ItemStack::getCount), DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter((getter) -> {
                return ((PatchedDataComponentMap) getter.getComponents()).asPatch();
            })).apply(p_347288_, ItemStack::new);
        });
    });
    public static Optional<ItemStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial((p_330102_) -> {
            CreateMoreMachines.LOGGER.error("Tried to load invalid item: '{}'", p_330102_);
        });
    }

    public static ItemStack parseOptional(HolderLookup.Provider lookupProvider, CompoundTag tag) {
        return tag.isEmpty() ? ItemStack.EMPTY : parse(lookupProvider, tag).orElse(ItemStack.EMPTY);
    }

    public Tag save(HolderLookup.Provider levelRegistryAccess, Tag outputTag) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty ItemStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(stack, CODEC, levelRegistryAccess, outputTag);
        }
    }

    public Tag saveOptional(HolderLookup.Provider levelRegistryAccess) {
        return (Tag)(stack.isEmpty() ? new CompoundTag() : this.save(levelRegistryAccess, new CompoundTag()));
    }
}
