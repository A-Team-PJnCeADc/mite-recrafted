# MITE-Recrafted

> "一个人不可能只是走进群山，用手中的石镐挖下铁矿。"

MITE-Recrafted 是经典 Minecraft 难度模组 **[MITE (Minecraft Is Too Easy)](https://www.mcmod.cn/class/226.html)** 的高版本移植项目，基于 Fabric 模组加载器，目标 Minecraft 版本为 **26.3**（Java 25）。

原版 MITE 仅基于 Minecraft 1.6.4 核心，通过直接覆盖类文件实现，不兼容任何其他模组。本项目使用 Mixin 技术以非侵入方式重新实现 MITE 的核心机制，使其能够在现代 Minecraft 版本上运行，并具备一定的与其他 Fabric 模组的兼容性。

## Progress

Developing

## 未实现的原版 MITE 功能

以下为原版 MITE 中已有但 MITE-Recrafted 尚未实现的功能：

- [ ] **玩家初始状态限制**：初始只有 3 格血与饥饿值（代码中有 `//todo 初始饱食度3个` 注释）
- [ ] **银质工具对亡灵伤害加成**：代码中有 `//TODO 银质工具对亡灵怪物有伤害加成` 注释
- [ ] **数值配平**：代码中有 `//todo 配平数值` 注释，表明工具/伤害/速度物品数值尚未最终确定
- [ ] **新的维度**：地下世界 (Underworld) 和修改后的地狱进入条件
- [ ] **新生物**：惧狼/地狱犬/食尸鬼/古尸/影子潜伏者等 26 种新生物
- [ ] **新的熔炉与热量等级**：粘土/沙石/黑曜石/地狱岩熔炉
- [ ] **附魔系统**：速度/再生/击晕/吸血/缴械/穿刺等新附魔
- [ ] **耕种机制修改**：洋葱种植/蓝莓丛/疫病/粪肥等
- [ ] **天气影响**：血月/蓝月/丰收之月
- [ ] **创造模式移除**
- [ ] **F3 屏幕限制**
- [ ] **方块重力系统修改**
- [ ] **动物生病系统**
- [ ] **其他**

---

### 客户端命令

- `/mite-recrafted:cnutrition` — 显示当前玩家的营养状态调试信息

---

## 依赖

- **Java 25**
- **[Fabric API](https://github.com/FabricMC/fabric)**
- **[IronChests](https://github.com/4nner/IronChests)**
- **[AppleSkin](https://github.com/squeek502/AppleSkin/tree/26.2-fabric)**

## 构建与开发

```bash
# 克隆仓库
git clone --recursive https://github.com/A-Team-PJnCeADc/mite-recrafted.git
cd mite-recrafted

# 生成数据
./gradlew runDatagen

# 构建模组
./gradlew build

# 生成 Minecraft 源代码
./gradlew genSources

# 运行客户端
./gradlew runClient

# 运行服务端
./gradlew runServer
```

## 项目结构

```
mite-recrafted/
├── src/
│   ├── main/
│   │   ├── java/com/mite/recraft/
│   │   │   ├── block/           # 方块相关
│   │   │   ├── component/       # 数据组件
│   │   │   ├── datagen/         # 数据生成（配方、标签、语言）
│   │   │   ├── entity/          # 实体相关
│   │   │   ├── item/            # 物品相关
│   │   │   │   ├── tools/       # 工具系统
│   │   │   │   ├── quality/     # 品质系统
│   │   │   │   └── moditems/    # 自定义物品（含 strongbox）
│   │   │   ├── mixin/           # 服务端/通用 Mixin 注入
│   │   │   ├── network/         # 网络同步
│   │   │   └── MiteRecrafted.java
│   │   └── resources/
│   │       ├── assets/          # 纹理、模型、语言文件
│   │       ├── data/            # 战利品表、标签
│   │       ├── fabric.mod.json  # 模组配置
│   │       └── mite-recrafted.mixins.json
│   ├── client/
│   │    java/com/mite/recraft/client/
│   │      ├── strongbox/       # 箱子渲染器、GUI 界面
│   │      └── datagen/         # 客户端数据生成（atlas、模型）
│   └── main/generated/          # datagen 输出
├── submodules/
│   └── IronChests/              # 子模块
└── build.gradle
```

---

## 贡献

欢迎提交 Pull Request 或 Issue。在提交前请确保：

1. 构建没有错误
2. 客户端代码放在 `src/client/` 源集下，通用代码放在 `src/main/`
3. 所有配方/标签/模型的修改优先使用 datagen 

---

## 致谢

- 所有参与开发和建议的人员

---

## License

<a href="https://github.com/A-Team-PJnCeADc/mite-recrafted">mite-recrafted</a> © 2026 by <a href="https://github.com/A-Team-PJnCeADc">A-Team-PJnCeADc</a> is licensed under <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International</a><img src="https://mirrors.creativecommons.org/presskit/icons/cc.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/by.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/nc.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;"><img src="https://mirrors.creativecommons.org/presskit/icons/sa.svg" alt="" style="max-width: 1em;max-height:1em;margin-left: .2em;">

