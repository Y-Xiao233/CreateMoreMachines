package net.yxiao233.createmoremachines.api.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.yxiao233.createmoremachines.CreateMoreMachines;

import java.util.HashMap;
import java.util.Map;

public class CMMPartialModelsRegistry {
    private static final Map<CMMTier, Map<String, PartialModel[]>> MODELS = new HashMap<>();
    private CMMPartialModelsRegistry(){}
    public static void registry(CMMTier tier, String type, PartialModel[] models){
        if(MODELS.containsKey(tier)){
            Map<String, PartialModel[]> map = MODELS.get(tier);
            if(map.containsKey(type)){
                throw new UnsupportedOperationException("value has been defined");
            }
            map.put(type,models);
        }else{
            Map<String, PartialModel[]> map = new HashMap<>();
            map.put(type,models);
            MODELS.put(tier,map);
        }
    }

    public static PartialModel[] getPartialModels(CMMTier tier, String type){
        if(MODELS.containsKey(tier)){
            Map<String, PartialModel[]> map = MODELS.get(tier);
            if(map.containsKey(type)){
                return map.get(type);
            }else{
                throw new RuntimeException("unknown type for tier: " + tier.getId().toString());
            }
        }else{
            throw new RuntimeException("unknown tier by id: " + tier.getId().toString());
        }
    }

    public static void registrySpouts(){
        CMMTier.getTiers().values().forEach(tier ->{
            registry(tier, "spout", new PartialModel[]{spout("top",tier),spout("middle",tier),spout("bottom",tier)});
        });
    }

    public static void registryMixers(){
        CMMTier.getTiers().values().forEach(tier ->{
            registry(tier, "mixer", new PartialModel[]{mixer("head",tier)});
        });
    }

    public static void registryCrushingWheel(){
        CMMTier.getTiers().forEach((id, tier) -> {
            if (CMMTier.shouldRegistry(tier, BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)) {
                PartialModel wheel = PartialModel.of(CreateMoreMachines.makeId("block/crushing_wheel/" + tier.getId().getPath() + "_crushing_wheel"));
                registry(tier, "crushing_wheel", new PartialModel[]{wheel});
            }
        });
    }

    public static void registrySteamEngine(){
        CMMTier.getTiers().values().forEach(tier -> {
            if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineTypes.STEAM_ENGINE)){
                PartialModel piston = PartialModel.of(CreateMoreMachines.makeId("block/steam_engine/" + tier.getId().getPath() + "_steam_engine/piston"));
                PartialModel linkage = PartialModel.of(CreateMoreMachines.makeId("block/steam_engine/" + tier.getId().getPath() + "_steam_engine/linkage"));
                PartialModel connector = PartialModel.of(CreateMoreMachines.makeId("block/steam_engine/" + tier.getId().getPath() + "_steam_engine/shaft_connector"));
                registry(tier,"steam_engine",new PartialModel[]{piston,linkage,connector});
            }
        });
    }

    public static void registryFluidTank(){
        CMMTier.getTiers().values().forEach(tier -> {
            if(CMMTier.shouldRegistry(tier,BuiltInAdvancedMachineTypes.STEAM_ENGINE)){
                PartialModel gauge = PartialModel.of(CreateMoreMachines.makeId("block/steam_engine/" + tier.getId().getPath() + "_steam_engine/gauge"));
                PartialModel gauge_dial = PartialModel.of(CreateMoreMachines.makeId("block/steam_engine/" + tier.getId().getPath() + "_steam_engine/gauge_dial"));
                registry(tier,"fluid_tank",new PartialModel[]{gauge,gauge_dial});
            }
        });
    }

    private static PartialModel spout(String path, CMMTier tier) {
        return block("spout/" + path + "/" + tier.getId().getPath() + "_spout_" + path);
    }

    private static PartialModel mixer(String path, CMMTier tier) {
        return block("mechanical_mixer/" + path + "/" + tier.getId().getPath() + "_mechanical_mixer_" + path);
    }

    private static PartialModel block(String path) {
        return PartialModel.of(CreateMoreMachines.makeId("block/" + path));
    }
}
