package me.maximumpower55.aura.registry;

import me.maximumpower55.aura.entity.SpellProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class AuraEntities {
	public static final EntityType<SpellProjectileEntity> SPELL_PROJECTILE = AuraRegistry.registerEntity("spell_projectile", EntityType.Builder.<SpellProjectileEntity>of(
		SpellProjectileEntity::new, MobCategory.MISC)
		.sized(.5f, .5f)
		.clientTrackingRange(64)
	);

	static void register() {}
}
