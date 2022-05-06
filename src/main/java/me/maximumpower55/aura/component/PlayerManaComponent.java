package me.maximumpower55.aura.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import me.maximumpower55.aura.AuraMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerManaComponent implements AutoSyncedComponent, PlayerComponent<PlayerManaComponent> {
	public static final ComponentKey<PlayerManaComponent> KEY = ComponentRegistry.getOrCreate(AuraMod.id("mana"), PlayerManaComponent.class);

	private Player player;

	private double mana;

	private int maxMana;

	public PlayerManaComponent(Player player) {
		this.player = player;
		this.maxMana = 100;
		this.mana = 100d;
	}

	public double getMana() {
		return mana;
	}

	public void setMana(double value) {
		if(value > maxMana){
			setMana(maxMana);
		} else if(value < 0){
			setMana(0);
		} else {
			mana = value;
		}

		PlayerManaComponent.KEY.sync(player);
	}

	public void addMana(double toAdd) {
		setMana(mana + toAdd);
	}

	public void removeMana(double toRemove) {
		if(toRemove < 0) toRemove = 0;
		setMana(mana - toRemove);
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int value) {
		maxMana = value;
		PlayerManaComponent.KEY.sync(player);
	}

	@Override
	public boolean shouldSyncWith(ServerPlayer player) {
		return player == this.player;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setMana(tag.getDouble("current"));
		setMaxMana(tag.getInt("max"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putDouble("current", mana);
		tag.putInt("max", maxMana);
	}

	@Override
	public void copyFrom(PlayerManaComponent other) {
		mana = other.getMana();
		maxMana = other.getMaxMana();
	}
}
