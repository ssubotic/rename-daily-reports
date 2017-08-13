package com.ssubotic.renamefiles;

import java.io.File;

public class RenameFiles 
{   
    
    public static void main(String[] args) 
    {
        File dir = new File("C:\\Users\\Stefan\\Desktop\\2017-07-July\\2017-07-01");
        File[] reports = dir.listFiles();
        
        for (File f : reports) {
            renameFile(dir, f);
            System.out.println(f);
        }
    }
    
    public static void renameFile(File dir, File f) 
    {
        if (f.getName().toLowerCase().contains("stat")) {
            f.renameTo(new File(dir.getPath() + "\\A.01 HotelStats_"));
            return;
        } 
        else if (f.getName().toLowerCase().contains("tran")) {
            f.renameTo(new File(dir.getPath() + "\\A.02 TransactionBalRpt_"));
            return;
        }
        else if (f.getName().toLowerCase().contains("shift")) {
            f.renameTo(new File(dir.getPath() + "\\A.03 ShiftRec_"));
            return;
        }
        else if (f.getName().toLowerCase().contains("rate")) {
            f.renameTo(new File(dir.getPath() + "\\A.04 RateDiscrpRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("guest")) {
            f.renameTo(new File(dir.getPath() + "\\A.05 GstLedgrRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("aging")) {
            f.renameTo(new File(dir.getPath() + "\\A.06 AR-AgingDetailRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("table")) {
            f.renameTo(new File(dir.getPath() + "\\Table of Contents "));
            return;
        }
    }
    
}
