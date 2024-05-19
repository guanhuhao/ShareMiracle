## 开发

### 安装开发环境

- 安装 java 和 maven
- 安装依赖库

```bash
cd ShareMiracleApp
mvn clean install
```

### 编译运行后端

```bash
cd ShareMiracleApp
mvn compile
mvn package
java -jar ShareMiracle-server/target/ShareMiracle-server-1.0-SNAPSHOT.jar
```

### 部署

```bash
cd ShareMiracleApp/ShareMiracle-server
mvn clean package
scp ShareMiracle-server/target/ShareMiracle-server-1.0-SNAPSHOT.jar xxx@xxx:/xxx/
```

### 开发环境与部署环境

服务端数据库配置文件（向项目负责人索要）根目录：

```
./ShareMiracleApp/ShareMiracle-server/src/main/resources
```

以下应有三个 yml 文件

```
application-dev.yml     配置参数 for 开发环境
application-lab.yml     配置参数 for 部署环境
application.yml         数据库配置文件
```

如果不在内网进行开发，可以在 `application.yml` 中把 `spring.profiles.active` 改成 `dev`

---

## 项目结构

```text
ShareMiracle-common 存放项目的通用工具
├─📁constant    常量
├─📁context     存放上下文信息（当前用户id）
├─📁exception   存放自定义异常类
├─📁json        对象映射器  基于jackson将Java对象转为json，或将json转为Java对象
├─📁properties  配置信息类（例如阿里云信息、jwt信息配置）
├─📁result      返回结果封装类 统一返回
└─📁utils       存放工具类 (如阿里云工具类、JWT工具类等)


ShareMiracle-pojo 存放项目中使用到的dto（传输）、vo（前端展示）、entity（实体）
├─📁 dto        前端信息传输的实体类
├─📁 vo         封装返回给前端展示的实体类
└─📁 entity     实体信息（与数据库对应）

ShareMiracle-server 存放项目具体的服务相关类
├─📁 config      存放配置信息类（如swagger测试文档配置、redis配置等；类命名规范：用途+Config（如MyBatisConfig）
├─📁 controller  存放控制器相关实现类；类命名规范：模块+Controller（如UserController）
├─📁 handler     存放全局处理器（如全局异常处理器，处理项目中抛出的业务异常）
├─📁 interceptor 存放全局拦截器（对所有请求进行拦截处理，根据具体的拦截处理逻辑，实现自定义拦截器）
├─📁 mapper      存放Mapper接口；类命名规范：模块+Mapper（如UserMapper）
├─📁 service     存放服务接口:命名规范：模块+Service（如UserService）
└─📁 impl        存放服务接口对应的实现类:命名规范：模块+ServiceImpl（如UserService）
```