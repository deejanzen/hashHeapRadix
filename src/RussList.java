public class RussList {
    private String key;
    private Object value;
    private RussList next;


    public RussList(String key, Object value, RussList next) {
        this.key = key;
        this.value = value;
        this.next = next;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public RussList getNext() {
        return next;
    }

    public void setNext(RussList next) {
        this.next = next;
    }

}



