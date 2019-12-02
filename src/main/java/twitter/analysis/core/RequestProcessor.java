package twitter.analysis.core;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import twitter.analysis.constants.TwitterConstants;
import twitter.analysis.manager.HttpConnectionManager;
import twitter.analysis.manager.LanguageProcessor;
import twitter.analysis.model.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PropertySource("classpath:application.properties")
public class RequestProcessor {

    @Autowired
    private HttpConnectionManager httpConnectionManager;

    @Autowired
    LanguageProcessor languageProcessor;

    @Value("${twitter.access.token}")
    private String twitterAccessToken;


    public String getTweetsBlob(String expression) throws IOException {
        JSONObject response = fetchResponse(expression);
        List<Tweet> tweets = parseOutput(response);
        return createTextBlob(tweets);
    }

    public Map<String,Integer> getSentimentAnalysis(String expression) throws IOException{
        String text = getTweetsBlob(expression);
        return languageProcessor.computeSentiment(text);
    }


    private String createTextBlob(List<Tweet> tweets){
        String text = "";
        for (Tweet tweet : tweets) {
            text = text + " " + tweet.getTweet();
        }
        return text;
    }

    private List parseOutput(JSONObject response) {
        List<Tweet> tweets = new ArrayList<>();

        if (!response.has("statuses"))
            return tweets;

        JSONArray statusArray = response.getJSONArray("statuses");

        statusArray.forEach(item -> {
            JSONObject obj = (JSONObject) item;              //org.json uses raw iterator, hence type conversion is required
            Tweet tweet = new Tweet(obj.getString("full_text"));
            tweets.add(tweet);
        });

        tweets.forEach(tweet -> {
            String str = tweet.getTweet();
            str = str.replaceAll("@[A-Za-z0-9]+", "");              //Remove user mentions
            str = str.replaceAll("https?://[A-Za-z0-9./]+", "");    //Remove links
            str = str.replaceAll("[^a-zA-Z0-9\\s'.,]", "");         //Remove special characters except whitespace,period,comma
            str = str.replaceAll("\\n", " ");                       //Replace newline with space
            tweet.setTweet(str);
        });

        return tweets;
    }


    private JSONObject fetchResponse (String expression) throws IOException {
        Map<String, String> queryParams = new HashMap<String, String>() {{
            put(TwitterConstants.BASE_QUERY_PARAMETER, expression);
            put(TwitterConstants.RESULT_TYPE_QUERY_PARAMETER, TwitterConstants.POPULAR);
            put(TwitterConstants.TWEET_MODE_QUERY_PARAMETER, TwitterConstants.EXTENDED_TWEET_MODE);
        }};

        ArrayList<Integer> expectedResponseCodes = new ArrayList<Integer>() {{
            add(HttpStatus.OK.value());
        }};

        return httpConnectionManager
                .buildUrl(TwitterConstants.BASE_URL, TwitterConstants.SEARCH_API_PATH, queryParams)
                .buildRequest(twitterAccessToken, HttpMethod.GET.toString(), null)
                .fetchJSONResponse(expectedResponseCodes);

    }

}

