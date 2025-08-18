package com.bbs.njtech.common.param;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class PageParam {

	@NotNull
	@DecimalMin(value = "1", inclusive = true)
	private Integer pageNum;

	@NotNull
	@DecimalMin(value = "1", inclusive = true)
	private Integer pageSize;

	private String propertie;

	private String direction;

}
