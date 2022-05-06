package me.maximumpower55.aura.item;

import me.maximumpower55.aura.component.PlayerManaComponent;
import me.maximumpower55.aura.entity.SpellProjectileEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SpellBookItem extends AuraItem {
	public SpellBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(!level.isClientSide) {
			PlayerManaComponent manaComponent = PlayerManaComponent.KEY.get(player);

			manaComponent.removeMana(.5d);

			SpellProjectileEntity projectileEntity = new SpellProjectileEntity(level, player);
			projectileEntity.setPos(player.position().add(0, player.getEyeHeight(), 0));
			projectileEntity.shoot(player.getLookAngle());
			level.addFreshEntity(projectileEntity);
		}

		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}
}
