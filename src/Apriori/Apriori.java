package Apriori;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Apriori {

    /**
     *
     * procedure apriori gen(Lk−1:frequent (k −1)-itemsets)
     * (1) for each itemset l1 2 Lk−1
     * (2) for each itemset l2 2 Lk−1
     * (3) if (l1[1] = l2[1])^(l1[2] = l2[2]) ^...^(l1[k −2] = l2[k −2])^(l1[k −1] < l2[k −1]) then {
     * (4) c = l1 1 l2; // join step: generate candidates
     * (5) if has infrequent subset(c, Lk−1) then
     * (6) delete c; // prune step: remove unfruitful candidate
     * (7) else add c to Ck;
     * (8) }
     * (9) return Ck;
     *
     * @param items the dataSet input.
     * @param supportThreshold
     * @param confidenceThreshold
     * @return The association rules with confidence >= confidence threshold for frequent itemsets with frequency >= support threshold
     */
    public static Hashtable<ItemSet, List<ItemSet>> apriori(int[][] items, double supportThreshold, double confidenceThreshold ) {
        Hashtable<ItemSet, Double> allFrequentItemSets = new Hashtable<>();
        Hashtable<ItemSet, Double> frequentItemSets = generateFrequentItemSetsLevel1( items, supportThreshold );

        // stores the frequentItemSets level 1
        for (ItemSet key: frequentItemSets.keySet()){
            allFrequentItemSets.put(key, frequentItemSets.get(key));
        }

        for (int k = 1; frequentItemSets.size() > 0; k++) {
            System.out.print( "Finding frequent itemsets of length " + (k + 1) + "…" );
            frequentItemSets = generateFrequentItemSets( supportThreshold, items, frequentItemSets );
            System.out.println( " found " + frequentItemSets.size() );

            // stores the frequentItemSets found
            for (ItemSet key: frequentItemSets.keySet()){
                allFrequentItemSets.put(key, frequentItemSets.get(key));
            }
        }

        //return association rules
        return generateAssociationRules(allFrequentItemSets, confidenceThreshold);
    }


    /**
     * Generating the association rules based on the  confidence Threshold
     * @param ItemSets
     * @param confidenceThreshold
     * @return association rules with confidence >= confidence Threshold
     */
    private static Hashtable<ItemSet, List<ItemSet>> generateAssociationRules(Hashtable<ItemSet, Double> ItemSets, double confidenceThreshold) {
        Hashtable<ItemSet, List<ItemSet>> associationRules = new Hashtable<>();

        // iterate through all the rules
        for (ItemSet key: ItemSets.keySet()) {
            // adding all the subsets to a list
            List<ItemSet> subsets = generateSubSets(ItemSets, key);


            for (int i = 0; i < subsets.size(); i ++) {
                ItemSet currentSubset = subsets.get(i);
                double subSetSupport = ItemSets.get(currentSubset);
                double supportOfFrequentItemSet = ItemSets.get(key);
                double ruleConfidence = supportOfFrequentItemSet/ subSetSupport;
                ItemSet subtractKey = key.subtract(subsets.get(i));
                if (ruleConfidence >= confidenceThreshold ){
                    if (!associationRules.containsKey(currentSubset)) {
                        List<ItemSet> list = new ArrayList<>();
                        associationRules.put(currentSubset, list);
                    }
                    associationRules.get(currentSubset).add(subtractKey);

                }
            }
        }
        return associationRules;
    }


    /**
     *  This function returns all the subsets of the given itemSet from a map of ItemSets
     * @param ItemSets
     * @param itemset
     * @return a list of subSets
     */
    public static List<ItemSet> generateSubSets(Hashtable<ItemSet, Double> ItemSets, ItemSet itemset) {
        List<ItemSet> candidateSubSets = new ArrayList<>();
        List<ItemSet> subSets = new ArrayList<>();
        candidateSubSets.addAll(ItemSets.keySet());
        for (int i = 0; i < candidateSubSets.size(); i ++) {
            if (candidateSubSets.get(i).isItASubset(itemset)) {
                subSets.add(candidateSubSets.get(i));
            }
        }

        return subSets;
    }


    /**
     * This method generates the frequent ItemSets based on the chosen supportThreshold.
     * @param supportThreshold
     * @param itemSets
     * @param lowerLevelItemSets
     * @return
     */
    private static Hashtable<ItemSet, Double> generateFrequentItemSets(double supportThreshold, int[][] itemSets,
                                                                       Hashtable<ItemSet, Double> lowerLevelItemSets ) {
        Hashtable<ItemSet, Double> candidateItemSets = new Hashtable<>();
        List<ItemSet> lowerLevelItemSetsList = new ArrayList<>();
        lowerLevelItemSetsList.addAll(lowerLevelItemSets.keySet());

        for (int i = 0; i < lowerLevelItemSetsList.size(); i ++) {
            for (int j = i + 1; j < lowerLevelItemSetsList.size(); j ++){
                ItemSet candidate = joinSets(lowerLevelItemSetsList.get(i), lowerLevelItemSetsList.get(j));

                if (candidate == null) continue;
                if (candidate.lowerSubsetsAreContained(lowerLevelItemSetsList)){ // pruning
                    double support = countSupport(candidate,itemSets);
                    //only adds if the support is equal to or bigger than the chosen threshold
                    if (support >= supportThreshold) candidateItemSets.put(candidate, support);
                }
            }
        }

        return candidateItemSets;
    }


    /**
     * Joins itemSet
     *
     *  Input: frequent (k−1)-itemsets: Lk−1
     *   • Elements in each itemset are ordered
     *   • Itemsets in Lk−1 are combined if their first k−2 elements
     *  are the same. Only the last may differ!
     *
     *  Example:
     *  L3 = {abc, abd, acd, ace, bcd}
     *  k = 4
     *  k_1 = 3
     *  k_2 = 2

     *  abc ∪ abd ⇒ abcd
     *  acd ∪ ace ⇒ acde
     *
     * @param first first itemset to be joined
     * @param second second itemset to be joined
     * @return
     */
    public static ItemSet joinSets(ItemSet first, ItemSet second ) {

        ItemSet result ;

        if (first.size() == second.size() && first.size() == 1){
            result = new ItemSet(first);
            result.add(second.get(0));
            return result;
        }


        for (int i = 0; i < first.size() ; i ++ ) {
            if (i < first.size() - 1) { // check if they are the same up to the last element
                if (first.get(i) != second.get(i)) return null;
            }
            else { ////only join item sets, if last elements != equal.
                if (first.get(i) != second.get(i)) {
                    result = new ItemSet(first);
                    result.add(second.get(i));
                    return result;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param items
     * @param supportThreshold
     * @return
     */
    private static Hashtable<ItemSet, Double> generateFrequentItemSetsLevel1(int[][] items, double supportThreshold ) {

        Hashtable<ItemSet, Double> initialCandidates = new Hashtable<>();

        for (int i = 0; i < items.length; i ++){
            for (int j = 0; j < items[i].length; j ++ ) {
                ItemSet item = new ItemSet(new int[] {items[i][j]});
                double support = countSupport(item, items);
                if( support >= supportThreshold ) {
                    initialCandidates.put(item, countSupport(item, items));
                }
            }
        }

        return initialCandidates;
    }

    /**
     * This method counts the support of a itemSet, going over all possible combinations of transactions.
     * @param itemSet
     * @param data
     * @return the countSupport as a double.
     */
    public static double countSupport(ItemSet itemSet, int[][] data ) {

        int support = 0;
        for (int i = 0; i < data.length; i ++) {
            int foundElements = 0;
            for (int j = 0; j < itemSet.getElements().length; j ++){
                for (int k = 0; k < data[i].length; k ++) {
                    if (itemSet.getElements()[j] == data[i][k]){
                        foundElements ++;
                        break;
                    }

                }
            }
            if (foundElements == itemSet.getElements().length) support++;
        }

        return support / (double) data.length;
    }

}
