package net.yxiao233.createmoremachines.api.config;

import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ConfigSettings {
    private final HashMap<Rule,List<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>>> settings = new HashMap<>();
    public ConfigSettings(){

    }
    public ConfigSettings addRule(Rule rule, BuiltInAdvancedMachineTypes.AdvancedMachineType<?>... types){
        if(settings.containsKey(rule)){
            settings.get(rule).addAll(List.copyOf(Arrays.stream(types).toList()));
        }else{
            settings.put(rule,List.copyOf(Arrays.stream(types).toList()));
        }
        return this;
    }

    public void forEach(Rule rule, Consumer<List<BuiltInAdvancedMachineTypes.AdvancedMachineType<?>>> consumer){
        if(settings.containsKey(rule)){
            consumer.accept(settings.get(rule));
        }
    }

    public enum Rule {
        NO_ITEM,
        NO_FLUID,
        NO_MULTIPLE;
    }
}
