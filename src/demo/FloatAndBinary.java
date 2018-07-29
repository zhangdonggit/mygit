package demo;

/**
 * @Description: TODO(float 转换 16进制字符串) float 的 二进制 正负位（0/1）+ 8位 阶位值 +23位 尾数
 *               单精度浮点数 阶位量 01111111
 * @Title: Demo02.java
 * @Package demo
 * @author zhangdong
 * @date 2018年6月8日 上午12:32:22
 * @version V1.0
 */

public class FloatAndBinary {
	public static void main(String[] args) {
		float f = (float) 178.153;
		String ieee754Binary = FloatAndBinary.getIEEE754Binary(f);
		System.out.println(ieee754Binary);
		float ieee754Float = FloatAndBinary.getIEEE754Float(ieee754Binary);
		System.out.println(ieee754Float);
		String strToHaxStr = FloatAndBinary.strToHaxStr(ieee754Binary);
		System.out.println(strToHaxStr);
	}

	/**
	 * 
	 * @Title: strToHaxStr @Description: TODO(将32位 浮点型二进制字符串 转成 16进制字符串) @param
	 * str @return 参数说明 @return String 返回类型 @throws
	 */
	public static String strToHaxStr(String str) {
		String bytesToHex = FloatAndBinary.bytesToHex(FloatAndBinary.strToBytes(str));
		return bytesToHex;
	}

	/**
	 * 
	 * @Title: strToByte @Description: TODO(将8位二进制字符串 转成一个 byte) @param str @return
	 * 参数说明 @return byte 返回类型 @throws
	 */
	public static byte strToByte(String str) {
		byte b = 0;
		for (int i = 0; i < str.length(); i++) {
			byte num = 1;
			if (str.charAt(str.length() - 1 - i) == '1') {
				for (int j = 0; j < i; j++) {
					num = (byte) (num * 2);
				}
				b = (byte) (b + num);
			}
		}
		return b;
	}

	/**
	 * 
	 * @Title: strToBytes @Description: TODO(将32位 二进制字符串 转成一个 byte[]) @param
	 * str @return 参数说明 @return byte[] 返回类型 @throws
	 */
	public static byte[] strToBytes(String str) {
		byte[] bytes = new byte[4];
		bytes[0] = FloatAndBinary.strToByte(str.substring(0, 8));
		bytes[1] = FloatAndBinary.strToByte(str.substring(8, 16));
		bytes[2] = FloatAndBinary.strToByte(str.substring(16, 24));
		bytes[3] = FloatAndBinary.strToByte(str.substring(24, 32));
		return bytes;
	}

	/**
	 * 
	 * @Title: bytesToHex @Description: TODO(将byte数组 转成 16进制字符串) @param
	 *         bytes @return 参数说明 @return String 返回类型 @throws
	 */
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

	/**
	 * 
	 * @Title: getIEEE754Float @Description: TODO(将 浮点型二进制 字符串 转成 float) @param
	 *         binaryStr @return 参数说明 @return float 返回类型 @throws
	 */
	public static float getIEEE754Float(String binaryStr) {
		char state = binaryStr.charAt(0);
		String orderStr = binaryStr.substring(1, 9);
		String returnOrder = FloatAndBinary.returnOrder(orderStr);
		int binaryToInt = FloatAndBinary.binaryToInt(returnOrder);
		String lastStr = "1" + binaryStr.substring(9, binaryStr.lastIndexOf("1") + 1);
		String numStr = lastStr.substring(0, binaryToInt + 1);
		String floatStr = lastStr.substring(binaryToInt + 1, lastStr.length());
		float intNum = (float) FloatAndBinary.binaryToInt(numStr);
		float floatNum = (float) FloatAndBinary.binaryToFloat(floatStr);
		while (floatNum > 1) {
			floatNum = floatNum / 10;
		}
		float result = intNum + floatNum;
		if (state == '1') {
			result = result * -1;
		}
		return result;
	}

	/**
	 * 
	 * @Title: getIEEE754Binary @Description: TODO(将 float 以IEEE754 标准 转成 32位
	 *         2进制字符串) @param f @return 参数说明 @return String 返回类型 @throws
	 */
	public static String getIEEE754Binary(float f) {
		int state = 0;
		if (f < 0) {
			state = 1;
		}
		int a = (int) f;
		float f1 = f - a;
		String numStr = "";
		String floatStr = "";
		numStr = FloatAndBinary.intToBinary(a);
		int order = numStr.length() - 1;
		floatStr = FloatAndBinary.floatToBinary(f1);
		numStr = numStr.substring(1);
		String lastStr = numStr + floatStr;
		while (lastStr.length() < 23) {
			lastStr = lastStr + "0";
		}
		String orderStr = FloatAndBinary.getOrder(FloatAndBinary.intToBinary(order));
		return state + orderStr + lastStr;
	}

