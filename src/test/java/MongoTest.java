import org.junit.Test;
import shared.mongodb.SpringMongo;

/**
 * Created by Nicholas on 11/11/2016.
 */
public class MongoTest {

	SpringMongo sm = SpringMongo.getInstance();

	@Test
	public void aa() {
		System.out.println(sm.getNewsInfoFromDb("110699089014688_1029809123770342"));
	}
}
