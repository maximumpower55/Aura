package me.maximumpower55.aura.api.spell;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

public interface SpellPart extends Comparable<SpellPart> {
	ResourceLocation getId();
	@Nullable String getName();

	int getManaCost();

	boolean isCompatibleWith(SpellPart other);

	@Override
	default int compareTo(SpellPart o) {
		return 0;
	}
}
