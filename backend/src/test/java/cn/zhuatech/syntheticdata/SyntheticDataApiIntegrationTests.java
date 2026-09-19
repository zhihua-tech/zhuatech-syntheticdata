/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata;
import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;import org.springframework.http.MediaType;import org.springframework.test.web.servlet.MockMvc;import java.nio.charset.StandardCharsets;import java.util.Base64;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest @AutoConfigureMockMvc class SyntheticDataApiIntegrationTests{
 @Autowired MockMvc mvc;/**
                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                         */
private String auth(){return "Basic "+Base64.getEncoder().encodeToString("admin:test-admin".getBytes(StandardCharsets.UTF_8));}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void dataEngineerCanGenerateApprovedDataset()throws Exception{String body="""
 {"requestId":"API-SYN-1","rowCount":5,"seed":42,"privacyReviewed":true,"requesterId":"engineer","approverId":"privacy-owner","fields":[{"name":"segment","type":"CATEGORY","nullRate":0,"unique":false,"sensitive":false,"min":1,"max":10,"categories":["制造","零售"],"startDate":"2025-01-01","endDate":"2026-12-31"},{"name":"amount","type":"MONEY","nullRate":0,"unique":false,"sensitive":false,"min":100,"max":1000,"categories":[],"startDate":"2025-01-01","endDate":"2026-12-31"}]}
 """;mvc.perform(post("/api/synthetic-data/generate").header("Authorization",auth()).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").value("EXPORTABLE")).andExpect(jsonPath("$.data.exportRows.length()").value(5)).andExpect(jsonPath("$.data.auditId").isNumber());}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void anonymousAuditAccessIsDenied()throws Exception{mvc.perform(get("/api/synthetic-data/audits")).andExpect(status().isUnauthorized());}
}
