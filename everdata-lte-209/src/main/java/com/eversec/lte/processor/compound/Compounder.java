package com.eversec.lte.processor.compound;

import org.apache.spark.api.java.function.FlatMapFunction;

import scala.Tuple2;

import com.eversec.lte.vo.compound.CompInfo;

/**
 * Compounder abstract
 * 
 */
public abstract class Compounder implements
		FlatMapFunction<Tuple2<String, Iterable<byte[]>>, CompInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1756668347507329403L;
}
