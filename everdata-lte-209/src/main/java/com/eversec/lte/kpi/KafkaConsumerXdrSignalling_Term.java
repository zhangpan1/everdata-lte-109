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
import com.eversec.lte.kpi.bean.Term;
import com.eversec.lte.kpi.bean.Term_Value;
import com.eversec.lte.kpi.coll.ByteValue;
import com.eversec.lte.kpi.config.KPIConfig;
import com.eversec.lte.kpi.util.ConvertUtils;
import com.eversec.lte.kpi.util.FormatUtils;
import com.eversec.lte.kpi.util.ReadTxtUtils;
import com.eversec.lte.kpi.util.Terimei;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class KafkaConsumerXdrSignalling_Term implements KafkaConsumerHandler {
	public final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static BlockingQueue<Term_Value> queue;
	private final static Thread worker;
	private Thread countWork;
	private int get_count_second;
	private boolean count_work;
	private static Map<String, Terimei> imeiMap; 

	static {
		imeiMap = ReadTxtUtils.readKpiImeiMap();
		
		queue = new ArrayBlockingQueue<Term_Value>(KPIConfig.getMiddleCacheSize(), true);

		worker = new Thread(new Runnable() {

			private Map<Long, Map<ByteValue, Term_Value>> total;

			BufferedOutputStream s1u_service_write_1;
			BufferedOutputStream s1u_tcp_write_2;
			BufferedOutputStream s1u_dns_write_3;
			BufferedOutputStream s1u_flow_write_4;
			
			BufferedOutputStream s1mme_attach_write_5;
			BufferedOutputStream s1mme_pdnconn_write_6;
			BufferedOutputStream s1mme_default_write_7;
			BufferedOutputStream s1mme_tau_write_8;
			
			@Override
			public void run() {
				total = new HashMap<Long, Map<ByteValue, Term_Value>>();
				ConvertUtils con = new ConvertUtils();
				int max = KPIConfig.getBlockNum();
				if (max < 2)
					max = 2;
				Term_Value data = null;
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
							long time = con.getLong(new byte[] { b[1], b[2],b[3], b[4], b[5], b[6], b[7], b[8] });
							if (time >= min) {
								Map<ByteValue, Term_Value> values = total.get(Long.valueOf(time));
								ByteValue bv = new ByteValue(b);
								if (values != null) {
									Term_Value value = values.get(bv);
									if (value != null) {
										value.addAll(data);
									} else {
										values.put(bv, data);
									}
								} else {
									Map<ByteValue, Term_Value> v = new HashMap<ByteValue, Term_Value>();
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
							Map<ByteValue, Term_Value> outData = total.remove(outTime);
							flush(outData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void flush(Map<ByteValue, Term_Value> outData) throws Exception {
				System.out.println("[Flash data Term ...]");
				String curr_date = null;
				Iterator<Term_Value> iter = outData.values().iterator();
				while (iter.hasNext()) {
					Term_Value v = iter.next();
					Term tuser = new Term(v);
					int type = tuser.getType();
					if (type == 1) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1u_service_write_1 = write(s1u_service_write_1,
								KPIConfig.getTermUserOutFile(1) + curr_date, tuser
										.toString().getBytes());
					} else if (type == 2) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1u_tcp_write_2 = write(s1u_tcp_write_2,
								KPIConfig.getTermUserOutFile(2) + curr_date, tuser
										.toString().getBytes());
					} else if (type == 3) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1u_dns_write_3 = write(s1u_dns_write_3,
								KPIConfig.getTermUserOutFile(3) + curr_date, tuser
										.toString().getBytes());
					} else if (type == 4) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1u_flow_write_4 = write(s1u_flow_write_4,
								KPIConfig.getTermUserOutFile(4) + curr_date, tuser
										.toString().getBytes());
					} else if (type == 5) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1mme_attach_write_5 = write(s1mme_attach_write_5,
								KPIConfig.getTermUserOutFile(5) + curr_date, tuser
										.toString().getBytes());
					} else if (type == 6) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1mme_pdnconn_write_6 = write(s1mme_pdnconn_write_6,
								KPIConfig.getTermUserOutFile(6) + curr_date, tuser
										.toString().getBytes());
					}else if (type == 7) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1mme_default_write_7 = write(s1mme_default_write_7,
								KPIConfig.getTermUserOutFile(7) + curr_date, tuser
										.toString().getBytes());
					}else if (type == 8) {
						if (curr_date == null)
							curr_date = tuser.getStartDate();
						s1mme_tau_write_8 = write(s1mme_tau_write_8,
								KPIConfig.getTermUserOutFile(8) + curr_date, tuser
										.toString().getBytes());
					}
				}

				if (s1u_service_write_1 != null) {
					close(s1u_service_write_1, KPIConfig.getTermUserOutFile(1)
							+ curr_date);
					s1u_service_write_1 = null;
				}

				if (s1u_tcp_write_2 != null) {
					close(s1u_tcp_write_2, KPIConfig.getTermUserOutFile(2)
							+ curr_date);
					s1u_tcp_write_2 = null;
				}

				if (s1u_dns_write_3 != null) {
					close(s1u_dns_write_3, KPIConfig.getTermUserOutFile(3)
							+ curr_date);
					s1u_dns_write_3 = null;
				}
				
				if (s1u_flow_write_4 != null) {
					close(s1u_flow_write_4, KPIConfig.getTermUserOutFile(4)
							+ curr_date);
					s1u_flow_write_4 = null;
				}
				
				if (s1mme_attach_write_5 != null) {
					close(s1mme_attach_write_5, KPIConfig.getTermUserOutFile(5)
							+ curr_date);
					s1mme_attach_write_5 = null;
				}
				
				if (s1mme_pdnconn_write_6 != null) {
					close(s1mme_pdnconn_write_6, KPIConfig.getTermUserOutFile(6)
							+ curr_date);
					s1mme_pdnconn_write_6 = null;
				}
				
				if (s1mme_default_write_7 != null) {
					close(s1mme_default_write_7, KPIConfig.getTermUserOutFile(7)
							+ curr_date);
					s1mme_default_write_7 = null;
				}
				
				if (s1mme_tau_write_8 != null) {
					close(s1mme_tau_write_8, KPIConfig.getTermUserOutFile(8)
							+ curr_date);
					s1mme_tau_write_8 = null;
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

	private LoadingCache<ByteValue, Term_Value> cache;

	public KafkaConsumerXdrSignalling_Term(final String id) {
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
							System.out.println("[" + id + "][" + dateformat.format(new Date()) + "][消费量get_count_second="+get_count_second
									+",每秒second="+(get_count_second/60)+"][目前queue="+queue.size()+"][目前cache="+cache.size()+"]");
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
		CacheLoader<ByteValue, Term_Value> loader = new CacheLoader<ByteValue, Term_Value>() {

			@Override
			public Term_Value load(ByteValue key) throws Exception {
				return new Term_Value();
			}
		};

		RemovalListener<ByteValue, Term_Value> listener = new RemovalListener<ByteValue, Term_Value>() {

			@Override
			public void onRemoval(RemovalNotification<ByteValue, Term_Value> data) {
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
		 //System.out.println("[date coming]");
		if (this.count_work)
			get_count_second++;
		filterAndFetch(message);
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
			cursor -= 5;
			if (interf == 11) {// s1u
					//处理Flow();
					createFlow(data, cursor);
				
					//imei 
					cursor += 30;
					byte[] _imei = new byte[] { data[++cursor], data[++cursor],
							data[++cursor], data[++cursor], data[++cursor], data[++cursor],
							data[++cursor], data[++cursor]};
					cursor -= 38; //复位指针
					String imei="";
					try {
						 imei = FormatUtils.TBCDFormat(_imei);
					} catch (Exception e) {
						System.out.println("Exception:"+e);
					}
					//System.out.println("imei号: 用户面:"+imei);
					if(!"".equals(imei)&&imei.length()>8){
						String kpi_imei = imei.substring(0, 8);
						Terimei ter = imeiMap.get(kpi_imei);
						if(ter!=null){
							int terfac_id = 0, tertype_id = 0;
							terfac_id = ter.getTerfac_id();
							tertype_id = ter.getTertype_id();
							
							cursor += 137;
							int apptype_code = data[++cursor] & 0xFF; //apptype_code
							// 协议类型
							cursor += 46;
							int l4_protocal = data[++cursor] & 0xFF; // l4_protocal
							
							// 指针复位
							cursor -= 185;
							
							/**
							 * 公共字段
							 */
							byte[] pub = new byte[58];
							pub[0] = 1; //默认 type 为1 term_service 2为term_tcp 3为term_dns
							// 填充startdate
							cursor += 138;
							long startdate = con.getLong(new byte[] { data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							// 颗粒度分钟
							long timebymin = con.time2min_term(startdate);
							byte[] min = con.getBytes(timebymin);
							pub[1] = min[0];
							pub[2] = min[1];
							pub[3] = min[2];
							pub[4] = min[3];
							pub[5] = min[4];
							pub[6] = min[5];
							pub[7] = min[6];
							pub[8] = min[7];

							long enddate = con.getLong(new byte[] { data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							
							//填充terfac_id 和 tertype_id
							byte[] terfac = ConvertUtils.intToBytes(terfac_id);
							byte[] tertype = ConvertUtils.intToBytes(tertype_id);
							for (int i = 9; i < 13; i++) 
								pub[i] = terfac[i-9];
							for (int i = 13; i < 17; i++) 
								pub[i] = tertype[i-13];
							
							//填充rat
							cursor -= 133;
							int rat = data[++cursor] & 0xFF;
							pub[17] = data[cursor];
							
							// 填充city
							cursor -= 20;
							pub[18] = data[++cursor];
							pub[19] = data[++cursor];

							// 填充ip
							cursor += 51;
							for (int i = 20; i < 52; i++)
								pub[i] = data[++cursor];
							
							// 填充tac
							cursor += 12;
							pub[52] = data[++cursor];
							pub[53] = data[++cursor];
							// 填充cellid
							pub[54] = data[++cursor];
							pub[55] = data[++cursor];
							pub[56] = data[++cursor];
							pub[57] = data[++cursor];

							// 指针复位
							cursor -= 105;
							
							if(l4_protocal == 0 ){ // TCP
								// tcplink_count
								cursor += 271;
								int tcplink_count = data[++cursor] & 0xFF;
								cursor -= 272;
								//排除 DNS 101
								if(apptype_code != 101&&tcplink_count > 0){
									byte[] tcp = new byte[59];
									for (int i = 0; i < 58; i++)
										tcp[i] = pub[i];
									tcp[0] = 2; //type
									//取值为5时，把rat填成1，其余取值照填
									if(rat == 5){
										tcp[17] = 1;
									}
									cursor += 239;
									// ultcp_responsetime
									long ultcp_responsetime = con.getUnsigendInt(new byte[] {
											data[++cursor], data[++cursor], data[++cursor],
											data[++cursor] });
									// dltcp_responsetime
									long dltcp_responsetime = con.getUnsigendInt(new byte[] {
											data[++cursor], data[++cursor], data[++cursor],
											data[++cursor] });
									// tcplink_count
									cursor += 25;
									//int tcplink_count = data[++cursor] & 0xFF;
									// status   (tcplink_state)
									int status = data[++cursor] & 0xFF;
									tcp[58] = data[cursor];
									// 推送数据
									putTermTcpData(tcp, status == 0 ? (ultcp_responsetime + dltcp_responsetime) : 0);
									// 指针复位
									cursor -= 273;
								} 
							}
							
							if(apptype_code == 101){ //DNS
								//取值为5时，把rat填成1，其余取值照填
								byte[] dns = new byte[59];
								for (int i = 0; i < 58; i++)
									dns[i] = pub[i];
								dns[0] = 3; //type
								if(rat == 5){
									dns[9] = 1;
								}
								// dnscode   
								cursor += 353;
								int dnscode = data[++cursor] & 0xFF;
								dns[58] = data[cursor];
								// 推送数据
								putTermDnsData(dns, dnscode == 0 ? (enddate - startdate) : 0);
								
								cursor -= 354;
							}
							
							if (apptype_code == 103) { //HTTP
								byte[] http = new byte[64];
								for (int i = 0; i < 58; i++)
									http[i] = pub[i];
								//取值为5时，把rat填成1，其余取值照填
								if(rat == 5){
									http[17] = 1;
								}
								// 填充apptype
								cursor += 156;
								http[58] = data[++cursor];
								http[59] = data[++cursor];
								// 填充appsubtype
								http[60] = data[++cursor];
								http[61] = data[++cursor];
								// ulthroughput、dlthroughput
								cursor += 47;
								long ulthroughput = con.getUnsigendInt(new byte[] {
										data[++cursor], data[++cursor], data[++cursor],
										data[++cursor] });
								long dlthroughput = con.getUnsigendInt(new byte[] {
										data[++cursor], data[++cursor], data[++cursor],
										data[++cursor] });
								
								// 填充code
								cursor += 62;
								http[62] = data[++cursor];
								http[63] = data[++cursor];
								int code = con.getUnsigendShort(new byte[] {http[62], http[63] }); 
								
								// responsetime
								long responsetime = con.getUnsigendInt(new byte[] {
										data[++cursor], data[++cursor], data[++cursor],
										data[++cursor] });
								//lastpacket_delay
								long lastpacket_delay = con.getUnsigendInt(new byte[] {
										data[++cursor], data[++cursor], data[++cursor],
										data[++cursor] });
								
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
								putTermServiceData(http, ulthroughput, dlthroughput, responsetime,
										ulthroughput2, dlthroughput2, delay_sum, 1);
								
								// 指针复位
								cursor -= 287;
							}
						}
					}
					
				// 跳到尾部 
				cursor += length;
			} else if (interf == 5) {  //终端信令面，目前只处理s1mme
				//imei 
				cursor += 30;
				byte[] _imei = new byte[] { data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor]};
				String imei="";
				try {
					 imei = FormatUtils.TBCDFormat(_imei);
				} catch (Exception e) {
					System.out.println("Exception 信令面:"+e);
				}
				//System.out.println("imei号:"+imei);
				cursor -= 38; //复位指针
				if(!"".equals(imei)){
					String kpi_imei = imei.substring(0, 8);
					Terimei ter = imeiMap.get(kpi_imei);
					if(ter!=null){
						byte[] pub = new byte[61];
						int terfac_id = 0, tertype_id = 0;
						terfac_id = ter.getTerfac_id();
						tertype_id = ter.getTertype_id();
						
						cursor += 54;
						int type = data[++cursor] & 0xFF;
						cursor -= 55;//复位指针
						
						if(type == 1||type == 7||type == 100||type == 5){
							//设置type
							if(type == 1){
								pub[0] = 5;
							}else if(type == 7){
								pub[0] = 6;
							}else if(type == 100){
								pub[0] = 7;
							}else if(type == 5){
								pub[0] = 8;
							}
							
							//设置时间
							cursor += 55;
							long startdate = con.getLong(new byte[] { data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							// 颗粒度分钟
							long timebymin = con.time2min_term(startdate);
							byte[] min = con.getBytes(timebymin);
							pub[1] = min[0];
							pub[2] = min[1];
							pub[3] = min[2];
							pub[4] = min[3];
							pub[5] = min[4];
							pub[6] = min[5];
							pub[7] = min[6];
							pub[8] = min[7];

							long enddate = con.getLong(new byte[] { data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor], data[++cursor], data[++cursor],
									data[++cursor] });
							
							//填充terfac_id 和 tertype_id
							byte[] terfac = ConvertUtils.intToBytes(terfac_id);
							byte[] tertype = ConvertUtils.intToBytes(tertype_id);
							for (int i = 9; i < 13; i++) 
								pub[i] = terfac[i-9];
							for (int i = 13; i < 17; i++) 
								pub[i] = tertype[i-13];
							//rat
							cursor -= 50;
							int rat = data[++cursor] & 0xFF;
							pub[17] = data[cursor];
							if(rat == 5){
								pub[17] = 1;
							}
							
							// 填充city
							cursor -= 20; 
							pub[18] = data[++cursor];
							pub[19] = data[++cursor];

							// 填充mmeip // 填充eNBip
							cursor += 118;
							for (int i = 20; i < 52; i++)
								pub[i] = data[++cursor];
							
							// 填充tac
							cursor += 4;
							pub[52] = data[++cursor];
							pub[53] = data[++cursor];
							// 填充cellid
							pub[54] = data[++cursor];
							pub[55] = data[++cursor];
							pub[56] = data[++cursor];
							pub[57] = data[++cursor];
							
							//填充failure_cause
							cursor -= 90;
							pub[58] = data[++cursor];
							pub[59] = data[++cursor];
							
							//填充status
							cursor -= 5;
							pub[60] = data[++cursor];
							int status = data[cursor] & 0xFF;
							
							// 指针复位
							cursor -= 72;
							
							//推送数据data
							putTermS1mmeData(pub, status == 0 ? (enddate - startdate) : 0);
						}
					}
				}
				
				//跳向XDR尾部
				cursor += length;
			} else {
				if(interf == 1||interf == 2||interf == 3||interf == 4||interf == 6
						||interf == 7||interf == 8||interf == 9||interf == 10||interf == 12){
					//跳向XDR尾部
					cursor += length;
				}else{
					System.out.println("[XDR ERR] inf:" + interf);
					break;
				}
			}
		}
	}
	
	
	public void createS1mme(byte[] data, int cursor, int type){
		
	}
	
	//FLOW
	public void createFlow(byte[] data, int cursor){
		byte[] pub = new byte[56];
		pub[0] = 4; //默认 type 为1 term_service 2为term_tcp 3为term_dns 4为flow
		// 填充startdate
		cursor += 138;
		long startdate = con.getLong(new byte[] { data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		// 颗粒度分钟
		long timebymin = con.time2min_term(startdate);
		byte[] min = con.getBytes(timebymin);
		pub[1] = min[0];
		pub[2] = min[1];
		pub[3] = min[2];
		pub[4] = min[3];
		pub[5] = min[4];
		pub[6] = min[5];
		pub[7] = min[6];
		pub[8] = min[7];
		
		//protocoltype
		cursor += 8;
		pub[54] = data[++cursor];
		pub[55] = data[++cursor];
		
		//填充rat
		cursor -= 135;
		int rat = data[++cursor] & 0xFF;
		pub[9] = data[cursor];
		if(rat == 5){
			pub[9] = 1;
		}
		
		// 填充city
		cursor -= 20; 
		pub[10] = data[++cursor];
		pub[11] = data[++cursor];

		// 填充ip
		cursor += 51;
		for (int i = 12; i < 44; i++)
			pub[i] = data[++cursor];
		
		// 填充tac
		cursor += 12;
		pub[44] = data[++cursor];
		pub[45] = data[++cursor];
		// 填充cellid
		pub[46] = data[++cursor];
		pub[47] = data[++cursor];
		pub[48] = data[++cursor];
		pub[49] = data[++cursor];
		
		// 填充apptype
		cursor += 51;
		pub[50] = data[++cursor];
		pub[51] = data[++cursor];
		// 填充appsubtype
		pub[52] = data[++cursor];
		pub[53] = data[++cursor];
		// ulthroughput、dlthroughput
		cursor += 47;
		long ulthroughput = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		long dlthroughput = con.getUnsigendInt(new byte[] {
				data[++cursor], data[++cursor], data[++cursor],
				data[++cursor] });
		//复位 到
		cursor -= 215;
		
		//推送数据
		putFlowData(pub, ulthroughput, dlthroughput);
	}
	
	//Term终端 预统计 LTE_HTTP
	public void putTermServiceData(byte[] key, long ulthroughput, long dlthroughput,
			long responsetime, long ulthroughput2, long dlthroughput2, 
			long delay_sum, long req_num){
		ByteValue b = new ByteValue(key);
		Term_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initTermService(key, ulthroughput, dlthroughput, responsetime, 
					ulthroughput2, dlthroughput2, delay_sum, req_num, 1);
		} else {
			v.addUlthroughput(ulthroughput);
			v.addDlthroughput(dlthroughput);
			v.addResponsetime(responsetime);
			v.addUlthroughput2(ulthroughput2);
			v.addDlthroughput2(dlthroughput2);
			v.addDelay_sum(delay_sum);
			v.addReq_num(req_num);
		}
	}
	
	
	//
	public void putTermTcpData(byte[] key, long delay_sum) {
		ByteValue b = new ByteValue(key);
		Term_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initTermTcp(key, delay_sum, 1, 2);
		} else {
			v.addDelay_sum(delay_sum);
			v.addReq_num(1);
		}
	}
	
	//
	public void putTermDnsData(byte[] key, long delay_sum) {
		ByteValue b = new ByteValue(key);
		Term_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initTermDns(key, delay_sum, 1, 3);
		} else {
			v.addDelay_sum(delay_sum);
			v.addReq_num(1);
		}
	}
	
	//LTE_Term_S1mme
	public void putTermS1mmeData(byte[] key, long delay_sum) {
		ByteValue b = new ByteValue(key);
		Term_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initTermS1mme(key, delay_sum, 1, 0);
		} else {
			v.addDelay_sum(delay_sum);
			v.addReq_num(1);
		}
	}
	
	//Flow
	public void putFlowData(byte[] key, long ulthroughput, long dlthroughput) {
		ByteValue b = new ByteValue(key);
		Term_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initFlow(key, ulthroughput, dlthroughput, 1, 4);
		} else {
			v.addUlthroughput(ulthroughput);
			v.addDlthroughput(dlthroughput);
			v.addReq_num(1);
		}
	}
}