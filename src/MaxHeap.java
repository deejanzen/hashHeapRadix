/**
 * Created by djanzen on 4/26/16.
 */
public class MaxHeap {
    public int [] data;
    public int count;

    public MaxHeap(){

    }

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

    public void buildMaxHeap(){
        //it must call maxHeapify() repeatedly.
        for (int i = count / 2 - 1; i > -1; i--){
            maxHeapify(i);
        }
    }

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



