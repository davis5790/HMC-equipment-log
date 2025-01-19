package com.example.handlingformsubmission;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Component("WriteExcel")
public class WriteExcel {
    static WritableCellFormat timesWhite ;
    static WritableCellFormat timesGrey ;
    static WritableCellFormat timesBoldWhite ;
    static WritableCellFormat timesBoldGrey ;
    static WritableCellFormat timesBoldUnderline;

    static {
        try {
            WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
            timesWhite = new WritableCellFormat(times10pt);
            timesWhite.setBackground(Colour.WHITE);
            timesWhite.setWrap(true);

            WritableFont timesBold10pt = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD);
            timesBoldWhite = new WritableCellFormat(timesBold10pt);
            timesBoldWhite.setBackground(Colour.WHITE);
            timesBoldWhite.setWrap(true);

            timesGrey = new WritableCellFormat(times10pt);
            timesGrey.setBackground(Colour.GRAY_25);
            timesGrey.setWrap(true);

            timesBoldGrey = new WritableCellFormat(timesBold10pt);
            timesBoldGrey.setBackground(Colour.GRAY_25);

            WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.SINGLE);
            timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
            timesBoldUnderline.setWrap(true);



        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public InputStream write(List<EnhancedLogItem> items) throws IOException, WriteException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "US"));

        WritableWorkbook workbook = Workbook.createWorkbook(os, wbSettings);
        workbook.createSheet("Work Item Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);

        addLabels(excelSheet);
        fillContent(excelSheet, items);

        workbook.write();
        workbook.close();

        return new ByteArrayInputStream(os.toByteArray());
    }

    private void addLabels(WritableSheet sheet) throws WriteException {
        CellView cv = new CellView();
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        addCaption(sheet, 0, 0, "Device");
        addCaption(sheet, 1, 0, "H-Number");
        addCaption(sheet, 2, 0, "Date");
        addCaption(sheet, 3, 0, "Time");
        addCaption(sheet, 4, 0, "Name");
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s) throws WriteException {
        Label label = new Label(column, row, s, timesBoldUnderline);
        int cc = s.length();
        sheet.setColumnView(column, cc);
        sheet.addCell(label);
    }

    private void addField(WritableSheet sheet, int column, int row, String s, WritableCellFormat cellFormat) throws WriteException {
        Label label = new Label(column, row, s, cellFormat);
        int cc = s.length();
        cc = cc > 200 ? 150 : cc + 10;
        sheet.setColumnView(column, cc);
        sheet.addCell(label);
    }

    private void fillContent(WritableSheet sheet, List<EnhancedLogItem> items) throws WriteException {
        int row = 2;
        String currentDevice = items.get(0).getDevice();
        WritableCellFormat cellFormat;

        for (EnhancedLogItem item : items) {
                if (!item.getDevice().equals(currentDevice) || row == 2){
                    cellFormat = timesBoldWhite;
                } else {
                    cellFormat = timesWhite;
                }
                addField(sheet, 0, row, item.getDevice(), cellFormat);
                addField(sheet, 1, row, item.getHNumber(), timesWhite );
                addField(sheet, 2, row, item.getDate(), timesWhite);
                addField(sheet, 3, row, item.getTime(), timesWhite);
                addField(sheet, 4, row, item.getName(), timesWhite);
            row += 1;
            currentDevice = item.getDevice();
            }
        }
    }