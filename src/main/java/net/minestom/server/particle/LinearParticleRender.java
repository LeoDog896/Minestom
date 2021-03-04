package net.minestom.server.particle;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.time.UpdateOption;

/**
 * Represents a particle renderer that renders from position A to positin b.
 */
class LinearParticleRenderer extends ParticleRenderer {

    private final int density;
    private final Position from;
    private final Position to;
    private final SpawnableParticle basePacket;

    public LinearParticleRenderer(
            Position from,
            Position to,
            SpawnableParticle basePacket,
            int density, int lifespan,
            UpdateOption generationInterval
    ) {

        super(lifespan, generationInterval);
        this.density = density;
        this.from = from;
        this.to = to;
        this.basePacket = basePacket;
    }

    @Override
    public ParticlePacket[] generatePackets() {

        final ParticlePacket[] packetArray = new ParticlePacket[density];

        final double stepX = (from.getX() - to.getX()) / density;
        final double stepY = (from.getY() - to.getY()) / density;
        final double stepZ = (from.getZ() - to.getZ()) / density;

        final Position reusablePosition = from.clone();

        for (int i = 0; i < density; i++) {

            ParticlePacket packet = basePacket.clone().getPacket();

            packet.x = reusablePosition.getX();
            packet.y = reusablePosition.getY();
            packet.z = reusablePosition.getZ();

            packetArray[i] = packet;

            reusablePosition.add(stepX, stepY, stepZ);
        }

        return packetArray;
    }
}
