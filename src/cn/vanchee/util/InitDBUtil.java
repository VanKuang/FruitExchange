package cn.vanchee.util;

import cn.vanchee.common.jdbc.DatabaseUtil;
import cn.vanchee.dao.*;
import cn.vanchee.model.Consumption;
import cn.vanchee.model.InDetail;
import cn.vanchee.model.OutDetail;
import cn.vanchee.model.PaidDetail;
import cn.vanchee.service.UserService;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class InitDBUtil {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void initOwner() throws IOException {
        OwnerDao ownerDao = new OwnerDao();
        ownerDao.createTable();
    }

    private void initConsumer() throws IOException {
        ConsumerDao consumerDao = new ConsumerDao();
        consumerDao.createTable();
    }

    private void initFruit() throws IOException {
        FruitDao fruitDao = new FruitDao();
        fruitDao.createTable();
    }

    private void initUser() throws IOException {
        UserService userService = new UserService();
        userService.createTable();
    }

    private void initOutDetail() throws IOException {

        Date now = new Date();

        // init out data
        List<OutDetail> list = new ArrayList<OutDetail>();
        for (int i = 1; i <= 0; i++) {
            OutDetail outDetail = new OutDetail();
            outDetail.setId(i);
            outDetail.setIid(i);
            outDetail.setCid(new Random().nextInt(10));
            outDetail.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            outDetail.setPrice(100.0 + i);
            outDetail.setNum(i + 10);
            outDetail.setStatus(new Random().nextInt(3));
            list.add(outDetail);
        }

        OutDetailDao outDetailDao = new OutDetailDao();
        outDetailDao.createTable();
    }

    private void initInDetail() throws IOException {
        Date now = new Date();

        // init out data
        List<InDetail> list = new ArrayList<InDetail>();
        for (int i = 1; i <= 0; i++) {
            InDetail inDetail = new InDetail();
            inDetail.setId(i);
            inDetail.setOid(new Random().nextInt(10));
            inDetail.setFid(new Random().nextInt(10));
//            inDetail.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            inDetail.setPrice(100.0 + i);
            inDetail.setNum(i + 10);
            list.add(inDetail);
        }

        InDetailDao inDetailDao = new InDetailDao();
        inDetailDao.createTable();
    }

    private void initMyPaidDetail() throws IOException {
        Date now = new Date();

        // init out data
        List<PaidDetail> list = new ArrayList<PaidDetail>();
        for (int i = 1; i <= 0; i++) {
            PaidDetail myPaid = new PaidDetail();
            myPaid.setId(i);
            myPaid.setOid(new Random().nextInt(10));
            myPaid.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            myPaid.setMoney(new Random().nextInt(10) * 100.0);
            list.add(myPaid);
        }

        PaidDao paidDao = new PaidDao();
        paidDao.createTable();
    }

    private void initPaidDetail() throws IOException {
        Date now = new Date();

        // init out data
        List<PaidDetail> list = new ArrayList<PaidDetail>();
        for (int i = 1; i <= 0; i++) {
            PaidDetail paidDetail = new PaidDetail();
            paidDetail.setId(i);
            paidDetail.setOid(new Random().nextInt(10));
            paidDetail.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            paidDetail.setMoney(new Random().nextInt(10) * 100.0);
            list.add(paidDetail);
        }
        //MyFactory.getPaidDetailService().createTable();
    }

    private void initOutDetailBackup() throws IOException {
        Date now = new Date();

        // init out data
        List<OutDetail> list = new ArrayList<OutDetail>();
        for (int i = 1; i <= 0; i++) {
            OutDetail outDetail = new OutDetail();
            outDetail.setId(i);
            outDetail.setCid(new Random().nextInt(10));
            outDetail.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            outDetail.setPrice(100.0 + i);
            outDetail.setNum(i + 10);
            outDetail.setStatus(Constants.OUT_STATUS_PAID_ENOUGH);
            list.add(outDetail);
        }
    }

    private void initConsumption() throws IOException {
        Date now = new Date();

        // init out data
        List<Consumption> list = new ArrayList<Consumption>();
        for (int i = 1; i <= 0; i++) {
            Consumption consumption = new Consumption();
            consumption.setId(i);
            consumption.setMoney(100.0 + i);
            consumption.setCreateAt(sdf.format(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH)));
            consumption.setDesc("desc" + i);
            list.add(consumption);
        }

        ConsumptionDao consumptionDao = new ConsumptionDao();
        consumptionDao.createTable();
    }

    public void initAllData() throws IOException {
        initOwner();
        initConsumer();
        initFruit();
        initUser();
        initOutDetail();
        initInDetail();
        initMyPaidDetail();
        initPaidDetail();
        initOutDetailBackup();
        initConsumption();
    }

    public static void main(String[] args) throws IOException {
        InitDBUtil initUtil = new InitDBUtil();

        DatabaseUtil.deleteDB();

        initUtil.initOwner();
        initUtil.initConsumer();
        initUtil.initFruit();
        initUtil.initUser();
        initUtil.initOutDetail();
        initUtil.initInDetail();
        initUtil.initMyPaidDetail();
        initUtil.initPaidDetail();
        initUtil.initOutDetailBackup();
        initUtil.initConsumption();
    }
}
