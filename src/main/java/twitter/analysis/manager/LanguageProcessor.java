package twitter.analysis.manager;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class LanguageProcessor {

    private StanfordCoreNLP pipeline;

    @PostConstruct
    public  void init()
    {
        pipeline = new StanfordCoreNLP("application.properties");
    }

    public  Map<String, Integer> computeSentiment(String text)
    {
        Map<String, Integer> scoreMap = new HashMap<>();
        int score = 2; // Default as Neutral. 1 = Negative, 2 = Neutral, 3 = Positive
        String scoreStr;
        Annotation annotation = pipeline.process(text);
        for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
        {
            scoreStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            score = RNNCoreAnnotations.getPredictedClass(tree);
            System.out.println(scoreStr + "\t" + score + "\t" + sentence);
            scoreMap.merge(scoreStr, 1, Integer::sum);
        }
        return scoreMap;
    }
}
