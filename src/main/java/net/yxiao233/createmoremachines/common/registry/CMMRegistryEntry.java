package net.yxiao233.createmoremachines.common.registry;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MapColor;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.CMMBuilderTransformers;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlockEntity;
import net.yxiao233.createmoremachines.api.registry.CMMRegistrate;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CMMRegistryEntry {
    public static final  CMMRegistrate REGISTRATE = CreateMoreMachines.registrate();
    //Casing
    public static final BlockEntry<CasingBlock> CREATIVE_CASING = casing("creative", CMMSpriteShifts.CREATIVE_CASING);
    public static final BlockEntry<CasingBlock> NETHERITE_CASING = casing("netherite", CMMSpriteShifts.NETHERITE_CASING);
    //Alloy
    public static final ItemEntry<Item> NETHERITE_ALLOY = item("netherite_alloy", Item::new);
    public static final ItemEntry<Item> END_ALLOY = item("end_alloy", Item::new);
    public static final ItemEntry<Item> BEYOND_ALLOY = item("beyond_alloy", Item::new);
    //Depot
    private static final Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> DEPOT_STORAGE_TYPES = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntry<CMMDepotBlock>> DEPOTS = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntityEntry<CMMDepotBlockEntity>> DEPOT_ENTITIES = new HashMap<>();
    //Mechanical Press
    private static final Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> MECHANICAL_PRESSES = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntityEntry<CMMMechanicalPressBlockEntity>> MECHANICAL_PRESS_ENTITIES = new HashMap<>();
    //Mechanical Mixer
    private static final Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> MECHANICAL_MIXERS = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntityEntry<CMMMechanicalMixerBlockEntity>> MECHANICAL_MIXER_ENTITIES = new HashMap<>();
    //Spout
    private static final Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> SPOUTS = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntityEntry<CMMSpoutBlockEntity>> SPOUT_ENTITIES = new HashMap<>();

    public static Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> getAllDepotStorageTypes(){
        return Collections.unmodifiableMap(DEPOT_STORAGE_TYPES);
    }
    public static Map<ResourceLocation, BlockEntry<CMMDepotBlock>> getDepots(){
        return Collections.unmodifiableMap(DEPOTS);
    }
    public static Map<ResourceLocation, BlockEntityEntry<CMMDepotBlockEntity>> getDepotEntities() {
        return Collections.unmodifiableMap(DEPOT_ENTITIES);
    }

    public static Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> getMechanicalPresses(){
        return Collections.unmodifiableMap(MECHANICAL_PRESSES);
    }

    public static Map<ResourceLocation, BlockEntityEntry<CMMMechanicalPressBlockEntity>> getMechanicalPressEntities(){
        return Collections.unmodifiableMap(MECHANICAL_PRESS_ENTITIES);
    }

    public static Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> getMechanicalMixers(){
        return Collections.unmodifiableMap(MECHANICAL_MIXERS);
    }

    public static Map<ResourceLocation, BlockEntityEntry<CMMMechanicalMixerBlockEntity>> getMechanicalMixerEntities(){
        return Collections.unmodifiableMap(MECHANICAL_MIXER_ENTITIES);
    }

    public static Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> getSpouts(){
        return Collections.unmodifiableMap(SPOUTS);
    }

    public static Map<ResourceLocation, BlockEntityEntry<CMMSpoutBlockEntity>> getSpoutEntities(){
        return Collections.unmodifiableMap(SPOUT_ENTITIES);
    }

    public static void register() {
        CMMTierManager.registryDepotTypes(DEPOT_STORAGE_TYPES);
        CMMTierManager.registryDepots(getAllDepotStorageTypes(),DEPOTS);
        CMMTierManager.registryDepotEntities(getDepots(),DEPOT_ENTITIES);

        CMMTierManager.registryMechanicalPresses(MECHANICAL_PRESSES);
        CMMTierManager.registryMechanicalPressEntities(getMechanicalPresses(),MECHANICAL_PRESS_ENTITIES);

        CMMTierManager.registryMechanicalMixers(MECHANICAL_MIXERS);
        CMMTierManager.registryMechanicalMixerEntities(getMechanicalMixers(),MECHANICAL_MIXER_ENTITIES);

        CMMTierManager.registrySpouts(SPOUTS);
        CMMTierManager.registrySpoutEntities(getSpouts(),SPOUT_ENTITIES);
    }

    @SuppressWarnings("SameParameterValue")
    private static BlockEntry<CasingBlock> casing(String name, CTSpriteShiftEntry spriteEntry){
        return REGISTRATE.block(name + "_casing", CasingBlock::new)
                .properties(properties -> properties.mapColor(MapColor.PODZOL))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .transform(CMMBuilderTransformers.casing(() -> spriteEntry))
                .register();
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> itemFunction){
        //TODO move no item model while has all textures
        return REGISTRATE
                .item(name,itemFunction)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }
}
