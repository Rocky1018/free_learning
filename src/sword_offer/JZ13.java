package sword_offer;

//输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，
// 所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
public class JZ13 {
    public void reOrderArray(int[] array) {
        int[] result = new int[array.length];
        int index = 0;
        for (int i : array) {
            if (i % 2 != 0) {
                result[index] = i;
                index++;
            }
        }
        for (int i : array) {
            if (i % 2 == 0) {
                result[index] = i;
                index++;
            }
        }
        for (int i = 0; i < result.length; i++) {
            array[i] = result[i];
        }
    }

    public static void main(String[] args) {
        int[] a = {2, 4, 6, 1, 3, 5};
        new JZ13().reOrderArray(a);
        for (int i : a) {
            System.out.println(i);
        }
    }
}
