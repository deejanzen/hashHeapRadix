import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;

public class Proj06Main
{
    public static void main(String[] args)
    {
        int pass  = 0;
        int count = 0;

        // the first several testcases are hard-coded here, because I
        // don't really have a good way to encode them in an
        // input language.

        // this is the dataset which we'll use in some of these tests.
        // It's random.  Since we don't have any seed-control code,
        // theres no way to repeat a random test you ran before (sorry!).
        String[] data   = new String[100];
        Object[] values = new Object[data.length];
        for (int i=0; i<data.length; i++)
        {
            // putting a prefix on the keys is a real kludge, but it
            // prevents duplicate keys quite handily.
            data  [i] = i+":"+(int)(Math.random()*1000);

            // our values are a random mix of various types.
            switch ((int)(Math.random()*4))
            {
                case 0: values[i] =             Math.random()*1000;   break;
                case 1: values[i] = (int)      (Math.random()*1000);  break;
                case 2: values[i] = (char)(int)(Math.random()*65536); break;
                case 3: values[i] = "v_"+(int) (Math.random()*1000);  break;
                default: throw new IllegalArgumentException();  // not the right exception, but oh well
            }
        }

        int[] int_data1 = new int[5 + (int)(Math.random()*40)];
        for (int i=0; i<int_data1.length; i++)
            int_data1[i] = (int)(Math.random()*1000);

        int[] int_data2 = new int[5 + (int)(Math.random()*40)];
        for (int i=0; i<int_data2.length; i++)
            int_data2[i] = (int)(Math.random()*1000);


        count++;
        if (hashTableSingleInsertTest(data, values))
            pass++;

        count++;
        if (randomHashTableTest(data, values, 1, data.length*100))
            pass++;
        count++;
        if (randomHashTableTest(data, values, 2, data.length/2))
            pass++;

        count++;
        if (radixSortChunkingTest())
            pass++;

        count++;
        if (radixSortOnePassTest())
            pass++;

        count++;
        if (randomRadixSortTest(int_data1))
            pass++;
        count++;
        if (randomRadixSortTest(int_data2))
            pass++;

        count++;
        if (maxHeapifyTest())
            pass++;

        count++;
        if (randomMaxHeapifyTest(makeElemsUnique(int_data1)))
            pass++;
        count++;
        if (randomMaxHeapifyTest(makeElemsUnique(int_data2)))
            pass++;

        count++;
        if (buildMaxHeapTest(int_data1))
            pass++;
        count++;
        if (buildMaxHeapTest(int_data2))
            pass++;

        count++;
        if (removeMaxTest(int_data1))
            pass++;
        count++;
        if (removeMaxTest(int_data2))
            pass++;

        count++;
        if (randomHeapSortTest(int_data1))
            pass++;
        count++;
        if (randomHeapSortTest(int_data2))
            pass++;


        /* ------ ALL TESTS DONE ------ */
        System.out.printf("FINAL REPORT: %d of %d testcases passed.\n",
        pass, count);
    }

    public static boolean hashTableSingleInsertTest(String[] data, Object[] values)
    {
        /* --- HASH TABLE SINGLE INSERT TEST:
         * --- Single value insertion/query in a hash table
         *
         * The first search() must work, every other search() must return
         * null.  We generate 10,000 random keys on the theory that if this
         * table *EVER* generates false positives, it would certainly do it
         * for one of these random numbers.
         */
        final int NUM_FALSE_POS_CHECKS = 10*1000;

        System.out.println("--- HASH TABLE SINGLE INSERT TEST: BEGIN ---");

        HashTable test1 = new HashTable(data.length/2);
        test1.insert(data[0], values[0]);

        Object search = test1.search(data[0]);
        if (search == null || search.equals(values[0]) == false)
        {
            System.out.println("ERROR: HASH TABLE SINGLE INSERT TEST: The value returned did not match the value inserted.");
            System.out.println("  key:   "+data[0]);
            System.out.println("  value: "+values[0]+" type: "+values[0].getClass());
            System.out.println("  returned: "+search+" type: "+search.getClass());
            test1.debugDump();
            System.out.println("--- HASH TABLE SINGLE INSERT TEST: FAIL ---");
            return false;
        }

        for (int i=0; i<NUM_FALSE_POS_CHECKS; i++)
        {
            String badKey = ((int)(Math.random()*data.length)) + ":" + (int)(Math.random()*1000);
            if (badKey.equals(data[0]))
                continue;    // don't look for this, it would work!

            Object value = test1.search(badKey);
            if (value != null)
            {
                System.out.println("ERROR: HASH TABLE SINGLE INSERT TEST: Expected.");
                System.out.println("  inserted key:   "+data[0]);
                System.out.println("  inserted value: "+values[0]+" type: "+values[0].getClass());
                System.out.println("  search key:     "+badKey);
                System.out.println("  returned value: "+value+" type: "+value.getClass());
                test1.debugDump();
                System.out.println("--- HASH TABLE SINGLE INSERT TEST: FAIL ---");
                return false;
            }
        }

        System.out.println("--- HASH TABLE SINGLE INSERT TEST: PASS ---");
        return true;
    }

