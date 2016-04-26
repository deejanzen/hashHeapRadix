public class HashTable{
    RussList [] hashTable;


    public HashTable(int sizeOfHashTable){
        if (sizeOfHashTable < 2) throw new IllegalArgumentException();
        hashTable = new RussList[sizeOfHashTable];


    }

    public void insert(String key, Object value){

    }

    public Object search(String key){
        return new Object();
    }

    public void debugDump(){

    }


}//end HashTable