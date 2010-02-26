package com.asu.validator;

/**
 * 版本信息
 * 
 * update log :
 * <br><br>
 * v1.2 : 
 * 
 * <br>
 *   1. 添加两种运行模式 Debug/Product, product模式中性能大幅度提升.
 *   (详见{@code debugMode()}与{@code productMode()})
 * <br/>
 *   2. 在产品模式中, 可以提前缓存注解信息, 详见{@code cachedRules(Class<?>)}
 * 
 * <br><br>
 * 
 * v1.1 : 添加AbstractCompareReferee以及几个比较实现<br>
 *  {@link EqualsReferee}
 *  {@link NonEqualsReferee}
 *  {@link GreaterThanReferee}
 *  {@link LessThanReferee}
 *  {@link LaterThanReferee}
 *  {@link EarlierThanReferee}
 * 
 * @version 1.2
 * <br>
 * 
 *  
 * @author Bevis.Zhao(avengerbevis@gmail.com)
 * 
 *
 */
public class Version {
	public final static String INFO = "version 1.2";
}
