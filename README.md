This is a web application that :

1) Fetches tweets based on given keyword (eg: #Trump)

2) Generates wordcloud based on fetched tweets. (localhost:8080/wordcloud)

3) Generates sentiment analysis of fetched tweets. (localhost:8080/sentiment)
(I have not been able to create the d3 chart, got stuck at a silly Javascript issue)
(The sentiment analysis is printed in the console for now)

To run the app, simply run :
-> gradle bootRun

IMPORTANT NOTE : This app uses twitter standard search api which retrieves tweets
from a sample set from past 30 days. Hence not all search terms may give back results.



