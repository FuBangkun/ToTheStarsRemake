package com.FuBangkun.tothestarsremake.haveasoltime;

public class HSTHelper
{
    /**
     * Real Gravity    GC Gravity
     * 1.00            0.0
     * 0.00            0.08
     * @param gravity in multiples of earth gravity
     * @return the gravity Galacticraft uses
     */
    public static double realGravityToGCGravity(double gravity) {
        return map(gravity, 1.0, 0.0, 0.0, 0.08);
    }



    /**
     * Real Density     Forge Density
     * 1.225            0
     * 997              1000
     * @param density the density in kg/m3
     * @return the density forge uses
     */
    public static double realDensityToForgeDensity(double density) {
        return map(density, 1.225, 997.0, 0, 1000);
    }

    /**
     * Real Viscosity     Forge Viscosity
     * 0                  0
     * 8.90e−4            1000
     * @param viscosity the viscosity in Pa-s
     * @return the viscosity forge uses
     */
    public static double realViscosityToForgeViscosity(double viscosity) {
        return map(viscosity, 0, 8.90e-4, 0, 1000);
    }

    /**
     * Real Temperature - 273.15         Minecraft Temperature
     * 37.7778                           2.0
     * 20.46296294444                    0.8
     * @param temperature the temperature in K
     * @return the temperature Minecraft biomes use
     */
    public static double realTemperatureToMinecraftTemperature(double temperature) {
        return map(temperature, 37.7778 + 273.15, 20.462963 + 273.15, 2.0, 0.8);
    }

    private final static double EPSILON = 1e-12;

    public static double map(double valueCoord1,
                             double startCoord1, double endCoord1,
                             double startCoord2, double endCoord2) {

        if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
            throw new ArithmeticException("/ 0");
        }

        double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
        return ratio * (valueCoord1 - startCoord1) + startCoord2;
    }
}
