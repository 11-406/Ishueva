public class Main{
    public static void main(String[] args) {
        QueueT<Integer> queue = new QueueT<>();
        queue.add(16);
        queue.add(1);
        queue.add(41);
        queue.add(13);
        queue.add(6);
        queue.add(2);
        queue.add(61);
        queue.add(15);
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove()); //exception empty
    }
}