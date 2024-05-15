package es.boffmedia.teras.objects;

import es.boffmedia.teras.objects.post.SmartRotomPost;

public class SmartRotomResponse extends SmartRotomPost {
    private int status;
    private String message;
    private String error;
    private String data;

    public SmartRotomResponse() {
        super();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
