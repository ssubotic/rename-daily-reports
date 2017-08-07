package com.ssubotic.renamefiles;

import java.io.File;
public class RenameFiles {

	public static void main(String[] args) {
		
		File dir = new File("C:\\Users\\Stefan\\Desktop\\2017-07-July\\2017-07-01");
		File[] reports = dir.listFiles();
		
		for (File f : reports) {
			if (f.getName().toLowerCase().contains("stat")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.01 HotelStats_"));
				continue;
			}
			if (f.getName().toLowerCase().contains("rate")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.04 RateDiscrpRpt "));
				continue;
			}
			if (f.getName().toLowerCase().contains("shift")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.03 ShiftRec_"));
				continue;
			}
			if (f.getName().toLowerCase().contains("guest")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.05 GstLedgrRpt "));
				continue;
			}
			if (f.getName().toLowerCase().contains("tran")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.02 TransactionBalRpt_"));
				continue;
			}
			if (f.getName().toLowerCase().contains("table")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\Table of Contents "));
				continue;
			}
			if (f.getName().toLowerCase().contains("aging")) {
				f.renameTo(new File(dir.getAbsolutePath() + "\\A.06 AR-AgingDetailRpt "));
				continue;
			}
			
		}
		
	}

}
