package net.yxiao233.createmoremachines.api.content.mechanical.saw;

import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import net.minecraft.world.item.ItemStack;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;

public class CMMSawInventory extends ProcessingInventory {
    private final CMMMechanicalSawBlockEntity entity;
    public CMMSawInventory(CMMMechanicalSawBlockEntity entity) {
        super(entity::start);
        this.entity = entity;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == 0){
            return Math.clamp(entity.getTier().getProcessingMultiple(BuiltInAdvancedMachineTypes.SAW), 1, 64);
        }
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot != 0){
            return false;
        }
        ItemStack current = getStackInSlot(0);
        if (!current.isEmpty()){
            return ItemStack.isSameItemSameComponents(stack, current);
        }
        return isEmpty();
    }
}
