//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.omg.framework.common.utils.scp;

import com.omg.framework.common.utils.scp.ScpBean;
import com.omg.framework.common.utils.scp.ScpUserInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScpUtil {
  private static Logger logger = LoggerFactory.getLogger(ScpUtil.class);

  public ScpUtil() {
  }

  public static boolean upload(ScpBean bean, String remoteFilePath, FileInputStream fis) {
    if(StringUtils.isBlank(remoteFilePath)) {
      logger.warn("远程文件地址不能为空");
      return false;
    } else if(fis == null) {
      logger.warn("本地文件流对象为null");
      return false;
    } else if(!bean.validate()) {
      logger.warn("登陆远程服务器配置信息校验失败");
      return false;
    } else {
      Session session = null;
      Channel channel = null;

      boolean userInfo;
      try {
        JSch e = new JSch();
        session = e.getSession(bean.getUsername(), bean.getRemoteAddress(), bean.getRemotePort());
        ScpUserInfo userInfo1 = new ScpUserInfo(bean.getPassword());
        session.setUserInfo(userInfo1);
        session.connect();
        String e1 = "scp  -t " + remoteFilePath;
        channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(e1);
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        channel.connect();
        if(checkAck(in) != 0) {
          throw new Exception("标准输入流读取出错！");
        }

        long filesize = (long)fis.available();
        e1 = "C0644 " + filesize + " ";
        e1 = e1 + "\n";
        out.write(e1.getBytes());
        out.flush();
        if(checkAck(in) != 0) {
          throw new Exception("标准输入流读取出错！");
        }

        byte[] buf = new byte[1024];

        while(true) {
          int len = fis.read(buf, 0, buf.length);
          if(len <= 0) {
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if(checkAck(in) != 0) {
              throw new Exception("标准输入流读取出错！");
            }

            out.close();
            in.close();
            return true;
          }

          out.write(buf, 0, len);
        }
      } catch (JSchException var26) {
        logger.warn("Scp文件上传失败，JSchException：", var26.getMessage());
        userInfo = false;
      } catch (IOException var27) {
        logger.warn("Scp文件上传失败，IOException：", var27.getMessage());
        userInfo = false;
        return userInfo;
      } catch (Exception var28) {
        logger.warn("Scp文件上传失败，Exception：", var28.getMessage());
        userInfo = false;
        return userInfo;
      } finally {
        if(fis != null) {
          try {
            fis.close();
            fis = null;
          } catch (Exception var25) {
            var25.printStackTrace();
          }
        }

        if(session != null && session.isConnected()) {
          session.disconnect();
        }

      }

      return userInfo;
    }
  }

  public static boolean download(ScpBean bean, String remoteFilePath, FileOutputStream fos) {
    if(StringUtils.isBlank(remoteFilePath)) {
      logger.warn("远程文件地址不能为空");
      return false;
    } else if(fos == null) {
      logger.warn("本地文件流对象不能为null");
      return false;
    } else if(!bean.validate()) {
      logger.warn("登陆远程服务器配置信息校验失败");
      return false;
    } else {
      Session session = null;
      Channel channel = null;

      boolean userInfo;
      try {
        JSch e = new JSch();
        session = e.getSession(bean.getUsername(), bean.getRemoteAddress(), bean.getRemotePort());
        ScpUserInfo var32 = new ScpUserInfo(bean.getPassword());
        session.setUserInfo(var32);
        session.connect();
        channel = session.openChannel("exec");
        String e1 = "scp -f " + remoteFilePath;
        ((ChannelExec)channel).setCommand(e1);
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] buf = new byte[1024];
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while(true) {
          int c = checkAck(in);
          if(c != 67) {
            in.close();
            out.close();
            return true;
          }

          in.read(buf, 0, 5);

          long filesize;
          for(filesize = 0L; in.read(buf, 0, 1) >= 0 && buf[0] != 32; filesize = filesize * 10L + (long)(buf[0] - 48)) {
            ;
          }

          Object file = null;
          int foo = 0;

          while(true) {
            in.read(buf, foo, 1);
            if(buf[foo] == 10) {
              new String(buf, 0, foo);
              buf[0] = 0;
              out.write(buf, 0, 1);
              out.flush();

              do {
                if((long)buf.length < filesize) {
                  foo = buf.length;
                } else {
                  foo = (int)filesize;
                }

                foo = in.read(buf, 0, foo);
                if(foo < 0) {
                  break;
                }

                fos.write(buf, 0, foo);
                filesize -= (long)foo;
              } while(filesize != 0L);

              if(checkAck(in) != 0) {
                throw new Exception("文件流未被完整读取！");
              }

              buf[0] = 0;
              out.write(buf, 0, 1);
              out.flush();
              break;
            }

            ++foo;
          }
        }
      } catch (JSchException var28) {
        logger.warn("Scp文件下载失败，JSchException：", var28.getMessage());
        userInfo = false;
        return userInfo;
      } catch (IOException var29) {
        logger.warn("Scp文件下载失败，IOException：", var29.getMessage());
        userInfo = false;
        return userInfo;
      } catch (Exception var30) {
        logger.warn("Scp文件下载失败，Exception：", var30.getMessage());
        userInfo = false;
      } finally {
        if(fos != null) {
          try {
            fos.close();
            fos = null;
          } catch (IOException var27) {
            var27.printStackTrace();
          }
        }

        if(session != null && session.isConnected()) {
          session.disconnect();
        }

      }

      return userInfo;
    }
  }

  private static int checkAck(InputStream in) throws IOException {
    int b = in.read();
    if(b == 0) {
      return b;
    } else if(b == -1) {
      return b;
    } else {
      if(b == 1 || b == 2) {
        StringBuffer sb = new StringBuffer();

        int c;
        do {
          c = in.read();
          sb.append((char)c);
        } while(c != 10);

        if(b == 1) {
          System.out.print(sb.toString());
        }

        if(b == 2) {
          System.out.print(sb.toString());
        }
      }

      return b;
    }
  }
}
