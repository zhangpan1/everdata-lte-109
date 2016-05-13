package com.eversec.lte.kpi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
import com.eversec.lte.kpi.bean.S11;
import com.eversec.lte.kpi.bean.S1MME;
import com.eversec.lte.kpi.bean.S1U;
import com.eversec.lte.kpi.bean.S1U_Value;
import com.eversec.lte.kpi.bean.S6A;
import com.eversec.lte.kpi.bean.SGS;
import com.eversec.lte.kpi.coll.ByteValue;
import com.eversec.lte.kpi.config.KPIConfig;
import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author jiangkui
 * kpi预统计 处理simple报给kafka的原生xdr
 * 同时处理用户面，信令面。 
 */
public class KafkaConsumerXdrSignalling implements KafkaConsumerHandler {

	private final static BlockingQueue<S1U_Value> queue;
	private final static Thread worker;
	private int xdr_1, xdr_2, xdr_5, xdr_6, xdr_7, xdr_9, xdr_other;
	private int s1u_1, s1u_2, s1u_3, s1u_5, s1u_6, s1u_7, s1u_8, s1u_9;
	private boolean count_work;
	
	private Thread countWork;
	private long get_count_second = 0L;
	SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		queue = new ArrayBlockingQueue<S1U_Value>(KPIConfig.getMiddleCacheSize(),true);
		
