package me.maximumpower55.aura.entity;

import org.quiltmc.qsl.networking.api.PlayerLookup;

import me.maximumpower55.aura.registry.AuraEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellProjectileEntity extends Projectile {
	// TODO: Put these in a config.
	private static final double SPEED = 1.5d;
	private static final int EXPIRE_TIME = 1 * 20;

	private Vec3 lastPosition;
	private int age;

	public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level levelIn) {
		super(entityType, levelIn);
	}

	public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level levelIn, double x, double y, double z) {
		super(entityType, levelIn);
		setPos(x, y, z);
	}

	public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, Level levelIn, LivingEntity shooter) {
		super(entityType, levelIn);
		setOwner(shooter);
	}

	public SpellProjectileEntity(Level levelIn, LivingEntity shooter) {
		this(AuraEntities.SPELL_PROJECTILE, levelIn);
	}

	@Override
	public void tick() {
		super.tick();

		age++;
		if(age > EXPIRE_TIME) {
			discard();
			return;
		}

		if(!level.isClientSide) {
			lastPosition = position();

			HitResult hit = level.clip(new ClipContext(lastPosition, position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

			if(hit != null) {
				switch(hit.getType()) {
					case MISS:
						break;
					default:
						kill();
						System.out.println(hit);
						return;
				}
			}

			spawnParticles();
		}

		setPos(position().add(getDeltaMovement()));
	}

	private void spawnParticles() {
		for (int i = 0; i < level.random.nextInt(4)+1; i++) {
			double x = getX();
			double y = getY();
			double z = getZ();
			double deltaX = level.random.nextDouble(.6d);
			double deltaY = level.random.nextDouble(.25d);
			double deltaZ = level.random.nextDouble(.6d);

			// Thanks to Arcanus for this line from https://github.com/CammiePone/Arcanus/blob/1.18-dev/src/main/java/dev/cammiescorner/arcanus/common/entities/MagicMissileEntity.java#L51
			PlayerLookup.tracking(this).forEach(player -> ((ServerLevel)level).sendParticles(player, ParticleTypes.END_ROD, true, x, y, z, 1, deltaX, deltaY, deltaZ, .1d));
		}
	}

	@Override
	public void lerpMotion(double d, double e, double f) {
		super.lerpMotion(d, e, f);
		age = 0;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double d) {
		double e = this.getBoundingBox().getSize() * 10.0;
		if (Double.isNaN(e)) {
			e = 1.0;
		}

		e *= 64.0 * getViewScale();
		return d < e * e;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putShort("Age", (short)age);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		age = tag.getShort("Age");
	}

	@Override
	protected void defineSynchedData() {}

	public void shoot(Vec3 rotation) {
		setDeltaMovement(rotation.scale(SPEED));
	}

	// TODO: Put this in a config.
	// private static final double SPEED = 1.5d;

	// private final Player owner;
	// private Vec3 position;
	// private Vec3 lastPosition;
	// private final Vec3 rotation;

	// public SpellProjectileEntity(Player owner, Vec3 startPosition, Vec3 rotation) {
	// 	this.owner = owner;
	// 	this.position = startPosition;
	// 	this.lastPosition = position;
	// 	this.rotation = rotation;
	// }

	// public boolean tick(ServerLevel level) {
	// 	boolean entityHit = ProjectileUtil.getEntityHitResult(owner, lastPosition, position, new AABB(lastPosition, position).inflate(1d), entity -> true, SPEED * SPEED) != null;
	// 	boolean blockHit = !level.getBlockState(new BlockPos(position)).isAir();
	// 	boolean hit = entityHit || blockHit;

	// 	if(entityHit) {
	// 		hitTarget();
	// 		AuraMod.LOGGER.info("EEEEEE");
	// 	}

	// 	if(hit) {
	// 		AuraUtil.spawnParticles(level, ParticleTypes.END_ROD, true, lastPosition.x, lastPosition.y, lastPosition.z, .1d, .1d, .1d, .2d, 30);
	// 		return false;
	// 	}

	// 	double particlePosX = position.x + .5d + level.random.nextFloat() * 6d / 16d;
	// 	double particlePosY = position.y + level.random.nextFloat() * 6d / 16d;
	// 	double particlePosZ = position.z + .5d + level.random.nextFloat() * 6d / 16d;

	// 	AuraUtil.spawnParticles(level, ParticleTypes.END_ROD, true, particlePosX, particlePosY, particlePosZ, 0d, 0d, 0d, 0d, 3);

	// 	lastPosition = position;
	// 	position = position.add(rotation.scale(SPEED));

	// 	return true;
	// }

	// public void hitTarget() {}

	// public static SpellProjectileEntity createFromNbt(Level level, CompoundTag tag) {
	// 	Player owner = level.getPlayerByUUID(tag.getUUID("Owner"));

	// 	if(owner != null)
	// 		return new SpellProjectileEntity(owner, readVec3Tag(tag.getList("Pos", Tag.TAG_DOUBLE)), readVec3Tag(tag.getList("Rotation", Tag.TAG_DOUBLE)));

	// 	return null;
	// }

	// public void saveToNbt(CompoundTag tag) {
	// 	tag.put("Position", createVec3Tag(position));
	// 	tag.put("Rotation", createVec3Tag(rotation));
	// 	tag.putUUID("Owner", owner.getUUID());
	// }

	// private static ListTag createVec3Tag(Vec3 vec) {
	// 	ListTag tag = new ListTag();

	// 	for(double component : new double[]{vec.x, vec.y, vec.z}) {
	// 		tag.add(DoubleTag.valueOf(component));
	// 	}

	// 	return tag;
	// }

	// private static Vec3 readVec3Tag(ListTag tag) {
	// 	return new Vec3(tag.getDouble(0), tag.getDouble(1), tag.getDouble(2));
	// }
}
