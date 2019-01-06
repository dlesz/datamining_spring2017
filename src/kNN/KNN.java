package kNN;

import java.util.*;
import DataWareHouse.Student;
import Enums.*;


/**
 * Created by dlesz on 12/03/2017.
 */


public class KNN {
    private Map<Double, List<Student>> distancesBetweenStudentTreeMap;
    private final List<Student> listOfStudents;
    private final int k;
    public int correct = 0;
    public int incorrect = 0;

    public KNN(List<Student> trainingSet, int k) {
        listOfStudents = trainingSet;
        this.k = k;
        distancesBetweenStudentTreeMap = new TreeMap<>(); //TreeMap Sorts by value. No need to sort a hashMap
    }

    /**
     *  Calculates distance between two students based on their Phone OS, age, and studyLine.
     * @param s1
     * @param s2
     * @return the distance (Double) between two Students.
     */
    public static double calculateDistances(Student s1, Student s2) {
        double distance = 0;

        distance += Math.pow(s1.phoneOS.getNumVal() - s2.phoneOS.getNumVal(),2);
        distance += Math.pow(s1.age - s2.age,2);
        distance += Math.pow(s1.studyLine.getNumVal() - s2.studyLine.getNumVal(),2);

        return Math.sqrt(distance);
    }

    /**
     * This function populates the map with the distance from the current student
     * to all other students in the listOfStudents.
     * @param student object
     */
    private void populateDistances(Student student) {
        distancesBetweenStudentTreeMap = new TreeMap<>();

        for (int i = 0; i < listOfStudents.size(); i++) {
            Student currentStudent = listOfStudents.get(i);
            Double distance = calculateDistances(student, currentStudent);
            if (!distancesBetweenStudentTreeMap.containsKey(distance)) {
                List<Student> currentList = new ArrayList<>();
                currentList.add(currentStudent);
                distancesBetweenStudentTreeMap.put(distance, currentList);
            } else {
                distancesBetweenStudentTreeMap.get(distance).add(currentStudent);
            }
        }
    }

    /**
     * This function classifies a student StudyLine based nearest neighbours
     * @param student current student to get classified
     */
    public void classifyStudent (Student student) {
        populateDistances(student);
        Map<StudyLine, Integer> neighbours = countNeighbours();
        StudyLine studyLineToAdd = null;
        int highestNeighbor = 0;
        for (StudyLine key: neighbours.keySet()){
            if (neighbours.get(key) > highestNeighbor) studyLineToAdd = key;
            highestNeighbor = neighbours.get(key);
        }

        // check if student was classified.
        if (studyLineToAdd!= student.studyLine) incorrect++;
        else correct++;

    }


    /**
     * This function returns a map containing the studyLine of the k nearest-neighbors of the given student.
     * @return
     */
    private Map<StudyLine, Integer> countNeighbours() {
        int neighbours = k;
        Map<StudyLine, Integer> counter = new HashMap<>();

        for (Double key : distancesBetweenStudentTreeMap.keySet()) {
            List<Student> currentList = distancesBetweenStudentTreeMap.get(key);
            for (int i = 0; i < currentList.size(); i++) {
                if (counter.containsKey(currentList.get(i).studyLine)) {
                    counter.put(currentList.get(i).studyLine, counter.get(currentList.get(i).studyLine) + 1);
                } else counter.put(currentList.get(i).studyLine, 1);
                neighbours--;
                if (neighbours <= 0) break;
            }
            if (neighbours <= 0) break;
        }
        return counter;
    }


    /**
     * Calculates the accuracy of kNN calculation based on the testSet.
     * @param testSet list of Students
     * @return the accuracy = correct / total
     */

    public double accuracy (List<Student> testSet) {
        for (Student s: testSet) {
            classifyStudent(s);
        }
        return (double) correct / (correct + incorrect);
    }
}
