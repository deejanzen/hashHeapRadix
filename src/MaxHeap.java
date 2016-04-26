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
    }

    public void buildMaxHeap(){
        //it must call maxHeapify() repeatedly.
    }

    public int removeMax(){
        //Before it returns, it fixes up the heap, as shown in the slides.
        return 0;
    }

//    This method performs Heap Sort. It must call buildMaxHeap() and removeMax().
//    If count is not equal to data.length when the method begins, throw
//    IllegalArgumentException. When the method returns, count must be zero.

    public void heapSort(){

    }

    public void debugDump(){
        //I encourage you to println() useful debug data.
    }


}
