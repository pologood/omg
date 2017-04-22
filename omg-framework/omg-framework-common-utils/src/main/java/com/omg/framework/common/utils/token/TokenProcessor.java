package com.omg.framework.common.utils.token;

/**
 * Created by wenjing on 2017-4-22.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;

public class TokenProcessor {
    private static TokenProcessor instance = new TokenProcessor();

    protected TokenProcessor() {
    }

    public static TokenProcessor getInstance() {
        return instance;
    }

    public synchronized String generateToken(String msg, boolean timeChange) {
        try {
            long e = System.nanoTime();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            if(timeChange) {
                byte[] now = (new Long(e)).toString().getBytes();
                md.update(now);
            }

            return this.toHex(md.digest());
        } catch (NoSuchAlgorithmException var7) {
            return null;
        }
    }

    private String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);

        for(int i = 0; i < buffer.length; ++i) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(50, 100, 1L, TimeUnit.DAYS, new LinkedBlockingQueue(), new DiscardOldestPolicy());

        try {
            DataOutputStream e = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("c:\\c.txt")));
            final BufferedWriter file = new BufferedWriter(new OutputStreamWriter(e, "gbk"));

            for(int i = 0; i < 100000; ++i) {
                tpe.execute(new Runnable() {
                    public void run() {
                        try {
                            file.write(TokenProcessor.instance.generateToken("192.168.21.200", true) + "\n");
                        } catch (IOException var2) {
                            var2.printStackTrace();
                        }

                    }
                });
            }

            tpe.shutdown();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }

    }
}
