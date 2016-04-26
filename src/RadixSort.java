/**
 * Created by djanzen on 4/26/16.
 */
public class RadixSort {
    public int [] values;

    public RadixSort(){

    }

//    Our Radix Sort sorts integers, and it breaks each one down into 4-bit chunks.
//    Chunk 0 is the least significant chunk, and chunk 7 is the most.
//    getBucket() takes an int as input, along with a chunk number;
//    it must return the 4 bits of that chunk,
//    shifted down so that the value is in the range 0-15 (inclusive).

    public static int getBucket(int value, int chunk){
        return 0;
    }

//    This method will do one pass of Radix Sort - that is, it must do Counting Sort,
//    using the selected chunk as the key to sort the rest of the values.

    public void doRadixSort_onePass(int chunk){

    }

//    This method performs the entire Radix Sort. It must call doRadixSort onePass()
//    to perform each of the passes through the data.

    public void doRadixSort(){

    }
}
