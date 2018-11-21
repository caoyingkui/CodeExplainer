package Controller;

/**
 * Created by oliver on 2018/11/5.
 */
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Controller
public class CodeSearchController {
    List<String> messageList = new ArrayList<>();
    int messageStart = 0;
    SearchMessage searchMessage;


    int id = 0;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/searching")
    public void codeSearch(SearchRequest request) throws Exception{
        System.out.println("connected!");
        String code = request.getCode();

        JSONObject returnResult = new JSONObject();

        code = code.replace("\r", " ").replace("\n", " ").replace("\"", "\\\"");
        String command = "python -u C:\\Users\\oliver\\PycharmProjects\\DiffRecovery\\CodeSearcher.py \"" + code + "\"";
        Process process = Runtime.getRuntime().exec(command);
        InputStream in = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        String result = "";
        boolean s = false;
        while( (line = reader.readLine()) != null){
            if(s)
                result += line;
            else
                feedBackMessage(line + "\n");
            if(line.compareTo("**RESULT**") == 0)
                s = true;

        }
        if(result.compareTo("None") != 0) {
            JSONObject object = new JSONObject(result);
            searchMessage = new SearchMessage(object.get("code").toString(), object.get("comment").toString(), object.get("relation").toString());
            while (messageStart < messageList.size()) {
                Thread.sleep(1000);
            }
            returnResultMessage();
        }else{
            feedBackMessage("No file found!");
        }

    }

    public void feedBackMessage(String feedback){
        this.template.convertAndSend("/message/feedback", new FeedbackMessage(0, feedback));
    }

    public void returnResultMessage(){
        this.template.convertAndSend("/message/result", searchMessage);
    }
}