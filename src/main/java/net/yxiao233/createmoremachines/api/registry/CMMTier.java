package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.content.fluids.tank.FluidTankRenderer;
import com.simibubi.create.content.kinetics.deployer.DeployerRenderer;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineRenderer;
import com.simibubi.create.content.processing.basin.BasinRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.config.TierConfigBase;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlockEntity;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotRenderer;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerRenderer;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlockEntity;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressRenderer;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlockEntity;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutRenderer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedReturnValue","unused","rawtypes","unchecked"})
public class CMMTier {
    private static final Map<ResourceLocation, CMMTier> tiers = new HashMap<>();
    private final ResourceLocation id;
    private final Map<String, Object> values = new HashMap<>();
    private int processingMultiple = 1;
    private int deployerProcessingMultiple = 1;
    private int itemCapability = 64;
    private int fluidCapability = 1;
    private double mechanicalPressImpact = 8;
    private double mechanicalMixerImpact = 4;
    private double deployerImpact = 4;
    private int fluidTankCapability = 8;
    private double steamEngineCapacity = 1024;
    private int steamEngineGeneratedSpeed = 64;
    private static boolean frozen = false;
    private static final HashMap<String, CreateRegistrate> REGISTRATIONS = new HashMap<>();
    private static boolean registrateFrozen = false;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMDepotBlockEntity>>> depotRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalPressBlockEntity>>> mechanicalPressRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalMixerBlockEntity>>> mechanicalMixerRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMSpoutBlockEntity>>> spoutRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BasinRenderer>> basinRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, DeployerRenderer>> deployerRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, SteamEngineRenderer>> steamEngineRenderer;
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, FluidTankRenderer>> fluidTankRenderer;
    private CMMTier(ResourceLocation id){
        tiers.put(id,this);
        this.id = id;
    }

    public static void createRegistrate(String modid){
        if(registrateFrozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        REGISTRATIONS.put(modid,CreateRegistrate.create(modid).defaultCreativeTab((ResourceKey)null).setTooltipModifierFactory((item) -> {
            return (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        }));
    }

    public static void of(CreateRegistrate registrate){
        if(registrateFrozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        REGISTRATIONS.put(registrate.getModid(),registrate);
    }

    public static CreateRegistrate getRegistrate(String modid){
        return REGISTRATIONS.get(modid);
    }
    public static List<CreateRegistrate> getAllRegistrate(){
        return REGISTRATIONS.values().stream().toList();
    }

    public static CMMTier create(ResourceLocation id){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        CreateMoreMachines.LOGGER.info("[Create More Machines] Successfully created tier {}", id.toString());
        return new CMMTier(id);
    }

    public CMMTier fromConfig(TierConfigBase config){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        return this.setItemCapability(config.getItemCapability())
                .setFluidCapability(config.getFluidCapability())
                .setFluidTankCapability(config.getFluidTankCapability())
                .setProcessingMultiple(config.getProcessingMultiple())
                .setMechanicalPressImpact(config.getMechanicalPressImpact())
                .setMechanicalMixerImpact(config.getMechanicalMixerImpact())
                .setDeployerImpact(config.getDeployerImpact())
                .setDeployerProcessingMultiple(config.getDeployerProcessingMultiple())
                .setSteamEngineGeneratedSpeed(config.getSteamEngineGeneratedSpeed())
                .setSteamEngineCapacity(config.getSteamEngineCapacity());
    }
    public static void freezy(){
        frozen = true;
    }
    public static void freezyRegistrate(){
        registrateFrozen = true;
    }

    public static Map<ResourceLocation, CMMTier> getTiers() {
        return Collections.unmodifiableMap(tiers);
    }

    public CMMTier set(String key, Object value){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.values.put(key,value);
        return this;
    }

    public Object get(String key){
        return this.values.get(key);
    }

    public CMMTier setItemCapability(int itemCapability) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.itemCapability = itemCapability;
        return this;
    }

    public CMMTier setSteamEngineGeneratedSpeed(int steamEngineGeneratedSpeed){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.steamEngineGeneratedSpeed = steamEngineGeneratedSpeed;
        return this;
    }

    public CMMTier setSteamEngineCapacity(double steamEngineCapacity){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.steamEngineCapacity = steamEngineCapacity;
        return this;
    }

    public CMMTier setFluidCapability(int fluidCapability) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.fluidCapability = fluidCapability;
        return this;
    }

    public CMMTier setFluidTankCapability(int fluidTankCapability) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.fluidTankCapability = fluidTankCapability;
        return this;
    }

