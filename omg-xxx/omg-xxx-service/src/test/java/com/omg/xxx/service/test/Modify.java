package com.omg.xxx.service.test;

/**
 * Created by wenjing on 2017-4-22.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * �滻�ļ���������ļ�������Ŀ¼���������Ŀ¼�����ļ�����ĳ���ַ�����д�������ݣ�Java����ʵ�֣�.
 *
 *ԭ�����ж�ȡԴ�ļ������ݣ�һ�߶�ȡһ��ͬʱдһ��*.tmp���ļ���
 *����ȡ�����з�������Ҫ���滻�͸�д��Ŀ�����ݡ��С�ʱ�����µ����ݡ��С��滻֮��
 *���գ�ɾ��Դ�ļ�����*.tmp���ļ�������ΪԴ�ļ����֡�
 *
 *ע�⣡���빦�������ж�ȡһ���ַ�����Ȼ������ַ������С����Ƿ����滻�����ݣ��������µ��ַ������С��滻Դ�ļ��иô������ַ������С���û�����������
 *ע�⣡�滻�ǻ��ڡ��С����������е��滻��
 *
 * */
public class Modify {

    private String path;
    private final String target;
    private final String newContent;

    public Modify(String path, String target, String newContent) {
        // ����Ŀ¼���Ӹ�Ŀ¼��ʼ�����ļ�Ŀ¼�¼���������Ŀ¼���ļ��������滻��
        this.path = path;
        // target:��Ҫ���滻����д�����ݡ�
        this.target = target;
        // newContent:��Ҫ��д������ݡ�
        this.newContent = newContent;

        operation();
    }

    private void operation() {
        File file = new File(path);
        opeationDirectory(file);
    }

    public void opeationDirectory(File dir) {

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory())
                // �����Ŀ¼����ݹ顣
                opeationDirectory(f);
            if (f.isFile())
                operationFile(f);
        }
    }

    public void operationFile(File file) {

        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));

            String filename = file.getName();
            // tmpfileΪ�����ļ�������������Ϻ���ļ���������ΪԴ�ļ����֡�
            File tmpfile = new File(file.getParentFile().getAbsolutePath()
                    + "\\" + filename + ".tmp");

            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpfile));

            boolean flag = false;
            String str = null;
            while (true) {
                str = reader.readLine();

                if (str == null)
                    break;

                if (str.contains(target)) {
                    String newLine = str.replace(target, newContent);
                    writer.write(newLine + "\n");

                    flag = true;
                } else
                    writer.write(str + "\n");
            }

            is.close();

            writer.flush();
            writer.close();

            if (flag) {
                file.delete();
                tmpfile.renameTo(new File(file.getAbsolutePath()));
            } else
                tmpfile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