    public static boolean randomHashTableTest(String[] data, Object[] values,
                                              int testNum, int tableSize)
    {
        /* --- RANDOM HASH TABLE TEST:
         * --- Full hash table insert/search
         *
         * Insert all of the keys, then search for all of the keys (in some
         * random fashion).  All searches must work.
         */
        final int NUM_CHECKS = data.length*100;

        System.out.printf("--- RANDOM HASH TABLE TEST %d: BEGIN ---\n", testNum);

        HashTable test2 = new HashTable(data.length*10);
        for (int i=0; i<data.length; i++)
            test2.insert(data[i], values[i]);

        for (int i=0; i<NUM_CHECKS; i++)
        {
            int indx = (int)(Math.random()*data.length);

            Object search = test2.search(data[indx]);
            if (search == null || search.equals(values[indx]) == false)
            {
                System.out.printf ("ERROR: RANDOM HASH TABLE TEST %d: The value returned did not match the value inserted.\n", testNum);
                System.out.println("  Check #:  "+i);
                System.out.println("  Index:    "+indx);
                System.out.println("  key:      "+data[indx]);
                System.out.println("  value:    "+values[indx]+" type: "+values[0].getClass());

                if (search != null)
                    System.out.println("  returned: "+search+" type: "+search.getClass());
                else
                    System.out.println("  returned: null");

                test2.debugDump();
                System.out.printf ("--- RANDOM HASH TABLE TEST %d: FAIL ---\n", testNum);
                return false;
            }
        }

        System.out.printf("--- RANDOM HASH TABLE TEST %d: PASS ---\n", testNum);
        return true;
    }


    public static boolean radixSortChunkingTest()
    {
        System.out.println("--- RADIX SORT CHUNKING TEST: BEGIN ---");

        int   val      = 0x12345678;
        int[] expected = {8,7,6,5,4,3,2,1};

        boolean retval = true;
        for (int i=0; i<expected.length; i++)
        {
            int bucket = RadixSort.getBucket(val, i);
            if (bucket != expected[i])
            {
                retval = false;
                System.out.printf("--- ERROR: RADIX SORT CHUNKING TEST: chunk=%d : expected=%d actual=%d ---\n", i, expected[i], bucket);
            }
        }

        if (retval)
            System.out.println("--- RADIX SORT CHUNKING TEST: PASS ---");
        else
            System.out.println("--- RADIX SORT CHUNKING TEST: FAIL ---");

        return retval;
    }

    public static boolean radixSortOnePassTest()
    {
        System.out.printf("--- RADIX SORT ONE PASS TEST: BEGIN ---\n");

        RadixSort[] sorts = new RadixSort[7];
        for (int s=0; s<sorts.length; s++)
        {
            sorts[s] = new RadixSort();
            sorts[s].values = Arrays.copyOf(rsop_data, rsop_data.length);
        }

        sorts[0].doRadixSort_onePass(0);
        sorts[1].doRadixSort_onePass(1);
        sorts[2].doRadixSort_onePass(2);
        sorts[3].doRadixSort_onePass(3);

        sorts[4].doRadixSort_onePass(0);
        sorts[4].doRadixSort_onePass(1);

        sorts[5].doRadixSort_onePass(0);
        sorts[5].doRadixSort_onePass(1);
        sorts[5].doRadixSort_onePass(2);

        sorts[6].doRadixSort_onePass(0);
        sorts[6].doRadixSort_onePass(1);
        sorts[6].doRadixSort_onePass(2);
        sorts[6].doRadixSort_onePass(3);

        int[][] compares = { rsop_data_0,  rsop_data_1,   rsop_data_2,  rsop_data_3,
        rsop_data_01, rsop_data_012, rsop_data_0123 };

        for (int s=0; s<sorts.length; s++)
        {
            for (int i=0; i<compares[s].length; i++)
            {
                if (sorts[s].values[i] != compares[s][i])
                {
                    System.out.printf ("ERROR: RADIX SORT ONE PASS TEST: Array mismatch on test %d.\n", s);
                    System.out.println("      0 - onePass[0]");
                    System.out.println("      1 - onePass[1]");
                    System.out.println("      2 - onePass[2]");
                    System.out.println("      3 - onePass[3]");
                    System.out.println("      4 - onePass[0]+onePass[1]");
                    System.out.println("      5 - onePass[0]+onePass[1]+onePass[2]");
                    System.out.println("      6 - onePass[0]+onePass[1]+onePass[2]+onePass[3]");

                    System.out.println("  Index of first mismatch: "+i);
                    System.out.printf ("  Correct value:           0x%x\n", compares[s][i]);
                    System.out.printf ("  Actual value:            0x%x\n", sorts[s].values[i]);
                    printArray_hex("  Original array contents:", rsop_data);
                    printArray_hex("  Radix Sort array:       ", sorts[s].values);
                    System.out.printf ("--- RADIX SORT ONE PASS TEST: FAIL ---\n");
                    return false;
                }
            }
        }

        System.out.printf("--- RADIX SORT ONE PASS TEST: PASS ---\n");
        return true;
    }

