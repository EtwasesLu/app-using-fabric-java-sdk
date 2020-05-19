# portal Fabric 模块

## 模块
* Fabric网络模块
* 链码交互模块（主要）

## 网络模块功能
* 新增通道
* 加入通道
* 安装链码（包括初始化链码）

### 新增通道
#### createChannel
* orgAdmin
* channelName
* orderer
* peers[]
* return channel

### 加入通道
#### joinChannel
* orgAdmin
* channelName
* orderer
* peers[]
* return boolean(true or false)

### 安装链码（包括初始化链码）
#### deployInstantiateChaincode
* orgAdmin
* channelName
* orderer
* peers[]
* chainCodeName
* chainCodePath
* chainRootDir
* lang
* chainCodeVersion
* return boolean(true or false)


## 链码交互模块功能
* 合同写入
* 合同查询

### 合同写入
#### insertContract
* code
* goodsName
* goodsCode
* accountCode
* totalPrice
* 其他参数待增加
* return boolean(true or false)

### 合同查询
#### queryContract
* code
* return contract
