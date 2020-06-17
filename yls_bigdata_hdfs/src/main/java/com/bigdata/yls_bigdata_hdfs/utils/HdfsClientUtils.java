package com.bigdata.yls_bigdata_hdfs.utils;

import com.bigdata.yls_bigdata_hdfs.hdfs.HdfsClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yls
 * @Description hdfs的工具类：主要提供上传、下载、文件目录递归等常用操作
 * @Date 2020/3/29 9:43
 **/
public class HdfsClientUtils {

	public static FileSystem fs = HdfsClient.fs;

	/**
	 * @Description 创建目录 支持递归创建
	 * @param path 路径地址
	 * @return 创建是否成功
	 */
	public static boolean mkdir(String path) throws Exception {
		return fs.mkdirs(new Path(path));
	}

	/**
	 * @Description 创建目录 并指定权限
	 * @param path 路径
	 */
	public static boolean mkDirWithPermission(String path) throws Exception {
		return fs.mkdirs(new Path(path), new FsPermission(FsAction.READ_WRITE, FsAction.READ, FsAction.READ));
	}

	/**
	 * @Description 创建文件并写入内容
	 * @param path 路径地址
	 * @param context 文件内容
	 */
	public static void createAndWrite(String path, String context) throws Exception {
		FSDataOutputStream out = fs.create(new Path(path));
		out.write(context.getBytes());
		out.flush();
		out.close();
	}

	/**
	 * @Description 判断文件是否存在
	 * @param path  路径地址
	 * @return boolean
	 */
	public static boolean exist(String path) throws Exception {
		return fs.exists(new Path(path));
	}

	/**
	 * @Description 文件重命名
	 * @param oldPath 旧文件路径
	 * @param newPath 新文件路径
	 * @return boolean 重命名是否成功
	 */
	public static boolean rename(String oldPath, String newPath) throws Exception {
		return fs.rename(new Path(oldPath), new Path(newPath));

	}

	/**
	 * @Description 查看文件内容
	 * @param path 路径地址
	 * @return String 返回文件内容字符串
	 */
	public static String text(String path, String encode) throws Exception {
		FSDataInputStream inputStream = fs.open(new Path(path));
		return inputStreamToString(inputStream, encode);
	}

