package KMeanCluster;

import java.util.ArrayList;

import DataWareHouse.Student;

public class KMeanCluster {

	private ArrayList<Student> clusterMembers;
    private Student centroid;

	public KMeanCluster(Student centroid)
	{
		this.clusterMembers = new ArrayList<Student>();
		this.centroid = centroid;
	}

	public KMeanCluster(KMeanCluster c) {
		this.clusterMembers = c.getMembers();
		this.centroid = c.getCentroid();
	}

	public Student getCentroid() {
		return centroid;
	}



	@Override
	public String toString() {
		String toPrintString = "-----------------------------------CLUSTER START------------------------------------------" + System.getProperty("line.separator");
		
		for(Student s : this.clusterMembers)
		{
			toPrintString += s.toString() + System.getProperty("line.separator");
		}
		toPrintString += "-----------------------------------CLUSTER END-------------------------------------------" + System.getProperty("line.separator");
		
		return toPrintString;
	}

	public void add(Student i) {
		clusterMembers.add(i);
	}

    public ArrayList<Student> getMembers() {
        return clusterMembers;
    }

    public void removeElements() {
        clusterMembers = new ArrayList<>();
    }

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		KMeanCluster that = (KMeanCluster) o;

		if (clusterMembers != null ? !clusterMembers.equals(that.clusterMembers) : that.clusterMembers != null)
			return false;
		return centroid != null ? centroid.equals(that.centroid) : that.centroid == null;
	}

	@Override
	public int hashCode() {
		int result = clusterMembers != null ? clusterMembers.hashCode() : 0;
		result = 31 * result + (centroid != null ? centroid.hashCode() : 0);
		return result;
	}

    /**
     * This function recalculates the centroid of the cluster based on its members average
     */
    public void recalculateCentroid() {
        double heightAvg = clusterMembers.stream().mapToDouble(o-> o.height).average().getAsDouble();
        double shoeSizeAvg = clusterMembers.stream().mapToDouble(o-> o.shoeSize).average().getAsDouble();
        centroid = new Student(heightAvg, shoeSizeAvg);
    }
}
