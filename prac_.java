import java.io.*;

public class prac_ {
    private String[][] symtab;
    private String[][] littab;
    private int[][] pooltab;
    private String[][] ic;
    private int si;
    private int li;
    private int pi;
    private int ii;
    private String[][] mc;
    private boolean symBool, litBool, poolBool, icBool;

    // Constructor
    prac_() {
        symtab = new String[20][2];
        ic = new String[30][3];
        littab = new String[20][3];
        pooltab = new int[10][2];
        mc = new String[20][3];
        si = li = pi = ii = 0;
        symBool = litBool = poolBool = icBool = false;
    }

    // Method to generate machine code
    public void pass2(String inputFile, String outputFile) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] token = line.split("\\s+");
                switch (token[0]) {
                    case "#symbol":
                        symBool = true;
                        litBool = poolBool = icBool = false;
                        //createTab(token);
                        break;
                    case "#literal":
                        symBool = poolBool = icBool = false;
                        litBool = true;
                        //createTab(token);
                        break;
                    case "#pool":
                        symBool = litBool = icBool = false;
                        poolBool = true;
                        //createTab(token);
                        break;
                    case "#intermediate":
                        symBool = litBool = poolBool = false;
                        icBool = true;
                        //createTab(token);
                        break;
                    default :
                         createTab(token);
                }
            }
            createMc();
            printMc(outputFile);
        }
    }

    // Method to create tables
    private void createTab(String[] a) {
        if (symBool) {
            symtab[si][0] = a[0];
            symtab[si][1] = a[1];
            si++;
        } else if (litBool) {
            littab[li][0] = a[0];
            littab[li][1] = a[1];
            littab[li][2] = a[2];
            li++;
        } else if (poolBool) {
            pooltab[pi][0] = Integer.parseInt(a[0]);
            pooltab[pi][1] = Integer.parseInt(a[1]);
            pi++;
        } else if (icBool) {
            ic[ii][0] = a[0];
            ic[ii][1] = a[1];
            ic[ii][2] = a[2];
            ii++;
        }
    }
    
   private void createMc(){
    int i=0;
    while (ic[i][0]!=null){
        if(ic[i][0].equals("(AD,01)")||ic[i][0].equals("(AD,03)")||ic[i][0].equals("(AD,04)")||ic[i][0].equals("(IS,00)"))
        {}
        else if(ic[i][0].equals("(AD,02)"))
        {
            mc[i][0]=mc[i][1]="00";
            mc[i][2]="000";
        }
        else
        {
            String[] token0 = detect(ic[i][0]);
                    if (token0 != null && token0.length > 1) {
                        mc[i][0] = token0[1];
                    }
                    String[] token1 = detect(ic[i][1]);
                    if (token1 != null && token1.length > 1) {
                        if (token1[0].equals("C")) {
                            mc[i][1] = "00";
                            mc[i][2] = token1[1];
                        } else {
                            mc[i][1] = token1[1];
                        }
                    }
                    String[] token2 = detect(ic[i][2]);
                    if (token2 != null && token2.length > 1) {
                        if(token2[0].equals("S"))
                        {
                            
                            mc[i][2]=symtab[Integer.parseInt(token2[1])][1];
                        }
                        else if(token2[0].equals("L"))
                        {
                            mc[i][2]=littab[Integer.parseInt(token2[1])][2];
                        }
                    }
                    

        }i++;
    }
}

    // Helper method to process tokens
    private String[] detect(String aa) {
       
            String x = aa.substring(1, aa.length() - 1);
            return x.split("\\,");
        
    }

    // Method to print machine code
    private void printMc(String outputfile)throws IOException{
        try
    (FileWriter w =new FileWriter(outputfile)){
    for (int i = 0; i < ii; i++) {
        String t = "";

        if (mc[i][0] != null) {
            t += String.format("%02d", Integer.parseInt(mc[i][0]));
        } 
        t += " ";  // Space between columns
        
        if (mc[i][1] != null) {
            t += String.format("%02d", Integer.parseInt(mc[i][1]));
        }
        t += " ";  // Space between columns
        
        if (mc[i][2] != null) {
            t += String.format("%03d", Integer.parseInt(mc[i][2]));
        }
        
        System.out.println(t);
        
    w.write(t);
    w.write(System.lineSeparator());
    
        
        }
        w.close();
    }
}



    // Main method
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: Prac <inputfile> <outputfile>");
            return;
        }

        prac_ pass_2 = new prac_();
        try {
            pass_2.pass2(args[0], args[1]);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
