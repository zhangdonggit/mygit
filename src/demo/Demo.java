package demo;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @Description: TODO(用一句话描述该类做什么)
 *
 * @Title: Demo.java
 * @Package demo
 * @author zhangdong
 * @date 2018年6月7日 下午10:26:47
 * @version V1.0
 */
public class Demo {
	public static void main(String[] args) {
		float f = (float) 5.5;
		byte[] array = ByteBuffer.allocate(8).putFloat(f).array();
		System.out.println(Arrays.toString(array));
		for (byte b : array) {
			System.out.println(byteToBinary(b));
		}
		System.out.println(bytesToHex(array));
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder("");
		for (int i = 0; i < bytes.length; i++) {
			int a = bytes[i] & 0xFF;
			String hexString = Integer.toHexString(a);
			if (hexString.length() < 2) {
				builder.append(0);
			}
			builder.append(hexString);
		}
		return builder.toString();
	}

	public static String byteToBinary(Byte b) {
		String result = "";
		for (int i = 0; i < 8; i++) {
			result = (b % 2) + result;
			b = (byte) (b / 2);
		}
		return result;
	}
}
