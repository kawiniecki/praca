package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import params.LiarsTestParameters;
import params.Parameters;
import rankTest.MainController;
import rankTest.Review;
import utils.RoundingHelper;

public class LiarsTest
{
    // liars percentage
    private static Integer LIARS_FROM = 0;

    private static Integer LIARS_TO = 50;

    private static Integer LIARS_STEP = 10;

    // users count
    private static Integer USERS_FROM = 30;

    private static Integer USERS_TO = 300;

    private static Integer USERS_STEP = 30;

    private static void runTest( Parameters parameters, Integer forcedUserCount )
    {
        MainController mainController = new MainController( parameters );
        mainController.generateUsers( null, forcedUserCount );
        mainController.generateReviews();

        mainController.simulateUsersVoting();
        mainController.recalculateSystemAverage();
        mainController.recalculateReviewsBayesianAverage();

        double sum;
        double votesAverageSum;
        Map<Integer, Double> results = new TreeMap<Integer, Double>();
        Map<Integer, Double> votesPerReview = new HashMap<>();

        for ( int liarsPercentage = LIARS_FROM; liarsPercentage <= LIARS_TO; liarsPercentage += LIARS_STEP )
        {
            sum = 0;
            votesAverageSum = 0;
            mainController.copyReviews();

            Integer percentage = new Integer( liarsPercentage );
            Integer customLiarCount = new Double( forcedUserCount * new Double( percentage ) / 100.0 ).intValue();

            mainController.generateUsers( customLiarCount, forcedUserCount );

            mainController.clearReviewVotes();

            mainController.simulateUsersVoting();

            mainController.recalculateSystemAverage();
            mainController.recalculateReviewsBayesianAverage();

            for ( Review r : mainController.getReviews().values() )
            {
                sum += Math.pow( ( r.getBayesAverage() - r.getLikeProbability() ), 2 );
                votesAverageSum += ( r.getLikeCount() + r.getUnlikeCount() );
            }

            results.put( liarsPercentage, Math.sqrt( sum / new Double( parameters.getReviewsCount() ) ) );
            votesPerReview.put( liarsPercentage, votesAverageSum / new Double( mainController.getReviews().values().size() ) );
        }

        for ( Integer i : results.keySet() )
        {
            String separator = mainController.getParameters().getSeparator();
            StringBuilder row = new StringBuilder();
            row.append( i );
            row.append( separator );
            row.append( RoundingHelper.round( results.get( i ), 5 ) );
            row.append( separator );
            row.append( RoundingHelper.round( votesPerReview.get( i ), 5 ) );

            System.out.println( row.toString() );
        }

    }

    public static void main( String[] args )
    {

        List<Parameters> parametersList = new ArrayList<Parameters>();
        parametersList.add( new LiarsTestParameters() );

        for ( Parameters p : parametersList )
        {
            System.out.println();
            System.out.println( p.getWeightsDescription() );
            System.out.println( "\nliars%" + p.getSeparator() + "RMSE" + p.getSeparator() + " votes/review" );

            for ( int usersCount = USERS_FROM; usersCount <= USERS_TO; usersCount += USERS_STEP )
            {
                System.out.println();
                System.out.println( "users:" + p.getSeparator() + usersCount );
                runTest( p, usersCount );
            }
        }
    }
}
