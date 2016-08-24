package service;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import utils.Knowledge;
import utils.MessageUtil;
import utils.MySQLUtil;


/**
 * 聊天服务类
 * @author sin5
 *
 */
public class ChatService {
	/**
	 * 得到索引存储目录
	 * 
	 * @return WEB-INF/classes/index/
	 */
	public static String getIndexDir(){
		//得到.class文件所在路径(WEB-INF/classes/)
		String classpath = ChatService.class.getResource("/").getPath();
		//将classpath中的%20替换为空格
		classpath = classpath.replaceAll("%20", " ");
		//索引存储位置：WEB-INF/classes/index/
		return classpath + "index/";
	}
	
	/**
	 * 创建索引
	 */
	public static void createIndex(){
		//取得问答知识库中的所有记录
		List<Knowledge> knowledgeList = MySQLUtil.findAllKnowledge();
		Directory directory = null;
		IndexWriter indexWriter = null;
		try {
			directory = FSDirectory.open(new File(getIndexDir()));
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_46, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, iwConfig);
			Document doc = null;
			//遍历问答知识库创建索引
			for (Knowledge knowledge : knowledgeList){
				doc = new Document();
				//对question进行分词存储
				doc.add(new TextField("question", knowledge.getQuestion(), Store.YES));
				//对id、answer和category不分词存储
				doc.add(new IntField("id", knowledge.getId(), Store.YES));
				doc.add(new StringField("answer", knowledge.getAnswer(), Store.YES));
				doc.add(new IntField("category", knowledge.getCategory(), Store.YES));
				indexWriter.addDocument(doc);
			}
			indexWriter.close();
			directory.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 从索引文件中根据问题检索答案
	 * 
	 * @param content
	 * @return knowledge
	 */
	@SuppressWarnings("deprecation")
	private static Knowledge searchIndex(String content){
		Knowledge knowledge = null;
		try {
			Directory directory = FSDirectory.open(new File(getIndexDir()));
			IndexReader reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			//使用查询解析器创建Query
			QueryParser questParser = new QueryParser(Version.LUCENE_46, "question", new IKAnalyzer(true));
			Query query = questParser.parse(QueryParser.escape(content));
			//检索得分最高的文档
			TopDocs topDocs = searcher.search(query, 1);
			if (topDocs.totalHits > 0){
				knowledge = new Knowledge();
				ScoreDoc[] scoreDoc = topDocs.scoreDocs;
				for (ScoreDoc sd : scoreDoc){
					Document doc = searcher.doc(sd.doc);
					knowledge.setId(doc.getField("id").numericValue().intValue());
					knowledge.setQuestion(doc.get("question"));
					knowledge.setAnswer(doc.get("answer"));
					
					knowledge.setCategory(doc.getField("category").numericValue().intValue());
				}
			}
			reader.close();
			directory.close();
		} catch (Exception e) {
			knowledge = null;
			e.printStackTrace();
		}
		return knowledge;
	}
	
	/**
	 * 聊天方法（根据question返回answer）
	 * 
	 * @param openId 用户的OpenID
	 * @param createeTime 消息的创建时间
	 * @param question 用户上行的问题
	 * @return answer
	 */
	public static String chat(String openId, String createTime, String question){
		String answer = null;
		int chatCategory = 0;
		Knowledge knowledge = searchIndex(question);
		//找到匹配项
		if (null != knowledge){
			//笑话
			if (2 == knowledge.getCategory()){
				answer = MySQLUtil.getJoke();
				chatCategory = 2;
			}
			//上下文
			else if (3 == knowledge.getCategory()){
				//判断上一次的聊天类别
				int category = MySQLUtil.getLastCategory(openId);
				//如果是笑话，本次继续回复笑话给用户
				if (2 == category){
					answer = MySQLUtil.getJoke();
					chatCategory = 2;
				}
				else {
					answer = knowledge.getAnswer();
					chatCategory = knowledge.getCategory();
				}
			}
			//普通对话
			else {
				answer = knowledge.getAnswer();
				//如果答案为空，根据指示id从问答知识分表中随机获取一条
				if ("".equals(answer)){
					answer = MySQLUtil.getKnowledgeSub(knowledge.getId());
					chatCategory = 1;
				}
			}
		}
		//未找到匹配项
		else {
			answer = getDefaultAnswer();
			chatCategory = 0;
		}
		//保存聊天记录
		MySQLUtil.saveChatLog(openId, createTime, question, answer, chatCategory);
		return answer;
	}
	
	/**
	 * 随机获取一个默认的答案
	 * 
	 * @return
	 */
	private static String getDefaultAnswer(){
		return MessageUtil.DEFAULT_ANSWERS[getRandomNumber(MessageUtil.DEFAULT_ANSWERS.length)];
	}

	/**
	 * 随机生成0~length-1之间的某个值
	 * 
	 * @param length
	 * @return
	 */
	private static int getRandomNumber(int length) {
		Random random = new Random();
		return random.nextInt(length);
	}
}
