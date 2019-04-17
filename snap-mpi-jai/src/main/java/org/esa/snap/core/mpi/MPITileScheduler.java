package org.esa.snap.core.mpi;

import mpi.*;
import javax.media.jai.*;
import java.awt.*;
import java.awt.image.Raster;

public class MPITileScheduler implements TileScheduler {

    // Local scheduler to schedule MPI broadcasted tasks on
    private TileScheduler scheduler;
    private int distributedParallelism = 0;

    public MPITileScheduler(TileScheduler scheduler) throws Exception {
        this.scheduler = scheduler;

        int[] p = new int[1];
        if(MPIJAI.IsMaster()) {
            int parallellism = this.scheduler.getParallelism();

            for(int i = 1; i < MPIJAI.WorldSize(); i++) {
                MPI.COMM_WORLD.recv(p, 1, MPI.INT, i, MPIJAI.TAG_TILESCHEDULER);
                if(parallellism != p[0]) {
                    System.err.println(
                            String.format("MPI error: parallelism not homogeneous on all nodes, offending node %d has %d, expected %d",
                                    i, p[0], parallellism));
                    throw new Exception("MPI error: parallelism not homogeneous on all nodes");
                }
            }
        } else {
            p[0] = this.scheduler.getParallelism();
            MPI.COMM_WORLD.send(p, 1, MPI.INT, 0, MPIJAI.TAG_TILESCHEDULER);
        }

        distributedParallelism = MPIJAI.WorldSize() * this.scheduler.getParallelism();

        System.out.println(String.format("MPI established, rank: %d, world size: %d", MPIJAI.Rank(), MPIJAI.WorldSize()));

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
        this.scheduler.prefetchTiles(planarImage, points);
    }

    @Override
    public void setParallelism(int i) {
        this.scheduler.setParallelism(i);
    }

    @Override
    public int getParallelism() {
        return this.scheduler.getParallelism();
    }

    @Override
    public void setPrefetchParallelism(int i) {
        this.scheduler.setPrefetchParallelism(i);
    }

    @Override
    public int getPrefetchParallelism() {
        return this.scheduler.getPrefetchParallelism();
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
