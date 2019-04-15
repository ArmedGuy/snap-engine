package org.esa.snap.core.mpi;

import mpi.*;
import javax.media.jai.*;
import java.awt.*;
import java.awt.image.Raster;

public class MPITileScheduler implements TileScheduler {

    // Local scheduler to schedule MPI broadcasted tasks on
    private TileScheduler scheduler;
    private int rank;
    private int worldSize;

    private boolean isMaster = false;

    public MPITileScheduler(TileScheduler scheduler) {
        this.scheduler = scheduler;

        this.rank = MPI.COMM_WORLD.Rank();
        this.worldSize = MPI.COMM_WORLD.Size();
        this.isMaster = this.rank == 0;

    }
    @Override
    public Raster scheduleTile(OpImage opImage, int i, int i1) {
        return this.scheduler.scheduleTile(opImage, i, i1);
    }

    @Override
    public Raster[] scheduleTiles(OpImage opImage, Point[] points) {
        return this.scheduler.scheduleTiles(opImage, points);
    }

    @Override
    public TileRequest scheduleTiles(PlanarImage planarImage, Point[] points, TileComputationListener[] tileComputationListeners) {
        return this.scheduler.scheduleTiles(planarImage, points, tileComputationListeners);
    }

    @Override
    public void cancelTiles(TileRequest tileRequest, Point[] points) {
        this.scheduler.cancelTiles(tileRequest, points);
    }

    @Override
    public void prefetchTiles(PlanarImage planarImage, Point[] points) {

    }

    @Override
    public void setParallelism(int i) {

    }

    @Override
    public int getParallelism() {
        return 0;
    }

    @Override
    public void setPrefetchParallelism(int i) {

    }

    @Override
    public int getPrefetchParallelism() {
        return 0;
    }

    @Override
    public void setPriority(int i) {

    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void setPrefetchPriority(int i) {

    }

    @Override
    public int getPrefetchPriority() {
        return 0;
    }
}
