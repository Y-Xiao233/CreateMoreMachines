package net.yxiao233.createmoremachines.api.content.encased_fan;

import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.yxiao233.createmoremachines.api.CMMTierTooltip;
import net.yxiao233.createmoremachines.api.content.IHaveTierInformation;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import net.yxiao233.createmoremachines.api.registry.CMMTier;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMMEncasedFanBlock extends EncasedFanBlock implements IHaveTierInformation {
    private final CMMTier tier;

    public CMMEncasedFanBlock(CMMTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public CMMTier getTier() {
        return tier;
    }

    @Override
    public @NotNull BlockEntityType<? extends EncasedFanBlockEntity> getBlockEntityType() {
        return CMMRegistryEntry.getEncasedFanEntities().get(tier.getId()).get();
    }

    @Override
    public @NotNull String getDescriptionId() {
        return ChatFormatting.YELLOW + Component.translatable(Util.makeDescriptionId("tier", tier.getId())).getString() + ChatFormatting.WHITE + Component.translatable("block.create.encased_fan").getString();
    }

    @Override
    public void addTierInformation(List<Component> tooltips) {
        tooltips.add(Component.translatable("tooltip.createmoremachines.encased_fan_speed",tier.getSpecial(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed")));
    }
}