    public static boolean randomRadixSortTest(int[] data)
    {
        /* --- RANDOM RADIX SORT TEST:
         * --- Perform a Radix Sort on a large dataset.
         *
         * We build a new Radix Sort object, using the data we've been given,
         * and then ask the code to duplicate it.  We'll make two duplicates
         * of the input data: one for the heap, and one which we'll use Java
         * to sort with (for comparison).
         */

        System.out.printf("--- RANDOM RADIX SORT TEST: BEGIN ---\n");

        int[] copy1 = Arrays.copyOf(data, data.length);
        int[] copy2 = Arrays.copyOf(data, data.length);

        RadixSort radix = new RadixSort();
        radix.values = copy1;

        radix.doRadixSort();
        Arrays.sort(copy2);

        for (int i=0; i<data.length; i++)
        {
            if (radix.values[i] != copy2[i])
            {
                System.out.printf ("ERROR: RANDOM RADIX SORT TEST: Array mismatch.\n");
                System.out.println("  Index of first mismatch: "+i);
                System.out.printf ("  Correct value:           0x%x\n", copy2[i]);
                System.out.printf ("  Actual value:            0x%x\n", radix.values[i]);
                printArray_hex("  Original array contents:", data);
                printArray_hex("  Sorted by Java:         ", copy2);
                printArray_hex("  Radix Sort array:       ", radix.values);
                System.out.printf ("--- RANDOM RADIX SORT TEST: FAIL ---\n");
                return false;
            }
        }

        System.out.printf("--- RANDOM RADIX SORT TEST: PASS ---\n");
        return true;
    }


    public static boolean heapCheckOne(MaxHeap heap, int indx)
    {
        if (indx < 0 || indx >= heap.count)
            return false;

        int left  = 2*indx+1;
        int right = 2*indx+2;
        if (left  < heap.count && heap.data[left ] > heap.data[indx])
            return false;
        if (right < heap.count && heap.data[right] > heap.data[indx])
            return false;

        return true;
    }
    public static boolean heapCheckRange(MaxHeap heap, int start, int end_excl)
    {
        for (int i=start; i<end_excl; i++)
        {
            if (heapCheckOne(heap, i) == false)
                return false;
        }
        return true;
    }
    public static boolean heapCheckAll(MaxHeap heap)
    {
        return heapCheckRange(heap, 0, heap.count);
    }

    public static boolean maxHeapifyTest()
    {
        /* --- MAX HEAPIFY TEST:
         * --- Run a suite of standard maxHeapify() exercises.
         *
         * We have a standard dataset stored in mh_data[].  We then simulate
         * buildMaxHeap() step by step; for each step, we have a solution
         * stored in mh_sols[][].  For simplicity of writing the test,
         * mh_sols[0] represents the state after the first maxHeapify(), and
         * mh_sols[1] represents the state after the second, and so on...
         */

        System.out.printf("--- MAX HEAPIFY TEST: BEGIN ---\n");

        MaxHeap heap = new MaxHeap();
        heap.data  = Arrays.copyOf(mh_data, mh_data.length);
        heap.count = heap.data.length;

        for (int i=0; i<mh_sols.length; i++)
        {
            heap.maxHeapify((heap.count-1)/2-i);

// code to sanity check the heap solution
//            if (heapCheckRange(heap, (heap.count-1)/2-i, heap.count) == false)
//                throw new IllegalArgumentException();

            for (int c=0; c<heap.count; c++)
            {
                if (heap.data[c] != mh_sols[i][c])
                {
                    System.out.printf ("ERROR: MAX HEAPIFY TEST: Array mismatch.\n");
                    System.out.println("  Test pass:               "+i);
                    System.out.println("  Index of first mismatch: "+c);
                    System.out.println("  Correct value:           "+mh_sols[i][c]);
                    System.out.println("  Actual value:            "+heap.data[c]);
                    heap.debugDump();
                    System.out.printf ("--- MAX HEAPIFY TEST: FAIL ---\n");
                    return false;
                }
            }
        }

        System.out.printf("--- MAX HEAPIFY TEST: PASS ---\n");
        return true;
    }

