package me.maximumpower55.aura.registry;

import org.jetbrains.annotations.NotNull;

import me.maximumpower55.aura.AuraMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;

public final class AuraRegistry {
	static @NotNull <T extends Item> T registerItem(String name, T item) {
		return Registry.register(Registry.ITEM, AuraMod.id(name), item);
	}

	static @NotNull <T extends Block> T registerBlock(String name, T block) {
		return Registry.register(Registry.BLOCK, AuraMod.id(name), block);
	}

	static @NotNull <T extends Block> T registerBlockWithItem(String name, T block, Properties blockItemProperties) {
		T ret = registerBlock(name, block);
		registerItem(name, new BlockItem(ret, blockItemProperties));
		return ret;
	}

	static @NotNull <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> builder) {
		ResourceLocation id = AuraMod.id(name);
		return Registry.register(Registry.ENTITY_TYPE, id, builder.build(id.toString()));
	}

	public static void register() {
		AuraItems.register();
		AuraEntities.register();
	}
}
