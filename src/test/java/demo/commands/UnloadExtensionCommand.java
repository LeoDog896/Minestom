package demo.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extensions.ExtensionManager;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UnloadExtensionCommand extends Command {
    public UnloadExtensionCommand() {
        super("unload");

        setDefaultExecutor(this::usage);

        Argument extension = ArgumentType.DynamicStringArray("extensionName");

        setArgumentCallback(this::extensionCallback, extension);

        addSyntax(this::execute, extension);
    }

    private void usage(CommandSender sender, Arguments arguments) {
        sender.sendMessage(Component.text("Usage: /unload <extension name>"));
    }

    private void execute(CommandSender sender, Arguments arguments) {
        String name = join(arguments.getStringArray("extensionName"));
        sender.sendMessage(Component.text("extensionName = " + name + "...."));

        ExtensionManager extensionManager = MinecraftServer.getExtensionManager();
        Extension ext = extensionManager.getExtension(name);
        if (ext != null) {
            try {
                extensionManager.unloadExtension(name);
            } catch (Throwable t) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    t.printStackTrace();
                    t.printStackTrace(new PrintStream(baos));
                    baos.flush();
                    baos.close();
                    String contents = baos.toString(StandardCharsets.UTF_8);
                    contents.lines().forEach(sender::sendMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            sender.sendMessage(Component.text("Extension '" + name + "' does not exist."));
        }
    }

    private void extensionCallback(CommandSender sender, ArgumentSyntaxException exception) {
        sender.sendMessage(Component.text("'" + exception.getInput() + "' is not a valid extension name!"));
    }

    private String join(String[] extensionNameParts) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < extensionNameParts.length; i++) {
            String s = extensionNameParts[i];
            if (i != 0) {
                b.append(StringUtils.SPACE);
            }
            b.append(s);
        }
        return b.toString();
    }
}
