package net.yxiao233.createmoremachines.api.content.mechanical.mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class CMMMechanicalMixerBlock extends MechanicalMixerBlock {
    private final CMMTier tier;
    public CMMMechanicalMixerBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public BlockEntityType<? extends MechanicalMixerBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getMechanicalMixerEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier",tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.mechanical_mixer").getString();
    }
}
