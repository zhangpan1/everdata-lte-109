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
public class TestComp extends Compounder {
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

		List<Comp> mainList = findMainS1MME(xdrs);
		comp(mainList, xdrs);
		return null;
	}

	private void comp(List<Comp> mainList, List<XdrSingleSource> xdrs) {

		for (Comp comp : mainList) {
			short compType = comp.compType;
			switch (compType) {
			case ATTACH:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case SERVICE_REQUEST:
				comp.xdrs = findSeviceRequest(comp.main, xdrs);
				break;
			case PAGING:
				comp.xdrs = findPaging(comp.main, xdrs);
				break;
			case TAU:
				comp.xdrs = findTau(comp.main, xdrs);
				break;
			case DETACH:
				comp.xdrs = findDetach(comp.main, xdrs);
				break;
			case PDN_CONNECTIVITY:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case PDN_DISCONNECTION:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case EPS_BEARER_RESOURCE_ALLOCATION:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case EPS_BEARER_RESOURCE_MODIFY:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case EPS_BEARER_CONTEXT_DEACTIVATION:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case EPS_BEARER_CONTEXT_MODIFICATION:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case DEDICATED_EPS_BEARER_CONTEXT_ACTIVATION:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case X2_HANDOVER:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case S1_HANDOVER:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case UE_CONTEXT_RELEASE:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case EPS_BEARER_RELEASE:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case CSFB:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			case SMS:
				comp.xdrs = findATTACH(comp.main, xdrs);
				break;
			default:
				break;
			}
		}
	}

	// 5．Detach
	private List<XdrSingleSource> findDetach(XdrSingleSourceS1MME main,
			List<XdrSingleSource> xdrs) {
		List<XdrSingleSource> result = new ArrayList<>();
		long startTime = main.getStartTime().getTime();
		long endTime = main.getEndTime().getTime();
		boolean findUuFirst = false;
		long uustartTime = -1;
		
		result.add(main);
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS6a) {
				if (timeInRange(xdr, endTime, endTime+1000)) {
//					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceSGs) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceUu) {
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, startTime - 1000, startTime + 1000)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findUuFirst = true;
					uustartTime = xdr.getProduceStartTime();
					continue;
				}
				if (!findUuFirst) {
					uustartTime = startTime;
				}
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, uustartTime, endTime)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findUuFirst = true;
					continue;
				}
			} else if (xdr instanceof XdrSingleSourceS10S11) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceX2) {
			}
		}
		return result;
	}

	// 4．TAU
	private List<XdrSingleSource> findTau(XdrSingleSourceS1MME main,
			List<XdrSingleSource> xdrs) {
		List<XdrSingleSource> result = new ArrayList<>();
		long startTime = main.getStartTime().getTime();
		long endTime = main.getEndTime().getTime();
		boolean findUuFirst = false;
		long uustartTime = -1;
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				if (LowXdrEquals(xdr, main)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS6a) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceSGs) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceUu) {
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, startTime - 1000, startTime + 1000)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findUuFirst = true;
					uustartTime = xdr.getProduceStartTime();
					continue;
				}
				if (!findUuFirst) {
					uustartTime = startTime;
				}
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, uustartTime, endTime)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findUuFirst = true;
					continue;
				}
			} else if (xdr instanceof XdrSingleSourceS10S11) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceX2) {
			}
		}
		return result;
	}

	// 3．Paging（PS寻呼）
	private List<XdrSingleSource> findPaging(XdrSingleSourceS1MME main,
			List<XdrSingleSource> xdrs) {
		List<XdrSingleSource> result = new ArrayList<>();
		long startTime = main.getStartTime().getTime();
		long endTime = main.getEndTime().getTime();
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				// 1. 判断此合成是否输出（条件：若在主流程Procedure End Time之后1秒内存在Procedure
				// Type取值为3的话单，
				// 且此话单的Procedure Start Time与主流程的Procedure End
				// Time相等时，则此合成不输出，其余情况均输出此合成）
				if (((XdrSingleSourceS1MME) xdr).getProcedureType() == 3
						&& timeInRange(xdr, endTime, endTime + 1000)
						&& xdr.getProduceStartTime() != xdr.getProduceEndTime()) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS6a) {
			} else if (xdr instanceof XdrSingleSourceSGs) {
			} else if (xdr instanceof XdrSingleSourceUu) {
				if (timeInRange(xdr, startTime, endTime)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS10S11) {
			} else if (xdr instanceof XdrSingleSourceX2) {
			}
		}
		return result;
	}

	// 2．Sevice Request
	private List<XdrSingleSource> findSeviceRequest(XdrSingleSourceS1MME main,
			List<XdrSingleSource> xdrs) {
		List<XdrSingleSource> result = new ArrayList<>();
		long startTime = main.getStartTime().getTime();
		long endTime = main.getEndTime().getTime();
		boolean findUuFirst = false;
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				if (LowXdrEquals(xdr, main)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS6a) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceSGs) {
			} else if (xdr instanceof XdrSingleSourceUu) {
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, startTime - 1000, startTime + 1000)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findUuFirst = true;
				}

				if (findUuFirst) {

				} else {

				}
				// 2. 查找其他话单的Procedure Start Time大于等于UU口Procedure
				// Type取值为1的话单Procedure Start Time，小于等于主流程的Procedure End
				// Time（条件：与主流程的MME UE S1AP ID、MME Group ID、MME Code组合相同）"

			} else if (xdr instanceof XdrSingleSourceS10S11) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceX2) {
			}
		}
		return result;
	}

	// 1. ATTACH
	private List<XdrSingleSource> findATTACH(XdrSingleSourceS1MME main,
			List<XdrSingleSource> xdrs) {
		List<XdrSingleSource> result = new ArrayList<>();
		long startTime = main.getStartTime().getTime();
		long endTime = main.getEndTime().getTime();
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				if (LowXdrEquals(xdr, main)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceS6a) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceSGs) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceUu) {
				boolean findFirst = false;
				if (((XdrSingleSourceUu) xdr).getProcedureType() == 1
						&& timeInRange(xdr, startTime - 1000, startTime + 1000)
						&& mmeEquals(xdr, main)) {
					result.add(xdr);
					findFirst = true;
				}
				if (findFirst) {

				}
				// 2. 查找其他话单的Procedure Start Time大于等于UU口Procedure
				// Type取值为1的话单Procedure Start Time，小于等于主流程的Procedure End
				// Time（条件：与主流程的MME UE S1AP ID、MME Group ID、MME Code组合相同）"

			} else if (xdr instanceof XdrSingleSourceS10S11) {
				if (timeInRange(xdr, startTime, endTime)) {
					result.add(xdr);
				}
			} else if (xdr instanceof XdrSingleSourceX2) {
			}
		}
		return result;
	}

	// ：与主流程的MME UE S1AP ID、MME Group ID、MME Code组合相同
	private boolean mmeEquals(XdrSingleSource xdr, XdrSingleSourceS1MME main) {
		return false;
	}

	private boolean timeInRange(XdrSingleSource xdr, long startTime,
			long endTime) {
		return xdr.getProduceStartTime() >= startTime
				&& xdr.getProduceStartTime() <= endTime;
	}

	/*
	 * 
	 */
	private boolean LowXdrEquals(XdrSingleSource xdr, XdrSingleSource main) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<Comp> findMainS1MME(List<XdrSingleSource> xdrs) {
		List<Comp> mains = new ArrayList<>();
		for (XdrSingleSource xdr : xdrs) {
			if (xdr instanceof XdrSingleSourceS1MME) {
				XdrSingleSourceS1MME main = (XdrSingleSourceS1MME) xdr;

				short procedureType = main.getProcedureType();
				short keyword = main.getKeyword1();

				short compType = UNKNOWN;
				if ((procedureType == 1)) {
					compType = ATTACH;
				} else if (procedureType == 2) {
					compType = SERVICE_REQUEST;
				} else if (procedureType == 4) {
					compType = PAGING;
				} else if (procedureType == 5) {
					compType = TAU;
				} else if (procedureType == 6) {
					compType = DETACH;
				} else if (procedureType == 7) {
					compType = PDN_CONNECTIVITY;
				} else if (procedureType == 8) {
					compType = 7;
				} else if (procedureType == 9) {
					compType = 8;
				} else if (procedureType == 10) {
					compType = 9;
				} else if (procedureType == 11) {
					compType = 10;
				} else if (procedureType == 12) {
					compType = 11;
				} else if (procedureType == 13) {
					compType = 12;
				} else if (procedureType == 16) {
					compType = 14;
				} else if (procedureType == 20) {
					compType = 15;
				} else if (procedureType == 21) {
					compType = 16;
				} else if (procedureType == 3
						&& (keyword == 0 || keyword == 2 || keyword == 3 || keyword == 4)) {
					compType = 171;
				} else if (procedureType == 32 && keyword == 1) {
					compType = 181;
				} else if (procedureType == 14) {
					compType = 13;
				} else if (procedureType == 3 && keyword == 1) {
					compType = 172;
				} else if (procedureType == 32 && keyword == 0) {
					compType = 182;
				}
				if (compType > 0) {
					mains.add(new Comp(compType, main));
				}
			}
		}
		return mains;
	}

	public static class Comp {
		short compType = -1;
		XdrSingleSourceS1MME main;
		List<XdrSingleSource> xdrs = new ArrayList<>();

		public Comp(short compType, XdrSingleSourceS1MME main) {
			super();
			this.compType = compType;
			this.main = main;
		}
	}
}
