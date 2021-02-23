package net.minestom.server.particle;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.clone.PublicCloneable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Cloneable ParticlePacket wrapper.
 */
public class ParticleWrapper implements PublicCloneable<ParticleWrapper> {

    private final ParticlePacket packet;

    public ParticleWrapper(Particle particle, boolean distance,
                           double x, double y, double z,
                           float offsetX, float offsetY, float offsetZ,
                           float particleData, int count, Consumer<BinaryWriter> dataWriter) {
        ParticlePacket particlePacket = new ParticlePacket();
        particlePacket.particleId = particle.getId();
        particlePacket.longDistance = distance;

        particlePacket.x = x;
        particlePacket.y = y;
        particlePacket.z = z;

        particlePacket.offsetX = offsetX;
        particlePacket.offsetY = offsetY;
        particlePacket.offsetZ = offsetZ;

        particlePacket.particleData = particleData;
        particlePacket.particleCount = count;
        particlePacket.dataConsumer = dataWriter;

        this.packet = particlePacket;
    }

    public ParticleWrapper(Particle particle,
                           double x, double y, double z,
                           float offsetX, float offsetY, float offsetZ,
                           int count) {
         this(particle, false,
                x, y, z,
                offsetX, offsetY, offsetZ,
                0, count, null);
    }

    public ParticlePacket getPacket() {
        return this.packet;
    }

    @NotNull
    @Override
    public ParticleWrapper clone() {
        try {
            return (ParticleWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            MinecraftServer.getExceptionManager().handleException(e);
            return null;
        }
    }
}
