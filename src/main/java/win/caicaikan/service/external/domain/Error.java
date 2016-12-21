/**
 * 
 */
package win.caicaikan.service.external.domain;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月22日
 * @ClassName Error
 */
public enum Error {
	// MissingParameter The input parameter “Action” that is mandatory for processing this request is not supplied 400
	// 缺少 Action 字段
	// MissingParameter The input parameter “AccessKeyId” that is mandatory for processing this request is not supplied
	// 400 缺少 AccessKeyId 字段
	// MissingParameter An input parameter “Signature” that is mandatory for processing the request is not supplied. 400
	// 缺少 Signature 字段
	// MissingParameter The input parameter “TimeStamp” that is mandatory for processing this request is not supplied
	// 400 缺少 Timestamp 字段
	// MissingParameter The input parameter “Version” that is mandatory for processing this request is not supplied 400
	// 缺少 Version 字段
	// InvalidParameter The specified parameter “Action or Version” is not valid. 400 无效的 Action 值（该 API 不存在）
	// InvalidAccessKeyId.NotFound The Access Key ID provided does not exist in our records. 400 无效的 AccessKeyId 值（该 key
	// 不存在）
	// Forbidden.AccessKeyDisabled The Access Key is disabled. 403 该 AccessKey 处于禁用状态
	// IncompleteSignature The request signature does not conform to Aliyun standards. 400 无效的 Signature 取值（签名结果错误）
	// InvalidParamater The specified parameter “SignatureMethod” is not valid. 400 无效的 SignatureMethod 取值
	// InvalidParamater The specified parameter “SignatureVersion” is not valid. 400 无效的 SignatureVersion 取值
	// IllegalTimestamp The input parameter “Timestamp” that is mandatory for processing this request is not supplied.
	// 400 无效的 Timestamp 取值（Timestamp 与服务器时间相差超过了 1 个小时）
	// SignatureNonceUsed The request signature nonce has been used. 400 无效的 SignatureNonce（该 SignatureNonce 值已被使用过）
	// InvalidParameter The specified parameter “Action or Version” is not valid. 400 无效的 Version 取值
	// InvalidOwnerId The specified OwnerId is not valid. 400 无效的 OwnerId 取值
	// InvalidOwnerAccount The specified OwnerAccount is not valid. 400 无效的 OwnerAccount 取值
	// InvalidOwner OwnerId and OwnerAccount can’t be used at one API access. 400 同时使用了 OwnerId 和 OwnerAccount
	// Throttling Request was denied due to request throttling. 400 因系统流控拒绝访问
	// Throttling Request was denied due to request throttling. 400 该 key 的调用 quota 已用完
	// InvalidAction Specified action is not valid. 403 该 key 无权调用该 API
	// UnsupportedHTTPMethod This http method is not supported. 403 用户使用了不支持的 Http Method（当前 TOP 只支持 post 和 get）
	// ServiceUnavailable The request has failed due to a temporary failure of the server. 500 服务不可用
	// UnsupportedParameter The parameter ”” is not supported. 400 使用了无效的参数
	// InternalError The request processing has failed due to some unknown error, exception or failure. 500 其他情况
	// MissingParameter The input parameter OwnerId,OwnerAccount that is mandatory for processing this request is not
	// supplied. 403 调用该接口没有指定 OwnerId
	// Forbidden.SubUser The specified action is not available for you。 403 无权调用订单类接口
	// UnsupportedParameter The parameter ”” is not supported. 400 该参数无权使用
	// Forbidden.InstanceNotFound The specified Instance is not found, so we cann’t get enough information to check
	// permission in RAM. 404 使用了 RAM 授权子账号进行资源访问，但是本次访问涉及到的 Instance 不存在
	// Forbidden.DiskNotFound The specified Disk is not found, so we cann’t get enough information to check permission
	// in RAM. 404 使用了 RAM 授权子账号进行资源访问,但是本次访问涉及到的 Disk 不存在
	// Forbidden.SecurityGroupNotFound The specified SecurityGroup is not found, so we cann’t get enough information to
	// check permission in RAM. 404 使用了 RAM 授权子账号进行资源访问,但是本次访问涉及到的 SecurityGroup 不存在
	// Forbidden.SnapshotNotFound The specified Snapshot is not found, so we cann’t get enough information to check
	// permission in RAM. 404 使用了 RAM 授权子账号进行资源访问,但是本次访问涉及到的 Snapshot 不存在
	// Forbidden.ImageNotFound The specified Image is not found, so we cann’t get enough information to check permission
	// in RAM. 404 使用了 RAM 授权子账号进行资源访问,但是本次访问涉及到的 Image 不存在
	// Forbidden.RAM User not authorized to operate the specified resource, or this API doesn’t support RAM. 403 使用了 RAM
	// 授权子账号进行资源访问,但是本次操作没有被正确的授权
	// Forbidden.NotSupportRAM This action does not support accessed by RAM mode. 403 该接口不允许使用 RAM 方式进行访问
	// InsufficientBalance Your account does not have enough balance. 400 余额不足
	// IdempotentParameterMismatch Request uses a client token in a previous request but is not identical to that
	// request. 400 使用了一个已经使用过的 ClientToken，但此次请求内容却又与上一次使用该 Token 的 request 不一样.
	// RealNameAuthenticationError Your account has not passed the real-name authentication yet. 403 用户未进行实名认证
	// InvalidIdempotenceParameter.Mismatch The specified parameters are different from before 403 幂等参数不匹配
	// LastTokenProcessing The last token request is processing 403 上一次请求还在处理中
	// InvalidParameter The specified parameter is not valid 400 参数校验失败
	// Forbidden.RiskControl This operation is forbidden by Aliyun Risk Control system. 403 阿里云风控系统拒绝了此次访问
}
