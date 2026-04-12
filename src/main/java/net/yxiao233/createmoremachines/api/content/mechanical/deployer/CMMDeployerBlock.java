package net.yxiao233.createmoremachines.api.content.mechanical.deployer;

import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
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

public class CMMDeployerBlock extends DeployerBlock implements IHaveTierInformation {
    private final CMMTier tier;
    public CMMDeployerBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public @NotNull BlockEntityType<? extends DeployerBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getDeployerEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.deployer").getString();
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        CMMTierTooltip.byTypes(tooltips, tier, CMMTierTooltip.Type.DEPLOYER_PROCESSING_MULTIPLE);
    }
}
