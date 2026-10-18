package net.yxiao233.createmoremachines;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.yxiao233.createmoremachines.api.config.BaseConfigBuilder;
import net.yxiao233.createmoremachines.api.config.ConfigSettings;
import net.yxiao233.createmoremachines.api.config.TierConfigBase;
import net.yxiao233.createmoremachines.api.registry.BuiltInAdvancedMachineTypes;

public class CMMConfig {
    private static final ModConfigSpec.Builder BUILDER;
    public static final ConfigSettings SETTINGS = new ConfigSettings()
            .addRule(ConfigSettings.Rule.NO_ITEM,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SPOUT,BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.ENCASED_FAN,BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)
            .addRule(ConfigSettings.Rule.NO_FLUID,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.DEPLOYER,BuiltInAdvancedMachineTypes.MIXER,BuiltInAdvancedMachineTypes.PRESS,BuiltInAdvancedMachineTypes.SAW,BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.ENCASED_FAN,BuiltInAdvancedMachineTypes.CRUSHING_WHEEL)
            .addRule(ConfigSettings.Rule.NO_MULTIPLE,BuiltInAdvancedMachineTypes.STEAM_ENGINE,BuiltInAdvancedMachineTypes.FLUID_TANK,BuiltInAdvancedMachineTypes.DEPOT,BuiltInAdvancedMachineTypes.BASIN,BuiltInAdvancedMachineTypes.ENCASED_FAN);

    public static final TierConfigBase BRASS = BaseConfigBuilder.builder("brass",256,4000,4,SETTINGS)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,32000)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",2048)
            .defineCustom(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed",8)
            .defineCustom(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed",4)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,16)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,16)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,8)
            .overrideImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN,8)
            .overrideImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,32)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,64)
            .build();

    public static final TierConfigBase NETHERITE = BaseConfigBuilder.builder("netherite",1024,16000,16,SETTINGS)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,64000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,8)
            .overrideMultiple(BuiltInAdvancedMachineTypes.SAW,8)
            .overrideMultiple(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,8)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",4096)
            .defineCustom(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed",16)
            .defineCustom(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed",8)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN,32)
            .overrideImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,128)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,128)
            .build();

    public static final TierConfigBase END = BaseConfigBuilder.builder("end",4096,64000,64,SETTINGS)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,256000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,16)
            .overrideMultiple(BuiltInAdvancedMachineTypes.SAW,16)
            .overrideMultiple(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,16)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",128)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",8192)
            .defineCustom(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed",32)
            .defineCustom(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed",16)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,512)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,256)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN,64)
            .overrideImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,512)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,256)
            .build();

    public static final TierConfigBase BEYOND = BaseConfigBuilder.builder("beyond",16384,256000,256,SETTINGS)
            .overrideFluidCap(BuiltInAdvancedMachineTypes.FLUID_TANK,1024000)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,32)
            .overrideMultiple(BuiltInAdvancedMachineTypes.SAW,32)
            .overrideMultiple(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,32)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",128)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",16384)
            .defineCustom(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed",64)
            .defineCustom(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed",32)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,2048)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,1024)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN,128)
            .overrideImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,2048)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,512)
            .build();

    public static final TierConfigBase CREATIVE = BaseConfigBuilder.builder("creative",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,SETTINGS)
            .overrideMultiple(BuiltInAdvancedMachineTypes.DEPLOYER,64)
            .overrideMultiple(BuiltInAdvancedMachineTypes.SAW,64)
            .overrideMultiple(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,64)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"generated_speed",256)
            .defineCustom(BuiltInAdvancedMachineTypes.STEAM_ENGINE,"capacity",Integer.MAX_VALUE)
            .defineCustom(BuiltInAdvancedMachineTypes.ENCASED_FAN,"speed",256)
            .defineCustom(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,"speed",64)
            .overrideImpact(BuiltInAdvancedMachineTypes.PRESS,8)
            .overrideImpact(BuiltInAdvancedMachineTypes.MIXER,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.DEPLOYER,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.SAW,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.ENCASED_FAN,4)
            .overrideImpact(BuiltInAdvancedMachineTypes.CRUSHING_WHEEL,8)
            .overrideItemCap(BuiltInAdvancedMachineTypes.SAW,4096)
            .build();

    protected static final ModConfigSpec SPEC;
    static {
        BUILDER = new ModConfigSpec.Builder();
        {
            BUILDER.translation(TierConfigBase.key("tier_settings")).push("TierSettings");
            {
                BRASS.registry(BUILDER);
                NETHERITE.registry(BUILDER);
                END.registry(BUILDER);
                BEYOND.registry(BUILDER);
                CREATIVE.registry(BUILDER);
            }
            BUILDER.pop();
        }
        SPEC = BUILDER.build();
    }

    protected static void loadConfig(ModConfigEvent.Loading event){
        BRASS.onLoad(event);
        NETHERITE.onLoad(event);
        END.onLoad(event);
        BEYOND.onLoad(event);
        CREATIVE.onLoad(event);
    }
}