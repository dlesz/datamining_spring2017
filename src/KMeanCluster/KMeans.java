package KMeanCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import DataWareHouse.Student;


/**
 * Method:
 * (1) arbitrarily choose k objects from D as the initial cluster centers;
 * (2) repeat
 * (3) (re)assign each object to the cluster to which the object is the most similar,
 * based on the mean value of the objects in the cluster;
 * (4) update the cluster means, that is, calculate the mean value of the objects for each
 */
public class KMeans {
    /**
     * This function generate clusters of students based on height and shoe size given number of clusters and a list of students
     *
     * @param k    the number of clusters
     * @param data the list of students
     * @return
     * @throws IllegalAccessException
     */
    public static ArrayList<KMeanCluster> KMeansPartition(int k, List<Student> data) throws IllegalAccessException {
        ArrayList<KMeanCluster> clusters;
        clusters = generateInitialClusters(k, data);
        ArrayList<KMeanCluster> oldClusters;
        do {
            oldClusters = new ArrayList<>();
            for (KMeanCluster c : clusters) {
                KMeanCluster oldCluster = new KMeanCluster(c);
                oldClusters.add(oldCluster);
            }
            clusters = assignClusters(data, clusters);
            clusters = recalculateCentroid(clusters);

        } while (!clusters.equals(oldClusters));


        return clusters;
    }

    /**
     * This function recalculates the centroid of a list of clusters
     *
     * @param clusters list of clusters
     * @return list of clusters with recalculated centroid.
     */
    private static ArrayList<KMeanCluster> recalculateCentroid(ArrayList<KMeanCluster> clusters) {
        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).recalculateCentroid();
        }
        return clusters;
    }

    /**
     * This function generates initial clusters with random centroids.
     *
     * @param k
     * @param data
     * @return
     */
    public static ArrayList<KMeanCluster> generateInitialClusters(int k, List<Student> data) {

        ArrayList<KMeanCluster> clusters = new ArrayList<>();
        Integer random = randInt(0, data.size() - 1); // initialize random number
        List<Integer> usedIndexes = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            while (usedIndexes.contains(random)) {
                random = randInt(0, data.size() - 1);
            }
            usedIndexes.add(random); // include the index used in order for the next cluster not to use as a centroid again
            KMeanCluster cluster = new KMeanCluster(data.get(random));
            clusters.add(cluster);
        }

        return clusters;
    }

    /**
     * assign values to clusters based on minimum distances to the centroids
     *
     * @param data     a list of students to be assigned
     * @param clusters a list of clusters to have students assigned to
     * @return a list of clusters with assigned students
     */
    public static ArrayList<KMeanCluster> assignClusters(List<Student> data, ArrayList<KMeanCluster> clusters) throws IllegalAccessException {

        clusters = removeElements(clusters);

        for (Student i : data) {
            double minDistance = Double.MAX_VALUE;
            int clusterIndex = 0;
            for (int j = 0; j < clusters.size(); j++) {
                double distance = i.distance(clusters.get(j).getCentroid());
                if (distance < minDistance) { // if distance less, then save cluster position and update mindistance
                    minDistance = distance;
                    clusterIndex = j;
                }
            }
            clusters.get(clusterIndex).add(i);
        }

        return clusters;
    }

    /**
     * This function removes elements from a list of clusters
     *
     * @param clusters the list of custers
     * @return a list of empty clusters
     */
    private static ArrayList<KMeanCluster> removeElements(ArrayList<KMeanCluster> clusters) {
        for (KMeanCluster c : clusters) {
            c.removeElements();
        }

        return clusters;
    }

    /**
     * This function generates a random integer based on a min and max
     *
     * @param min min value for the int
     * @param max max value for the int
     * @return a random integer between min and max
     */
    public static int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
