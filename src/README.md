# PoorSpace 插件

## 简介

PoorSpace 是一款为 Minecraft Bukkit/Spigot 服务器设计的插件，旨在提供一个强大的区域管理系统。它允许玩家拥有、管理和自定义被称为“空间”的特定区域，并为这些空间设置详细的权限，同时支持多世界环境。

## 主要功能

*   **空间管理**:
    *   玩家可以在不同世界（如主世界、下界、末地、创造界、小游戏界）拥有和管理自己的空间。
    *   空间通过唯一的ID进行标识。
*   **精细化权限系统**:
    *   空间所有者可以为空间内的不同权限组（通常分为多个等级）设置详细的操作权限。
    *   权限控制包括：方块放置/破坏、物品拾取/丢弃、实体交互、容器使用等。
*   **权限组 (Space Groups)**:
    *   玩家可以创建和管理“空间组”，方便地将一组玩家作为一个整体进行权限管理或共享空间。
    *   空间组拥有者、管理员和成员等角色。
*   **选择器 (Selectors)**:
    *   玩家可以定义和使用选择器（如 `all`, `now`, `new` 或自定义选择器）来批量管理多个空间的权限。
*   **GUI 操作界面**:
    *   提供用户友好的图形用户界面（通过箱子菜单），方便玩家浏览、管理其拥有的空间、空间组以及相关设置。
*   **指令操作**:
    *   提供丰富的指令集，用于高级管理和自动化操作。
*   **多世界支持**:
    *   支持在多个世界中创建和管理空间，例如：
        *   `world` (主世界)
        *   `world_nether` (下界)
        *   `world_the_end` (末地)
        *   `creative` (创造界)
        *   `minigame` (小游戏界)
*   **数据存储**:
    *   空间、玩家和空间组的数据通过 YAML 文件存储在插件的配置文件夹中。

## 主要类和模块

*   **`PoorSpace.java`**: <mcfile name="PoorSpace.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\PoorSpace.java"></mcfile> - 插件的主类，负责插件的加载、初始化监听器、指令注册等。
*   **`SpaceExecutor.java`**: <mcfile name="SpaceExecutor.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\SpaceExecutor.java"></mcfile> - 处理 `/poorspace` 相关指令的执行逻辑。
*   **`SpaceTabCompleter.java`**: <mcfile name="SpaceTabCompleter.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\SpaceTabCompleter.java"></mcfile> - 为 `/poorspace` 指令提供参数自动补全功能。
*   **`Space.java`** (<mcfile name="Space.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\Space.java"></mcfile>), **`NormalSpace.java`** (<mcsymbol name="NormalSpace" filename="NormalSpace.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\NormalSpace.java" startline="15" type="class"></mcsymbol>), **`DefaultSpace.java`** (<mcsymbol name="DefaultSpace" filename="DefaultSpace.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\DefaultSpace.java" startline="6" type="class"></mcsymbol>): 定义和管理空间对象及其属性（如权限、所属世界、ID等）的核心类。
*   **`SpacePlayer.java`**: <mcsymbol name="SpacePlayer" filename="SpacePlayer.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\SpacePlayer.java" startline="15" type="class"></mcsymbol> - 管理玩家特定的空间数据，如拥有的空间列表、自定义选择器等。
*   **`SpaceGroup.java`**: <mcsymbol name="SpaceGroup" filename="SpaceGroup.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\SpaceGroup.java" startline="14" type="class"></mcsymbol> - 管理空间组的创建、成员、权限等。
*   **`SpaceManager.java`**: <mcsymbol name="SpaceManager" filename="SpaceManager.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\SpaceManager.java" startline="8" type="class"></mcsymbol> - 负责加载、缓存和管理服务器中活动的空间实例。
*   **`SpaceOpen.java`**: <mcsymbol name="SpaceOpen" filename="SpaceOpen.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\space\SpaceOpen.java" startline="18" type="class"></mcsymbol> - 处理打开各种PoorSpace相关GUI界面的逻辑。
*   **`InvListener.java`**: <mcsymbol name="InvListener" filename="InvListener.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\listener\InvListener.java" startline="24" type="class"></mcsymbol> - 监听GUI界面中的点击事件，并执行相应操作。
*   **`SpaceListener.java`**: <mcsymbol name="SpaceListener" filename="SpaceListener.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\listener\SpaceListener.java" startline="35" type="class"></mcsymbol> - 监听游戏内与空间相关的各种事件（如方块放置/破坏、实体交互等），并根据空间权限进行处理。
*   **`FileListener.java`**: <mcsymbol name="FileListener" filename="FileListener.java" path="d:\Code\java\PoorSpace\src\com\gmail\jobstone\listener\FileListener.java" startline="10" type="class"></mcsymbol> - 在玩家登录时，确保其相关的配置文件存在。

## 主要指令

### `/poorspace` (别名: `/ps`)

这是PoorSpace插件的主指令。

*   **`/ps`**: 打开当前所在空间的管理界面。
*   **`/ps space <world_name> <space_id>`**: 打开指定世界中指定ID的空间管理界面。
    *   `world_name`: `world`, `world_nether`, `world_the_end`, `creative`。
    *   `space_id`: 空间的坐标ID (例如 `0.0`)。
