package org.rebate.json.request;

import java.util.List;

import org.rebate.json.base.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

public class UserEvaluateOrderRequest extends BaseRequest {

	/**
	 * 评分
	 */
	private Integer score;
	private String content;
	private List<MultipartFile> evaluateImage;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<MultipartFile> getEvaluateImage() {
		return evaluateImage;
	}

	public void setEvaluateImage(List<MultipartFile> evaluateImage) {
		this.evaluateImage = evaluateImage;
	}

}
