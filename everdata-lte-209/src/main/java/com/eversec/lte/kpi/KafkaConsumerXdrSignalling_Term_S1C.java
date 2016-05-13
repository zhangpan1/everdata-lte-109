package com.eversec.lte.kpi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
import com.eversec.lte.kpi.bean.S11;
import com.eversec.lte.kpi.bean.S1MME;
import com.eversec.lte.kpi.bean.S6A;
import com.eversec.lte.kpi.bean.SGS;
import com.eversec.lte.kpi.coll.ByteValue;
import com.eversec.lte.kpi.config.KPIConfig;
import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.CountJob;
import com.eversec.lte.kpi.util.QuartzManager;
import com.eversec.lte.kpi.util.ReadTxtUtils;
import com.eversec.lte.kpi.util.Terimei;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class KafkaConsumerXdrSignalling_Term_S1C implements KafkaConsumerHandler {

	private final static BlockingQueue<_VALUE> queue;
	private final static Thread worker;
	// private Thread countWork;
	private int xdr_1, xdr_2, xdr_5, xdr_6, xdr_7, xdr_9, xdr_other;
	private static boolean count_work;
	public static List<KafkaConsumerXdrSignalling_Term_S1C> owner;
	private int get_count_second;
	public final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Map<String, Terimei> imeiMap; 
	
	
	static {
		imeiMap = ReadTxtUtils.readKpiImeiMap();
		
		
		count_work = KPIConfig.getCountWork();
		if (count_work) {
			try {
				QuartzManager.addJob("count", new CountJob(), "0 * * * * ?");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		owner = new ArrayList<KafkaConsumerXdrSignalling_Term_S1C>();
		queue = new ArrayBlockingQueue<_VALUE>(KPIConfig.getMiddleCacheSize(),true);
		worker = new Thread(new Runnable() {
			
			private Map<Long, Map<ByteValue, _VALUE>> total;
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

			@Override
			public void run() {
				total = new HashMap<Long, Map<ByteValue, _VALUE>>();
				ConvertUtils con = new ConvertUtils();
				//最大缓冲块
				int max = KPIConfig.getBlockNum();
				if (max < 2){
					max = 2;
				}
				_VALUE data = null;
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
							long delay = data.getDelay();
							long count = data.getCount();

							long time = con.getLong(new byte[] { b[2], b[3],b[4], b[5], b[6], b[7], b[8], b[9]});
							if (time >= min) {
								Map<ByteValue, _VALUE> values = total.get(Long.valueOf(time));
								ByteValue bv = new ByteValue(b);

								if (values != null) {
									_VALUE value = values.get(bv);
									if (value != null) {
										value.addDelay(delay);
										value.addCount(count);
									} else {
										values.put(bv, data);
									}
								} else {
									Map<ByteValue, _VALUE> v = new HashMap<ByteValue, _VALUE>();
									v.put(bv, data);
									total.put(Long.valueOf(time), v);
								}
							} else {
								System.out.println("drop " + time);
							}
						}

						if (total.size() > max) {
							Object[] arr = total.keySet().toArray();
							Arrays.sort(arr);
							Long outTime = (Long) arr[0];
							min = (Long) arr[1];
							Map<ByteValue, _VALUE> outData = total.remove(outTime);
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
			public void flush(Map<ByteValue, _VALUE> outData) throws Exception {
				System.out.println("[Flush data 4G manager ...]");
				String curr_date = null;
				Iterator<_VALUE> iter = outData.values().iterator();
				while (iter.hasNext()) {
					_VALUE v = iter.next();
					byte[] data = v.getData();
					long delay = v.getDelay();
					long count = v.getCount();
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

	private LoadingCache<ByteValue, _VALUE> cache;

	private String id;

	public KafkaConsumerXdrSignalling_Term_S1C(final String id) {
		this.id = id;
		con = new ConvertUtils();
		initCache();
		System.out.println("[READ WORKING...][" + this.hashCode() + "]");
		owner.add(this);
	}

	public void initCache() {
		CacheLoader<ByteValue, _VALUE> loader = new CacheLoader<ByteValue, _VALUE>() {
			@Override
			public _VALUE load(ByteValue key) throws Exception {
				return new _VALUE();
			}
		};
		RemovalListener<ByteValue, _VALUE> listener = new RemovalListener<ByteValue, _VALUE>() {
			@Override
			public void onRemoval(RemovalNotification<ByteValue, _VALUE> data) {
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
		if (count_work)
			get_count_second++;
		filterAndFetch(message);
		// Thread.sleep(1000);
	}

	public void filterAndFetch(byte[] data) {
		int len = data.length;
		int cursor = -1;
		cursor += 9;
		while ((len - cursor) >= 55) {
			cursor += 4;
			// interface
			int interf = data[++cursor] & 0xFF;
			// type
			cursor += 49;
			int type = data[++cursor] & 0xFF;
			if (interf == 1) {// UU

				if (count_work)
					xdr_1++;

				cursor += 55;
				int epsNum = data[++cursor] & 0xFF;
				// 跳到xdr尾部
				cursor += (2 * epsNum);
			} else if (interf == 2) {  // X2

				if (count_work)
					xdr_2++;

				cursor += 44;
				int epsNum = data[++cursor] & 0xFF;
				// 跳到xdr尾部
				cursor += (2 * epsNum);
			} else if (interf == 5) {    // S1mme
				if (count_work)
					xdr_5++;
				if (type == 1 || type == 2 || type == 4 || type == 25
						|| type == 26 || type == 29 || type == 30 || type == 31
						|| type == 6 || type == 17 || type == 9 || type == 10
						|| type == 11 || type == 12 || type == 13 || type == 21
						|| type == 7 || type == 8 || type == 5 || type == 15
						|| type == 16||type == 100) {
					// System.out.println("[interf 5]");
					byte[] s1mme = new byte[95];
					// 填充interface
					s1mme[0] = 5;
					// 填充 type
					s1mme[1] = (byte) type;
					// 填充 city
					cursor -= 53;
					s1mme[10] = data[++cursor];
					s1mme[11] = data[++cursor];
					cursor += 51;
					// startdate
					long startdate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// endate
					long enddate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// 颗粒度分钟
					long timebymin = con.time2min(startdate);
					byte[] min = con.getBytes(timebymin);
					// 填充其他字段
					// startdate(精确到分钟)
					s1mme[2] = min[0];
					s1mme[3] = min[1];
					s1mme[4] = min[2];
					s1mme[5] = min[3];
					s1mme[6] = min[4];
					s1mme[7] = min[5];
					s1mme[8] = min[6];
					s1mme[9] = min[7];
					// status
					s1mme[12] = data[++cursor];
					// request cause
					s1mme[54] = data[++cursor];
					s1mme[55] = data[++cursor];
					// failure cause
					s1mme[13] = data[++cursor];
					s1mme[14] = data[++cursor];
					// keyword1
					s1mme[15] = data[++cursor];
					// keyword2
					s1mme[56] = data[++cursor];
					// mme ip
					cursor += 44;
					for (int i = 16; i < 32; i++)
						s1mme[i] = data[++cursor];
					// enb ip
					for (int i = 32; i < 48; i++)
						s1mme[i] = data[++cursor];
					// tac
					cursor += 4;
					s1mme[48] = data[++cursor];
					s1mme[49] = data[++cursor];
					// cellid
					s1mme[50] = data[++cursor];
					s1mme[51] = data[++cursor];
					s1mme[52] = data[++cursor];
					s1mme[53] = data[++cursor];
					// other tac
					s1mme[89] = data[++cursor];
					s1mme[90] = data[++cursor];
					// other cellid
					s1mme[91] = data[++cursor];
					s1mme[92] = data[++cursor];
					s1mme[93] = data[++cursor];
					s1mme[94] = data[++cursor];
					// apn
					for (int i = 57; i < 89; i++)
						s1mme[i] = data[++cursor];
					// 计算时延
					long delay = (s1mme[12] == 0) ? (enddate - startdate) : 0;

					// 根据不同type需要不同字段作为组成员
					createS1mmeKeyByType(s1mme);
					// 推送数据
					putData(s1mme, delay);
					// 得到eps承载数
					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += (16 * epsNum);
				} else {
					cursor += 147;
					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += (16 * epsNum);
				}
			} else if (interf == 6) {   //S6a
				if (count_work)
					xdr_6++;
				if (type == 1 || type == 2 || type == 6 || type == 3
						|| type == 8) {
					byte[] s6a = new byte[263];
					// 填充interface
					s6a[0] = 6;
					// 填充 type
					s6a[1] = (byte) type;
					// 填充 city
					cursor -= 53;
					s6a[10] = data[++cursor];
					s6a[11] = data[++cursor];
					cursor += 51;
					// startdate
					long startdate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// endate
					long enddate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// 颗粒度分钟
					long timebymin = con.time2min(startdate);
					byte[] min = con.getBytes(timebymin);
					// 填充其他字段
					// startdate(精确到分钟)
					s6a[2] = min[0];
					s6a[3] = min[1];
					s6a[4] = min[2];
					s6a[5] = min[3];
					s6a[6] = min[4];
					s6a[7] = min[5];
					s6a[8] = min[6];
					s6a[9] = min[7];
					// status
					s6a[262] = data[++cursor];
					// failurecause
					s6a[260] = data[++cursor];
					s6a[261] = data[++cursor];
					// mmeip
					cursor += 20;
					for (int i = 12; i < 28; i++)
						s6a[i] = data[++cursor];
					// hssip
					for (int i = 28; i < 44; i++)
						s6a[i] = data[++cursor];
					// o_realm
					cursor += 4;
					for (int i = 44; i < 88; i++)
						s6a[i] = data[++cursor];
					// d_realm
					for (int i = 88; i < 132; i++)
						s6a[i] = data[++cursor];
					// o_host
					for (int i = 132; i < 196; i++)
						s6a[i] = data[++cursor];
					// d_host
					for (int i = 196; i < 260; i++)
						s6a[i] = data[++cursor];
					createS6aKeyByType(s6a);
					// 计算时延
					long delay = (s6a[262] == 0) ? (enddate - startdate) : 0;
					putData(s6a, delay);
					// 跳到尾部
					cursor += 6;
				} else {
					// 跳到尾部
					cursor += 297;
				}
			} else if (interf == 7) { //S11接口

				if (count_work)
					xdr_7++;

				if (type == 1 || type == 3 || type == 7 || type == 8
						|| type == 2 || type == 9 || type == 10) {
					byte[] s11 = new byte[81];
					// 填充interface
					s11[0] = 7;
					// 填充 type
					s11[1] = (byte) type;
					// 填充 city
					cursor -= 53;
					s11[10] = data[++cursor];
					s11[11] = data[++cursor];
					cursor += 51;
					// startdate
					long startdate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// endate
					long enddate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// 颗粒度分钟
					long timebymin = con.time2min(startdate);
					byte[] min = con.getBytes(timebymin);
					// 填充其他字段
					// startdate(精确到分钟)
					s11[2] = min[0];
					s11[3] = min[1];
					s11[4] = min[2];
					s11[5] = min[3];
					s11[6] = min[4];
					s11[7] = min[5];
					s11[8] = min[6];
					s11[9] = min[7];
					// status
					s11[80] = data[++cursor];
					// failure_cause
					s11[76] = data[++cursor];
					s11[77] = data[++cursor];
					// request_cause
					s11[78] = data[++cursor];
					s11[79] = data[++cursor];
					// mmeip
					cursor += 20;
					for (int i = 12; i < 28; i++)
						s11[i] = data[++cursor];
					// oldmmeip
					for (int i = 28; i < 44; i++)
						s11[i] = data[++cursor];
					// apn
					cursor += 12;
					for (int i = 44; i < 76; i++)
						s11[i] = data[++cursor];

					createS11KeyByType(s11);
					// 计算时延
					long delay = (s11[80] == 0) ? (enddate - startdate) : 0;
					putData(s11, delay);

					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += (12 * epsNum);
				} else {
					cursor += 117;
					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += (12 * epsNum);
				}
			} else if (interf == 9) {    //SGS

				if (count_work)
					xdr_9++;

				if (type == 1 || type == 5 || type == 9 || type == 10) {
					byte[] sgs = new byte[58];
					// 填充interface
					sgs[0] = 9;
					// 填充 type
					sgs[1] = (byte) type;
					// 填充 city
					cursor -= 53;
					sgs[10] = data[++cursor];
					sgs[11] = data[++cursor];
					cursor += 51;
					// startdate
					long startdate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// endate
					long enddate = con.getLong(new byte[] { data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					// 颗粒度分钟
					long timebymin = con.time2min(startdate);
					byte[] min = con.getBytes(timebymin);
					// 填充其他字段
					// startdate(精确到分钟)
					sgs[2] = min[0];
					sgs[3] = min[1];
					sgs[4] = min[2];
					sgs[5] = min[3];
					sgs[6] = min[4];
					sgs[7] = min[5];
					sgs[8] = min[6];
					sgs[9] = min[7];
					// status
					sgs[57] = data[++cursor];
					// sgscause
					sgs[56] = data[++cursor];
					// rejectcause
					sgs[54] = data[++cursor];
					// mmeip
					cursor += 22;
					for (int i = 12; i < 28; i++)
						sgs[i] = data[++cursor];
					// mscip
					for (int i = 34; i < 50; i++)
						sgs[i] = data[++cursor];
					// service_indicator
					cursor += 4;
					sgs[55] = data[++cursor];
					// newlai
					cursor += 59;
					sgs[50] = data[++cursor];
					sgs[51] = data[++cursor];
					// oldlai
					sgs[52] = data[++cursor];
					sgs[53] = data[++cursor];
					// tai
					sgs[28] = data[++cursor];
					sgs[29] = data[++cursor];
					// cellid
					sgs[30] = data[++cursor];
					sgs[31] = data[++cursor];
					sgs[32] = data[++cursor];
					sgs[33] = data[++cursor];

					createSgsKeyByType(sgs);

					// 计算时延
					long delay = (sgs[57] == 0) ? (enddate - startdate) : 0;
					putData(sgs, delay);

					cursor += 24;
					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += epsNum;
				} else {
					cursor += 171;
					int epsNum = data[++cursor] & 0xFF;
					// 跳到xdr尾部
					cursor += epsNum;
				}
			} else {
				if(interf == 11){
					break;
				}else{
					if (count_work)
						xdr_other++;
					System.out.println("[ERR inf]" + interf);
					break;
				}
			}
		}
	}
	
	//生产term 公共部分
	public byte[] createTermPublic(byte[] data, int cursor){
		//公共的
		byte[] pub = new byte[38];
		
		cursor += 19;
		byte[] _imei = new byte[] { data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor], data[++cursor],	data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor], data[++cursor], data[++cursor]};
		String imei = ConvertUtils.getImeiToString(_imei);
		if(!"".equals(imei) && !"-1".equals(imei) && null!=imei){
			String kpi_imei = imei.substring(0, 8);
			Terimei ter = imeiMap.get(kpi_imei);
			if(ter!=null){
				int terfac_id = 0, tertype_id = 0;
				terfac_id = ter.getTerfac_id();
				tertype_id = ter.getTertype_id();
				//填充startdate
				for (int i = 0; i < 8; i++) 
					pub[i] = 0;
				
				// 填充city
				cursor -= 33;
				pub[17] = data[++cursor];
				pub[18] = data[++cursor];
				
				//填充terfac_id 和 tertype_id
				byte[] terfac = ConvertUtils.intToBytes(terfac_id);
				byte[] tertype = ConvertUtils.intToBytes(tertype_id);
				for (int i = 8; i < 12; i++) 
					pub[i] = terfac[i-8];
				for (int i = 12; i < 16; i++) 
					pub[i] = tertype[i-12];
				
				//lac 
				cursor += 45;
				pub[27] = data[++cursor];
				pub[28] = data[++cursor];
				pub[29] = data[++cursor];
				pub[30] = data[++cursor];
				
				//cellid
				cursor += 4;
				pub[31] = data[++cursor];
				pub[32] = data[++cursor];
				pub[33] = data[++cursor];
				pub[34] = data[++cursor];
				
				// 填充ip
				cursor += 20;
				for (int i = 19; i < 27; i++)
					pub[i] = data[++cursor];
				//填充rat
				int rat = data[++cursor] & 0xFF;
				pub[16] = data[cursor];
				if(rat == 5){
					pub[16] = 1;
				}

				//填充failure_cause
				pub[35] = 0;
				pub[36] = 0;
				
				//填充 status
				pub[37] = 0;
				
				//复位
				cursor -= 88;
			}else{
				cursor -= 35;
			}
		}else{
			cursor -= 35;
		}
		return pub;
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
		_VALUE v = cache.getUnchecked(b);
		if (!v.init) {
			v.init(key, value, 1);
		} else {
			v.addDelay(value);
			v.addCount(1l);
		}
	}
	
	/**
	 *  _VALUE 用累加 时延delay 条数count
	 */
	class _VALUE {

		byte[] data;
		long delay;
		long count;
		boolean init;

		public _VALUE() {
			this.init = false;
		}

		// public _VALUE(byte[] data, long delay, long count){
		// this.data = data;
		// this.delay = delay;
		// this.count = count;
		// this.init = true;
		// }

		public boolean init() {
			return this.init;
		}

		public void init(byte[] data, long delay, long count) {
			this.data = data;
			this.delay = delay;
			this.count = count;
			this.init = true;
		}

		public byte[] getData() {
			return this.data;
		}

		public long getDelay() {
			return this.delay;
		}

		public long getCount() {
			return this.count;
		}

		public void addDelay(long delay) {
			this.delay += delay;
		}

		public void addCount(long count) {
			this.count += count;
		}
	}

	public void showCount(String date) {
		int c1 = xdr_1;
		xdr_1 = 0;
		int c2 = xdr_2;
		xdr_2 = 0;
		int c5 = xdr_5;
		xdr_5 = 0;
		int c6 = xdr_6;
		xdr_6 = 0;
		int c7 = xdr_7;
		xdr_7 = 0;
		int c9 = xdr_9;
		xdr_9 = 0;
		int c_other = xdr_other;
		xdr_other = 0;
		int total = c1 + c2 + c5 + c6 + c7 + c9 + c_other;
		System.out.println("[" + id + "][" + date + "][1=" + c1 + ",2=" + c2
				+ ",5=" + c5 + ",6=" + c6 + ",7=" + c7 + ",9=" + c9 + ",other="
				+ c_other + ",total=" + total + "]");
		
		System.out.println("[" + id + "][" + dateformat.format(new Date()) + "][消费量get_count_second="+get_count_second
				+",每秒second="+(get_count_second/60)+"][目前queue="+queue.size()+"][目前cache="+cache.size()+"]");
		get_count_second = 0;
	}
}
