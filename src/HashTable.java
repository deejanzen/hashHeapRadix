/**
 * HashTable implements a basic ht using a sha-256 based hash() function
 * @author Dustin Janzen
 */
public class HashTable{
    RussList [] hashTable;

    /**
     * HashTable constructor.
     * @param sizeOfHashTable the size of the table
     */
    public HashTable(int sizeOfHashTable){
        if (sizeOfHashTable < 2) throw new IllegalArgumentException();
        hashTable = new RussList[sizeOfHashTable];
    }

    /**
     * Inserts the key, value pair at the end of LL using a linked list collision strategy hash table.
     * @param key the key
     * @param value the matching value
     */
    public void insert(String key, Object value){
        int indexToAddObject =Proj06Main.genHash(key) % hashTable.length;

        if (hashTable[indexToAddObject] == null) {
            hashTable[indexToAddObject] = new RussList(key, value, null);
            return;
        }

        RussList current = hashTable[indexToAddObject];
        while(true){
           if (current.getNext() == null) {
               current.setNext(new RussList(key, value, null));
               return;
           }

            current = current.getNext();
        }
    }

    /**
     * Search for the key parameter. Returns Object, if found, null otherwise.
     * @param key the key for which the value you seek.
     * @return Object(may be null)
     */
    public Object search(String key){
        int indexToGetObject =Proj06Main.genHash(key) % hashTable.length;
        if (hashTable[indexToGetObject] == null) {
            return null;
        }
        RussList current = hashTable[indexToGetObject];
        while(true){
            if (current.getNext() == null) {
                if (current.getKey().equals(key)) return current.getValue();
                else                              return null;
            }
            if (current.getKey().equals(key))     return current.getValue();
            current = current.getNext();
        }

    }

    /**
     * debug println
     */
    public void debugDump(){
        System.out.println("debugDump was called");
    }

    /**
     * RussList is a simple singlely linked list
     * @author Dustin Janzen
     */
    private class RussList {
        private String key;
        private Object value;
        private RussList next;

        /**
         * RussList constructor
         * @param key the String key
         * @param value the Object value
         * @param next the pointer to next
         */
        public RussList(String key, Object value, RussList next) {
            this.key = key;
            this.value = value;
            this.next = next;

        }

        /**
         * Accessor for key
         * @return key
         */
        public String getKey() {
            return key;
        }

        /**
         * Mutator for key
         * @param key string to set
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * Accessor for value
         * @return the String value
         */
        public Object getValue() {
            return value;
        }

        /**
         * Mutator for value
         * @param value the Object
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Accessor for next
         * @return gets the pointer to next
         */
        public RussList getNext() {
            return next;
        }

        /**
         * Mutator for next
         * @param next the obj ref for next
         */
        public void setNext(RussList next) {
            this.next = next;
        }

    }//end private inner
}//end HashTable