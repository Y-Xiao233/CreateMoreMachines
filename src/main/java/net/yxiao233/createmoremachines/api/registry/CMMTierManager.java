package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.content.fluids.tank.*;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.kinetics.deployer.DeployerMovingInteraction;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineVisual;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotMountedStorageType;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankBlock;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerVisual;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMixerVisual;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMPressVisual;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlockEntity;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineBlock;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineBlockEntity;
import net.yxiao233.createmoremachines.datagen.content.CMMBasinGenerator;
import net.yxiao233.createmoremachines.datagen.content.CMMBlockStateGen;
import net.yxiao233.createmoremachines.utils.AnnotationUtil;
import net.yxiao233.createmoremachines.datagen.content.CMMAssetLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CodeBlock2Expr")
public class CMMTierManager {
    private static final List<ICMMPlugin> PLUGINS = new ArrayList<>();
    @SuppressWarnings("deprecation")
    public static void loadAllPlugin(){
        AnnotationUtil.getAllClasses(CMMPlugin.class).forEach(clazz ->{
            if(ICMMPlugin.class.isAssignableFrom(clazz)){
                try {
                    ICMMPlugin plugin = (ICMMPlugin) clazz.newInstance();
                    if(plugin.shouldLoad()){
                        PLUGINS.add(plugin);
                        CreateMoreMachines.LOGGER.info("[Create More Machines] Discovered CMMPlugin: {}", clazz.getName());
                    }else{
                        CreateMoreMachines.LOGGER.info("[Create More Machines] Skip CMMPlugin: {}", clazz.getName());
                    }
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
                entityMap.put(id,CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_depot", (type, pos, state) -> new CMMDepotBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getDepotRenderer())
                        .register()
                );
            });
        });
    }

