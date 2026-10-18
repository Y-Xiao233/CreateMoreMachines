package net.yxiao233.createmoremachines.api.config;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.yxiao233.createmoremachines.CreateMoreMachines;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class TierConfigBase {
    private final String tierId;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> itemCaps;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> fluidCaps;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> multiples;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Double> impacts;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, HashMap<String, Object>> specials;

    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, ModConfigSpec.IntValue> specItemCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, ModConfigSpec.IntValue> specFluidCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, ModConfigSpec.IntValue> specMultiples = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, ModConfigSpec.DoubleValue> specImpacts = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, HashMap<String, Object>> specSpecials = new HashMap<>();

    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> loadedItemCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> loadedFluidCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> loadedMultiples = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Double> loadedImpacts = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, HashMap<String, Object>> loadedSpecials = new HashMap<>();

    private final int defaultItemCap;
    private final int defaultFluidCap;
    private final int defaultMultiple;
    private final double defaultImpact;

    protected TierConfigBase(String tierId, HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> itemCaps, HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> fluidCaps, HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> multiples, HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Double> impacts, HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, HashMap<String, Object>> specials, int itemCap, int fluidCap, int multiple, double impact){
        this.tierId = tierId;
        this.itemCaps = new HashMap<>(itemCaps);
        this.fluidCaps = new HashMap<>(fluidCaps);
        this.multiples = new HashMap<>(multiples);
        this.impacts = new HashMap<>(impacts);
        this.specials = new HashMap<>(specials);
        this.defaultItemCap = itemCap;
        this.defaultFluidCap = fluidCap;
        this.defaultMultiple = multiple;
        this.defaultImpact = impact;
    }

    public void registry(ModConfigSpec.Builder BUILDER){
        BUILDER.translation(key(tierId + "_tier")).push(upperCaseForFirstChar(tierId) + " Tier");

        {
            BUILDER.translation(key("item_capability")).push("Item Capability");
            BuiltInAdvancedMachineTypes.forEach(type -> {
                int value = itemCaps.getOrDefault(type,defaultItemCap);
                if(value == 0){
                    return;
                }
                String typeId = type.getName();
                ModConfigSpec.IntValue intValue = BUILDER
                        .translation(key(typeId))
                        .comment(String.format("Item capability for %s %s",tierId,typeId))
                        .defineInRange(String.format("%s_%s_item_capability",tierId,typeId),value,1,Integer.MAX_VALUE);
                specItemCaps.put(type,intValue);
            });
            BUILDER.pop();
        }

        {
            BUILDER.translation(key("fluid_capability")).push("Fluid Capability");
            BuiltInAdvancedMachineTypes.forEach(type -> {
                int value = fluidCaps.getOrDefault(type,defaultFluidCap);
                if(value == 0){
                    return;
                }
                String typeId = type.getName();
                ModConfigSpec.IntValue intValue = BUILDER
                        .translation(key(typeId))
                        .comment(String.format("Fluid capability for %s %s",tierId,typeId))
                        .defineInRange(String.format("%s_%s_fluid_capability",tierId,typeId),value,1,Integer.MAX_VALUE);
                specFluidCaps.put(type,intValue);
            });
            BUILDER.pop();
        }

        {
            BUILDER.translation(key("processing_multiple")).push("Processing Multiple");
            BuiltInAdvancedMachineTypes.forEach(type -> {
                int value = multiples.getOrDefault(type,defaultMultiple);
                if(value == 0){
                    return;
                }
                String typeId = type.getName();
                ModConfigSpec.IntValue intValue = BUILDER
                        .translation(key(typeId))
                        .comment(String.format("Processing Multiple for %s %s",tierId,typeId))
                        .defineInRange(String.format("%s_%s_processing_multiple",tierId,typeId),value,1,Integer.MAX_VALUE);
                specMultiples.put(type,intValue);
            });
            BUILDER.pop();
        }

        {
            BUILDER.translation(key("mechanical_impact")).push("Mechanical Impact");
            BuiltInAdvancedMachineTypes.forEach(type -> {
                double value = impacts.getOrDefault(type,defaultImpact);
                if(value == 0){
                    return;
                }
                String typeId = type.getName();
                ModConfigSpec.DoubleValue doubleValue = BUILDER
                        .translation(key(typeId))
                        .comment(String.format("Mechanical Impact for %s %s",tierId,typeId))
                        .defineInRange(String.format("%s_%s_mechanical_impact",tierId,typeId),value,1,Integer.MAX_VALUE);
                specImpacts.put(type,doubleValue);
            });
            BUILDER.pop();
        }

        {
            BUILDER.translation(key("special")).push("Special");
            specials.forEach((type, map) -> {
                String typeId = type.getName();
                BUILDER.translation(key(typeId)).push(StringUtils.capitalize(typeId));
                HashMap<String, Object> specMap = new HashMap<>();
                map.forEach((key, value) ->{
                    Object v;
                    if(value instanceof Integer i){
                        v = BUILDER
                                .translation(key(typeId,key))
                                .defineInRange(String.format("%s_%s_%s",tierId,typeId,key),i,1,Integer.MAX_VALUE);
                        specMap.put(key,v);
                    }else if(value instanceof Double d){
                        v = BUILDER
                                .translation(key(typeId,key))
                                .defineInRange(String.format("%s_%s_%s",tierId,typeId,key),d,1,Double.MAX_VALUE);
                        specMap.put(key,v);
                    }
                });
                specSpecials.put(type,specMap);
                BUILDER.pop();
            });
            BUILDER.pop();
        }

        BUILDER.pop();
    }

    @SuppressWarnings("unused")
    public void onLoad(ModConfigEvent.Loading event){
        this.specItemCaps.forEach(((type, intValue) -> {
            this.loadedItemCaps.put(type,intValue.get());
        }));
        this.specFluidCaps.forEach(((type, intValue) -> {
            this.loadedFluidCaps.put(type,intValue.get());
        }));
        this.specMultiples.forEach(((type, intValue) -> {
            this.loadedMultiples.put(type,intValue.get());
        }));
        this.specImpacts.forEach(((type, doubleValue) -> {
            this.loadedImpacts.put(type,doubleValue.get());
        }));
        this.specSpecials.forEach(((type, map) -> {
            HashMap<String, Object> loadedMap = new HashMap<>();
            map.forEach((key,value) ->{
                if(value instanceof ModConfigSpec.IntValue intValue){
                    loadedMap.put(key,intValue.get());
                }else if(value instanceof ModConfigSpec.DoubleValue doubleValue){
                    loadedMap.put(key,doubleValue.get());
                }
            });
            this.loadedSpecials.put(type,loadedMap);
        }));
    }

    public int getItemCapability(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        return loadedItemCaps.get(type);
    }

    public int getFluidCapability(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        return loadedFluidCaps.get(type);
    }

    public int getProcessingMultiple(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        return loadedMultiples.get(type);
    }

    public double getMechanicalImpact(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type){
        return loadedImpacts.get(type);
    }

    public Object getSpecial(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, String key){
        if(loadedSpecials.containsKey(type)){
            return loadedSpecials.get(type).get(key);
        }
        return null;
    }

    public static String key(String... prefix){
        StringBuilder builder = new StringBuilder();
        builder.append("config.");
        builder.append(CreateMoreMachines.MODID + ".");
        for (int i = 0; i < prefix.length; i++) {
            builder.append(prefix[i]);
            if(i != prefix.length - 1){
                builder.append(".");
            }
        }
        return builder.toString();
    }

    private static String upperCaseForFirstChar(String s){
        char first = s.toCharArray()[0];
        return s.replaceFirst(String.valueOf(first), String.valueOf(first).toUpperCase());
    }
}
