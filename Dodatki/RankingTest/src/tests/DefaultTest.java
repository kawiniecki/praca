package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import params.DefaultTestParameters;
import params.Parameters;
import rankTest.MainController;
import rankTest.Review;
import rankTest.User;
import utils.RoundingHelper;

public class DefaultTest
{
    public static void printAuthorsInfo( MainController mainController )
    {
        System.out.println( "ReviewAuthors" );

        String separator = mainController.getParameters().getSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append( "id" );
        sb.append( separator );
        sb.append( "likes" );
        sb.append( separator );
        sb.append( "total" );
        sb.append( separator );
        sb.append( "rank" );
        sb.append( separator );
        sb.append( "expectedRank" );
        sb.append( separator );
        sb.append( "reviews" );

        System.out.println( sb.toString() );
        for ( int i = 0; i < mainController.getReviewWriters().size(); i++ )
        {
            User u = mainController.getReviewWriters().get( i );

            double expectedLikeProb = RoundingHelper.round( u.getDefaultLikeProb(), 2 ) * 100;
            int likes = u.getLikeCountFromOwnReviews();
            int allVotes = u.getVotesCountFromOwnReviews();
            double rank = RoundingHelper.round( u.getRank( mainController ), 2 );
            StringBuilder reviews = new StringBuilder();
            for ( Review r : mainController.getReviewWriters().get( i ).getOwnReviews() )
            {
                reviews.append( r.getId() + "," );
            }
            StringBuilder toPrint = new StringBuilder();
            toPrint.append( String.valueOf( i ) );
            toPrint.append( separator );
            toPrint.append( likes );
            toPrint.append( separator );
            toPrint.append( allVotes );
            toPrint.append( separator );
            toPrint.append( rank );
            toPrint.append( separator );
            toPrint.append( expectedLikeProb );
            toPrint.append( separator );
            toPrint.append( reviews.toString() );

            System.out.println( toPrint );
        }
    }

    public static void printReviews( MainController mainController, boolean printPrevIterationReviews )
    {
        double sum = 0;
        double totalDiff = 0;
        Map<Integer, Double> diffMap = new TreeMap<Integer, Double>();
        String separator = mainController.getParameters().getSeparator();

        for ( Review r : mainController.getReviews().values() )
        {
            double d = Math.pow( ( r.getBayesAverage() - r.getLikeProbability() ), 2 );
            sum += d;
            diffMap.put( r.getId(), d );
        }

        totalDiff = Math.sqrt( sum / new Double( diffMap.size() ) ); // size of diffMap = reviewCount
        totalDiff = RoundingHelper.round( totalDiff, 2 );

        for ( Review r : mainController.getSortedReviews() )
        {
            double roundedDiff = RoundingHelper.round( diffMap.get( r.getId() ), 7 );

            System.out.println( r.toString() + separator + roundedDiff );

            String reviewEntry = r.toString();

            if ( printPrevIterationReviews )
            {
                Double prevBayes = mainController.getPrevIterationReviews().get( r.getId() ).getBayesAverage();
                Double diff = Math.abs( prevBayes - r.getBayesAverage() );
                reviewEntry = reviewEntry.concat( separator ).concat( RoundingHelper.round( prevBayes, 2 ).toString() );
                reviewEntry = reviewEntry.concat( separator ).concat( RoundingHelper.round( diff, 2 ).toString() );
            }
        }

        String[] splited = mainController.getSortedReviews().get( 0 ).toString().split( separator );
        for ( String s : splited )
        {
            System.out.print( separator );
        }

        System.out.println( totalDiff );

        for ( int i = 0; i < 3; i++ )
        {
            System.out.println( "" );
        }
    }

    public static void printHeader( MainController mainController, boolean printPrevIterationReviews )
    {
        // header
        String separator = mainController.getParameters().getSeparator();
        StringBuilder header = new StringBuilder();
        header.append( "id" );
        header.append( separator );
        header.append( "expected" );
        header.append( separator );
        header.append( "votes" );
        header.append( separator );
        header.append( "positive" );
        header.append( separator );
        header.append( "positive/votes" );
        header.append( separator );
        header.append( "Usefulness" );
        header.append( separator );
        header.append( "error" );

        if ( printPrevIterationReviews )
        {
            header.append( separator );
            header.append( "prevVal" );
            header.append( separator );
            header.append( "diff" );
        }
        ;
        System.out.println( "sysAverage:" + separator + RoundingHelper.round( mainController.getSystemAverageUsefulness(), 2 ) );

        System.out.println( header );

    }

    public static void main( String[] args )
    {

        List<Parameters> paramList = new ArrayList<>();
        paramList.add( new DefaultTestParameters() );

        for ( Parameters ap : paramList )
        {
            System.out.println( ap.getWeightsDescription() );

            MainController mainController = new MainController( ap );

            mainController.generateUsers( null, null );
            mainController.generateReviews();

            mainController.simulateUsersVoting();
            mainController.recalculateSystemAverage();
            mainController.recalculateReviewsBayesianAverage();

            printHeader( mainController, false );
            printReviews( mainController, false );
            printAuthorsInfo( mainController );
        }
    }
}
