package com.eversec.lte.processor.statistics;

import static com.eversec.lte.processor.data.StaticData.ABANDON_RAW_COUNT;
import static com.eversec.lte.processor.data.StaticData.ABANDON_S1U_XDR_2_SDTP_COUNT;
import static com.eversec.lte.processor.data.StaticData.ABANDON_XDR_COUNT;
import static com.eversec.lte.processor.data.StaticData.CXDR_SIGNALING_CACHE;
import static com.eversec.lte.processor.data.StaticData.CXDR_TYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.FAILING_FILL_COUNT;
import static com.eversec.lte.processor.data.StaticData.FULLINFO_COUNT;
import static com.eversec.lte.processor.data.StaticData.RAW_2_KAFKA_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_2_KAFKA_COUNT;
import static com.eversec.lte.processor.data.StaticData.RAW_2_KAFKA_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_2_SDTP_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_BYTES;
import static com.eversec.lte.processor.data.StaticData.RAW_RECEIVE_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.REFILL_COUNT;
import static com.eversec.lte.processor.data.StaticData.REPLACED_COUNT;
import static com.eversec.lte.processor.data.StaticData.S1U_APPTYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.SOURCE_COUNT;
import static com.eversec.lte.processor.data.StaticData.SUCC_FILL_COUNT;
import static com.eversec.lte.processor.data.StaticData.XDR_2_KAFKA_BYTES;
import static com.eversec.lte.processor.data.StaticData.XDR_2_KAFKA_COUNT;
import static com.eversec.lte.processor.data.StaticData.XDR_2_KAFKA_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_BYTES;
import static com.eversec.lte.processor.data.StaticData.XDR_2_SDTP_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.XDR_PROCEDURE_TYPE_CACHE;
import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_BYTES;
import static com.eversec.lte.processor.data.StaticData.XDR_RECEIVE_PACKAGE;
import static com.eversec.lte.processor.data.StaticData.XDR_TYPE_CACHE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.main.LteMain;
import com.eversec.lte.processor.data.QueueData;
import com.eversec.lte.processor.data.StaticData;
import com.eversec.lte.vo.DataQueueCache;
import com.eversec.lte.vo.compound.CompStatisInfo;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpStaticServer {

	private static Logger LOGGER = LoggerFactory
			.getLogger(HttpStaticServer.class);

	public static void start(int port) throws Exception {

		final long startTime = System.currentTimeMillis();
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);// 设置HttpServer的端口
		server.createContext("/", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("welcome !");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					StringBuilder info = new StringBuilder();
					info.append("Welcome to SdtpServer! \n");
					info.append("Usage:\n");
					info.append("/reset\n");
					info.append("\t Reset All Xdr Count!\n");
					info.append("/stat\n");
					info.append("\t Print statistics information!\n");
					info.append("/raw_stat\n");
					info.append("\t for test!\n");
					info.append("/export\n");
					info.append("\t export rule to file "
							+ SdtpConfig.getBackFillRuleFile() + "!\n");
					info.append("/raw_switch\n");
					info.append("\t  process raw  ,default is true!\n");
					info.append("/xdr_switch\n");
					info.append("\t  process xdr  ,default is true!\n");
					info.append("/s1u2app_switch\n");
					info.append("\t  whether s1u send 2 app server  ,default is true!\n");
					responseBody.write(info.toString().getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/reset", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				reset();
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					Headers requestHeaders = exchange.getRequestHeaders();
					Set<String> keySet = requestHeaders.keySet();
					Iterator<String> iter = keySet.iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						List<?> values = requestHeaders.get(key);
						String s = key + " = " + values.toString() + "\n";
						responseBody.write(s.getBytes());
					}
					responseBody.close();
				}
			}
		});
		server.createContext("/stat", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("stat !");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					StringBuilder info = getStatStr(startTime);
					responseBody.write(info.toString().getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/raw_stat", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("raw_stat !");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					// InputStream requestBody =

					String parameter = exchange.getRequestURI().toString();
					// remove /raw_stat?
					String xdrId = parameter.substring(10);
					String result = "startTime:" + RawStat.getStartTime()
							+ "\n";
					result += "query xdrId:" + xdrId + "\n";
					result += "result:" + RawStat.queryByXdrId(xdrId) + "\n";

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write(result.getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/export", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("export!");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					File exportFile = new File(SdtpConfig.getBackFillRuleFile());
					//20151021
//					RuleCache cache = AbstractBackFill.PERSISTENT_RULE_CACHE;
//					ConcurrentMap<String, RuleData> map = cache.asMap();
//					PrintWriter pw = new PrintWriter(exportFile);
//					for (Map.Entry<String, RuleData> entry : map.entrySet()) {
//						pw.println(entry.getValue().getRule());
//					}
//					pw.close();

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write(exportFile.getAbsolutePath().getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/xdr_switch", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("xdr_switch!");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {

					SdtpConfig.IS_PROCESS_XDR = !SdtpConfig.IS_PROCESS_XDR;

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write(("is_process_xdr:"
							+ SdtpConfig.IS_PROCESS_XDR + ",abandon_xdr_count:"
							+ StaticData.ABANDON_XDR_COUNT + "\n").getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/raw_switch", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("raw_switch!");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {

					SdtpConfig.IS_PROCESS_RAW = !SdtpConfig.IS_PROCESS_RAW;

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write(("is_process_raw:"
							+ SdtpConfig.IS_PROCESS_RAW + ",abandon_raw_count:"
							+ StaticData.ABANDON_RAW_COUNT + "\n").getBytes());
					responseBody.close();
				}
			}
		});
		server.createContext("/s1u2app_switch", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				LOGGER.info("s1u_switch!");
				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {

					SdtpConfig.IS_OUTPUT_FILLED_S1U_2_SDTP = !SdtpConfig.IS_OUTPUT_FILLED_S1U_2_SDTP;

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write(("is_output_filled_s1u_2_sdtp:"
							+ SdtpConfig.IS_OUTPUT_FILLED_S1U_2_SDTP
							+ ",abandon_s1u_xdr_2_sdtp_count:"
							+ StaticData.ABANDON_S1U_XDR_2_SDTP_COUNT + "\n")
							.getBytes());
					responseBody.close();
				}
			}
		});
		server.setExecutor(LteMain.PROCESS_EXEC);
		server.start();
		LOGGER.info("http server is listenig at port : {} ", port);
	}

	/**
	 * 增加合成统计项
	 * 
	 * @param info
	 */
	public static void addCompStaticInfo(CompStatisInfo info) {
		try {
			SOURCE_COUNT.addAndGet(info.getSourceCount().get());
			XDR_2_SDTP_PACKAGE.addAndGet(info.getXdr2sdtpPackage().get());
			XDR_2_SDTP_BYTES.addAndGet(info.getXdr2sdtpBytes().get());

			for (Map.Entry<String, AtomicLong> entry : info.getCxdrTypeCache()
					.entrySet()) {
				CXDR_TYPE_CACHE.get(entry.getKey()).getValue()
						.addAndGet(entry.getValue().get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清空全部统计项
	 */
	public static void reset() {
		LOGGER.info("reset stat !");
		SUCC_FILL_COUNT.set(0);
		FAILING_FILL_COUNT.set(0);
		REPLACED_COUNT.set(0);
		SOURCE_COUNT.set(0);
		REFILL_COUNT.set(0);
		FULLINFO_COUNT.set(0);

		XDR_RECEIVE_PACKAGE.set(0);
		XDR_RECEIVE_BYTES.set(0);
		RAW_RECEIVE_PACKAGE.set(0);
		RAW_RECEIVE_BYTES.set(0);

		XDR_2_SDTP_PACKAGE.set(0);
		RAW_2_SDTP_PACKAGE.set(0);
		XDR_2_SDTP_BYTES.set(0);
		RAW_2_SDTP_BYTES.set(0);

		XDR_2_KAFKA_PACKAGE.set(0);
		XDR_2_KAFKA_BYTES.set(0);
		XDR_2_KAFKA_COUNT.set(0);
		RAW_2_KAFKA_COUNT.set(0);
		RAW_2_KAFKA_PACKAGE.set(0);
		RAW_2_KAFKA_BYTES.set(0);

		ABANDON_RAW_COUNT.set(0);
		ABANDON_XDR_COUNT.set(0);
		ABANDON_S1U_XDR_2_SDTP_COUNT.set(0);

		XDR_TYPE_CACHE.invalidateAll();
		XDR_PROCEDURE_TYPE_CACHE.invalidateAll();
		S1U_APPTYPE_CACHE.invalidateAll();
		CXDR_TYPE_CACHE.invalidateAll();
		
		CXDR_SIGNALING_CACHE.invalidateAll();

	}

	/**
	 * 得到统计信息
	 * 
	 * @param startTime
	 * @return
	 */
	protected static StringBuilder getStatStr(long startTime) {
		StringBuilder message = new StringBuilder();

		message.append("Sdtp Server ! \n");

		message.append(MessageFormat.format("running time : {0} ms \n",
				(System.currentTimeMillis() - startTime)));
		message.append(MessageFormat
				.format("xdr_receive_package : {0} , xdr_receive_bytes : {1} , raw_receive_package : {2} ,raw_receive_bytes : {3}\n",
						XDR_RECEIVE_PACKAGE, XDR_RECEIVE_BYTES,
						RAW_RECEIVE_PACKAGE, RAW_RECEIVE_BYTES));

		message.append(MessageFormat
				.format("count : {0} , succ : {1} , fail : {2} , refill : {3} ,full : {4} \n",
						SOURCE_COUNT, SUCC_FILL_COUNT, FAILING_FILL_COUNT,
						REFILL_COUNT, FULLINFO_COUNT));
//		message.append(MessageFormat.format(
//				"rule0 : {0} , rule1 : {1} , rule2 : {2} , rule4 : {3} \n",
//				RULE0_CACHE.size(), RULE1_CACHE.size(), RULE2_CACHE.size(),
//				RULE4_CACHE.size()));

		message.append(MessageFormat.format("xdr_pending_data_queue : {0} \n",
				QueueData.PENDING_XDR_DATA_QUEUE.size()));

		message.append(MessageFormat
				.format("xdr_pending : {0} \n" ,
 						QueueData.PROCESS_XDR_DATA_QUEUE.size() ));

//		message.append(MessageFormat
//				.format("s11_pending : {0} ,s1u_pending : {1} , uu_pending : {2} , x2_pending : {3} , uemr_pending : {4} \n",
//						S11_PENDING_CACHE.size(), S1U_PENDING_CACHE.size(),
//						UU_PENDING_CACHE.size(), X2_PENDING_CACHE.size(),
//						UEMR_PENDING_CACHE.size()));

//		message.append(MessageFormat
//				.format("sgs_source : {0} ,s1mme_source: {1} , s6a_source: {2} ,s11_source: {3} \n",
//						S1MME_SOURCE_QUEUE.size(), SGS_SOURCE_QUEUE.size(),
//						S6A_SOURCE_QUEUE.size(), S11_SOURCE_QUEUE.size()));
//
//		message.append(MessageFormat
//				.format("s1u_source : {0} ,uu_source: {1} ,x2_source : {2} ,uemr_source: {3} ,cellmr_source: {4} \n",
//						S1U_SOURCE_QUEUE.size(), UU_SOURCE_QUEUE.size(),
//						X2_SOURCE_QUEUE.size(), UEMR_SOURCE_QUEUE.size(),
//						CELLMR_SOURCE_QUEUE.size()));
//
//		message.append(MessageFormat
//				.format("s1mme_filled: {0} ,sgs_filled : {1} , s6a_filled: {2} ,s11_filled: {3} \n",
//						S1MME_FILLED_QUEUE.size(), SGS_FILLED_QUEUE.size(),
//						S6A_FILLED_QUEUE.size(), S11_FILLED_QUEUE.size()));
//
//		message.append(MessageFormat
//				.format("s1u_filled : {0} , uu_filled: {1} ,x2_filled : {2} ,uemr_filled: {3} ,cellmr_filled: {4} \n",
//						S1U_FILLED_QUEUE.size(), UU_FILLED_QUEUE.size(),
//						X2_FILLED_QUEUE.size(), UEMR_FILLED_QUEUE.size(),
//						CELLMR_FILLED_QUEUE.size()));

		message.append(MessageFormat
				.format("xdr_2_sdtp_package: {0} ,raw_2_sdtp_package : {1} ,xdr_2_sdtp_bytes: {2} , raw_2_sdtp_bytes: {3} \n",
						XDR_2_SDTP_PACKAGE.get(), RAW_2_SDTP_PACKAGE.get(),
						XDR_2_SDTP_BYTES.get(), RAW_2_SDTP_BYTES.get()));

		message.append(DataQueueCache.Info() + "\n");

		message.append(MessageFormat.format("xdr_type : {0} \n",
				XDR_TYPE_CACHE.asMap()));

		message.append(MessageFormat.format("procedure_type : {0} \n",
				XDR_PROCEDURE_TYPE_CACHE.asMap()));

		message.append(MessageFormat.format("s1u_apptype : {0} \n",
				S1U_APPTYPE_CACHE.asMap()));

		message.append(MessageFormat.format("cxdr_type : {0} \n",
				CXDR_TYPE_CACHE.asMap()));
		
		message.append(MessageFormat.format("cxdr_signaling : {0} \n",
				CXDR_SIGNALING_CACHE.asMap()));

		message.append(MessageFormat.format(
				"is_process_xdr : {0} , abandon_xdr_count:{1} \n",
				SdtpConfig.IS_PROCESS_XDR, StaticData.ABANDON_XDR_COUNT));

		message.append(MessageFormat.format(
				"is_process_raw : {0} , abandon_raw_count:{1} \n",
				SdtpConfig.IS_PROCESS_RAW, StaticData.ABANDON_RAW_COUNT));

		message.append(MessageFormat.format(
				"is_output_filled_s1u_2_sdtp : {0} , abandon_s1u_xdr_2_sdtp_count:{1} \n",
				SdtpConfig.IS_OUTPUT_FILLED_S1U_2_SDTP, StaticData.ABANDON_S1U_XDR_2_SDTP_COUNT));

		return message;
	}
}
