package org.esa.snap.core.mpi;

import mpi.MPI;

import javax.media.jai.JAI;

public class MPIJAI {
    public static void Init(String[] args) {
        MPI.Init(args);
        MPITileScheduler scheduler = new MPITileScheduler(JAI.getDefaultInstance().getTileScheduler());
        JAI.getDefaultInstance().setTileScheduler(scheduler);
    }
    public static void Finalize() {
        MPI.Finalize();
    }

    public static boolean UseMPI() {
        return true;
    }
}