    public static boolean randomMaxHeapifyTest(int[] data)
    {
        /* --- MAX HEAPIFY TEST:
         * --- Run maxHeapify() on randomly built elements in a heap.
         *
         * Starting with random data, we sort the data, and then reverse it,
         * so that it's a (trivial) max heap.  Then we randomly will reduce
         * one element, and use maxHeapify() on that element to force it
         * downwards.  We then ensure that the
         */

        System.out.printf("--- RANDOM MAX HEAPIFY TEST: BEGIN ---\n");

        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);

        int[] reversed = new int[copy.length];
        for (int i=0; i<reversed.length; i++)
            reversed[i] = copy[copy.length-1-i];

        MaxHeap heap = new MaxHeap();
        heap.data  = reversed;
        heap.count = heap.data.length;

        // sanity check!
        if (heapCheckAll(heap) == false)
            throw new IllegalArgumentException();   // if you hit this, it's probably a bug in Russ' test code.

        final int STEPS = 10*1000;
        for (int i=0; i<STEPS; i++)
        {
            // randomly select an index to reduce
            int indx = (int)(Math.random()*heap.count);

            // randomly reduce it by some non-trivial amount.
            heap.data[indx] -= (int)(Math.random()*heap.count*2);

            // fix up the heap
            heap.maxHeapify(indx);

            // is the heap back into good form?
            if (heapCheckAll(heap) == false)
            {
                System.out.printf("--- ERROR: RANDOM MAX HEAPIFY TEST: The heap is not in the proper form! ---\n");
                heap.debugDump();
                System.out.printf("--- RANDOM MAX HEAPIFY TEST: FAIL ---\n");
                return false;
            }
        }

