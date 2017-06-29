# About Actions


### 定义设备能力

actions 是一个对象，如下是一个彩色智能灯泡的 actions

```json
{
  "brightness": ["num", "up", "down"],
  "color": ["num"],
  "switch": ["on", "off"]
}
```

- keys `color`, `brightness`, `switch` 表示这个设备支持什么控制能力；
- value 是一个数组， 表示驱动支持该能力下什么样的特性


上面这个示例表明， 当前设备支持开关， 颜色， 亮度。 其中， 

- 开关 `switch`
  - 支持两个值， 开 `on`, 关 `off`
- 颜色 `color`
  - 支持传入数值 `num`， 所以支持RGB的数字形式
- 亮度 `brightness` 
  - 支持传入数值，`num`， 支持颜色 0-100调节；
  - 支持传入，`up`， 驱动控制亮度的升高；
  - 支持传入，`down`， 由驱动控制亮度的降低。
  
  
## 自动补全

设备不必实现所有功能的特性， 比如，如果我知道设备当前亮度， 设备又提供 brightness.num 特性， 那么， 智能家居的控制器就能自动支持 `brightness.up`, `brightness.down`, `brightness.min`, `brightness.max`.

自动补全随机颜色

`actions.color.num` -&gt; `random`

自动补全亮度调节

如果知道设备支持调节亮度， 并且知道设备当前亮度， 就可以支持诸如， 调亮， 调暗， 调到最亮， 调到最暗的操作

`actions.brightness.num + state.brightness` -&gt; `up, down, min, max`

## 设备能力

| 状态量 | action  | type  |  accept |
|---|---|---|---|
| 开关 | switch  | string  | 'on', 'off', 'stop' |
| 颜色 | color  | number  | 'random', 'num' |
| 亮度 | brightness  | number  | 'up', 'down', 'max', 'min', 'num' |
| 模式 | mode  | string  |'str' |
| 位置量 | position  | number  | 'up'， 'down'， 'num' |
| 转速 | fanspeed  | number  | 'up', 'down', 'max', 'min', 'switch', 'num' |
| 转向模式 (new) | swing_mode  | 'string'  | 'auto', 'horizon', 'vertical' |
| 音量 | volume | number | 'up', 'down', 'max', 'min', 'num' |
| 频道 | channel | number | 'next', 'prev', 'random', 'num', '<string>' |
| 湿度 | humidity | number | 'up', 'down', 'max', 'min', 'num' |
| 温度 | temperature | number | 'up', 'down', 'num' |
| Ping | ping | Boolean | 'trigger' |