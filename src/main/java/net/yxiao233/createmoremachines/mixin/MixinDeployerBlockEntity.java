package net.yxiao233.createmoremachines.mixin;

import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.PackagedEnumHelper;
import net.yxiao233.createmoremachines.utils.BigItemStack;
import net.yxiao233.createmoremachines.utils.ReflectionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;

@Mixin(DeployerBlockEntity.class)
public abstract class MixinDeployerBlockEntity extends KineticBlockEntity implements Clearable {
    @Shadow protected int timer;

    @Shadow protected boolean redstoneLocked;

    @Shadow protected UUID owner;

    @Shadow private ListTag deferredInventoryList;

    @Shadow protected List<ItemStack> overflowItems;

    @Shadow protected ItemStack heldItem;

    @Shadow protected boolean fistBump;

    @Shadow protected float reach;

    @Shadow protected abstract Vec3 getMovementVector();

    @Shadow protected DeployerFakePlayer player;

    public MixinDeployerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        ReflectionUtil.setPackagedEnumField("state",this, DeployerBlockEntity.class, PackagedEnumHelper.readEnumFromNbt(compound, "State", new String[]{
                "WAITING",
                "EXPANDING",
                "RETRACTING",
                "DUMPING"
        }));
        ReflectionUtil.setPackagedEnumField("mode",this, DeployerBlockEntity.class, PackagedEnumHelper.readEnumFromNbt(compound, "Mode", new String[]{
                "PUNCH",
                "USE"
        }));
        this.timer = compound.getInt("Timer");
        this.redstoneLocked = compound.getBoolean("Powered");
        if (compound.contains("Owner")) {
            this.owner = compound.getUUID("Owner");
        }

        this.deferredInventoryList = compound.getList("Inventory", 10);
        this.overflowItems = NBTHelper.readItemList(compound.getList("Overflow", 10), registries);
        if (compound.contains("HeldItem")) {
            this.heldItem = ItemStack.parseOptional(registries, compound.getCompound("HeldItem"));
        }

        super.read(compound, registries, clientPacket);
        if (clientPacket) {
            this.fistBump = compound.getBoolean("Fistbump");
            this.reach = compound.getFloat("Reach");
            if (compound.contains("Particle") && this.level != null) {
                ItemStack particleStack = BigItemStack.parseOptional(registries, compound.getCompound("Particle"));
                SandPaperItem.spawnParticles(VecHelper.getCenterOf(this.worldPosition).add(this.getMovementVector().scale(this.reach + 1.0F)), particleStack, this.level);
            }

        }
    }

    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        Enum<?> mode = ReflectionUtil.getPackagedEnumField("mode", this, DeployerBlockEntity.class);
        Enum<?> state = ReflectionUtil.getPackagedEnumField("state", this, DeployerBlockEntity.class);
        if(mode != null){
            NBTHelper.writeEnum(compound,"Mode",mode);
        }
        if(state != null){
            NBTHelper.writeEnum(compound, "State", state);
        }
        compound.putInt("Timer", this.timer);
        compound.putBoolean("Powered", this.redstoneLocked);
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }

        if (this.player != null) {
            ListTag invNBT = new ListTag();
            this.player.getInventory().save(invNBT);
            compound.put("Inventory", invNBT);
            compound.put("HeldItem", this.player.getMainHandItem().saveOptional(registries));
            compound.put("Overflow", NBTHelper.writeItemList(this.overflowItems, registries));
        } else if (this.deferredInventoryList != null) {
            compound.put("Inventory", this.deferredInventoryList);
        }

        super.write(compound, registries, clientPacket);
        if (clientPacket) {
            compound.putBoolean("Fistbump", this.fistBump);
            compound.putFloat("Reach", this.reach);
            if (this.player != null) {
                compound.put("HeldItem", this.player.getMainHandItem().saveOptional(registries));
                ItemStack spawnedItemEffects = ReflectionUtil.getPrivateField("spawnedItemEffects", ItemStack.class, this.player, DeployerFakePlayer.class);
                if (spawnedItemEffects != null) {
                    compound.put("Particle", BigItemStack.of(spawnedItemEffects).saveOptional(registries));
                    ReflectionUtil.setPrivateField("spawnedItemEffects", ItemStack.class, this.player, DeployerFakePlayer.class, null);
                }

            }
        }
    }
}
