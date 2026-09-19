/* Copyright © 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.syntheticdata.controller;import cn.zhuatech.syntheticdata.common.ApiResponse;import cn.zhuatech.syntheticdata.service.SyntheticDatasetReleaseService;import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController@RequestMapping("/api/synthetic-data")public class SyntheticDatasetReleaseController{private final SyntheticDatasetReleaseService service;/**
                                                                                                                                                          * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                          */
public SyntheticDatasetReleaseController(SyntheticDatasetReleaseService s){service=s;}/**
                                                                                                                                                                                                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                */
@PostMapping("/release-evaluation")@PreAuthorize("hasAnyRole('ADMIN','DATA_ENGINEER')")public ApiResponse<SyntheticDatasetReleaseService.Result>evaluate(@Valid@RequestBody SyntheticDatasetReleaseService.Request r){return ApiResponse.ok("合成数据发布评估完成",service.evaluate(r));}}
