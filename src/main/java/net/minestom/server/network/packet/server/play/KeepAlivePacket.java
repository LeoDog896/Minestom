package net.minestom.server.network.packet.server.play;

import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public class KeepAlivePacket implements ServerPacket {

    public final long id;

    public KeepAlivePacket(long id) {
        this.id = id;
    }

    @Override
    public void write(@NotNull BinaryWriter writer) {
        writer.writeLong(id);
    }

    @Override
    public int getId() {
        return ServerPacketIdentifier.KEEP_ALIVE;
    }
}
