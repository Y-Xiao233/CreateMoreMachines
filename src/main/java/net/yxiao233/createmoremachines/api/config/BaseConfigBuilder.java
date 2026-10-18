package net.yxiao233.createmoremachines.api.config;

import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;

import java.util.HashMap;

public class BaseConfigBuilder {
    private final String tierId;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> itemCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> fluidCaps = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Integer> multiples = new HashMap<>();
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, Double> impacts = new HashMap<>();
    private final int defaultItemCap;
    private final int defaultFluidCap;
    private final int defaultMultiple;
    private final HashMap<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>, HashMap<String, Object>> specials = new HashMap<>();
    private BaseConfigBuilder(String tierId, int itemCap, int fluidCap, int multiple){
        this.tierId = tierId;
        this.defaultItemCap = itemCap;
        this.defaultFluidCap = fluidCap;
        this.defaultMultiple = multiple;
    }

    public static BaseConfigBuilder builder(String tierId, int itemCap, int fluidCap, int multiple){
        return new BaseConfigBuilder(tierId,itemCap,fluidCap,multiple);
    }

    public BaseConfigBuilder overrideItemCap(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, int itemCap){
        return action(() -> this.itemCaps.put(type,itemCap));
    }

    public BaseConfigBuilder noItemCap(BuiltInAdvancedMachineTypes.AdvancedMachineType<?>... types){
        return action(() ->{
            for (BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type : types) {
                this.itemCaps.put(type,0);
            }
        });
    }

    public BaseConfigBuilder overrideFluidCap(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, int fluidCap){
        return action(() -> this.fluidCaps.put(type,fluidCap));
    }

    public BaseConfigBuilder noFluidCap(BuiltInAdvancedMachineTypes.AdvancedMachineType<?>... types){
        return action(() ->{
            for (BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type : types) {
                this.fluidCaps.put(type,0);
            }
        });
    }

    public BaseConfigBuilder overrideMultiple(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, int multiple){
        return action(() -> this.multiples.put(type,multiple));
    }

    public BaseConfigBuilder noMultiple(BuiltInAdvancedMachineTypes.AdvancedMachineType<?>... types){
        return action(() ->{
            for (BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type : types) {
                this.multiples.put(type,0);
            }
        });
    }

    public BaseConfigBuilder overrideImpact(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, double impact){
        return action(() -> this.impacts.put(type,impact));
    }

    public BaseConfigBuilder defineCustom(BuiltInAdvancedMachineTypes.AdvancedMachineType<?> type, String key, Object value){
        return action(() -> {
            if(this.specials.containsKey(type)){
                this.specials.get(type).put(key,value);
            }else{
                HashMap<String, Object> map = new HashMap<>();
                map.put(key,value);
                this.specials.put(type,map);
            }
        });
    }

    private BaseConfigBuilder action(Operation operation){
        operation.action();
        return this;
    }

    public TierConfigBase build(){
        return new TierConfigBase(tierId,itemCaps,fluidCaps,multiples,impacts,specials,defaultItemCap,defaultFluidCap,defaultMultiple,0);
    }

    @FunctionalInterface
    private interface Operation {
        void action();
    }
}
