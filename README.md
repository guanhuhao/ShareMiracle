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
cd ShareMiracleApp/ShareMiracle-server
mvn compile && mvn spring-boot:run
```

### 部署

```bash
cd ShareMiracleApp/ShareMiracle-server
mvn clean package
scp target/xxx ...
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