package com.eversec.lte.utils;

import com.eversec.common.cache.IData;

public class GeoUtil {
	public static class Point implements IData {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8311383616293873279L;
		public double lat;
		public double lon;

		@Override
		public int getMemoryBytes() {
			return 0;
		}
	}

	/*
	 * 输入和输出的角度都是以Degree为单位
	 */

	public static Point calcUELonLat(Point p1, double aoa1, Point p2,
			double aoa2) {
		// 以西边的点为原点建立坐标系
		Point zero = null, another = null;
		double zeroRAD = 0, anotherRAD = 0;

		if (p1.lon < p2.lon) {
			zero = toRadians(p1);
			another = toRadians(p2);
			zeroRAD = toRadians(aoa1);
			anotherRAD = toRadians(aoa2);
		} else {
			zero = toRadians(p2);
			another = toRadians(p1);
			zeroRAD = toRadians(aoa2);
			anotherRAD = toRadians(aoa1);
		}

		// 计算两点之间的距离，km为单位
		double distRAD = distVincentyRAD(zero.lat, zero.lon, another.lat,
				another.lon); // 赤道半径 EARTH_EQUATORIAL_RADIUS_KM

		// 计算两点连线的方位角
		double bearingRAD = bearingRADOnPoint(zero.lat, zero.lon, another.lat,
				another.lon);

		// 求解定位用三角形
		double aRAD = Math.abs(bearingRAD - zeroRAD);
		if (aRAD < 0.01 * Math.PI / 180)
			throw new IllegalArgumentException(
					"zeroRAD西边基站的到达角和两基站延长线的夹角小于0.01度，这个角度有点小吧！计算停止。");

		double bRAD = 0;

		if (Math.abs(anotherRAD - bearingRAD) < 0.01 * Math.PI / 180
				|| Math.abs(anotherRAD - Math.PI - bearingRAD) < 0.01 * Math.PI / 180) {
			throw new IllegalArgumentException(
					"anotherRAD东边基站的到达角和两基站延长线的夹角小于0.01度，这个角度有点小吧！计算停止。");
		} else if (anotherRAD < bearingRAD) {
			bRAD = Math.PI - bearingRAD + anotherRAD;
		} else if (anotherRAD > bearingRAD && anotherRAD <= Math.PI) {
			bRAD = bearingRAD + Math.PI - anotherRAD;
		} else if (anotherRAD > Math.PI && anotherRAD < (Math.PI + bearingRAD)) {
			bRAD = bearingRAD - anotherRAD + Math.PI;
		} else if (anotherRAD > Math.PI + bearingRAD
				&& anotherRAD <= 2 * Math.PI) {
			bRAD = anotherRAD - bearingRAD - Math.PI;
		}

		// 计算目标点距离原点的距离和方位角
		double distUERAD = distRAD * Math.sin(Math.PI - aRAD - bRAD)
				/ Math.sin(bRAD);

		// 求出目标点的经纬度，并换算成degree输出
		Point posUERAD = pointOnBearingRAD(zero.lat, zero.lon, distUERAD,
				zeroRAD);

		return toDegrees(posUERAD);

	}

	/**
	 * Converts a distance in the units of <code>radius</code> (e.g. kilometers)
	 * to radians (multiples of the radius). A spherical earth model is assumed.
	 */
	public static double dist2Radians(double dist, double radius) {
		return dist / radius;
	}

	/**
	 * Given a start point (startLat, startLon) and a bearing on a sphere,
	 * return the destination point.
	 * 
	 * @param startLat
	 *            The starting point latitude, in radians
	 * @param startLon
	 *            The starting point longitude, in radians
	 * @param distanceRAD
	 *            The distance to travel along the bearing in radians.
	 * @param bearingRAD
	 *            The bearing, in radians. North is a 0, moving clockwise till
	 *            radians(360).
	 * 
	 * @return The destination point, IN RADIANS.
	 */
	public static Point pointOnBearingRAD(double startLat, double startLon,
			double distanceRAD, double bearingRAD) {
		/*
		 * lat2 = asin(sin(lat1)*cos(d/R) + cos(lat1)*sin(d/R)*cos(θ)) lon2 =
		 * lon1 + atan2(sin(θ)*sin(d/R)*cos(lat1), cos(d/R)−sin(lat1)*sin(lat2))
		 */
		double cosAngDist = Math.cos(distanceRAD);
		double cosStartLat = Math.cos(startLat);
		double sinAngDist = Math.sin(distanceRAD);
		double sinStartLat = Math.sin(startLat);
		double sinLat2 = sinStartLat * cosAngDist + cosStartLat * sinAngDist
				* Math.cos(bearingRAD);
		double lat2 = Math.asin(sinLat2);
		double lon2 = startLon
				+ Math.atan2(Math.sin(bearingRAD) * sinAngDist * cosStartLat,
						cosAngDist - sinStartLat * sinLat2);

		// normalize lon first
		if (lon2 > DEG_180_AS_RADS) {
			lon2 = -1.0 * (DEG_180_AS_RADS - (lon2 - DEG_180_AS_RADS));
		} else if (lon2 < -DEG_180_AS_RADS) {
			lon2 = (lon2 + DEG_180_AS_RADS) + DEG_180_AS_RADS;
		}

		// normalize lat - could flip poles
		if (lat2 > DEG_90_AS_RADS) {
			lat2 = DEG_90_AS_RADS - (lat2 - DEG_90_AS_RADS);
			if (lon2 < 0) {
				lon2 = lon2 + DEG_180_AS_RADS;
			} else {
				lon2 = lon2 - DEG_180_AS_RADS;
			}
		} else if (lat2 < -DEG_90_AS_RADS) {
			lat2 = -DEG_90_AS_RADS - (lat2 + DEG_90_AS_RADS);
			if (lon2 < 0) {
				lon2 = lon2 + DEG_180_AS_RADS;
			} else {
				lon2 = lon2 - DEG_180_AS_RADS;
			}
		}

		Point p = new Point();
		p.lat = lat2;
		p.lon = lon2;

		return p;
	}