    public CMMTier setMechanicalPressImpact(double impact) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.mechanicalPressImpact = impact;
        return this;
    }

    public CMMTier setMechanicalMixerImpact(double impact) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.mechanicalMixerImpact = impact;
        return this;
    }

    public CMMTier setDeployerImpact(double impact) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.deployerImpact = impact;
        return this;
    }

    public CMMTier setProcessingMultiple(int processingMultiple) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.processingMultiple = processingMultiple;
        return this;
    }

    public CMMTier setDeployerProcessingMultiple(int deployerProcessingMultiple) {
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.deployerProcessingMultiple = deployerProcessingMultiple;
        return this;
    }

    public CMMTier setDepotRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMDepotBlockEntity>>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.depotRenderer = renderer;
        return this;
    }

    public CMMTier setMechanicalPressRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalPressBlockEntity>>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.mechanicalPressRenderer = renderer;
        return this;
    }

    public CMMTier setMechanicalMixerRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalMixerBlockEntity>>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.mechanicalMixerRenderer = renderer;
        return this;
    }

    public CMMTier setSpoutRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMSpoutBlockEntity>>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.spoutRenderer = renderer;
        return this;
    }

    public CMMTier setBasinRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BasinRenderer>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.basinRenderer = renderer;
        return this;
    }

    public CMMTier setDeployerRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, DeployerRenderer>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.deployerRenderer = renderer;
        return this;
    }

    public CMMTier setSteamEngineRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, SteamEngineRenderer>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.steamEngineRenderer = renderer;
        return this;
    }

    public CMMTier setFluidTankRenderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, FluidTankRenderer>> renderer){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        this.fluidTankRenderer = renderer;
        return this;
    }

    public CMMTier defaultRenderer(){
        if(frozen){
            throw new UnsupportedOperationException("registration CMMTier has been frozen");
        }
        return this.withDefaultDepotRenderer()
                .withDefaultSpoutRenderer()
                .withDefaultMechanicalMixerRenderer()
                .withDefaultMechanicalPressRenderer()
                .withDefaultBasinRenderer()
                .withDefaultDeployerRenderer()
                .withDefaultSteamEngineRenderer()
                .withDefaultFluidTankRenderer();
    }


    public CMMTier withDefaultDepotRenderer(){
        return setDepotRenderer(() -> CMMDepotRenderer::new);
    }
    public CMMTier withDefaultMechanicalPressRenderer(){
        return setMechanicalPressRenderer(() -> CMMMechanicalPressRenderer::new);
    }

    public CMMTier withDefaultMechanicalMixerRenderer(){
        return setMechanicalMixerRenderer(() -> CMMMechanicalMixerRenderer::new);
    }

    public CMMTier withDefaultSpoutRenderer(){
        return setSpoutRenderer(() -> CMMSpoutRenderer::new);
    }
    public CMMTier withDefaultBasinRenderer(){
        return setBasinRenderer(() -> BasinRenderer::new);
    }

    public CMMTier withDefaultDeployerRenderer(){
        return setDeployerRenderer(() -> DeployerRenderer::new);
    }

    public CMMTier withDefaultSteamEngineRenderer(){
        return setSteamEngineRenderer(() -> SteamEngineRenderer::new);
    }
    public CMMTier withDefaultFluidTankRenderer(){
        return setFluidTankRenderer(() -> FluidTankRenderer::new);
    }
    public int getItemCapability() {
        return itemCapability;
    }

    public int getFluidCapability() {
        return fluidCapability;
    }

    public int getFluidTankCapability() {
        return fluidTankCapability;
    }

    public int getProcessingMultiple() {
        return processingMultiple;
    }

    public int getDeployerProcessingMultiple() {
        return deployerProcessingMultiple;
    }

    public double getMechanicalPressImpact() {
        return mechanicalPressImpact;
    }

    public double getMechanicalMixerImpact() {
        return mechanicalMixerImpact;
    }

    public double getDeployerImpact() {
        return deployerImpact;
    }

    public double getSteamEngineCapacity() {
        return steamEngineCapacity;
    }

    public int getSteamEngineGeneratedSpeed() {
        return steamEngineGeneratedSpeed;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, SteamEngineRenderer>> getSteamEngineRenderer() {
        return steamEngineRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalMixerBlockEntity>>> getMechanicalMixerRenderer() {
        return mechanicalMixerRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMDepotBlockEntity>>> getDepotRenderer() {
        return depotRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMMechanicalPressBlockEntity>>> getMechanicalPressRenderer() {
        return mechanicalPressRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<CMMSpoutBlockEntity>>> getSpoutRenderer() {
        return spoutRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BasinRenderer>> getBasinRenderer() {
        return basinRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, DeployerRenderer>> getDeployerRenderer() {
        return deployerRenderer;
    }

    public NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, FluidTankRenderer>> getFluidTankRenderer() {
        return fluidTankRenderer;
    }

    public ResourceLocation getId() {
        return id;
    }

    public static boolean isReady(){
        return !frozen && !registrateFrozen;
    }
}
