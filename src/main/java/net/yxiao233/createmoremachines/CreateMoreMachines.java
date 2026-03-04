package net.yxiao233.createmoremachines;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutingBehaviours;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMCreativeModeTab;
import net.yxiao233.createmoremachines.api.registry.CMMTierManager;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import net.yxiao233.createmoremachines.api.registry.CMMRegistrate;
import net.yxiao233.createmoremachines.datagen.CMMMixingRecipeProvider;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

@Mod(CreateMoreMachines.MODID)
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class CreateMoreMachines {
    public static final String MODID = "createmoremachines";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final CMMRegistrate REGISTRATE = CMMRegistrate.create(MODID).defaultCreativeTab((ResourceKey)null).setTooltipModifierFactory((item) -> {
        return (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item)));
    });

    public CreateMoreMachines(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(CMMConfig::loadConfig);
        modContainer.registerConfig(ModConfig.Type.STARTUP, CMMConfig.SPEC);
        CMMTierManager.loadAllPlugin();
        CMMTierManager.registryTiers();
        CMMTierManager.registryRegistrate();
        CMMTier.getAllRegistrate().forEach(registrate -> registrate.registerEventListeners(modEventBus));
        CMMRegistryEntry.register();
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

        generator.addProvider(event.includeServer(), new CMMMixingRecipeProvider(packOutput,lookupProvider));
    }

    public static CMMRegistrate registrate() {
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
