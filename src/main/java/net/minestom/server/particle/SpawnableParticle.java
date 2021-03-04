package net.minestom.server.particle;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.utils.binary.BinaryWriter;
import net.minestom.server.utils.clone.PublicCloneable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Cloneable particle that also has metadata.
 */
public class SpawnableParticle extends ParticlePacket implements PublicCloneable<SpawnableParticle> {

    public SpawnableParticle(Particle particle, boolean distance,
                             double x, double y, double z,
                             float offsetX, float offsetY, float offsetZ,
                             float particleData, int count, Consumer<BinaryWriter> dataWriter) {

        this.particleId = particle.getId();
        this.longDistance = distance;

        this.x = x;
        this.y = y;
        this.z = z;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.particleData = particleData;
        this.particleCount = count;
        this.dataConsumer = dataWriter;
    }

    public SpawnableParticle(Particle particle,
                             double x, double y, double z,
                             float offsetX, float offsetY, float offsetZ,
                             int count) {
         this(particle, false,
                x, y, z,
                offsetX, offsetY, offsetZ,
                0, count, null);
    }

    @NotNull
    @Override
    public SpawnableParticle clone() {
        try {
            return (SpawnableParticle) super.clone();
        } catch (CloneNotSupportedException e) {
            MinecraftServer.getExceptionManager().handleException(e);
            return null;
        }
    }
}
