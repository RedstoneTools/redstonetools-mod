# Contributing
Please read this document before contributing.

# Bug reports
If you find a bug please create a new GitHub issue for it (make sure it hasn't been reported before) including the steps needed to reproduce the issue, any relevant information about your environment and the latest Minecraft log file (google how to find them if you don't know how to).

# Feature requests
If you want to request a feature please create a new GitHub issue for it (make sure it hasn't been requested before) containing all the necessary details about the feature.

# Contributions
If you would like to work on this project by fixing bugs or adding features please ask to be assigned to the corresponding issue and create a pull request into the dev branch once you fixed the bug or added the feature and request a review from the person that assigned you. Please write clear commit messages and keep your commits small. Be prepared to make any additional changes if the reviewer asks for them.

<br>

If you have any questions or feedback, please reach out to us on Discord (link in the README).

# Developer Guide: How to make a feature

### Model
The mod is divided into the core and features.

The core  is the part of the mod which provides systems for
the features  to use, like the configuration system, or
houses general services, like telemetry data or macros.

The features are responsible for implementing the content.

### Setting up a base feature
Each feature has to extend [`AbstractFeature`](src/main/java/com/domain/redstonetools/features/AbstractFeature.java) in some way,
as well as provide basic information through the `@Feature` annotation.

```java
@Feature(name = "My Feature", description = "Test feature", command = "" /* this option is useless unless you extend CommandFeature */)
public class MyFeature extends AbstractFeature {
    
    /* Automatically called when commands should be registered to 
     * the server dispatcher, no need to subscribe to an event.  */
    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        
    }
    
}
```

This feature will not do much, as it does not provide any additional logic
by extending built in classes. All it provides is a registration callback
in the form of the `void register()` method which you can override.

### Making a command feature
To make a feature which provides a (server)command to the user,
you will need to extend [`CommandFeature`](src/main/java/com/domain/redstonetools/features/commands/CommandFeature.java).

It provides standardized logic for building a command with (optional) arguments.
It will read the command name from the descriptive `@Feature` annotation and read the arguments
from all [`Argument<T>`](src/main/java/com/domain/redstonetools/features/arguments/Argument.java) fields in your feature class.
> **NOTE:** This architecture does not allow for any subcommands.
> Only one argument chain can be bound to the main command.

They take [`TypeSerializer`](src/main/java/com/domain/redstonetools/features/arguments/TypeSerializer.java)s
as the type for parsing and suggesting values.
The argument fields are read in order, so to create a command `/myfeature <a string> <an optional integer>` we can do:
```java
@Feature(name = "My Feature", description = "Test feature", command = "mycommand")
public class MyFeature extends CommandFeature {
    
    /* Each argument has to be declared as public static final
     * and is automatically named using the field name if no name is explicitly set. */
    public static final Argument<String> aString = Argument
            .ofType(StringSerializer.string());
            
    public static final Argument<Integer> anOptionalInteger = Argument
            .ofType(IntegerSerializer.integer())
            /* withDefault automatically marks it as optional */
            .withDefault(1);
        
    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        /* put your handler code here */
        
        return Feedback.none();
    }

}
```

To make your command do something, you have to override and implement the
`Feedback execute(ServerCommandSource source)` method declared by the `CommandFeature` class.

Before we do that, you need to understand the `Feedback` system. It is a standardized way of
sending formatted feedback to the user and handler code. There are 5 types:
- `Feedback.none()` - No explicit feedback in the form of a message, treated as a success.
- `Feedback.success(String message)` - Signals successful completion, with a message attached.
- `Feedback.warning(String message)` - Signals that it was completed, but with a warning attached.
- `Feedback.error(String message)` - Signals that the operation failed severely.
- `Feedback.invalidUsage(String message)` - Signals invalid usage/invocation of the operation.

To write a simple command which repeats the given string a number of times, with 1 being the default
we can do:
```java
@Feature(name = "My Feature", description = "Test feature", command = "mycommand")
public class MyFeature extends CommandFeature {
    
    /* Each argument has to be declared as public static final
     * and is automatically named using the field name if no name is explicitly set. */
    public static final Argument<String> repeatString = Argument
            .ofType(StringSerializer.string());
            
    public static final Argument<Integer> count = Argument
            .ofType(IntegerSerializer.integer())
            /* withDefault automatically marks it as optional */
            .withDefault(1);
        
    @Override
    protected Feedback execute(ServerCommandSource source) throws CommandSyntaxException {
        String str = repeatString.getValue();
        Integer count = count.getValue();
        
        // error if count is below 1
        if (count < 1) {
            return Feedback.invalidUsage("Count can not be below 1");
        }
        
        return Feedback.success("Here is the repeated string: " + str.repeat(count));
    }

}
```

Not a very useful feature or command, but it shows the different systems
associated with creating a command.

### Making a toggleable feature
> **TODO**