*   **`/ps pmgroup <set|add|remove> <world_name> <selector> <group_id> [player_names...]`**: 管理空间内指定权限组的玩家列表。
    *   `operation`: `set` (设置), `add` (添加), `remove` (移除)。
    *   `selector`: 空间选择器 (`now`, `all`, `new`, 或自定义名称)。
    *   `group_id`: 权限组编号 (通常为 1, 2, 3)。
*   **`/ps permission set <world_name> <selector> <group_id> <permission_string>`**: 设置空间内指定权限组的权限。
    *   `group_id`: 权限组编号 (通常为 1, 2, 3, 4)。
    *   `permission_string`: 代表权限开关的二进制字符串。
*   **`/ps group`**: 打开玩家所在的空间组列表界面。
    *   **`/ps group search [keyword]`**: 搜索空间组。
    *   **`/ps group create <group_name>`**: 创建一个新的空间组。
    *   **`/ps group add <group_name> [player_names...]`**: 向空间组添加成员 (需要相应权限)。
    *   **`/ps group remove <group_name> [player_names...]`**: 从空间组移除成员 (需要相应权限)。
*   **`/ps selector set <selector_name> <selector_value>`**: 设置一个自定义空间选择器。
*   **`/ps selector remove <selector_name>`**: 移除一个自定义空间选择器。
*   **`/ps selector list`**: 列出所有自定义空间选择器。
*   **`/ps on`**: 开启侧边栏空间信息显示。
*   **`/ps off`**: 关闭侧边栏空间信息显示。
*   **`/ps copy <world_name> <selector> <creative_space_id> [amount]`**: 复制指定选择器选中的空间到创造界。

### `/ps-op`

此指令用于插件的操作员级别管理，具体功能需参照 `OpExecutor.java` (未在当前上下文中提供)。

## 配置文件和数据存储

*   **插件主配置**: `plugins/PoorSpace/config.yml` (由插件自动生成，包含基本设置)。
*   **空间数据**: 存储在 `plugins/PoorSpace/spaces/<world_name>/<chunk_folder>/<space_id.yml>`。
*   **玩家数据**: 存储在 `plugins/PoorSpace/players/<player_name>/` 目录下，包括：
    *   `settings.yml`: 玩家个人设置，如空间信息显示开关、自定义选择器等。
    *   `default_<world_name>.yml`: 玩家在该世界的默认空间权限配置。
    *   `<world_name>.yml`: 玩家在该世界拥有的空间列表。
*   **空间组数据**: 存储在 `plugins/PoorSpace/groups/<group_name>/data.yml`。

## 安装

1.  将 `PoorSpace.jar` 文件放入您服务器的 `plugins` 文件夹中。
2.  重启或重载您的服务器。

## 依赖

*   Bukkit / Spigot / Paper API (版本需与插件兼容)。


## 权限节点 (Permissions)

以下是 PoorSpace 插件可能使用的一些主要权限节点。请注意，这部分内容需要您根据插件的 `plugin.yml` 文件或实际代码进行核实和填充。

### 用户指令权限

*   `poorspace.user`: 允许玩家使用基础的用户指令，例如打开个人空间菜单。
*   `poorspace.command.space`: 允许使用 `/ps space <world> <id>` 指令。
*   `poorspace.command.pmgroup`: 允许使用 `/ps pmgroup ...` 指令来管理空间权限组的玩家。
*   `poorspace.command.permission`: 允许使用 `/ps permission set ...` 指令来设置空间权限组的权限。
*   `poorspace.command.group.create`: 允许玩家创建新的空间组。
*   `poorspace.command.group.manage`: 允许玩家管理其拥有或作为管理员的空间组（如添加/移除成员）。
*   `poorspace.command.group.search`: 允许玩家搜索空间组。
*   `poorspace.command.selector.set`: 允许玩家设置自定义选择器。
*   `poorspace.command.selector.remove`: 允许玩家移除自定义选择器。
*   `poorspace.command.selector.list`: 允许玩家列出自己的选择器。
*   `poorspace.command.copy`: 允许玩家使用空间复制功能。
*   `poorspace.toggleinfo`: 允许玩家使用 `/ps on` 和 `/ps off` 来控制侧边栏信息显示。

### 管理员指令权限

*   `poorspace.admin`: 可能是一个总括性的管理员权限，允许访问所有管理功能。
*   `poorspace.op.<subcommand>`: (如果 `/ps-op` 指令存在) 针对 `/ps-op` 子指令的权限。

### 其他权限

*   `poorspace.bypass.protection`: (可选) 允许玩家绕过所有空间保护限制，通常给予服务器最高管理员。

**请务必检查您的 `plugin.yml` 文件以获取最准确和完整的权限节点列表。**

## 配置文件详解

PoorSpace 插件将其数据和配置存储在 `plugins/PoorSpace/` 文件夹下。

*   **`config.yml`**:
    *   路径: `plugins/PoorSpace/config.yml`
    *   用途: 插件的主要配置文件。从您提供的片段来看，它可能包含一些全局设置，例如 `level` 和 `talentpoint` (具体用途需参照插件逻辑)。您可以在此文件中调整插件的全局行为。

