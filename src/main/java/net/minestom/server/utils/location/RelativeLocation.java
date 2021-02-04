package net.minestom.server.utils.location;

import net.minestom.server.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a location which can have fields relative to an {@link Entity} position.
 *
 * @param <T> the location type
 */
public abstract class RelativeLocation<T> {

    protected final T location;
    protected final boolean relativeX;
    protected final boolean relativeY;
    protected final boolean relativeZ;

    public RelativeLocation(@NotNull T location, boolean relativeX, boolean relativeY, boolean relativeZ) {
        this.location = location;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeZ = relativeZ;
    }

    /**
     * Gets the location based on the relative fields and {@code entity}.
     *
     * @param entity the entity to get the relative position from
     * @return the location
     */
    public abstract T fromRelativePosition(@Nullable Entity entity);

    /**
     * Gets if the 'x' field is relative.
     *
     * @return true if the 'x' field is relative
     */
    public boolean isRelativeX() {
        return relativeX;
    }

    /**
     * Gets if the 'y' field is relative.
     *
     * @return true if the 'y' field is relative
     */
    public boolean isRelativeY() {
        return relativeY;
    }

    /**
     * Gets if the 'z' field is relative.
     *
     * @return true if the 'z' field is relative
     */
    public boolean isRelativeZ() {
        return relativeZ;
    }
}
