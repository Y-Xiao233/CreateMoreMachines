package net.yxiao233.createmoremachines.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.createmoremachines.CreateMoreMachines;

public class CMMCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,CreateMoreMachines.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register("create_more_machines",() -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.createmoremachines"))
            .icon(() -> CMMRegistryEntry.getDepots().values().stream().toList().getFirst().get().asItem().getDefaultInstance())
            .build()
    );
}
