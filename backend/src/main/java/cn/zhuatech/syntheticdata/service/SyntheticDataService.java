/* Copyright © 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata.service;
import cn.zhuatech.syntheticdata.model.GenerationAudit;import cn.zhuatech.syntheticdata.repository.GenerationAuditRepository;import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.math.*;import java.time.*;import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service public class SyntheticDataService{
 private static final String[]SURNAMES={"林","周","沈","陈","顾","许","韩","宋","叶","陆"},GIVEN={"知远","清和","以安","景行","若宁","嘉树","云舟","书言","闻溪","星遥"};
 private final GenerationAuditRepository repository;/**
                                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                     */
public SyntheticDataService(GenerationAuditRepository r){repository=r;}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Transactional public GenerationResult generate(GenerationRequest r,String actor){var old=repository.findByRequestId(r.requestId());if(old.isPresent()){var e=old.get();return new GenerationResult(e.getId(),Decision.valueOf(e.getDecision()),RiskLevel.valueOf(e.getRiskLevel()),List.of(),List.of(),List.of("重复请求已返回原审计结果"),true);}
  List<String>errors=validate(r);if(!errors.isEmpty()){var saved=repository.save(new GenerationAudit(r.requestId(),Decision.BLOCKED.name(),0,RiskLevel.HIGH.name(),"schema errors="+errors.size(),actor));return new GenerationResult(saved.getId(),Decision.BLOCKED,RiskLevel.HIGH,List.of(),List.of(),List.copyOf(errors),false);}
  SplittableRandom random=new SplittableRandom(r.seed());List<Map<String,Object>>rows=new ArrayList<>();for(int i=0;i<r.rowCount();i++){Map<String,Object>row=new LinkedHashMap<>();for(FieldSpec f:r.fields())row.put(f.name(),random.nextDouble()<f.nullRate()?null:value(f,random,i));rows.add(row);}
  long sensitive=r.fields().stream().filter(FieldSpec::sensitive).count();boolean uniqueSensitive=r.fields().stream().anyMatch(f->f.sensitive()&&f.unique());RiskLevel risk=uniqueSensitive?RiskLevel.HIGH:sensitive*2>=r.fields().size()?RiskLevel.MEDIUM:RiskLevel.LOW;List<String>warnings=new ArrayList<>();if(uniqueSensitive)warnings.add("敏感字段设置为唯一值，存在可链接性风险");if(r.rowCount()<100)warnings.add("小数据集可能增加属性组合重识别风险");
  boolean approved=r.privacyReviewed()&&!r.requesterId().equals(r.approverId());Decision decision=approved&&risk!=RiskLevel.HIGH?Decision.EXPORTABLE:Decision.PREVIEW_ONLY;if(!r.privacyReviewed())warnings.add("尚未完成隐私复核，仅返回掩码预览");if(r.requesterId().equals(r.approverId()))warnings.add("生成申请人与导出审批人必须职责分离");
  List<Map<String,Object>>preview=rows.stream().limit(20).map(x->mask(x,r.fields())).toList();List<Map<String,Object>>export=decision==Decision.EXPORTABLE?rows:List.of();var saved=repository.save(new GenerationAudit(r.requestId(),decision.name(),r.rowCount(),risk.name(),"fields="+r.fields().size()+", sensitive="+sensitive,actor));return new GenerationResult(saved.getId(),decision,risk,preview,export,List.copyOf(warnings),false);
 }
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private List<String>validate(GenerationRequest r){
  List<String>e=new ArrayList<>();Set<String>names=new HashSet<>();
  for(FieldSpec f:r.fields()){
   if(!names.add(f.name()))e.add("字段名重复："+f.name());
   if((f.type()==FieldType.INTEGER||f.type()==FieldType.MONEY)&&f.min()>f.max())e.add("数值范围无效："+f.name());
   if(f.type()==FieldType.CATEGORY&&f.categories().isEmpty())e.add("分类字段缺少候选值："+f.name());
   if(f.unique()&&f.type()==FieldType.CATEGORY&&f.categories().size()<r.rowCount())e.add("分类候选数量不足以满足唯一约束："+f.name());
   if(f.unique()&&f.type()==FieldType.NAME&&r.rowCount()>SURNAMES.length*GIVEN.length)e.add("姓名组合数量不足以满足唯一约束："+f.name());
   if(f.unique()&&(f.type()==FieldType.INTEGER||f.type()==FieldType.MONEY)&&f.max()-f.min()+1<r.rowCount())e.add("数值范围不足以满足唯一约束："+f.name());
   if(f.unique()&&f.type()==FieldType.DATE&&LocalDate.parse(f.endDate()).toEpochDay()-LocalDate.parse(f.startDate()).toEpochDay()+1<r.rowCount())e.add("日期范围不足以满足唯一约束："+f.name());
  }
  return e;
 }
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private Object value(FieldSpec f,SplittableRandom r,int i){return switch(f.type()){case NAME->f.unique()?SURNAMES[i/GIVEN.length]+GIVEN[i%GIVEN.length]:SURNAMES[r.nextInt(SURNAMES.length)]+GIVEN[r.nextInt(GIVEN.length)];case EMAIL->"user"+(f.unique()?i:Math.abs(r.nextLong())%1000000)+"@example.invalid";case PHONE->f.unique()?"139"+String.format("%08d",i):"1"+(30+r.nextInt(70))+String.format("%08d",Math.abs(r.nextInt())%100000000);case INTEGER->f.unique()?f.min()+i:r.nextLong(f.min(),f.max()+1);case MONEY->f.unique()?BigDecimal.valueOf(f.min()+i).setScale(2,RoundingMode.HALF_UP):BigDecimal.valueOf(r.nextDouble(f.min(),f.max())).setScale(2,RoundingMode.HALF_UP);case DATE->f.unique()?LocalDate.parse(f.startDate()).plusDays(i).toString():LocalDate.ofEpochDay(r.nextLong(LocalDate.parse(f.startDate()).toEpochDay(),LocalDate.parse(f.endDate()).toEpochDay()+1)).toString();case CATEGORY->f.categories().get(f.unique()?i:r.nextInt(f.categories().size()));case UUID->new UUID(r.nextLong(),r.nextLong()).toString();};}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 private Map<String,Object>mask(Map<String,Object>row,List<FieldSpec>fields){Map<String,Object>m=new LinkedHashMap<>(row);for(FieldSpec f:fields)if(f.sensitive()&&m.get(f.name())!=null){String v=m.get(f.name()).toString();m.put(f.name(),v.length()<4?"***":v.substring(0,2)+"***"+v.substring(v.length()-2));}return m;}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Transactional(readOnly=true)public List<GenerationAudit>audits(){return repository.findTop100ByOrderByCreatedAtDesc();}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public enum FieldType{NAME,EMAIL,PHONE,INTEGER,MONEY,DATE,CATEGORY,UUID}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record FieldSpec(@NotBlank String name,@NotNull FieldType type,@DecimalMin("0")@DecimalMax("1")double nullRate,boolean unique,boolean sensitive,long min,long max,@NotNull List<String>categories,@NotBlank String startDate,@NotBlank String endDate){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record GenerationRequest(@NotBlank String requestId,@Min(1)@Max(1000)int rowCount,long seed,@NotEmpty List<@Valid FieldSpec>fields,boolean privacyReviewed,@NotBlank String requesterId,@NotBlank String approverId){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record GenerationResult(Long auditId,Decision decision,RiskLevel riskLevel,List<Map<String,Object>>preview,List<Map<String,Object>>exportRows,List<String>warnings,boolean duplicate){}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public enum Decision{EXPORTABLE,PREVIEW_ONLY,BLOCKED}/**
                                                       * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                       */
public enum RiskLevel{LOW,MEDIUM,HIGH}
}
