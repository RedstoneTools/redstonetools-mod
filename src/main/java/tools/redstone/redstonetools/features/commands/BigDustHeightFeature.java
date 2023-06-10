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

import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;

@AutoService(AbstractFeature.class)
@Feature(name = "Big Dust", description = "Change the size of redstone's hitbox.", command = "bigdust")
public class BigDustHeightFeature extends CommandFeature {

    public float customHeight = 8.0f;

    public static final Argument<Float> height = Argument
            .ofType(floatArg(1.0f, 16.0f));

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        customHeight = height.getValue();

        BigDustFeature bigDustFeature = RedstoneToolsClient.INJECTOR.getInstance(BigDustFeature.class);
        if (!bigDustFeature.isEnabled()) {
            bigDustFeature.enable(source);
        }

        double heightInBlocks = customHeight/16.0;

        if (heightInBlocks == 1) {
            return Feedback.success("Redstone dust hitbox height set to " + heightInBlocks + " block.");
        }
        return Feedback.success("Redstone dust hitbox height set to " + heightInBlocks + " blocks.");
    }

}
