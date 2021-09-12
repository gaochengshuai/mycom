package com.gaocs.utils;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

@Slf4j
@Data
public class SFTPUtils {

	Logger log = LoggerFactory.getLogger(this.getClass());

	private String host;// 服务器连接ip
	private String password;// 密码
	private int port = 22;// 端口号
	private ChannelSftp sftp = null;
	private Session sshSession = null;
	private String username;// 用户名

	/**
	 * 批量上传文件
	 *
	 * @param remotePath：远程保存目录
	 * @param localPath：本地上传目录(以路径符号结束)
	 * @param del：上传后是否删除本地文件
	 * @return
	 * @throws JSchException
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public boolean bacthUploadFile(String remotePath, String localPath, boolean del)
		throws Exception {
		try {
			connect();
			File file = new File(localPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().indexOf("bak") == -1) {
					if (this.uploadFile(remotePath, files[i].getName(), localPath, files[i].getName()) && del) {
						deleteFile(localPath + files[i].getName());
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("upload file is success:remotePath=" + remotePath + "and localPath=" + localPath
					+ ",file size is " + files.length);
			}
			return true;
		} finally {
			this.disconnect();
		}

	}

	/**
	 * 批量上传文件
	 *
	 * @param remotePath：远程保存目录
	 * @param localPath：本地上传目录(以路径符号结束)
	 * @param del：上传后是否删除本地文件
	 * @return
	 * @throws JSchException
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public boolean bacthUploadFiles(String remotePath, String localPath, boolean del)
		throws Exception {
		try {
			connect();
			File file = new File(localPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					if (this.upload(remotePath, files[i],sftp) && del) {
						deleteFile(localPath + files[i].getName());
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("upload file is success:remotePath=" + remotePath + "and localPath=" + localPath
					+ ",file size is " + files.length);
			}
			return true;
		} finally {
			this.disconnect();
		}

	}

	/**
	 * 批量下载文件
	 *
	 * @param remotePath：远程下载目录(以路径符号结束,可以为相对路径eg:/assess/sftp/jiesuan_2/2014/)
	 * @param localPath：本地保存目录(以路径符号结束,D:\Duansha\sftp\)
	 * @param fileFormat：下载文件格式(以特定字符开头,为空不做检验)
	 * @param fileEndFormat：下载文件格式(文件格式)
	 * @param del：下载后是否删除sftp文件
	 * @return
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public List<String> batchDownLoadFile(String remotePath, String localPath, String fileFormat, String fileEndFormat,
										  boolean del) throws SftpException, FileNotFoundException {
		List<String> filenames = new ArrayList<String>();
		try {
			// connect();
			@SuppressWarnings("rawtypes")
			Vector v = listFiles(remotePath);
			// sftp.cd(remotePath);
			if (v.size() > 0) {
				System.out.println("本次处理文件个数不为零,开始下载...fileSize=" + v.size());
				@SuppressWarnings("rawtypes")
				Iterator it = v.iterator();
				while (it.hasNext()) {
					LsEntry entry = (LsEntry) it.next();
					String filename = entry.getFilename();
					SftpATTRS attrs = entry.getAttrs();
					if (!attrs.isDir()) {
						boolean flag = false;
						String localFileName = localPath + filename;
						fileFormat = fileFormat == null ? "" : fileFormat.trim();
						fileEndFormat = fileEndFormat == null ? "" : fileEndFormat.trim();
						// 三种情况
						if (fileFormat.length() > 0 && fileEndFormat.length() > 0) {
							if (filename.startsWith(fileFormat) && filename.endsWith(fileEndFormat)) {
								flag = downloadFile(remotePath, filename, localPath, filename);
								if (flag) {
									filenames.add(localFileName);
									if (del) {
										deleteSFTP(remotePath, filename);
									}
								}
							}
						} else if (fileFormat.length() > 0 && "".equals(fileEndFormat)) {
							if (filename.startsWith(fileFormat)) {
								flag = downloadFile(remotePath, filename, localPath, filename);
								if (flag) {
									filenames.add(localFileName);
									if (del) {
										deleteSFTP(remotePath, filename);
									}
								}
							}
						} else if (fileEndFormat.length() > 0 && "".equals(fileFormat)) {
							if (filename.endsWith(fileEndFormat)) {
								flag = downloadFile(remotePath, filename, localPath, filename);
								if (flag) {
									filenames.add(localFileName);
									if (del) {
										deleteSFTP(remotePath, filename);
									}
								}
							}
						} else {
							flag = downloadFile(remotePath, filename, localPath, filename);
							if (flag) {
								filenames.add(localFileName);
								if (del) {
									deleteSFTP(remotePath, filename);
								}
							}
						}
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("download file is success:remotePath=" + remotePath + "and localPath=" + localPath
					+ ",file size is" + v.size());
			}
		} finally {
			this.disconnect();
		}
		return filenames;
	}

	/**
	 * 通过SFTP连接服务器
	 *
	 * @throws JSchException
	 */
	public void connect() throws JSchException {
		JSch jsch = new JSch();
		sshSession = jsch.getSession(username, host, port);
		if (log.isInfoEnabled()) {
			log.info("Session created.");
		}
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		if (log.isInfoEnabled()) {
			log.info("Session connected.");
		}
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		if (log.isInfoEnabled()) {
			log.info("Opening Channel.");
		}
		sftp = (ChannelSftp) channel;
		if (log.isInfoEnabled()) {
			log.info("Connected to " + host + ".");
		}
	}

