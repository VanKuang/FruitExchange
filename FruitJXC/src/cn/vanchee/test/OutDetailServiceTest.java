package cn.vanchee.test;

import cn.vanchee.model.OutDetail;
import cn.vanchee.service.OutDetailService;
import cn.vanchee.util.MyFactory;
import org.apache.commons.lang.time.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.test
 * @verson v1.0.0
 */
public class OutDetailServiceTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OutDetailService outDetailService = MyFactory.getOutDetailService();

        System.out.println(outDetailService.getOutDetailList() != null);
        System.out.println(outDetailService.getOutDetailList().size() > 0);

        int listSize = outDetailService.getOutDetailList().size();

        long start = System.currentTimeMillis();
        List<OutDetail> list = outDetailService.queryOutDetail(-1, -1, 1, 1, -1, -1, -1, -1, 1, -1);
        long end = System.currentTimeMillis();
        System.out.println("query1 use time(ms):" + (end - start) + ",size:" + list.size());
        System.out.println(list != null);
        System.out.println(list.size() > 0);

        OutDetail od = list.get(0);
        System.out.println("test get owner name:" + od.getOwnerName());
        System.out.println("test get consumer name:" + od.getConsumerName());
        System.out.println("test get fruit name:" + od.getFruitName());

        start = System.currentTimeMillis();
        OutDetailService d = MyFactory.getOutDetailService();
        list = d.queryOutDetail(-1, 1, 1, 1, -1, -1, -1, -1, 1, -1);
        end = System.currentTimeMillis();
        System.out.println("query2 use time(ms):" + (end - start) + ",size:" + list.size());

        //create
        OutDetail outDetail = new OutDetail();
        outDetail.setId(10000);
        //outDetail.setConsumer(1);
        outDetail.setDate(new Date().getTime());
        //outDetail.setFruit(1);
        outDetail.setPrice(50);
        outDetail.setNum(10);

        outDetailService.create(outDetail);

        listSize++;

        //delete
        OutDetail outDetail1 = list.get(0);
        outDetailService.delete(outDetail1.getId());
        listSize--;
        System.out.println("delete test:" + (outDetailService.getOutDetailList().size() == listSize));

        //update
        OutDetail outDetail2 = list.get(1);
        //outDetail2.setOwner(1111);
        outDetailService.update(outDetail2);
        System.out.println("update test:" + (outDetailService.getOutDetailList().size() == listSize));
    }
}
