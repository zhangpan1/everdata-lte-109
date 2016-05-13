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
import com.eversec.lte.kpi.bean.S1U;
import com.eversec.lte.kpi.bean.S1U_Value;
import com.eversec.lte.kpi.coll.ByteValue;
import com.eversec.lte.kpi.config.KPIConfig;
import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class KafkaConsumerXdrSignalling_S1U implements KafkaConsumerHandler {

	public final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static BlockingQueue<S1U_Value> queue;

	private final static Thread worker;

	private Thread countWork;

	private int get_count_second;

	private boolean count_work;

	static {
		queue = new ArrayBlockingQueue<S1U_Value>(
				KPIConfig.getMiddleCacheSize(), true);

		worker = new Thread(new Runnable() {

			private Map<Long, Map<ByteValue, S1U_Value>> total;
			//private Map<Long, Map<ByteValue, S1U_Value>> drop;

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
				//drop = new HashMap<Long, Map<ByteValue, S1U_Value>>();
				
				ConvertUtils con = new ConvertUtils();
				int max = KPIConfig.getBlockNum();
				if (max < 2)
					max = 2;
				S1U_Value data = null;
				long min = 0l;

				System.out.println("[SIGNAL WORKER RUNING]");
				while (true) {
					try {
						synchronized (queue) {
							if ((data = queue.poll()) == null) {
								queue.wait();
							}
						}

						if (data != null) {
							byte[] b = data.getData();
							long time = con.getLong(new byte[] { b[0], b[1],
									b[2], b[3], b[4], b[5], b[6], b[7] });
							
							if (time >= min) {
								Map<ByteValue, S1U_Value> values = total
										.get(Long.valueOf(time));
								ByteValue bv = new ByteValue(b);

								if (values != null) {
									S1U_Value value = values.get(bv);
									if (value != null) {
										value.addAll(data);
									} else {
										values.put(bv, data);
									}
								} else {
									Map<ByteValue, S1U_Value> v = new HashMap<ByteValue, S1U_Value>();
									v.put(bv, data);
									total.put(Long.valueOf(time), v);
								}
							} else {
								//多计算10个小时
//								if((time - min) < 40 ){
//									Map<ByteValue, S1U_Value> values = drop
//											.get(Long.valueOf(time));
//									ByteValue bv = new ByteValue(b);
//
//									if (values != null) {
//										S1U_Value value = values.get(bv);
//										if (value != null) {
//											value.addAll(data);
//										} else {
//											values.put(bv, data);
//										}
//									} else {
//										Map<ByteValue, S1U_Value> v = new HashMap<ByteValue, S1U_Value>();
//										v.put(bv, data);
//										drop.put(Long.valueOf(time), v);
//									}
//								}else{
//									
//								}
								System.out.println("drop " + time);
							}
						}
						if (total.size() > max) {
							Object[] arr = total.keySet().toArray();
							Arrays.sort(arr);
							Long outTime = (Long) arr[0];
							min = (Long) arr[1];
							Map<ByteValue, S1U_Value> outData = total
									.remove(outTime);
							flush(outData, "");
						}
						
						//drop 将每一小时处理一次
//						long  date= System.currentTimeMillis();
//						if ((date%3600000)==0){
//							Object[] arr = drop.keySet().toArray();
//							Arrays.sort(arr);
//							for (int i = 0; i < arr.length; i++) {
//								Long outTime = (Long) arr[i];
//								Long now = date/3600000;
//								if(outTime < now){
//									Map<ByteValue, S1U_Value> outData = drop.remove(outTime);
//									flush(outData, "_" + dateformat.format(date));
//								}
//							}
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void flush(Map<ByteValue, S1U_Value> outData, String val) throws Exception {
				System.out.println("[Flash data S1U ...]");
				String curr_date = null;
				String flowUrl = null;
				S1U_Value v = null;
				Iterator<S1U_Value> iter = outData.values().iterator();
				while (iter.hasNext()) {
					v = iter.next();
					int type = v.getType();
					S1U s1u = new S1U(v);
					if (type == 1) {
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_1 = write(s1u_tcplink_write_1,
								KPIConfig.getS1uOutFile(1) + curr_date + val, s1u
										.toString().getBytes());
					} else if (type == 2) {
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_2 = write(s1u_tcplink_write_2,
								KPIConfig.getS1uOutFile(2) + curr_date + val, s1u
										.toString().getBytes());
					} else if (type == 3) {
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_3 = write(s1u_tcplink_write_3,
								KPIConfig.getS1uOutFile(3) + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 4){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_4 = write(s1u_tcplink_write_4,
								KPIConfig.getS1uOutFile(4) + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 5){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
							String url = KPIConfig.getS1uOutFile(5);
							int flowid = url.indexOf("flow") + 4;
							flowUrl = url.substring(0, flowid) + "/flow_" + curr_date.substring(0, 8) 
									+ url.substring(flowid, url.length());
						s1u_tcplink_write_5 = write(s1u_tcplink_write_5, flowUrl + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 6){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_6 = write(s1u_tcplink_write_6,
								KPIConfig.getS1uOutFile(6) + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 7){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_7 = write(s1u_tcplink_write_7,
								KPIConfig.getS1uOutFile(7) + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 8){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_8 = write(s1u_tcplink_write_8,
								KPIConfig.getS1uOutFile(8) + curr_date + val, s1u
										.toString().getBytes());
					} else if(type == 9){
						if (curr_date == null)
							curr_date = s1u.getStartDate();
						s1u_tcplink_write_9 = write(s1u_tcplink_write_9,
								KPIConfig.getS1uOutFile(9) + curr_date + val, s1u
										.toString().getBytes());
					}
				}

				if (s1u_tcplink_write_1 != null) {
					close(s1u_tcplink_write_1, KPIConfig.getS1uOutFile(1)
							+ curr_date + val);
					s1u_tcplink_write_1 = null;
				}

				if (s1u_tcplink_write_2 != null) {
					close(s1u_tcplink_write_2, KPIConfig.getS1uOutFile(2)
							+ curr_date + val);
					s1u_tcplink_write_2 = null;
				}

				if (s1u_tcplink_write_3 != null) {
					close(s1u_tcplink_write_3, KPIConfig.getS1uOutFile(3)
							+ curr_date + val);
					s1u_tcplink_write_3 = null;
				}
				
				if (s1u_tcplink_write_4 != null) {
					close(s1u_tcplink_write_4, KPIConfig.getS1uOutFile(4)
							+ curr_date + val);
					s1u_tcplink_write_4 = null;
				}
				
				if (s1u_tcplink_write_5 != null) {
					close(s1u_tcplink_write_5, flowUrl + curr_date + val);
					s1u_tcplink_write_5 = null;
				}
				
				if (s1u_tcplink_write_6 != null) {
					close(s1u_tcplink_write_6, KPIConfig.getS1uOutFile(6)
							+ curr_date + val);
					s1u_tcplink_write_6 = null;
				}
				
				if (s1u_tcplink_write_7 != null) {
					close(s1u_tcplink_write_7, KPIConfig.getS1uOutFile(7)
							+ curr_date + val);
					s1u_tcplink_write_7 = null;
				}
				
				if (s1u_tcplink_write_8 != null) {
					close(s1u_tcplink_write_8, KPIConfig.getS1uOutFile(8)
							+ curr_date + val);
					s1u_tcplink_write_8 = null;
				}
				
				if (s1u_tcplink_write_9 != null) {
					close(s1u_tcplink_write_9, KPIConfig.getS1uOutFile(9)
							+ curr_date + val);
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

	public KafkaConsumerXdrSignalling_S1U(final String id) {
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
							int c = get_count_second;
							System.out.println("[" + id + "][" + dateformat.format(new Date()) + "][消费量get_count_second="+get_count_second
									+",每秒second="+(c/60)+"][目前queue="+queue.size()+"][目前cache="+cache.size()+"]");
							get_count_second = 0;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			this.countWork.start();
		}
		System.out.println("[READ WORKING][" + this.hashCode() + "]");
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
		// System.out.println("[date coming]");
		if (this.count_work)
			get_count_second++;
		filterAndFetch(message);
		// Thread.sleep(1000);
	}

	public void filterAndFetch(byte[] data) {
		int len = data.length;
		int cursor = -1;
		cursor += 9;
		while ((len - cursor) >= 250) {
			// length
			int length = con.getUnsigendShort(new byte[] { data[++cursor],
					data[++cursor] });
			cursor += 2;
			// interface
			int interf = data[++cursor] & 0xFF;
			if (interf == 11) {// s1u
				/**
				 *  flow 处理
				 *  为什么同数据量，用户面没有控制面程序处理速度快？
				 *  1.用户面数据比控制面数据量大
				 *  2.用户面单条数据多次处理（将会生产多个符合指标的话单），控制面单条数据处理一次。
				 *  
				 */
				//TODO   
				//createFlow(data, cursor);
				
				// ipv4 or ipv6
				cursor += 49;
				int ip_type = data[++cursor] & 0xFF;
				// 统一成ipv6格式,即使实际为ipv4,剩余位置补0xff
				ip_type = 2;

				// 业务类型
				cursor += (58 + 24);
				int apptype_code = data[++cursor] & 0xFF; //apptype_code
				// 协议类型
				cursor += 46;
				int type = data[++cursor] & 0xFF; //l4_protocal
				// 指针复位
				cursor -= (161 + 24);
				
				/**
				 * 公共字段
				 */
				byte[] pub = new byte[70];

				// 填充startdate
				cursor += (114 + 24);
				long startdate = con.getLong(new byte[] { data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 颗粒度分钟
				long timebymin = con.time2min_s1u(startdate);
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
				cursor -= (128 + 24);
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
				cursor -= (249 + 24);
				
				/**
				 * S1U_XDR_M_TCPLINK_STAT
				 */
				if (type == 0) {
					// tcplink_count
					cursor += 271;
					int tcplink_count = data[++cursor] & 0xFF;
					cursor -= 272;
					//排除 DNS 101
					if(apptype_code != 101&&tcplink_count > 0){
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
						
						cursor += 79;
						// ultcp_responsetime
						long ultcp_responsetime = con.getUnsigendInt(new byte[] {
								data[++cursor], data[++cursor], data[++cursor],
								data[++cursor] });
						// dltcp_responsetime
						long dltcp_responsetime = con.getUnsigendInt(new byte[] {
								data[++cursor], data[++cursor], data[++cursor],
								data[++cursor] });
						
						cursor += 25;
						// status   (tcplink_state)
						int status = data[++cursor] & 0xFF;
						tcpLink[53] = data[cursor];
						// 推送数据
						putTCPLinkData(tcpLink, status == 0 ? (ultcp_responsetime + dltcp_responsetime) : 0, 1);
						// 指针复位
						cursor -= 273;
					} 
					
						/**
						 * S1U_XDR_M_TCPPACKET_STAT
						 */
						byte[] tcpPacket = new byte[53];
						for (int i = 0; i < 49; i++)
							tcpPacket[i] = pub[i];
						
						cursor += (132 + 24);
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
				
				if (apptype_code == 101) {    //DNS
					byte[] dns = new byte[70];
					for (int i = 0; i < 69; i++)
						dns[i] = pub[i];
					// 填充dnscode
					cursor += 329 + 24;
					dns[69] = data[++cursor];
					// 时延
					long dns_delay = dns[69] == 0 ? (enddate - startdate) : 0;
					putDnsData(dns, dns_delay, 1);
					// 指针复位
					cursor -= (330 + 24);
				} else if (apptype_code == 103) { //HTTP
					
					byte[] http = new byte[55];
					for (int i = 0; i < 49; i++)
						http[i] = pub[i];
					// 填充apptype
					cursor += (132 + 24);
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
							responsetime, ulthroughput2, dlthroughput2, delay_sum ,rate_num);
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
			} else {
				cursor += length - 5;
				//System.out.println("ERR inf-" + interf);
			}
		}
	}
	
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
				if (code>0 && code<400 && lastpacket_delay >= responsetime) {
					ulthroughput2 = ulthroughput;
					dlthroughput2 = dlthroughput;
					delay_sum = enddate - startdate;
				} else {
					responsetime = 0;
				}
				
				putAppLargeData(app, ulthroughput, dlthroughput, ulpackets, dlpackets, 
						responsetime, ulthroughput2, dlthroughput2, delay_sum);
				
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
		}
	}
	
	//创建 FLOW
	public void createFlow(byte[] data, int cursor){
		//cursor 已运行到位置5
		byte[] pub = new byte[54];
		
		// 填充startdate
		cursor += 133;
		long startdate = con.getLong(new byte[] { data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		// 颗粒度分钟
		long timebymin = con.time2min_s1u(startdate);
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
//package com.eversec.lte.kpi;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import com.eversec.lte.kafka.consumer.KafkaConsumerHandler;
//import com.eversec.lte.kpi.bean.S1U;
//import com.eversec.lte.kpi.bean.S1U_Value;
//import com.eversec.lte.kpi.coll.ByteValue;
//import com.eversec.lte.kpi.config.KPIConfig;
//import com.eversec.lte.kpi.util.ConvertUtils;
//import com.eversec.lte.kpi.util.FormatUtils;
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.google.common.cache.RemovalListener;
//import com.google.common.cache.RemovalNotification;
//
//public class KafkaConsumerXdrSignalling_S1U implements KafkaConsumerHandler {
//
//	public final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private final static BlockingQueue<S1U_Value> queue;
//
//	private final static Thread worker;
//
//	private Thread countWork;
//
//	private int get_count_second;
//
//	private boolean count_work;
//
//	static {
//		queue = new ArrayBlockingQueue<S1U_Value>(
//				KPIConfig.getMiddleCacheSize(), true);
//
//		worker = new Thread(new Runnable() {
//
//			private Map<Long, Map<ByteValue, S1U_Value>> total;
//
//			BufferedOutputStream s1u_tcplink_write_1;
//			BufferedOutputStream s1u_tcplink_write_2;
//			BufferedOutputStream s1u_tcplink_write_3;
//			BufferedOutputStream s1u_tcplink_write_4;
//			BufferedOutputStream s1u_tcplink_write_5;
//			BufferedOutputStream s1u_tcplink_write_6;
//			BufferedOutputStream s1u_tcplink_write_7;
//			BufferedOutputStream s1u_tcplink_write_8;
//			BufferedOutputStream s1u_tcplink_write_9;
//
//			@Override
//			public void run() {
//				total = new HashMap<Long, Map<ByteValue, S1U_Value>>();
//				
//				ConvertUtils con = new ConvertUtils();
//				int max = KPIConfig.getBlockNum();
//				if (max < 2)
//					max = 2;
//				S1U_Value data = null;
//				long min = 0l;
//
//				System.out.println("[SIGNAL WORKER RUNING]");
//				while (true) {
//					try {
//						synchronized (queue) {
//							if ((data = queue.poll()) == null) {
//								queue.wait();
//							}
//						}
//
//						if (data != null) {
//							byte[] b = data.getData();
//							long time = con.getLong(new byte[] { b[0], b[1],
//									b[2], b[3], b[4], b[5], b[6], b[7] });
//							
//							if (time >= min) {
//								Map<ByteValue, S1U_Value> values = total
//										.get(Long.valueOf(time));
//								ByteValue bv = new ByteValue(b);
//
//								if (values != null) {
//									S1U_Value value = values.get(bv);
//									if (value != null) {
//										value.addAll(data);
//									} else {
//										values.put(bv, data);
//									}
//								} else {
//									Map<ByteValue, S1U_Value> v = new HashMap<ByteValue, S1U_Value>();
//									v.put(bv, data);
//									total.put(Long.valueOf(time), v);
//								}
//							} else {
//								System.out.println("drop " + time);
//							}
//						}
//						if (total.size() > max) {
//							Object[] arr = total.keySet().toArray();
//							Arrays.sort(arr);
//							Long outTime = (Long) arr[0];
//							min = (Long) arr[1];
//							Map<ByteValue, S1U_Value> outData = total
//									.remove(outTime);
//							flush(outData, "");
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//			public void flush(Map<ByteValue, S1U_Value> outData, String val) throws Exception {
//				System.out.println("[Flash data S1U ...]");
//				String curr_date = null;
//				String flowUrl = null;
//				S1U_Value v = null;
//				Iterator<S1U_Value> iter = outData.values().iterator();
//				while (iter.hasNext()) {
//					v = iter.next();
//					int type = v.getType();
//					S1U s1u = new S1U(v);
//					if (type == 1) {
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_1 = write(s1u_tcplink_write_1,
//								KPIConfig.getS1uOutFile(1) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if (type == 2) {
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_2 = write(s1u_tcplink_write_2,
//								KPIConfig.getS1uOutFile(2) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if (type == 3) {
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_3 = write(s1u_tcplink_write_3,
//								KPIConfig.getS1uOutFile(3) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 4){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_4 = write(s1u_tcplink_write_4,
//								KPIConfig.getS1uOutFile(4) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 5){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//							String url = KPIConfig.getS1uOutFile(5);
//							int flowid = url.indexOf("flow") + 4;
//							flowUrl = url.substring(0, flowid) + "/flow_" + curr_date.substring(0, 8) 
//									+ url.substring(flowid, url.length());
//						s1u_tcplink_write_5 = write(s1u_tcplink_write_5, flowUrl + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 6){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_6 = write(s1u_tcplink_write_6,
//								KPIConfig.getS1uOutFile(6) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 7){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_7 = write(s1u_tcplink_write_7,
//								KPIConfig.getS1uOutFile(7) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 8){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_8 = write(s1u_tcplink_write_8,
//								KPIConfig.getS1uOutFile(8) + curr_date + val, s1u
//										.toString().getBytes());
//					} else if(type == 9){
//						if (curr_date == null)
//							curr_date = s1u.getStartDate();
//						s1u_tcplink_write_9 = write(s1u_tcplink_write_9,
//								KPIConfig.getS1uOutFile(9) + curr_date + val, s1u
//										.toString().getBytes());
//					}
//				}
//
//				if (s1u_tcplink_write_1 != null) {
//					close(s1u_tcplink_write_1, KPIConfig.getS1uOutFile(1)
//							+ curr_date + val);
//					s1u_tcplink_write_1 = null;
//				}
//
//				if (s1u_tcplink_write_2 != null) {
//					close(s1u_tcplink_write_2, KPIConfig.getS1uOutFile(2)
//							+ curr_date + val);
//					s1u_tcplink_write_2 = null;
//				}
//
//				if (s1u_tcplink_write_3 != null) {
//					close(s1u_tcplink_write_3, KPIConfig.getS1uOutFile(3)
//							+ curr_date + val);
//					s1u_tcplink_write_3 = null;
//				}
//				
//				if (s1u_tcplink_write_4 != null) {
//					close(s1u_tcplink_write_4, KPIConfig.getS1uOutFile(4)
//							+ curr_date + val);
//					s1u_tcplink_write_4 = null;
//				}
//				
//				if (s1u_tcplink_write_5 != null) {
//					close(s1u_tcplink_write_5, flowUrl + curr_date + val);
//					s1u_tcplink_write_5 = null;
//				}
//				
//				if (s1u_tcplink_write_6 != null) {
//					close(s1u_tcplink_write_6, KPIConfig.getS1uOutFile(6)
//							+ curr_date + val);
//					s1u_tcplink_write_6 = null;
//				}
//				
//				if (s1u_tcplink_write_7 != null) {
//					close(s1u_tcplink_write_7, KPIConfig.getS1uOutFile(7)
//							+ curr_date + val);
//					s1u_tcplink_write_7 = null;
//				}
//				
//				if (s1u_tcplink_write_8 != null) {
//					close(s1u_tcplink_write_8, KPIConfig.getS1uOutFile(8)
//							+ curr_date + val);
//					s1u_tcplink_write_8 = null;
//				}
//				
//				if (s1u_tcplink_write_9 != null) {
//					close(s1u_tcplink_write_9, KPIConfig.getS1uOutFile(9)
//							+ curr_date + val);
//					s1u_tcplink_write_9 = null;
//				}
//			}
//
//			public BufferedOutputStream write(BufferedOutputStream out,
//					String path, byte[] data) throws Exception {
//				if (out == null) {
//					File f = new File(path);
//					if (!f.getParentFile().exists())
//						f.getParentFile().mkdirs();
//					out = new BufferedOutputStream(new FileOutputStream(f));
//				}
//				out.write(data);
//				return out;
//			}
//
//			public void close(BufferedOutputStream out, String path)
//					throws Exception {
//				out.flush();
//				out.close();
//				File target = new File(path);
//				target.renameTo(new File(target.getPath() + ".txt"));
//			}
//		});
//
//		worker.start();
//	}
//
//	private ConvertUtils con;
//
//	private LoadingCache<ByteValue, S1U_Value> cache;
//
//	public KafkaConsumerXdrSignalling_S1U(final String id) {
//		con = new ConvertUtils();
//		initCache();
//		this.count_work = KPIConfig.getCountWork();
//		if (this.count_work) {
//			this.countWork = new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					while (true) {
//						try {
//							TimeUnit.MINUTES.sleep(1);
//							int c = get_count_second;
//							System.out.println("[" + id + "][" + dateformat.format(new Date()) + "][消费量get_count_second="+get_count_second
//									+",每秒second="+(c/60)+"][目前queue="+queue.size()+"][目前cache="+cache.size()+"]");
//							get_count_second = 0;
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			});
//			this.countWork.start();
//		}
//		System.out.println("[READ WORKING][" + this.hashCode() + "]");
//	}
//
//	public void initCache() {
//		CacheLoader<ByteValue, S1U_Value> loader = new CacheLoader<ByteValue, S1U_Value>() {
//
//			@Override
//			public S1U_Value load(ByteValue key) throws Exception {
//				return new S1U_Value();
//			}
//		};
//
//		RemovalListener<ByteValue, S1U_Value> listener = new RemovalListener<ByteValue, S1U_Value>() {
//
//			@Override
//			public void onRemoval(RemovalNotification<ByteValue, S1U_Value> data) {
//				try {
//					queue.put(data.getValue());
//					synchronized (queue) {
//						queue.notify();
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//
//		cache = CacheBuilder
//				.newBuilder()
//				.removalListener(listener)
//				.expireAfterWrite(KPIConfig.getTimeFlashInterval(),
//						TimeUnit.MILLISECONDS)
//				.maximumSize(KPIConfig.getTimeCacheSize()).build(loader);
//	}
//
//	@Override
//	public void messageReceived(byte[] message) throws Exception {
//		// System.out.println("[date coming]");
//		if (this.count_work)
//			get_count_second++;
//		filterAndFetch(message);
//		// Thread.sleep(1000);
//	}
//
//	public void filterAndFetch(byte[] data) {
//		int len = data.length;
//		int cursor = -1;
//		cursor += 9;
//		while ((len - cursor) >= 250) {
//			// length
//			int length = con.getUnsigendShort(new byte[] { data[++cursor],
//					data[++cursor] });
//			cursor += 2;
//			// interface
//			int interf = data[++cursor] & 0xFF;
//			if (interf == 11) {// s1u
//				/**
//				 *  flow 处理
//				 */
//				//TODO   
//				//createFlow(data, cursor);
//				
//				// ipv4 or ipv6
//				cursor += 49;
//				int ip_type = data[++cursor] & 0xFF;
//				// 统一成ipv6格式,即使实际为ipv4,剩余位置补0xff
//				ip_type = 2;
//				int ex = ip_type == 1 ? 0 : 24;
//
//				// 业务类型
//				cursor += (58 + ex);
//				int apptype_code = data[++cursor] & 0xFF; //apptype_code
//				// 协议类型
//				cursor += 46;
//				int type = data[++cursor] & 0xFF; //l4_protocal
//				// 指针复位
//				cursor -= (161 + ex);
//				
//				/**
//				 * 公共字段
//				 */
//				byte[] pub = new byte[70];
//
//				// 填充startdate
//				cursor += (114 + ex);
//				long startdate = con.getLong(new byte[] { data[++cursor],
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				// 颗粒度分钟
//				long timebymin = con.time2min(startdate);
//				byte[] min = con.getBytes(timebymin);
//				pub[0] = min[0];
//				pub[1] = min[1];
//				pub[2] = min[2];
//				pub[3] = min[3];
//				pub[4] = min[4];
//				pub[5] = min[5];
//				pub[6] = min[6];
//				pub[7] = min[7];
//
//				long enddate = con.getLong(new byte[] { data[++cursor],
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//
//				// 填充city
//				cursor -= (128 + ex);
//				pub[8] = data[++cursor];
//				pub[9] = data[++cursor];
//
//				// 填充ip
//				cursor += 51;
//				pub[10] = (byte) ip_type;
//				if (ip_type == 1) {
//					pub[11] = data[++cursor];
//					pub[12] = data[++cursor];
//					pub[13] = data[++cursor];
//					pub[14] = data[++cursor];
//
//					pub[15] = data[++cursor];
//					pub[16] = data[++cursor];
//					pub[17] = data[++cursor];
//					pub[18] = data[++cursor];
//				} else {
//					for (int i = 11; i < 43; i++)
//						pub[i] = data[++cursor];
//				}
//
//				// 填充tac
//				cursor += 12;
//				pub[43] = data[++cursor];
//				pub[44] = data[++cursor];
//				// 填充cellid
//				pub[45] = data[++cursor];
//				pub[46] = data[++cursor];
//				pub[47] = data[++cursor];
//				pub[48] = data[++cursor];
//				// 填充appserver_ipv4
//				cursor += 80;
//				pub[49] = data[++cursor];
//				pub[50] = data[++cursor];
//				pub[51] = data[++cursor];
//				pub[52] = data[++cursor];
//
//				// 填充appserver_ipv6
//				for (int i = 53; i < 69; i++)
//					pub[i] = data[++cursor];
//				//填充tcplink_state
//				cursor += 67;
//				pub[69] = data[++cursor];
//
//				// 指针复位
//				cursor -= (249 + ex);
//				
//				/**
//				 * S1U_XDR_M_TCPLINK_STAT
//				 */
//				if (type == 0) {
//					//排除 HTTP 103,MMS 102,EMAIL 105
//					//不推送数据 就排除掉了.
//					//if(apptype_code != 103 && apptype_code != 102 && apptype_code != 105){
//						byte[] tcpLink = new byte[54];
//						for (int i = 0; i < 49; i++)
//							tcpLink[i] = pub[i];
//						
//						cursor += 156;
//						//apptype
//						tcpLink[49] =  data[++cursor];
//						tcpLink[50] =  data[++cursor];
//						//appsubtype
//						tcpLink[51] =  data[++cursor];
//						tcpLink[52] =  data[++cursor];
//						//
//						
//						cursor += 79;
//						// ultcp_responsetime
//						long ultcp_responsetime = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						// dltcp_responsetime
//						long dltcp_responsetime = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						
//						cursor += 24;
//						// tcplink_count
//						int tcplink_count = data[++cursor] & 0xFF;
//						// status   (tcplink_state)
//						int status = data[++cursor] & 0xFF;
//						tcpLink[53] = data[cursor];
//						// 推送数据
//						putTCPLinkData(tcpLink, status == 0 ? (ultcp_responsetime + dltcp_responsetime) : 0, 1);
//						// 指针复位
//						cursor -= 273;
//					//} 
//					
//						/**
//						 * S1U_XDR_M_TCPPACKET_STAT
//						 */
//					if(tcplink_count > 0){
//						byte[] tcpPacket = new byte[53];
//						for (int i = 0; i < 49; i++)
//							tcpPacket[i] = pub[i];
//						
//						cursor += (132 + ex);
//						//apptype
//						tcpPacket[49] =  data[++cursor];
//						tcpPacket[50] =  data[++cursor];
//						//appsubtype
//						tcpPacket[51] =  data[++cursor];
//						tcpPacket[52] =  data[++cursor];
//						
//						cursor += 55;
//						//ultcp_packets
//						long ultcp_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						//dltcp_packets
//						long dltcp_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						//ultcp_disord_packets
//						long ultcp_disord_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						//dltcp_disord_packets
//						long dltcp_disord_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						//ultcp_retrans_packets
//						long ultcp_retrans_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						//dltcp_retrans_packets
//						long dltcp_retrans_packets = con.getUnsigendInt(new byte[] {
//								data[++cursor], data[++cursor], data[++cursor],
//								data[++cursor] });
//						
//						// 推送数据
//						putTcpPacketData(tcpPacket, ultcp_packets, dltcp_packets, ultcp_disord_packets, dltcp_disord_packets, ultcp_retrans_packets, dltcp_retrans_packets);
//						// 指针复位
//						cursor -= 239 ;
//					}
//				}
//				
//				if (apptype_code == 101) {    //DNS
//					byte[] dns = new byte[70];
//					for (int i = 0; i < 69; i++)
//						dns[i] = pub[i];
//					// 填充dnscode
//					cursor += 329 + ex;
//					dns[69] = data[++cursor];
//					// 时延
//					long dns_delay = dns[69] == 0 ? (enddate - startdate) : 0;
//					putDnsData(dns, dns_delay, 1);
//					// 指针复位
//					cursor -= (330 + ex);
//				} else if (apptype_code == 103) { //HTTP
//					
//					byte[] http = new byte[55];
//					for (int i = 0; i < 49; i++)
//						http[i] = pub[i];
//					// 填充apptype
//					cursor += (132 + ex);
//					http[49] = data[++cursor];
//					http[50] = data[++cursor];
//					// 填充appsubtype
//					http[51] = data[++cursor];
//					http[52] = data[++cursor];
//					// ulthroughput、dlthroughput、ulpackets、dlpackets
//					cursor += 47;
//					long ulthroughput = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					long dlthroughput = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					long ulpackets = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					long dlpackets = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					// 填充code
//					cursor += 54;
//					http[53] = data[++cursor];
//					http[54] = data[++cursor];
//					int code = con.getUnsigendShort(new byte[] {http[53], http[54] }); 
//					
//					// responsetime
//					long responsetime = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					//lastpacket_delay
//					long lastpacket_delay = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					
//					// 指针复位
//					cursor -= 287;
//					
//					long ulthroughput2 = 0;
//					long dlthroughput2 = 0;
//					long delay_sum = 0;
//					if (code>0 && code<400) {
//						if (lastpacket_delay >= responsetime) {
//							ulthroughput2 = ulthroughput;
//							dlthroughput2 = dlthroughput;
//							delay_sum = enddate - startdate;
//						}
//					} else {
//						responsetime = 0;
//					}
//					putHttpData(http, ulthroughput, dlthroughput, ulpackets, dlpackets, 
//							responsetime, ulthroughput2, dlthroughput2, delay_sum);
//				}
//				
//				/**
//				 * 		AppStore  IM        
//				 *      AppLarge HttpLarge
//				 */
//				if(apptype_code == 103){
//					createAppStore(data, cursor, pub, startdate, enddate);
//					createIM(data, cursor, pub, startdate, enddate);
//					
//					cursor += 211;
//					long dlthroughput = con.getUnsigendInt(new byte[] {
//							data[++cursor], data[++cursor], data[++cursor],
//							data[++cursor] });
//					cursor -= 215;
//					if(dlthroughput >= 512000){
//						createHttpLarge(data, cursor, pub, startdate, enddate);
//						createAppLarge(data, cursor, pub, startdate, enddate);
//					}
//				}
//				
//				// 跳到尾部
//				cursor += length;
//			} else {
//				cursor += length - 5;
//				//System.out.println("ERR inf-" + interf);
//			}
//		}
//	}
//	
//	//创建HttpLarge
//	public void createHttpLarge(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
//		byte[] http = new byte[56];
//		for (int i = 0; i < 49; i++)
//			http[i] = pub[i];
//		// 填充apptype
//		cursor += 156;
//		http[49] = data[++cursor];
//		http[50] = data[++cursor];
//		// 填充appsubtype
//		http[51] = data[++cursor];
//		http[52] = data[++cursor];
//		// ulthroughput、dlthroughput、ulpackets、dlpackets
//		cursor += 47;
//		long ulthroughput = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		long dlthroughput = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		long ulpackets = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		long dlpackets = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		// 填充code
//		cursor += 54;
//		http[53] = data[++cursor];
//		http[54] = data[++cursor];
//		int code = con.getUnsigendShort(new byte[] {http[53], http[54] }); 
//		
//		// responsetime
//		long responsetime = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		//lastpacket_delay
//		long lastpacket_delay = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		
//		// 指针复位
//		cursor -= 287;
//		
//		long ulthroughput2 = 0;
//		long dlthroughput2 = 0;
//		long delay_sum = 0;
//		if (code>0 && code<400) {
//			if (lastpacket_delay >= responsetime) {
//				ulthroughput2 = ulthroughput;
//				dlthroughput2 = dlthroughput;
//				delay_sum = enddate - startdate;
//			}
//		} else {
//			responsetime = 0;
//		}
//		
//		putHttpLargeData(http, ulthroughput, dlthroughput, ulpackets, dlpackets, 
//				responsetime, ulthroughput2, dlthroughput2, delay_sum);
//	}
//	
//	
//	//创建AppStore
//	public void createAppStore(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
//		cursor += 156;
//		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
//		//cursor复位
//		cursor -= 158;
//		if(apptype == 7){
//			byte[] content_type = new byte[128];
//			cursor += 1251;
//			for (int i = 0; i < 128; i++)
//				content_type[i] = data[++cursor];
//			//cursor复位
//			cursor -= 1379;
//			String _content_type = FormatUtils.byte2String(content_type);
//			//content_type包含application/vnd.android或包含application/vnd.apple
//			if(_content_type.indexOf("application/vnd.android") >= 0||_content_type.indexOf("application/vnd.apple") >= 0){
//				byte[] app = new byte[75];
//				for (int i = 0; i < 69; i++)
//					app[i] = pub[i];
//				// 填充apptype
//				cursor += 156;
//				app[69] = data[++cursor];
//				app[70] = data[++cursor];
//				// 填充appsubtype
//				app[71] = data[++cursor];
//				app[72] = data[++cursor];
//				// ulthroughput、dlthroughput、ulpackets、dlpackets
//				cursor += 47;
//				long ulthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long dlthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long ulpackets = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long dlpackets = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				// 填充code
//				cursor += 54;
//				app[73] = data[++cursor];
//				app[74] = data[++cursor];
//				int code = con.getUnsigendShort(new byte[] {app[73], app[74] }); 
//				
//				// responsetime
//				long responsetime = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				//lastpacket_delay
//				long lastpacket_delay = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				// 指针复位
//				cursor -= 287;
//				
//				long ulthroughput2 = 0;
//				long dlthroughput2 = 0;
//				long delay_sum = 0;
//				if (code>0 && code<400) {
//					if (lastpacket_delay >= responsetime) {
//						ulthroughput2 = ulthroughput;
//						dlthroughput2 = dlthroughput;
//						delay_sum = enddate - startdate;
//					}
//				} else {
//					responsetime = 0;
//				}
//				
//				putAppStoreData(app, ulthroughput, dlthroughput, ulpackets, dlpackets, 
//						responsetime, ulthroughput2, dlthroughput2, delay_sum);
//				
//			}
//		}
//	}
//	
//	
//	//创建AppLarge
//	public void createAppLarge(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
//		cursor += 156;
//		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
//		//cursor复位
//		cursor -= 158;
//		if(apptype == 7){
//			byte[] content_type = new byte[128];
//			cursor += 1251;
//			for (int i = 0; i < 128; i++)
//				content_type[i] = data[++cursor];
//			//cursor复位
//			cursor -= 1379;
//			String _content_type = FormatUtils.byte2String(content_type);
//			//content_type包含application/vnd.android或包含application/vnd.apple
//			if(_content_type.indexOf("application/vnd.android") >= 0||_content_type.indexOf("application/vnd.apple") >= 0){
//				byte[] app = new byte[76];
//				for (int i = 0; i < 69; i++)
//					app[i] = pub[i];
//				// 填充apptype
//				cursor += 156;
//				app[69] = data[++cursor];
//				app[70] = data[++cursor];
//				// 填充appsubtype
//				app[71] = data[++cursor];
//				app[72] = data[++cursor];
//				// ulthroughput、dlthroughput、ulpackets、dlpackets
//				cursor += 47;
//				long ulthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long dlthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long ulpackets = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long dlpackets = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				// 填充code
//				cursor += 54;
//				app[73] = data[++cursor];
//				app[74] = data[++cursor];
//				int code = con.getUnsigendShort(new byte[] {app[73], app[74] }); 
//				
//				// responsetime
//				long responsetime = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				//lastpacket_delay
//				long lastpacket_delay = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				// 指针复位
//				cursor -= 287;
//				
//				long ulthroughput2 = 0;
//				long dlthroughput2 = 0;
//				long delay_sum = 0;
//				if (code>0 && code<400) {
//					if (lastpacket_delay >= responsetime) {
//						ulthroughput2 = ulthroughput;
//						dlthroughput2 = dlthroughput;
//						delay_sum = enddate - startdate;
//					}
//				} else {
//					responsetime = 0;
//				}
//				
//				putAppLargeData(app, ulthroughput, dlthroughput, ulpackets, dlpackets, 
//						responsetime, ulthroughput2, dlthroughput2, delay_sum);
//				
//			}
//		}
//	}
//	
//	
//	
//	//创建IM
//	public void createIM(byte[] data, int cursor,byte[] pub, long startdate, long enddate){
//		cursor += 156;
//		
//		int apptype =  con.getUnsigendShort(new byte[] { data[++cursor], data[++cursor] });
//		//cursor复位
//		cursor -= 158;
//		if(apptype == 1){
//				byte[] im = new byte[15];
//				for (int i = 0; i < 10; i++)
//					im[i] = pub[i];
//				// 填充apptype
//				cursor += 156;
//				im[10] = data[++cursor];
//				im[11] = data[++cursor];
//				// 填充appsubtype
//				im[12] = data[++cursor];
//				im[13] = data[++cursor];
//				// 填充appcontent
//				im[14] = data[++cursor];
//				
//				// ulthroughput、dlthroughput
//				cursor += 46;
//				long ulthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				long dlthroughput = con.getUnsigendInt(new byte[] {
//						data[++cursor], data[++cursor], data[++cursor],
//						data[++cursor] });
//				
//				// 指针复位
//				cursor -= 215;
//				
//				putIM(im, ulthroughput, dlthroughput, enddate - startdate);
//		}
//	}
//	
//	//创建 FLOW
//	public void createFlow(byte[] data, int cursor){
//		//cursor 已运行到位置5
//		byte[] pub = new byte[54];
//		
//		// 填充startdate
//		cursor += 133;
//		long startdate = con.getLong(new byte[] { data[++cursor],
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		// 颗粒度分钟
//		long timebymin = con.time2min(startdate);
//		byte[] min = con.getBytes(timebymin);
//		pub[0] = min[0];
//		pub[1] = min[1];
//		pub[2] = min[2];
//		pub[3] = min[3];
//		pub[4] = min[4];
//		pub[5] = min[5];
//		pub[6] = min[6];
//		pub[7] = min[7];
//		
//		//protocoltype
//		cursor += 8;
//		pub[52] = data[++cursor];
//		pub[53] = data[++cursor];
//		
//		// 填充city
//		cursor -= 154; 
//		pub[8] = data[++cursor];
//		pub[9] = data[++cursor];
//
//		// 填充ip
//		cursor += 51;
//		for (int i = 10; i < 42; i++)
//			pub[i] = data[++cursor];
//		
//		// 填充tac
//		cursor += 12;
//		pub[42] = data[++cursor];
//		pub[43] = data[++cursor];
//		// 填充cellid
//		pub[44] = data[++cursor];
//		pub[45] = data[++cursor];
//		pub[46] = data[++cursor];
//		pub[47] = data[++cursor];
//		
//		// 填充apptype
//		cursor += 51;
//		pub[48] = data[++cursor];
//		pub[49] = data[++cursor];
//		// 填充appsubtype
//		pub[50] = data[++cursor];
//		pub[51] = data[++cursor];
//		// ulthroughput、dlthroughput
//		cursor += 47;
//		long ulthroughput = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		long dlthroughput = con.getUnsigendInt(new byte[] {
//				data[++cursor], data[++cursor], data[++cursor],
//				data[++cursor] });
//		//复位 到5
//		cursor -= 210;
//		
//		//推送数据
//		putFlowData(pub, ulthroughput, dlthroughput, 1);
//	}
//		
//	//	推送 	Flow
//	public void putFlowData(byte[] key, long ulthroughput, long dlthroughput, int req_num) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initFlow(key, ulthroughput, dlthroughput, req_num, 5);
//		} else {
//			v.addUlthroughput(ulthroughput);
//			v.addDlthroughput(dlthroughput);
//			v.addReq_num(req_num);
//		}
//	}
//
//	public void putTCPLinkData(byte[] key, long tcp_delay_sum, int tcp_req_num) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initTCP(key, tcp_delay_sum, tcp_req_num, 1);
//		} else {
//			v.addDelay_sum(tcp_delay_sum);
//			v.addReq_num(tcp_req_num);
//		}
//	}
//	
//	//TCP性能 预统计
//	public void putTcpPacketData(byte[] key, long tcp_ultcp_packets, long tcp_dltcp_packets,
//			long tcp_ultcp_disord_packets, long tcp_dltcp_disord_packets,
//			long tcp_ultcp_retrans_packets, long tcp_dltcp_retrans_packets){
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initTcpPacket(key, tcp_ultcp_packets, tcp_dltcp_packets, tcp_ultcp_disord_packets, tcp_dltcp_disord_packets,
//					tcp_ultcp_retrans_packets, tcp_dltcp_retrans_packets, 4);
//		} else {
//			v.addUlpackets(tcp_ultcp_packets);
//			v.addDlpackets(tcp_dltcp_packets);
//			v.addUlthroughput(tcp_ultcp_disord_packets);
//			v.addDlthroughput(tcp_dltcp_disord_packets);
//			v.addUlthroughput2(tcp_ultcp_retrans_packets);
//			v.addDlthroughput2(tcp_dltcp_retrans_packets);
//		}
//	}
//	
//	public void putDnsData(byte[] key, long dns_delay, int dns_count) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initDNS(key, dns_delay, dns_count, 2);
//		} else {
//			v.addDelay_sum(dns_delay);
//			v.addReq_num(dns_count);
//		}
//	}
//
//	public void putHttpData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
//			long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
//			long http_delay_sum) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initHttp(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
//					http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
//					http_delay_sum, 1, 3);
//		} else {
//			v.addUlthroughput(http_ulthroughput);
//			v.addDlthroughput(http_dlthroughput);
//			v.addUlpackets(http_ulpackets);
//			v.addDlpackets(http_dlpackets);
//			v.addResponsetime(http_responsetime);
//			v.addUlthroughput2(http_ulthroughput2);
//			v.addDlthroughput2(http_dlthroughput2);
//			v.addDelay_sum(http_delay_sum);
//			v.addReq_num(1);
//		}
//	}
//	
//	public void putAppStoreData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
//			long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
//			long http_delay_sum) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initAppStore(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
//					http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
//					http_delay_sum, 1, 6);
//		} else {
//			v.addUlthroughput(http_ulthroughput);
//			v.addDlthroughput(http_dlthroughput);
//			v.addUlpackets(http_ulpackets);
//			v.addDlpackets(http_dlpackets);
//			v.addResponsetime(http_responsetime);
//			v.addUlthroughput2(http_ulthroughput2);
//			v.addDlthroughput2(http_dlthroughput2);
//			v.addDelay_sum(http_delay_sum);
//			v.addReq_num(1);
//		}
//	}
//	
//	public void putIM(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_delay_sum) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initIM(key, http_ulthroughput, http_dlthroughput, http_delay_sum, 1, 7);
//		} else {
//			v.addUlthroughput(http_ulthroughput);
//			v.addDlthroughput(http_dlthroughput);
//			v.addDelay_sum(http_delay_sum);
//			v.addReq_num(1);
//		}
//	}
//	
//	//HTTP large
//	public void putHttpLargeData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
//			long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
//			long http_delay_sum) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initHttpLarge(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
//					http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
//					http_delay_sum, 1, 8);
//		} else {
//			v.addUlthroughput(http_ulthroughput);
//			v.addDlthroughput(http_dlthroughput);
//			v.addUlpackets(http_ulpackets);
//			v.addDlpackets(http_dlpackets);
//			v.addResponsetime(http_responsetime);
//			v.addUlthroughput2(http_ulthroughput2);
//			v.addDlthroughput2(http_dlthroughput2);
//			v.addDelay_sum(http_delay_sum);
//			v.addReq_num(1);
//		}
//	}
//	
//	//App     large
//	public void putAppLargeData(byte[] key, long http_ulthroughput, long http_dlthroughput, long http_ulpackets, 
//			long http_dlpackets, long http_responsetime, long http_ulthroughput2, long http_dlthroughput2,
//			long http_delay_sum) {
//		ByteValue b = new ByteValue(key);
//		S1U_Value v = cache.getUnchecked(b);
//		if (!v.init()) {
//			v.initAppLarge(key, http_ulthroughput, http_dlthroughput, http_ulpackets,
//					http_dlpackets, http_responsetime, http_ulthroughput2, http_dlthroughput2,
//					http_delay_sum, 1, 9);
//		} else {
//			v.addUlthroughput(http_ulthroughput);
//			v.addDlthroughput(http_dlthroughput);
//			v.addUlpackets(http_ulpackets);
//			v.addDlpackets(http_dlpackets);
//			v.addResponsetime(http_responsetime);
//			v.addUlthroughput2(http_ulthroughput2);
//			v.addDlthroughput2(http_dlthroughput2);
//			v.addDelay_sum(http_delay_sum);
//			v.addReq_num(1);
//		}
//	}
//}
