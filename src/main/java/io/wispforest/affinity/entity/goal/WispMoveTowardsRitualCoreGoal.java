package io.wispforest.affinity.entity.goal;

import io.wispforest.affinity.entity.WispEntity;
import io.wispforest.affinity.misc.util.BlockFinder;
import io.wispforest.affinity.object.AffinityPoiTypes;
import io.wispforest.owo.util.VectorRandomUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.poi.PointOfInterest;

import java.util.Comparator;
import java.util.EnumSet;

public class WispMoveTowardsRitualCoreGoal extends Goal {

    private final WispEntity wisp;
    private BlockPos closestCore;

    public WispMoveTowardsRitualCoreGoal(WispEntity wisp) {
        this.wisp = wisp;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        this.closestCore = BlockFinder.findPoi(this.wisp.world, AffinityPoiTypes.RITUAL_CORE, this.wisp.getBlockPos(), 10)
                .sorted(Comparator.comparing(poi -> poi.getPos().getSquaredDistanceFromCenter(this.wisp.getX(), this.wisp.getY(), this.wisp.getZ())))
                .map(PointOfInterest::getPos)
                .findFirst()
                .orElse(null);

        return this.closestCore != null && this.closestCore.getSquaredDistanceFromCenter(this.wisp.getX(), this.wisp.getY(), this.wisp.getZ()) >= (6 * 6);
    }

    @Override
    public void start() {
        var target = VectorRandomUtils.getRandomOffsetSpecific(this.wisp.world, Vec3d.ofCenter(this.closestCore.up(3)), 8, 5, 8);
        this.wisp.getNavigation().startMovingTo(target.x, target.y, target.z, .75);
    }

    @Override
    public boolean shouldContinue() {
        return !this.wisp.getNavigation().isIdle();
    }
}