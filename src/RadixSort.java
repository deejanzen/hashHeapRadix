/**
 * RadixSort uses bit shifting inorder to limit the number of buckets for counting sort of integer
 * @author Dustin Janzen
 */
public class RadixSort {
    public int [] values;
    private int [] bucket;

    /**
     * constructor does nothing per public instance variables
     * @author Dustin Janzen
     */
    public RadixSort(){

    }

//    Our Radix Sort sorts integers, and it breaks each one down into 4-bit chunks.
//    Chunk 0 is the least significant chunk, and chunk 7 is the most.
//    getBucket() takes an int as input, along with a chunk number;
//    it must return the 4 bits of that chunk,
//    shifted down so that the value is in the range 0-15 (inclusive).

    /**
     * Does bit shifting and masking in order to limit the buckets to 16
     * @param value the int to be sorted
     * @param chunk the 4 bit chink or the 32 bit int
     * @return
     */
    public static int getBucket(int value, int chunk){
        return (value >> 4 * chunk) & 0x0f;
    }

//    This method will do one pass of Radix Sort - that is, it must do Counting Sort,
//    using the selected chunk as the key to sort the rest of the values.

    /**
     * Does one counting sort of a given chunk.
     * Counts the numbers in the buckets and the sets the buckets up for sorting.
     * Sorts.
     * @param chunk
     */
    public void doRadixSort_onePass(int chunk){
        //count
        bucket = new int [16];
        for (int i = 0; i < values.length;i++){
            bucket[getBucket(values[i], chunk)] += 1;
        }
        //setup buckets for indexing
        int carry = -100;
        int temp;
        for(int i = 0; i < bucket.length;i++){
            if (i == 0){
                carry = bucket[i];
                bucket[i] = 0;

            }
            else {
                temp = bucket[i];
                bucket[i] = carry + bucket[i-1];
                carry = temp;
            }
        }

        //sort
        int [] sorted = new int [values.length];
        for(int i = 0; i < values.length;i++){
            //System.out.println("\nvalues @ " + i + " = " + values[i]);
            //printBucket();
            sorted[bucket[getBucket(values[i], chunk)]] = values[i];
            bucket[getBucket(values[i], chunk)] += 1;
            //printBucket();
        }

        values = sorted;
    }

//    This method performs the entire Radix Sort. It must call doRadixSort onePass()
//    to perform each of the passes through the data.

    /**
     * calls doRadixSort_onePass for each chunk
     */
    public void doRadixSort(){
        for (int i = 0; i < 8; i++){
            doRadixSort_onePass(i);
        }
    }

    private void printBucket(){
        for (int i = 0; i < bucket.length;i++){
            System.out.print(bucket[i] + " ");
        }
        System.out.println();
    }
}
