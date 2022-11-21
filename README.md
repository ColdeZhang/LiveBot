# LiveBot
 将指定玩家切换为旁观模式并自动跟随其他玩家，便于直播。

![12f9f66964a77fb0.jpg](https://sslbackend.deercloud.site:450/LightPicture/2022/11/12f9f66964a77fb0.jpg)

## 说明

因为我的服务器是公益服，平时忙于工作也没有时间自己亲自直播宣传，更没有钱找人代直播。之前一直在直播间放实时卫星地图，但是据说这样不合规？

有天在 mcbbs 看到了一个插件可以用来自动跟随，但是插件是收费的。想了下这种功能实现起来应该不难，所以就自己写了个。

> 本人是C++程序员，在此之前没有写过Java。所以如果发现代码里有不合理的地方，欢迎指出。

## 功能介绍

1. 设定指定的玩家为旁观者模式；
2. 自动跟随其他玩家；
3. 可以设置随机跟随或者顺序跟随；
4. 可以设置在每个玩家身上停留的时间；
5. 玩家可以使用指令让自己不被跟随；
6. 可以选择不跟随挂机的玩家；

## 使用方法

1. 将插件放入服务器的 `plugins` 目录下
2. 重启服务器
3. 在 `plugins/LiveBot/config.yml` 中配置
4. 控制台或OP输入 `/livebot reload` 重载配置

> 也可以不修改配置文件，直接使用指令进行设置。
> 
> 使用指令设置后也会同步更新到配置文件。

## 指令

### OP指令

`/livebot reload` 重载配置文件。
    
`/livebot setBot <playerID>` 设置用作直播视角的玩家。
    
`/livebot setTime <second>` 在每个玩家视角停留的时间。
    
`/livebot setPattern <ORDER | RANDOM>` 切换方式（顺序 ｜ 随机）。

`/livebot setCanMoved <true | false>` 玩家是否可以不被跟随。

`/livebot setSkipAFK <true | false>` 是否跳过挂机玩家。

`/livebot start` 开始自动跟随（默认都是开启的）。
    
`/livebot stop` 停止自动跟随。

`/livebot setAFKTime <second>` 设置挂机判断时间。

`/livebot status` 查看当前状态。

### 玩家指令

`/livebot away` 让自己不被跟随。


## 配置文件参考

```yaml
# LiveBot 配置文件
Bot:
  # 希望被用作直播机器人的ID
  Name: Steve
Setting:
  # 在单个玩家身上的聚焦时间（单位：秒）
  FocusTime: 300
  # 切换方案 ORDER: 顺序 RANDOM: 随机
  ChangingPattern: ORDER
  # 玩家能否设置自己不被跟随
  CanBeMoved: true
  # 是否在聊天栏公告自己的状态 (未实装)
  IsNagging: true
  # 是否跳过挂机玩家
  SkipAFK: true
  # 挂机判断时间（单位：秒）
  AFKTime: 60
```