    public static void registryDepots(Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> storageTypeMap, Map<ResourceLocation, BlockEntry<CMMDepotBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            storageTypeMap.forEach((id, type) ->{
                blockMap.put(id,CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_depot", properties -> new CMMDepotBlock(CMMTier.getTiers().get(id), properties))
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
                        .register()
                );
            });
        });
    }

    public static void registryDepotTypes(Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> storageTypeMap){
        PLUGINS.forEach(plugin ->{
            Map<ResourceLocation, CMMTier> tiers = CMMTier.getTiers();
            tiers.forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineType.DEPOT)) {
                    storageTypeMap.put(id, CMMTier.getRegistrate(id.getNamespace()).mountedItemStorage(id.getPath() + "_depot", () -> new CMMDepotMountedStorageType(tier)).register());
                }
            });
        });
    }

    //mechanical press
    @SuppressWarnings("unchecked")
    public static void registryMechanicalPressEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalPressBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, press) ->{
                CreateBlockEntityBuilder<CMMMechanicalPressBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_mechanical_press", (type, pos, state) -> new CMMMechanicalPressBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
                entityMap.put(id,builder
                        .visual(() -> CMMPressVisual::new)
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getMechanicalPressRenderer())
                        .register()
                );
            });
        });
    }

    public static void registryMechanicalPresses(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineType.PRESS)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_mechanical_press", properties -> new CMMMechanicalPressBlock(tier,properties))
                            .initialProperties(SharedProperties::stone)
                            .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalPressImpact()))
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
                CreateBlockEntityBuilder<CMMMechanicalMixerBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_mechanical_mixer", (type, pos, state) -> new CMMMechanicalMixerBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
                entityMap.put(id,builder.visual(() -> CMMMixerVisual::new)
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getMechanicalMixerRenderer())
                        .register()
                );
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registryMechanicalMixers(Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineType.MIXER)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_mechanical_mixer", properties -> new CMMMechanicalMixerBlock(tier,properties))
                            .initialProperties(SharedProperties::stone)
                            .properties(properties -> {
                                return properties.noOcclusion().mapColor(MapColor.STONE);
                            })
                            .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalMixerImpact()))
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
                entityMap.put(id,CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_spout", (type, pos, state) -> new CMMSpoutBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getSpoutRenderer())
                        .register()
                );
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registrySpouts(Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineType.SPOUT)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_spout", properties -> new CMMSpoutBlock(tier,properties))
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
                entityMap.put(id,CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_basin", (type, pos, state) -> new CMMBasinBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getBasinRenderer())
                        .register()
                );
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registryBasins(Map<ResourceLocation, BlockEntry<CMMBasinBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineType.BASIN)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_basin", properties -> new CMMBasinBlock(tier,properties))
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

    @SuppressWarnings("unchecked")
    public static void registryDeployers(Map<ResourceLocation, BlockEntry<CMMDeployerBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id, tier) ->{
                if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineType.DEPLOYER)){
                    blockMap.put(id, (BlockEntry<CMMDeployerBlock>) CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_deployer", properties -> new CMMDeployerBlock(tier,properties))
                            .initialProperties(SharedProperties::stone)
                            .properties(properties -> {
                                return properties.mapColor(MapColor.PODZOL);
                            })
                            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                            .transform(TagGen.axeOrPickaxe())
                            .blockstate(CMMBlockStateGen.directionalAxisBlockProvider())
                            .onRegister(MovementBehaviour.movementBehaviour(new DeployerMovementBehaviour()))
                            .onRegister(MovingInteractionBehaviour.interactionBehaviour(new DeployerMovingInteraction()))
                            .onRegister(CMMBlockStressValues.setImpact(tier.getDeployerImpact()))
                            .item(AssemblyOperatorBlockItem::new)
                            .tag(new TagKey[]{AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag})
                            .transform(ModelGen.customItemModel("deployer","item","_"))
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public static void registryDeployerEntities(Map<ResourceLocation, BlockEntry<CMMDeployerBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMDeployerBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, spout) ->{
                CreateBlockEntityBuilder<CMMDeployerBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_deployer", (type, pos, state) -> new CMMDeployerBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
                entityMap.put(id,builder
                        .visual(() -> CMMDeployerVisual::new)
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getDeployerRenderer())
                        .register()
                );
            });
        });
    }

    @SuppressWarnings("removal")
    public static void registryFluidTankBlocks(Map<ResourceLocation, BlockEntry<CMMFluidTankBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id,tier) ->{
                if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineType.FLUID_TANK)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_fluid_tank", properties -> new CMMFluidTankBlock(tier,properties))
                            .initialProperties(SharedProperties::copperMetal)
                            .properties(properties -> properties.noOcclusion().isRedstoneConductor((blockState, blockGetter, blockPos) -> true))
                            .transform(TagGen.pickaxeOnly())
                            .blockstate(new FluidTankGenerator()::generate)
                            .onRegister(CreateRegistrate.blockModel(() ->{
                                return FluidTankModel::standard;
                            }))
                            .transform(DisplaySource.displaySource(AllDisplaySources.BOILER))
                            .transform(MountedFluidStorageType.mountedFluidStorage(AllMountedStorageTypes.FLUID_TANK))
                            .onRegister(MovementBehaviour.movementBehaviour(new FluidTankMovementBehavior()))
                            .addLayer(() ->{
                                return RenderType::cutoutMipped;
                            })
                            .item(FluidTankItem::new)
                            .model(AssetLookup.customBlockItemModel("_","block_single_window"))
                            .build()
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public static void registryFluidTankEntities(Map<ResourceLocation, BlockEntry<CMMFluidTankBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMFluidTankBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, spout) ->{
                CreateBlockEntityBuilder<CMMFluidTankBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_fluid_tank", (type, pos, state) -> new CMMFluidTankBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
                entityMap.put(id,builder
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getFluidTankRenderer())
                        .register()
                );
            });
        });
    }

    public static void registrySteamEngines(Map<ResourceLocation, BlockEntry<CMMSteamEngineBlock>> blockMap){
        PLUGINS.forEach(plugin ->{
            CMMTier.getTiers().forEach((id,tier) ->{
                if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineType.STEAM_ENGINE)){
                    blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_steam_engine", properties -> new CMMSteamEngineBlock(tier,properties))
                            .initialProperties(SharedProperties::copperMetal)
                            .transform(TagGen.pickaxeOnly())
                            .blockstate((context, provider) ->{
                                provider.horizontalFaceBlock(context.get(), AssetLookup.partialBaseModel(context, provider));
                            })
                            .onRegister(CMMBlockStressValues.setGeneratorSpeed(tier.getSteamEngineGeneratedSpeed(),true))
                            .onRegister(CMMBlockStressValues.setCapacities(tier.getSteamEngineCapacity()))
                            .item()
                            .transform(ModelGen.customItemModel())
                            .register()
                    );
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public static void registrySteamEngineEntities(Map<ResourceLocation, BlockEntry<CMMSteamEngineBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMSteamEngineBlockEntity>> entityMap){
        PLUGINS.forEach(plugin ->{
            blockMap.forEach((id, steamEngine) ->{
                CreateBlockEntityBuilder<CMMSteamEngineBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_steam_engine", (type, pos, state) -> new CMMSteamEngineBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
                entityMap.put(id,builder
                        .visual(() -> SteamEngineVisual::new,false)
                        .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                        .renderer(CMMTier.getTiers().get(id).getSteamEngineRenderer())
                        .register()
                );
            });
        });
    }

    public static void registryModels(){
        PLUGINS.forEach(ICMMPlugin::registryPartialModels);
    }

    public static List<ICMMPlugin> getPlugins(){
        return PLUGINS;
    }
}
