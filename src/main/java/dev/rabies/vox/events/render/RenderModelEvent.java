package dev.rabies.vox.events.render;

import dev.rabies.vox.events.VoxEvent;
import dev.rabies.vox.events.VoxEventTiming;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;

public class RenderModelEvent extends VoxEvent {

    @Getter private final RenderCallback<RenderModelEvent> modelRenderCallback;
    @Getter private final RenderCallback<RenderModelEvent> layerRenderCallback;
    @Getter private final EntityLivingBase livingBase;

    @Getter @Setter
    private float limbSwing;
    @Getter @Setter
    private float limbSwingAmount;
    @Getter @Setter
    private float ageInTicks;
    @Getter @Setter
    private float netHeadYaw;
    @Getter @Setter
    private float headPitch;
    @Getter @Setter
    private float scaleFactor;

    public RenderModelEvent(VoxEventTiming timing, EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float ageInTicks,
                            float netHeadYaw, float headPitch, float scaleFactor,
                            RenderCallback<RenderModelEvent> modelRenderCallback,
                            RenderCallback<RenderModelEvent> layerRenderCallback) {
        super(timing);
        this.livingBase = livingBase;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleFactor = scaleFactor;
        this.modelRenderCallback = modelRenderCallback;
        this.layerRenderCallback = layerRenderCallback;
    }

    public void renderModelAndLayers() {
        renderModel();
        renderLayers();
    }

    public void renderModel() {
        modelRenderCallback.render(this);
    }

    public void renderLayers() {
        layerRenderCallback.render(this);
    }
}
