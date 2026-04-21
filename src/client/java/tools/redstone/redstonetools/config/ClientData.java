package tools.redstone.redstonetools.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigString;
import kr1v.malilibApi.annotation.Config;
import tools.redstone.redstonetools.RedstoneTools;

@Config(RedstoneTools.MOD_ID)
public class ClientData {
	public static final ConfigBoolean ENABLE_MATH_VARIABLES = new ConfigBoolean("Enable math and variables for the chat input suggester", true,
		"""
			Whether or not to try to inject variables and math expressions into the command input suggester.
			
			With this enabled, Redstone tools will attempt to prevent chat suggestion from breaking if you're using variables and or math expressions inside of a command.
			With this disabled, variables and math expressions will still be inserted upon sending a chat command""");
	public static final ConfigString VARIABLE_BEGIN_STRING 		= new ConfigString("Variable begin string", "'", "The string that should be used to denote the start of a variable. Can be empty");
	public static final ConfigString VARIABLE_END_STRING 		= new ConfigString("Variable end string", 	"'", "The string that should be used to denote the end of a variable. Can be empty");
	public static final ConfigString MATH_BEGIN_STRING 			= new ConfigString("Math begin string", 	"{", "The string that should be used to denote the start of a math expression. Can be empty, unsure if you'd want that though.");
	public static final ConfigString MATH_END_STRING 			= new ConfigString("Math end string", 		"}", "The string that should be used to denote the end of a math expression. Can be empty, unsure if you'd want that though.");
	public static final ConfigString AUTORUN_FIRST_WORLD_ENTRY 	= new ConfigString("First world entry", 	"",  "Command/message that will be run/sent the first time you join a world in this session");
	public static final ConfigString AUTORUN_WORLD_ENTRY 		= new ConfigString("World entry", 			"",  "Command/message that will be run/sent when you join a world");
	public static final ConfigString AUTORUN_DIMENSION_CHANGE 	= new ConfigString("Dimension change", 		"",  "Command/message that will be run/sent after you change dimensions");
}
