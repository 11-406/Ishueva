import java.util.*;
import java.io.*;

public class PatienceSortCSVTester {

    // Класс для представления стопки в алгоритме Patience Sort
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

    // Модифицированный Patience Sort с подсчётом итераций
    private static class PatienceSortWithCounter {
        private static long iterationCount = 0;

        public static void resetIterationCounter() {
            iterationCount = 0;
        }

        public static long getIterationCount() {
            return iterationCount;
        }

        private static void incrementCounter() {
            iterationCount++;
        }

        public static void patienceSort(List<Integer> arr) {
            if (arr == null || arr.size() <= 1) {
                return;
            }

            List<List<Integer>> piles = new ArrayList<>();

            // Фаза создания стопок
            for (Integer num : arr) {
                incrementCounter(); // Итерация внешнего цикла

                // Бинарный поиск для определения подходящей стопки
                int left = 0;
                int right = piles.size();

                while (left < right) {
                    incrementCounter(); // Итерация бинарного поиска
                    int mid = left + (right - left) / 2;
                    if (piles.get(mid).get(piles.get(mid).size() - 1) < num) {
                        left = mid + 1;
                    } else {
                        right = mid;
                    }
                }

                if (left == piles.size()) {
                    // Создание новой стопки
                    List<Integer> newPile = new ArrayList<>();
                    newPile.add(num);
                    piles.add(newPile);
                } else {
                    // Добавление в существующую стопку
                    piles.get(left).add(num);
                }
            }

            // Фаза слияния стопок
            PriorityQueue<Pile> heap = new PriorityQueue<>();
            for (List<Integer> pile : piles) {
                incrementCounter(); // Итерация создания кучи
                heap.offer(new Pile(pile));
            }

            for (int i = 0; i < arr.size(); i++) {
                incrementCounter(); // Итерация основного цикла слияния
                Pile minPile = heap.poll();
                arr.set(i, minPile.removeTop());

                if (!minPile.isEmpty()) {
                    heap.offer(minPile);
                }
            }
        }
    }

    // Метод для проверки отсортированности массива
    private static boolean isSorted(List<Integer> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (array.get(i) > array.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        final int TEST_CASES = 100;
        final int MIN_SIZE = 100;
        final int MAX_SIZE = 10000;

        try (PrintWriter writer = new PrintWriter("PatienceSort_Full_Results.csv")) {
            // Заголовки столбцов с табуляцией
            writer.println("Test Case\tArray Size\tTime (ms)\tIterations\tCorrectly Sorted\tIs Sorted");

            Random random = new Random();

            for (int i = 1; i <= TEST_CASES; i++) {
                // Генерация случайного массива
                int size = MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE + 1);
                List<Integer> array = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    array.add(random.nextInt());
                }

                // Создание копии для проверки
                List<Integer> copy = new ArrayList<>(array);

                // Сброс счётчика итераций перед тестом
                PatienceSortWithCounter.resetIterationCounter();

                // Замер времени выполнения
                long startTime = System.currentTimeMillis();
                PatienceSortWithCounter.patienceSort(array);
                long duration = System.currentTimeMillis() - startTime;

                // Получение результатов
                long iterations = PatienceSortWithCounter.getIterationCount();
                Collections.sort(copy);
                boolean isCorrect = array.equals(copy);
                boolean isSorted = isSorted(array);

                // Запись результатов в файл
                writer.printf("%d\t%d\t%d\t%d\t%b\t%b%n",
                        i, size, duration, iterations, isCorrect, isSorted);

                // Вывод в консоль для мониторинга прогресса
                System.out.printf("Test %3d: size=%5d, time=%5dms, iterations=%7d, correct=%5b, sorted=%5b%n",
                        i, size, duration, iterations, isCorrect, isSorted);
            }

            System.out.println("Тестирование завершено. Результаты сохранены в PatienceSort_Full_Results.csv");

        } catch (IOException e) {
            System.err.println("Ошибка при записи файла:");
            e.printStackTrace();
        }
    }
}