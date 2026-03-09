package net.yxiao233.createmoremachines.api.content;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class CMMBuilderTransformers {
    @SuppressWarnings("unchecked")
    public static <B extends CasingBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> casing(Supplier<CTSpriteShiftEntry> ct) {
        return (b) -> {
            return (BlockBuilder<B, CreateRegistrate>) b.initialProperties(SharedProperties::stone)
                    .properties(p -> p.sound(SoundType.WOOD))
                    .transform(TagGen.axeOrPickaxe())
                    .blockstate((c, p) -> p.simpleBlock(c.get()))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(ct.get())))
                    .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.makeCasing(block, ct.get())))
                    .tag(new TagKey[]{AllTags.AllBlockTags.CASING.tag})
                    .item()
                    .tag(new TagKey[]{AllTags.AllItemTags.CASING.tag})
                    .build();
        };
    }
}
