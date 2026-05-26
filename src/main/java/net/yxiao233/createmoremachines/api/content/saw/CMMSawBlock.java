package net.yxiao233.createmoremachines.api.content.saw;

import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
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

public class CMMSawBlock extends SawBlock implements IHaveTierInformation {
    private final CMMTier tier;

    public CMMSawBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public @NotNull BlockEntityType<? extends SawBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getSawEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW +
            Component.translatable(Util.makeDescriptionId("tier", tier.getId())).getString() +
            ChatFormatting.WHITE +
            Component.translatable("block.create.mechanical_saw").getString();
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips, tier, CMMTierTooltip.Type.PROCESSING_MULTIPLE);
    }
}
