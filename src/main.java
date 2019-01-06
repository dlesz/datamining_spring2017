import Apriori.Apriori;
import Apriori.ItemSet;
import DataWareHouse.*;
import KMeanCluster.KMeanCluster;
import KMeanCluster.KMeans;
import kNN.KNN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by dlesz on 22/04/2017.
 *
 *  Project criteria:
 *  You must apply at least two different pre-processing methods,
 *  one sequential (find relevant patterns)/frequent pattern mining method (Apriori algo)
 *  one clustering method and one supervised learning method
 *
 */
class Main {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        //parsing and transforming all data needed for mining
        List<Student> parsedData = DataWareHouse.parseData();
        //System.out.println(parsedData);

        /**************************************************************************************************
         * Implementation of kNN:                                                                         *
         * Query: Is it possible to determine a students gender looking at age, study line and Phone OS?  *
         **************************************************************************************************/

        System.out.println("-----------------------------------K NEAREST NEIGHBOURS RESULTS-------------------------------------------\n");
        List<Double> kNNresults = new ArrayList<>();
        for (int i = 1; i < 21; i++){ // using 20 different neighbors.
            for (int j = 0; j < 300; j++){ // run the algorithm 100 times
                Collections.shuffle(parsedData); // first shuffle the data
                List<Student> trainingSet = new ArrayList<>();
                List<Student> testSet = new ArrayList<>();
                //using 2/3 of dataSet for trainingSet and 1/3 for testSet
                for (int k = 0; k < parsedData.size(); k++) {
                    if (k < parsedData.size() * 2 / 3) trainingSet.add(parsedData.get(k));
                    else testSet.add(parsedData.get(k));
                }
                KNN myKNN = new KNN(trainingSet, i);
                kNNresults.add(myKNN.accuracy(testSet));
            }
            System.out.println( i + ";"
                    + kNNresults.stream().mapToDouble(o->o.doubleValue()).average().getAsDouble());
        }


        /************************************************************************************************
         * Implementation of Apriori:                                                                   *
         * Query: Are there any interesting patterns regarding the programming languages students know? *
         ************************************************************************************************/


        System.out.println("-----------------------------------APRIORI RESULTS-------------------------------------------\n");

        // create an double [][] array with a subArray for representing each student.
        int[][] allStudyLines   = new int[parsedData.size()][];
        for (int i = 0; i <allStudyLines.length; i ++) {
            List<Integer> currentStudyLine = parsedData.get(i).programmingLanguages;
            allStudyLines[i] = new int[currentStudyLine.size()];
            for (int j = 0; j < currentStudyLine.size(); j++) {
                allStudyLines[i][j] = currentStudyLine.get(j);
            }
        }


        Hashtable<ItemSet, List<ItemSet>> associationRules;
        associationRules = Apriori.apriori(allStudyLines, 0.15, 0.80 );
        for (ItemSet key: associationRules.keySet()) {
            for (ItemSet value: associationRules.get(key)){
                System.out.println(key.toString() + " --> " + value.toString());
            }
        }


        /*********************************************************************************
         *  Implementation of KMeanCluster.KMeans Clustering algorithm                   *
         *  Is it possible to determine students gender based on height and shoe size?   *
         *********************************************************************************/

        /**
         * Cleaning data by replacing missing values.
         */
        List<Student> cleanedData = DataWareHouse.addMissingValues(parsedData);
        //System.out.println("Cleaned data w/ missing values replaced. ");
        //System.out.println(cleanedData);


        /**
         * Normalizing data
         */
        List<Student> normalizedData = DataWareHouse.normalization(cleanedData);
        //System.out.println(normalizedData.toString());


        /* Set studyLine to UNKNOWN, since you want to predict this.
        List<Student> studentsForKMean = DataWareHouse.normalization(dataForKMeans);
        for (Student s : studentsForKMean){
            s.studyLine = StudyLine.UNKNOWN; // or null?
            System.out.println(s);
        }
        */

        System.out.println("-----------------------------------CLUSTER RESULTS-------------------------------------------\n");
        ArrayList<KMeanCluster> ClustersFound = KMeans.KMeansPartition(2, normalizedData);
        for (KMeanCluster c : ClustersFound) {
            System.out.println(c.toString());
        }

    }
}
