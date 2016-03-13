package cn.vanchee.test;

import cn.vanchee.model.InDetail;
import cn.vanchee.service.InDetailService;
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
public class InDetailServiceTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InDetailService inDetailService = MyFactory.getInDetailService();

        System.out.println(inDetailService.getInDetailList() != null);
        System.out.println(inDetailService.getInDetailList().size() > 0);

        int listSize = inDetailService.getInDetailList().size();

        long start = System.currentTimeMillis();
        List<InDetail> list = inDetailService.queryInDetail(-1, 1, 1, -1, -1, -1, -1);
        long end = System.currentTimeMillis();
        System.out.println("query1 use time(ms):" + (end - start) + ",size:" + list.size());
        System.out.println(list != null);
        System.out.println(list.size() > 0);

        InDetail od = list.get(0);
        System.out.println("test get owner name:" + od.getOwnerName());
        System.out.println("test get fruit name:" + od.getFruitName());

        start = System.currentTimeMillis();
        InDetailService d = MyFactory.getInDetailService();
        list = d.queryInDetail(-1, 1, 1, -1, -1, -1, -1);
        end = System.currentTimeMillis();
        System.out.println("query2 use time(ms):" + (end - start) + ",size:" + list.size());

        //create
        InDetail outDetail = new InDetail();
        outDetail.setId(10000);
        outDetail.setOwner(1);
        outDetail.setDate(new Date().getTime());
        outDetail.setFruit(1);
        outDetail.setPrice(50);
        outDetail.setNum(10);

        inDetailService.create(outDetail);

        listSize++;
        System.out.println("create test:" + (inDetailService.getInDetailList().size() == listSize));

        //delete
        InDetail outDetail1 = list.get(0);
        inDetailService.delete(outDetail1.getId());
        listSize--;
        System.out.println("delete test:" + (inDetailService.getInDetailList().size() == listSize));

        //update
        InDetail outDetail2 = list.get(1);
        outDetail2.setOwner(1111);
        inDetailService.update(outDetail2);
        System.out.println("update test:" + (inDetailService.getInDetailList().size() == listSize));
    }
}
