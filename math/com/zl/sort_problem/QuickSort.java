package zl.study.math;import org.apache.commons.lang.ArrayUtils;

/**
 * @author zhang
 * @创建时间2017年7月20日
 * 快速排序
 * 快速排序是需要 选取一个最小的索引和一个最大的索引作为驱动条件进行排序
 */
public class QuickSort {
	private static int[] arr = new int[]{1,41,4,41,32,31,221,19,1};
	
	public static void main(String[] args) {
		new QuickSort().quickSort(arr, 0, arr.length - 1);
		System.out.println(ArrayUtils.toString(arr));
	}
	
	public void quickSort(int[] original, int left, int right) {
		int key = original[left];	//第一个为关键值
		if (left < right) {
			int low = left;
			int high = right;
			while (low < high) {
				while(low < high && original[high] >= key) {
					high-- ;	//说明不需要交换这两个值
				}
				original[low] = original[high];
				while(low < high && original[low] <= key) {
					low++ ;		//说明left比key值要小不需要交换
				}
				original[high] = original[low];
			}
			original[low] = key;
			quickSort(original, left, low - 1);
			quickSort(original, low + 1, right);
		}
		
	}
}
