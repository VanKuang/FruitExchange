package cn.vanchee.util;

import cn.vanchee.model.Consumption;
import cn.vanchee.model.InDetail;
import cn.vanchee.model.OutDetail;
import cn.vanchee.model.PaidVo;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static void writePaid(String fileName, String sheetName, String[] headers, List<PaidVo> data)
            throws WriteException {
        List<Label> labels = new ArrayList<Label>();
        for (int i = 0, length = data.size(); i < length; i++) {
            PaidVo paidVo = data.get(i);
            Color c = new Color(paidVo.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, paidVo.getName(), wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, paidVo.getShouldPaid() + "", wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, paidVo.getHadPaid() + "", wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, (paidVo.getShouldPaid() - paidVo.getHadPaid()) + "", wcfFC);
            labels.add(label);
        }
        write(fileName, sheetName, headers, labels);
    }

    public static void writeMyPaid(String fileName, String sheetName, String[] headers, List<PaidVo> data) throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();
        for (int i = 0, length = data.size(); i < length; i++) {
            PaidVo paidVo = data.get(i);
            Color c = new Color(paidVo.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, paidVo.getName(), wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, paidVo.getFruitName() + "", wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, sdf.format(new Date(paidVo.getDate())) + "", wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, paidVo.getShouldPaid() + "", wcfFC);
            labels.add(label);
            label = new Label(4, i + 1, paidVo.getHadPaid() + "", wcfFC);
            labels.add(label);
            label = new Label(5, i + 1, (paidVo.getShouldPaid() - paidVo.getHadPaid()) + "", wcfFC);
            labels.add(label);
        }
        write(fileName, sheetName, headers, labels);
    }

    public static void writePaidDetail(String fileName, String sheetName, String[] headers, List<OutDetail> data)
            throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();

        //data
        for (int i = 0, length = data.size(); i < length; i++) {
            OutDetail outDetail = data.get(i);
            Color c = new Color(outDetail.getColor());

            WritableCellFormat wcf = new WritableCellFormat();
            wcf.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, outDetail.getId() + "", wcf);
            labels.add(label);
            label = new Label(1, i + 1, outDetail.getIid() + "", wcf);
            labels.add(label);
            label = new Label(2, i + 1, sdf.format(new Date(outDetail.getDate())), wcf);
            labels.add(label);
            label = new Label(3, i + 1, outDetail.getOwnerName(), wcf);
            labels.add(label);
            label = new Label(4, i + 1, outDetail.getConsumerName(), wcf);
            labels.add(label);
            label = new Label(5, i + 1, outDetail.getFruitName(), wcf);
            labels.add(label);
            label = new Label(6, i + 1, outDetail.getPrice() + "", wcf);
            labels.add(label);
            label = new Label(7, i + 1, outDetail.getNum() + "", wcf);
            labels.add(label);
            label = new Label(8, i + 1, outDetail.getMoney() + "", wcf);
            labels.add(label);
            label = new Label(9, i + 1, outDetail.getPaidMoneyNotIncludeDiscount() + "", wcf);
            labels.add(label);
            label = new Label(10, i + 1, outDetail.getPaidMoneyDiscount() + "", wcf);
            labels.add(label);
            label = new Label(11, i + 1, outDetail.getStatusName(), wcf);
            labels.add(label);
            label = new Label(12, i + 1, Constants.getCensorName(outDetail.getCensored()), wcf);
            labels.add(label);
        }
        write(fileName, sheetName, headers, labels);
    }

    public static void reportInDetails(String fileName, String sheetName, String[] headers, List<InDetail> data)
            throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();
        for (int i = 0, length = data.size(); i < length; i++) {
            InDetail inDetail = data.get(i);
            Color c = new Color(inDetail.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, inDetail.getId() + "", wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, sdf.format(new Date(inDetail.getDate())), wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, inDetail.getOwnerName(), wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, inDetail.getFruitName() + "", wcfFC);
            labels.add(label);
            label = new Label(4, i + 1, inDetail.getPrice() + "", wcfFC);
            labels.add(label);
            label = new Label(5, i + 1, inDetail.getNum() + "", wcfFC);
            labels.add(label);
            label = new Label(6, i + 1, inDetail.getMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(7, i + 1, inDetail.getPaidMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(8, i + 1, inDetail.getSale() + "", wcfFC);
            labels.add(label);
            label = new Label(9, i + 1, inDetail.getRemain() + "", wcfFC);
            labels.add(label);
        }
        write(fileName, sheetName, headers, labels);
    }

    public static void reportOutDetails(String fileName, String sheetName, String[] headers, List<OutDetail> data) throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();

        for (int i = 0, length = data.size(); i < length; i++) {
            OutDetail outDetail = data.get(i);
            Color c = new Color(outDetail.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, outDetail.getId() + "", wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, outDetail.getIid() + "", wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, sdf.format(new Date(outDetail.getDate())), wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, outDetail.getOwnerName() + "", wcfFC);
            labels.add(label);
            label = new Label(4, i + 1, outDetail.getConsumerName() + "", wcfFC);
            labels.add(label);
            label = new Label(5, i + 1, outDetail.getFruitName() + "", wcfFC);
            labels.add(label);
            label = new Label(6, i + 1, outDetail.getPrice() + "", wcfFC);
            labels.add(label);
            label = new Label(7, i + 1, outDetail.getNum() + "", wcfFC);
            labels.add(label);
            label = new Label(8, i + 1, outDetail.getMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(9, i + 1, outDetail.getPaidMoneyNotIncludeDiscount() + "", wcfFC);
            labels.add(label);
            label = new Label(10, i + 1, outDetail.getPaidMoneyDiscount() + "", wcfFC);
            labels.add(label);
            label = new Label(11, i + 1, outDetail.getStatusName(), wcfFC);
            labels.add(label);
            label = new Label(12, i + 1, Constants.getCensorName(outDetail.getCensored()), wcfFC);
            labels.add(label);
        }

        write(fileName, sheetName, headers, labels);
    }

    public static void reportConsumptions(String fileName, String sheetName, String[] headers, List<Consumption> data)
            throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();

        for (int i = 0, length = data.size(); i < length; i++) {
            Consumption consumption = data.get(i);
            Color c = new Color(consumption.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, consumption.getId() + "", wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, sdf.format(new Date(consumption.getDate())), wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, consumption.getMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, consumption.getDesc() + "", wcfFC);
            labels.add(label);
        }
        write(fileName, sheetName, headers, labels);
    }

    public static void writeInDetail(String fileName, String sheetName, String[] headers, List<InDetail> data)
            throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();

        for (int i = 0, length = data.size(); i < length; i++) {
            InDetail inDetail = data.get(i);
            Color c = new Color(inDetail.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, inDetail.getId() + "", wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, sdf.format(new Date(inDetail.getDate())), wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, inDetail.getOwnerName(), wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, inDetail.getFruitName(), wcfFC);
            labels.add(label);
            label = new Label(4, i + 1, inDetail.getPrice() + "", wcfFC);
            labels.add(label);
            label = new Label(5, i + 1, inDetail.getNum() + "", wcfFC);
            labels.add(label);
            label = new Label(6, i + 1, inDetail.getMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(7, i + 1, inDetail.getPaidMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(8, i + 1, inDetail.getSale() + "", wcfFC);
            labels.add(label);
            label = new Label(9, i + 1, inDetail.getRemain() + "", wcfFC);
            labels.add(label);
            label = new Label(10, i + 1, Constants.getCensorName(inDetail.getCensored()) + "", wcfFC);
            labels.add(label);
        }

        write(fileName, sheetName, headers, labels);
    }

    public static void writeOutDetail(String fileName, String sheetName, String[] headers, List<OutDetail> data)
            throws WriteException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Label> labels = new ArrayList<Label>();

        for (int i = 0, length = data.size(); i < length; i++) {
            OutDetail outDetail = data.get(i);
            Color c = new Color(outDetail.getColor());

            WritableCellFormat wcfFC = new WritableCellFormat();
            wcfFC.setBackground(ColourUtil.getNearestColour(c.getRed(), c.getGreen(), c.getBlue()));

            Label label = new Label(0, i + 1, outDetail.getId() + "", wcfFC);
            labels.add(label);
            label = new Label(1, i + 1, outDetail.getIid() + "", wcfFC);
            labels.add(label);
            label = new Label(2, i + 1, sdf.format(new Date(outDetail.getDate())), wcfFC);
            labels.add(label);
            label = new Label(3, i + 1, outDetail.getOwnerName(), wcfFC);
            labels.add(label);
            label = new Label(4, i + 1, outDetail.getConsumerName() + "", wcfFC);
            labels.add(label);
            label = new Label(5, i + 1, outDetail.getFruitName() + "", wcfFC);
            labels.add(label);
            label = new Label(6, i + 1, outDetail.getPrice() + "", wcfFC);
            labels.add(label);
            label = new Label(7, i + 1, outDetail.getNum() + "", wcfFC);
            labels.add(label);
            label = new Label(8, i + 1, outDetail.getMoney() + "", wcfFC);
            labels.add(label);
            label = new Label(9, i + 1, outDetail.getPaidMoneyNotIncludeDiscount() + "", wcfFC);
            labels.add(label);
            label = new Label(10, i + 1, outDetail.getDiscounts() + "", wcfFC);
            labels.add(label);
            label = new Label(11, i + 1, outDetail.getStatusName(), wcfFC);
            labels.add(label);
            label = new Label(12, i + 1, Constants.getCensorName(outDetail.getCensored()), wcfFC);
            labels.add(label);
        }

        write(fileName, sheetName, headers, labels);
    }

    private static void write(String fileName, String sheetName, String[] headers, List<Label> labels) {
        File file = null;
        OutputStream os = null;
        WritableWorkbook wwb = null;
        WritableSheet ws = null;
        try {
            file = new File(getReportPath() + fileName);
            os = new FileOutputStream(file);
            wwb = Workbook.createWorkbook(os);
            ws = wwb.createSheet(sheetName, 0); //创建sheet

            //header
            for (int i = 0, length = headers.length; i < length; i++) {
                Label label = new Label(i, 0, headers[i]);
                ws.addCell(label);
            }
            //data
            for (Label label : labels) {
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
