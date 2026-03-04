package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.yxiao233.createmoremachines.utils.BigItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PressingBehaviour.class)
public abstract class MixinPressingBehaviour extends BeltProcessingBehaviour {
    @Shadow public boolean running;

    @Shadow public PressingBehaviour.Mode mode;

    @Shadow public boolean finished;

    @Shadow public int prevRunningTicks;

    @Shadow public int runningTicks;

    @Shadow public List<ItemStack> particleItems;

    @Shadow protected abstract void spawnParticles();

    public MixinPressingBehaviour(SmartBlockEntity be) {
        super(be);
    }
    /**
     * @author Y_Xiao233
     * @reason allow stack size over 99
     */
    @Overwrite
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        this.running = compound.getBoolean("Running");
        this.mode = PressingBehaviour.Mode.values()[compound.getInt("Mode")];
        this.finished = compound.getBoolean("Finished");
        this.prevRunningTicks = this.runningTicks = compound.getInt("Ticks");
        super.read(compound, registries, clientPacket);
        if (clientPacket) {
            NBTHelper.iterateCompoundList(compound.getList("ParticleItems", 10), (c) -> {
                this.particleItems.add(BigItemStack.parseOptional(registries,c));
            });
            this.spawnParticles();
        }

    }

    /**
     * @author Y_Xiao233
     * @reason allow stack size over 99
     */
    @Overwrite
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putInt("Mode", this.mode.ordinal());
        compound.putBoolean("Finished", this.finished);
        compound.putInt("Ticks", this.runningTicks);
        super.write(compound, registries, clientPacket);
        if (clientPacket) {
            compound.put("ParticleItems", NBTHelper.writeCompoundList(this.particleItems, (s) -> {
                return (CompoundTag)BigItemStack.of(s).saveOptional(registries);
            }));
            this.particleItems.clear();
        }

    }
}
