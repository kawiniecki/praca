package params;

public class ProgressiveTestParameters extends Parameters
{
    public ProgressiveTestParameters()
    {
        defaultUsefulnessWeight = 1;
        reviewRateWeight = 8;
        authorRateWeight = 3;
        constValue = 15;

        reviewsCount = 100;
    }
}
