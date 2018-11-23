package com.dcdz.drivers.utils;

import com.hzdongcheng.components.toolkits.utils.DateUtils;

import java.io.File;

/**
 * 删除一个月之前的crash日志
 *
 * @author Peace
 * @date 2018/1/19.
 */

public class CrashLogUtil {

    private final static long retainedSpan = DateUtils.addDay(DateUtils.nowDate(), -30).getTime();

    public static int clean(String dirPath) {
        int cleanCount = 0;
        File dirFile = getFileByPath(dirPath);
        if (isDir(dirFile)) {
            File[] listFiles = dirFile.listFiles();
            for (File file : listFiles) {
                if (file.isFile() && file.lastModified() < retainedSpan && file.delete()) {
                    cleanCount++;
                }
            }
        }
        return cleanCount;
    }

    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
