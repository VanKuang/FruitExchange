package cn.vanchee.test;

import cn.vanchee.model.OutDetail;
import cn.vanchee.model.PaidDetail;
import cn.vanchee.service.OutDetailService;
import cn.vanchee.service.PaidDetailService;
import cn.vanchee.util.MyFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class PaidDetailServiceTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PaidDetailService inDetailService = MyFactory.getPaidDetailService();

        System.out.println(inDetailService.getPaidDetailList() != null);
        System.out.println(inDetailService.getPaidDetailList().size() > 0);

        int listSize = inDetailService.getPaidDetailList().size();

        long start = System.currentTimeMillis();
        List<PaidDetail> list = inDetailService.queryPaidDetail(-1, -1, 1, -1, -1, -1, -1, -1, -1, -1);
        long end = System.currentTimeMillis();
        System.out.println("query1 use time(ms):" + (end - start) + ",size:" + list.size());
        System.out.println(list != null);
        System.out.println(list.size() > 0);

        start = System.currentTimeMillis();
        PaidDetailService d = MyFactory.getPaidDetailService();
        list = d.queryPaidDetail(-1, -1, 1, -1, -1, -1, -1, -1, -1, -1);
        end = System.currentTimeMillis();
        System.out.println("query2 use time(ms):" + (end - start) + ",size:" + list.size());

        OutDetailService outDetailService = MyFactory.getOutDetailService();
        OutDetail outDetail = outDetailService.getOutDetail(1);
        double givenMoney = outDetail.getNum() * outDetail.getPrice();
        System.out.println("should give money:" + givenMoney);
        System.out.println("before paid status:" + outDetail.getStatus());
        System.out.println("before paid should paid money:" + outDetail.getNum() * outDetail.getPrice());

        //create
        PaidDetail paidDetail = new PaidDetail();
        paidDetail.setId(10000);
        paidDetail.setOid(1);
        paidDetail.setDate(new Date().getTime());
        paidDetail.setMoney(100000);
        inDetailService.create(paidDetail);

        listSize++;
        System.out.println("create test:" + (inDetailService.getPaidDetailList().size() == listSize));

        outDetail = outDetailService.getOutDetail(1);
        givenMoney = outDetail.getNum() * outDetail.getPrice();
        System.out.println("should give money:" + givenMoney);
        System.out.println("actual give money:" + 100000);
        System.out.println("after paid status:" + outDetail.getStatus());

        //delete
        PaidDetail outDetail1 = list.get(0);
        inDetailService.delete(outDetail1.getId());
        listSize--;
        System.out.println("delete test:" + (inDetailService.getPaidDetailList().size() == listSize));

        //update
        PaidDetail outDetail2 = list.get(1);
        outDetail2.setOid(1111);
        inDetailService.update(outDetail2);
        System.out.println("update test:" + (inDetailService.getPaidDetailList().size() == listSize));
    }
}
