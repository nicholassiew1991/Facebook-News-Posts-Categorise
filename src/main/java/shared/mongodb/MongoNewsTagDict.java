package shared.mongodb;

import com.mongodb.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import shared.models.tags.AbstractTag;
import shared.models.tags.TagPoliticsEvents;
import shared.models.tags.TagPoliticsPeoples;
import shared.models.tags.TagPoliticsTerms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 20/10/2016.
 */
public class MongoNewsTagDict {

	private static MongoNewsTagDict instance = null;

	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new ClassPathResource("spring-config.xml").getPath());

	private MongoOperations newsTagDict = (MongoOperations) context.getBean("newsTagDict");

	private static final String COLLECTION_POLITICS_EVENTS = "politics_events";
	private static final String COLLECTION_POLITICS_PEOPLES = "politics_peoples";
	private static final String COLLECTION_POLITICS_TERMS = "politics_terms";

	private MongoNewsTagDict() {
	}

	public static MongoNewsTagDict getInstance() {
		if (instance == null) {
			instance = new MongoNewsTagDict();
		}
		return instance;
	}

	public TagPoliticsPeoples findPeoples(String name) {

		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_PEOPLES);

		ArrayList<String> names = new ArrayList<>();
		BasicDBObject query = new BasicDBObject();

		names.add(name);
		query.put("values", new BasicDBObject("$in", names));
		DBObject obj = c.findOne(query);
		return newsTagDict.getConverter().read(TagPoliticsPeoples.class, obj);
	}

	public ArrayList<TagPoliticsEvents> getEventsTags(String name) {

		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_EVENTS);

		ArrayList<TagPoliticsEvents> data = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		BasicDBObject query = new BasicDBObject();

		names.add(name);
		query.put("values", new BasicDBObject("$in", names));
		DBCursor obj = c.find(query);

		for (DBObject dobj : obj) {
			TagPoliticsEvents tpe = newsTagDict.getConverter().read(TagPoliticsEvents.class, dobj);
			data.add(tpe);
		}
		return data;
	}


	public ArrayList<TagPoliticsTerms> getTermsTags(String name) {

		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_TERMS);

		ArrayList<TagPoliticsTerms> data = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		BasicDBObject query = new BasicDBObject();

		names.add(name);
		query.put("values", new BasicDBObject("$in", names));
		DBCursor obj = c.find(query);

		for (DBObject dobj : obj) {
			TagPoliticsTerms tpt = newsTagDict.getConverter().read(TagPoliticsTerms.class, dobj);
			data.add(tpt);
		}
		return data;

	}
	public void savePoliticsPeoplesTag(List<TagPoliticsPeoples> t) {
		for (TagPoliticsPeoples o : t) {
			savePoliticsPeoplesTag(o);
		}
	}

	public void savePoliticsPeoplesTag(TagPoliticsPeoples t) {

		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_PEOPLES);

		BasicDBObject dbObj = new BasicDBObject();
		newsTagDict.getConverter().write(t, dbObj);
		c.save(dbObj);
	}

	public void savePoliticsEventsTag(List<TagPoliticsEvents> t) {
		for (TagPoliticsEvents o : t) {
			savePoliticsEventsTag(o);
		}
	}

	public void savePoliticsEventsTag(TagPoliticsEvents t) {

		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_EVENTS);

		BasicDBObject dbObj = new BasicDBObject();
		newsTagDict.getConverter().write(t, dbObj);
		c.save(dbObj);
	}

	public void savePoliticsTermsTag(List<TagPoliticsTerms> t) {
		for (TagPoliticsTerms o : t) {
			savePoliticsTermsTag(o);
		}
	}

	public void savePoliticsTermsTag(TagPoliticsTerms t) {
		DBCollection c = newsTagDict.getCollection(COLLECTION_POLITICS_TERMS);
		BasicDBObject dbObj = new BasicDBObject();
		newsTagDict.getConverter().write(t, dbObj);
		c.save(dbObj);
	}
}
