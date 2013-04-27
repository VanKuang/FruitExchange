package cn.vanchee.util;

import cn.vanchee.service.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vanchee
 * @date 13-1-30
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class MyFactory {

    private static OutDetailService outDetailService;
    private static InDetailService inDetailService;
    private static OwnerService ownerService;
    private static UserService userService;
    private static ConsumerService consumerService;
    private static FruitService fruitService;
    private static PaidDetailService paidDetailService;
    private static ConsumptionService consumptionService;
    private static MyPaidService myPaidService;
    private static ResourceService resourceService;

    private static ExecutorService executorService;

    public void init() {
        executorService = Executors.newCachedThreadPool();
        outDetailService = new OutDetailService();
        inDetailService = new InDetailService();
        ownerService = new OwnerService();
        userService = new UserService();
        consumerService = new ConsumerService();
        fruitService = new FruitService();
        paidDetailService = new PaidDetailService();
        consumptionService = new ConsumptionService();
        myPaidService = new MyPaidService();
        resourceService = new ResourceService();
    }

    public void shutdown() {
        executorService.shutdown();
        outDetailService = null;
        inDetailService = null;
        ownerService = null;
        userService = null;
        consumerService = null;
        fruitService = null;
        paidDetailService = null;
        consumptionService = null;
        myPaidService = null;
        resourceService = null;
    }

    public static ExecutorService getExecutorService() {
        if (executorService != null) {
            return executorService;
        }
        return Executors.newCachedThreadPool();
    }

    public synchronized static OutDetailService getOutDetailService() {
        if (outDetailService == null) {
            outDetailService = new OutDetailService();
        }
        return outDetailService;
    }

    public synchronized static InDetailService getInDetailService() {
        if (inDetailService == null) {
            inDetailService = new InDetailService();
        }
        return inDetailService;
    }

    public synchronized static OwnerService getOwnerService() {
        if (ownerService == null) {
            ownerService = new OwnerService();
        }
        return ownerService;
    }

    public synchronized static UserService getUserService() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public synchronized static ConsumerService getConsumerService() {
        if (consumerService == null) {
            consumerService = new ConsumerService();
        }
        return consumerService;
    }

    public synchronized static FruitService getFruitService() {
        if (fruitService == null) {
            fruitService = new FruitService();
        }
        return fruitService;
    }

    public synchronized static PaidDetailService getPaidDetailService() {
        if (paidDetailService == null) {
            paidDetailService = new PaidDetailService();
        }
        return paidDetailService;
    }

    public synchronized static ConsumptionService getConsumptionService() {
        if (consumptionService == null) {
            consumptionService = new ConsumptionService();
        }
        return consumptionService;
    }

    public synchronized static MyPaidService getMyPaidService() {
        if (myPaidService == null) {
            myPaidService = new MyPaidService();
        }
        return myPaidService;
    }

    public synchronized static ResourceService getResourceService() {
        if (resourceService == null) {
            resourceService = new ResourceService();
        }
        return resourceService;
    }
}
