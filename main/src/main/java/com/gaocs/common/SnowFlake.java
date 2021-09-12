package com.gaocs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * twitter的SnowFlake算法 -- java实现
 * 64位的long类型，依赖系统时间，如果超过每秒26万生成数据则不适用
 *
 */
public class SnowFlake {

	public  static Logger logger = LoggerFactory.getLogger(SnowFlake.class);

	private static SnowFlake snowFlake = null;
    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心 (不能超过32)
    private long machineId;     //机器标识 (不能超过32)
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {//机器标识不能超过数据中心 ，不能小于0
            throw new IllegalArgumentException("SnowFlake datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("SnowFlake machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static synchronized SnowFlake getInstance() {
    	if(snowFlake == null){
    		long  machineId = 3;
    		try {
				InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址
				String host = address.getHostAddress();//192.168.0.121
				host = host.substring(host.length()-1, host.length());
				machineId = Long.valueOf(host);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				logger.error("snowFlake get id UnknownHostException {} ======");
			}
    		snowFlake = new SnowFlake(3,machineId);
        }
        return snowFlake;
    }

    // 私有构造方法
    @SuppressWarnings("unused")
	private SnowFlake() {}

    public static void main(String[] args) {
        SnowFlake snowFlake = getInstance();

        for (int i = 0; i < (1 << 2); i++) {
            System.out.println(snowFlake.nextId());
        }
    }
}
