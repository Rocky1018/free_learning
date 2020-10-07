package sword_offer;

public class JZ6 {
    /**
     * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
     * 输入一个非递减排序的数组的一个旋转，输出旋转数组的最小元素。
     * 例如数组[3,4,5,1,2]为[1,2,3,4,5]的一个旋转，该数组的最小值为1。
     * NOTE：给出的所有元素都大于0，若数组大小为0，请返回0。
     */
    public int minNumberInRotateArray(int[] array) {
        switch (array.length) {
            case 0:
                return 0;
            case 1:
                return array[0];
            case 2:
                return array[1];
        }
        int left = 0, right = array.length - 1;
        int mid = array.length / 2;
        while (array[mid] > array[mid - 1] || array[mid] > array[mid + 1]) {
            /**
             * 这里想象成两个递增的数组连在一起，目标是找到第二个数组开头的数字
             * 第二个数组的每一个元素都是比第一个数组任意元素小的
             * 所以如果mid比0号元素还大，那肯定还在第一个数组，所以要找右半边
             */
            if (array[mid] > array[0]) {
                left = mid;
            } else {
                if (array[mid] > array[mid - 1]) {
                    right = mid;
                } else if (array[mid] > array[mid + 1]) {
                    left = mid;
                }
            }
            mid = (left + right) / 2;
        }
        return array[mid];
    }

    public static void main(String[] args) {
        int a[] = {4, 5, 6, 1, 2, 3};
        System.out.println(new JZ6().minNumberInRotateArray(a));
    }
}
