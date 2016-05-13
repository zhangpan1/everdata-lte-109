package com.eversec.lte.processor.compound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Hex;

import scala.Tuple2;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.vo.compound.CompInfo;
import com.eversec.lte.vo.compound.CompMessage.MrInfo;

import static com.eversec.lte.constant.SdtpConstants.CompSignalingProcedureType.*;

/**
 * 排序一遍 （时间，类型s1mme最前，结束时间）
 * 
 * @author lirongzhi
 * 
 */
public class TestComp2 extends Compounder {
	@Override
	public Iterable<CompInfo> call(Tuple2<String, Iterable<byte[]>> t)
			throws Exception {
		TreeMap<Long, MrInfo> mrInfos = new TreeMap<>();
		List<XdrSingleSource> xdrs = new ArrayList<>();
		// for (byte[] load : t._2) {
		// CompMessage compMessage = xdrCustomBytesDecoder.decode(load);
		// mrInfos.putAll(compMessage.getMrInfos());
		// xdrs.addAll(compMessage.getXdrs());
		// }
		// 1.xdr按照开始时间排序
		Collections.sort(xdrs, XdrSingleSource.XDR_SINGLE_SOURCE_COMPARATOR);

		// 2.信令流程开始结束index集合
		// List<IndexPair> indexPairs = getIndexPairs(xdrs);
		// 3.合成信令XDR
		// CompInfo compInfo = compoundSignalingXdr(xdrs, mrInfos, indexPairs);
		// return Arrays.asList(compInfo);

		return null;
	}

	 
}
