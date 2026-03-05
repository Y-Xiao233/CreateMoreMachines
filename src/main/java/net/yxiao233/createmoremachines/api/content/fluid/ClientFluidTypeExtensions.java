package net.yxiao233.createmoremachines.api.content.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class ClientFluidTypeExtensions implements IClientFluidTypeExtensions {
    private final ResourceLocation still;
    private final ResourceLocation flow;

    public ClientFluidTypeExtensions(ResourceLocation still, ResourceLocation flow) {
        this.still = still;
        this.flow = flow;
    }

    public @NotNull ResourceLocation getStillTexture() {
        return this.still;
    }

    public @NotNull ResourceLocation getFlowingTexture() {
        return this.flow;
    }
}