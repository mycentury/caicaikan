/**
 * 
 */
package win.caicaikan.service.external.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityReq
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AliSecurityRes {
	String RequestId;
	String HostId;// 必传。目标安全组编码
	String Code;// 必传。目标安全组所属Region ID
	String Message;// 必传。IP协议，取值：tcp | udp | icmp | gre | all；all表示同时支持四种协议
}
