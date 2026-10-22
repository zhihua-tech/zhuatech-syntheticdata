/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata.workbench;
import org.springframework.stereotype.Service;
import java.math.*;
import java.util.*;
import static cn.zhuatech.syntheticdata.workbench.WorkbenchInput.*;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
    /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public Map<String,Object> execute(Map<String,Object> r){
        validate(r);

        keys(r,"required","unique","records");var required=strings(r,"required",1,50);var unique=strings(r,"unique",0,50);if(new HashSet<>(required).size()!=required.size()||new HashSet<>(unique).size()!=unique.size())throw new IllegalArgumentException("列声明重复");
        var records=rows(r,"records",1,200);List<Map<String,Object>> issues=new ArrayList<>();Map<String,Map<String,Integer>> seen=new TreeMap<>();for(String key:unique)seen.put(key,new HashMap<>());Map<String,Integer> missing=new TreeMap<>();required.forEach(x->missing.put(x,0));
        for(int i=0;i<records.size();i++){var row=records.get(i);for(Object v:row.values())if(v instanceof Map||v instanceof List)throw new IllegalArgumentException("仅支持标量列");for(String key:required){Object value=row.get(key);if(value==null||value instanceof String s&&s.isBlank()){missing.merge(key,1,Integer::sum);issues.add(Map.of("row",i+1,"field",key,"code","REQUIRED"));}}
            for(String key:unique){Object value=row.get(key);if(value==null||value instanceof String s&&s.isBlank())continue;String normal=value instanceof Number n?"n:"+new BigDecimal(n.toString()).stripTrailingZeros().toPlainString():value.getClass().getSimpleName()+":"+value;Integer first=seen.get(key).putIfAbsent(normal,i+1);if(first!=null)issues.add(Map.of("row",i+1,"field",key,"code","DUPLICATE","firstRow",first));}
        }
        List<Map<String,Object>> columns=new ArrayList<>();for(var e:missing.entrySet())columns.add(Map.of("field",e.getKey(),"missing",e.getValue(),"completeness",round(1.0-(double)e.getValue()/records.size())));
        return Map.of("valid",issues.isEmpty(),"recordCount",records.size(),"columns",columns,"issues",issues);

    }
}
