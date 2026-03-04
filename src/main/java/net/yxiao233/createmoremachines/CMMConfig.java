package net.yxiao233.createmoremachines;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.yxiao233.createmoremachines.api.config.TierConfigBase;

public class CMMConfig {
    private static final ModConfigSpec.Builder BUILDER;
    public static final TierConfigBase BRASS = TierConfigBase.create("brass",256,4000,4,32,12);
    public static final TierConfigBase NETHERITE = TierConfigBase.create("netherite",1024,16000,16,128,64);
    public static final TierConfigBase END = TierConfigBase.create("end",4096,64000,64,512,256);
    public static final TierConfigBase BEYOND = TierConfigBase.create("beyond",16384,256000,256,2048,1024);
    public static final TierConfigBase CREATIVE = TierConfigBase.create("creative",Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,8,4);
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