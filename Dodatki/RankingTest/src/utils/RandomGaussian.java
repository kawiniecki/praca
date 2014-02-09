package utils;

import java.util.Random;

public class RandomGaussian
{

    private static Random fRandom = new Random();

    public static double getGaussian( double aMean, double aVariance )
    {

        return aMean + fRandom.nextGaussian() * aVariance;
    }
}
