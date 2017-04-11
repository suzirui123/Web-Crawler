import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Invoker
 *
 */
public class Spider {
	private static CloseableHttpClient htttpClient = HttpClients.createDefault();
	public static boolean downloadPages(String path) throws Exception{
		InputStream input = null;
		OutputStream output = null;
		GetMethod get
	}
}
