package me.maximumpower55.aura;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.maximumpower55.aura.registry.AuraRegistry;

public class AuraMod implements ModInitializer {
	public static final String MOD_ID = "aura";

	public static final Logger LOGGER = LoggerFactory.getLogger("Aura");

	@Override
	public void onInitialize(ModContainer mod) {
		AuraRegistry.register();
	}

	public static ResourceLocation id(@NotNull String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
