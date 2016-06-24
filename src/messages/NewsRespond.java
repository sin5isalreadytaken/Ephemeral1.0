package messages;

import java.util.ArrayList;

public class NewsRespond extends BaseRespond{
	private int ArticleCount;
	private ArrayList<Article> Articles;
	
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	
	public ArrayList<Article> getArticles() {
		return Articles;
	}
	public void setArticles(ArrayList<Article> articles) {
		Articles = articles;
	}

}