*   **空间数据 (Space Data)**:
    *   路径: `plugins/PoorSpace/spaces/<world_name>/<chunk_folder>/<space_id.yml>`
    *   例如: `plugins/PoorSpace/spaces/world/0.0/0.0.yml`
    *   用途: 每个 `.yml` 文件代表一个独立的空间。它存储了该空间的详细信息，包括：
        *   所有者 (owner)
        *   权限组设置 (group1, group2, group3 的成员列表)
        *   各权限组的权限字符串 (permission1, permission2, permission3, permission4)
        *   其他空间元数据。

*   **玩家数据 (Player Data)**:
    *   路径: `plugins/PoorSpace/players/<player_name>/`
    *   例如: `plugins/PoorSpace/players/Notch/`
    *   用途: 存储每个玩家特定的数据。
        *   `settings.yml`: 包含玩家的个人设置，例如：
            *   `spaceinfo`:布尔值，控制是否显示侧边栏空间信息。
            *   `selectors`: 存储玩家自定义的空间选择器。
            *   `groups`: 玩家所属的空间组列表。
        *   `default_<world_name>.yml` (例如 `default_world.yml`): 定义了玩家在该世界创建新空间时的默认权限配置。
        *   `<world_name>.yml` (例如 `world.yml`): 列出了玩家在该世界拥有的所有空间的ID。

*   **空间组数据 (Space Group Data)**:
    *   路径: `plugins/PoorSpace/groups/<group_name>/data.yml`
    *   例如: `plugins/PoorSpace/groups/MyAwesomeGroup/data.yml`
    *   用途: 每个 `data.yml` 文件代表一个空间组。它存储了该组的详细信息，包括：
        *   所有者 (owner)
        *   管理员列表 (ops)
        *   成员列表 (members)
        *   空间组的其他设置，如描述、图标等。


## 故障排查 (Troubleshooting / FAQ)

*   **Q: 为什么我无法在某个空间内放置方块/破坏方块/进行其他操作？**
    *   A: 请检查您在该空间的权限。您可能不属于具有相应操作权限的权限组。空间所有者可以通过 `/ps pmgroup` 或 `/ps permission` 指令来管理权限。确保您在正确的权限组中，并且该权限组拥有您想执行的操作的权限。

*   **Q: 我的侧边栏没有显示空间信息。**
    *   A: 请尝试使用 `/ps on` 指令来开启侧边栏空间信息显示。如果依然不显示，请检查服务器控制台是否有相关的错误信息，并确认插件已正确加载。

*   **Q: 我创建了一个空间组，但其他玩家无法加入。**
    *   A: 确保您已正确地将其他玩家添加到空间组的成员列表或管理员列表中。同时，检查被添加的玩家是否已达到其可加入空间组数量的上限（默认为9个）。

*   **Q: 插件指令无效或报错。**
    *   A:
        1.  请确保您输入的指令格式正确，可以参考本文档中的“主要指令”部分。
        2.  检查服务器控制台是否有与 PoorSpace 相关的错误日志，这有助于定位问题。
        3.  确认您拥有执行该指令所需的权限节点。
        4.  确保插件版本与您的服务器版本兼容。

*   **Q: 如何备份我的空间数据？**
    *   A: 您可以备份整个 `plugins/PoorSpace/` 文件夹。其中 `spaces/`, `players/`, 和 `groups/` 子文件夹包含了核心数据。

*(您可以根据用户反馈和实际遇到的问题，继续补充此部分。)*

## 贡献 (Contributing)

如果您对 PoorSpace 插件有任何改进建议、发现任何BUG，或希望贡献代码，欢迎通过以下方式参与：

1.  **报告问题 (Issue Tracker)**: 如果您发现了BUG或有功能建议，请在本项目的 Issue Tracker 中提交新的 Issue。（请替换为您的项目 Issue Tracker 链接，例如 GitHub Issues）
2.  **拉取请求 (Pull Requests)**: 如果您修复了BUG或实现了新功能，欢迎提交 Pull Request。请确保您的代码遵循项目的编码规范，并提供清晰的提交信息。

我们感谢所有为 PoorSpace 做出贡献的开发者！

## 许可证 (License)

PoorSpace 插件采用 [在此处填写您的许可证名称，例如：MIT License, Apache 2.0 License, GPLv3] 许可证。
请查阅项目根目录下的 `LICENSE` 文件获取完整的许可证文本。

*(如果您还没有选择许可证，可以考虑选择一个开源许可证。例如，MIT License 比较宽松。)*

## 支持与联系 (Support / Contact)

如果您在使用 PoorSpace 插件时遇到问题，或有任何疑问，可以通过以下方式获取支持：

*   **[您的项目/插件发布页链接，例如 SpigotMC 页面]**: 您可以在此页面提问或查找已知问题。
*   **[您的 Discord 服务器链接 (如果有)]**: 加入我们的 Discord 服务器进行实时交流。
*   **[您的联系邮箱 (可选)]**: `1393907820@qq.com.com`

我们会尽力为您提供帮助。