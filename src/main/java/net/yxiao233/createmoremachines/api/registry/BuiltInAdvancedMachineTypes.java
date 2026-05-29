package net.yxiao233.createmoremachines.api.registry;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.yxiao233.createmoremachines.api.content.basin.CMMBasinBlock;
import net.yxiao233.createmoremachines.api.content.depot.CMMDepotBlock;
import net.yxiao233.createmoremachines.api.content.fluid_tank.CMMFluidTankBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.deployer.CMMDeployerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.mixer.CMMMechanicalMixerBlock;
import net.yxiao233.createmoremachines.api.content.mechanical.press.CMMMechanicalPressBlock;
import net.yxiao233.createmoremachines.api.content.saw.CMMSawBlock;
import net.yxiao233.createmoremachines.api.content.spout.CMMSpoutBlock;
import net.yxiao233.createmoremachines.api.content.steam_engine.CMMSteamEngineBlock;
import net.yxiao233.createmoremachines.common.registry.CMMRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuiltInAdvancedMachineTypes {
    public static final AdvancedMachineType<CMMBasinBlock> BASIN = AdvancedMachineType.create("basin", AllBlocks.BASIN, CMMRegistryEntry.getBasins());
    public static final AdvancedMachineType<CMMDepotBlock> DEPOT = AdvancedMachineType.create("depot", AllBlocks.DEPOT, CMMRegistryEntry.getDepots());
    public static final AdvancedMachineType<CMMFluidTankBlock> FLUID_TANK = AdvancedMachineType.create("fluid_tank", AllBlocks.FLUID_TANK, CMMRegistryEntry.getFluidTanks());
    public static final AdvancedMachineType<CMMDeployerBlock> DEPLOYER = AdvancedMachineType.create("deployer", AllBlocks.DEPLOYER, CMMRegistryEntry.getDeployers());
    public static final AdvancedMachineType<CMMMechanicalMixerBlock> MIXER = AdvancedMachineType.create("mixer", AllBlocks.MECHANICAL_MIXER, CMMRegistryEntry.getMechanicalMixers());
    public static final AdvancedMachineType<CMMMechanicalPressBlock> PRESS = AdvancedMachineType.create("press", AllBlocks.MECHANICAL_PRESS, CMMRegistryEntry.getMechanicalPresses());
    public static final AdvancedMachineType<CMMSpoutBlock> SPOUT = AdvancedMachineType.create("spout", AllBlocks.SPOUT, CMMRegistryEntry.getSpouts());
    public static final AdvancedMachineType<CMMSawBlock> SAW = AdvancedMachineType.create("saw", AllBlocks.MECHANICAL_SAW, CMMRegistryEntry.getSaws());
    public static final AdvancedMachineType<CMMSteamEngineBlock> STEAM_ENGINE = AdvancedMachineType.create("steam_engine", AllBlocks.STEAM_ENGINE, CMMRegistryEntry.getSteamEngines());
    public static class AdvancedMachineType<T extends Block>{
        private final String name;
        private final BlockEntry<?> mechanical;
        private final Map<ResourceLocation,BlockEntry<T>> advancedMechanicals;
        private AdvancedMachineType(String name, BlockEntry<?> mechanical, Map<ResourceLocation,BlockEntry<T>> advancedMechanicals){
            this.name = name;
            this.mechanical = mechanical;
            this.advancedMechanicals = advancedMechanicals;
        }

        public static <T extends Block> AdvancedMachineType<T> create(String name, BlockEntry<?> mechanical, Map<ResourceLocation,BlockEntry<T>> advancedMechanicals){
            return new AdvancedMachineType<>(name,mechanical,advancedMechanicals);
        }

        public String getName() {
            return name;
        }

        public BlockEntry<?> getMechanical() {
            return mechanical;
        }

        public Map<ResourceLocation, BlockEntry<T>> getAdvancedMechanicals() {
            return advancedMechanicals;
        }

        public List<CMMTier> getEnableTiers(){
            List<CMMTier> enables = new ArrayList<>();
            CMMTier.getTiers().forEach((id, tier) ->{
                if(tier.shouldRegistry(this) && !enables.contains(tier)){
                    enables.add(tier);
                }
            });
            return enables;
        }

        public void withoutAll(){
            CMMTier.getTiers().forEach((id, tier) ->{
                tier.without(this);
            });
        }

        public void without(CMMTier... tiers){
            for (CMMTier tier : tiers) {
                tier.without(this);
            }
        }

        @Override
        public boolean equals(Object other){
            if(other instanceof AdvancedMachineType type){
                return type.name.equals(this.name);
            }else if(other instanceof String s){
                return s.equals(this.name);
            }
            return false;
        }
    }
}
