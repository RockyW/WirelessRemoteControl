package zxing.toptech.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

public class PropertiesUtil {

	private static final String KEY = "contents";

	public static ArrayList<String> load() {
		ArrayList<String> arrayList = new ArrayList<String>();
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(getUsersFile()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = pro.getProperty(KEY);
		// String content = str.substring(1, str.length() - 1);
		Log.d("remote", "load()-->content=" + content);
		arrayList.clear();
		if (content != null && !(content.equals(""))) {
			String[] contents = Util.regexSplit("[#]", content);
			for (int i = 0; i < contents.length; i++) {
				Log.d("remote", "load()-->contents" + "[" + i + "] = "
						+ contents[i]);
				arrayList.add(contents[i]);
			}
//			arrayList = (ArrayList<String>) Arrays.asList(contents);
		}
		return arrayList;
	}

	public static void store(ArrayList<String> list) {
		if (list != null && list.size() > 0) {
			String content = listToString(list);
			Log.d("remote", "store()-->listToString(list) = "
					+ listToString(list));
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(getUsersFile()));
				properties.setProperty(KEY, content);
				properties.store(new FileOutputStream(Constants.PATH), "ALL USERS");
				Log.d("remote", "store()-->properties.getProperty(KEY)2222 = "
						+ properties.getProperty(KEY));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private static String listToString(ArrayList<String> list) {
		StringBuffer stringBuffer = new StringBuffer();
		Iterator<String> interater = list.iterator();
		while (interater.hasNext()) {
			stringBuffer.append(interater.next() + "#");
		}
		return stringBuffer.toString();
	}

	private static File getUsersFile() {
		File file = new File(Constants.PATH);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return file;
	}
}
