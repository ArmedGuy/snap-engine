package org.esa.beam.binning.support;

import org.esa.beam.binning.SpatialDataPeriod;

/**
 * @author Norman Fomferra
 */
public class SpatialDataPeriodImpl implements SpatialDataPeriod {
    private static final double SLOPE = -24.0 / 360.0;
    private static final double EPS = 1.0 / (60.0 * 60.0 * 1000); // 1 ms

    private final double startTime;
    private final int duration;
    private final double minDataHour;

    /**
     * @param startTime   Binning period's start time in MJD.
     * @param duration    The binning period's duration in days.
     * @param minDataHour The time in hours of a day (0 to 24) at which a given sensor has a minimum number of observations at the date line (the 180deg meridian).
     *                    This number is usually found plotting longitude-time pairs of given sensor observations and then finding
     *                    the area where there are a minimum number of observations at the date line.
     */
    public SpatialDataPeriodImpl(double startTime, int duration, double minDataHour) {
        this.startTime = startTime;
        this.duration = duration;
        this.minDataHour = minDataHour;
    }

    @Override
    public double getStartTime() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getObservationMembership(double lon, double time) {

        final double h = 24.0 * (time - startTime);
        final double h0 = minDataHour + (lon + 180.0) * SLOPE;

        if (h - EPS < h0) {
            // pixel is attached to data-period (p-1)
            return -1;
        } else if (h + EPS > h0 + 24.0 * duration) {
            // pixel is attached to data-period (p+1)
            return +1;
        } else {
            // pixel is attached to data-period (p)
            return 0;
        }
    }
}
