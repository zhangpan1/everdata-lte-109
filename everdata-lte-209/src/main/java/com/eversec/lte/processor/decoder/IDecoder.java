package com.eversec.lte.processor.decoder;

import java.io.Serializable;

public interface IDecoder extends Serializable{
	<T> T decode(byte[] load);
}