	/**
	 * 
	 * @Title: returnOrder @Description: TODO(通过 阶位值 计算并返回 小数点移动位数 ) @param
	 * orderStr @return 参数说明 @return String 返回类型 @throws
	 */
	public static String returnOrder(String orderStr) {
		String floatOrder = "01111111";
		char[] floatChars = floatOrder.toCharArray();
		char[] orderChars = orderStr.toCharArray();
		for (int i = floatChars.length - 1; i >= 0; i--) {
			for (int j = orderChars.length - floatChars.length + i; j >= 0; j--) {
				if (floatChars[i] == '1') {
					if (orderChars[j] == '0') {
						orderChars[j] = '1';
					} else if (orderChars[j] == '1') {
						orderChars[j] = '0';
						break;
					}
				}
			}
		}
		String string = "";
		for (char c : orderChars) {
			string = string + c;
		}

		return string;
	}

	/**
	 * 
	 * @Title: getOrder @Description: TODO(以小数点移动位数+阶位数 计算 阶位值) @param
	 * orderStr @return 参数说明 @return String 返回类型 @throws
	 */
	public static String getOrder(String orderStr) {
		String floatOrder = "01111111";
		char[] floatChars = floatOrder.toCharArray();
		char[] orderChars = orderStr.toCharArray();
		for (int i = orderChars.length - 1; i >= 0; i--) {
			for (int j = floatChars.length - orderChars.length + i; j >= 0; j--) {
				if (orderChars[i] == '1') {
					if (floatChars[j] == '1') {
						floatChars[j] = '0';
					} else if (floatChars[j] == '0') {
						floatChars[j] = '1';
						break;
					}
				}
			}
		}
		String string = "";
		for (char c : floatChars) {
			string = string + c;
		}

		return string;
	}

	/**
	 * 
	 * @Title: binaryToInt @Description: TODO(二进制 字符串 转 int) @param binary @return
	 * 参数说明 @return int 返回类型 @throws
	 */
	public static int binaryToInt(String binary) {
		int num = 0;
		if (binary.charAt(binary.length() - 1) == '1') {
			num = num + 1;
		}
		for (int i = binary.length() - 2; i >= 0; i--) {
			if (binary.charAt(i) == '1') {
				int numMultiplier = 2;
				for (int j = 1; j < binary.length() - i - 1; j++) {
					numMultiplier = numMultiplier * 2;
				}
				num = num + numMultiplier;
			}
		}
		return num;
	}

	/**
	 * 
	 * @Title: intToBinary @Description: TODO(int 转 二进制字符串) @param num @return
	 * 参数说明 @return String 返回类型 @throws
	 */
	public static String intToBinary(int num) {
		String str = "";
		for (int i = 0; i < 8; i++) {
			str = (num % 2) + str;
			num = num / 2;
		}
		return str;
	}

	/**
	 * 
	 * @Title: binaryToFloat @Description: TODO(浮点数小数位二进制字符串 转 float小数位) @param
	 * binary @return 参数说明 @return float 返回类型 @throws
	 */
	public static float binaryToFloat(String binary) {
		float lastF = (float) 0.0;
		for (int i = 1; i <= binary.length(); i++) {
			if (binary.charAt(i - 1) == '1') {
				float f = (float) 1.0;
				int num = 2;
				for (int j = 1; j < i; j++) {
					num = num * 2;
				}
				lastF = lastF + f / num;
			}
		}
		return lastF;
	}

	/**
	 * 
	 * @Title: floatToBinary @Description: TODO(float的小数位 转换成 浮点型小数位二进制字符串) @param
	 * num @return 参数说明 @return String 返回类型 @throws
	 */
	public static String floatToBinary(float num) {
		String str = "";
		int model = 1;
		while ((num - (int) num) > 0.00000000001 && num < 1000000000) {
			num = num * 10;
			model = model * 10;
		}
		int numInt = (int) num;
		int i = 0;
		// 不要求精度的时候，小数点后面的取8-10次
		while (i < 16 && numInt != 0 && (numInt - model) != 0) {
			i++;
			numInt = numInt * 2;
			if (numInt >= model) {
				str = str + "1";
				numInt = numInt - model;
			} else {
				str = str + "0";
			}
		}
		return str;
	}
}
