package com.androidstudy.demo.sinanews;

/**新闻信息实体类
 * @author andong
 */
public class NewsInfo {
	private String title; // 标题
	private String detail; // 详细
	private Integer comment; // 跟帖数量
	private String imageUrl; // 图片连接
	@Override
	public String toString() {
		return "NewsInfo [title=" + title + ", detail=" + detail + ", comment="
				+ comment + ", imageUrl=" + imageUrl + "]";
	}
	public NewsInfo(String title, String detail, Integer comment, String imageUrl) {
		super();
		this.title = title;
		this.detail = detail;
		this.comment = comment;
		this.imageUrl = imageUrl;
	}
	public NewsInfo() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Integer getComment() {
		return comment;
	}
	public void setComment(Integer comment) {
		this.comment = comment;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
