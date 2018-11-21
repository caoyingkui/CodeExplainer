package Controller;

/**
 * Created by oliver on 2018/11/6.
 */
public class SearchRequest {
    String code;

    public SearchRequest(){

    }

    public SearchRequest(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }



}
