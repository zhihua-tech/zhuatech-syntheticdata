# ZhuaTech Synthetic Data｜企业 AI 合成数据平台

由 **[知华科技（上海如静知华信息科技有限公司）](https://www.zhuatech.cn/)** 发布，面向 AI 训练、软件测试和数据演示生成不依赖真实个人信息的数据集。

## 主要功能不是预留

- 支持 `NAME / EMAIL / PHONE / INTEGER / MONEY / DATE / CATEGORY / UUID` 八类字段。
- 种子可复现；支持空值率、数值范围、日期范围、分类候选和唯一约束。
- 姓名、电话和邮箱由算法组合生成，不内置真实个人记录。
- 自动校验字段名、行数、范围、候选值和唯一容量。
- 计算敏感字段比例、唯一敏感字段风险并给出风险等级。
- 预览自动掩码敏感字段；只有通过隐私门禁和独立审批才返回完整导出数据。
- 请求 ID 幂等、生成摘要进入 MySQL 审计，管理员和审计员权限隔离。
- Vue 管理台可编辑 Schema、生成数据并查看风险结论。

## 接口

- `POST /api/synthetic-data/generate`：生成、校验和评估数据集。
- `GET /api/synthetic-data/audits`：查询生成审计。

## 启动

```bash
cp .env.example .env
docker compose up --build
```

访问 `http://localhost:8095`。

> 合成数据仍需做重识别评估。本工程仅限个人非商业学习交流；企业使用、生产部署、SaaS 和其他商业行为必须取得上海如静知华信息科技有限公司书面授权，详见 [LICENSE](LICENSE)。

AI 数据治理、测试数据平台和私有化开发请联系[知华科技](https://www.zhuatech.cn/)。

## 合成数据隐私与效用发布门禁

新增 `POST /api/synthetic-data/release-evaluation`，同时评估 k-匿名、成员推断攻击优势、训练样本最近邻距离、直接标识、业务效用、Schema 覆盖、隐私审批和职责分离，输出 `RELEASE / REVIEW / BLOCKED`，并要求发布数据卡和指标快照。
