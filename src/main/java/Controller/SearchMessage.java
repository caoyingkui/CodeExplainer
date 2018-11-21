package Controller;

/**
 * Created by oliver on 2018/11/6.
 */
public class SearchMessage {
    String code;
    String comment;
    String relation;

    public SearchMessage(String code, String comment, String relation){
        this.code = code;
        this.comment = comment;
        this.relation = relation;
    }

    public String getCode(){
        return this.code;
    }

    public String getComment(){
        return this.comment;
    }

    public String getRelation(){
        return this.relation;
    }
}
