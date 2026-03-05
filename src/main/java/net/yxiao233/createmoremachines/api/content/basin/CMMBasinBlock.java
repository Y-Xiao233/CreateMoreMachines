package net.yxiao233.createmoremachines.api.content.basin;

import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.yxiao233.createmoremachines.api.CMMTierTooltip;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMMBasinBlock extends BasinBlock implements IHaveTierInformation {
    private final CMMTier tier;
    public CMMBasinBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public BlockEntityType<? extends BasinBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getBasinEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.basin").getString();
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips,tier, CMMTierTooltip.Type.FLUID_CAPABILITY);
    }
}
