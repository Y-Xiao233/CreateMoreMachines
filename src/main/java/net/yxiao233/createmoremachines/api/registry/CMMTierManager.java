package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.processing.basin.BasinGenerator;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotMountedStorageType;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlockEntity;
import net.yxiao233.createmoremachines.datagen.content.CMMBasinGenerator;
import net.yxiao233.createmoremachines.datagen.content.CMMBlockStateGen;
import net.yxiao233.createmoremachines.utils.AnnotationUtil;
import net.yxiao233.createmoremachines.datagen.content.CMMAssetLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CMMTierManager {
    private static final List<ICMMPlugin> PLUGINS = new ArrayList<>();
    @SuppressWarnings("deprecation")
    public static void loadAllPlugin(){
        AnnotationUtil.getAllClasses(CMMPlugin.class).forEach(clazz ->{
            if(ICMMPlugin.class.isAssignableFrom(clazz)){
                try {
                    ICMMPlugin plugin = (ICMMPlugin) clazz.newInstance();
                    PLUGINS.add(plugin);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void registryRegistrate(){
        PLUGINS.forEach(ICMMPlugin::registryRegistrate);
    }

    public static void registryTiers(){
        PLUGINS.forEach(ICMMPlugin::registryTiers);
    }

    //Depot
    @SuppressWarnings("unchecked")
    public static void registryDepotEntities(Map<ResourceLocation, BlockEntry<CMMDepotBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMDepotBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, depot) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    entityMap.put(id,plugin.getRegistrate().blockEntity(id.getPath() + "_depot", (type, pos, state) -> new CMMDepotBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                            .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                            .renderer(CMMTier.getTiers().get(id).getDepotRenderer())
                            .register()
                    );
                }
            });
        });
    }

    public static void registryDepots(Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> storageTypeMap, Map<ResourceLocation, BlockEntry<CMMDepotBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            storageTypeMap.forEach((id, type) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    blockMap.put(id,plugin.getRegistrate().block(id.getPath() + "_depot", properties -> new CMMDepotBlock(CMMTier.getTiers().get(id), properties))
                            .initialProperties(SharedProperties::stone)
                            .properties(properties -> {
                                return properties.mapColor(MapColor.COLOR_GRAY);
                            })
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(TagGen.axeOrPickaxe())
                            .blockstate((c, p) -> {
                                p.simpleBlock(c.getEntry(), CMMAssetLookup.partialBaseModel(c, p,"depot"));
                            })
                            .transform(DisplaySource.displaySource(AllDisplaySources.ITEM_NAMES))
                            .onRegister(MovingInteractionBehaviour.interactionBehaviour(new MountedDepotInteractionBehaviour()))
                            .transform(MountedItemStorageType.mountedItemStorage(type))
                            .item()
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(ModelGen.customItemModel("depot","_"))
                            .register());
                }
            });
        });
    }

    public static void registryDepotTypes(Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> storageTypeMap){
        PLUGINS.forEach(plugin ->{
            Map<ResourceLocation, CMMTier> tiers = CMMTier.getTiers();
            tiers.forEach((id, tier) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    storageTypeMap.put(id, plugin.getRegistrate().mountedItemStorage(id.getPath() + "_depot", () -> new CMMDepotMountedStorageType(CMMTier.getTiers().get(id))).register());
                }
            });
        });
    }

    //mechanical press
    @SuppressWarnings("unchecked")
    public static void registryMechanicalPressEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalPressBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, press) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    entityMap.put(id,plugin.getRegistrate().blockEntity(id.getPath() + "_mechanical_press", (type, pos, state) -> new CMMMechanicalPressBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                            .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                            .renderer(CMMTier.getTiers().get(id).getMechanicalPressRenderer())
                            .register()
                    );
                }
            });
        });
    }

    public static void registryMechanicalPresses(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, type) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    blockMap.put(id, plugin.getRegistrate().block(id.getPath() + "_mechanical_press", properties -> new CMMMechanicalPressBlock(CMMTier.getTiers().get(id),properties))
                            .initialProperties(SharedProperties::stone)
                            .onRegister(CMMBlockStressValues.setImpact(CMMTier.getTiers().get(id).getMechanicalPressImpact()))
                            .properties(properties -> {
                                return properties.noOcclusion().mapColor(MapColor.PODZOL);
                            })
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(TagGen.axeOrPickaxe())
                            .blockstate(CMMBlockStateGen.horizontalBlockProvider("mechanical_press"))
                            .item(AssemblyOperatorBlockItem::new)
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(ModelGen.customItemModel("mechanical_press","item","_"))
                            .register()
                    );
                }
            });
        });
    }

    //mechanical mixer
    @SuppressWarnings("unchecked")
    public static void registryMechanicalMixerEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalMixerBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, press) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    entityMap.put(id,plugin.getRegistrate().blockEntity(id.getPath() + "_mechanical_mixer", (type, pos, state) -> new CMMMechanicalMixerBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                            .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                            .renderer(CMMTier.getTiers().get(id).getMechanicalMixerRenderer())
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registryMechanicalMixers(Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, type) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    blockMap.put(id, plugin.getRegistrate().block(id.getPath() + "_mechanical_mixer", properties -> new CMMMechanicalMixerBlock(CMMTier.getTiers().get(id),properties))
                                    .initialProperties(SharedProperties::stone)
                                    .properties(properties -> {
                                        return properties.noOcclusion().mapColor(MapColor.STONE);
                                    })
                                    .onRegister(CMMBlockStressValues.setImpact(CMMTier.getTiers().get(id).getMechanicalMixerImpact()))
                                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                                    .transform(TagGen.axeOrPickaxe())
                                    .blockstate((c, p) -> {
                                        p.simpleBlock(c.getEntry(), CMMAssetLookup.partialBaseModel(c, p,"mechanical_mixer"));
                                    })
                                    .addLayer(() -> {
                                        return RenderType::cutoutMipped;
                                    })
                                    .item(AssemblyOperatorBlockItem::new)
                                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                                    .transform(ModelGen.customItemModel("mechanical_mixer","item","_"))
                                    .register()
                    );
                }
            });
        });
    }

    //spout
    @SuppressWarnings("unchecked")
    public static void registrySpoutEntities(Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMSpoutBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, spout) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    entityMap.put(id,plugin.getRegistrate().blockEntity(id.getPath() + "_spout", (type, pos, state) -> new CMMSpoutBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                            .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                            .renderer(CMMTier.getTiers().get(id).getSpoutRenderer())
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registrySpouts(Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, type) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    blockMap.put(id, plugin.getRegistrate().block(id.getPath() + "_spout", properties -> new CMMSpoutBlock(CMMTier.getTiers().get(id),properties))
                            .initialProperties(SharedProperties::copperMetal)
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(TagGen.axeOrPickaxe())
                            .blockstate((c, p) -> {
                                p.simpleBlock(c.getEntry(), CMMAssetLookup.partialBaseModel(c, p,"spout"));
                            })
                            .addLayer(() -> {
                                return RenderType::cutoutMipped;
                            })
                            .item(AssemblyOperatorBlockItem::new)
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(ModelGen.customItemModel("spout","item","_"))
                            .register()
                    );
                }
            });
        });
    }
    //basin
    @SuppressWarnings("unchecked")
    public static void registryBasinEntities(Map<ResourceLocation, BlockEntry<CMMBasinBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMBasinBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, spout) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    entityMap.put(id,plugin.getRegistrate().blockEntity(id.getPath() + "_basin", (type, pos, state) -> new CMMBasinBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                            .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                            .renderer(CMMTier.getTiers().get(id).getBasinRenderer())
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registryBasins(Map<ResourceLocation, BlockEntry<CMMBasinBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, type) ->{
                if(id.getNamespace().equals(plugin.getRegistrate().getModid())){
                    blockMap.put(id, plugin.getRegistrate().block(id.getPath() + "_basin", properties -> new CMMBasinBlock(CMMTier.getTiers().get(id),properties))
                            .initialProperties(SharedProperties::stone)
                            .properties(properties -> {
                                return properties.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK);
                            })
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(TagGen.pickaxeOnly())
                            .blockstate(new CMMBasinGenerator()::generate)
                            .addLayer(() -> {
                                return RenderType::cutoutMipped;
                            })
                            .onRegister(MovementBehaviour.movementBehaviour(new BasinMovementBehaviour()))
                            .item()
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(ModelGen.customItemModel("basin","_"))
                            .register()
                    );
                }
            });
        });
    }
}
