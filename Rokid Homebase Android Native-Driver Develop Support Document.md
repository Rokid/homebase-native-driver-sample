# Rokid Homebase Android Native-Driver Develop Support Document

# 通用
### 通用格式(Json)

**驱动控制成功返回数据格式**

- `status` 必须 0
- `data` 可选, 根据接口类型返回不同的结果

```json
{
  "status": 0,
  "data": <content>
}
```
**驱动控制错误返回数据格式**

- `status` 必须 大于0 的正整数
- `errorNum` 标准错误码
- `message` 必须 错误简单描述

```json
{
  "status": 1,
  "message": "time out"
}
```

## 标准接口

### `POST /list` 设备搜索

搜索给定用户账号下的所有设备， 包括虚拟设备， 子设备

驱动接收请求参数：

- userAuth 用户授权信息

```json

{
  "userAuth": {
    "userId": "",
    "userToken": ""
  }
}
```


**驱动执行请求后返回值**

标准化后的设备列表 { Array }, 支持如下字段：

- type 设备类型
- deviceId 设备类型
- name 设备名称
- state 设备状态， 如无法获取状态，可以不返回
- deviceId 设备厂商唯一ID， 字符串， 可以用来保存用来识别设备的信息
- actions

```json
{
  "status": 0,
    "data": [
      {
        "type": "light",
        "deviceId": "123123",
        "name": "灯灯灯灯",
        "actions": {
          "switch": ["on", "off"]
        },
        "state": {
          "switch": "off"
        }
      }
    ]
}
```

### `POST /get` 获取单个设备状态

获取单个设备最新状态和信息

**参数**

- device
- device.deviceId  {String} 厂商设备ID， 可以包含不可变数据， 与厂商ID一起，可以唯一确认一台设备
- device.state  {Object} 获取设备当前状态
- device.userAuthObj {Object} 设备关联的用户授权信息

```json
{
  "device": {
     "deviceId": "xxx"
  },
  "userAUth": {
    "userId": "",
    "userToken": ""
  }
}
```

response

```json

{
  "status": 0,
  "data": {
     "deviceId": "xxx",
     "state": {
       "switch": "on"
     },
     "userAUth": {
       "userId": "",
       "userToken": ""
     }
  }
}

```

**返回值**

Object 设备最新状态对象：

- `name` 设备名称
- `actions` 设备支持的 action
- `state` 设备运行状态

**注意事项**

- 有些设备如果无法返回状态，返回一个空的对象


### `POST /execute` 执行操作指令， 并返回可确定的最新状态

设备执行操作， 将返回最新状态

**参数**

- device `{Object}`
- device.deviceId  `{String}`, 厂商设备ID， 可以包含不可变数据， 与厂商ID一起，可以唯一确认一台设备
- device.state  `{Object}`, 获取设备当前状态
- device.userAuth `{Object}`, 用户授权信息
- action `{Object}` 需要执行的操作， 参考文档 homebase.devices.v4.pdf



request

```json
{
  "device": {
     "deviceId": "xxx",
     "state": {
        "switch": "on"
     },
     "userAUth": {
        "userId": "",
        "userToken": ""
     }
  },
  "action": {
    "switch": "on"（在我们协议中，颜色使用RGB的int值来表示，而亮度使用单独的一个属性brightness(支持0-100，超出范围需驱动进行数据转换)来描述。）
  }
}
```

response

```json

{
  "status": 0,
  "data": {
    "switch": "on"
  }
}

```




actions 一个系列操作， 示例 "打开灯并设置成红色"
```
{
  "switch": "on",
  "color": RGB的int值，如蓝色用(int)255
}
```


**返回值**

Object 设备最新状态， 如果无法确定设备的状态， 返回字段可以置空， 返回的设备状态将被缓存到虚拟设备中去


### `POST /command` 执行特定指令

执行自定义指令

说明：

- 如果是用户名，密码登录的 Driver 必须提供 `login` command，返回 userId,userToken
- 如果是 OAuth 登录的， 必须提供 `OAuth` command, 返回 OAuth loginUrl

**参数**

- command `{String}`
- params  `{Object}`

request sample

```json
{
  "command": "login",
  "params": {
    "username": "superman",
    "password" "xxxxxx"
  }
}
```

request sample

