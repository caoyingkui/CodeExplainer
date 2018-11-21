package Controller;

/**
 * Created by oliver on 2018/11/6.
 */
public class FeedbackMessage {
    private int type; // 0 add 1 update
    private String content;

    public FeedbackMessage(int type, String content){
        this.type = type;
        this.content = content;
    }

    public int getType(){
        return type;
    }

    public String getContent(){
        return content;
    }
}
