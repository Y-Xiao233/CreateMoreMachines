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
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.kinetics.deployer.DeployerMovingInteraction;
import com.simibubi.create.content.kinetics.fan.FanVisual;
import com.simibubi.create.content.kinetics.saw.SawVisual;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlockEntity;
import net.yxiao233.createmoremachines.api.content.crushing_wheel.CMMCrushingWheelBlock;
import net.yxiao233.createmoremachines.api.content.crushing_wheel.CMMCrushingWheelBlockEntity;
import net.yxiao233.createmoremachines.api.content.crushing_wheel.CMMCrushingWheelControllerBlock;
import net.yxiao233.createmoremachines.api.content.crushing_wheel.CMMCrushingWheelControllerBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotMountedStorageType;
import net.yxiao233.createmoremachines.api.content.encased_fan.CMMEncasedFanBlock;
import net.yxiao233.createmoremachines.api.content.encased_fan.CMMEncasedFanBlockEntity;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankBlock;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankBlockEntity;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankItem;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankModel;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerVisual;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMixerVisual;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMPressVisual;
import net.yxiao233.createmoremachines.api.content.mechanical.saw.CMMMechanicalSawBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.saw.CMMMechanicalSawBlockEntity;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlockEntity;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineBlock;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineBlockEntity;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineVisual;
import net.yxiao233.createmoremachines.datagen.content.CMMBasinGenerator;
import net.yxiao233.createmoremachines.datagen.content.CMMBlockStateGen;
import net.yxiao233.createmoremachines.datagen.content.CMMFluidTankGenerator;
import net.yxiao233.createmoremachines.utils.AnnotationUtil;
import net.yxiao233.createmoremachines.datagen.content.CMMAssetLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    }

    public static void registryDepotTypes(Map<ResourceLocation, RegistryEntry<MountedItemStorageType<?>, ?>> storageTypeMap){
        Map<ResourceLocation, CMMTier> tiers = CMMTier.getTiers();
        tiers.forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.DEPOT)) {
                storageTypeMap.put(id, CMMTier.getRegistrate(id.getNamespace()).mountedItemStorage(id.getPath() + "_depot", () -> new CMMDepotMountedStorageType(tier)).register());
            }
        });
    }

    //mechanical press
    @SuppressWarnings("unchecked")
    public static void registryMechanicalPressEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalPressBlockEntity>> entityMap){
        blockMap.forEach((id, press) ->{
            CreateBlockEntityBuilder<CMMMechanicalPressBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_mechanical_press", (type, pos, state) -> new CMMMechanicalPressBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder
                    .visual(() -> CMMPressVisual::new)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getMechanicalPressRenderer())
                    .register()
            );
        });
    }

    public static void registryMechanicalPresses(Map<ResourceLocation, BlockEntry<CMMMechanicalPressBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.PRESS)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_mechanical_press", properties -> new CMMMechanicalPressBlock(tier,properties))
                        .initialProperties(SharedProperties::stone)
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.PRESS)))
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
    }

    //mechanical mixer
    @SuppressWarnings("unchecked")
    public static void registryMechanicalMixerEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalMixerBlockEntity>> entityMap){
        blockMap.forEach((id, press) ->{
            CreateBlockEntityBuilder<CMMMechanicalMixerBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_mechanical_mixer", (type, pos, state) -> new CMMMechanicalMixerBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder.visual(() -> CMMMixerVisual::new)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getMechanicalMixerRenderer())
                    .register()
            );
        });
    }

    @SuppressWarnings("removal")
    public static void registryMechanicalMixers(Map<ResourceLocation, BlockEntry<CMMMechanicalMixerBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.MIXER)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_mechanical_mixer", properties -> new CMMMechanicalMixerBlock(tier,properties))
                        .initialProperties(SharedProperties::stone)
                        .properties(properties -> {
                            return properties.noOcclusion().mapColor(MapColor.STONE);
                        })
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.MIXER)))
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
    }

    //spout
    @SuppressWarnings("unchecked")
    public static void registrySpoutEntities(Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMSpoutBlockEntity>> entityMap){
        blockMap.forEach((id, spout) ->{
            entityMap.put(id,CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_spout", (type, pos, state) -> new CMMSpoutBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getSpoutRenderer())
                    .register()
            );
        });
    }

    @SuppressWarnings("removal")
    public static void registrySpouts(Map<ResourceLocation, BlockEntry<CMMSpoutBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.SPOUT)){
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
    }
    //basin
    @SuppressWarnings("unchecked")
    public static void registryBasinEntities(Map<ResourceLocation, BlockEntry<CMMBasinBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMBasinBlockEntity>> entityMap){
        blockMap.forEach((id, spout) ->{
            entityMap.put(id,CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_basin", (type, pos, state) -> new CMMBasinBlockEntity(CMMTier.getTiers().get(id),type,pos,state))
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getBasinRenderer())
                    .register()
            );
        });
    }

    @SuppressWarnings("removal")
    public static void registryBasins(Map<ResourceLocation, BlockEntry<CMMBasinBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.BASIN)){
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
    }

    //deployer
    @SuppressWarnings("unchecked")
    public static void registryDeployers(Map<ResourceLocation, BlockEntry<CMMDeployerBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.DEPLOYER)){
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
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.DEPLOYER)))
                        .item(AssemblyOperatorBlockItem::new)
                        .tag(new TagKey[]{AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag})
                        .transform(ModelGen.customItemModel("deployer","item","_"))
                        .register()
                );
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void registryDeployerEntities(Map<ResourceLocation, BlockEntry<CMMDeployerBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMDeployerBlockEntity>> entityMap){
        blockMap.forEach((id, spout) ->{
            CreateBlockEntityBuilder<CMMDeployerBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_deployer", (type, pos, state) -> new CMMDeployerBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
            entityMap.put(id,builder
                    .visual(() -> CMMDeployerVisual::new)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getDeployerRenderer())
                    .register()
            );
        });
    }

    //fluid tank
    @SuppressWarnings("removal")
    public static void registryFluidTankBlocks(Map<ResourceLocation, BlockEntry<CMMFluidTankBlock>> blockMap){
        CMMTier.getTiers().forEach((id,tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.FLUID_TANK)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_fluid_tank", properties -> new CMMFluidTankBlock(tier,properties))
                        .initialProperties(SharedProperties::copperMetal)
                        .properties(properties -> properties.noOcclusion().isRedstoneConductor((blockState, blockGetter, blockPos) -> true))
                        .transform(TagGen.pickaxeOnly())
                        .blockstate(new CMMFluidTankGenerator()::generate)
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .onRegister(CreateRegistrate.blockModel(() -> {
                            return model -> CMMFluidTankModel.byTier(model, tier);
                        }))
                        .transform(DisplaySource.displaySource(AllDisplaySources.BOILER))
                        .transform(MountedFluidStorageType.mountedFluidStorage(AllMountedStorageTypes.FLUID_TANK))
                        .onRegister(MovementBehaviour.movementBehaviour(new FluidTankMovementBehavior()))
                        .addLayer(() ->{
                            return RenderType::cutoutMipped;
                        })
                        .item(CMMFluidTankItem::new)
                        .model(AssetLookup.customBlockItemModel("fluid_tank","_","block_single_window"))
                        .build()
                        .register()
                );
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void registryFluidTankEntities(Map<ResourceLocation, BlockEntry<CMMFluidTankBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMFluidTankBlockEntity>> entityMap){
        blockMap.forEach((id, spout) ->{
            CreateBlockEntityBuilder<CMMFluidTankBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_fluid_tank", (type, pos, state) -> new CMMFluidTankBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
            entityMap.put(id,builder
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getFluidTankRenderer())
                    .register()
            );
        });
    }

    //steam engine
    public static void registrySteamEngines(Map<ResourceLocation, BlockEntry<CMMSteamEngineBlock>> blockMap){
        CMMTier.getTiers().forEach((id,tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.STEAM_ENGINE)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_steam_engine", properties -> new CMMSteamEngineBlock(tier,properties))
                        .initialProperties(SharedProperties::copperMetal)
                        .transform(TagGen.pickaxeOnly())
                        .setData(ProviderType.LANG,NonNullBiConsumer.noop())
                        .blockstate((context, provider) ->{
                            provider.horizontalFaceBlock(context.get(), CMMAssetLookup.partialBaseModel(context, provider,"steam_engine","/block"));
                        })
                        .onRegister(CMMBlockStressValues.setGeneratorSpeed((int) tier.getSpecial(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed"),true))
                        .onRegister(CMMBlockStressValues.setCapacities((int) tier.getSpecial(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity")))
                        .item()
                        .transform(ModelGen.customItemModel("steam_engine","_","item"))
                        .register()
                );
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void registrySteamEngineEntities(Map<ResourceLocation, BlockEntry<CMMSteamEngineBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMSteamEngineBlockEntity>> entityMap){
        blockMap.forEach((id, steamEngine) ->{
            CreateBlockEntityBuilder<CMMSteamEngineBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_steam_engine", (type, pos, state) -> new CMMSteamEngineBlockEntity(CMMTier.getTiers().get(id), type, pos, state));
            entityMap.put(id,builder
                    .visual(() -> CMMSteamEngineVisual::new,false)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getSteamEngineRenderer())
                    .register()
            );
        });
    }

    //saw
    @SuppressWarnings("unchecked")
    public static void registrySawEntities(Map<ResourceLocation, BlockEntry<CMMMechanicalSawBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMMechanicalSawBlockEntity>> entityMap){
        blockMap.forEach((id, saw) ->{
            CreateBlockEntityBuilder<CMMMechanicalSawBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_mechanical_saw", (type, pos, state) -> new CMMMechanicalSawBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder
                    .visual(() -> SawVisual::new)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getSawRenderer())
                    .register()
            );
        });
    }

    public static void registrySaws(Map<ResourceLocation, BlockEntry<CMMMechanicalSawBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.SAW)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_mechanical_saw", properties -> new CMMMechanicalSawBlock(tier,properties))
                        .initialProperties(SharedProperties::stone)
                        .properties(properties -> {
                            return properties.noOcclusion().mapColor(MapColor.PODZOL);
                        })
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.SAW)))
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .transform(TagGen.axeOrPickaxe())
                        .blockstate(NonNullBiConsumer.noop())
                        .item(AssemblyOperatorBlockItem::new)
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .model(NonNullBiConsumer.noop())
                        .build()
                        .register()
                );
            }
        });
    }

    //Encased Fan
    @SuppressWarnings("unchecked")
    public static void registryEncasedFanEntities(Map<ResourceLocation, BlockEntry<CMMEncasedFanBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMEncasedFanBlockEntity>> entityMap){
        blockMap.forEach((id, encasedFan) ->{
            CreateBlockEntityBuilder<CMMEncasedFanBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_encased_fan", (type, pos, state) -> new CMMEncasedFanBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder
                    .visual(() -> FanVisual::new)
                    .validBlocks(new NonNullSupplier[]{blockMap.get(id)})
                    .renderer(CMMTier.getTiers().get(id).getEncasedFanRenderer())
                    .register()
            );
        });
    }

    @SuppressWarnings("removal")
    public static void registerEncasedFans(Map<ResourceLocation, BlockEntry<CMMEncasedFanBlock>> blockMap) {
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.ENCASED_FAN)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_encased_fan", properties -> new CMMEncasedFanBlock(tier,properties))
                        .initialProperties(SharedProperties::stone)
                        .properties(properties -> {
                            return properties.noOcclusion().mapColor(MapColor.PODZOL);
                        })
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN)))
                        .addLayer(() -> RenderType::cutoutMipped)
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .transform(TagGen.axeOrPickaxe())
                        .blockstate(NonNullBiConsumer.noop())
                        .item(AssemblyOperatorBlockItem::new)
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .model(NonNullBiConsumer.noop())
                        .build()
                        .register()
                );
            }
        });
    }

    //Crushing Wheel
    @SuppressWarnings("removal")
    public static void registerCrushingWheels(Map<ResourceLocation, BlockEntry<CMMCrushingWheelBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_crushing_wheel", properties -> new CMMCrushingWheelBlock(tier,properties))
                        .properties(properties -> properties.mapColor(MapColor.METAL))
                        .initialProperties(SharedProperties::stone)
                        .properties(BlockBehaviour.Properties::noOcclusion)
                        .transform(TagGen.pickaxeOnly())
                        .onRegister(CMMBlockStressValues.setImpact(tier.getMechanicalImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)))
                        .addLayer(() -> RenderType::cutoutMipped)
                        .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .item()
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .model(NonNullBiConsumer.noop())
                        .build()
                        .register()
                );
            }
        });
    }

    public static void registryCrushingWheelEntities(Map<ResourceLocation, BlockEntry<CMMCrushingWheelBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMCrushingWheelBlockEntity>> entityMap){
        blockMap.forEach((id, crushingWheel) ->{
            CreateBlockEntityBuilder<CMMCrushingWheelBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_crushing_wheel", (type, pos, state) -> new CMMCrushingWheelBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder
                    .visual(() -> SingleAxisRotatingVisual.of(CMMPartialModelsRegistry.getPartialModels(CMMTier.getTiers().get(id), "crushing_wheel")[0]), false)
                    .validBlocks(crushingWheel)
                    .renderer(() -> KineticBlockEntityRenderer::new)
                    .register()
            );
        });
    }

    public static void registerCrushingWheelControllers(Map<ResourceLocation, BlockEntry<CMMCrushingWheelControllerBlock>> blockMap){
        CMMTier.getTiers().forEach((id, tier) ->{
            if(CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)){
                blockMap.put(id, CMMTier.getRegistrate(id.getNamespace()).block(id.getPath() + "_crushing_wheel_controller", properties -> new CMMCrushingWheelControllerBlock(tier,properties))
                        .properties(properties -> properties.mapColor(MapColor.STONE).noOcclusion().noLootTable().air().noCollission().pushReaction(PushReaction.BLOCK))
                        .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                        .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                        .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                        .register()
                );
            }
        });
    }

    public static void registryCrushingWheelControllerEntities(Map<ResourceLocation, BlockEntry<CMMCrushingWheelControllerBlock>> blockMap, Map<ResourceLocation, BlockEntityEntry<CMMCrushingWheelControllerBlockEntity>> entityMap){
        blockMap.forEach((id, controller) ->{
            CreateBlockEntityBuilder<CMMCrushingWheelControllerBlockEntity, CreateRegistrate> builder = CMMTier.getRegistrate(id.getNamespace()).blockEntity(id.getPath() + "_crushing_wheel_controller", (type, pos, state) -> new CMMCrushingWheelControllerBlockEntity(CMMTier.getTiers().get(id),type,pos,state));
            entityMap.put(id,builder
                    .validBlocks(controller)
                    .register()
            );
        });
    }

    public static void registryModels(){
        PLUGINS.forEach(ICMMPlugin::registryPartialModels);
    }

    public static List<ICMMPlugin> getPlugins(){
        return PLUGINS;
    }
}
