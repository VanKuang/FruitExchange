package cn.vanchee.util;

import cn.vanchee.model.Consumption;
import cn.vanchee.model.InDetail;
import cn.vanchee.model.OutDetail;
import cn.vanchee.model.PaidVo;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-2-12
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {
    }

    public static String getReportPath() {
        return new File("").getAbsolutePath() + "\\report\\";
    }

    public static void writePaid(String fileName, String[] headers, List<PaidVo> data) {
        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("欠款总表", 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                PaidVo paidVo = data.get(i);
                Color c = new Color(paidVo.getColor());

                WritableCellFormat wcfFC = new WritableCellFormat();
                wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, paidVo.getName(), wcfFC);
                ws.addCell(label);
                label = new Label(1, i + 1, paidVo.getShouldPaid() + "", wcfFC);
                ws.addCell(label);
                label = new Label(2, i + 1, paidVo.getHadPaid() + "", wcfFC);
                ws.addCell(label);
                label = new Label(3, i + 1, (paidVo.getShouldPaid() - paidVo.getHadPaid()) + "", wcfFC);
                ws.addCell(label);
            }

            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void writeMyPaid(String fileName, String[] headers, List<PaidVo> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("欠款总表", 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                PaidVo paidVo = data.get(i);
                Color c = new Color(paidVo.getColor());

                WritableCellFormat wcfFC = new WritableCellFormat();
                wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, paidVo.getName(), wcfFC);
                ws.addCell(label);
                label = new Label(1, i + 1, paidVo.getFruitName() + "", wcfFC);
                ws.addCell(label);
                label = new Label(2, i + 1, sdf.format(new Date(paidVo.getDate())) + "", wcfFC);
                ws.addCell(label);
                label = new Label(3, i + 1, paidVo.getShouldPaid() + "", wcfFC);
                ws.addCell(label);
                label = new Label(4, i + 1, paidVo.getHadPaid() + "", wcfFC);
                ws.addCell(label);
                label = new Label(5, i + 1, (paidVo.getShouldPaid() - paidVo.getHadPaid()) + "", wcfFC);
                ws.addCell(label);
            }

            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void writePaidDetail(String fileName, String[] headers, List<OutDetail> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("欠款明细", 0); //创建sheet


            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                OutDetail outDetail = data.get(i);
                Color c = new Color(outDetail.getColor());

                WritableCellFormat wcf = new WritableCellFormat();
                wcf.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, outDetail.getId() + "", wcf);
                ws.addCell(label);
                label = new Label(1, i + 1, outDetail.getIid() + "", wcf);
                ws.addCell(label);
                label = new Label(2, i + 1, sdf.format(new Date(outDetail.getDate())), wcf);
                ws.addCell(label);
                label = new Label(3, i + 1, outDetail.getOwnerName(), wcf);
                ws.addCell(label);
                label = new Label(4, i + 1, outDetail.getConsumerName(), wcf);
                ws.addCell(label);
                label = new Label(5, i + 1, outDetail.getFruitName(), wcf);
                ws.addCell(label);
                label = new Label(6, i + 1, outDetail.getPrice() + "", wcf);
                ws.addCell(label);
                label = new Label(7, i + 1, outDetail.getNum() + "", wcf);
                ws.addCell(label);
                label = new Label(8, i + 1, outDetail.getMoney() + "", wcf);
                ws.addCell(label);
                label = new Label(9, i + 1, outDetail.getPaidMoneyNotIncludeDiscount() + "", wcf);
                ws.addCell(label);
                label = new Label(10, i + 1, outDetail.getPaidMoneyDiscount() + "", wcf);
                ws.addCell(label);
                label = new Label(11, i + 1, outDetail.getStatusName(), wcf);
                ws.addCell(label);
                label = new Label(12, i + 1, Constants.getCensorName(outDetail.getCensored()), wcf);
                ws.addCell(label);
            }

            wwb.write();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    public static void reportInDetails(String fileName, String[] headers, List<InDetail> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("进货明细", 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                InDetail inDetail = data.get(i);
                Color c = new Color(inDetail.getColor());

                WritableCellFormat wcfFC = new WritableCellFormat();
                wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, inDetail.getId() + "", wcfFC);
                ws.addCell(label);
                label = new Label(1, i + 1, sdf.format(new Date(inDetail.getDate())), wcfFC);
                ws.addCell(label);
                label = new Label(2, i + 1, inDetail.getOwnerName(), wcfFC);
                ws.addCell(label);
                label = new Label(3, i + 1, inDetail.getFruitName() + "", wcfFC);
                ws.addCell(label);
                label = new Label(4, i + 1, inDetail.getPrice() + "", wcfFC);
                ws.addCell(label);
                label = new Label(5, i + 1, inDetail.getNum() + "", wcfFC);
                ws.addCell(label);
                label = new Label(6, i + 1, inDetail.getMoney() + "", wcfFC);
                ws.addCell(label);
                label = new Label(7, i + 1, inDetail.getPaidMoney() + "", wcfFC);
                ws.addCell(label);
                label = new Label(8, i + 1, inDetail.getSale() + "", wcfFC);
                ws.addCell(label);
                label = new Label(9, i + 1, inDetail.getRemain() + "", wcfFC);
                ws.addCell(label);
            }

            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static void reportOutDetails(String fileName, String[] headers, List<OutDetail> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("销售明细", 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                OutDetail outDetail = data.get(i);
                Color c = new Color(outDetail.getColor());

                WritableCellFormat wcfFC = new WritableCellFormat();
                wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, outDetail.getId() + "", wcfFC);
                ws.addCell(label);
                label = new Label(1, i + 1, outDetail.getIid() + "", wcfFC);
                ws.addCell(label);
                label = new Label(2, i + 1, sdf.format(new Date(outDetail.getDate())), wcfFC);
                ws.addCell(label);
                label = new Label(3, i + 1, outDetail.getOwnerName() + "", wcfFC);
                ws.addCell(label);
                label = new Label(4, i + 1, outDetail.getConsumerName() + "", wcfFC);
                ws.addCell(label);
                label = new Label(5, i + 1, outDetail.getFruitName() + "", wcfFC);
                ws.addCell(label);
                label = new Label(6, i + 1, outDetail.getPrice() + "", wcfFC);
                ws.addCell(label);
                label = new Label(7, i + 1, outDetail.getNum() + "", wcfFC);
                ws.addCell(label);
                label = new Label(8, i + 1, outDetail.getMoney() + "", wcfFC);
                ws.addCell(label);
                label = new Label(9, i + 1, outDetail.getPaidMoneyNotIncludeDiscount() + "", wcfFC);
                ws.addCell(label);
                label = new Label(10, i + 1, outDetail.getPaidMoneyDiscount() + "", wcfFC);
                ws.addCell(label);
                label = new Label(11, i + 1, outDetail.getStatusName(), wcfFC);
                ws.addCell(label);
                label = new Label(12, i + 1, Constants.getCensorName(outDetail.getCensored()), wcfFC);
                ws.addCell(label);
            }

            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    public static void reportConsumptions(String fileName, String[] headers, List<Consumption> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file  = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet("销售明细", 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (int i = 0, length = data.size(); i < length; i++) {
                Consumption consumption = data.get(i);
                Color c = new Color(consumption.getColor());

                WritableCellFormat wcfFC = new WritableCellFormat();
                wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

                Label label = new Label(0, i + 1, consumption.getId() + "", wcfFC);
                ws.addCell(label);
                label = new Label(1, i + 1, sdf.format(new Date(consumption.getDate())), wcfFC);
                ws.addCell(label);
                label = new Label(2, i + 1, consumption.getMoney() + "", wcfFC);
                ws.addCell(label);
                label = new Label(3, i + 1, consumption.getDesc() + "", wcfFC);
                ws.addCell(label);
            }
            wwb.write();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                wwb.close();
                os.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }
}
