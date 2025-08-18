package com.bbs.njtech.setting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "system_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SystemSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 32)
	private String id;

	private String appUrl;

	private Double appVersion;

	private String appSchema;

	private String h5Gateway;

	private String localStoragePath;

	private String customerServiceUrl;

	private Date latelyUpdateTime;

	private String wxSubscribeTemplate;

	private String appAvatarUrl;//小程序的logo

	private String appName;//小程序名字

	private String appContactUsImg;//联系我们的图片

	private String appOfficialAccountImg;//公众号图片地址

	private String appGroupImg;//社群的图片地址


}
