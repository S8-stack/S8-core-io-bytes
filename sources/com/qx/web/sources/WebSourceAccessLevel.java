package com.qx.web.sources;

public enum WebSourceAccessLevel {

	/**
	 * <b>Unknown user</b>.User with typically no rights apart from viewing public static
	 * contents like marketing and login box. No access to evolved public client-side sources
	 */
	UNREGISTERED(0),

	/**
	 * <b>User which subscribed a free plan</b>. User can see demo resources.
	 */
	FREE(1),

	/**
	 * <b>User which subscribed a PAID BASIC plan</b>: User can all client-side features
	 */
	BASIC(2),

	/**
	 * <b>User which subscribed a PAID PRO plan</b>: User can all client-side features
	 */
	PRO(3),
	
	/**
	 * <b>User which subscribed a PAID TEAM plan</b>: User can all client-side features
	 */
	TEAM(4),
	
	/**
	 * <b>User which subscribed a PAID ENTERPRISE plan</b>: User can all client-side features
	 */
	ENTERPRISE(5),
	
	/**
	 * <b>User which subscribed is a key resource in developing the platform</b>: User can all client-side features
	 */
	CORE(6),
	
	/**
	 * <b>User with the highest level of access</b>: Grants super powers (like full admin).
	 */
	ROOT(7);


	/**
	 * <p>
	 * <code>level</code> refers to a level of accreditation. The bigger the better.
	 * Level is from 0 to 7. Typical level are:
	 * </p>
	 * <ul>
	 * <li><b>Level 0</b> : 
	 * <li><b>Level 1</b> : Regist</li>
	 * 
	 * </ul>
	 */
	public int level;

	private WebSourceAccessLevel(int level) {
		this.level = level;
	}
	
	public WebSourceAccessLevel takeHighest(WebSourceAccessLevel right) {
		if(level>right.level) {
			return this;
		}
		else {
			return right;
		}
	}
	
	public boolean isEqualOrHigherThan(WebSourceAccessLevel right) {
		return level>=right.level;
	}
}