		worker = new Thread(new Runnable() {
			
			private Map<Long, Map<ByteValue, S1U_Value>> total;
			BufferedOutputStream s1mme_write_1;
			BufferedOutputStream s1mme_write_2;
			BufferedOutputStream s1mme_write_4;
			BufferedOutputStream s1mme_write_2526;
			BufferedOutputStream s1mme_write_29;
			BufferedOutputStream s1mme_write_30;
			BufferedOutputStream s1mme_write_31;
			BufferedOutputStream s1mme_write_6;
			BufferedOutputStream s1mme_write_17;
			BufferedOutputStream s1mme_write_9;
			BufferedOutputStream s1mme_write_10;
			BufferedOutputStream s1mme_write_11;
			BufferedOutputStream s1mme_write_12;
			BufferedOutputStream s1mme_write_13;
			BufferedOutputStream s1mme_write_21;
			BufferedOutputStream s1mme_write_7;
			BufferedOutputStream s1mme_write_8;
			BufferedOutputStream s1mme_write_5;
			BufferedOutputStream s1mme_write_1516;
			BufferedOutputStream s1mme_write_100;
			
			BufferedOutputStream s6a_write_1;
			BufferedOutputStream s6a_write_2;
			BufferedOutputStream s6a_write_6;
			BufferedOutputStream s6a_write_3;
			BufferedOutputStream s6a_write_8;
			BufferedOutputStream sgs_write_1;
			BufferedOutputStream sgs_write_5;
			BufferedOutputStream sgs_write_910;
			BufferedOutputStream s11_write_1;
			BufferedOutputStream s11_write_3;
			BufferedOutputStream s11_write_7;
			BufferedOutputStream s11_write_8;
			BufferedOutputStream s11_write_2;
			BufferedOutputStream s11_write_9;
			BufferedOutputStream s11_write_10;
			
			BufferedOutputStream s1u_tcplink_write_1;
			BufferedOutputStream s1u_tcplink_write_2;
			BufferedOutputStream s1u_tcplink_write_3;
			BufferedOutputStream s1u_tcplink_write_4;
			BufferedOutputStream s1u_tcplink_write_5;
			BufferedOutputStream s1u_tcplink_write_6;
			BufferedOutputStream s1u_tcplink_write_7;
			BufferedOutputStream s1u_tcplink_write_8;
			BufferedOutputStream s1u_tcplink_write_9;
			
			
			

			@Override
			public void run() {
				total = new HashMap<Long, Map<ByteValue, S1U_Value>>();
				ConvertUtils con = new ConvertUtils();
				//最大缓冲块
				int max = KPIConfig.getBlockNum();
				if (max < 2){
					max = 2;
				}
				S1U_Value data = null;
				long min = 0l;

				System.out.println("[SIGNAL WORKER RUNING 运行... ]");
				while (true) {
					try {
						synchronized (queue) {
							if ((data = queue.poll()) == null) {
								queue.wait();
							}
						}
						if (data != null) {
							byte[] b = data.getData();
							long delay_sum = data.getDelay_sum();
							int req_num = data.getReq_num();
							long time;
							int type = data.getType();
							if(type == 0){
								//当type==0时，是控制面数据 
								time = con.getLong(new byte[] { b[2], b[3],b[4], b[5], b[6], b[7], b[8], b[9]});
							}else{
								time = con.getLong(new byte[] { b[0], b[1],b[2], b[3], b[4], b[5], b[6], b[7]});
							}
							
							if (time >= min) {
								Map<ByteValue, S1U_Value> values = total.get(Long.valueOf(time));
								ByteValue bv = new ByteValue(b);

								if (values != null) {
									S1U_Value value = values.get(bv);
									if (value != null) {
										value.addDelay_sum(delay_sum);
										value.addReq_num(req_num);
									} else {
										values.put(bv, data);
									}
								} else {
									Map<ByteValue, S1U_Value> v = new HashMap<ByteValue, S1U_Value>();
									v.put(bv, data);
									total.put(Long.valueOf(time), v);
								}
							} else {
								//TODO
								//System.out.println("drop " + time);
							}
						}

						if (total.size() > max) {
							Object[] arr = total.keySet().toArray();
							Arrays.sort(arr);
							Long outTime = (Long) arr[0];
							min = (Long) arr[1];
							Map<ByteValue, S1U_Value> outData = total.remove(outTime);
							flush(outData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			
			/** 文件输出   
			 * 将缓冲区内的数据data写入硬盘,生产txt文件
			 * @param outData
			 * @throws Exception
			 */
			public void flush(Map<ByteValue, S1U_Value> outData) throws Exception {
				System.out.println("[Flush data 4G topic xdr ...]");
				String curr_date = null;
				String flowUrl = null;
				Iterator<S1U_Value> iter = outData.values().iterator();
				while (iter.hasNext()) {
					S1U_Value v = iter.next();
					int type = v.getType();
					if(type == 0){
						byte[] data = v.getData();
						long delay = v.getDelay_sum();
						int count = v.getReq_num();
						
						if (data[0] == 5) {
							if (data[1] == 1) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_1 = write(s1mme_write_1,
										KPIConfig.getS1mmeOutFile(1) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 2) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_2 = write(s1mme_write_2,
										KPIConfig.getS1mmeOutFile(2) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 4) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_4 = write(s1mme_write_4,
										KPIConfig.getS1mmeOutFile(4) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 25 || data[1] == 26) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_2526 = write(
										s1mme_write_2526,
										KPIConfig.getS1mmeOutFile(2526) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 29) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_29 = write(s1mme_write_29,
										KPIConfig.getS1mmeOutFile(29) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 30) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_30 = write(s1mme_write_30,
										KPIConfig.getS1mmeOutFile(30) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 31) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_31 = write(s1mme_write_31,
										KPIConfig.getS1mmeOutFile(31) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 6) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_6 = write(s1mme_write_6,
										KPIConfig.getS1mmeOutFile(6) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 17) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_17 = write(s1mme_write_17,
										KPIConfig.getS1mmeOutFile(17) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 9) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_9 = write(s1mme_write_9,
										KPIConfig.getS1mmeOutFile(9) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 10) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_10 = write(s1mme_write_10,
										KPIConfig.getS1mmeOutFile(10) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 11) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_11 = write(s1mme_write_11,
										KPIConfig.getS1mmeOutFile(11) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 12) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_12 = write(s1mme_write_12,
										KPIConfig.getS1mmeOutFile(12) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 13) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_13 = write(s1mme_write_13,
										KPIConfig.getS1mmeOutFile(13) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 21) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_21 = write(s1mme_write_21,
										KPIConfig.getS1mmeOutFile(21) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 7) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_7 = write(s1mme_write_7,
										KPIConfig.getS1mmeOutFile(7) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 8) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_8 = write(s1mme_write_8,
										KPIConfig.getS1mmeOutFile(8) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 5) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_5 = write(s1mme_write_5,
										KPIConfig.getS1mmeOutFile(5) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 15 || data[1] == 16) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_1516 = write(
										s1mme_write_1516,
										KPIConfig.getS1mmeOutFile(1516) + curr_date,
										s1mme.toString().getBytes());
							} else if (data[1] == 100) {
								S1MME s1mme = new S1MME(data, delay, count);
								if (curr_date == null)
									curr_date = s1mme.getStartDate();
								s1mme_write_100 = write(
										s1mme_write_100,
										KPIConfig.getS1mmeOutFile(100) + curr_date,
										s1mme.toString().getBytes());
							}
						} else if (data[0] == 6) {
							if (data[1] == 1) {
								S6A s6a = new S6A(data, delay, count);
								if (curr_date == null)
									curr_date = s6a.getStartDate();
								s6a_write_1 = write(s6a_write_1,
										KPIConfig.getS6aOutFile(1) + curr_date, s6a
												.toString().getBytes());
							} else if (data[1] == 2) {
								S6A s6a = new S6A(data, delay, count);
								if (curr_date == null)
									curr_date = s6a.getStartDate();
								s6a_write_2 = write(s6a_write_2,
										KPIConfig.getS6aOutFile(2) + curr_date, s6a
												.toString().getBytes());
							} else if (data[1] == 6) {
								S6A s6a = new S6A(data, delay, count);
								if (curr_date == null)
									curr_date = s6a.getStartDate();
								s6a_write_6 = write(s6a_write_6,
										KPIConfig.getS6aOutFile(6) + curr_date, s6a
												.toString().getBytes());
							} else if (data[1] == 3) {
								S6A s6a = new S6A(data, delay, count);
								if (curr_date == null)
									curr_date = s6a.getStartDate();
								s6a_write_3 = write(s6a_write_3,
										KPIConfig.getS6aOutFile(3) + curr_date, s6a
												.toString().getBytes());
							} else if (data[1] == 8) {
								S6A s6a = new S6A(data, delay, count);
								if (curr_date == null)
									curr_date = s6a.getStartDate();
								s6a_write_8 = write(s6a_write_8,
										KPIConfig.getS6aOutFile(8) + curr_date, s6a
												.toString().getBytes());
							}
						} else if (data[0] == 7) {
							if (data[1] == 1) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_1 = write(s11_write_1,
										KPIConfig.getS11OutFile(1) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 3) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_3 = write(s11_write_3,
										KPIConfig.getS11OutFile(3) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 7) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_7 = write(s11_write_7,
										KPIConfig.getS11OutFile(7) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 8) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_8 = write(s11_write_8,
										KPIConfig.getS11OutFile(8) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 2) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_2 = write(s11_write_2,
										KPIConfig.getS11OutFile(2) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 9) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_9 = write(s11_write_9,
										KPIConfig.getS11OutFile(9) + curr_date, s11
												.toString().getBytes());
							} else if (data[1] == 10) {
								S11 s11 = new S11(data, delay, count);
								if (curr_date == null)
									curr_date = s11.getStartDate();
								s11_write_10 = write(s11_write_10,
										KPIConfig.getS11OutFile(10) + curr_date,
										s11.toString().getBytes());
							}
						} else if (data[0] == 9) {
							if (data[1] == 1) {
								SGS sgs = new SGS(data, delay, count);
								if (curr_date == null)
									sgs.getStartDate();
								sgs_write_1 = write(sgs_write_1,
										KPIConfig.getSgsOutFile(1) + curr_date, sgs
												.toString().getBytes());
							} else if (data[1] == 5) {
								SGS sgs = new SGS(data, delay, count);
								if (curr_date == null)
									sgs.getStartDate();
								sgs_write_5 = write(sgs_write_5,
										KPIConfig.getSgsOutFile(5) + curr_date, sgs
												.toString().getBytes());
							} else if (data[1] == 9 || data[1] == 10) {
								SGS sgs = new SGS(data, delay, count);
								if (curr_date == null)
									sgs.getStartDate();
								sgs_write_910 = write(sgs_write_910,
										KPIConfig.getSgsOutFile(910) + curr_date,
										sgs.toString().getBytes());
							}
						}
					} else {
						//TODO
						S1U s1u = new S1U(v);
						if (type == 1) {
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_1 = write(s1u_tcplink_write_1,
									KPIConfig.getS1uOutFile(1) + curr_date, s1u
											.toString().getBytes());
						} else if (type == 2) {
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_2 = write(s1u_tcplink_write_2,
									KPIConfig.getS1uOutFile(2) + curr_date, s1u
											.toString().getBytes());
						} else if (type == 3) {
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_3 = write(s1u_tcplink_write_3,
									KPIConfig.getS1uOutFile(3) + curr_date, s1u
											.toString().getBytes());
						} else if(type == 4){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_4 = write(s1u_tcplink_write_4,
									KPIConfig.getS1uOutFile(4) + curr_date, s1u
											.toString().getBytes());
						} else if(type == 5){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
								String url = KPIConfig.getS1uOutFile(5);
								int flowid = url.indexOf("flow") + 4;
								flowUrl = url.substring(0, flowid) + "/flow_" + curr_date.substring(0, 8) 
										+ url.substring(flowid, url.length());
							s1u_tcplink_write_5 = write(s1u_tcplink_write_5, flowUrl + curr_date, s1u
											.toString().getBytes());
						} else if(type == 6){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_6 = write(s1u_tcplink_write_6,
									KPIConfig.getS1uOutFile(6) + curr_date, s1u
											.toString().getBytes());
						} else if(type == 7){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_7 = write(s1u_tcplink_write_7,
									KPIConfig.getS1uOutFile(7) + curr_date, s1u
											.toString().getBytes());
						} else if(type == 8){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_8 = write(s1u_tcplink_write_8,
									KPIConfig.getS1uOutFile(8) + curr_date, s1u
											.toString().getBytes());
						} else if(type == 9){
							if (curr_date == null)
								curr_date = s1u.getStartDate();
							s1u_tcplink_write_9 = write(s1u_tcplink_write_9,
									KPIConfig.getS1uOutFile(9) + curr_date, s1u
											.toString().getBytes());
						}
					}
				}

				if (s1mme_write_1 != null) {
					close(s1mme_write_1, KPIConfig.getS1mmeOutFile(1)
							+ curr_date);
					s1mme_write_1 = null;
				}

				if (s1mme_write_2 != null) {
					close(s1mme_write_2, KPIConfig.getS1mmeOutFile(2)
							+ curr_date);
					s1mme_write_2 = null;
				}

				if (s1mme_write_4 != null) {
					close(s1mme_write_4, KPIConfig.getS1mmeOutFile(4)
							+ curr_date);
					s1mme_write_4 = null;
				}

				if (s1mme_write_2526 != null) {
					close(s1mme_write_2526, KPIConfig.getS1mmeOutFile(2526)
							+ curr_date);
					s1mme_write_2526 = null;
				}

				if (s1mme_write_29 != null) {
					close(s1mme_write_29, KPIConfig.getS1mmeOutFile(29)
							+ curr_date);
					s1mme_write_29 = null;
				}

				if (s1mme_write_30 != null) {
					close(s1mme_write_30, KPIConfig.getS1mmeOutFile(30)
							+ curr_date);
					s1mme_write_30 = null;
				}

				if (s1mme_write_31 != null) {
					close(s1mme_write_31, KPIConfig.getS1mmeOutFile(31)
							+ curr_date);
					s1mme_write_31 = null;
				}

				if (s1mme_write_6 != null) {
					close(s1mme_write_6, KPIConfig.getS1mmeOutFile(6)
							+ curr_date);
					s1mme_write_6 = null;
				}

				if (s1mme_write_17 != null) {
					close(s1mme_write_17, KPIConfig.getS1mmeOutFile(17)
							+ curr_date);
					s1mme_write_17 = null;
				}

				if (s1mme_write_9 != null) {
					close(s1mme_write_9, KPIConfig.getS1mmeOutFile(9)
							+ curr_date);
					s1mme_write_9 = null;
				}

				if (s1mme_write_10 != null) {
					close(s1mme_write_10, KPIConfig.getS1mmeOutFile(10)
							+ curr_date);
					s1mme_write_10 = null;
				}

				if (s1mme_write_11 != null) {
					close(s1mme_write_11, KPIConfig.getS1mmeOutFile(11)
							+ curr_date);
					s1mme_write_11 = null;
				}

				if (s1mme_write_12 != null) {
					close(s1mme_write_12, KPIConfig.getS1mmeOutFile(12)
							+ curr_date);
					s1mme_write_12 = null;
				}

				if (s1mme_write_13 != null) {
					close(s1mme_write_13, KPIConfig.getS1mmeOutFile(13)
							+ curr_date);
					s1mme_write_13 = null;
				}

				if (s1mme_write_21 != null) {
					close(s1mme_write_21, KPIConfig.getS1mmeOutFile(21)
							+ curr_date);
					s1mme_write_21 = null;
				}

				if (s1mme_write_7 != null) {
					close(s1mme_write_7, KPIConfig.getS1mmeOutFile(7)
							+ curr_date);
					s1mme_write_7 = null;
				}

				if (s1mme_write_8 != null) {
					close(s1mme_write_8, KPIConfig.getS1mmeOutFile(8)
							+ curr_date);
					s1mme_write_8 = null;
				}

				if (s1mme_write_5 != null) {
					close(s1mme_write_5, KPIConfig.getS1mmeOutFile(5)
							+ curr_date);
					s1mme_write_5 = null;
				}

				if (s1mme_write_1516 != null) {
					close(s1mme_write_1516, KPIConfig.getS1mmeOutFile(1516)
							+ curr_date);
					s1mme_write_1516 = null;
				}

				if (s1mme_write_100 != null) {
					close(s1mme_write_100, KPIConfig.getS1mmeOutFile(100)
							+ curr_date);
					s1mme_write_100 = null;
				}

				if (s6a_write_1 != null) {
					close(s6a_write_1, KPIConfig.getS6aOutFile(1) + curr_date);
					s6a_write_1 = null;
				}

				if (s6a_write_2 != null) {
					close(s6a_write_2, KPIConfig.getS6aOutFile(2) + curr_date);
					s6a_write_2 = null;
				}

				if (s6a_write_6 != null) {
					close(s6a_write_6, KPIConfig.getS6aOutFile(6) + curr_date);
					s6a_write_6 = null;
				}

				if (s6a_write_3 != null) {
					close(s6a_write_3, KPIConfig.getS6aOutFile(3) + curr_date);
					s6a_write_3 = null;
				}

				if (s6a_write_8 != null) {
					close(s6a_write_8, KPIConfig.getS6aOutFile(8) + curr_date);
					s6a_write_8 = null;
				}

				if (sgs_write_1 != null) {
					close(sgs_write_1, KPIConfig.getSgsOutFile(1) + curr_date);
					sgs_write_1 = null;
				}

				if (sgs_write_5 != null) {
					close(sgs_write_5, KPIConfig.getSgsOutFile(5) + curr_date);
					sgs_write_5 = null;
				}

				if (sgs_write_910 != null) {
					close(sgs_write_910, KPIConfig.getSgsOutFile(910)
							+ curr_date);
					sgs_write_910 = null;
				}

				if (s11_write_1 != null) {
					close(s11_write_1, KPIConfig.getS11OutFile(1) + curr_date);
					s11_write_1 = null;
				}

				if (s11_write_3 != null) {
					close(s11_write_3, KPIConfig.getS11OutFile(3) + curr_date);
					s11_write_3 = null;
				}

				if (s11_write_7 != null) {
					close(s11_write_7, KPIConfig.getS11OutFile(7) + curr_date);
					s11_write_7 = null;
				}

				if (s11_write_8 != null) {
					close(s11_write_8, KPIConfig.getS11OutFile(8) + curr_date);
					s11_write_8 = null;
				}

				if (s11_write_2 != null) {
					close(s11_write_2, KPIConfig.getS11OutFile(2) + curr_date);
					s11_write_2 = null;
				}

				if (s11_write_9 != null) {
					close(s11_write_9, KPIConfig.getS11OutFile(9) + curr_date);
					s11_write_9 = null;
				}

				if (s11_write_10 != null) {
					close(s11_write_10, KPIConfig.getS11OutFile(10) + curr_date);
					s11_write_10 = null;
				}
				
				if (s1u_tcplink_write_1 != null) {
					close(s1u_tcplink_write_1, KPIConfig.getS1uOutFile(1)
							+ curr_date);
					s1u_tcplink_write_1 = null;
				}

				if (s1u_tcplink_write_2 != null) {
					close(s1u_tcplink_write_2, KPIConfig.getS1uOutFile(2)
							+ curr_date);
					s1u_tcplink_write_2 = null;
				}

				if (s1u_tcplink_write_3 != null) {
					close(s1u_tcplink_write_3, KPIConfig.getS1uOutFile(3)
							+ curr_date);
					s1u_tcplink_write_3 = null;
				}
				
				if (s1u_tcplink_write_4 != null) {
					close(s1u_tcplink_write_4, KPIConfig.getS1uOutFile(4)
							+ curr_date);
					s1u_tcplink_write_4 = null;
				}
				
				if (s1u_tcplink_write_5 != null) {
					close(s1u_tcplink_write_5, flowUrl + curr_date);
					s1u_tcplink_write_5 = null;
				}
				
				if (s1u_tcplink_write_6 != null) {
					close(s1u_tcplink_write_6, KPIConfig.getS1uOutFile(6)
							+ curr_date );
					s1u_tcplink_write_6 = null;
				}
				
				if (s1u_tcplink_write_7 != null) {
					close(s1u_tcplink_write_7, KPIConfig.getS1uOutFile(7)
							+ curr_date );
					s1u_tcplink_write_7 = null;
				}
				
				if (s1u_tcplink_write_8 != null) {
					close(s1u_tcplink_write_8, KPIConfig.getS1uOutFile(8)
							+ curr_date );
					s1u_tcplink_write_8 = null;
				}
				
				if (s1u_tcplink_write_9 != null) {
					close(s1u_tcplink_write_9, KPIConfig.getS1uOutFile(9)
							+ curr_date );
					s1u_tcplink_write_9 = null;
				}
				
			}

			public BufferedOutputStream write(BufferedOutputStream out,
					String path, byte[] data) throws Exception {
				if (out == null) {
					File f = new File(path);
					if (!f.getParentFile().exists())
						f.getParentFile().mkdirs();
					out = new BufferedOutputStream(new FileOutputStream(f));
				}
				out.write(data);
				return out;
			}

			public void close(BufferedOutputStream out, String path)
					throws Exception {
				out.flush();
				out.close();
				File target = new File(path);
				target.renameTo(new File(target.getPath() + ".txt"));
			}
		});

		worker.start();
	}

	private ConvertUtils con;

	private LoadingCache<ByteValue, S1U_Value> cache;

	private String id;

	public KafkaConsumerXdrSignalling(final String id) {
		this.id = id;
		con = new ConvertUtils();
		initCache();
		this.count_work = KPIConfig.getCountWork();
		if (this.count_work) {
			this.countWork = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							TimeUnit.MINUTES.sleep(1);
							showCount(DateFormat.format(new Date()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.countWork.start();
		}
		System.out.println("[READ WORKING...][" + this.hashCode() + "]");
	}

	public void initCache() {
		CacheLoader<ByteValue, S1U_Value> loader = new CacheLoader<ByteValue, S1U_Value>() {
			@Override
			public S1U_Value load(ByteValue key) throws Exception {
				return new S1U_Value();
			}
		};
		RemovalListener<ByteValue, S1U_Value> listener = new RemovalListener<ByteValue, S1U_Value>() {
			@Override
			public void onRemoval(RemovalNotification<ByteValue, S1U_Value> data) {
				try {
					queue.put(data.getValue());
					synchronized (queue) {
						queue.notify();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		cache = CacheBuilder
				.newBuilder()
				.removalListener(listener)
				.expireAfterWrite(KPIConfig.getTimeFlashInterval(),
						TimeUnit.MILLISECONDS)
				.maximumSize(KPIConfig.getTimeCacheSize()).build(loader);
	}

	@Override
	public void messageReceived(byte[] message) throws Exception {
		
		//System.out.println("[date coming]...."+new Date().getTime());
		filterAndFetch(message);
		get_count_second++;
		// Thread.sleep(1000);
	}

	public void filterAndFetch(byte[] data) {
		int len = data.length;
		int cursor = -1;
		cursor += 9;
		while ((len - cursor) >= 55) {
			// length
			int length = con.getUnsigendShort(new byte[] { data[++cursor],
					data[++cursor] });
			cursor += 2;
			// interface
			int interf = data[++cursor] & 0xFF;
			
			//TODO
			if (interf == 11) {// s1u
				//cursor += length - 5;
				
				
				/**
				 *  flow 处理
				 */
				//createFlow(data, cursor);
				
				// ipv4 or ipv6
				cursor += 49;
				int ip_type = data[++cursor] & 0xFF;
				// 统一成ipv6格式,即使实际为ipv4,剩余位置补0xff
				ip_type = 2;
				int ex = ip_type == 1 ? 0 : 24;

				// 业务类型
				cursor += (58 + ex);
				int apptype_code = data[++cursor] & 0xFF; //apptype_code
				// 协议类型
				cursor += 46;
				int type = data[++cursor] & 0xFF; //l4_protocal
				// 指针复位
				cursor -= (161 + ex);
				
				/**
				 * 公共字段
				 */
				byte[] pub = new byte[70];

				// 填充startdate
				cursor += (114 + ex);
				long startdate = con.getLong(new byte[] { data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 颗粒度分钟
				long timebymin = con.time2min(startdate);
				byte[] min = con.getBytes(timebymin);
				pub[0] = min[0];
				pub[1] = min[1];
				pub[2] = min[2];
				pub[3] = min[3];
				pub[4] = min[4];
				pub[5] = min[5];
				pub[6] = min[6];
				pub[7] = min[7];

				long enddate = con.getLong(new byte[] { data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });

				// 填充city
				cursor -= (128 + ex);
				pub[8] = data[++cursor];
				pub[9] = data[++cursor];

				// 填充ip
				cursor += 51;
				pub[10] = (byte) ip_type;
				if (ip_type == 1) {
					pub[11] = data[++cursor];
					pub[12] = data[++cursor];
					pub[13] = data[++cursor];
					pub[14] = data[++cursor];

					pub[15] = data[++cursor];
					pub[16] = data[++cursor];
					pub[17] = data[++cursor];
					pub[18] = data[++cursor];
				} else {
					for (int i = 11; i < 43; i++)
						pub[i] = data[++cursor];
				}

				// 填充tac
				cursor += 12;
				pub[43] = data[++cursor];
				pub[44] = data[++cursor];
				// 填充cellid
				pub[45] = data[++cursor];
				pub[46] = data[++cursor];
				pub[47] = data[++cursor];
				pub[48] = data[++cursor];
				// 填充appserver_ipv4
				cursor += 80;
				pub[49] = data[++cursor];
				pub[50] = data[++cursor];
				pub[51] = data[++cursor];
				pub[52] = data[++cursor];

				// 填充appserver_ipv6
				for (int i = 53; i < 69; i++)
					pub[i] = data[++cursor];
				//填充tcplink_state
				cursor += 67;
				pub[69] = data[++cursor];

				// 指针复位
				cursor -= (249 + ex);
				
				/**
				 * S1U_XDR_M_TCPLINK_STAT
				 */
				if (type == 0) {
					if (count_work)
						s1u_1++;
					
					//排除 HTTP 103,MMS 102,EMAIL 105
					//不推送数据 就排除掉了.
					//if(apptype_code != 103 && apptype_code != 102 && apptype_code != 105){
						byte[] tcpLink = new byte[54];
						for (int i = 0; i < 49; i++)
							tcpLink[i] = pub[i];
						
						cursor += 156;
						//apptype
						tcpLink[49] =  data[++cursor];
						tcpLink[50] =  data[++cursor];
						//appsubtype
						tcpLink[51] =  data[++cursor];
						tcpLink[52] =  data[++cursor];
						//
						
						
						cursor += 79;
						// ultcp_responsetime
						long ultcp_responsetime = con.getUnsigendInt(new byte[] {
								data[++cursor], data[++cursor], data[++cursor],
								data[++cursor] });
						// dltcp_responsetime
						long dltcp_responsetime = con.getUnsigendInt(new byte[] {
								data[++cursor], data[++cursor], data[++cursor],
								data[++cursor] });
						
						cursor += 24;
						// tcplink_count
						int tcplink_count = data[++cursor] & 0xFF;
						// status   (tcplink_state)
						int status = data[++cursor] & 0xFF;
						tcpLink[53] = data[cursor];
						// 推送数据
						putTCPLinkData(tcpLink, status == 0 ? (ultcp_responsetime + dltcp_responsetime) : 0, 1);
						// 指针复位
						cursor -= 273;
					//} 
					
						/**
						 * S1U_XDR_M_TCPPACKET_STAT
						 */
						if(tcplink_count > 0){
							byte[] tcpPacket = new byte[53];
							for (int i = 0; i < 49; i++)
								tcpPacket[i] = pub[i];
							
							cursor += (132 + ex);
							//apptype
							tcpPacket[49] =  data[++cursor];
							tcpPacket[50] =  data[++cursor];
							//appsubtype
							tcpPacket[51] =  data[++cursor];
							tcpPacket[52] =  data[++cursor];
							
							cursor += 55;
							//ultcp_packets
							long ultcp_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							//dltcp_packets
							long dltcp_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							//ultcp_disord_packets
							long ultcp_disord_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							//dltcp_disord_packets
							long dltcp_disord_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							//ultcp_retrans_packets
							long ultcp_retrans_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							//dltcp_retrans_packets
							long dltcp_retrans_packets = con.getUnsigendInt(new byte[] {
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							
							// 推送数据
							putTcpPacketData(tcpPacket, ultcp_packets, dltcp_packets, ultcp_disord_packets, dltcp_disord_packets, ultcp_retrans_packets, dltcp_retrans_packets);
							// 指针复位
							cursor -= 239 ;
						}
				}
				
				if (apptype_code == 101) {    //DNS
					byte[] dns = new byte[70];
					for (int i = 0; i < 69; i++)
						dns[i] = pub[i];
					// 填充dnscode
					cursor += 329 + ex;
					dns[69] = data[++cursor];
					// 时延
					long dns_delay = dns[69] == 0 ? (enddate - startdate) : 0;
					putDnsData(dns, dns_delay, 1);
					if (count_work)
						s1u_2++;
					// 指针复位
					cursor -= (330 + ex);
				} else if (apptype_code == 103) { //HTTP
					
					byte[] http = new byte[55];
					for (int i = 0; i < 49; i++)
						http[i] = pub[i];
					// 填充apptype
					cursor += (132 + ex);
					http[49] = data[++cursor];
					http[50] = data[++cursor];
					// 填充appsubtype
					http[51] = data[++cursor];
					http[52] = data[++cursor];
					// ulthroughput、dlthroughput、ulpackets、dlpackets
					cursor += 47;
					long ulthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					long dlthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					long ulpackets = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					long dlpackets = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// 填充code
					cursor += 54;
					http[53] = data[++cursor];
					http[54] = data[++cursor];
					int code = con.getUnsigendShort(new byte[] {http[53], http[54] }); 
					
					// responsetime
					long responsetime = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					//lastpacket_delay
					long lastpacket_delay = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					
					// 指针复位
					cursor -= 287;
					
					long ulthroughput2 = 0;
					long dlthroughput2 = 0;
					long delay_sum = 0;
					int rate_num = 0;
					if (code>0 && code<400 && lastpacket_delay >= responsetime) {
						ulthroughput2 = ulthroughput;
						dlthroughput2 = dlthroughput;
						delay_sum = enddate - startdate;
						//速率
						long time = enddate-startdate;
						if(time <= 0){
							time = 1;
						}
						if(dlthroughput*1000*8/1024/1024/time<2){
							rate_num = 1;
						}
					} else {
						responsetime = 0;
					}
					putHttpData(http, ulthroughput, dlthroughput, ulpackets, dlpackets, 
							responsetime, ulthroughput2, dlthroughput2, delay_sum, rate_num);
					if (count_work)
						s1u_3++;
				}
				
				/**
				 * 		AppStore  IM        
				 *      AppLarge HttpLarge
				 */
				if(apptype_code == 103){
					createAppStore(data, cursor, pub, startdate, enddate);
					createIM(data, cursor, pub, startdate, enddate);
					
					cursor += 211;
					long dlthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					cursor -= 215;
					if(dlthroughput >= 512000){
						createHttpLarge(data, cursor, pub, startdate, enddate);
						createAppLarge(data, cursor, pub, startdate, enddate);
					}
				}
				
				// 跳到尾部
				cursor += length;
			} 
			else {
				
				cursor += length - 5;
				
				
				//TODO
//				// type
//				cursor += 49;
//				int type = data[++cursor] & 0xFF;
//				if (interf == 1) {// UU
//					if (count_work)
//						xdr_1++;
//					cursor += 55;
//					int epsNum = data[++cursor] & 0xFF;
//					// 跳到xdr尾部
//					cursor += (2 * epsNum);
//				} else if (interf == 2) {  // X2
//					if (count_work)
//						xdr_2++;
//					cursor += 44;
//					int epsNum = data[++cursor] & 0xFF;
//					// 跳到xdr尾部
//					cursor += (2 * epsNum);
//				} else if (interf == 5) {    // S1mme
//					if (count_work)
//						xdr_5++;
//					if (type == 1 || type == 2 || type == 4 || type == 25
//							|| type == 26 || type == 29 || type == 30 || type == 31
//							|| type == 6 || type == 17 || type == 9 || type == 10
//							|| type == 11 || type == 12 || type == 13 || type == 21
//							|| type == 7 || type == 8 || type == 5 || type == 15
//							|| type == 16||type == 100) {
//						
//						if(type == 100){
//							System.out.println(type);
//						}
//						
//						
//						
//						// System.out.println("[interf 5]");
//						byte[] s1mme = new byte[95];
//						// 填充interface
//						s1mme[0] = 5;
//						// 填充 type
//						s1mme[1] = (byte) type;
//						// 填充 city
//						cursor -= 53;
//						s1mme[10] = data[++cursor];
//						s1mme[11] = data[++cursor];
//						cursor += 51;
//						// startdate
//						long startdate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// endate
//						long enddate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// 颗粒度分钟
//						long timebymin = con.time2min(startdate);
//						byte[] min = con.getBytes(timebymin);
//						// 填充其他字段
//						// startdate(精确到分钟)
//						s1mme[2] = min[0];
//						s1mme[3] = min[1];
//						s1mme[4] = min[2];
//						s1mme[5] = min[3];
//						s1mme[6] = min[4];
//						s1mme[7] = min[5];
//						s1mme[8] = min[6];
//						s1mme[9] = min[7];
//						// status
//						s1mme[12] = data[++cursor];
//						// request cause
//						s1mme[54] = data[++cursor];
//						s1mme[55] = data[++cursor];
//						// failure cause
//						s1mme[13] = data[++cursor];
//						s1mme[14] = data[++cursor];
//						// keyword1
//						s1mme[15] = data[++cursor];
//						// keyword2
//						s1mme[56] = data[++cursor];
//						// mme ip
//						cursor += 44;
//						for (int i = 16; i < 32; i++)
//							s1mme[i] = data[++cursor];
//						// enb ip
//						for (int i = 32; i < 48; i++)
//							s1mme[i] = data[++cursor];
//						// tac
//						cursor += 4;
//						s1mme[48] = data[++cursor];
//						s1mme[49] = data[++cursor];
//						// cellid
//						s1mme[50] = data[++cursor];
//						s1mme[51] = data[++cursor];
//						s1mme[52] = data[++cursor];
//						s1mme[53] = data[++cursor];
//						// other tac
//						s1mme[89] = data[++cursor];
//						s1mme[90] = data[++cursor];
//						// other cellid
//						s1mme[91] = data[++cursor];
//						s1mme[92] = data[++cursor];
//						s1mme[93] = data[++cursor];
//						s1mme[94] = data[++cursor];
//						// apn
//						for (int i = 57; i < 89; i++)
//							s1mme[i] = data[++cursor];
//						// 计算时延
//						long delay = (s1mme[12] == 0) ? (enddate - startdate) : 0;
//
//						// 根据不同type需要不同字段作为组成员
//						createS1mmeKeyByType(s1mme);
//						// 推送数据
//						putData(s1mme, delay);
//						// 得到eps承载数
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += (16 * epsNum);
//					} else {
//						cursor += 147;
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += (16 * epsNum);
//					}
//				} else if (interf == 6) {   //S6a
//					if (count_work)
//						xdr_6++;
//					if (type == 1 || type == 2 || type == 6 || type == 3
//							|| type == 8) {
//						byte[] s6a = new byte[263];
//						// 填充interface
//						s6a[0] = 6;
//						// 填充 type
//						s6a[1] = (byte) type;
//						// 填充 city
//						cursor -= 53;
//						s6a[10] = data[++cursor];
//						s6a[11] = data[++cursor];
//						cursor += 51;
//						// startdate
//						long startdate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// endate
//						long enddate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// 颗粒度分钟
//						long timebymin = con.time2min(startdate);
//						byte[] min = con.getBytes(timebymin);
//						// 填充其他字段
//						// startdate(精确到分钟)
//						s6a[2] = min[0];
//						s6a[3] = min[1];
//						s6a[4] = min[2];
//						s6a[5] = min[3];
//						s6a[6] = min[4];
//						s6a[7] = min[5];
//						s6a[8] = min[6];
//						s6a[9] = min[7];
//						// status
//						s6a[262] = data[++cursor];
//						// failurecause
//						s6a[260] = data[++cursor];
//						s6a[261] = data[++cursor];
//						// mmeip
//						cursor += 20;
//						for (int i = 12; i < 28; i++)
//							s6a[i] = data[++cursor];
//						// hssip
//						for (int i = 28; i < 44; i++)
//							s6a[i] = data[++cursor];
//						// o_realm
//						cursor += 4;
//						for (int i = 44; i < 88; i++)
//							s6a[i] = data[++cursor];
//						// d_realm
//						for (int i = 88; i < 132; i++)
//							s6a[i] = data[++cursor];
//						// o_host
//						for (int i = 132; i < 196; i++)
//							s6a[i] = data[++cursor];
//						// d_host
//						for (int i = 196; i < 260; i++)
//							s6a[i] = data[++cursor];
//						createS6aKeyByType(s6a);
//						// 计算时延
//						long delay = (s6a[262] == 0) ? (enddate - startdate) : 0;
//						putData(s6a, delay);
//						// 跳到尾部
//						cursor += 6;
//					} else {
//						// 跳到尾部
//						cursor += 297;
//					}
//				} else if (interf == 7) { //S11接口
//					if (count_work)
//						xdr_7++;
//					if (type == 1 || type == 3 || type == 7 || type == 8
//							|| type == 2 || type == 9 || type == 10) {
//						byte[] s11 = new byte[81];
//						// 填充interface
//						s11[0] = 7;
//						// 填充 type
//						s11[1] = (byte) type;
//						// 填充 city
//						cursor -= 53;
//						s11[10] = data[++cursor];
//						s11[11] = data[++cursor];
//						cursor += 51;
//						// startdate
//						long startdate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// endate
//						long enddate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// 颗粒度分钟
//						long timebymin = con.time2min(startdate);
//						byte[] min = con.getBytes(timebymin);
//						// 填充其他字段
//						// startdate(精确到分钟)
//						s11[2] = min[0];
//						s11[3] = min[1];
//						s11[4] = min[2];
//						s11[5] = min[3];
//						s11[6] = min[4];
//						s11[7] = min[5];
//						s11[8] = min[6];
//						s11[9] = min[7];
//						// status
//						s11[80] = data[++cursor];
//						// failure_cause
//						s11[76] = data[++cursor];
//						s11[77] = data[++cursor];
//						// request_cause
//						s11[78] = data[++cursor];
//						s11[79] = data[++cursor];
//						// mmeip
//						cursor += 20;
//						for (int i = 12; i < 28; i++)
//							s11[i] = data[++cursor];
//						// oldmmeip
//						for (int i = 28; i < 44; i++)
//							s11[i] = data[++cursor];
//						// apn
//						cursor += 12;
//						for (int i = 44; i < 76; i++)
//							s11[i] = data[++cursor];
//
//						createS11KeyByType(s11);
//						// 计算时延
//						long delay = (s11[80] == 0) ? (enddate - startdate) : 0;
//						putData(s11, delay);
//
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += (12 * epsNum);
//					} else {
//						cursor += 117;
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += (12 * epsNum);
//					}
//				} else if (interf == 9) {    //SGS
//
//					if (count_work)
//						xdr_9++;
//
//					if (type == 1 || type == 5 || type == 9 || type == 10) {
//						byte[] sgs = new byte[58];
//						// 填充interface
//						sgs[0] = 9;
//						// 填充 type
//						sgs[1] = (byte) type;
//						// 填充 city
//						cursor -= 53;
//						sgs[10] = data[++cursor];
//						sgs[11] = data[++cursor];
//						cursor += 51;
//						// startdate
//						long startdate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// endate
//						long enddate = con.getLong(new byte[] { data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// 颗粒度分钟
//						long timebymin = con.time2min(startdate);
//						byte[] min = con.getBytes(timebymin);
//						// 填充其他字段
//						// startdate(精确到分钟)
//						sgs[2] = min[0];
//						sgs[3] = min[1];
//						sgs[4] = min[2];
//						sgs[5] = min[3];
//						sgs[6] = min[4];
//						sgs[7] = min[5];
//						sgs[8] = min[6];
//						sgs[9] = min[7];
//						// status
//						sgs[57] = data[++cursor];
//						// sgscause
//						sgs[56] = data[++cursor];
//						// rejectcause
//						sgs[54] = data[++cursor];
//						// mmeip
//						cursor += 22;
//						for (int i = 12; i < 28; i++)
//							sgs[i] = data[++cursor];
//						// mscip
//						for (int i = 34; i < 50; i++)
//							sgs[i] = data[++cursor];
//						// service_indicator
//						cursor += 4;
//						sgs[55] = data[++cursor];
//						// newlai
//						cursor += 59;
//						sgs[50] = data[++cursor];
//						sgs[51] = data[++cursor];
//						// oldlai
//						sgs[52] = data[++cursor];
//						sgs[53] = data[++cursor];
//						// tai
//						sgs[28] = data[++cursor];
//						sgs[29] = data[++cursor];
//						// cellid
//						sgs[30] = data[++cursor];
//						sgs[31] = data[++cursor];
//						sgs[32] = data[++cursor];
//						sgs[33] = data[++cursor];
//
//						createSgsKeyByType(sgs);
//
//						// 计算时延
//						long delay = (sgs[57] == 0) ? (enddate - startdate) : 0;
//						putData(sgs, delay);
//
//						cursor += 24;
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += epsNum;
//					} else {
//						cursor += 171;
//						int epsNum = data[++cursor] & 0xFF;
//						// 跳到xdr尾部
//						cursor += epsNum;
//					}
//				} else {
//					if (count_work)
//						xdr_other++;
//					System.out.println("[XDR ERR] interface:====" + interf);
//					break;
//				}
				
				
				
				
				
				//TODO
			}
			
			}
		}

		/**
		 * 清空对应字段的值设为0,得到实际想要的结果
		 * @param s1mme
		 */
		public void createS1mmeKeyByType(byte[] s1mme) {
			if (s1mme[1] == 1) {
				for (int i = 54; i < 95; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 2 || s1mme[1] == 25 || s1mme[1] == 26
					|| s1mme[1] == 29 || s1mme[1] == 30 || s1mme[1] == 31
					|| s1mme[1] == 9 || s1mme[1] == 10 || s1mme[1] == 12) {
				// keyword1
				s1mme[15] = 0;
				for (int i = 54; i < 95; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 4 || s1mme[1] == 21) {
				// failure cause
				s1mme[13] = 0;
				s1mme[14] = 0;
				for (int i = 54; i < 95; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 17 || s1mme[1] == 11) {
				// failure cause
				s1mme[13] = 0;
				s1mme[14] = 0;
				// keyword1
				s1mme[15] = 0;
				// keyword2 apn othertac othercell
				for (int i = 56; i < 95; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 6) {
				// failure cause
				s1mme[13] = 0;
				s1mme[14] = 0;
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				for (int i = 57; i < 95; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 13) {
				// keyword1
				s1mme[15] = 0;
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				// keyword2
				s1mme[56] = 0;
				// othertac
				s1mme[89] = 0;
				s1mme[90] = 0;
				// othercell
				s1mme[91] = 0;
				s1mme[92] = 0;
				s1mme[93] = 0;
				s1mme[94] = 0;

			} else if (s1mme[1] == 7) {
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				// keyword2
				s1mme[56] = 0;
				// othertac
				s1mme[89] = 0;
				s1mme[90] = 0;
				// othercell
				s1mme[91] = 0;
				s1mme[92] = 0;
				s1mme[93] = 0;
				s1mme[94] = 0;
			} else if (s1mme[1] == 8) {
				// keyword1
				s1mme[15] = 0;
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				// keyword2
				s1mme[56] = 0;
				// othertac
				s1mme[89] = 0;
				s1mme[90] = 0;
				// othercell
				s1mme[91] = 0;
				s1mme[92] = 0;
				s1mme[93] = 0;
				s1mme[94] = 0;
			} else if (s1mme[1] == 5) {
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				// keyword2
				s1mme[56] = 0;
				for (int i = 57; i < 89; i++)
					s1mme[i] = 0;
				// othercell
				s1mme[91] = 0;
				s1mme[92] = 0;
				s1mme[93] = 0;
				s1mme[94] = 0;

				// 根据keyword1重新分组
				if (s1mme[15] == 0 || s1mme[15] == 8)
					s1mme[15] = 1;
				else if (s1mme[15] == 3 || s1mme[15] == 11)
					s1mme[15] = 2;
				else if (s1mme[15] == 1 || s1mme[15] == 2 || s1mme[15] == 9
						|| s1mme[15] == 10)
					s1mme[15] = 3;
			} else if (s1mme[1] == 15 || s1mme[1] == 16) {
				// keyword2
				s1mme[56] = 0;
				// apn
				for (int i = 56; i < 89; i++)
					s1mme[i] = 0;
			} else if (s1mme[1] == 100) {
				// keyword1
				s1mme[15] = 0;
				// request_cause
				s1mme[54] = 0;
				s1mme[55] = 0;
				// keyword2
				s1mme[56] = 0;
				// othertac
				s1mme[89] = 0;
				s1mme[90] = 0;
				// othercell
				s1mme[91] = 0;
				s1mme[92] = 0;
				s1mme[93] = 0;
				s1mme[94] = 0;
			}
		}

	public void createS6aKeyByType(byte[] s6a) {

	}

	public void createSgsKeyByType(byte[] sgs) {
		if (sgs[1] == 1) {
			// oldlai
			sgs[52] = 0;
			sgs[53] = 0;
			// rejectcause
			sgs[54] = 0;
		} else if (sgs[1] == 5) {
			// service_indicator
			sgs[55] = 0;
			// sgscause
			sgs[56] = 0;
		} else if (sgs[1] == 9 || sgs[1] == 10) {
			// oldlai
			sgs[52] = 0;
			sgs[53] = 0;
			// rejectcause
			sgs[54] = 0;
			// service_indicator
			sgs[55] = 0;
		}
	}

	public void createS11KeyByType(byte[] s11) {

	}

	public void putData(byte[] key, long value) {
		ByteValue b = new ByteValue(key);
		S1U_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initKpi(key, value, 1, 0);
		} else {
			v.addDelay_sum(value);
			v.addReq_num(1);
		}
	}

	//TODO
	public void showCount(String date) {
		int c1 = xdr_1,c2 = xdr_2,c5 = xdr_5,c6 = xdr_6,c7 = xdr_7,c9 = xdr_9,c_other = xdr_other;
		xdr_1 = 0;
		xdr_2 = 0;
		xdr_5 = 0;
		xdr_6 = 0;
		xdr_7 = 0;
		xdr_9 = 0;
		xdr_other = 0;
		int total = c1 + c2 + c5 + c6 + c7 + c9 + c_other;
		System.out.println("[" + id + "][" + date + "][1=" + c1 + ",2=" + c2
				+ ",5=" + c5 + ",6=" + c6 + ",7=" + c7 + ",9=" + c9 + ",other="
				+ c_other + ",total=" + total + "]");
		
		int s1 = s1u_1,s2 = s1u_2,s3 = s1u_3,s5 = s1u_5,s6 = s1u_6,s7 = s1u_7,s8 = s1u_8,s9 = s1u_9;
		s1u_1 = 0;
		s1u_2 = 0;
		s1u_3 = 0;
		s1u_5 = 0;
		s1u_6 = 0;
		s1u_7 = 0;
		s1u_8 = 0;
		s1u_9 = 0;
		System.out.println("[" + id + "][" + date + "][s1_tcp=" + s1 + ",s2=" + s2
				+ ",s3=" + s3 + ",s5=" + s5 + ",s6=" + s6 + ",s7=" + s7 + ",s8=" + s8 + ",s9=" + s9 
				+ ",s_total=" + s5 + "]");
		
		System.out.println("[" + id + "][" + date + "][消费量Total:"+(total+s5)+",每秒:"+((total+s5)/60)+"][get_count_second="+get_count_second
				+",second="+(get_count_second/60)+"][目前queue="+queue.size()+"][目前cache="+cache.size()+"]");
		get_count_second = 0L;
		
	}

//	 public static void main(String[] args) {
//		 try {
//			 File f = new File("D:\\test\\data");
//			 byte[] data = new byte[(int) f.length()];
//			 FileInputStream in = new FileInputStream(f);
//			 in.read(data);
//			 in.close();
//			
//			 KafkaConsumerXdrSignallingKPI k = new KafkaConsumerXdrSignallingKPI("a");
//			 k.filterAndFetch(data);
//		 } catch (Exception e) {
//			 e.printStackTrace();
//		 }
//	 }
	
	
	//创建HttpLarge
	public void createHttpLarge(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
		byte[] http = new byte[56];
		for (int i = 0; i < 49; i++)
			http[i] = pub[i];
		// 填充apptype
		cursor += 156;
		http[49] = data[++cursor];
		http[50] = data[++cursor];
		// 填充appsubtype
		http[51] = data[++cursor];
		http[52] = data[++cursor];
		// ulthroughput、dlthroughput、ulpackets、dlpackets
		cursor += 47;
		long ulthroughput = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		long dlthroughput = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		long ulpackets = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		long dlpackets = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		// 填充code
		cursor += 54;
		http[53] = data[++cursor];
		http[54] = data[++cursor];
		int code = con.getUnsigendShort(new byte[] {http[53], http[54] }); 
		
		// responsetime
		long responsetime = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		//lastpacket_delay
		long lastpacket_delay = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		// 指针复位
		cursor -= 287;
		
		long ulthroughput2 = 0;
		long dlthroughput2 = 0;
		long delay_sum = 0;
		if (code>0 && code<400) {
			if (lastpacket_delay >= responsetime) {
				ulthroughput2 = ulthroughput;
				dlthroughput2 = dlthroughput;
				delay_sum = enddate - startdate;
			}
		} else {
			responsetime = 0;
		}
		
		putHttpLargeData(http, ulthroughput, dlthroughput, ulpackets, dlpackets, 
				responsetime, ulthroughput2, dlthroughput2, delay_sum);
		if (count_work)
			s1u_8++;
	}
	
	//创建AppStore
	public void createAppStore(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
		cursor += 156;
		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
		//cursor复位
		cursor -= 158;
		if(apptype == 7){
			byte[] content_type = new byte[128];
			cursor += 1251;
			for (int i = 0; i < 128; i++)
				content_type[i] = data[++cursor];
			//cursor复位
			cursor -= 1379;
			String _content_type = FormatUtils.byte2String(content_type);
			//content_type包含application/vnd.android或包含application/vnd.apple
			if(_content_type.indexOf("application/vnd.android") >= 0||_content_type.indexOf("application/vnd.apple") >= 0){
				byte[] app = new byte[75];
				for (int i = 0; i < 69; i++)
					app[i] = pub[i];
				// 填充apptype
				cursor += 156;
				app[69] = data[++cursor];
				app[70] = data[++cursor];
				// 填充appsubtype
				app[71] = data[++cursor];
				app[72] = data[++cursor];
				// ulthroughput、dlthroughput、ulpackets、dlpackets
				cursor += 47;
				long ulthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long dlthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long ulpackets = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long dlpackets = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 填充code
				cursor += 54;
				app[73] = data[++cursor];
				app[74] = data[++cursor];
				int code = con.getUnsigendShort(new byte[] {app[73], app[74] }); 
				
				// responsetime
				long responsetime = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				//lastpacket_delay
				long lastpacket_delay = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 指针复位
				cursor -= 287;
				
				long ulthroughput2 = 0;
				long dlthroughput2 = 0;
				long delay_sum = 0;
				if (code>0 && code<400) {
					if (lastpacket_delay >= responsetime) {
						ulthroughput2 = ulthroughput;
						dlthroughput2 = dlthroughput;
						delay_sum = enddate - startdate;
					}
				} else {
					responsetime = 0;
				}
				
				putAppStoreData(app, ulthroughput, dlthroughput, ulpackets, dlpackets, 
						responsetime, ulthroughput2, dlthroughput2, delay_sum);
				if (count_work)
					s1u_6++;
				
			}
		}
	}

	//创建AppLarge
	public void createAppLarge(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
		cursor += 156;
		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
		//cursor复位
		cursor -= 158;
		if(apptype == 7){
			byte[] content_type = new byte[128];
			cursor += 1251;
			for (int i = 0; i < 128; i++)
				content_type[i] = data[++cursor];
			//cursor复位
			cursor -= 1379;
			String _content_type = FormatUtils.byte2String(content_type);
			//content_type包含application/vnd.android或包含application/vnd.apple
			if(_content_type.indexOf("application/vnd.android") >= 0||_content_type.indexOf("application/vnd.apple") >= 0){
				byte[] app = new byte[76];
				for (int i = 0; i < 69; i++)
					app[i] = pub[i];
				// 填充apptype
				cursor += 156;
				app[69] = data[++cursor];
				app[70] = data[++cursor];
				// 填充appsubtype
				app[71] = data[++cursor];
				app[72] = data[++cursor];
				// ulthroughput、dlthroughput、ulpackets、dlpackets
				cursor += 47;
				long ulthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long dlthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long ulpackets = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long dlpackets = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 填充code
				cursor += 54;
				app[73] = data[++cursor];
				app[74] = data[++cursor];
				int code = con.getUnsigendShort(new byte[] {app[73], app[74] }); 
				
				// responsetime
				long responsetime = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				//lastpacket_delay
				long lastpacket_delay = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 指针复位
				cursor -= 287;
				
				long ulthroughput2 = 0;
				long dlthroughput2 = 0;
				long delay_sum = 0;
				if (code>0 && code<400) {
					if (lastpacket_delay >= responsetime) {
						ulthroughput2 = ulthroughput;
						dlthroughput2 = dlthroughput;
						delay_sum = enddate - startdate;
					}
				} else {
					responsetime = 0;
				}
				
				putAppLargeData(app, ulthroughput, dlthroughput, ulpackets, dlpackets, 
						responsetime, ulthroughput2, dlthroughput2, delay_sum);
				if (count_work)
					s1u_9++;
				
			}
		}
	}
	
	//创建IM
	public void createIM(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
		cursor += 156;
		
		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
		//cursor复位
		cursor -= 158;
		if(apptype == 1){
				byte[] im = new byte[15];
				for (int i = 0; i < 10; i++)
					im[i] = pub[i];
				// 填充apptype
				cursor += 156;
				im[10] = data[++cursor];
				im[11] = data[++cursor];
				// 填充appsubtype
				im[12] = data[++cursor];
				im[13] = data[++cursor];
				// 填充appcontent
				im[14] = data[++cursor];
				
				// ulthroughput、dlthroughput
				cursor += 46;
				long ulthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				long dlthroughput = con.getUnsigendInt(new byte[] {
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				
				// 指针复位
				cursor -= 215;
				
				putIM(im, ulthroughput, dlthroughput, enddate - startdate);
				if (count_work)
					s1u_7++;
		}
	}
	
	//创建 FLOW
		public void createFlow(byte[] data, int cursor){
			if (count_work)
				s1u_5++;
			//cursor 已运行到位置5
			byte[] pub = new byte[54];
			
			// 填充startdate
			cursor += 133;
			long startdate = con.getLong(new byte[] { data[++cursor],
					data[++cursor], data[++cursor], data[++cursor],
					data[++cursor], data[++cursor], data[++cursor],
					data[++cursor] });
			// 颗粒度分钟
			long timebymin = con.time2min(startdate);
			byte[] min = con.getBytes(timebymin);
			pub[0] = min[0];
			pub[1] = min[1];
			pub[2] = min[2];
			pub[3] = min[3];
			pub[4] = min[4];
			pub[5] = min[5];
			pub[6] = min[6];
			pub[7] = min[7];
			
			//protocoltype
			cursor += 8;
			pub[52] = data[++cursor];
			pub[53] = data[++cursor];
			
			// 填充city
			cursor -= 154; 
			pub[8] = data[++cursor];
			pub[9] = data[++cursor];

			// 填充ip
			cursor += 51;
			for (int i = 10; i < 42; i++)
				pub[i] = data[++cursor];
			
			// 填充tac
			cursor += 12;
			pub[42] = data[++cursor];
			pub[43] = data[++cursor];
			// 填充cellid
			pub[44] = data[++cursor];
			pub[45] = data[++cursor];
			pub[46] = data[++cursor];
			pub[47] = data[++cursor];
			
			// 填充apptype
			cursor += 51;
			pub[48] = data[++cursor];
			pub[49] = data[++cursor];
			// 填充appsubtype
			pub[50] = data[++cursor];
			pub[51] = data[++cursor];
			// ulthroughput、dlthroughput
			cursor += 47;
			long ulthroughput = con.getUnsigendInt(new byte[] {
					data[++cursor], data[++cursor], data[++cursor],
					data[++cursor] });
			long dlthroughput = con.getUnsigendInt(new byte[] {
					data[++cursor], data[++cursor], data[++cursor],
					data[++cursor] });
			//复位 到5
			cursor -= 210;
			
			//推送数据
			putFlowData(pub, ulthroughput, dlthroughput, 1);
		}
			
		//	推送 	Flow
		public void putFlowData(byte[] key, long ulthroughput, long dlthroughput, int req_num) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initFlow(key, ulthroughput, dlthroughput, req_num, 5);
			} else {
				v.addUlthroughput(ulthroughput);
				v.addDlthroughput(dlthroughput);
				v.addReq_num(req_num);
			}
		}

		public void putTCPLinkData(byte[] key, long tcp_delay_sum, int tcp_req_num) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initTCP(key, tcp_delay_sum, tcp_req_num, 1);
			} else {
				v.addDelay_sum(tcp_delay_sum);
				v.addReq_num(tcp_req_num);
			}
		}
		
		//TCP性能 预统计
		public void putTcpPacketData(byte[] key, long tcp_ultcp_packets, long tcp_dltcp_packets,
				long tcp_ultcp_disord_packets, long tcp_dltcp_disord_packets,
				long tcp_ultcp_retrans_packets, long tcp_dltcp_retrans_packets){
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initTcpPacket(key, tcp_ultcp_packets, tcp_dltcp_packets, tcp_ultcp_disord_packets, tcp_dltcp_disord_packets,
						tcp_ultcp_retrans_packets, tcp_dltcp_retrans_packets, 4);
			} else {
				v.addUlpackets(tcp_ultcp_packets);
				v.addDlpackets(tcp_dltcp_packets);
				v.addUlthroughput(tcp_ultcp_disord_packets);
				v.addDlthroughput(tcp_dltcp_disord_packets);
				v.addUlthroughput2(tcp_ultcp_retrans_packets);
				v.addDlthroughput2(tcp_dltcp_retrans_packets);
			}
		}
		
		public void putDnsData(byte[] key, long dns_delay, int dns_count) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initDNS(key, dns_delay, dns_count, 2);
			} else {
				v.addDelay_sum(dns_delay);
				v.addReq_num(dns_count);
			}
		}

		public void putHttpData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
				long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
				long http_delay_sum, int rate_num) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initHttp(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
						http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
						http_delay_sum, 1, rate_num, 3);
			} else {
				v.addUlthroughput(http_ulthroughput);
				v.addDlthroughput(http_dlthroughput);
				v.addUlpackets(http_ulpackets);
				v.addDlpackets(http_dlpackets);
				v.addResponsetime(http_responsetime);
				v.addUlthroughput2(http_ulthroughput2);
				v.addDlthroughput2(http_dlthroughput2);
				v.addDelay_sum(http_delay_sum);
				v.addReq_num(1);
				v.addRate_num(rate_num);
			}
		}
		
		public void putAppStoreData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
				long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
				long http_delay_sum) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initAppStore(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
						http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
						http_delay_sum, 1, 6);
			} else {
				v.addUlthroughput(http_ulthroughput);
				v.addDlthroughput(http_dlthroughput);
				v.addUlpackets(http_ulpackets);
				v.addDlpackets(http_dlpackets);
				v.addResponsetime(http_responsetime);
				v.addUlthroughput2(http_ulthroughput2);
				v.addDlthroughput2(http_dlthroughput2);
				v.addDelay_sum(http_delay_sum);
				v.addReq_num(1);
			}
		}
		
		public void putIM(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_delay_sum) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initIM(key, http_ulthroughput, http_dlthroughput, http_delay_sum, 1, 7);
			} else {
				v.addUlthroughput(http_ulthroughput);
				v.addDlthroughput(http_dlthroughput);
				v.addDelay_sum(http_delay_sum);
				v.addReq_num(1);
			}
		}
		
		//HTTP large
		public void putHttpLargeData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
				long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
				long http_delay_sum) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initHttpLarge(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
						http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
						http_delay_sum, 1, 8);
			} else {
				v.addUlthroughput(http_ulthroughput);
				v.addDlthroughput(http_dlthroughput);
				v.addUlpackets(http_ulpackets);
				v.addDlpackets(http_dlpackets);
				v.addResponsetime(http_responsetime);
				v.addUlthroughput2(http_ulthroughput2);
				v.addDlthroughput2(http_dlthroughput2);
				v.addDelay_sum(http_delay_sum);
				v.addReq_num(1);
			}
		}
		
		//App     large
		public void putAppLargeData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
				long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
				long http_delay_sum) {
			ByteValue b = new ByteValue(key);
			S1U_Value v = cache.getUnchecked(b);
			if (!v.init()) {
				v.initAppLarge(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
						http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
						http_delay_sum, 1, 9);
			} else {
				v.addUlthroughput(http_ulthroughput);
				v.addDlthroughput(http_dlthroughput);
				v.addUlpackets(http_ulpackets);
				v.addDlpackets(http_dlpackets);
				v.addResponsetime(http_responsetime);
				v.addUlthroughput2(http_ulthroughput2);
				v.addDlthroughput2(http_dlthroughput2);
				v.addDelay_sum(http_delay_sum);
				v.addReq_num(1);
			}
		}
}
