package bluehack.table9.medical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class extractInfo {

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 8; i++) {
            String fn = String.format("text/seizure%d.txt", i);
            System.out.println(fn);
            filterInfo(fn);
            System.out.println();
        }
    }

    private static void filterInfo(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;

        Pattern datePt = Pattern.compile("[0-9]?[0-9]?\\/[0-9]?[0-9]?\\/2...");
        Pattern diagPt = Pattern.compile(".*(ICD10-CM .*, Working, Diagnosis).");

        String date = "", diag = "";

        while ((str = br.readLine()) != null) {
            //get the date for the note
            Matcher dateMt = datePt.matcher(str);
            if (dateMt.find()) {
                date = dateMt.group(0).trim();
            }
            // get the diagnosis for the note
            Matcher diagMt = diagPt.matcher(str);

            if (diagMt.find()) {
                String diagTerm = diagMt.group(0).replace(", Working, Diagnosis\\).", "").split(" \\(")[0];
                diag = diagTerm.replace("Diagnosis:", "").trim();
            }
        }
        br.close();

        System.out.println(String.format("Date: %s\tDiagnosis: %s", date, diag));
    }

}
