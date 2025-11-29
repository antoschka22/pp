import java.util.Iterator;

@ProjectClass
@Author(name = "Simon Oberdörfer")
@Invariant(condition = "size >= 0")
public class Set implements Iterable{

    private Node head;
    private int size;


    @Author(name = "Simon Oberdörfer")
    @Post(condition = "head == null && size == 0")
    public Set(){
        this.head = null;
        this.size = 0;
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "obj != null")
    @Post(condition = "contains(obj) == true")
    public void add (Object obj){
        if(contains(obj)){
            return;
        } else {
            this.head = new Node(obj, head);
            size++;
        }
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "obj != null")
    @Post(condition = "contains(obj) == false")
    public void remove (Object obj){
        // Liste ist leer
        if(this.head == null){
            return;
        }

        // zu löschendes Element ist im Kopf
        if(head.data.equals(obj)){
            this.head = this.head.next;
            size--;
            return;
        }

        Node current = this.head;
        while(current.next != null){
            if(current.next.data.equals(obj)){
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }

    @Author(name = "Simon Oberdörfer")
    @Pre(condition = "index >= 0 && index < size()")
    public Object get(int index){
        Node curr = this.head;
        int k = 0;
        while(curr != null){
            if(k == index){ return curr.data;}
            curr = curr.next;
            k++;
        }
        return null;
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "size >= 0")
    public int size() {
        return size;
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "returns true, when set is empty, false otherwise")
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "returns iterator over all elements in set != null")
    public Iterator iterator(){
        return new SetIterator(head);
    }

    @Author(name = "Simon Oberdörfer")
    @Post(condition = "returns true, when obj is in set, false otherwise")
    public boolean contains(Object obj){
        if(obj == null){
            return false;
        }
        Node current = head;
        while (current != null){
            if(current.data.equals(obj)){
                return true;
            }
            current = current.next;
        }
        return false;
    }


    @ProjectClass
    @Author(name = "Simon Oberdörfer")
    private static class Node{
        Object data;
        Node next;

        @Author(name = "Simon Oberdörfer")
        @Pre(condition = "data != null")
        private Node(Object data, Node next){
            this.data = data;
            this.next = next;
        }
    }

    @ProjectClass
    @Author(name = "Simon Oberdörfer")
    private static class SetIterator implements Iterator {
        private Node current;

        @Author(name = "Simon Oberdörfer")
        @Post(condition = "current is set to head")
        public SetIterator(Node head) {
            this.current = head;
        }

        @Author(name = "Simon Oberdörfer")
        @Post(condition = "returns true if there is a next element, false otherwise")
        public boolean hasNext() {
            return current != null;
        }

        @Author(name = "Simon Oberdörfer")
        @Post(condition = "returns the next element, null otherwise")
        public Object next() {
            if (current == null) return null;
            Object data = current.data;
            current = current.next;
            return data;
        }
    }
}
