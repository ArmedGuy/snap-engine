package org.esa.snap.core.mpi;

import mpi.MPI;
import mpi.MPIException;

import javax.media.jai.JAI;
import java.awt.*;

public class MPIJAI {
    public static final int TAG_BASE = 0;
    public static final int TAG_FILECACHE = 1;
    public static final int TAG_TILESCHEDULER = 2;
    public static String[] Init(String[] args) throws Exception {
        System.out.println("MPI JAI Tooling initializing...");
        String[] real_args = MPI.Init(args);
        MPITileScheduler scheduler = new MPITileScheduler(JAI.getDefaultInstance().getTileScheduler());
        JAI.getDefaultInstance().setTileScheduler(scheduler);
        return real_args;
    }
    public static void Finalize() throws MPIException {
        MPI.Finalize();
    }

    public static boolean IsMaster() throws MPIException {
        return Rank() == 0;
    }
    public static int Rank() throws MPIException {
        return MPI.COMM_WORLD.getRank();
    }
    public static int WorldSize() throws MPIException {
        return MPI.COMM_WORLD.getSize();
    }

    public static int TileToRank(int tileX, int tileY) throws MPIException {
        return tileX % WorldSize();
    }

    public static int TileToRank(Point p) throws MPIException {
        return TileToRank(p.x, p.y);
    }

    public static boolean IsLocalTile(Point p) throws MPIException {
        return Rank() == TileToRank(p);
    }

    public static boolean IsLocalTile(int tileX, int tileY) throws MPIException {
        return Rank() == TileToRank(tileX, tileY);
    }

    public static boolean UseMPI() {
        return true;
    }

    public static void WaitForExit() throws InterruptedException {
        while(true) {
            Thread.sleep(100);
        }
    }
}
