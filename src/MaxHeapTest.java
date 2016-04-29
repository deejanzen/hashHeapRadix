import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by djanzen on 4/28/16.
 */
public class MaxHeapTest {
    @Test
    public void testMaxHeapify(){
        int [] testArray = new int [6];
        testArray[0] = 1;
        testArray[1] = 2;
        testArray[2] = 3;
        testArray[3] = 4;
        testArray[4] = 5;
//        testArray[4] = 3;
        for (int i = 0; i< testArray.length;i++){
            System.out.print(testArray[i]);
        }
        System.out.println();
        MaxHeap mh = new MaxHeap();
        mh.data = testArray;
        mh.count = 5;
        mh.maxHeapify(0);

        for (int i = 0; i< mh.count;i++){
            System.out.print(mh.data[i]);
        }
    }

    @Test
    public void testbuildMax(){
        int [] testArray = new int [6];
        testArray[0] = 1;
        testArray[1] = 2;
        testArray[2] = 3;
        testArray[3] = 4;
        testArray[4] = 5;

        MaxHeap mh = new MaxHeap();
        mh.data = testArray;
        mh.count = 5;

        mh.buildMaxHeap();

        for (int i = 0; i< mh.count;i++){
            System.out.print(mh.data[i]);
        }
    }
}