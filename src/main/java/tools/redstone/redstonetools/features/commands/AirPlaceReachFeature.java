package tools.redstone.redstonetools.features.commands;

import com.google.auto.service.AutoService;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import tools.redstone.redstonetools.RedstoneToolsClient;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.Feature;
import tools.redstone.redstonetools.features.arguments.Argument;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.features.toggleable.AirPlaceFeature;

import static tools.redstone.redstonetools.features.arguments.serializers.FloatSerializer.floatArg;

@AutoService(AbstractFeature.class)
@Feature(name = "Air Place", description = "Allows you to place blocks in the air.", command = "airplace")
public class AirPlaceReachFeature extends CommandFeature {

    public float reach = 5.0f;

    public static final Argument<Float> distance = Argument
        .ofType(floatArg(5.0f));

    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {

        reach = distance.getValue();

        AirPlaceFeature airPlaceFeature = RedstoneToolsClient.INJECTOR.getInstance(AirPlaceFeature.class);
        if (!airPlaceFeature.isEnabled()) {
            airPlaceFeature.enable(source);
        }

        return Feedback.success("Air Place reach set to " + distance.getValue().toString() + " blocks.");

    }
}
