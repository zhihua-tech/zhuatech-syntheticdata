/* Copyright © 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata.service;
import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import java.math.BigDecimal;import java.util.*;
/**
 * 用隐私攻击指标、最近邻距离和业务效用决定合成数据能否导出。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service public class SyntheticDatasetReleaseService{
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public Result evaluate(Request r){List<String>blockers=new ArrayList<>(),reviews=new ArrayList<>(),actions=new ArrayList<>();
  if(r.directIdentifiersPresent())blockers.add("数据集中仍存在直接身份标识");if(r.kAnonymity()<r.minKAnonymity())blockers.add("k-匿名水平低于发布阈值");if(r.membershipInferenceAdvantage().compareTo(r.maxMembershipInferenceAdvantage())>0)blockers.add("成员推断攻击优势超过阈值");if(r.nearestNeighborDistance().compareTo(r.minNearestNeighborDistance())<0)blockers.add("与训练样本最近邻距离过小");
  if(r.utilityScore().compareTo(r.minUtilityScore())<0)reviews.add("业务效用低于目标阈值");if(r.schemaCoverage().compareTo(r.minSchemaCoverage())<0)reviews.add("字段与约束覆盖不足");if(!r.privacyReviewApproved())reviews.add("隐私负责人尚未批准发布");if(r.requesterId().equals(r.approverId()))reviews.add("申请人与审批人未实现职责分离");
  Decision d=!blockers.isEmpty()?Decision.BLOCKED:!reviews.isEmpty()?Decision.REVIEW:Decision.RELEASE;
  actions.add(d==Decision.BLOCKED?"禁止导出并重新生成、泛化或删除高风险字段":d==Decision.REVIEW?"补充效用验证、隐私审批或独立审批人":"发布带数据卡、种子摘要、指标和使用边界的数据集");
  return new Result(d,List.copyOf(blockers),List.copyOf(reviews),List.copyOf(actions));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Request(@NotBlank String datasetId,@Min(1)int rowCount,@Min(1)int kAnonymity,@Min(1)int minKAnonymity,@DecimalMin("0")BigDecimal nearestNeighborDistance,@DecimalMin("0")BigDecimal minNearestNeighborDistance,@DecimalMin("0")@DecimalMax("1")BigDecimal membershipInferenceAdvantage,@DecimalMin("0")@DecimalMax("1")BigDecimal maxMembershipInferenceAdvantage,@DecimalMin("0")@DecimalMax("1")BigDecimal utilityScore,@DecimalMin("0")@DecimalMax("1")BigDecimal minUtilityScore,@DecimalMin("0")@DecimalMax("1")BigDecimal schemaCoverage,@DecimalMin("0")@DecimalMax("1")BigDecimal minSchemaCoverage,boolean directIdentifiersPresent,boolean privacyReviewApproved,@NotBlank String requesterId,@NotBlank String approverId){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Result(Decision decision,List<String>blockers,List<String>reviewReasons,List<String>actions){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public enum Decision{RELEASE,REVIEW,BLOCKED}
}
