/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata.workbench;
import org.junit.jupiter.api.Test;
import java.math.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class AiWorkbenchServiceTest {
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversAdditionalBoundaryOne(){assertEquals(true,service.execute(Map.of("required",List.of("id"),"unique",List.of("id"),"records",List.of(Map.of("id",1),Map.of("id","1")))).get("valid"));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversAdditionalBoundaryTwo(){var r=service.execute(Map.of("required",List.of("id"),"unique",List.of("id"),"records",List.of(Map.of("name","a"))));assertEquals(false,r.get("valid"));}
 private final AiWorkbenchService service=new AiWorkbenchService();
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 private Map<String,Object> sample(){return Map.<String,Object>ofEntries(Map.entry("required",List.of("id","name")),Map.entry("unique",List.of("id")),Map.entry("records",List.of(Map.<String,Object>ofEntries(Map.entry("id",1),Map.entry("name","A")),Map.<String,Object>ofEntries(Map.entry("id",1),Map.entry("name"," ")),Map.<String,Object>ofEntries(Map.entry("id",2),Map.entry("name","B")))));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void computesBusinessResult(){var result=service.execute(sample());assertEquals(false,result.get("valid"));assertEquals(2,((List<?>)result.get("issues")).size());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversFeatureBoundary(){var bad=Map.<String,Object>of("required",List.of("id"),"unique",List.of("id"),"records",List.of(Map.of("id",List.of("nested"))));assertThrows(IllegalArgumentException.class,()->service.execute(bad));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void deterministicAndDoesNotMutateInput(){var input=sample();String before=input.toString();assertEquals(service.execute(input),service.execute(input));assertEquals(before,input.toString());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsNull(){assertThrows(IllegalArgumentException.class,()->service.execute(null));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsMissingFields(){assertThrows(IllegalArgumentException.class,()->service.execute(Map.of()));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsUnknownFields(){var input=new HashMap<>(sample());input.put("__unknown",true);assertThrows(IllegalArgumentException.class,()->service.execute(input));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsOversizedText(){assertThrows(IllegalArgumentException.class,()->service.execute(Map.of("payload","x".repeat(64001))));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsDeepInput(){Map<String,Object> v=Map.of("x",1);for(int i=0;i<14;i++)v=Map.of("x",v);final var input=v;assertThrows(IllegalArgumentException.class,()->service.execute(input));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpRequiresAuthentication()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/dataset-profile").contentType(MediaType.APPLICATION_JSON).content("{\"required\":[\"id\",\"name\"],\"unique\":[\"id\"],\"records\":[{\"id\":1,\"name\":\"A\"},{\"id\":1,\"name\":\" \"},{\"id\":2,\"name\":\"B\"}]}")).andExpect(status().isUnauthorized());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpAllowsAuthenticatedComputation()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/dataset-profile").principal(new TestingAuthenticationToken("test-user","unused","ROLE_OPERATOR")).contentType(MediaType.APPLICATION_JSON).content("{\"required\":[\"id\",\"name\"],\"unique\":[\"id\"],\"records\":[{\"id\":1,\"name\":\"A\"},{\"id\":1,\"name\":\" \"},{\"id\":2,\"name\":\"B\"}]}")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpRejectsInvalidInput()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/dataset-profile").principal(new TestingAuthenticationToken("test-user","unused","ROLE_OPERATOR")).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.success").value(false));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsAnonymousAuthenticationObject(){var actor=new AnonymousAuthenticationToken("test-key","anonymous",List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ANONYMOUS")));assertEquals(401,new AiWorkbenchController(service).run(sample(),actor).getStatusCode().value());}
}
