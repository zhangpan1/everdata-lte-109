package com.eversec.lte.kpi.coll;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ByteValue {

	private byte[] key;

	private int hash;

	public ByteValue(byte[] key) {
		this.key = key;
	}

	public byte[] getKey() {
		return this.key;
	}

	@Override
	public int hashCode() {
		int h = hash;
		if (h == 0 && key.length > 0) {
			for (int i = 0; i < key.length; i++) {
				h = 31 * h + key[i];
			}
			hash = h;
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ByteValue) {
			byte[] other = ((ByteValue) obj).getKey();
			int length = 0;
			if (other != null && (length = this.key.length) == other.length) {
				for (int i = 0; i < length; i++) {
					if (other[i] != this.key[i])
						return false;
				}
				return true;
			}
		}

		return false;
	}
	
	public static void main(String[] args) {
		byte[] a = new byte[5];
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			a[i] = (byte) r.nextInt();
		}
		byte[] b = new byte[5];
		for (int i = 0; i < 5; i++){
			b[i] = (byte) r.nextInt();
			//b[i] = a[i];
		}
			

		ByteValue aa = new ByteValue(a);
		ByteValue bb = new ByteValue(b);

		System.out.println(aa.hashCode());
		System.out.println(bb.hashCode());

		System.out.println(aa.equals(bb));

		Map<ByteValue, String> m = new HashMap<ByteValue, String>();
		m.put(aa, "123");
		System.out.println(m.get(bb));
	}

}
