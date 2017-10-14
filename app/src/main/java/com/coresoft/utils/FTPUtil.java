package com.coresoft.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.coresoft.base.IntentDef.FTP_STATE;

public class FTPUtil {

	private String hostName;

	private int serverPort;

	private String userName;

	private String password;

	private FTPClient ftpClient;

	public FTPUtil() {
		this.hostName = "v1h6679432.51mypc.cn";
		this.serverPort = 25910;
		this.userName = "updataid";
		this.password = "83123008";
		this.ftpClient = new FTPClient();
	}

	public void uploadSingleFile(File singleFile, String remotePath,
			UploadProgressListener listener) throws IOException {

		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;
		flag = uploadingSingle(singleFile, listener);
		if (flag) {
			listener.onUploadProgress(FTP_STATE.FTP_UPLOAD_SUCCESS, 0,
					singleFile);
		} else {
			listener.onUploadProgress(FTP_STATE.FTP_UPLOAD_FAIL, 0,
					singleFile);
		}

		this.uploadAfterOperate(listener);
	}

	public void uploadMultiFile(LinkedList<File> fileList, String remotePath,
			UploadProgressListener listener) throws IOException {

		this.uploadBeforeOperate(remotePath, listener);

		boolean flag;

		for (File singleFile : fileList) {
			flag = uploadingSingle(singleFile, listener);
			if (flag) {
				listener.onUploadProgress(FTP_STATE.FTP_UPLOAD_SUCCESS, 0,
						singleFile);
			} else {
				listener.onUploadProgress(FTP_STATE.FTP_UPLOAD_FAIL, 0,
						singleFile);
			}
		}

		this.uploadAfterOperate(listener);
	}

	private boolean uploadingSingle(File localFile,
			UploadProgressListener listener) throws IOException {
		boolean flag = true;

		BufferedInputStream buffIn = new BufferedInputStream(
				new FileInputStream(localFile));
		ProgressInputStream progressInput = new ProgressInputStream(buffIn,
				listener, localFile);
		flag = ftpClient.storeFile(localFile.getName(), progressInput);
		buffIn.close();

		return flag;
	}

	private void uploadBeforeOperate(String remotePath,
			UploadProgressListener listener) throws IOException {

		try {
			this.openConnect();
			listener.onUploadProgress(FTP_STATE.FTP_CONNECT_SUCCESSS, 0,
					null);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onUploadProgress(FTP_STATE.FTP_CONNECT_FAIL, 0, null);
			return;
		}

		ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
		ftpClient.makeDirectory(remotePath);
		ftpClient.changeWorkingDirectory(remotePath);
	}

	private void uploadAfterOperate(UploadProgressListener listener)
			throws IOException {
		this.closeConnect();
		listener.onUploadProgress(FTP_STATE.FTP_DISCONNECT_SUCCESS, 0, null);
	}

	public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
			throws Exception {

		try {
			this.openConnect();
			listener.onDownLoadProgress(FTP_STATE.FTP_CONNECT_SUCCESSS, 0, null);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onDownLoadProgress(FTP_STATE.FTP_CONNECT_FAIL, 0, null);
			return;
		}

		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDownLoadProgress(FTP_STATE.FTP_FILE_NOTEXISTS, 0, null);
			return;
		}

		File mkFile = new File(localPath);
		if (!mkFile.exists()) {
			mkFile.mkdirs();
		}

		localPath = localPath + fileName;
		long serverSize = files[0].getSize();
		File localFile = new File(localPath);
		long localSize = 0;
		if (localFile.exists()) {
			localSize = localFile.length();
			if (localSize >= serverSize) {
				File file = new File(localPath);
				file.delete();
			}
		}

		long step = serverSize / 100;
		long process = 0;
		long currentSize = 0;

		OutputStream out = new FileOutputStream(localFile, true);
		ftpClient.setRestartOffset(localSize);
		InputStream input = ftpClient.retrieveFileStream(serverPath);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = input.read(b)) != -1) {
			out.write(b, 0, length);
			currentSize = currentSize + length;
			if (currentSize / step != process) {
				process = currentSize / step;
				if (process % 5 == 0) {
					listener.onDownLoadProgress(FTP_STATE.FTP_DOWN_LOADING, process, null);
				}
			}
		}
		out.flush();
		out.close();
		input.close();
		
		if (ftpClient.completePendingCommand()) {
			listener.onDownLoadProgress(FTP_STATE.FTP_DOWN_SUCCESS, 0, new File(localPath));
		} else {
			listener.onDownLoadProgress(FTP_STATE.FTP_DOWN_FAIL, 0, null);
		}

		this.closeConnect();
		listener.onDownLoadProgress(FTP_STATE.FTP_DISCONNECT_SUCCESS, 0, null);

		return;
	}


	public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener)
			throws Exception {

		try {
			this.openConnect();
			listener.onDeleteProgress(FTP_STATE.FTP_CONNECT_SUCCESSS);
		} catch (IOException e1) {
			e1.printStackTrace();
			listener.onDeleteProgress(FTP_STATE.FTP_CONNECT_FAIL);
			return;
		}

		FTPFile[] files = ftpClient.listFiles(serverPath);
		if (files.length == 0) {
			listener.onDeleteProgress(FTP_STATE.FTP_FILE_NOTEXISTS);
			return;
		}
		
		boolean flag = true;
		flag = ftpClient.deleteFile(serverPath);
		if (flag) {
			listener.onDeleteProgress(FTP_STATE.FTP_DELETEFILE_SUCCESS);
		} else {
			listener.onDeleteProgress(FTP_STATE.FTP_DELETEFILE_FAIL);
		}
		
		this.closeConnect();
		listener.onDeleteProgress(FTP_STATE.FTP_DISCONNECT_SUCCESS);
		
		return;
	}

	public void openConnect() throws IOException {
		ftpClient.setControlEncoding("UTF-8");
		int reply;
		ftpClient.connect(hostName, serverPort);
		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		}
		ftpClient.login(userName, password);

		reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			throw new IOException("connect fail: " + reply);
		} else {
			FTPClientConfig config = new FTPClientConfig(ftpClient
					.getSystemType().split(" ")[0]);
			config.setServerLanguageCode("zh");
			ftpClient.configure(config);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
		}
	}

	public void closeConnect() throws IOException {
		if (ftpClient != null) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}

	public interface UploadProgressListener {
		public void onUploadProgress(String currentStep, long uploadSize, File file);
	}


	public interface DownLoadProgressListener {
		public void onDownLoadProgress(String currentStep, long downProcess, File file);
	}

	public interface DeleteFileProgressListener {
		public void onDeleteProgress(String currentStep);
	}

}