	/**
	 * @Description 把输入流转换为指定字符
	 * @param inputStream 输入流
	 * @param encode 指定编码类型
	 * @return String
	 */
	private static String inputStreamToString(InputStream inputStream, String encode) {
		try {
			if (encode == null || ("".equals(encode))) {
				encode = "utf-8";
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encode));
			StringBuilder builder = new StringBuilder();
			String str = "";
			while ((str = reader.readLine()) != null) {
				builder.append(str).append("\n");
			}
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Description 拷贝文件到HDFS
	 * @param localPath 本地文件路径
	 * @param hdfsPath 存储到hdfs上的路径
	 */
	public static void copyFromLocalFile(String localPath, String hdfsPath) throws Exception {
		fs.copyFromLocalFile(new Path(localPath), new Path(hdfsPath));
	}

	/**
	 * @Description 从HDFS下载文件
	 * @param hdfsPath 文件在hdfs上的路径
	 * @param localPath 存储到本地的路径
	 */
	public static void copyToLocalFile(String hdfsPath, String localPath) throws Exception {
		/**
		 * 第一个参数控制下载完成后是否删除源文件,默认是 true,即删除; 最后一个参数表示是否将 RawLocalFileSystem 用作本地文件系统;
		 * RawLocalFileSystem 默认为 false,通常情况下可以不设置, 但如果你在执行时候抛出 NullPointerException
		 * 异常,则代表你的文件系统与程序可能存在不兼容的情况 (window 下常见), 此时可以将 RawLocalFileSystem 设置为 true
		 */
		fs.copyToLocalFile(false, new Path(hdfsPath), new Path(localPath), true);
		// fs.copyToLocalFile(new Path(hdfsPath), new Path(localPath));
	}

	/**
	 * 
	 * @param path 待删除的文件或者文件夹
	 * @param isRecursiveDel 是否递归删除 如果 path 是一个目录且递归删除为 true, 则删除该目录及其中所有文件; 如果 path
	 *            			是一个目录但递归删除为 false,则会则抛出异常。
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean delete(String path, boolean isRecursiveDel) throws Exception {
		return fs.delete(new Path(path), isRecursiveDel);
	}

	/**
	 * @Description 查询给定路径中文件/目录的状态
	 * @param path 目录路径
	 * @return 文件信息的数组
	 */
	public static FileStatus[] listFiles(String path) throws Exception {
		return fs.listStatus(new Path(path));
	}

	/**
	 * @Description 查询给定路径中文件的状态和块位置
	 * @param path 路径可以是目录路径也可以是文件路径
	 * @return 文件信息的数组
	 */
	public static RemoteIterator<LocatedFileStatus> listFilesRecursive(String path, boolean recursive)
			throws Exception {
		return fs.listFiles(new Path(path), recursive);
	}

	/**
	 * @Description 查看文件块信息
	 * @param path 文件路径（必须是文件，不能是文件夹）
	 * @return 块信息数组
	 */
	public static BlockLocation[] getFileBlockLocations(String path) throws Exception {
		FileStatus fileStatus = fs.getFileStatus(new Path(path));
		return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
	}

	/**
	 * @Description 递归扫描hdfs的文件 递归查看当前文件目录下的文件或者文件夹名称。 注意：递归查看所有的文件
	 * @param path hdfs的文件路径
	 * @throws IOException
	 */
	public static void viewFilesFromHdfsDir(String path, List<String> fileLists, boolean isContainsFolder)
			throws Exception {
		FileStatus[] status = fs.listStatus(new Path(path));
		for (FileStatus fileStatus : status) {
			if (fileStatus.isDirectory()) {
				if (isContainsFolder) {
					fileLists.add(fileStatus.getPath().toString());
				}
				// 如果是文件夹，继续扫描
				viewFilesFromHdfsDir(fileStatus.getPath().toString(), fileLists, isContainsFolder);
			} else {
				// 如果是文件，可以开始处理
				fileLists.add(fileStatus.getPath().toString());
			}
		}
	}

	/**
	 * @Description 判断文件夹下面是否还有文件夹
	 * @param path hdfs的文件路径
	 * @return boolean
	 */
	public static boolean isContainFolders(String path) throws Exception {
		FileStatus[] status = fs.listStatus(new Path(path));
		for (FileStatus fileStatus : status) {
			// 如果该文件夹下还有文件夹。
			if (fileStatus.isDirectory()) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @Description 判断文件夹下面是否含有指定文件
	 * @param path  hdfs的文件路径
	 * @param fileFlag  匹配的规则，包含（模糊匹配）
	 * @return boolean
	 */
	public static boolean isContainNormalFile(String path, String fileFlag) throws Exception {
		FileStatus[] status = fs.listStatus(new Path(path));
		for (FileStatus fileStatus : status) {
			if (fileStatus.getPath().getName().contains(fileFlag)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @Description 递归扫描hdfs的文件夹 查看当前文件目录下的文件或者文件夹名称。 注意：递归查看文件夹，直到最后一层文件夹
	 * @param path hdfs的文件路径
	 * @throws IOException
	 */
	public static void viewEndDirFromHdfsUrl(String path, List<String> folderLists) throws Exception {
		FileStatus[] status = fs.listStatus(new Path(path));
		for (FileStatus fileStatus : status) {
			String dirPath = fileStatus.getPath().toString();
			// 当前路径如果是目录，继续递归
			if (fileStatus.isDirectory()) {
				// 判断该路径下是否还有文件夹，如果有，继续递归，如果没有就可以开始处理
				if (isContainFolders(dirPath)) {
					viewEndDirFromHdfsUrl(dirPath, folderLists);
				} else {
					// 如果该路径下不再有文件夹
					folderLists.add(dirPath);
				}
			}
		}
	}

	/**
	 * @Description 删除指定的文件名 （不做递归删除，只是删除当前文件夹下符合标准的）
	 * @param path hdfs的文件路径
	 * @param filter  删除的标准（模糊匹配，谨慎使用）
	 */
	public static void delHdfsDir(String path, String filter) throws IOException {
		FileStatus[] status = fs.listStatus(new Path(path));
		for (FileStatus fileStatus : status) {
			if (filter != null && !"".equals(filter)) {
				if (fileStatus.getPath().toString().contains(filter)) {
					if (fs.exists(fileStatus.getPath())) {
						fs.delete(fileStatus.getPath(), true);
					}
				}
			}

		}
	}

	/**
	 * @Description 获取上传进度
	 * @param localPath 本地文件地址
	 * @param hdfsPath hdfs的文件地址
	 */
	public static void copyFromLocalBigFile(String localPath, String hdfsPath) throws Exception {
		File file = new File(localPath);
		final float fileSize = file.length();
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		if (hdfsPath.trim().endsWith("/")) {
			hdfsPath = hdfsPath +  file.getName();
		}else {
			hdfsPath = hdfsPath + "/" + file.getName();
		}
		FSDataOutputStream out = fs.create(new Path(hdfsPath), new Progressable() {
			long fileCount = 0;
			public void progress() {
				fileCount++;
				// progress 方法每上传大约 64KB 的数据后就会被调用一次
				float progessCount =  (fileCount * 64 * 1024 / fileSize) * 100;
				if (progessCount > 100) {
					progessCount=100F;
				}
				System.out.println("上传进度：" + progessCount + " %");
			}
		});

		IOUtils.copyBytes(in, out, 4096);
	}


	/**
	 * @Author yls
	 * @Description 工具类测试-主方法
	 * @Date 2020/3/29 9:48
	 * @param args
	 * @return void
	 **/
	public static void main(String[] args) {
		String url = "hdfs://192.168.133.101:9000/";
		try {
			Configuration conf = new Configuration();
			// 如果这样去获取，那conf里面就可以不要配"fs.defaultFS"参数，而且，这个客户端的身份标识已经是hadoop用户
			fs = FileSystem.get(new URI(url), conf, "hadoop");
			// FileStatus[] listFiles = listFiles("/");
			// for (FileStatus fileStatus : listFiles) {
			// System.out.println(fileStatus.getPath());
			// }
			// System.out.println();
			// List<String> viewEndDirFromHdfsUrl = new ArrayList<>();
			// viewEndDirFromHdfsUrl("/yls_test", viewEndDirFromHdfsUrl);
			// for (String string : viewEndDirFromHdfsUrl) {
			// System.out.println("00:"+string);
			// }

			// boolean mkdir = mkdir("/yls_test");
			// System.out.println("创建文件夹："+mkdir);
			// boolean mkDirWithPermission = mkDirWithPermission("/yls_test_01");
			// System.out.println("创建文件夹（带权限）："+mkDirWithPermission);
			// fs = FileSystem.get(new URI(url), conf, "hadoop");

			String localPath = "E:\\workspace\\yls_hadoop_test\\hadoop_test\\hadoop_test\\test - 副本 (4).txt";
			// rename("/yls_test/hadoop_test/aa.csv", "/yls_test/aa.csv");

			String hdfsPath = "/yls_test/";
			copyFromLocalBigFile(localPath, hdfsPath);

			// delHdfsDir(hdfsPath, "副本");
			List<String> list = new ArrayList<>();
			viewFilesFromHdfsDir(hdfsPath, list, false);
			for (String string : list) {
				System.out.println("11:" + string);
			}

			// boolean exist = exist(hdfsPath);
			// System.out.println(exist);
			// System.out.println(exist(hdfsPath+"00"));
			// BlockLocation[] fileBlockLocations =
			// getFileBlockLocations(hdfsPath+"/hadoop_test/aa.csv");
			// for (BlockLocation blockLocation : fileBlockLocations) {
			// System.out.println(blockLocation);
			// }
			//
			// System.out.println("是否有文件夹："+isContainFolders(hdfsPath));
			// System.out.println("是否有文件："+isContainNormalFile(hdfsPath, "aa.csv"));
			//
			//
			// RemoteIterator<LocatedFileStatus> files = listFilesRecursive(hdfsPath, true);
			// while (files.hasNext()) {
			// System.out.println(files.next());
			//
			// }

			// copyFromLocalFile(localPath, hdfsPath);

			// createAndWrite(hdfsPath+"/aa.txt", "你好，我是杨李尚！");
			// String text = text(hdfsPath+"/aa.txt", "utf-8");
			// System.out.println(text);

			// boolean delete = delete(hdfsPath, true);
			// System.out.println("删除："+delete);

			// copyToLocalFile("/yls_test/hadoop_test", localPath);
			// delHdfsDir(hdfsPath, "副本");

			// listFiles = listFiles(hdfsPath);
			// for (FileStatus fileStatus : listFiles) {
			// System.out.println(fileStatus.getPath()+":"+fileStatus.getPermission());
			// }
			// System.out.println();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
