package rankTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import params.Parameters;
import utils.RandomGaussian;
import utils.ReviewCollectionCloner;
import utils.ReviewComparator;

public class MainController
{
    List<User> users = new ArrayList<User>();

    List<User> reviewWriters = new ArrayList<User>();

    HashMap<Integer, Review> prevIterationReviews = new HashMap<Integer, Review>();

    HashMap<Integer, Review> reviews = new HashMap<Integer, Review>();

    double systemAverageUsefulness;

    Parameters parameters;

    public MainController( Parameters params )
    {
        this.parameters = params;
    }

    public int randInt( int min, int max )
    {

        Random rand = new Random();

        int randomNum = rand.nextInt( ( max - min ) + 1 ) + min;

        return randomNum;
    }

    public void generateUsers( Integer customLiarCount, Integer forceCount )
    {
        this.users.clear();

        Integer liarCount = customLiarCount;
        if ( liarCount == null )
        {
            liarCount = parameters.getLiarsCount();
        }

        int count = parameters.getUserCount();
        if ( forceCount != null )
        {
            count = forceCount;
        }

        int greatCount = parameters.getGreatUserCount();
        int poorCount = parameters.getPoorUersCount();

        for ( int i = 0; i < count; i++ )
        {
            User u = new User();

            if ( i < greatCount )
            {
                // great user
                int ratingCount = randInt( parameters.getAverageRatingsPerUser(), parameters.getMaxRatingsPerUser() );
                u.setReviewToMark( ratingCount );
            }
            else if ( i >= greatCount && i < greatCount + poorCount )
            {
                // poor user
                int ratingCount = randInt( 1, parameters.getAverageRatingsPerUser() );
                u.setReviewToMark( ratingCount );
            }
            else
            {
                // common user
                u.setReviewToMark( parameters.getAverageRatingsPerUser() );
            }

            this.users.add( u );
        }

        Collections.shuffle( users );
        for ( int i = 0; i < users.size(); i++ )
        {
            if ( i < liarCount )
            {
                users.get( i ).setLiar( true );
            }
            else
            {
                users.get( i ).setLiar( false );
            }
        }

        Collections.shuffle( users );
        // generate authors
        for ( int i = 0; i < parameters.getReviewWritersCount(); i++ )
        {
            double p = RandomGaussian.getGaussian( parameters.getMEAN(), parameters.getVARIANCE() );
            if ( p > 100 )
            {
                p = 100;
            }
            else if ( p < 0 )
            {
                p = 0;
            }

            p = p / 100.0;

            users.get( i ).setDefaultLikeProb( p );
            reviewWriters.add( users.get( i ) );
        }

    }

    public void generateReviews()
    {
        for ( int i = 0; i < parameters.getReviewsCount(); i++ )
        {
            int userId = new Random().nextInt( reviewWriters.size() );
            User u = reviewWriters.get( userId );

            Review r = new Review( i, u.generateReviewLikeProbability(), this );

            r.setAuthor( reviewWriters.get( userId ) );
            reviewWriters.get( userId ).addOwnReview( r );

            reviews.put( r.getId(), r );
        }
    }

    public void recalculateSystemAverage()
    {
        Double sum = 0.0;
        for ( Review r : reviews.values() )
        {
            sum += r.getCommonUsefulness();
        }

        systemAverageUsefulness = sum / parameters.getReviewsCount();
    }

    public void recalculateReviewsBayesianAverage()
    {
        for ( Review r : reviews.values() )
        {
            r.computeBayesianAverage( systemAverageUsefulness );
        }
    }

    public void copyReviews()
    {
        prevIterationReviews = ReviewCollectionCloner.copy( reviews );
    }

    public void simulateUsersVoting()
    {
        for ( User u : users )
        {
            for ( Review r : getRandomReviews( u ) )
            {
                u.vote( r );
            }
        }
    }

    public List<User> getUsers()
    {
        return users;
    }

    private Set<Review> getRandomReviews( User u )
    {
        Set<Review> result = new HashSet<Review>();

        ArrayList<Integer> ids = new ArrayList<Integer>( parameters.getReviewsCount() );

        ArrayList<Review> reviewList = new ArrayList<Review>( parameters.getReviewsCount() );
        reviewList.addAll( reviews.values() );

        for ( int i = 0; i < parameters.getReviewsCount(); i++ )
        {
            ids.add( i );
        }
        Collections.shuffle( ids );

        for ( int i = 0; i < u.getReviewToMark(); i++ )
        {
            Integer id = ids.get( i );
            result.add( reviewList.get( id ) );
        }
        return result;
    }

    public void setUsers( List<User> users )
    {
        this.users = users;
    }

    public void setReviews( HashMap<Integer, Review> reviews )
    {
        this.reviews = reviews;
    }

    public HashMap<Integer, Review> getReviews()
    {
        return reviews;
    }

    public List<Review> getSortedReviews()
    {
        List<Review> list = new ArrayList<>( reviews.values() );
        Collections.sort( list, new ReviewComparator() );
        return list;
    }

    public void clearReviewVotes()
    {
        for ( Review r : reviews.values() )
        {
            r.setLikeCount( 0 );
            r.setUnlikeCount( 0 );
        }
    }

    public double getSystemAverageUsefulness()
    {
        return systemAverageUsefulness;
    }

    public void setSystemAverageUsefulness( double systemAverageUsefulness )
    {
        this.systemAverageUsefulness = systemAverageUsefulness;
    }

    public HashMap<Integer, Review> getPrevIterationReviews()
    {
        return prevIterationReviews;
    }

    public List<User> getReviewWriters()
    {
        return reviewWriters;
    }

    public void setReviewWriters( List<User> reviewWriters )
    {
        this.reviewWriters = reviewWriters;
    }

    public Parameters getParameters()
    {
        return parameters;
    }

    public void setParameters( Parameters parameters )
    {
        this.parameters = parameters;
    }

    public Integer getLiarsCount()
    {
        int count = 0;
        for ( User u : getUsers() )
        {
            if ( u.isLiar() )
            {
                count++;
            }
        }
        return count;
    }

}