```json

{
  "status": 0,
  "data": {
    "userId": "superman",
    "userToken": "DFGHJKLCVBNMFGHJKL"
  }
}

```


#### 常用指令 Command

command **OAuth**

params:

- authCallbackUrl 登录后回调页面

request
```json
{
  "authCallbackUrl": "http://foo/bar"
}

```

returns { String } 登录跳转 URL

response
```json
{
  "status": 0,
  "data": "http://oauthurl"
}
```

command **OAuthRefresh**

params:

- userId 用户Id
- userToken 用户Token

```json
{
    "userId": "superman",
    "userToken": "fjfjfjfjfjfj"
}
```

returns result <Object>

- result.userToken
- result.expireTime

```json
{
  "status": 0,
  "data": {
    "userToken": "",
    "expireTime": ""
  }
}
```

command **login**

params:

- username 用户Id
- password 密码

```json
{
    "username": "superman",
    "password": "fjfjfjfjfjfj"
}
```

returns result <Object>

- result.userToken

```json
{
  "status": 0,
  "data": {
    "userToken": "userToken"
  }
}
```

### 关于 deviceId

关于 deviceId， deviceId 用户存储一些在设备执行需要用到的设备识别参数， 我们建议将需要的参数序列化成字符串（比如， 通过 JSON ）， 在获取的时候反序列化，并拿到其中的参数

deviceId 需要保持灵活性和可扩展性， 以便后续设备需要更多的信息。


### 标准设备

标准设备是基于文档  homebase.devices.v4.pdf， 数据接口示例。

```
    {
      "type": "light",
      "state": {
        "switch": "off",
        "brightness": 60
      },
      "deviceId": "A3MAAABlADcWRzUzMTgxNA##2732##SL_LI_RGBW",
      "name": "智能灯泡",
      "vendor": "lifesmart",
      "tags":["<type>", "卧室的灯"],
      "accountId": 1,
      "actions": {
        "switch": ["on", "off"],
        "color": ["random", "color"],
        "brightness": ["up", "down", "max", "min", "num"]
      }
    }
```


- `type` 设备类型, `string`
- `state` 设备状态对象, `object`
- `actions` 支持的动作, `Object`
- `accountId` 关联的厂商用户token ID, `number`
- `vendor` 厂商ID, `string`
- `name` 原始名称, `string`
- `stat` 是否离线, `string "on"|"off"`
- `deviceId` 厂商设备ID, `string`


### 标准设备属性（propertyTypes）定义

设备属性像 Rokid 描述了被操控的设备具有什么样的功能，以及此功能的具体支持程度

对象的 key 表示可以控制的物理量， 对象的值数组是对当前物理状态支持的控制值



```
{
  "switch": ["on", "off", "value"],
  "color": ["random", "value"],
  "mode": [
    {
      name: 'auto',
      label: '自动模式'，
      description: "自动模式自动控制"
    },
    {
      name: 'manual',
      label: '手动模式'，
      description: "手动控制"
    },
    {
      name: 'sleep',
      label: '睡眠模式'，
      description: "自动模式自动控制"
    }
  ]
}
```


## 设备能力列表

| 状态量 | action  | type  |  accept |
|---|---|---|---|
| 开关 | switch  | string  | 'on', 'off' |
| 颜色 | color  | number  | 'random', <number> |
| 亮度 | brightness  | number  | 'up', 'down', 'max', 'min', <number> |
| 模式 | mode  | string  | <string> |
| 启动停止 | stop  | boolean  | 'stop', 'start' |
| 位置量 | position  | number  | 'up'， 'down'， <number> |
| 转速 | fanspeed  | number  | 'up', 'down', 'max', 'min', 'switch', <number> |
| 转向模式 (new) | swing_mode  | string  | 'auto', 'horizon', 'vertical' |
| 音量 | volume | number | 'up', 'down', 'max', 'min', <number>' |
| 频道 | channel | number | 'next', 'prev', 'random', <number>, <string> |
| 湿度 | humidity | number | 'up', 'down', 'max', 'min', <number> |
| 温度 | temp | number | 'up', 'down', 'max', 'min', <number> |
| Ping | ping | Boolean | 'trigger' |

## 标准错误码

`errNumber` For Better User Feed back, we support some of error code that will readout in tts; these is

- 10002  Driver Sign Error, Please check rokid app and reconnect your account
- 10003  Device Not Found
- 10004  Driver Sign Error
