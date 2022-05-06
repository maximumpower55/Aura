package me.maximumpower55.aura;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import me.maximumpower55.aura.client.renderer.entity.SpellProjectileEntityRenderer;
import me.maximumpower55.aura.registry.AuraEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AuraClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(AuraEntities.SPELL_PROJECTILE, SpellProjectileEntityRenderer::new);
	}
}
