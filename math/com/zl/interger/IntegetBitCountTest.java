package math.com.zl

/**
 * 数二进数字中有多少个1
 * 如：0b11011 则有4个
 */
public class IntegerBitCountTest {

	//最简单的一种方法，判断每一位是否都是1
	public static int naiveWay(int inputNum) {
		int count = 0;
		while(inputNum != 0) {
			count += inputNum & 1;
			inputNun >>= 1;
		}
		return count;
	}

	//第二种方法 就是 n & n - 1 直到最后 n = 0 时，计算n & n - 1的次数就是1的数量
	public static int secWay(int inputNum) {
		int count = 0;
		while(inputNum != 0) {
			inputNum &= inputNum - 1;
			count ++;
		}
		return count;
	}

	//第三种方法就是，将8bit的每一种数字中包含的1都存在一个数组中，然后对int进行4次计算 看每8个bit中含有多少个1
	public static int thridWay(int inputNum) {
		int[] lookupTable = new int[256];
		for(int i = 0; i< 256; i++) {
			lookupTable[i] = lookupTable[i >> 1] + (i & 1);
		}
		return lookupTable[inputNum& 0xff] + lookupTable[(inputNum >> 8) & 0xff] + lookupTable[(inputNum >> 16 ) & 0xff] 
				+ lookupTable[inputNum >> 24];
	}
}