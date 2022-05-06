package me.maximumpower55.aura.spell;

import java.util.HashMap;

import me.maximumpower55.aura.api.spell.SpellPart;
import net.minecraft.resources.ResourceLocation;

public class SpellPartRegistry {
	private static final HashMap<ResourceLocation, SpellPart> SPELL_PARTS = new HashMap<>();

	public static SpellPart register(SpellPart spellPart) {
		ResourceLocation id = spellPart.getId();

		if(SPELL_PARTS.containsKey(id)) {
			throw new IllegalStateException(String.format("A spell part with a duplicate id tried to register : %s", id));
		}
		SPELL_PARTS.put(id, spellPart);

		return spellPart;
	}

	public static SpellPart get(ResourceLocation id) {
		if(!SPELL_PARTS.containsKey(id)) {
			throw new IllegalArgumentException("Could not get origin from id '" + id.toString() + "', as it was not registered!");
		}
		SpellPart spellPart = SPELL_PARTS.get(id);

		return spellPart;
	}

	public static void clear() {
		SPELL_PARTS.clear();
	}
}
