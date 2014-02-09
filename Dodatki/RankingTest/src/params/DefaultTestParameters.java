package params;

public class DefaultTestParameters
    extends Parameters
{

    public DefaultTestParameters()
    {
        defaultUsefulnessWeight = 1;
        reviewRateWeight = 8;
        authorRateWeight = 3;
        constValue = 15;

        liarsPercentage = 0;
        liarsCount = ( new Double( userCount * liarsPercentage / 100.0 ) ).intValue();

        userCount = 100;
        reviewsCount = 120;
    }
}
