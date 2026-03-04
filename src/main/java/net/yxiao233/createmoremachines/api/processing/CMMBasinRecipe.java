package net.yxiao233.createmoremachines.api.processing;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import net.minecraft.world.item.crafting.Recipe;

public class CMMBasinRecipe {
    public static boolean apply(BasinBlockEntity basin, Recipe<?> recipe, int max){
        boolean canContinue;
        int processingTime = 0;
        do{
            canContinue = BasinRecipe.apply(basin,recipe);
            processingTime ++;
        }while (canContinue && processingTime < max);

        return processingTime > 0;
    }

}
