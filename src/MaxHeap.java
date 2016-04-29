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


//if (2*index +1 < data.length) left  =
//        if (2*index +2 < data.length) right = data[2*index +2];
//
//        if (data[index] < left && data[index] < right){
//            if (left - right >= 0 ){
//                //maxHeapify(2*index +1);
//                data[2 * index + 1] = data[index];
//                data[index] = left;
//                maxHeapify(2 * index + 1);
//            }
//            else{
//                //maxHeapify(2*index +2);
//                data[2 * index + 2] = data[index];
//                data[index] = right;
//                maxHeapify(2 * index + 2);
//            }
//        }


//        if (count - index == 1 || count - index == 2) return; // < 3 ??
//        if (2*index +1 == count) return;
