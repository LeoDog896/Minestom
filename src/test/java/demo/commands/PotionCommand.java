package demo.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Arguments;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

public class PotionCommand extends Command {

    public PotionCommand() {
        super("potion");

        setCondition(this::condition);

        setDefaultExecutor(((sender, args) -> {
            sender.sendMessage(Component.text("Usage: /potion [type] [duration (seconds)]"));
        }));

        var potionArg = ArgumentType.Potion("potion");
        var durationArg = ArgumentType.Integer("duration");

        addSyntax(this::onPotionCommand, potionArg, durationArg);
    }

    private boolean condition(CommandSender sender, String commandString) {
        if (!sender.isPlayer()) {
            sender.sendMessage(Component.text("The command is only available for players"));
            return false;
        }
        return true;
    }

    private void onPotionCommand(CommandSender sender, Arguments args) {
        final Player player = (Player) sender;
        final PotionEffect potion = args.get("potion");
        final int duration = args.get("duration");

        player.sendMessage(Component.text(player.getActiveEffects().toString()));
        player.addEffect(new Potion(
                potion,
                (byte) 0,
                duration * MinecraftServer.TICK_PER_SECOND
        ));
    }

}