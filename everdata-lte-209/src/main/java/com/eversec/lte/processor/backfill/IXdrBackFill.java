package com.eversec.lte.processor.backfill;

import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.processor.backfill.AbstractBackFill.RuleType;
import com.eversec.lte.vo.backfill.S11FillParam;
import com.eversec.lte.vo.backfill.S1UFillParam;
import com.eversec.lte.vo.backfill.S1mmeFillParam;
import com.eversec.lte.vo.backfill.S6aFillParam;
import com.eversec.lte.vo.backfill.SgsFillParam;
import com.eversec.lte.vo.backfill.UemrFillParam;
import com.eversec.lte.vo.backfill.UuFillParam;
import com.eversec.lte.vo.backfill.X2FillParam;

/**
 * 回填逻辑处理
 * @author lirongzhi
 *
 */
public interface IXdrBackFill {

	/**
	 * UU回填逻辑
	 * 
	 * @param param
	 * @param data
	 * @return
	 */
	void fillUu(UuFillParam param, XdrSingleSourceUu data);

	/**
	 * UU重新尝试回填
	 * 
	 * @param key
	 * @param data
	 */
	void refillUu(XdrSingleSourceUu data);

	/**
	 * X2回填逻辑
	 * 
	 * @param param
	 * @param data
	 * @return
	 */
	void fillX2(X2FillParam param, XdrSingleSourceX2 data);

	/**
	 * X2重新尝试回填
	 * 
	 * @param key
	 * @param data
	 */
	void refillX2(XdrSingleSourceX2 data);

	/**
	 * UE_MR回填逻辑
	 * 
	 * @param param
	 * @param data
	 * @return
	 */
	void filledUemr(UemrFillParam param, XdrSingleSourceUEMR data);

	/**
	 * UE_MR重新尝试回填
	 * 
	 * @param key
	 * @param data
	 */
	void refillUEMR(XdrSingleSourceUEMR data);

	/**
	 * s11回填逻辑
	 * 
	 * <pre>
	 * 通过s11建立3种映射关系
	 * 1.userIpv4_sgwTeid --> msisdn_imsi_imei
	 * 2.imsi --> msisdn_imsi_imei
	 * 3.imei --> msisdn_imsi_imei
	 * </pre>
	 * 
	 * @param param
	 * @param data
	 * @return
	 */
	void fillS11(S11FillParam param, XdrSingleSourceS10S11 data);

	/**
	 * s11重新尝试回填
	 * 
	 * @param data
	 */
	void refillS11(XdrSingleSourceS10S11 data);

	/**
	 * s1mme回填逻辑
	 * 
	 * <pre>
	 * 建立5种回填规则
	 * 1.userIpv4_sgwTeid --> msisdn_imsi_imei
	 * 2.imsi --> msisdn_imsi_imei
	 * 3.imei --> msisdn_imsi_imei
	 * 4.mmeS1apID_mmeGroupId_mmeCode --> msisdn_imsi_imei
	 * 5.mmeGroupId_mmeCode_m_tmsi --> msisdn_imsi_imei
	 * </pre>
	 * 
	 * @param param
	 * @param data
	 * @return
	 */
	void fillS1mme(S1mmeFillParam param, XdrSingleSourceS1MME data);

	/**
	 * s1mme重新尝试回填
	 * 
	 * @param key
	 * @param data
	 */
	void refillS1mme(XdrSingleSourceS1MME data);

	/**
	 * s6a回填逻辑
	 * 
	 * @param msisdn
	 * @param imsi
	 * @param imei
	 * @param data
	 * @return
	 */
	void fillS6a(S6aFillParam param, XdrSingleSourceS6a data);

	/**
	 * 重新尝试s6a回填
	 * 
	 * @param data
	 */
	void refillS6a(XdrSingleSourceS6a data);

	/**
	 * sgs回填逻辑
	 * 
	 * @param msisdn
	 * @param imsi
	 * @param imei
	 * @param data
	 * @return
	 */
	void fillSgs(SgsFillParam param, XdrSingleSourceSGs data);

	/**
	 * 重新尝试sgs回填
	 * 
	 * @param data
	 */
	void refillSgs(XdrSingleSourceSGs data);

	/**
	 * s1u回填逻辑
	 * 
	 * @param msisdn
	 * @param imsi
	 * @param imei
	 * @param data
	 * @return
	 */
	void fillS1U(S1UFillParam param, XdrSingleSourceS1U data);

	/**
	 * 重新尝试s1u回填
	 * 
	 * @param data
	 */
	void refillS1U(XdrSingleSourceS1U data);

	/**
	 * 获取回填信息，msisdn_imsi_imei
	 * 
	 * @param key
	 * @return
	 */
	XdrBackFillInfo getInfo(String key,RuleType type);

}