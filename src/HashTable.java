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
     * Inserts the key, value pair into a linked list collision strategy hash table at the end of LL.
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
     * @return
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
}//end HashTable