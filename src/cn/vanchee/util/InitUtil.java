package cn.vanchee.util;

/**
 * @author vanchee
 * @date 13-1-28
 * @package cn.vanchee.util
 * @verson v1.0.0
 */
public class InitUtil {

    /*
    private void initOwner() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_OWNER, true);

        // init owner data
        List<Owner> ownerList = new ArrayList<Owner>();
        for (int i = 0; i < 0; i++) {
            Owner owner = new Owner();
            owner.setId(i);
            owner.setName("owner_" + i);
            ownerList.add(owner);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_OWNER, ownerList);
    }

    private void initConsumer() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_CONSUMER, true);

        // init owner data
        List<Consumer> consumerList = new ArrayList<Consumer>();
        for (int i = 0; i < 0; i++) {
            Consumer consumer = new Consumer();
            consumer.setId(i);
            consumer.setName("consumer_" + i);
            consumerList.add(consumer);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_CONSUMER, consumerList);
    }

    private void initFruit() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_FRUIT, true);

        // init owner data
        List<Fruit> fruitList = new ArrayList<Fruit>();
        for (int i = 0; i < 0; i++) {
            Fruit fruit = new Fruit();
            fruit.setId(i);
            fruit.setName("fruit_" + i);
            fruitList.add(fruit);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_FRUIT, fruitList);
    }

    private void initUser() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_USER, true);

        List<User> userList = new ArrayList<User>();

        User user = User.admin;
        userList.add(user);
        DataUtil.writeListToFile(Constants.FILE_NAME_USER, userList);
    }

    private void initOutDetail() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_OUT_DETAIL, true);

        Date now = new Date();

        // init out data
        List<OutDetail> list = new ArrayList<OutDetail>();
        for (int i = 1; i <= 0; i++) {
            OutDetail outDetail = new OutDetail();
            outDetail.setId(i);
            outDetail.setIid(i);
            outDetail.setCid(new Random().nextInt(10));
            outDetail.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            outDetail.setPrice(100 + i);
            outDetail.setNum(i + 10);
            outDetail.setStatus(new Random().nextInt(3));
            list.add(outDetail);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_OUT_DETAIL, list);
    }

    private void initInDetail() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_IN_DETAIL, true);

        Date now = new Date();

        // init out data
        List<InDetail> list = new ArrayList<InDetail>();
        for (int i = 1; i <= 0; i++) {
            InDetail inDetail = new InDetail();
            inDetail.setId(i);
            inDetail.setOwner(new Random().nextInt(10));
            inDetail.setFruit(new Random().nextInt(10));
            inDetail.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            inDetail.setPrice(100 + i);
            inDetail.setNum(i + 10);
            list.add(inDetail);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_IN_DETAIL, list);
    }

    private void initMyPaidDetail() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_MY_PAID, true);

        Date now = new Date();

        // init out data
        List<MyPaid> list = new ArrayList<MyPaid>();
        for (int i = 1; i <= 0; i++) {
            MyPaid myPaid = new MyPaid();
            myPaid.setId(i);
            myPaid.setIid(new Random().nextInt(10));
            myPaid.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            myPaid.setMoney(new Random().nextInt(10) * 100);
            list.add(myPaid);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_MY_PAID, list);
    }

    private void initPaidDetail() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_PAID_DETAIL, true);

        Date now = new Date();

        // init out data
        List<PaidDetail> list = new ArrayList<PaidDetail>();
        for (int i = 1; i <= 0; i++) {
            PaidDetail paidDetail = new PaidDetail();
            paidDetail.setId(i);
            paidDetail.setOid(new Random().nextInt(10));
            paidDetail.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            paidDetail.setMoney(new Random().nextInt(10) * 100);
            list.add(paidDetail);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_PAID_DETAIL, list);
    }

    private void initOutDetailBackup() throws IOException {
        FileUtil.createFile(Constants.OUT_DETAIL_BACKUP_FILE_NAME, true);

        Date now = new Date();

        // init out data
        List<OutDetail> list = new ArrayList<OutDetail>();
        for (int i = 1; i <= 0; i++) {
            OutDetail outDetail = new OutDetail();
            outDetail.setId(i);
            outDetail.setCid(new Random().nextInt(10));
            outDetail.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            outDetail.setPrice(100 + i);
            outDetail.setNum(i + 10);
            outDetail.setStatus(Constants.OUT_STATUS_PAID_ENOUGH);
            list.add(outDetail);
        }
        DataUtil.writeListToFile(Constants.OUT_DETAIL_BACKUP_FILE_NAME, list);
    }

    private void initConsumption() throws IOException {
        FileUtil.createFile(Constants.FILE_NAME_CONSUMPTION, true);

        Date now = new Date();

        // init out data
        List<Consumption> list = new ArrayList<Consumption>();
        for (int i = 1; i <= 0; i++) {
            Consumption consumption = new Consumption();
            consumption.setId(i);
            consumption.setMoney(100 + i);
            consumption.setDate(DateUtils.truncate(DateUtils.addDays(now, new Random().nextInt(10)), Calendar.DAY_OF_MONTH).getTime());
            consumption.setDesc("desc" + i);
            list.add(consumption);
        }
        DataUtil.writeListToFile(Constants.FILE_NAME_CONSUMPTION, list);
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
        InitUtil initUtil = new InitUtil();
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

    */
}
