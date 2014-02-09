package rankTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import params.Parameters;

public class User
{

    private int reviewToMark;

    private boolean liar;

    private double defaultLikeProb;

    private List<Review> ownReviews = new ArrayList<Review>();

    public int getReviewToMark()
    {
        return reviewToMark;
    }

    public void setReviewToMark( int reviewToMark )
    {
        this.reviewToMark = reviewToMark;
    }

    @Override
    public String toString()
    {
        return "User (" + reviewToMark + " to do)";
    }

    public void vote( Review r )
    {
        if ( new Random().nextDouble() >= r.getLikeProbability() )
        {
            if ( !this.isLiar() )
            {
                r.addUnLike();
            }
            else
            {
                r.addLike();
            }
        }
        else
        {
            if ( !this.isLiar() )
            {
                r.addLike();
            }
            else
            {
                r.addUnLike();
            }
        }
    }

    public boolean isLiar()
    {
        return liar;
    }

    public void setLiar( boolean liar )
    {
        this.liar = liar;
    }

    public List<Review> getOwnReviews()
    {
        return ownReviews;
    }

    public void setOwnReviews( List<Review> ownReviews )
    {
        this.ownReviews = ownReviews;
    }

    public void addOwnReview( Review r )
    {
        getOwnReviews().add( r );
    }

    public int getLikeCountFromOwnReviews()
    {
        int total = 0;
        for ( Review r : getOwnReviews() )
        {
            total += r.getLikeCount();
        }

        return total;
    }

    public int getUnlikeCountFromOwnReviews()
    {
        int total = 0;
        for ( Review r : getOwnReviews() )
        {
            total += r.getUnlikeCount();
        }

        return total;
    }

    public int getVotesCountFromOwnReviews()
    {
        int total = 0;
        for ( Review r : getOwnReviews() )
        {
            total += r.getLikeCount();
            total += r.getUnlikeCount();
        }

        return total;
    }

    public double generateReviewLikeProbability()
    {
        double result = 0;
        double randRange = defaultLikeProb * 0.1;
        double min = defaultLikeProb - randRange;
        double max = defaultLikeProb + randRange;

        result = min + ( max - min ) * new Random().nextDouble();

        if ( result < 0 )
        {
            result = 0;
        }
        if ( result > 1 )
        {
            result = 1;
        }

        return result;
    }

    public double getDefaultLikeProb()
    {
        return defaultLikeProb;
    }

    public void setDefaultLikeProb( double defaultLikeProb )
    {
        this.defaultLikeProb = defaultLikeProb;
    }

    public double getRank( MainController mc )
    {
        Parameters p = mc.getParameters();

        double numerator = p.getDefaultUserRank() * p.getDefaultUerRankWeight() * p.getUserConstValue();
        numerator += getLikeCountFromOwnReviews() * p.getUserRatingsWeight();

        double denominator = p.getDefaultUerRankWeight() * p.getUserConstValue();
        denominator += p.getUserRatingsWeight() * getVotesCountFromOwnReviews();

        double result = numerator * 100.0 / denominator;

        return result;
    }

}