	/**
	 * 创建目录
	 *
	 * @param createpath
	 * @return
	 * @throws SftpException
	 */
	public boolean createDir(String createpath) throws Exception {
		if (isDirExist(createpath)) {
			this.sftp.cd(createpath);
			return true;
		}
		String pathArry[] = createpath.split("/");
		StringBuffer filePath = new StringBuffer("/");
		for (String path : pathArry) {
			if (path.equals("")) {
				continue;
			}
			filePath.append(path + "/");
			if (isDirExist(filePath.toString())) {
				sftp.cd(filePath.toString());
			} else {
				// 建立目录
				sftp.mkdir(filePath.toString());
				// 进入并设置为当前目录
				sftp.cd(filePath.toString());
			}

		}
		this.sftp.cd(createpath);
		return true;
	}

	/**
	 * 删除本地文件
	 *
	 * @param filePath
	 * @return
	 */
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		if (!file.isFile()) {
			return false;
		}
		boolean rs = file.delete();
		if (rs && log.isInfoEnabled()) {
			log.info("delete file success from local.");
		}
		return rs;
	}

	/**
	 * 删除stfp文件
	 *
	 * @param directory：要删除文件所在目录
	 * @param deleteFile：要删除的文件
	 * @throws SftpException
	 */
	public void deleteSFTP(String directory, String deleteFile) throws SftpException {
		// sftp.cd(directory);
		sftp.rm(directory + deleteFile);
		if (log.isInfoEnabled()) {
			log.info("delete file success from sftp.");
		}
	}

	/**
	 * 关闭连接
	 */
	public void disconnect() {
		if (this.sftp != null) {
			if (this.sftp.isConnected()) {
				this.sftp.disconnect();
				if (log.isInfoEnabled()) {
					log.info("sftp is closed already");
				}
			}
		}
		if (this.sshSession != null) {
			if (this.sshSession.isConnected()) {
				this.sshSession.disconnect();
				if (log.isInfoEnabled()) {
					log.info("sshSession is closed already");
				}
			}
		}
	}

	/**
	 * 下载单个文件
	 *
	 * @param remotePath：远程下载目录(以路径符号结束)
	 * @param remoteFileName：下载文件名
	 * @param localPath：本地保存目录(以路径符号结束)
	 * @param localFileName：保存文件名
	 * @return
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName)
		throws SftpException, FileNotFoundException {
			FileOutputStream fieloutput = null;
			mkdirs(localPath + localFileName);
			log.info("===localPath:" + localPath + "=====,localFileName:{}"+localFileName+" success from sftp.");
			File file = new File(localPath + localFileName);
			fieloutput = new FileOutputStream(file);
			sftp.get(remotePath + remoteFileName, fieloutput);
			if (log.isInfoEnabled()) {
				log.info("===DownloadFile:" + remoteFileName + " success from sftp.");
			}
			return true;
		}


	/**
	 * 判断目录是否存在
	 *
	 * @param directory
	 * @return
	 * @throws SftpException
	 */
	public boolean isDirExist(String directory) throws Exception {
		SftpATTRS sftpATTRS = sftp.lstat(directory);
		return sftpATTRS.isDir();
	}

	/**
	 * 列出目录下的文件
	 *
	 * @param directory：要列出的目录
	 * @return
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public Vector listFiles(String directory) throws SftpException {
		return sftp.ls(directory);
	}

	/**
	 * 如果目录不存在就创建目录
	 *
	 * @param path
	 */
	public void mkdirs(String path) {
		File f = new File(path);

		String fs = f.getParent();

		f = new File(fs);

		if (!f.exists()) {
			f.mkdirs();
		}
	}

	/**
	 * 上传单个文件
	 *
	 * @param remotePath：远程保存目录
	 * @param remoteFileName：保存文件名
	 * @param localPath：本地上传目录(以路径符号结束)
	 * @param localFileName：上传的文件名
	 * @return
	 * @throws SftpException
	 * @throws FileNotFoundException
	 */
	public boolean uploadFile(String remotePath, String remoteFileName, String localPath, String localFileName)
		throws Exception {
		log.info("remotePath = " + remotePath + ",remoteFileName = " + remoteFileName + ",localPath = " + localPath);
		FileInputStream in = null;
//		remotePath = file/download/image/20200313/signImage/,
//			remoteFileName = 436173941867020289_006.pdf,
//			localPath = /home/kduser/loan-channel-sftp/unzip/shengyin/20200313/436173941867020289/
		try {
			createDir(remotePath);
			File file = new File(localPath + localFileName);
			in = new FileInputStream(file);
			sftp.put(in, remoteFileName);
			return true;
		} finally {
			if (in != null) {
				in.close();
			}
			sftp.disconnect();
		}
	}

	/**
	 * 上传文件
	 *
	 * @param remotePath：远程保存目录
	 * @param remoteFileName：保存文件名
	 * @param in：文件流
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFileByFileInputStream(FileInputStream in, String remoteFileName, String remotePath)
		throws Exception {
		log.info("remotePath = " + remotePath + ",remoteFileName = " + remoteFileName);
		try {
			createDir(remotePath);
			sftp.put(in, remoteFileName);
			return true;
		} finally {
			if (in != null) {
				in.close();
			}
			sftp.disconnect();
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		SFTPUtils sftp = null;
		// 本地存放地址
		String localPath = "D:/tmp/";
		// Sftp下载路径
		String sftpPath = "/u01/app/loanusr/data/out/20220321/";

		try {
			sftp = new SFTPUtils("60.60.70.30", "loanusr", "Kd$_20181210");
			sftp.connect();
			// 下载
//			sftp.downloadFile(sftpPath, "20220321.zip", localPath, "20220321.zip");
			sftp.batchDownLoadFile(sftpPath, localPath, null, "zip", false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftp.disconnect();
		}
	}

	public SFTPUtils() {
	}

	public SFTPUtils(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public SFTPUtils(String host, int port, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public SFTPUtils(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public SFTPUtils(String host, String username, int port) {
		this.host = host;
		this.username = username;
		this.port = port;
	}

	/**
	*  sftp多级目录创建  zangzd
	* @param filepath
	* @param sftp
	 * @throws Exception
	*/
	private void createDirs(String filepath, ChannelSftp sftp) throws Exception {
		String pathArry[] = filepath.split("/");
		StringBuffer filePath = new StringBuffer("/");
		for (String path : pathArry) {
			try {
				if (path.equals("")) {
					continue;
				}else {
					filePath.append(path + "/");
					sftp.cd(filePath.toString());
				}
			} catch (Exception e) {
				try {
					//创建目录+CD
					sftp.mkdir(filePath.toString());
					sftp.cd(filePath.toString());
				} catch (Exception e1) {
					log.error("SFTP服务器创建路径异常");
	    			throw new Exception("SFTP链接关闭异常",e1);
				}
			}
		}
	}
	/**
	 * 上传单个文件 zangzd
	 * @param remotePath
	 * @param remoteFileName
	 * @param localPath
	 * @param localFileName
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFileSingle(String remotePath, String remoteFileName, String localPath, String localFileName)
			throws Exception {
			FileInputStream in = null;
			try {
				createDirs(remotePath,sftp);
				File file = new File(localPath + localFileName);
				in = new FileInputStream(file);
				sftp.put(in, remoteFileName);
				return true;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}

	/**
	 * 批量上传文件 （包含上传文件夹中还有文件夹继续上传功能，目前只支持文件夹中有一个文件夹的上传）zangzd
	 *
	 * @param remotePath：远程保存目录
	 * @param localPath：本地上传目录(以路径符号结束)
	 * @param del：上传后是否删除本地文件
	 * @return
	 * @throws Exception
	 */
	public boolean bacthUploadFileManual(String remotePath, String localPath, boolean del)
		throws Exception {
		try {
			connect();
			File file = new File(localPath);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().indexOf("bak") == -1) {
					if (this.uploadFile(remotePath, files[i].getName(), localPath, files[i].getName()) && del) {
						deleteFile(localPath + files[i].getName());
					}
				}else if(files[i].isDirectory() && files[i].getName().indexOf("bak") == -1) {//如果是文件夹则在sftp上进行创建路径，然后进行上传文件夹中的文件
		            File[] filesDir = files[i].listFiles();
		            if (filesDir == null || filesDir.length <= 0) {
		                return false;
		            }
		            //如果是文件夹且不为空则将里面文件进行在sftp创建路径且上传文件夹中的文件。、、、、
		            for (File f : filesDir) {
		                //String fp = f.getAbsolutePath();
		                if (f.isDirectory()) { //如果文件夹中还有文件，

		                } else {
		                	createDirs(remotePath + files[i].getName() + "/", sftp);
		                	if (this.uploadFile(remotePath + files[i].getName() + "/", f.getName(), localPath + files[i].getName() + "/", f.getName()) && del) {
								deleteFile(localPath + files[i].getName() + "/" + f.getName());
							}
		                }
		            }
				}
			}
			if (log.isInfoEnabled()) {
				log.info("upload file is success:remotePath=" + remotePath + "and localPath=" + localPath
					+ ",file size is " + files.length);
			}
			return true;
		} finally {
			this.disconnect();
		}
	}

	/*从SFTP远程读取一个TXT文件，并且将文件内容String一行一行地放进LIST里面
	 * @param fileSrc 远程SFTP的文件目录
	 * @param filename 远程SFTP的文件名称
	 */
	public List<String> getListString(String fileSrc,String filename) throws Exception{
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		InputStream inputStream = null;
		ChannelSftp channel = null;
		//连接远程sftp
		channel = (ChannelSftp) sftp;
		try {
			channel.cd(fileSrc);//进入指定目录操作
			inputStream = channel.get(filename);
			br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			String tempString = null;
        while ((tempString = br.readLine()) != null) {
            //数据处理
        	log.info("读取一个txt文件的行内容======" +tempString);
        	list.add(tempString);
        }
		}catch (SftpException e) {
			log.error("远程读取sftp上的文件异常{}：" + e.getMessage(),e);
			log.error("远程读取sftp上的文件异常{}：fileSrc = " + fileSrc + ",filename = " + filename);
		}finally {
			inputStream.close();
		}
		return list;
	}

	/**
	 * 包含文件名的全路径
	 * @param path
	 * @return
	 */
	public byte[] getFileByte(String path) {
		InputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] fileByte = null;
		try {
			in = sftp.get(path);
			byte[] buffer = new byte[1024 * 12];
			int n = 0;
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}
			fileByte = out.toByteArray();
		} catch (Exception e) {
			log.error("远程读取sftp上的文件转换成字节数字异常{}：" + e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				out.close();
			} catch (Exception e) {
				log.error("流关闭异常{}：" + e.getMessage(), e);
			}
			return fileByte;
		}
	}

	/**
	 * 获取文件流
	 * @param src 文件路径
	 * @return
	 * @throws SftpException
	 */
	public InputStream get(String src) throws SftpException{
		return sftp.get(src);
	}

	/**
	 * 上传文件
	 * 注：文档服务器上路径必须存在，既directory存在
	 * @throws Exception
	 */
	public boolean upload(String directory, File uploadFile, ChannelSftp sftp) throws Exception {
		boolean f = false;
		try {
			this.createDir(directory, sftp);
			sftp.put(new FileInputStream(uploadFile), uploadFile.getName());
			f = true;
		} catch (Exception e) {
			log.error("SFTP上传失败");
			throw new Exception("上传异常!"+e.getMessage(),e);
		}
		return  f;
	}

	/**
	 * 创建目录
	 * @param createpath 远程保存目录
	 * @return
	 */
	public static boolean createDir(String createpath,ChannelSftp sftp)  {
		try {
			if (isDirExist(createpath,sftp)){  //文件存在
				sftp.cd(createpath);  //cd()更改当前远程目录
				return true;
			}
			String pathArry[] = createpath.split("/");
			for (String path : pathArry){
				if (path.equals("")){
					continue;
				}
				if (isDirExist(path.toString(),sftp)){
					sftp.cd(path.toString());
				}else{
					// 建立目录
					sftp.mkdir(path.toString()); //mkdir()创建一个新的远程目录
					// 进入并设置为当前目录
					sftp.cd(path.toString());
				}
			}
			return true;
		}
		catch (SftpException e){
			e.printStackTrace();
		}
		return false;
	}

	public void setKeyGetSftp(String path){
		JSch jsch = new JSch();
		try {
			jsch.addIdentity(path);
			sshSession = jsch.getSession(username, host, port);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(config);
			sshSession.setTimeout(20000);
			sshSession.connect();
			sftp = (ChannelSftp) sshSession.openChannel("sftp");
			sftp.connect();

			/*Vector vector = sftp.ls("/infiles/");
			for (Object obj : vector) {
				if (obj instanceof ChannelSftp.LsEntry) {
					ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) obj;
					System.out.println(entry.getFilename());
				}
			}*/
		} catch (Exception e) {
			log.error("获取连接异常：[{}]",e);
		}
	}

	/**
	 * 判断目录是否存在
	 * @param directory
	 * @return
	 */
	public static boolean isDirExist(String directory, ChannelSftp channelSftp){
		boolean isDirExistFlag = false;
		try{
			SftpATTRS sftpATTRS = channelSftp.lstat(directory);  //lstat()检索文件或目录的文件属性
			isDirExistFlag = true;
			System.out.println(sftpATTRS.isDir());
			return sftpATTRS.isDir();  //isDir()检查此文件是否为目录
		}catch (Exception e){
			if (e.getMessage().toLowerCase().equals("no such file")){
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

}
