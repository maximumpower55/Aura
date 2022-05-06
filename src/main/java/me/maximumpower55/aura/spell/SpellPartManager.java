package me.maximumpower55.aura.spell;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import org.quiltmc.qsl.resource.loader.api.reloader.SimpleSynchronousResourceReloader;

import me.maximumpower55.aura.AuraMod;
import me.maximumpower55.aura.api.spell.SpellPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class SpellPartManager implements SimpleSynchronousResourceReloader {
	public static final SpellPartManager INSTANCE = new SpellPartManager();

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		HashSet<String> resourcesHandled = new HashSet<>();

		for (ResourceLocation id : resourceManager.listResources("spell_parts", path -> path.endsWith(".json"))) {
			String path = id.getPath();
			ResourceLocation idWithoutFileExtension = new ResourceLocation(id.getNamespace(), path.substring(0, path.lastIndexOf(".json")));

			resourcesHandled.clear();

			try {
				resourceManager.getResources(id).forEach(resource -> {
					if(!resourcesHandled.contains(resource.getSourceName())) {
						resourcesHandled.add(resource.getSourceName());

						JsonReader jsonReader = GSON.newJsonReader(new InputStreamReader(resource.getInputStream()));
						SpellPartRegistry.register(GSON.fromJson(jsonReader, SpellPart.class));
					}
				});
			} catch (Exception e) {
				AuraMod.LOGGER.error("Couldn't parse json file {} from {}", idWithoutFileExtension, id, e);
			}
		}
	}

	@Override
	public ResourceLocation getQuiltId() {
		return AuraMod.id("spell_parts");
	}
}
