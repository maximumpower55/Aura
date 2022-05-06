package me.maximumpower55.aura.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import me.maximumpower55.aura.AuraMod;
import me.maximumpower55.aura.component.PlayerManaComponent;
import me.maximumpower55.aura.registry.AuraItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class ManaBarOverlayRenderer extends GuiComponent {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	private static final ResourceLocation MANA_BAR_BORDER_TEXTURE = AuraMod.id("textures/gui/mana_bar_border.png");
	private static final ResourceLocation MANA_BAR_MANA_TEXTURE = AuraMod.id("textures/gui/mana_bar_mana.png");

	public static void renderOverlay(PoseStack poseStack, float tickDelta) {
		boolean holdingSpellBook = AuraItems.SPELL_BOOK == MINECRAFT.player.getMainHandItem().getItem() || AuraItems.SPELL_BOOK == MINECRAFT.player.getOffhandItem().getItem();

		if(!holdingSpellBook) return;

		PlayerManaComponent manaComponent = PlayerManaComponent.KEY.get(MINECRAFT.player);

		int width = MINECRAFT.getWindow().getGuiScaledWidth() - 5;
		int height = MINECRAFT.getWindow().getGuiScaledHeight() - 5;

		RenderSystem.setShaderTexture(0, MANA_BAR_MANA_TEXTURE);
		blit(poseStack, width / 2 + 100, height - 13, 0, 0, (int)(62 * (manaComponent.getMana() / (double)manaComponent.getMaxMana())), 14, 62, 14);

		RenderSystem.setShaderTexture(0, MANA_BAR_BORDER_TEXTURE);
		blit(poseStack, width / 2 + 100, height - 13, 0, 0, 62, 14, 62, 14);

		drawCenteredString(poseStack, MINECRAFT.font, String.format("%s / %s", manaComponent.getMana(), manaComponent.getMaxMana()), width / 2 + 130, height - 23, 16777215);
	}
}
