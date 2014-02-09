package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import params.Parameters;
import params.ProgressiveTestParameters;
import rankTest.MainController;
import rankTest.Review;
import utils.RoundingHelper;

public class ProgressiveTest
{
    private static Integer FROM = 30;

    private static Integer TO = 300;

    private static Integer STEP = 30;

    private static void runTest( Parameters parameters )
    {

        double sum;
        double votesAverageSum;
        Map<Integer, Double> results = new TreeMap<Integer, Double>();
        Map<Integer, Double> votesPerReview = new TreeMap<>();

        for ( int usersCount = FROM; usersCount <= TO; usersCount += STEP )
        {
            sum = 0;
            votesAverageSum = 0;
            MainController mainController = new MainController( parameters );

            mainController.generateUsers( null, usersCount );
            mainController.generateReviews();

            mainController.simulateUsersVoting();
            mainController.recalculateSystemAverage();
            mainController.recalculateReviewsBayesianAverage();

            for ( Review r : mainController.getReviews().values() )
            {
                sum += Math.pow( ( r.getBayesAverage() - r.getLikeProbability() ), 2 );
                votesAverageSum += ( r.getLikeCount() + r.getUnlikeCount() );
            }

            results.put( usersCount, Math.sqrt( sum / parameters.getReviewsCount() ) );
            votesPerReview.put( usersCount, votesAverageSum / new Double( mainController.getReviews().values().size() ) );
        }

        System.out.println( "users" + parameters.getSeparator() + "RMSE" + parameters.getSeparator() + "votes/review" );

        for ( Integer i : results.keySet() )
        {
            StringBuilder row = new StringBuilder();
            row.append( i );
            row.append( parameters.getSeparator() );
            row.append( RoundingHelper.round( results.get( i ), 5 ) );
            row.append( parameters.getSeparator() );
            row.append( RoundingHelper.round( votesPerReview.get( i ), 5 ) );

            System.out.println( row.toString() );
        }
    }

    public static void main( String[] args )
    {
        List<Parameters> parametersList = new ArrayList<Parameters>();
        parametersList.add( new ProgressiveTestParameters() );

        for ( Parameters p : parametersList )
        {
            System.out.println( "\n" + p.getWeightsDescription() );
            runTest( p );
        }
    }

}