	/**
	 * Calculates the great circle distance using the Vincenty Formula,
	 * simplified for a spherical model. This formula is accurate for any pair
	 * of points. The equation was taken from <a
	 * href="http://en.wikipedia.org/wiki/Great-circle_distance">Wikipedia</a>.
	 * <p/>
	 * The arguments are in radians, and the result is in radians.
	 */
	public static double distVincentyRAD(double lat1, double lon1, double lat2,
			double lon2) {
		// Check for same position
		if (lat1 == lat2 && lon1 == lon2)
			return 0.0;

		double cosLat1 = Math.cos(lat1);
		double cosLat2 = Math.cos(lat2);
		double sinLat1 = Math.sin(lat1);
		double sinLat2 = Math.sin(lat2);
		double dLon = lon2 - lon1;
		double cosDLon = Math.cos(dLon);
		double sinDLon = Math.sin(dLon);

		double a = cosLat2 * sinDLon;
		double b = cosLat1 * sinLat2 - sinLat1 * cosLat2 * cosDLon;
		double c = sinLat1 * sinLat2 + cosLat1 * cosLat2 * cosDLon;

		return Math.atan2(Math.sqrt(a * a + b * b), c);
	}

	/*
	 * The arguments are in radians, and the result is in radians.
	 */
	public static double bearingRADOnPoint(double lat1, double lon1,
			double lat2, double lon2) {
		double d = 0;

		d = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2)
				* Math.cos(lon2 - lon1);
		d = Math.sqrt(1 - d * d);
		d = Math.cos(lat2) * Math.sin(lon2 - lon1) / d;
		d = Math.asin(d);
		return d;
	}

	/**
	 * Same as {@link Math#toRadians(double)} but 3x faster (multiply vs.
	 * divide). See CompareRadiansSnippet.java in tests.
	 */
	public static Point toRadians(Point degrees) {

		degrees.lat = degrees.lat * DEGREES_TO_RADIANS;
		degrees.lon = degrees.lon * DEGREES_TO_RADIANS;
		return degrees;
	}

	/**
	 * Same as {@link Math#toDegrees(double)} but 3x faster (multiply vs.
	 * divide). See CompareRadiansSnippet.java in tests.
	 */
	public static Point toDegrees(Point radians) {
		radians.lat = radians.lat * RADIANS_TO_DEGREES;
		radians.lon = radians.lon * RADIANS_TO_DEGREES;
		return radians;
	}

	public static double toRadians(double degrees) {
		return degrees * DEGREES_TO_RADIANS;
	}

	// pre-compute some angles that are commonly used
	public static final double DEG_45_AS_RADS = Math.PI / 4;
	public static final double SIN_45_AS_RADS = Math.sin(DEG_45_AS_RADS);
	public static final double DEG_90_AS_RADS = Math.PI / 2;
	public static final double DEG_180_AS_RADS = Math.PI;
	public static final double DEG_225_AS_RADS = 5 * DEG_45_AS_RADS;
	public static final double DEG_270_AS_RADS = 3 * DEG_90_AS_RADS;

	public static final double DEGREES_TO_RADIANS = Math.PI / 180;
	public static final double RADIANS_TO_DEGREES = 1 / DEGREES_TO_RADIANS;

	public static final double KM_TO_MILES = 0.621371192;
	public static final double MILES_TO_KM = 1 / KM_TO_MILES;// 1.609

	public static final double TA = 78.12;

	/**
	 * The International Union of Geodesy and Geophysics says the Earth's mean
	 * radius in KM is:
	 * 
	 * [1] http://en.wikipedia.org/wiki/Earth_radius
	 */
	public static final double EARTH_MEAN_RADIUS_KM = 6371.0087714;
	public static final double EARTH_EQUATORIAL_RADIUS_KM = 6378.1370;

	/** Equivalent to degrees2Dist(1, EARTH_MEAN_RADIUS_KM) */
	public static final double DEG_TO_KM = DEGREES_TO_RADIANS
			* EARTH_MEAN_RADIUS_KM;
	public static final double KM_TO_DEG = 1 / DEG_TO_KM;

	public static final double EARTH_MEAN_RADIUS_MI = EARTH_MEAN_RADIUS_KM
			* KM_TO_MILES;
	public static final double EARTH_EQUATORIAL_RADIUS_MI = EARTH_EQUATORIAL_RADIUS_KM
			* KM_TO_MILES;
}
