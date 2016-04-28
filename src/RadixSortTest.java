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

}