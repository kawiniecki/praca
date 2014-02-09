package rankTest;

import params.Parameters;
import utils.RoundingHelper;

public class Review
{
    private int id;

    private int likeCount;

    private int unlikeCount;

    private double bayesAverage;

    private User author;

    private double likeProbability;

    private Parameters parameters;

    private MainController mainController;

    public Review( int id, double likeProbability, MainController mainController )
    {
        this.id = id;
        this.likeProbability = likeProbability;
        this.parameters = mainController.getParameters();
        this.mainController = mainController;
    }

    public double computeBayesianAverage( double systemAverageUsefulness )
    {
        double computed = 0;

        Integer constValue = parameters.getConstValue();

        double upperPartA = parameters.getDefaultUsefulnessWeight() * systemAverageUsefulness * constValue;
        double upperPartB = parameters.getReviewRateWeight() * likeCount;
        double upperPartC = parameters.getAuthorRateWeight() * getAuthor().getLikeCountFromOwnReviews();
        double bottomPart =
            parameters.getDefaultUsefulnessWeight() * constValue + parameters.getReviewRateWeight() * ( likeCount + unlikeCount )
                + parameters.getAuthorRateWeight() * getAuthor().getVotesCountFromOwnReviews();

        try
        {
            computed = ( upperPartA + upperPartB + upperPartC ) / bottomPart;
        }
        catch ( Exception e )
        {
            System.out.println( e );
        }

        this.bayesAverage = computed;
        return computed;
    }

    @Override
    public String toString()
    {
        String separator = parameters.getSeparator();
        StringBuilder txt = new StringBuilder();
        txt.append( id );
        txt.append( separator );
        txt.append( RoundingHelper.round( likeProbability, 2 ) );
        txt.append( separator );
        txt.append( likeCount + unlikeCount );
        txt.append( separator );
        txt.append( likeCount );
        txt.append( separator );
        txt.append( getCommonUsefulness() );
        txt.append( separator );
        txt.append( RoundingHelper.round( bayesAverage, 2 ) );

        return txt.toString();
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount( int likeCount )
    {
        this.likeCount = likeCount;
    }

    public int getUnlikeCount()
    {
        return unlikeCount;
    }

    public void setUnlikeCount( int unlikeCount )
    {
        this.unlikeCount = unlikeCount;
    }

    public double getBayesAverage()
    {
        return bayesAverage;
    }

    public void setBayesAverage( double bayesAverage )
    {
        this.bayesAverage = bayesAverage;
    }

    public double getCommonUsefulness()
    {
        double result = 0;

        if ( ( likeCount + unlikeCount ) == 0 )
        {
            return 0;
            // return parameters.getdefaultUsefulness;
        }

        result = new Double( likeCount ) / new Double( likeCount + unlikeCount );
        return RoundingHelper.round( result, 2 );
    }

    public void addLike()
    {
        this.likeCount++;
    }

    public void addUnLike()
    {
        this.unlikeCount++;
    }

    public double getLikeProbability()
    {
        return likeProbability;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor( User author )
    {
        this.author = author;
    }

    public Parameters getParameters()
    {
        return parameters;
    }

    public void setParameters( Parameters parameters )
    {
        this.parameters = parameters;
    }

    public MainController getMainController()
    {
        return mainController;
    }

    public void setMainController( MainController mainController )
    {
        this.mainController = mainController;
    }
}
