package twitter.analysis.manager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HttpConnectionManagerTest {

    @Mock
    OkHttpClient okHttpClient;

    @InjectMocks
    HttpConnectionManager httpConnectionManager;

    @Before
    public void setup() throws Exception{
        httpConnectionManager = new HttpConnectionManager();
        Field okHttpClient = httpConnectionManager.getClass().getDeclaredField("okHttpClient");
        okHttpClient.setAccessible(true);
        okHttpClient.set(httpConnectionManager, this.okHttpClient);

    }

    @Test
    public void testUrlBuilder() throws Exception{
        String expected = "https://base/path?param=%23qwerty&param2=%40nope";
        Map<String, String> queryParams = new HashMap<String, String>(){{
            put("param","#qwerty");
            put("param2","@nope");
        }};
        httpConnectionManager.buildUrl("https://base","path",queryParams);
        Field url = httpConnectionManager.getClass().getDeclaredField("url");
        url.setAccessible(true);
        String value = (String)url.get(httpConnectionManager);
        assertEquals(expected,value);
    }
}
