package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.features.toggleable.BigDustFeature;

import static tools.redstone.redstonetools.features.arguments.serializers.IntegerSerializer.integer;

@AutoService(AbstractFeature.class)
@Feature(name = "Big Dust", description = "Change the size of redstone's hitbox.", command = "bigdust")
public class BigDustHeightFeature extends CommandFeature {

    public int customHeight = 1;

    public static final Argument<Integer> heightPixels = Argument
            .ofType(integer(1, 16));

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        customHeight = heightPixels.getValue();

        BigDustFeature bigDustFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustFeature.class);
        if (!bigDustFeature.isEnabled()) {
            bigDustFeature.enable(source);
        }

        double heightInBlocks = customHeight/16.0;

        return Feedback.success("Redstone dust hitbox height set to {} block(s).", heightInBlocks);
    }

}
