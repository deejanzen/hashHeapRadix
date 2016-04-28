import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by djanzen on 4/27/16.
 */
public class RadixSortTest {
    @Test
    public void sortingIndex(){
        int [] bucket = {0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0};
        RadixSort mth = new RadixSort();
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

        for (int i = 0; i < bucket.length;i++){
            System.out.println(bucket[i]);
        }

    }
    @Test
    public void TestOnePass(){
        int [] testArray = {0xd334, 0x57ef, 0xd2be, 0x81c0, 0xa80c };
        for (int i = 0; i < testArray.length;i++){
            System.out.print(testArray[i] + " ");
        }


        RadixSort mth = new RadixSort();
        mth.values = testArray;

        mth.doRadixSort_onePass(0);

        System.out.println();
        for (int i = 0; i < mth.values.length;i++){
            System.out.print(mth.values[i] + " ");
        }
        mth.doRadixSort_onePass(1);

        System.out.println();
        for (int i = 0; i < mth.values.length;i++){
            System.out.print(mth.values[i] + " ");
        }

        mth.doRadixSort_onePass(2);

        System.out.println();
        for (int i = 0; i < mth.values.length;i++){
            System.out.print(mth.values[i] + " ");
        }

        mth.doRadixSort_onePass(3);

        System.out.println();
        for (int i = 0; i < mth.values.length;i++){
            System.out.print(mth.values[i] + " ");
        }

    }

}