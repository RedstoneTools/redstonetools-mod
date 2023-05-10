# Telemetry
When you launch Minecraft with Redstone Tools loaded for the first time you'll be asked whether you want to enable telemetry or not. Telemetry allows us mod developers to improve the quality of the mod and find and solve issues faster, absolutely no personal data is sent to our servers or stored anywhere so please consider enabling it.

<br>

# Macros
One of Redstone Tools' biggest features is the macro system it adds. The macro system allows users to increase their productivity by removing the need to type commands into the chat and instead lets the user press a key to run a single or multiple commands (macros can also be triggered using a command, they don't require keybinds). Below is a guide on how to get started using macros.

## The Macro Menu
To add, edit, delete, disable/enable or simply inspect macros you have to open the macro menu, to do this open the main menu (or the escape menu), and go to `Options... -> Controls... -> Macros...`. Once you're in the macro menu you'll be able to:
- Edit macros by clicking the pencil icon next to the macro you want to edit.
- Delete macros by clicking the red X next to the macro you want to delete.
- Disable/enable macros by unchecking/checking the box next to the macro you want to disable/enable.
- Create a new macro by pressing the `Create new...` button.

## The Macro Edit Screen
Once you're editing a macro you'll have the option to change its name, keybind, and commands. Note that changes won't be saved until you click `Done`, to discard changes you can click `Cancel` or press escape.

<br>

# Commands
Redstone Tools adds a bunch of commands to improve productivity. Below is a list of the names of all commands added by Redstone Tools and what they do.

## New Commands
### `/copystate`
Gives you an exact copy of the block you're looking at including it's NBT data.

<br>

### `/glass`
Gives you a glass version of the block you're looking at, or, if you're looking at a glass block, a wool version.

<br>

### `/quicktp [distance=50.0] [includeFluids=false]`
Teleports you `distance` blocks in the direction you're looking or to the block you are looking at if it's closer.

#### **distance**
The distance to teleport you in blocks, defaults to 50.0.

#### **includeFluids**
Whether to teleport to a fluid you're looking at if it's closer than `distance`, will let you teleport through fluids if set to false, defaults to false.

<br>

### `/macro <macro>`
Runs a macro.

#### **macro**
The name of the macro to run.

<br>

### `/base <number> <fromBase> <toBase>`
Converts a number to a different base and outputs it.

#### **number**
The number to convert.

#### **fromBase**
The base `number` is in.

#### **toBase**
The base to convert `number` to.

<br>

### `/ss [signalStrength=15]`
Gives you a barrel that outputs the specified signal strength.

#### **signalStrength**
The signal strength the barrel should output, defaults to 15.

<br>

### `/airplace`
Lets you place blocks in the air if there is no block in reach.

<br>

### `/autodust`
Automatically places redstone dust on colored blocks such as wool and glass that you place.

<br>

## WorldEdit Extensions
### `//colorcode <color> [onlyColor]`
Converts all colored blocks such as wool and glass in your selection to the given color.

#### **color**
The color to convert the selected blocks to.

#### **onlyColor**
If set will only color the blocks with this color. For example, if `onlyColor` is red only red blocks will be recolored.

<br>

### `//rstack [count=1] [direction=me] [spacing=2]`
Stacks your selection with a custom distance.

#### **count**
The amount of times to stack your selection, defaults to 1.

#### **direction**
The direction to stack your selection in, defaults to me (the direction you're looking in).

#### **spacing**
The space between copies, defaults to 2.

<br>

### `//update`
Force updates all blocks in your selection.

<br>

### `//minsel`
Minimizes your selection by removing surrounding layers consisting of only air.

<br>

### `//read [spacing=1] [onBlock=redstone_lamp[lit=true]] [toBase=10] [reverseBits=false]`
Interprets your selection as a binary number and outputs it.

#### **spacing**
The space between blocks, defaults to 1.

#### **onBlock**
The block that represents a 1 bit, defaults to redstone_lamp[lit=true] (a lit redstone lamp).

#### **toBase**
The base to output the value of the binary number in, defaults to 10.

#### **reverseBits**
Whether the bits are in reverse, defaults to false.
