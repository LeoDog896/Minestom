package net.minestom.server.network.packet.server.play;

import net.minestom.server.MinecraftServer;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.registry.Registries;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TagsPacket implements ServerPacket {

    public final List<Tag> blockTags = new LinkedList<>();
    public final List<Tag> itemTags = new LinkedList<>();
    public final List<Tag> fluidTags = new LinkedList<>();
    public final List<Tag> entityTags = new LinkedList<>();

    private static final TagsPacket REQUIRED_TAGS_PACKET = new TagsPacket();

    static {
        MinecraftServer.getTagManager().addRequiredTagsToPacket(REQUIRED_TAGS_PACKET);
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writeTags(writer, blockTags, name -> Registries.getBlock(name).ordinal());
        writeTags(writer, itemTags, name -> Registries.getMaterial(name).ordinal());
        writeTags(writer, fluidTags, name -> Registries.getFluid(name).ordinal());
        writeTags(writer, entityTags, name -> Registries.getEntityType(name).ordinal());
    }

    private void writeTags(BinaryWriter writer, List<Tag> tags, Function<NamespaceID, Integer> idSupplier) {
        writer.writeVarInt(tags.size());
        for (Tag tag : tags) {
            // name
            writer.writeSizedString(tag.getName().toString());

            final Set<NamespaceID> values = tag.getValues();
            // count
            writer.writeVarInt(values.size());
            // entries
            for (NamespaceID name : values) {
                writer.writeVarInt(idSupplier.apply(name));
            }
        }
    }

    @Override
    public int getId() {
        return ServerPacketIdentifier.TAGS;
    }

    /**
     * Gets the {@link TagsPacket} sent to every {@link net.minestom.server.entity.Player}
     * on login.
     *
     * @return the default tags packet
     */
    @NotNull
    public static TagsPacket getRequiredTagsPacket() {
        return REQUIRED_TAGS_PACKET;
    }
}
