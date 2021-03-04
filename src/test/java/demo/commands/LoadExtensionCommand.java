package demo.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import net.minestom.server.extensions.ExtensionManager;

import java.io.IOException;
import java.nio.file.Path;

public class LoadExtensionCommand extends Command {
    public LoadExtensionCommand() {
        super("load");

        setDefaultExecutor(this::usage);

        Argument extension = ArgumentType.DynamicStringArray("extensionName");

        setArgumentCallback(this::extensionCallback, extension);

        addSyntax(this::execute, extension);
    }

    private void usage(CommandSender sender, Arguments arguments) {
        sender.sendMessage(Component.text("Usage: /load <extension file name>"));
    }

    private void execute(CommandSender sender, Arguments arguments) {
        String name = join(arguments.getStringArray("extensionName"));
        sender.sendMessage(Component.text("extensionFile = "+name+"...."));

        ExtensionManager extensionManager = MinecraftServer.getExtensionManager();
        Path extensionFolder = extensionManager.getExtensionFolder().toPath().toAbsolutePath();
        Path extensionJar = extensionFolder.resolve(name);
        try {
            if(!extensionJar.toFile().getCanonicalPath().startsWith(extensionFolder.toFile().getCanonicalPath())) {
                sender.sendMessage(Component.text("File name '"+name+"' does not represent a file inside the extensions folder. Will not load"));
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(Component.text("Failed to load extension: "+e.getMessage()));
            return;
        }

        try {
            boolean managed = extensionManager.loadDynamicExtension(extensionJar.toFile());
            if(managed) {
                sender.sendMessage(Component.text("Extension loaded!"));
            } else {
                sender.sendMessage(Component.text("Failed to load extension, check your logs."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Component.text("Failed to load extension: "+e.getMessage()));
        }
    }

    private void extensionCallback(CommandSender sender, ArgumentSyntaxException exception) {
        sender.sendMessage(Component.text("'" + exception.getInput() + "' is not a valid extension name!"));
    }

    private String join(String[] extensionNameParts) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < extensionNameParts.length; i++) {
            String s = extensionNameParts[i];
            if(i != 0) {
                b.append(" ");
            }
            b.append(s);
        }
        return b.toString();
    }
}
