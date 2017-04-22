//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.omg.framework.common.utils.scp;

import com.omg.framework.common.utils.scp.ScpBean;
import com.omg.framework.common.utils.scp.ScpUserInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpUtil {
    private static Logger logger = LoggerFactory.getLogger(SftpUtil.class);

    public SftpUtil() {
    }

    public static boolean upload(ScpBean bean, String remoteFilePath, String localFilePath, boolean type) {
        if(StringUtils.isBlank(remoteFilePath)) {
            logger.warn("远程文件地址不能为空");
            return false;
        } else if(StringUtils.isBlank(localFilePath)) {
            logger.warn("本地文件地址为空");
            return false;
        } else if(!bean.validate()) {
            logger.warn("登陆远程服务器配置信息校验失败");
            return false;
        } else {
            Session session = null;
            Channel channel = null;

            label84: {
                boolean userInfo;
                try {
                    JSch e = new JSch();
                    session = e.getSession(bean.getUsername(), bean.getRemoteAddress(), bean.getRemotePort());
                    ScpUserInfo userInfo1 = new ScpUserInfo(bean.getPassword());
                    session.setUserInfo(userInfo1);
                    session.connect();
                    SftpUtil.FileProgressMonitor monitor = new SftpUtil.FileProgressMonitor();
                    channel = session.openChannel("sftp");
                    channel.connect();
                    ChannelSftp c = (ChannelSftp)channel;
                    byte mode = 0;
                    c.put(localFilePath, remoteFilePath, monitor, mode);
                    break label84;
                } catch (Exception var14) {
                    logger.warn("SFTP上传文件失败，失败信息：", var14);
                    userInfo = false;
                } finally {
                    if(session != null && session.isConnected()) {
                        session.disconnect();
                    }

                }

                return userInfo;
            }

            logger.info("SFTP文件上传完成...");
            return true;
        }
    }

    public static boolean download(ScpBean bean, String remoteFilePath, String localFilePath, boolean type) {
        if(StringUtils.isBlank(remoteFilePath)) {
            logger.warn("远程文件地址不能为空");
            return false;
        } else if(StringUtils.isBlank(localFilePath)) {
            logger.warn("本地文件地址为空");
            return false;
        } else if(!bean.validate()) {
            logger.warn("登陆远程服务器配置信息校验失败");
            return false;
        } else {
            Session session = null;
            Channel channel = null;

            label84: {
                boolean userInfo;
                try {
                    JSch e = new JSch();
                    session = e.getSession(bean.getUsername(), bean.getRemoteAddress(), bean.getRemotePort());
                    ScpUserInfo userInfo1 = new ScpUserInfo(bean.getPassword());
                    session.setUserInfo(userInfo1);
                    session.connect();
                    SftpUtil.FileProgressMonitor monitor = new SftpUtil.FileProgressMonitor();
                    channel = session.openChannel("sftp");
                    channel.connect();
                    ChannelSftp c = (ChannelSftp)channel;
                    byte mode = 0;
                    c.get(remoteFilePath, localFilePath, monitor, mode);
                    break label84;
                } catch (Exception var14) {
                    logger.warn("SFTP下载文件失败，失败信息：", var14);
                    userInfo = false;
                } finally {
                    if(session != null && session.isConnected()) {
                        session.disconnect();
                    }

                }

                return userInfo;
            }

            logger.info("SFTP文件下载完成...");
            return true;
        }
    }

    public static class FileProgressMonitor implements SftpProgressMonitor {
        long count = 0L;
        long max = 0L;
        private long percent = -1L;

        public FileProgressMonitor() {
        }

        public void init(int op, String src, String dest, long max) {
            this.max = max;
            this.count = 0L;
            this.percent = -1L;
        }

        public boolean count(long count) {
            this.count += count;
            if(this.percent >= this.count * 100L / this.max) {
                return true;
            } else {
                this.percent = this.count * 100L / this.max;
                SftpUtil.logger.info("文件传输比率：" + this.percent);
                return this.percent != 100L;
            }
        }

        public void end() {
        }
    }
}
