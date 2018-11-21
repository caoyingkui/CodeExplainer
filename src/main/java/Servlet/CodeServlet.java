package Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.json.*;




@WebServlet(name = "Servlet.CodeServlet")
public class CodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("Code");
        System.out.println(code);

        System.out.println("接受输入");
        JSONObject returnResult = new JSONObject();

        String command = "python C:\\Users\\oliver\\PycharmProjects\\DiffRecovery\\CodeSearcher.py '" + code + "'";
        Process process = Runtime.getRuntime().exec(command);
        InputStream in = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        String result = "";
        boolean s = false;
        while( (line = reader.readLine()) != null){
            System.out.println(line);
            if(s)
                result += line;
            if(line.compareTo("**RESULT**") == 0)
                s = true;

        }
        JSONObject object = new JSONObject(result);
        //returnResult.put("co" ,"123");
        response.getWriter().print(object.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
