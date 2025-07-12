//package tools.redstone.redstonetools.features.arguments.serializers;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.command.CommandRegistryAccess;
//import net.minecraft.command.argument.BlockStateArgument;
//import net.minecraft.command.argument.BlockStateArgumentType;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.Registry;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryWrapper;
//import net.minecraft.resource.featuretoggle.FeatureSet;
//
//import java.util.Optional;
//import java.util.stream.Stream;
//
//public class BlockStateArgumentSerializer extends BrigadierSerializer<BlockStateArgument, String> {
//
//    private static final BlockStateArgumentSerializer INSTANCE = new BlockStateArgumentSerializer();
//
//    private BlockStateArgumentSerializer() {
//        super(BlockStateArgument.class, BlockStateArgumentType.blockState(new CommandRegistryAccess() {
//            @Override
//            public FeatureSet getEnabledFeatures() {
//                return null;
//            }
//
//            @Override
//            public Stream<RegistryKey<? extends Registry<?>>> streamAllRegistryKeys() {
//                return Stream.empty();
//            }
//
//            @Override
//            public <T> Optional<? extends RegistryWrapper.Impl<T>> getOptional(RegistryKey<? extends Registry<? extends T>> registryRef) {
//                return Optional.empty();
//            }
//        })); // what is this for?
//    }
//
////    private BlockStateArgumentSerializer() {
////        super(BlockStateArgument.class, BlockStateArgumentType.blockState());
////    }
//
//    public static BlockStateArgumentSerializer blockState() {
//        return INSTANCE;
//    }
//
//    @Override
//    public BlockStateArgument deserialize(String serialized) {
//        try {
//            return deserialize(new StringReader(serialized));
//        } catch (CommandSyntaxException e) {
//            throw new IllegalStateException("Syntax Exception: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String serialize(BlockStateArgument value) {
//        var state = value.getBlockState();
//        var block = state.getBlock();
//
//        var builder = new StringBuilder()
//                .append(Registries.BLOCK.getId(block));
//
//        if (state.getProperties().size() == 0) {
//            return builder.toString();
//        }
//
//        builder.append('[');
//        var first = true;
//        for (var prop : state.getProperties()) {
//            if (first) {
//                first = false;
//            } else {
//                builder.append(',');
//            }
//
//            builder.append(prop.getName())
//                    .append('=')
//                    .append(state.get(prop));
//        }
//        builder.append(']');
//
//        return builder.toString();
//    }
//
//}
