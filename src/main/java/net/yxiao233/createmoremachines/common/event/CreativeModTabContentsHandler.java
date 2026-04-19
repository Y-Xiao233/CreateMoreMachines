package net.yxiao233.createmoremachines.common.event;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.fluid.CMMBaseFluidInstance;
import net.yxiao233.createmoremachines.common.registry.CMMCreativeModeTab;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import net.yxiao233.createmoremachines.utils.MapUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreateMoreMachines.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeModTabContentsHandler {
    @SubscribeEvent
    public static void onBuild(BuildCreativeModeTabContentsEvent event){
        if(event.getTab() == CMMCreativeModeTab.TAB.get()){
            collectItems(CMMRegistryEntry.class).forEach(event::accept);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static <T> List<ItemLike> collectItems(Class<T> clazz){
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
                    }else if(object instanceof CMMBaseFluidInstance fluidInstance){
                        items.add(fluidInstance.getBucketFluid().get());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            T instance = declaredConstructor.newInstance();
            Arrays.stream(clazz.getMethods()).toList().forEach(method -> {
                if(Map.class.isAssignableFrom(method.getReturnType())){
                    try {
                        Map<?,?> map = (Map<?, ?>) method.invoke(instance);
                        MapUtil.valueList(map).forEach(value ->{
                            if(value instanceof ItemLike itemLike){
                                items.add(itemLike);
                            }
                        });
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return items;
    }
}
