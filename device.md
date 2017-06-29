## 标准设备

标准化后的设备列表 { Array } 支持如下字段：

- {String} id 设备唯一标识 ID
- {String} type 设备类型
- {String} deviceId 设备ID， 用于标识唯一设备
- {Object} deviceInfo 设备信息， 可选， 驱动可以用到的信息
- {String} name 设备名称
- {Object} state 设备状态， 如无法获取状态，可以不返回
- {Object} actions 设备能力
- {Boolean} offline 是否离线
- {Array} tags 设备标签
- {String} accountId 帐号 ID
- {String} parent 母设备的 deviceId

## Sample

```json
{
  "actions": {
    "switch": [
      "on",
      "off"
    ]
  },
  "name": "流光开关 L1",
  "type": "switch",
  "state": {
    "switch": "off"
  },
  "deviceId": "{\"agt\":\"A3MAAAApAFwURzUzMTgxNA\",\"me\":\"27fd\",\"devtype\":\"SL_SF_IF1\",\"info\":{\"idx\":\"L1\"}}",
  "parent": "{\"agt\":\"A3MAAAApAFwURzUzMTgxNA\",\"me\":\"27fd\",\"devtype\":\"SL_SF_IF1\"}",
  "vendor": "lifesmart",
  "accountId": "BJiFejMHl",
  "offline": false,
  "disabled": false,
  "id": "S1glJziMHx",
  "tags": [
    "<name>",
    "<type>"
  ],
  "createdAt": 1483022808374,
  "updateAt": 1483076224945
}
```


## Device Type 

设备目前支持如下类型：

## 目前支持的设备类型

- `curtain` 自动窗帘
- `light` 灯
- `irController` 红外控制器
- `socket` 插座
- `switch` 开关
- `tv` 电视
- `fan` 风扇
- `ac` 空调
- `airPurifier` 空气净化器
- `scene` 场景


## 设备能力列表

| 状态量 | action  | type  |  accept |
|---|---|---|---|
| 开关 | switch  | string  | 'on', 'off', 'stop' |
| 颜色 | color  | number  | 'random', 'number' |
| 亮度 | brightness  | number  | 'up', 'down', 'max', 'min', 'num' |
| 模式 | mode  | string  |'str' |
| 启动停止 | stop  | boolean  | 'stop', 'start' |
| 位置量 | position  | number  | 'up'， 'down'， 'num' |
| 转速 | fanspeed  | number  | 'up', 'down', 'max', 'min', 'switch', 'num' |
| 转向模式 (new) | swing_mode  | 'string'  | 'auto', 'horizon', 'vertical' |
| 音量 | volume | number | 'up', 'down', 'max', 'min', 'num' |
| 频道 | channel | number | 'next', 'prev', 'random', 'num', '<string>' |
| 湿度 | humidity | number | 'up', 'down', 'max', 'min', 'num' |
| 温度 | temperature | number | 'up', 'down', 'num' |
| Ping | ping | Boolean | 'trigger' |

## 语音指令

### switch

把 Tag 打开
开