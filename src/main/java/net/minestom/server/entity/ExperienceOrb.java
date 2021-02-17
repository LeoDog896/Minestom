package net.minestom.server.entity;

import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.SpawnExperienceOrbPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExperienceOrb extends Entity {

    private short experienceCount;
    private Player target;
    private long lastTargetUpdateTick;


    public ExperienceOrb(short experienceCount, @NotNull Position spawnPosition) {
        super(EntityType.EXPERIENCE_ORB, spawnPosition);
        setGravity(0.02f, 0.04f, 1.96f);
        setBoundingBox(0.5f, 0.5f, 0.5f);
        //todo vanilla sets random velocity here?
        this.experienceCount = experienceCount;
    }

    public ExperienceOrb(short experienceCount, @NotNull Position spawnPosition, @Nullable Instance instance) {
        this(experienceCount, spawnPosition);

        if (instance != null) {
            setInstance(instance);
        }
    }

    @Override
    public void update(long time) {

        // TODO slide toward nearest player

        //todo water movement
        if (hasNoGravity()) {
            setVelocity(getVelocity().add(0, -0.3f, 0));
        }

        //todo lava

        float distance = 8.0f;
        if (lastTargetUpdateTick < time - 20 + getEntityId() % 100) {
            if (target == null || target.getPosition().getDistanceSquared(getPosition()) > 64) {
                this.target = getClosestPlayer(this, distance);
            }

            lastTargetUpdateTick = time;
        }

        if (target != null && target.getGameMode() == GameMode.SPECTATOR) {
            target = null;
        }

        if (target != null) {
            Position pos = getPosition();
            Position targetPos = target.getPosition();
            Vector toTarget = new Vector(targetPos.getX() - pos.getX(), targetPos.getY() + (target.getEyeHeight() / 2) - pos.getY(), targetPos.getZ() - pos.getZ());
            double e = toTarget.length(); //could really be lengthSquared
            if (e < 8) {
                double f = 1 - (e / 8);
                setVelocity(getVelocity().add(toTarget.normalize().multiply(f * f * 0.1)));
            }
        }

        // Move should be called here
        float g = 0.98f;
        if (this.onGround) {
//            g = 2f;
            g = 0.6f * 0.98f;
        }

        // apply slipperiness

        setVelocity(getVelocity().multiply(new Vector(g, 0.98f, g)));
        if (isOnGround())
            setVelocity(getVelocity().multiply(new Vector(1, -0.9f, 1)));
    }

    @Override
    public void spawn() {

    }

    @Override
    public boolean addViewer(@NotNull Player player) {
        final boolean result = super.addViewer(player); // Add player to viewers list
        if (!result)
            return false;

        final PlayerConnection playerConnection = player.getPlayerConnection();

        SpawnExperienceOrbPacket experienceOrbPacket = new SpawnExperienceOrbPacket();
        experienceOrbPacket.entityId = getEntityId();
        experienceOrbPacket.position = getPosition();
        experienceOrbPacket.expCount = experienceCount;

        playerConnection.sendPacket(experienceOrbPacket);
        playerConnection.sendPacket(getVelocityPacket());

        return true;
    }

    /**
     * Gets the experience count.
     *
     * @return the experience count
     */
    public short getExperienceCount() {
        return experienceCount;
    }

    /**
     * Changes the experience count.
     *
     * @param experienceCount the new experience count
     */
    public void setExperienceCount(short experienceCount) {
        for (Player viewer : getViewers()) this.removeViewer(viewer);

        this.experienceCount = experienceCount;

        for (Player viewer : getViewers()) this.addViewer(viewer);
    }

    private Player getClosestPlayer(Entity entity, float maxDistance) {

        if (entity.getInstance() == null) return null;

        Player closestPlayer = null;

        for (Player player : entity.getInstance().getPlayers()) {
            if (closestPlayer == null) {
                closestPlayer = player;
                continue;
            }

            if (player.getDistance(entity) < closestPlayer.getDistance(entity))
                closestPlayer = player;
        }

        if (closestPlayer == null) return null;
        if (closestPlayer.getDistance(entity) > maxDistance) return null;
        return closestPlayer;
    }
}
