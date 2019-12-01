package twitter.analysis.exception;

public class HttpConnectionException extends RuntimeException{

    private String message;

    public HttpConnectionException(String message, int responseCode, String url){
        super();
        this.message = String.format("HTTP request to \"%s\" returned follwing response [%d]%s",
                url,
                responseCode,
                message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

