package com.omniacom.omniapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

	private String uploadDir;

	private String uploadPic;

	private String uploadLogo;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	/**
	 * @return the uploadPic
	 */
	public String getUploadPic() {
		return uploadPic;
	}

	/**
	 * @param uploadPic
	 *            the uploadPic to set
	 */
	public void setUploadPic(String uploadPic) {
		this.uploadPic = uploadPic;
	}

	/**
	 * @return the uploadLogo
	 */
	public String getUploadLogo() {
		return uploadLogo;
	}

	/**
	 * @param uploadLogo
	 *            the uploadLogo to set
	 */
	public void setUploadLogo(String uploadLogo) {
		this.uploadLogo = uploadLogo;
	}
}
