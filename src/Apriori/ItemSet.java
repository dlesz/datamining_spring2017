package Apriori;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * The ItemSet class is used to store information concerning a single transaction.
 * Should not need any changes.
 *
 */
public class ItemSet {

	/***
	 * The PRIMES array is internally in the ItemSet-class' hashCode method
	 */
	private static final int[] PRIMES = { 2, 3, 5, 7, 11, 13, 17, 23, 27, 31, 37 };
     int[] set;

    /**
     * Creates a new instance of the ItemSet class.
     * @param set Transaction content
     */
    public ItemSet( int[] set ) {
        this.set = set;
    }

    /**
     * Subtracts a given item from the ItemSet, returns the remaining itemSet.
     * @param itemToSubtract
     * @return
     */
    public ItemSet subtract(ItemSet itemToSubtract) {

        int[] resultingArray = new int[this.size() - itemToSubtract.size()];
        List<Integer> temp = new ArrayList<>();

        for (int i = 0; i < this.size(); i ++) {
            for (int j = 0 ; j < itemToSubtract.size(); j ++) {
                if (this.set[i] == itemToSubtract.set[j]) break;
                if (j == itemToSubtract.size() - 1) temp.add(this.set[i]);
            }
        }

        for (int i = 0; i < temp.size(); i ++) {
            resultingArray[i] = temp.get(i);
        }

        return new ItemSet(resultingArray);
    }

    /**
     * makes a copy of the itemset
     * @param another the itemset to be copied
     */
    public ItemSet (ItemSet another) {
        this.set = another.set;
    }

    /**
     * Checks if its  a subset of the given itemSet
     * @param itemSet
     * @return
     */
    public boolean isItASubset(ItemSet itemSet) {
        if (this.size() >= itemSet.size()) return false;

        boolean result = true;
        for (int i = 0; i < this.size(); i ++) {
            for (int j = 0; j < itemSet.size(); j ++) {
                if (this.set[i] == itemSet.set[j]) break;
                if (j == itemSet.size() - 1) result = false;
            }
        }

        return result;
    }

    @Override
    /**
     * hashCode functioned used internally in Hashtable
     */
    public int hashCode() {
        int code = 0;
        for (int i = 0; i < set.length; i++) {
            code += set[i] * PRIMES[i];
        }
        return code;
    }

    
    @Override
    /**
     * Used to determine whether two ItemSet objects are equal
     */
    public boolean equals( Object o ) {
        if (!(o instanceof ItemSet)) {
            return false;
        }
        ItemSet other = (ItemSet) o;
        if (other.set.length != this.set.length) {
            return false;
        }
        for (int i = 0; i < set.length; i++) {
            if (set[i] != other.set[i]) {
                return false;
            }
        }
        return true;
    }

    public int[] getElements() {
        return set;
    }

    public int get(int index) {
        return set[index];
    }

    public int size() {
        return set.length;
    }

     /**
     * Adds an element to the end of the itemSet
     * @param value value to be added
     */
    public void add(int value) {
        int[] newItem = new int[set.length + 1];
        for (int i = 0; i < set.length; i ++) {
            newItem[i] = set[i];
        }
        newItem[newItem.length - 1] = value;
        set = newItem;
    }

    /**
     * Checks if the current itemSet lowerLevel subsets are contained in the itemSetList passed. Used for pruning
     * @param itemSetList a list of itemSets
     * @return true if all lowersubsets of the itemset are contained in the itemSetList
     */
    public boolean lowerSubsetsAreContained(List<ItemSet> itemSetList) {
        List<ItemSet> subsets = generateLowerSubsets();
        for (int i = 0; i < subsets.size(); i ++) {
            if (!itemSetList.contains(subsets.get(i))) return false;
        }
        return true;
    }


    /**
     * Generates all the lower subsets
     * @return a List of all the lowerLevel Subsets
     */
    private List<ItemSet> generateLowerSubsets() {
        List<ItemSet> lowerLevelSubsets = new ArrayList<>();
        for (int i = 0; i < this.size(); i ++) {
            ItemSet currentItemToSubtract = new ItemSet(new int[] {this.get(i)} );
            lowerLevelSubsets.add(subtract(currentItemToSubtract));
        }
        return lowerLevelSubsets;
 }

    @Override
    public String toString() {
        return "ItemSet{" +
                "set=" + Arrays.toString(set) +
                '}';
    }
}
