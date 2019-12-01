package twitter.analysis.manager;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import twitter.analysis.core.RequestProcessor;
import twitter.analysis.model.Query;

import java.util.Map;

@Controller
public class RestApiManager {

    @Autowired
    RequestProcessor requestProcessor;

    @GetMapping("/wordcloud")
    public String wordcloudForm(Model model) {
        model.addAttribute("query", new Query());
        return "wordcloudInput";
    }

    @PostMapping("/wordcloud")
    public String displayWordlcoud(Query query, Model model) throws Exception{
        String text =  requestProcessor.getTweetsBlob(query.getContent());
        model.addAttribute("tweetArray",text);
        return "wordcloud";
    }

    @GetMapping("/sentiment")
    public String sentimentForm(Model model) {
        model.addAttribute("query", new Query());
        return "sentimentInput";
    }

    //TODO: Figure out a way to prevent javascript from turning " to &quot
    @PostMapping(value = "/sentiment")
    public String publishSentimentAnalysis(Query query, Model model) throws Exception{
        Map<String,Integer>sentimentMap = requestProcessor.getSentimentAnalysis(query.getContent());
        JSONObject jsonObject = new JSONObject(sentimentMap);
        model.addAttribute("sentimentMap",jsonObject);
        return "sentiment";
    }

}