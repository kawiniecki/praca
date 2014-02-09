package params;

public class LiarsTestParameters
    extends Parameters
{

    public LiarsTestParameters()
    {
        defaultUsefulnessWeight = 1;
        reviewRateWeight = 8;
        authorRateWeight = 3;
        constValue = 15;

        userCount = 100;
        reviewsCount = 120;
    }
}
