package Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class NewsMessage extends BaseMessage{

	public NewsMessage(Map<String, String> requestMap,List<Articles> articles) {
		super(requestMap);
		setMsgType("news");
		this.articleCount=articles.size()+"";
		this.articles=articles;
	}
	
	@XStreamAlias("ArticleCount")
	private String articleCount;
	@XStreamAlias("Articles")
	private List<Articles> articles=new ArrayList<>();//��Ϊ������Ϣ�Ļظ�ֻ�ܻظ�һ��ͼ����Ϣ�����������������ɻظ�������Ϣ��������������
	
	public String getarticleCount() {
		return articleCount;
	}
	public void setarticleCount(String articleCount) {
		articleCount = articleCount;
	}
	public List<Articles> getarticles() {
		return articles;
	}
	public void setarticles(List<Articles> articles) {
		this.articles = articles;
	}
	
	
	
}