        System.out.printf("--- RANDOM MAX HEAPIFY TEST: PASS ---\n");
        return true;
    }

    public static boolean buildMaxHeapTest(int[] data)
    {
        /* --- BUILD MAX HEAP TEST:
         * --- Run buildMaxHeap() and confirm its result.
         *
         * Run buildMaxHeap().  Then use heapCheckAll() to confirm that it is
         * correct; finally, compare a sorted version of the heap's data to
         * a sorted version of the original input, and see if they match.
         */

        System.out.printf("--- BUILD MAX HEAP TEST: BEGIN ---\n");

        MaxHeap heap = new MaxHeap();
        heap.data  = Arrays.copyOf(data, data.length);
        heap.count = heap.data.length;

        heap.buildMaxHeap();
        if (heapCheckAll(heap) == false)
        {
            System.out.printf("--- ERROR: BUILD MAX HEAP TEST: Heap was not in proper form after buildMaxHeap() ---\n");
            heap.debugDump();
            System.out.printf("--- BUILD MAX HEAP TEST: FAIL ---\n");
            return false;
        }

        int[] heapSorted = Arrays.copyOf(heap.data, heap.data.length);
        Arrays.sort(heapSorted);
        int[] origSorted = Arrays.copyOf(data, data.length);
        Arrays.sort(origSorted);
        if (Arrays.equals(heapSorted, origSorted) == false)
        {
            System.out.printf("--- ERROR: BUILD MAX HEAP TEST: Heap did not contain the correct elements ---\n");
            heap.debugDump();
            System.out.printf("--- BUILD MAX HEAP TEST: FAIL ---\n");
            return false;
        }

        System.out.printf("--- BUILD MAX HEAP TEST: PASS ---\n");
        return true;
    }

    public static boolean removeMaxTest(int[] data)
    {
        /* --- REMOVE MAX TEST:
         * --- Build a heap; then remove each element in turn.  We'll use a
         * sorted version of the input array to check each value that is
         * returned.
         */

        System.out.printf("--- REMOVE MAX TEST: BEGIN ---\n");

        MaxHeap heap = new MaxHeap();
        heap.data  = Arrays.copyOf(data, data.length);
        heap.count = heap.data.length;
        heap.buildMaxHeap();

        int[] origSorted = Arrays.copyOf(data, data.length);
        Arrays.sort(origSorted);

        for (int i=origSorted.length-1; i>=0; i--)
        {
            int val = heap.removeMax();
            if (val != origSorted[i])
            {
                System.out.printf("--- ERROR: REMOVE MAX TEST: removeMax() returned the wrong value. ---\n");
                printArray("  Original Array:         ", data);
                printArray("  Original Array (sorted):", origSorted);
                System.out.printf("  Iteration: %d\n", origSorted.length-1-i);
                System.out.printf("  Expected:  %d\n", origSorted[i]);
                System.out.printf("  Actual:    %d\n", val);
                heap.debugDump();
                System.out.printf("--- REMOVE MAX TEST: FAIL ---\n");
                return false;
            }
        }

        System.out.printf("--- REMOVE MAX TEST: PASS ---\n");
        return true;
    }

    public static boolean randomHeapSortTest(int[] data)
    {
        /* --- RANDOM HEAP SORT TEST:
         * --- Perform a Heap Sort on a large dataset.
         *
         * We build a new MaxHeap, using the data we've been given, and then
         * ask the code to sort it.  We'll make two duplicates of the
         * input data: one for the heap, and one which we'll use Java to sort
         * with (for comparison).
         */

        System.out.printf("--- RANDOM HEAP SORT TEST: BEGIN ---\n");

        int[] copy1 = Arrays.copyOf(data, data.length);
        int[] copy2 = Arrays.copyOf(data, data.length);
        Arrays.sort(copy2);

        MaxHeap heap = new MaxHeap();
        heap.data  = copy1;
        heap.count = copy1.length;

        heap.heapSort();
        if (heap.count != 0)
        {
            System.out.printf("--- ERROR: HEAP SORT TEST: Count was not zero after the sort ---\n");
            heap.debugDump();
            System.out.printf("--- BUILD MAX HEAP TEST: FAIL ---\n");
            return false;
        }

        for (int i=0; i<data.length; i++)
        {
            if (heap.data[i] != copy2[i])
            {
                System.out.printf ("ERROR: RANDOM HEAP SORT TEST: Array mismatch.\n");
                System.out.println("  Index of first mismatch: "+i);
                System.out.println("  Correct value:           "+copy2[i]);
                System.out.println("  Actual value:            "+heap.data[i]);
                heap.debugDump();
                printArray("  Original array contents:", data);
                printArray("  Sorted by Java:         ", copy2);
                printArray("  Heap:                   ", heap.data);
                System.out.printf ("--- RANDOM HEAP SORT TEST: FAIL ---\n");
                return false;
            }
        }

        System.out.printf("--- RANDOM HEAP SORT TEST: PASS ---\n");
        return true;
    }


    /* USE THIS HELPER METHOD FOR YOUR HASH TABLE */
    public static int genHash(String str)
    {
        // write the String into the digest.  Since we convert char to
        // byte here, this won't work with great accuracy for non-ASCII
        // characters.  But hey - it's a hash function!  Do we really
        // care???
        for (int i=0; i<str.length(); i++)
            md.update((byte)str.charAt(i));

        // read the digest out.  This action also resets the digest, so
        // that it's pristine for the next block to come.
        byte[] digest = md.digest();

        // construct a 64-bit hash out of the first 4 bytes of the big
        // hash value.  This is basically modulo-64-bits...although I
        // haven't checked to see if I'm pulling out the MSB or LSB.  I
        // don't really care.

        return ((((int)digest[0]) << 24) |
        (((int)digest[1]) << 16) |
        (((int)digest[2]) << 8 ) |
        ((int)digest[3])) & 0x7fffffff;
    }

    /* these are variables (and their initializers) for the helper
     * method above.  Feel free to ignore all this.
     */
    private static MessageDigest md;
    static
    {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e)
        {
            System.out.println("ERROR: Could not build Proj06Main.md");
            System.exit(-1);
        }
    }


    public static void printArray_fmt(String prefix, int[] array, String fmt)
    {
        System.out.print(prefix);
        for (int val: array)
            System.out.printf(fmt, val);
        System.out.println();
    }
    public static void printArray_hex(String prefix, int[] array)
    {
        printArray_fmt(prefix, array, " 0x%04x");
    }
    public static void printArray(String prefix, int[] array)
    {
        printArray_fmt(prefix, array, " %d");
    }

    public static int[] makeElemsUnique(int[] input)
    {
        // set to true after the first duplication.  After that, we can
        // modify in-place.
        boolean isDup = false;
        int[] retval = input;

        for (int i=0; i<input.length; i++)
        {
            for (int j=0; j<i; j++)
            {
                if (input[j] == input[i])
                {
                    // [i] is a duplicate of something earlier.  Increment it
                    // by 1 to fix that...then re-start the search for [i].
                    //
                    // If we haven't yet duplicated the array, then do so now;
                    // after that, we'll never do it again.
                    if (isDup == false)
                    {
                        retval = Arrays.copyOf(retval, retval.length);
                        isDup = true;
                    }

                    input[i]++;   // change [i]
                    j = -1;       // restart the search for duplicates of [i]
                }
            }
        }

        return retval;
    }


    /* these arrays are used as test data for the Radix Sort one pass test */
    public static final int[] rsop_data = {
    0xd334, 0x57ef, 0xd2be, 0x81c0,
    0xa80c, 0x996a, 0xa8d6, 0xaf45,
    0xbf75, 0x5a34, 0x4f9d, 0xd42d,
    0x9376, 0x21e6, 0x32f6, 0xb531,
    0x1c03, 0x6e23, 0x92a5, 0x274e,
    0x6a53, 0x1cae, 0x1085, 0x6475,
    0x57fc, 0x0ab5, 0x9874, 0x2376,
    0x88cf, 0x86b6, 0x31d3, 0x8867,
    0x6ee3, 0x0c71, 0x2c0f, 0x4a40,
    0x9bf8, 0x5201, 0x9bd9, 0x22ea,
    0x7142, 0x7f90, 0x7fa1, 0xdb47,
    0xebc3, 0x3cbf, 0xf4ce, 0xd6ed,
    0xac0b, 0x8cd8, 0x8479, 0xf9d4,
    0xf961, 0x014b, 0x0851, 0x618e,
    0x095b, 0xda2b, 0x268b, 0x1e9d,
    0x0e39, 0x0cd1, 0xea5f, 0xc3c7,
    };

    public static final int[] rsop_data_0 = {
    0x81c0, 0x4a40, 0x7f90, 0xb531,
    0x0c71, 0x5201, 0x7fa1, 0xf961,
    0x0851, 0x0cd1, 0x7142, 0x1c03,
    0x6e23, 0x6a53, 0x31d3, 0x6ee3,
    0xebc3, 0xd334, 0x5a34, 0x9874,
    0xf9d4, 0xaf45, 0xbf75, 0x92a5,
    0x1085, 0x6475, 0x0ab5, 0xa8d6,
    0x9376, 0x21e6, 0x32f6, 0x2376,
    0x86b6, 0x8867, 0xdb47, 0xc3c7,
    0x9bf8, 0x8cd8, 0x9bd9, 0x8479,
    0x0e39, 0x996a, 0x22ea, 0xac0b,
    0x014b, 0x095b, 0xda2b, 0x268b,
    0xa80c, 0x57fc, 0x4f9d, 0xd42d,
    0xd6ed, 0x1e9d, 0xd2be, 0x274e,
    0x1cae, 0xf4ce, 0x618e, 0x57ef,
    0x88cf, 0x2c0f, 0x3cbf, 0xea5f,
    };

    public static final int[] rsop_data_01 = {
    0x5201, 0x1c03, 0xac0b, 0xa80c,
    0x2c0f, 0x6e23, 0xda2b, 0xd42d,
    0xb531, 0xd334, 0x5a34, 0x0e39,
    0x4a40, 0x7142, 0xaf45, 0xdb47,
    0x014b, 0x274e, 0x0851, 0x6a53,
    0x095b, 0xea5f, 0xf961, 0x8867,
    0x996a, 0x0c71, 0x9874, 0xbf75,
    0x6475, 0x9376, 0x2376, 0x8479,
    0x1085, 0x268b, 0x618e, 0x7f90,
    0x4f9d, 0x1e9d, 0x7fa1, 0x92a5,
    0x1cae, 0x0ab5, 0x86b6, 0xd2be,
    0x3cbf, 0x81c0, 0xebc3, 0xc3c7,
    0xf4ce, 0x88cf, 0x0cd1, 0x31d3,
    0xf9d4, 0xa8d6, 0x8cd8, 0x9bd9,
    0x6ee3, 0x21e6, 0x22ea, 0xd6ed,
    0x57ef, 0x32f6, 0x9bf8, 0x57fc,
    };

    public static final int[] rsop_data_012 = {
    0x1085, 0x7142, 0x014b, 0x618e,
    0x81c0, 0x31d3, 0x21e6, 0x5201,
    0x92a5, 0xd2be, 0x22ea, 0x32f6,
    0xd334, 0x9376, 0x2376, 0xc3c7,
    0xd42d, 0x6475, 0x8479, 0xf4ce,
    0xb531, 0x268b, 0x86b6, 0xd6ed,
    0x274e, 0x57ef, 0x57fc, 0xa80c,
    0x0851, 0x8867, 0x9874, 0x88cf,
    0xa8d6, 0x095b, 0xf961, 0x996a,
    0xf9d4, 0xda2b, 0x5a34, 0x4a40,
    0x6a53, 0xea5f, 0x0ab5, 0xdb47,
    0xebc3, 0x9bd9, 0x9bf8, 0x1c03,
    0xac0b, 0x2c0f, 0x0c71, 0x1cae,
    0x3cbf, 0x0cd1, 0x8cd8, 0x6e23,
    0x0e39, 0x1e9d, 0x6ee3, 0xaf45,
    0xbf75, 0x7f90, 0x4f9d, 0x7fa1,
    };

    public static final int[] rsop_data_0123 = {
    0x014b, 0x0851, 0x095b, 0x0ab5,
    0x0c71, 0x0cd1, 0x0e39, 0x1085,
    0x1c03, 0x1cae, 0x1e9d, 0x21e6,
    0x22ea, 0x2376, 0x268b, 0x274e,
    0x2c0f, 0x31d3, 0x32f6, 0x3cbf,
    0x4a40, 0x4f9d, 0x5201, 0x57ef,
    0x57fc, 0x5a34, 0x618e, 0x6475,
    0x6a53, 0x6e23, 0x6ee3, 0x7142,
    0x7f90, 0x7fa1, 0x81c0, 0x8479,
    0x86b6, 0x8867, 0x88cf, 0x8cd8,
    0x92a5, 0x9376, 0x9874, 0x996a,
    0x9bd9, 0x9bf8, 0xa80c, 0xa8d6,
    0xac0b, 0xaf45, 0xb531, 0xbf75,
    0xc3c7, 0xd2be, 0xd334, 0xd42d,
    0xd6ed, 0xda2b, 0xdb47, 0xea5f,
    0xebc3, 0xf4ce, 0xf961, 0xf9d4,
    };

    public static final int[] rsop_data_1 = {
    0xa80c, 0x1c03, 0x2c0f, 0x5201,
    0xac0b, 0xd42d, 0x6e23, 0xda2b,
    0xd334, 0x5a34, 0xb531, 0x0e39,
    0xaf45, 0x274e, 0x4a40, 0x7142,
    0xdb47, 0x014b, 0x6a53, 0x0851,
    0x095b, 0xea5f, 0x996a, 0x8867,
    0xf961, 0xbf75, 0x9376, 0x6475,
    0x9874, 0x2376, 0x0c71, 0x8479,
    0x1085, 0x618e, 0x268b, 0x4f9d,
    0x7f90, 0x1e9d, 0x92a5, 0x1cae,
    0x7fa1, 0xd2be, 0x0ab5, 0x86b6,
    0x3cbf, 0x81c0, 0x88cf, 0xebc3,
    0xf4ce, 0xc3c7, 0xa8d6, 0x31d3,
    0x9bd9, 0x8cd8, 0xf9d4, 0x0cd1,
    0x57ef, 0x21e6, 0x6ee3, 0x22ea,
    0xd6ed, 0x32f6, 0x57fc, 0x9bf8,
    };

    public static final int[] rsop_data_2 = {
    0x1085, 0x81c0, 0x21e6, 0x31d3,
    0x7142, 0x014b, 0x618e, 0xd2be,
    0x32f6, 0x92a5, 0x5201, 0x22ea,
    0xd334, 0x9376, 0x2376, 0xc3c7,
    0xd42d, 0x6475, 0xf4ce, 0x8479,
    0xb531, 0x86b6, 0xd6ed, 0x268b,
    0x57ef, 0x274e, 0x57fc, 0xa80c,
    0xa8d6, 0x9874, 0x88cf, 0x8867,
    0x0851, 0x996a, 0xf9d4, 0xf961,
    0x095b, 0x5a34, 0x6a53, 0x0ab5,
    0x4a40, 0xda2b, 0xea5f, 0x9bf8,
    0x9bd9, 0xdb47, 0xebc3, 0x1c03,
    0x1cae, 0x0c71, 0x2c0f, 0x3cbf,
    0xac0b, 0x8cd8, 0x0cd1, 0x6e23,
    0x6ee3, 0x1e9d, 0x0e39, 0xaf45,
    0xbf75, 0x4f9d, 0x7f90, 0x7fa1,
    };

    public static final int[] rsop_data_3 = {
    0x0ab5, 0x0c71, 0x014b, 0x0851,
    0x095b, 0x0e39, 0x0cd1, 0x1c03,
    0x1cae, 0x1085, 0x1e9d, 0x21e6,
    0x274e, 0x2376, 0x2c0f, 0x22ea,
    0x268b, 0x32f6, 0x31d3, 0x3cbf,
    0x4f9d, 0x4a40, 0x57ef, 0x5a34,
    0x57fc, 0x5201, 0x6e23, 0x6a53,
    0x6475, 0x6ee3, 0x618e, 0x7142,
    0x7f90, 0x7fa1, 0x81c0, 0x88cf,
    0x86b6, 0x8867, 0x8cd8, 0x8479,
    0x996a, 0x9376, 0x92a5, 0x9874,
    0x9bf8, 0x9bd9, 0xa80c, 0xa8d6,
    0xaf45, 0xac0b, 0xbf75, 0xb531,
    0xc3c7, 0xd334, 0xd2be, 0xd42d,
    0xdb47, 0xd6ed, 0xda2b, 0xebc3,
    0xea5f, 0xf4ce, 0xf9d4, 0xf961,
    };

    private static final int[] mh_data = {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 44,  2,  4,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 46,
    34, 60, 50, 28,
    };

    private static final int[][] mh_sols = {
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 44,  2, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 46,
    34, 60, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 44, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 46,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 49, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 63, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  7, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 61,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    0,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  8,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 15,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 39,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 53, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 62,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    16, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 38, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 37, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 59, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    17, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 48, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 19,
    48, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 28,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 33, 28,
    48, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 60, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 50, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 11, 60, 28,
    48, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 57,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 45,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    18, 57, 60, 28,
    48, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 63, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 52, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 26,
    63, 57, 60, 28,
    48, 58, 59, 42,
    38, 54, 62, 39,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 22, 39,
    63, 57, 60, 28,
    48, 58, 59, 42,
    38, 54, 62, 26,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 53,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25,  9, 62, 39,
    63, 57, 60, 28,
    48, 58, 59, 42,
    38, 54, 53, 26,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    23, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    25, 42, 62, 39,
    63, 57, 60, 28,
    48, 58, 59, 23,
    38, 54, 53, 26,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 56, 37, 21,
    9, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 35,
    59, 42, 62, 39,
    63, 57, 60, 28,
    48, 58, 56, 23,
    38, 54, 53, 26,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 25, 37, 21,
    9, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55,  5, 48,
    59, 42, 62, 39,
    63, 57, 60, 28,
    35, 58, 56, 23,
    38, 54, 53, 26,
    8,  61, 52, 45,
    36, 46, 50, 19,
    20, 31, 17, 51,
    29, 25, 37, 21,
    9, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34, 2, 33, 4,
    },
    {
    10, 13, 47, 12,
    41, 55, 60, 48,
    59, 42, 62, 39,
    63, 57, 50, 28,
    35, 58, 56, 23,
    38, 54, 53, 26,
    8,  61, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 51,
    29, 25, 37, 21,
    9, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    10, 13, 47, 12,
    41, 63, 60, 48,
    59, 42, 62, 39,
    61, 57, 50, 28,
    35, 58, 56, 23,
    38, 54, 53, 26,
    8,  55, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 51,
    29, 25, 37, 21,
    9, 16, 14, 43,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    10, 13, 47, 12,
    62, 63, 60, 48,
    59, 42, 54, 39,
    61, 57, 50, 28,
    35, 58, 56, 23,
    38, 43, 53, 26,
    8,  55, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 51,
    29, 25, 37, 21,
    9, 16, 14, 41,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    10, 13, 47, 59,
    62, 63, 60, 48,
    58, 42, 54, 39,
    61, 57, 50, 28,
    35, 51, 56, 23,
    38, 43, 53, 26,
    8,  55, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 12,
    29, 25, 37, 21,
    9, 16, 14, 41,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    10, 13, 63, 59,
    62, 61, 60, 48,
    58, 42, 54, 39,
    55, 57, 50, 28,
    35, 51, 56, 23,
    38, 43, 53, 26,
    8,  47, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 12,
    29, 25, 37, 21,
    9, 16, 14, 41,
    32, 22,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    10, 62, 63, 59,
    54, 61, 60, 48,
    58, 42, 53, 39,
    55, 57, 50, 28,
    35, 51, 56, 23,
    38, 43, 22, 26,
    8,  47, 52, 45,
    36, 46, 33, 19,
    20, 31, 17, 12,
    29, 25, 37, 21,
    9, 16, 14, 41,
    32, 13,  6, 15,
    3,  0,  1, 7,
    30, 18, 49, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    {
    63, 62, 61, 59,
    54, 55, 60, 48,
    58, 42, 53, 39,
    52, 57, 50, 28,
    35, 51, 56, 23,
    38, 43, 22, 26,
    8,  47, 49, 45,
    36, 46, 33, 19,
    20, 31, 17, 12,
    29, 25, 37, 21,
    9, 16, 14, 41,
    32, 13,  6, 15,
    3,  0,  1, 7,
    30, 18, 10, 11,
    40, 27, 24, 44,
    34,  2,  5, 4,
    },
    };
}
