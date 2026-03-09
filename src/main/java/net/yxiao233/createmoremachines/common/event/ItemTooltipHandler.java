package net.yxiao233.createmoremachines.common.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;

import java.util.List;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreateMoreMachines.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ItemTooltipHandler {
    @SubscribeEvent
    public static void onAdded(ItemTooltipEvent event){
        Item item = event.getItemStack().getItem();
        List<Component> tooltips = event.getToolTip();
        Block block = Block.byItem(item);
        if(BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(CreateMoreMachines.MODID)){
            if(block instanceof IHaveTierInformation informationBlock){
                informationBlock.addTierInformation(tooltips);
            }
        }
    }
}
