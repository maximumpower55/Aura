package me.maximumpower55.aura.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.maximumpower55.aura.client.gui.ManaBarOverlayRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
abstract class GuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Unique
	public float aura$render$tickDelta;

	@Inject(method = "render", at = @At("HEAD"))
	private void captureTickDelta(PoseStack poseStack, float tickDelta, CallbackInfo ci) {
		aura$render$tickDelta = tickDelta;
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"))
	private void renderManaBar(PoseStack poseStack, CallbackInfo ci) {
		ManaBarOverlayRenderer.renderOverlay(poseStack, aura$render$tickDelta);

		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
		RenderSystem.enableBlend();
	}
}
