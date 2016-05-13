package com.eversec.lte.river.utils;

@SuppressWarnings("rawtypes") 
public class EsDeleteCurlUtil extends EsCurlUtil {
	// #XdrSingleSourceCellMR
	// curl -XPUT
	// 'http://192.168.30.39:9200/sdtp-lte/xdr_single_cellmr/_mapping' -d '
	@SuppressWarnings("unused")
	public void main(String[] args) throws Exception {
		String host = "192.168.30.39";
		int port = 9200;
		String index = "sdtp-lte";
		// new EsMappingCurlUtil().printMapping(host, port, index,
		// CdrModel.class);
	}

	public String createDelete(String host, int port, String index, String type)
			throws Exception {
		// curl -XDELETE 
		// 'http://192.168.30.39:9200/sdtp-lte/xdr_single_cellmr/_mapping' -d '

		return "curl -XDELETE 'http://" + host + ":" + port + "/" + index + "/"
				+ type + "/'";
	}

	public void printDelete(String host, int port, String index, Class clazz)
			throws Exception {
		System.out.println(createDelete(host, port, index,
				createTypeName(clazz)));

	}

}
