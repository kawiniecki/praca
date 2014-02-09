package utils;

import java.util.Comparator;

import rankTest.Review;

public class ReviewComparator
    implements Comparator<Review>
{

    @Override
    public int compare( Review arg0, Review arg1 )
    {
        if ( arg1 == null )
            return -1;
        if ( arg0.getBayesAverage() > arg1.getBayesAverage() )
            return 1;
        else if ( arg0.getBayesAverage() < arg1.getBayesAverage() )
            return -1;
        else
            return 0;
    }

}
