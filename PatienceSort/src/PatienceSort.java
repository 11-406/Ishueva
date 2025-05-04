import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class PatienceSort {
    public static void patienceSort(List<Integer> arr) {
        if (arr == null || arr.size() <= 1) {
            return;
        }

        List<List<Integer>> piles = new ArrayList<>();

        // Первый этап: раскладываем элементы в стопки с бинарным поиском
        for (Integer num : arr) {
            // Бинарный поиск подходящей стопки
            int left = 0;
            int right = piles.size();

            while (left < right) {
                int mid = left + (right - left) / 2;
                if (piles.get(mid).get(piles.get(mid).size() - 1) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            if (left == piles.size()) {
                // Создаем новую стопку
                List<Integer> newPile = new ArrayList<>();
                newPile.add(num);
                piles.add(newPile);
            } else {
                // Добавляем в существующую стопку
                piles.get(left).add(num);
            }
        }

        // Второй этап: извлекаем минимальные элементы из стопок
        PriorityQueue<Pile> heap = new PriorityQueue<>();
        for (List<Integer> pile : piles) {
            heap.offer(new Pile(pile));
        }

        // Собираем отсортированный массив
        for (int i = 0; i < arr.size(); i++) {
            Pile minPile = heap.poll();
            arr.set(i, minPile.removeTop());

            if (!minPile.isEmpty()) {
                heap.offer(minPile);
            }
        }
    }

    // Вспомогательный класс для работы с кучей
    private static class Pile implements Comparable<Pile> {
        private final List<Integer> elements;
        private int topIndex;

        public Pile(List<Integer> elements) {
            this.elements = elements;
            this.topIndex = elements.size() - 1;
        }

        public int peekTop() {
            return elements.get(topIndex);
        }

        public int removeTop() {
            return elements.get(topIndex--);
        }

        public boolean isEmpty() {
            return topIndex < 0;
        }

        @Override
        public int compareTo(Pile other) {
            return Integer.compare(this.peekTop(), other.peekTop());
        }
    }

    // Пример использования
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        Collections.addAll(numbers, 7, 3, 1, 5, 9, 2, 8, 6, 4);

        System.out.println("До сортировки: " + numbers);
        patienceSort(numbers);
        System.out.println("После сортировки: " + numbers);
    }
}