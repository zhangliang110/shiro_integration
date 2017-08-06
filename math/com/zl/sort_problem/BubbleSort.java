package zl.study.math;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author zhang
 * @创建时间2017年7月20日
 * 冒泡排序
 */
public class BubbleSort {
	private int[] arr = new int[]{1,41,4,41,32,31,221,19,21};
	
	public static void main(String[] args) {
		int[] s = new BubbleSort().bubbleSort();
		System.out.println(ArrayUtils.toString(s));
	}
	public int[] bubbleSort() {
		int[] a = arr;
		for (int i = arr.length - 1; i > 0; i--) {
			for (int j = 0; j < i ; j++) {
				if (arr[j + 1] < arr[j]) {
					int temp = arr[j + 1];
					arr[j + 1] = arr[j];
					arr[j] = temp;
				}
			}
		}
		
		return a;
	}
}
