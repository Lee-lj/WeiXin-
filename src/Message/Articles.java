package Message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("item")
public class Articles {
	@XStreamAlias("Title")
	private String title;
	@XStreamAlias("Description")
	private String description;
	@XStreamAlias("Picurl")
	private String picurl;
	@XStreamAlias("Url")
	private String url;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Articles(String title, String description, String picurl, String url) {
		super();
		this.title = title;
		this.description = description;
		this.picurl = picurl;
		this.url = url;
	}
	
	

}
