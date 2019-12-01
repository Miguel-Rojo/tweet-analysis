package twitter.analysis.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Tweet {

    private String tweet;

    public Tweet(String tweet) {
        this.tweet = tweet;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
