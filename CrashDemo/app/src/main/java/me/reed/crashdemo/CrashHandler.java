package me.reed.crashdemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author reed on 2017/10/7
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final CrashHandler instance = new CrashHandler();

    private Thread.UncaughtExceptionHandler defaultHandler;

    private Context context;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Application context) {
        this.context = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (!handleException(e)) {
            //未人为处理， 使用系统默认的处理器处理
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(t, e);
            }
        } else {
            //已经人为处理
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 人为处理异常
     *
     * @param e 异常
     * @return true:已经处理 false:未处理
     */
    private boolean handleException(Throwable e) {
        if (e == null) {
            return false;
        }
        //收集错误信息
        Map<String, String> info = collectErrorInfo();
        //保存错误信息
        saveErrorInfo(info, e);
        return true;
    }

    /**
     * 保存错误信息
     * @param info 应用信息及设备信息
     * @param e 错误日志
     */
    private void saveErrorInfo(Map<String, String> info, Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        saveToFile(sb.toString());
    }

    /**
     * 收集设备情况
     * @return 应用信息和设备信息的集合
     */
    private Map<String, String> collectErrorInfo() {
        Map<String, String> info = new TreeMap<>();
        info.put("versionName", BuildConfig.VERSION_NAME);
        info.put("versionCode", BuildConfig.VERSION_CODE + "");
        Field[] fields = Build.class.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                info.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    /**
     * 将采集信息存入文件
     * 此处最好将错误日志的文件存储在系统为应用分配的私密目录中，
     * 这样即使用户没有给予应用读写存储卡的权限也能保证错误日志能正常保存
     * @param log 采集的信息
     */
    private void saveToFile(String log) {
        String fileName = "crash-" + format.format(new Date()) + "-" + System.currentTimeMillis() + ".log";
        File file = new File(context.getExternalFilesDir(null), fileName);
        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            writer.write(log);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
