package DataWareHouse;

import Enums.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dlesz on 06/02/2017.
 */
public class DataWareHouse {

    /**
     * This method parses and transforms all tuples using the DataWareHouse.DataWareHouse.CSVFileReader, pre-process them by cleaning the data
     * and replacing unknown values by the median value for the respective attribute.
     * @return List of students/tuples
     */
    public static List<Student> parseData() {
        List<Student> students = new ArrayList<>();

        try {
            String[][] rawData = CSVFileReader.readDataFile("data/Data Mining - Spring 2017.csv",";;", "-",false);

            for(int i = 0; i < rawData.length; i++) {
                Student newTuple = new Student();
                newTuple.age = loadAge(rawData[i][1]);
                newTuple.gender = loadGender(rawData[i][2]);
                newTuple.shoeSize = loadShoeSize(rawData[i][3]);
                newTuple.height = loadHeight(rawData[i][4]);
                newTuple.studyLine = loadStudyLine(rawData[i][5]);
                newTuple.programmingLanguages = loadProgrammingLanguages(rawData[i][7]);
                newTuple.phoneOS = loadPhoneOs(rawData[i][8]);
                students.add(newTuple);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }




    /**
     * Parses student age, if age is not null, it returns 0, if age below 20 and above 70 it returns 0.
     * @param age
     * @return an int of age.
     */
    private static int loadAge(String age) {
        //Data cleaning - removing odd age.
        if (age == null) return 0;
        age = age.replaceAll("\"", "");
        age = age.replaceAll("[^,.0-9]", "").replaceAll(" ", "");
        int intAge;
        try {
            intAge = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            return 0;
        }
        if (intAge < 20 || intAge > 70)
            return 0;
        else return intAge;
    }

    /**
     *  Parses gender based on the following rules, if the inputted data does not match,
     *  it returns null.
     * @param gender
     * @return
     */
    private static Gender loadGender(String gender){
        if (gender == null) return null;
        gender = gender.replaceAll("\"", "").replaceAll(" ", "");
        gender = gender.toLowerCase();
        if (gender.equals("male") ||  gender.startsWith("m")) return Gender.MALE;
        else if (gender.equals("female") || gender.startsWith("f")) return Gender.FEMALE;
        else return null;
    }

    /**
     *  Parses shoeSizes, if bigger than 60 or less than 30 returns null.
     * @param shoeSize
     * @return
     */
    private static Double loadShoeSize(String shoeSize) {
        if (shoeSize == null) return null;
        shoeSize = shoeSize.replaceAll("\"", "").replaceAll(",", ".").replaceAll("[^.0-9]", "").replaceAll(" ", "");
        Double s; // s for shoeSize
        try {
            s = Double.parseDouble(shoeSize);
        } catch (NumberFormatException e) {
            return null;
        }
        if (s < 30 ||  s  >60 ) return null; //sets shoeSize to null if less than 30 or bigger than size 60
        return s;
    }

    private static Double loadHeight(String height){
        if (height == null) return null;
        height = height.replaceAll("\"", "");
        height = height.replaceAll("[^,.0-9]", "").replaceAll(" ", "");
        Double h;
        try {
            h = Double.parseDouble(height);
        } catch (NumberFormatException e) {
            return null;
        }
        if (h > 147 &&  h < 220) return h; // person is between 147 and 220 return their height, else return null.
        return null;
    }


    /**
     * Parses the studylines/education tracks of the student
     * @param studyLines
     * @return an enum
     */
    private static StudyLine loadStudyLine(String studyLines){
        if (studyLines == null) return null;
        studyLines = studyLines.replaceAll("\"", "");
        studyLines = studyLines.toLowerCase();
        if (studyLines.contains("guest")) return StudyLine.GUEST;
        else if (studyLines.startsWith("games")) return StudyLine.GAMES;
        else if (studyLines.contains("sdt-dt")) return StudyLine.SDT_DT;
        else if (studyLines.endsWith("c")) return StudyLine.SDT_AC;
        else if (studyLines.endsWith("e")) return StudyLine.SDT_SE;
        else if (studyLines.contains("dim")) return StudyLine.DIM;
        else if (studyLines.equals("swu")) return StudyLine.SWU;

        else return StudyLine.UNKNOWN;
    }

    /**
     * Parses the preferable phone OS
     * @param phoneOs
     * @return
     */
    private static PhoneOs loadPhoneOs(String phoneOs) {
        if (phoneOs == null) return null;
        phoneOs = phoneOs.replaceAll("\"", "");
        phoneOs = phoneOs.toLowerCase();
        switch (phoneOs) {
            case "ios":
                return PhoneOs.IOS;
            case "windows":
                return PhoneOs.WINDOWS;
            case "ubuntu touch":
                return PhoneOs.UBUNTU_TOUCH;
            case "arch linux":
                return PhoneOs.ARCH_LINUX;
            case "android":
                return PhoneOs.ANDROID;
            default:
                return null;
        }
    }

    private static List<Integer> loadProgrammingLanguages(String pL) {
        List<Integer> results = new ArrayList<>();
        if (pL == null) results.add(-1);
        //replace " with nothing ---> replace semi-colon with coma ---> replace dot with nothing.
        pL = pL.replaceAll("\"", "").replaceAll(";", " ").replaceAll(",", " ").replaceAll("\\.", "");
        String[] arrayOfLanguages = pL.replaceAll("( )+", ",").toLowerCase().split(",");
        for (String language: arrayOfLanguages) {
            int languageNo = ProgrammingLanguages.ProgrammingLanguages(language.trim()); //trim removes spaces
            results.add(languageNo);
        }
        //System.out.println(results.toString());

        return results;
    }

    /**
     * Adds missing Values to gender, shoeSize and Height.
     * @param students
     * @return
     */
    public static List<Student> addMissingValues(List<Student> students) throws NoSuchFieldException, IllegalAccessException {
        List<Student> result = new ArrayList<>();
        Double medianShoeSizeMale = median(getAttribute(students, "gender", Gender.MALE, "shoeSize"));
        Double medianShoeSizeFemale = median(getAttribute(students, "gender", Gender.FEMALE, "shoeSize"));
        Double medianHeightMale = median(getAttribute(students, "gender", Gender.MALE, "height"));
        Double medianHeightFemale = median(getAttribute(students, "gender", Gender.FEMALE, "height"));
        for (Student s : students) {
            if (s.gender == null) s.gender = Gender.UNKNOWN;
            if (s.gender.equals(Gender.MALE)) {
                if (s.shoeSize == null) s.shoeSize = medianShoeSizeMale;
                if (s.height == null) s.height = medianHeightMale;
            }
            else {
                if (s.shoeSize == null) s.shoeSize = medianShoeSizeFemale;
                if (s.height == null) s.height = medianHeightFemale;
            }
            result.add(s);
        }
        /*
        //TestPrints
        System.out.println("Male Median ShoeSize: " + medianShoeSizeMale);
        System.out.println("Female Median ShoeSize: " + medianShoeSizeFemale);
        System.out.println("Male Median Height: " + medianHeightMale);
        System.out.println("Female Median Height: " + medianHeightFemale);
        */
        return result;
    }

    /**
     *
     * @param students
     * @param fieldCondition
     * @param fieldValue
     * @param fieldToGet
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static List<Double> getAttribute(List<Student> students, String fieldCondition, Object fieldValue, String fieldToGet) throws NoSuchFieldException, IllegalAccessException {
        List<Double> result = new ArrayList<>();
        Field field = Student.class.getField(fieldCondition);
        for (Student s: students) {
            Double fieldToAdd = (Double) Student.class.getField(fieldToGet).get(s);
            Gender fieldToCompare = (Gender) field.get(s);
            if (fieldToAdd == null || fieldToCompare == null ) continue;
            if (fieldToCompare.equals(fieldValue)){
                result.add(fieldToAdd);
            }
        }
        return result;
    }


    /**
     * Mean Method - is very sensitive to outliers (2 + 4 + 100(outlier) = 106 / 3 = 35,333… )
     * @param input
     * @return
     */
    public double meanDouble(ArrayList<Double> input) {
        double mean = 0;

        for (double d: input) {
            mean += d;
        }

        return (double) Math.round(mean/input.size()*100.0) / 100.0;
    }

    /**
     *
     * @param input
     * @return
     */
    public double meanAge(ArrayList<Integer> input){
        double mean = 0;

        for (int i : input){
            mean +=i;
        }

        return (double) Math.round(mean/input.size()*100.0) /100.0;
    }

    /**
     *
     * @param data
     * @return
     */
    public static Double median (List<Double> data) {
        List<Double> sortedData = data;
        Collections.sort(sortedData);
        Double median;
        if (sortedData.size() % 2 == 0) {
            median = (sortedData.get(sortedData.size() / 2) + sortedData.get(sortedData.size() / 2  - 1)) / 2;
        }
        else median = sortedData.get((int)Math.floor((sortedData.size()/2)));
        return median;
    }
    /**
     * Min-max normalization for shoeSize, height and age.
     * @param list
     * @return
     */
    public static List<Student> normalization(List<Student> list){
        List<Student> result = new ArrayList<>();
        Double minShoeSizeValue = list.stream().mapToDouble(o -> o.shoeSize).min().getAsDouble();
        Double maxShoeSizeValue = list.stream().mapToDouble(o -> o.shoeSize).max().getAsDouble();
        Double minHeightValue = list.stream().mapToDouble(o -> o.height).min().getAsDouble();
        Double maxHeightValue = list.stream().mapToDouble(o -> o.height).max().getAsDouble();
        //Double maxAgeValue = list.stream().mapToDouble(o -> o.age).max().getAsDouble();
        //Double minAgeValue = list.stream().mapToDouble(o -> o.age).min().getAsDouble();


        for (Student s: list) {
            s.height = (s.height - minHeightValue) / (maxHeightValue - minHeightValue);
            s.shoeSize = (s.shoeSize - minShoeSizeValue) / (maxShoeSizeValue - minShoeSizeValue);
            //s.age = ((s.age - minAgeValue) / (maxAgeValue - minAgeValue));

            result.add(s);
        }

        return result;

    }
}
