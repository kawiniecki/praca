package params;

public class Parameters
{
    protected Integer constValue = 15;

    protected String separator = "\t";

    // bayes average
    protected int defaultUsefulnessWeight = 1;

    protected int reviewRateWeight = 1;

    protected int authorRateWeight = 1;

    // users
    protected int userCount = 80;

    protected int reviewWritersPercentage = 10;

    protected int reviewWritersCount = ( new Double( userCount * reviewWritersPercentage / 100.0 ) ).intValue();

    protected int liarsPercentage = 0;

    protected int liarsCount = ( new Double( userCount * liarsPercentage / 100.0 ) ).intValue();

    protected int greatUsersPercent = 25;

    protected int greatUserCount = ( new Double( userCount * greatUsersPercent / 100.0 ) ).intValue();

    protected int averageRatingsPerUser = 25;

    protected int poorUersCount = userCount - greatUserCount;

    protected int maxRatingsPerUser = 50;

    // reviews
    protected int reviewsCount = 100;

    // user
    protected double defaultUserRank = 0.5;

    protected double defaultUerRankWeight = 1;

    protected double userRatingsWeight = 1;

    protected double userConstValue = 15;

    // RandomGaussian
    protected double MEAN = 75;

    protected double VARIANCE = 10;

    public Integer getConstValue()
    {
        return constValue;
    }

    public String getSeparator()
    {
        return separator;
    }

    public int getDefaultUsefulnessWeight()
    {
        return defaultUsefulnessWeight;
    }

    public int getReviewRateWeight()
    {
        return reviewRateWeight;
    }

    public int getAuthorRateWeight()
    {
        return authorRateWeight;
    }

    public int getUserCount()
    {
        return userCount;
    }

    public int getReviewWritersPercentage()
    {
        return reviewWritersPercentage;
    }

    public int getReviewWritersCount()
    {
        return reviewWritersCount;
    }

    public int getLiarsPercentage()
    {
        return liarsPercentage;
    }

    public int getLiarsCount()
    {
        return liarsCount;
    }

    public int getGreatUsersPercent()
    {
        return greatUsersPercent;
    }

    public int getGreatUserCount()
    {
        return greatUserCount;
    }

    public int getAverageRatingsPerUser()
    {
        return averageRatingsPerUser;
    }

    public int getPoorUersCount()
    {
        return poorUersCount;
    }

    public int getMaxRatingsPerUser()
    {
        return maxRatingsPerUser;
    }

    public int getReviewsCount()
    {
        return reviewsCount;
    }

    public double getDefaultUserRank()
    {
        return defaultUserRank;
    }

    public double getDefaultUerRankWeight()
    {
        return defaultUerRankWeight;
    }

    public double getUserRatingsWeight()
    {
        return userRatingsWeight;
    }

    public double getUserConstValue()
    {
        return userConstValue;
    }

    public double getMEAN()
    {
        return MEAN;
    }

    public double getVARIANCE()
    {
        return VARIANCE;
    }

    protected Parameters()
    {
        super();
    }

    public String getWeightsDescription()
    {
        StringBuilder b = new StringBuilder();
        b.append( "defaultUsefulnessWeight: " );
        b.append( defaultUsefulnessWeight );
        b.append( "\nreviewRateWeight: " );
        b.append( reviewRateWeight );
        b.append( "\nauthorRateWeight: " );
        b.append( authorRateWeight );
        b.append( "\nconst: " );
        b.append( constValue );
        b.append( "\n" );

        return b.toString();
    }
}