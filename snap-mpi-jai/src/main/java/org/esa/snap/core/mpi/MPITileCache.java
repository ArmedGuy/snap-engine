package org.esa.snap.core.mpi;

import mpi.MPIException;

import javax.media.jai.TileCache;
import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.util.Comparator;

public class MPITileCache implements TileCache {

    private TileCache tileCache;

    public MPITileCache(TileCache tileCache) {
        this.tileCache = tileCache;
    }
    @Override
    public void add(RenderedImage renderedImage, int tileX, int tileY, Raster raster) {
        add(renderedImage, tileX, tileY, raster, null);

    }

    @Override
    public void add(RenderedImage renderedImage, int tileX, int tileY, Raster raster, Object tileCacheMetric) {
        try {
            if (MPIJAI.IsLocalTile(tileX, tileY)) {
                this.tileCache.add(renderedImage, tileX, tileY, raster, tileCacheMetric);
            }
            else {
                // TODO: MPI call
            }
        } catch(MPIException e) {

        }
    }

    @Override
    public void remove(RenderedImage renderedImage, int tileX, int tileY) {
        try {
            if (MPIJAI.IsLocalTile(tileX, tileY)) {
                this.tileCache.remove(renderedImage, tileX, tileY);
            }
            else {
                // TODO: MPI call
            }
        } catch(MPIException e) {

        }
    }

    @Override
    public Raster getTile(RenderedImage renderedImage, int tileX, int tileY) {
        try {
            if (MPIJAI.IsLocalTile(tileX, tileY)) {
                return this.tileCache.getTile(renderedImage, tileX, tileY);
            }
            else {
                // TODO: MPI call
                return null;
            }
        } catch(MPIException e) {
            return null;
        }
    }

    @Override
    public Raster[] getTiles(RenderedImage renderedImage) {
        return new Raster[0];
    }

    @Override
    public void removeTiles(RenderedImage renderedImage) {

    }

    @Override
    public void addTiles(RenderedImage renderedImage, Point[] points, Raster[] rasters, Object o) {

    }

    @Override
    public Raster[] getTiles(RenderedImage renderedImage, Point[] points) {
        return new Raster[0];
    }

    @Override
    public void flush() {

    }

    @Override
    public void memoryControl() {

    }

    @Override
    public void setTileCapacity(int i) {

    }

    @Override
    public int getTileCapacity() {
        return 0;
    }

    @Override
    public void setMemoryCapacity(long l) {

    }

    @Override
    public long getMemoryCapacity() {
        return 0;
    }

    @Override
    public void setMemoryThreshold(float v) {

    }

    @Override
    public float getMemoryThreshold() {
        return 0;
    }

    @Override
    public void setTileComparator(Comparator comparator) {

    }

    @Override
    public Comparator getTileComparator() {
        return null;
    }
}
