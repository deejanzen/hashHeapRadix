/**
 * MaxHeap incl. maxHeapify, buildMaxHeap, removeMax, and heapSort
 * @author Dustin Janzen
 */
public class MaxHeap {
    public int [] data;
    public int count;

    /**
     * Does nothing. Public data.
     */
    public MaxHeap(){

    }

    /**
     * maxHeapify takes an array index and implements the heap property, if necessary.
     * Uses recursion to walk the array.
     * @param index
     */
    public void maxHeapify(int index){
        if (index > count) throw new IllegalArgumentException();
        //check  leaf
        if (index >= count / 2 ) return;

        int leftIndex  = 2 * index + 1;
        int rightIndex = 2 * index + 2;

        //check both
        if (leftIndex < count && rightIndex < count){
            if (data[leftIndex] <= data[index] && data[rightIndex] <= data[index]) return; //heap property
            else{
                if(data[leftIndex] - data[rightIndex] >= 0){
                    //left one or tie. swap left, recurse left
                    int temp = data[index];
                    data[index] = data[leftIndex];
                    data[leftIndex] = temp;
                    maxHeapify(leftIndex);
                }
                else{
                    //go right
                    int temp = data[index];
                    data[index] = data[rightIndex];
                    data[rightIndex] = temp;
                    maxHeapify(rightIndex);
                }
            }
        }

        else{
            //check left
            if (data[leftIndex] <= data[index])return; //heap property
            int temp = data[index];
            data[index] = data[leftIndex];
            data[leftIndex] = temp;
            return;

        }
    }

    /**
     * calls masHeapify from the farthest right internal node to build a max heap from an array
     */
    public void buildMaxHeap(){
        //it must call maxHeapify() repeatedly.
        for (int i = count / 2 - 1; i > -1; i--){
            maxHeapify(i);
        }
    }

    /**
     * removes the max from the head of the array, inserts the last value in the heap, and fixes up the heap from the root
     * @return
     */
    public int removeMax(){
        //Before it returns, it fixes up the heap, as shown in the slides.
        int result = data[0];
        data[0] = data[count - 1];
        count -= 1;
        this.maxHeapify(0);
        return result;
    }

//    This method performs Heap Sort. It must call buildMaxHeap() and removeMax().
//    If count is not equal to data.length when the method begins, throw
//    IllegalArgumentException. When the method returns, count must be zero.

    /**
     * heapSort first call buildMaxHeap, then it calls removesMax, puts that value at the end of the sorted array invariant
     * and then loops until the start of the heap
     */
    public void heapSort(){
        if (count != data.length) throw new IllegalArgumentException();
        this.buildMaxHeap();
        int startOfSorted = data.length - 1;
        while(count > 0){
            int putToEnd = this.removeMax();
            data[startOfSorted] = putToEnd;
            startOfSorted -=1;
        }


    }

    public void debugDump(){
        //I encourage you to println() useful debug data.
    }


}



