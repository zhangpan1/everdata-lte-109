package com.eversec.lte.ttl;

import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceCellMR;
import com.eversec.lte.model.single.XdrSingleSourceS10S11;
import com.eversec.lte.model.single.XdrSingleSourceS1MME;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.XdrSingleSourceS6a;
import com.eversec.lte.model.single.XdrSingleSourceSGs;
import com.eversec.lte.model.single.XdrSingleSourceUEMR;
import com.eversec.lte.model.single.XdrSingleSourceUu;
import com.eversec.lte.model.single.XdrSingleSourceX2;
import com.eversec.lte.processor.backfill.AbstractBackFill;

public class XdrPendingDelay implements Runnable {
	XdrSingleSource source;
	AbstractBackFill abstractCmBackFill2;

	public XdrPendingDelay(XdrSingleSource source, AbstractBackFill abstractCmBackFill2) {
		super();
		this.source = source;
		this.abstractCmBackFill2 = abstractCmBackFill2; 
	}

	@Override
	public void run() {
		if (source != null) {
			if (source instanceof XdrSingleSourceS10S11) {
				XdrSingleSourceS10S11 value = (XdrSingleSourceS10S11) source;
				abstractCmBackFill2.refillS11(  value);

			} else if (source instanceof XdrSingleSourceS1MME) {
				XdrSingleSourceS1MME value = (XdrSingleSourceS1MME) source;
				abstractCmBackFill2.refillS1mme(  value);

			} else if (source instanceof XdrSingleSourceS1U) {
				XdrSingleSourceS1U value = (XdrSingleSourceS1U) source;
				abstractCmBackFill2.refillS1U(  value);

			} else if (source instanceof XdrSingleSourceS6a) {
				XdrSingleSourceS6a value = (XdrSingleSourceS6a) source;
				abstractCmBackFill2.refillS6a( value);

			} else if (source instanceof XdrSingleSourceSGs) {
				XdrSingleSourceSGs value = (XdrSingleSourceSGs) source;
				abstractCmBackFill2.refillSgs( value);

			} else if (source instanceof XdrSingleSourceUEMR) {
				XdrSingleSourceUEMR value = (XdrSingleSourceUEMR) source;
				abstractCmBackFill2.refillUEMR(  value);

			} else if (source instanceof XdrSingleSourceCellMR) {
				XdrSingleSourceCellMR value = (XdrSingleSourceCellMR) source;

			} else if (source instanceof XdrSingleSourceUu) {
				XdrSingleSourceUu value = (XdrSingleSourceUu) source;
				abstractCmBackFill2.refillUu(  value);
			} else if (source instanceof XdrSingleSourceX2) {
				XdrSingleSourceX2 value = (XdrSingleSourceX2) source;
				abstractCmBackFill2.refillX2( value);
			}
		}

	}

}
