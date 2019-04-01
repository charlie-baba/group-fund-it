/**
 * 
 */
package com.gfi.web.util;

import static com.gfi.web.util.AppConstants.HOME_DIR;

import java.io.File;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gfi.web.dao.SettingsDao;
import com.gfi.web.enums.SettingsEnum;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
@Component
public class ImageUtil {

	@Autowired
	SettingsDao settingsDao;
	
	public static File getDirectory(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			boolean created = folder.mkdirs();
			if(!created){
				log.info("Unable to create this directory -- "+ folder.getParentFile()); 
				return null;
			}
		}
		return folder;
	}
	
	public byte[] getImageFromUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		
		byte[] bytes = null;
		try {
			File image = new File(url);
			bytes = FileUtils.readFileToByteArray(image);
		} catch (Exception e) {
			log.error("Unable to retrieve file: ", e);
		}
		return bytes;
	}
	
	public String getImageStringFromUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		
		try {
			byte[] bytes = getImageFromUrl(url);
			if (bytes == null) 
				return null;
			return new String(Base64.getEncoder().encode(bytes), "UTF-8");
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	public String saveImage(MultipartFile picture, String newFileName, SettingsEnum dir) {
		try {
			final String PROFILE_PIC_DIR = System.getProperty(HOME_DIR) + settingsDao.getSetting(dir);
			String newPicName = newFileName + "." + FilenameUtils.getExtension(picture.getOriginalFilename());
			String fullPath = ImageUtil.getDirectory(PROFILE_PIC_DIR).getAbsolutePath() + "\\" +newPicName;
			FileCopyUtils.copy(picture.getBytes(), new File(fullPath));
			return fullPath;
		} catch (Exception e) {
			log.error("error while procession profile picture", e);
			return null;
		}
	}
	
	public String saveImage(MultipartFile picture, String newFileName, File dir) {
		try {
			String newPicName = newFileName + "." + FilenameUtils.getExtension(picture.getOriginalFilename());
			String fullPath = dir.getAbsolutePath() + "\\" +newPicName;
			FileCopyUtils.copy(picture.getBytes(), new File(fullPath));
			return fullPath;
		} catch (Exception e) {
			log.error("error while procession profile picture", e);
			return null;
		}
	}
	
}
