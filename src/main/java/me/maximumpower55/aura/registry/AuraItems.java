package me.maximumpower55.aura.registry;

import me.maximumpower55.aura.item.SpellBookItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class AuraItems {
	public static final Item SPELL_BOOK = AuraRegistry.registerItem("spell_book", new SpellBookItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));

	static void register() {}
}
