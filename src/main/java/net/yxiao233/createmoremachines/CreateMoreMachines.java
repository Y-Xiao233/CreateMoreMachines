package net.yxiao233.createmoremachines;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.createmoremachines.api.annotation.RecipeGen;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutingBehaviours;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMCreativeModeTab;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import net.yxiao233.createmoremachines.utils.AnnotationUtil;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

@Mod(CreateMoreMachines.MODID)
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class CreateMoreMachines {
    public static final String MODID = "createmoremachines";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab((ResourceKey)null).setTooltipModifierFactory((item) -> {
        return (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
    });
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CreateMoreMachines.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CreateMoreMachines.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CreateMoreMachines.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, CreateMoreMachines.MODID);

    public CreateMoreMachines(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(CMMConfig::loadConfig);
        modContainer.registerConfig(ModConfig.Type.STARTUP, CMMConfig.SPEC);
        CMMTierManager.loadAllPlugin();
        CMMTierManager.registryTiers();
        CMMTierManager.registryRegistrate();
        CMMTier.getAllRegistrate().forEach(registrate -> registrate.registerEventListeners(modEventBus));
        CMMRegistryEntry.register();
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CMMCreativeModeTab.TABS.register(modEventBus);
        CMMTier.freezy();
        CMMTier.freezyRegistrate();
        modEventBus.addListener(CreateMoreMachines::init);
        modEventBus.addListener(CreateMoreMachines::gatherData);

        if (FMLEnvironment.dist == Dist.CLIENT){
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }

    private static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(CMMSpoutingBehaviours::registerDefaults);
    }
    private static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        AnnotationUtil.getAllClasses(RecipeGen.class).forEach(clazz ->{
            try {
                if(RecipeProvider.class.isAssignableFrom(clazz)){
                    Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(PackOutput.class, CompletableFuture.class);
                    declaredConstructor.setAccessible(true);
                    RecipeProvider provider = (RecipeProvider) declaredConstructor.newInstance(packOutput, lookupProvider);
                    generator.addProvider(event.includeServer(), provider);
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CreateRegistrate registrate() {
        if (!STACK_WALKER.getCallerClass().getPackageName().startsWith("net.yxiao233.createmoremachines")) {
            throw new UnsupportedOperationException("Other mods are not permitted to use createmoremachines' registrate instance.");
        } else {
            return REGISTRATE;
        }
    }

    public static ResourceLocation makeId(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
