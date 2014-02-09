package utils;

import java.util.HashMap;

import rankTest.Review;

public class ReviewCollectionCloner
{
    public static HashMap<Integer, Review> copy( HashMap<Integer, Review> from )
    {
        HashMap<Integer, Review> coll = new HashMap<Integer, Review>( from.size() );
        for ( Review r : from.values() )
        {
            Review copied = new Review( r.getId(), r.getLikeProbability(), r.getMainController() );
            copied.setBayesAverage( r.getBayesAverage() );
            copied.setLikeCount( r.getLikeCount() );
            copied.setUnlikeCount( r.getUnlikeCount() );
            coll.put( copied.getId(), copied );
        }

        return coll;
    }
}
