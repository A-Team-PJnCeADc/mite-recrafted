# MITE-Recrafted

MITE-Recrafted 是 **[MITE (Minecraft Is Too Easy)](https://www.mcmod.cn/class/226.html)** 的高版本移植项目，基于 Architectury，同时构建 Fabric 与 NeoForge 版本，目标 Minecraft **26.3**（Java 25）。

原版 MITE 基于 1.6.4 并覆盖类文件实现，不兼容其他模组。本项目用 Mixin 非侵入地重新实现 MITE 机制，并保持与其他模组的兼容性。

## Progress

Developing

## 未实现的原版 MITE 功能

- [ ] 玩家初始状态限制：初始 3 格血与饥饿值
- [ ] 银质工具对亡灵伤害加成
- [ ] 数值配平：工具/伤害/速度尚未最终确定
- [ ] 新的维度：地下世界 (Underworld) 与修改后的地狱进入条件
- [ ] 新生物：惧狼/地狱犬/食尸鬼/古尸/影子潜伏者等 26 种
- [ ] 耕种机制修改：洋葱种植/蓝莓丛/疫病/粪肥
- [ ] 天气影响：血月/蓝月/丰收之月
- [ ] 创造模式移除
- [ ] F3 屏幕限制
- [ ] 方块重力系统修改
- [ ] 动物生病系统

## 依赖

- **Java 25**
- **[Architectury API](https://github.com/architectury/architectury-api)** `22.0.3`
- **[Fabric API](https://github.com/FabricMC/fabric)** `0.165.2+26.3`（Fabric 构建）
- **NeoForge** `26.3.0.41-beta`（NeoForge 构建）
- **[AppleSkin](https://github.com/squeek502/AppleSkin)**（Fabric 开发环境与客户端可选，编译期仅用 API）

## 构建

```bash
./gradlew :fabric:runDatagen     # 生成数据，输出到 common/src/main/generated（不在版本库内）
./gradlew build                  # 构建两端模组，产物在 fabric/build/libs 与 neoforge/build/libs
./gradlew :fabric:runClient      # Fabric 客户端
./gradlew :fabric:runServer      # Fabric 服务端
./gradlew :neoforge:runClient    # NeoForge 客户端
./gradlew :neoforge:runServer    # NeoForge 服务端
```

## 项目结构

三模块 Architectury 布局：`common` 放内容、公开 API 与共享客户端代码，`fabric` / `neoforge` 只放加载器入口与平台实现。目录树、源集约束、子模组接入方式与示例见 [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)。

## 客户端命令

- `/mite_recrafted:cnutrition` - 显示当前玩家的营养状态

## 贡献

1. 先 `./gradlew :fabric:runDatagen` 生成数据，再 `./gradlew build` 无错误
2. 通用内容放 `common`，平台专有实现放 `fabric` / `neoforge`；客户端注册写进各平台的 `ClientRegistrationsImpl`
3. 注册表写入放 `registry/`，配方/标签/模型优先使用 datagen
4. 注释只写实现事实：公式、单位、调用时机、约束

## 致谢

- 所有参与开发和建议的人员

## License

<a href="https://github.com/A-Team-PJnCeADc/mite-recrafted">mite-recrafted</a> © 2026 by <a href="https://github.com/A-Team-PJnCeADc">A-Team-PJnCeADc</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>

例外：`assets/ironchests/` 下的 38 张贴图（箱子实体贴图与升级物品图标）取自 <a href="https://github.com/cpw/ironchest">IronChests</a> 项目，按 <a href="[https://www.gnu.org/licenses/gpl-3.0.txt](https://github.com/4nner/IronChests?tab=GPL-3.0-1-ov-file)">GPL-3.0</a> 提供。该目录与项目其余部分为聚合关系：贴图独立存放并按普通 PNG 资源读取，主项目不含 IronChests 的 GPL 代码，其余代码仍适用前述 CC BY-NC-SA 4.0。逐文件清单与隔离方式见 [THIRD-PARTY-NOTICES.md](docs/THIRD-PARTY-NOTICES.md)，许可全文见 `LICENSES/IronChests-GPL-3.0-license`。
