package net.yxiao233.createmoremachines.common.registry;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.*;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidType;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.CMMBuilderTransformers;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.fluid.CMMBaseFluidInstance;
import net.yxiao233.createmoremachines.api.content.fluid.ClientFluidTypeExtensions;
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

@SuppressWarnings("unused")
public class CMMRegistryEntry {
    public static final  CMMRegistrate REGISTRATE = CreateMoreMachines.registrate();
    //Casing
    public static final BlockEntry<CasingBlock> CREATIVE_CASING = casing("creative", CMMSpriteShifts.CREATIVE_CASING, SoundType.AMETHYST);
    public static final BlockEntry<CasingBlock> NETHERITE_CASING = casing("netherite", CMMSpriteShifts.NETHERITE_CASING, SoundType.NETHERRACK);
    public static final BlockEntry<CasingBlock> END_CASING = casing("end", CMMSpriteShifts.END_CASING, SoundType.STONE);
    public static final BlockEntry<CasingBlock> BEYOND_CASING = casing("beyond", CMMSpriteShifts.BEYOND_CASING, SoundType.AMETHYST);
    //Alloy
    public static final ItemEntry<Item> NETHERITE_ALLOY = simpleItem("netherite_alloy");
    public static final ItemEntry<Item> END_ALLOY = simpleItem("end_alloy");
    public static final ItemEntry<Item> BEYOND_ALLOY = simpleItem("beyond_alloy");
    //Fluid
    public static final CMMBaseFluidInstance BINDER = simpleFluid("binder");
    public static final CMMBaseFluidInstance PURE_WATER = simpleFluid("pure_water");
    //Block
    public static final BlockEntry<Block> NETHERITE_ALLOY_BLOCK = simpleBlock("netherite_alloy_block");
    public static final BlockEntry<Block> END_ALLOY_BLOCK = simpleBlock("end_alloy_block");
    public static final BlockEntry<Block> BEYOND_ALLOY_BLOCK = simpleBlock("beyond_alloy_block");
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
    //Basin
    private static final Map<ResourceLocation, BlockEntry<CMMBasinBlock>> BASINS = new HashMap<>();
    private static final Map<ResourceLocation, BlockEntityEntry<CMMBasinBlockEntity>> BASIN_ENTITIES = new HashMap<>();

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

    public static Map<ResourceLocation, BlockEntry<CMMBasinBlock>> getBasins(){
        return Collections.unmodifiableMap(BASINS);
    }

    public static Map<ResourceLocation, BlockEntityEntry<CMMBasinBlockEntity>> getBasinEntities(){
        return Collections.unmodifiableMap(BASIN_ENTITIES);
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

        CMMTierManager.registryBasins(BASINS);
        CMMTierManager.registryBasinEntities(getBasins(),BASIN_ENTITIES);
    }

    private static BlockEntry<CasingBlock> casing(String name, CTSpriteShiftEntry spriteEntry, SoundType soundType){
        return REGISTRATE.block(name + "_casing", CasingBlock::new)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .transform(CMMBuilderTransformers.casing(() -> spriteEntry))
                .properties(properties -> properties.mapColor(MapColor.PODZOL).sound(soundType))
                .register();
    }

    private static <T extends Item> ItemEntry<T> simpleItem(String name, NonNullFunction<Item.Properties, T> itemFunction){
        return REGISTRATE
                .item(name,itemFunction)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .register();
    }

    private static ItemEntry<Item> simpleItem(String name){
        return REGISTRATE
                .item(name,Item::new)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .register();
    }

    private static CMMBaseFluidInstance simpleFluid(String name){
        return new CMMBaseFluidInstance(CreateMoreMachines.ITEMS, CreateMoreMachines.BLOCKS, CreateMoreMachines.FLUIDS, CreateMoreMachines.FLUID_TYPES, name,
                FluidType.Properties.create().density(1000),
                new ClientFluidTypeExtensions(CreateMoreMachines.makeId("block/fluids/" + name + "_still"), CreateMoreMachines.makeId("block/fluids/" + name + "_flow")));
    }

    private static BlockEntry<Block> simpleBlock(String name){
        return REGISTRATE.block(name, Block::new)
                .initialProperties(SharedProperties::stone)
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item()
                .build()
                .register();
    }
}
