package DataWareHouse;

import Enums.StudyLine;
import Enums.Gender;
import Enums.PhoneOs;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The DataWareHouse.Student class is used to obtain the data for each students questionnaire (tuples/row) found in the cvs file.
 * Created by dlesz on 06/02/2017.
 */
public class Student {
    public double age;
    public Gender gender;
    public Double shoeSize;
    public Double height;
    public StudyLine studyLine;
    public List<Integer> programmingLanguages;
    public PhoneOs phoneOS;


    public Student() {

    }

    public Student(Student student) {
        this.age = student.age;
        this.gender = student.gender;
        this.shoeSize = student.shoeSize;
        this.height = student.height;
        this.studyLine = student.studyLine;
        this.phoneOS = student.phoneOS;
        this.programmingLanguages = student.programmingLanguages;

    }

    /**
     * Extra Constructor for KMeans algo
     * @param height
     * @param shoeSize
     */
    public Student(double height, double shoeSize) {
        this.height = height;
        this.shoeSize = shoeSize;
    }

    @Override
    public String toString() {
        return  age + " ," + //How can I use this variable normalized?
                gender +
                " ," + shoeSize +
                " ," + height
                +" ," + studyLine;
                //+ " ," + programmingLanguages
                //+ " ," + phoneOS ;

    }

    /**
     *
     * This method compares the distance between two Student tuples based on their shoeSize and height.
     * @param two
     * @return
     * @throws IllegalAccessException
     */
    public double distance(Student two) throws IllegalAccessException {
        double distance = 0;
        // iterating through all variables
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(Double.class)) {
                Double doubleDistance = Math.abs((double) field.get(this) - (double) field.get(two));
                doubleDistance = Math.pow(doubleDistance, 2.0);
                distance += doubleDistance;
            }
        }
        return Math.sqrt(distance); // returns the Euclidean Distance
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (Double.compare(student.age, age) != 0) return false;
        if (gender != student.gender) return false;
        if (shoeSize != null ? !shoeSize.equals(student.shoeSize) : student.shoeSize != null) return false;
        if (height != null ? !height.equals(student.height) : student.height != null) return false;
        if (studyLine != student.studyLine) return false;
        if (programmingLanguages != null ? !programmingLanguages.equals(student.programmingLanguages) : student.programmingLanguages != null)
            return false;
        return phoneOS == student.phoneOS;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(age);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (shoeSize != null ? shoeSize.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (studyLine != null ? studyLine.hashCode() : 0);
        result = 31 * result + (programmingLanguages != null ? programmingLanguages.hashCode() : 0);
        result = 31 * result + (phoneOS != null ? phoneOS.hashCode() : 0);
        return result;
    }
}
