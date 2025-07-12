package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public abstract class PickBlockFeature extends BlockRaycastFeature {
    protected int execute(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            throw new SimpleCommandExceptionType(Text.literal("Failed to get player.")).create();
        }

        var stackOrFeedback = getItemStack(context, blockInfo);
        if (stackOrFeedback.right().isPresent()) {
            throw new SimpleCommandExceptionType(Text.literal("An error has occurred.")).create();
        }

        assert stackOrFeedback.left().isPresent();
        var stack = stackOrFeedback.left().get();

        PlayerInventory playerInventory = client.player.getInventory();
        addPickBlock(playerInventory, stack);
        if (client.interactionManager == null) {
            throw new SimpleCommandExceptionType(Text.literal("Failed to get interaction manager.")).create();
        }

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.getSelectedSlot());

        return 1;
    }

    // reimplementation from 1.18.2
    public void addPickBlock(PlayerInventory pi, ItemStack stack) {
        int i = pi.getSlotWithStack(stack);
        if (PlayerInventory.isValidHotbarIndex(i)) {
            pi.setSelectedSlot(i);
            return;
        }
        if (i == -1) {
            int j;
            pi.setSelectedSlot(pi.getSwappableHotbarSlot());
            if (!pi.getMainStacks().get(pi.getSelectedSlot()).isEmpty() && (j = pi.getEmptySlot()) != -1) {
                pi.getMainStacks().set(j, pi.getMainStacks().get(pi.getSelectedSlot()));
            }
            pi.getMainStacks().set(pi.getSelectedSlot(), stack);
        } else {
            pi.swapSlotWithHotbar(i);
        }
    }

	protected Either<ItemStack, Feedback> getItemStack(CommandContext<FabricClientCommandSource> context, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
		return null;
	}
}
