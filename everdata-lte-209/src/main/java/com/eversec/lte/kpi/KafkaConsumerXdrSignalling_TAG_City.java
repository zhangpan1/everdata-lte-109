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
import com.eversec.lte.kpi.bean.StreamBean;
import com.eversec.lte.kpi.bean.TAG;
import com.eversec.lte.kpi.bean.TAG_Value;
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

public class KafkaConsumerXdrSignalling_TAG_City implements KafkaConsumerHandler {

	public final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static BlockingQueue<TAG_Value> queue;
	private final static Thread worker;
	private Thread countWork;
	private int get_count_second;
	private boolean count_work;
	private static Map<String, Terimei> imeiMap; 

	static {
		imeiMap = ReadTxtUtils.readKpiImeiMap();
		
		queue = new ArrayBlockingQueue<TAG_Value>(
				KPIConfig.getMiddleCacheSize(), true);

		worker = new Thread(new Runnable() {
			private Map<Long, Map<ByteValue, TAG_Value>> total;
			private Map<String, StreamBean> map;

			@Override
			public void run() {
				total = new HashMap<Long, Map<ByteValue, TAG_Value>>();
				map = new HashMap<String, StreamBean>();
				
				ConvertUtils con = new ConvertUtils();
				int max = KPIConfig.getBlockNum();
				if (max < 2)
					max = 2;
				TAG_Value data = null;
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
								Map<ByteValue, TAG_Value> values = total
										.get(Long.valueOf(time));
								ByteValue bv = new ByteValue(b);

								if (values != null) {
									TAG_Value value = values.get(bv);
									if (value != null) {
										value.addAll(data);
									} else {
										values.put(bv, data);
									}
								} else {
									Map<ByteValue, TAG_Value> v = new HashMap<ByteValue, TAG_Value>();
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
							Map<ByteValue, TAG_Value> outData = total
									.remove(outTime);
							flush(outData, "");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			public void flush(Map<ByteValue, TAG_Value> outData, String val) throws Exception {
				System.out.println("[Flash data TAG ...]");
				String curr_date = null, city = "", path = null;
				TAG_Value v = null;
				Iterator<TAG_Value> iter = outData.values().iterator();
				while (iter.hasNext()) {
					v = iter.next();
					int type = v.getType();
					TAG tag = new TAG(v);
					if(type == 2){
						System.out.println(type);
					}
					if(type == 1||type == 2||type == 3){
						if(curr_date == null)
							curr_date = tag.getStartDate();
						city = tag.getCity();
						path = KPIConfig.getTAGOutFile(type) + curr_date +"_"+ city;
						proessFlush(tag, type, curr_date, path, city);
					}else{
						System.out.println("flush TAG type=" + type + "异常 : Exception !!!");
					}
				}
				
				Iterator<StreamBean> itrmap = map.values().iterator();
				while(itrmap.hasNext()){
					StreamBean model = itrmap.next();
					close(model.getOut(), model.getPath());
				}
				map.clear();
			}
			
			public void proessFlush(Object tag, Integer type, String curr_date, String path, String city){
				try {
					if(map!=null && map.get(city+"_"+type)!=null){          //if(map.containsKey(city)){
						BufferedOutputStream out = map.get(city+"_"+type).getOut();
						write(out, path, tag.toString().getBytes());
					}else{
						File f = new File(path);
						if (!f.getParentFile().exists())
							f.getParentFile().mkdirs();
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
						StreamBean model = new StreamBean();
						model.setOut(out);
						model.setPath(path);
						map.put(city+"_"+type, model);
						write(out, path, tag.toString().getBytes());
					}
				} catch (Exception e) {
					e.printStackTrace();
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

	private LoadingCache<ByteValue, TAG_Value> cache;

	public KafkaConsumerXdrSignalling_TAG_City(final String id) {
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
		CacheLoader<ByteValue, TAG_Value> loader = new CacheLoader<ByteValue, TAG_Value>() {
			@Override
			public TAG_Value load(ByteValue key) throws Exception {
				return new TAG_Value();
			}
		};
		RemovalListener<ByteValue, TAG_Value> listener = new RemovalListener<ByteValue, TAG_Value>() {
			@Override
			public void onRemoval(RemovalNotification<ByteValue, TAG_Value> data) {
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
				// 业务类型
				cursor += 132;
				int apptype_code = data[++cursor] & 0xFF; //apptype_code
				// 指针复位
				cursor -= 138;
				
				
				//imei 
				cursor += 30;
				byte[] _imei = new byte[] { data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor]};
				//复位指针
				cursor -= 38; 
				String imei="";
				try {
					 imei = FormatUtils.TBCDFormat(_imei);
				} catch (Exception e) {
					System.out.println("Exception:"+e);
				}
				//System.out.println("imei号: 用户面:"+imei);
				int terclass_id = 0;
				if(!"".equals(imei)&&imei.length()>8){
					String kpi_imei = imei.substring(0, 8);
					Terimei ter = imeiMap.get(kpi_imei);
					if(ter!=null){
						terclass_id = ter.getTerclass_id();
					}
				}	
				
				/**
				 * 公共字段
				 */
				byte[] pub = new byte[39];

				// 填充startdate
				cursor += (114 + 24);
				long startdate = con.getLong(new byte[] { data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor], data[++cursor], data[++cursor],
						data[++cursor] });
				// 颗粒度分钟
				long timebymin = con.time2min_tag(startdate);
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
				cursor -= 152;
				pub[8] = data[++cursor];
				pub[9] = data[++cursor];

				// 填充imsi
				cursor += 18;
				for (int i = 10; i < 18; i++)
					pub[i] = data[++cursor];
				// 填充msisdn
				cursor += 8;
				for (int i = 18; i < 34; i++)
					pub[i] = data[++cursor];
				
				//填充terclass_id
				byte[] terfac = ConvertUtils.intToBytes(terclass_id);
				for (int i = 34; i < 38; i++)
					pub[i] = terfac[i-34];
				
				// 填充 rat
				cursor -= 33;
				int rat = data[++cursor] & 0xFF;
				pub[38] = data[cursor];
				if(rat == 5)
					pub[38] = 1;

				// 指针复位
				cursor -= 22;
				
				/**
				 *    APPTYPETAG
				 */
				byte[] appTag = new byte[41];
				for (int i = 0; i < 39; i++)
					appTag[i] = pub[i];
				cursor += 156;
				//apptype
				appTag[39] =  data[++cursor];
				appTag[40] =  data[++cursor];
				
				long throughput = 0;
				// 排除 EMAIL 105 MMS 102 
				if(apptype_code != 105&&apptype_code != 102){
					cursor += 49;
					long ulthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					long dlthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					throughput = ulthroughput + dlthroughput;
					cursor -= 57;
				}
				
				long delay_sum = 0;
				if(apptype_code == 103){
					cursor += 119;
					//code
					int code = con.getUnsigendShort(new byte[] {data[++cursor], data[++cursor] }); 
					// responsetime
					long responsetime = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					//lastpacket_delay
					long lastpacket_delay = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					if (code>0 && code<400 && lastpacket_delay >= responsetime) {
						delay_sum = enddate - startdate;
					}
					cursor -= 129;
				}
				
				// 指针复位
				cursor -= 158;
				
				// 推送数据
				putApptypeTagData(appTag, throughput, 1, delay_sum);
					
				/**
				 * APPSUBTYPETAG
				 */
				byte[] appsubTag = new byte[43];
				for (int i = 0; i < 39; i++)
					appsubTag[i] = pub[i];
				cursor += 156;
				//apptype
				appsubTag[39] =  data[++cursor];
				appsubTag[40] =  data[++cursor];
//				//appsubtype
				appsubTag[41] =  data[++cursor];
				appsubTag[42] =  data[++cursor];
				
				
				throughput = 0;
				// 排除 EMAIL 105 MMS 102 
				if(apptype_code != 105&&apptype_code != 102){
					cursor += 49;
					long ulthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					long dlthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					throughput = ulthroughput + dlthroughput;
					cursor -= 57;
				}
				
				delay_sum = 0;
				if(apptype_code == 103){
					cursor += 119;
					//code
					int code = con.getUnsigendShort(new byte[] {data[++cursor], data[++cursor] }); 
					// responsetime
					long responsetime = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					//lastpacket_delay
					long lastpacket_delay = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					if (code>0 && code<400 && lastpacket_delay >= responsetime) {
						delay_sum = enddate - startdate;
					}
					cursor -= 129;
				}
				
				// 指针复位
				cursor -= 160;
				
				// 推送数据
				putAppsubtypeTagData(appsubTag, throughput, 1, delay_sum);
				
				/**
				 * 	NightTag        
				 */
				byte[] nightTag = new byte[39];
				for (int i = 0; i < 39; i++)
					nightTag[i] = pub[i];
				
				long ulthroughput = 0;
				long dlthroughput = 0;
				// 排除 EMAIL 105 MMS 102 
				if(apptype_code != 105&&apptype_code != 102){
					cursor += 207;
					ulthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					dlthroughput = con.getUnsigendInt(new byte[] {
							data[++cursor], data[++cursor], data[++cursor],
							data[++cursor] });
					cursor -= 215;
				}
				
				putNightTagData(nightTag, ulthroughput, dlthroughput, 0);
				
				// 跳到尾部
				cursor += length;
			} else {
				cursor += length - 5;
				//System.out.println("ERR inf-" + interf);
			}
		}
	}
		
	//	推送 	NightTag
	public void putNightTagData(byte[] key, long ulthroughput, long dlthroughput, int nightthroughput) {
		ByteValue b = new ByteValue(key);
		TAG_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initNightTag(key, ulthroughput, dlthroughput, nightthroughput, 3);
		} else {
			v.addUlthroughput(ulthroughput);
			v.addDlthroughput(dlthroughput);
			v.addNightThroughput(nightthroughput);
		}
	}

	public void putApptypeTagData(byte[] key, long throughput, int times, long delay_sum) {
		ByteValue b = new ByteValue(key);
		TAG_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initApptypeTag(key, throughput, times, delay_sum, 1);
		} else {
			v.addThroughput(throughput);
			v.addTimes(times);
			v.addDelay_sum(delay_sum);
		}
	}
	
	public void putAppsubtypeTagData(byte[] key, long throughput, int times, long delay_sum) {
		ByteValue b = new ByteValue(key);
		TAG_Value v = cache.getUnchecked(b);
		if (!v.init()) {
			v.initAppsubtypeTag(key, throughput, times, delay_sum, 2);
		} else {
			v.addThroughput(throughput);
			v.addTimes(times);
			v.addDelay_sum(delay_sum);
		}
	}
}
