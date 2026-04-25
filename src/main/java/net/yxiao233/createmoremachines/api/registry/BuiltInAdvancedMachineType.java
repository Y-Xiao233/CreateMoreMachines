package net.yxiao233.createmoremachines.api.registry;

public class BuiltInAdvancedMachineType {
    public static final AdvancedMachineType BASIN = AdvancedMachineType.create("basin");
    public static final AdvancedMachineType DEPOT = AdvancedMachineType.create("depot");
    public static final AdvancedMachineType FLUID_TANK = AdvancedMachineType.create("fluid_tank");
    public static final AdvancedMachineType DEPLOYER = AdvancedMachineType.create("deployer");
    public static final AdvancedMachineType MIXER = AdvancedMachineType.create("mixer");
    public static final AdvancedMachineType PRESS = AdvancedMachineType.create("press");
    public static final AdvancedMachineType SPOUT = AdvancedMachineType.create("spout");
    public static final AdvancedMachineType STEAM_ENGINE = AdvancedMachineType.create("steam_engine");
    public static class AdvancedMachineType{
        private final String name;
        private AdvancedMachineType(String name){
            this.name = name;
        }

        public static AdvancedMachineType create(String name){
            return new AdvancedMachineType(name);
        }

        public String getName() {
            return name;
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
