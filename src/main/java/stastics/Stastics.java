package stastics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Stastics {
    public static void per_commit_modify_file_number(String diffPath){
        File root = new File(diffPath);
        File[] files = root.listFiles();

        int count0, count1, count2, count3;
        count0 = count1 = count2 = count3 = 0;
        int count = 0;
        int total = 0;
        for(File commitFile : files){
            if(total ++ % 100 == 0) System.out.println(total);

            count = 0;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(commitFile));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("---")) {
                        String next_line = reader.readLine();
                        if(line.endsWith(".java") || next_line.endsWith(".java")){
                            count ++;
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if(count == 0) count0 ++;
            else if(count == 1) count1 ++;
            else if(count == 2) count2 ++;
            else count3 ++;
        }
        System.out.println("per_commit_modify_file_number");
        System.out.println(String.format("  total:%d, count0:%d, count1:%d, count2:%d, more:%d", total, count0, count1, count2, count3));
    }

    public static void main(String[] args){
        per_commit_modify_file_number("C:\\Users\\oliver\\Downloads\\lucene-solr-master\\diffs\\hadoop");
    }
}
