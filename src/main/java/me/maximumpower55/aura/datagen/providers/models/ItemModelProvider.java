package me.maximumpower55.aura.datagen.providers.models;

import me.maximumpower55.aura.registry.AuraItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ItemModelProvider extends FabricModelProvider {
	public ItemModelProvider(FabricDataGenerator generator) {
		super(generator);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		itemModelGenerator.generateFlatItem(AuraItems.SPELL_BOOK, ModelTemplates.FLAT_HANDHELD_ITEM);
	}
}
