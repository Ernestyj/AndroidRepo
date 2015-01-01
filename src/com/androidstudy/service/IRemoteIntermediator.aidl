package com.androidstudy.service;

/**IRemoteIntermediator
 * 中间人接口定义（AIDL）；远程服务端、调用远程服务端都需此AIDL文件；
 *
 * AIDL：android interface definition language 安卓接口定义语言；
 * AIDL文件都是公有的，没有访问权限修饰符；
 *
 * @author Eugene
 * @data 2015-1-1
 */
interface IRemoteIntermediator {
	
	/**中间人调用远程服务中的方法
	 */
	void callMethodInRemoteService();
}
