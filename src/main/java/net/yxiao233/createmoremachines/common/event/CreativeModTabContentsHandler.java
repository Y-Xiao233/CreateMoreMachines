package net.yxiao233.createmoremachines.common.event;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.common.registry.CMMCreativeModeTab;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import net.yxiao233.createmoremachines.utils.MapUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreateMoreMachines.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeModTabContentsHandler {
    @SubscribeEvent
    public static void onBuild(BuildCreativeModeTabContentsEvent event){
        if(event.getTab() == CMMCreativeModeTab.TAB.get()){
            MapUtil.valueList(CMMRegistryEntry.getDepots()).forEach(event::accept);
            MapUtil.valueList(CMMRegistryEntry.getMechanicalPresses()).forEach(event::accept);
            MapUtil.valueList(CMMRegistryEntry.getMechanicalMixers()).forEach(event::accept);
            MapUtil.valueList(CMMRegistryEntry.getSpouts()).forEach(event::accept);
            collectItems(CMMRegistryEntry.class).forEach(event::accept);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> List<ItemLike> collectItems(Class<T> clazz){
        List<ItemLike> items = new ArrayList<>();
        Arrays.stream(clazz.getFields()).toList().forEach(field ->{
            if(Modifier.isStatic(field.getModifiers())){
                try {
                    field.setAccessible(true);
                    Object object = field.get(null);
                    if(object instanceof ItemLike itemLike){
                        items.add(itemLike);
                    }else if(object instanceof FluidEntry<?> fluidEntry){
                        items.add(fluidEntry.get().getBucket());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return items;
    }
}
