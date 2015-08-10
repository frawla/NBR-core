package org.frawla.nbr.core.exporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.frawla.nbr.core.common.Util;

import de.schlichtherle.truezip.file.TFile;

public final class SheetExporter extends AbstractExporter
{
    private Workbook workBook;

    public SheetExporter()
    {
        super();
    }

    @Override
    public void export(final Map<String, String> resultMap)
    {
        assert null != resultMap : "Parameter 'resultMap' of method 'export' must not be null";

        try
        {
            final TFile destination = copyTmpFileIntoTmpDir();

            try (FileInputStream fin = new FileInputStream(destination);)
            {
                workBook = WorkbookFactory.create(fin);
                resultMap.forEach((k, v) -> ExportToExcel(k, v, destination));
            }
            catch (EncryptedDocumentException | InvalidFormatException e)
            {
                e.printStackTrace();
            }
            //Util.openFileInDefalutApplication(destination.getPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private TFile copyTmpFileIntoTmpDir() throws IOException
    {
        final String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
        /** copy template file into Temp Dir. */
        final String NBRTemp = TEMP_DIRECTORY + java.io.File.separator + "NBRTemp";
        final TFile NBRTempDir = new TFile(NBRTemp);
        NBRTempDir.mkdir();
        final TFile dst = new TFile(NBRTempDir + java.io.File.separator + "Samitemp.xlsx");
        TFile.cp(new File(Util.getMainResourcesPath("/template.xlsx")), dst);
        return dst;
    }

    private void ExportToExcel(final String path, final String formattedString, final File dst)
    {
        assert null != formattedString && !formattedString.isEmpty() : "Parameter 'formattedString' of method 'ExportToExcel' must not be empty";
        assert null != dst : "Parameter 'dst' of method 'ExportToExcel' must not be null";

        System.out.println("Proccessing...");

        String SheetName = Util.getFileWithoutExtention(path);
        Sheet mySht = workBook.createSheet(SheetName);

        String[] Lines = formattedString.split("\n");
        String[] Tabs;

        for (int i = 0; i < Lines.length; i++)
        {
            Tabs = Lines[i].split("\t");
            mySht.createRow(i);
            for (int j = 0; j < Tabs.length; j++)
            {
                Cell c = mySht.getRow(i).createCell(j);
                c.setCellValue(Tabs[j]);

                c.setCellStyle(getCellStyle());
            }
        }

        //auto-size all column
        //for (int colNum = 0; colNum < mySht.getRow(7).getLastCellNum(); colNum++)
        workBook.getSheet(SheetName).autoSizeColumn(1);

        //header           
        Row rHeader = insertRowBefore(0, mySht);
        mySht.addMergedRegion(new CellRangeAddress( /* Row */0, 0, /* Col */0, 3));
        rHeader.setHeightInPoints(30);
        Cell cHeader = rHeader.createCell(0);
        cHeader.setCellValue(SheetName.toUpperCase());
        CellStyle styleHeader = getCellStyle();
        styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
        styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font fHeader = getCellFont();
        fHeader.setFontHeightInPoints((short) 18);
        styleHeader.setFont(fHeader);

        cHeader.setCellStyle(styleHeader);

        //CellRangeAddress selectAll = new CellRangeAddress( /* Row */0, 16, /* Col */0, 4);

        //finlize
        workBook.removeSheetAt(0);

        try (FileOutputStream fout = new FileOutputStream(dst);)
        {
            workBook.write(fout);
        }
        catch (SecurityException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private CellStyle getCellStyle()
    {
        CellStyle cstyle = workBook.createCellStyle();
        cstyle.setFillForegroundColor(IndexedColors.YELLOW.index);

        cstyle.setFont(getCellFont());
        cstyle.setBorderBottom(CellStyle.BORDER_THIN);
        cstyle.setBorderLeft(CellStyle.BORDER_THIN);
        cstyle.setBorderTop(CellStyle.BORDER_THIN);
        cstyle.setBorderRight(CellStyle.BORDER_THIN);
        cstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cstyle.setWrapText(true);

        return cstyle;
    }

    private Font getCellFont()
    {
        final Font font = workBook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName("Consolas");
        return font;
    }

    private Row insertRowBefore(final int index, final Sheet sheet)
    {
        sheet.shiftRows(index, sheet.getLastRowNum(), 1, true, false);
        Row newRow = sheet.createRow(index);
        return newRow;
    }

    Workbook getWorkBook()
    {
        return this.workBook;
    }
}
