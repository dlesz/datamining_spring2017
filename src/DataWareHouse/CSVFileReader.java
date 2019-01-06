package DataWareHouse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The DataWareHouse.DataWareHouse.CSVFileReader class is used to load a csv file
 *
 */
public class CSVFileReader {
	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * This method is utilizes the string.split method for splitting each line of the data file.
	 * String tokenizer bug fix provided by Martin Marcher.
	 * @param csvFile File to load
	 * @param seperationChar Character used to seperate entries
	 * @param nullValue What to insert in case of missing values
	 * @return Data file content as a 2D string array
	 * @throws IOException
	 */

	public static String[][] readDataFile(String csvFile, String seperationChar, String nullValue, boolean skipHeaderRow) throws IOException
	{

		List<String[]> lines = new ArrayList<>();
		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(csvFile)));

		// read the header
		String line = bufRdr.readLine();

		while ((line = bufRdr.readLine()) != null) {
			String[] arr = line.split(seperationChar);

			for(int i = 0; i < arr.length; i++)
			{
                //arr[i] = arr[i].replace("\"", ""); //replaces " with empty space

				if(arr[i].equals(""))
				{
					arr[i] = nullValue;
				}

			}
			if(!skipHeaderRow)
			{
				lines.add(arr);
			}


		}

		String[][] ret = new String[lines.size()][];
		bufRdr.close();
		return lines.toArray(ret);
	}


	public static void main(String args[]) {
		try {
			String[][] data = readDataFile("data/Data Mining - Spring 2017.csv",";", "-",false);
			for (String[] line : data) {
				System.out.println(Arrays.toString(line));
			}
			//System.out.println("Number of tuples loaded: "+data.length);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());

		}


	}
}
