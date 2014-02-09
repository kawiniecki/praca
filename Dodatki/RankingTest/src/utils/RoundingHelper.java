package utils;

public class RoundingHelper
{
    public static Double round( double value, int decimalNumbers )
    {
        if ( decimalNumbers <= 0 )
        {
            Integer i = Double.valueOf( value ).intValue();
            Double.valueOf( (double) i );
        }

        double d = 10;
        d = Math.pow( d, decimalNumbers );

        return Double.valueOf( Math.round( value * d ) / d );
    }
}
