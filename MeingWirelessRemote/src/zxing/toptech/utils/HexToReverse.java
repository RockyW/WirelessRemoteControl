package zxing.toptech.utils;

import android.util.SparseArray;

/**
 * �����ַ�����ת����16���ƣ������ַ�����ʽ���
 * 
 */
public class HexToReverse {

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	// ת��ʮ�����Ʊ���Ϊ�ַ���
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/*
	 * ��ָ���ַ���src����ÿ�����ַ��ָ�ת��Ϊ16������ʽ �磺"2B44EFD9" �C> byte[]{0x2B, 0��44, 0xEF,
	 * 0xD9}
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/*
	 * �ϲ������ֽڣ�����9��e���ϲ���9e
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	// ���ֽ�������ת���ɶ�Ӧ��16�����ַ���
	public static String printHexString(byte[] b) {
		// System.out.print(hint);
		String hexString = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString = hexString + hex;
			// System.out.print(hex.toUpperCase() + " ");
		}
		// System.out.println("");
		return hexString;

	}

	public static byte[] getHexByte(String beforeReversed) {
		String st = HexToReverse.toHexString(beforeReversed);
		byte[] finalBytes = HexToReverse.HexString2Bytes(st);
		return finalBytes;
	}

	/**
	 * �����ַ�������ת����16������ʽ���ٶ�16����ȡ�����õ�ȡ�����16�����ַ���������
	 * 
	 * @param beforeReversed
	 * @return
	 */
	public static byte[] getReversedHex(String beforeReversed) {
		// ����16�����ַ����飬�κ��ַ����������ַ���16����Ascii��ֵ������0~f���
		char[] charHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		// Map<Integer, Character> hexMap = new HashMap<Integer, Character>();
		SparseArray<Character> hexSparseArray = new SparseArray<Character>();
		for (int i = 0; i < 16; i++) {
			hexSparseArray.put(i, charHex[i]);
		}
		// �����ַ������õ���16�����ַ�����ʽ����������ַ���ab,��16�����ַ�����ʽΪ6162
		String st = HexToReverse.toHexString(beforeReversed);
		// ��������ȡ������ַ�16���ƣ����磬16����6162ȡ����Ϊ9e9d
		char[] reversedChar = new char[st.length()];
		// ��ѯ�����ַ�����ÿ���ַ�16���ƣ������浽�ַ�����reversedChar��
		for (int i = 0; i < st.length(); i++) {
			for (int j = 0; j < 16; j++) {
				if (charHex[j] == st.charAt(i)) {
					/*
					 * System.out.println("--------charHex[" + j + "]: " +
					 * charHex[j] + " and reverse : " + hexSparseArray.get(15 -
					 * j));
					 */
					reversedChar[i] = hexSparseArray.get(15 - j);
				}
			}
		}
		// ���ַ�����ת�����ַ���������ȡ�����16�����ַ�����ת�����ַ�����ʽ
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < reversedChar.length; i++) {
			sb.append(reversedChar[i]);
		}
		// System.out.println("--------reversedString is: " + sb.toString());

		// ��16�����ַ����ָ��16������ʽ�������byte������
		byte[] finalBytes = HexToReverse.HexString2Bytes(sb.toString());
		return finalBytes;
	}

	// ����ȡ�����16�����ַ�����ת����ȡ��ǰ��16����
	public static String getHexFromReversedHex(String reversedHex) {
		char[] charHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		SparseArray<Character> hexSparseArray = new SparseArray<Character>();
		for (int i = 0; i < 16; i++) {
			hexSparseArray.put(i, charHex[i]);
		}

		String st = reversedHex;
		char[] reversedChar = new char[st.length()];
		for (int i = 0; i < st.length(); i++) {
			for (int j = 0; j < 16; j++) {
				if (charHex[j] == st.charAt(i)) {
					/*
					 * System.out.println("--------charHex[" + j + "]: " +
					 * charHex[j] + " and reverse : " + hexSparseArray.get(15 -
					 * j));
					 */
					reversedChar[i] = hexSparseArray.get(15 - j);
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < reversedChar.length; i++) {
			sb.append(reversedChar[i]);
		}
		return toStringHex(sb.toString());
	}
	
	// ʮ�����Ʊ���ת����Ϊ�ַ���
	public static String HextoString(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

}
