package com.hanson.core.tools;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.hanson.foundation.domain.Accessory;

/**
 * 文件操作相关的方法
 * 
 * @author hanson
 *
 */
public class FileHelper {
	
	private static final Logger log = LoggerFactory.getLogger(FileHelper.class);

	/**
	 * 将文件上传到服务器
	 * @param request
	 * @param fileField 上传文件的 表单字段
	 * @param uploadFolderPath 文件的上传目录
	 * @param saveFileName 保存文件的文件名
	 * @param extendes 文件的扩展类型
	 * @return
	 */
	public static Map saveFileToServer(HttpServletRequest request, String fileField, String uploadFolderPath,
			String saveFileName, String[] extendes) {
		MultipartRequest mrequest = (MultipartRequest) request;
		MultipartFile multipartFile = mrequest.getFile(fileField);
		log.info("文件字段【{}】不存在文件---{}", fileField, multipartFile.isEmpty());
		return saveMultipartFileToServer(request, multipartFile,  uploadFolderPath, saveFileName, extendes);
	}

	/**
	 * 将multipartFile文件上传到服务器
	 * @param multipartFile
	 * @param request
	 * @param fileField 上传文件的 表单字段
	 * @param uploadFolderPath 文件的上传目录
	 * @param saveFileName 保存文件的文件名
	 * @param extendes 文件的扩展类型
	 * @return
	 */
	public static Map saveMultipartFileToServer(HttpServletRequest request, MultipartFile multipartFile,  
			String uploadFolderPath, String saveFileName, String[] extendes) {
		// 真实的上传路径
		String realSavePath = CommUtil.getRealPath(request) + uploadFolderPath;
		System.out.println("文件上传路径：" + realSavePath);
		Map map = new HashMap();
		// 1.获取上传文件file对象
		MultipartFile file = multipartFile;
		log.info("不存在文件---{}", file.isEmpty());
		if (file != null && !file.isEmpty()) {
			// 2.获取上传文件的后缀并转化成大写
			String extend = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
					.toLowerCase();
			// 3.修改保存文件的名称,处理同名问题；
			if (StringUtils.isEmpty(saveFileName)) {
				saveFileName = UUID.randomUUID().toString() + "." + extend;
			}
			if (saveFileName.lastIndexOf(".") < 0) {
				saveFileName = saveFileName + "." + extend;
			}
			// 4.获取上传文件的大小
			float fileSize = Float.valueOf((float) file.getSize()).floatValue();
			// 5.判断上传的文件后缀是否是支持的类型
			boolean flag = true;
			if (extendes != null) {
				for (String s : extendes) {
					if (extend.toLowerCase().equals(s))
						flag = true;
				}
			}
			/** 封装上传文件的错误信息；上传正确就为空 */
			List errors = new ArrayList();
			// 目录打散
			Calendar calendar = new GregorianCalendar();
			int month = calendar.get(Calendar.MONTH) + 1;
			int week = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (flag) {
				// 6.保存上传文件到保存路径
				File path = new File(realSavePath, month + File.separator + week + File.separator + hour);
				if (!path.exists()) {
					path.mkdirs();
				}
				DataOutputStream out = null;
				InputStream is = null;
				try {
					out = new DataOutputStream(new FileOutputStream(realSavePath + File.separator + month
							+ File.separator + week + File.separator + hour + File.separator + saveFileName));
					is = file.getInputStream();
					int size = (int) fileSize;
					byte[] buffer = new byte[size];
					while (is.read(buffer) > 0)
						out.write(buffer);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException exception) {
					exception.printStackTrace();
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// 7.封装上传文件信息到map中；如果上传文件是图片，还封装图片宽和高到map中
				if (isImg(extend)) {
					File img = new File(realSavePath + File.separator + month + File.separator + week + File.separator
							+ hour + File.separator + saveFileName);
					try {
						BufferedImage bis = ImageIO.read(img);
						int w = bis.getWidth();
						int h = bis.getHeight();
						map.put("width", Integer.valueOf(w));
						map.put("height", Integer.valueOf(h));
					} catch (Exception localException) {
					}
				}
				map.put("mime", extend);
				map.put("fileName", month + "/" + week + "/" + hour + "/" + saveFileName);
				map.put("fileSize", Float.valueOf(fileSize));
				map.put("error", errors);// 上传正确就为空
				map.put("oldName", file.getOriginalFilename());
			} else {// 上传文件类型不支持，就不保存
				errors.add("不允许的扩展名");
			}
		} else {// 上传文件为空
			map.put("width", Integer.valueOf(0));
			map.put("height", Integer.valueOf(0));
			map.put("mime", "");
			map.put("fileName", "");
			map.put("fileSize", Float.valueOf(0.0F));
			map.put("oldName", "");
		}
		return map;
	}
	
	/**
	 * 将文件保存到Accessory对象中  同时上传到服务器
	 * @param request 
	 * @param uploadConfig  上传的配置项包括以下
	 * (fileField:表单的文件字段，(若fileField不为空则忽略下面的multipartFile);
	 *  multipartFile: 文件对象 (fileField和multipartFile必须有一个不为空);
	 *  uploadFolderPath:上传目录，不能空;
	 *  saveFileName:保存的文件名，为空时自动生成唯一名称;
	 *  extendes,允许的扩展名，为空时不受限制;
	 *  originalAccessory:原始的Accessory附件对象，为空时表示新增;)
	 * @return
	 */
	 public static Accessory saveFileToAcc(HttpServletRequest request, Map uploadConfig){
		String fileField = (String) uploadConfig.get("fileField");
		MultipartFile multipartFile = (MultipartFile) uploadConfig.get("multipartFile");
		String uploadFolderPath = (String) uploadConfig.get("uploadFolderPath");
		String uploadFolderWebPath = uploadFolderPath.replace("\\", "/"); // web的访问路径，只能是左斜杠/
		String saveFileName = (String) uploadConfig.get("saveFileName");
		String[] extendes = (String[]) uploadConfig.get("extendes");
		Accessory originalAccessory = (Accessory) uploadConfig.get("originalAccessory");
		Map file_info = null;
		if(StringUtils.isNotEmpty(fileField)){
			file_info = saveFileToServer(request, fileField, uploadFolderPath, saveFileName, extendes);
		}else{
			file_info = saveMultipartFileToServer(request, multipartFile, uploadFolderPath, saveFileName, extendes);
		}
		Accessory acc = originalAccessory;
		if (originalAccessory == null) { // 原本没有文件
			if (StringUtils.isNotEmpty((String) file_info.get("fileName"))) {
				acc = new Accessory();
				fileInfoToAcc(file_info, acc, uploadFolderWebPath, true);
			}
		} else { // 原本有文件
			if (StringUtils.isNotEmpty((String) file_info.get("fileName"))) { // 上传有新的文件 就需要把原来的删除
				FileHelper.deleteAcc(request, originalAccessory); // 删除原有文件
				acc = originalAccessory;
				FileHelper.fileInfoToAcc(file_info, acc, uploadFolderWebPath, true);
			}
		}
		return acc;
	}
	 

	/**
	 * @Description: 将文件信息映射给acc；
	 * @param map
	 *            存放文件信息的map
	 * @param acc
	 *            图片附件
	 */
	public static void mapToAcc(Map map, Accessory acc) {
		acc.setName(CommUtil.null2String(map.get("fileName")));
		acc.setExt(CommUtil.null2String(map.get("mime")));
		acc.setSize(CommUtil.null2Float(map.get("fileSize")));
		if (map.get("width") != null) {
			acc.setWidth(CommUtil.null2Int(map.get("width")));
		}
		if (map.get("height") != null) {
			acc.setHeight(CommUtil.null2Int(map.get("height")));
		}
	}

	/**
	 * 将文件信息存到Accessory附件对象
	 * @param map
	 * @param acc
	 * @param uploadFolderPath
	 * @param newTime
	 */
	public static void fileInfoToAcc(Map map, Accessory acc, String uploadFolderPath, boolean newTime) {
		mapToAcc(map, acc);
		acc.setPath(uploadFolderPath);
		if (newTime) {
			acc.setAddTime(new Date());
		}
	}

	/**
	 * @Description: 删除上传的文件，Accessory
	 * @param request
	 * @param acc
	 * @return
	 */
	public static boolean deleteAcc(HttpServletRequest request, Accessory acc) {
		boolean ret = true;
		if (acc != null) {
			String path = CommUtil.getRealPath(request) + acc.getPath() + File.separator + acc.getName();
			ret = deleteFile(path);
		}
		return ret;
	}
	
	public static boolean deleteAcc(ServletContext servletContext, Accessory acc) {
		boolean ret = true;
		if (acc != null) {
			String path = CommUtil.getRealPath(servletContext) + acc.getPath() + File.separator + acc.getName();
			ret = deleteFile(path);
		}
		return ret;
	}

	/**
	 * @Description: 删除文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if ((file.isFile()) && (file.exists())) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFolder(String path) {
		boolean flag = false;
		File file = new File(path);

		if (!file.exists()) {
			return flag;
		}

		if (file.isFile()) {
			return deleteFile(path);
		}
		return deleteDirectory(path);
	}

	/**
	 * 删除目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(String path) {
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File dirFile = new File(path);

		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;

		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}

		return dirFile.delete();
	}

	/**
	 * 判断是不是图片
	 * 
	 * @param extend
	 * @return
	 */
	public static boolean isImg(String extend) {
		boolean ret = false;
		List<String> list = new ArrayList<String>();
		list.add("jpg");
		list.add("jpeg");
		list.add("bmp");
		list.add("gif");
		list.add("png");
		list.add("tif");
		for (String s : list) {
			if (s.equals(extend))
				ret = true;
		}
		return ret;
	}

	/**
	 * 导出文件时，对文件名称进行处理。避免出现文件名中文乱码的问题
	 * 
	 * @param request
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String handleExportFileName(HttpServletRequest request, String fileName)
			throws UnsupportedEncodingException {
		final String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String finalFileName = "";System.out.println("userAgent:"+userAgent);
		if(StringUtils.containsAny(userAgent, new String[]{"firefox","chrome","safari"}) ){
			finalFileName = new String(fileName.getBytes(), "ISO8859-1");
		}else {
			finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
		}
		/*if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
			finalFileName = URLEncoder.encode(fileName, "UTF8");
		} else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
			finalFileName = new String(fileName.getBytes(), "ISO8859-1");
		} else {
			finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
		}*/
		return finalFileName;
	}

}
