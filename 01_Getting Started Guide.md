# Keycloak入门指南

[原文地址: https://www.keycloak.org/docs/latest/getting_started/index.html](https://www.keycloak.org/docs/latest/getting_started/index.html)


<a name="1___1__概述"></a>
## 1. 概述

本指南可帮助您开始使用Keycloak。 它涵盖了服务器配置和默认数据库的使用。 不包括高级部署选项。 有关功能或配置选项的更深入说明，请参阅其他参考指南。

<a name="2___2__安装和启动"></a>
## 2. 安装和启动

本节介绍如何在独立模式下启动Keycloak服务器，设置初始管理员用户，以及登录Keycloak管理控制台。

<a name="3____2_1__安装分发文件"></a>
### 2.1. 安装分发文件

下载Keycloak服务器分发文件:

- **keycloak-6.0.1.[zip|tar.gz]**

> 该文件可以从 [Keycloak downloads](https://www.keycloak.org/downloads.html)下载. 

**keycloak-6.0.1.[zip|tar.gz]**文件是服务器专用的分发版。它只包含运行Keycloak服务器的脚本和二进制文件。

将文件放在您选择的目录中，并使用`unzip`或`tar`实用程序来提取它。

Linux/Unix

```
$ unzip keycloak-6.0.1.zip

or

$ tar -xvzf keycloak-6.0.1.tar.gz
```

Windows

```
> unzip keycloak-6.0.1.zip
```

<a name="4____2_2__启动服务器"></a>
### 2.2. 启动服务器

要启动Keycloak服务器，请转到服务器分发的`bin`目录并运行`standalone`启动脚本：

Linux/Unix

```
$ cd bin
$ ./standalone.sh
```

Windows

```
> ...\bin\standalone.bat
```

<a name="5____2_3__创建管理员帐户"></a>
### 2.3. 创建管理员帐户

服务器启动后，在web浏览器中打开`http://localhost:8080/auth`。欢迎页面将指示服务器正在运行。

输入用户名和密码来创建初始管理用户。

该帐户将被允许登录到`master(主)` realm(域)的管理控制台，您将从该控制台创建域和用户，并注册应用程序，这些应用程序将由Keycloak进行保护。

> 如果使用`localhost`连接，则只能在欢迎页面上创建初始管理员用户。 这是一项安全预防措施。 您可以使用`add-user-keycloak.sh`脚本在命令行创建初始管理员用户。 有关详细信息，请参阅[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)和[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。 

<a name="6____2_4__登录管理控制台"></a>
### 2.4. 登录管理控制台

创建初始管理员帐户后，请使用以下步骤登录管理控制台：

1. 单击 **Welcome** 页面上的 **Administration Console** 链接或直接转到控制台URL <http://localhost:8080/auth/admin/>

2. 在**Welcome**页面上键入您创建的用户名和密码，打开**Keycloak Admin Console**。

   Admin Console(管理控制台)

   ![admin console](assets/admin-console.png)

<a name="7___3__创建领域和用户"></a>
## 3. 创建领域和用户

在本节中，您将在Keycloak管理控制台中创建一个新领域，并向该领域添加新用户。 您将使用该新用户登录到您的新域并访问所有用户都可以访问的内置用户帐户服务。

<a name="8____3_1__在你开始之前"></a>
### 3.1. 在你开始之前

(https://www.keycloak.org/docs/latest/getting_started/index.html#_install-boot)

<a name="9____3_2__创建一个新领域"></a>
### 3.2. 创建一个新领域

要创建新领域，请完成以下步骤：

1. 转到<http://localhost:8080/auth/admin/>并使用您在[安装和启动](https://www.keycloak.org/docs/latest/getting_started/index.html#_install-boot)中创建的帐户登录Keycloak管理控制台。
2. 从**Master**下拉菜单中，单击**Add Realm**。 当您登录到主域时，此下拉菜单会列出所有现有域。
3. 在**Name**字段中输入`demo`，然后单击**Create**。

创建领域后，将打开主管理控制台页面。 注意，当前的域现在设置为`demo`。 通过单击**Select realm**下拉菜单中的条目，在管理`master`领域和刚刚创建的领域之间切换。

<a name="10____3_3__创建新用户"></a>
### 3.3. 创建新用户

要在`demo`域中创建新用户，以及该新用户的临时密码，请完成以下步骤：

1. 从菜单中，单击**Users**以打开用户列表页面。
2. 在空用户列表的右侧，单击**Add User**以打开添加用户页面。
3. 在Username`字段中输入名称; 这是唯一必填字段。 单击**保存**以保存数据并打开新用户的管理页面。
4. 单击**Credentials**选项卡为新用户设置临时密码。
5. 输入新密码并确认。 单击**Reset Password**将用户密码设置为您指定的新密码。

> 此密码是临时的，用户需要在首次登录后进行更改。 要创建持久密码，请在单击**Reset Password**之前将**Temporary**开关从**On**翻转到**Off**。

<a name="11____3_4__用户帐户服务"></a>
### 3.4. 用户帐户服务

1. 创建新用户后，打开用户下拉菜单并选择**Sign Out**，注销管理控制台。
2. 转到<http://localhost:8080/auth/realms/demo/account>并使用刚创建的用户登录您的`demo`域的用户帐户服务。
3. 输入您创建的用户名和密码。 成功登录后，您将需要创建一个永久密码，除非您在创建密码时将**Temporary**设置更改为**Off**。

将打开用户帐户服务页面。 默认情况下，领域中的每个用户都可以访问此帐户服务。 在此页面中，您可以更新配置文件信息并更改或添加其他凭据。 有关此服务的详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。

<a name="12___4__保护JBoss_Servlet应用程序"></a>
## 4. 保护JBoss Servlet应用程序

本节介绍如何通过以下方式在WildFly应用程序服务器上保护Java servlet应用程序：

- 在WildFly应用程序服务器分发上安装Keycloak客户端适配器
- 在Keycloak管理控制台中创建和注册客户端应用程序
- 配置要由Keycloak保护的应用程序

<a name="13____4_1__在你开始之前"></a>
### 4.1. 在你开始之前

在确保Java servlet应用程序安全之前，必须完成Keycloak的安装并创建初始管理员用户，如[安装和引导](https://www.keycloak.org/docs/latest/getting_started/index.html#_install-boot)中所示。

有一点需要注意：尽管WildFly与Keycloak捆绑在一起，但您不能将其用作应用程序容器。 相反，您必须在与Keycloak服务器相同的机器上运行单独的WildFly实例才能运行Java servlet应用程序。 使用与WildFly不同的端口运行Keycloak，以避免端口冲突。

要调整使用的端口，请在从命令行启动服务器时更改`jboss.socket.binding.port-offset`系统属性的值。 此属性的值是一个数字，将添加到Keycloak服务器打开的每个端口的基值。

要在调整端口的同时启动Keycloak服务器：

Linux/Unix

```bash
$ cd bin
$ ./standalone.sh -Djboss.socket.binding.port-offset=100
```

Windows

```bat
> ...\bin\standalone.bat -Djboss.socket.binding.port-offset=100
```

启动Keycloak后，转到<http://localhost:8180/auth/admin/>以访问管理控制台。

<a name="14____4_2__安装客户端适配器"></a>
### 4.2. 安装客户端适配器

下载WildFly发行版并将其从压缩文件中提取到计算机上的目录中。

从[keycloak.org](https://www.keycloak.org/downloads.html)下载WildFly OpenID Connect适配器分发版。

将此文件的内容解压缩到WildFly发行版的根目录中。

为您的平台运行适当的脚本：

WildFly 10 和 Linux/Unix

```bash
$ cd bin
$ ./jboss-cli.sh --file=adapter-install-offline.cli
```

WildFly 10 和 Windows

```bat
> cd bin
> jboss-cli.bat --file=adapter-install-offline.cli
```

Wildfly 11 和 Linux/Unix

```bash
$ cd bin
$ ./jboss-cli.sh --file=adapter-elytron-install-offline.cli
```

Wildfly 11 和 Windows

```
> cd bin
> jboss-cli.bat --file=adapter-elytron-install-offline.cli
```

> 此脚本将对您的应用服务器分发的`…/standalone/configuration/standalone.xml`文件进行必要的编辑，可能需要一些时间才能完成。

启动应用程序服务器。

Linux/Unix

```bash
$ cd bin
$ ./standalone.sh
```

Windows

```bat
> ...\bin\standalone.bat
```

<a name="15____4_3__下载，构建和部署应用程序代码"></a>
### 4.3. 下载，构建和部署应用程序代码

您必须在您的机器上安装以下软件，并在您的PATH路径中可用，然后才能继续:

- Java JDK 8
- Apache Maven 3.1.1 or higher
- Git

> 您可以通过克隆<https://github.com/keycloak/keycloak-quickstarts>上的Keycloak Quickstarts存储库来获取代码。 快速入门旨在与最新的Keycloak版本一起使用。

在继续之前，请确保已启动WildFly应用程序服务器。

要下载，构建和部署代码，请完成以下步骤。

克隆项目

```bash
$ git clone https://github.com/keycloak/keycloak-quickstarts
$ cd keycloak-quickstarts/app-profile-jee-vanilla
$ mvn clean wildfly:deploy
```

在安装过程中，您将在应用程序服务器控制台窗口中看到一些文本滚动。

要确认应用程序已成功部署，请转至<http://localhost:8080/vanilla>，然后将显示登录页面。

> 如果单击**Login**，浏览器将弹出BASIC auth登录对话框。 但是，应用程序尚未受到任何身份提供程序的保护，因此您在对话框中输入的任何内容都将导致服务器发回`Forbidden`消息。 您可以通过查找应用程序的`web.xml`文件中的设置来确认应用程序当前是通过`BASIC`authentication保护的。

<a name="16____4_4__创建和注册客户端"></a>
### 4.4. 创建和注册客户端

要在Keycloak管理控制台中定义和注册客户端，请完成以下步骤：

1. 使用您的管理员帐户登录管理控制台。

2. 在左上角的下拉菜单中选择并管理  `Demo` 领域。 单击左侧菜单中的`Clients`以打开“客户端”页面。

   Clients(客户端)

   ![clients](assets/clients.png)

3. 在右侧，单击**Create**。

4. 填写如下所示的字段：

   Add Client(添加客户端)

   ![add client](assets/add-client.png)

5. 单击**Save**以创建客户端应用程序条目。

6. 单击Keycloak管理控制台中的**Installation**选项卡以获取配置模板。

7. 选择**Keycloak OIDC JBoss Subsystem XML**以生成XML模板。 复制内容以供下一部分使用。

   模板 XML

   ![client install selected](assets/client-install-selected.png)

<a name="17____4_5__配置子系统"></a>
### 4.5. 配置子系统

要配置部署应用程序的WildFly实例，以便Keycloak保护此应用程序，请完成以下步骤。

1. 打开部署了应用程序的WildFly实例中的`standalone/configuration/standalone.xml`文件，并搜索以下文本：

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1"/>
   ```

2. 修改此文本以准备粘贴内容的文件来自**Keycloak OIDC JBoss Subsystem XML**模板我们获得Keycloak管理控制台**Installation**选项卡，通过将XML条目从自动关闭更改为使用一对开放和结束标签：

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
   </subsystem>
   ```

3. 将模板的内容粘贴到`<subsystem>`元素中，如下例所示：

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
     <secure-deployment name="WAR MODULE NAME.war">
       <realm>demo</realm>
       <auth-server-url>http://localhost:8180/auth</auth-server-url>
       <public-client>true</public-client>
       <ssl-required>EXTERNAL</ssl-required>
       <resource>vanilla</resource>
     </secure-deployment>
   </subsystem>
   ```

4. 将`name`更改为`vanilla.war`：

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
     <secure-deployment name="vanilla.war">
     ...
   </subsystem>
   ```

5. 重新启动应用程序服务器。

6. 转到<http://localhost:8080/vanilla>并单击**Login**。 当Keycloak登录页面打开时，使用您在[创建新用户](https://www.keycloak.org/docs/latest/getting_started/index.html#_create-new-user)中创建的用户登录。

