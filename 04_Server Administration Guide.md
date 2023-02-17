# Server Administration Guide(服务器管理指南)

> 翻译: 白石(https://github.com/wjw465150/KeycloakGuid)
> 原文地址: https://www.keycloak.org/docs/latest/server_admin/index.html

<a name="1___1__概述"></a>
## 1. 概述
Keycloak是针对Web应用程序和RESTful Web服务的单点登录解决方案。 Keycloak的目标是使安全性变得简单，以便应用程序开发人员可以轻松保护他们在组织中部署的应用程序和服务。开发人员通常必须为自己编写的安全功能是开箱即用的，并且可以根据组织的个性化需求轻松定制。 Keycloak为登录，注册，管理和帐户管理提供可定制的用户界面。您还可以使用Keycloak作为集成平台将其挂钩到现有的LDAP和Active Directory服务器中。您还可以将身份验证委派给第三方身份提供商，如Facebook和Google+。

<a name="2____1_1__特征"></a>
### 1.1. 特征

- 用于浏览器应用程序的单点登录和单点登录。
- OpenID Connect支持。
- OAuth 2.0 支持.
- SAML 支持.
- 身份代理 - 使用外部OpenID Connect或SAML身份提供商进行身份验证。
- 社交登录 - 允许使用Google，GitHub，Facebook，Twitter和其他社交网络登录。
- 用户联合 - 从LDAP和Active Directory服务器同步用户。
- Kerberos桥 - 自动验证登录到Kerberos服务器的用户。
- 管理控制台，用于集中管理用户，角色，角色映射，客户端和配置。
- 帐户管理控制台，允许用户集中管理其帐户。
- 主题支持 - 自定义所有面向用户的页面以与您的应用程序和品牌集成。
- 双因素身份验证 - 通过Google身份验证器或FreeOTP支持TOTP / HOTP。
- 登录流程 - 可选用户自行注册，恢复密码，验证电子邮件，需要密码更新等。
- 会话管理 - 管理员和用户自己可以查看和管理用户会话。
- 令牌映射器 - 将用户属性，角色等映射到令牌和语句中。
- 每个领域，应用程序和用户之前的撤销策略。
- CORS支持 - 客户端适配器具有对CORS的内置支持。
- 服务提供者接口（SPI） - 许多SPI，可以自定义服务器的各个方面。身份验证流程，用户联合提供程序，协议映射器等等。
- 适用于JavaScript应用程序，WildFly，JBoss EAP，Fuse，Tomcat，Jetty，Spring等的客户端适配器
- 支持具有OpenID Connect资源提供程序库或SAML 2.0服务提供程序库的任何平台/语言。

<a name="3____1_2__安全是如何工作的_"></a>
### 1.2. 安全是如何工作的?

Keycloak是您在网络上管理的独立服务器。应用程序配置为指向此服务器并受其保护。 Keycloak使用开放协议标准，如[OpenID Connect](https://openid.net/connect/) 或 [SAML 2.0](http://saml.xml.org/saml-specifications)来保护您的应用程序。浏览器应用程序将用户的浏览器从应用程序重定向到Keycloak身份验证服务器，并在其中输入凭据。这很重要，因为用户与应用程序完全隔离，应用程序永远不会看到用户的凭据。相反，应用程序会获得一个加密签名的身份令牌或断言。这些令牌可以包含用户名，地址，电子邮件和其他个人资料数据等身份信息。他们还可以保存权限数据，以便应用程序可以做出授权决策。这些令牌还可用于对基于REST的服务进行安全调用。

<a name="4____1_3__核心概念和术语"></a>
### 1.3. 核心概念和术语

在尝试使用Keycloak保护Web应用程序和REST服务之前，您应该了解一些关键概念和术语。

- users

  用户是能够登录系统的实体。他们可以拥有与自己相关的属性，如电子邮件，用户名，地址，电话号码和出生日。可以为他们分配组成员身份并为其分配特定角色。

- authentication

  识别和验证用户的过程。

- authorization

  授予用户访问权限的过程。

- credentials

  凭据是Keycloak用于验证用户身份的数据。一些示例是密码，一次性密码，数字证书甚至指纹。

- roles

  角色标识用户的类型或类别。 `Admin`，`user`，`manager`和`employee`都是组织中可能存在的典型角色。应用程序通常为特定角色而不是单个用户分配访问权限和权限，因为与用户打交道可能过于细粒度且难以管理。

- user role mapping

  用户角色映射定义角色和用户之间的映射。用户可以与零个或多个角色相关联。可以将此角色映射信息封装到令牌和断言中，以便应用程序可以决定对其管理的各种资源的访问权限。

- composite roles

  复合角色是可以与其他角色关联的角色。例如，`superuser`复合角色可以与`sales-admin`和`order-entry-admin`角色相关联。如果用户被映射到“超级用户”角色，他们也会继承`sales-admin`和`order-entry-admin`角色。

- groups

  组管理用户组。可以为组定义属性。您也可以将角色映射到组。成为组成员的用户将继承组定义的属性和角色映射。

- realms

  领域管理一组用户，凭据，角色和组。用户属于并登录领域。领域彼此隔离，只能管理和验证他们控制的用户。

- clients

  客户端是可以请求Keycloak对用户进行身份验证的实体。大多数情况下，客户端是希望使用Keycloak保护自己并提供单点登录解决方案的应用程序和服务。客户端也可以是只想要请求身份信息或访问令牌的实体，以便他们可以安全地调用网络上由Keycloak保护的其他服务。

- client adapters

  客户端适配器是您安装到应用程序环境中的插件，可以通过Keycloak进行通信和保护。 Keycloak有许多适用于不同平台的适配器，您可以下载。对于我们未涵盖的环境，您还可以获得第三方适配器。

- consent

  同意是指您作为管理员希望用户在客户端参与身份验证过程之前向客户端授予权限。在用户提供其凭证之后，Keycloak将弹出一个屏幕，标识请求登录的客户端以及请求用户的身份信息。用户可以决定是否授予请求。

- client scopes

  注册客户端时，必须为该客户端定义协议映射器和角色范围映射。存储客户端范围通常很有用，通过共享一些常用设置可以更轻松地创建新客户端。这对于根据`scope`参数的值有条件地请求某些声明或角色也很有用。 Keycloak为此提供了客户端范围的概念。

- client role

  客户端可以定义特定于它们的角色。这基本上是专用于客户端的角色命名空间。

- identity token

  提供有关用户身份信息的令牌。 OpenID Connect规范的一部分。

- access token

  可以作为HTTP请求的一部分提供的令牌，用于授予对正在调用的服务的访问权限。这是OpenID Connect和OAuth 2.0规范的一部分。

- assertion

  有关用户的信息。这通常适用于SAML身份验证响应中包含的XML blob，该响应提供有关经过身份验证的用户的身份元数据。

- service account

  每个客户端都有一个内置的服务帐户，允许它获取访问令牌。

- direct grant

  客户端通过REST调用代表用户获取访问令牌的方法。

- protocol mappers

  对于每个客户端，您可以定制在OIDC令牌或SAML断言中存储的声明和断言。您可以通过创建和配置协议映射器为每个客户端执行此操作

- session

  用户登录时，会创建会话以管理登录会话。会话包含的信息，例如用户登录时以及在该会话期间单点登录的参与者。管理员和用户都可以查看会话信息。

- user federation provider

  Keycloak可以存储和管理用户。通常，公司已经拥有存储用户和凭据信息的LDAP或Active Directory服务。您可以指向Keycloak来验证来自这些外部存储的凭据并提取身份信息。

- identity provider

  身份提供者（IDP）是可以对用户进行身份验证的服务。 Keycloak是一名IDP。

- identity provider federation

  Keycloak可以配置为将身份验证委派给一个或多个IDP。通过Facebook或Google+进行社交登录是身份提供商联盟的一个示例。您还可以挂钩Keycloak将身份验证委派给任何其他OpenID Connect或SAML 2.0 IDP。

- identity provider mappers

  在执行IDP联合时，您可以将传入令牌和断言映射到用户和会话属性。这有助于您将身份信息从外部IDP传播到请求身份验证的客户端。

- required actions

  必需操作是用户在身份验证过程中必须执行的操作。在完成这些操作之前，用户将无法完成身份验证过程。例如，管理员可以安排用户每月重置其密码。将为所有这些用户设置“更新密码”所需的操作。

- authentication flows

  认证流程是用户在与系统的某些方面交互时必须执行的工作流程。登录流程可以定义所需的凭证类型。注册流程定义了用户必须输入的配置文件信息以及是否必须使用reCAPTCHA来过滤机器人。凭据重置流定义用户在重置密码之前必须执行的操作。

- events

  事件是管理员可以查看和挂钩的审计流。

- themes

  Keycloak提供的每个屏幕都有一个主题支持。主题定义HTML模板和样式表，您可以根据需要覆盖它们。

<a name="5___2__服务器初始化"></a>
## 2. 服务器初始化

执行[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)中定义的所有安装和配置任务后，您需要创建一个初始管理员帐户。 Keycloak没有开箱即用的任何已配置的管理员帐户。此帐户将允许您创建一个可以登录*master* realm管理控制台的管理员，以便您可以开始创建领域，用户并注册要由Keycloak保护的应用程序。

如果您的服务器可以从`localhost`访问，则可以通过转到`http://localhost:8080/auth`URL来启动它并创建此管理员用户。

欢迎页面

![initial welcome page](assets/initial-welcome-page.png)

只需为此初始管理员指定所需的用户名和密码即可。

如果您无法通过`localhost`地址访问服务器，或者只想从命令行配置Keycloak，则可以使用`... /bin/add-user-keycloak`脚本执行此操作。

add-user-keycloak 脚本

![add user script](assets/add-user-script.png)

根据您是使用独立操作模式还是域操作模式，参数稍有不同。对于独立模式，以下是使用脚本的方式。

Linux/Unix

```bash
$ .../bin/add-user-keycloak.sh -r master -u <username> -p <password>
```

Windows

```
> ...\bin\add-user-keycloak.bat -r master -u <username> -p <password>
```

对于域模式，您必须使用`-sc`开关将脚本指向其中一个服务器主机。

Linux/Unix

```
$ .../bin/add-user-keycloak.sh --sc domain/servers/server-one/configuration -r master -u <username> -p <password>
```

Windows

```
> ...\bin\add-user-keycloak.bat --sc domain/servers/server-one/configuration -r master -u <username> -p <password>
```

<a name="6___3__管理控制台"></a>
## 3. 管理控制台

您的大部分管理任务将通过Keycloak管理控制台完成。 您可以直接转到`http://localhost:8080/auth/admin/`的控制台URL

登录页面

![login page](assets/login-page.png)

输入您在欢迎页面上创建的用户名和密码，或者在bin目录中输入`add-user-keycloak`脚本。 这将带您进入Keycloak管理控制台。

管理控制台

![admin console](assets/admin-console.png)

左下拉菜单允许您选择要管理的领域或创建新领域。 右下拉菜单允许您查看用户帐户或注销。 如果您对管理控制台中的某个功能，按钮或字段感到好奇，只需将鼠标悬停在任何问号`？`图标上。 这将弹出工具提示文本以描述您感兴趣的控制台区域。上图显示了工具提示的实际操作。

<a name="7____3_1__Master_领域"></a>
### 3.1. Master 领域

当你第一次启动Keycloak时，Keycloak会为你创建一个预先定义的领域。 这个初始领域是*master*领域。 它是领域等级中的最高级别。 此领域中的管理员帐户具有查看和管理在服务器实例上创建的任何其他领域的权限。 定义初始管理员帐户时，可以在*master*域中创建帐户。 您最初登录管理控制台也将通过*master*领域。

我们建议您不要使用* master *领域来管理组织中的用户和应用程序。 保留使用*master*realm 给 * super * admins来创建和管理系统中的领域。 遵循此安全模型有助于防止意外更改，并遵循允许用户帐户仅访问成功完成其当前任务所需的权限和权限的传统。

可以禁用*master*领域，并在您创建的每个新领域中定义管理员帐户。 每个领域都有自己的专用管理控制台，您可以使用本地帐户登录。 本指南在[Dedicated Realm Admin Consoles](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions)章节中详细介绍了这一点。

<a name="8____3_2__创造一个新领域"></a>
### 3.2. 创造一个新领域

创建一个新领域非常简单。 将鼠标悬停在标题为`Master`的左上角下拉菜单中。 如果您已登录主域，则此下拉菜单会列出所有已创建的域。 此下拉菜单的最后一个条目始终是`Add Realm`。 单击此按钮可添加领域。

添加领域菜单

![add realm menu](assets/add-realm-menu.png)

此菜单选项将带您进入`Add Realm` 页面。 指定要定义的域名，然后单击`Create`按钮。 或者，您可以导入定义新领域的JSON文档。 我们将在[导出和导入](https://www.keycloak.org/docs/latest/server_admin/index.html#_export_import)章节中详细介绍这一点。

创建领域

![create realm](assets/create-realm.png)

创建领域后，您将返回主管理控制台页面。 现在的领域现在将设置为您刚刚创建的领域。 您可以通过在左上角的下拉菜单中执行鼠标操作来切换管理不同领域。

<a name="9____3_3__SSL模式"></a>
### 3.3. SSL模式

每个领域都有一个与之关联的SSL模式。 SSL模式定义了与域进行交互的SSL/HTTPS要求。 与领域交互的浏览器和应用程序必须遵守SSL模式定义的SSL/HTTPS要求，否则将不允许它们与服务器交互。

> Keycloak在第一次运行时生成自签名证书。 请注意，自签名证书不安全，只能用于测试目的。 强烈建议您在Keycloak服务器本身或Keycloak服务器前面的反向代理上安装CA签名证书。 请参阅[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)。

要配置领域的SSL模式，您需要单击`Realm Settings`左侧菜单项并转到`Login`选项卡。

登录选项卡

![login tab](assets/login-tab.png)

 `Require SSL` 选项允许您选择所需的SSL模式。 以下是每种模式的说明：

- external requests

  用户可以在没有SSL的情况下与Keycloak进行交互，只要他们坚持使用`localhost`，`127.0.0.1`，`10.0.x.x`，`192.168.x.x`和`172.16.x.x`等私有IP地址即可。 如果您尝试从非私有IP地址访问没有SSL的Keycloak，您将收到错误。

- none

  Keycloak不需要SSL。 实际上，这只应该在开发时使用，而不想在服务器上配置SSL。

- all requests

  Keycloak要求所有IP地址都使用SSL。

<a name="10____3_4__清除服务器缓存"></a>
### 3.4. 清除服务器缓存

Keycloak将在JVM的限制范围内和/或为其配置的限制内容缓存内存中的所有内容。 如果Keycloak数据库被服务器的REST API或管理控制台范围之外的第三方（即DBA）修改，则内存缓存的某些部分可能是陈旧的。 您可以通过转到`Realm Settings`左侧菜单项，从管理控制台清除外部公钥（外部客户端或身份提供者的公钥，Keycloak通常用于验证特定外部实体的签名）的域缓存，用户缓存或缓存 菜单项和`Cache`选项卡。

缓存选项卡

![cache tab](assets/cache-tab.png)

只需单击要清除的缓存上的`clear`按钮即可。

<a name="11____3_5__电子邮件设置"></a>
### 3.5. 电子邮件设置

Keycloak向用户发送电子邮件以验证他们的电子邮件地址，忘记密码时，或者管理员需要接收有关服务器事件的通知。 要启用Keycloak发送电子邮件，您需要向Keycloak提供SMTP服务器设置。 这是根据领域配置的。 转到`Realm Settings`左侧菜单项，然后单击`Email`选项卡。

电子邮件选项卡

![email tab](assets/email-tab.png)

- Host

  `Host`表示用于发送电子邮件的SMTP服务器主机名。

- Port

  `Port`表示SMTP服务器端口。

- From

  `From`表示用于发送电子邮件的`From` SMTP-Header的地址。

- From Display Name

  `From Display Name`允许配置用户友好的电子邮件地址别名（可选）。 如果没有设置，普通的`From`电子邮件地址将显示在电子邮件客户端中。

- Reply To

  `Reply To`表示用于发送邮件的`Reply-To`SMTP-Header的地址（可选）。 如果没有设置，将使用普通的`From`电子邮件地址。

- Reply To Display Name

  `Reply To Display Name`允许配置用户友好的电子邮件地址别名（可选）。 如果没有设置，将显示普通的`Reply To`电子邮件地址。

- Envelope From

  `Envelope From`表示[Bounce Address](https://en.wikipedia.org/wiki/Bounce_address) ，用于发送邮件的`Return-Path`SMTP-Header（可选）。

由于电子邮件用于恢复用户名和密码，因此建议使用SSL或TLS，尤其是在SMTP服务器位于外部网络上时。 要启用SSL，请单击`Enable SSL`或启用TLS，单击`Enable TLS`。 您很可能还需要更改`Port`（SSL/TLS的默认端口是465）。

如果您的SMTP服务器需要身份验证，请单击 `Enable Authentication`并插入`Username`和`Password`。

<a name="12____3_6__主题与国际化"></a>
### 3.6. 主题与国际化

主题允许您更改Keycloak中任何UI的外观。 每个领域都配置了主题。 要更改主题，请转到`Realm Settings`左侧菜单项，然后单击`Themes`选项卡。

Themes 选项卡

![themes tab](assets/themes-tab.png)

为每个UI类别选择所需的主题，然后单击`Save`。

- Login Theme

  用户名密码输入，OTP输入，新用户注册以及与登录相关的其他类似屏幕。

- Account Theme

  每个用户都有一个用户帐户管理UI。

- Admin Console Theme

  Keycloak管理控制台的外观。

- Email Theme

  每当Keycloak发送电子邮件时，它都会使用此主题中定义的模板来制作电子邮件。

[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/) 介绍了如何创建新主题或修改现有主题。

<a name="13_____3_6_1__国际化"></a>
#### 3.6.1. 国际化
每个UI屏幕都在Keycloak中国际化。 默认语言是英语，但是如果打开`Theme`选项卡上的`Internationalization`开关，您可以选择要支持的语言环境以及默认语言环境。 用户下次登录时，他们将能够在登录页面上选择一种语言，用于登录屏幕，用户帐户管理UI和管理控制台。 [服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)介绍了如何提供其他语言。

<a name="14___4__用户管理"></a>
## 4. 用户管理

本节介绍管理用户的管理功能。

<a name="15____4_1__搜索用户"></a>
### 4.1. 搜索用户

如果您需要管理特定用户，请单击左侧菜单栏中的`Users`。

Users

![users](assets/users.png)

此菜单选项将您带到用户列表页面。 在搜索框中，您可以在用户数据库中键入要搜索的全名，姓氏或电子邮件地址。 该查询将显示符合您条件的所有用户。 `View all users`按钮将列出系统中的每个用户。 这将仅搜索本地Keycloak数据库而不搜索联合数据库（即LDAP），因为某些后端（如LDAP）无法通过用户进行分页。 因此，如果您希望联合后端的用户同步到Keycloak数据库，您需要：

- 调整搜索条件。 这将只将符合条件的后端用户同步到Keycloak数据库中。
- 转到`User Federation`选项卡，然后单击页面中的`Sync all users`或`Sync changed users`与联合提供程序。

有关详细信息，请参阅[用户联合](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-storage-federation)。

<a name="16____4_2__创建新用户"></a>
### 4.2. 创建新用户

要创建用户，请单击左侧菜单栏中的`Users`。

Users

![users](assets/users.png)

此菜单选项将您带到用户列表页面。 在空用户列表的右侧，您应该看到`Add User`按钮。 单击该按钮开始创建新用户。

Add User

![add user](assets/add-user.png)

唯一必填字段是`Username`。 点击保存。 这将带您进入新用户的管理页面。

<a name="17____4_3__删除用户"></a>
### 4.3. 删除用户

要删除用户，请单击左侧菜单栏中的`Users`。

Users

![users](assets/users.png)

此菜单选项将您带到用户列表页面。 单击`View all users`或搜索以查找要删除的用户。

Add User

![delete user](assets/delete-user.png)

在用户列表中，单击要删除的用户旁边的`Delete`。 系统将要求您确认是否要删除此用户。 单击确认框中的`Delete`进行确认。

<a name="18____4_4__用户属性"></a>
### 4.4. 用户属性

除了名称和电子邮件等基本用户元数据之外，您还可以存储任意用户属性。 选择要管理的用户，然后单击`Attributes`选项卡。

Users

![user attributes](assets/user-attributes.png)

在空字段中输入属性名称和值，然后单击旁边的`Add`按钮以添加新字段。 请注意，在您点击`Save`按钮之前，您在此页面上所做的任何编辑都不会被存储。

<a name="19____4_5__用户凭据"></a>
### 4.5. 用户凭据

当您查看用户时，如果您转到`Credentials`选项卡，则可以管理用户的凭据。

Credential Management

![user credentials](assets/user-credentials.png)

<a name="20_____4_5_1__更改密码"></a>
#### 4.5.1. 更改密码
要更改用户密码，请键入新密码。 在您输入所有内容后，将显示`Reset Password`按钮。如果`Temporary`开关打开，则此新密码只能使用一次，并且用户将被要求更改密码 登录。

或者，如果您设置了[email](https://www.keycloak.org/docs/latest/server_admin/index.html#_email)，则可以向用户发送电子邮件，要求他们重置密码。 从`Reset Actions`列表框中选择`Update Password`，然后单击`Send Email`。 您可以选择设置电子邮件链接的有效性，该链接默认为领域设置中`Tokens`选项卡中预设的一个。 发送的电子邮件包含一个链接，用于将用户带到更新密码屏幕。

<a name="21_____4_5_2__修改_OTPs"></a>
#### 4.5.2. 修改 OTPs
您无法在管理控制台中为特定用户配置一次性密码。 这是用户的责任。 如果用户丢失了他们的OTP生成器，你可以在`Credentials`选项卡上为它们禁用OTP。 如果您的领域中的OTP是可选的，则用户必须转到用户帐户管理服务以重新配置新的OTP生成器。 如果需要OTP，则会要求用户在登录时重新配置新的OTP生成器。

与密码一样，您也可以向用户发送电子邮件，要求他们重置OTP生成器。 在`Reset Actions`列表框中选择`Configure OTP`，然后单击`Send Email`按钮。 发送的电子邮件包含一个链接，用于将用户带到OTP设置屏幕。

<a name="22____4_6__必需操作"></a>
### 4.6. 必需操作

必需操作是用户在允许登录之前必须完成的任务。用户必须在执行所需操作之前提供其凭据。 完成所需操作后，用户将不必再次执行操作。 以下是一些内置必需操作类型的说明：

- Update Password

  设置后，用户必须更改其密码。

- Configure OTP

  设置后，用户必须使用Free OTP或Google Authenticator应用程序在其移动设备上配置一次性密码生成器。

- Verify Email

  设置后，用户必须验证他们是否拥有有效的电子邮件帐户。 将向用户发送一封电子邮件，其中包含他们必须单击的链接。 成功完成此工作流后，将允许他们登录。

- Update Profile

  该要求的动作要求用户更新他们的简档信息，即他们的姓名，地址，电子邮件和/或电话号码。

管理员可以在管理控制台的用户`Details`选项卡中为每个用户添加所需的操作。

Setting Required Action

![user required action](assets/user-required-action.png)

在`Required User Actions`列表框中，选择要添加到帐户的所有操作。 如果要删除一个，请单击操作名称旁边的`X`。 还要记得在确定要添加的操作后单击`Save`按钮。

<a name="23_____4_6_1__默认必需操作"></a>
#### 4.6.1. 默认必需操作
您还可以指定在创建新用户时将添加到帐户的必需操作，即通过用户列表屏幕的`Add User`按钮，或通过[用户注册](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-registration) 登录页面上的链接。 要指定默认的必需操作，请转到`Authentication`左侧菜单项，然后单击`Required Actions`选项卡。

Default Required Actions

![default required actions](assets/default-required-actions.png)

只需单击全新用户登录时要执行的所需操作的`Default Action`列中的复选框即可。

<a name="24_____4_6_2__条款和条件"></a>
#### 4.6.2. 条款和条件
许多组织都要求当新用户第一次登录时，他们需要同意网站的条款和条件。 Keycloak将此功能实现为必需的操作，但它需要一些配置。 首先，您必须转到前面描述的`Required Actions`选项卡并启用`Terms and Conditions`操作。 您还必须编辑*base* login主题中的*terms.ftl*文件。 有关扩展和创建主题的更多信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/) 。

<a name="25____4_7__模拟"></a>
### 4.7. 模拟

管理员冒充用户通常很有用。 例如，用户可能在您的某个应用程序中遇到错误，并且管理员可能想要冒充用户以查看他们是否可以复制问题。 具有适当权限的管理员可以模拟用户。 管理员可以通过两个位置发起模拟。 第一个是在`Users`列表选项卡上。

Users

![user search](assets/user-search.png)

您可以在此处看到管理员已搜索`john`。 在John的帐户旁边，您可以看到一个模拟按钮。 单击该按钮以模拟用户。

此外，您可以从用户`Details`选项卡模拟用户。

User Details

![user details](assets/user-details.png)

在页面底部附近，您可以看到`Impersonate`按钮。 单击该按钮以模拟用户。

模拟时，如果管理员和用户处于同一领域，则管理员将被注销并在用户被模拟时自动登录。 如果管理员和用户不在同一领域，管理员将保持登录状态，但另外以该用户领域中的用户身份登录。 在这两种情况下，浏览器都将重定向到模拟用户的“用户帐户管理”页面。

具有领域`impersonation`角色的任何用户都可以模拟用户。 有关分配管理权限的更多详细信息，请参阅[管理控制台访问控制](https://www.keycloak.org/docs/latest/server_admin/index.html#_admin_permissions)一章。

<a name="26____4_8__用户注册"></a>
### 4.8. 用户注册

您可以启用Keycloak以允许用户自行注册。 启用后，登录页面会有一个注册链接，用户可以单击该链接以创建新帐户。

启用用户自注册后，可以使用注册表单来检测有效的用户名和电子邮件。 也可以启用[reCAPTCHA支持](https://www.keycloak.org/docs/latest/server_admin/index.html#_recaptcha)。

启用注册非常简单。 转到`Realm Settings`左侧菜单并单击它。 然后转到`Login`选项卡。 此选项卡上有一个`User Registration`开关。 打开它，然后单击`Save`按钮。

Login Tab

![login tab](assets/login-tab.png)

启用此设置后，`Register`链接应显示在登录页面上。

Registration Link

![registration link](assets/registration-link.png)

单击此链接将使用户进入注册页面，他们必须在其中输入一些用户配置文件信息和新密码。

Registration Form

![registration form](assets/registration-form.png)

您可以更改注册表单的外观，以及删除或添加必须输入的其他字段。 有关详细信息，请参阅[Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="27_____4_8_1__reCAPTCHA_支持"></a>
#### 4.8.1. reCAPTCHA 支持

为了防止机器人注册，Keycloak与Google reCAPTCHA集成。 要启用此功能，您需要先访问[Google Recaptcha网站](https://developers.google.com/recaptcha/)并创建API密钥，以便获取reCAPTCHA网站密钥和密钥。 （仅供参考，localhost默认工作，因此您不必指定域）。

接下来，您需要在Keycloak管理控制台中执行几个步骤。 单击`Authentication`左侧菜单项，然后转到`Flows`选项卡。 从此页面的下拉列表中选择`Registration`流程。

Registration Flow

![registration flow](assets/registration-flow.png)

单击相应的单选按钮，将`reCAPTCHA`要求设置为`Required`。 这将在屏幕上启用reCAPTCHA。 接下来，您必须输入您在Google reCAPTCHA网站上生成的reCAPTCHA网站密钥和密码。 单击reCAPTCHA流条目右侧的`Actions`按钮，然后单击“Config”链接，并在此配置页面上输入reCAPTCHA站点密钥和秘密。

Recaptcha Config Page

![recaptcha config](assets/recaptcha-config.png)

您需要做的最后一步是更改Keycloak设置的一些默认HTTP响应标头。 Keycloak将阻止网站在iframe中包含任何登录页面。 这是为了防止点击劫持攻击。 您需要授权Google在iframe中使用注册页面。 转到`Realm Settings`左侧菜单项，然后转到`Security Defenses`选项卡。 您需要将`https://www.google.com`添加到`X-Frame-Options`和`Content-Security-Policy`标头的值中。

Authorizing Iframes

![security headers](assets/security-headers.png)

完成此操作后，reCAPTCHA应显示在您的注册页面上。 您可能希望在登录主题中编辑*register.ftl*，以便使用reCAPTCHA按钮的放置和样式进行清理。 有关扩展和创建主题的更多信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="28___5__登录页面设置"></a>
## 5. 登录页面设置

如果您需要这些功能，可以启用几个很好的内置登录页面功能。

<a name="29____5_1__忘记密码"></a>
### 5.1. 忘记密码

如果您启用它，用户可以在忘记密码或丢失OTP生成器时重置其凭据。 转到`Realm Settings`左侧菜单项，然后单击`Login`选项卡。 打开`Forgot Password`开关。

Login Tab

![login tab](assets/login-tab.png)

`forgot password`链接现在将显示在您的登录页面上。

Forgot Password Link

![forgot password link](assets/forgot-password-link.png)

点击此链接会将用户带到一个页面，用户可以在其中输入用户名或电子邮件，并收到一封电子邮件，其中包含重置其凭据的链接。

Forgot Password Page

![forgot password page](assets/forgot-password-page.png)

电子邮件中发送的文本是完全可配置的。 您只需要扩展或编辑与之关联的主题。 有关详细信息，请参阅[Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)。

当用户点击电子邮件链接时，系统会要求他们更新密码，如果他们设置了OTP生成器，他们也会被要求重新配置。 根据组织的安全要求，您可能不希望用户能够通过电子邮件重置其OTP生成器。 您可以通过转到`Authentication`左侧菜单项，单击`Flows`选项卡，然后选择`Reset Credentials`流来改变这种行为：

Reset Credentials Flow

![reset credentials flow](assets/reset-credentials-flow.png)

如果你不想重置OTP，那么只需选择`Reset OTP`右侧的`disabled`单选按钮。

<a name="30____5_2__记住账号"></a>
### 5.2. 记住账号

如果登录用户关闭了他们的浏览器，他们的会话将被销毁，他们将不得不再次登录。 您可以进行设置，以便在用户选中*remember me*复选框时，即使浏览器已关闭，它们仍将保持登录状态。 这基本上将登录cookie从仅会话cookie转换为持久性cookie。

要启用此功能，请转到`Realm Settings`左侧菜单项，然后单击`Login`选项卡并打开`Remember Me`switch`:

Login Tab

![login tab](assets/login-tab.png)

保存此设置后，将在领域的登录页面上显示`remember me`复选框。

Remember Me

![remember me](assets/remember-me.png)

<a name="31___6__认证"></a>
## 6. 认证

在为领域配置身份验证时，您应该注意一些功能。 许多组织都有严格的密码和OTP策略，您可以通过管理控制台中的设置强制执行这些策略。 您可能希望也可能不希望要求不同的凭据类型进行身份验证。 您可能希望为用户提供通过Kerberos登录或禁用或启用各种内置凭据类型的选项。 本章涵盖所有这些主题。

<a name="32____6_1__密码策略"></a>
### 6.1. 密码策略

创建的每个新领域都没有与之关联的密码策略。 用户可以根据需要拥有尽可能短，复杂，不安全的密码。 简单的设置适用于开发或学习Keycloak，但在生产环境中是不可接受的。 Keycloak拥有一组丰富的密码策略，您可以通过管理控制台启用这些策略。

单击`Authentication`左侧菜单项，然后转到`Password Policy`选项卡。 在右侧下拉列表框中选择要添加的策略。 这将在屏幕上的表中添加策略。 选择策略的参数。 点击`Save`按钮存储您的更改。

Password Policy

![password policy](assets/password-policy.png)

保存策略后，用户注册和“需要更新密码”操作将强制执行新策略。 用户未通过策略检查的示例：

Failed Password Policy

![failed password policy](assets/failed-password-policy.png)

如果更新了密码策略，则必须为每个用户设置“更新密码”操作。 自动触发器被安排为未来的增强功能。

<a name="33_____6_1_1__密码策略类型"></a>
#### 6.1.1. 密码策略类型
以下是每种策略类型的说明：

- HashAlgorithm

  密码不会以明文形式存储。 相反，它们在存储或验证之前使用标准哈希算法进行哈希处理。 唯一可用的内置和默认算法是PBKDF2。 有关如何插入自己的算法，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。 请注意，如果您确实更改了算法，则密码哈希值在用户登录时才会在存储中更改。

- Hashing Iterations

  此值指定在存储或验证密码之前对密码进行哈希处理的次数。 默认值为20,000。 这种散列是在黑客获取密码数据库的极少数情况下完成的。 一旦他们可以访问数据库，他们就可以对用户密码进行反向工程。 随着CPU功率的提高，该参数的行业推荐值每年都在变化。 较高的散列迭代值需要更多的CPU功率进行散列，并且可能会影响性能。 您必须权衡对您更重要的事情：性能或保护您的密码存储。 可能有更具成本效益的方法来保护您的密码存储。

- Digits

  密码字符串中需要的位数。

- Lowercase Characters

  密码字符串中需要的小写字母数。

- Uppercase Characters

  密码字符串中需要大写字母的数量。

- Special Characters

  需要在密码字符串中包含`?!#%$`等特殊字符的数量。

- Not Username

  设置后，密码不允许与用户名相同。

- Regular Expression

  定义一个或多个密码必须匹配的Perl正则表达式模式。

- Expire Password

  密码有效的天数。 在天数过期后，用户需要更改其密码。

- Not Recently Used

  此策略保存以前密码的历史记录。 存储的旧密码数量是可配置的。 当用户更改其密码时，他们无法使用任何存储的旧密码。

- Password Blacklist

  此策略检查给定密码是否包含在黑名单文件中，该文件可能是一个非常大的文件。 密码黑名单是带有Unix行结尾的UTF-8纯文本文件，其中每行代表一个列入黑名单的密码。 必须提供黑名单文件的文件名作为密码策略值，例如`10_million_password_list_top_1000000.txt`。 黑名单文件默认解析为`${jboss.server.data.dir}/password-blacklists/`。 可以通过`keycloak.password.blacklists.path`系统属性或`passwordBlacklist`策略SPI配置的`blacklistsPath`属性来定制此路径。

<a name="34____6_2__OTP_策略"></a>
### 6.2. OTP 策略

Keycloak有许多政策可以为FreeOTP或Google身份验证器一次性密码生成器设置。 单击`Authentication`左侧菜单项，然后转到`OTP Policy`选项卡。

OTP Policy

![otp policy](assets/otp-policy.png)

您在此处设置的任何策略都将用于验证一次性密码。 配置OTP时，FreeOTP和Google Authenticator可以扫描在Keycloak所具有的OTP设置页面上生成的QR码。 条形码也是从`OTP Policy`选项卡上配置的信息生成的。

<a name="35_____6_2_1__TOTP_vs__HOTP"></a>
#### 6.2.1. TOTP vs. HOTP
您的OTP生成器有两种不同的算法可供选择。 基于时间（TOTP）和基于计数器（HOTP）。 对于TOTP，您的令牌生成器将散列当前时间和共享密钥。 服务器通过将特定时间窗口内的所有哈希值与提交的值进行比较来验证OTP。 因此，TOTP仅在短时间内（通常为30秒）有效。 对于HOTP，使用共享计数器而不是当前时间。 服务器会在每次成功的OTP登录时递增计数器。 因此，有效的OTP仅在成功登录后才会更改。

TOTP被认为更安全，因为匹配的OTP仅在短时间内有效，而HOTP的OTP可以在不确定的时间内有效。 HOTP更加用户友好，因为用户在时间间隔结束之前不必急于进入他们的OTP。 随着Keycloak实施TOTP的方式，这种区别变得更加模糊。 每次服务器想要递增计数器时，HOTP都需要更新数据库。 当负载很重时，这可能会导致身份验证服务器的性能下降。 因此，为了提供更有效的替代方案，TOTP不记得使用的密码。 这绕过了进行任何数据库更新的需要，但缺点是TOTP可以在有效时间间隔内重复使用。 对于Keycloak的未来版本，计划您能够配置TOTP是否在时间间隔内检查较旧的OTP。

<a name="36_____6_2_2__TOTP配置选项"></a>
#### 6.2.2. TOTP配置选项
- OTP Hash Algorithm

  默认值为SHA1，更安全的选项是SHA256和SHA512。

- Number of Digits

  OTP有多少个字符？ Short表示用户更友好，因为用户输入的内容较少。 更多意味着更多安全。

- Look Ahead Window

  服务器尝试匹配哈希的前方间隔数是多少？ 这样做只是为了防止TOTP生成器或认证服务器的时钟不同步。 默认值1通常足够好。 例如，如果新令牌的时间间隔是每30秒，则默认值1表示它只接受该30秒窗口中的有效令牌。 此配置值的每次递增都会使有效窗口增加30秒。

- OTP令牌时间段

  服务器匹配哈希的时间间隔（以秒为单位）。 每次间隔通过时，令牌生成器将生成新的TOTP。

<a name="37_____6_2_3__HOTP配置选项"></a>
#### 6.2.3. HOTP配置选项
- OTP Hash Algorithm

  默认值为SHA1，更安全的选项是SHA256和SHA512。

- Number of Digits

  OTP有多少个字符？ Short表示用户更友好，因为用户输入的内容较少。 更多意味着更多安全。

- Look Ahead Window

  如果服务器尝试匹配哈希，前面有多少个计数器？ 默认值为1.这是为了涵盖用户计数器超前于服务器的情况。 这通常会发生，因为用户经常意外地手动递增计数器太多次。 这个值确实应该增加到10左右。

- Initial Counter

  初始计数器的价值是多少？

<a name="38____6_3__身份验证流程"></a>
### 6.3. 身份验证流程

*身份验证流程*是用于登录，注册和其他Keycloak工作流程期间必须执行的所有身份验证，屏幕和操作的容器。 如果您转到管理控制台`Authentication`左侧菜单项并转到`Flows`选项卡，您可以查看系统中所有已定义的流以及每个流所需的操作和检查。 本节将介绍浏览器登录流程。 在左侧下拉列表中选择`browser`进入如下界面：

Browser Flow

![browser flow](assets/browser-flow.png)

如果将鼠标悬停在流选择列表右侧的工具提示（小问号）上，则会描述流程的作用和作用。

`Auth Type`列是要执行的身份验证或操作的名称。 如果验证是缩进的，则意味着它在子流中，并且可能会也可能不会执行，具体取决于其父级的行为。 `Requirement`列是一组单选按钮，用于定义操作是否执行。 让我们描述每个单选按钮的含义：

- Required

  此身份验证执行必须成功执行。 如果用户未配置该类型的身份验证机制，并且存在与该身份验证类型关联的必需操作，则会将所需操作附加到该帐户。 例如，如果将`OTP Form`切换为`Required`，则会要求未配置OTP生成器的用户执行此操作。

- Optional

  如果用户配置了身份验证类型，则会执行该身份验证。 否则，它将被忽略。

- Disabled

  如果禁用，则不执行身份验证类型。

- Alternative

  这意味着至少有一种备用身份验证类型必须在该流级别成功执行。

这在一个例子中有更好的描述。 让我们来看看`browser`认证流程。

1. 第一种身份验证类型是`Cookie`。 当用户第一次成功登录时，会设置会话cookie。 如果已设置此cookie，则此身份验证类型成功。 由于cookie提供程序返回成功，并且此流级别的每次执行都是*alternative*，因此不会执行其他执行，这会导致成功登录。
2. 接下来，该流程将查看Kerberos执行情况。 默认情况下禁用此身份验证器，将跳过此身份验证器。
3. 下一个执行是一个名为Forms的子流。 由于此子流标记为*alternative*，如果传递了`Cookie`身份验证类型，则不会执行该子流。 此子流包含需要执行的其他身份验证类型。 加载此子流的执行并发生相同的处理逻辑
4. Forms子流中的第一个执行是用户名密码表单。 此身份验证类型呈现用户名和密码页面。 它标记为*required*，因此用户必须输入有效的用户名和密码。
5. 下一次执行是OTP表格。 这标记为*optional*。 如果用户已设置OTP，则此身份验证类型必须运行并成功。 如果用户未设置OTP，则忽略此身份验证类型。

<a name="39____6_4__执行"></a>
### 6.4. 执行
可以使用执行

脚本身份验证器

*script(脚本)*认证器允许通过JavaScript定义自定义的认证逻辑。自定义的身份验证器。为了使用这个功能，它必须显式启用:


```bash
bin/standalone.sh|bat -Dkeycloak.profile.feature.scripts=enabled
```

有关详细信息，请参阅[配置文件](https://www.keycloak.org/docs/6.0/server_installation/#profiles)部分。

身份验证脚本必须至少提供以下功能之一:`authenticate(..)`调用自`Authenticator#authenticate(AuthenticationFlowContext)` `action(..)` ，这是从`Authenticator#action(AuthenticationFlowContext)`调用的。

自定义`Authenticator`至少应该提供`authenticate(..)`函数。 以下脚本`javax.script.Bindings`可以在脚本代码中方便地使用。

- `script`

  用于访问脚本元数据的`ScriptModel`

- `realm`

  the `RealmModel`

- `user`

  当前的`UserModel`

- `session`

  活跃的`KeycloakSession`

- `authenticationSession`

  当前的`AuthenticationSessionModel`

- `httpRequest`

  当前的 `org.jboss.resteasy.spi.HttpRequest`

- `LOG`

  一个`org.jboss.logging.Logger`作用于`ScriptBasedAuthenticator`

请注意，可以从传递给`authenticate(context)` `action(context)`函数的`context`参数中提取其他上下文信息。

```java
AuthenticationFlowError = Java.type("org.keycloak.authentication.AuthenticationFlowError");

function authenticate(context) {

  LOG.info(script.name + " --> trace auth for: " + user.username);

  if (   user.username === "tester"
      && user.getAttribute("someAttribute")
      && user.getAttribute("someAttribute").contains("someValue")) {

      context.failure(AuthenticationFlowError.INVALID_USER);
      return;
  }

  context.success();
}
```

<a name="40____6_5__Kerberos"></a>
### 6.5. Kerberos

Keycloak支持通过SPNEGO协议使用Kerberos票证登录。 SPNEGO（简单和受保护的GSSAPI协商机制）用于在登录其会话后对用户进行身份验证后通过Web浏览器进行透明身份验证。 对于非Web案例或在登录期间无法使用票证时，Keycloak还支持使用Kerberos用户名/密码登录。

Web身份验证的典型用例如下：

1. 用户登录到他的桌面（例如Active Directory域中的Windows计算机或启用了Kerberos集成的Linux计算机）。
2. 然后，用户使用他的浏览器(IE / Firefox / Chrome)访问由Keycloak保护的Web应用程序。
3. 应用程序重定向到Keycloak登录。
4. Keycloak将HTML登录屏幕与状态401和HTTP标题`WWW-Authenticate: Negotiate`一起呈现
5. 如果浏览器具有来自桌面登录的Kerberos票证，它会将桌面登录信息传输到标题`Authorization: Negotiate 'spnego-token'`中的Keycloak。 否则它只显示登录屏幕。
6. Keycloak验证来自浏览器的令牌并验证用户身份。 它从LDAP中提供用户数据（如果LDAPFederationProvider具有Kerberos身份验证支持），或者让用户更新其配置文件和预填充数据（如果是KerberosFederationProvider）。
7. Keycloak返回应用程序。 Keycloak和应用程序之间的通信通过OpenID Connect或SAML消息进行。 Keycloak通过Kerberos进行身份验证的事实对应用程序是隐藏的。 所以Keycloak充当Kerberos / SPNEGO登录的经纪人。

设置有3个主要部分：

1. Kerberos服务器（KDC）的设置和配置
2. Keycloak服务器的设置和配置
3. 客户端计算机的设置和配置

<a name="41_____6_5_1__Kerberos服务器的设置"></a>
#### 6.5.1. Kerberos服务器的设置
这取决于平台。 确切的步骤取决于您将要使用的操作系统和Kerberos供应商。 有关如何设置和配置Kerberos服务器的详细信息，请参阅Windows Active Directory，MIT Kerberos和操作系统文档。

至少你需要：

- 将一些用户主体添加到Kerberos数据库。 您还可以将Kerberos与LDAP集成，这意味着将从LDAP服务器配置用户帐户。

- 添加“HTTP”服务的服务主体。 例如，如果您的Keycloak服务器将在`www.mydomain.org`上运行，您可能需要添加主体`HTTP/www.mydomain.org@MYDOMAIN.ORG`，假设MYDOMAIN.ORG将成为您的Kerberos领域。

  例如，在MIT Kerberos上，您可以运行“kadmin”会话。 如果您在MIT Kerberos所在的同一台机器上，您只需使用以下命令：

```bash
sudo kadmin.local
```

然后添加HTTP主体并将其密钥导出到keytab文件，其命令如下：

```bash
addprinc -randkey HTTP/www.mydomain.org@MYDOMAIN.ORG
ktadd -k /tmp/http.keytab HTTP/www.mydomain.org@MYDOMAIN.ORG
```

需要在运行Keycloak服务器的主机上访问Keytab文件`/tmp/http.keytab`。

<a name="42_____6_5_2__Keycloak服务器的设置和配置"></a>
#### 6.5.2. Keycloak服务器的设置和配置
您需要在计算机上安装kerberos客户端。 这也取决于平台。 如果您使用的是Fedora，Ubuntu或RHEL，则可以安装包含Kerberos客户端和其他几个实用程序的`freeipa-client`软件包。 配置kerberos客户端（在Linux上，它在文件`/etc/krb5.conf`中）。 您需要放置Kerberos领域，并至少配置您的服务器将运行的HTTP域。 对于示例领域MYDOMAIN.ORG，您可以像这样配置`domain_realm`部分：

```properties
[domain_realm]
  .mydomain.org = MYDOMAIN.ORG
  mydomain.org = MYDOMAIN.ORG
```

接下来，您需要使用HTTP主体导出keytab文件，并确保该文件可供运行Keycloak服务器的进程访问。 对于生产来说，如果它只是通过这个过程而不是其他人可读，那么它是理想的。 对于上面的MIT Kerberos示例，我们已经将keytab导出到`/tmp/http.keytab`。 如果您的KDC和Keycloak在同一主机上运行，则您已经可以使用该文件。

<a name="43______启用SPNEGO处理"></a>
##### 启用SPNEGO处理
Keycloak默认情况下没有打开SPNEGO协议支持。 因此，您必须转到[浏览器流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_authentication-flows) 并启用`Kerberos`。

Browser Flow

![browser flow](assets/browser-flow.png)

将`Kerberos`要求从*disabled*切换到*alternative*或*required*。 *Alternative *基本上意味着Kerberos是可选的。 如果用户的浏览器尚未配置为使用SPNEGO/Kerberos，则Keycloak将回退到常规登录屏幕。 如果将需求设置为*required*，则所有用户必须为其浏览器启用Kerberos。

<a name="44______配置Kerberos用户存储联合提供程序"></a>
##### 配置Kerberos用户存储联合提供程序
现在，在身份验证服务器上打开了SPNEGO协议，您需要配置Keycloak如何解释Kerberos票证。 这是通过[用户存储联合](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-storage-federation)完成的。 我们有2个不同的联合提供程序，支持Kerberos身份验证。

如果要使用LDAP服务器支持的Kerberos进行身份验证，则必须先配置[LDAP Federation Provider](https://www.keycloak.org/docs/latest/server_admin/index.html#_ldap)。 如果查看LDAP提供程序的配置页面，您将看到`Kerberos Integration`部分。

LDAP Kerberos Integration

![ldap kerberos](assets/ldap-kerberos.png)

打开`Allow Kerberos authentication`开关将使Keycloak使用Kerberos主体查找有关用户的信息，以便将其导入Keycloak环境。

如果LDAP服务器不支持您的Kerberos解决方案，则必须使用`Kerberos`用户存储联合提供程序。 转到`User Federation`左侧菜单项，然后从`Add provider`选择框中选择`Kerberos`。

Kerberos User Storage Provider

![kerberos provider](assets/kerberos-provider.png)

此提供程序解析Kerberos票证以获取简单的主体信息，并对本地Keycloak数据库进行少量导入。 不提供姓名、姓氏和电子邮件等用户概要信息。

<a name="45_____6_5_3__客户端计算机的设置和配置"></a>
#### 6.5.3. 客户端计算机的设置和配置
客户端需要安装kerberos客户端并如上所述设置krb5.conf。 此外，他们还需要在浏览器中启用SPNEGO登录支持。 如果您使用的是该浏览器，请参阅[为Kerberos配置Firefox](http://www.microhowto.info/howto/configure_firefox_to_authenticate_using_spnego_and_kerberos.html)。 必须在`network.negotiate-auth.trusted-uris`配置选项中允许URI` .mydomain.org`。

在Windows域中，客户端通常不需要配置任何特殊内容，因为IE已经能够参与Windows域的SPNEGO身份验证。

<a name="46_____6_5_4__示例设置"></a>
#### 6.5.4. 示例设置
为了便于使用Kerberos进行测试，我们提供了一些示例设置进行测试。

<a name="47______Keycloak_and_FreeIPA_docker_镜像"></a>
##### Keycloak and FreeIPA docker 镜像
安装[docker](https://www.docker.com/)后，可以运行装有FreeIPA服务器的docker镜像。 FreeIPA提供集成的安全解决方案，包括MIT Kerberos和389 LDAP服务器等。 该镜像还提供了使用LDAP联合提供程序配置的Keycloak服务器，并针对FreeIPA服务器启用了SPNEGO/Kerberos身份验证。 详见[此处](https://github.com/mposolda/keycloak-freeipa-docker/blob/master/README.md)。

<a name="48______ApacheDS测试Kerberos服务器"></a>
##### ApacheDS测试Kerberos服务器
对于快速测试和单元测试，我们使用非常简单的[ApacheDS](http://directory.apache.org/apacheds/) Kerberos服务器。 您需要从源代码构建Keycloak，然后使用我们的测试套件中的maven-exec-plugin运行Kerberos服务器。 详见[此处](https://github.com/keycloak/keycloak/blob/master/docs/tests.md#kerberos-server) 。

<a name="49_____6_5_5__凭证授权"></a>
#### 6.5.5. 凭证授权
Kerberos 5支持凭证委派的概念。 在这种情况下，您的应用程序可能希望访问Kerberos票证，以便它们可以重新使用它与Kerberos保护的其他服务进行交互。 由于SPNEGO协议是在Keycloak服务器中处理的，因此您必须在OpenID Connect令牌声明或从Keycloak服务器传输到您的应用程序的SAML断言属性中将GSS凭据传播到您的应用程序。 要将此声明插入到令牌或断言中，每个应用程序都需要启用名为`gss delegation credential`的内置协议映射器。 这在应用程序客户端页面的`Mappers`选项卡中启用。 有关详细信息，请参阅[协议映射器](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers)一章。

应用程序需要对从Keycloak收到的声明进行反序列化，然后才能使用它对其他服务进行GSS调用。 将凭证从访问令牌反序列化到GSSCredential对象后，需要创建GSSContext，并将此凭据传递给方法`GSSManager.createContext`，例如：

```java
// Obtain accessToken in your application.
KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) servletReq.getUserPrincipal();
AccessToken accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();

// Retrieve kerberos credential from accessToken and deserialize it
String serializedGssCredential = (String) accessToken.getOtherClaims().
    get(org.keycloak.common.constants.KerberosConstants.GSS_DELEGATION_CREDENTIAL);

GSSCredential deserializedGssCredential = org.keycloak.common.util.KerberosSerializationUtils.
    deserializeCredential(serializedGssCredential);

// Create GSSContext to call other kerberos-secured services
GSSContext context = gssManager.createContext(serviceName, krb5Oid,
    deserializedGssCredential, GSSContext.DEFAULT_LIFETIME);
```

我们有一个例子，详细说明了这一点。 它位于Keycloak示例分发或演示分发下载中的`examples/kerberos`中。 您也可以直接[这里](https://github.com/keycloak/keycloak/tree/master/examples/kerberos)查看示例源。

请注意，您还需要在`krb5.conf`文件中配置`forwardable`kerberos票证，并在您的浏览器中添加对委派凭据的支持。

> 凭据授权具有一些安全隐患，因此只有在您真正需要时才使用它。 强烈建议将它与HTTPS一起使用。 有关详细信息，请参阅[本文](http://www.microhowto.info/howto/configure_firefox_to_authenticate_using_spnego_and_kerberos.html)。

<a name="50_____6_5_6__跨领域的信任"></a>
#### 6.5.6. 跨领域的信任
在Kerberos V5协议中，`realm`是Kerberos数据库（通常是LDAP服务器）中定义的一组Kerberos主体。 Kerberos协议具有跨领域信任的概念。 例如，如果有2个kerberos域A和B，则跨域信任将允许来自域A的用户访问域B的资源（服务）。这意味着域B信任域A.

Kerberos cross-realm trust

![kerberos trust basic](assets/kerberos-trust-basic.png)

Keycloak服务器支持跨领域信任。 要实现这一目标，有几件事情需要做：

- 为跨领域信任配置Kerberos服务器。 此步骤取决于所使用的具体Kerberos服务器实现。 通常，需要将Kerberos主体`krbtgt/B@A`添加到域A和B的Kerberos数据库中。需要此主体在两个Kerberos域上具有相同的键。 这通常在主体具有相同密码，密钥版本号并且在两个领域中使用相同密码时实现。 建议查阅Kerberos服务器文档以获取更多详细信息。

> 默认情况下，跨领域信任是单向的。 如果您希望双向信任使域A也信任域B，则还必须将主体`krbtgt/A@B`添加到两个Kerberos数据库。 但是，默认情况下，信任是可传递的。 如果领域B信任领域A并且领域C信任领域B，那么领域C自动信任领域A而不需要具有可用的主要`krbtgt/C@A`。 在Kerberos客户端上可能需要一些其他配置（例如`capaths`），以便客户端能够找到信任路径。 有关更多详细信息，请参阅Kerberos文档。

- 配置Keycloak服务器
  - 如果您使用具有Kerberos支持的LDAP存储提供程序，则需要为域B配置服务器主体，如下例所示：`HTTP/mydomain.com@B`。 如果您希望领域A中的用户成功通过Keycloak进行身份验证，则LDAP服务器必须能够从领域A中查找用户，因为Keycloak服务器必须能够执行SPNEGO流，然后才能找到用户。 例如，kerberos主要用户`john @ A`必须作为LDAP中的用户在LDAP DN下可用，例如`uid=john,ou=People,dc=example,dc=com`。 如果您希望领域A和B中的用户都进行身份验证，则需要确保LDAP能够从域A和B中查找用户。我们希望在将来的版本中改进此限制，以便您可以创建更多单独的LDAP提供程序 对于单独的领域并确保SPNEGO适用于它们。
  - 如果使用Kerberos用户存储提供程序（通常是没有LDAP集成的Kerberos），则需要将服务器主体配置为`HTTP/mydomain.com@B`，并且来自Kerberos域A和B的用户都应该能够进行身份验证。

> 对于Kerberos用户存储提供程序，建议在kerberos领域中没有冲突用户。 如果存在冲突用户，则它们将映射到相同的Keycloak用户。 这也是我们希望在未来版本中改进的东西，并提供从Kerberos主体到Keycloak用户名的一些更灵活的映射。

<a name="51_____6_5_7__故障排除"></a>
#### 6.5.7. 故障排除
如果您遇到问题，我们建议您启用其他日志记录来调试问题：

- 在管理控制台中为Kerberos或LDAP联合提供程序启用`Debug`标志
- 在`standalone/configuration/standalone.xml`的日志记录部分中为类别`org.keycloak`启用TRACE日志记录，以获得更多信息`standalone/log/server.log`
- 添加系统属性`-Dsun.security.krb5.debug=true` 和 `-Dsun.security.spnego.debug=true`

<a name="52____6_6__X_509客户端证书用户身份验证"></a>
### 6.6. X.509客户端证书用户身份验证

如果服务器配置为进行相互SSL身份验证，Keycloak支持使用X.509客户端证书登录。

典型的工作流程如下：

- 客户端通过SSL/TLS通道发送身份验证请求
- 在SSL/TLS握手期间，服务器和客户端交换其x.509/v3 证书
- 容器(WildFly)验证证书PKIX路径和证书过期
- x.509客户端证书身份验证器验证客户端证书，如下所示：
  - （可选）使用CRL和/或CRL分发点检查证书吊销状态
  - （可选）使用OCSP（在线证书状态协议）检查证书吊销状态
  - （可选）验证证书中的密钥用法是否与预期的密钥用法匹配
  - （可选）验证证书中的扩展密钥用法是否与预期的扩展密钥用法匹配
- 如果上述任何检查失败，则x.509身份验证将失败
- 否则，验证者提取证书身份并将其映射到现有用户
- 证书映射到现有用户后，行为会根据身份验证流程而有所不同：
  - 在浏览器流程中，服务器会提示用户确认身份或忽略身份，而是使用用户名/密码登录
  - 在直接授权流程的情况下，服务器登录用户

<a name="53_____6_6_1__特征"></a>
#### 6.6.1. 特征
- 支持的证书标识源

  使用正则表达式匹配SubjectDNX500主题的电子邮件属性X500来自主题备用名称扩展名的主题电子邮件（RFC822Name通用名称）X500主题备用名称扩展名的主题另一个名称。 这通常是UPN（用户主体名称）X500主题的公共名称attributeMatch IssuerDN使用正则表达式X500颁发者的电子邮件属性X500颁发者的公共名称attributeCertificate序列号

- Regular Expressions

  可以使用正则表达式作为过滤器从主题DN或颁发者DN中提取证书身份。 例如，下面的正则表达式将匹配电子邮件属性：

```javascript
emailAddress=(.*?)(?:,|$)
```

正则表达式过滤仅在`Identity Source`设置为`Match SubjectDN using regular expression`或`Match IssuerDN using regular expression`时适用。

- 将证书身份映射到现有用户

  证书身份映射可以配置为将提取的用户身份映射到现有用户的用户名或电子邮件，或映射到与证书身份匹配的自定义属性。 例如，将`Identity source`设置为 *Subject’s e-mail* 和 `User mapping method`设置为 *Username or email* 将使X.509客户端证书验证者使用证书的主题DN中的电子邮件属性作为 搜索条件，通过用户名或电子邮件查找现有用户。

> 请注意，如果我们在领域设置中禁用`Login with email`，则相同的规则将应用于证书身份验证。 换句话说，用户将无法使用电子邮件属性登录。

- 其他功能：扩展证书验证

  使用 CRL/Distribution 进行CRLRevocation状态检查的撤销状态检查使用 OCSP/Responder 进行分发PointRevocation状态检查URICertificate KeyUsage validationCertificate ExtendedKeyUsage验证

<a name="54_____6_6_2__启用X_509客户端证书用户身份验证"></a>
#### 6.6.2. 启用X.509客户端证书用户身份验证
以下部分介绍如何配置WildFly/Undertow和Keycloak Server以启用X.509客户端证书身份验证。

- 在WildFly中启用相互SSL

  请参阅 [启用SSL](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide-EnableSSL) 和 [SSL](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide) 有关如何在WildFly中启用SSL的说明。打开`KEYCLOAK_HOME/standalone/configuration/standalone.xml`并添加新域：

```xml
<security-realms>
    <security-realm name="ssl-realm">
        <server-identities>
            <ssl>
                <keystore path="servercert.jks"
                          relative-to="jboss.server.config.dir"
                          keystore-password="servercert password"/>
            </ssl>
        </server-identities>
        <authentication>
            <truststore path="truststore.jks"
                        relative-to="jboss.server.config.dir"
                        keystore-password="truststore password"/>
        </authentication>
    </security-realm>
</security-realms>
```

- `ssl/keystore`

  `ssl`元素包含`keystore`元素，该元素定义如何从JKS密钥库加载服务器公钥对

- `ssl/keystore/path`

  JKS密钥库的路径

- `ssl/keystore/relative-to`

  定义密钥库路径相对于的路径

- `ssl/keystore/keystore-password`

  用于打开密钥库的密码

- `ssl/keystore/alias` (可选)

  密钥库中条目的别名。 如果密钥库包含多个条目，请设置它

- `ssl/keystore/key-password` (可选)

  私钥密码，如果与密钥库密码不同。

- `authentication/truststore`

  定义如何加载信任存储以验证入站/出站连接的远程端提供的证书。 通常，信任库包含一组可信CA证书。

- `authentication/truststore/path`

  包含受信任CA证书（证书颁发机构）的JKS密钥库的路径

- `authentication/truststore/relative-to`

  定义信任库路径相对于的路径

- `authentication/truststore/keystore-password`

  用于打开信任库的密码

- Enable https listener

  有关如何在WildFly中启用HTTPS的说明，请参阅[HTTPS侦听器](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide-HTTPSlistener)。添加`<https-listener>`元素，如图所示 下面：

```xml
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
        ....
    <server name="default-server">
            <https-listener name="default"
                        socket-binding="https"
                        security-realm="ssl-realm"
                        verify-client="REQUESTED"/>
    </server>
</subsystem>
```

- `https-listener/security-realm`

  该值必须与上一节中的域名相匹配

- `https-listener/verify-client`

  如果设置为`REQUESTED`，服务器将可选择请求客户端证书。 如果没有提供客户端证书，则将该属性设置为`REQUIRED`将使服务器拒绝入站连接。

<a name="55_____6_6_3__将X_509客户端证书身份验证添加到浏览器流"></a>
#### 6.6.3. 将X.509客户端证书身份验证添加到浏览器流
- 选择一个领域，单击Authentication链接，选择“Browser”流程
- 制作内置“Browser”流程的副本。 您可能希望为新流程指定一个独特的名称，即“X.509 Browser”
- 使用下拉列表，选择复制的流程，然后单击“Add execution”
- 使用下拉菜单选择“X509/Validate User Form”，然后单击“Save”

![x509 execution](assets/x509-execution.png)

- 使用向上/向下箭头，通过在“Browser Forms”执行上方移动“X509/Validate Username Form”的顺序来更改它，并将需求设置为“ALTERNATIVE”

![x509 browser flow](assets/x509-browser-flow.png)

- 选择“Bindings”选项卡，找到“Browser Flow”的下拉列表。 从下拉列表中选择新创建的X509浏览器流，然后单击“Save”。

![x509 browser flow bindings](assets/x509-browser-flow-bindings.png)

- 配置X.509客户端证书身份验证

  ![x509 configuration](assets/x509-configuration.png)

- `User Identity Source`

  定义如何从客户端证书中提取用户身份。

- `A regular expression` (可选)

  定义正则表达式以用作过滤器以提取证书标识。 正则表达式必须包含单个组。

- `User Mapping Method`

  定义如何将证书标识与现有用户匹配。 *Username or e-mail*将通过用户名或电子邮件搜索现有用户。 *Custom Attribute Mapper*将搜索具有与证书标识匹配的自定义属性的现有用户。 自定义属性的名称是可配置的。

- `A name of user attribute` (可选)

  一个自定义属性，该值将与证书标识匹配。

- `CRL Checking Enabled` (可选)

  定义是否使用证书吊销列表检查证书的吊销状态。

- `Enable CRL Distribution Point to check certificate revocation status` (可选)

  定义是否使用CDP检查证书吊销状态。 大多数PKI机构都在其证书中包含CDP。

- `CRL file path` (可选)

  定义包含CRL列表的文件的路径。 如果启用了`CRL Checking Enabled`选项，则该值必须是有效文件的路径。

- `OCSP Checking Enabled`(可选)

  定义是否使用在线证书状态协议检查证书吊销状态。

- `OCSP Responder URI` (可选)

  允许覆盖证书中OCSP响应者URI的值。

- `Validate Key Usage` (可选)

  验证是否设置了证书的KeyUsage扩展位。 例如，“digitalSignature,KeyEncipherment”将验证KeyUsage扩展中的位0和2是否被断言。 将参数保留为空以禁用密钥用法验证。 参见[RFC5280, Section-4.2.1.3](https://tools.ietf.org/html/rfc5280#section-4.2.1.3)。 仅当颁发CA标记为关键时，服务器才会引发错误，并且密钥使用扩展名不匹配。

- `Validate Extended Key Usage` (可选)

  验证扩展密钥用法扩展中定义的一个或多个目的。 参见 [RFC5280, Section-4.2.1.12](https://tools.ietf.org/html/rfc5280#section-4.2.1.12)。 将参数保留为空以禁用扩展密钥用法验证。 仅当颁发CA标记为关键时，服务器才会引发错误，并且密钥使用扩展名不匹配。

- `Bypass identity confirmation`

  如果设置，X.509客户端证书身份验证将不会提示用户确认证书身份，并将在成功身份验证后自动登录用户。

<a name="56_____6_6_4__将X_509客户端证书身份验证添加到直接授权流程"></a>
#### 6.6.4. 将X.509客户端证书身份验证添加到直接授权流程
- 使用Keycloak管理控制台，单击“Authentication”并选择“Direct Grant”流程，
- 制作内置“Direct Grant”流程的副本。 您可能想给新流程一个独特的名称，即“X509 Direct Grant”，
- 删除“Username Validation”和“Password”验证者，
- 单击“Add execution”并添加“X509/Validate Username”并单击“Save”以将执行步骤添加到父流程。

![x509 directgrant execution](assets/x509-directgrant-execution.png)

- 将`Requirement`更改为*REQUIRED*。

![x509 directgrant flow](assets/x509-directgrant-flow.png)

- 按照前面x.509浏览器流程部分中描述的步骤设置x509身份验证配置。
- 选择“Bindings”选项卡，找到“Direct Grant Flow”的下拉列表。 从下拉列表中选择新创建的X509直接授权流程，然后单击“Save”。

![x509 directgrant flow bindings](assets/x509-directgrant-flow-bindings.png)

<a name="57_____6_6_5__客户端证书查找"></a>
#### 6.6.5. 客户端证书查找
当HTTP请求直接发送到Keycloak服务器时，WildFly undertow 子系统将建立SSL握手并提取客户端证书。 然后，客户端证书将保存到HTTP请求的属性`javax.servlet.request.X509Certificate`中，如servlet规范中所指定。 然后，Keycloak X509身份验证器将能够从此属性中查找证书。

但是，当Keycloak服务器侦听负载均衡器或反向代理后面的HTTP请求时，它可能是代理服务器，它提取客户端证书并建立相互SSL连接。 反向代理通常将经过身份验证的客户端证书放入基础请求的HTTP标头中，并将其转发到后端Keycloak服务器。 在这种情况下，Keycloak必须能够从HTTP标头而不是HTTP请求的属性中查找X.509证书链，就像Undertow所做的那样。

如果Keycloak位于反向代理之后，通常需要在`KEYCLOAK_HOME/standalone/configuration/standalone.xml`中配置`x509cert-lookup` SPI的备用提供程序。 除了从HTTP头查找证书的`default`提供程序外，我们还有两个额外的内置提供程序：`haproxy`和`apache`，下面将对其进行描述。

<a name="58______HAProxy证书查找提供程序"></a>
##### HAProxy证书查找提供程序
当Keycloak服务器位于HAProxy反向代理后面时，您可以使用此提供程序。 像这样配置服务器：

```xml
<spi name="x509cert-lookup">
    <default-provider>haproxy</default-provider>
    <provider name="haproxy" enabled="true">
        <properties>
            <property name="sslClientCert" value="SSL_CLIENT_CERT"/>
            <property name="sslCertChainPrefix" value="CERT_CHAIN"/>
            <property name="certificateChainLength" value="10"/>
        </properties>
    </provider>
</spi>
```

在此示例配置中，将从HTTP标头`SSL_CLIENT_CERT`中查找客户端证书，并从HTTP标头中查找其链中的其他证书，如`CERT_CHAIN_0`，`CERT_CHAIN_1`，...，`CERT_CHAIN_9`。 属性`certificateChainLength`是链的最大长度，因此最后一个尝试的属性将是`CERT_CHAIN_9`。

有关如何配置客户端证书和客户端证书链的HTTP标头及其专有名称的详细信息，请参阅[HAProxy文档](http://www.haproxy.org/#docs)。

<a name="59______Apache证书查找提供程序"></a>
##### Apache证书查找提供程序
当Keycloak服务器位于Apache反向代理后面时，您可以使用此提供程序。 像这样配置服务器：

```xml
<spi name="x509cert-lookup">
    <default-provider>apache</default-provider>
    <provider name="apache" enabled="true">
        <properties>
            <property name="sslClientCert" value="SSL_CLIENT_CERT"/>
            <property name="sslCertChainPrefix" value="CERT_CHAIN"/>
            <property name="certificateChainLength" value="10"/>
        </properties>
    </provider>
</spi>
```

配置与`haproxy`提供程序相同。 请参阅[mod_ssl](https://httpd.apache.org/docs/current/mod/mod_ssl.html)和[mod_headers](https://httpd.apache.org/docs/current/mod/mod_headers.html)上的有关如何配置客户端证书和客户端证书链的HTTP标头及其专有名称的详细信息。

<a name="60______Nginx证书查找提供程序"></a>
##### Nginx证书查找提供程序
当Keycloak服务器位于Nginx反向代理后面时，您可以使用此提供程序。 像这样配置服务器：

```xml
<spi name="x509cert-lookup">
    <default-provider>nginx</default-provider>
    <provider name="nginx" enabled="true">
        <properties>
            <property name="sslClientCert" value="ssl-client-cert"/>
            <property name="sslCertChainPrefix" value="USELESS"/>
            <property name="certificateChainLength" value="2"/>
        </properties>
    </provider>
</spi>
```

> NGINX [SSL/TLS module](http://nginx.org/en/docs/http/ngx_http_ssl_module.html#variables)不公开客户端证书链，因此Keycloak NGINX证书查找提供程序正在使用[Keycloak truststore](https://www.keycloak.org/docs/6.0/server_installation/#_truststore)重建它。 请使用keytool CLI填充Keycloak信任库，其中包含重建客户端证书链所需的所有根CA和中间CA.

有关如何配置客户端证书的HTTP标头的详细信息，请参阅NGINX文档。 NGINX配置文件示例：

```json
 ...
 server {
    ...
    ssl_client_certificate                  trusted-ca-list-for-client-auth.pem;
    ssl_verify_client                       optional_no_ca;
    ssl_verify_depth                        2;
    ...
    location / {
      ...
      proxy_set_header ssl-client-cert        $ssl_client_escaped_cert;
      ...
    }
    ...
}
```

> 必须将trusted-ca-list-for-client-auth.pem中的所有证书添加到 [Keycloak truststore](https://www.keycloak.org/docs/6.0/server_installation/#_truststore)。

<a name="61______其他反向代理实现"></a>
##### 其他反向代理实现
我们没有内置支持其他反向代理实现。 但是，可以使其他反向代理以与`apache`或`haproxy`类似的方式运行，并且可以使用其中一些提供程序。 如果这些都不起作用，您可能需要创建自己的`org.keycloak.services.x509.X509ClientCertificateLookupFactory`和`org.keycloak.services.x509.X509ClientCertificateLookup`提供程序的实现。 有关如何添加自己的提供程序的详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="62_____6_6_6__Troubleshooting"></a>
#### 6.6.6. Troubleshooting
- 转储HTTP标头

  如果要查看反向代理发送给Keycloak的内容，只需激活[RequestDumpingHandler](https://mirocupak.com/logging-requests-with-undertow/)并查阅`server.log`文件。

- 在日志记录子系统下启用TRACE日志记录

```xml
...
    <profile>
        <subsystem xmlns="urn:jboss:domain:logging:3.0">
...
            <logger category="org.keycloak.authentication.authenticators.x509">
                <level name="TRACE"/>
            </logger>
            <logger category="org.keycloak.services.x509">
                <level name="TRACE"/>
            </logger>
WARNING: Don't use RequestDumpingHandler or TRACE logging in production.
```

- 使用X.509直接授予身份验证

  以下模板可用于使用资源所有者密码凭据授权请求令牌：

```bash
$ curl https://[host][:port]/auth/realms/master/protocol/openid-connect/token \
       --insecure \
       --data "grant_type=password&scope=openid profile&username=&password=&client_id=CLIENT_ID&client_secret=CLIENT_SECRET" \
       -E /path/to/client_cert.crt \
       --key /path/to/client_cert.key
```

- `[host][:port]`

  已配置为允许用户使用Direct Grant Flow对x.509客户端证书进行身份验证的远程Keycloak服务器的主机和端口号。

- `CLIENT_ID`

  客户端ID。

- `CLIENT_SECRET`

  对于机密客户，客户机密; 否则，留空。

- `client_cert.crt`

  公钥证书，用于在相互SSL身份验证中验证客户端的身份。 证书应采用PEM格式。

- `client_cert.key`

  公钥对中的私钥。 也期望以PEM格式。

<a name="63___7__SSO协议"></a>
## 7. SSO协议

本章简要概述了身份验证协议以及Keycloak身份验证服务器及其保护的应用程序如何与这些协议进行交互。

<a name="64____7_1__OpenID_连接"></a>
### 7.1. OpenID 连接

[OpenID Connect](https://openid.net/connect/) (OIDC)是一种身份验证协议，是 [OAuth 2.0](https://tools.ietf.org/html/rfc6749)的扩展。 虽然OAuth 2.0只是构建授权协议的框架，但主要是不完整的，OIDC是一种成熟的身份验证和授权协议。 OIDC还大量使用 [Json Web Token](https://jwt.io/) (JWT) 标准集。 这些标准定义了身份令牌JSON格式以及以紧凑和Web友好的方式对数据进行数字签名和加密的方法。

使用OIDC时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到 *identity token* 和 *access token*。 *identity token*包含有关用户的信息，例如用户名，电子邮件和其他个人资料信息。 *access token*由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取*access token*，它可以代表用户在其他远程服务上调用。 Keycloak对用户进行身份验证，然后要求用户同意授予访问请求它的客户端的权限。 然后客户端接收*access token*。 此*access token*由领域进行数字签名。 客户端可以使用此*access token*在远程服务上进行REST调用。 REST服务提取*access token*，验证令牌的签名，然后根据令牌内的访问信息决定是否处理请求。

<a name="65_____7_1_1__OIDC_Auth_Flows"></a>
#### 7.1.1. OIDC Auth Flows
OIDC有不同的方式让客户端或应用程序对用户进行身份验证并接收*identity*和*access*令牌。 您使用的路径在很大程度上取决于请求访问的应用程序或客户端的类型。 所有这些流程都在OIDC和OAuth 2.0规范中进行了描述，因此这里仅提供简要概述。

<a name="66______授权代码流程"></a>
##### 授权代码流程
这是一个基于浏览器的协议，我们建议您使用它来验证和授权基于浏览器的应用程序。 它大量使用浏览器重定向来获取*identity*和*access*令牌。 这是一个简短的总结：

1. 浏览器访问应用, 应用程序注意到用户未登录，因此它将浏览器重定向到Keycloak进行身份验证。 应用程序传递回调URL（重定向URL）作为此浏览器重定向中的查询参数，Keycloak将在完成身份验证时使用该重定向。
2. Keycloak对用户进行身份验证，并创建一次性的，非常短暂的临时代码。 Keycloak使用前面提供的回调URL重定向回应用程序，另外还将临时代码作为查询参数添加到回调URL中。
3. 应用程序提取临时代码并将带外REST调用的后台调用到Keycloak，以交换*identity*，*access*和*refresh* token的代码。 一旦使用此临时代码一次获取令牌，就永远不能再使用它。 这可以防止潜在的重播攻击。

重要的是要注意*access*令牌通常是短暂的，并且经常在几分钟后过期。 登录协议传输的附加*refresh*令牌允许应用程序在到期后获取新的访问令牌。 此刷新协议在受损系统的情况下非常重要。 如果访问令牌是短暂的，则整个系统仅在访问令牌的生命周期中容易受到被盗令牌的攻击。 如果管理员已撤销访问权限，则未来的刷新令牌请求将失败。 这使事情更安全，更具可扩展性。

此流程的另一个重要方面是*public* 与 *confidential*客户端的概念。 *Confidential*客户在交换令牌的临时代码时需要提供客户机密。 *Public*客户不需要提供此客户机密钥。只要严格执行HTTPS并且您对为客户端注册的重定向URI非常严格，*Public*客户端就完全没问题。 HTML5/JavaScript客户端必须始终是*public*客户端，因为无法以安全的方式将客户端密钥传输给它们。 再次，只要您使用HTTPS并严格执行重定向URI注册，这就没问题。 本指南在[管理客户端](https://www.keycloak.org/docs/latest/server_admin/index.html#_clients)章节中详细介绍了这一点。

Keycloak还支持可选的[代码交换证明密钥](https://tools.ietf.org/html/rfc7636)规范。

<a name="67______隐式流"></a>
##### 隐式流
这是一种基于浏览器的协议，类似于授权代码流，除了涉及的请求较少且没有刷新令牌。 我们不建议使用此流程，因为由于重定向URI（见下文）传递令牌，因此在浏览器历史记录中可能会泄漏*access*令牌。 此外，由于此流程不向客户端提供刷新令牌，因此访问令牌必须是长期存在的，或者用户在过期时必须重新进行身份验证。 支持此流程，因为它符合OIDC和OAuth 2.0规范。 以下是协议的简短摘要：

1. 浏览器访问应用 应用程序注意到用户未登录，因此它将浏览器重定向到Keycloak进行身份验证。 应用程序传递回调URL（重定向URL）作为此浏览器重定向中的查询参数，Keycloak将在完成身份验证时使用该重定向。
2. Keycloak对用户进行身份验证并创建*identity*和*access*令牌。 Keycloak使用前面提供的回调URL重定向回应用程序，并在回调URL中另外添加*identity*和*access* tokens作为查询参数。
3. 应用程序从回调URL中提取*identity*和*access*标记。

<a name="68______资源所有者密码凭据授予__直接访问授予_"></a>
##### 资源所有者密码凭据授予 (直接访问授予)
这在管理控制台中称为*Direct Access Grants*。 这是由希望代表用户获取令牌的REST客户端使用的。 它是一个HTTP POST请求，包含用户的凭据以及客户端的ID和客户端的秘密（如果它是机密客户端）。 用户的凭据在表单参数内发送。 HTTP响应包含*identity*，*access*和*refresh* tokens。

<a name="69______客户凭证授权"></a>
##### 客户凭证授权
REST客户端也使用它，但不是获取代表外部用户工作的令牌，而是根据与客户端关联的服务帐户的元数据和权限创建令牌。 更多信息和示例在[服务帐户](https://www.keycloak.org/docs/latest/server_admin/index.html#_service_accounts)章节中。

<a name="70_____7_1_2__Keycloak_Server_OIDC_URI端点"></a>
#### 7.1.2. Keycloak Server OIDC URI端点
这是Keycloak发布的OIDC端点列表。 如果您使用非Keycloak客户端适配器与OIDC与auth服务器通信，这些URL非常有用。 这些都是相对URL，URL的根是HTTP(S)协议，主机名，通常以*/auth*为前缀的路径：即`https://localhost:8080/auth`

- `/realms/{realm-name}/protocol/openid-connect/token`

  这是用于在授权代码流中获取临时代码或通过隐式流，直接授权或客户端授权获取令牌的URL端点。

- `/realms/{realm-name}/protocol/openid-connect/auth`

  这是授权代码流将临时代码转换为令牌的URL端点。

- `/realms/{realm-name}/protocol/openid-connect/logout`

  这是执行注销的URL端点。

- `/realms/{realm-name}/protocol/openid-connect/userinfo`

  这是OIDC规范中描述的用户信息服务的URL端点。

在所有这些中，将*{realm-name}*替换为领域的名称。

<a name="71____7_2__SAML"></a>
### 7.2. SAML

[SAML 2.0](http://saml.xml.org/saml-specifications) 是与OIDC类似的规范，但是更老，更成熟。 它的根源在于SOAP和过多的WS-*规范，所以它往往比OIDC更冗长。 SAML 2.0主要是一种身份验证协议，通过在身份验证服务器和应用程序之间交换XML文档来工作。 XML签名和加密用于验证请求和响应。

使用SAML时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到一个XML文档，其中包含称为SAML断言的内容，该断言指定了有关用户的各种属性。 此XML文档由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取一个SAML断言，它可以代表用户在其他远程服务上调用它。

<a name="72_____7_2_1_SAML绑定"></a>
#### 7.2.1.SAML绑定
SAML定义了在执行身份验证协议时交换XML文档的几种不同方法。 *Redirect*和*Post*绑定涵盖基于浏览器的应用程序。 *ECP*绑定涵盖REST调用。 还有其他绑定类型，但Keycloak只支持这三种。

<a name="73______重定向绑定"></a>
##### 重定向绑定
*Redirect* 绑定 使用一系列浏览器重定向URI来交换信息。 这是它如何工作的粗略概述。

1. 用户访问应用程序，应用程序发现用户未经过身份验证。 它生成XML身份验证请求文档，并将其编码为URI中的查询参数，该URI用于重定向到Keycloak服务器。 根据您的设置，应用程序还可以对此XML文档进行数字签名，并将此签名作为查询参数填充到Keycloak的重定向URI中。 此签名用于验证发送此请求的客户端。
2. 浏览器被重定向到Keycloak。 服务器提取XML身份验证请求文档，并在需要时验证数字签名。 然后，用户必须输入他们的凭证才能进行身份验证。
3. 身份验证后，服务器生成XML身份验证响应文档。 本文档包含一个SAML断言，其中包含有关用户的元数据，如名称，地址，电子邮件以及用户可能拥有的任何角色映射。 该文档几乎总是使用XML签名进行数字签名，也可以加密。
4. 然后将XML身份验证响应文档编码为重定向URI中的查询参数，该重定向URI将浏览器带回应用程序。 数字签名也包括在查询参数中。
5. 应用程序接收重定向URI并提取XML文档并验证领域的签名以确保它正在接收有效的身份验证响应。 然后，SAML断言内的信息用于制定访问决策或显示用户数据。

<a name="74______POST_绑定"></a>
##### POST 绑定
SAML *POST*绑定的工作方式几乎与*Redirect*绑定完全相同，但不是GET请求，而是通过POST请求交换XML文档。 *POST* Binding使用JavaScript来欺骗浏览器在交换文档时向Keycloak服务器或应用程序发出POST请求。 基本上，HTTP响应包含一个HTML文档，其中包含带有嵌入式JavaScript的HTML表单。 加载页面时，JavaScript会自动调用表单。 你真的不需要知道这些东西，但这是一个非常聪明的技巧。

由于安全性和大小限制，通常建议使用*POST*绑定。 使用*REDIRECT*时，SAML响应是URL的一部分（它是之前解释过的查询参数），因此可以在日志中捕获它，并且它被认为不太安全。 关于大小，如果断言包含很多或大的属性，则在HTTP有效负载内发送文档总是比在更有限的URL中更好。

<a name="75______ECP"></a>
##### ECP
ECP代表“Enhanced Client or Proxy(增强客户端或代理)”，SAML v.2.0配置文件，允许在Web浏览器的上下文之外交换SAML属性。 这通常用于REST或基于SOAP的客户端。

<a name="76_____7_2_2__Keycloak_Server_SAML_URI端点"></a>
#### 7.2.2. Keycloak Server SAML URI端点
Keycloak实际上只有一个端点用于所有SAML请求。

```javascript
http(s)://authserver.host/auth/realms/{realm-name}/protocol/saml
```

所有绑定都使用此端点。

<a name="77____7_3__OpenID_Connect_与_SAML"></a>
### 7.3. OpenID Connect 与 SAML

在OpenID Connect和SAML之间进行选择不仅仅是使用更新的协议（OIDC）而不是旧的更成熟的协议（SAML）。

在大多数情况下，Keycloak建议使用OIDC。

SAML往往比OIDC更冗长。

除了交换数据的详细程度之外，如果您比较规范，您会发现OIDC旨在与Web一起工作，同时SAML被改装为在Web上运行。 例如，OIDC也更适合HTML5/JavaScript应用程序，因为它比SAML更容易在客户端实现。 由于令牌采用JSON格式，因此JavaScript更易于使用。 您还将找到一些很好的功能，可以更轻松地在Web应用程序中实现安全性。 例如，查看规范用于轻松确定用户是否仍在登录的[iframe技巧](https://openid.net/specs/openid-connect-session-1_0.html#ChangeNotification)。

SAML虽然有它的用途。 正如您所看到的，OIDC规范的发展，您会发现它们实现了SAML多年来所拥有的越来越多的功能。 我们经常看到人们选择SAML而不是OIDC，因为人们认为它更成熟，也因为他们已经有了现有的应用程序。

<a name="78____7_4__Docker_Registry_v2身份验证"></a>
### 7.4. Docker Registry v2身份验证

> 默认情况下禁用Docker身份验证。 要启用，请参阅  [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles)。

[Docker Registry V2身份验证](https://docs.docker.com/registry/spec/auth/)是一种OIDC-Like协议，用于根据Docker注册表对用户进行身份验证。 Keycloak对此协议的实现允许Docker客户端使用Keycloak身份验证服务器对注册表进行身份验证。 虽然该协议使用相当标准的令牌和签名机制，但它有一些缺点，使其不能被视为真正的OIDC实现。 最大的偏差包括用于请求和响应的非常特定的JSON格式，以及了解如何将存储库名称和权限映射到OAuth范围机制的能力。

<a name="79_____7_4_1__Docker_验证_流程"></a>
#### 7.4.1. Docker 验证 流程
[Docker API文档](https://docs.docker.com/registry/spec/auth/token/) 最好地描述和说明了这个过程，但是下面将从Keycloak认证服务器的角度给出一个简短的总结。

> 此流假定已执行`docker login`命令

- 当Docker客户端从Docker注册表请求资源时，流程开始。 如果资源受到保护且请求中不存在身份验证令牌，则Docker注册服务器将使用401 +响应客户端，获取有关所需权限的信息以及在何处查找授权服务器。
- Docker客户端将根据Docker注册表中的401响应构造一个身份验证请求。 然后，客户端将使用本地缓存的凭据（来自以前运行的`docker login`命令）作为[HTTP基本身份验证](https://tools.ietf.org/html/rfc2617)对Keycloak身份验证服务器的请求的一部分。
- Keycloak身份验证服务器将尝试对用户进行身份验证，并返回包含OAuth样式的Bearer令牌的JSON正文。
- Docker客户端将从JSON响应中获取承载令牌，并在Authorization标头中使用它来请求受保护资源。
- 当Docker注册表使用来自Keycloak服务器的令牌接收受保护资源的新请求时，注册表将验证令牌并授予对所请求资源的访问权限（如果适用）。

<a name="80_____7_4_2__Keycloak_Docker_Registry_v2身份验证服务器URI端点"></a>
#### 7.4.2. Keycloak Docker Registry v2身份验证服务器URI端点
Keycloak实际上只有一个端点用于所有Docker auth v2请求。

```javascript
http(s)://authserver.host/auth/realms/{realm-name}/protocol/docker-v2
```

<a name="81___8__管理客户端"></a>
## 8. 管理客户端

客户端是可以请求用户身份验证的实体。 客户有两种形式。 第一种类型的客户端是想要参与单点登录的应用程序。 这些客户只希望Keycloak为他们提供安全保障。 另一种类型的客户端是请求访问令牌的客户端，以便它可以代表经过身份验证的用户调用其他服务。 本节讨论有关配置客户端的各个方面以及执行此操作的各种方法。

<a name="82____8_1__OIDC_客户端"></a>
### 8.1. OIDC 客户端

[OpenID Connect](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc)是保护应用程序的首选协议。 它的设计从一开始就是Web友好的，并且最适合HTML5/JavaScript应用程序。

要创建OIDC客户端，请转到`Clients`左侧菜单项。 在此页面上，您将看到右侧的`Create`按钮。

Clients

![clients](assets/clients.png)

这将带您进入`Add Client`页面。

Add Client

![add client oidc](assets/add-client-oidc.png)

输入客户端的`Client ID`。 这应该是一个简单的字母数字字符串，将在请求和Keycloak数据库中用于标识客户端。 接下来在`Client Protocol`下拉框中选择`openid-connect`。 最后在`Root URL`字段中输入应用程序的基本URL，然后单击`Save`。 这将创建客户端并将您带到客户端`Settings`选项卡。

Client Settings

![client settings oidc](assets/client-settings-oidc.png)

让我们来看看这个页面上的每个配置项。

**Client ID**

这指定了一个字母数字字符串，该字符串将用作OIDC请求的客户端标识符。

**Name**

这是客户端在Keycloak UI屏幕中显示时的显示名称。 您可以通过设置替换字符串值(即${myapp})来本地化此字段的值。 有关详细信息，请参阅[Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)。

**Description**

这指定了客户端的描述。 这也可以是本地化的。

**Enabled**

如果关闭此选项，则不允许客户端请求身份验证。

**Consent Required**

如果启用此选项，则用户将获得一个同意页面，询问用户是否授予对该应用程序的访问权限。 它还将显示客户端感兴趣的元数据，以便用户确切地知道客户端可以访问哪些信息。 如果您曾经对Google进行过社交登录，那么您通常会看到类似的页面。 Keycloak提供相同的功能。

**Access Type**

这定义了OIDC客户端的类型。

- *confidential*

  机密访问类型适用于需要执行浏览器登录并在将访问代码转换为访问令牌时需要客户端密钥的服务器端客户端（请参阅[访问令牌请求](https://tools.ietf.org/html/rfc6749#section-4.1.3) 有关详细信息，请参阅OAuth 2.0规范）。 此类型应用于服务器端应用程序。

- *public*

  公共访问类型适用于需要执行浏览器登录的客户端客户端。 使用客户端应用程序无法保密。 相反，通过为客户端配置正确的重定向URI来限制访问非常重要。

- *bearer-only*

  仅承载访问类型意味着应用程序仅允许承载令牌请求。 如果启用此选项，则此应用程序无法参与浏览器登录。

**Root URL**

如果Keycloak使用任何已配置的相对URL，则会为其添加此值。

**Valid Redirect URIs**

这是一个必填字段。 输入网址格式，然后点击要添加的`+`号。 点击要删除的网址旁边的`-`符号。 请记住，您仍然需要单击`Save`按钮！ 通配符(\*)仅允许在URI的末尾，例如: `http://host.com/*`

注册有效的重定向URI模式时，应采取额外的预防措施。 如果你使它们过于笼统，你很容易受到攻击。 有关详细信息，请参阅[威胁模型缓解](https://www.keycloak.org/docs/latest/server_admin/index.html#_unspecific-redirect-uris) 一章。

**Base URL**

如果Keycloak需要链接到客户端，则使用此URL。

**Standard Flow Enabled**

如果启用此选项，则允许客户端使用OIDC [授权代码流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows)。

**Implicit Flow Enabled**

如果启用此选项，则允许客户端使用OIDC [隐式流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows)。

**Direct Grants Enabled**

如果启用此选项，则允许客户使用OIDC [Direct Grants](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows)。

**Admin URL**

对于Keycloak特定的客户端适配器，这是客户端的回调端点。 Keycloak服务器将使用此URI进行回调，例如推送撤销策略，执行反向通道注销以及其他管理操作。 对于Keycloak servlet适配器，这可以是servlet应用程序的根URL。 有关详细信息，请参阅[保护应用程序和服务指南](https://www.keycloak.org/docs/6.0/securing_apps/)。

**Web Origins**

该选项以[CORS](http://www.w3.org/TR/cors/)为中心，代表跨源资源共享。 如果浏览器JavaScript尝试向其域与JavaScript代码所来的域不同的服务器发出AJAX HTTP请求，则该请求必须使用CORS。 服务器必须以特殊方式处理CORS请求，否则浏览器将不会显示或允许处理请求。 此协议用于防止XSS，CSRF和其他基于JavaScript的攻击。

Keycloak支持经过验证的CORS请求。 它的工作方式是客户端的`Web Origins`设置中列出的域嵌入发送到客户端应用程序的访问令牌中。 然后，客户端应用程序可以使用此信息来决定是否允许在其上调用CORS请求。 这是OIDC协议的扩展，因此只有Keycloak客户端适配器支持此功能。 有关详细信息，请参阅[保护应用程序和服务指南](https://www.keycloak.org/docs/6.0/securing_apps/)。

要填写`Web Origins`数据，请输入基本URL并单击要添加的`+`号。 点击要删除的网址旁边的`-` 符号。 请记住，您仍然需要单击`Save`按钮！

<a name="83_____8_1_1__高级设置"></a>
#### 8.1.1. 高级设置
**OAuth 2.0 Mutual TLS客户端证书绑定访问令牌**

Mutual TLS使用在TLS握手期间交换的客户端证书绑定访问令牌和刷新令牌。 这可以防止找到窃取这些令牌的方法的攻击者行使令牌。 这种类型的令牌称为持有者令牌。 与承载令牌不同，持有者令牌的接收者可以验证令牌的发送者是否合法。

如果令牌请求满足以下条件，Keycloak将使用客户端证书绑定访问令牌和刷新令牌，并将其作为持有者令牌发布。 如果不满足所有条件，Keycloak将拒绝令牌请求。

- 该功能已打开
- 令牌请求在授权代码流或混合流中发送到令牌端点
- 在TLS握手时，Keycloak请求客户端证书，客户端发送其客户端证书
- 在TLS握手时，Keycloak成功验证了客户端证书

要在Keycloak中启用相互TLS，请参阅[在WildFly中启用相互SSL](https://www.keycloak.org/docs/latest/server_admin/index.html#_enable-mtls-wildfly)。

在以下情况下，Keycloak将验证客户端发送访问令牌或刷新令牌; 如果验证失败，Keycloak拒绝令牌。

- 使用持有者刷新令牌将令牌刷新请求发送到令牌端点
- UserInfo请求通过持有者密钥访问令牌发送到UserInfo端点
- 使用持有者刷新令牌将注销请求发送到Logout端点

请参阅OAuth 2.0 Mutual TLS客户端身份验证和证书绑定访问中的[Mutual TLS客户端证书绑定访问令牌](https://tools.ietf.org/html/draft-ietf-oauth-mtls-08#section-3) 令牌更多细节。

> 警告：目前没有任何keycloak客户端适配器支持持有者令牌验证。 相反，keycloak适配器当前将访问和刷新令牌视为承载令牌。

<a name="84_____8_1_2__机密客户端凭据"></a>
#### 8.1.2. 机密客户端凭据

如果您已在客户端的`Settings`选项卡中将客户端的[访问类型](https://www.keycloak.org/docs/latest/server_admin/index.html#_access-type)设置为`confidential`，则为新的 `Credentials`选项卡将显示出来。 作为处理此类客户端的一部分，您必须配置客户端的凭据。

Credentials Tab

![client credentials](assets/client-credentials.png)

`Client Authenticator`列表框指定您将用于机密客户端的凭据类型。 它默认为客户端ID和秘密。 秘密会自动为您生成，`Regenerate Secret`按钮允许您根据需要或需要重新创建此秘密。

Alternatively, you can opt to use a signed Json Web Token (JWT) or x509 certificate validation (also called Mutual TLS) instead of a secret.

Signed JWT

![client credentials jwt](assets/client-credentials-jwt.png)

选择此凭据类型时，您还必须为客户端生成私钥和证书。 私钥将用于签署JWT，而服务器使用证书来验证签名。 单击`Generate new keys and certificate`按钮以启动此过程。

Generate Keys

![generate client keys](assets/generate-client-keys.png)

当您生成这些密钥时，Keycloak将存储证书，您需要下载私钥和证书供您的客户使用。 选择所需的存档格式，并指定私钥和存储的密码。

您也可以选择通过外部工具生成这些内容，然后只导入客户端证书。

Import Certificate

![import_client_cert](assets/import-client-cert.png)

您可以导入多种格式，只需选择存储证书的存档格式，选择文件，然后单击`Import`按钮。

最后请注意，如果选择`Use JWKS URL`，则甚至不需要导入证书。 在这种情况下，您可以在[JWK](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html)格式中提供客户端发布公钥的URL。 这很灵活，因为当客户更改密钥时，Keycloak会自动下载它们，而无需在Keycloak端重新导入任何内容。

如果您使用由Keycloak适配器保护的客户端，您可以配置JWKS URL，如`<https://myhost.com/myapp/k_jwks>`，假设`<https://myhost.com/myapp>`是客户端应用程序的根URL。 有关其他详细信息，请参阅[Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)。

> 出于性能目的，Keycloak缓存OIDC客户端的公钥。 如果您认为客户端的私钥被泄露，那么更新密钥显然很好，但清除密钥缓存也很好。 有关详细信息，请参阅[清除缓存](https://www.keycloak.org/docs/latest/server_admin/index.html#_clear-cache)部分。

Signed JWT with Client Secret

如果在`Client Authenticator`列表框中选择此选项，则可以使用由客户端密钥签名的JWT而不是私钥。

此客户端密钥将用于由客户端签署JWT。

X509 Certificate

通过启用此选项，Keycloak将验证客户端是否在TLS握手期间使用正确的X509证书。

> 此选项需要Keycloak中的相互TLS，请参阅[在WildFly中启用相互SSL](https://www.keycloak.org/docs/latest/server_admin/index.html#_enable-mtls-wildfly)。

Import Certificate

![x509 client auth](assets/x509-client-auth.png)

验证器还使用配置的regexp验证表达式检查证书的Subject DN字段。 对于某些用例，接受所有证书就足够了。 在这种情况下，您可以使用 `(.*?)(?:$)` 表达式。

Keycloak有两种方法从请求中获取客户端ID。 第一个选项是查询中的`client_id`参数（在[OAuth 2.0规范]的第2.2节(https://tools.ietf.org/html/rfc6749)中描述）。 第二个选项是提供`client_id`作为查询参数。

<a name="85_____8_1_3__服务帐户"></a>
#### 8.1.3. 服务帐户

每个OIDC客户端都有一个内置的*service account*，允许它获取访问令牌。 这在[客户端凭据授权]（(https://www.keycloak.org/docs/latest/server_admin/index.html#_client_credentials_grant)下的OAuth 2.0规范中介绍。 要使用此功能，您必须将客户端的[访问类型](https://www.keycloak.org/docs/latest/server_admin/index.html#_access-type)设置为`confidential`。 执行此操作时，将显示`Service Accounts Enabled`开关。 您需要打开此开关。 还要确保已配置[客户端凭据](https://www.keycloak.org/docs/latest/server_admin/index.html#_client-credentials)。

要使用它，您必须注册一个有效的`confidential`客户端，并且您需要在Keycloak管理控制台中检查此客户端的`Service Accounts Enabled`。 在`Service Account Roles`选项卡中，您可以配置代表此客户端检索的服务帐户可用的角色。 请记住，除非您具有`Full Scope Allowed`，否则您必须具有此客户端的角色范围映射（选项卡`Scope`）中可用的角色。 与正常登录一样，访问令牌中的角色是以下内容的交集：

- 特定客户端的角色范围映射与从链接的客户端范围继承的角色范围映射相结合
- 服务帐户角色

要调用的REST URL是`/auth/realms/{realm-name}/protocol/openid-connect/token`。 调用此URL是POST请求，并要求您发布客户端凭据。 默认情况下，客户端凭据由`Authorization: Basic`标头中的客户端的clientId和clientSecret表示，但您也可以使用签名的JWT断言或任何其他自定义机制对客户端进行身份验证来验证客户端。 您还需要根据OAuth2规范使用参数`grant_type=client_credentials`。

例如，用于检索服务帐户的POST调用可能如下所示：

```bash
    POST /auth/realms/demo/protocol/openid-connect/token
    Authorization: Basic cHJvZHVjdC1zYS1jbGllbnQ6cGFzc3dvcmQ=
    Content-Type: application/x-www-form-urlencoded

    grant_type=client_credentials
```

响应将来自OAuth 2.0规范中的[标准JSON文档]（(https://tools.ietf.org/html/rfc6749#section-4.4.3)。

```javascript
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
    "access_token":"2YotnFZFEjr1zCsicMWpAA",
    "token_type":"bearer",
    "expires_in":60,
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
    "refresh_expires_in":600,
    "id_token":"tGzv3JOkF0XG5Qx2TlKWIA",
    "not-before-policy":0,
    "session_state":"234234-234234-234234"
}
```

可以通过越界请求刷新或注销检索到的访问令牌。

<a name="86_____8_1_4__受众支持"></a>
#### 8.1.4. 受众支持

部署Keycloak的典型环境通常包括一组*confidential*或*public*客户端应用程序（前端客户端应用程序），它们使用Keycloak进行身份验证。

还有*services*（在OAuth 2规范中称为*Resource Servers*），它服务于来自前端客户端应用程序的请求并提供资源。 这些服务通常需要向其发送*Access token*（Bearer token(承载令牌)）以针对特定请求进行身份验证。 此令牌以前是由前端应用程序尝试登录Keycloak时获得的。

在服务之间的信任度较低的环境中，您可能会遇到以下情况：

1. 名为`my-app`的前端客户端需要针对Keycloak进行身份验证。
2. 用户在Keycloak中进行了身份验证。 Keycloak随后向`my-app`应用程序发出了令牌。
3. 应用程序`my-app`使用令牌来调用服务`evil-service`。 应用程序需要调用`evil-service`，因为服务能够提供一些非常有用的数据。
4. `evil-service`应用程序将响应返回给`my-app`。 但是，与此同时，它保留了先前发送给它的令牌。
5. 然后，`evil-service`应用程序使用先前保存的令牌调用另一个名为`good-service`的服务。 调用成功，`good-service`返回数据。 这导致安全性被破坏，因为`evil-service`使用令牌来代表客户端`my-app`访问其他服务。

在服务之间具有高度信任的许多环境中，该流程可能不是问题。 然而，在服务之间的信任度较低的其他环境中，这可能是有问题的。

> 在某些环境中，此示例工作流可能甚至是请求的行为，因为`evil-service`可能需要从`good-service`检索其他数据，以便能够将请求的数据正确地返回给原始调用者（my-app客户端））。 您可能会注意到Kerberos凭据委派的相似之处。 与Kerberos凭证委派一样，无限受众是一种喜忧参半的祝福，因为它仅在服务之间存在高度信任时才有用。 否则，建议限制观众，如下所述。 你可以限制观众，同时允许`evil-service`从`good-service`中检索所需的数据。 在这种情况下，您需要确保将`evil-service`和`good-service`添加为令牌的受众。

为防止滥用访问令牌，如上例所示，建议限制令牌上的*Audience*并配置您的服务以验证令牌上的受众。 如果这样做，上面的流程将会改变，如下所示：

1. 名为`my-app`的前端客户端需要针对Keycloak进行身份验证。
2. 用户在Keycloak中进行了身份验证。 Keycloak随后向`my-app`应用程序发出了令牌。 客户端应用程序已经知道它将需要调用服务`evil-service`，因此它在发送给Keycloak服务器的身份验证请求中使用了`scope=evil-service`。 有关*scope*参数的更多详细信息，请参见[Client Scopes部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes)。 发给`my-app`客户端的令牌包含受众，如`"audience"：["evil-service"]`，它声明客户端想要使用此访问令牌来仅调用服务`evil-service`。
3. `evil-service`应用程序向`my-app`提供了请求。 同时，它保留了先前发送给它的令牌。
4. 然后，`evil-service`应用程序使用先前保存的令牌调用`good-service`。 调用没有成功，因为`good-service`检查了令牌上的受众，并且它看到受众只是`evil-service`。 这是预期的行为，安全性没有被打破。

如果客户端想要稍后调用`good-service`，则需要通过使用`scope=good-service`发出SSO登录来获取另一个令牌。 然后，返回的令牌将包含`good-service`作为受众：

```javascript
"audience": [ "good-service" ]
```

并可用于调用`good-service`。

<a name="87______设置"></a>
##### 设置
要正确设置受众群体检查：

- 确保通过在适配器配置中添加标志*verify-token-audience*，将服务配置为检查发送给它们的访问令牌的受众。 有关详细信息，请参阅[适配器配置](https://www.keycloak.org/docs/6.0/securing_apps/#_java_adapter_config)。
- 确保当Keycloak发出访问令牌时，它包含所有请求的受众，并且不包含任何不需要的受众。 可以根据[下一节](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_resolve)中描述的客户端角色自动添加受众，也可以按照描述进行硬编码[下文](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_hardcoded)。

<a name="88______自动添加受众群体"></a>
##### 自动添加受众群体
在默认客户端范围*roles*中，定义了*Audience Resolve*协议映射器。 此协议映射器将检查当前令牌至少具有一个可用客户端角色的所有客户端。 然后，每个客户端的客户端ID将自动添加为受众。 如果您的服务（通常仅限于承载）客户端依赖客户端角色，则此功能尤其有用。

举个例子，让我们假设您有一个仅限持有客户端`good-service`和机密客户端`my-app`，您要对其进行身份验证，然后使用为`my-app`发出的访问令牌来 调用`good-service` REST服务。 如果以下情况属实：

- `good-service`客户端有自己定义的任何客户角色
- 目标用户至少分配了一个客户端角色
- 客户端`my-app`具有指定角色的角色范围映射

那么`good-service`将自动作为观众添加到为`my-app`发布的访问令牌中。

> 如果要确保不自动添加受众，请不要直接在`my-app`客户端上配置角色作用域映射，而是创建一个专用的客户端作用域，例如名为`good-service`，它将包含角色`good-service`客户端的客户端角色的范围映射。 假设此客户端作用域将作为可选的客户端作用域添加到`my-app`客户端，则只有在`scope=good-service`参数明确请求时，才会将客户端角色和受众添加到令牌中。

> 前端客户端本身不会自动添加到访问令牌受众。 这允许容易区分访问令牌和ID令牌，因为访问令牌将不包含作为受众发布令牌的客户端。 因此，在上面的示例中，`my-app`不会作为受众添加。 如果您需要客户本身作为受众，请参阅[硬编码的受众](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_hardcoded) 选项。 但是，不建议使用相同的客户端作为前端和REST服务。

<a name="89______硬编码的受众"></a>
##### 硬编码的受众
对于您的服务依赖于领域角色或根本不依赖于令牌中的角色的情况，使用硬编码的受众可能很有用。 这是一个协议映射器，它将指定服务客户端的客户端ID作为标记的受众添加。 如果您需要不同于客户端ID的受众，您甚至可以使用任何自定义值，例如某些URL。

您可以将协议映射器直接添加到前端客户端，但始终会添加受众。 如果您想要更精细的控制，可以在专用的客户端范围上创建协议映射器，例如`good-service`。

受众协议映射器

![audience mapper](assets/audience_mapper.png)

- 从`good-service`客户端的[安装选项卡](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_installation) ，您可以生成适配器配置，您可以确认*verify-token-audience*选项将设置为true。 这表示如果使用此生成的配置，适配器将需要验证受众。
- 最后，您需要确保`my-app`前端客户端能够在其令牌中请求`good-service`作为受众。 在`my-app`客户端上，单击*Client Scopes*选项卡。 然后将`good-service`指定为可选（或默认）客户端范围。 有关详细信息，请参阅[客户端范围链接部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking)。
- 您可以选择[评估客户端范围](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_evaluate)并生成示例访问令牌。 如果这样做，请注意，只有在您将其指定为可选客户端范围的情况下，*scope*参数中包含`good-service`时，才会将`good-service`添加到生成的访问令牌的受众中。
- 在你的`my-app`应用程序中，当你想发出用于访问`good-service`的令牌时，你必须确保*scope*参数与值'good-service`一起使用。 如果您的应用程序使用servlet适配器，请参阅[参数转发部分](https://www.keycloak.org/docs/6.0/securing_apps/#_params_forwarding)，或[javascript adapter section](https://www.keycloak.org/docs/6.0/securing_apps/#_javascript_adapter)，如果您的应用程序使用javascript适配器。

> 如果您不确定令牌中的正确受众和角色是什么，那么[评估客户端范围](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_evaluate)总是一个好主意,在管理控制台中并围绕它进行一些测试。

> *Audience* 和 *Audience Resolve*协议映射器默认情况下仅将访客添加到访问令牌。 ID令牌通常仅包含单个受众，即为其颁发令牌的客户端的客户端ID。 这是OpenID Connect规范的要求。 另一方面，访问令牌不一定具有客户端的客户端ID，该客户端ID是为其颁发的令牌，除非任何观众映射器添加了它。

<a name="90____8_2__SAML_客户端"></a>
### 8.2. SAML 客户端

Keycloak支持[SAML 2.0](https://www.keycloak.org/docs/latest/server_admin/index.html#_saml)用于已注册的应用程序。 POST和Redirect绑定都受支持。 您可以选择要求客户端签名验证，也可以让服务器签名和/或加密响应。

要创建SAML客户端，请转到`Clients`左侧菜单项。 在此页面上，您将看到右侧的`Create`按钮。

Clients

![clients](assets/clients.png)

这将带您进入`Add Client`页面。

Add Client

![add client saml](assets/add-client-saml.png)

输入客户端的`Client ID`。 这通常是一个URL，并且是应用程序发送的SAML请求中预期的`issuer`值。 接下来在`Client Protocol`下拉框中选择`saml`。 最后输入`Client SAML Endpoint` URL。 输入您希望Keycloak服务器向其发送SAML请求和响应的URL。 通常，应用程序只有一个用于处理SAML请求的URL。 如果您的应用程序的绑定具有不同的URL，请不要担心，您可以在客户端的`Settings`选项卡中修复此问题。 点击`Save`。 这将创建客户端并将您带到客户端`Settings`选项卡。

Client Settings

![client settings saml](assets/client-settings-saml.png)

- Client ID

  此值必须与AuthNRequests发送的颁发者值相匹配。 Keycloak将从Authn SAML请求中提取发行者，并通过此值将其与客户端匹配。

- Name

  这是客户端在Keycloak UI屏幕中显示时的显示名称。 您可以通过设置替换字符串值（即${myapp}）来本地化此字段的值。 有关详细信息，请参阅[Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)。

- Description

  这指定了客户端的描述。 这也可以是本地化的。

- Enabled

  如果关闭此选项，则不允许客户端请求身份验证。

- Consent Required

  如果启用此选项，则用户将获得一个同意页面，询问用户是否授予对该应用程序的访问权限。 它还将显示客户端感兴趣的元数据，以便用户确切地知道客户端可以访问哪些信息。 如果您曾经对Google进行过社交登录，那么您通常会看到类似的页面。 Keycloak提供相同的功能。

- Include AuthnStatement

  SAML登录响应可以指定使用的身份验证方法（密码等）以及登录的时间戳。 将此设置为on将在响应文档中包含该语句。

- Sign Documents

  打开时，Keycloak将使用领域的私钥对文档进行签名。

- Optimize REDIRECT signing key lookup

  打开时，SAML协议消息将包含Keycloak本机扩展，其中包含带有签名密钥ID的提示。 当SP理解此扩展时，它可以将其用于签名验证，而不是尝试使用所有已知密钥验证签名。 此选项仅适用于REDIRECT绑定，其中签名在查询参数中传输，其中签名信息中没有此信息的位置（与文档签名中始终包含密钥ID的POST绑定消息相反）。 目前，这与Keycloak服务器和适配器提供IDP和SP的情况相关。 此选项仅在`Sign Documents`打开时有效。

- Sign Assertions

  `Sign Documents`开关标志整个文件。 通过此设置，断言也会被签名并嵌入到SAML XML Auth响应中。

- Signature Algorithm

  选择用于签署SAML文档的各种算法。

- SAML Signature Key Name

  通过POST绑定发送的签名SAML文档包含`KeyName`元素中的签名密钥的标识。 默认情况下，此项包含Keycloak密钥ID。 然而，各种供应商可能期望具有不同的密钥名称或根本没有密钥名称。 此开关控制`KeyName`是否包含密钥ID（选项`KEY_ID`），来自对应于领域密钥的证书（选项`CERT_SUBJECT` - 例如Microsoft Active Directory联合服务预期），或者密钥名称提示是完全的 从SAML消息中省略（选项`NONE`）。

- Canonicalization Method

  XML签名的规范化方法。

- Encrypt Assertions

  使用领域的私钥加密SAML文档中的断言。 AES算法的密钥大小为128位。

- Client Signature Required

  期望来自客户的文档已签名。 Keycloak将使用在`SAML Keys`选项卡中设置的客户端公钥或证书来验证此签名。

- Force POST Binding

  默认情况下，Keycloak将使用原始请求的初始SAML绑定进行响应。 通过打开此开关，即使原始请求是重定向绑定，您也将强制Keycloak始终使用SAML POST绑定进行响应。

- Front Channel Logout

  如果为true，则此应用程序需要浏览器重定向才能执行注销。 例如，应用程序可能需要重置cookie，这只能通过重定向完成。 如果此开关为false，则Keycloak将调用后台SAML请求以注销该应用程序。

- Force Name ID Format

  如果请求具有名称ID策略，请忽略它并使用名称ID格式下管理控制台中配置的值

- Name ID Format

  名称ID主题的格式。 如果请求中未指定名称ID策略，或者“强制名称ID格式”属性为true，则使用此值。 用于每种格式的属性定义如下。

- Root URL

  如果Keycloak使用任何已配置的相对URL，则会为其添加此值。

- Valid Redirect URIs

  这是个可选的选项。 输入网址格式，然后点击要添加的 `+` 号。 点击要删除的网址旁边的 `-` 符号。 请记住，您仍然需要单击`Save`按钮！ 通配符(\*) 仅允许在URI的末尾，即`http://host.com/*`。 如果未注册确切的SAML端点且Keycloak正在从请求中提取断言使用者URL，则使用此字段。

- Base URL

  如果Keycloak需要链接到客户端，则将使用此URL。

- Master SAML Processing URL

  此URL将用于所有SAML请求，并且响应将定向到SP。 它将用作断言使用者服务URL和单一注销服务URL。 如果登录请求包含断言使用者服务URL，则该URL优先，但此URL必须由注册的有效重定向URI模式进行保护

- Assertion Consumer Service POST Binding URL

  断言使用者服务的POST绑定URL。

- Assertion Consumer Service Redirect Binding URL

  重定向断言使用者服务的绑定URL。

- Logout Service POST Binding URL

  注销服务的POST绑定URL。

- Logout Service Redirect Binding URL

  重定向注销服务的绑定URL。

<a name="91_____8_2_1__IDP发起登录"></a>
#### 8.2.1. IDP发起登录

IDP Initiated Login是一项功能，允许您在Keycloak服务器上设置端点，该端点将登录到特定的应用程序/客户端。 在客户端的`Settings`选项卡中，您需要指定`IDP Initiated SSO URL Name`。 这是一个简单的字符串，里面没有空格。 在此之后，您可以通过以下URL引用您的客户端：`root/auth/realms/{realm}/protocol/saml/clients/{url-name}`

IDP发起的登录实现更喜欢*POST* 通过 *REDIRECT* 绑定（检查[saml bindings](https://www.keycloak.org/docs/latest/server_admin/index.html#saml-bindings)以获取更多信息）。 因此，以下列方式选择最终绑定和SP URL：

1. 如果定义了特定的`Assertion Consumer Service POST Binding URL(断言消费者服务POST绑定URL)`（在客户端设置的`Fine Grain SAML Endpoint Configuration`部分内），则通过该URL使用 *POST*绑定。
2. 如果指定了通用的`Master SAML Processing URL(主SAML处理URL)`，则通过此常规URL再次使用*POST*绑定。
3. 作为最后的手段，如果配置了`Assertion Consumer Service Redirect Binding URL(断言消费者服务重定向绑定URL)`（在`Fine Grain SAML Endpoint Configuration(精细粒度SAML端点配置)`中）*REDIRECT*绑定与此URL一起使用。

如果您的客户端需要特殊的中继状态，您也可以在`IDP Initiated SSO Relay State(IDP启动的SSO中继状态)`字段的`Settings`选项卡上进行配置。 或者，浏览器可以在`RelayState`查询参数中指定中继状态，即`root/auth/realms/{realm}/protocol/saml/clients/{url-name}?RelayState=thestate`。

使用[identity brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker)时，可以从外部IDP为客户端设置IDP启动登录。 如上所述，在代理IDP处为IDP启动登录设置实际客户端。 外部IDP必须为应用程序IDP启动登录设置客户端，该客户端将指向指向代理的特殊URL，并代表代理IDP上所选客户端的IDP启动登录端点。 这意味着在外部IDP的客户端设置中：

- `IDP Initiated SSO URL Name`设置为将作为IDP Initiated Login初始点发布的名称，
- `Fine Grain SAML Endpoint Configuration`部分中的`Assertion Consumer Service POST Binding URL(断言消费者服务POST绑定URL)`必须设置为以下URL：`broker-root/auth/realms/{broker-realm}/broker/{idp-name}/endpoint/clients/{client-id}`，其中：
  - *broker-root*是基础代理URL
  - *broker-realm*是声明外部IDP的代理域的域名
  - *idp-name*是经纪人的外部IDP的名称
  - *client-id*是代理处定义的SAML客户端的`IDP Initiated SSO URL Name`属性的值。 正是这个客户端，将从外部IDP用于IDP启动登录。

请注意，您可以将基本客户端设置从代理IDP导入外部IDP的客户端设置 - 只需使用[SP描述符](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker_saml_sp_descriptor) 可以从代理IDP中的身份提供者的设置中获得，并将`clients/*client-id*`添加到端点URL。

<a name="92_____8_2_2__SAML实体描述符"></a>
#### 8.2.2. SAML实体描述符

您可以通过标准SAML实体描述符XML文件导入SAML 2.0客户端，而不是手动注册SAML 2.0客户端。 “添加客户端”页面上有一个`Import`选项。

添加客户端

![add client saml](assets/add-client-saml.png)

单击`Select File`按钮并加载实体描述符文件。 您应该查看那里的所有信息，以确保所有设置都正确。

某些SAML客户端适配器（如*mod-auth-mellon*）需要IDP的XML实体描述符。 您可以通过转到此公共URL来获取此信息：`root/auth/realms/{realm}/protocol/saml/descriptor`

<a name="93____8_3__客户端链接"></a>
### 8.3. 客户端链接

对于想要从一个客户端链接到另一个客户端的场景，Keycloak提供了一个特殊的重定向端点：`/realms/realm_name/clients/{client-id}/redirect`。

如果客户端通过`HTTP GET`请求访问此端点，Keycloak将通过响应的`Location`头以`HTTP 307`（临时重定向）的形式返回所提供的Client和Realm的配置基本URL。

因此，客户端只需知道领域名称和客户端ID即可链接到它们。 此间接有助于避免硬编码客户端基本URL。

例如，给定领域`master`和`client-id`帐户`：

```javascript
http://host:port/auth/realms/master/clients/account/redirect
```

将临时重定向到：`http://host:port/auth/realms/master/account`

<a name="94____8_4__OIDC令牌和SAML断言映射"></a>
### 8.4. OIDC令牌和SAML断言映射

接收ID令牌，访问令牌或SAML断言的应用程序可能需要或想要不同的用户元数据和角色。 Keycloak允许您定义确切传输的内容。 您可以对角色，声明和自定义属性进行硬编码。 您可以将用户元数据拉入令牌或断言。 您可以重命名角色。 基本上你可以很好地控制究竟是什么回到客户端。

在管理控制台中，如果您转到已注册的应用程序，您将看到`Mappers`选项卡。 这是一个基于OIDC的客户端。

Mappers Tab

![mappers oidc](assets/mappers-oidc.png)

新客户端没有任何内置映射器，但它通常从客户端作用域继承一些映射器，如[客户端作用域部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes)中所述。协议映射器将诸如电子邮件地址之类的内容映射到身份和访问令牌中的特定声明。 他们的功能应该从他们的名字中自我解释。 还有一些未附加到客户端的预配置映射器，您可以通过单击`Add Builtin`按钮添加这些映射器。

每个映射器都有常用设置以及其他设置，具体取决于您要添加的映射器类型。 单击列表中其中一个映射器旁边的`Edit`按钮进入配置屏幕。

Mapper Config

![mapper config](assets/mapper-config.png)

了解配置选项的最佳方法是将鼠标悬停在其工具提示上。

大多数OIDC映射器还允许您控制声明的放置位置。 您可以通过摆弄`Add to ID token` 和 `Add to access token` 开关来选择在*id* 和 *access*令牌中包含或排除声明。

最后，您还可以添加其他映射器类型。 如果您返回`Mappers`选项卡，请单击`Create`按钮。

Add Mapper

![add mapper](assets/add-mapper.png)

从列表框中选择`Mapper Type`。 如果将鼠标悬停在工具提示上，您将看到该映射器类型的描述。 将针对不同的映射器类型显示不同的配置参数。

<a name="95_____8_4_1__优先顺序"></a>
#### 8.4.1. 优先顺序
映射器实现具有*priority order(优先级顺序)*。 此优先级顺序不是映射器的配置属性; 相反，它是mapper具体实现的属性。

映射器在管理控制台中按照映射器列表中的顺序进行排序，并且将使用该顺序应用令牌或断言中的更改，并且首先应用最低值。 这意味着依赖于其他实现的实现按所需顺序处理。

例如，当我们首先想要计算将包含在令牌中的角色时，我们首先根据这些角色来解析受众。 然后，我们处理一个JavaScript脚本，该脚本使用令牌中已有的角色和受众。

<a name="96_____8_4_2__OIDC用户会话记录映射器"></a>
#### 8.4.2. OIDC用户会话记录映射器
用户会话详细信息是通过映射器，并取决于各种标准。 在客户端上使用或启用功能时，将自动包含用户会话详细信息。 您还可以单击`Add builtin`按钮以包含会话详细信息。

模拟的用户会话提供以下详细信息：

- `IMPERSONATOR_ID`: 模拟用户的ID
- `IMPERSONATOR_USERNAME`: 模拟用户的用户名

服务帐户会话提供以下详细信息：

- `clientId`: 服务帐户的客户端ID
- `clientAddress`: 服务帐户验证设备的远程主机IP
- `clientHost`: 服务帐户验证设备的远程主机名

<a name="97____8_5__生成客户端适配器配置"></a>
### 8.5. 生成客户端适配器配置

Keycloak可以预先生成配置文件，您可以使用这些配置文件在应用程序的部署环境中安装客户端适配器。 OIDC和SAML都支持许多适配器类型。 转到要为其生成配置的客户端的`Installation`选项卡。

![client installation](assets/client-installation.png)

选择要为其生成配置的`Format Option`。 支持所有用于OIDC和SAML的Keycloak客户端适配器。 支持SAML的`mod-auth-mellon Apache HTTPD`适配器以及标准SAML实体描述符文件。

<a name="98____8_6__客户端作用域"></a>
### 8.6. 客户端作用域

如果您需要在组织内保护和注册许多应用程序，为每个客户端配置[协议映射器](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) 和 [角色范围映射](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings) 可能会变得很繁琐。 Keycloak允许您在名为*client scope*的实体中定义共享客户端配置。

客户端作用域还为OAuth 2的`scope`参数提供支持，该参数允许客户端应用程序根据应用程序需求在访问令牌中请求更多或更少的声明或角色。

要创建客户端作用域，请按照下列步骤操作：

- 转到`Client Scopes`左侧菜单项。 此初始屏幕显示当前定义的客户端作用域列表。

Client Scopes List

![client scopes list](assets/client-scopes-list.png)

- 单击`Create`按钮。 命名客户端范围并保存。 A *client scope(客户端作用域)* 将具有与常规客户端类似的选项卡。 您可以定义[协议映射器](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) 和 [角色范围映射](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings)，可以由其他客户端继承，并配置为从此客户端作用域继承。

<a name="99_____8_6_1__协议"></a>
#### 8.6.1. 协议
在创建客户端作用域时，必须选择`Protocol`。 然后，只有使用相同协议的客户端才能与此客户端作用域链接。

创建新领域后，您可以看到菜单中有一个预定义（内置）客户端作用域列表。

- 对于SAML协议，有一个内置客户端作用域`roles_list`，其中包含一个协议映射器，用于显示SAML断言中的角色列表。
- 对于OpenID Connect协议，有客户端作用域`profile`，`email`，`address`，`phone`，`offline_access`，`roles`，`web-originins`和`microprofile-jwt`。

当客户端想要获取脱机令牌时，客户端作用域`offline_access`非常有用。 在[离线访问部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access) 或 [OpenID Connect规范](https://openid.net/specs/openid-connect-core-1_0.html#OfflineAccess) 中了解离线令牌，其中scope参数定义为值`offline_access`。

客户端作用域 `profile`，`email`，`address`和`phone`也在[OpenID Connect规范](https://openid.net/specs/openid-connect-core-1_0.html#ScopeClaims)中定义。 这些客户端作用域没有定义任何角色作用域映射，但是它们定义了一些协议映射器，并且这些映射器对应于OpenID Connect规范中定义的声明。

例如，当您单击打开`phone`客户端范围并打开`Mappers`选项卡时，您将看到协议映射器，它对应于范围`phone`的规范中定义的声明。

Client Scope Mappers

![client scopes phone](assets/client-scopes-phone.png)

当`phone`客户端作用域链接到客户端时，该客户端自动继承在`phone`客户端作用域中定义的所有协议映射器。 为该客户端发出的访问令牌将包含有关用户的电话号码信息，假设用户具有已定义的电话号码。

Builtin客户端作用域包含按照规范定义的协议映射器，但是您可以自由编辑客户端作用域并`创建/更新/删除`任何协议映射器（或角色作用域映射）。

客户端作用域`roles`未在OpenID Connect规范中定义，也不会自动添加到访问令牌中的`scope`声明中。 此客户端范围有一些映射器，用于将用户的角色添加到访问令牌，并可能为具有至少一个客户端角色的客户端添加一些受众，如[受众节](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_resolve)部分所述。

客户端范围`web-originins`也没有在OpenID Connect规范中定义，也没有添加到`scope`声明中。 这用于将允许的Web来源添加到访问令牌`allowed-origins`声明中。

创建客户端作用域`microprofile-jwt`以处理[MicroProfile / JWT Auth Specification](https://wiki.eclipse.org/MicroProfile/JWT_Auth)中定义的声明。 此客户端作用域为`upn`声明定义了一个用户属性映射器，并为`groups`声明定义了一个领域角色映射器。 可以根据需要更改这些映射器，以便可以使用不同的属性来创建`MicroProfile/JWT`特定声明。

<a name="100_____8_6_2__同意相关设置"></a>
#### 8.6.2. 同意相关设置
客户端作用域包含与同意屏幕相关的选项。 仅当链接客户端配置为需要同意时（如果在客户端上启用了`Consent Required(同意所需)`开关），这些选项才有用。

- 在同意屏幕上显示

  如果启用，并且如果将此客户端作用域添加到需要同意的客户端，则`Consent Screen Text(同意屏幕文本)`指定的文本将显示在同意屏幕上，一旦用户通过身份验证并且在重定向之前显示 给客户的Keycloak。 如果开关已关闭，则此同一客户端作用域将不会显示在同意屏幕上。

- 同意屏幕文字

  当此客户端作用域被添加到需要同意的某个客户端时，同意屏幕上显示的文本默认为客户端范围的名称。 通过使用`${var-name}`字符串指定替换变量，可以对此文本的值进行本地化。 然后，在主题的属性文件中配置本地化值。 有关本地化的更多信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="101_____8_6_3__将客户端作用域与客户端链接"></a>
#### 8.6.3. 将客户端作用域与客户端链接
客户端作用域和客户端之间的链接在特定客户端的`Client Scopes`选项卡中配置。 客户端作用域和客户端之间有两种链接方式。

- Default Client Scopes

  这适用于OpenID Connect和SAML客户端。 在为此客户端发出OpenID Connect令牌或SAML断言时，始终应用默认客户端作用域。 客户端将继承客户端作用域上定义的协议映射器和角色作用域映射。 对于OpenID Connect协议，无论在OpenID Connect授权请求中使用scope参数的值如何，始终应用Mappers和Role Scope Mappings。

- Optional Client Scopes

  这仅适用于OpenID Connect客户端。 在为此客户端发出令牌时应用可选的客户端作用域，但仅当它们由OpenID Connect授权请求中的`scope`参数请求时才应用。

<a name="102______例子"></a>
##### 例子
对于此示例，我们假设客户端将`profile`和`email`链接为默认客户端作用域，并且`phone`和`address`作为可选的客户端作用域链接。 在向OpenID Connect授权端点发送请求时，客户端将使用scope参数的值：

```properties
scope=openid phone
```

scope参数包含字符串，范围值除以空格（这也是客户端范围名称中不能包含空格字符的原因）。 值`openid`是用于所有OpenID Connect请求的元值，因此我们将在此示例中忽略它。 令牌将包含来自客户端作用域`profile`，`email`（默认作用域）和`phone`（作用域参数请求的可选客户端作用域）的映射器和角色作用域映射。

<a name="103_____8_6_4__评估客户端作用域"></a>
#### 8.6.4. 评估客户端作用域
客户端的`Mappers` 和 `Scope`选项卡包含仅为此客户端声明的协议映射器和角色范围映射。 它们不包含从客户端作用域继承的映射器和作用域映射。 但是，查看有效协议映射器将是什么（在客户端本身定义的协议映射器以及从链接的客户端作用域继承）以及为特定客户端生成令牌时使用的有效角色作用域映射可能很有用。

当您单击客户端的`Client Scopes`选项卡，然后打开子选项卡`Evaluate(评估)`时，您可以看到所有这些。 从这里，您可以选择要应用的可选客户端范围。 这还将显示`scope`参数的值，该值需要从应用程序发送到Keycloak OpenID Connect授权端点。

Evaluating Client Scopes

![client scopes evaluate](assets/client-scopes-evaluate.png)

> 如果您想了解如何从应用程序发送`scope`参数的自定义值，请参阅[参数转发部分](https://www.keycloak.org/docs/6.0/securing_apps/#_params_forwarding)， 如果您的应用程序使用servlet适配器，或[javascript适配器部分](https://www.keycloak.org/docs/6.0/securing_apps/#_javascript_adapter)，如果您的应用程序使用javascript适配器。

<a name="104______生成示例令牌"></a>
##### 生成示例令牌
要查看为特定用户生成并为特定客户端发出的具有指定值`scope`参数的实际访问令牌的示例，请从`Evaluate`屏幕中选择用户。 这将生成一个示例标记，其中包含所有使用的声明和角色映射。

<a name="105_____8_6_5__客户端作用域权限"></a>
#### 8.6.5. 客户端作用域权限
为特定用户颁发令牌时，仅在允许用户使用客户端作用域时才应用客户端作用域。 如果客户端作用域没有自己定义的任何角色作用域映射，则会自动允许每个用户使用此客户端作用域。 但是，当客户端作用域具有自身定义的任何角色作用域映射时，用户必须至少是其中一个角色的成员。 换句话说，用户角色与客户端范围的角色之间必须存在交集。 评估此交集时会考虑复合角色。

如果不允许用户使用客户端作用域，则在生成令牌时将不使用协议映射器或角色作用域映射，并且客户端作用域不会出现在令牌中的*scope*值中。

<a name="106_____8_6_6__领域默认客户端作用域"></a>
#### 8.6.6. 领域默认客户端作用域
`Realm Default Client Scopes`允许您定义一组客户端作用域，这些作用域将自动链接到新创建的客户端。

打开左侧菜单项`Client Scopes`，然后选择`Default Client Scopes`。

从此处，为新创建的客户端选择要添加为`Default Client Scopes`的客户端作用域，为新创建的客户端选择`Optional Client Scopes`。

Default Client Scopes

![client scopes default](assets/client-scopes-default.png)

创建客户端后，您可以根据需要取消链接默认客户端作用域。 这与删除[默认角色](https://www.keycloak.org/docs/latest/server_admin/index.html#_default_roles)的方式类似。

<a name="107_____8_6_7__作用域解释"></a>
#### 8.6.7. 作用域解释
术语`scope`在Keycloak中用于少数几个地方。 各种作用域的出现彼此相关，但可能具有不同的上下文和含义。 为了澄清，这里我们解释Keycloak中使用的各种`scopes`。

- Client scope

  在本章中引用。 客户端作用域是Keycloak中的实体，它们在领域级别配置，并且可以链接到客户端。 当使用相应的`scope`参数值向Keycloak授权端点发送请求时，客户端作用域将通过其名称引用。 详细信息在[关于客户端作用域链接的部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking)中进行了描述。

- Role scope mapping

  当您打开客户端或客户端范围的`Scope`选项卡时，可以看到这一点。 角色作用域映射允许您限制可以在访问令牌中使用的角色。 详细信息在[Role Scope Mappings部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings)中描述。

- Authorization scopes

  这由授权功能使用。 `Authorization Scope`是可以在应用程序中完成的操作。 [授权服务指南](https://www.keycloak.org/docs/6.0/authorization_services/)中的更多详细信息。

<a name="108___9__角色"></a>
## 9. 角色

角色标识用户的类型或类别。 `Admin`，`user`，`manager`和`employee`都是组织中可能存在的典型角色。 应用程序通常为特定角色而不是单个用户分配访问权限和权限，因为与用户打交道可能过于细粒度且难以管理。 例如，管理控制台具有特定角色，这些角色授予用户访问管理控制台UI部分并执行某些操作的权限。 角色有一个全局命名空间，每个客户端也有自己的专用命名空间，可以定义角色。

<a name="109____9_1__领域角色"></a>
### 9.1. 领域角色

领域级角色是定义角色的全局命名空间。 您可以通过单击`Roles`左侧菜单项来查看内置和创建的角色列表。

![roles](assets/roles.png)

要创建角色，请单击此页面上的**Add Role**，输入角色的名称和描述，然后单击**Save**。

Add Role

![role](assets/role.png)

通过使用`${var-name}`字符串指定替换变量，可以对`description`字段的值进行本地化。 然后，在主题的属性文件中配置本地化值。 有关本地化的更多信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="110____9_2__客户端_角色"></a>
### 9.2. 客户端 角色

客户端角色基本上是专用于客户端的命名空间。 每个客户端都有自己的命名空间 客户角色在每个客户端下的`Roles`选项卡下进行管理。 您与此UI的交互方式与您对领域级角色的交互方式相同。

<a name="111____9_3__复合_角色"></a>
### 9.3. 复合 角色

任何领域或客户级角色都可以转换为*composite role(复合角色)*。 一个 *composite role*是具有一个或多个与之关联的其他角色的角色。 将复合角色映射到用户时，用户还将获得与该复合关联的角色。 这种继承是递归的，因此任何复合合成也都会被继承。

要将常规角色转换为复合角色，请转到角色详细信息页面并打开`Composite Role`开关。

Composite Role

![composite role](assets/composite-role.png)

一旦您翻转此开关，角色选择UI将显示在页面的较低位置，您将能够将领域级别和客户端级别角色与您正在创建的组合关联。 在此示例中，`employee`领域级角色与`developer`复合角色相关联。 任何具有`developer`角色的用户现在也将继承`employee`角色。

> 创建令牌和SAML断言时，任何组合也将其相关角色添加到发送回客户端的身份验证响应的声明和断言中。

<a name="112____9_4__用户角色映射"></a>
### 9.4. 用户角色映射

用户角色映射可以通过该单个用户的`Role Mappings(角色映射)`选项卡单独分配给每个用户。

Role Mappings

![user role mappings](assets/user-role-mappings.png)

在上面的例子中，我们将分配在[Composite Roles](https://www.keycloak.org/docs/latest/server_admin/index.html#_composite-roles)章节中创建的复合角色`developer`。

Effective Role Mappings

![effective role mappings](assets/effective-role-mappings.png)

一旦分配了`developer`角色，您就会看到与`developer`合成相关联的`employee`角色出现在`Effective Roles(有效角色)`中。 `Effective Roles`是显式分配给用户的所有角色以及从复合体继承的任何角色。

<a name="113_____9_4_1__默认角色"></a>
#### 9.4.1. 默认角色

默认角色允许您在通过[Identity Brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker)新创建或导入任何用户时自动分配用户角色映射。 要指定默认角色，请转到`Roles`左侧菜单项，然后单击`Default Roles`选项卡。

Default Roles

![default roles](assets/default-roles.png)

从屏幕截图中可以看出，默认情况下已经设置了许多*default roles*。

<a name="114____9_5__角色范围映射"></a>
### 9.5. 角色范围映射

创建OIDC访问令牌或SAML断言时，默认情况下，用户的所有用户角色映射都会在令牌或断言中添加为声明。 应用程序使用此信息对该应用程序控制的资源进行访问决策。 在Keycloak中，访问令牌经过数字签名，实际上可以被应用程序重用，以调用其他远程安全的REST服务。 这意味着，如果某个应用程序受到攻击或者该域名中存在一个流氓客户端，则攻击者可以获得具有广泛权限的访问权限，并且您的整个网络都会受到攻击。 这就是*role scope mappings(角色范围映射)*变得重要的地方。

*Role Scope Mappings*是一种限制在访问令牌中声明的角色的方法。 当客户端请求对用户进行身份验证时，他们收到的访问令牌将仅包含您为客户端范围明确指定的角色映射。 这允许您限制每个单独的访问令牌具有的权限，而不是让客户端访问所有用户的权限。 默认情况下，每个客户端都会获取用户的所有角色映射。 您可以在每个客户端的`Scope`选项卡中查看此内容。

Full Scope

![full client scope](assets/full-client-scope.png)

从图中可以看出，范围的有效角色是领域中每个声明的角色。 要更改此默认行为，您必须明确关闭`Full Scope Allowed(允许的全范围)`开关，并在每个单独的客户端中声明所需的特定角色。 或者，您也可以使用[客户端作用域](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes)为整组客户端定义相同的角色作用域映射。

Partial Scope

![client scope](assets/client-scope.png)

<a name="115___10__组"></a>
## 10. 组

Keycloak中的组允许您为一组用户管理一组通用属性和角色映射。 用户可以是零个或多个组的成员。 用户继承分配给每个组的属性和角色映射。 要管理组，请转到`Groups`左侧菜单项。

Groups

![groups](assets/groups.png)

组是分层的。 一个组可以有许多子组，但一个组只能有一个父组。 子组从父级继承属性和角色映射。 这也适用于用户。 因此，如果您有父组和子组以及仅属于子组的用户，则用户将继承父级和子级的属性和角色映射。 在这个例子中，我们有一个顶级的`Sales`组和一个子`North America`子组。 要添加组，请单击要添加新子项的父项，然后单击`New`按钮。 选择树中的`Groups`图标以创建顶级组。 在`Create Group`屏幕中输入组名并点击`Save`将进入单个组管理页面。

Group

![group](assets/group.png)

`Attributes`和`Role Mappings`选项卡的工作方式与用户下具有相似名称的选项卡完全相同。 您定义的任何属性和角色映射都将由作为该组成员的组和用户继承。

要将用户添加到组，您需要一直返回到用户详细信息页面，然后单击那里的`Groups`选项卡。

User Groups

![user groups](assets/user-groups.png)

从`Available Groups`树中选择一个组，然后单击`join`按钮将用户添加到组中。 反之亦然删除一个组。 在这里，我们将用户*Jim*添加到*North America*销售组。 如果您返回该组的详细信息页面并选择`Membership`选项卡，则*Jim*现在显示在那里。

Group Membership

![group membership](assets/group-membership.png)

<a name="116____10_1__群组与角色"></a>
### 10.1. 群组与角色

在IT世界中，组和角色的概念通常是模糊和可互换的。 在Keycloak中，组只是一组用户，您可以在一个位置应用角色和属性。 角色定义一种用户，应用程序为角色分配权限和访问控制

是不是[复合角色](https://www.keycloak.org/docs/latest/server_admin/index.html#_composite-roles)也与群组相似？ 从逻辑上讲，它们提供了相同的功能，但区别在于概念。 应使用组合角色将权限模型应用于您的服务和应用程序集。 组应关注用户集合及其在组织中的角色。 使用组来管理用户。 使用复合角色来管理应用程序和服务。

<a name="117____10_2__默认组"></a>
### 10.2. 默认组

默认组允许您在通过[Identity Brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker)创建或导入任何新用户时自动分配组成员资格。 要指定默认组，请转到`Groups`左侧菜单项，然后单击`Default Groups`选项卡。

Default Groups

![default groups](assets/default-groups.png)

<a name="118___11__管理控制台访问控制和权限"></a>
## 11. 管理控制台访问控制和权限

Keycloak上创建的每个领域都有一个专用的管理控制台，可以从中管理该领域。 `master`领域是一个特殊的领域，允许管理员管理系统上的多个领域。 您还可以定义对不同领域中的用户的细粒度访问以管理服务器。 本章将讨论所有场景。

<a name="119____11_1__Master_Realm访问控制"></a>
### 11.1. Master Realm访问控制

Keycloak中的`master`领域是一个特殊的领域，与其他领域的处理方式不同。 可以授予Keycloak`master`域中的用户管理部署在Keycloak服务器上的零个或多个域的权限。 创建领域时，Keycloak会自动创建各种角色，授予细粒度权限以访问新领域。 可以通过将这些角色映射到`master`领域中的用户来控制对`Admin Console`和`Admin REST`端点的访问。 可以创建多个超级用户，以及只能管理特定领域的用户。

<a name="120_____11_1_1__Global_Roles"></a>
#### 11.1.1. Global Roles
在`master`领域有两个领域级别的角色。 这些是：

- admin
- create-realm

具有`admin`角色的用户是超级用户，并且拥有管理服务器上任何领域的完全访问权限。 具有`create-realm`角色的用户可以创建新领域。 他们将被授予对他们创建的任何新领域的完全访问权限。

<a name="121_____11_1_2__领域特定角色"></a>
#### 11.1.2. 领域特定角色
`master`领域内的管理员用户可以被授予系统中一个或多个其他领域的管理权限。 Keycloak中的每个领域都由`master`领域的客户端代表。 客户端的名称是`<realm name>-realm`。 这些客户端每个都定义了客户端级角色，这些角色定义了管理单个领域的不同访问级别。

可用的角色是：

- view-realm
- view-users
- view-clients
- view-events
- manage-realm
- manage-users
- create-client
- manage-clients
- manage-events
- view-identity-providers
- manage-identity-providers
- impersonation

将您想要的角色分配给您的用户，他们只能使用管理控制台的特定部分。

> 具有`manage-users`角色的管理员只能为自己拥有的用户分配管理员角色。 因此，如果管理员具有`manage-users角`色但没有`manage-realm`角色，则他们将无法分配此角色。

<a name="122____11_2__专用领域管理控制台"></a>
### 11.2. 专用领域管理控制台

每个领域都有一个专用的管理控制台，可以通过访问url`/auth/admin/{realm-name}/console`来访问。 通过分配特定的用户角色映射，可以为该领域中的用户授予领域管理权限。

每个领域都有一个名为`realm-management`的内置客户端。 您可以通过转到领域的`Clients`左侧菜单项来查看此客户端。 此客户端定义客户端级角色，这些角色指定可以授予管理域的权限。

- view-realm
- view-users
- view-clients
- view-events
- manage-realm
- manage-users
- create-client
- manage-clients
- manage-events
- view-identity-providers
- manage-identity-providers
- impersonation

将您想要的角色分配给您的用户，他们只能使用管理控制台的特定部分。

<a name="123____11_3__细粒度管理员权限"></a>
### 11.3. 细粒度管理员权限

> Fine Grain管理员权限是**技术预览**，并不完全支持。 默认情况下禁用此功能。要启用`-Dkeycloak.profile=preview`或`-Dkeycloak.profile.feature.admin_fine_grained_authz=enabled`启动服务器。 有关详细信息，请参阅[配置文件](https://www.keycloak.org/docs/6.0/server_installation/#profiles)。

有时像`manage-realm`或`manage-users`这样的角色太粗糙，你想要创建具有更多细粒度权限的受限管理员帐户。 Keycloak允许您定义和分配用于管理领域的受限访问策略。 像：

- 管理一个特定的客户
- 管理属于特定组的用户
- 管理组的成员身份
- 有限的用户管理
- 细粒模仿控制
- 能够为用户分配特定的受限制角色集
- 能够将特定的受限制角色集分配给复合角色
- 能够将特定的受限制角色集分配给客户的范围
- 用于查看和管理用户，组，角色和客户端的新常规策略

有关细粒度管理员权限的一些重要事项需要注意：

- 细粒度管理员权限在[授权服务](https://www.keycloak.org/docs/6.0/authorization_services/)之上实现。 强烈建议您在深入了解细粒度权限之前先阅读这些功能。
- 细粒度权限仅在[专用管理控制台](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions)和在这些领域内定义的管理员中可用。 您无法定义跨领域细粒度权限。
- 细粒度权限用于授予其他权限。 您无法覆盖内置管理角色的默认行为。

<a name="124_____11_3_1__管理一个特定的客户"></a>
#### 11.3.1. 管理一个特定的客户
让我们首先看一下管理员只管理一个客户端和一个客户端。 在我们的例子中，我们有一个名为`test`的领域和一个名为`sales-application`的客户端。 在领域`test`中，我们将为该领域的用户授予仅管理该应用程序的权限。

> 你不能做cross realm细粒度权限。 `master`领域的管理员仅限于前面章节中定义的预定义管理员角色。

<a name="125______权限设置"></a>
##### 权限设置
我们必须做的第一件事是登录管理控制台，以便我们可以为该客户端设置权限。 我们导航到我们要为其定义细粒度权限的客户端的管理部分。

Client Management

![fine grain client](assets/fine-grain-client.png)

您应该看到一个名为`Permissions`的标签菜单项。 单击该选项卡。

Client Permissions Tab

![fine grain client permissions tab off](assets/fine-grain-client-permissions-tab-off.png)

默认情况下，不启用每个客户端来执行细粒度权限。 因此，将`Permissions Enabled`开关设置为on以初始化权限。

> 如果您将`Permissions Enabled`开关设置为off，它将删除您为此客户端定义的所有权限。

Client Permissions Tab

![fine grain client permissions tab on](assets/fine-grain-client-permissions-tab-on.png)

当您启用`Permissions Enabled`时，它会使用[授权服务](https://www.keycloak.org/docs/6.0/authorization_services/)在幕后初始化各种权限对象。 对于此示例，我们对客户端的`manage`权限感兴趣。 单击它会将您重定向到处理客户端`manage`权限的权限。 所有授权对象都包含在`realm-management`客户端的`Authorization`选项卡中。

Client Manage Permission

![fine grain client manage permissions](assets/fine-grain-client-manage-permissions.png)

首次初始化时，`manage`权限没有任何与之关联的策略。 您需要转到策略选项卡创建一个。 要快速到达，请单击上图中显示的`Authorization(授权)`链接。 然后单击“策略”选项卡。

这个页面上有一个名为`Create policy`的下拉菜单。 您可以定义多种策略。 您可以定义与角色或组关联的策略，甚至可以在JavaScript中定义规则。 对于这个简单的例子，我们将创建一个`User Policy`。

User Policy

![fine grain client user policy](assets/fine-grain-client-user-policy.png)

此策略将匹配用户数据库中的硬编码用户。 在这种情况下，它是`sales-admin`用户。 然后我们必须回到`sales-application`客户端的`manage`权限页面并将策略分配给权限对象。

Assign User Policy

![fine grain client assign user policy](assets/fine-grain-client-assign-user-policy.png)

`sales-admin`用户现在可以拥有管理`sales-application`客户端的权限。

我们还有一件事要做。 转到`Role Mappings`选项卡并将`query-clients`角色分配给`sales-admin`。

Assign query-clients

![fine grain assign query clients](assets/fine-grain-assign-query-clients.png)

你为什么要这样做？ 此角色告诉管理控制台当`sales-admin`访问管理控制台时要呈现的菜单项。 `query-clients`角色告诉管理控制台它应该为`sales-admin`user呈现客户菜单。

重要: 如果您没有设置`query-clients`角色，那么受限制的管理员（例如`sales-admin`）在登录管理控制台时将看不到任何菜单选项

<a name="126______测试它"></a>
##### 测试它
接下来，我们退出主域并重新登录到[专用管理控制台](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions)，用于`test`域使用 `sales-admin`作为用户名。 它位于`/auth/admin/test/console`下。

Sales Admin Login

![fine grain sales admin login](assets/fine-grain-sales-admin-login.png)

此管理员现在可以管理这个客户端。

<a name="127_____11_3_2__限制用户角色映射"></a>
#### 11.3.2. 限制用户角色映射
您可能想要做的另一件事是限制集合允许管理员分配给用户的角色。 继续我们的最后一个示例，让我们扩展`sales-admin`用户的权限集，以便他还可以控制允许哪些用户访问此应用程序。 通过细粒度权限，我们可以启用它，以便`sales-admin`只能分配授予对`sales-application`的特定访问权限的角色。 我们还可以对其进行限制，以便管理员只能映射角色而不执行任何其他类型的用户管理。

`sales-application`定义了三种不同的客户角色。

销售应用程序角色

![fine grain sales application roles](assets/fine-grain-sales-application-roles.png)

我们希望`sales-admin`用户能够将这些角色映射到系统中的任何用户。 执行此操作的第一步是允许管理员映射角色。 如果我们单击`viewLeads`角色，您将看到此角色有一个`Permissions`选项卡。

查看潜在客户角色权限选项卡

![fine grain view leads role tab](assets/fine-grain-view-leads-role-tab.png)

如果我们单击该选项卡并打开`Permissions Enabled`，您将看到我们可以应用策略执行多项操作。

查看潜在客户权限

![fine grain view leads permissions](assets/fine-grain-view-leads-permissions.png)

我们感兴趣的是`map-role`。 单击此权限并添加在先前示例中创建的相同用户策略。

映射角色权限

![fine grain map roles permission](assets/fine-grain-map-roles-permission.png)

我们所做的就是说`sales-admin`可以映射`viewLeads`角色。 我们尚未做的是指定管理员也可以映射此角色的用户。 为此，我们必须转到此领域的管理控制台的`Users`部分。 单击`Users`左侧菜单项将我们带到领域的用户界面。 你应该看到一个`Permissions`选项卡。 单击它并启用它。

用户权限

![fine grain users permissions](assets/fine-grain-users-permissions.png)

我们感兴趣的权限是`map-roles`。 这是一项限制性策略，因为它只允许管理员将角色映射到用户。 如果我们点击`map-roles`权限并再次添加我们为此创建的用户策略，我们的`sales-admin`将能够将角色映射到任何用户。

我们要做的最后一件事是将`view-users`角色添加到`sales-admin`。 这将允许管理员查看他想要添加`sales-application`角色的领域中的用户。

添加视图用户

![fine grain add view users](assets/fine-grain-add-view-users.png)

<a name="128______测试它_"></a>
##### 测试它.
接下来，我们退出主域并重新登录到[专用管理控制台](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions)，用于`test`域使用 `sales-admin`作为用户名。 它位于`/auth/admin/test/console`下。

您将看到`sales-admin`现在可以查看系统中的用户。 如果您选择其中一个用户，您将看到每个用户详细信息页面都是只读的，`Role Mappings`选项卡除外。 转到这些选项卡，您会发现管理员没有`Available`角色映射到用户，除非我们浏览`sales-application`角色。

Add viewLeads

![fine grain add view leads](assets/fine-grain-add-view-leads.png)

我们只指定`sales-admin`可以映射`viewLeads`角色。

<a name="129______每个客户端_映射_角色_快捷方式"></a>
##### 每个客户端 映射-角色 快捷方式
如果我们必须为`sales-application`发布的每个客户角色执行此操作，那将是单调乏味的。 为了简化操作，有一种方法可以指定管理员可以映射客户端定义的任何角色。 如果我们重新登录管理控制台到我们的主域管理员并返回到`sales-application`权限页面，您将看到`map-roles`权限。

客户端 映射-角色 权限

![fine grain client permissions tab on](assets/fine-grain-client-permissions-tab-on.png)

如果您授予管理员对此特定权限的访问权限，则该管理员将能够映射客户端定义的任何角色。

<a name="130_____11_3_3__完整的权限列表"></a>
#### 11.3.3. 完整的权限列表
除了管理特定客户端或客户端的特定角色之外，您还可以使用细粒度权限执行更多操作。 本章定义了可以为领域描述的权限类型的完整列表。

<a name="131______Role"></a>
##### Role
当转到特定角色的`Permissions`选项卡时，您将看到列出的这些权限类型。

- map-role

  决定管理员是否可以将此角色映射到用户的策略。 这些策略仅指定角色可以映射到用户，而不是允许管理员执行用户角色映射任务。 管理员还必须具有管理或角色映射权限。 有关详细信息，请参阅[用户权限](https://www.keycloak.org/docs/latest/server_admin/index.html#_users-permissions)。

- map-role-composite

  决定管理员是否可以将此角色作为复合映射到另一个角色的策略。 管理员可以为客户定义角色，如果他具有该客户的管理权限，但他将无法向这些角色添加复合，除非他对要添加为复合的角色具有`map-role-composite`权限。

- map-role-client-scope

  决定管理员是否可以将此角色应用于客户端范围的策略。 即使管理员可以管理客户端，他也无权为包含此角色的客户端创建令牌，除非授予此权限。

<a name="132______客户端"></a>
##### 客户端
当转到特定客户端的`Permissions`选项卡时，您将看到列出的这些权限类型。

- view

  决定管理员是否可以查看客户端配置的策略。

- manage

  决定管理员是否可以查看和管理客户端配置的策略。 这有一些问题，特权可能会无意中泄露。 例如，管理员可以定义一个协议映射器，即使管理员没有将角色映射到客户端范围的权限，也会对该角色进行硬编码。 这是目前协议映射器的限制，因为它们没有办法像角色那样为它们分配单独的权限。

- configure

  减少了一组管理客户端的权利。 它类似于`manage`范围，但管理员不允许定义协议映射器，更改客户端模板或客户端范围。

- map-roles

  决定管理员是否可以将客户端定义的任何角色映射到用户的策略。 这是一种易于使用的快捷方式，可避免为客户定义的每个角色定义策略。

- map-roles-composite

  决定管理员是否可以将客户端定义的任何角色作为复合映射到另一个角色的策略。 这是一种易于使用的快捷方式，可避免为客户端定义的每个角色定义策略。

- map-roles-client-scope

  决定管理员是否可以将客户端定义的任何角色映射到另一个客户端范围的策略。 这是一种易于使用的快捷方式，可避免为客户端定义的每个角色定义策略。

<a name="133______用户"></a>
##### 用户
当进入所有用户的`Permissions`选项卡时，您将看到列出的这些权限类型。

- view

  决定管理员是否可以查看领域中所有用户的策略。

- manage

  决定管理员是否可以管理领域中所有用户的策略。 此权限授予管理员执行用户角色映射的权限，但不指定管理员允许映射的角色。 您需要为管理员能够映射的每个角色定义权限。

- map-roles

  这是`manage`范围授予的权限的子集。 在这种情况下，只允许管理员映射角色。 不允许管理员执行任何其他用户管理操作。 此外，与`manage`一样，如果处理客户端角色，则必须为每个角色或每组角色指定允许管理员应用的角色。

- manage-group-membership

  类似于`map-roles`，除了它与组成员资格相关：可以添加或删除用户的组。 这些策略仅授予管理员管理组成员资格的权限，而不授予管理员管理其成员资格的组。 您必须为每个组的`manage-members`权限指定策略。

- impersonate

  决定是否允许管理员模仿其他用户的策略。 这些策略应用于管理员的属性和角色映射。

- user-impersonated

  决定可以模拟哪些用户的策略。 这些策略将应用于被模拟的用户。 例如，您可能希望定义一个策略，禁止任何人冒充具有管理员权限的用户。

##### 组
当转到特定组的`Permissions`选项卡时，您将看到列出的这些权限类型。

- view

  决定管理员是否可以查看有关该组的信息的策略。

- manage

  决定管理员是否可以管理组配置的策略。

- view-members

  决定管理员是否可以查看该组成员的用户详细信息的策略。

- manage-members

  决定管理员是否可以管理属于该组的用户的策略。

- manage-membership

  决定管理员是否可以更改组成员身份的策略。 在组中添加或删除成员。

<a name="134____11_4__领域密钥"></a>
### 11.4. 领域密钥

Keycloak使用的身份验证协议需要加密签名，有时还需要加密。 Keycloak使用非对称密钥对，私钥和公钥来实现这一目标。

Keycloak一次只有一个活动密钥对，但也可以有几个被动密钥。 活动密钥对用于创建新签名，而被动密钥对可用于验证以前的签名。 这使得可以定期旋转键而无需停机或中断用户。

创建领域时，会自动生成密钥对和自签名证书。

要查看领域的活动密钥，请在管理控制台中选择领域，单击`Realm settings`，然后单击`Keys`。 这将显示领域的当前活动密钥。 Keycloak目前仅支持RSA签名，因此只有一个活动密钥对。 将来随着更多签名算法的增加，将会有更多活跃的密钥对。

要查看所有可用键，请选择`All`。 这将显示所有活动，被动和禁用键。 密钥对可以具有`Active`状态，但仍未被选为该领域的当前活动密钥对。 基于按能够提供活动密钥对的优先级排序的第一密钥提供者来选择用于签名的所选活动对。

<a name="135_____11_4_1__轮流密钥"></a>
#### 11.4.1. 轮流密钥
建议定期轮流密钥。 为此，您应该首先创建优先级高于现有活动密钥的新密钥。 或者创建具有相同优先级的新密钥并使之前的密钥处于被动状态。

一旦有新密钥可用，所有新令牌和cookie都将使用新密钥进行签名。 当用户对应用程序进行身份验证时，将使用新签名更新SSO cookie。 刷新OpenID Connect令牌时，将使用新密钥对新令牌进行签名。 这意味着随着时间的推移，所有cookie和令牌都将使用新密钥，过一会儿就可以删除旧密钥。

您等待删除旧密钥的时间是安全性之间的权衡，并确保更新所有cookie和令牌。 一般来说，几周后丢弃旧密钥应该是可以接受的。 在添加的新密钥和删除的旧密钥之间的时间段内未处于活动状态的用户必须重新进行身份验证。

这也适用于离线令牌。 为确保更新它们，应用程序需要在删除旧密钥之前刷新令牌。

作为指导，每3-6个月创建一个新密钥并在创建新密钥后1-2个月删除旧密钥可能是个好主意。

<a name="136_____11_4_2__添加生成的密钥对"></a>
#### 11.4.2. 添加生成的密钥对
要添加新生成的密钥对，请选择`Providers`并从下拉列表中选择`rsa-generated`。 您可以更改优先级以确保新密钥对成为活动密钥对。 如果需要更小或更大的键，也可以更改`keysize`（默认值为2048，支持的值为1024,2048和4096）。

单击`Save`以添加新密钥。 这将生成一个新的密钥对，包括自签名证书。

更改提供程序的优先级不会导致重新生成密钥，但如果要更改密钥大小，则可以编辑提供程序并生成新密钥。

<a name="137_____11_4_3__添加现有密钥对和证书"></a>
#### 11.4.3. 添加现有密钥对和证书
要添加在其他地方获得的密钥对和证书，请选择`Providers`并从下拉列表中选择`rsa`。 您可以更改优先级以确保新密钥对成为活动密钥对。

单击`Select RSA Key`的`Select file`以上传您的私钥。 该文件应以PEM格式编码。 您无需上传公钥，因为它是从私钥中自动提取的。

如果您有密钥的签名证书，请单击`X509证书`旁边的`Select file`。 如果您没有，则可以跳过此项，并生成自签名证书。

<a name="138_____11_4_4__从Java密钥库加载密钥"></a>
#### 11.4.4. 从Java密钥库加载密钥
要在主机上添加存储在Java Keystore文件中的密钥对和证书，请选择`Providers`并从下拉列表中选择`java-keystore`。 您可以更改优先级以确保新密钥对成为活动密钥对。

填写`Keystore`，`Keystore Password`，`Key Alias`和`Key Password`的值，然后单击`Save`。

<a name="139_____11_4_5__使密钥消极"></a>
#### 11.4.5. 使密钥消极
在`Active`或`All`中找到密钥对，然后单击`Provider`列中的提供程序。 这将带您进入密钥的密钥提供程序的配置屏幕。 点击`Active`将其变为`OFF`，然后点击`Save`。 密钥将不再处于活动状态，只能用于验证签名。

<a name="140_____11_4_6__禁用密钥"></a>
#### 11.4.6. 禁用密钥
在`Active`或`All`中找到密钥对，然后单击`Provider`列中的提供程序。 这将带您进入密钥的密钥提供程序的配置屏幕。 单击`Enabled`将其设置为`OFF`，然后单击`Save`。 将不再启用密钥。

或者，您可以从`Providers`表中删除提供程序。

<a name="141_____11_4_7__泄露的密钥"></a>
#### 11.4.7. 泄露的密钥
Keycloak只在本地存储签名密钥，它们永远不会与客户端应用程序，用户或其他实体共享。 但是，如果您认为您的域签名密钥已被破坏，则应首先生成如上所述的新密钥对，然后立即删除受损密钥对。

然后，为了确保客户端应用程序不接受受攻击密钥签名的令牌，您应该更新并推送域的非先行策略，这可以从管理控制台执行。 推出新策略将确保客户端应用程序不会接受由受感染密钥签名的现有令牌，但客户端应用程序将被强制从Keycloak下载新密钥对，因此受攻击密钥签名的令牌将无效了。 请注意，您的REST和机密客户端必须设置`Admin URL`,以便Keycloak能够向他们发送有关推送的不在之前策略的请求。

<a name="142___12__身份代理"></a>
## 12. 身份代理

Identity Broker是一种中间服务，它将多个服务提供者与不同的身份提供者连接起来。 作为中间服务，身份代理负责与外部身份提供者建立信任关系，以便使用其身份访问服务提供者公开的内部服务。

从用户的角度来看，身份代理提供了一种以用户为中心的集中方式来管理不同安全域或领域的身份。 现有帐户可以与来自不同身份提供者的一个或多个身份链接，或甚至基于从他们获得的身份信息创建。

身份提供者通常基于特定协议，该协议用于向其用户验证和传递身份验证和授权信息。 它可以是Facebook，Google或Twitter等社交提供商。 它可以是用户需要访问您的服务的业务合作伙伴。 或者它可以是您要与之集成的基于云的身份服务。

通常，身份提供者基于以下协议：

- `SAML v2.0`
- `OpenID Connect v1.0`
- `OAuth v2.0`

在下一节中，我们将了解如何配置和使用Keycloak作为身份代理，涵盖一些重要方面，例如：

- `Social Authentication`
- `OpenID Connect v1.0 Brokering`
- `SAML v2.0 Brokering`
- `Identity Federation`

<a name="143____12_1__经纪概述"></a>
### 12.1. 经纪概述

使用Keycloak作为身份代理时，不会强制用户提供其凭据以在特定领域中进行身份验证。 相反，它们会显示一个身份提供者列表，他们可以从中进行身份验证。

您还可以配置默认身份提供程序。 在这种情况下，将不会为用户提供选择，而是将其直接重定向到默认提供程序。

下图演示了使用Keycloak代理外部身份提供程序时涉及的步骤：

身份代理流程

![identity broker flow](assets/identity_broker_flow.png)

1. 用户未经过身份验证，并在客户端应用程序中请求受保护的资源。
2. 客户端应用程序将用户重定向到Keycloak进行身份验证。
3. 此时，向用户呈现登录页面，其中存在在领域中配置的身份提供者列表。
4. 用户通过单击其相应的按钮或链接来选择一个身份提供者。
5. Keycloak向目标身份提供者发出身份验证请求，要求进行身份验证，并将用户重定向到身份提供者的登录页面。 身份提供程序的连接属性和其他配置选项先前由管理员在管理控制台中设置。
6. 用户提供其凭据或同意，以便与身份提供商进行身份验证。
7. 在身份提供商成功进行身份验证后，用户将通过身份验证响应重定向回Keycloak。 通常，此响应包含一个安全令牌，Keycloak将使用该令牌来信任身份提供程序执行的身份验证并检索有关该用户的信息。
8. 现在，Keycloak将检查身份提供者的响应是否有效。 如果有效，它将导入并创建新用户，或者如果用户已存在则跳过该用户。 如果是新用户，Keycloak可能会向身份提供者询问有关用户的信息，如果该信息中尚不存在该信息。 这就是我们所说的*identity federation*。 如果用户已存在，Keycloak可能会要求他将身份提供商返回的身份与现有帐户相关联。 我们将此流程称为*account linking*。 具体做法是可配置的，可以通过[首次登录流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker_first_login)的设置来指定。 在此步骤结束时，Keycloak对用户进行身份验证并发出自己的令牌，以便访问服务提供者中请求的资源。
9. 一旦用户进行了本地身份验证，Keycloak就会通过发送先前在本地身份验证期间发出的令牌将用户重定向到服务提供者。
10. 服务提供商从Keycloak接收令牌并允许访问受保护资源。

我们将在稍后讨论这种流程的一些变化。 例如，客户端应用程序可以请求特定的身份提供者，而不是呈现身份提供者列表。 或者，您可以告诉Keycloak强制用户在联合其身份之前提供其他信息。

> 不同协议可能需要不同的认证流程。 此时，Keycloak支持的所有身份提供者都使用如上所述的流程。 但是，无论使用何种协议，用户体验应该基本相同。

您可能会注意到，在身份验证过程结束时，Keycloak将始终向客户端应用程序发出自己的令牌。 这意味着客户端应用程序与外部身份提供程序完全分离。 他们不需要知道使用了哪种协议（例如：SAML，OpenID Connect，OAuth等）或如何验证用户的身份。 他们只需要了解Keycloak。

<a name="144____12_2__默认身份提供者"></a>
### 12.2. 默认身份提供者

可以自动重定向到身份提供者，而不是显示登录表单。 要启用此功能，请转到管理控制台中的`Authentication`页面，然后选择`Browser`流程。 然后单击`Identity Provider Redirector`身份验证器。 将`Default Identity Provider`设置为您要自动将用户重定向到的身份提供程序的别名。

如果未找到配置的默认身份提供程序，则将显示登录表单。

此验证器还负责处理`kc_idp_hint`查询参数。 有关详细信息，请参阅[客户建议的身份提供商](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_suggested_idp) 部分。

<a name="145____12_3__一般配置"></a>
### 12.3. 一般配置

身份代理配置全部基于身份提供者。 为每个领域创建身份提供程序，默认情况下，它们为每个应用程序启用。 这意味着来自领域的用户在登录应用程序时可以使用任何已注册的身份提供者。

要创建身份提供程序，请单击`Identity Providers`左侧菜单项。

身份提供者

![identity providers](assets/identity-providers.png)

在下拉列表框中，选择要添加的身份提供程序。 这将带您进入该身份提供程序类型的配置页面。

添加身份提供者

![add identity provider](assets/add-identity-provider.png)

以上是配置Google社交登录提供程序的示例。 配置IDP后，它将作为选项显示在Keycloak登录页面上。

IDP登录页面

![identity provider login page](assets/identity-provider-login-page.png)

- 社交

  社交提供程序允许您在您的领域中启用社交身份验证。 Keycloak使用户可以轻松地使用具有社交网络的现有帐户登录您的应用程序。 目前支持的提供商包括：Twitter，Facebook，谷歌，LinkedIn，Instagram，微软，PayPal，Openshift v3，GitHub，GitLab，Bitbucket和Stack Overflow。

- Protocol-based

  基于协议的提供程序是依赖于特定协议以对用户进行身份验证和授权的提供程序。 它们允许您连接到符合特定协议的任何身份提供者。 Keycloak支持SAML v2.0和OpenID Connect v1.0协议。 它可以根据这些开放标准轻松配置和代理任何身份提供商。

虽然每种类型的身份提供者都有自己的配置选项，但它们都共享一些非常常见的配置。 无论您创建哪个身份提供程序，您都会看到以下配置选项：

| 配置          | 描述                                                  |
| :--------------------- | :----------------------------------------------------------- |
| Alias                  | 别名是身份提供者的唯一标识符。 它用于在内部引用身份提供者。 某些协议（如OpenID Connect）需要重定向URI或回调URL才能与身份提供者进行通信。 在这种情况下，别名用于构建重定向URI。 每个身份提供者都必须拥有别名。 例如`facebook`，`google`，`idp.acme.com`等。 |
| Enabled                | 打开/关闭提供商。                                    |
| Hide on Login Page     | 当此开关打开时，此提供程序将不会在登录页面上显示为登录选项。 客户端仍然可以通过在用于请求登录的URL中使用`kc_idp_hint`参数来请求使用此提供程序。 |
| Account Linking Only   | 当此开关打开时，此提供程序不能用于登录用户，也不会在登录页面上显示为选项。 但是，现有帐户仍可与此提供商链接。 |
| Store Tokens           | 是否存储从身份提供者收到的令牌。 |
| Stored Tokens Readable | 是否允许用户检索存储的身份提供者令牌。 这也适用于*broker*客户端级角色*read token(读取令牌)*。 |
| Trust Email            | 如果身份提供商提供电子邮件地址，则此电子邮件地址将受信任。 如果领域需要电子邮件验证，则从此IDP登录的用户将不必通过电子邮件验证过程。 |
| GUI Order              | 用于对登录页面上列出的可用IDP进行排序的订单号。 |
| First Login Flow       | 这是第一次通过此IDP登录Keycloak的用户将触发的身份验证流程。 |
| Post Login Flow        | 用户完成使用外部身份提供程序登录后触发的身份验证流。 |

<a name="146____12_4__社交身份提供者"></a>
### 12.4. 社交身份提供者

对于面向Internet的应用程序，用户必须在您的站点注册才能获得访问权限，这非常麻烦。 它要求他们记住另一个用户名和密码组合。 社交身份提供程序允许您将身份验证委派给用户可能已拥有帐户的半受信任和受尊重的实体。 Keycloak为最常见的社交网络提供内置支持，例如Google，Facebook，Twitter，GitHub，LinkedIn，Microsoft和Stack Overflow。

<a name="147_____12_4_1__Bitbucket"></a>
#### 12.4.1. Bitbucket

您必须完成许多步骤才能启用Bitbucket登录。

首先，打开`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Bitbucket`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![bitbucket add identity provider](assets/bitbucket-add-identity-provider.png)

在您单击`Save`之前，您必须从Bitbucket获取`Client ID`和`Client Secret`。

> 您将在稍后的步骤中使用此页面中的`Redirect URI`，当您在其中注册Keycloak作为客户端时，您将提供给Bitbucket。

添加新应用

要使用Bitbucket启用登录，您必须首先在[关于Bitbucket Cloud的OAuth](https://confluence.atlassian.com/bitbucket/oauth-on-bitbucket-cloud-238027431.html)中注册一个应用程序项目。

> Bitbucket经常改变应用程序注册的外观和感觉，所以你在Bitbucket网站上看到的可能会有所不同。 如有疑问，请参阅Bitbucket文档。

![bitbucket developer applications](assets/bitbucket-developer-applications.png)

单击`Add consumer`按钮。

注册应用程序

![bitbucket register app](assets/bitbucket-register-app.png)

从Keycloak`Addd Identity Provider`页面复制`Redirect URI`并将其输入`Bitbucket Add OAuth Consumer`页面上的`Callback URL`字段。

在同一页面上，在`Account`下标记`Email`和`Read`框，以允许您的应用程序读取用户电子邮件。

Bitbucket应用页面

![bitbucket app page](assets/bitbucket-app-page.png)

完成注册后，单击`Save`。 这将打开Bitbucket中的应用程序管理页面。 从此页面中查找客户端ID和密码，以便您可以将其输入Keycloak`Add identity provider`页面。 点击`Save`。

<a name="148_____12_4_2__Facebook"></a>
#### 12.4.2. Facebook

您必须完成许多步骤才能启用Facebook登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Facebook`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![facebook add identity provider](assets/facebook-add-identity-provider.png)

您无法单击`保存`，因为您需要从Facebook获取`Client ID`和`Client Secret`。 您需要从此页面获得的一条数据是`Redirect URI`。 当您在其中注册Keycloak作为客户端时，您必须向Facebook提供该功能，因此请将此URI复制到剪贴板。

要启用Facebook登录，首先必须在[Facebook Developer Console](https://developers.facebook.com/)中创建项目和客户端。

> Facebook经常改变Facebook Developer Console的外观和风格，因此这些指示可能并不总是最新的，配置步骤可能略有不同。

登录控制台后，屏幕右上角会出现一个下拉菜单，上面写着`My Apps`。 选择`Add a New App`菜单项。

添加新应用

![facebook add new app](assets/facebook-add-new-app.png)

选择`Website`图标。 单击`Skip and Create App ID`按钮。

创建一个新的应用程序ID

![facebook create app id](assets/facebook-create-app-id.png)

电子邮件地址和应用类别是必填字段。 完成后，您将被带到应用程序的仪表板。 单击`Settings`左侧菜单项。

创建一个新的应用程序ID

![facebook app settings](assets/facebook-app-settings.png)

单击本页末尾的`+ Add Platform`按钮，然后选择`Website`图标。 将`Redirect  URI`从Keycloak`Add identity provider`页面复制并粘贴到Facebook`Website`设置块的`Site  URL`中。

指定网站

![facebook app settings website](assets/facebook-app-settings-website.png)

在此之后，有必要公开Facebook应用程序。 单击`App Review`左侧菜单项，然后将按钮切换为`Yes`。

您还需要从此页面获取`App ID`和`App Secret`，以便将其输入Keycloak`Add identity provider`页面。 要获得此单击`Dashboard`左侧菜单项并单击`App Secret`下的`Show`。 返回Keycloak并指定这些项目，最后保存您的Facebook身份提供商。

在Facebook的`Add identity provider`页面上注释的一个配置选项是`Default Scopes`字段。 此字段允许您手动指定用户在使用此提供程序进行身份验证时必须授权的范围。 有关范围的完整列表，请查看`<https://developers.facebook.com/docs/graph-api>`。 默认情况下，Keycloak使用以下范围：`email`。

<a name="149_____12_4_3__GitHub"></a>
#### 12.4.3. GitHub

您必须完成许多步骤才能启用GitHub登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`GitHub`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![github add identity provider](assets/github-add-identity-provider.png)

您无法单击`Save`，因为您需要从GitHub获取`Client ID`和`Client Secret`。 您需要从此页面获得的一条数据是`Redirect  URI`。 当您在其中注册Keycloak作为客户端时，您必须将其提供给GitHub，因此请将此URI复制到剪贴板。

要使用GitHub启用登录，首先必须在[GitHub Developer applications](https://github.com/settings/developers)中注册一个应用程序项目。

> GitHub经常更改应用程序注册的外观，因此这些指示可能并不总是最新的，配置步骤可能略有不同。

添加新应用

![github developer applications](assets/github-developer-applications.png)

单击`Register a new application`按钮。

注册应用程序

![github register app](assets/github-register-app.png)

您必须从Keycloak`Addd Identity Provider`页面复制`Redirect URI`并将其输入到GitHub上的`Authorization callback URL` 字段 `Register a new OAuth application`页面。 完成此页面后，您将进入应用程序的管理页面。

GitHub应用页面

![github app page](assets/github-app-page.png)

您需要从此页面获取客户端ID和密码，以便将其输入Keycloak`Add identity provider`页面。 返回Keycloak并指定这些项目。

<a name="150_____12_4_4__GitLab"></a>
#### 12.4.4. GitLab

为了能够使用GitLab启用登录，您必须完成许多步骤。

首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`GitLab`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![gitlab add identity provider](assets/gitlab-add-identity-provider.png)

在单击`Save`之前，您必须从GitLab获取`Client ID`和`Client Secret`。

> 您将在稍后的步骤中使用此页面中的 `Redirect URI`，当您在其中注册Keycloak作为客户端时，您将提供给GitLab。

要使用GitLab启用登录，首先必须在[GitLab as OAuth2身份验证服务提供商](https://docs.gitlab.com/ee/integration/oauth_provider.html)中注册应用程序。

> GitLab经常更改应用程序注册的外观，因此您在GitLab站点上看到的内容可能会有所不同。 如有疑问，请参阅GitLab文档。

添加新应用

![gitlab developer applications](assets/gitlab-developer-applications.png)

从Keycloak `Add Identity Provider`页面复制`Redirect URI`，并将其输入到GitLab添加新应用程序页面的重定向URI字段中。

GitLab应用页面

![gitlab app page](assets/gitlab-app-page.png)

完成注册后，单击`Save application`。 这将打开GitLab中的应用程序管理页面。 从此页面中查找客户端ID和密码，以便您可以将其输入Keycloak `Add identity provider`页面。

完成后，返回Keycloak并输入它们。 点击`Save`。

<a name="151_____12_4_5__Google"></a>
#### 12.4.5. Google

您必须完成许多步骤才能启用Google登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Google`。 这将带您进入 `Add identity provider` 页面。

添加身份提供者

![google add identity provider](assets/google-add-identity-provider.png)

您无法单击`Save`，因为您需要从Google获取`Client ID` 和 `Client Secret`。 您需要从此页面获得的一条数据是`Redirect URI`。 当您在其中注册Keycloak作为客户端时，您必须向Google提供该功能，因此请将此URI复制到剪贴板。

要启用Google登录，您首先必须在[Google Developer Console](https://console.cloud.google.com/project)中创建项目和客户。 然后，您需要将客户端ID和密钥复制到Keycloak管理控制台。

> Google经常更改Google Developer Console的外观，因此这些说明可能并不总是最新的，配置步骤可能略有不同。

我们先来看看如何使用Google创建项目。

登录到[Google Developer Console](https://console.cloud.google.com/project)。

Google Developer Console

![google developer console](assets/google-developer-console.png)

单击`Create Project`按钮。 使用所需的`Project name`和`Project  ID`的任何值，然后单击`Create`按钮。 等待创建项目（这可能需要一段时间）。 创建后，您将被带到项目的仪表板。

仪表板

![google dashboard](assets/google-dashboard.png)

然后导航到Google Developer Console中的`APIs & Services`部分。 在该屏幕上，导航到`Credentials`管理。

当用户从Keycloak登录Google时，他们会看到Google的同意屏幕，该屏幕将询问用户是否允许Keycloak查看有关其用户个人资料的信息。 因此，在为其创建任何秘密之前，Google需要一些有关该产品的基本信息。 对于新项目，您首先要配置`OAuth consent screen`。

对于非常基本的设置，填写应用程序名称就足够了。 您还可以在此页面中设置其他详细信息，例如Google API的范围。

填写OAuth同意屏幕详细信息

![google oauth consent screen](assets/google-oauth-consent-screen.png)

下一步是创建OAuth客户端ID和客户端密钥。 回到`Credentials`管理，导航到`Credentials`tab并在`Create credentials`按钮下选择`OAuth client ID`。

创建凭据

![google create credentials](assets/google-create-credentials.png)

然后，您将进入`Create OAuth client ID`页面。 选择`Web application`作为应用程序类型。 指定客户端所需的名称。 您还需要将`Redirect URI`从Keycloak `Add Identity Provider`页面复制并粘贴到 `Authorized redirect URIs`字段中。 完成后，单击`Create`按钮。

创建OAuth客户端ID

![google create oauth id](assets/google-create-oauth-id.png)

单击`Create`后，您将进入`Credentials`页面。 点击新的OAuth 2.0客户端ID可查看新Google客户端的设置。

Google客户端凭据

![google client credentials](assets/google-client-credentials.png)

您需要从此页面获取客户端ID和密码，以便将其输入Keycloak `Add identity provider`页面。 返回Keycloak并指定这些项目。

在Google的`Add identity provider`页面上注明的一个配置选项是`Default Scopes`字段。 此字段允许您手动指定用户在使用此提供程序进行身份验证时必须授权的范围。 有关范围的完整列表，请查看`<https://developers.google.com/oauthplayground/>`。 默认情况下，Keycloak使用以下范围：`openid` `profile` `email`。

如果您的组织使用G Suite并且您希望仅限制对组织成员的访问，则必须将用于G Suite的域输入`Hosted Domain`字段以启用它。

<a name="152_____12_4_6__LinkedIn"></a>
#### 12.4.6. LinkedIn

您必须完成许多步骤才能启用LinkedIn登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`LinkedIn`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![linked in add identity provider](assets/linked-in-add-identity-provider.png)

您无法单击`Save`，因为您需要从LinkedIn获取`Client ID`和`Client Secret`。 您需要从此页面获得的一条数据是`Redirect  URI`。 当您在其中注册Keycloak作为客户端时，您必须向LinkedIn提供该功能，因此请将此URI复制到剪贴板。

要启用LinkedIn登录，首先必须在[LinkedIn开发人员网络](https://www.linkedin.com/developer/apps)中创建应用程序。

> LinkedIn可能会更改应用程序注册的外观，因此这些说明可能并不总是最新的。

开发者网络

![linked in developer network](assets/linked-in-developer-network.png)

单击`Create Application`按钮。 这将带您进入`Create a New Application`页面。

创建应用程序

![linked in create app](assets/linked-in-create-app.png)

使用适当的值填写表单，然后单击`Submit`按钮。 这将带您进入新应用程序的设置页面。

应用设置

![linked in app settings](assets/linked-in-app-settings.png)

在`Default Application Permissions`部分中选择`r_basicprofile`和`r_emailaddress`。 您必须从Keycloak `Add Identity Provider`页面复制`Redirect URI`并将其输入到LinkedIn应用程序设置页面上的`OAuth 2.0` `Authorized Redirect URLs`字段中。 执行此操作后，请不要忘记单击`Update`按钮！

然后，您需要从此页面获取客户端ID和密码，以便将其输入Keycloak `Add identity provider`页面。 返回Keycloak并指定这些项目。

<a name="153_____12_4_7__Microsoft"></a>
#### 12.4.7. Microsoft

您必须完成许多步骤才能启用Microsoft登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Microsoft`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![microsoft add identity provider](assets/microsoft-add-identity-provider.png)

您无法单击`Save`，因为您需要从Microsoft获取`Client  ID` 和 `Client Secret`。 您需要从此页面获得的一条数据是`Redirect URI`。 当您在其中注册Keycloak作为客户端时，您必须向Microsoft提供此功能，因此请将此URI复制到剪贴板。

要启用使用Microsoft帐户登录，首先必须在Microsoft注册OAuth应用程序。 转到[Microsoft应用程序注册](https://account.live.com/developers/applications/create)URL。

> Microsoft经常更改应用程序注册的外观，因此这些说明可能并不总是最新的，配置步骤可能略有不同。

注册应用程序

![microsoft app register](assets/microsoft-app-register.png)

输入应用程序名称，然后单击`Create application`。 这将带您进入新应用程序的应用程序设置页面。

设置

![microsoft app settings](assets/microsoft-app-settings.png)

您必须从Keycloak`Addd Identity Provider`页面复制`Redirect URI`并将其添加到Microsoft应用程序页面上的`Redirect URIs`字段中。 一定要点击`Add Url`按钮并`Save`您的更改。

最后，您需要从此页面获取应用程序ID和密码，以便您可以在Keycloak `Add identity provider`页面上输入它们。 返回Keycloak并指定这些项目。

> 从2018年11月起，Microsoft将取消对Live SDK API的支持，转而使用新的Microsoft Graph API。 Keycloak Microsoft身份提供程序已更新为使用新端点，因此请确保升级到Keycloak 4.6.0或更高版本才能使用此提供程序。 此外，在“Live SDK应用程序”下向Microsoft注册的客户端应用程序需要在[Microsoft应用程序注册](https://account.live.com/developers/applications/create)门户中重新注册才能获取应用程序ID 与Microsoft Graph API兼容。

<a name="154_____12_4_8__OpenShift"></a>
#### 12.4.8. OpenShift

> OpenShift Online目前处于开发者预览模式。 本文档基于本地安装和本地`minishift`开发环境。

您需要完成几个步骤才能启用OpenShift登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`OpenShift`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![openshift add identity provider](assets/openshift-add-identity-provider.png)

注册OAuth客户端

您可以使用`oc`命令行工具注册您的客户端。

```bash
$ oc create -f <(echo '
kind: OAuthClient
apiVersion: v1
metadata:
 name: kc-client 
secret: "..." 
redirectURIs:
 - "http://www.example.com/" 
grantMethod: prompt 
')
```

> 您的OAuth客户端的`name`。 在向`*<openshift_master>*/oauth/authorize` 和 `*<openshift_master>*/oauth/token`发出请求时，将其作为`client_id`请求参数传递。

> `secret`用作`client_secret`请求参数。

> 在对`*<openshift_master>*/oauth/authorize` 和 `*<openshift_master>*/oauth/token`的请求中指定的`redirect_uri`参数必须等于（或以前缀为）`redirectURIs`中的一个URI。

> `grantMethod`用于确定当此客户端请求令牌并且尚未被用户授予访问权时要采取的操作。

使用`oc create`命令定义的客户机ID和密码将它们输入Keycloak `Addd identity provider` 页面。 返回Keycloak并指定这些项目。

有关更多详细指南，请参阅[官方OpenShift文档](https://docs.okd.io/latest/architecture/additional_concepts/authentication.html#oauth)。

<a name="155_____12_4_9__PayPal"></a>
#### 12.4.9. PayPal

您必须完成许多步骤才能启用PayPal登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`PayPal`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![paypal add identity provider](assets/paypal-add-identity-provider.png)

您无法单击`Save`，因为您需要从PayPal获取`Client ID`和`Client Secret`。 您需要从此页面获得的一条数据是`Redirect URI`。 当您在其中注册Keycloak作为客户端时，您必须将其提供给PayPal，因此请将此URI复制到剪贴板。

要使用PayPal启用登录，首先必须在[PayPal开发人员应用程序](https://developer.paypal.com/developer/applications)中注册应用程序项目。

添加新应用

![paypal developer applications](assets/paypal-developer-applications.png)

单击 `Create App` 按钮。

注册应用程序

![paypal register app](assets/paypal-register-app.png)

现在，您将进入应用程序设置页面。

进行以下更改

- 选择配置Sandbox或Live（如果尚未在`Add identity provider` 页面上启用`Target Sandbox`开关，请选择“Live (实时)”）
- 复制客户端ID和密码，以便将它们粘贴到Keycloak `Add identity provider`页面。
- 向下滚动到 `App Settings`
- 从Keycloak `Addd Identity Provider`页面复制 `Redirect URI` 并将其输入到 `Return URL`字段中。
- 选中 `Log In with PayPal` 复选框。
- 检查个人信息部分下的 `Full name` 复选框。
- 检查地址信息部分下的 `Email address` 复选框。
- 添加指向您域中相应页面的隐私和用户协议URL。

<a name="156_____12_4_10__Stack_Overflow"></a>
#### 12.4.10. Stack Overflow

为了能够使用Stack Overflow启用登录，您必须完成许多步骤。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Stack Overflow`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![stack overflow add identity provider](assets/stack-overflow-add-identity-provider.png)

要使用Stack Overflow启用登录，首先必须在[StackApps](https://stackapps.com/)上注册OAuth应用程序。 转到[在Stack Apps上注册您的应用程序](https://stackapps.com/apps/oauth/register) URL并登录。

> 堆栈溢出通常会更改应用程序注册的外观，因此这些指示可能并不总是最新的，配置步骤可能略有不同。

注册应用程序

![stack overflow app register](assets/stack-overflow-app-register.png)

输入应用程序的应用程序名称和OAuth域名，然后单击`Register your Application`。 输入您想要的其他项目。

设置

![stack overflow app settings](assets/stack-overflow-app-settings.png)

最后，您需要从此页面获取客户端ID，密钥和密钥，以便您可以在Keycloak `Add identity provider` 页面上输入它们。 返回Keycloak并指定这些项目。

<a name="157_____12_4_11__Twitter"></a>
#### 12.4.11. Twitter

您必须完成许多步骤才能启用Twitter登录。 首先，转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`Twitter`。 这将带您进入 `Add identity provider` 页面。

添加身份提供者

![twitter add identity provider](assets/twitter-add-identity-provider.png)

您无法单击`Save`，因为您需要从Twitter获取 `Client ID` 和 `Client Secret`。 您需要从此页面获得的一条数据是`Redirect URI`。 当您在其中注册Keycloak作为客户端时，您必须将其提供给Twitter，因此请将此URI复制到剪贴板。

要使用Twtter启用登录，首先必须在[Twitter应用程序管理](https://developer.twitter.com/apps/)中创建应用程序。

注册应用程序

![twitter app register](assets/twitter-app-register.png)

单击 `Create New App` 按钮。 这将带您进入 `Create an Application` 页面。

注册应用程序

![twitter app create](assets/twitter-app-create.png)

输入名称和描述。 网站可以是任何东西，但不能有`localhost`地址。 对于`Callback URL`，您必须从Keycloak `Addd Identity Provider` 页面复制`Redirect URI`。

> 你不能在`Callback URL`中使用`localhost`。 如果您尝试在笔记本电脑上试驾Twitter登录，请将其替换为`127.0.0.1`。

单击保存后，您将进入 `Details` 页面。

应用详情

![twitter details](assets/twitter-details.png)

接下来转到 `Keys and Access Tokens` 选项卡。

密钥和访问令牌

![twitter keys](assets/twitter-keys.png)

最后，您需要从此页面获取API密钥和密钥，并将它们复制回Keycloak `Add identity provider` 页面上的 `Client ID` 和 `Client Secret`字段。

<a name="158____12_5__OpenID_Connect_v1_0_身份提供商"></a>
### 12.5. OpenID Connect v1.0 身份提供商

Keycloak可以基于OpenID Connect协议来代理身份提供商。 这些IDP必须支持规范定义的[授权代码流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc)，以便对用户进行身份验证并授权访问。

要开始配置OIDC提供程序，请转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`OpenID Connect v1.0`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![oidc add identity provider](assets/oidc-add-identity-provider.png)

此常规配置选项在[常规IDP配置](https://www.keycloak.org/docs/latest/server_admin/index.html#_general-idp-config)中介绍。 您还必须定义OpenID Connect配置选项。 它们基本上描述了您正在与之通信的`OIDC IDP`。

| 配置            | 描述                                                  |
| :----------------------- | :----------------------------------------------------------- |
| Authorization URL        | OIDC协议所需的授权URL端点。    |
| Token URL                | OIDC协议所需的令牌URL端点。 |
| Logout URL               | OIDC协议中定义的注销URL端点。 该值是可选的。 |
| Backchannel Logout       | Backchannel注销是IDP的后台带外REST调用，用于注销用户。 一些IDP只能通过浏览器重定向执行注销，因为它们可能只能通过浏览器cookie识别会话。 |
| User Info URL            | 用户信息由OIDC协议定义的URL端点。 这是可以从中下载用户配置文件信息的端点。 |
| Client ID                | 该领域将充当外部IDP的OIDC客户端。 使用授权代码流与外部IDP交互时，您的领域将需要OIDC客户端ID。 |
| Client Secret            | 在使用授权代码流时，此领域需要使用客户端密钥。 |
| Issuer                   | 国内流离失所者的回应可能包含发行人索赔。 此配置值是可选的。 如果指定，此声明将根据您提供的值进行验证。 |
| Default Scopes           | 以空格分隔的OIDC范围列表，以便与身份验证请求一起发送。 默认为`openid`。 |
| Prompt                   | 另一个可选开关。 这是OIDC规范定义的提示参数。 通过它，您可以强制重新身份验证和其他选项。 有关详细信息，请参阅规范。 |
| Validate Signatures      | 另一个可选开关。 这是为了指定Keycloak是否将验证由此身份提供者签名的外部ID令牌上的签名。 如果启用此选项，Keycloak将需要知道外部OIDC身份提供程序的公钥。 请参阅下文，了解如何进行设置。 警告：出于性能目的，Keycloak会缓存外部OIDC身份提供程序的公钥。 如果您认为您的身份提供商的私钥遭到破坏，那么更新密钥显然很好，但清除密钥缓存也很好。 有关详细信息，请参阅[清除缓存](https://www.keycloak.org/docs/latest/server_admin/index.html#_clear-cache) 部分。 |
| Use JWKS URL             | 如果启用 `Validate Signatures`，则适用。 如果开关打开，则将从给定的JWKS URL下载身份提供者公钥。 这允许极大的灵活性，因为当身份提供者生成新的密钥对时，将始终重新下载新密钥。 如果交换机关闭，则使用Keycloak DB中的公钥（或证书），因此每当身份提供程序密钥对更改时，您始终需要将新密钥导入Keycloak DB。 |
| JWKS URL                 | 存储身份提供程序JWK密钥的URL。 有关详细信息，请参阅[JWK规范](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html)。 如果您使用外部Keycloak作为身份提供者，那么您可以使用URL，如`http://broker-keycloak:8180/auth/realms/test/protocol/openid-connect/certs`，假设您的代理密钥泄露正在运行http://broker-keycloak:8180](http://broker-keycloak:8180/)，它的领域是`test`。 |
| Validating Public Key    | 如果 `Use JWKS URL` 已关闭，则适用。 以下是PEM格式的公钥，必须用于验证外部IDP签名。 |
| Validating Public Key Id | 如果 `Use JWKS URL` 已关闭，则适用。 该字段以PEM格式指定公钥的ID。 此配置值是可选的。 由于没有从密钥计算密钥ID的标准方法，因此各种外部身份提供商可能使用Keycloak中的不同算法。 如果未指定此字段的值，则无论外部IDP发送的密钥ID如何，上面指定的验证公钥都将用于所有请求。 设置时，此字段的值用作Keycloak用于验证来自此类提供程序的签名的密钥ID，并且必须与IDP指定的密钥ID匹配。 |

您还可以通过提供指向OpenID提供程序元数据的URL或文件来导入所有此配置数据（请参阅OIDC发现规范）。 如果要连接到Keycloak外部IDP，则可以从URL `<root>/auth/realms/{realm-name}/.well-known/openid-configuration` 导入IDP设置。 此链接是一个JSON文档，描述有关IDP的元数据。

<a name="159____12_6__SAML_v2_0_身份提供商"></a>
### 12.6. SAML v2.0 身份提供商

Keycloak可以基于SAML v2.0协议代理身份提供商。

要开始配置SAML v2.0提供程序，请转到`Identity Providers`左侧菜单项，然后从`Add provider`下拉列表中选择`SAML v2.0`。 这将带您进入`Add identity provider`页面。

添加身份提供者

![saml add identity provider](assets/saml-add-identity-provider.png)

此常规配置选项在[常规IDP配置](https://www.keycloak.org/docs/latest/server_admin/index.html#_general-idp-config)中介绍。 您还必须定义SAML配置选项。 它们基本上描述了您正在与之通信的`SAML IDP`。

| 配置                      | 描述                                                  |
| :--------------------------------- | :----------------------------------------------------------- |
| Single Sign-On Service URL         | 这是必填字段，指定SAML端点以启动身份验证过程。 如果您的SAML IDP发布IDP实体描述符，则将在此处指定此字段的值。 |
| Single Logout Service URL          | 这是一个可选字段，用于指定SAML注销端点。 如果您的SAML IDP发布IDP实体描述符，则将在此处指定此字段的值。 |
| Backchannel Logout                 | 如果您的SAML IDP支持反向信道注销，则启用。 |
| NameID Policy Format               | 指定与名称标识符格式对应的URI引用。 默认为 `urn:oasis:names:tc:SAML:2.0:nameid-format:persistent`。 |
| HTTP-POST Binding Response         | 当这个领域响应外部IDP发送的任何SAML请求时，应该使用哪个SAML绑定？ 如果设置为`off`，则将使用Redirect Binding。 |
| HTTP-POST Binding for AuthnRequest | 当此领域从外部SAML IDP请求身份验证时，应使用哪个SAML绑定？ 如果设置为`off`，则将使用Redirect Binding。 |
| Want AuthnRequests Signed          | 如果为true，它将使用领域的密钥对来签署发送到外部SAML IDP的请求。 |
| Signature Algorithm                | 如果启用了 `Want AuthnRequests Signed` ，那么您也可以选择要使用的签名算法。 |
| SAML Signature Key Name            | 通过POST绑定发送的签名SAML文档包含`KeyName`元素中的签名密钥的标识。 默认情况下，此项包含Keycloak密钥ID。 但是，各种外部SAML IDP可能需要不同的密钥名称或根本没有密钥名称。 此开关控制`KeyName`是否包含密钥ID（选项`KEY_ID`），来自对应于领域密钥的证书（选项`CERT_SUBJECT` - 例如Microsoft Active Directory联合服务预期），或者密钥名称提示是完全的 从SAML消息中省略（选项`NONE`）。 |
| Force Authentication               | 表示即使用户已经登录，也会强制用户在外部IDP上输入凭据。 |
| Validate Signature                 | 该域是否应该期望来自外部IDP的SAML请求和响应被数字签名。 强烈建议你打开它！ |
| Validating X509 Certificate        | 将用于验证来自外部IDP的SAML请求和响应的签名的公共证书。 |

You can also import all this configuration data by providing a URL or file that points to the SAML IDP entity descriptor of the external IDP. If you are connecting to a Keycloak external IDP, you can import the IDP settings from the URL `<root>/auth/realms/{realm-name}/protocol/saml/descriptor`. This link is an XML document describing metadata about the IDP.

You can also import all this configuration data by providing a URL or XML file that points to the entity descriptor of the external SAML IDP you want to connect to.

<a name="160_____12_6_1__SP描述符"></a>
#### 12.6.1. SP描述符
创建SAML提供程序后，在查看该提供程序时会出现一个`EXPORT`按钮。 单击此按钮将导出SAML SP实体描述符，您可以使用该描述符导入外部SP。

此元数据也可通过转到URL公开获得。

```javascript
http[s]://{host:port}/auth/realms/{realm-name}/broker/{broker-alias}/endpoint/descriptor
```

<a name="161____12_7__客户建议身份提供商"></a>
### 12.7. 客户建议身份提供商

OIDC应用程序可以通过指定他们想要使用哪个身份提供者的提示来绕过Keycloak登录页面。

这是通过在授权代码流授权端点中设置`kc_idp_hint`查询参数来完成的。

Keycloak OIDC客户端适配器还允许您在应用程序中访问受保护资源时指定此查询参数。

例如：

```javascript
GET /myapplication.com?kc_idp_hint=facebook HTTP/1.1
Host: localhost:8080
```

在这种情况下，预计您的领域有一个带有别名`facebook`的身份提供者。 如果此提供程序不存在，将显示登录表单。

如果您使用`keycloak.js`适配器，您也可以实现相同的行为：

```javascript
var keycloak = new Keycloak('keycloak.json');

keycloak.createLoginUrl({
        idpHint: 'facebook'
});
```

`kc_idp_hint`查询参数还允许客户端覆盖默认身份提供者（如果为`Identity Provider Redirector`身份验证器配置了一个身份提供者）。 客户端还可以通过将`kc_idp_hint`查询参数设置为空值来禁用自动重定向。

<a name="162____12_8__映射声明和断言"></a>
### 12.8. 映射声明和断言

您可以将要进行身份验证的外部IDP提供的SAML和OpenID Connect元数据导入到领域的环境中。 这允许您提取用户配置文件元数据和其他信息，以便您可以将其提供给您的应用程序。

通过外部身份提供者登录您的领域的每个新用户将根据SAML或OIDC断言和声明中的元数据，在本地Keycloak数据库中创建一个条目。

如果您点击您所在领域的“身份提供商”页面中列出的身份提供商，您将被带到IDP `Settings` 标签。 在这个页面上还有一个`Mappers`选项卡。 单击该选项卡以开始映射传入的IDP元数据。

![identity provider mappers](assets/identity-provider-mappers.png)

这个页面上有一个 `Create` 按钮。 单击此创建按钮可以创建代理映射器。 Broker mappers可以将SAML属性或 `OIDC ID/Access` 令牌声明导入用户属性和用户角色映射。

![identity provider mapper](assets/identity-provider-mapper.png)

从`Mapper Type`列表中选择一个映射器。 将鼠标悬停在工具提示上可查看映射器的功能说明。 工具提示还描述了您需要输入的配置信息。 单击`Save`，将添加新的映射器。

对于基于JSON的声明，您可以使用点表示法进行嵌套，使用方括号来按索引访问数组字段。 例如`contact.address[0].country`。

要调查社交提供程序提供的用户配置文件JSON数据的结构，您可以启用`DEBUG`级别记录器`org.keycloak.social.user_profile_dump`。 这是在服务器的app-server配置文件（domain.xml或standalone.xml）中完成的。

<a name="163____12_9__可用的用户会话数据"></a>
### 12.9. 可用的用户会话数据

用户从外部IDP登录后，Keycloak会存储一些您可以访问的其他用户会话记录数据。 此数据可以传播到客户端，通过令牌请求登录，或者使用适当的客户端映射器将SAML断言传递回客户端。

- identity_provider

  这是用于执行登录的代理的IDP别名。

- identity_provider_identity

  这是当前经过身份验证的用户的IDP用户名。 这通常与Keycloak用户名相同，但不一定需要。 例如，Keycloak用户`john`可以链接到Facebook用户`john123@gmail.com`，因此在这种情况下，用户会话注释的值将是`john123@gmail.com`。

您可以使用类型为 `User Session Note` 的[协议映射器](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers)将此信息传播给您的客户端。

<a name="164____12_10__首次登录流程"></a>
### 12.10. 首次登录流程

当用户通过身份代理登录时，会在域的本地数据库中导入和链接用户的某些方面。 当Keycloak通过外部身份提供商成功验证用户时，可能存在两种情况：

- 已导入Keycloak用户帐户并与经过身份验证的身份提供商帐户关联。 在这种情况下，Keycloak将仅作为现有用户进行身份验证并重定向回应用程序。
- 尚未为此外部用户导入和链接现有的Keycloak用户帐户。 通常，您只想注册并将新帐户导入Keycloak数据库，但如果现有的Keycloak帐户使用相同的电子邮件，该怎么办？ 自动将现有本地帐户链接到外部身份提供商是一个潜在的安全漏洞，因为您无法始终信任从外部身份提供商处获得的信息。

在处理上面列出的一些冲突和情况时，不同的组织有不同的要求。 为此，IDP设置中有一个`First Login Flow`选项，允许您选择[工作流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_authentication-flows)用户首次从外部IDP登录后使用。 默认情况下，它指向 `first broker login` 流，但您可以配置和使用自己的流，并为不同的身份提供者使用不同的流。

流本身在管理控制台的 `Authentication` 选项卡下配置。 当您选择 `First Broker Login` 流程时，您将看到默认情况下使用的验证器。 您可以重新配置现有流。 （例如，您可以禁用某些验证器，将其中一些标记为`required`，配置一些验证器等）。

您还可以创建新的身份验证流和/或编写自己的身份验证器实现，并在流中使用它。 有关详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="165_____12_10_1__默认首次登录流程"></a>
#### 12.10.1. 默认首次登录流程
让我们描述`First Broker Login`流程提供的默认行为。

- Review Profile(回顾概要)

  此验证器可能会显示配置文件信息页面，用户可以在其中查看从身份提供商处检索到的配置文件。 验证器是可配置的。 您可以设置`Update Profile On First Login`选项。 当`On`时，将始终向用户显示要查询其他信息的个人资料页面，以便联合他们的身份。 当`missing`时，只有在身份提供者未提供某些必需信息（电子邮件，名字，姓氏）时，才会向用户显示个人资料页面。 如果`Off`，则不会显示配置文件页面，除非用户在`Review profile info`链接的后续阶段点击（后续阶段显示的页面为`Confirm Link Existing Account`验证者）。

- Create User If Unique(创建用户如果唯一)

  此身份验证器检查是否已存在具有相同电子邮件或用户名的现有Keycloak帐户，例如来自身份提供商的帐户。 如果不是，那么验证者只需创建一个新的本地Keycloak帐户并将其与身份提供者链接，整个流程就完成了。 否则它将转到下一个`Handle Existing Account`子流。 如果您始终希望确保没有重复的帐户，则可以将此身份验证器标记为`REQUIRED`。 在这种情况下，如果存在现有的Keycloak帐户，则用户将看到错误页面，并且用户需要通过帐户管理链接其身份提供商帐户。

- Confirm Link Existing Account(确认链接现有帐户)

  在信息页面上，用户将看到存在具有相同电子邮件的现有Keycloak帐户。 他们可以再次查看他们的个人资料并使用不同的电子邮件或用户名（重新启动流程并返回`Review Profile`身份验证器）。 或者他们可以确认他们想要将他们的身份提供者帐户与他们现有的Keycloak帐户相关联。 如果您不希望用户看到此确认页面，请禁用此身份验证器，但直接通过电子邮件验证或重新身份验证链接身份提供商帐户。

- Verify Existing Account By Email(通过电子邮件验证现有帐户)

  默认情况下，此身份验证器为`ALTERNATIVE`，因此仅在域配置了SMTP设置时才使用它。 它将向用户发送电子邮件，在那里他们可以确认他们想要将身份提供者与他们的Keycloak帐户相关联。 如果您不想通过电子邮件确认链接，请禁用此选项，但您始终希望用户使用其密码（以及OTP）重新进行身份验证。

- 通过重新身份验证验证现有帐户

  如果禁用或不可用电子邮件身份验证器（SMTP未配置为域），则使用此身份验证器。 它将显示一个登录屏幕，用户需要使用其密码进行身份验证，以将其Keycloak帐户与身份提供商进行链接。 用户还可以使用某些不同的身份提供程序重新进行身份验证，该提供程序已与其Keycloak帐户相关联。 您还可以强制用户使用OTP。 否则它是可选的，仅在已为用户帐户设置OTP时使用。

<a name="166_____12_10_2__自动链接现有的第一个登录流程"></a>
#### 12.10.2. 自动链接现有的第一个登录流程
> 在用户可以使用任意`用户名/电子邮件地址`注册自己的通用环境中，AutoLink身份验证器会很危险。 除非精心策划用户注册并分配`用户名/电子邮件地址`，否则请勿使用此身份验证器。

要配置第一个登录流，用户在不提示的情况下自动链接，请使用以下两个身份验证器创建新流：

- Create User If Unique(创建用户如果唯一)

  此身份验证器可确保处理唯一用户。 将验证者要求设置为"Alternative(备选)"。

- Automatically Link Brokered Account(自动链接经纪人账户)

  使用此身份验证器自动链接代理身份而无需任何验证。 这在多个用户数据库的Intranet环境中非常有用，每个用户数据库都有重叠的`用户名/电子邮件地址`，但密码不同，并且您希望允许用户使用任何密码而无需验证。 如果您管理所有内部数据库，并且来自与另一个数据库中的`用户名/电子邮件地址`匹配的`用户名/电子邮件地址`属于同一个人，则这是合理的。 将验证者要求设置为"Alternative(备选)"。

> 所描述的设置使用两个验证器，并且是最简单的验证器，但可以根据您的需要使用其他验证器。 例如，如果您仍希望最终用户确认其配置文件信息，则可以将审阅配置文件身份验证器添加到流的开头。

<a name="167____12_11__检索外部IDP令牌"></a>
### 12.11. 检索外部IDP令牌

Keycloak允许您使用外部IDP存储令牌和来自身份验证过程的响应。 为此，您可以在IDP的设置页面上使用`Store Token`配置选项。

应用程序代码可以检索这些令牌和响应以提取额外的用户信息，或安全地调用外部IDP上的请求。 例如，应用程序可能希望使用Google令牌来调用其他Google服务和REST API。 要检索特定身份提供者的令牌，您需要发送请求，如下所示：

```javascript
GET /auth/realms/{realm}/broker/{provider_alias}/token HTTP/1.1
Host: localhost:8080
Authorization: Bearer <KEYCLOAK ACCESS TOKEN>
```

应用程序必须已通过Keycloak进行身份验证并已收到访问令牌。 此访问令牌需要设置`broker`客户端级别角色`read-token`。 这意味着用户必须具有此角色的角色映射，并且客户端应用程序必须在其范围内具有该角色。 在这种情况下，假设您在Keycloak中访问受保护的服务，则需要在用户身份验证期间发送Keycloak发出的访问令牌。 在代理配置页面中，您可以通过打开`Stored Tokens Readable`开关，自动将此角色分配给新导入的用户。

可以通过再次通过提供程序登录或使用客户端启动的帐户链接API重新建立这些外部令牌。

<a name="168____12_12__身份代理注销"></a>
### 12.12. 身份代理注销

当触发从Keycloak注销时，Keycloak将向用于登录Keycloak的外部身份提供者发送请求，并且该用户也将从该身份提供者注销。 可以跳过此行为并避免在外部身份提供程序中注销。 有关详细信息，请参阅[适配器注销文档](https://www.keycloak.org/docs/6.0/securing_apps/#_java_adapter_logout)。

<a name="169___13__用户会话管理"></a>
## 13. 用户会话管理

当用户登录领域时，Keycloak会为他们维护一个用户会话，并记住他们在会话中访问过的每个客户端。 领域管理员可以对这些用户会话执行许多管理功能。 他们可以查看整个领域的登录统计信息，并深入到每个客户端以查看谁登录以及在哪里登录。 管理员可以从管理控制台注销用户或用户组。 他们可以撤销令牌并在那里设置所有令牌和会话超时。

<a name="170____13_1__管理会话"></a>
### 13.1. 管理会话

如果您转到`Sessions`左侧菜单项，您可以看到该领域当前活动的会话数的顶级视图。

Sessions

![sessions](assets/sessions.png)

给出了客户端列表以及当前为该客户端提供的活动会话数。 您还可以通过单击此列表右侧的`Logout all`按钮注销域中的所有用户。

<a name="171_____13_1_1__退出所有限制"></a>
#### 13.1.1. 退出所有限制
任何SSO cookie集现在都将无效，并且在活动浏览器会话中请求身份验证的客户端现在必须重新登录。 只有某些客户端会收到此注销事件的通知，特别是使用Keycloak OIDC客户端适配器的客户端。 其他客户端类型（即SAML）将不会收到反向信道注销请求。

重要的是要注意，单击“全部注销”不会撤消任何未完成的访问令牌。 他们必须自然到期。 您必须将[撤销策略](https://www.keycloak.org/docs/latest/server_admin/index.html#_revocation-policy)推送到客户端，但这也仅适用于使用Keycloak OIDC客户端的客户端 适配器。

<a name="172_____13_1_2__应用追溯"></a>
#### 13.1.2. 应用追溯
在`Sessions`页面上，您还可以深入查看每个客户端。 这将带您进入该客户端的`Sessions`选项卡。 单击`Show Sessions`按钮，可以查看哪些用户登录到该应用程序。

Application Sessions(应用程序会话)

![application sessions](assets/application-sessions.png)

<a name="173_____13_1_3__用户追溯"></a>
#### 13.1.3. 用户追溯
如果您转到单个用户的`Sessions`选项卡，您还可以查看会话信息。

User Sessions(用户会话)

![user sessions](assets/user-sessions.png)

<a name="174____13_2__撤销策略"></a>
### 13.2. 撤销策略

如果您的系统遭到入侵，您将需要一种方法来撤销所有会话并访问已分发的令牌。 您可以通过转到`Sessions`屏幕的`Revocation`选项卡来完成此操作。

Revocation(撤销)

![revocation](assets/revocation.png)

您只能设置基于时间的撤销策略。 控制台允许您指定在该时间和日期之前发出的任何会话或令牌无效的时间和日期。 `Set to now`将策略设置为当前时间和日期。 `Push`按钮会将此撤销策略推送到任何已安装Keycloak OIDC客户端适配器的已注册OIDC客户端。

<a name="175____13_3__会话和令牌超时"></a>
### 13.3. 会话和令牌超时

Keycloak为您提供对会话，cookie和令牌超时的精细控制。 这都是在`Realm Settings`左侧菜单项的`Tokens`选项卡上完成的。

Tokens Tab

![tokens tab](assets/tokens-tab.png)

让我们来看看这个页面上的每个项目。

| 配置                           | 描述                                                  |
| :-------------------------------------- | :----------------------------------------------------------- |
| Revoke Refresh Token                    | 对于正在执行刷新令牌流的OIDC客户端，此标志（如果启用）将撤消该刷新令牌，并发出另一个请求客户端必须使用的令牌。 这基本上意味着刷新令牌具有一次性使用。 |
| SSO Session Idle                        | 也适用于OIDC客户。 如果用户的活动时间超过此超时时间，则用户会话将失效。 如何检查空闲时间？ 请求身份验证的客户端将阻止空闲超时。 刷新令牌请求也会影响空闲超时。 在会话实际无效之前，始终会有一个小的时间窗口添加到空闲超时（请参阅下面的注释）。 |
| SSO Session Max                         | 用户会话到期和无效之前的最长时间。 这是一个艰难的数字和时间。 它控制用户会话保持活动状态的最长时间，无论活动如何。 |
| SSO Session Idle Remember Me            | 与标准SSO会话空闲配置相同，但特定于登录并记住我已启用。 当在登录过程中选择记住我时，它允许规定更长的会话空闲超时。 它是可选配置，如果未设置为大于0的值，则使用SSO会话空闲配置中设置的相同空闲超时。 |
| SSO Session Max Remember Me             | 与标准SSO会话最大值相同，但特定于登录并记住我已启用。 当在登录过程中选择记住我时，它允许规定更长寿的会话。 它是可选配置，如果未设置为大于0的值，则使用SSO会话最大配置中设置的相同会话生命周期。 |
| Offline Session Idle                    | 对于[离线访问](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access)，这是在撤消脱机令牌之前允许会话保持空闲的时间。 在会话实际无效之前，始终会有一个小的时间窗口添加到空闲超时（请参阅下面的注释）。 |
| Offline Session Max Limited             | 对于[离线访问](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access)，如果启用此标志，则启用Offline Session Max以控制脱机令牌的最长时间 无论活动如何，都可以保持活跃状态 |
| Offline Session Max                     | 对于[离线访问](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access)，这是撤消相应脱机令牌之前的最长时间。 这是一个艰难的数字和时间。 它控制脱机令牌保持活动状态的最长时间，无论活动如何。 |
| Access Token Lifespan                   | 创建OIDC访问令牌时，此值会影响到期时间。 |
| Access Token Lifespan For Implicit Flow | 使用Implicit Flow，不提供刷新令牌。 因此，使用Implicit Flow创建的访问令牌会有单独的超时。 |
| Client login timeout                    | 这是客户端在OIDC中完成授权代码流的最长时间。 |
| Login timeout                           | 登录必须花费的总时间。 如果身份验证的时间超过此时间，则用户必须启动身份验证过程。 |
| Login action timeout                    | 用户在身份验证过程中可以在任何一个页面上花费的最长时间。 |
| User-Initiated Action Lifespan          | 用户发送的动作许可证（例如，忘记密码电子邮件）之前的最长时间已过期。 建议此值较短，因为预计用户会快速响应自行创建的操作。 |
| Default Admin-Initiated Action Lifespan | 管理员向用户发送操作许可证之前的最长时间已过期。 建议此值很长，以允许管理员为当前处于脱机状态的用户发送电子邮件。 在发出令牌之前，可以覆盖默认超时。 |
| Override User-Initiated Action Lifespan | 允许每次操作具有独立超时的可能性（例如，电子邮件验证，忘记密码，用户操作和身份提供商电子邮件验证）。 此字段不是必需的，如果未指定任何内容，则默认为 *User-Initiated Action Lifespan* 中配置的值。 |

> 对于空闲超时，会有一个小的时间窗口（2分钟），在此期间会话保持未到期。 例如，当您将超时设置为30分钟时，它实际上将在会话过期前32分钟。 对于集群和跨数据中心环境中的某些角落情况，如果令牌在到期前的一个集群节点上刷新很短的时间，而其他集群节点在此期间错误地将会话视为 已过期，因为他们尚未从执行刷新的节点收到有关成功刷新的消息。

<a name="176____13_4__离线访问"></a>
### 13.4. 离线访问

脱机访问是[OpenID Connect规范](https://openid.net/specs/openid-connect-core-1_0.html#OfflineAccess)中描述的功能。 我们的想法是，在登录期间，您的客户端应用程序将请求脱机令牌而不是经典的刷新令牌。 应用程序可以将此脱机令牌保存在数据库或磁盘上，即使用户已注销，也可以在以后使用它。 如果您的应用程序需要代表用户执行某些"offline"操作，即使用户不在线，这也很有用。 一个例子是每晚定期备份一些数据。

您的应用程序负责将脱机令牌保存在某个存储（通常是数据库）中，然后使用它从Keycloak服务器手动检索新的访问令牌。

经典刷新令牌和离线令牌之间的区别在于，离线令牌在默认情况下永不过期，并且不受 `SSO Session Idle timeout` 和 `SSO Session Max lifespan` 的影响。即使在用户注销或服务器重新启动后，脱机令牌仍然有效。但是，默认情况下，您需要至少每30天使用脱机令牌进行一次刷新令牌操作（此值，`Offline Session Idle timeout`，可以在管理控制台中的`Tokens`选项卡中更改为`Realm Settings`设置）。此外，如果启用`Offline Session Max Limited`选项，则脱机令牌将在60天后过期，无论使用脱机令牌进行刷新令牌操作（此值，`Offline Session Max lifespan`，也可以在“领域设置”下的“令牌”选项卡中的管理控制台。此外，如果启用`Revoke refresh tokens`选项，则每个脱机令牌只能使用一次。因此，刷新后，您始终需要将刷新响应中的新脱机令牌存储到您的数据库中，而不是之前的数据库中。

用户可以在[用户帐户服务](https://www.keycloak.org/docs/latest/server_admin/index.html#_account-service)中查看和撤消已由他们授予的脱机令牌。 管理员用户可以在特定用户的`Consents(同意)`选项卡中撤消管理控制台中各个用户的离线令牌。 管理员还可以查看在每个客户端的`Offline Access`选项卡中发布的所有脱机令牌。 也可以通过设置[撤销策略](https://www.keycloak.org/docs/latest/server_admin/index.html#_revocation-policy)撤销离线令牌。

为了能够发出脱机令牌，用户需要具有领域级角色`offline_access`的角色映射。 客户还需要在其范围内具有该角色。 最后，客户端需要将`offline_access`客户端作用域添加为`Optional client scope`，默认情况下完成。

客户端可以在向Keycloak发送授权请求时通过添加参数`scope=offline_access`来请求脱机令牌。 当您使用Keycloak OIDC客户端适配器访问应用程序的安全URL（即`http://localhost:8080/customer-portal/secured?scope=offline_access`）时，它会自动添加此参数。 如果在身份验证请求的正文中包含`scope=offline_access`，则直接访问授权和服务帐户也支持脱机令牌。

<a name="177___14__用户存储联合"></a>
## 14. 用户存储联合

许多公司都有现有的用户数据库，用于保存有关用户及其密码或其他凭据的信息。 在许多情况下，无法将这些现有存储迁移到纯粹的Keycloak部署。 Keycloak可以联合现有的外部用户数据库。 开箱即用，我们支持LDAP和Active Directory。 您还可以使用我们的用户存储SPI为您可能拥有的任何自定义用户数据库编写自己的扩展。

它的工作方式是当用户登录时，Keycloak将查看其自己的内部用户存储以查找用户。 如果它找不到它，它将遍历为域配置的每个用户存储提供程序，直到找到匹配项。 来自外部存储的数据映射到Keycloak运行时使用的公共用户模型。 然后，可以将此公共用户模型映射到OIDC令牌声明和SAML断言属性。

外部用户数据库很少拥有支持Keycloak所具有的所有功能所需的每一项数据。 在这种情况下，用户存储提供程序可以选择在Keycloak用户存储中本地存储一些内容。 有些提供商甚至在本地导入用户并定期与外部商店同步。 所有这些都取决于提供商的功能及其配置方式。 例如，您的外部用户存储可能不支持OTP。 根据提供商的不同，Keycloak可以处理和存储此OTP。

<a name="178____14_1__添加提供商"></a>
### 14.1. 添加提供商
要添加存储提供程序，请转到管理控制台中的`User Federation`左侧菜单项。

用户联盟

![user federation](assets/user-federation.png)

在中心，有一个`Add Provider`列表框。 选择要添加的提供程序类型，然后您将进入该提供程序的配置页面。

<a name="179____14_2__处理提供商失败"></a>
### 14.2. 处理提供商失败
如果用户存储提供程序失败，也就是说，如果LDAP服务器已关闭，则可能无法登录，并且可能无法在管理控制台中查看用户。 使用存储提供程序查找用户时，Keycloak不会捕获故障。 它将中止调用。 因此，如果您有一个优先级较高的存储提供程序在用户查找期间失败，则登录或用户查询将完全失败并发生异常并中止。 它不会故障转移到下一个配置的提供程序。

始终首先搜索本地Keycloak用户数据库，以便在任何LDAP或自定义用户存储提供程序之前解析用户。 您可能需要考虑创建存储在本地Keycloak用户数据库中的管理员帐户，以防万一在连接到LDAP和自定义后端时出现任何问题。

每个LDAP和自定义用户存储提供程序在其管理控制台页面上都有一个`enable`开关。 禁用用户存储提供程序将在执行用户查询时跳过提供程序，以便您可以查看和登录可能存储在具有较低优先级的其他提供程序中的用户。 如果您的提供商使用`import`策略并禁用它，则导入的用户仍可用于查找，但仅限于只读模式。 在重新启用提供程序之前，您将无法修改这些用户。

如果存储提供程序查找失败，Keycloak不会进行故障转移的原因是用户数据库通常具有重复的用户名或它们之间的重复电子邮件。 这可能导致安全问题和无法预料的问题，因为当管理员期望从另一个用户加载用户时，可以从一个外部存储加载用户。

<a name="180____14_3__LDAP和Active_Directory"></a>
### 14.3. LDAP和Active Directory

Keycloak附带内置的`LDAP/AD`提供程序。 可以在同一Keycloak领域中联合多个不同的LDAP服务器。 您可以将LDAP用户属性映射到Keycloak通用用户模型。 默认情况下，它映射用户名，电子邮件，名字和姓氏，但您可以自由配置其他[映射](https://www.keycloak.org/docs/latest/server_admin/index.html#_ldap_mappers)。 LDAP提供程序还支持通过`LDAP/AD`协议进行密码验证以及不同的存储，编辑和同步模式。

要配置联合LDAP存储，请转至管理控制台。 单击 `User Federation` 左侧菜单选项。 当你到达这个页面时，有一个`Add Provider`选择框。 您应该在此列表中看到*ldap*。 选择*ldap*将带您进入LDAP配置页面。

<a name="181_____14_3_1__存储模式"></a>
#### 14.3.1. 存储模式
默认情况下，Keycloak会将用户从LDAP导入到本地Keycloak用户数据库中。 该用户副本可以按需同步，也可以通过定期后台任务同步。 一个例外是密码。 不会导入密码，并且会将密码验证委派给LDAP服务器。 这种方法的好处是所有Keycloak功能都可以工作，因为所需的任何额外的每用户数据都可以存储在本地。 此方法还减少了LDAP服务器上的负载，因为第二次访问时，Keycloak数据库会加载未缓存的用户。 LDAP服务器唯一的负载是密码验证。 这种方法的缺点是，当首次查询用户时，这将需要Keycloak数据库插入。 导入还必须根据需要与LDAP服务器同步。

或者，您可以选择不将用户导入Keycloak用户数据库。 在这种情况下，Keycloak运行时使用的公共用户模型仅由LDAP服务器支持。 这意味着如果LDAP不支持Keycloak功能所需的数据，则该功能将无法使用。 这种方法的好处是您没有将LDAP用户的副本导入和同步到Keycloak用户数据库的开销。

此存储模式由`Import Users`开关控制。 设置为`On`以导入用户。

<a name="182_____14_3_2__编辑模式"></a>
#### 14.3.2. 编辑模式
用户通过[用户帐户服务](https://www.keycloak.org/docs/latest/server_admin/index.html#_account-service)和管理员通过管理控制台可以修改用户元数据。 根据您的设置，您可能拥有或不拥有LDAP更新权限。 `Edit Mode`配置选项定义了LDAP存储的编辑策略。

- READONLY(只读)

  用户名，电子邮件，名字，姓氏和其他映射属性将不可更改。 任何人试图更新这些字段时，Keycloak都会显示错误。 此外，不支持密码更新。

- WRITABLE(可写)

  用户名，电子邮件，名字，姓氏以及其他映射的属性和密码都可以更新，并将自动与LDAP存储同步。

- UNSYNCED(不同步)

  对用户名，电子邮件，名字，姓氏和密码的任何更改都将存储在Keycloak本地存储中。 由您决定如何同步回LDAP。 这允许Keycloak部署支持在只读LDAP服务器上更新用户元数据。 此选项仅在将用户从LDAP导入本地Keycloak用户数据库时适用。

<a name="183_____14_3_3__其他配置选项"></a>
#### 14.3.3. 其他配置选项
- Console Display Name(控制台显示名称)

  在管理控制台中引用此提供程序时使用的名称

- Priority(优先级)

  查找用户或添加用户时此提供程序的优先级。

- Sync Registrations(同步注册)

  您的LDAP是否支持添加新用户？ 如果希望将管理控制台中的Keycloak创建的新用户或注册页面添加到LDAP，请单击此开关。

- Allow Kerberos authentication(允许Kerberos身份验证)

  使用从LDAP配置的用户数据在领域中启用`Kerberos/SPNEGO`身份验证。 更多信息请参见[Kerberos部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_kerberos)。

- Other options(其他选项)

  其余配置选项应该是自解释的。 您可以将鼠标悬停在管理控制台中的工具提示，以查看有关它们的更多详细信息。

<a name="184_____14_3_4__通过SSL连接到LDAP"></a>
#### 14.3.4. 通过SSL连接到LDAP
当您为LDAP存储配置安全连接URL时（例如`ldaps://myhost.com:636`），Keycloak将使用SSL与LDAP服务器进行通信。 重要的是在Keycloak服务器端正确配置信任库，否则Keycloak不能信任到LDAP的SSL连接。

可以使用Truststore SPI配置Keycloak的全局信任库。 有关更多详细信息，请查看[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)。 如果未配置信任库SPI，则信任库将回退到Java提供的缺省机制（系统属性`javax.net.ssl.trustStore`提供的文件或JDK提供的cacerts文件，如果系统属性为 没有设置）。

在LDAP联合提供程序配置中有一个配置属性`Use Truststore SPI`，您可以在其中选择是否使用`Truststore SPI`。 默认情况下，该值为`Only for ldaps`，这适用于大多数部署。 仅当与LDAP的连接以`ldaps`开头时，才会使用`Truststore SPI`。

<a name="185_____14_3_5__LDAP用户与Keycloak的同步"></a>
#### 14.3.5. LDAP用户与Keycloak的同步
如果启用了导入，LDAP提供程序将自动负责将所需LDAP用户同步（导入）到Keycloak本地数据库中。 当用户登录时，LDAP提供程序会将LDAP用户导入Keycloak数据库，然后根据LDAP密码进行身份验证。 这是用户导入的唯一时间。 如果您转到管理控制台中的`Users`左侧菜单项并单击`View all users`按钮，您将只看到那些已被Keycloak至少验证过一次的LDAP用户。 它以这种方式实现，以便管理员不会意外地尝试导入庞大的LDAP用户数据库。

如果要将所有LDAP用户同步到Keycloak数据库，可以配置并启用您配置的LDAP提供程序的`Sync Settings`。 有两种类型的同步：

- 定期完全同步

  这会将所有LDAP用户同步到Keycloak DB中。 那些已经存在于Keycloak中并在LDAP中直接更改的LDAP用户将在Keycloak DB中更新（例如，如果用户`Mary Kelly`在LDAP中更改为'Mary Smith`）。

- 定期更改用户同步

  发生同步时，仅更新和/或导入在上次同步后创建或更新的用户。

处理同步的最佳方法是在首次创建LDAP提供程序时单击`Synchronize all users`按钮，然后设置已更改用户的定期同步。 LDAP提供程序的配置页面有几个选项可以为您提供支持。

<a name="186_____14_3_6__LDAP映射器"></a>
#### 14.3.6. LDAP映射器
LDAP映射器是`listeners`，由LDAP提供程序在各个点触发，为LDAP集成提供另一个扩展点。 当用户通过LDAP登录并需要导入，在Keycloak启动的注册期间或从管理控制台查询用户时，会触发它们。 当您创建LDAP联合提供程序时，Keycloak将自动为此提供程序提供一组内置的`mappers`。 您可以自由更改此设置并创建新的映射器或更新/删除现有映射器。

- User Attribute Mapper(用户属性映射器)

  这允许您指定将哪个LDAP属性映射到Keycloak用户的哪个属性。 因此，例如，您可以将LDAP属性`mail`配置为Keycloak数据库中的`email`属性。 对于此映射器实现，始终存在一对一映射（一个LDAP属性映射到一个Keycloak属性）

- FullName Mapper(全名映射器)

  这允许您指定保存在某个LDAP属性（通常是`cn`）中的用户的全名将映射到Keycloak数据库中的`firstName`和`lastname`属性。 让`cn`包含用户的全名是某些LDAP部署的常见情况。

- Role Mapper(角色映射器)

  这允许您配置从LDAP到Keycloak角色映射的角色映射。 可以使用一个角色映射器将LDAP角色（通常是来自LDAP树的特定分支的组）映射到与指定客户端的域角色或客户端角色相对应的角色。 为同一LDAP提供程序配置更多角色映射器不是问题。 因此，例如，您可以指定来自`ou=main,dc=example,dc=org`下的组的角色映射将映射到域`ou=finance,dc=example,dc=org`下的域中的域角色映射和角色映射 `将映射到客户端`finance`的客户端角色映射。

- Hardcoded Role Mapper(硬编码角色映射器)

  此映射器将为与LDAP链接的每个Keycloak用户授予指定的Keycloak角色。

- Group Mapper(组映射器)

  这允许您将组映射从LDAP配置为Keycloak组映射。 组映射器可用于将LDAP树的特定分支中的LDAP组映射到Keycloak中的组。 它还会将用户组映射从LDAP传播到Keycloak中的用户组映射。

- MSAD User Account Mapper(MSAD用户帐户映射器)

  此映射器特定于Microsoft Active Directory(MSAD)。 它能够将MSAD用户帐户状态紧密集成到Keycloak帐户状态（启用帐户，密码已过期等）。 它使用`userAccountControl`和`pwdLastSet` LDAP属性。 （两者都是MSAD特有的，不是LDAP标准）。 例如，如果`pwdLastSet`为`0`，则Keycloak用户需要更新其密码，并且将向用户添加UPDATE_PASSWORD所需的操作。 如果`userAccountControl`是`514`（禁用帐户），Keycloak用户也被禁用。

默认情况下，有用户属性映射器将基本的Keycloak用户属性（如用户名，名字，姓氏和电子邮件）映射到相应的LDAP属性。 您可以自由扩展这些并提供其他属性映射。 管理控制台提供工具提示，这有助于配置相应的映射器。

<a name="187_____14_3_7__密码哈希"></a>
#### 14.3.7. 密码哈希
当用户的密码从Keycloak更新并发送到LDAP时，它始终以纯文本形式发送。 这与将密码更新为内置Keycloak数据库不同，当在将密码发送到DB之前对密码应用散列和salting时。 对于LDAP，Keycloak依赖于LDAP服务器来提供密码的散列和腌制。

大多数LDAP服务器（Microsoft Active Directory，RHDS，FreeIPA）默认提供此功能。 其他一些（OpenLDAP，ApacheDS）可能默认以纯文本形式存储密码，您可能需要为它们显式启用密码散列。 请参阅LDAP服务器的文档更多详细信息。

<a name="188____14_4__SSSD和FreeIPA身份管理集成"></a>
### 14.4. SSSD和FreeIPA身份管理集成

Keycloak还附带了一个内置的[SSSD](https://fedoraproject.org/wiki/Features/SSSD)（系统安全服务守护程序）插件。 SSSD是最新的Fedora或Red Hat Enterprise Linux的一部分，可以访问多个身份和身份验证提供程序。 它提供故障转移和脱机支持等好处。 有关配置选项的详细信息，请参阅[红帽企业Linux身份管理文档](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/system-level_authentication_guide/sssd)。

SSSD还与FreeIPA身份管理（IdM）服务器集成，提供身份验证和访问控制。 对于Keycloak，我们受益于此集成验证PAM服务和从SSSD检索用户数据。 有关在Linux环境中使用Red Hat Identity Management的更多信息，请参阅[Red Hat Enterprise Linux身份管理文档](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/linux_domain_identity_authentication_and_policy_guide/index)。

![keycloak sssd freeipa integration overview](assets/keycloak-sssd-freeipa-integration-overview.png)

Keycloak和SSSD之间的大多数通信都是通过只读D-Bus接口实现的。 因此，配置和更新用户的唯一方法是使用`FreeIPA/IdM`管理界面。 默认情况下，与LDAP联合提供程序一样，它仅设置为导入用户名，电子邮件，名字和姓氏。

> 组和角色会自动注册，但不会同步，因此Keycloak管理员直接在Keycloak中所做的任何更改都不会与SSSD同步。

下面是有关如何配置`FreeIPA/IdM`服务器的信息。

<a name="189_____14_4_1__FreeIPA_IdM服务器"></a>
#### 14.4.1. FreeIPA/IdM服务器
为简单起见，使用了[FreeIPA Docker image](https://hub.docker.com/r/freeipa/freeipa-server/)。 要设置服务器，请参阅[FreeIPA文档](https://www.freeipa.org/page/Quick_Start_Guide)。

使用Docker运行FreeIPA服务器需要以下命令：

```bash
docker run --name freeipa-server-container -it \
-h server.freeipa.local -e PASSWORD=YOUR_PASSWORD \
-v /sys/fs/cgroup:/sys/fs/cgroup:ro \
-v /var/lib/ipa-data:/data:Z freeipa/freeipa-server
```

带有`server.freeipa.local`的参数`-h`代表FreeIPA/IdM服务器主机名。 务必将`YOUR_PASSWORD`更改为您选择的密码。

容器启动后，将`/etc/hosts`更改为：

```
x.x.x.x     server.freeipa.local
```

如果不进行此更改，则必须设置DNS服务器。

为了在Keycloak上启动并运行SSSD联合提供程序，您必须在IPA域中注册Linux机器：

```bash
ipa-client-install --mkhomedir -p admin -w password
```

要确保一切按预期工作，请在客户端计算机上运行：

```bash
kinit admin
```

系统将提示您输入密码。 之后，您可以使用以下命令将用户添加到IPA服务器：

```bash
$ ipa user-add john --first=John --last=Smith --email=john@smith.com --phone=042424242 --street="Testing street" \      --city="Testing city" --state="Testing State" --postalcode=0000000000
```

<a name="190_____14_4_2__SSSD_and_D_Bus"></a>
#### 14.4.2. SSSD and D-Bus
如前所述，联合提供程序使用D-BUS从SSSD获取数据，并使用PAM进行身份验证。

首先，您必须安装sssd-dbus RPM，它允许来自SSSD的信息通过系统总线传输。

```bash
$ sudo yum install sssd-dbus
```

您必须运行Keycloak发行版中提供的配置脚本：

```bash
$ bin/federation-sssd-setup.sh
```

该脚本对`/etc/sssd/sssd.conf`进行必要的更改：

```properties
[domain/your-hostname.local]
...
ldap_user_extra_attrs = mail:mail, sn:sn, givenname:givenname, telephoneNumber:telephoneNumber
...
[sssd]
services = nss, sudo, pam, ssh, ifp
...
[ifp]
allowed_uids = root, yourOSUsername
user_attributes = +mail, +telephoneNumber, +givenname, +sn
```

另外，`/etc/pam.d/`下包含`keycloak`文件：

```
auth    required   pam_sss.so
account required   pam_sss.so
```

通过运行`dbus-send`确保一切正常运行：

```bash
sudo dbus-send --print-reply --system --dest=org.freedesktop.sssd.infopipe /org/freedesktop/sssd/infopipe org.freedesktop.sssd.infopipe.GetUserGroups string:john
```

您应该能够看到用户的组。 如果此命令返回超时或错误，则表示联合提供程序也无法在Keycloak上检索任何内容。

大多数情况下，这是因为机器未注册FreeIPA IdM服务器或您无权访问SSSD服务。

如果您没有权限，请确保运行Keycloak的用户包含在以下部分的`/etc/sssd/sssd.conf`文件中：

```properties
[ifp]
allowed_uids = root, your_username
```

<a name="191_____14_4_3__启用SSSD联合提供程序"></a>
#### 14.4.3. 启用SSSD联合提供程序
Keycloak使用DBus-Java与D-Bus进行低级通信，这取决于[Unix套接字库](http://www.matthew.ath.cx/projects/java/)。

可以在[此存储库](https://github.com/keycloak/libunix-dbus-java/releases)中找到此库的RPM。 在安装之前，请务必检查RPM签名：

```bash
$ rpm -K libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm
libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm:
  Header V4 RSA/SHA256 Signature, key ID 84dc9914: OK
  Header SHA1 digest: OK (d17bb7ebaa7a5304c1856ee4357c8ba4ec9c0b89)
  V4 RSA/SHA256 Signature, key ID 84dc9914: OK
  MD5 digest: OK (770c2e68d052cb4a4473e1e9fd8818cf)
$ sudo yum install libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm
```

使用PAM进行身份验证Keycloak使用JNA。 确保安装了此软件包：

```bash
$ sudo yum install jna
```

使用`sssctl user-checks`命令验证您的设置：

```bash
$ sudo sssctl user-checks admin -s keycloak
```

<a name="192____14_5__配置联合SSSD存储"></a>
### 14.5. 配置联合SSSD存储
安装后，您需要配置联合SSSD存储。

要配置联合SSSD存储，请完成以下步骤：

1. 导航到管理控制台。
2. 从左侧菜单中选择 **User Federation.**
3. 从**Add Provider**下拉列表中，选择**sssd**将打开sssd配置页面。
4. 点击**Save**。

现在，您可以使用`FreeIPA/IdM`凭据对Keycloak进行身份验证。

<a name="193____14_6__定制供应商"></a>
### 14.6. 定制供应商

Keycloak确实有一个用于用户存储联合的SPI，您可以使用它来编写自己的自定义提供程序。 您可以在我们的[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)中找到相关文档。

<a name="194___15__审计和事件"></a>
## 15. 审计和事件

Keycloak提供丰富的审计功能。 每个登录操作都可以记录并存储在数据库中，并在管理控制台中查看。 还可以记录和审查所有管理操作。 还有一个监听器SPI，插件可以监听这些事件并执行某些操作。 内置侦听器包括简单的日志文件以及在事件发生时发送电子邮件的功能。

<a name="195____15_1__登录活动"></a>
### 15.1. 登录活动

登录事件发生在用户成功登录，有人输入错误密码或用户帐户更新时。 可以记录和查看发生在用户身上的每个事件。 默认情况下，管理控制台中不会存储或查看任何事件。 只有错误事件记录到控制台和服务器的日志文件中。 要开始持久化，您需要启用存储。 转到`Events`左侧菜单项并选择`Config`选项卡。

事件配置

![login events config](assets/login-events-config.png)

要开始存储事件，您需要在`Login Events Settings`下将`Save Events`开关打开。

Save Events(保存事件)

![login events settings](assets/login-events-settings.png)

`Saved Types`字段允许您指定要在事件存储中存储的事件类型。 `Clear events`按钮允许您删除数据库中的所有事件。 `Expiration`字段允许您指定要保存事件的时间。 一旦您启用了登录事件的存储并决定了您的设置，请不要忘记单击本页底部的`Save`按钮。

要查看事件，请转到`Login Events`选项卡。

Login Events(登录事件)

![login events](assets/login-events.png)

如您所见，存储了大量信息，如果您要存储每个事件，则每个登录操作都会存储大量事件。 此页面上的`Filter`按钮允许您过滤您实际感兴趣的事件。

Login Event Filter(登录事件过滤器)

![login events filter](assets/login-events-filter.png)

在此屏幕截图中，我们仅过滤了`Login`事件。 单击`Update`按钮可运行过滤器。

<a name="196_____15_1_1__事件类型"></a>
#### 15.1.1. 事件类型
登录事件：

- Login - 用户已登录。
- Register - 用户已注册。
- Logout - 用户已注销。
- Code to Token - 应用程序/客户端已交换令牌代码。
- Refresh Token - 应用程序/客户端刷新了令牌。

帐户事件：

- Social Link - 帐户已与社交提供商相关联。
- Remove Social Link - 已从帐户中删除社交提供程序。
- Update Email - 帐户的电子邮件地址已更改。
- Update Profile - 帐户的个人资料已更改。
- Send Password Reset - 已发送密码重置电子邮件。
- Update Password - 帐户的密码已更改。
- Update TOTP - 帐户的TOTP设置已更改。
- Remove TOTP - TOTP已从帐户中删除。
- Send Verify Email - 已发送电子邮件验证电子邮件。
- Verify Email - 帐户的电子邮件地址已经过验证。

对于所有事件，都存在相应的错误事件。

<a name="197_____15_1_2__事件监听器"></a>
#### 15.1.2. 事件监听器
事件侦听器侦听事件并基于该事件执行操作。 Keycloak附带了两个内置监听器：`Logging Event Listener` 和 `Email Event Listener`。

每当发生错误事件时，`Logging Event Listener`都会写入日志文件，并且默认情况下处于启用状态。 这是一个示例日志消息：

```
11:36:09,965 WARN  [org.keycloak.events] (default task-51) type=LOGIN_ERROR, realmId=master,
                    clientId=myapp,
                    userId=19aeb848-96fc-44f6-b0a3-59a17570d374, ipAddress=127.0.0.1,
                    error=invalid_user_credentials, auth_method=openid-connect, auth_type=code,
                    redirect_uri=http://localhost:8180/myapp,
                    code_id=b669da14-cdbb-41d0-b055-0810a0334607, username=admin
```

如果您想使用像Fail2Ban这样的工具来检测是否存在试图猜测用户密码的黑客机器人，则此日志记录非常有用。 您可以解析日志文件中的`LOGIN_ERROR`并提出IP地址。 然后将此信息提供给Fail2Ban，以便它可以帮助防止攻击。

发生事件时，电子邮件事件监听器会向用户的帐户发送电子邮件。 电子邮件事件监听器目前仅支持以下事件：

- Login Error(登录错误)
- Update Password(更新密码)
- Update TOTP(更新TOTP)
- Remove TOTP(删除TOTP)

要启用电子邮件侦听器，请转到`Config`选项卡，然后单击`Event Listeners`字段。 这将显示一个下拉列表框，您可以在其中选择电子邮件。

您可以通过编辑分发附带的`standalone.xml`，`standalone-ha.xml`或`domain.xml`来排除一个或多个事件，例如：

```xml
<spi name="eventsListener">
  <provider name="email" enabled="true">
    <properties>
      <property name="exclude-events" value="[&quot;UPDATE_TOTP&quot;,&quot;REMOVE_TOTP&quot;]"/>
    </properties>
  </provider>
</spi>
```

有关`standalone.xml`，`standalone-ha.xml`或`domain的详细信息，请参阅[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)。

<a name="198____15_2__管理事件"></a>
### 15.2. 管理事件

可以记录管理员在管理控制台中执行的任何操作以进行审计。 管理控制台通过调用Keycloak REST接口来执行管理功能。 Keycloak审核这些REST调用。 然后，可以在管理控制台中查看生成的事件。

要启用管理员操作的审核，请转到`Events`左侧菜单项并选择`Config`选项卡。

Event Configuration(事件配置)

![login events config](assets/login-events-config.png)

在`Admin Events Settings`部分中，打开`Save Events`开关。

Admin Event Configuration(管理事件配置)

![admin events settings](assets/admin-events-settings.png)

`Include Representation`开关将包含通过管理REST API发送的任何JSON文档。 这使您可以准确查看管理员已完成的操作，但可以导致存储在数据库中的大量信息。 `Clear admin events`按钮允许您清除存储的当前信息。

要查看管理事件，请转到`Admin Events`选项卡。

Admin Events(管理事)

![admin events](assets/admin-events.png)

如果`Details`列有一个`Representation`框，你可以点击它来查看随该操作发送的JSON。

Admin Representation(管理员代表)

![admin events representation](assets/admin-events-representation.png)

您还可以通过单击`Filter`按钮来过滤您感兴趣的事件。

Admin Event Filter(管理事件筛选器)

![admin events filter](assets/admin-events-filter.png)

<a name="199___16__导出和导入"></a>
## 16. 导出和导入

Keycloak具有导出和导入整个数据库的能力。 如果要将整个Keycloak数据库从一个环境迁移到另一个环境或迁移到其他数据库（例如从MySQL到Oracle），这可能特别有用。 导出和导入在服务器启动时触发，其参数通过Java系统属性传递。 需要注意的是，由于导入和导出是在服务器启动时发生的，因此在发生这种情况时，不应对服务器或数据库执行任何其他操作。

您可以将数据库导出/导入到：

- 本地文件系统上的目录
- 文件系统上的单个JSON文件

使用目录策略导入时，请注意文件需要遵循下面指定的命名约定。 如果要导入先前导出的文件，则文件已遵循此约定。

- <REALM_NAME>-realm.json，例如名为"acme-roadrunner-affairs"的"acme-roadrunner-affairs-realm.json"
- `<REALM_NAME>-users-<INDEX>.json`，例如"acme-roadrunner-affairs-users-0.json"，用于名为"acme-roadrunner-affairs"的域的第一个用户文件

如果导出到目录，还可以指定将存储在每个JSON文件中的用户数。

> 如果您的数据库中有大量用户（500或更多），强烈建议导出到目录而不是单个文件。 导出到单个文件可能会导致非常大的文件。 此外，目录提供程序正在为每个"page"（具有用户的文件）使用单独的事务，这会带来更好的性能。 每个文件（和事务）的默认用户数为50，这表明我们的性能最佳，但您可以覆盖（见下文）。 导出到单个文件每个导出使用一个事务，每个导入使用一个事务，这导致大量用户的性能不佳。

要导出到未加密的目录，您可以使用：

```bash
bin/standalone.sh -Dkeycloak.migration.action=export
-Dkeycloak.migration.provider=dir -Dkeycloak.migration.dir=<DIR TO EXPORT TO>
```

同样，对于import，只需使用`-Dkeycloak.migration.action=import`而不是`export`。 要导出到单个JSON文件，您可以使用：

```bash
bin/standalone.sh -Dkeycloak.migration.action=export
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=<FILE TO EXPORT TO>
```

以下是导入示例：

```bash
bin/standalone.sh -Dkeycloak.migration.action=import
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=<FILE TO IMPORT>
-Dkeycloak.migration.strategy=OVERWRITE_EXISTING
```

其他可用选项包括：

- -Dkeycloak.migration.realmName

  如果要仅导出一个指定的域而不是全部，则使用此属性。 如果未指定，则将导出所有领域。

- -Dkeycloak.migration.usersExportStrategy

  此属性用于指定用户的导出位置。 可能的值有：`DIFFERENT_FILES` - 根据每个文件的最大用户数将用户导出到不同的文件中。 这是默认值. `SKIP` - 将完全跳过用户导出. `REALM_FILE` - 所有用户将使用领域设置导出到同一文件。 （结果将是一个文件，如"foo-realm.json"，包含领域数据和用户。）`SAME_FILE` - 所有用户将被导出到同一文件但不同于领域文件。 （结果将是带有领域数据的"foo-realm.json"文件和带有用户的"foo-users.json"。）

- -Dkeycloak.migration.usersPerFile

  此属性用于指定每个文件的用户数（以及每个数据库事务）。 它默认为50。 仅当usersExportStrategy为DIFFERENT_FILES时才使用它

- -Dkeycloak.migration.strategy

  导入期间使用此属性。 如果要导入数据的数据库中已存在具有相同名称的域，则可以使用它指定如何继续。 可能的值有：`IGNORE_EXISTING` - 如果此名称的域已经存在，则忽略导入. `OVERWRITE_EXISTING` - 删除现有域并使用JSON文件中的新数据再次导入它。 如果要将一个环境完全迁移到另一个环境并确保新环境包含与旧环境相同的数据，则可以指定此环境。

导入以前未导出的域文件时，可以使用选项`keycloak.import`。 如果需要导入多个域文件，则可以指定逗号分隔的文件名列表。 这比以前的情况更合适，因为只有在初始化主域之后才会发生这种情况。 例子：

- -Dkeycloak.import=/tmp/realm1.json
- -Dkeycloak.import=/tmp/realm1.json,/tmp/realm2.json

<a name="200____16_1__管理控制台导出_导入"></a>
### 16.1. 管理控制台导出/导入
可以从管理控制台执行大多数资源的导入，也可以导出大多数资源。 不支持导出用户。

注意：包含机密或私人信息的属性将在导出文件中屏蔽。 因此，通过管理控制台获取的导出文件不适用于服务器之间的备份或数据传输。 只有启动时导出才适合。

在"startup"导出期间创建的文件也可用于从管理UI导入。 这样，您可以从一个领域导出并导入到另一个领域。 或者，您可以从一台服务器导出并导入到另一台服务器。 注意：管理控制台导出/导入每个文件只允许一个域。

> 管理控制台导入允许您在选择时"overwrite"资源。 请谨慎使用此功能，尤其是在生产系统上。 从管理控制台导出操作导出.json文件通常不适合数据导入，因为它们包含无效的机密值。

> 管理控制台导出允许您导出客户端，组和角色。 如果您的领域中存在大量这些资产，则操作可能需要一些时间才能完成。 在此期间，服务器可能无法响应用户请求。 请谨慎使用此功能，尤其是在生产系统上。

<a name="201___17__用户帐户服务"></a>
## 17. 用户帐户服务

Keycloak有一个内置的用户帐户服务，每个用户都可以访问。 此服务允许用户管理其帐户，更改其凭据，更新其个人资料以及查看其登录会话。 此服务的URL是`<server-root>/auth/realms/{realm-name}/account`。

Account Service(帐户服务)

![account service profile](assets/account-service-profile.png)

初始页面是用户的配置文件，即`Account`左侧菜单项。 这是他们指定自己的基本数据的地方。 可以扩展此屏幕以允许用户管理其他属性。 有关详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

`Password`左侧菜单项允许用户更改其密码。

Password Update(密码更新)

![account service password](assets/account-service-password.png)

`Authenticator`菜单项允许用户根据需要设置OTP。 这只会在OTP是您的领域的有效身份验证机制时显示。 用户可以获得安装[FreeOTP](https://freeotp.github.io/)或[Google身份验证器](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2)他们的移动设备上的OTP生成器。 您在屏幕截图中看到的QR码可以扫描到FreeOTP或Google Authenticator移动应用程序中，以便进行简单的设置。

OTP Authenticator(OTP身份验证器)

![account service authenticator](assets/account-service-authenticator.png)

`Federated Identity`菜单项允许用户将他们的帐户与[身份代理](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker)链接（这通常用于链接 社交提供者帐户在一起）。 这将显示您为领域配置的外部身份提供程序列表。

Federated Identity(联合身份)

![account service federated identity](assets/account-service-federated-identity.png)

`Sessions`菜单项允许用户查看和管理登录和从哪里登录的设备。 他们也可以从这个屏幕执行这些会话的注销。

Sessions(会话)

![account service sessions](assets/account-service-sessions.png)

`Applications`菜单项向用户显示他们可以访问的应用程序。

Applications(应用)

![account service apps](assets/account-service-apps.png)

<a name="202____17_1__主题化"></a>
### 17.1. 主题化
与Keycloak中的所有UI一样，用户帐户服务完全可以主题化和国际化。 有关详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)。

<a name="203___18__威胁模型缓解"></a>
## 18. 威胁模型缓解

本章讨论了任何身份验证服务器可能存在的安全漏洞以及Keycloak如何减轻这些漏洞。 在IETF提出的[OAuth 2.0威胁模型](https://tools.ietf.org/html/rfc6819)文档中可以找到一个很好的潜在漏洞清单以及安全实施应该采取哪些措施来缓解这些漏洞。 这里讨论了许多漏洞。

<a name="204____18_1__主机名"></a>
### 18.1. 主机名

Keycloak使用公共主机名进行许多操作。 例如，在密码重置电子邮件中发送的令牌颁发者字段和URL中。

默认情况下，主机名基于请求标头，并且不会检查以确保此主机名有效。

如果您未在Keycloak前面使用负载均衡器或代理来阻止无效的主机标头，则必须明确配置应接受的主机名。

Hostname SPI提供了一种为请求配置主机名的方法。 开箱即用有两个提供商。 这些是请求和修复。 如果内置提供程序不提供所需的功能，也可以开发自己的提供程序。

<a name="205_____18_1_1__请求提供者"></a>
#### 18.1.1. 请求提供者
这是默认的主机名提供程序，并使用请求标头来确定主机名。 由于它使用请求中的标头，因此将其与代理或拒绝无效主机名的过滤器结合使用非常重要。

提供有关如何为代理配置有效主机名的说明超出了本文档的范围。 要在过滤器中对其进行配置，您需要编辑standalone.xml以为服务器设置允许的别名。 以下示例仅允许对`auth.example.com`的请求：

```xml
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
    <server name="default-server" default-host="ignore">
        ...
        <host name="default-host" alias="auth.example.com">
            <location name="/" handler="welcome-content"/>
            <http-invoker security-realm="ApplicationRealm"/>
        </host>
    </server>
</subsystem>
```

从默认配置中进行的更改是添加属性`default-host="ignore"`并更新属性`alias`。 `default-host="ignore"`防止处理未知主机，而`alias`用于列出接受的主机。

以下是使用CLI命令的等效配置：

```bash
/subsystem=undertow/server=default-server:write-attribute(name=default-host,value=ignore)
/subsystem=undertow/server=default-server/host=default-host:write-attribute(name=alias,value=[auth.example.com]

:reload
```

<a name="206_____18_1_2__固定提供商"></a>
#### 18.1.2. 固定提供商
固定提供程序可以配置固定主机名。 与请求提供程序不同，固定提供程序允许内部应用程序在备用URL（例如内部IP地址）上调用Keycloak。 还可以通过管理控制台中域的配置覆盖特定域的主机名。

这是在生产中使用的推荐提供商。

要更改为固定提供程序并配置主机名编辑`standalone.xml`。 以下示例显示了主机名设置为`auth.example.com`的固定提供程序：

```xml
<spi name="hostname">
    <default-provider>fixed</default-provider>
    <provider name="fixed" enabled="true">
        <properties>
            <property name="hostname" value="auth.example.com"/>
            <property name="httpPort" value="-1"/>
            <property name="httpsPort" value="-1"/>
        </properties>
    </provider>
</spi>
```

以下是使用CLI命令的等效配置：

```
/subsystem=keycloak-server/spi=hostname:write-attribute(name=default-provider, value="fixed")
/subsystem=keycloak-server/spi=hostname/provider=fixed:write-attribute(name=properties.hostname,value="auth.example.com")
```

默认情况下，从请求中接收`httpPort`和`httpsPort`。 只要正确配置了任何代理，就不必更改它。 如有必要，可以通过在固定提供程序上设置`httpPort`和`httpsPort`属性来配置固定端口。

在大多数情况下，应该正确设置方案。 如果反向代理无法正确设置`X-Forwarded-For`标头，或者如果有内部应用程序使用非https来调用Keycloak，则可能不是这样。 在这种情况下，可以将`alwaysHttps`设置为`true`。

<a name="207_____18_1_3__定制提供商"></a>
#### 18.1.3. 定制提供商
要开发自定义主机名提供程序，您需要实现`org.keycloak.urls.HostnameProviderFactory`和`orl.keycloak.urls.HostnameProvider`。

按照[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)中“服务提供商接口”部分中的说明，了解有关如何开发自定义提供程序的更多信息。

<a name="208____18_2__管理员端点和控制台"></a>
### 18.2. 管理员端点和控制台

默认情况下，Keycloak管理REST API和Web控制台在与非管理员用法相同的端口上公开。 如果您在Internet上公开Keycloak，我们建议您不要在Internet上公开管理端点。

这可以直接在Keycloak中实现，也可以使用Apache或nginx等代理实现。

有关代理选项，请按照代理的文档进行操作。 您需要控制对`/auth/admin`的任何请求的访问。

要在Keycloak中直接实现这一点，有一些选择。 本文档包含两个选项，IP限制和单独端口。

<a name="209_____18_2_1__IP限制"></a>
#### 18.2.1. IP限制
可以将对`/auth/admin`的访问限制为仅限于特定的IP地址。

以下示例将对`/auth/admin`的访问限制为`10.0.0.1`到`10.0.0.255`范围内的IP地址。

```xml
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
    ...
    <server name="default-server">
        ...
        <host name="default-host" alias="localhost">
            ...
            <filter-ref name="ipAccess"/>
        </host>
    </server>
    <filters>
        <expression-filter name="ipAccess" expression="path-prefix('/auth/admin') -> ip-access-control(acl={'10.0.0.0/24 allow'})"/>
    </filters>
    ...
</subsystem>
```

使用CLI命令进行等效配置：

```
/subsystem=undertow/configuration=filter/expression-filter=ipAccess:add(,expression="path-prefix[/auth/admin] -> ip-access-control(acl={'10.0.0.0/24 allow'})")
/subsystem=undertow/server=default-server/host=default-host/filter-ref=ipAccess:add()
```

> 对于IP限制，如果您使用代理，请务必正确配置以确保Keycloak接收客户端IP地址而不是代理IP地址

<a name="210_____18_2_2__端口限制"></a>
#### 18.2.2. 端口限制
可以将`/auth/admin`暴露给未在Internet上公开的其他端口。

以下示例在端口`8444`上公开`/auth/admin`，而不允许使用默认端口`8443`进行访问。

```xml
<subsystem xmlns="urn:jboss:domain:undertow:8.0">
    ...
    <server name="default-server">
        ...
        <https-listener name="https" socket-binding="https" security-realm="ApplicationRealm" enable-http2="true"/>
        <https-listener name="https-admin" socket-binding="https-admin" security-realm="ApplicationRealm" enable-http2="true"/>
        <host name="default-host" alias="localhost">
            ...
            <filter-ref name="portAccess"/>
        </host>
    </server>
    <filters>
        <expression-filter name="portAccess" expression="path-prefix('/auth/admin') and not equals(%p, 8444) -> response-code(403)"/>
    </filters>
    ...
</subsystem>

...

<socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
    ...
    <socket-binding name="https" port="${jboss.https.port:8443}"/>
    <socket-binding name="https-admin" port="${jboss.https.port:8444}"/>
    ...
</socket-binding-group>
```

使用CLI命令进行等效配置：

```bash
/socket-binding-group=standard-sockets/socket-binding=https-admin/:add(port=8444)

/subsystem=undertow/server=default-server/https-listener=https-admin:add(socket-binding=https-admin, security-realm=ApplicationRealm, enable-http2=true)

/subsystem=undertow/configuration=filter/expression-filter=portAccess:add(,expression="path-prefix('/auth/admin') and not equals(%p, 8444) -> response-code(403)")
/subsystem=undertow/server=default-server/host=default-host/filter-ref=portAccess:add()
```

<a name="211____18_3__密码猜测：暴力攻击"></a>
### 18.3. 密码猜测：暴力攻击

当攻击者试图猜测用户的密码时，会发生暴力攻击。 Keycloak具有一些有限的强力检测功能。 如果启用，则在达到登录失败阈值时将暂时禁用用户帐户。 要启用此功能，请转到`Realm Settings`左侧菜单项，单击`Security Defenses`选项卡，然后转到`Brute Force Detection`子选项卡。

Brute Force Detection(蛮力检测)

![brute force](assets/brute-force.png)

蛮力检测有2种不同的配置; 永久锁定和临时锁定。 永久锁定将在检测到攻击后禁用用户的帐户; 该帐户将被禁用，直到管理员将其重新设置为止。 临时锁定将在检测到攻击后的一段时间内禁用用户的帐户; 帐户被禁用的时间段越长，攻击持续的时间越长。

**常用参数**

- Max Login Failures(最大登录失败)

  允许的最大登录失败次数。 默认值为30。

- Quick Login Check Milli Seconds(快速登录检查毫秒)

  登录尝试之间所需的最短时间。 默认值为1000。

- 最低快速登录等待

  如果登录尝试比*快速登录检查毫秒*更快，则用户将被暂时禁用的最短时间。 默认为1分钟。

**临时锁定参数**

- Wait Increment(等待增量)

  每次达到*Max Login Failures(最大登录失败)*后暂时禁用用户的时间量。 默认为1分钟。

- Max Wait(最长等待)

  用户暂时禁用的最长时间。 默认为15分钟。

- Failure Reset Time(失败重置时间)

  重置故障计数的时间; 计时器从上次失败的登录中运行。 默认为12小时。

**永久锁定算法**

1. 成功登录后
   1. 重置`count`
2. 登录失败
   1. 增加 `count`
   2. 如果`count`大于*Max Login Failures(最大登录失败)*
      1. 永久禁用用户
   3. 否则，如果此故障与上次故障之间的时间间隔小于*快速登录检查毫秒*
      1. 暂时禁用用户*最小快速登录等待*

当用户被禁用时，他们无法登录，直到管理员启用该用户; 启用帐户会重置`count`。

**临时锁定算法**

1. 成功登录后
   1. 重置 `count`
2. 登录失败
   1. 如果此故障与上次故障之间的时间间隔大于*故障重置时间*
      1. 重置 `count`
   2. 增加 `count`
   3. 使用*Wait Increment* * (`count` / *Max Login Failures*)计算`wait`。 除法是整数除法，因此总是向下舍入为整数
   4. 如果`wait`等于0，则此故障与上次故障之间的时间小于 *Quick Login Check Milli Seconds* 然后将`wait`设置为 *Minimum Quick Login Wait*
      1. 暂时禁用用户较小的`wait`和*Max Wait*秒

暂时禁用用户时登录失败不会增加`count`。

Keycloak强力检测的缺点是服务器容易受到拒绝服务攻击。 攻击者可以简单地尝试猜测其知道的任何帐户的密码，并且这些帐户将被禁用。 最终，我们将扩展此功能，以在决定是否阻止用户时考虑客户端IP地址。

更好的选择可能是像[Fail2Ban]这样的工具(http://www.fail2ban.org/wiki/index.php/Main_Page)。 您可以将此服务指向Keycloak服务器的日志文件。 Keycloak记录每次登录失败和发生故障的客户端IP地址。 在检测到阻止来自特定IP地址的连接的攻击后，Fail2Ban可用于修改防火墙。

<a name="212_____18_3_1__密码策略"></a>
#### 18.3.1. 密码策略
防止密码猜测应该做的另一件事是拥有足够复杂的密码策略，以确保用户选择难以猜测的密码。 有关详细信息，请参阅[密码策略](https://www.keycloak.org/docs/latest/server_admin/index.html#_password-policies)一章。

防止密码猜测的最佳方法是将服务器设置为使用一次性密码（OTP）。

<a name="213____18_4__点击劫持"></a>
### 18.4. 点击劫持

通过点击劫持，恶意网站将目标网站加载到覆盖在一组虚拟按钮顶部的透明iFrame中，这些虚拟按钮经过精心构造，可直接放置在目标站点上的重要按钮下。 当用户单击可见按钮时，他们实际上是在隐藏页面上单击按钮（例如“登录”按钮）。 攻击者可以窃取用户的身份验证凭据并访问其资源。

默认情况下，Keycloak的每个响应都会设置一些特定的浏览器标头，以防止这种情况发生。 具体来说，它设置[X-FRAME_OPTIONS](https://tools.ietf.org/html/rfc7034) 和 [Content-Security-Policy](http://www.w3.org/TR/CSP/)。 你应该看一下这两个标题的定义，因为你可以控制很多细粒度的浏览器访问。 在管理控制台中，您可以指定这些标头将具有的值。 转到`Realm Settings`左侧菜单项，然后单击`Security Defenses`选项卡，确保您位于`Headers`子选项卡上。

![security headers](assets/security-headers.png)

默认情况下，Keycloak仅为iframe设置*同源(same-origin)*政策。

<a name="214____18_5__SSL_HTTPS要求"></a>
### 18.5. SSL/HTTPS要求

如果您没有使用`SSL/HTTPS`进行Keycloak auth服务器与它所保护的客户端之间的所有通信，那么在中间攻击中您将非常容易受到攻击。 `OAuth 2.0/OpenID Connect`使用访问令牌来提高安全性。 如果没有`SSL/HTTPS`，攻击者可以嗅探您的网络并获取访问令牌。 一旦他们拥有访问令牌，他们就可以执行令牌已获得权限的任何操作。

Keycloak有[SSL / HTTPS的三种模式](https://www.keycloak.org/docs/latest/server_admin/index.html#_ssl_modes)。 SSL可能很难设置，因此开箱即用，Keycloak允许通过私有IP地址（如`localhost`，`192.168.x.x`和其他私有IP地址）进行非HTTPS通信。 在生产中，您应该确保SSL已全面启用并且是必需的。

在适配器/客户端，Keycloak允许您关闭SSL信任管理器。 信任管理器确保客户端正在与之交谈。 它根据服务器的证书检查DNS域名。 在生产中，您应确保将每个客户端适配器配置为使用信任库。 否则你在中间攻击中容易受到DNS人员的攻击。

<a name="215____18_6__CSRF_攻击"></a>
### 18.6. CSRF 攻击

跨站点请求伪造（CSRF）是基于Web的攻击，其中HTTP请求从网站信任或已经过身份验证的用户（例如，通过HTTP重定向或HTML表单）传输。 任何使用基于cookie的身份验证的站点都容易受到这些类型的攻击。 通过将状态cookie与发布的表单或查询参数进行匹配来减轻这些攻击。

OAuth 2.0登录规范要求使用状态cookie并与传输的状态参数进行匹配。 Keycloak完全实现了规范的这一部分，因此所有登录都受到保护。

Keycloak管理控制台是一个纯JavaScript / HTML5应用程序，可以对后端Keycloak管理REST API进行REST调用。 这些调用都需要承载令牌认证，并通过JavaScript Ajax调用进行。 CSRF不适用于此处。 管理REST API也可以配置为验证CORS源。

Keycloak中唯一真正落入CSRF的部分是用户帐户管理页面。 要缓解此Keycloak设置状态cookie，并将此状态cookie的值嵌入隐藏表单字段或操作链接中的查询参数。 将针对状态cookie检查此查询或表单参数，以验证用户是否进行了调用。

<a name="216____18_7__非特定的重定向URI"></a>
### 18.7. 非特定的重定向URI

对于[授权代码流程](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows)，如果您注册过于笼统的重定向URI，那么就有可能 对于流氓客户端冒充具有更广泛访问范围的不同客户端。 例如，如果两个客户端位于同一域下，则可能发生这种情况。 因此，最好使注册的重定向URI尽可能具体。

<a name="217____18_8__受损的访问和刷新令牌"></a>
### 18.8. 受损的访问和刷新令牌

您可以采取一些措施来减少访问令牌并刷新令牌被盗。 最重要的是在Keycloak及其客户端和应用程序之间强制执行`SSL/HTTPS`通信。 这似乎很明显，但由于Keycloak默认情况下没有启用SSL，因此管理员可能没有意识到这是必要的。

您可以采取的另一项措施是减少泄露的访问权限，缩短其生命周期。 您可以在[超时页面](https://www.keycloak.org/docs/latest/server_admin/index.html#_timeouts)中指定。 用于访问令牌的短寿命（分钟），以便客户端和应用程序在短时间内刷新其访问令牌。 如果管理员检测到泄漏，他们可以注销所有用户会话以使这些刷新令牌无效或设置撤销策略。 确保刷新令牌永远保持对客户端的私密性并且永远不会传输也是非常重要的。

您还可以通过将这些令牌作为持有者密钥令牌来缓解泄露的访问令牌和刷新令牌。 请参阅[OAuth 2.0 Mutual TLS客户端证书绑定访问令牌](https://www.keycloak.org/docs/latest/server_admin/index.html#_mtls-client-certificate-bound-tokens)以了解具体方法。

如果访问令牌或刷新令牌受到威胁，您应该做的第一件事就是转到管理控制台并将一个不在之前的撤销策略推送到所有应用程序。 这将强制执行在该日期之前发布的任何令牌现在无效。 推出新的not-before策略还将确保应用程序将被迫从Keycloak下载新的公钥，因此当您认为领域签名密钥被泄露时，它也适用于该案例。 [密钥章节](https://www.keycloak.org/docs/latest/server_admin/index.html#realm_keys)中的更多信息。

如果您认为这些实体中的任何一个完全受到损害，您还可以禁用特定应用程序，客户端和用户。

<a name="218____18_9__受损的授权码"></a>
### 18.9. 受损的授权码

对于[OIDC Auth Code Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows)，攻击者很难破解Keycloak授权码。 Keycloak为其授权码生成加密强随机值，因此很难猜测访问令牌。 授权代码只能使用一次才能获得访问令牌。 在管理控制台中，您可以在[超时页面](https://www.keycloak.org/docs/latest/server_admin/index.html#_timeouts)上指定授权代码的有效期。 这个值应该非常短，只需几秒钟，并且足够长，以便客户端从代码中获取请求以获取令牌。

<a name="219____18_10__打开重定向器"></a>
### 18.10. 打开重定向器

攻击者可以使用最终用户授权端点和重定向URI参数将授权服务器滥用为开放重定向器。 开放重定向器是一个端点，使用参数自动将用户代理重定向到参数值指定的位置，而不进行任何验证。 攻击者可以利用用户对授权服务器的信任来发起网络钓鱼攻击。

Keycloak要求所有注册的应用程序和客户端至少注册一个重定向URI模式。 每当客户端要求Keycloak执行重定向（例如登录或注销）时，Keycloak将检查重定向URI与有效注册URI模式列表。 客户端和应用程序注册为特定的URI模式以减轻开放重定向器攻击非常重要。

<a name="220____18_11__密码数据库受损"></a>
### 18.11. 密码数据库受损

Keycloak不会以原始文本存储密码。 它使用PBKDF2算法存储它们的散列。 它实际上使用默认的20,000次散列迭代！ 这是安全社区建议的迭代次数。 这可能会对您的系统产生相当大的性能影响，因为PBKDF2在设计上占用了大量的CPU。 您需要决定保护密码数据库的严肃程度。

<a name="221____18_12__限制范围"></a>
### 18.12. 限制范围

默认情况下，每个新的客户端应用程序都具有无限的`角色范围映射`。 这意味着为该客户端创建的每个访问令牌都将包含用户拥有的所有权限。 如果客户端遭到入侵并且访问令牌泄露，则用户有权访问的每个系统现在也会受到损害。 强烈建议您使用每个客户端的[范围菜单](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings)限制访问令牌的角色。 或者，您可以在客户端作用域级别设置角色范围映射，并使用[客户端范围菜单](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking)将客户端范围分配给客户端。

<a name="222____18_13__限制令牌受众"></a>
### 18.13. 限制令牌受众

在服务之间的信任级别较低的环境中，限制令牌上的受众是一种好习惯。 其背后的动机在[OAuth2威胁模型](https://tools.ietf.org/html/rfc6819#section-5.1.5.5)文档中有所描述，更多详细信息请参见[受众支持部分](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience)。

<a name="223____18_14__SQL注入攻击"></a>
### 18.14. SQL注入攻击

在这个时间点，没有在任何Keycloak SQL注入漏洞的知识。

<a name="224___19__管理员_命令行"></a>
## 19. 管理员 命令行

在前面的章节中，我们介绍了如何使用Keycloak管理控制台执行管理任务。 您还可以使用Admin CLI命令行工具从命令行界面（CLI）执行这些任务。

<a name="225____19_1__安装Admin_CLI"></a>
### 19.1. 安装Admin CLI
Admin CLI打包在Keycloak Server发行版中。 您可以在`bin`目录中找到执行脚本。

Linux脚本称为`kcadm.sh`，Windows脚本称为`kcadm.bat`。

您可以将Keycloak服务器目录添加到`PATH`以从文件系统上的任何位置使用客户端。

例如，在：

- Linux:

```bash
$ export PATH=$PATH:$KEYCLOAK_HOME/bin
$ kcadm.sh
```

- Windows:

```bash
c:\> set PATH=%PATH%;%KEYCLOAK_HOME%\bin
c:\> kcadm
```

我们假设`KEYCLOAK_HOME`环境（env）变量设置为您解压缩Keycloak Server分发的路径。

> 为避免重复，本文档的其余部分仅在CLI中的差异超出`kcadm`命令名称的地方提供Windows示例。

<a name="226____19_2__使用Admin_CLI"></a>
### 19.2. 使用Admin CLI
Admin CLI通过向Admin REST端点发出HTTP请求来工作。 对它们的访问受到保护并需要身份验证。

  > 有关特定端点的JSON属性的详细信息，请参阅`Admin REST API`文档。  

1. 通过提供凭据（即登录）启动经过身份验证的会话。您已准备好执行创建，读取，更新和删除（CRUD）操作。

   例如，在: 

   - Linux:

     ```bash
     $ kcadm.sh config credentials --server http://localhost:8080/auth --realm demo --user admin --client admin
     $ kcadm.sh create realms -s realm=demorealm -s enabled=true -o
     $ CID=$(kcadm.sh create clients -r demorealm -s clientId=my_client -s 'redirectUris=["http://localhost:8980/myapp/*"]' -i)
     $ kcadm.sh get clients/$CID/installation/providers/keycloak-oidc-keycloak-json
     ```

   - Windows:

     ```bash
     c:\> kcadm config credentials --server http://localhost:8080/auth --realm demo --user admin --client admin
     c:\> kcadm create realms -s realm=demorealm -s enabled=true -o
     c:\> kcadm create clients -r demorealm -s clientId=my_client -s "redirectUris=[\"http://localhost:8980/myapp/*\"]" -i > clientid.txt
     c:\> set /p CID=<clientid.txt
     c:\> kcadm get clients/%CID%/installation/providers/keycloak-oidc-keycloak-json
     ```

2. 在生产环境中，您必须使用 `https:` 访问Keycloak，以避免将令牌暴露给网络嗅探器。 如果服务器的证书不是由Java的默认证书信任库中包含的受信任证书颁发机构（CA）之一颁发的，请准备`truststore.jks`文件并指示Admin CLI使用它。

   例如，在:

   - Linux:

     ```bash
     $ kcadm.sh config truststore --trustpass $PASSWORD ~/.keycloak/truststore.jks
     ```

   - Windows:

     ```bash
     c:\> kcadm config truststore --trustpass %PASSWORD% %HOMEPATH%\.keycloak\truststore.jks
     ```

<a name="227____19_3__认证"></a>
### 19.3. 认证
使用Admin CLI登录时，指定服务器端点URL和域，然后指定用户名。 另一种选择是仅指定clientId，这导致使用特殊的"(service account)服务帐户"。 使用用户名登录时，必须使用指定用户的密码。 使用clientId登录时，只需要客户端密码，而不是用户密码。 您也可以使用`Signed JWT`而不是客户端密钥。

确保用于会话的帐户具有调用Admin REST API操作的适当权限。 例如，`realm-management`客户端的`realm-admin`角色允许用户管理定义用户的领域。

验证有两种主要机制。 一种机制使用`kcadm config credentials`来启动经过身份验证的会话。

```bash
$ kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin
```

此方法通过保存获取的访问令牌和关联的刷新令牌来维护`kcadm`命令调用之间的经过身份验证的会话。 它还可以在私有配置文件中维护其他秘密。 有关配置文件的更多信息，请参见[下一章](https://www.keycloak.org/docs/latest/server_admin/index.html#_working_with_alternative_configurations)。

第二种方法仅在该调用期间验证每个命令调用。 这种方法增加了服务器上的负载以及通过往返获取令牌所花费的时间。 这种方法的好处是不需要在调用之间保存任何令牌，这意味着什么都没有保存到磁盘。 指定`--no-config`参数时使用此模式。

例如，在执行操作时，我们指定身份验证所需的所有信息。

```bash
$ kcadm.sh get realms --no-config --server http://localhost:8080/auth --realm master --user admin --password admin
```

有关使用Admin CLI的更多信息，请运行`kcadm.sh help`命令。

运行`kcadm.sh config credentials --help`命令以获取有关启动经过身份验证的会话的更多信息。

<a name="228____19_4__使用其他配置"></a>
### 19.4. 使用其他配置
默认情况下，Admin CLI会自动维护位于用户主目录下的名为`kcadm.config`的配置文件。 在基于Linux的系统中，完整路径名是`$HOME/.keycloak/kcadm.config`。 在Windows上，完整路径名是`%HOMEPATH%\.keycloak\kcadm.config`。 您可以使用`--config`选项指向不同的文件或位置，以便您可以并行维护多个经过身份验证的会话。

> 最好从单个线程执行绑定到单个配置文件的操作。

确保不要使配置文件对系统上的其他用户可见。 它包含应该保密的访问令牌和秘密。 默认情况下，`~/.keycloak` 目录及其内容是使用适当的访问限制自动创建的。 如果该目录已存在，则不会更新其权限。

If your unique circumstances require you to avoid storing secrets inside a configuration file, you can do so. It will be less convenient and you will have to make more token requests. To not store secrets, use the `--no-config` option with all your commands and specify all the authentication information needed by the `config credentials` command with each `kcadm`invocation.

<a name="229____19_5__Basic_operations_and_resource_URIs"></a>
### 19.5. Basic operations and resource URIs
The Admin CLI allows you to generically perform CRUD operations against Admin REST API endpoints with additional commands that simplify performing certain tasks.

The main usage pattern is listed below, where the `create`, `get`, `update`, and `delete` commands are mapped to the HTTP verbs `POST`, `GET`, `PUT`, and `DELETE`, respectively.

```
$ kcadm.sh create ENDPOINT [ARGUMENTS]
$ kcadm.sh get ENDPOINT [ARGUMENTS]
$ kcadm.sh update ENDPOINT [ARGUMENTS]
$ kcadm.sh delete ENDPOINT [ARGUMENTS]
```

ENDPOINT is a target resource URI and can either be absolute (starting with `http:` or `https:`) or relative, used to compose an absolute URL of the following format:

```
SERVER_URI/admin/realms/REALM/ENDPOINT
```

For example, if you authenticate against the server `http://localhost:8080/auth` and realm is `master`, then using `users` as ENDPOINT results in the resource URL `http://localhost:8080/auth/admin/realms/master/users`.

If you set ENDPOINT to `clients`, the effective resource URI would be `http://localhost:8080/auth/admin/realms/master/clients`.

There is a `realms` endpoint that is treated slightly differently because it is the container for realms. It resolves to:

```
SERVER_URI/admin/realms
```

There is also a `serverinfo` endpoint, which is treated the same way because it is independent of realms.

When you authenticate as a user with realm-admin powers, you might need to perform commands on multiple realms. In that case, specify the `-r` option to tell explicitly which realm the command should be executed against. Instead of using `REALM` as specified via the `--realm` option of `kcadm.sh config credentials`, the `TARGET_REALM` is used.

```
SERVER_URI/admin/realms/TARGET_REALM/ENDPOINT
```

For example,

```
$ kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin
$ kcadm.sh create users -s username=testuser -s enabled=true -r demorealm
```

In this example, you start a session authenticated as the `admin` user in the `master` realm. You then perform a POST call against the resource URL `http://localhost:8080/auth/admin/realms/demorealm/users`.

The `create` and `update` commands send a JSON body to the server by default. You can use `-f FILENAME` to read a premade document from a file. When you can use `-f -` option, the message body is read from standard input. You can also specify individual attributes and their values as seen in the previous `create users` example. They are composed into a JSON body and sent to the server.

There are several ways to update a resource using the `update` command. You can first determine the current state of a resource and save it to a file, and then edit that file and send it to the server for updating.

For example:

```
$ kcadm.sh get realms/demorealm > demorealm.json
$ vi demorealm.json
$ kcadm.sh update realms/demorealm -f demorealm.json
```

This method updates the resource on the server with all the attributes in the sent JSON document.

Another option is to perform an on-the-fly update using the `-s, --set` options to set new values.

For example:

```
$ kcadm.sh update realms/demorealm -s enabled=false
```

That method only updates the `enabled` attribute to `false`.

By default, the `update` command first performs a `get` and then merges the new attribute values with existing values. This is the preferred behavior. In some cases, the endpoint may support the `PUT` command but not the `GET` command. You can use the `-n` option to perform a "no-merge" update, which performs a `PUT` command without first running a `GET`command.

<a name="230____19_6__Realm_operations"></a>
### 19.6. Realm operations
<a name="231_____Creating_a_new_realm"></a>
#### Creating a new realm
Use the `create` command on the `realms` endpoint to create a new enabled realm, and set the attributes to `realm` and `enabled`.

```
$ kcadm.sh create realms -s realm=demorealm -s enabled=true
```

A realm is not enabled by default. By enabling it, you can use a realm immediately for authentication.

A description for a new object can also be in a JSON format.

```
$ kcadm.sh create realms -f demorealm.json
```

You can send a JSON document with realm attributes directly from a file or piped to a standard input.

For example, on:

- Linux:

```
$ kcadm.sh create realms -f - << EOF
{ "realm": "demorealm", "enabled": true }
EOF
```

- Windows:

```
c:\> echo { "realm": "demorealm", "enabled": true } | kcadm create realms -f -
```

<a name="232_____Listing_existing_realms"></a>
#### Listing existing realms
The following command returns a list of all realms.

```
$ kcadm.sh get realms
```

|      | A list of realms is additionally filtered on the server to return only realms a user can see. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Returning the entire realm description often provides too much information. Most users are interested only in a subset of attributes, such as realm name and whether the realm is enabled. You can specify which attributes to return by using the `--fields` option.

```
$ kcadm.sh get realms --fields realm,enabled
```

You can also display the result as comma separated values.

```
$ kcadm.sh get realms --fields realm --format csv --noquotes
```

<a name="233_____Getting_a_specific_realm"></a>
#### Getting a specific realm
You append a realm name to a collection URI to get an individual realm.

```
$ kcadm.sh get realms/master
```

<a name="234_____Updating_a_realm"></a>
#### Updating a realm
1. Use the `-s` option to set new values for the attributes when you want to change only some of the realm’s attributes.

   For example:

   ```
   $ kcadm.sh update realms/demorealm -s enabled=false
   ```

2. If you want to set all writable attributes with new values, run a `get` command, edit the current values in the JSON file, and resubmit.

   For example:

   ```
   $ kcadm.sh get realms/demorealm > demorealm.json
   $ vi demorealm.json
   $ kcadm.sh update realms/demorealm -f demorealm.json
   ```

<a name="235_____Deleting_a_realm"></a>
#### Deleting a realm
Run the following command to delete a realm.

```
$ kcadm.sh delete realms/demorealm
```

<a name="236_____Turning_on_all_login_page_options_for_the_realm"></a>
#### Turning on all login page options for the realm
Set the attributes controlling specific capabilities to `true`.

For example:

```
$ kcadm.sh update realms/demorealm -s registrationAllowed=true -s registrationEmailAsUsername=true -s rememberMe=true -s verifyEmail=true -s resetPasswordAllowed=true -s editUsernameAllowed=true
```

<a name="237_____Listing_the_realm_keys"></a>
#### Listing the realm keys
Use the `get` operation on the `keys` endpoint of the target realm.

```
$ kcadm.sh get keys -r demorealm
```

<a name="238_____Generating_new_realm_keys"></a>
#### Generating new realm keys
1. Get the ID of the target realm before adding a new RSA-generated key pair.

   For example:

   ```
   $ kcadm.sh get realms/demorealm --fields id --format csv --noquotes
   ```

2. Add a new key provider with a higher priority than the existing providers as revealed by `kcadm.sh get keys -r demorealm`.

   For example, on:

   - Linux:

     ```
     $ kcadm.sh create components -r demorealm -s name=rsa-generated -s providerId=rsa-generated -s providerType=org.keycloak.keys.KeyProvider -s parentId=959844c1-d149-41d7-8359-6aa527fca0b0 -s 'config.priority=["101"]' -s 'config.enabled=["true"]' -s 'config.active=["true"]' -s 'config.keySize=["2048"]'
     ```

   - Windows:

     ```
     c:\> kcadm create components -r demorealm -s name=rsa-generated -s providerId=rsa-generated -s providerType=org.keycloak.keys.KeyProvider -s parentId=959844c1-d149-41d7-8359-6aa527fca0b0 -s "config.priority=[\"101\"]" -s "config.enabled=[\"true\"]" -s "config.active=[\"true\"]" -s "config.keySize=[\"2048\"]"
     ```

3. Set the `parentId` attribute to the value of the target realm’s ID.

   The newly added key should now become the active key as revealed by `kcadm.sh get keys -r demorealm`.

<a name="239_____Adding_new_realm_keys_from_a_Java_Key_Store_file"></a>
#### Adding new realm keys from a Java Key Store file
1. Add a new key provider to add a new key pair already prepared as a JKS file on the server.

   For example, on:

   - Linux:

     ```
     $ kcadm.sh create components -r demorealm -s name=java-keystore -s providerId=java-keystore -s providerType=org.keycloak.keys.KeyProvider -s parentId=959844c1-d149-41d7-8359-6aa527fca0b0 -s 'config.priority=["101"]' -s 'config.enabled=["true"]' -s 'config.active=["true"]' -s 'config.keystore=["/opt/keycloak/keystore.jks"]' -s 'config.keystorePassword=["secret"]' -s 'config.keyPassword=["secret"]' -s 'config.alias=["localhost"]'
     ```

   - Windows:

     ```
     c:\> kcadm create components -r demorealm -s name=java-keystore -s providerId=java-keystore -s providerType=org.keycloak.keys.KeyProvider -s parentId=959844c1-d149-41d7-8359-6aa527fca0b0 -s "config.priority=[\"101\"]" -s "config.enabled=[\"true\"]" -s "config.active=[\"true\"]" -s "config.keystore=[\"/opt/keycloak/keystore.jks\"]" -s "config.keystorePassword=[\"secret\"]" -s "config.keyPassword=[\"secret\"]" -s "config.alias=[\"localhost\"]"
     ```

2. Make sure to change the attribute values for `keystore`, `keystorePassword`, `keyPassword`, and `alias` to match your specific keystore.

3. Set the `parentId` attribute to the value of the target realm’s ID.

<a name="240_____Making_the_key_passive_or_disabling_the_key"></a>
#### Making the key passive or disabling the key
1. Identify the key you want to make passive

   ```
   $ kcadm.sh get keys -r demorealm
   ```

2. Use the key’s `providerId` attribute to construct an endpoint URI, such as `components/PROVIDER_ID`.

3. Perform an `update`.

   For example, on:

   - Linux:

     ```
     $ kcadm.sh update components/PROVIDER_ID -r demorealm -s 'config.active=["false"]'
     ```

   - Windows:

     ```
     c:\> kcadm update components/PROVIDER_ID -r demorealm -s "config.active=[\"false\"]"
     ```

     You can update other key attributes.

4. Set a new `enabled` value to disable the key, for example, `config.enabled=["false"]`.

5. Set a new `priority` value to change the key’s priority, for example, `config.priority=["110"]`.

<a name="241_____Deleting_an_old_key"></a>
#### Deleting an old key
1. Make sure the key you are deleting has been passive and disabled to prevent any existing tokens held by applications and users from abruptly failing to work.

2. Identify the key you want to make passive.

   ```
   $ kcadm.sh get keys -r demorealm
   ```

3. Use the `providerId` of that key to perform a delete.

   ```
   $ kcadm.sh delete components/PROVIDER_ID -r demorealm
   ```

<a name="242_____Configuring_event_logging_for_a_realm"></a>
#### Configuring event logging for a realm
Use the `update` command on the `events/config` endpoint.

The `eventsListeners` attribute contains a list of EventListenerProviderFactory IDs that specify all event listeners receiving events. Separately, there are attributes that control a built-in event storage, which allows querying past events via the Admin REST API. There is separate control over logging of service calls (`eventsEnabled`) and auditing events triggered during Admin Console or Admin REST API (`adminEventsEnabled`). You may want to set up expiry of old events so that your database does not fill up; `eventsExpiration` is set to time-to-live expressed in seconds.

Here is an example of setting up a built-in event listener that receives all the events and logs them through jboss-logging. (Using a logger called `org.keycloak.events`, error events are logged as `WARN`, and others are logged as `DEBUG`.)

For example, on:

- Linux:

```
$ kcadm.sh update events/config -r demorealm -s 'eventsListeners=["jboss-logging"]'
```

- Windows:

```
c:\> kcadm update events/config -r demorealm -s "eventsListeners=[\"jboss-logging\"]"
```

Here is an example of turning on storage of all available ERROR events—not including auditing events—for 2 days so they can be retrieved via Admin REST.

For example, on:

- Linux:

```
$ kcadm.sh update events/config -r demorealm -s eventsEnabled=true -s 'enabledEventTypes=["LOGIN_ERROR","REGISTER_ERROR","LOGOUT_ERROR","CODE_TO_TOKEN_ERROR","CLIENT_LOGIN_ERROR","FEDERATED_IDENTITY_LINK_ERROR","REMOVE_FEDERATED_IDENTITY_ERROR","UPDATE_EMAIL_ERROR","UPDATE_PROFILE_ERROR","UPDATE_PASSWORD_ERROR","UPDATE_TOTP_ERROR","VERIFY_EMAIL_ERROR","REMOVE_TOTP_ERROR","SEND_VERIFY_EMAIL_ERROR","SEND_RESET_PASSWORD_ERROR","SEND_IDENTITY_PROVIDER_LINK_ERROR","RESET_PASSWORD_ERROR","IDENTITY_PROVIDER_FIRST_LOGIN_ERROR","IDENTITY_PROVIDER_POST_LOGIN_ERROR","CUSTOM_REQUIRED_ACTION_ERROR","EXECUTE_ACTIONS_ERROR","CLIENT_REGISTER_ERROR","CLIENT_UPDATE_ERROR","CLIENT_DELETE_ERROR"]' -s eventsExpiration=172800
```

- Windows:

```
c:\> kcadm update events/config -r demorealm -s eventsEnabled=true -s "enabledEventTypes=[\"LOGIN_ERROR\",\"REGISTER_ERROR\",\"LOGOUT_ERROR\",\"CODE_TO_TOKEN_ERROR\",\"CLIENT_LOGIN_ERROR\",\"FEDERATED_IDENTITY_LINK_ERROR\",\"REMOVE_FEDERATED_IDENTITY_ERROR\",\"UPDATE_EMAIL_ERROR\",\"UPDATE_PROFILE_ERROR\",\"UPDATE_PASSWORD_ERROR\",\"UPDATE_TOTP_ERROR\",\"VERIFY_EMAIL_ERROR\",\"REMOVE_TOTP_ERROR\",\"SEND_VERIFY_EMAIL_ERROR\",\"SEND_RESET_PASSWORD_ERROR\",\"SEND_IDENTITY_PROVIDER_LINK_ERROR\",\"RESET_PASSWORD_ERROR\",\"IDENTITY_PROVIDER_FIRST_LOGIN_ERROR\",\"IDENTITY_PROVIDER_POST_LOGIN_ERROR\",\"CUSTOM_REQUIRED_ACTION_ERROR\",\"EXECUTE_ACTIONS_ERROR\",\"CLIENT_REGISTER_ERROR\",\"CLIENT_UPDATE_ERROR\",\"CLIENT_DELETE_ERROR\"]" -s eventsExpiration=172800
```

Here is an example of how to reset stored event types to **all available event types**; setting to empty list is the same as enumerating all.

```
$ kcadm.sh update events/config -r demorealm -s enabledEventTypes=[]
```

Here is an example of how to enable storage of auditing events.

```
$ kcadm.sh update events/config -r demorealm -s adminEventsEnabled=true -s adminEventsDetailsEnabled=true
```

Here is an example of how to get the last 100 events; they are ordered from newest to oldest.

```
$ kcadm.sh get events --offset 0 --limit 100
```

Here is an example of how to delete all saved events.

```
$ kcadm delete events
```

<a name="243_____Flushing_the_caches"></a>
#### Flushing the caches
1. Use the `create` command and one of the following endpoints: `clear-realm-cache`, `clear-user-cache`, or `clear-keys-cache`.

2. Set `realm` to the same value as the target realm.

   For example:

   ```
   $ kcadm.sh create clear-realm-cache -r demorealm -s realm=demorealm
   $ kcadm.sh create clear-user-cache -r demorealm -s realm=demorealm
   $ kcadm.sh create clear-keys-cache -r demorealm -s realm=demorealm
   ```

<a name="244_____Importing_a_realm_from_exported__json_file"></a>
#### Importing a realm from exported .json file
1. Use the `create` command on the `partialImport` endpoint.

2. Set `ifResourceExists` to one of `FAIL`, `SKIP`, `OVERWRITE`.

3. Use `-f` to submit the exported realm `.json` file

   For example:

   ```
   $ kcadm.sh create partialImport -r demorealm2 -s ifResourceExists=FAIL -o -f demorealm.json
   ```

   If realm does not yet exist, you first have to create it.

   For example:

   ```
   $ kcadm.sh create realms -s realm=demorealm2 -s enabled=true
   ```

<a name="245____19_7__Role_operations"></a>
### 19.7. Role operations
<a name="246_____Creating_a_realm_role"></a>
#### Creating a realm role
Use the `roles` endpoint to create a realm role.

```
$ kcadm.sh create roles -r demorealm -s name=user -s 'description=Regular user with limited set of permissions'
```

<a name="247_____Creating_a_client_role"></a>
#### Creating a client role
1. Identify the client first and then use the `get` command to list available clients when creating a client role.

   ```
   $ kcadm.sh get clients -r demorealm --fields id,clientId
   ```

2. Create a new role by using the `clientId` attribute to construct an endpoint URI, such as `clients/ID/roles`.

   For example:

   ```
   $ kcadm.sh create clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles -r demorealm -s name=editor -s 'description=Editor can edit, and publish any article'
   ```

<a name="248_____Listing_realm_roles"></a>
#### Listing realm roles
Use the `get` command on the `roles` endpoint to list existing realm roles.

```
$ kcadm.sh get roles -r demorealm
```

You can also use the `get-roles` command.

```
$ kcadm.sh get-roles -r demorealm
```

<a name="249_____Listing_client_roles"></a>
#### Listing client roles
There is a dedicated `get-roles` command to simplify listing realm and client roles. It is an extension of the `get` command and behaves the same with additional semantics for listing roles.

Use the `get-roles` command, passing it either the clientId attribute (via the `--cclientid` option) or `id` (via the `--cid`option) to identify the client to list client roles.

For example:

```
$ kcadm.sh get-roles -r demorealm --cclientid realm-management
```

<a name="250_____Getting_a_specific_realm_role"></a>
#### Getting a specific realm role
Use the `get` command and the role `name` to construct an endpoint URI for a specific realm role: `roles/ROLE_NAME`, where `user` is the name of the existing role.

For example:

```
$ kcadm.sh get roles/user -r demorealm
```

You can also use the special `get-roles` command, passing it a role name (via the `--rolename` option) or ID (via the `--roleid` option).

For example:

```
$ kcadm.sh get-roles -r demorealm --rolename user
```

<a name="251_____Getting_a_specific_client_role"></a>
#### Getting a specific client role
Use a dedicated `get-roles` command, passing it either the clientId attribute (via the `--cclientid` option) or ID (via the `--cid` option) to identify the client, and passing it either the role name (via the `--rolename` option) or ID (via the `--roleid`) to identify a specific client role.

For example:

```
$ kcadm.sh get-roles -r demorealm --cclientid realm-management --rolename manage-clients
```

<a name="252_____Updating_a_realm_role"></a>
#### Updating a realm role
Use the `update` command with the same endpoint URI that you used to get a specific realm role.

For example:

```
$ kcadm.sh update roles/user -r demorealm -s 'description=Role representing a regular user'
```

<a name="253_____Updating_a_client_role"></a>
#### Updating a client role
Use the `update` command with the same endpoint URI that you used to get a specific client role.

For example:

```
$ kcadm.sh update clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles/editor -r demorealm -s 'description=User that can edit, and publish articles'
```

<a name="254_____Deleting_a_realm_role"></a>
#### Deleting a realm role
Use the `delete` command with the same endpoint URI that you used to get a specific realm role.

For example:

```
$ kcadm.sh delete roles/user -r demorealm
```

<a name="255_____Deleting_a_client_role"></a>
#### Deleting a client role
Use the `delete` command with the same endpoint URI that you used to get a specific client role.

For example:

```
$ kcadm.sh delete clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles/editor -r demorealm
```

<a name="256_____Listing_assigned__available__and_effective_realm_roles_for_a_composite_role"></a>
#### Listing assigned, available, and effective realm roles for a composite role
Use a dedicated `get-roles` command to list assigned, available, and effective realm roles for a composite role.

1. To list **assigned** realm roles for the composite role, you can specify the target composite role by either name (via the `--rname` option) or ID (via the `--rid` option).

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole
   ```

2. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole --effective
   ```

3. Use the `--available` option to list realm roles that can still be added to the composite role.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole --available
   ```

<a name="257_____Listing_assigned__available__and_effective_client_roles_for_a_composite_role"></a>
#### Listing assigned, available, and effective client roles for a composite role
Use a dedicated `get-roles` command to list assigned, available, and effective client roles for a composite role.

1. To list **assigned** client roles for the composite role, you can specify the target composite role by either name (via the `--rname` option) or ID (via the `--rid` option) and client by either the clientId attribute (via the `--cclientid` option) or ID (via the `--cid` option).

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole --cclientid realm-management
   ```

2. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole --cclientid realm-management --effective
   ```

3. Use the `--available` option to list realm roles that can still be added to the target composite role.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --rname testrole --cclientid realm-management --available
   ```

<a name="258_____Adding_realm_roles_to_a_composite_role"></a>
#### Adding realm roles to a composite role
There is a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the `user` role to the composite role `testrole`.

```
$ kcadm.sh add-roles --rname testrole --rolename user -r demorealm
```

<a name="259_____Removing_realm_roles_from_a_composite_role"></a>
#### Removing realm roles from a composite role
There is a dedicated `remove-roles` command that can be used to remove realm roles and client roles.

The following example removes the `user` role from the target composite role `testrole`.

```
$ kcadm.sh remove-roles --rname testrole --rolename user -r demorealm
```

<a name="260_____Adding_client_roles_to_a_realm_role"></a>
#### Adding client roles to a realm role
Use a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the roles defined on the client `realm-management` - `create-client` role and the `view-users` role to the `testrole` composite role.

```
$ kcadm.sh add-roles -r demorealm --rname testrole --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="261_____Adding_client_roles_to_a_client_role"></a>
#### Adding client roles to a client role
1. Determine the ID of the composite client role by using the `get-roles` command.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --cclientid test-client --rolename operations
   ```

2. Assume that there is a client with a clientId attribute of `test-client`, a client role called `support`, and another client role called `operations`, which becomes a composite role, that has an ID of "fc400897-ef6a-4e8c-872b-1581b7fa8a71".

3. Use the following example to add another role to the composite role.

   ```
   $ kcadm.sh add-roles -r demorealm --cclientid test-client --rid fc400897-ef6a-4e8c-872b-1581b7fa8a71 --rolename support
   ```

4. List the roles of a composite role by using the `get-roles --all` command.

   For example:

   ```
   $ kcadm.sh get-roles --rid fc400897-ef6a-4e8c-872b-1581b7fa8a71 --all
   ```

<a name="262_____Removing_client_roles_from_a_composite_role"></a>
#### Removing client roles from a composite role
Use a dedicated `remove-roles` command to remove client roles from a composite role.

Use the following example to remove two roles defined on the client `realm management` - `create-client` role and the `view-users` role from the `testrole` composite role.

```
$ kcadm.sh remove-roles -r demorealm --rname testrole --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="263_____Adding_client_roles_to_a_group"></a>
#### Adding client roles to a group
Use a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the roles defined on the client `realm-management` - `create-client` role and the `view-users` role to the `Group` group (via the `--gname` option). The group can alternatively be specified by ID (via the `--gid`option).

See [Group operations](https://www.keycloak.org/docs/latest/server_admin/index.html#_group_operations) for more operations that can be performed to groups.

```
$ kcadm.sh add-roles -r demorealm --gname Group --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="264_____Removing_client_roles_from_a_group"></a>
#### Removing client roles from a group
Use a dedicated `remove-roles` command to remove client roles from a group.

Use the following example to remove two roles defined on the client `realm management` - `create-client` role and the `view-users` role from the `Group` group.

See [Group operations](https://www.keycloak.org/docs/latest/server_admin/index.html#_group_operations) for more operations that can be performed to groups.

```
$ kcadm.sh remove-roles -r demorealm --gname Group --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="265____19_8__Client_operations"></a>
### 19.8. Client operations
<a name="266_____Creating_a_client"></a>
#### Creating a client
1. Run the `create` command on a `clients` endpoint to create a new client.

   For example:

   ```
   $ kcadm.sh create clients -r demorealm -s clientId=myapp -s enabled=true
   ```

2. Specify a secret if you want to set a secret for adapters to authenticate.

   For example:

   ```
   $ kcadm.sh create clients -r demorealm -s clientId=myapp -s enabled=true -s clientAuthenticatorType=client-secret -s secret=d0b8122f-8dfb-46b7-b68a-f5cc4e25d000
   ```

<a name="267_____Listing_clients"></a>
#### Listing clients
Use the `get` command on the `clients` endpoint to list clients.

For example:

```
$ kcadm.sh get clients -r demorealm --fields id,clientId
```

This example filters the output to list only the `id` and `clientId` attributes.

<a name="268_____Getting_a_specific_client"></a>
#### Getting a specific client
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm
```

<a name="269_____Getting_the_current_secret_for_a_specific_client"></a>
#### Getting the current secret for a specific client
Use a client’s ID to construct an endpoint URI, such as `clients/ID/client-secret`.

For example:

```
$ kcadm.sh get clients/$CID/client-secret
```

<a name="270_____Getting_an_adapter_configuration_file__keycloak_json__for_a_specific_client"></a>
#### Getting an adapter configuration file (keycloak.json) for a specific client
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/keycloak-oidc-keycloak-json`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3/installation/providers/keycloak-oidc-keycloak-json -r demorealm
```

<a name="271_____Getting_a_WildFly_subsystem_adapter_configuration_for_a_specific_client"></a>
#### Getting a WildFly subsystem adapter configuration for a specific client
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/keycloak-oidc-jboss-subsystem`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3/installation/providers/keycloak-oidc-jboss-subsystem -r demorealm
```

<a name="272_____Getting_a_Docker_v2_example_configuration_for_a_specific_client"></a>
#### Getting a Docker-v2 example configuration for a specific client
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/docker-v2-compose-yaml`.

Note that response will be in `.zip` format.

For example:

```
$ kcadm.sh get http://localhost:8080/auth/admin/realms/demorealm/clients/8f271c35-44e3-446f-8953-b0893810ebe7/installation/providers/docker-v2-compose-yaml -r demorealm > keycloak-docker-compose-yaml.zip
```

<a name="273_____Updating_a_client"></a>
#### Updating a client
Use the `update` command with the same endpoint URI that you used to get a specific client.

For example, on:

- Linux:

```
$ kcadm.sh update clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm -s enabled=false -s publicClient=true -s 'redirectUris=["http://localhost:8080/myapp/*"]' -s baseUrl=http://localhost:8080/myapp -s adminUrl=http://localhost:8080/myapp
```

- Windows:

```
c:\> kcadm update clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm -s enabled=false -s publicClient=true -s "redirectUris=[\"http://localhost:8080/myapp/*\"]" -s baseUrl=http://localhost:8080/myapp -s adminUrl=http://localhost:8080/myapp
```

<a name="274_____Deleting_a_client"></a>
#### Deleting a client
Use the `delete` command with the same endpoint URI that you used to get a specific client.

For example:

```
$ kcadm.sh delete clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm
```

<a name="275_____Adding_or_removing_roles_for_client_s_service_account"></a>
#### Adding or removing roles for client’s service account
Service account for the client is just a special kind of user account with username `service-account-CLIENT_ID`. You can perform user operations on this account as if it was a regular user.

<a name="276____19_9__User_operations"></a>
### 19.9. User operations
<a name="277_____Creating_a_user"></a>
#### Creating a user
Run the `create` command on the `users` endpoint to create a new user.

For example:

```
$ kcadm.sh create users -r demorealm -s username=testuser -s enabled=true
```

<a name="278_____Listing_users"></a>
#### Listing users
Use the `users` endpoint to list users. The target user will have to change the password the next time they log in.

For example:

```
$ kcadm.sh get users -r demorealm --offset 0 --limit 1000
```

You can filter users by `username`, `firstName`, `lastName`, or `email`.

For example:

```
$ kcadm.sh get users -r demorealm -q email=google.com
$ kcadm.sh get users -r demorealm -q username=testuser
```

|      | Filtering does not use exact matching. For example, the above example would match the value of the `username` attribute against the `*testuser*` pattern. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

You can also filter across multiple attributes by specifying multiple `-q` options, which return only users that match the condition for all the attributes.

<a name="279_____Getting_a_specific_user"></a>
#### Getting a specific user
Use a user’s ID to compose an endpoint URI, such as `users/USER_ID`.

For example:

```
$ kcadm.sh get users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm
```

<a name="280_____Updating_a_user"></a>
#### Updating a user
Use the `update` command with the same endpoint URI that you used to get a specific user.

For example, on:

- Linux:

```
$ kcadm.sh update users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm -s 'requiredActions=["VERIFY_EMAIL","UPDATE_PROFILE","CONFIGURE_TOTP","UPDATE_PASSWORD"]'
```

- Windows:

```
c:\> kcadm update users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm -s "requiredActions=[\"VERIFY_EMAIL\",\"UPDATE_PROFILE\",\"CONFIGURE_TOTP\",\"UPDATE_PASSWORD\"]"
```

<a name="281_____Deleting_a_user"></a>
#### Deleting a user
Use the `delete` command with the same endpoint URI that you used to get a specific user.

For example:

```
$ kcadm.sh delete users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm
```

<a name="282_____Resetting_a_user_s_password"></a>
#### Resetting a user’s password
Use the dedicated `set-password` command to reset a user’s password.

For example:

```
$ kcadm.sh set-password -r demorealm --username testuser --new-password NEWPASSWORD --temporary
```

That command sets a temporary password for the user. The target user will have to change the password the next time they log in.

You can use `--userid` if you want to specify the user by using the `id` attribute.

You can achieve the same result using the `update` command on an endpoint constructed from the one you used to get a specific user, such as `users/USER_ID/reset-password`.

For example:

```
$ kcadm.sh update users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2/reset-password -r demorealm -s type=password -s value=NEWPASSWORD -s temporary=true -n
```

The last parameter (`-n`) ensures that only the `PUT` command is performed without a prior `GET` command. It is necessary in this instance because the `reset-password` endpoint does not support `GET`.

<a name="283_____Listing_assigned__available__and_effective_realm_roles_for_a_user"></a>
#### Listing assigned, available, and effective realm roles for a user
You can use a dedicated `get-roles` command to list assigned, available, and effective realm roles for a user.

1. Specify the target user by either user name or ID to list **assigned** realm roles for the user.

   For example:

```
$ kcadm.sh get-roles -r demorealm --uusername testuser
```

1. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --uusername testuser --effective
   ```

2. Use the `--available` option to list realm roles that can still be added to the user.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --uusername testuser --available
   ```

<a name="284_____Listing_assigned__available__and_effective_client_roles_for_a_user"></a>
#### Listing assigned, available, and effective client roles for a user
Use a dedicated `get-roles` command to list assigned, available, and effective client roles for a user.

1. Specify the target user by either a user name (via the `--uusername` option) or an ID (via the `--uid` option) and client by either a clientId attribute (via the `--cclientid` option) or an ID (via the `--cid` option) to list **assigned** client roles for the user.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --uusername testuser --cclientid realm-management
   ```

2. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --uusername testuser --cclientid realm-management --effective
   ```

3. Use the `--available` option to list realm roles that can still be added to the user.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --uusername testuser --cclientid realm-management --available
   ```

<a name="285_____Adding_realm_roles_to_a_user"></a>
#### Adding realm roles to a user
Use a dedicated `add-roles` command to add realm roles to a user.

Use the following example to add the `user` role to user `testuser`.

```
$ kcadm.sh add-roles --uusername testuser --rolename user -r demorealm
```

<a name="286_____Removing_realm_roles_from_a_user"></a>
#### Removing realm roles from a user
Use a dedicated `remove-roles` command to remove realm roles from a user.

Use the following example to remove the `user` role from the user `testuser`.

```
$ kcadm.sh remove-roles --uusername testuser --rolename user -r demorealm
```

<a name="287_____Adding_client_roles_to_a_user"></a>
#### Adding client roles to a user
Use a dedicated `add-roles` command to add client roles to a user.

Use the following example to add two roles defined on the client `realm management` - `create-client` role and the `view-users` role to the user `testuser`.

```
$ kcadm.sh add-roles -r demorealm --uusername testuser --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="288_____Removing_client_roles_from_a_user"></a>
#### Removing client roles from a user
Use a dedicated `remove-roles` command to remove client roles from a user.

Use the following example to remove two roles defined on the realm management client.

```
$ kcadm.sh remove-roles -r demorealm --uusername testuser --cclientid realm-management --rolename create-client --rolename view-users
```

<a name="289_____Listing_a_user_s_sessions"></a>
#### Listing a user’s sessions
1. Identify the user’s ID, and then use it to compose an endpoint URI, such as `users/ID/sessions`.

2. Use the `get` command to retrieve a list of the user’s sessions.

   For example:

   ```
   $kcadm get users/6da5ab89-3397-4205-afaa-e201ff638f9e/sessions
   ```

<a name="290_____Logging_out_a_user_from_a_specific_session"></a>
#### Logging out a user from a specific session
1. Determine the session’s ID as described above.

2. Use the session’s ID to compose an endpoint URI, such as `sessions/ID`.

3. Use the `delete` command to invalidate the session.

   For example:

   ```
   $ kcadm.sh delete sessions/d0eaa7cc-8c5d-489d-811a-69d3c4ec84d1
   ```

<a name="291_____Logging_out_a_user_from_all_sessions"></a>
#### Logging out a user from all sessions
You need a user’s ID to construct an endpoint URI, such as `users/ID/logout`.

Use the `create` command to perform `POST` on that endpoint URI.

For example:

```
$ kcadm.sh create users/6da5ab89-3397-4205-afaa-e201ff638f9e/logout -r demorealm -s realm=demorealm -s user=6da5ab89-3397-4205-afaa-e201ff638f9e
```

<a name="292____19_10__Group_operations"></a>
### 19.10. Group operations
<a name="293_____Creating_a_group"></a>
#### Creating a group
Use the `create` command on the `groups` endpoint to create a new group.

For example:

```
$ kcadm.sh create groups -r demorealm -s name=Group
```

<a name="294_____Listing_groups"></a>
#### Listing groups
Use the `get` command on the `groups` endpoint to list groups.

For example:

```
$ kcadm.sh get groups -r demorealm
```

<a name="295_____Getting_a_specific_group"></a>
#### Getting a specific group
Use the group’s ID to construct an endpoint URI, such as `groups/GROUP_ID`.

For example:

```
$ kcadm.sh get groups/51204821-0580-46db-8f2d-27106c6b5ded -r demorealm
```

<a name="296_____Updating_a_group"></a>
#### Updating a group
Use the `update` command with the same endpoint URI that you used to get a specific group.

For example:

```
$ kcadm.sh update groups/51204821-0580-46db-8f2d-27106c6b5ded -s 'attributes.email=["group@example.com"]' -r demorealm
```

<a name="297_____Deleting_a_group"></a>
#### Deleting a group
Use the `delete` command with the same endpoint URI that you used to get a specific group.

For example:

```
$ kcadm.sh delete groups/51204821-0580-46db-8f2d-27106c6b5ded -r demorealm
```

<a name="298_____Creating_a_subgroup"></a>
#### Creating a subgroup
Find the ID of the parent group by listing groups, and then use that ID to construct an endpoint URI, such as `groups/GROUP_ID/children`.

For example:

```
$ kcadm.sh create groups/51204821-0580-46db-8f2d-27106c6b5ded/children -r demorealm -s name=SubGroup
```

<a name="299_____Moving_a_group_under_another_group"></a>
#### Moving a group under another group
1. Find the ID of an existing parent group and of an existing child group.
2. Use the parent group’s ID to construct an endpoint URI, such as `groups/PARENT_GROUP_ID/children`.
3. Run the `create` command on this endpoint and pass the child group’s ID as a JSON body.

For example:

```
$ kcadm.sh create groups/51204821-0580-46db-8f2d-27106c6b5ded/children -r demorealm -s id=08d410c6-d585-4059-bb07-54dcb92c5094
```

<a name="300_____Get_groups_for_a_specific_user"></a>
#### Get groups for a specific user
Use a user’s ID to determine a user’s membership in groups to compose an endpoint URI, such as `users/USER_ID/groups`.

For example:

```
$ kcadm.sh get users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups -r demorealm
```

<a name="301_____Adding_a_user_to_a_group"></a>
#### Adding a user to a group
Use the `update` command with an endpoint URI composed from user’s ID and a group’s ID, such as `users/USER_ID/groups/GROUP_ID`, to add a user to a group.

For example:

```
$ kcadm.sh update users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups/ce01117a-7426-4670-a29a-5c118056fe20 -r demorealm -s realm=demorealm -s userId=b544f379-5fc4-49e5-8a8d-5cfb71f46f53 -s groupId=ce01117a-7426-4670-a29a-5c118056fe20 -n
```

<a name="302_____Removing_a_user_from_a_group"></a>
#### Removing a user from a group
Use the `delete` command on the same endpoint URI as used for adding a user to a group, such as `users/USER_ID/groups/GROUP_ID`, to remove a user from a group.

For example:

```
$ kcadm.sh delete users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups/ce01117a-7426-4670-a29a-5c118056fe20 -r demorealm
```

<a name="303_____Listing_assigned__available__and_effective_realm_roles_for_a_group"></a>
#### Listing assigned, available, and effective realm roles for a group
Use a dedicated `get-roles` command to list assigned, available, and effective realm roles for a group.

1. Specify the target group by name (via the `--gname` option), path (via the [command] `--gpath` option), or ID (via the `--gid` option) to list **assigned** realm roles for the group.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group
   ```

2. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group --effective
   ```

3. Use the `--available` option to list realm roles that can still be added to the group.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group --available
   ```

<a name="304_____Listing_assigned__available__and_effective_client_roles_for_a_group"></a>
#### Listing assigned, available, and effective client roles for a group
Use a dedicated `get-roles` command to list assigned, available, and effective client roles for a group.

1. Specify the target group by either name (via the `--gname` option) or ID (via the `--gid` option), and client by either the clientId attribute (via the [command] `--cclientid` option) or ID (via the `--id` option) to list **assigned** client roles for the user.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group --cclientid realm-management
   ```

2. Use the additional `--effective` option to list **effective** realm roles.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group --cclientid realm-management --effective
   ```

3. Use the `--available` option to list realm roles that can still be added to the group.

   For example:

   ```
   $ kcadm.sh get-roles -r demorealm --gname Group --cclientid realm-management --available
   ```

<a name="305____19_11__Identity_provider_operations"></a>
### 19.11. Identity provider operations
<a name="306_____Listing_available_identity_providers"></a>
#### Listing available identity providers
Use the `serverinfo` endpoint to list available identity providers.

For example:

```
$ kcadm.sh get serverinfo -r demorealm --fields 'identityProviders(*)'
```

|      | The `serverinfo` endpoint is handled similarly to the `realms` endpoint in that it is not resolved relative to a target realm because it exists outside any specific realm. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

<a name="307_____Listing_configured_identity_providers"></a>
#### Listing configured identity providers
Use the `identity-provider/instances` endpoint.

For example:

```
$ kcadm.sh get identity-provider/instances -r demorealm --fields alias,providerId,enabled
```

<a name="308_____Getting_a_specific_configured_identity_provider"></a>
#### Getting a specific configured identity provider
Use the `alias` attribute of the identity provider to construct an endpoint URI, such as `identity-provider/instances/ALIAS`, to get a specific identity provider.

For example:

```
$ kcadm.sh get identity-provider/instances/facebook -r demorealm
```

<a name="309_____Removing_a_specific_configured_identity_provider"></a>
#### Removing a specific configured identity provider
Use the `delete` command with the same endpoint URI that you used to get a specific configured identity provider to remove a specific configured identity provider.

For example:

```
$ kcadm.sh delete identity-provider/instances/facebook -r demorealm
```

<a name="310_____Configuring_a_Keycloak_OpenID_Connect_identity_provider"></a>
#### Configuring a Keycloak OpenID Connect identity provider
1. Use `keycloak-oidc` as the `providerId` when creating a new identity provider instance.

2. Provide the `config` attributes: `authorizationUrl`, `tokenUrl`, `clientId`, and `clientSecret`.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=keycloak-oidc -s providerId=keycloak-oidc -s enabled=true -s 'config.useJwksUrl="true"' -s config.authorizationUrl=http://localhost:8180/auth/realms/demorealm/protocol/openid-connect/auth -s config.tokenUrl=http://localhost:8180/auth/realms/demorealm/protocol/openid-connect/token -s config.clientId=demo-oidc-provider -s config.clientSecret=secret
   ```

<a name="311_____Configuring_an_OpenID_Connect_identity_provider"></a>
#### Configuring an OpenID Connect identity provider
Configure the generic OpenID Connect provider the same way you configure the Keycloak OpenID Connect provider, except that you set the `providerId` attribute value to `oidc`.

<a name="312_____Configuring_a_SAML_2_identity_provider"></a>
#### Configuring a SAML 2 identity provider
1. Use `saml` as the `providerId`.
2. Provide the `config` attributes: `singleSignOnServiceUrl`, `nameIDPolicyFormat`, and `signatureAlgorithm`.

For example:

```
$ kcadm.sh create identity-provider/instances -r demorealm -s alias=saml -s providerId=saml -s enabled=true -s 'config.useJwksUrl="true"' -s config.singleSignOnServiceUrl=http://localhost:8180/auth/realms/saml-broker-realm/protocol/saml -s config.nameIDPolicyFormat=urn:oasis:names:tc:SAML:2.0:nameid-format:persistent -s config.signatureAlgorithm=RSA_SHA256
```

<a name="313_____Configuring_a_Facebook_identity_provider"></a>
#### Configuring a Facebook identity provider
1. Use `facebook` as the `providerId`.

2. Provide the `config` attributes: `clientId` and `clientSecret`. You can find these attributes in the Facebook Developers application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=facebook -s providerId=facebook -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=FACEBOOK_CLIENT_ID -s config.clientSecret=FACEBOOK_CLIENT_SECRET
   ```

<a name="314_____Configuring_a_Google_identity_provider"></a>
#### Configuring a Google identity provider
1. Use `google` as the `providerId`.

2. Provide the `config` attributes: `clientId` and `clientSecret`. You can find these attributes in the Google Developers application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=google -s providerId=google -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=GOOGLE_CLIENT_ID -s config.clientSecret=GOOGLE_CLIENT_SECRET
   ```

<a name="315_____Configuring_a_Twitter_identity_provider"></a>
#### Configuring a Twitter identity provider
1. Use `twitter` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the Twitter Application Management application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=google -s providerId=google -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=TWITTER_API_KEY -s config.clientSecret=TWITTER_API_SECRET
   ```

<a name="316_____Configuring_a_GitHub_identity_provider"></a>
#### Configuring a GitHub identity provider
1. Use `github` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the GitHub Developer Application Settings page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=github -s providerId=github -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=GITHUB_CLIENT_ID -s config.clientSecret=GITHUB_CLIENT_SECRET
   ```

<a name="317_____Configuring_a_LinkedIn_identity_provider"></a>
#### Configuring a LinkedIn identity provider
1. Use `linkedin` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the LinkedIn Developer Console application page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=linkedin -s providerId=linkedin -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=LINKEDIN_CLIENT_ID -s config.clientSecret=LINKEDIN_CLIENT_SECRET
   ```

<a name="318_____Configuring_a_Microsoft_Live_identity_provider"></a>
#### Configuring a Microsoft Live identity provider
1. Use `microsoft` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the Microsoft Application Registration Portal page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=microsoft -s providerId=microsoft -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=MICROSOFT_APP_ID -s config.clientSecret=MICROSOFT_PASSWORD
   ```

<a name="319_____Configuring_a_Stack_Overflow_identity_provider"></a>
#### Configuring a Stack Overflow identity provider
1. Use `stackoverflow` command as the `providerId`.

2. Provide the `config` attributes `clientId`, `clientSecret`, and `key`. You can find these attributes in the Stack Apps OAuth page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=stackoverflow -s providerId=stackoverflow -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=STACKAPPS_CLIENT_ID -s config.clientSecret=STACKAPPS_CLIENT_SECRET -s config.key=STACKAPPS_KEY
   ```

<a name="320____19_12__Storage_provider_operations"></a>
### 19.12. Storage provider operations
<a name="321_____Configuring_a_Kerberos_storage_provider"></a>
#### Configuring a Kerberos storage provider
1. Use the `create` command against the `components` endpoint.

2. Specify realm id as a value of the `parentId` attribute.

3. Specify `kerberos` as the value of the `providerId` attribute, and `org.keycloak.storage.UserStorageProvider` as the value of the `providerType` attribute.

4. For example:

   ```
   $ kcadm.sh create components -r demorealm -s parentId=demorealmId -s id=demokerberos -s name=demokerberos -s providerId=kerberos -s providerType=org.keycloak.storage.UserStorageProvider -s 'config.priority=["0"]' -s 'config.debug=["false"]' -s 'config.allowPasswordAuthentication=["true"]' -s 'config.editMode=["UNSYNCED"]' -s 'config.updateProfileFirstLogin=["true"]' -s 'config.allowKerberosAuthentication=["true"]' -s 'config.kerberosRealm=["KEYCLOAK.ORG"]' -s 'config.keyTab=["http.keytab"]' -s 'config.serverPrincipal=["HTTP/localhost@KEYCLOAK.ORG"]' -s 'config.cachePolicy=["DEFAULT"]'
   ```

<a name="322_____Configuring_an_LDAP_user_storage_provider"></a>
#### Configuring an LDAP user storage provider
1. Use the `create` command against the `components` endpoint.

2. Specify `ldap` as a value of the `providerId` attribute, and `org.keycloak.storage.UserStorageProvider` as the value of the `providerType` attribute.

3. Provide the realm ID as the value of the `parentId` attribute.

4. Use the following example to create a Kerberos-integrated LDAP provider.

   ```
   $ kcadm.sh create components -r demorealm -s name=kerberos-ldap-provider -s providerId=ldap -s providerType=org.keycloak.storage.UserStorageProvider -s parentId=3d9c572b-8f33-483f-98a6-8bb421667867  -s 'config.priority=["1"]' -s 'config.fullSyncPeriod=["-1"]' -s 'config.changedSyncPeriod=["-1"]' -s 'config.cachePolicy=["DEFAULT"]' -s config.evictionDay=[] -s config.evictionHour=[] -s config.evictionMinute=[] -s config.maxLifespan=[] -s 'config.batchSizeForSync=["1000"]' -s 'config.editMode=["WRITABLE"]' -s 'config.syncRegistrations=["false"]' -s 'config.vendor=["other"]' -s 'config.usernameLDAPAttribute=["uid"]' -s 'config.rdnLDAPAttribute=["uid"]' -s 'config.uuidLDAPAttribute=["entryUUID"]' -s 'config.userObjectClasses=["inetOrgPerson, organizationalPerson"]' -s 'config.connectionUrl=["ldap://localhost:10389"]'  -s 'config.usersDn=["ou=People,dc=keycloak,dc=org"]' -s 'config.authType=["simple"]' -s 'config.bindDn=["uid=admin,ou=system"]' -s 'config.bindCredential=["secret"]' -s 'config.searchScope=["1"]' -s 'config.useTruststoreSpi=["ldapsOnly"]' -s 'config.connectionPooling=["true"]' -s 'config.pagination=["true"]' -s 'config.allowKerberosAuthentication=["true"]' -s 'config.serverPrincipal=["HTTP/localhost@KEYCLOAK.ORG"]' -s 'config.keyTab=["http.keytab"]' -s 'config.kerberosRealm=["KEYCLOAK.ORG"]' -s 'config.debug=["true"]' -s 'config.useKerberosForPasswordAuthentication=["true"]'
   ```

<a name="323_____Removing_a_user_storage_provider_instance"></a>
#### Removing a user storage provider instance
1. Use the storage provider instance’s `id` attribute to compose an endpoint URI, such as `components/ID`.

2. Run the `delete` command against this endpoint.

   For example:

   ```
   $ kcadm.sh delete components/3d9c572b-8f33-483f-98a6-8bb421667867 -r demorealm
   ```

<a name="324_____Triggering_synchronization_of_all_users_for_a_specific_user_storage_provider"></a>
#### Triggering synchronization of all users for a specific user storage provider
1. Use the storage provider’s `id` attribute to compose an endpoint URI, such as `user-storage/ID_OF_USER_STORAGE_INSTANCE/sync`.

2. Add the `action=triggerFullSync` query parameter and run the `create` command.

   For example:

   ```
   $ kcadm.sh create user-storage/b7c63d02-b62a-4fc1-977c-947d6a09e1ea/sync?action=triggerFullSync
   ```

<a name="325_____Triggering_synchronization_of_changed_users_for_a_specific_user_storage_provider"></a>
#### Triggering synchronization of changed users for a specific user storage provider
1. Use the storage provider’s `id` attribute to compose an endpoint URI, such as `user-storage/ID_OF_USER_STORAGE_INSTANCE/sync`.

2. Add the `action=triggerChangedUsersSync` query parameter and run the `create` command.

   For example:

   ```
   $ kcadm.sh create user-storage/b7c63d02-b62a-4fc1-977c-947d6a09e1ea/sync?action=triggerChangedUsersSync
   ```

<a name="326_____Test_LDAP_user_storage_connectivity"></a>
#### Test LDAP user storage connectivity
1. Run the `get` command on the `testLDAPConnection` endpoint.

2. Provide query parameters `bindCredential`, `bindDn`, `connectionUrl`, and `useTruststoreSpi`, and then set the `action` query parameter to `testConnection`.

   For example:

   ```
   $ kcadm.sh get testLDAPConnection -q action=testConnection -q bindCredential=secret -q bindDn=uid=admin,ou=system -q connectionUrl=ldap://localhost:10389 -q useTruststoreSpi=ldapsOnly
   ```

<a name="327_____Test_LDAP_user_storage_authentication"></a>
#### Test LDAP user storage authentication
1. Run the `get` command on the `testLDAPConnection` endpoint.

2. Provide the query parameters `bindCredential`, `bindDn`, `connectionUrl`, and `useTruststoreSpi`, and then set the `action` query parameter to `testAuthentication`.

   For example:

   ```
   $ kcadm.sh get testLDAPConnection -q action=testAuthentication -q bindCredential=secret -q bindDn=uid=admin,ou=system -q connectionUrl=ldap://localhost:10389 -q useTruststoreSpi=ldapsOnly
   ```

<a name="328____19_13__Adding_mappers"></a>
### 19.13. Adding mappers
<a name="329_____Adding_a_hardcoded_role_LDAP_mapper"></a>
#### Adding a hardcoded role LDAP mapper
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `hardcoded-ldap-role-mapper`. Make sure to provide a value of `role` configuration parameter.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=hardcoded-ldap-role-mapper -s providerId=hardcoded-ldap-role-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config.role=["realm-management.create-client"]'
   ```

<a name="330_____Adding_an_MS_Active_Directory_mapper"></a>
#### Adding an MS Active Directory mapper
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `msad-user-account-control-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=msad-user-account-control-mapper -s providerId=msad-user-account-control-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea
   ```

<a name="331_____Adding_a_user_attribute_LDAP_mapper"></a>
#### Adding a user attribute LDAP mapper
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `user-attribute-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=user-attribute-ldap-mapper -s providerId=user-attribute-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."user.model.attribute"=["email"]' -s 'config."ldap.attribute"=["mail"]' -s 'config."read.only"=["false"]' -s 'config."always.read.value.from.ldap"=["false"]' -s 'config."is.mandatory.in.ldap"=["false"]'
   ```

<a name="332_____Adding_a_group_LDAP_mapper"></a>
#### Adding a group LDAP mapper
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `group-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=group-ldap-mapper -s providerId=group-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."groups.dn"=[]' -s 'config."group.name.ldap.attribute"=["cn"]' -s 'config."group.object.classes"=["groupOfNames"]' -s 'config."preserve.group.inheritance"=["true"]' -s 'config."membership.ldap.attribute"=["member"]' -s 'config."membership.attribute.type"=["DN"]' -s 'config."groups.ldap.filter"=[]' -s 'config.mode=["LDAP_ONLY"]' -s 'config."user.roles.retrieve.strategy"=["LOAD_GROUPS_BY_MEMBER_ATTRIBUTE"]' -s 'config."mapped.group.attributes"=["admins-group"]' -s 'config."drop.non.existing.groups.during.sync"=["false"]' -s 'config.roles=["admins"]' -s 'config.groups=["admins-group"]' -s 'config.group=[]' -s 'config.preserve=["true"]' -s 'config.membership=["member"]'
   ```

<a name="333_____Adding_a_full_name_LDAP_mapper"></a>
#### Adding a full name LDAP mapper
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `full-name-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=full-name-ldap-mapper -s providerId=full-name-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."ldap.full.name.attribute"=["cn"]' -s 'config."read.only"=["false"]' -s 'config."write.only"=["true"]'
   ```

<a name="334____19_14__Authentication_operations"></a>
### 19.14. Authentication operations
<a name="335_____Setting_a_password_policy"></a>
#### Setting a password policy
1. Set the realm’s `passwordPolicy` attribute to an enumeration expression that includes the specific policy provider ID and optional configuration.

2. Use the following example to set a password policy to default values. The default values include:

   - 27,500 hashing iterations

   - at least one special character

   - at least one uppercase character

   - at least one digit character

   - not be equal to a user’s `username`

   - be at least eight characters long

     ```
     $ kcadm.sh update realms/demorealm -s 'passwordPolicy="hashIterations and specialChars and upperCase and digits and notUsername and length"'
     ```

3. If you want to use values different from defaults, pass the configuration in brackets.

4. Use the following example to set a password policy to:

   - 25,000 hash iterations

   - at least two special characters

   - at least two uppercase characters

   - at least two lowercase characters

   - at least two digits

   - be at least nine characters long

   - not be equal to a user’s `username`

   - not repeat for at least four changes back

     ```
     $ kcadm.sh update realms/demorealm -s 'passwordPolicy="hashIterations(25000) and specialChars(2) and upperCase(2) and lowerCase(2) and digits(2) and length(9) and notUsername and passwordHistory(4)"'
     ```

<a name="336_____Getting_the_current_password_policy"></a>
#### Getting the current password policy
Get the current realm configuration and filter everything but the `passwordPolicy` attribute.

Use the following example to display `passwordPolicy` for `demorealm`.

```
$ kcadm.sh get realms/demorealm --fields passwordPolicy
```

<a name="337_____Listing_authentication_flows"></a>
#### Listing authentication flows
Run the `get` command on the `authentication/flows` endpoint.

For example:

```
$ kcadm.sh get authentication/flows -r demorealm
```

<a name="338_____Getting_a_specific_authentication_flow"></a>
#### Getting a specific authentication flow
Run the `get` command on the `authentication/flows/FLOW_ID` endpoint.

For example:

```
$ kcadm.sh get authentication/flows/febfd772-e1a1-42fb-b8ae-00c0566fafb8 -r demorealm
```

<a name="339_____Listing_executions_for_a_flow"></a>
#### Listing executions for a flow
Run the `get` command on the `authentication/flows/FLOW_ALIAS/executions` endpoint.

For example:

```bash
$ kcadm.sh get authentication/flows/Copy%20of%20browser/executions -r demorealm
```

<a name="340_____Adding_configuration_to_an_execution"></a>
#### Adding configuration to an execution
1. Get execution for a flow, and take note of its ID
2. Run the `create` command on the `authentication/executions/{executionId}/config` endpoint.

For example:

```
$ kcadm create "authentication/executions/a3147129-c402-4760-86d9-3f2345e401c7/config" -r examplerealm -b '{"config":{"x509-cert-auth.mapping-source-selection":"Match SubjectDN using regular expression","x509-cert-auth.regular-expression":"(.*?)(?:$)","x509-cert-auth.mapper-selection":"Custom Attribute Mapper","x509-cert-auth.mapper-selection.user-attribute-name":"usercertificate","x509-cert-auth.crl-checking-enabled":"","x509-cert-auth.crldp-checking-enabled":false,"x509-cert-auth.crl-relative-path":"crl.pem","x509-cert-auth.ocsp-checking-enabled":"","x509-cert-auth.ocsp-responder-uri":"","x509-cert-auth.keyusage":"","x509-cert-auth.extendedkeyusage":"","x509-cert-auth.confirmation-page-disallowed":""},"alias":"my_otp_config"}'
```

<a name="341_____Getting_configuration_for_an_execution"></a>
#### Getting configuration for an execution
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `get` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm get "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm
```

<a name="342_____Updating_configuration_for_an_execution"></a>
#### Updating configuration for an execution
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `update` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm update "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm -b '{"id":"dd91611a-d25c-421a-87e2-227c18421833","alias":"my_otp_config","config":{"x509-cert-auth.extendedkeyusage":"","x509-cert-auth.mapper-selection.user-attribute-name":"usercertificate","x509-cert-auth.ocsp-responder-uri":"","x509-cert-auth.regular-expression":"(.*?)(?:$)","x509-cert-auth.crl-checking-enabled":"true","x509-cert-auth.confirmation-page-disallowed":"","x509-cert-auth.keyusage":"","x509-cert-auth.mapper-selection":"Custom Attribute Mapper","x509-cert-auth.crl-relative-path":"crl.pem","x509-cert-auth.crldp-checking-enabled":"false","x509-cert-auth.mapping-source-selection":"Match SubjectDN using regular expression","x509-cert-auth.ocsp-checking-enabled":""}}'
```

<a name="343_____Deleting_configuration_for_an_execution"></a>
#### Deleting configuration for an execution
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `delete` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm delete "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm
```

