# Server Administration Guide(服务器管理指南)

## 1. 概述 {#Overview}
Keycloak是针对Web应用程序和RESTful Web服务的单点登录解决方案。 Keycloak的目标是使安全性变得简单，以便应用程序开发人员可以轻松保护他们在组织中部署的应用程序和服务。开发人员通常必须为自己编写的安全功能是开箱即用的，并且可以根据组织的个性化需求轻松定制。 Keycloak为登录，注册，管理和帐户管理提供可定制的用户界面。您还可以使用Keycloak作为集成平台将其挂钩到现有的LDAP和Active Directory服务器中。您还可以将身份验证委派给第三方身份提供商，如Facebook和Google+。

### 1.1. 特征 {#Features}

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

### 1.2. 安全是如何工作的? {#How_Does_Security_Work_}

Keycloak是您在网络上管理的独立服务器。应用程序配置为指向此服务器并受其保护。 Keycloak使用开放协议标准，如[OpenID Connect](https://openid.net/connect/) 或 [SAML 2.0](http://saml.xml.org/saml-specifications)来保护您的应用程序。浏览器应用程序将用户的浏览器从应用程序重定向到Keycloak身份验证服务器，并在其中输入凭据。这很重要，因为用户与应用程序完全隔离，应用程序永远不会看到用户的凭据。相反，应用程序会获得一个加密签名的身份令牌或断言。这些令牌可以包含用户名，地址，电子邮件和其他个人资料数据等身份信息。他们还可以保存权限数据，以便应用程序可以做出授权决策。这些令牌还可用于对基于REST的服务进行安全调用。

### 1.3. 核心概念和术语 {#Core_Concepts_and_Terms}

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

## 2. 服务器初始化 {#Server_Initialization}

执行[服务器安装和配置指南](https://www.keycloak.org/docs/6.0/server_installation/)中定义的所有安装和配置任务后，您需要创建一个初始管理员帐户。 Keycloak没有开箱即用的任何已配置的管理员帐户。此帐户将允许您创建一个可以登录*master* realm管理控制台的管理员，以便您可以开始创建领域，用户并注册要由Keycloak保护的应用程序。

如果您的服务器可以从`localhost`访问，则可以通过转到<http://localhost:8080/auth>URL来启动它并创建此管理员用户。

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

## 3. 管理控制台 {#Admin_Console}

您的大部分管理任务将通过Keycloak管理控制台完成。 您可以直接转到<http://localhost:8080/auth/admin/>的控制台URL

登录页面

![login page](assets/login-page.png)

输入您在欢迎页面上创建的用户名和密码，或者在bin目录中输入`add-user-keycloak`脚本。 这将带您进入Keycloak管理控制台。

管理控制台

![admin console](assets/admin-console.png)

左下拉菜单允许您选择要管理的领域或创建新领域。 右下拉菜单允许您查看用户帐户或注销。 如果您对管理控制台中的某个功能，按钮或字段感到好奇，只需将鼠标悬停在任何问号`？`图标上。 这将弹出工具提示文本以描述您感兴趣的控制台区域。上图显示了工具提示的实际操作。

### 3.1. Master 领域 {#The_Master_Realm}

当你第一次启动Keycloak时，Keycloak会为你创建一个预先定义的领域。 这个初始领域是*master*领域。 它是领域等级中的最高级别。 此领域中的管理员帐户具有查看和管理在服务器实例上创建的任何其他领域的权限。 定义初始管理员帐户时，可以在*master*域中创建帐户。 您最初登录管理控制台也将通过*master*领域。

我们建议您不要使用* master *领域来管理组织中的用户和应用程序。 保留使用*master*realm 给 * super * admins来创建和管理系统中的领域。 遵循此安全模型有助于防止意外更改，并遵循允许用户帐户仅访问成功完成其当前任务所需的权限和权限的传统。

可以禁用*master*领域，并在您创建的每个新领域中定义管理员帐户。 每个领域都有自己的专用管理控制台，您可以使用本地帐户登录。 本指南在[Dedicated Realm Admin Consoles](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions)章节中详细介绍了这一点。

### 3.2. 创造一个新领域 {#Create_a_New_Realm}

创建一个新领域非常简单。 将鼠标悬停在标题为`Master`的左上角下拉菜单中。 如果您已登录主域，则此下拉菜单会列出所有已创建的域。 此下拉菜单的最后一个条目始终是`Add Realm`。 单击此按钮可添加领域。

添加领域菜单

![add realm menu](assets/add-realm-menu.png)

此菜单选项将带您进入`Add Realm` 页面。 指定要定义的域名，然后单击`Create`按钮。 或者，您可以导入定义新领域的JSON文档。 我们将在[导出和导入](https://www.keycloak.org/docs/latest/server_admin/index.html#_export_import)章节中详细介绍这一点。

创建领域

![create realm](assets/create-realm.png)

创建领域后，您将返回主管理控制台页面。 现在的领域现在将设置为您刚刚创建的领域。 您可以通过在左上角的下拉菜单中执行鼠标操作来切换管理不同领域。

### 3.3. SSL模式 {#SSL_Mode}

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

### 3.4. 清除服务器缓存 {#Clearing_Server_Caches}

Keycloak将在JVM的限制范围内和/或为其配置的限制内容缓存内存中的所有内容。 如果Keycloak数据库被服务器的REST API或管理控制台范围之外的第三方（即DBA）修改，则内存缓存的某些部分可能是陈旧的。 您可以通过转到`Realm Settings`左侧菜单项，从管理控制台清除外部公钥（外部客户端或身份提供者的公钥，Keycloak通常用于验证特定外部实体的签名）的域缓存，用户缓存或缓存 菜单项和`Cache`选项卡。

缓存选项卡

![cache tab](assets/cache-tab.png)

只需单击要清除的缓存上的`clear`按钮即可。

### 3.5. 电子邮件设置 {#Email_Settings}

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

由于电子邮件用于恢复用户名和密码，因此建议使用SSL或TLS，尤其是在SMTP服务器位于外部网络上时。 要启用SSL，请单击``Enable SSL`或启用TLS，单击`Enable TLS`。 您很可能还需要更改`Port`（SSL/TLS的默认端口是465）。

如果您的SMTP服务器需要身份验证，请单击 `Enable Authentication`并插入`Username`和`Password`。

### 3.6. 主题与国际化 {#Themes_and_Internationalization}

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

#### 3.6.1. 国际化 {#Internationalization}
每个UI屏幕都在Keycloak中国际化。 默认语言是英语，但是如果打开`Theme`选项卡上的`Internationalization`开关，您可以选择要支持的语言环境以及默认语言环境。 用户下次登录时，他们将能够在登录页面上选择一种语言，用于登录屏幕，用户帐户管理UI和管理控制台。 [服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)介绍了如何提供其他语言。

## 4. 用户管理 {#User_Management}

本节介绍管理用户的管理功能。

### 4.1. Searching For Users {#Searching_For_Users}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/viewing.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/viewing.adoc)

If you need to manage a specific user, click on `Users` in the left menu bar.

Users

![users](assets/users.png)

This menu option brings you to the user list page. In the search box you can type in a full name, last name, or email address you want to search for in the user database. The query will bring up all users that match your criteria. The `View all users`button will list every user in the system. This will search just local Keycloak database and not the federated database (ie. LDAP) because some backends like LDAP don’t have a way to page through users. So if you want the users from federated backend to be synced into Keycloak database you need to either:

- Adjust search criteria. That will sync just the backend users matching the criteria into Keycloak database.
- Go to `User Federation` tab and click `Sync all users` or `Sync changed users` in the page with your federation provider.

See [User Federation](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-storage-federation) for more details.

### 4.2. Creating New Users {#Creating_New_Users}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/create-user.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/create-user.adoc)

To create a user click on `Users` in the left menu bar.

Users

![users](assets/users.png)

This menu option brings you to the user list page. On the right side of the empty user list, you should see an `Add User`button. Click that to start creating your new user.

Add User

![add user](assets/add-user.png)

The only required field is `Username`. Click save. This will bring you to the management page for your new user.

### 4.3. Deleting Users {#Deleting_Users}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/delete-user.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/delete-user.adoc)

To delete a user click on `Users` in the left menu bar.

Users

![users](assets/users.png)

This menu option brings you to the user list page. Click `View all users` or search to find the user you intend to delete.

Add User

![delete user](assets/delete-user.png)

In the list of users, click `Delete` next to the user you want to remove. You will be asked to confirm that you are sure you want to delete this user. Click `Delete` in the confirmation box to confirm.

### 4.4. User Attributes {#User_Attributes}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/attributes.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/attributes.adoc)

Beyond basic user metadata like name and email, you can store arbitrary user attributes. Choose a user to manage then click on the `Attributes` tab.

Users

![user attributes](assets/user-attributes.png)

Enter in the attribute name and value in the empty fields and click the `Add` button next to it to add a new field. Note that any edits you make on this page will not be stored until you hit the `Save` button.

### 4.5. User Credentials {#User_Credentials}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/credentials.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/credentials.adoc)

When viewing a user if you go to the `Credentials` tab you can manage a user’s credentials.

Credential Management

![user credentials](assets/user-credentials.png)

#### 4.5.1. Changing Passwords {#Changing_Passwords}
To change a user’s password, type in a new one. A `Reset Password` button will show up that you click after you’ve typed everything in. If the `Temporary` switch is on, this new password can only be used once and the user will be asked to change their password after they have logged in.

Alternatively, if you have [email](https://www.keycloak.org/docs/latest/server_admin/index.html#_email) set up, you can send an email to the user that asks them to reset their password. Choose `Update Password` from the `Reset Actions` list box and click `Send Email`. You can optionally set the validity of the e-mail link which defaults to the one preset in `Tokens` tab in the realm settings. The sent email contains a link that will bring the user to the update password screen.

#### 4.5.2. Changing OTPs {#Changing_OTPs}
You cannot configure One-Time Passwords for a specific user within the Admin Console. This is the responsibility of the user. If the user has lost their OTP generator all you can do is disable OTP for them on the `Credentials` tab. If OTP is optional in your realm, the user will have to go to the User Account Management service to re-configure a new OTP generator. If OTP is required, then the user will be asked to re-configure a new OTP generator when they log in.

Like passwords, you can alternatively send an email to the user that will ask them to reset their OTP generator. Choose`Configure OTP` in the `Reset Actions` list box and click the `Send Email` button. The sent email contains a link that will bring the user to the OTP setup screen.

### 4.6. Required Actions {#Required_Actions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/required-actions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/required-actions.adoc)

Required Actions are tasks that a user must finish before they are allowed to log in. A user must provide their credentials before required actions are executed. Once a required action is completed, the user will not have to perform the action again. Here are explanations of some of the built-in required action types:

- Update Password

  When set, a user must change their password.

- Configure OTP

  When set, a user must configure a one-time password generator on their mobile device using either the Free OTP or Google Authenticator application.

- Verify Email

  When set, a user must verify that they have a valid email account. An email will be sent to the user with a link they have to click. Once this workflow is successfully completed, they will be allowed to log in.

- Update Profile

  This required action asks the user to update their profile information, i.e. their name, address, email, and/or phone number.

Admins can add required actions for each individual user within the user’s `Details` tab in the Admin Console.

Setting Required Action

![user required action](assets/user-required-action.png)

In the `Required User Actions` list box, select all the actions you want to add to the account. If you want to remove one, click the `X` next to the action name. Also remember to click the `Save` button after you’ve decided what actions to add.

#### 4.6.1. Default Required Actions {#Default_Required_Actions}
You can also specify required actions that will be added to an account whenever a new user is created, i.e. through the `Add User` button the user list screen, or via the [user registration](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-registration) link on the login page. To specify the default required actions go to the `Authentication` left menu item and click on the `Required Actions` tab.

Default Required Actions

![default required actions](assets/default-required-actions.png)

Simply click the checkbox in the `Default Action` column of the required actions that you want to be executed when a brand new user logs in.

#### 4.6.2. Terms and Conditions {#Terms_and_Conditions}
Many organizations have a requirement that when a new user logs in for the first time, they need to agree to the terms and conditions of the website. Keycloak has this functionality implemented as a required action, but it requires some configuration. For one, you have to go to the `Required Actions` tab described earlier and enable the `Terms and Conditions` action. You must also edit the *terms.ftl* file in the *base* login theme. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information on extending and creating themes.

### 4.7. Impersonation {#Impersonation}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/impersonation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/impersonation.adoc)

It is often useful for an admin to impersonate a user. For example, a user may be experiencing a bug in one of your applications and an admin may want to impersonate the user to see if they can duplicate the problem. Admins with the appropriate permission can impersonate a user. There are two locations an admin can initiate impersonation. The first is on the `Users` list tab.

Users

![user search](assets/user-search.png)

You can see here that the admin has searched for `john`. Next to John’s account you can see an impersonate button. Click that to impersonate the user.

Also, you can impersonate the user from the user `Details` tab.

User Details

![user details](assets/user-details.png)

Near the bottom of the page you can see the `Impersonate` button. Click that to impersonate the user.

When impersonating, if the admin and the user are in the same realm, then the admin will be logged out and automatically logged in as the user being impersonated. If the admin and user are not in the same realm, the admin will remain logged in, but additionally be logged in as the user in that user’s realm. In both cases, the browser will be redirected to the impersonated user’s User Account Management page.

Any user with the realm’s `impersonation` role can impersonate a user. Please see the [Admin Console Access Control](https://www.keycloak.org/docs/latest/server_admin/index.html#_admin_permissions)chapter for more details on assigning administration permissions.

### 4.8. User Registration {#User_Registration}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/user-registration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/user-registration.adoc)

You can enable Keycloak to allow user self registration. When enabled, the login page has a registration link the user can click on to create their new account.

When user self registration is enabled it is possible to use the registration form to detect valid usernames and emails. It is also possible to enable [reCAPTCHA Support](https://www.keycloak.org/docs/latest/server_admin/index.html#_recaptcha).

Enabling registration is pretty simple. Go to the `Realm Settings` left menu and click it. Then go to the `Login` tab. There is a `User Registration` switch on this tab. Turn it on, then click the `Save` button.

Login Tab

![login tab](assets/login-tab.png)

After you enable this setting, a `Register` link should show up on the login page.

Registration Link

![registration link](assets/registration-link.png)

Clicking on this link will bring the user to the registration page where they have to enter in some user profile information and a new password.

Registration Form

![registration form](assets/registration-form.png)

You can change the look and feel of the registration form as well as removing or adding additional fields that must be entered. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information.

#### 4.8.1. reCAPTCHA Support {#reCAPTCHA_Support}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/users/recaptcha.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/users/recaptcha.adoc)

To safeguard registration against bots, Keycloak has integration with Google reCAPTCHA. To enable this you need to first go to [Google Recaptcha Website](https://developers.google.com/recaptcha/) and create an API key so that you can get your reCAPTCHA site key and secret. (FYI, localhost works by default so you don’t have to specify a domain).

Next, there are a few steps you need to perform in the Keycloak Admin Console. Click the `Authentication` left menu item and go to the `Flows` tab. Select the `Registration` flow from the drop down list on this page.

Registration Flow

![registration flow](assets/registration-flow.png)

Set the 'reCAPTCHA' requirement to `Required` by clicking the appropriate radio button. This will enable reCAPTCHA on the screen. Next, you have to enter in the reCAPTCHA site key and secret that you generated at the Google reCAPTCHA Website. Click on the 'Actions' button that is to the right of the reCAPTCHA flow entry, then "Config" link, and enter in the reCAPTCHA site key and secret on this config page.

Recaptcha Config Page

![recaptcha config](assets/recaptcha-config.png)

The final step you have to do is to change some default HTTP response headers that Keycloak sets. Keycloak will prevent a website from including any login page within an iframe. This is to prevent clickjacking attacks. You need to authorize Google to use the registration page within an iframe. Go to the `Realm Settings` left menu item and then go to the `Security Defenses` tab. You will need to add `https://www.google.com` to the values of both the `X-Frame-Options` and `Content-Security-Policy` headers.

Authorizing Iframes

![security headers](assets/security-headers.png)

Once you do this, reCAPTCHA should show up on your registration page. You may want to edit *register.ftl* in your login theme to muck around with the placement and styling of the reCAPTCHA button. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information on extending and creating themes.

## 5. Login Page Settings {#Login_Page_Settings}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/login-settings.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/login-settings.adoc)

There are several nice built-in login page features you can enable if you need the functionality.

### 5.1. Forgot Password {#Forgot_Password}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/login-settings/forgot-password.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/login-settings/forgot-password.adoc)

If you enable it, users are able to reset their credentials if they forget their password or lose their OTP generator. Go to the `Realm Settings` left menu item, and click on the `Login` tab. Switch on the `Forgot Password` switch.

Login Tab

![login tab](assets/login-tab.png)

A `forgot password` link will now show up on your login pages.

Forgot Password Link

![forgot password link](assets/forgot-password-link.png)

Clicking on this link will bring the user to a page where they can enter in their username or email and receive an email with a link to reset their credentials.

Forgot Password Page

![forgot password page](assets/forgot-password-page.png)

The text sent in the email is completely configurable. You just need to extend or edit the theme associated with it. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information.

When the user clicks on the email link, they will be asked to update their password, and, if they have an OTP generator set up, they will also be asked to reconfigure this as well. Depending on the security requirements of your organization you may not want users to be able to reset their OTP generator through email. You can change this behavior by going to the `Authentication` left menu item, clicking on the `Flows` tab, and selecting the `Reset Credentials` flow:

Reset Credentials Flow

![reset credentials flow](assets/reset-credentials-flow.png)

If you do not want OTP reset, then just chose the `disabled` radio button to the right of `Reset OTP`.

### 5.2. Remember Me {#Remember_Me}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/login-settings/remember-me.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/login-settings/remember-me.adoc)

If a logged in user closes their browser, their session is destroyed and they will have to log in again. You can set things up so that if a user checks a *remember me* checkbox, they will remain logged in even if the browser is closed. This basically turns the login cookie from a session-only cookie to a persistence cookie.

To enable this feature go to `Realm Settings` left menu item and click on the `Login` tab and turn on the `Remember Me`switch:

Login Tab

![login tab](assets/login-tab.png)

Once you save this setting, a `remember me` checkbox will be displayed on the realm’s login page.

Remember Me

![remember me](assets/remember-me.png)

## 6. Authentication {#Authentication}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication.adoc)

There are a few features you should be aware of when configuring authentication for your realm. Many organizations have strict password and OTP policies that you can enforce via settings in the Admin Console. You may or may not want to require different credential types for authentication. You may want to give users the option to login via Kerberos or disable or enable various built-in credential types. This chapter covers all of these topics.

### 6.1. Password Policies {#Password_Policies}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication/password-policies.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication/password-policies.adoc)

Each new realm created has no password policies associated with it. Users can have as short, as long, as complex, as insecure a password, as they want. Simple settings are fine for development or learning Keycloak, but unacceptable in production environments. Keycloak has a rich set of password policies you can enable through the Admin Console.

Click on the `Authentication` left menu item and go to the `Password Policy` tab. Choose the policy you want to add in the right side drop down list box. This will add the policy in the table on the screen. Choose the parameters for the policy. Hit the `Save` button to store your changes.

Password Policy

![password policy](assets/password-policy.png)

After saving your policy, user registration and the Update Password required action will enforce your new policy. An example of a user failing the policy check:

Failed Password Policy

![failed password policy](assets/failed-password-policy.png)

If the password policy is updated, an Update Password action must be set for every user. An automatic trigger is scheduled as a future enhancement.

#### 6.1.1. Password Policy Types {#Password_Policy_Types}
Here’s an explanation of each policy type:

- HashAlgorithm

  Passwords are not stored as clear text. Instead they are hashed using standard hashing algorithms before they are stored or validated. The only built-in and default algorithm available is PBKDF2. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) on how to plug in your own algorithm. Note that if you do change the algorithm, password hashes will not change in storage until the next time the user logs in.

- Hashing Iterations

  This value specifies the number of times a password will be hashed before it is stored or verified. The default value is 20,000. This hashing is done in the rare case that a hacker gets access to your password database. Once they have access to the database, they can reverse engineer user passwords. The industry recommended value for this parameter changes every year as CPU power improves. A higher hashing iteration value takes more CPU power for hashing, and can impact performance. You’ll have to weigh what is more important to you: performance or protecting your passwords stores. There may be more cost effective ways of protecting your password stores.

- Digits

  The number of digits required to be in the password string.

- Lowercase Characters

  The number of lower case letters required to be in the password string.

- Uppercase Characters

  The number of upper case letters required to be in the password string.

- Special Characters

  The number of special characters like '?!#%$' required to be in the password string.

- Not Username

  When set, the password is not allowed to be the same as the username.

- Regular Expression

  Define one or more Perl regular expression patterns that passwords must match.

- Expire Password

  The number of days for which the password is valid. After the number of days has expired, the user is required to change their password.

- Not Recently Used

  This policy saves a history of previous passwords. The number of old passwords stored is configurable. When a user changes their password they cannot use any stored passwords.

- Password Blacklist

  This policy checks if a given password is contained in a blacklist file, which is potentially a very large file. Password blacklists are UTF-8 plain-text files with Unix line endings where every line represents a blacklisted password. The file name of the blacklist file must be provided as the password policy value, e.g. `10_million_password_list_top_1000000.txt`. Blacklist files are resolved against `${jboss.server.data.dir}/password-blacklists/` by default. This path can be customized via the `keycloak.password.blacklists.path` system property, or the `blacklistsPath` property of the `passwordBlacklist` policy SPI configuration.

### 6.2. OTP Policies {#OTP_Policies}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication/otp-policies.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication/otp-policies.adoc)

Keycloak has a number of policies you can set up for your FreeOTP or Google Authenticator One-Time Password generator. Click on the `Authentication` left menu item and go to the `OTP Policy` tab.

OTP Policy

![otp policy](assets/otp-policy.png)

Any policies you set here will be used to validate one-time passwords. When configuring OTP, FreeOTP and Google Authenticator can scan a QR code that is generated on the OTP set up page that Keycloak has. The bar code is also generated from information configured on the `OTP Policy` tab.

#### 6.2.1. TOTP vs. HOTP {#TOTP_vs__HOTP}
There are two different algorithms to choose from for your OTP generators. Time Based (TOTP) and Counter Based (HOTP). For TOTP, your token generator will hash the current time and a shared secret. The server validates the OTP by comparing all the hashes within a certain window of time to the submitted value. So, TOTPs are valid only for a short window of time (usually 30 seconds). For HOTP a shared counter is used instead of the current time. The server increments the counter with each successful OTP login. So, valid OTPs only change after a successful login.

TOTP is considered a little more secure because the matchable OTP is only valid for a short window of time while the OTP for HOTP can be valid for an indeterminate amount of time. HOTP is much more user friendly as the user won’t have to hurry to enter in their OTP before the time interval is up. With the way Keycloak has implemented TOTP this distinction becomes a little more blurry. HOTP requires a database update every time the server wants to increment the counter. This can be a performance drain on the authentication server when there is heavy load. So, to provide a more efficient alternative, TOTP does not remember passwords used. This bypasses the need to do any DB updates, but the downside is that TOTPs can be re-used in the valid time interval. For future versions of Keycloak it is planned that you will be able to configure whether TOTP checks older OTPs in the time interval.

#### 6.2.2. TOTP Configuration Options {#TOTP_Configuration_Options}
- OTP Hash Algorithm

  Default is SHA1, more secure options are SHA256 and SHA512.

- Number of Digits

  How many characters is the OTP? Short means more user friendly as it is less the user has to type. More means more security.

- Look Ahead Window

  How many intervals ahead should the server try and match the hash? This exists so just in case the clock of the TOTP generator or authentication server get out of sync. The default value of 1 is usually good enough. For example, if the time interval for a new token is every 30 seconds, the default value of 1 means that it will only accept valid tokens in that 30 second window. Each increment of this config value will increase the valid window by 30 seconds.

- OTP Token Period

  Time interval in seconds during which the server will match a hash. Each time the interval passes, a new TOTP will be generated by the token generator.

#### 6.2.3. HOTP Configuration Options {#HOTP_Configuration_Options}
- OTP Hash Algorithm

  Default is SHA1, more secure options are SHA256 and SHA512.

- Number of Digits

  How many characters is the OTP? Short means more user friendly as it is less the user has to type. More means more security.

- Look Ahead Window

  How many counters ahead should the server try and match the hash? The default value is 1. This exists to cover the case where the user’s counter gets ahead of the server’s. This can often happen as users often increment the counter manually too many times by accident. This value really should be increased to a value of 10 or so.

- Initial Counter

  What is the value of the initial counter?

### 6.3. Authentication Flows {#Authentication_Flows}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication/flows.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication/flows.adoc)

An *authentication flow* is a container for all authentications, screens, and actions that must happen during login, registration, and other Keycloak workflows. If you go to the admin console `Authentication` left menu item and go to the `Flows` tab, you can view all the defined flows in the system and what actions and checks each flow requires. This section does a walk-through of the browser login flow. In the left drop-down list select `browser` to come to the screen shown below:

Browser Flow

![browser flow](assets/browser-flow.png)

If you hover over the tooltip (the tiny question mark) to the right of the flow selection list, this will describe what the flow is and does.

The `Auth Type` column is the name of authentication or action that will be executed. If an authentication is indented this means it is in a sub-flow and may or may not be executed depending on the behavior of its parent. The `Requirement`column is a set of radio buttons which define whether or not the action will execute. Let’s describe what each radio button means:

- Required

  This authentication execution must execute successfully. If the user doesn’t have that type of authentication mechanism configured and there is a required action associated with that authentication type, then a required action will be attached to that account. For example, if you switch `OTP Form` to `Required`, users that don’t have an OTP generator configured will be asked to do so.

- Optional

  If the user has the authentication type configured, it will be executed. Otherwise, it will be ignored.

- Disabled

  If disabled, the authentication type is not executed.

- Alternative

  This means that at least one alternative authentication type must execute successfully at that level of the flow.

This is better described in an example. Let’s walk through the `browser` authentication flow.

1. The first authentication type is `Cookie`. When a user successfully logs in for the first time, a session cookie is set. If this cookie has already been set, then this authentication type is successful. Since the cookie provider returned success and each execution at this level of the flow is *alternative*, no other execution is executed and this results in a successful login.
2. Next the flow looks at the Kerberos execution. This authenticator is disabled by default and will be skipped.
3. The next execution is a subflow called Forms. Since this subflow is marked as *alternative* it will not be executed if the `Cookie` authentication type passed. This subflow contains additional authentication type that needs to be executed. The executions for this subflow are loaded and the same processing logic occurs
4. The first execution in the Forms subflow is the Username Password Form. This authentication type renders the username and password page. It is marked as *required* so the user must enter in a valid username and password.
5. The next execution is the OTP Form. This is marked as *optional*. If the user has OTP set up, then this authentication type must run and be successful. If the user doesn’t have OTP set up, this authentication type is ignored.

### 6.4. Executions {#Executions}
Executions can be used

Script Authenticator

A *script* authenticator allows to define custom authentication logic via JavaScript. Custom authenticators. In order to make use of this feature, it must be explicitly enabled:

```
bin/standalone.sh|bat -Dkeycloak.profile.feature.scripts=enabled
```

For more information, see the [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles) section.

Authentication scripts must at least provide one of the following functions: `authenticate(..)` which is called from `Authenticator#authenticate(AuthenticationFlowContext)` `action(..)` which is called from `Authenticator#action(AuthenticationFlowContext)`

Custom `Authenticator` should at least provide the `authenticate(..)` function. The following script `javax.script.Bindings` are available for convenient use within script code.

- `script`

  the `ScriptModel` to access script metadata

- `realm`

  the `RealmModel`

- `user`

  the current `UserModel`

- `session`

  the active `KeycloakSession`

- `authenticationSession`

  the current `AuthenticationSessionModel`

- `httpRequest`

  the current `org.jboss.resteasy.spi.HttpRequest`

- `LOG`

  a `org.jboss.logging.Logger` scoped to `ScriptBasedAuthenticator`

Note that additional context information can be extracted from the `context` argument passed to the `authenticate(context)` `action(context)` function.

```
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

### 6.5. Kerberos {#Kerberos}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication/kerberos.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication/kerberos.adoc)

Keycloak supports login with a Kerberos ticket through the SPNEGO protocol. SPNEGO (Simple and Protected GSSAPI Negotiation Mechanism) is used to authenticate transparently through the web browser after the user has been authenticated when logging-in his session. For non-web cases or when ticket is not available during login, Keycloak also supports login with Kerberos username/password.

A typical use case for web authentication is the following:

1. User logs into his desktop (Such as a Windows machine in Active Directory domain or Linux machine with Kerberos integration enabled).
2. User then uses his browser (IE/Firefox/Chrome) to access a web application secured by Keycloak.
3. Application redirects to Keycloak login.
4. Keycloak renders HTML login screen together with status 401 and HTTP header `WWW-Authenticate: Negotiate`
5. In case that the browser has Kerberos ticket from desktop login, it transfers the desktop sign on information to the Keycloak in header `Authorization: Negotiate 'spnego-token'` . Otherwise it just displays the login screen.
6. Keycloak validates token from the browser and authenticates the user. It provisions user data from LDAP (in case of LDAPFederationProvider with Kerberos authentication support) or let user to update his profile and prefill data (in case of KerberosFederationProvider).
7. Keycloak returns back to the application. Communication between Keycloak and application happens through OpenID Connect or SAML messages. The fact that Keycloak was authenticated through Kerberos is hidden from the application. So Keycloak acts as broker to Kerberos/SPNEGO login.

For setup there are 3 main parts:

1. Setup and configuration of Kerberos server (KDC)
2. Setup and configuration of Keycloak server
3. Setup and configuration of client machines

#### 6.5.1. Setup of Kerberos server {#Setup_of_Kerberos_server}
This is platform dependent. Exact steps depend on your OS and the Kerberos vendor you’re going to use. Consult Windows Active Directory, MIT Kerberos and your OS documentation for how exactly to setup and configure Kerberos server.

At least you will need to:

- Add some user principals to your Kerberos database. You can also integrate your Kerberos with LDAP, which means that user accounts will be provisioned from LDAP server.

- Add service principal for "HTTP" service. For example if your Keycloak server will be running on `www.mydomain.org` you may need to add principal `HTTP/www.mydomain.org@MYDOMAIN.ORG` assuming that MYDOMAIN.ORG will be your Kerberos realm.

  For example on MIT Kerberos you can run a "kadmin" session. If you are on the same machine where is MIT Kerberos, you can simply use the command:

```
sudo kadmin.local
```

Then add HTTP principal and export his key to a keytab file with the commands like:

```
addprinc -randkey HTTP/www.mydomain.org@MYDOMAIN.ORG
ktadd -k /tmp/http.keytab HTTP/www.mydomain.org@MYDOMAIN.ORG
```

The Keytab file `/tmp/http.keytab` will need to be accessible on the host where Keycloak server will be running.

#### 6.5.2. Setup and configuration of Keycloak server {#Setup_and_configuration_of_Keycloak_server}
You need to install a kerberos client on your machine. This is also platform dependent. If you are on Fedora, Ubuntu or RHEL, you can install the package `freeipa-client`, which contains a Kerberos client and several other utilities. Configure the kerberos client (on Linux it’s in file `/etc/krb5.conf` ). You need to put your Kerberos realm and at least configure the HTTP domains your server will be running on. For the example realm MYDOMAIN.ORG you may configure the `domain_realm`section like this:

```
[domain_realm]
  .mydomain.org = MYDOMAIN.ORG
  mydomain.org = MYDOMAIN.ORG
```

Next you need to export the keytab file with the HTTP principal and make sure the file is accessible to the process under which Keycloak server is running. For production, it’s ideal if it’s readable just by this process and not by someone else. For the MIT Kerberos example above, we already exported keytab to `/tmp/http.keytab` . If your KDC and Keycloak are running on same host, you have that file already available.

##### Enable SPNEGO Processing {#Enable_SPNEGO_Processing}
Keycloak does not have the SPNEGO protocol support turned on by default. So, you have to go to the [browser flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_authentication-flows) and enable `Kerberos`.

Browser Flow

![browser flow](assets/browser-flow.png)

Switch the `Kerberos` requirement from *disabled* to either *alternative* or *required*. *Alternative* basically means that Kerberos is optional. If the user’s browser hasn’t been configured to work with SPNEGO/Kerberos, then Keycloak will fall back to the regular login screens. If you set the requirement to *required* then all users must have Kerberos enabled for their browser.

##### Configure Kerberos User Storage Federation Provider {#Configure_Kerberos_User_Storage_Federation_Provider}
Now that the SPNEGO protocol is turned on at the authentication server, you’ll need to configure how Keycloak interprets the Kerberos ticket. This is done through [User Storage Federation](https://www.keycloak.org/docs/latest/server_admin/index.html#_user-storage-federation). We have 2 different federation providers with Kerberos authentication support.

If you want to authenticate with Kerberos backed by an LDAP server, you have to first configure the [LDAP Federation Provider](https://www.keycloak.org/docs/latest/server_admin/index.html#_ldap). If you look at the configuration page for your LDAP provider you’ll see a `Kerberos Integration` section.

LDAP Kerberos Integration

![ldap kerberos](assets/ldap-kerberos.png)

Turning on the switch `Allow Kerberos authentication` will make Keycloak use the Kerberos principal to lookup information about the user so that it can be imported into the Keycloak environment.

If your Kerberos solution is not backed by an LDAP server, you have to use the `Kerberos` User Storage Federation Provider. Go to the `User Federation` left menu item and select `Kerberos` from the `Add provider` select box.

Kerberos User Storage Provider

![kerberos provider](assets/kerberos-provider.png)

This provider parses the Kerberos ticket for simple principal information and does a small import into the local Keycloak database. User profile information like first name, last name, and email are not provisioned.

#### 6.5.3. Setup and configuration of client machines {#Setup_and_configuration_of_client_machines}
Clients need to install kerberos client and setup krb5.conf as described above. Additionally they need to enable SPNEGO login support in their browser. See [configuring Firefox for Kerberos](http://www.microhowto.info/howto/configure_firefox_to_authenticate_using_spnego_and_kerberos.html) if you are using that browser. URI `.mydomain.org` must be allowed in the `network.negotiate-auth.trusted-uris` config option.

In a Windows domain, clients usually don’t need to configure anything special as IE is already able to participate in SPNEGO authentication for the Windows domain.

#### 6.5.4. Example setups {#Example_setups}
For easier testing with Kerberos, we provided some example setups to test.

##### Keycloak and FreeIPA docker image {#Keycloak_and_FreeIPA_docker_image}
Once you install [docker](https://www.docker.com/), you can run docker image with FreeIPA server installed. FreeIPA provides integrated security solution with MIT Kerberos and 389 LDAP server among other things . The image provides also Keycloak server configured with LDAP Federation provider and enabled SPNEGO/Kerberos authentication against the FreeIPA server. See details [here](https://github.com/mposolda/keycloak-freeipa-docker/blob/master/README.md) .

##### ApacheDS testing Kerberos server {#ApacheDS_testing_Kerberos_server}
For quick testing and unit tests, we use a very simple [ApacheDS](http://directory.apache.org/apacheds/) Kerberos server. You need to build Keycloak from sources and then run the Kerberos server with maven-exec-plugin from our testsuite. See details [here](https://github.com/keycloak/keycloak/blob/master/docs/tests.md#kerberos-server) .

#### 6.5.5. Credential Delegation {#Credential_Delegation}
Kerberos 5 supports the concept of credential delegation. In this scenario, your applications may want access to the Kerberos ticket so that they can re-use it to interact with other services secured by Kerberos. Since the SPNEGO protocol is processed in the Keycloak server, you have to propagate the GSS credential to your application within the OpenID Connect token claim or a SAML assertion attribute that is transmitted to your application from the Keycloak server. To have this claim inserted into the token or assertion, each application will need to enable the built-in protocol mapper called `gss delegation credential`. This is enabled in the `Mappers` tab of the application’s client page. See [Protocol Mappers](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) chapter for more details.

Applications will need to deserialize the claim it receives from Keycloak before it can use it to make GSS calls against other services. Once you deserialize the credential from the access token to the GSSCredential object, the GSSContext will need to be created with this credential passed to the method `GSSManager.createContext` for example like this:

```
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

We have an example, that shows this in detail. It’s in `examples/kerberos` in the Keycloak example distribution or demo distribution download. You can also check the example sources directly [here](https://github.com/keycloak/keycloak/tree/master/examples/kerberos) .

Note that you also need to configure `forwardable` kerberos tickets in `krb5.conf` file and add support for delegated credentials to your browser.

|      | Credential delegation has some security implications so only use it if you really need it. It’s highly recommended to use it together with HTTPS. See for example [this article](http://www.microhowto.info/howto/configure_firefox_to_authenticate_using_spnego_and_kerberos.html) for more details. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 6.5.6. Cross-realm trust {#Cross_realm_trust}
In the Kerberos V5 protocol, the `realm` is a set of Kerberos principals defined in the Kerberos database (typically LDAP server). The Kerberos protocol has a concept of cross-realm trust. For example, if there are 2 kerberos realms A and B, the cross-realm trust will allow the users from realm A to access resources (services) of realm B. This means that realm B trusts the realm A.

Kerberos cross-realm trust

![kerberos trust basic](https://www.keycloak.org/docs/latest/server_admin/images/kerberos-trust-basic.png)

The Keycloak server has support for cross-realm trust. There are few things which need to be done to achieve this:

- Configure the Kerberos servers for the cross-realm trust. This step is dependent on the concrete Kerberos server implementations used. In general, it is needed to add the Kerberos principal `krbtgt/B@A` to both Kerberos databases of realm A and B. It is needed that this principal has same keys on both Kerberos realms. This is usually achieved when the principals have same password, key version number and there are same ciphers used in both realms. It is recommended to consult the Kerberos server documentation for more details.

|      | The cross-realm trust is unidirectional by default. If you want bidirectional trust to have realm A also trust realm B, you must also add the principal `krbtgt/A@B` to both Kerberos databases. However, trust is transitive by default. If realm B trusts realm A and realm C trusts realm B, then realm C automatically trusts realm A without a need to have principal `krbtgt/C@A` available. Some additional configuration (for example `capaths`) may be needed to configure on Kerberos client side, so that the clients are able to find the trust path. Consult the Kerberos documentation for more details. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

- Configure Keycloak server
  - If you use an LDAP storage provider with Kerberos support, you need to configure the server principal for realm B as in this example: `HTTP/mydomain.com@B`. The LDAP server must be able to find the users from realm A if you want users from realm A to successfully authenticate to Keycloak, as Keycloak server must be able to do SPNEGO flow and then find the users. For example, kerberos principal user `john@A` must be available as a user in the LDAP under an LDAP DN such as `uid=john,ou=People,dc=example,dc=com`. If you want both users from realm A and B to authenticate, you need to ensure that LDAP is able to find users from both realms A and B. We want to improve this limitation in future versions, so you can potentially create more separate LDAP providers for separate realms and ensure that SPNEGO works for both of them.
  - If you use a Kerberos user storage provider (typically the Kerberos without LDAP integration), you need to configure the server principal as `HTTP/mydomain.com@B` and users from both Kerberos realms A and B should be able to authenticate.

|      | For the Kerberos user storage provider, it is recommended that there are no conflicting users among kerberos realms. If conflicting users exist, they will be mapped to the same Keycloak user. This is also something, which we want to improve in future versions and provide some more flexible mappings from Kerberos principals to Keycloak usernames. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 6.5.7. Troubleshooting {#Troubleshooting}
If you have issues, we recommend that you enable additional logging to debug the problem:

- Enable `Debug` flag in admin console for Kerberos or LDAP federation providers
- Enable TRACE logging for category `org.keycloak` in logging section of `standalone/configuration/standalone.xml`to receive more info `standalone/log/server.log`
- Add system properties `-Dsun.security.krb5.debug=true` and `-Dsun.security.spnego.debug=true`

### 6.6. X.509 Client Certificate User Authentication {#X_509_Client_Certificate_User_Authentication}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/authentication/x509.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/authentication/x509.adoc)

Keycloak supports login with a X.509 client certificate if the server is configured for mutual SSL authentication.

A typical workflow is as follows:

- A client sends an authentication request over SSL/TLS channel
- During SSL/TLS handshake, the server and the client exchange their x.509/v3 certificates
- The container (WildFly) validates the certificate PKIX path and the certificate expiration
- The x.509 client certificate authenticator validates the client certificate as follows:
  - Optionally checks the certificate revocation status using CRL and/or CRL Distribution Points
  - Optionally checks the Certificate revocation status using OCSP (Online Certificate Status Protocol)
  - Optionally validates whether the key usage in the certificate matches the expected key usage
  - Optionally validates whether the extended key usage in the certificate matches the expected extended key usage
- If any of the above checks fails, the x.509 authentication fails
- Otherwise, the authenticator extracts the certificate identity and maps it to an existing user
- Once the certificate is mapped to an existing user, the behavior diverges depending on the authentication flow:
  - In the Browser Flow, the server prompts the user to confirm identity or to ignore it and instead sign in with username/password
  - In the case of the Direct Grant Flow, the server signs in the user

#### 6.6.1. Features {#Features}
- Supported Certificate Identity Sources

  Match SubjectDN using regular expressionX500 Subject’s e-mail attributeX500 Subject’s e-mail from Subject Alternative Name Extension (RFC822Name General Name)X500 Subject’s other name from Subject Alternative Name Extension. This is typically UPN (User Principal Name)X500 Subject’s Common Name attributeMatch IssuerDN using regular expressionX500 Issuer’s e-mail attributeX500 Issuer’s Common Name attributeCertificate Serial Number

- Regular Expressions

  The certificate identity can be extracted from either Subject DN or Issuer DN using a regular expression as a filter. For example, the regular expression below will match the e-mail attribute:

```
emailAddress=(.*?)(?:,|$)
```

The regular expression filtering is applicable only if the `Identity Source` is set to either `Match SubjectDN using regular expression` or `Match IssuerDN using regular expression`.

- Mapping certificate identity to an existing user

  The certificate identity mapping can be configured to map the extracted user identity to an existing user’s username or e-mail or to a custom attribute which value matches the certificate identity. For example, setting the `Identity source` to *Subject’s e-mail* and `User mapping method` to *Username or email* will have the X.509 client certificate authenticator use the e-mail attribute in the certificate’s Subject DN as a search criteria to look up an existing user by username or by e-mail.

|      | Please notice that if we disable `Login with email` at realm settings, the same rules will be applied to certificate authentication. In other words, users won’t be able to log in using e-mail attribute. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

- Other Features: Extended Certificate Validation

  Revocation status checking using CRLRevocation status checking using CRL/Distribution PointRevocation status checking using OCSP/Responder URICertificate KeyUsage validationCertificate ExtendedKeyUsage validation

#### 6.6.2. Enable X.509 Client Certificate User Authentication {#Enable_X_509_Client_Certificate_User_Authentication}
The following sections describe how to configure WildFly/Undertow and the Keycloak Server to enable X.509 client certificate authentication.

- Enable mutual SSL in WildFly

  See [Enable SSL](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide-EnableSSL) and [SSL](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide-{{}}) for the instructions how to enable SSL in WildFly.Open KEYCLOAK_HOME/standalone/configuration/standalone.xml and add a new realm:

```
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

  The `ssl` element contains the `keystore` element that defines how to load the server public key pair from a JKS keystore

- `ssl/keystore/path`

  A path to a JKS keystore

- `ssl/keystore/relative-to`

  Defines a path the keystore path is relative to

- `ssl/keystore/keystore-password`

  The password to open the keystore

- `ssl/keystore/alias` (optional)

  The alias of the entry in the keystore. Set it if the keystore contains multiple entries

- `ssl/keystore/key-password` (optional)

  The private key password, if different from the keystore password.

- `authentication/truststore`

  Defines how to load a trust store to verify the certificate presented by the remote side of the inbound/outgoing connection. Typically, the truststore contains a collection of trusted CA certificates.

- `authentication/truststore/path`

  A path to a JKS keystore that contains the certificates of the trusted CAs (certificate authorities)

- `authentication/truststore/relative-to`

  Defines a path the truststore path is relative to

- `authentication/truststore/keystore-password`

  The password to open the truststore

- Enable https listener

  See [HTTPS Listener](https://docs.jboss.org/author/display/WFLY10/Admin+Guide#AdminGuide-HTTPSlistener) for the instructions how to enable HTTPS in WildFly.Add the <https-listener> element as shown below:

```
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

  The value must match the name of the realm from the previous section

- `https-listener/verify-client`

  If set to `REQUESTED`, the server will optionally ask for a client certificate. Setting the attribute to `REQUIRED` will have the server to refuse inbound connections if no client certificate has been provided.

#### 6.6.3. Adding X.509 Client Certificate Authentication to a Browser Flow {#Adding_X_509_Client_Certificate_Authentication_to_a_Browser_Flow}
- Select a realm, click on Authentication link, select the "Browser" flow
- Make a copy of the built-in "Browser" flow. You may want to give the new flow a distinctive name, i.e. "X.509 Browser"
- Using the drop down, select the copied flow, and click on "Add execution"
- Select "X509/Validate User Form" using the drop down and click on "Save"

![x509 execution](https://www.keycloak.org/docs/latest/server_admin/images/x509-execution.png)

- Using the up/down arrows, change the order of the "X509/Validate Username Form" by moving it above the "Browser Forms" execution, and set the requirement to "ALTERNATIVE"

![x509 browser flow](https://www.keycloak.org/docs/latest/server_admin/images/x509-browser-flow.png)

- Select the "Bindings" tab, find the drop down for "Browser Flow". Select the newly created X509 browser flow from the drop down and click on "Save".

![x509 browser flow bindings](https://www.keycloak.org/docs/latest/server_admin/images/x509-browser-flow-bindings.png)

- Configuring X.509 Client Certificate Authentication

  ![x509 configuration](https://www.keycloak.org/docs/latest/server_admin/images/x509-configuration.png)

- `User Identity Source`

  Defines how to extract the user identity from a client certificate.

- `A regular expression` (optional)

  Defines a regular expression to use as a filter to extract the certificate identity. The regular expression must contain a single group.

- `User Mapping Method`

  Defines how to match the certificate identity to an existing user. *Username or e-mail* will search for an existing user by username or e-mail. *Custom Attribute Mapper* will search for an existing user with a custom attribute which value matches the certificate identity. The name of the custom attribute is configurable.

- `A name of user attribute` (optional)

  A custom attribute which value will be matched against the certificate identity.

- `CRL Checking Enabled` (optional)

  Defines whether to check the revocation status of the certificate using Certificate Revocation List.

- `Enable CRL Distribution Point to check certificate revocation status` (optional)

  Defines whether to use CDP to check the certificate revocation status. Most PKI authorities include CDP in their certificates.

- `CRL file path` (optional)

  Defines a path to a file that contains a CRL list. The value must be a path to a valid file if `CRL Checking Enabled` option is turned on.

- `OCSP Checking Enabled`(optional)

  Defines whether to check the certificate revocation status using Online Certificate Status Protocol.

- `OCSP Responder URI` (optional)

  Allows to override a value of the OCSP responder URI in the certificate.

- `Validate Key Usage` (optional)

  Verifies whether the certificate’s KeyUsage extension bits are set. For example, "digitalSignature,KeyEncipherment" will verify if bits 0 and 2 in the KeyUsage extension are asserted. Leave the parameter empty to disable the Key Usage validation. See [RFC5280, Section-4.2.1.3](https://tools.ietf.org/html/rfc5280#section-4.2.1.3). The server will raise an error only when flagged as critical by the issuing CA and there is a key usage extension mismatch.

- `Validate Extended Key Usage` (optional)

  Verifies one or more purposes as defined in the Extended Key Usage extension. See [RFC5280, Section-4.2.1.12](https://tools.ietf.org/html/rfc5280#section-4.2.1.12). Leave the parameter empty to disable the Extended Key Usage validation. The server will raise an error only when flagged as critical by the issuing CA and there is a key usage extension mismatch.

- `Bypass identity confirmation`

  If set, X.509 client certificate authentication will not prompt the user to confirm the certificate identity and will automatically sign in the user upon successful authentication.

#### 6.6.4. Adding X.509 Client Certificate Authentication to a Direct Grant Flow {#Adding_X_509_Client_Certificate_Authentication_to_a_Direct_Grant_Flow}
- Using Keycloak admin console, click on "Authentication" and select the "Direct Grant" flow,
- Make a copy of the build-in "Direct Grant" flow. You may want to give the new flow a distinctive name, i.e. "X509 Direct Grant",
- Delete "Username Validation" and "Password" authenticators,
- Click on "Add execution" and add "X509/Validate Username" and click on "Save" to add the execution step to the parent flow.

![x509 directgrant execution](https://www.keycloak.org/docs/latest/server_admin/images/x509-directgrant-execution.png)

- Change the `Requirement` to *REQUIRED*.

![x509 directgrant flow](https://www.keycloak.org/docs/latest/server_admin/images/x509-directgrant-flow.png)

- Set up the x509 authentication configuration by following the steps described earlier in the x.509 Browser Flow section.
- Select the "Bindings" tab, find the drop down for "Direct Grant Flow". Select the newly created X509 direct grant flow from the drop down and click on "Save".

![x509 directgrant flow bindings](https://www.keycloak.org/docs/latest/server_admin/images/x509-directgrant-flow-bindings.png)

#### 6.6.5. Client certificate lookup {#Client_certificate_lookup}
When an HTTP request is sent directly to Keycloak server, the WildFly undertow subsystem will establish an SSL handshake and extract the client certificate. The client certificate will be then saved to the attribute `javax.servlet.request.X509Certificate` of the HTTP request, as specified in the servlet specification. The Keycloak X509 authenticator will be then able to lookup the certificate from this attribute.

However, when the Keycloak server listens to HTTP requests behind a load balancer or reverse proxy, it may be the proxy server which extracts the client certificate and establishes the mutual SSL connection. A reverse proxy usually puts the authenticated client certificate in the HTTP header of the underlying request and forwards it to the back end Keycloak server. In this case, Keycloak must be able to look up the X.509 certificate chain from the HTTP headers instead of from the attribute of HTTP request, as is done for Undertow.

If Keycloak is behind a reverse proxy, you usually need to configure alternative provider of the `x509cert-lookup` SPI in KEYCLOAK_HOME/standalone/configuration/standalone.xml. Along with the `default` provider, which looks up the certificate from the HTTP header, we also have two additional built-in providers: `haproxy` and `apache`, which are described next.

##### HAProxy certificate lookup provider {#HAProxy_certificate_lookup_provider}
You can use this provider when your Keycloak server is behind an HAProxy reverse proxy. Configure the server like this:

```
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

In this example configuration, the client certificate will be looked up from the HTTP header, `SSL_CLIENT_CERT`, and the other certificates from its chain will be looked up from HTTP headers like `CERT_CHAIN_0` , `CERT_CHAIN_1`, …, `CERT_CHAIN_9` . The attribute `certificateChainLength` is the maximum length of the chain, so the last one tried attribute would be `CERT_CHAIN_9` .

Consult the [HAProxy documentation](http://www.haproxy.org/#docs) for the details of how the HTTP Headers for the client certificate and client certificate chain can be configured and their proper names.

##### Apache certificate lookup provider {#Apache_certificate_lookup_provider}
You can use this provider when your Keycloak server is behind an Apache reverse proxy. Configure the server like this:

```
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

The configuration is same as for the `haproxy` provider. Consult the Apache documentation on [mod_ssl](https://httpd.apache.org/docs/current/mod/mod_ssl.html) and [mod_headers](https://httpd.apache.org/docs/current/mod/mod_headers.html)for the details of how the HTTP Headers for the client certificate and client certificate chain can be configured and their proper names.

##### Nginx certificate lookup provider {#Nginx_certificate_lookup_provider}
You can use this provider when your Keycloak server is behind an Nginx reverse proxy. Configure the server like this:

```
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

|      | NGINX [SSL/TLS module](http://nginx.org/en/docs/http/ngx_http_ssl_module.html#variables) does not expose the client certificate chain, so Keycloak NGINX certificate lookup provider is rebuilding it using the [Keycloak truststore](https://www.keycloak.org/docs/6.0/server_installation/#_truststore). Please populate Keycloak truststore using keytool CLI with all root and intermediate CA’s needed for rebuilding client certificate chain. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Consult the NGINX documentation for the details of how the HTTP Headers for the client certificate can be configured. Example of NGINX configuration file :

```
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

|      | all certificates in trusted-ca-list-for-client-auth.pem must be added to [Keycloak truststore](https://www.keycloak.org/docs/6.0/server_installation/#_truststore). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Other reverse proxy implementations {#Other_reverse_proxy_implementations}
We do not have built-in support for other reverse proxy implementations. However, it is possible that other reverse proxies can be made to behave in a similar way to `apache` or `haproxy` and that some of those providers can be used. If none of those works, you may need to create your own implementation of the `org.keycloak.services.x509.X509ClientCertificateLookupFactory` and `org.keycloak.services.x509.X509ClientCertificateLookup` provider. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for the details on how to add your own provider.

#### 6.6.6. Troubleshooting {#Troubleshooting}
- Dumping HTTP headers

  If you want to view what the reverse proxy is sending to Keycloak, simply activate [RequestDumpingHandler](https://mirocupak.com/logging-requests-with-undertow/) and consult `server.log` file.

- Enable TRACE logging under the logging subsystem

```
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

- Direct Grant authentication with X.509

  The following template can be used to request a token using the Resource Owner Password Credentials Grant:

```
$ curl https://[host][:port]/auth/realms/master/protocol/openid-connect/token \
       --insecure \
       --data "grant_type=password&scope=openid profile&username=&password=&client_id=CLIENT_ID&client_secret=CLIENT_SECRET" \
       -E /path/to/client_cert.crt \
       --key /path/to/client_cert.key
```

- `[host][:port]`

  The host and the port number of a remote Keycloak server that has been configured to allow users authenticate with x.509 client certificates using the Direct Grant Flow.

- `CLIENT_ID`

  A client id.

- `CLIENT_SECRET`

  For confidential clients, a client secret; otherwise, leave it empty.

- `client_cert.crt`

  A public key certificate that will be used to verify the identity of the client in mutual SSL authentication. The certificate should be in PEM format.

- `client_cert.key`

  A private key in the public key pair. Also expected in PEM format.

## 7. SSO Protocols {#SSO_Protocols}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sso-protocols.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sso-protocols.adoc)

The chapter gives a brief overview of the authentication protocols and how the Keycloak authentication server and the applications it secures interact with these protocols.

### 7.1. OpenID Connect {#OpenID_Connect}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sso-protocols/oidc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sso-protocols/oidc.adoc)

[OpenID Connect](https://openid.net/connect/) (OIDC) is an authentication protocol that is an extension of [OAuth 2.0](https://tools.ietf.org/html/rfc6749). While OAuth 2.0 is only a framework for building authorization protocols and is mainly incomplete, OIDC is a full-fledged authentication and authorization protocol. OIDC also makes heavy use of the [Json Web Token](https://jwt.io/) (JWT) set of standards. These standards define an identity token JSON format and ways to digitally sign and encrypt that data in a compact and web-friendly way.

There are really two types of use cases when using OIDC. The first is an application that asks the Keycloak server to authenticate a user for them. After a successful login, the application will receive an *identity token* and an *access token*. The *identity token* contains information about the user such as username, email, and other profile information. The *access token* is digitally signed by the realm and contains access information (like user role mappings) that the application can use to determine what resources the user is allowed to access on the application.

The second type of use cases is that of a client that wants to gain access to remote services. In this case, the client asks Keycloak to obtain an *access token* it can use to invoke on other remote services on behalf of the user. Keycloak authenticates the user then asks the user for consent to grant access to the client requesting it. The client then receives the *access token*. This *access token* is digitally signed by the realm. The client can make REST invocations on remote services using this *access token*. The REST service extracts the *access token*, verifies the signature of the token, then decides based on access information within the token whether or not to process the request.

#### 7.1.1. OIDC Auth Flows {#OIDC_Auth_Flows}
OIDC has different ways for a client or application to authenticate a user and receive an *identity* and *access* token. Which path you use depends greatly on the type of application or client requesting access. All of these flows are described in the OIDC and OAuth 2.0 specifications so only a brief overview will be provided here.

##### Authorization Code Flow {#Authorization_Code_Flow}
This is a browser-based protocol and it is what we recommend you use to authenticate and authorize browser-based applications. It makes heavy use of browser redirects to obtain an *identity* and *access* token. Here’s a brief summary:

1. Browser visits application. The application notices the user is not logged in, so it redirects the browser to Keycloak to be authenticated. The application passes along a callback URL (a redirect URL) as a query parameter in this browser redirect that Keycloak will use when it finishes authentication.
2. Keycloak authenticates the user and creates a one-time, very short lived, temporary code. Keycloak redirects back to the application using the callback URL provided earlier and additionally adds the temporary code as a query parameter in the callback URL.
3. The application extracts the temporary code and makes a background out of band REST invocation to Keycloak to exchange the code for an *identity*, *access* and *refresh* token. Once this temporary code has been used once to obtain the tokens, it can never be used again. This prevents potential replay attacks.

It is important to note that *access* tokens are usually short lived and often expired after only minutes. The additional *refresh*token that was transmitted by the login protocol allows the application to obtain a new access token after it expires. This refresh protocol is important in the situation of a compromised system. If access tokens are short lived, the whole system is only vulnerable to a stolen token for the lifetime of the access token. Future refresh token requests will fail if an admin has revoked access. This makes things more secure and more scalable.

Another important aspect of this flow is the concept of a *public* vs. a *confidential* client. *Confidential* clients are required to provide a client secret when they exchange the temporary codes for tokens. *Public* clients are not required to provide this client secret. *Public* clients are perfectly fine so long as HTTPS is strictly enforced and you are very strict about what redirect URIs are registered for the client. HTML5/JavaScript clients always have to be *public* clients because there is no way to transmit the client secret to them in a secure manner. Again, this is ok so long as you use HTTPS and strictly enforce redirect URI registration. This guide goes more detail into this in the [Managing Clients](https://www.keycloak.org/docs/latest/server_admin/index.html#_clients) chapter.

Keycloak also supports the optional [Proof Key for Code Exchange](https://tools.ietf.org/html/rfc7636) specification.

##### Implicit Flow {#Implicit_Flow}
This is a browser-based protocol that is similar to Authorization Code Flow except there are fewer requests and no refresh tokens involved. We do not recommend this flow as there remains the possibility of *access* tokens being leaked in the browser history as tokens are transmitted via redirect URIs (see below). Also, since this flow doesn’t provide the client with a refresh token, access tokens would either have to be long-lived or users would have to re-authenticate when they expired. This flow is supported because it is in the OIDC and OAuth 2.0 specification. Here’s a brief summary of the protocol:

1. Browser visits application. The application notices the user is not logged in, so it redirects the browser to Keycloak to be authenticated. The application passes along a callback URL (a redirect URL) as a query parameter in this browser redirect that Keycloak will use when it finishes authentication.
2. Keycloak authenticates the user and creates an *identity* and *access* token. Keycloak redirects back to the application using the callback URL provided earlier and additionally adding the *identity* and *access* tokens as query parameters in the callback URL.
3. The application extracts the *identity* and *access* tokens from the callback URL.

##### Resource Owner Password Credentials Grant (Direct Access Grants) {#Resource_Owner_Password_Credentials_Grant__Direct_Access_Grants_}
This is referred to in the Admin Console as *Direct Access Grants*. This is used by REST clients that want to obtain a token on behalf of a user. It is one HTTP POST request that contains the credentials of the user as well as the id of the client and the client’s secret (if it is a confidential client). The user’s credentials are sent within form parameters. The HTTP response contains *identity*, *access*, and *refresh* tokens.

##### Client Credentials Grant {#Client_Credentials_Grant}
This is also used by REST clients, but instead of obtaining a token that works on behalf of an external user, a token is created based on the metadata and permissions of a service account that is associated with the client. More info together with example is in [Service Accounts](https://www.keycloak.org/docs/latest/server_admin/index.html#_service_accounts) chapter.

#### 7.1.2. Keycloak Server OIDC URI Endpoints {#Keycloak_Server_OIDC_URI_Endpoints}
Here’s a list of OIDC endpoints that the Keycloak publishes. These URLs are useful if you are using a non-Keycloak client adapter to talk OIDC with the auth server. These are all relative URLs and the root of the URL being the HTTP(S) protocol, hostname, and usually path prefixed with */auth*: i.e. https://localhost:8080/auth

- /realms/{realm-name}/protocol/openid-connect/token

  This is the URL endpoint for obtaining a temporary code in the Authorization Code Flow or for obtaining tokens via the Implicit Flow, Direct Grants, or Client Grants.

- /realms/{realm-name}/protocol/openid-connect/auth

  This is the URL endpoint for the Authorization Code Flow to turn a temporary code into a token.

- /realms/{realm-name}/protocol/openid-connect/logout

  This is the URL endpoint for performing logouts.

- /realms/{realm-name}/protocol/openid-connect/userinfo

  This is the URL endpoint for the User Info service described in the OIDC specification.

In all of these replace *{realm-name}* with the name of the realm.

### 7.2. SAML {#SAML}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sso-protocols/saml.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sso-protocols/saml.adoc)

[SAML 2.0](http://saml.xml.org/saml-specifications) is a similar specification to OIDC but a lot older and more mature. It has its roots in SOAP and the plethora of WS-* specifications so it tends to be a bit more verbose than OIDC. SAML 2.0 is primarily an authentication protocol that works by exchanging XML documents between the authentication server and the application. XML signatures and encryption is used to verify requests and responses.

There are really two types of use cases when using SAML. The first is an application that asks the Keycloak server to authenticate a user for them. After a successful login, the application will receive an XML document that contains something called a SAML assertion that specify various attributes about the user. This XML document is digitally signed by the realm and contains access information (like user role mappings) that the application can use to determine what resources the user is allowed to access on the application.

The second type of use cases is that of a client that wants to gain access to remote services. In this case, the client asks Keycloak to obtain an SAML assertion it can use to invoke on other remote services on behalf of the user.

#### 7.2.1. SAML Bindings {#SAML_Bindings}
SAML defines a few different ways to exchange XML documents when executing the authentication protocol. The *Redirect*and *Post* bindings cover browser based applications. The *ECP* binding covers REST invocations. There are other binding types but Keycloak only supports those three.

##### Redirect Binding {#Redirect_Binding}
The *Redirect* Binding uses a series of browser redirect URIs to exchange information. This is a rough overview of how it works.

1. The user visits the application and the application finds the user is not authenticated. It generates an XML authentication request document and encodes it as a query param in a URI that is used to redirect to the Keycloak server. Depending on your settings, the application may also digitally sign this XML document and also stuff this signature as a query param in the redirect URI to Keycloak. This signature is used to validate the client that sent this request.
2. The browser is redirected to Keycloak. The server extracts the XML auth request document and verifies the digital signature if required. The user then has to enter in their credentials to be authenticated.
3. After authentication, the server generates an XML authentication response document. This document contains a SAML assertion that holds metadata about the user like name, address, email, and any role mappings the user might have. This document is almost always digitally signed using XML signatures, and may also be encrypted.
4. The XML auth response document is then encoded as a query param in a redirect URI that brings the browser back to the application. The digital signature is also included as a query param.
5. The application receives the redirect URI and extracts the XML document and verifies the realm’s signature to make sure it is receiving a valid auth response. The information inside the SAML assertion is then used to make access decisions or display user data.

##### POST Binding {#POST_Binding}
The SAML *POST* binding works almost the exact same way as the *Redirect* binding, but instead of GET requests, XML documents are exchanged by POST requests. The *POST* Binding uses JavaScript to trick the browser into making a POST request to the Keycloak server or application when exchanging documents. Basically HTTP responses contain an HTML document that contains an HTML form with embedded JavaScript. When the page is loaded, the JavaScript automatically invokes the form. You really don’t need to know about this stuff, but it is a pretty clever trick.

*POST* binding is usually recommended because of security and size restrictions. When using *REDIRECT* the SAML response is part of the URL (it is a query parameter as it was explained before), so it can be captured in logs and it is considered less secure. Regarding size, if the assertion contains a lot or large attributes sending the document inside the HTTP payload is always better than in the more limited URL.

##### ECP {#ECP}
ECP stands for "Enhanced Client or Proxy", a SAML v.2.0 profile which allows for the exchange of SAML attributes outside the context of a web browser. This is used most often for REST or SOAP-based clients.

#### 7.2.2. Keycloak Server SAML URI Endpoints {#Keycloak_Server_SAML_URI_Endpoints}
Keycloak really only has one endpoint for all SAML requests.

```
http(s)://authserver.host/auth/realms/{realm-name}/protocol/saml
```

All bindings use this endpoint.

### 7.3. OpenID Connect vs. SAML {#OpenID_Connect_vs__SAML}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sso-protocols/saml-vs-oidc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sso-protocols/saml-vs-oidc.adoc)

Choosing between OpenID Connect and SAML is not just a matter of using a newer protocol (OIDC) instead of the older more mature protocol (SAML).

In most cases Keycloak recommends using OIDC.

SAML tends to be a bit more verbose than OIDC.

Beyond verbosity of exchanged data, if you compare the specifications you’ll find that OIDC was designed to work with the web while SAML was retrofitted to work on top of the web. For example, OIDC is also more suited for HTML5/JavaScript applications because it is easier to implement on the client side than SAML. As tokens are in the JSON format, they are easier to consume by JavaScript. You will also find several nice features that make implementing security in your web applications easier. For example, check out the [iframe trick](https://openid.net/specs/openid-connect-session-1_0.html#ChangeNotification) that the specification uses to easily determine if a user is still logged in or not.

SAML has its uses though. As you see the OIDC specifications evolve you see they implement more and more features that SAML has had for years. What we often see is that people pick SAML over OIDC because of the perception that it is more mature and also because they already have existing applications that are secured with it.

### 7.4. Docker Registry v2 Authentication {#Docker_Registry_v2_Authentication}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sso-protocols/docker.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sso-protocols/docker.adoc)

|      | Docker authentication is disabled by default. To enable see [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

[Docker Registry V2 Authentication](https://docs.docker.com/registry/spec/auth/) is an OIDC-Like protocol used to authenticate users against a Docker registry. Keycloak’s implementation of this protocol allows for a Keycloak authentication server to be used by a Docker client to authenticate against a registry. While this protocol uses fairly standard token and signature mechanisms, it has a few wrinkles that prevent it from being treated as a true OIDC implementation. The largest deviations include a very specific JSON format for requests and responses as well as the ability to understand how to map repository names and permissions to the OAuth scope mechanism.

#### 7.4.1. Docker Auth Flow {#Docker_Auth_Flow}
The [Docker API documentation](https://docs.docker.com/registry/spec/auth/token/) best describes and illustrates this process, however a brief summary will be given below from the perspective of the Keycloak authentication server.

|      | This flow assumes that a `docker login` command has already been performed |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

- The flow begins when the Docker client requests a resource from the Docker registry. If the resource is protected and no auth token is present in the request, the Docker registry server will respond to the client with a 401 + some information on required permissions and where to find the authorization server.
- The Docker client will construct an authentication request based on the 401 response from the Docker registry. The client will then use the locally cached credentials (from a previously run `docker login` command) as part of a [HTTP Basic Authentication](https://tools.ietf.org/html/rfc2617) request to the Keycloak authentication server.
- The Keycloak authentication server will attempt to authenticate the user and return a JSON body containing an OAuth-style Bearer token.
- The Docker client will get the bearer token from the JSON response and use it in the Authorization header to request the protected resource.
- When the Docker registry receives the new request for the protected resource with the token from the Keycloak server, the registry validates the token and grants access to the requested resource (if appropriate).

#### 7.4.2. Keycloak Docker Registry v2 Authentication Server URI Endpoints {#Keycloak_Docker_Registry_v2_Authentication_Server_URI_Endpoints}
Keycloak really only has one endpoint for all Docker auth v2 requests.

```
http(s)://authserver.host/auth/realms/{realm-name}/protocol/docker-v2
```

## 8. Managing Clients {#Managing_Clients}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients.adoc)

Clients are entities that can request authentication of a user. Clients come in two forms. The first type of client is an application that wants to participate in single-sign-on. These clients just want Keycloak to provide security for them. The other type of client is one that is requesting an access token so that it can invoke other services on behalf of the authenticated user. This section discusses various aspects around configuring clients and various ways to do it.

### 8.1. OIDC Clients {#OIDC_Clients}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/client-oidc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/client-oidc.adoc)

[OpenID Connect](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc) is the preferred protocol to secure applications. It was designed from the ground up to be web friendly and work best with HTML5/JavaScript applications.

To create an OIDC client go to the `Clients` left menu item. On this page you’ll see a `Create` button on the right.

Clients

![clients](assets/clients.png)

This will bring you to the `Add Client` page.

Add Client

![add client oidc](assets/add-client-oidc.png)

Enter in the `Client ID` of the client. This should be a simple alpha-numeric string that will be used in requests and in the Keycloak database to identify the client. Next select `openid-connect` in the `Client Protocol` drop down box. Finally enter in the base URL of your application in the `Root URL` field and click `Save`. This will create the client and bring you to the client `Settings` tab.

Client Settings

![client settings oidc](assets/client-settings-oidc.png)

Let’s walk through each configuration item on this page.

**Client ID**

This specifies an alpha-numeric string that will be used as the client identifier for OIDC requests.

**Name**

This is the display name for the client whenever it is displayed in a Keycloak UI screen. You can localize the value of this field by setting up a replacement string value i.e. ${myapp}. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information.

**Description**

This specifies the description of the client. This can also be localized.

**Enabled**

If this is turned off, the client will not be allowed to request authentication.

**Consent Required**

If this is on, then users will get a consent page which asks the user if they grant access to that application. It will also display the metadata that the client is interested in so that the user knows exactly what information the client is getting access to. If you’ve ever done a social login to Google, you’ll often see a similar page. Keycloak provides the same functionality.

**Access Type**

This defines the type of the OIDC client.

- *confidential*

  Confidential access type is for server-side clients that need to perform a browser login and require a client secret when they turn an access code into an access token, (see [Access Token Request](https://tools.ietf.org/html/rfc6749#section-4.1.3) in the OAuth 2.0 spec for more details). This type should be used for server-side applications.

- *public*

  Public access type is for client-side clients that need to perform a browser login. With a client-side application there is no way to keep a secret safe. Instead it is very important to restrict access by configuring correct redirect URIs for the client.

- *bearer-only*

  Bearer-only access type means that the application only allows bearer token requests. If this is turned on, this application cannot participate in browser logins.

**Root URL**

If Keycloak uses any configured relative URLs, this value is prepended to them.

**Valid Redirect URIs**

This is a required field. Enter in a URL pattern and click the + sign to add. Click the - sign next to URLs you want to remove. Remember that you still have to click the `Save` button! Wildcards (\*) are only allowed at the end of a URI, i.e. http://host.com/*

You should take extra precautions when registering valid redirect URI patterns. If you make them too general you are vulnerable to attacks. See [Threat Model Mitigation](https://www.keycloak.org/docs/latest/server_admin/index.html#_unspecific-redirect-uris) chapter for more information.

**Base URL**

If Keycloak needs to link to the client, this URL is used.

**Standard Flow Enabled**

If this is on, clients are allowed to use the OIDC [Authorization Code Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows).

**Implicit Flow Enabled**

If this is on, clients are allowed to use the OIDC [Implicit Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows).

**Direct Grants Enabled**

If this is on, clients are allowed to use the OIDC [Direct Grants](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows).

**Admin URL**

For Keycloak specific client adapters, this is the callback endpoint for the client. The Keycloak server will use this URI to make callbacks like pushing revocation policies, performing backchannel logout, and other administrative operations. For Keycloak servlet adapters, this can be the root URL of the servlet application. For more information see [Securing Applications and Services Guide](https://www.keycloak.org/docs/6.0/securing_apps/).

**Web Origins**

This option centers around [CORS](http://www.w3.org/TR/cors/) which stands for Cross-Origin Resource Sharing. If browser JavaScript tries to make an AJAX HTTP request to a server whose domain is different from the one the JavaScript code came from, then the request must use CORS. The server must handle CORS requests in a special way, otherwise the browser will not display or allow the request to be processed. This protocol exists to protect against XSS, CSRF and other JavaScript-based attacks.

Keycloak has support for validated CORS requests. The way it works is that the domains listed in the `Web Origins` setting for the client are embedded within the access token sent to the client application. The client application can then use this information to decide whether or not to allow a CORS request to be invoked on it. This is an extension to the OIDC protocol so only Keycloak client adapters support this feature. See [Securing Applications and Services Guide](https://www.keycloak.org/docs/6.0/securing_apps/) for more information.

To fill in the `Web Origins` data, enter in a base URL and click the + sign to add. Click the - sign next to URLs you want to remove. Remember that you still have to click the `Save` button!

#### 8.1.1. Advanced Settings {#Advanced_Settings}
**OAuth 2.0 Mutual TLS Client Certificate Bound Access Token**

Mutual TLS binds an access token and a refresh token with a client certificate exchanged during TLS handshake. This prevents an attacker who finds a way to steal these tokens from exercising the tokens. This type of token is called a holder-of-key token. Unlike bearer tokens, the recipient of a holder-of-key token can verify whether the sender of the token is legitimate.

If the following conditions are satisfied on a token request, Keycloak will bind an access token and a refresh token with a client certificate and issue them as holder-of-key tokens. If all conditions are not met, Keycloak rejects the token request.

- The feature is turned on
- A token request is sent to the token endpoint in an authorization code flow or a hybrid flow
- On TLS handshake, Keycloak requests a client certificate and a client send its client certificate
- On TLS handshake, Keycloak successfully verifies the client certificate

To enable mutual TLS in Keycloak, see [Enable mutual SSL in WildFly](https://www.keycloak.org/docs/latest/server_admin/index.html#_enable-mtls-wildfly).

In the following cases, Keycloak will verify the client sending the access token or the refresh token; if verification fails, Keycloak rejects the token.

- A token refresh request is sent to the token endpoint with a holder-of-key refresh token
- A UserInfo request is sent to UserInfo endpoint with a holder-of-key access token
- A logout request is sent to Logout endpoint with a holder-of-key refresh token

Please see [Mutual TLS Client Certificate Bound Access Tokens](https://tools.ietf.org/html/draft-ietf-oauth-mtls-08#section-3) in the OAuth 2.0 Mutual TLS Client Authentication and Certificate Bound Access Tokens for more details.

WARNING: None of the keycloak client adapters currently support holder-of-key token verification. Instead, keycloak adapters currently treat access and refresh tokens as bearer tokens.

#### 8.1.2. Confidential Client Credentials {#Confidential_Client_Credentials}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/oidc/confidential.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/oidc/confidential.adoc)

If you’ve set the client’s [access type](https://www.keycloak.org/docs/latest/server_admin/index.html#_access-type) to `confidential` in the client’s `Settings` tab, a new `Credentials` tab will show up. As part of dealing with this type of client you have to configure the client’s credentials.

Credentials Tab

![client credentials](assets/client-credentials.png)

The `Client Authenticator` list box specifies the type of credential you are going to use for your confidential client. It defaults to client ID and secret. The secret is automatically generated for you and the `Regenerate Secret` button allows you to recreate this secret if you want or need to.

Alternatively, you can opt to use a signed Json Web Token (JWT) or x509 certificate validation (also called Mutual TLS) instead of a secret.

Signed JWT

![client credentials jwt](assets/client-credentials-jwt.png)

When choosing this credential type you will have to also generate a private key and certificate for the client. The private key will be used to sign the JWT, while the certificate is used by the server to verify the signature. Click on the `Generate new keys and certificate` button to start this process.

Generate Keys

![generate client keys](assets/generate-client-keys.png)

When you generate these keys, Keycloak will store the certificate, and you’ll need to download the private key and certificate for your client to use. Pick the archive format you want and specify the password for the private key and store.

You can also opt to generate these via an external tool and just import the client’s certificate.

Import Certificate

![import client cert](assets/import-client-cert.png)

There are multiple formats you can import from, just choose the archive format you have the certificate stored in, select the file, and click the `Import` button.

Finally note that you don’t even need to import certificate if you choose to `Use JWKS URL` . In that case, you can provide the URL where client publishes it’s public key in [JWK](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html) format. This is flexible because when client changes it’s keys, Keycloak will automatically download them without need to re-import anything on Keycloak side.

If you use client secured by Keycloak adapter, you can configure the JWKS URL like <https://myhost.com/myapp/k_jwks>assuming that <https://myhost.com/myapp> is the root URL of your client application. See [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for additional details.

|      | For the performance purposes, Keycloak caches the public keys of the OIDC clients. If you think that private key of your client was compromised, it is obviously good to update your keys, but it’s also good to clear the keys cache. See [Clearing the cache](https://www.keycloak.org/docs/latest/server_admin/index.html#_clear-cache) section for more details. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Signed JWT with Client Secret

If you select this option in the `Client Authenticator` list box, you can use a JWT signed by client secret instead of the private key.

This client secret will be used to sign the JWT by the client.

X509 Certificate

By enabling this option Keycloak will validate if the client uses proper X509 certificate during the TLS Handshake.

|      | This option requires mutual TLS in Keycloak, see [Enable mutual SSL in WildFly](https://www.keycloak.org/docs/latest/server_admin/index.html#_enable-mtls-wildfly). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Import Certificate

![x509 client auth](assets/x509-client-auth.png)

The validator checks also the certificate’s Subject DN field with configured regexp validation expression. For some use cases, it is sufficient to accept all certificates. In that case, you can use `(.*?)(?:$)` expression.

There are two ways for Keycloak to obtain the Client ID from the request. The first option is the `client_id` parameter in the query (described in Section 2.2 of the [OAuth 2.0 Specification](https://tools.ietf.org/html/rfc6749)). The second option is to supply `client_id` as a query parameter.

#### 8.1.3. Service Accounts {#Service_Accounts}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/oidc/service-accounts.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/oidc/service-accounts.adoc)

Each OIDC client has a built-in *service account* which allows it to obtain an access token. This is covered in the OAuth 2.0 specifiation under [Client Credentials Grant](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_credentials_grant). To use this feature you must set the [Access Type](https://www.keycloak.org/docs/latest/server_admin/index.html#_access-type) of your client to `confidential`. When you do this, the `Service Accounts Enabled` switch will appear. You need to turn on this switch. Also make sure that you have configured your [client credentials](https://www.keycloak.org/docs/latest/server_admin/index.html#_client-credentials).

To use it you must have registered a valid `confidential` Client and you need to check the switch `Service Accounts Enabled` in Keycloak admin console for this client. In tab `Service Account Roles` you can configure the roles available to the service account retrieved on behalf of this client. Remember that you must have the roles available in Role Scope Mappings (tab `Scope`) of this client as well, unless you have `Full Scope Allowed` on. As in a normal login, roles from access token are the intersection of:

- Role scope mappings of particular client combined with the role scope mappings inherited from linked client scopes
- Service account roles

The REST URL to invoke on is `/auth/realms/{realm-name}/protocol/openid-connect/token`. Invoking on this URL is a POST request and requires you to post the client credentials. By default, client credentials are represented by clientId and clientSecret of the client in `Authorization: Basic` header, but you can also authenticate the client with a signed JWT assertion or any other custom mechanism for client authentication. You also need to use the parameter `grant_type=client_credentials` as per the OAuth2 specification.

For example the POST invocation to retrieve a service account can look like this:

```
    POST /auth/realms/demo/protocol/openid-connect/token
    Authorization: Basic cHJvZHVjdC1zYS1jbGllbnQ6cGFzc3dvcmQ=
    Content-Type: application/x-www-form-urlencoded

    grant_type=client_credentials
```

The response would be this [standard JSON document](https://tools.ietf.org/html/rfc6749#section-4.4.3) from the OAuth 2.0 specification.

```
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

The retrieved access token can be refreshed or logged out by an out-of-bound request.

#### 8.1.4. Audience Support {#Audience_Support}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/oidc/audience.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/oidc/audience.adoc)

The typical environment where the Keycloak is deployed generally consists of a set of *confidential* or *public*client applications (frontend client applications) which use Keycloak for authentication.

There are also *services* (called *Resource Servers* in the OAuth 2 specification), which serve requests from frontend client applications and provide resources. These services typically require an *Access token* (Bearer token) to be sent to them to authenticate for a particular request. This token was previously obtained by the frontend application when it tries to log in against Keycloak.

In the environment where the trust among services is low, you may encounter this scenario:

1. A frontend client called `my-app` is required to be authenticated against Keycloak.
2. A user is authenticated in Keycloak. Keycloak then issued tokens to the `my-app` application.
3. The application `my-app` used the token to invoke the service `evil-service`. The application needs to invoke `evil-service` as the service is able to serve some very useful data.
4. The `evil-service` application returned the response to `my-app`. However, at the same time, it kept the token previously sent to it.
5. The `evil-service` application then invoked another service called `good-service` with the previously kept token. The invocation was successful and `good-service` returned the data. This results in broken security as the `evil-service`misused the token to access other services on behalf of the client `my-app`.

This flow may not be an issue in many environments with the high level of trust among services. However in other environments, where the trust among services is lower, this can be problematic.

|      | In some environments, this example work flow may be even requested behavior as the `evil-service` may need to retrieve additional data from `good-service` to be able to properly return the requested data to the original caller (my-app client). You may notice similarities with the Kerberos Credential Delegation. As with the Kerberos Credential Delegation, an unlimited audience is a mixed blessing as it is only useful when a high level of trust exists among services. Otherwise, it is recommended to limit audience as described next. You can limit audience and at the same time allow the `evil-service` to retrieve required data from the `good-service`. In this case, you need to ensure that both the `evil-service` and `good-service` are added as audiences to the token. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

To prevent any misuse of the access token as in the example above, it is recommended to limit *Audience* on the token and configure your services to verify the audience on the token. If this is done, the flow above will change, like this:

1. A frontend client called `my-app` is required to be authenticated against Keycloak.
2. A user is authenticated in Keycloak. Keycloak then issued tokens to the `my-app` application. The client application already knows that it will need to invoke service `evil-service`, so it used `scope=evil-service` in the authentication request sent to the Keycloak server. See [Client Scopes section](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes) for more details about the *scope* parameter. The token issued to the `my-app` client contains the audience, as in `"audience": [ "evil-service" ]`, which declares that the client wants to use this access token to invoke just the service `evil-service`.
3. The `evil-service` application served the request to the `my-app`. At the same time, it kept the token previously sent to it.
4. The `evil-service` application then invoked the `good-service` with the previously kept token. Invocation was not successful because `good-service` checks the audience on the token and it sees that audience is only `evil-service`. This is expected behavior and security is not broken.

If the client wants to invoke the `good-service` later, it will need to obtain another token by issuing the SSO login with the`scope=good-service`. The returned token will then contain `good-service` as an audience:

```
"audience": [ "good-service" ]
```

and can be used to invoke `good-service`.

##### Setup {#Setup}
To properly set up audience checking:

- Ensure that services are configured to check audience on the access token sent to them by adding the flag *verify-token-audience* in the adapter configuration. See [Adapter configuration](https://www.keycloak.org/docs/6.0/securing_apps/#_java_adapter_config) for details.
- Ensure that when an access token is issued by Keycloak, it contains all requested audiences and does not contain any audiences that are not needed. The audience can be either automatically added due the client roles as described in the [next section](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_resolve) or it can be hardcoded as described [below](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_hardcoded).

##### Automatically add audience {#Automatically_add_audience}
In the default client scope *roles*, there is an *Audience Resolve* protocol mapper defined. This protocol mapper will check all the clients for which current token has at least one client role available. Then the client ID of each of those clients will be added as an audience automatically. This is especially useful if your service (usually bearer-only) clients rely on client roles.

As an example, let us assume that you have a bearer-only client `good-service` and the confidential client `my-app`, which you want to authenticate and then use the access token issued for the `my-app` to invoke the `good-service` REST service. If the following are true:

- The `good-service` client has any client roles defined on itself
- Target user has at least one of those client roles assigned
- Client `my-app` has the role scope mappings for the assigned role

then the `good-service` will be automatically added as an audience to the access token issued for the `my-app`.

|      | If you want to ensure that audience is not added automatically, do not configure role scope mappings directly on the `my-app` client, but instead create a dedicated client scope, for example called `good-service`, which will contain the role scope mappings for the client roles of the `good-service` client. Assuming that this client scope will be added as an optional client scope to the `my-app` client, the client roles and audience will be added to the token just if explicitly requested by the `scope=good-service` parameter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

|      | The frontend client itself is not automatically added to the access token audience. This allows for easy differentiation between the access token and the ID token, because the access token will not contain the client for which the token was issued as an audience. So in he example above, the `my-app` won’t be added as an audience. If you need the client itself as an audience, see the [hardcoded audience](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_hardcoded) option. However, using the same client as both frontend and REST service is not recommended. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Hardcoded audience {#Hardcoded_audience}
For the case when your service relies on realm roles or does not rely on the roles in the token at all, it can be useful to use hardcoded audience. This is a protocol mapper, which will add client ID of the specified service client as an audience to the token. You can even use any custom value, for example some URL, if you want different audience than client ID.

You can add protocol mapper directly to the frontend client, however than the audience will be always added. If you want more fine-grain control, you can create protocol mapper on the dedicated client scope, which will be called for example `good-service`.

Audience Protocol Mapper

![audience mapper](assets/audience_mapper.png)

- From the [Installation tab](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_installation) of the `good-service` client, you can generate the adapter configuration and you can confirm that *verify-token-audience* option will be set to true. This indicates that the adapter will require verifying the audience if you use this generated configuration.
- Finally, you need to ensure that the `my-app` frontend client is able to request `good-service` as an audience in its tokens. On the `my-app` client, click the *Client Scopes* tab. Then assign `good-service` as an optional (or default) client scope. See [Client Scopes Linking section](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking) for more details.
- You can optionally [Evaluate Client Scopes](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_evaluate) and generate an example access token. If you do, notice that `good-service`will be added to the audience of the generated access token only if `good-service` is included in the *scope* parameter in the case you assigned it as an optional client scope.
- In your `my-app` application, you must ensure that *scope* parameter is used with the value `good-service` always included when you want to issue the token for accessing the `good-service`. See the [parameters forwarding section](https://www.keycloak.org/docs/6.0/securing_apps/#_params_forwarding), if your application uses the servlet adapter, or the [javascript adapter section](https://www.keycloak.org/docs/6.0/securing_apps/#_javascript_adapter), if your application uses the javascript adapter.

|      | If you are unsure what the correct audience and roles in the token will be, it is always a good idea to [Evaluate Client Scopes](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_evaluate) in the admin console and do some testing around it. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

|      | Both the *Audience* and *Audience Resolve* protocol mappers add the audiences just to the access token by default. The ID Token typically contains only single audience, which is the client ID of the client for which the token was issued. This is a requirement of the OpenID Connect specification. On the other hand, the access token does not necessarily have the client ID of the client, which was the token issued for, unless any of the audience mappers added it. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 8.2. SAML Clients {#SAML_Clients}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/client-saml.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/client-saml.adoc)

Keycloak supports [SAML 2.0](https://www.keycloak.org/docs/latest/server_admin/index.html#_saml) for registered applications. Both POST and Redirect bindings are supported. You can choose to require client signature validation and can have the server sign and/or encrypt responses as well.

To create a SAML client go to the `Clients` left menu item. On this page you’ll see a `Create` button on the right.

Clients

![clients](assets/clients.png)

This will bring you to the `Add Client` page.

Add Client

![add client saml](assets/add-client-saml.png)

Enter in the `Client ID` of the client. This is often a URL and will be the expected `issuer` value in SAML requests sent by the application. Next select `saml` in the `Client Protocol` drop down box. Finally enter in the `Client SAML Endpoint` URL. Enter the URL you want the Keycloak server to send SAML requests and responses to. Usually applications have only one URL for processing SAML requests. If your application has different URLs for its bindings, don’t worry, you can fix this in the `Settings` tab of the client. Click `Save`. This will create the client and bring you to the client `Settings` tab.

Client Settings

![client settings saml](assets/client-settings-saml.png)

- Client ID

  This value must match the issuer value sent with AuthNRequests. Keycloak will pull the issuer from the Authn SAML request and match it to a client by this value.

- Name

  This is the display name for the client whenever it is displayed in a Keycloak UI screen. You can localize the value of this field by setting up a replacement string value i.e. ${myapp}. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information.

- Description

  This specifies the description of the client. This can also be localized.

- Enabled

  If this is turned off, the client will not be allowed to request authentication.

- Consent Required

  If this is on, then users will get a consent page which asks the user if they grant access to that application. It will also display the metadata that the client is interested in so that the user knows exactly what information the client is getting access to. If you’ve ever done a social login to Google, you’ll often see a similar page. Keycloak provides the same functionality.

- Include AuthnStatement

  SAML login responses may specify the authentication method used (password, etc.) as well as a timestamp of the login. Setting this to on will include that statement in the response document.

- Sign Documents

  When turned on, Keycloak will sign the document using the realm’s private key.

- Optimize REDIRECT signing key lookup

  When turned on, the SAML protocol messages will include Keycloak native extension that contains a hint with signing key ID. When the SP understands this extension, it can use it for signature validation instead of attempting to validate signature with all known keys. This option only applies to REDIRECT bindings where the signature is transferred in query parameters where there is no place with this information in the signature information (contrary to POST binding messages where key ID is always included in document signature). Currently this is relevant to situations where both IDP and SP are provided by Keycloak server and adapter. This option is only relevant when `Sign Documents` is switched on.

- Sign Assertions

  The `Sign Documents` switch signs the whole document. With this setting the assertion is also signed and embedded within the SAML XML Auth response.

- Signature Algorithm

  Choose between a variety of algorithms for signing SAML documents.

- SAML Signature Key Name

  Signed SAML documents sent via POST binding contain identification of signing key in `KeyName` element. This by default contains Keycloak key ID. However various vendors might expect a different key name or no key name at all. This switch controls whether `KeyName` contains key ID (option `KEY_ID`), subject from certificate corresponding to the realm key (option `CERT_SUBJECT` - expected for instance by Microsoft Active Directory Federation Services), or that the key name hint is completely omitted from the SAML message (option `NONE`).

- Canonicalization Method

  Canonicalization method for XML signatures.

- Encrypt Assertions

  Encrypt assertions in SAML documents with the realm’s private key. The AES algorithm is used with a key size of 128 bits.

- Client Signature Required

  Expect that documents coming from a client are signed. Keycloak will validate this signature using the client public key or cert set up in the `SAML Keys` tab.

- Force POST Binding

  By default, Keycloak will respond using the initial SAML binding of the original request. By turning on this switch, you will force Keycloak to always respond using the SAML POST Binding even if the original request was the Redirect binding.

- Front Channel Logout

  If true, this application requires a browser redirect to be able to perform a logout. For example, the application may require a cookie to be reset which could only be done via a redirect. If this switch is false, then Keycloak will invoke a background SAML request to logout the application.

- Force Name ID Format

  If the request has a name ID policy, ignore it and used the value configured in the admin console under Name ID Format

- Name ID Format

  Name ID Format for the subject. If no name ID policy is specified in the request or if the Force Name ID Format attribute is true, this value is used. Properties used for each of the respective formats are defined below.

- Root URL

  If Keycloak uses any configured relative URLs, this value is prepended to them.

- Valid Redirect URIs

  This is an optional field. Enter in a URL pattern and click the + sign to add. Click the - sign next to URLs you want to remove. Remember that you still have to click the `Save` button! Wildcards (\*) are only allowed at the end of of a URI, i.e. http://host.com/*. This field is used when the exact SAML endpoints are not registered and Keycloak is pulling the Assertion Consumer URL from the request.

- Base URL

  If Keycloak needs to link to the client, this URL would be used.

- Master SAML Processing URL

  This URL will be used for all SAML requests and the response will be directed to the SP. It will be used as the Assertion Consumer Service URL and the Single Logout Service URL. If a login request contains the Assertion Consumer Service URL, that will take precedence, but this URL must be valided by a registered Valid Redirect URI pattern

- Assertion Consumer Service POST Binding URL

  POST Binding URL for the Assertion Consumer Service.

- Assertion Consumer Service Redirect Binding URL

  Redirect Binding URL for the Assertion Consumer Service.

- Logout Service POST Binding URL

  POST Binding URL for the Logout Service.

- Logout Service Redirect Binding URL

  Redirect Binding URL for the Logout Service.

#### 8.2.1. IDP Initiated Login {#IDP_Initiated_Login}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/saml/idp-initiated-login.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/saml/idp-initiated-login.adoc)

IDP Initiated Login is a feature that allows you to set up an endpoint on the Keycloak server that will log you into a specific application/client. In the `Settings` tab for your client, you need to specify the `IDP Initiated SSO URL Name`. This is a simple string with no whitespace in it. After this you can reference your client at the following URL: `root/auth/realms/{realm}/protocol/saml/clients/{url-name}`

The IDP initiated login implementation prefers *POST* over *REDIRECT* binding (check [saml bindings](https://www.keycloak.org/docs/latest/server_admin/index.html#saml-bindings) for more information). Therefore the final binding and SP URL are selected in the following way:

1. If the specific `Assertion Consumer Service POST Binding URL` is defined (inside `Fine Grain SAML Endpoint Configuration` section of the client settings) *POST* binding is used through that URL.
2. If the general `Master SAML Processing URL` is specified then *POST* binding is used again throught this general URL.
3. As the last resort, if the `Assertion Consumer Service Redirect Binding URL` is configured (inside `Fine Grain SAML Endpoint Configuration`) *REDIRECT* binding is used with this URL.

If your client requires a special relay state, you can also configure this on the `Settings` tab in the `IDP Initiated SSO Relay State` field. Alternatively, browsers can specify the relay state in a `RelayState` query parameter, i.e.`root/auth/realms/{realm}/protocol/saml/clients/{url-name}?RelayState=thestate`.

When using [identity brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker), it is possible to set up an IDP Initiated Login for a client from an external IDP. The actual client is set up for IDP Initiated Login at broker IDP as described above. The external IDP has to set up the client for application IDP Initiated Login that will point to a special URL pointing to the broker and representing IDP Initiated Login endpoint for a selected client at the brokering IDP. This means that in client settings at the external IDP:

- `IDP Initiated SSO URL Name` is set to a name that will be published as IDP Initiated Login initial point,
- `Assertion Consumer Service POST Binding URL` in the `Fine Grain SAML Endpoint Configuration` section has to be set to the following URL: `broker-root/auth/realms/{broker-realm}/broker/{idp-name}/endpoint/clients/{client-id}`, where:
  - *broker-root* is base broker URL
  - *broker-realm* is name of the realm at broker where external IDP is declared
  - *idp-name* is name of the external IDP at broker
  - *client-id* is the value of `IDP Initiated SSO URL Name` attribute of the SAML client defined at broker. It is this client, which will be made available for IDP Initiated Login from the external IDP.

Please note that you can import basic client settings from the brokering IDP into client settings of the external IDP - just use [SP Descriptor](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker_saml_sp_descriptor) available from the settings of the identity provider in the brokering IDP, and add `clients/*client-id*` to the endpoint URL.

#### 8.2.2. SAML Entity Descriptors {#SAML_Entity_Descriptors}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/saml/entity-descriptors.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/saml/entity-descriptors.adoc)

Instead of manually registering a SAML 2.0 client, you can import it via a standard SAML Entity Descriptor XML file. There is an `Import` option on the Add Client page.

Add Client

![add client saml](assets/add-client-saml.png)

Click the `Select File` button and load your entity descriptor file. You should review all the information there to make sure everything is set up correctly.

Some SAML client adapters like *mod-auth-mellon* need the XML Entity Descriptor for the IDP. You can obtain this by going to this public URL: `root/auth/realms/{realm}/protocol/saml/descriptor`

### 8.3. Client Links {#Client_Links}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/client-link.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/client-link.adoc)

For scenarios where one wants to link from one client to another, Keycloak provides a special redirect endpoint: `/realms/realm_name/clients/{client-id}/redirect`.

If a client accesses this endpoint via an `HTTP GET` request, Keycloak returns the configured base URL for the provided Client and Realm in the form of an `HTTP 307` (Temporary Redirect) via the response’s `Location` header.

Thus, a client only needs to know the Realm name and the Client ID in order to link to them. This indirection helps avoid hard-coding client base URLs.

As an example, given the realm `master` and the client-id `account`:

```
http://host:port/auth/realms/master/clients/account/redirect
```

Would temporarily redirect to: <http://host:port/auth/realms/master/account>

### 8.4. OIDC Token and SAML Assertion Mappings {#OIDC_Token_and_SAML_Assertion_Mappings}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/protocol-mappers.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/protocol-mappers.adoc)

Applications that receive ID Tokens, Access Tokens, or SAML assertions may need or want different user metadata and roles. Keycloak allows you to define what exactly is transferred. You can hardcode roles, claims and custom attributes. You can pull user metadata into a token or assertion. You can rename roles. Basically you have a lot of control of what exactly goes back to the client.

Within the Admin Console, if you go to an application you’ve registered, you’ll see a `Mappers` tab. Here’s one for an OIDC based client.

Mappers Tab

![mappers oidc](assets/mappers-oidc.png)

The new client does not have any built-in mappers, however it usually inherits some mappers from the client scopes as described in the [client scopes section](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes). Protocol mappers map things like, for example, email address to a specific claim in the identity and access token. Their function should each be self explanatory from their name. There are additional pre-configured mappers that are not attached to the client that you can add by clicking the `Add Builtin` button.

Each mapper has common settings as well as additional ones depending on which type of mapper you are adding. Click the `Edit` button next to one of the mappers in the list to get to the config screen.

Mapper Config

![mapper config](assets/mapper-config.png)

The best way to learn about a config option is to hover over its tooltip.

Most OIDC mappers also allow you to control where the claim gets put. You can opt to include or exclude the claim from both the *id* and *access* tokens by fiddling with the `Add to ID token` and `Add to access token` switches.

Finally, you can also add other mapper types. If you go back to the `Mappers` tab, click the `Create` button.

Add Mapper

![add mapper](assets/add-mapper.png)

Pick a `Mapper Type` from the list box. If you hover over the tooltip, you’ll see a description of what that mapper type does. Different config parameters will appear for different mapper types.

#### 8.4.1. Priority order {#Priority_order}
Mapper implementations have *priority order*. This priority order is not the configuration property of the mapper; rather, it is the property of the concrete implementation of the mapper.

Mappers are sorted in the admin console by the order in the list of mappers and the changes in the token or assertion will be applied using that order with the lowest being applied first. This means that implementations which are dependent on other implementations are processed in the needed order.

For example, when we first want to compute the roles which will be included with a token, we first resolve audiences based on those roles. Then, we process a JavaScript script that uses the roles and audiences already available in the token.

#### 8.4.2. OIDC User Session Note Mappers {#OIDC_User_Session_Note_Mappers}
User session details are via mappers and depend on various criteria. User session details are automatically included when you use or enable a feature on a client. You can also click the `Add builtin` button to include session details.

Impersonated user sessions provide the following details:

- `IMPERSONATOR_ID`: The ID of an impersonating user
- `IMPERSONATOR_USERNAME`: The username of an impersonating user

Service account sessions provide the following details:

- `clientId`: The client ID of the service account
- `clientAddress`: The remote host IP of the service account authenticated device
- `clientHost`: The remote host name of the service account authenticated device

### 8.5. Generating Client Adapter Config {#Generating_Client_Adapter_Config}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/installation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/installation.adoc)

The Keycloak can pre-generate configuration files that you can use to install a client adapter for in your application’s deployment environment. A number of adapter types are supported for both OIDC and SAML. Go to the `Installation` tab of the client you want to generate configuration for.

![client installation](assets/client-installation.png)

Select the `Format Option` you want configuration generated for. All Keycloak client adapters for OIDC and SAML are supported. The mod-auth-mellon Apache HTTPD adapter for SAML is supported as well as standard SAML entity descriptor files.

### 8.6. Client Scopes {#Client_Scopes}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/clients/client-scopes.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/clients/client-scopes.adoc)

If you have many applications you need to secure and register within your organization, it can become tedious to configure the [protocol mappers](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) and [role scope mappings](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings) for each of these clients. Keycloak allows you to define a shared client configuration in an entity called a *client scope*.

Client scopes also provide support for the OAuth 2 `scope` parameter, which allows a client application to request more or fewer claims or roles in the access token, according to the application needs.

To create a client scope, follow these steps:

- Go to the `Client Scopes` left menu item. This initial screen shows you a list of currently defined client scopes.

Client Scopes List

![client scopes list](assets/client-scopes-list.png)

- Click the `Create` button. Name the client scope and save. A *client scope* will have similar tabs to a regular clients. You can define [protocol mappers](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) and [role scope mappings](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings), which can be inherited by other clients, and which are configured to inherit from this client scope.

#### 8.6.1. Protocol {#Protocol}
When you are creating the client scope, you must choose the `Protocol`. Only the clients which use same protocol can then be linked with this client scope.

Once you have created new realm, you can see that there is a list of pre-defined (builtin) client scopes in the menu.

- For the SAML protocol, there is one builtin client scope, `roles_list`, which contains one protocol mapper for showing the roles list in the SAML assertion.
- For the OpenID Connect protocol, there are client scopes `profile`, `email`, `address`, `phone`, `offline_access`, `roles`, `web-origins` and `microprofile-jwt`.

The client scope, `offline_access`, is useful when client wants to obtain offline tokens. Learn about offline tokens in the[Offline Access section](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access) or in the [OpenID Connect specification](https://openid.net/specs/openid-connect-core-1_0.html#OfflineAccess), where scope parameter is defined with the value `offline_access`.

The client scopes `profile`, `email`, `address` and `phone` are also defined in the [OpenID Connect specification](https://openid.net/specs/openid-connect-core-1_0.html#ScopeClaims). These client scopes do not have any role scope mappings defined, but they have some protocol mappers defined, and these mappers correspond to the claims defined in the OpenID Connect specification.

For example, when you click to open the `phone` client scope and open the `Mappers` tab, you will see the protocol mappers, which correspond to the claims defined in the specification for the scope `phone`.

Client Scope Mappers

![client scopes phone](assets/client-scopes-phone.png)

When the `phone` client scope is linked to a client, that client automatically inherits all the protocol mappers defined in the`phone` client scope. Access tokens issued for this client will contain the phone number information about the user, assuming that the user has a defined phone number.

Builtin client scopes contain exactly the protocol mappers as defined per the specification, however you are free to edit client scopes and create/update/remove any protocol mappers (or role scope mappings).

The client scope `roles` is not defined in the OpenID Connect specification and it is also not added automatically to the `scope` claim in the access token. This client scope has some mappers, which are used to add roles of the user to the access token and possibly add some audiences for the clients with at least one client role as described in the [Audience section](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience_resolve).

The client scope `web-origins` is also not defined in the OpenID Connect specification and not added to the `scope` claim. This is used to add allowed web origins to the access token `allowed-origins` claim.

The client scope `microprofile-jwt` was created to handle the claims defined in the [MicroProfile/JWT Auth Specification](https://wiki.eclipse.org/MicroProfile/JWT_Auth). This client scope defines a user property mapper for the `upn` claim and also a realm role mapper for the `groups` claim. These mappers can be changed as needed so that different properties can be used to create the MicroProfile/JWT specific claims.

#### 8.6.2. Consent related settings {#Consent_related_settings}
Client scope contains options related to the consent screen. Those options are useful only if the linked client is configured to require consent (if the `Consent Required` switch is enabled on the client).

- Display On Consent Screen

  If on, and if this client scope is added to a client with consent required, then the text specified by `Consent Screen Text`will be displayed on the consent screen, which is shown once the user is authenticated and right before he is redirected from Keycloak to the client. If the switch is off, then this client scope will not be displayed on the consent screen.

- Consent Screen Text

  The text shown on the consent screen when this client scope is added to some client with consent required defaults to the name of client scope. The value for this text is localizable by specifying a substitution variable with `${var-name}` strings. The localized value is then configured within property files in your theme. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information on localization.

#### 8.6.3. Link Client Scope with the Client {#Link_Client_Scope_with_the_Client}
Linking between client scope and client is configured in the `Client Scopes` tab of the particular client. There are 2 ways of linking between client scope and client.

- Default Client Scopes

  This is applicable for both OpenID Connect and SAML clients. Default client scopes are always applied when issuing OpenID Connect tokens or SAML assertions for this client. The client will inherit Protocol mappers and Role Scope Mappings defined on the client scope. For the OpenID Connect Protocol, the Mappers and Role Scope Mappings are always applied, regardless of the value used for the scope parameter in the OpenID Connect authorization request.

- Optional Client Scopes

  This is applicable only for OpenID Connect clients. Optional client scopes are applied when issuing tokens for this client, but only when they are requested by the `scope` parameter in the OpenID Connect authorization request.

##### Example {#Example}
For this example, we assume that the client has `profile` and `email` linked as default client scopes, and `phone` and `address` are linked as optional client scopes. The client will use the value of the scope parameter when sending a request to the OpenID Connect authorization endpoint:

```
scope=openid phone
```

The scope parameter contains the string, with the scope values divided by space (which is also the reason why a client scope name cannot contain a space character in it). The value `openid` is the meta-value used for all OpenID Connect requests, so we will ignore it for this example. The token will contain mappers and role scope mappings from the client scopes `profile`, `email` (which are default scopes) and `phone` (an optional client scope requested by the scope parameter).

#### 8.6.4. Evaluating Client Scopes {#Evaluating_Client_Scopes}
The tabs `Mappers` and `Scope` of the client contain the protocol mappers and role scope mappings declared solely for this client. They do not contain the mappers and scope mappings inherited from client scopes. However, it may be useful to see what the effective protocol mappers will be (protocol mappers defined on the client itself as well as inherited from the linked client scopes) and the effective role scope mappings used when you generate the token for the particular client.

You can see all of these when you click the `Client Scopes` tab for the client and then open the sub-tab `Evaluate`. From here you can select the optional client scopes that you want to apply. This will also show you the value of the `scope`parameter, which needs to be sent from the application to the Keycloak OpenID Connect authorization endpoint.

Evaluating Client Scopes

![client scopes evaluate](assets/client-scopes-evaluate.png)

|      | If you want to see how you can send a custom value for a `scope` parameter from your application, see the[parameters forwarding section](https://www.keycloak.org/docs/6.0/securing_apps/#_params_forwarding), if your application uses the servlet adapter, or the [javascript adapter section](https://www.keycloak.org/docs/6.0/securing_apps/#_javascript_adapter), if your application uses the javascript adapter. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Generating Example Tokens {#Generating_Example_Tokens}
To see an example of a real access token, generated for the particular user and issued for the particular client, with the specified value of `scope` parameter, select the user from the `Evaluate` screen. This will generate an example token that includes all of the claims and role mappings used.

#### 8.6.5. Client Scopes Permissions {#Client_Scopes_Permissions}
When issuing tokens for a particular user, the client scope is applied only if the user is permitted to use it. In the case that a client scope does not have any role scope mappings defined on itself, then each user is automatically permitted to use this client scope. However, when a client scope has any role scope mappings defined on itself, then the user must be a member of at least one of the roles. In other words, there must be an intersection between the user roles and the roles of the client scope. Composite roles are taken into account when evaluating this intersection.

If a user is not permitted to use the client scope, then no protocol mappers or role scope mappings will be used when generating tokens and the client scope will not appear in the *scope* value in the token.

#### 8.6.6. Realm Default Client Scopes {#Realm_Default_Client_Scopes}
The `Realm Default Client Scopes` allow you to define set of client scopes, which will be automatically linked to newly created clients.

Open the left menu item `Client Scopes` and then select `Default Client Scopes`.

From here, select the client scopes that you want to add as `Default Client Scopes` to newly created clients and `Optional Client Scopes` to newly created clients.

Default Client Scopes

![client scopes default](assets/client-scopes-default.png)

Once the client is created, you can unlink the default client scopes, if needed. This is similar to how you remove [Default Roles](https://www.keycloak.org/docs/latest/server_admin/index.html#_default_roles).

#### 8.6.7. Scopes explained {#Scopes_explained}
The term `scope` is used in Keycloak on few places. Various occurrences of scopes are related to each other, but may have a different context and meaning. To clarify, here we explain the various `scopes` used in Keycloak.

- Client scope

  Referenced in this chapter. Client scopes are entities in Keycloak, which are configured at the realm level and they can be linked to clients. The client scopes are referenced by their name when a request is sent to the Keycloak authorization endpoint with a corresponding value of the `scope` parameter. The details are described in the [section about client scopes linking](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking).

- Role scope mapping

  This can be seen when you open tab `Scope` of a client or client scope. Role scope mapping allows you to limit the roles which can be used in the access tokens. The details are described in the [Role Scope Mappings section](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings).

- Authorization scopes

  This is used by the Authorization feature. The `Authorization Scope` is the action which can be done in the application. More details in the [Authorization Services Guide](https://www.keycloak.org/docs/6.0/authorization_services/).

## 9. Roles {#Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles.adoc)

Roles identify a type or category of user. `Admin`, `user`, `manager`, and `employee` are all typical roles that may exist in an organization. Applications often assign access and permissions to specific roles rather than individual users as dealing with users can be too fine grained and hard to manage. For example, the Admin Console has specific roles which give permission to users to access parts of the Admin Console UI and perform certain actions. There is a global namespace for roles and each client also has its own dedicated namespace where roles can be defined.

### 9.1. Realm Roles {#Realm_Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/realm-roles.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/realm-roles.adoc)

Realm-level roles are a global namespace to define your roles. You can see the list of built-in and created roles by clicking the `Roles` left menu item.

![roles](assets/roles.png)

To create a role, click **Add Role** on this page, enter in the name and description of the role, and click **Save**.

Add Role

![role](assets/role.png)

The value for the `description` field is localizable by specifying a substitution variable with `${var-name}` strings. The localized value is then configured within property files in your theme. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information on localization.

### 9.2. Client Roles {#Client_Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/client-roles.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/client-roles.adoc)

Client roles are basically a namespace dedicated to a client. Each client gets its own namespace. Client roles are managed under the `Roles` tab under each individual client. You interact with this UI the same way you do for realm-level roles.

### 9.3. Composite Roles {#Composite_Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/composite.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/composite.adoc)

Any realm or client level role can be turned into a *composite role*. A *composite role* is a role that has one or more additional roles associated with it. When a composite role is mapped to the user, the user also gains the roles associated with that composite. This inheritance is recursive so any composite of composites also gets inherited.

To turn a regular role into a composite role, go to the role detail page and flip the `Composite Role` switch on.

Composite Role

![composite role](assets/composite-role.png)

Once you flip this switch the role selection UI will be displayed lower on the page and you’ll be able to associate realm level and client level roles to the composite you are creating. In this example, the `employee` realm-level role was associated with the `developer` composite role. Any user with the `developer` role will now also inherit the `employee` role too.

|      | When tokens and SAML assertions are created, any composite will also have its associated roles added to the claims and assertions of the authentication response sent back to the client. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 9.4. User Role Mappings {#User_Role_Mappings}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/user-role-mappings.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/user-role-mappings.adoc)

User role mappings can be assigned individually to each user through the `Role Mappings` tab for that single user.

Role Mappings

![user role mappings](assets/user-role-mappings.png)

In the above example, we are about to assign the composite role `developer` that was created in the [Composite Roles](https://www.keycloak.org/docs/latest/server_admin/index.html#_composite-roles)chapter.

Effective Role Mappings

![effective role mappings](assets/effective-role-mappings.png)

Once the `developer` role is assigned, you see that the `employee` role that is associated with the `developer` composite shows up in the `Effective Roles`. `Effective Roles` are all roles that are explicitly assigned to the user as well as any roles that are inherited from composites.

#### 9.4.1. Default Roles {#Default_Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/user-role-mappings/default-roles.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/user-role-mappings/default-roles.adoc)

Default roles allow you to automatically assign user role mappings when any user is newly created or imported through [Identity Brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker). To specify default roles go to the `Roles` left menu item, and click the `Default Roles` tab.

Default Roles

![default roles](assets/default-roles.png)

As you can see from the screenshot, there are already a number of *default roles* set up by default.

### 9.5. Role Scope Mappings {#Role_Scope_Mappings}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/roles/role-scope-mappings.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/roles/role-scope-mappings.adoc)

When an OIDC access token or SAML assertion is created, all the user role mappings of the user are, by default, added as claims within the token or assertion. Applications use this information to make access decisions on the resources controlled by that application. In Keycloak, access tokens are digitally signed and can actually be re-used by the application to invoke on other remotely secured REST services. This means that if an application gets compromised or there is a rogue client registered with the realm, attackers can get access tokens that have a broad range of permissions and your whole network is compromised. This is where *role scope mappings* becomes important.

*Role Scope Mappings* is a way to limit the roles that get declared inside an access token. When a client requests that a user be authenticated, the access token they receive back will only contain the role mappings you’ve explicitly specified for the client’s scope. This allows you to limit the permissions each individual access token has rather than giving the client access to all of the user’s permissions. By default, each client gets all the role mappings of the user. You can view this in the `Scope` tab of each client.

Full Scope

![full client scope](assets/full-client-scope.png)

You can see from the picture that the effective roles of the scope are every declared role in the realm. To change this default behavior, you must explicitly turn off the `Full Scope Allowed` switch and declare the specific roles you want in each individual client. Alternatively, you can also use [client scopes](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes) to define the same role scope mappings for a whole set of clients.

Partial Scope

![client scope](assets/client-scope.png)

## 10. Groups {#Groups}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/groups.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/groups.adoc)

Groups in Keycloak allow you to manage a common set of attributes and role mappings for a set of users. Users can be members of zero or more groups. Users inherit the attributes and role mappings assigned to each group. To manage groups go to the `Groups` left menu item.

Groups

![groups](assets/groups.png)

Groups are hierarchical. A group can have many subgroups, but a group can only have one parent. Subgroups inherit the attributes and role mappings from the parent. This applies to the user as well. So, if you have a parent group and a child group and a user that only belongs to the child group, the user inherits the attributes and role mappings of both the parent and child. In this example, we have a top level `Sales` group and a child `North America` subgroup. To add a group, click on the parent you want to add a new child to and click `New` button. Select the `Groups` icon in the tree to make a top-level group. Entering in a group name in the `Create Group` screen and hitting `Save` will bring you to the individual group management page.

Group

![group](assets/group.png)

The `Attributes` and `Role Mappings` tab work exactly as the tabs with similar names under a user. Any attributes and role mappings you define will be inherited by the groups and users that are members of this group.

To add a user to a group you need to go all the way back to the user detail page and click on the `Groups` tab there.

User Groups

![user groups](assets/user-groups.png)

Select a group from the `Available Groups` tree and hit the `join` button to add the user to a group. Vice versa to remove a group. Here we’ve added the user *Jim* to the *North America* sales group. If you go back to the detail page for that group and select the `Membership` tab, *Jim* is now displayed there.

Group Membership

![group membership](assets/group-membership.png)

### 10.1. Groups vs. Roles {#Groups_vs__Roles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/groups/groups-vs-roles.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/groups/groups-vs-roles.adoc)

In the IT world the concepts of Group and Role are often blurred and interchangeable. In Keycloak, Groups are just a collection of users that you can apply roles and attributes to in one place. Roles define a type of user and applications assign permission and access control to roles

Aren’t [Composite Roles](https://www.keycloak.org/docs/latest/server_admin/index.html#_composite-roles) also similar to Groups? Logically they provide the same exact functionality, but the difference is conceptual. Composite roles should be used to apply the permission model to your set of services and applications. Groups should focus on collections of users and their roles in your organization. Use groups to manage users. Use composite roles to manage applications and services.

### 10.2. Default Groups {#Default_Groups}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/groups/default-groups.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/groups/default-groups.adoc)

Default groups allow you to automatically assign group membership whenever any new user is created or imported through [Identity Brokering](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker). To specify default groups go to the `Groups` left menu item, and click the `Default Groups` tab.

Default Groups

![default groups](assets/default-groups.png)

## 11. Admin Console Access Control and Permissions {#Admin_Console_Access_Control_and_Permissions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/admin-console-permissions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/admin-console-permissions.adoc)

Each realm created on the Keycloak has a dedicated Admin Console from which that realm can be managed. The `master` realm is a special realm that allows admins to manage more than one realm on the system. You can also define fine-grained access to users in different realms to manage the server. This chapter goes over all the scenarios for this.

### 11.1. Master Realm Access Control {#Master_Realm_Access_Control}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/admin-console-permissions/master-realm.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/admin-console-permissions/master-realm.adoc)

The `master` realm in Keycloak is a special realm and treated differently than other realms. Users in the Keycloak `master` realm can be granted permission to manage zero or more realms that are deployed on the Keycloak server. When a realm is created, Keycloak automatically creates various roles that grant fine-grain permissions to access that new realm. Access to The Admin Console and Admin REST endpoints can be controlled by mapping these roles to users in the `master` realm. It’s possible to create multiple super users, as well as users that can only manage specific realms.

#### 11.1.1. Global Roles {#Global_Roles}
There are two realm-level roles in the `master` realm. These are:

- admin
- create-realm

Users with the `admin` role are super users and have full access to manage any realm on the server. Users with the `create-realm` role are allowed to create new realms. They will be granted full access to any new realm they create.

#### 11.1.2. Realm Specific Roles {#Realm_Specific_Roles}
Admin users within the `master` realm can be granted management privileges to one or more other realms in the system. Each realm in Keycloak is represented by a client in the `master` realm. The name of the client is `<realm name>-realm`. These clients each have client-level roles defined which define varying level of access to manage an individual realm.

The roles available are:

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

Assign the roles you want to your users and they will only be able to use that specific part of the administration console.

|      | Admins with the `manage-users` role will only be able to assign admin roles to users that they themselves have. So, if an admin has the `manage-users` role but doesn’t have the `manage-realm` role, they will not be able to assign this role. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 11.2. Dedicated Realm Admin Consoles {#Dedicated_Realm_Admin_Consoles}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/admin-console-permissions/per-realm.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/admin-console-permissions/per-realm.adoc)

Each realm has a dedicated Admin Console that can be accessed by going to the url `/auth/admin/{realm-name}/console`. Users within that realm can be granted realm management permissions by assigning specific user role mappings.

Each realm has a built-in client called `realm-management`. You can view this client by going to the `Clients` left menu item of your realm. This client defines client-level roles that specify permissions that can be granted to manage the realm.

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

Assign the roles you want to your users and they will only be able to use that specific part of the administration console.

### 11.3. Fine Grain Admin Permissions {#Fine_Grain_Admin_Permissions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/admin-console-permissions/fine-grain.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/admin-console-permissions/fine-grain.adoc)

|      | Fine Grain Admin Permissions is **Technology Preview** and is not fully supported. This feature is disabled by default.To enable start the server with `-Dkeycloak.profile=preview` or `-Dkeycloak.profile.feature.admin_fine_grained_authz=enabled` . For more details see [Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles). |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Sometimes roles like `manage-realm` or `manage-users` are too coarse grain and you want to create restricted admin accounts that have more fine grain permissions. Keycloak allows you to define and assign restricted access policies for managing a realm. Things like:

- Managing one specific client
- Managing users that belong to a specific group
- Managing membership of a group
- Limited user management.
- Fine grain impersonization control
- Being able to assign a specific restricted set of roles to users.
- Being able to assign a specific restricted set of roles to a composite role.
- Being able to assign a specific restricted set of roles to a client’s scope.
- New general policies for viewing and managing users, groups, roles, and clients.

There’s some important things to note about fine grain admin permissions:

- Fine grain admin permissions were implemented on top of [Authorization Services](https://www.keycloak.org/docs/6.0/authorization_services/). It is highly recommended that you read up on those features before diving into fine grain permissions.
- Fine grain permissions are only available within [dedicated admin consoles](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions) and admins defined within those realms. You cannot define cross-realm fine grain permissions.
- Fine grain permissions are used to grant additional permissions. You cannot override the default behavior of the built in admin roles.

#### 11.3.1. Managing One Specific Client {#Managing_One_Specific_Client}
Let’s look first at allowing an admin to manage one client and one client only. In our example we have a realm called `test`and a client called `sales-application`. In realm `test` we will give a user in that realm permission to only manage that application.

|      | You cannot do cross realm fine grain permissions. Admins in the `master` realm are limited to the predefined admin roles defined in previous chapters. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

##### Permission Setup {#Permission_Setup}
The first thing we must do is login to the Admin Console so we can set up permissions for that client. We navigate to the management section of the client we want to define fine-grain permissions for.

Client Management

![fine grain client](assets/fine-grain-client.png)

You should see a tab menu item called `Permissions`. Click on that tab.

Client Permissions Tab

![fine grain client permissions tab off](assets/fine-grain-client-permissions-tab-off.png)

By default, each client is not enabled to do fine grain permissions. So turn the `Permissions Enabled` switch to on to initialize permissions.

|      | If you turn the `Permissions Enabled` switch to off, it will delete any and all permissions you have defined for this client. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Client Permissions Tab

![fine grain client permissions tab on](assets/fine-grain-client-permissions-tab-on.png)

When you witch `Permissions Enabled` to on, it initializes various permission objects behind the scenes using [Authorization Services](https://www.keycloak.org/docs/6.0/authorization_services/). For this example, we’re interested in the `manage` permission for the client. Clicking on that will redirect you to the permission that handles the `manage` permission for the client. All authorization objects are contained in the `realm-management` client’s `Authorization` tab.

Client Manage Permission

![fine grain client manage permissions](assets/fine-grain-client-manage-permissions.png)

When first initialized the `manage` permission does not have any policies associated with it. You will need to create one by going to the policy tab. To get there fast, click on the `Authorization` link shown in the above image. Then click on the policies tab.

There’s a pull down menu on this page called `Create policy`. There’s a multitude of policies you can define. You can define a policy that is associated with a role or a group or even define rules in JavaScript. For this simple example, we’re going to create a `User Policy`.

User Policy

![fine grain client user policy](assets/fine-grain-client-user-policy.png)

This policy will match a hard-coded user in the user database. In this case it is the `sales-admin` user. We must then go back to the `sales-application` client’s `manage` permission page and assign the policy to the permission object.

Assign User Policy

![fine grain client assign user policy](assets/fine-grain-client-assign-user-policy.png)

The `sales-admin` user can now has permission to manage the `sales-application` client.

There’s one more thing we have to do. Go to the `Role Mappings` tab and assign the `query-clients` role to the `sales-admin`.

Assign query-clients

![fine grain assign query clients](assets/fine-grain-assign-query-clients.png)

Why do you have to do this? This role tells the Admin Console what menu items to render when the `sales-admin` visits the Admin Console. The `query-clients` role tells the Admin Console that it should render client menus for the `sales-admin`user.

IMPORTANT If you do not set the `query-clients` role, restricted admins like `sales-admin` will not see any menu options when they log into the Admin Console

##### Testing It Out. {#Testing_It_Out_}
Next we log out of the master realm and re-login to the [dedicated admin console](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions) for the `test` realm using the `sales-admin` as a username. This is located under `/auth/admin/test/console`.

Sales Admin Login

![fine grain sales admin login](assets/fine-grain-sales-admin-login.png)

This admin is now able to manage this one client.

#### 11.3.2. Restrict User Role Mapping {#Restrict_User_Role_Mapping}
Another thing you might want to do is to restrict the set a roles an admin is allowed to assign to a user. Continuing our last example, let’s expand the permission set of the 'sales-admin' user so that he can also control which users are allowed to access this application. Through fine grain permissions we can enable it so that the `sales-admin` can only assign roles that grant specific access to the `sales-application`. We can also restrict it so that the admin can only map roles and not perform any other types of user administration.

The `sales-application` has defined three different client roles.

Sales Application Roles

![fine grain sales application roles](assets/fine-grain-sales-application-roles.png)

We want the `sales-admin` user to be able to map these roles to any user in the system. The first step to do this is to allow the role to be mapped by the admin. If we click on the `viewLeads` role, you’ll see that there is a `Permissions` tab for this role.

View Leads Role Permission Tab

![fine grain view leads role tab](assets/fine-grain-view-leads-role-tab.png)

If we click on that tab and turn the `Permissions Enabled` on, you’ll see that there are a number of actions we can apply policies to.

View Leads Permissions

![fine grain view leads permissions](assets/fine-grain-view-leads-permissions.png)

The one we are interested in is `map-role`. Click on this permission and add the same User Policy that was created in the earlier example.

Map-roles Permission

![fine grain map roles permission](assets/fine-grain-map-roles-permission.png)

What we’ve done is say that the `sales-admin` can map the `viewLeads` role. What we have not done is specify which users the admin is allowed to map this role too. To do that we must go to the `Users` section of the admin console for this realm. Clicking on the `Users` left menu item brings us to the users interface of the realm. You should see a `Permissions` tab. Click on that and enable it.

Users Permissions

![fine grain users permissions](assets/fine-grain-users-permissions.png)

The permission we are interested in is `map-roles`. This is a restrictive policy in that it only allows admins the ability to map roles to a user. If we click on the `map-roles` permission and again add the User Policy we created for this, our `sales-admin` will be able to map roles to any user.

The last thing we have to do is add the `view-users` role to the `sales-admin`. This will allow the admin to view users in the realm he wants to add the `sales-application` roles to.

Add view-users

![fine grain add view users](assets/fine-grain-add-view-users.png)

##### Testing It Out. {#Testing_It_Out_}
Next we log out of the master realm and re-login to the [dedicated admin console](https://www.keycloak.org/docs/latest/server_admin/index.html#_per_realm_admin_permissions) for the `test` realm using the `sales-admin` as a username. This is located under `/auth/admin/test/console`.

You will see that now the `sales-admin` can view users in the system. If you select one of the users you’ll see that each user detail page is read only, except for the `Role Mappings` tab. Going to these tab you’ll find that there are no `Available` roles for the admin to map to the user except when we browse the `sales-application` roles.

Add viewLeads

![fine grain add view leads](assets/fine-grain-add-view-leads.png)

We’ve only specified that the `sales-admin` can map the `viewLeads` role.

##### Per Client map-roles Shortcut {#Per_Client_map_roles_Shortcut}
It would be tedious if we had to do this for every client role that the `sales-application` published. to make things easier, there’s a way to specify that an admin can map any role defined by a client. If we log back into the admin console to our master realm admin and go back to the `sales-application` permissions page, you’ll see the `map-roles` permission.

Client map-roles Permission

![fine grain client permissions tab on](assets/fine-grain-client-permissions-tab-on.png)

If you grant access to this particular parmission to an admin, that admin will be able map any role defined by the client.

#### 11.3.3. Full List of Permissions {#Full_List_of_Permissions}
You can do a lot more with fine grain permissions beyond managing a specific client or the specific roles of a client. This chapter defines the whole list of permission types that can be described for a realm.

##### Role {#Role}
When going to the `Permissions` tab for a specific role, you will see these permission types listed.

- map-role

  Policies that decide if an admin can map this role to a user. These policies only specify that the role can be mapped to a user, not that the admin is allowed to perform user role mapping tasks. The admin will also have to have manage or role mapping permissions. See [Users Permissions](https://www.keycloak.org/docs/latest/server_admin/index.html#_users-permissions) for more information.

- map-role-composite

  Policies that decide if an admin can map this role as a composite to another role. An admin can define roles for a client if he has manage permissions for that client but he will not be able to add composites to those roles unless he has the `map-role-composite` privileges for the role he wants to add as a composite.

- map-role-client-scope

  Policies that decide if an admin can apply this role to the scope of a client. Even if the admin can manage the client, he will not have permission to create tokens for that client that contain this role unless this privilege is granted.

##### Client {#Client}
When going to the `Permissions` tab for a specific client, you will see these permission types listed.

- view

  Policies that decide if an admin can view the client’s configuration.

- manage

  Policies that decide if an admin can view and manage the client’s configuration. There is some issues with this in that privileges could be leaked unintentionally. For example, the admin could define a protocol mapper that hardcoded a role even if the admin does not have privileges to map the role to the client’s scope. This is currently the limitation of protocol mappers as they don’t have a way to assign individual permissions to them like roles do.

- configure

  Reduced set of prileges to manage the client. Its like the `manage` scope except the admin is not allowed to define protocol mappers, change the client template, or the client’s scope.

- map-roles

  Policies that decide if an admin can map any role defined by the client to a user. This is a shortcut, easy-of-use feature to avoid having to defin policies for each and every role defined by the client.

- map-roles-composite

  Policies that decide if an admin can map any role defined by the client as a composite to another role. This is a shortcut, easy-of-use feature to avoid having to define policies for each and every role defined by the client.

- map-roles-client-scope

  Policies that decide if an admin can map any role defined by the client to the scope of another client. This is a shortcut, easy-of-use feature to avoid having to define policies for each and every role defined by the client.

##### Users {#Users}
When going to the `Permissions` tab for all users, you will see these permission types listed.

- view

  Policies that decide if an admin can view all users in the realm.

- manage

  Policies that decide if an admin can manage all users in the realm. This permission grants the admin the privilege to perfor user role mappings, but it does not specify which roles the admin is allowed to map. You’ll need to define the privilege for each role you want the admin to be able to map.

- map-roles

  This is a subset of the privileges granted by the `manage` scope. In this case the admin is only allowed to map roles. The admin is not allowed to perform any other user management operation. Also, like `manage`, the roles that the admin is allowed to apply must be specified per role or per set of roles if dealing with client roles.

- manage-group-membership

  Similar to `map-roles` except that it pertains to group membership: which groups a user can be added or removed from. These policies just grant the admin permission to manage group membership, not which groups the admin is allowed to manage membership for. You’ll have to specify policies for each group’s `manage-members` permission.

- impersonate

  Policies that decide if the admin is allowed to impersonate other users. These policies are applied to the admin’s attributes and role mappings.

- user-impersonated

  Policies that decide which users can be impersonated. These policies will be applied to the user being impersonated. For example, you might want to define a policy that will forbid anybody from impersonating a user that has admin privileges.

##### Group {#Group}
When going to the `Permissions` tab for a specific group, you will see these permission types listed.

- view

  Policies that decide if the admin can view information about the group.

- manage

  Policies that decide if the admin can manage the configuration of the group.

- view-members

  Policies that decide if the admin can view the user details of members of the group.

- manage-members

  Policies that decide if the admin can manage the users that belong to this group.

- manage-membership

  Policies that decide if an admin can change the membership of the group. Add or remove members from the group.

### 11.4. Realm Keys {#Realm_Keys}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/realms/keys.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/realms/keys.adoc)

The authentication protocols that are used by Keycloak require cryptographic signatures and sometimes encryption. Keycloak uses asymmetric key pairs, a private and public key, to accomplish this.

Keycloak has a single active keypair at a time, but can have several passive keys as well. The active keypair is used to create new signatures, while the passive keypairs can be used to verify previous signatures. This makes it possible to regularly rotate the keys without any downtime or interruption to users.

When a realm is created a key pair and a self-signed certificate is automatically generated.

To view the active keys for a realm select the realm in the admin console click on `Realm settings` then `Keys`. This will show the currently active keys for the realm. Keycloak currently only supports RSA signatures so there is only one active keypair. In the future as more signature algorithms are added there will be more active keypairs.

To view all available keys select `All`. This will show all active, passive and disabled keys. A keypair can have the status `Active`, but still not be selected as the currently active keypair for the realm. The selected active pair which is used for signatures is selected based on the first key provider sorted by priority that is able to provide an active keypair.

#### 11.4.1. Rotating keys {#Rotating_keys}
It’s recommended to regularly rotate keys. To do so you should start by creating new keys with a higher priority than the existing active keys. Or create new keys with the same priority and making the previous keys passive.

Once new keys are available all new tokens and cookies will be signed with the new keys. When a user authenticates to an application the SSO cookie is updated with the new signature. When OpenID Connect tokens are refreshed new tokens are signed with the new keys. This means that over time all cookies and tokens will use the new keys and after a while the old keys can be removed.

How long you wait to delete old keys is a tradeoff between security and making sure all cookies and tokens are updated. In general it should be acceptable to drop old keys after a few weeks. Users that have not been active in the period between the new keys where added and the old keys removed will have to re-authenticate.

This also applies to offline tokens. To make sure they are updated the applications need to refresh the tokens before the old keys are removed.

As a guideline it may be a good idea to create new keys every 3-6 months and delete old keys 1-2 months after the new keys where created.

#### 11.4.2. Adding a generated keypair {#Adding_a_generated_keypair}
To add a new generated keypair select `Providers` and choose `rsa-generated` from the dropdown. You can change the priority to make sure the new keypair becomes the active keypair. You can also change the `keysize` if you want smaller or larger keys (default is 2048, supported values are 1024, 2048 and 4096).

Click `Save` to add the new keys. This will generated a new keypair including a self-signed certificate.

Changing the priority for a provider will not cause the keys to be re-generated, but if you want to change the keysize you can edit the provider and new keys will be generated.

#### 11.4.3. Adding an existing keypair and certificate {#Adding_an_existing_keypair_and_certificate}
To add a keypair and certificate obtained elsewhere select `Providers` and choose `rsa` from the dropdown. You can change the priority to make sure the new keypair becomes the active keypair.

Click on `Select file` for `Private RSA Key` to upload your private key. The file should be encoded in PEM format. You don’t need to upload the public key as it is automatically extracted from the private key.

If you have a signed certificate for the keys click on `Select file` next to `X509 Certificate`. If you don’t have one you can skip this and a self-signed certificate will be generated.

#### 11.4.4. Loading keys from a Java Keystore {#Loading_keys_from_a_Java_Keystore}
To add a keypair and certificate stored in a Java Keystore file on the host select `Providers` and choose `java-keystore`from the dropdown. You can change the priority to make sure the new keypair becomes the active keypair.

Fill in the values for `Keystore`, `Keystore Password`, `Key Alias` and `Key Password` and click on `Save`.

#### 11.4.5. Making keys passive {#Making_keys_passive}
Locate the keypair in `Active` or `All` then click on the provider in the `Provider` column. This will take you to the configuration screen for the key provider for the keys. Click on `Active` to turn it `OFF`, then click on `Save`. The keys will no longer be active and can only be used for verifying signatures.

#### 11.4.6. Disabling keys {#Disabling_keys}
Locate the keypair in `Active` or `All` then click on the provider in the `Provider` column. This will take you to the configuration screen for the key provider for the keys. Click on `Enabled` to turn it `OFF`, then click on `Save`. The keys will no longer be enabled.

Alternatively, you can delete the provider from the `Providers` table.

#### 11.4.7. Compromised keys {#Compromised_keys}
Keycloak has the signing keys stored just locally and they are never shared with the client applications, users or other entities. However if you think that your realm signing key was compromised, you should first generate new keypair as described above and then immediatelly remove the compromised keypair.

Then to ensure that client applications won’t accept the tokens signed by the compromised key, you should update and push not-before policy for the realm, which is doable from the admin console. Pushing new policy will ensure that client applications won’t accept the existing tokens signed by the compromised key, but also the client application will be forced to download new keypair from the Keycloak, hence the tokens signed by the compromised key won’t be valid anymore. Note that your REST and confidential clients must have set `Admin URL`, so that Keycloak is able to send them the request about pushed not-before policy.

## 12. Identity Brokering {#Identity_Brokering}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker.adoc)

An Identity Broker is an intermediary service that connects multiple service providers with different identity providers. As an intermediary service, the identity broker is responsible for creating a trust relationship with an external identity provider in order to use its identities to access internal services exposed by service providers.

From a user perspective, an identity broker provides a user-centric and centralized way to manage identities across different security domains or realms. An existing account can be linked with one or more identities from different identity providers or even created based on the identity information obtained from them.

An identity provider is usually based on a specific protocol that is used to authenticate and communicate authentication and authorization information to their users. It can be a social provider such as Facebook, Google or Twitter. It can be a business partner whose users need to access your services. Or it can be a cloud-based identity service that you want to integrate with.

Usually, identity providers are based on the following protocols:

- `SAML v2.0`
- `OpenID Connect v1.0`
- `OAuth v2.0`

In the next sections we’ll see how to configure and use Keycloak as an identity broker, covering some important aspects such as:

- `Social Authentication`
- `OpenID Connect v1.0 Brokering`
- `SAML v2.0 Brokering`
- `Identity Federation`

### 12.1. Brokering Overview {#Brokering_Overview}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/overview.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/overview.adoc)

When using Keycloak as an identity broker, users are not forced to provide their credentials in order to authenticate in a specific realm. Instead, they are presented with a list of identity providers from which they can authenticate.

You can also configure a default identity provider. In this case the user will not be given a choice, but will instead be redirected directly to the default provider.

The following diagram demonstrates the steps involved when using Keycloak to broker an external identity provider:

Identity Broker Flow

![identity broker flow](https://www.keycloak.org/docs/latest/server_admin/images/identity_broker_flow.png)

1. User is not authenticated and requests a protected resource in a client application.
2. The client applications redirects the user to Keycloak to authenticate.
3. At this point the user is presented with the login page where there is a list of identity providers configured in a realm.
4. User selects one of the identity providers by clicking on its respective button or link.
5. Keycloak issues an authentication request to the target identity provider asking for authentication and the user is redirected to the login page of the identity provider. The connection properties and other configuration options for the identity provider were previously set by the administrator in the Admin Console.
6. User provides his credentials or consent in order to authenticate with the identity provider.
7. Upon a successful authentication by the identity provider, the user is redirected back to Keycloak with an authentication response. Usually this response contains a security token that will be used by Keycloak to trust the authentication performed by the identity provider and retrieve information about the user.
8. Now Keycloak is going to check if the response from the identity provider is valid. If valid, it will import and create a new user or just skip that if the user already exists. If it is a new user, Keycloak may ask the identity provider for information about the user if that info doesn’t already exist in the token. This is what we call *identity federation*. If the user already exists Keycloak may ask him to link the identity returned from the identity provider with the existing account. We call this process *account linking*. What exactly is done is configurable and can be specified by setup of [First Login Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker_first_login). At the end of this step, Keycloak authenticates the user and issues its own token in order to access the requested resource in the service provider.
9. Once the user is locally authenticated, Keycloak redirects the user to the service provider by sending the token previously issued during the local authentication.
10. The service provider receives the token from Keycloak and allows access to the protected resource.

There are some variations of this flow that we will talk about later. For instance, instead of presenting a list of identity providers, the client application can request a specific one. Or you can tell Keycloak to force the user to provide additional information before federating his identity.

|      | Different protocols may require different authentication flows. At this moment, all the identity providers supported by Keycloak use a flow just like described above. However, regardless of the protocol in use, user experience should be pretty much the same. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

As you may notice, at the end of the authentication process Keycloak will always issue its own token to client applications. What this means is that client applications are completely decoupled from external identity providers. They don’t need to know which protocol (eg.: SAML, OpenID Connect, OAuth, etc) was used or how the user’s identity was validated. They only need to know about Keycloak.

### 12.2. Default Identity Provider {#Default_Identity_Provider}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/default-provider.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/default-provider.adoc)

It is possible to automatically redirect to a identity provider instead of displaying the login form. To enable this go to the `Authentication` page in the administration console and select the `Browser` flow. Then click on config for the `Identity Provider Redirector` authenticator. Set `Default Identity Provider` to the alias of the identity provider you want to automatically redirect users to.

If the configured default identity provider is not found the login form will be displayed instead.

This authenticator is also responsible for dealing with the `kc_idp_hint` query parameter. See [client suggested identity provider](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_suggested_idp) section for more details.

### 12.3. General Configuration {#General_Configuration}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/configuration.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/configuration.adoc)

The identity broker configuration is all based on identity providers. Identity providers are created for each realm and by default they are enabled for every single application. That means that users from a realm can use any of the registered identity providers when signing in to an application.

In order to create an identity provider click the `Identity Providers` left menu item.

Identity Providers

![identity providers](assets/identity-providers.png)

In the drop down list box, choose the identity provider you want to add. This will bring you to the configuration page for that identity provider type.

Add Identity Provider

![add identity provider](assets/add-identity-provider.png)

Above is an example of configuring a Google social login provider. Once you configure an IDP, it will appear on the Keycloak login page as an option.

IDP login page

![identity provider login page](assets/identity-provider-login-page.png)

- Social

  Social providers allow you to enable social authentication in your realm. Keycloak makes it easy to let users log in to your application using an existing account with a social network. Currently supported providers include: Twitter, Facebook, Google, LinkedIn, Instagram, Microsoft, PayPal, Openshift v3, GitHub, GitLab, Bitbucket, and Stack Overflow.

- Protocol-based

  Protocol-based providers are those that rely on a specific protocol in order to authenticate and authorize users. They allow you to connect to any identity provider compliant with a specific protocol. Keycloak provides support for SAML v2.0 and OpenID Connect v1.0 protocols. It makes it easy to configure and broker any identity provider based on these open standards.

Although each type of identity provider has its own configuration options, all of them share some very common configuration. Regardless of which identity provider you are creating, you’ll see the following configuration options available:

| Configuration          | Description                                                  |
| :--------------------- | :----------------------------------------------------------- |
| Alias                  | The alias is a unique identifier for an identity provider. It is used to reference an identity provider internally. Some protocols such as OpenID Connect require a redirect URI or callback url in order to communicate with an identity provider. In this case, the alias is used to build the redirect URI. Every single identity provider must have an alias. Examples are `facebook`, `google`, `idp.acme.com`, etc. |
| Enabled                | Turn the provider on/off.                                    |
| Hide on Login Page     | When this switch is on, this provider will not be shown as a login option on the login page. Clients can still request to use this provider by using the 'kc_idp_hint' parameter in the URL they use to request a login. |
| Account Linking Only   | When this switch is on, this provider cannot be used to login users and will not be shown as an option on the login page. Existing accounts can still be linked with this provider though. |
| Store Tokens           | Whether or not to store the token received from the identity provider. |
| Stored Tokens Readable | Whether or not users are allowed to retrieve the stored identity provider token. This also applies to the *broker* client-level role *read token*. |
| Trust Email            | If the identity provider supplies an email address this email address will be trusted. If the realm required email validation, users that log in from this IDP will not have to go through the email verification process. |
| GUI Order              | The order number that sorts how the available IDPs are listed on the login page. |
| First Login Flow       | This is the authentication flow that will be triggered for users that log into Keycloak through this IDP for the first time ever. |
| Post Login Flow        | Authentication flow that is triggered after the user finishes logging in with the external identity provider. |

### 12.4. Social Identity Providers {#Social_Identity_Providers}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social-login.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social-login.adoc)

For Internet facing applications, it is quite burdensome for users to have to register at your site to obtain access. It requires them to remember yet another username and password combination. Social identity providers allow you to delegate authentication to a semi-trusted and respected entity where the user probably already has an account. Keycloak provides built-in support for the most common social networks out there, such as Google, Facebook, Twitter, GitHub, LinkedIn, Microsoft and Stack Overflow.

#### 12.4.1. Bitbucket {#Bitbucket}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/bitbucket.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/bitbucket.adoc)

There are a number of steps you have to complete to be able to enable login with Bitbucket.

First, open the `Identity Providers` left menu item and select `Bitbucket` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![bitbucket add identity provider](assets/bitbucket-add-identity-provider.png)

Before you can click `Save`, you must obtain a `Client ID` and `Client Secret` from Bitbucket.

|      | You will use the `Redirect URI` from this page in a later step, which you will provide to Bitbucket when you register Keycloak as a client there. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Add a New App

To enable login with Bitbucket you must first register an application project in [OAuth on Bitbucket Cloud](https://confluence.atlassian.com/bitbucket/oauth-on-bitbucket-cloud-238027431.html).

|      | Bitbucket often changes the look and feel of application registration, so what you see on the Bitbucket site may differ. If in doubt, see the Bitbucket documentation. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

![bitbucket developer applications](https://www.keycloak.org/docs/latest/server_admin/images/bitbucket-developer-applications.png)

Click the `Add consumer` button.

Register App

![bitbucket register app](https://www.keycloak.org/docs/latest/server_admin/images/bitbucket-register-app.png)

Copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and enter it into the Callback URL field on the Bitbucket Add OAuth Consumer page.

On the same page, mark the `Email` and `Read` boxes under `Account` to allow your application to read user email.

Bitbucket App Page

![bitbucket app page](https://www.keycloak.org/docs/latest/server_admin/images/bitbucket-app-page.png)

When you are done registering, click `Save`. This will open the application management page in Bitbucket. Find the client ID and secret from this page so you can enter them into the Keycloak `Add identity provider` page. Click `Save`.

#### 12.4.2. Facebook {#Facebook}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/facebook.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/facebook.adoc)

There are a number of steps you have to complete to be able to enable login with Facebook. First, go to the `Identity Providers` left menu item and select `Facebook` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![facebook add identity provider](assets/facebook-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from Facebook. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to Facebook when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with Facebook you first have to create a project and a client in the [Facebook Developer Console](https://developers.facebook.com/).

|      | Facebook often changes the look and feel of the Facebook Developer Console, so these directions might not always be up to date and the configuration steps might be slightly different. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Once you’ve logged into the console there is a pull down menu in the top right corner of the screen that says `My Apps`. Select the `Add a New App` menu item.

Add a New App

![facebook add new app](https://www.keycloak.org/docs/latest/server_admin/images/facebook-add-new-app.png)

Select the `Website` icon. Click the `Skip and Create App ID` button.

Create a New App ID

![facebook create app id](https://www.keycloak.org/docs/latest/server_admin/images/facebook-create-app-id.png)

The email address and app category are required fields. Once you’re done with that, you will be brought to the dashboard for the application. Click the `Settings` left menu item.

Create a New App ID

![facebook app settings](https://www.keycloak.org/docs/latest/server_admin/images/facebook-app-settings.png)

Click on the `+ Add Platform` button at the end of this page and select the `Website` icon. Copy and paste the `Redirect URI` from the Keycloak `Add identity provider` page into the `Site URL` of the Facebook `Website` settings block.

Specify Website

![facebook app settings website](https://www.keycloak.org/docs/latest/server_admin/images/facebook-app-settings-website.png)

After this it is necessary to make the Facebook app public. Click `App Review` left menu item and switch button to "Yes".

You will need also to obtain the App ID and App Secret from this page so you can enter them into the Keycloak `Add identity provider` page. To obtain this click on the `Dashboard` left menu item and click on `Show` under `App Secret`. Go back to Keycloak and specify those items and finally save your Facebook Identity Provider.

One config option to note on the `Add identity provider` page for Facebook is the `Default Scopes` field. This field allows you to manually specify the scopes that users must authorize when authenticating with this provider. For a complete list of scopes, please take a look at <https://developers.facebook.com/docs/graph-api>. By default, Keycloak uses the following scopes: `email`.

#### 12.4.3. GitHub {#GitHub}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/github.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/github.adoc)

There are a number of steps you have to complete to be able to enable login with GitHub. First, go to the `Identity Providers` left menu item and select `GitHub` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![github add identity provider](assets/github-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from GitHub. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to GitHub when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with GitHub you first have to register an application project in [GitHub Developer applications](https://github.com/settings/developers).

|      | GitHub often changes the look and feel of application registration, so these directions might not always be up to date and the configuration steps might be slightly different. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Add a New App

![github developer applications](https://www.keycloak.org/docs/latest/server_admin/images/github-developer-applications.png)

Click the `Register a new application` button.

Register App

![github register app](https://www.keycloak.org/docs/latest/server_admin/images/github-register-app.png)

You’ll have to copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and enter it into the`Authorization callback URL` field on the GitHub `Register a new OAuth application` page. Once you’ve completed this page you will be brought to the application’s management page.

GitHub App Page

![github app page](https://www.keycloak.org/docs/latest/server_admin/images/github-app-page.png)

You will need to obtain the client ID and secret from this page so you can enter them into the Keycloak `Add identity provider` page. Go back to Keycloak and specify those items.

#### 12.4.4. GitLab {#GitLab}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/gitlab.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/gitlab.adoc)

There are a number of steps you have to complete to be able to enable login with GitLab.

First, go to the `Identity Providers` left menu item and select `GitLab` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![gitlab add identity provider](assets/gitlab-add-identity-provider.png)

Before you can click `Save`, you must obtain a `Client ID` and `Client Secret` from GitLab.

|      | You will use the `Redirect URI` from this page in a later step, which you will provide to GitLab when you register Keycloak as a client there. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

To enable login with GitLab you first have to register an application in [GitLab as OAuth2 authentication service provider](https://docs.gitlab.com/ee/integration/oauth_provider.html).

|      | GitLab often changes the look and feel of application registration, so what you see on the GitLab site may differ. If in doubt, see the GitLab documentation. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Add a New App

![gitlab developer applications](https://www.keycloak.org/docs/latest/server_admin/images/gitlab-developer-applications.png)

Copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and enter it into the Redirect URI field on the GitLab Add new application page.

GitLab App Page

![gitlab app page](https://www.keycloak.org/docs/latest/server_admin/images/gitlab-app-page.png)

When you are done registering, click `Save application`. This will open the application management page in GitLab. Find the client ID and secret from this page so you can enter them into the Keycloak `Add identity provider` page.

To finish, return to Keycloak and enter them. Click `Save`.

#### 12.4.5. Google {#Google}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/google.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/google.adoc)

There are a number of steps you have to complete to be able to enable login with Google. First, go to the `Identity Providers` left menu item and select `Google` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![google add identity provider](assets/google-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from Google. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to Google when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with Google you first have to create a project and a client in the [Google Developer Console](https://console.cloud.google.com/project). Then you need to copy the client ID and secret into the Keycloak Admin Console.

|      | Google often changes the look and feel of the Google Developer Console, so these directions might not always be up to date and the configuration steps might be slightly different. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Let’s see first how to create a project with Google.

Log in to the [Google Developer Console](https://console.cloud.google.com/project).

Google Developer Console

![google developer console](https://www.keycloak.org/docs/latest/server_admin/images/google-developer-console.png)

Click the `Create Project` button. Use any value for `Project name` and `Project ID` you want, then click the `Create`button. Wait for the project to be created (this may take a while). Once created you will be brought to the project’s dashboard.

Dashboard

![google dashboard](https://www.keycloak.org/docs/latest/server_admin/images/google-dashboard.png)

Then navigate to the `APIs & Services` section in the Google Developer Console. On that screen, navigate to `Credentials`administration.

When users log into Google from Keycloak they will see a consent screen from Google which will ask the user if Keycloak is allowed to view information about their user profile. Thus Google requires some basic information about the product before creating any secrets for it. For a new project, you have first to configure `OAuth consent screen`.

For the very basic setup, filling in the Application name is sufficient. You can also set additional details like scopes for Google APIs in this page.

Fill in OAuth consent screen details

![google oauth consent screen](https://www.keycloak.org/docs/latest/server_admin/images/google-oauth-consent-screen.png)

The next step is to create OAuth client ID and client secret. Back in `Credentials` administration, navigate to `Credentials`tab and select `OAuth client ID` under the `Create credentials` button.

Create credentials

![google create credentials](https://www.keycloak.org/docs/latest/server_admin/images/google-create-credentials.png)

You will then be brought to the `Create OAuth client ID` page. Select `Web application` as the application type. Specify the name you want for your client. You’ll also need to copy and paste the `Redirect URI` from the Keycloak `Add Identity Provider` page into the `Authorized redirect URIs` field. After you do this, click the `Create` button.

Create OAuth client ID

![google create oauth id](https://www.keycloak.org/docs/latest/server_admin/images/google-create-oauth-id.png)

After you click `Create` you will be brought to the `Credentials` page. Click on your new OAuth 2.0 Client ID to view the settings of your new Google Client.

Google Client Credentials

![google client credentials](https://www.keycloak.org/docs/latest/server_admin/images/google-client-credentials.png)

You will need to obtain the client ID and secret from this page so you can enter them into the Keycloak `Add identity provider` page. Go back to Keycloak and specify those items.

One config option to note on the `Add identity provider` page for Google is the `Default Scopes` field. This field allows you to manually specify the scopes that users must authorize when authenticating with this provider. For a complete list of scopes, please take a look at <https://developers.google.com/oauthplayground/> . By default, Keycloak uses the following scopes: `openid` `profile` `email`.

If your organization uses the G Suite and you want to restrict access to only members of your organization, you must enter the domain that is used for the G Suite into the `Hosted Domain` field to enable it.

#### 12.4.6. LinkedIn {#LinkedIn}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/linked-in.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/linked-in.adoc)

There are a number of steps you have to complete to be able to enable login with LinkedIn. First, go to the `Identity Providers` left menu item and select `LinkedIn` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![linked in add identity provider](assets/linked-in-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from LinkedIn. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to LinkedIn when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with LinkedIn you first have to create an application in [LinkedIn Developer Network](https://www.linkedin.com/developer/apps).

|      | LinkedIn may change the look and feel of application registration, so these directions may not always be up to date. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Developer Network

![linked in developer network](https://www.keycloak.org/docs/latest/server_admin/images/linked-in-developer-network.png)

Click on the `Create Application` button. This will bring you to the `Create a New Application` Page.

Create App

![linked in create app](https://www.keycloak.org/docs/latest/server_admin/images/linked-in-create-app.png)

Fill in the form with the appropriate values, then click the `Submit` button. This will bring you to the new application’s settings page.

App Settings

![linked in app settings](https://www.keycloak.org/docs/latest/server_admin/images/linked-in-app-settings.png)

Select `r_basicprofile` and `r_emailaddress` in the `Default Application Permissions` section. You’ll have to copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and enter it into the `OAuth 2.0` `Authorized Redirect URLs` field on the LinkedIn app settings page. Don’t forget to click the `Update` button after you do this!

You will then need to obtain the client ID and secret from this page so you can enter them into the Keycloak `Add identity provider` page. Go back to Keycloak and specify those items.

#### 12.4.7. Microsoft {#Microsoft}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/microsoft.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/microsoft.adoc)

There are a number of steps you have to complete to be able to enable login with Microsoft. First, go to the `Identity Providers` left menu item and select `Microsoft` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![microsoft add identity provider](assets/microsoft-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from Microsoft. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to Microsoft when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with Microsoft account you first have to register an OAuth application at Microsoft. Go to the [Microsoft Application Registration](https://account.live.com/developers/applications/create) url.

|      | Microsoft often changes the look and feel of application registration, so these directions might not always be up to date and the configuration steps might be slightly different. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Register Application

![microsoft app register](https://www.keycloak.org/docs/latest/server_admin/images/microsoft-app-register.png)

Enter in the application name and click `Create application`. This will bring you to the application settings page of your new application.

Settings

![microsoft app settings](https://www.keycloak.org/docs/latest/server_admin/images/microsoft-app-settings.png)

You’ll have to copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and add it to the `Redirect URIs`field on the Microsoft application page. Be sure to click the `Add Url` button and `Save` your changes.

Finally, you will need to obtain the Application ID and secret from this page so you can enter them back on the Keycloak `Add identity provider` page. Go back to Keycloak and specify those items.

|      | From November 2018 onwards, Microsoft is removing support for the Live SDK API in favor of the new Microsoft Graph API. The Keycloak Microsoft identity provider has been updated to use the new endpoints so make sure to upgrade to Keycloak version 4.6.0 or later in order to use this provider. Furthermore, client applications registered with Microsoft under "Live SDK applications" will need to be re-registered in the [Microsoft Application Registration](https://account.live.com/developers/applications/create) portal to obtain an application id that is compatible with the Microsoft Graph API. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 12.4.8. OpenShift {#OpenShift}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/openshift.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/openshift.adoc)

|      | OpenShift Online is currently in the developer preview mode. This documentation has been based on on-premise installations and local `minishift` development environment. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

There are a just a few steps you have to complete to be able to enable login with OpenShift. First, go to the `Identity Providers` left menu item and select `OpenShift` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![openshift add identity provider](https://www.keycloak.org/docs/latest/server_admin/images/openshift-add-identity-provider.png)

Registering OAuth client

You can register your client using `oc` command line tool.

```
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

|      | The `name` of your OAuth client. Passed as `client_id` request parameter when making requests to `*<openshift_master>*/oauth/authorize` and `*<openshift_master>*/oauth/token`. |
| ---- | ------------------------------------------------------------ |
|      | `secret` is used as the `client_secret` request parameter.   |
|      | The `redirect_uri` parameter specified in requests to `*<openshift_master>*/oauth/authorize` and `*<openshift_master>*/oauth/token` must be equal to (or prefixed by) one of the URIs in `redirectURIs`. |
|      | The `grantMethod` is used to determine what action to take when this client requests tokens and has not yet been granted access by the user. |

Use client ID and secret defined by `oc create` command to enter them back on the Keycloak `Add identity provider`page. Go back to Keycloak and specify those items.

Please refer to [official OpenShift documentation](https://docs.okd.io/latest/architecture/additional_concepts/authentication.html#oauth) for more detailed guides.

#### 12.4.9. PayPal {#PayPal}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/paypal.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/paypal.adoc)

There are a number of steps you have to complete to be able to enable login with PayPal. First, go to the `Identity Providers` left menu item and select `PayPal` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![paypal add identity provider](assets/paypal-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from PayPal. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to PayPal when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with PayPal you first have to register an application project in [PayPal Developer applications](https://developer.paypal.com/developer/applications).

Add a New App

![paypal developer applications](https://www.keycloak.org/docs/latest/server_admin/images/paypal-developer-applications.png)

Click the `Create App` button.

Register App

![paypal register app](https://www.keycloak.org/docs/latest/server_admin/images/paypal-register-app.png)

You will now be brought to the app settings page.

Do the following changes

- Choose to configure either Sandbox or Live (choose Live if you haven’t enabled the `Target Sandbox` switch on the `Add identity provider` page)
- Copy Client ID and Secret so you can paste them into the Keycloak `Add identity provider` page.
- Scroll down to `App Settings`
- Copy the `Redirect URI` from the Keycloak `Add Identity Provider` page and enter it into the `Return URL` field.
- Check the `Log In with PayPal` checkbox.
- Check the `Full name` checkbox under the personal information section.
- Check the `Email address` checkbox under the address information section.
- Add both a privacy and a user agreement URL pointing to the respective pages on your domain.

#### 12.4.10. Stack Overflow {#Stack_Overflow}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/stack-overflow.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/stack-overflow.adoc)

There are a number of steps you have to complete to be able to enable login with Stack Overflow. First, go to the `Identity Providers` left menu item and select `Stack Overflow` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![stack overflow add identity provider](assets/stack-overflow-add-identity-provider.png)

To enable login with Stack Overflow you first have to register an OAuth application on [StackApps](https://stackapps.com/). Go to [registering your application on Stack Apps](https://stackapps.com/apps/oauth/register) URL and login.

|      | Stack Overflow often changes the look and feel of application registration, so these directions might not always be up to date and the configuration steps might be slightly different. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Register Application

![stack overflow app register](https://www.keycloak.org/docs/latest/server_admin/images/stack-overflow-app-register.png)

Enter in the application name and the OAuth Domain Name of your application and click `Register your Application`. Type in anything you want for the other items.

Settings

![stack overflow app settings](https://www.keycloak.org/docs/latest/server_admin/images/stack-overflow-app-settings.png)

Finally, you will need to obtain the client ID, secret, and key from this page so you can enter them back on the Keycloak `Add identity provider` page. Go back to Keycloak and specify those items.

#### 12.4.11. Twitter {#Twitter}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/social/twitter.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/social/twitter.adoc)

There are a number of steps you have to complete to be able to enable login with Twitter. First, go to the `Identity Providers` left menu item and select `Twitter` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![twitter add identity provider](assets/twitter-add-identity-provider.png)

You can’t click save yet, as you’ll need to obtain a `Client ID` and `Client Secret` from Twitter. One piece of data you’ll need from this page is the `Redirect URI`. You’ll have to provide that to Twitter when you register Keycloak as a client there, so copy this URI to your clipboard.

To enable login with Twtter you first have to create an application in the [Twitter Application Management](https://developer.twitter.com/apps/).

Register Application

![twitter app register](https://www.keycloak.org/docs/latest/server_admin/images/twitter-app-register.png)

Click on the `Create New App` button. This will bring you to the `Create an Application` page.

Register Application

![twitter app create](https://www.keycloak.org/docs/latest/server_admin/images/twitter-app-create.png)

Enter in a Name and Description. The Website can be anything, but cannot have a `localhost` address. For the `Callback URL` you must copy the `Redirect URI` from the Keycloak `Add Identity Provider` page.

|      | You cannot use `localhost` in the `Callback URL`. Instead replace it with `127.0.0.1` if you are trying to test drive Twitter login on your laptop. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

After clicking save you will be brought to the `Details` page.

App Details

![twitter details](https://www.keycloak.org/docs/latest/server_admin/images/twitter-details.png)

Next go to the `Keys and Access Tokens` tab.

Keys and Access Tokens

![twitter keys](https://www.keycloak.org/docs/latest/server_admin/images/twitter-keys.png)

Finally, you will need to obtain the API Key and secret from this page and copy them back into the `Client ID` and `Client Secret` fields on the Keycloak `Add identity provider` page.

### 12.5. OpenID Connect v1.0 Identity Providers {#OpenID_Connect_v1_0_Identity_Providers}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/oidc.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/oidc.adoc)

Keycloak can broker identity providers based on the OpenID Connect protocol. These IDPs must support the [Authorization Code Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc) as defined by the specification in order to authenticate the user and authorize access.

To begin configuring an OIDC provider, go to the `Identity Providers` left menu item and select `OpenID Connect v1.0`from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![oidc add identity provider](assets/oidc-add-identity-provider.png)

The initial configuration options on this page are described in [General IDP Configuration](https://www.keycloak.org/docs/latest/server_admin/index.html#_general-idp-config). You must define the OpenID Connect configuration options as well. They basically describe the OIDC IDP you are communicating with.

| Configuration            | Description                                                  |
| :----------------------- | :----------------------------------------------------------- |
| Authorization URL        | Authorization URL endpoint required by the OIDC protocol.    |
| Token URL                | Token URL endpoint required by the OIDC protocol.            |
| Logout URL               | Logout URL endpoint defined in the OIDC protocol. This value is optional. |
| Backchannel Logout       | Backchannel logout is a background, out-of-band, REST invocation to the IDP to logout the user. Some IDPs can only perform logout through browser redirects as they may only be able to identity sessions via a browser cookie. |
| User Info URL            | User Info URL endpoint defined by the OIDC protocol. This is an endpoint from which user profile information can be downloaded. |
| Client ID                | This realm will act as an OIDC client to the external IDP. Your realm will need an OIDC client ID when using the Authorization Code Flow to interact with the external IDP. |
| Client Secret            | This realm will need a client secret to use when using the Authorization Code Flow. |
| Issuer                   | Responses from the IDP may contain an issuer claim. This config value is optional. If specified, this claim will be validated against the value you provide. |
| Default Scopes           | Space-separated list of OIDC scopes to send with the authentication request. The default is `openid`. |
| Prompt                   | Another optional switch. This is the prompt parameter defined by the OIDC specification. Through it you can force re-authentication and other options. See the specification for more details. |
| Validate Signatures      | Another optional switch. This is to specify if Keycloak will verify the signatures on the external ID Token signed by this identity provider. If this is on, the Keycloak will need to know the public key of the external OIDC identity provider. See below for how to set it up. WARNING: For the performance purposes, Keycloak caches the public key of the external OIDC identity provider. If you think that private key of your identity provider was compromised, it is obviously good to update your keys, but it’s also good to clear the keys cache. See [Clearing the cache](https://www.keycloak.org/docs/latest/server_admin/index.html#_clear-cache)section for more details. |
| Use JWKS URL             | Applicable if `Validate Signatures` is on. If the switch is on, then identity provider public keys will be downloaded from given JWKS URL. This allows great flexibility because new keys will be always re-downloaded when the identity provider generates new keypair. If the switch is off, then public key (or certificate) from the Keycloak DB is used, so whenever the identity provider keypair changes, you will always need to import the new key to the Keycloak DB as well. |
| JWKS URL                 | URL where the identity provider JWK keys are stored. See the [JWK specification](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html) for more details. If you use an external Keycloak as an identity provider, then you can use URL like <http://broker-keycloak:8180/auth/realms/test/protocol/openid-connect/certs> assuming your brokered Keycloak is running on [http://broker-keycloak:8180](http://broker-keycloak:8180/) and it’s realm is `test`. |
| Validating Public Key    | Applicable if `Use JWKS URL` is off. Here is the public key in PEM format that must be used to verify external IDP signatures. |
| Validating Public Key Id | Applicable if `Use JWKS URL` is off. This field specifies ID of the public key in PEM format. This config value is optional. As there is no standard way for computing key ID from key, various external identity providers might use different algorithm from Keycloak. If the value of this field is not specified, the validating public key specified above is used for all requests regardless of key ID sent by external IDP. When set, value of this field serves as key ID used by Keycloak for validating signatures from such providers and must match the key ID specified by the IDP. |

You can also import all this configuration data by providing a URL or file that points to OpenID Provider Metadata (see OIDC Discovery specification). If you are connecting to a Keycloak external IDP, you can import the IDP settings from the url `<root>/auth/realms/{realm-name}/.well-known/openid-configuration`. This link is a JSON document describing metadata about the IDP.

### 12.6. SAML v2.0 Identity Providers {#SAML_v2_0_Identity_Providers}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/saml.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/saml.adoc)

Keycloak can broker identity providers based on the SAML v2.0 protocol.

To begin configuring an SAML v2.0 provider, go to the `Identity Providers` left menu item and select `SAML v2.0` from the `Add provider` drop down list. This will bring you to the `Add identity provider` page.

Add Identity Provider

![saml add identity provider](assets/saml-add-identity-provider.png)

The initial configuration options on this page are described in [General IDP Configuration](https://www.keycloak.org/docs/latest/server_admin/index.html#_general-idp-config). You must define the SAML configuration options as well. They basically describe the SAML IDP you are communicating with.

| Configuration                      | Description                                                  |
| :--------------------------------- | :----------------------------------------------------------- |
| Single Sign-On Service URL         | This is a required field and specifies the SAML endpoint to start the authentication process. If your SAML IDP publishes an IDP entity descriptor, the value of this field will be specified there. |
| Single Logout Service URL          | This is an optional field that specifies the SAML logout endpoint. If your SAML IDP publishes an IDP entity descriptor, the value of this field will be specified there. |
| Backchannel Logout                 | Enable if your SAML IDP supports backchannel logout.         |
| NameID Policy Format               | Specifies the URI reference corresponding to a name identifier format. Defaults to `urn:oasis:names:tc:SAML:2.0:nameid-format:persistent`. |
| HTTP-POST Binding Response         | When this realm responds to any SAML requests sent by the external IDP, which SAML binding should be used? If set to `off`, then the Redirect Binding will be used. |
| HTTP-POST Binding for AuthnRequest | When this realm requests authentication from the external SAML IDP, which SAML binding should be used? If set to `off`, then the Redirect Binding will be used. |
| Want AuthnRequests Signed          | If true, it will use the realm’s keypair to sign requests sent to the external SAML IDP. |
| Signature Algorithm                | If `Want AuthnRequests Signed` is on, then you can also pick the signature algorithm to use. |
| SAML Signature Key Name            | Signed SAML documents sent via POST binding contain identification of signing key in `KeyName` element. This by default contains Keycloak key ID. However various external SAML IDPs might expect a different key name or no key name at all. This switch controls whether `KeyName` contains key ID (option `KEY_ID`), subject from certificate corresponding to the realm key (option `CERT_SUBJECT` - expected for instance by Microsoft Active Directory Federation Services), or that the key name hint is completely omitted from the SAML message (option `NONE`). |
| Force Authentication               | Indicates that the user will be forced to enter their credentials at the external IDP even if they are already logged in. |
| Validate Signature                 | Whether or not the realm should expect that SAML requests and responses from the external IDP to be digitally signed. It is highly recommended you turn this on! |
| Validating X509 Certificate        | The public certificate that will be used to validate the signatures of SAML requests and responses from the external IDP. |

You can also import all this configuration data by providing a URL or file that points to the SAML IDP entity descriptor of the external IDP. If you are connecting to a Keycloak external IDP, you can import the IDP settings from the URL `<root>/auth/realms/{realm-name}/protocol/saml/descriptor`. This link is an XML document describing metadata about the IDP.

You can also import all this configuration data by providing a URL or XML file that points to the entity descriptor of the external SAML IDP you want to connect to.

#### 12.6.1. SP Descriptor {#SP_Descriptor}
Once you create a SAML provider, there is an `EXPORT` button that appears when viewing that provider. Clicking this button will export a SAML SP entity descriptor which you can use to import into the external SP.

This metadata is also available publicly by going to the URL.

```
http[s]://{host:port}/auth/realms/{realm-name}/broker/{broker-alias}/endpoint/descriptor
```

### 12.7. Client-suggested Identity Provider {#Client_suggested_Identity_Provider}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/suggested.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/suggested.adoc)

OIDC applications can bypass the Keycloak login page by specifying a hint on which identity provider they want to use.

This is done by setting the `kc_idp_hint` query parameter in the Authorization Code Flow authorization endpoint.

Keycloak OIDC client adapters also allow you to specify this query parameter when you access a secured resource at the application.

For example:

```
GET /myapplication.com?kc_idp_hint=facebook HTTP/1.1
Host: localhost:8080
```

In this case, it is expected that your realm has an identity provider with an alias `facebook`. If this provider doesn’t exist the login form will be displayed.

If you are using `keycloak.js` adapter, you can also achieve the same behavior:

```
var keycloak = new Keycloak('keycloak.json');

keycloak.createLoginUrl({
        idpHint: 'facebook'
});
```

The `kc_idp_hint` query parameter also allows the client to override the default identity provider if one is configured for the `Identity Provider Redirector` authenticator. The client can also disable the automatic redirecting by setting the `kc_idp_hint` query parameter to an empty value.

### 12.8. Mapping Claims and Assertions {#Mapping_Claims_and_Assertions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/mappers.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/mappers.adoc)

You can import the SAML and OpenID Connect metadata provided by the external IDP you are authenticating with into the environment of the realm. This allows you to extract user profile metadata and other information so that you can make it available to your applications.

Each new user that logs into your realm via an external identity provider will have an entry for them created in the local Keycloak database, based on the metadata from the SAML or OIDC assertions and claims.

If you click on an identity provider listed in the `Identity Providers` page for your realm, you will be brought to the IDPs`Settings` tab. On this page there is also a `Mappers` tab. Click on that tab to start mapping your incoming IDP metadata.

![identity provider mappers](assets/identity-provider-mappers.png)

There is a `Create` button on this page. Clicking on this create button allows you to create a broker mapper. Broker mappers can import SAML attributes or OIDC ID/Access token claims into user attributes and user role mappings.

![identity provider mapper](assets/identity-provider-mapper.png)

Select a mapper from the `Mapper Type` list. Hover over the tooltip to see a description of what the mapper does. The tooltips also describe what configuration information you need to enter. Click `Save` and your new mapper will be added.

For JSON based claims, you can use dot notation for nesting and square brackets to access array fields by index. For example 'contact.address[0].country'.

To investigate the structure of user profile JSON data provided by social providers you can enable the `DEBUG` level logger `org.keycloak.social.user_profile_dump`. This is done in the server’s app-server configuration file (domain.xml or standalone.xml).

### 12.9. Available User Session Data {#Available_User_Session_Data}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/session-data.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/session-data.adoc)

After a user logs in from the external IDP, there is some additional user session note data that Keycloak stores that you can access. This data can be propagated to the client requesting a login via the token or SAML assertion being passed back to it by using an appropriate client mapper.

- identity_provider

  This is the IDP alias of the broker used to perform the login.

- identity_provider_identity

  This is the IDP username of the currently authenticated user. This is often the same as the Keycloak username, but doesn’t necessarily needs to be. For example Keycloak user `john` can be linked to the Facebook user `john123@gmail.com`, so in that case value of user session note will be `john123@gmail.com` .

You can use a [Protocol Mapper](https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers) of type `User Session Note` to propagate this information to your clients.

### 12.10. First Login Flow {#First_Login_Flow}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/first-login-flow.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/first-login-flow.adoc)

When a user logs in through identity brokering some aspects of the user are imported and linked within the realm’s local database. When Keycloak successfully authenticates users through an external identity provider there can be two situations:

- There is already a Keycloak user account imported and linked with the authenticated identity provider account. In this case, Keycloak will just authenticate as the existing user and redirect back to application.
- There is not yet an existing Keycloak user account imported and linked for this external user. Usually you just want to register and import the new account into Keycloak database, but what if there is an existing Keycloak account with the same email? Automatically linking the existing local account to the external identity provider is a potential security hole as you can’t always trust the information you get from the external identity provider.

Different organizations have different requirements when dealing with some of the conflicts and situations listed above. For this, there is a `First Login Flow` option in the IDP settings which allows you to choose a [workflow](https://www.keycloak.org/docs/latest/server_admin/index.html#_authentication-flows) that will be used after a user logs in from an external IDP the first time. By default it points to `first broker login` flow, but you can configure and use your own flow and use different flows for different identity providers.

The flow itself is configured in admin console under `Authentication` tab. When you choose `First Broker Login` flow, you will see what authenticators are used by default. You can re-configure the existing flow. (For example you can disable some authenticators, mark some of them as `required`, configure some authenticators, etc).

You can also create a new authentication flow and/or write your own Authenticator implementations and use it in your flow. See [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more details.

#### 12.10.1. Default First Login Flow {#Default_First_Login_Flow}
Let’s describe the default behavior provided by `First Broker Login` flow.

- Review Profile

  This authenticator might display the profile info page, where the user can review their profile retrieved from an identity provider. The authenticator is configurable. You can set the `Update Profile On First Login` option. When `On`, users will be always presented with the profile page asking for additional information in order to federate their identities. When `missing`, users will be presented with the profile page only if some mandatory information (email, first name, last name) is not provided by the identity provider. If `Off`, the profile page won’t be displayed, unless user clicks in later phase on `Review profile info` link (page displayed in later phase by `Confirm Link Existing Account` authenticator).

- Create User If Unique

  This authenticator checks if there is already an existing Keycloak account with the same email or username like the account from the identity provider. If it’s not, then the authenticator just creates a new local Keycloak account and links it with the identity provider and the whole flow is finished. Otherwise it goes to the next `Handle Existing Account`subflow. If you always want to ensure that there is no duplicated account, you can mark this authenticator as `REQUIRED`. In this case, the user will see the error page if there is an existing Keycloak account and the user will need to link his identity provider account through Account management.

- Confirm Link Existing Account

  On the info page, the user will see that there is an existing Keycloak account with the same email. They can review their profile again and use different email or username (flow is restarted and goes back to `Review Profile` authenticator). Or they can confirm that they want to link their identity provider account with their existing Keycloak account. Disable this authenticator if you don’t want users to see this confirmation page, but go straight to linking identity provider account by email verification or re-authentication.

- Verify Existing Account By Email

  This authenticator is `ALTERNATIVE` by default, so it’s used only if the realm has SMTP setup configured. It will send email to the user, where they can confirm that they want to link the identity provider with their Keycloak account. Disable this if you don’t want to confirm linking by email, but instead you always want users to reauthenticate with their password (and alternatively OTP).

- Verify Existing Account By Re-authentication

  This authenticator is used if email authenticator is disabled or not available (SMTP not configured for realm). It will display a login screen where the user needs to authenticate with his password to link their Keycloak account with the Identity provider. User can also re-authenticate with some different identity provider, which is already linked to their Keycloak account. You can also force users to use OTP. Otherwise it’s optional and used only if OTP is already set for the user account.

#### 12.10.2. Automatically Link Existing First Login Flow {#Automatically_Link_Existing_First_Login_Flow}
|      | The AutoLink authenticator would be dangerous in a generic environment where users can register themselves using arbitrary usernames/email addresses. Do not use this authenticator unless registration of users is carefully curated and usernames/email addresses are assigned, not requested. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

In order to configure a first login flow in which users are automatically linked without being prompted, create a new flow with the following two authenticators:

- Create User If Unique

  This authenticator ensures that unique users are handled. Set the authenticator requirement to "Alternative".

- Automatically Link Brokered Account

  Automatically link brokered identities without any validation with this authenticator. This is useful in an intranet environment of multiple user databases each with overlapping usernames/email addresses, but different passwords, and you want to allow users to use any password without having to validate. This is only reasonable if you manage all internal databases, and usernames/email addresses from one database matching those in another database belong to the same person. Set the authenticator requirement to "Alternative".

|      | The described setup uses two authenticators, and is the simplest one, but it is possible to use other authenticators according to your needs. For example, you can add the Review Profile authenticator to the beginning of the flow if you still want end users to confirm their profile information. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 12.11. Retrieving External IDP Tokens {#Retrieving_External_IDP_Tokens}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/tokens.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/tokens.adoc)

Keycloak allows you to store tokens and responses from the authentication process with the external IDP. For that, you can use the `Store Token` configuration option on the IDP’s settings page.

Application code can retrieve these tokens and responses to pull in extra user information, or to securely invoke requests on the external IDP. For example, an application might want to use the Google token to invoke on other Google services and REST APIs. To retrieve a token for a particular identity provider you need to send a request as follows:

```
GET /auth/realms/{realm}/broker/{provider_alias}/token HTTP/1.1
Host: localhost:8080
Authorization: Bearer <KEYCLOAK ACCESS TOKEN>
```

An application must have authenticated with Keycloak and have received an access token. This access token will need to have the `broker` client-level role `read-token` set. This means that the user must have a role mapping for this role and the client application must have that role within its scope. In this case, given that you are accessing a protected service in Keycloak, you need to send the access token issued by Keycloak during the user authentication. In the broker configuration page you can automatically assign this role to newly imported users by turning on the `Stored Tokens Readable` switch.

These external tokens can be re-established by either logging in again through the provider, or using the client-initiated account linking API.

### 12.12. Identity broker logout {#Identity_broker_logout}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/identity-broker/logout.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/identity-broker/logout.adoc)

When logout from Keycloak is triggered, Keycloak will send a request to the external identity provider that was used to login to Keycloak, and the user will be logged out from this identity provider as well. It is possible to skip this behavior and avoid logout at the external identity provider. See [adapter logout documentation](https://www.keycloak.org/docs/6.0/securing_apps/#_java_adapter_logout) for more details.

## 13. User Session Management {#User_Session_Management}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sessions.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sessions.adoc)

When a user logs into a realm, Keycloak maintains a user session for them and remembers each and every client they have visited within the session. There are a lot of administrative functions that realm admins can perform on these user sessions. They can view login stats for the entire realm and dive down into each client to see who is logged in and where. Admins can logout a user or set of users from the Admin Console. They can revoke tokens and set up all the token and session timeouts there too.

### 13.1. Administering Sessions {#Administering_Sessions}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sessions/administering.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sessions/administering.adoc)

If you go to the `Sessions` left menu item you can see a top level view of the number of sessions that are currently active in the realm.

Sessions

![sessions](assets/sessions.png)

A list of clients is given and how many active sessions there currently are for that client. You can also logout all users in the realm by clicking the `Logout all` button on the right side of this list.

#### 13.1.1. Logout All Limitations {#Logout_All_Limitations}
Any SSO cookies set will now be invalid and clients that request authentication in active browser sessions will now have to re-login. Only certain clients are notified of this logout event, specifically clients that are using the Keycloak OIDC client adapter. Other client types (i.e. SAML) will not receive a backchannel logout request.

It is important to note that any outstanding access tokens are not revoked by clicking `Logout all`. They have to expire naturally. You have to push a [revocation policy](https://www.keycloak.org/docs/latest/server_admin/index.html#_revocation-policy) out to clients, but that also only works with clients using the Keycloak OIDC client adapter.

#### 13.1.2. Application Drilldown {#Application_Drilldown}
On the `Sessions` page, you can also drill down to each client. This will bring you to the `Sessions` tab of that client. Clicking on the `Show Sessions` button there allows you to see which users are logged into that application.

Application Sessions

![application sessions](assets/application-sessions.png)

#### 13.1.3. User Drilldown {#User_Drilldown}
If you go to the `Sessions` tab of an individual user, you can also view the session information.

User Sessions

![user sessions](assets/user-sessions.png)

### 13.2. Revocation Policies {#Revocation_Policies}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sessions/revocation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sessions/revocation.adoc)

If your system is compromised you will want a way to revoke all sessions and access tokens that have been handed out. You can do this by going to the `Revocation` tab of the `Sessions` screen.

Revocation

![revocation](assets/revocation.png)

You can only set a time-based revocation policy. The console allows you to specify a time and date where any session or token issued before that time and date is invalid. The `Set to now` will set the policy to the current time and date. The `Push`button will push this revocation policy to any registered OIDC client that has the Keycloak OIDC client adapter installed.

### 13.3. Session and Token Timeouts {#Session_and_Token_Timeouts}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sessions/timeouts.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sessions/timeouts.adoc)

Keycloak gives you fine grain control of session, cookie, and token timeouts. This is all done on the `Tokens`tab in the `Realm Settings` left menu item.

Tokens Tab

![tokens tab](assets/tokens-tab.png)

Let’s walk through each of the items on this page.

| Configuration                           | Description                                                  |
| :-------------------------------------- | :----------------------------------------------------------- |
| Revoke Refresh Token                    | For OIDC clients that are doing the refresh token flow, this flag, if on, will revoke that refresh token and issue another with the request that the client has to use. This basically means that refresh tokens have a one time use. |
| SSO Session Idle                        | Also pertains to OIDC clients. If the user is not active for longer than this timeout, the user session will be invalidated. How is idle time checked? A client requesting authentication will bump the idle timeout. Refresh token requests will also bump the idle timeout. There is a small window of time that is always added to the idle timeout before the session is actually invalidated (See note below). |
| SSO Session Max                         | Maximum time before a user session is expired and invalidated. This is a hard number and time. It controls the maximum time a user session can remain active, regardless of activity. |
| SSO Session Idle Remember Me            | Same as the standard SSO Session Idle configuration but specific to logins with remember me enabled. It allows for the specification of longer session idle timeouts when remember me is selected during the login process. It is an optional configuration and if not set to a value bigger than 0 it uses the same idle timeout set in the SSO Session Idle configuration. |
| SSO Session Max Remember Me             | Same as the standard SSO Session Max but specific to logins with remember me enabled. It allows for the specification of longer lived sessions when remember me is selected during the login process. It is an optional configuration and if not set to a value bigger than 0 it uses the same session lifespan set in the SSO Session Max configuration. |
| Offline Session Idle                    | For [offline access](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access), this is the time the session is allowed to remain idle before the offline token is revoked. There is a small window of time that is always added to the idle timeout before the session is actually invalidated (See note below). |
| Offline Session Max Limited             | For [offline access](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access), if this flag is on, Offline Session Max is enabled to control the maximum time the offline token can remain active, regardless of activity. |
| Offline Session Max                     | For [offline access](https://www.keycloak.org/docs/latest/server_admin/index.html#_offline-access), this is the maximum time before the corresponding offline token is revoked. This is a hard number and time. It controls the maximum time the offline token can remain active, regardless of activity. |
| Access Token Lifespan                   | When an OIDC access token is created, this value affects the expiration. |
| Access Token Lifespan For Implicit Flow | With the Implicit Flow no refresh token is provided. For this reason there’s a separate timeout for access tokens created with the Implicit Flow. |
| Client login timeout                    | This is the maximum time that a client has to finish the Authorization Code Flow in OIDC. |
| Login timeout                           | Total time a login must take. If authentication takes longer than this time then the user will have to start the authentication process over. |
| Login action timeout                    | Maximum time a user can spend on any one page in the authentication process. |
| User-Initiated Action Lifespan          | Maximum time before an action permit sent by a user (e.g. forgot password e-mail) is expired. This value is recommended to be short because it is expected that the user would react to self-created action quickly. |
| Default Admin-Initiated Action Lifespan | Maximum time before an action permit sent to a user by an admin is expired. This value is recommended to be long to allow admins send e-mails for users that are currently offline. The default timeout can be overridden right before issuing the token. |
| Override User-Initiated Action Lifespan | Permits the possibility of having independent timeouts per operation (e.g. e-mail verification, forgot password, user actions and Identity Provider E-mail Verification). This field is non mandatory and if nothing is specified it defaults to the value configured at *User-Initiated Action Lifespan*. |

|      | For idle timeouts, there is a small window of time (2 minutes) during which the session is kept unexpired. For example, when you have timeout set to 30 minutes, it will be actually 32 minutes before the session is expired. This is needed for some corner-case scenarios in cluster and cross-datacenter environments, in cases where the token was refreshed on one cluster node for a very short time before the expiration and the other cluster nodes would in the meantime incorrectly consider the session as expired, because they had not yet received the message about successful refresh from the node which did the refresh. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 13.4. Offline Access {#Offline_Access}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/sessions/offline.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/sessions/offline.adoc)

Offline access is a feature described in [OpenID Connect specification](https://openid.net/specs/openid-connect-core-1_0.html#OfflineAccess) . The idea is that during login, your client application will request an Offline token instead of a classic Refresh token. The application can save this offline token in a database or on disk and can use it later even if user is logged out. This is useful if your application needs to do some "offline" actions on behalf of user even when the user is not online. An example is a periodic backup of some data every night.

Your application is responsible for persisting the offline token in some storage (usually a database) and then using it to manually retrieve new access token from Keycloak server.

The difference between a classic Refresh token and an Offline token is, that an offline token will never expire by default and is not subject of `SSO Session Idle timeout` and `SSO Session Max lifespan` . The offline token is valid even after a user logout or server restart. However by default you do need to use the offline token for a refresh token action at least once per 30 days (this value, `Offline Session Idle timeout`, can be changed in the administration console in the `Tokens` tab under `Realm Settings`). Moreover, if you enable the option `Offline Session Max Limited`, then the offline token expires after 60 days regardless of using the offline token for a refresh token action (this value, `Offline Session Max lifespan`, can also be changed in the administration console in the Tokens tab under Realm Settings). Also if you enable the option `Revoke refresh tokens`, then each offline token can be used just once. So after refresh, you always need to store the new offline token from refresh response into your DB instead of the previous one.

Users can view and revoke offline tokens that have been granted by them in the [User Account Service](https://www.keycloak.org/docs/latest/server_admin/index.html#_account-service). The admin user can revoke offline tokens for individual users in admin console in the `Consents` tab of a particular user. The admin can also view all the offline tokens issued in the `Offline Access` tab of each client. Offline tokens can also be revoked by setting a [revocation policy](https://www.keycloak.org/docs/latest/server_admin/index.html#_revocation-policy).

To be able to issue an offline token, users need to have the role mapping for the realm-level role `offline_access`. Clients also need to have that role in their scope. Finally, the client needs to have an `offline_access` client scope added as an `Optional client scope` to it, which is done by default.

The client can request an offline token by adding the parameter `scope=offline_access` when sending authorization request to Keycloak. The Keycloak OIDC client adapter automatically adds this parameter when you use it to access secured URL of your application (i.e. http://localhost:8080/customer-portal/secured?scope=offline_access). The Direct Access Grant and Service Accounts also support offline tokens if you include `scope=offline_access` in the body of the authentication request.

## 14. User Storage Federation {#User_Storage_Federation}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/user-federation.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/user-federation.adoc)

Many companies have existing user databases that hold information about users and their passwords or other credentials. In many cases, it is just not possible to migrate off of those existing stores to a pure Keycloak deployment. Keycloak can federate existing external user databases. Out of the box we have support for LDAP and Active Directory. You can also code your own extension for any custom user databases you might have using our User Storage SPI.

The way it works is that when a user logs in, Keycloak will look into its own internal user store to find the user. If it can’t find it there it will iterate over every User Storage provider you have configured for the realm until it finds a match. Data from the external store is mapped into a common user model that is consumed by the Keycloak runtime. This common user model can then be mapped to OIDC token claims and SAML assertion attributes.

External user databases rarely have every piece of data needed to support all the features that Keycloak has. In this case, the User Storage Provider can opt to store some things locally in the Keycloak user store. Some providers even import the user locally and sync periodically with the external store. All this depends on the capabilities of the provider and how its configured. For example, your external user store may not support OTP. Depending on the provider, this OTP can be handled and stored by Keycloak.

### 14.1. Adding a Provider {#Adding_a_Provider}
To add a storage provider go to the `User Federation` left menu item in the Admin Console.

User Federation

![user federation](assets/user-federation.png)

On the center, there is an `Add Provider` list box. Choose the provider type you want to add and you will be brought to the configuration page of that provider.

### 14.2. Dealing with Provider Failures {#Dealing_with_Provider_Failures}
If a User Storage Provider fails, that is, if your LDAP server is down, you may have trouble logging in and may not be able to view users in the admin console. Keycloak does not catch failures when using a Storage Provider to lookup a user. It will abort the invocation. So, if you have a Storage Provider with a higher priority that fails during user lookup, the login or user query will fail entirely with an exception and abort. It will not fail over to the next configured provider.

The local Keycloak user database is always searched first to resolve users before any LDAP or custom User Storage Provider. You may want to consider creating an admin account that is stored in the local Keycloak user database just in case any problems come up in connecting to your LDAP and custom back ends.

Each LDAP and custom User Storage Provider has an `enable` switch on its admin console page. Disabling the User Storage Provider will skip the provider when doing user queries so that you can view and login with users that might be stored in a different provider with lower priority. If your provider is using an `import` strategy and you disable it, imported users are still available for lookup, but only in read only mode. You will not be able to modify these users until you re-enable the provider.

The reason why Keycloak does not fail over if a Storage Provider lookup fails is that user databases often have duplicate usernames or duplicate emails between them. This can cause security issues and unforeseen problems as the user may be loaded from one external store when the admin is expecting the user to be loaded from another.

### 14.3. LDAP and Active Directory {#LDAP_and_Active_Directory}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/user-federation/ldap.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/user-federation/ldap.adoc)

Keycloak comes with a built-in LDAP/AD provider. It is possible to federate multiple different LDAP servers in the same Keycloak realm. You can map LDAP user attributes into the Keycloak common user model. By default, it maps username, email, first name, and last name, but you are free to configure additional [mappings](https://www.keycloak.org/docs/latest/server_admin/index.html#_ldap_mappers). The LDAP provider also supports password validation via LDAP/AD protocols and different storage, edit, and synchronization modes.

To configure a federated LDAP store go to the Admin Console. Click on the `User Federation` left menu option. When you get to this page there is an `Add Provider` select box. You should see *ldap* within this list. Selecting *ldap* will bring you to the LDAP configuration page.

#### 14.3.1. Storage Mode {#Storage_Mode}
By default, Keycloak will import users from LDAP into the local Keycloak user database. This copy of the user is either synchronized on demand, or through a periodic background task. The one exception to this is passwords. Passwords are not imported and password validation is delegated to the LDAP server. The benefits to this approach is that all Keycloak features will work as any extra per-user data that is needed can be stored locally. This approach also reduces load on the LDAP server as uncached users are loaded from the Keycloak database the 2nd time they are accessed. The only load your LDAP server will have is password validation. The downside to this approach is that when a user is first queried, this will require a Keycloak database insert. The import will also have to be synchronized with your LDAP server as needed.

Alternatively, you can choose not to import users into the Keycloak user database. In this case, the common user model that the Keycloak runtime uses is backed only by the LDAP server. This means that if LDAP doesn’t support a piece of data that a Keycloak feature needs that feature will not work. The benefit to this approach is that you do not have the overhead of importing and synchronizing a copy of the LDAP user into the Keycloak user database.

This storage mode is controled by the `Import Users` switch. Set to `On` to import users.

#### 14.3.2. Edit Mode {#Edit_Mode}
Users, through the [User Account Service](https://www.keycloak.org/docs/latest/server_admin/index.html#_account-service), and admins through the Admin Console have the ability to modify user metadata. Depending on your setup you may or may not have LDAP update privileges. The `Edit Mode` configuration option defines the edit policy you have with your LDAP store.

- READONLY

  Username, email, first name, last name, and other mapped attributes will be unchangeable. Keycloak will show an error anytime anybody tries to update these fields. Also, password updates will not be supported.

- WRITABLE

  Username, email, first name, last name, and other mapped attributes and passwords can all be updated and will be synchronized automatically with your LDAP store.

- UNSYNCED

  Any changes to username, email, first name, last name, and passwords will be stored in Keycloak local storage. It is up to you to figure out how to synchronize back to LDAP. This allows Keycloak deployments to support updates of user metadata on a read-only LDAP server. This option only applies when you are importing users from LDAP into the local Keycloak user database.

#### 14.3.3. Other config options {#Other_config_options}
- Console Display Name

  Name used when this provider is referenced in the admin console

- Priority

  The priority of this provider when looking up users or adding a user.

- Sync Registrations

  Does your LDAP support adding new users? Click this switch if you want new users created by Keycloak in the admin console or the registration page to be added to LDAP.

- Allow Kerberos authentication

  Enable Kerberos/SPNEGO authentication in realm with users data provisioned from LDAP. More info in [Kerberos section](https://www.keycloak.org/docs/latest/server_admin/index.html#_kerberos).

- Other options

  The rest of the configuration options should be self explanatory. You can mouseover the tooltips in Admin Console to see some more details about them.

#### 14.3.4. Connect to LDAP over SSL {#Connect_to_LDAP_over_SSL}
When you configure a secured connection URL to your LDAP store(for example `ldaps://myhost.com:636` ), Keycloak will use SSL for the communication with LDAP server. The important thing is to properly configure a truststore on the Keycloak server side, otherwise Keycloak can’t trust the SSL connection to LDAP.

The global truststore for the Keycloak can be configured with the Truststore SPI. Please check out the [Server Installation and Configuration Guide](https://www.keycloak.org/docs/6.0/server_installation/) for more detail. If you don’t configure the truststore SPI, the truststore will fallback to the default mechanism provided by Java (either the file provided by system property `javax.net.ssl.trustStore` or the cacerts file from the JDK if the system property is not set).

There is a configuration property `Use Truststore SPI` in the LDAP federation provider configuration, where you can choose whether the Truststore SPI is used. By default, the value is `Only for ldaps`, which is fine for most deployments. The Truststore SPI will only be used if the connection to LDAP starts with `ldaps`.

#### 14.3.5. Sync of LDAP users to Keycloak {#Sync_of_LDAP_users_to_Keycloak}
If you have import enabled, the LDAP Provider will automatically take care of synchronization (import) of needed LDAP users into the Keycloak local database. As users log in, the LDAP provider will import the LDAP user into the Keycloak database and then authenticate against the LDAP password. This is the only time users will be imported. If you go to the `Users` left menu item in the Admin Console and click the `View all users` button, you will only see those LDAP users that have been authenticated at least once by Keycloak. It is implemented this way so that admins don’t accidentally try to import a huge LDAP DB of users.

If you want to sync all LDAP users into the Keycloak database, you may configure and enable the `Sync Settings` of the LDAP provider you configured. There are 2 types of synchronization:

- Periodic Full sync

  This will synchronize all LDAP users into Keycloak DB. Those LDAP users, which already exist in Keycloak and were changed in LDAP directly will be updated in Keycloak DB (For example if user `Mary Kelly` was changed in LDAP to `Mary Smith`).

- Periodic Changed users sync

  When syncing occurs, only those users that were created or updated after the last sync will be updated and/or imported.

The best way to handle syncing is to click the `Synchronize all users` button when you first create the LDAP provider, then set up a periodic sync of changed users. The configuration page for your LDAP Provider has several options to support you.

#### 14.3.6. LDAP Mappers {#LDAP_Mappers}
LDAP mappers are `listeners`, which are triggered by the LDAP Provider at various points, provide another extension point to LDAP integration. They are triggered when a user logs in via LDAP and needs to be imported, during Keycloak initiated registration, or when a user is queried from the Admin Console. When you create an LDAP Federation provider, Keycloak will automatically provide set of built-in `mappers` for this provider. You are free to change this set and create a new mapper or update/delete existing ones.

- User Attribute Mapper

  This allows you to specify which LDAP attribute is mapped to which attribute of Keycloak user. So, for example, you can configure that LDAP attribute `mail` to the attribute `email` in the Keycloak database. For this mapper implementation, there is always a one-to-one mapping (one LDAP attribute is mapped to one Keycloak attribute)

- FullName Mapper

  This allows you to specify that the full name of the user, which is saved in some LDAP attribute (usually `cn` ) will be mapped to `firstName` and `lastname` attributes in the Keycloak database. Having `cn` to contain full name of user is a common case for some LDAP deployments.

- Role Mapper

  This allows you to configure role mappings from LDAP into Keycloak role mappings. One Role mapper can be used to map LDAP roles (usually groups from a particular branch of LDAP tree) into roles corresponding to either realm roles or client roles of a specified client. It’s not a problem to configure more Role mappers for the same LDAP provider. So for example you can specify that role mappings from groups under `ou=main,dc=example,dc=org` will be mapped to realm role mappings and role mappings from groups under `ou=finance,dc=example,dc=org` will be mapped to client role mappings of client `finance` .

- Hardcoded Role Mapper

  This mapper will grant a specified Keycloak role to each Keycloak user linked with LDAP.

- Group Mapper

  This allows you to configure group mappings from LDAP into Keycloak group mappings. Group mapper can be used to map LDAP groups from a particular branch of an LDAP tree into groups in Keycloak. It will also propagate user-group mappings from LDAP into user-group mappings in Keycloak.

- MSAD User Account Mapper

  This mapper is specific to Microsoft Active Directory (MSAD). It’s able to tightly integrate the MSAD user account state into the Keycloak account state (account enabled, password is expired etc). It’s using the `userAccountControl` and `pwdLastSet` LDAP attributes. (both are specific to MSAD and are not LDAP standard). For example if `pwdLastSet` is `0`, the Keycloak user is required to update their password and there will be an UPDATE_PASSWORD required action added to the user. If `userAccountControl` is `514` (disabled account) the Keycloak user is disabled as well.

By default, there are User Attribute mappers that map basic Keycloak user attributes like username, firstname, lastname, and email to corresponding LDAP attributes. You are free to extend these and provide additional attribute mappings. Admin console provides tooltips, which should help with configuring the corresponding mappers.

#### 14.3.7. Password Hashing {#Password_Hashing}
When the password of user is updated from Keycloak and sent to LDAP, it is always sent in plain-text. This is different from updating the password to built-in Keycloak database, when the hashing and salting is applied to the password before it is sent to DB. In the case of LDAP, the Keycloak relies on the LDAP server to provide hashing and salting of passwords.

Most of LDAP servers (Microsoft Active Directory, RHDS, FreeIPA) provide this by default. Some others (OpenLDAP, ApacheDS) may store the passwords in plain-text by default and you may need to explicitly enable password hashing for them. See the documentation of your LDAP server more details.

### 14.4. SSSD and FreeIPA Identity Management Integration {#SSSD_and_FreeIPA_Identity_Management_Integration}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/user-federation/sssd.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/user-federation/sssd.adoc)

Keycloak also comes with a built-in [SSSD](https://fedoraproject.org/wiki/Features/SSSD) (System Security Services Daemon) plugin. SSSD is part of the latest Fedora or Red Hat Enterprise Linux and provides access to multiple identity and authentication providers. It provides benefits such as failover and offline support. To see configuration options and for more information see [the Red Hat Enterprise Linux Identity Management documentation](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/system-level_authentication_guide/sssd).

SSSD also integrates with the FreeIPA identity management (IdM) server, providing authentication and access control. For Keycloak, we benefit from this integration authenticating against PAM services and retrieving user data from SSSD. For more information about using Red Hat Identity Management in Linux environments, see [the Red Hat Enterprise Linux Identity Management documentation](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/linux_domain_identity_authentication_and_policy_guide/index).

![keycloak sssd freeipa integration overview](assets/keycloak-sssd-freeipa-integration-overview.png)

Most of the communication between Keycloak and SSSD occurs through read-only D-Bus interfaces. For this reason, the only way to provision and update users is to use the FreeIPA/IdM administration interface. By default, like the LDAP federation provider, it is set up only to import username, email, first name, and last name.

|      | Groups and roles are automatically registered, but not synchronized, so any changes made by the Keycloak administrator directly in Keycloak is not synchronized with SSSD. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Information on how to configure the FreeIPA/IdM server follows.

#### 14.4.1. FreeIPA/IdM Server {#FreeIPA_IdM_Server}
As a matter of simplicity, a [FreeIPA Docker image](https://hub.docker.com/r/freeipa/freeipa-server/) already available is used. To set up a server, see the [FreeIPA documentation](https://www.freeipa.org/page/Quick_Start_Guide).

Running a FreeIPA server with Docker requires this command:

```
docker run --name freeipa-server-container -it \
-h server.freeipa.local -e PASSWORD=YOUR_PASSWORD \
-v /sys/fs/cgroup:/sys/fs/cgroup:ro \
-v /var/lib/ipa-data:/data:Z freeipa/freeipa-server
```

The parameter `-h` with `server.freeipa.local` represents the FreeIPA/IdM server hostname. Be sure to change `YOUR_PASSWORD` to a password of your choosing.

After the container starts, change `/etc/hosts` to:

```
x.x.x.x     server.freeipa.local
```

If you do not make this change, you must set up a DNS server.

So that the SSSD federation provider is started and running on Keycloak you must enroll your Linux machine in the IPA domain:

```
ipa-client-install --mkhomedir -p admin -w password
```

To ensure that everything is working as expected, on the client machine, run:

```
kinit admin
```

You should be prompted for the password. After that, you can add users to the IPA server using this command:

```
$ ipa user-add john --first=John --last=Smith --email=john@smith.com --phone=042424242 --street="Testing street" \      --city="Testing city" --state="Testing State" --postalcode=0000000000
```

#### 14.4.2. SSSD and D-Bus {#SSSD_and_D_Bus}
As mentioned previously, the federation provider obtains the data from SSSD using D-BUS and authentication occurs using PAM.

First, you have to install the sssd-dbus RPM, which allows information from SSSD to be transmitted over the system bus.

```
$ sudo yum install sssd-dbus
```

You must run the provisioning script available from the Keycloak distribution:

```
$ bin/federation-sssd-setup.sh
```

This script makes the necessary changes to `/etc/sssd/sssd.conf`:

```
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

Also, a `keycloak` file is included under `/etc/pam.d/`:

```
auth    required   pam_sss.so
account required   pam_sss.so
```

Ensure everything is working as expected by running `dbus-send`:

```
sudo dbus-send --print-reply --system --dest=org.freedesktop.sssd.infopipe /org/freedesktop/sssd/infopipe org.freedesktop.sssd.infopipe.GetUserGroups string:john
```

You should be able to see the user’s group. If this command returns a timeout or an error, it means that the federation provider will also not be able to retrieve anything on Keycloak.

Most of the time this occurs because the machine was not enrolled in the FreeIPA IdM server or you do not have permission to access the SSSD service.

If you do not have permission, ensure that the user running Keycloak is included in the `/etc/sssd/sssd.conf` file in the following section:

```
[ifp]
allowed_uids = root, your_username
```

#### 14.4.3. Enabling the SSSD Federation Provider {#Enabling_the_SSSD_Federation_Provider}
Keycloak uses DBus-Java to communicate at a low level with D-Bus, which depends on the [Unix Sockets Library](http://www.matthew.ath.cx/projects/java/).

An RPM for this library can be found in [this repository](https://github.com/keycloak/libunix-dbus-java/releases). Before installing it, be sure to check the RPM signature:

```
$ rpm -K libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm
libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm:
  Header V4 RSA/SHA256 Signature, key ID 84dc9914: OK
  Header SHA1 digest: OK (d17bb7ebaa7a5304c1856ee4357c8ba4ec9c0b89)
  V4 RSA/SHA256 Signature, key ID 84dc9914: OK
  MD5 digest: OK (770c2e68d052cb4a4473e1e9fd8818cf)
$ sudo yum install libunix-dbus-java-0.8.0-1.fc24.x86_64.rpm
```

For authentication with PAM Keycloak uses JNA. Be sure you have this package installed:

```
$ sudo yum install jna
```

Use `sssctl user-checks` command to validate your setup:

```
$ sudo sssctl user-checks admin -s keycloak
```

### 14.5. Configuring a Federated SSSD Store {#Configuring_a_Federated_SSSD_Store}
After installation, you need to configure a federated SSSD store.

To configure a federated SSSD store, complete the following steps:

1. Navigate to the Administration Console.
2. From the left menu, select **User Federation.**
3. From the **Add Provider** dropdown list, select **sssd.** The sssd configuration page opens.
4. Click **Save**.

Now you can authenticate against Keycloak using FreeIPA/IdM credentials.

### 14.6. Custom Providers {#Custom_Providers}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/user-federation/custom.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/user-federation/custom.adoc)

Keycloak does have an SPI for User Storage Federation that you can use to write your own custom providers. You can find documentation for this in our [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/).

## 15. Auditing and Events {#Auditing_and_Events}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/events.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/events.adoc)

Keycloak provides a rich set of auditing capabilities. Every single login action can be recorded and stored in the database and reviewed in the Admin Console. All admin actions can also be recorded and reviewed. There is also a Listener SPI with which plugins can listen for these events and perform some action. Built-in listeners include a simple log file and the ability to send an email if an event occurs.

### 15.1. Login Events {#Login_Events}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/events/login.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/events/login.adoc)

Login events occur for things like when a user logs in successfully, when somebody enters in a bad password, or when a user account is updated. Every single event that happens to a user can be recorded and viewed. By default, no events are stored or viewed in the Admin Console. Only error events are logged to the console and the server’s log file. To start persisting you’ll need to enable storage. Go to the `Events` left menu item and select the `Config` tab.

Event Configuration

![login events config](assets/login-events-config.png)

To start storing events you’ll need to turn the `Save Events` switch to on under the `Login Events Settings`.

Save Events

![login events settings](assets/login-events-settings.png)

The `Saved Types` field allows you to specify which event types you want to store in the event store. The `Clear events`button allows you to delete all the events in the database. The `Expiration` field allows you to specify how long you want to keep events stored. Once you’ve enabled storage of login events and decided on your settings, don’t forget to click the `Save`button on the bottom of this page.

To view events, go to the `Login Events` tab.

Login Events

![login events](assets/login-events.png)

As you can see, there’s a lot of information stored and, if you are storing every event, there are a lot of events stored for each login action. The `Filter` button on this page allows you to filter which events you are actually interested in.

Login Event Filter

![login events filter](assets/login-events-filter.png)

In this screenshot, we’re filtering only `Login` events. Clicking the `Update` button runs the filter.

#### 15.1.1. Event Types {#Event_Types}
Login events:

- Login - A user has logged in.
- Register - A user has registered.
- Logout - A user has logged out.
- Code to Token - An application/client has exchanged a code for a token.
- Refresh Token - An application/client has refreshed a token.

Account events:

- Social Link - An account has been linked to a social provider.
- Remove Social Link - A social provider has been removed from an account.
- Update Email - The email address for an account has changed.
- Update Profile - The profile for an account has changed.
- Send Password Reset - A password reset email has been sent.
- Update Password - The password for an account has changed.
- Update TOTP - The TOTP settings for an account have changed.
- Remove TOTP - TOTP has been removed from an account.
- Send Verify Email - An email verification email has been sent.
- Verify Email - The email address for an account has been verified.

For all events there is a corresponding error event.

#### 15.1.2. Event Listener {#Event_Listener}
Event listeners listen for events and perform an action based on that event. There are two built-in listeners that come with Keycloak: Logging Event Listener and Email Event Listener.

The Logging Event Listener writes to a log file whenever an error event occurs and is enabled by default. Here’s an example log message:

```
11:36:09,965 WARN  [org.keycloak.events] (default task-51) type=LOGIN_ERROR, realmId=master,
                    clientId=myapp,
                    userId=19aeb848-96fc-44f6-b0a3-59a17570d374, ipAddress=127.0.0.1,
                    error=invalid_user_credentials, auth_method=openid-connect, auth_type=code,
                    redirect_uri=http://localhost:8180/myapp,
                    code_id=b669da14-cdbb-41d0-b055-0810a0334607, username=admin
```

This logging is very useful if you want to use a tool like Fail2Ban to detect if there is a hacker bot somewhere that is trying to guess user passwords. You can parse the log file for `LOGIN_ERROR` and pull out the IP Address. Then feed this information into Fail2Ban so that it can help prevent attacks.

The Email Event Listener sends an email to the user’s account when an event occurs. The Email Event Listener only supports the following events at the moment:

- Login Error
- Update Password
- Update TOTP
- Remove TOTP

To enable the Email Listener go to the `Config` tab and click on the `Event Listeners` field. This will show a drop down list box where you can select email.

You can exclude one or more events by editing the `standalone.xml`, `standalone-ha.xml`, or `domain.xml` that comes with your distribution and adding for example:

```
<spi name="eventsListener">
  <provider name="email" enabled="true">
    <properties>
      <property name="exclude-events" value="[&quot;UPDATE_TOTP&quot;,&quot;REMOVE_TOTP&quot;]"/>
    </properties>
  </provider>
</spi>
```

See the [Server Installation and Configuration Guide](https://www.keycloak.org/docs/6.0/server_installation/) for more details on where the `standalone.xml`, `standalone-ha.xml`, or `domain.xml` file lives.

### 15.2. Admin Events {#Admin_Events}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/events/admin.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/events/admin.adoc)

Any action an admin performs within the admin console can be recorded for auditing purposes. The Admin Console performs administrative functions by invoking on the Keycloak REST interface. Keycloak audits these REST invocations. The resulting events can then be viewed in the Admin Console.

To enable auditing of Admin actions, go to the `Events` left menu item and select the `Config` tab.

Event Configuration

![login events config](assets/login-events-config.png)

In the `Admin Events Settings` section, turn on the `Save Events` switch.

Admin Event Configuration

![admin events settings](assets/admin-events-settings.png)

The `Include Representation` switch will include any JSON document that is sent through the admin REST API. This allows you to view exactly what an admin has done, but can lead to a lot of information stored in the database. The `Clear admin events` button allows you to wipe out the current information stored.

To view the admin events go to the `Admin Events` tab.

Admin Events

![admin events](assets/admin-events.png)

If the `Details` column has a `Representation` box, you can click on that to view the JSON that was sent with that operation.

Admin Representation

![admin events representation](assets/admin-events-representation.png)

You can also filter for the events you are interested in by clicking the `Filter` button.

Admin Event Filter

![admin events filter](assets/admin-events-filter.png)

## 16. Export and Import {#Export_and_Import}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/export-import.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/export-import.adoc)

Keycloak has the ability to export and import the entire database. This can be especially useful if you want to migrate your whole Keycloak database from one environment to another or migrate to a different database (for example from MySQL to Oracle). Export and import is triggered at server boot time and its parameters are passed in via Java system properties. It is important to note that because import and export happens at server startup, no other actions should be taken on the server or the database while this happens.

You can export/import your database either to:

- Directory on local filesystem
- Single JSON file on your filesystem

When importing using the directory strategy, note that the files need to follow the naming convention specified below. If you are importing files which were previously exported, the files already follow this convention.

- <REALM_NAME>-realm.json, such as "acme-roadrunner-affairs-realm.json" for the realm named "acme-roadrunner-affairs"
- <REALM_NAME>-users-<INDEX>.json, such as "acme-roadrunner-affairs-users-0.json" for the first users file of the realm named "acme-roadrunner-affairs"

If you export to a directory, you can also specify the number of users that will be stored in each JSON file.

|      | If you have bigger amount of users in your database (500 or more), it’s highly recommended to export into directory rather than to single file. Exporting into single file may lead to the very big file. Also the directory provider is using separate transaction for each "page" (file with users), which leads to much better performance. Default count of users per file (and transaction) is 50, which showed us best performance, but you have possibility to override (See below). Exporting to single file is using one transaction per whole export and one per whole import, which results in bad performance with large amount of users. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

To export into unencrypted directory you can use:

```
bin/standalone.sh -Dkeycloak.migration.action=export
-Dkeycloak.migration.provider=dir -Dkeycloak.migration.dir=<DIR TO EXPORT TO>
```

And similarly for import just use `-Dkeycloak.migration.action=import` instead of `export` . To export into single JSON file you can use:

```
bin/standalone.sh -Dkeycloak.migration.action=export
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=<FILE TO EXPORT TO>
```

Here’s an example of importing:

```
bin/standalone.sh -Dkeycloak.migration.action=import
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=<FILE TO IMPORT>
-Dkeycloak.migration.strategy=OVERWRITE_EXISTING
```

Other available options are:

- -Dkeycloak.migration.realmName

  This property is used if you want to export just one specified realm instead of all. If not specified, then all realms will be exported.

- -Dkeycloak.migration.usersExportStrategy

  This property is used to specify where users are exported. Possible values are:DIFFERENT_FILES - Users will be exported into different files according to the maximum number of users per file. This is default value.SKIP - Exporting of users will be skipped completely.REALM_FILE - All users will be exported to same file with the realm settings. (The result will be a file like "foo-realm.json" with both realm data and users.)SAME_FILE - All users will be exported to same file but different from the realm file. (The result will be a file like "foo-realm.json" with realm data and "foo-users.json" with users.)

- -Dkeycloak.migration.usersPerFile

  This property is used to specify the number of users per file (and also per DB transaction). It’s 50 by default. It’s used only if usersExportStrategy is DIFFERENT_FILES

- -Dkeycloak.migration.strategy

  This property is used during import. It can be used to specify how to proceed if a realm with same name already exists in the database where you are going to import data. Possible values are:IGNORE_EXISTING - Ignore importing if a realm of this name already exists.OVERWRITE_EXISTING - Remove existing realm and import it again with new data from the JSON file. If you want to fully migrate one environment to another and ensure that the new environment will contain the same data as the old one, you can specify this.

When importing realm files that weren’t exported before, the option `keycloak.import` can be used. If more than one realm file needs to be imported, a comma separated list of file names can be specified. This is more appropriate than the cases before, as this will happen only after the master realm has been initialized. Examples:

- -Dkeycloak.import=/tmp/realm1.json
- -Dkeycloak.import=/tmp/realm1.json,/tmp/realm2.json

### 16.1. Admin console export/import {#Admin_console_export_import}
Import of most resources can be performed from the admin console as well as export of most resources. Export of users is not supported.

Note: Attributes containing secrets or private information will be masked in export file. Export files obtained via Admin Console are thus not appropriate for backups or data transfer between servers. Only boot-time exports are appropriate for that.

The files created during a "startup" export can also be used to import from the admin UI. This way, you can export from one realm and import to another realm. Or, you can export from one server and import to another. Note: The admin console export/import allows just one realm per file.

|      | The admin console import allows you to "overwrite" resources if you choose. Use this feature with caution, especially on a production system. Export .json files from Admin Console Export operation are generally not appropriate for data import since they contain invalid values for secrets. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

|      | The admin console export allows you to export clients, groups, and roles. If there is a great number of any of these assets in your realm, the operation may take some time to complete. During that time server may not be responsive to user requests. Use this feature with caution, especially on a production system. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

## 17. User Account Service {#User_Account_Service}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/account.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/account.adoc)

Keycloak has a built-in User Account Service which every user has access to. This service allows users to manage their account, change their credentials, update their profile, and view their login sessions. The URL to this service is `<server-root>/auth/realms/{realm-name}/account`.

Account Service

![account service profile](assets/account-service-profile.png)

The initial page is the user’s profile, which is the `Account` left menu item. This is where they specify basic data about themselves. This screen can be extended to allow the user to manage additional attributes. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/)for more details.

The `Password` left menu item allows the user to change their password.

Password Update

![account service password](assets/account-service-password.png)

The `Authenticator` menu item allows the user to set up OTP if they desire. This will only show up if OTP is a valid authentication mechanism for your realm. Users are given directions to install [FreeOTP](https://freeotp.github.io/) or [Google Authenticator](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2) on their mobile device to be their OTP generator. The QR code you see in the screen shot can be scanned into the FreeOTP or Google Authenticator mobile application for nice and easy setup.

OTP Authenticator

![account service authenticator](assets/account-service-authenticator.png)

The `Federated Identity` menu item allows the user to link their account with an [identity broker](https://www.keycloak.org/docs/latest/server_admin/index.html#_identity_broker) (this is usually used to link social provider accounts together). This will show the list of external identity providers you have configured for your realm.

Federated Identity

![account service federated identity](assets/account-service-federated-identity.png)

The `Sessions` menu item allows the user to view and manage which devices are logged in and from where. They can perform logout of these sessions from this screen too.

Sessions

![account service sessions](assets/account-service-sessions.png)

The `Applications` menu item shows users which applications they have access to.

Applications

![account service apps](assets/account-service-apps.png)

### 17.1. Themeable {#Themeable}
Like all UIs in Keycloak, the User Account Service is completely themeable and internationalizable. See the [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more details.

## 18. Threat Model Mitigation {#Threat_Model_Mitigation}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat.adoc)

This chapter discusses possible security vulnerabilities any authentication server could have and how Keycloak mitigates those vulnerabilities. A good list of potential vulnerabilities and what security implementations should do to mitigate them can be found in the [OAuth 2.0 Threat Model](https://tools.ietf.org/html/rfc6819) document put out by the IETF. Many of those vulnerabilities are discussed here.

### 18.1. Host {#Host}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/host.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/host.adoc)

Keycloak uses the public hostname for a number of things. For example, in the token issuer fields and URLs sent in password reset emails.

By default, the hostname is based on the request headers and there is no check to make sure this hostname is valid.

If you are not using a load balancer or proxy in front of Keycloak that prevents invalid host headers, you must explicitly configure what hostnames should be accepted.

The Hostname SPI provides a way to configure the hostname for a request. Out of the box there are two providers. These are request and fixed. It is also possible to develop your own provider in the case the built-in providers do not provide the functionality needed.

#### 18.1.1. Request provider {#Request_provider}
This is the default hostname provider and uses request headers to determine the hostname. As it uses the headers from the request it is important to use this in combination with a proxy or a filter that rejects invalid hostnames.

It is beyond the scope of this documentation to provide instructions on how to configure valid hostnames for a proxy. To configure it in a filter you need to edit standalone.xml to set permitted aliases for the server. The following example will only permit requests to `auth.example.com`:

```
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

The changes that have been made from the default config is to add the attribute `default-host="ignore"` and update the attribute `alias`. `default-host="ignore"` prevents unknown hosts from being handled, while `alias` is used to list the accepted hosts.

Here is the equivalent configuration using CLI commands:

```
/subsystem=undertow/server=default-server:write-attribute(name=default-host,value=ignore)
/subsystem=undertow/server=default-server/host=default-host:write-attribute(name=alias,value=[auth.example.com]

:reload
```

#### 18.1.2. Fixed provider {#Fixed_provider}
The fixed provider makes it possible to configure a fixed hostname. Unlike the request provider the fixed provider allows internal applications to invoke Keycloak on an alternative URL (for example an internal IP address). It is also possible to override the hostname for a specific realm through the configuration of the realm in the admin console.

This is the recommended provider to use in production.

To change to the fixed provider and configure the hostname edit standalone.xml. The following example shows the fixed provider with the hostname set to auth.example.com:

```
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

Here is the equivalent configuration using CLI commands:

```
/subsystem=keycloak-server/spi=hostname:write-attribute(name=default-provider, value="fixed")
/subsystem=keycloak-server/spi=hostname/provider=fixed:write-attribute(name=properties.hostname,value="auth.example.com")
```

By default the `httpPort` and `httpsPort` are received from the request. As long as any proxies are configured correctly it should not be necessary to change this. It is possible to configure fixed ports if necessary by setting the `httpPort` and`httpsPort` properties on the fixed provider.

In most cases the scheme should be set correctly. This may not be true if the reverse proxy is unable to set the `X-Forwarded-For` header correctly, or if there is an internal application using non-https to invoke Keycloak. In such cases it is possible to set the `alwaysHttps` to `true`.

#### 18.1.3. Custom provider {#Custom_provider}
To develop a custom hostname provider you need to implement `org.keycloak.urls.HostnameProviderFactory` and`org.keycloak.urls.HostnameProvider`.

Follow the instructions in the Service Provider Interfaces section in [Server Developer Guide](https://www.keycloak.org/docs/6.0/server_development/) for more information on how to develop a custom provider.

### 18.2. Admin Endpoints and Console {#Admin_Endpoints_and_Console}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/admin.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/admin.adoc)

The Keycloak administrative REST API and the web console are exposed by default on the same port as non-admin usage. If you are exposing Keycloak on the Internet, we recommend not also exposing the admin endpoints on the Internet.

This can be achieve either directly in Keycloak or with a proxy such as Apache or nginx.

For the proxy option please follow the documentation for the proxy. You need to control access to any requests to `/auth/admin`.

To achieve this directly in Keycloak there are a few options. This document covers two options, IP restriction and separate ports.

#### 18.2.1. IP Restriction {#IP_Restriction}
It is possible to restrict access to `/auth/admin` to only specific IP addresses.

The following example restricts access to `/auth/admin` to IP addresses in the range `10.0.0.1` to `10.0.0.255`.

```
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

Equivalent configuration using CLI commands:

```
/subsystem=undertow/configuration=filter/expression-filter=ipAccess:add(,expression="path-prefix[/auth/admin] -> ip-access-control(acl={'10.0.0.0/24 allow'})")
/subsystem=undertow/server=default-server/host=default-host/filter-ref=ipAccess:add()
```

|      | For IP restriction if you are using a proxy it is important to configure it correctly to make sure Keycloak receives the client IP address and not the proxy IP address |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### 18.2.2. Port Restriction {#Port_Restriction}
It is possible to expose `/auth/admin` to a different port that is not exposed on the Internet.

The following example exposes `/auth/admin` on port `8444` while not permitting access with the default port `8443`.

```
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

Equivalent configuration using CLI commands:

```
/socket-binding-group=standard-sockets/socket-binding=https-admin/:add(port=8444)

/subsystem=undertow/server=default-server/https-listener=https-admin:add(socket-binding=https-admin, security-realm=ApplicationRealm, enable-http2=true)

/subsystem=undertow/configuration=filter/expression-filter=portAccess:add(,expression="path-prefix('/auth/admin') and not equals(%p, 8444) -> response-code(403)")
/subsystem=undertow/server=default-server/host=default-host/filter-ref=portAccess:add()
```

### 18.3. Password guess: brute force attacks {#Password_guess:_brute_force_attacks}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/brute-force.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/brute-force.adoc)

A brute force attack happens when an attacker is trying to guess a user’s password. Keycloak has some limited brute force detection capabilities. If turned on, a user account will be temporarily disabled if a threshold of login failures is reached. To enable this feature go to the `Realm Settings` left menu item, click on the `Security Defenses` tab, then additional go to the `Brute Force Detection` sub-tab.

Brute Force Detection

![brute force](assets/brute-force.png)

There are 2 different configurations for brute force detection; permanent lockout and temporary lockout. Permanent lockout will disable a user’s account after an attack is detected; the account will be disabled until an administrator renables it. Temporary lockout will disable a user’s account for a time period after an attack is detected; the time period for which the account is disabled increases the longer the attack continues.

**Common Parameters**

- Max Login Failures

  Maximum number of login failures permitted. Default value is 30.

- Quick Login Check Milli Seconds

  Minimum time required between login attempts. Default is 1000.

- Minimum Quick Login Wait

  Minimum amount of time the user will be temporarily disabled if logins attempts are quicker than *Quick Login Check Milli Seconds*. Default is 1 minute.

**Temporary Lockout Parameters**

- Wait Increment

  Amount of time added to the time a user is temporarily disabled after each time *Max Login Failures* is reached. Default is 1 minute.

- Max Wait

  The maximum amount of time for which a user will be temporarily disabled. Default is 15 minutes.

- Failure Reset Time

  Time after which the failure count will be reset; timer runs from the last failed login. Default is 12 hours.

**Permanent Lockout Algorithm**

1. On successful login
   1. Reset `count`
2. On failed login
   1. Increment `count`
   2. If `count` greater than *Max Login Failures*
      1. Permanently disable user
   3. Else if time between this failure and the last failure is less than *Quick Login Check Milli Seconds*
      1. Temporarily disable user for *Minimum Quick Login Wait*

When a user is disabled they can not login until an administrator enables the user; enabling an account resets `count`.

**Temporary Lockout Algorithm**

1. On successful login
   1. Reset `count`
2. On failed login
   1. If time between this failure and the last failure is greater than *Failure Reset Time*
      1. Reset `count`
   2. Increment `count`
   3. Calculate `wait` using *Wait Increment* * (`count` / *Max Login Failures*). The division is an integer division so will always be rounded down to a whole number
   4. If `wait` equals 0 and time between this failure and the last failure is less than *Quick Login Check Milli Seconds* then set `wait` to *Minimum Quick Login Wait* instead
      1. Temporarily disable the user for the smaller of `wait` and *Max Wait* seconds

Login failures when a user is temporarily disabled do not increment `count`.

The downside of Keycloak brute force detection is that the server becomes vulnerable to denial of service attacks. An attacker can simply try to guess passwords for any accounts it knows and these account will be disabled. Eventually we will expand this functionality to take client IP address into account when deciding whether to block a user.

A better option might be a tool like [Fail2Ban](http://www.fail2ban.org/wiki/index.php/Main_Page). You can point this service at the Keycloak server’s log file. Keycloak logs every login failure and client IP address that had the failure. Fail2Ban can be used to modify firewalls after it detects an attack to block connections from specific IP addresses.

#### 18.3.1. Password Policies {#Password_Policies}
Another thing you should do to prevent password guess is to have a complex enough password policy to ensure that users pick hard to guess passwords. See the [Password Policies](https://www.keycloak.org/docs/latest/server_admin/index.html#_password-policies) chapter for more details.

The best way to prevent password guessing though is to set up the server to use a one-time-password (OTP).

### 18.4. Clickjacking {#Clickjacking}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/clickjacking.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/clickjacking.adoc)

With clickjacking, a malicious site loads the target site in a transparent iFrame overlaid on top of a set of dummy buttons that are carefully constructed to be placed directly under important buttons on the target site. When a user clicks a visible button, they are actually clicking a button (such as a "login" button) on the hidden page. An attacker can steal a user’s authentication credentials and access their resources.

By default, every response by Keycloak sets some specific browser headers that can prevent this from happening. Specifically, it sets [X-FRAME_OPTIONS](https://tools.ietf.org/html/rfc7034) and [Content-Security-Policy](http://www.w3.org/TR/CSP/). You should take a look at the definition of both of these headers as there is a lot of fine-grain browser access you can control. In the admin console you can specify the values these headers will have. Go to the `Realm Settings` left menu item and click the `Security Defenses` tab and make sure you are on the `Headers` sub-tab.

![security headers](assets/security-headers.png)

By default, Keycloak only sets up a *same-origin* policy for iframes.

### 18.5. SSL/HTTPS Requirement {#SSL_HTTPS_Requirement}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/ssl.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/ssl.adoc)

If you do not use SSL/HTTPS for all communication between the Keycloak auth server and the clients it secures, you will be very vulnerable to man in the middle attacks. OAuth 2.0/OpenID Connect uses access tokens for security. Without SSL/HTTPS, attackers can sniff your network and obtain an access token. Once they have an access token they can do any operation that the token has been given permission for.

Keycloak has [three modes for SSL/HTTPS](https://www.keycloak.org/docs/latest/server_admin/index.html#_ssl_modes). SSL can be hard to set up, so out of the box, Keycloak allows non-HTTPS communication over private IP addresses like localhost, 192.168.x.x, and other private IP addresses. In production, you should make sure SSL is enabled and required across the board.

On the adapter/client side, Keycloak allows you to turn off the SSL trust manager. The trust manager ensures identity the client is talking to. It checks the DNS domain name against the server’s certificate. In production you should make sure that each of your client adapters is configured to use a truststore. Otherwise you are vulnerable to DNS man in the middle attacks.

### 18.6. CSRF Attacks {#CSRF_Attacks}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/csrf.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/csrf.adoc)

Cross-site request forgery (CSRF) is a web-based attack whereby HTTP requests are transmitted from a user that the web site trusts or has authenticated with(e.g. via HTTP redirects or HTML forms). Any site that uses cookie based authentication is vulnerable to these types of attacks. These attacks are mitigated by matching a state cookie against a posted form or query parameter.

The OAuth 2.0 login specification requires that a state cookie be used and matched against a transmitted state parameter. Keycloak fully implements this part of the specification so all logins are protected.

The Keycloak Admin Console is a pure JavaScript/HTML5 application that makes REST calls to the backend Keycloak admin REST API. These calls all require bearer token authentication and are made via JavaScript Ajax calls. CSRF does not apply here. The admin REST API can also be configured to validate the CORS origins as well.

The only part of Keycloak that really falls into CSRF is the user account management pages. To mitigate this Keycloak sets a state cookie and also embeds the value of this state cookie within hidden form fields or query parameters in action links. This query or form parameter is checked against the state cookie to verify that the call was made by the user.

### 18.7. Unspecific Redirect URIs {#Unspecific_Redirect_URIs}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/redirect.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/redirect.adoc)

For the [Authorization Code Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows), if you register redirect URIs that are too general, then it would be possible for a rogue client to impersonate a different client that has a broader scope of access. This could happen for instance if two clients live under the same domain. So, it’s a good idea to make your registered redirect URIs as specific as feasible.

### 18.8. Compromised Access and Refresh Tokens {#Compromised_Access_and_Refresh_Tokens}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/compromised-tokens.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/compromised-tokens.adoc)

There are a few things you can do to mitigate access tokens and refresh tokens from being stolen. The most important thing is to enforce SSL/HTTPS communication between Keycloak and its clients and applications. It might seem obvious, but since Keycloak does not have SSL enabled by default, an administrator might not realize that it is necessary.

Another thing you can do to mitigate leaked access tokens is to shorten their lifespans. You can specify this within the [timeouts page](https://www.keycloak.org/docs/latest/server_admin/index.html#_timeouts). Short lifespans (minutes) for access tokens for clients and applications to refresh their access tokens after a short amount of time. If an admin detects a leak, they can logout all user sessions to invalidate these refresh tokens or set up a revocation policy. Making sure refresh tokens always stay private to the client and are never transmitted ever is very important as well.

You can also mitigate against leaked access tokens and refresh tokens by issuing these tokens as holder-of-key tokens. See [OAuth 2.0 Mutual TLS Client Certificate Bound Access Token](https://www.keycloak.org/docs/latest/server_admin/index.html#_mtls-client-certificate-bound-tokens) to learn how.

If an access token or refresh token is compromised, the first thing you should do is go to the admin console and push a not-before revocation policy to all applications. This will enforce that any tokens issued prior to that date are now invalid. Pushing new not-before policy will also ensure that application will be forced to download new public keys from Keycloak, hence it is also useful for the case, when you think that realm signing key was compromised. More info in the [keys chapter](https://www.keycloak.org/docs/latest/server_admin/index.html#realm_keys).

You can also disable specific applications, clients, and users if you feel that any one of those entities is completely compromised.

### 18.9. Compromised Authorization Code {#Compromised_Authorization_Code}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/compromised-codes.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/compromised-codes.adoc)

For the [OIDC Auth Code Flow](https://www.keycloak.org/docs/latest/server_admin/index.html#_oidc-auth-flows), it would be very hard for an attacker to compromise Keycloak authorization codes. Keycloak generates a cryptographically strong random value for its authorization codes so it would be very hard to guess an access token. An authorization code can only be used once to obtain an access token. In the admin console you can specify how long an authorization code is valid for on the [timeouts page](https://www.keycloak.org/docs/latest/server_admin/index.html#_timeouts). This value should be really short, as short as a few seconds and just long enough for the client to make the request to obtain a token from the code.

### 18.10. Open redirectors {#Open_redirectors}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/open-redirect.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/open-redirect.adoc)

An attacker could use the end-user authorization endpoint and the redirect URI parameter to abuse the authorization server as an open redirector. An open redirector is an endpoint using a parameter to automatically redirect a user agent to the location specified by the parameter value without any validation. An attacker could utilize a user’s trust in an authorization server to launch a phishing attack.

Keycloak requires that all registered applications and clients register at least one redirection URI pattern. Any time a client asks Keycloak to perform a redirect (on login or logout for example), Keycloak will check the redirect URI vs. the list of valid registered URI patterns. It is important that clients and applications register as specific a URI pattern as possible to mitigate open redirector attacks.

### 18.11. Password database compromised {#Password_database_compromised}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/password-db-compromised.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/password-db-compromised.adoc)

Keycloak does not store passwords in raw text. It stores a hash of them using the PBKDF2 algorithm. It actually uses a default of 20,000 hashing iterations! This is the security community’s recommended number of iterations. This can be a rather large performance hit on your system as PBKDF2, by design, gobbles up a significant amount of CPU. It is up to you to decide how serious you want to be to protect your password database.

### 18.12. Limiting Scope {#Limiting_Scope}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/scope.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/scope.adoc)

By default, each new client application has an unlimited `role scope mappings`. This means that every access token that is created for that client will contain all the permissions the user has. If the client gets compromised and the access token is leaked, then each system that the user has permission to access is now also compromised. It is highly suggested that you limit the roles an access token is assigned by using the [Scope menu](https://www.keycloak.org/docs/latest/server_admin/index.html#_role_scope_mappings) for each client. Or alternatively, you can set role scope mappings at the Client Scope level and assign Client Scopes to your client by using the [Client Scope menu](https://www.keycloak.org/docs/latest/server_admin/index.html#_client_scopes_linking).

### 18.13. Limit Token Audience {#Limit_Token_Audience}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/audience-limit.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/audience-limit.adoc)

In environments where the level of trust among services is low, it is a good practice to limit the audiences on the token. The motivation behind this is described in the [OAuth2 Threat Model](https://tools.ietf.org/html/rfc6819#section-5.1.5.5) document and more details are in the [Audience Support section](https://www.keycloak.org/docs/latest/server_admin/index.html#_audience).

### 18.14. SQL Injection Attacks {#SQL_Injection_Attacks}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/threat/sql.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/threat/sql.adoc)

At this point in time, there is no knowledge of any SQL injection vulnerabilities in Keycloak.

## 19. The Admin CLI {#The_Admin_CLI}
[Edit this section](https://github.com/keycloak/keycloak-documentation/blob/master/server_admin/topics/admin-cli.adoc)[Report an issue](https://issues.jboss.org/secure/CreateIssueDetails!init.jspa?pid=12313920&components=12323375&issuetype=1&priority=3&description=File: server_admin/topics/admin-cli.adoc)

In previous chapters, we described how to use the Keycloak Admin Console to perform administrative tasks. You can also perform those tasks from the command-line interface (CLI) by using the Admin CLI command-line tool.

### 19.1. Installing the Admin CLI {#Installing_the_Admin_CLI}
The Admin CLI is packaged inside Keycloak Server distribution. You can find execution scripts inside the `bin` directory.

The Linux script is called `kcadm.sh`, and the script for Windows is called `kcadm.bat`.

You can add the Keycloak server directory to your `PATH` to use the client from any location on your file system.

For example, on:

- Linux:

```
$ export PATH=$PATH:$KEYCLOAK_HOME/bin
$ kcadm.sh
```

- Windows:

```
c:\> set PATH=%PATH%;%KEYCLOAK_HOME%\bin
c:\> kcadm
```

We assume the `KEYCLOAK_HOME` environment (env) variable is set to the path where you extracted the Keycloak Server distribution.

|      | To avoid repetition, the rest of this document only gives Windows examples in places where the difference in the CLI is more than just in the `kcadm` command name. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

### 19.2. Using the Admin CLI {#Using_the_Admin_CLI}
The Admin CLI works by making HTTP requests to Admin REST endpoints. Access to them is protected and requires authentication.

|      | Consult the Admin REST API documentation for details about JSON attributes for specific endpoints. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

1. Start an authenticated session by providing credentials, that is, logging in. You are ready to perform create, read, update, and delete (CRUD) operations.

   For example, on

   - Linux:

     ```
     $ kcadm.sh config credentials --server http://localhost:8080/auth --realm demo --user admin --client admin
     $ kcadm.sh create realms -s realm=demorealm -s enabled=true -o
     $ CID=$(kcadm.sh create clients -r demorealm -s clientId=my_client -s 'redirectUris=["http://localhost:8980/myapp/*"]' -i)
     $ kcadm.sh get clients/$CID/installation/providers/keycloak-oidc-keycloak-json
     ```

   - Windows:

     ```
     c:\> kcadm config credentials --server http://localhost:8080/auth --realm demo --user admin --client admin
     c:\> kcadm create realms -s realm=demorealm -s enabled=true -o
     c:\> kcadm create clients -r demorealm -s clientId=my_client -s "redirectUris=[\"http://localhost:8980/myapp/*\"]" -i > clientid.txt
     c:\> set /p CID=<clientid.txt
     c:\> kcadm get clients/%CID%/installation/providers/keycloak-oidc-keycloak-json
     ```

2. In a production environment, you must access Keycloak with `https:` to avoid exposing tokens to network sniffers. If a server’s certificate is not issued by one of the trusted certificate authorities (CAs) that are included in Java’s default certificate truststore, prepare a `truststore.jks` file and instruct the Admin CLI to use it.

   For example, on:

   - Linux:

     ```
     $ kcadm.sh config truststore --trustpass $PASSWORD ~/.keycloak/truststore.jks
     ```

   - Windows:

     ```
     c:\> kcadm config truststore --trustpass %PASSWORD% %HOMEPATH%\.keycloak\truststore.jks
     ```

### 19.3. Authenticating {#Authenticating}
When you log in with the Admin CLI, you specify a server endpoint URL and a realm, and then you specify a user name. Another option is to specify only a clientId, which results in using a special "service account". When you log in using a user name, you must use a password for the specified user. When you log in using a clientId, you only need the client secret, not the user password. You could also use `Signed JWT` instead of the client secret.

Make sure the account used for the session has the proper permissions to invoke Admin REST API operations. For example, the `realm-admin` role of the `realm-management` client allows the user to administer the realm within which the user is defined.

There are two primary mechanisms for authentication. One mechanism uses `kcadm config credentials` to start an authenticated session.

```
$ kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin
```

This approach maintains an authenticated session between the `kcadm` command invocations by saving the obtained access token and the associated refresh token. It may also maintain other secrets in a private configuration file. See [next chapter](https://www.keycloak.org/docs/latest/server_admin/index.html#_working_with_alternative_configurations) for more information on the configuration file.

The second approach only authenticates each command invocation for the duration of that invocation. This approach increases the load on the server and the time spent with roundtrips obtaining tokens. The benefit of this approach is not needing to save any tokens between invocations, which means nothing is saved to disk. This mode is used when the `--no-config` argument is specified.

For example, when performing an operation, we specify all the information required for authentication.

```
$ kcadm.sh get realms --no-config --server http://localhost:8080/auth --realm master --user admin --password admin
```

Run the `kcadm.sh help` command for more information on using the Admin CLI.

Run the `kcadm.sh config credentials --help` command for more information about starting an authenticated session.

### 19.4. Working with alternative configurations {#Working_with_alternative_configurations}
By default, the Admin CLI automatically maintains a configuration file called `kcadm.config` located under the user’s home directory. In Linux-based systems, the full path name is `$HOME/.keycloak/kcadm.config`. On Windows, the full path name is `%HOMEPATH%\.keycloak\kcadm.config`. You can use the `--config` option to point to a different file or location so you can maintain multiple authenticated sessions in parallel.

|      | It is best to perform operations tied to a single configuration file from a single thread. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

Make sure you do not make the configuration file visible to other users on the system. It contains access tokens and secrets that should be kept private. By default, the `~/.keycloak` directory and its content are created automatically with proper access limits. If the directory already exists, its permissions are not updated.

If your unique circumstances require you to avoid storing secrets inside a configuration file, you can do so. It will be less convenient and you will have to make more token requests. To not store secrets, use the `--no-config` option with all your commands and specify all the authentication information needed by the `config credentials` command with each `kcadm`invocation.

### 19.5. Basic operations and resource URIs {#Basic_operations_and_resource_URIs}
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

For example, if you authenticate against the server <http://localhost:8080/auth> and realm is `master`, then using `users` as ENDPOINT results in the resource URL <http://localhost:8080/auth/admin/realms/master/users>.

If you set ENDPOINT to `clients`, the effective resource URI would be <http://localhost:8080/auth/admin/realms/master/clients>.

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

### 19.6. Realm operations {#Realm_operations}
#### Creating a new realm {#Creating_a_new_realm}
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

#### Listing existing realms {#Listing_existing_realms}
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

#### Getting a specific realm {#Getting_a_specific_realm}
You append a realm name to a collection URI to get an individual realm.

```
$ kcadm.sh get realms/master
```

#### Updating a realm {#Updating_a_realm}
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

#### Deleting a realm {#Deleting_a_realm}
Run the following command to delete a realm.

```
$ kcadm.sh delete realms/demorealm
```

#### Turning on all login page options for the realm {#Turning_on_all_login_page_options_for_the_realm}
Set the attributes controlling specific capabilities to `true`.

For example:

```
$ kcadm.sh update realms/demorealm -s registrationAllowed=true -s registrationEmailAsUsername=true -s rememberMe=true -s verifyEmail=true -s resetPasswordAllowed=true -s editUsernameAllowed=true
```

#### Listing the realm keys {#Listing_the_realm_keys}
Use the `get` operation on the `keys` endpoint of the target realm.

```
$ kcadm.sh get keys -r demorealm
```

#### Generating new realm keys {#Generating_new_realm_keys}
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

#### Adding new realm keys from a Java Key Store file {#Adding_new_realm_keys_from_a_Java_Key_Store_file}
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

#### Making the key passive or disabling the key {#Making_the_key_passive_or_disabling_the_key}
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

#### Deleting an old key {#Deleting_an_old_key}
1. Make sure the key you are deleting has been passive and disabled to prevent any existing tokens held by applications and users from abruptly failing to work.

2. Identify the key you want to make passive.

   ```
   $ kcadm.sh get keys -r demorealm
   ```

3. Use the `providerId` of that key to perform a delete.

   ```
   $ kcadm.sh delete components/PROVIDER_ID -r demorealm
   ```

#### Configuring event logging for a realm {#Configuring_event_logging_for_a_realm}
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

#### Flushing the caches {#Flushing_the_caches}
1. Use the `create` command and one of the following endpoints: `clear-realm-cache`, `clear-user-cache`, or `clear-keys-cache`.

2. Set `realm` to the same value as the target realm.

   For example:

   ```
   $ kcadm.sh create clear-realm-cache -r demorealm -s realm=demorealm
   $ kcadm.sh create clear-user-cache -r demorealm -s realm=demorealm
   $ kcadm.sh create clear-keys-cache -r demorealm -s realm=demorealm
   ```

#### Importing a realm from exported .json file {#Importing_a_realm_from_exported__json_file}
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

### 19.7. Role operations {#Role_operations}
#### Creating a realm role {#Creating_a_realm_role}
Use the `roles` endpoint to create a realm role.

```
$ kcadm.sh create roles -r demorealm -s name=user -s 'description=Regular user with limited set of permissions'
```

#### Creating a client role {#Creating_a_client_role}
1. Identify the client first and then use the `get` command to list available clients when creating a client role.

   ```
   $ kcadm.sh get clients -r demorealm --fields id,clientId
   ```

2. Create a new role by using the `clientId` attribute to construct an endpoint URI, such as `clients/ID/roles`.

   For example:

   ```
   $ kcadm.sh create clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles -r demorealm -s name=editor -s 'description=Editor can edit, and publish any article'
   ```

#### Listing realm roles {#Listing_realm_roles}
Use the `get` command on the `roles` endpoint to list existing realm roles.

```
$ kcadm.sh get roles -r demorealm
```

You can also use the `get-roles` command.

```
$ kcadm.sh get-roles -r demorealm
```

#### Listing client roles {#Listing_client_roles}
There is a dedicated `get-roles` command to simplify listing realm and client roles. It is an extension of the `get` command and behaves the same with additional semantics for listing roles.

Use the `get-roles` command, passing it either the clientId attribute (via the `--cclientid` option) or `id` (via the `--cid`option) to identify the client to list client roles.

For example:

```
$ kcadm.sh get-roles -r demorealm --cclientid realm-management
```

#### Getting a specific realm role {#Getting_a_specific_realm_role}
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

#### Getting a specific client role {#Getting_a_specific_client_role}
Use a dedicated `get-roles` command, passing it either the clientId attribute (via the `--cclientid` option) or ID (via the `--cid` option) to identify the client, and passing it either the role name (via the `--rolename` option) or ID (via the `--roleid`) to identify a specific client role.

For example:

```
$ kcadm.sh get-roles -r demorealm --cclientid realm-management --rolename manage-clients
```

#### Updating a realm role {#Updating_a_realm_role}
Use the `update` command with the same endpoint URI that you used to get a specific realm role.

For example:

```
$ kcadm.sh update roles/user -r demorealm -s 'description=Role representing a regular user'
```

#### Updating a client role {#Updating_a_client_role}
Use the `update` command with the same endpoint URI that you used to get a specific client role.

For example:

```
$ kcadm.sh update clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles/editor -r demorealm -s 'description=User that can edit, and publish articles'
```

#### Deleting a realm role {#Deleting_a_realm_role}
Use the `delete` command with the same endpoint URI that you used to get a specific realm role.

For example:

```
$ kcadm.sh delete roles/user -r demorealm
```

#### Deleting a client role {#Deleting_a_client_role}
Use the `delete` command with the same endpoint URI that you used to get a specific client role.

For example:

```
$ kcadm.sh delete clients/a95b6af3-0bdc-4878-ae2e-6d61a4eca9a0/roles/editor -r demorealm
```

#### Listing assigned, available, and effective realm roles for a composite role {#Listing_assigned,_available,_and_effective_realm_roles_for_a_composite_role}
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

#### Listing assigned, available, and effective client roles for a composite role {#Listing_assigned,_available,_and_effective_client_roles_for_a_composite_role}
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

#### Adding realm roles to a composite role {#Adding_realm_roles_to_a_composite_role}
There is a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the `user` role to the composite role `testrole`.

```
$ kcadm.sh add-roles --rname testrole --rolename user -r demorealm
```

#### Removing realm roles from a composite role {#Removing_realm_roles_from_a_composite_role}
There is a dedicated `remove-roles` command that can be used to remove realm roles and client roles.

The following example removes the `user` role from the target composite role `testrole`.

```
$ kcadm.sh remove-roles --rname testrole --rolename user -r demorealm
```

#### Adding client roles to a realm role {#Adding_client_roles_to_a_realm_role}
Use a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the roles defined on the client `realm-management` - `create-client` role and the `view-users` role to the `testrole` composite role.

```
$ kcadm.sh add-roles -r demorealm --rname testrole --cclientid realm-management --rolename create-client --rolename view-users
```

#### Adding client roles to a client role {#Adding_client_roles_to_a_client_role}
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

#### Removing client roles from a composite role {#Removing_client_roles_from_a_composite_role}
Use a dedicated `remove-roles` command to remove client roles from a composite role.

Use the following example to remove two roles defined on the client `realm management` - `create-client` role and the `view-users` role from the `testrole` composite role.

```
$ kcadm.sh remove-roles -r demorealm --rname testrole --cclientid realm-management --rolename create-client --rolename view-users
```

#### Adding client roles to a group {#Adding_client_roles_to_a_group}
Use a dedicated `add-roles` command that can be used for adding realm roles and client roles.

The following example adds the roles defined on the client `realm-management` - `create-client` role and the `view-users` role to the `Group` group (via the `--gname` option). The group can alternatively be specified by ID (via the `--gid`option).

See [Group operations](https://www.keycloak.org/docs/latest/server_admin/index.html#_group_operations) for more operations that can be performed to groups.

```
$ kcadm.sh add-roles -r demorealm --gname Group --cclientid realm-management --rolename create-client --rolename view-users
```

#### Removing client roles from a group {#Removing_client_roles_from_a_group}
Use a dedicated `remove-roles` command to remove client roles from a group.

Use the following example to remove two roles defined on the client `realm management` - `create-client` role and the `view-users` role from the `Group` group.

See [Group operations](https://www.keycloak.org/docs/latest/server_admin/index.html#_group_operations) for more operations that can be performed to groups.

```
$ kcadm.sh remove-roles -r demorealm --gname Group --cclientid realm-management --rolename create-client --rolename view-users
```

### 19.8. Client operations {#Client_operations}
#### Creating a client {#Creating_a_client}
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

#### Listing clients {#Listing_clients}
Use the `get` command on the `clients` endpoint to list clients.

For example:

```
$ kcadm.sh get clients -r demorealm --fields id,clientId
```

This example filters the output to list only the `id` and `clientId` attributes.

#### Getting a specific client {#Getting_a_specific_client}
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm
```

#### Getting the current secret for a specific client {#Getting_the_current_secret_for_a_specific_client}
Use a client’s ID to construct an endpoint URI, such as `clients/ID/client-secret`.

For example:

```
$ kcadm.sh get clients/$CID/client-secret
```

#### Getting an adapter configuration file (keycloak.json) for a specific client {#Getting_an_adapter_configuration_file__keycloak_json__for_a_specific_client}
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/keycloak-oidc-keycloak-json`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3/installation/providers/keycloak-oidc-keycloak-json -r demorealm
```

#### Getting a WildFly subsystem adapter configuration for a specific client {#Getting_a_WildFly_subsystem_adapter_configuration_for_a_specific_client}
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/keycloak-oidc-jboss-subsystem`.

For example:

```
$ kcadm.sh get clients/c7b8547f-e748-4333-95d0-410b76b3f4a3/installation/providers/keycloak-oidc-jboss-subsystem -r demorealm
```

#### Getting a Docker-v2 example configuration for a specific client {#Getting_a_Docker_v2_example_configuration_for_a_specific_client}
Use a client’s ID to construct an endpoint URI that targets a specific client, such as `clients/ID/installation/providers/docker-v2-compose-yaml`.

Note that response will be in `.zip` format.

For example:

```
$ kcadm.sh get http://localhost:8080/auth/admin/realms/demorealm/clients/8f271c35-44e3-446f-8953-b0893810ebe7/installation/providers/docker-v2-compose-yaml -r demorealm > keycloak-docker-compose-yaml.zip
```

#### Updating a client {#Updating_a_client}
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

#### Deleting a client {#Deleting_a_client}
Use the `delete` command with the same endpoint URI that you used to get a specific client.

For example:

```
$ kcadm.sh delete clients/c7b8547f-e748-4333-95d0-410b76b3f4a3 -r demorealm
```

#### Adding or removing roles for client’s service account {#Adding_or_removing_roles_for_client’s_service_account}
Service account for the client is just a special kind of user account with username `service-account-CLIENT_ID`. You can perform user operations on this account as if it was a regular user.

### 19.9. User operations {#User_operations}
#### Creating a user {#Creating_a_user}
Run the `create` command on the `users` endpoint to create a new user.

For example:

```
$ kcadm.sh create users -r demorealm -s username=testuser -s enabled=true
```

#### Listing users {#Listing_users}
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

#### Getting a specific user {#Getting_a_specific_user}
Use a user’s ID to compose an endpoint URI, such as `users/USER_ID`.

For example:

```
$ kcadm.sh get users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm
```

#### Updating a user {#Updating_a_user}
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

#### Deleting a user {#Deleting_a_user}
Use the `delete` command with the same endpoint URI that you used to get a specific user.

For example:

```
$ kcadm.sh delete users/0ba7a3fd-6fd8-48cd-a60b-2e8fd82d56e2 -r demorealm
```

#### Resetting a user’s password {#Resetting_a_user’s_password}
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

#### Listing assigned, available, and effective realm roles for a user {#Listing_assigned,_available,_and_effective_realm_roles_for_a_user}
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

#### Listing assigned, available, and effective client roles for a user {#Listing_assigned,_available,_and_effective_client_roles_for_a_user}
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

#### Adding realm roles to a user {#Adding_realm_roles_to_a_user}
Use a dedicated `add-roles` command to add realm roles to a user.

Use the following example to add the `user` role to user `testuser`.

```
$ kcadm.sh add-roles --uusername testuser --rolename user -r demorealm
```

#### Removing realm roles from a user {#Removing_realm_roles_from_a_user}
Use a dedicated `remove-roles` command to remove realm roles from a user.

Use the following example to remove the `user` role from the user `testuser`.

```
$ kcadm.sh remove-roles --uusername testuser --rolename user -r demorealm
```

#### Adding client roles to a user {#Adding_client_roles_to_a_user}
Use a dedicated `add-roles` command to add client roles to a user.

Use the following example to add two roles defined on the client `realm management` - `create-client` role and the `view-users` role to the user `testuser`.

```
$ kcadm.sh add-roles -r demorealm --uusername testuser --cclientid realm-management --rolename create-client --rolename view-users
```

#### Removing client roles from a user {#Removing_client_roles_from_a_user}
Use a dedicated `remove-roles` command to remove client roles from a user.

Use the following example to remove two roles defined on the realm management client.

```
$ kcadm.sh remove-roles -r demorealm --uusername testuser --cclientid realm-management --rolename create-client --rolename view-users
```

#### Listing a user’s sessions {#Listing_a_user’s_sessions}
1. Identify the user’s ID, and then use it to compose an endpoint URI, such as `users/ID/sessions`.

2. Use the `get` command to retrieve a list of the user’s sessions.

   For example:

   ```
   $kcadm get users/6da5ab89-3397-4205-afaa-e201ff638f9e/sessions
   ```

#### Logging out a user from a specific session {#Logging_out_a_user_from_a_specific_session}
1. Determine the session’s ID as described above.

2. Use the session’s ID to compose an endpoint URI, such as `sessions/ID`.

3. Use the `delete` command to invalidate the session.

   For example:

   ```
   $ kcadm.sh delete sessions/d0eaa7cc-8c5d-489d-811a-69d3c4ec84d1
   ```

#### Logging out a user from all sessions {#Logging_out_a_user_from_all_sessions}
You need a user’s ID to construct an endpoint URI, such as `users/ID/logout`.

Use the `create` command to perform `POST` on that endpoint URI.

For example:

```
$ kcadm.sh create users/6da5ab89-3397-4205-afaa-e201ff638f9e/logout -r demorealm -s realm=demorealm -s user=6da5ab89-3397-4205-afaa-e201ff638f9e
```

### 19.10. Group operations {#Group_operations}
#### Creating a group {#Creating_a_group}
Use the `create` command on the `groups` endpoint to create a new group.

For example:

```
$ kcadm.sh create groups -r demorealm -s name=Group
```

#### Listing groups {#Listing_groups}
Use the `get` command on the `groups` endpoint to list groups.

For example:

```
$ kcadm.sh get groups -r demorealm
```

#### Getting a specific group {#Getting_a_specific_group}
Use the group’s ID to construct an endpoint URI, such as `groups/GROUP_ID`.

For example:

```
$ kcadm.sh get groups/51204821-0580-46db-8f2d-27106c6b5ded -r demorealm
```

#### Updating a group {#Updating_a_group}
Use the `update` command with the same endpoint URI that you used to get a specific group.

For example:

```
$ kcadm.sh update groups/51204821-0580-46db-8f2d-27106c6b5ded -s 'attributes.email=["group@example.com"]' -r demorealm
```

#### Deleting a group {#Deleting_a_group}
Use the `delete` command with the same endpoint URI that you used to get a specific group.

For example:

```
$ kcadm.sh delete groups/51204821-0580-46db-8f2d-27106c6b5ded -r demorealm
```

#### Creating a subgroup {#Creating_a_subgroup}
Find the ID of the parent group by listing groups, and then use that ID to construct an endpoint URI, such as `groups/GROUP_ID/children`.

For example:

```
$ kcadm.sh create groups/51204821-0580-46db-8f2d-27106c6b5ded/children -r demorealm -s name=SubGroup
```

#### Moving a group under another group {#Moving_a_group_under_another_group}
1. Find the ID of an existing parent group and of an existing child group.
2. Use the parent group’s ID to construct an endpoint URI, such as `groups/PARENT_GROUP_ID/children`.
3. Run the `create` command on this endpoint and pass the child group’s ID as a JSON body.

For example:

```
$ kcadm.sh create groups/51204821-0580-46db-8f2d-27106c6b5ded/children -r demorealm -s id=08d410c6-d585-4059-bb07-54dcb92c5094
```

#### Get groups for a specific user {#Get_groups_for_a_specific_user}
Use a user’s ID to determine a user’s membership in groups to compose an endpoint URI, such as `users/USER_ID/groups`.

For example:

```
$ kcadm.sh get users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups -r demorealm
```

#### Adding a user to a group {#Adding_a_user_to_a_group}
Use the `update` command with an endpoint URI composed from user’s ID and a group’s ID, such as `users/USER_ID/groups/GROUP_ID`, to add a user to a group.

For example:

```
$ kcadm.sh update users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups/ce01117a-7426-4670-a29a-5c118056fe20 -r demorealm -s realm=demorealm -s userId=b544f379-5fc4-49e5-8a8d-5cfb71f46f53 -s groupId=ce01117a-7426-4670-a29a-5c118056fe20 -n
```

#### Removing a user from a group {#Removing_a_user_from_a_group}
Use the `delete` command on the same endpoint URI as used for adding a user to a group, such as `users/USER_ID/groups/GROUP_ID`, to remove a user from a group.

For example:

```
$ kcadm.sh delete users/b544f379-5fc4-49e5-8a8d-5cfb71f46f53/groups/ce01117a-7426-4670-a29a-5c118056fe20 -r demorealm
```

#### Listing assigned, available, and effective realm roles for a group {#Listing_assigned,_available,_and_effective_realm_roles_for_a_group}
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

#### Listing assigned, available, and effective client roles for a group {#Listing_assigned,_available,_and_effective_client_roles_for_a_group}
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

### 19.11. Identity provider operations {#Identity_provider_operations}
#### Listing available identity providers {#Listing_available_identity_providers}
Use the `serverinfo` endpoint to list available identity providers.

For example:

```
$ kcadm.sh get serverinfo -r demorealm --fields 'identityProviders(*)'
```

|      | The `serverinfo` endpoint is handled similarly to the `realms` endpoint in that it is not resolved relative to a target realm because it exists outside any specific realm. |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

#### Listing configured identity providers {#Listing_configured_identity_providers}
Use the `identity-provider/instances` endpoint.

For example:

```
$ kcadm.sh get identity-provider/instances -r demorealm --fields alias,providerId,enabled
```

#### Getting a specific configured identity provider {#Getting_a_specific_configured_identity_provider}
Use the `alias` attribute of the identity provider to construct an endpoint URI, such as `identity-provider/instances/ALIAS`, to get a specific identity provider.

For example:

```
$ kcadm.sh get identity-provider/instances/facebook -r demorealm
```

#### Removing a specific configured identity provider {#Removing_a_specific_configured_identity_provider}
Use the `delete` command with the same endpoint URI that you used to get a specific configured identity provider to remove a specific configured identity provider.

For example:

```
$ kcadm.sh delete identity-provider/instances/facebook -r demorealm
```

#### Configuring a Keycloak OpenID Connect identity provider {#Configuring_a_Keycloak_OpenID_Connect_identity_provider}
1. Use `keycloak-oidc` as the `providerId` when creating a new identity provider instance.

2. Provide the `config` attributes: `authorizationUrl`, `tokenUrl`, `clientId`, and `clientSecret`.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=keycloak-oidc -s providerId=keycloak-oidc -s enabled=true -s 'config.useJwksUrl="true"' -s config.authorizationUrl=http://localhost:8180/auth/realms/demorealm/protocol/openid-connect/auth -s config.tokenUrl=http://localhost:8180/auth/realms/demorealm/protocol/openid-connect/token -s config.clientId=demo-oidc-provider -s config.clientSecret=secret
   ```

#### Configuring an OpenID Connect identity provider {#Configuring_an_OpenID_Connect_identity_provider}
Configure the generic OpenID Connect provider the same way you configure the Keycloak OpenID Connect provider, except that you set the `providerId` attribute value to `oidc`.

#### Configuring a SAML 2 identity provider {#Configuring_a_SAML_2_identity_provider}
1. Use `saml` as the `providerId`.
2. Provide the `config` attributes: `singleSignOnServiceUrl`, `nameIDPolicyFormat`, and `signatureAlgorithm`.

For example:

```
$ kcadm.sh create identity-provider/instances -r demorealm -s alias=saml -s providerId=saml -s enabled=true -s 'config.useJwksUrl="true"' -s config.singleSignOnServiceUrl=http://localhost:8180/auth/realms/saml-broker-realm/protocol/saml -s config.nameIDPolicyFormat=urn:oasis:names:tc:SAML:2.0:nameid-format:persistent -s config.signatureAlgorithm=RSA_SHA256
```

#### Configuring a Facebook identity provider {#Configuring_a_Facebook_identity_provider}
1. Use `facebook` as the `providerId`.

2. Provide the `config` attributes: `clientId` and `clientSecret`. You can find these attributes in the Facebook Developers application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=facebook -s providerId=facebook -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=FACEBOOK_CLIENT_ID -s config.clientSecret=FACEBOOK_CLIENT_SECRET
   ```

#### Configuring a Google identity provider {#Configuring_a_Google_identity_provider}
1. Use `google` as the `providerId`.

2. Provide the `config` attributes: `clientId` and `clientSecret`. You can find these attributes in the Google Developers application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=google -s providerId=google -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=GOOGLE_CLIENT_ID -s config.clientSecret=GOOGLE_CLIENT_SECRET
   ```

#### Configuring a Twitter identity provider {#Configuring_a_Twitter_identity_provider}
1. Use `twitter` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the Twitter Application Management application configuration page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=google -s providerId=google -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=TWITTER_API_KEY -s config.clientSecret=TWITTER_API_SECRET
   ```

#### Configuring a GitHub identity provider {#Configuring_a_GitHub_identity_provider}
1. Use `github` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the GitHub Developer Application Settings page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=github -s providerId=github -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=GITHUB_CLIENT_ID -s config.clientSecret=GITHUB_CLIENT_SECRET
   ```

#### Configuring a LinkedIn identity provider {#Configuring_a_LinkedIn_identity_provider}
1. Use `linkedin` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the LinkedIn Developer Console application page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=linkedin -s providerId=linkedin -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=LINKEDIN_CLIENT_ID -s config.clientSecret=LINKEDIN_CLIENT_SECRET
   ```

#### Configuring a Microsoft Live identity provider {#Configuring_a_Microsoft_Live_identity_provider}
1. Use `microsoft` as the `providerId`.

2. Provide the `config` attributes `clientId` and `clientSecret`. You can find these attributes in the Microsoft Application Registration Portal page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=microsoft -s providerId=microsoft -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=MICROSOFT_APP_ID -s config.clientSecret=MICROSOFT_PASSWORD
   ```

#### Configuring a Stack Overflow identity provider {#Configuring_a_Stack_Overflow_identity_provider}
1. Use `stackoverflow` command as the `providerId`.

2. Provide the `config` attributes `clientId`, `clientSecret`, and `key`. You can find these attributes in the Stack Apps OAuth page for your application.

   For example:

   ```
   $ kcadm.sh create identity-provider/instances -r demorealm -s alias=stackoverflow -s providerId=stackoverflow -s enabled=true  -s 'config.useJwksUrl="true"' -s config.clientId=STACKAPPS_CLIENT_ID -s config.clientSecret=STACKAPPS_CLIENT_SECRET -s config.key=STACKAPPS_KEY
   ```

### 19.12. Storage provider operations {#Storage_provider_operations}
#### Configuring a Kerberos storage provider {#Configuring_a_Kerberos_storage_provider}
1. Use the `create` command against the `components` endpoint.

2. Specify realm id as a value of the `parentId` attribute.

3. Specify `kerberos` as the value of the `providerId` attribute, and `org.keycloak.storage.UserStorageProvider` as the value of the `providerType` attribute.

4. For example:

   ```
   $ kcadm.sh create components -r demorealm -s parentId=demorealmId -s id=demokerberos -s name=demokerberos -s providerId=kerberos -s providerType=org.keycloak.storage.UserStorageProvider -s 'config.priority=["0"]' -s 'config.debug=["false"]' -s 'config.allowPasswordAuthentication=["true"]' -s 'config.editMode=["UNSYNCED"]' -s 'config.updateProfileFirstLogin=["true"]' -s 'config.allowKerberosAuthentication=["true"]' -s 'config.kerberosRealm=["KEYCLOAK.ORG"]' -s 'config.keyTab=["http.keytab"]' -s 'config.serverPrincipal=["HTTP/localhost@KEYCLOAK.ORG"]' -s 'config.cachePolicy=["DEFAULT"]'
   ```

#### Configuring an LDAP user storage provider {#Configuring_an_LDAP_user_storage_provider}
1. Use the `create` command against the `components` endpoint.

2. Specify `ldap` as a value of the `providerId` attribute, and `org.keycloak.storage.UserStorageProvider` as the value of the `providerType` attribute.

3. Provide the realm ID as the value of the `parentId` attribute.

4. Use the following example to create a Kerberos-integrated LDAP provider.

   ```
   $ kcadm.sh create components -r demorealm -s name=kerberos-ldap-provider -s providerId=ldap -s providerType=org.keycloak.storage.UserStorageProvider -s parentId=3d9c572b-8f33-483f-98a6-8bb421667867  -s 'config.priority=["1"]' -s 'config.fullSyncPeriod=["-1"]' -s 'config.changedSyncPeriod=["-1"]' -s 'config.cachePolicy=["DEFAULT"]' -s config.evictionDay=[] -s config.evictionHour=[] -s config.evictionMinute=[] -s config.maxLifespan=[] -s 'config.batchSizeForSync=["1000"]' -s 'config.editMode=["WRITABLE"]' -s 'config.syncRegistrations=["false"]' -s 'config.vendor=["other"]' -s 'config.usernameLDAPAttribute=["uid"]' -s 'config.rdnLDAPAttribute=["uid"]' -s 'config.uuidLDAPAttribute=["entryUUID"]' -s 'config.userObjectClasses=["inetOrgPerson, organizationalPerson"]' -s 'config.connectionUrl=["ldap://localhost:10389"]'  -s 'config.usersDn=["ou=People,dc=keycloak,dc=org"]' -s 'config.authType=["simple"]' -s 'config.bindDn=["uid=admin,ou=system"]' -s 'config.bindCredential=["secret"]' -s 'config.searchScope=["1"]' -s 'config.useTruststoreSpi=["ldapsOnly"]' -s 'config.connectionPooling=["true"]' -s 'config.pagination=["true"]' -s 'config.allowKerberosAuthentication=["true"]' -s 'config.serverPrincipal=["HTTP/localhost@KEYCLOAK.ORG"]' -s 'config.keyTab=["http.keytab"]' -s 'config.kerberosRealm=["KEYCLOAK.ORG"]' -s 'config.debug=["true"]' -s 'config.useKerberosForPasswordAuthentication=["true"]'
   ```

#### Removing a user storage provider instance {#Removing_a_user_storage_provider_instance}
1. Use the storage provider instance’s `id` attribute to compose an endpoint URI, such as `components/ID`.

2. Run the `delete` command against this endpoint.

   For example:

   ```
   $ kcadm.sh delete components/3d9c572b-8f33-483f-98a6-8bb421667867 -r demorealm
   ```

#### Triggering synchronization of all users for a specific user storage provider {#Triggering_synchronization_of_all_users_for_a_specific_user_storage_provider}
1. Use the storage provider’s `id` attribute to compose an endpoint URI, such as `user-storage/ID_OF_USER_STORAGE_INSTANCE/sync`.

2. Add the `action=triggerFullSync` query parameter and run the `create` command.

   For example:

   ```
   $ kcadm.sh create user-storage/b7c63d02-b62a-4fc1-977c-947d6a09e1ea/sync?action=triggerFullSync
   ```

#### Triggering synchronization of changed users for a specific user storage provider {#Triggering_synchronization_of_changed_users_for_a_specific_user_storage_provider}
1. Use the storage provider’s `id` attribute to compose an endpoint URI, such as `user-storage/ID_OF_USER_STORAGE_INSTANCE/sync`.

2. Add the `action=triggerChangedUsersSync` query parameter and run the `create` command.

   For example:

   ```
   $ kcadm.sh create user-storage/b7c63d02-b62a-4fc1-977c-947d6a09e1ea/sync?action=triggerChangedUsersSync
   ```

#### Test LDAP user storage connectivity {#Test_LDAP_user_storage_connectivity}
1. Run the `get` command on the `testLDAPConnection` endpoint.

2. Provide query parameters `bindCredential`, `bindDn`, `connectionUrl`, and `useTruststoreSpi`, and then set the `action` query parameter to `testConnection`.

   For example:

   ```
   $ kcadm.sh get testLDAPConnection -q action=testConnection -q bindCredential=secret -q bindDn=uid=admin,ou=system -q connectionUrl=ldap://localhost:10389 -q useTruststoreSpi=ldapsOnly
   ```

#### Test LDAP user storage authentication {#Test_LDAP_user_storage_authentication}
1. Run the `get` command on the `testLDAPConnection` endpoint.

2. Provide the query parameters `bindCredential`, `bindDn`, `connectionUrl`, and `useTruststoreSpi`, and then set the `action` query parameter to `testAuthentication`.

   For example:

   ```
   $ kcadm.sh get testLDAPConnection -q action=testAuthentication -q bindCredential=secret -q bindDn=uid=admin,ou=system -q connectionUrl=ldap://localhost:10389 -q useTruststoreSpi=ldapsOnly
   ```

### 19.13. Adding mappers {#Adding_mappers}
#### Adding a hardcoded role LDAP mapper {#Adding_a_hardcoded_role_LDAP_mapper}
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `hardcoded-ldap-role-mapper`. Make sure to provide a value of `role` configuration parameter.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=hardcoded-ldap-role-mapper -s providerId=hardcoded-ldap-role-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config.role=["realm-management.create-client"]'
   ```

#### Adding an MS Active Directory mapper {#Adding_an_MS_Active_Directory_mapper}
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `msad-user-account-control-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=msad-user-account-control-mapper -s providerId=msad-user-account-control-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea
   ```

#### Adding a user attribute LDAP mapper {#Adding_a_user_attribute_LDAP_mapper}
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `user-attribute-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=user-attribute-ldap-mapper -s providerId=user-attribute-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."user.model.attribute"=["email"]' -s 'config."ldap.attribute"=["mail"]' -s 'config."read.only"=["false"]' -s 'config."always.read.value.from.ldap"=["false"]' -s 'config."is.mandatory.in.ldap"=["false"]'
   ```

#### Adding a group LDAP mapper {#Adding_a_group_LDAP_mapper}
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `group-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=group-ldap-mapper -s providerId=group-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."groups.dn"=[]' -s 'config."group.name.ldap.attribute"=["cn"]' -s 'config."group.object.classes"=["groupOfNames"]' -s 'config."preserve.group.inheritance"=["true"]' -s 'config."membership.ldap.attribute"=["member"]' -s 'config."membership.attribute.type"=["DN"]' -s 'config."groups.ldap.filter"=[]' -s 'config.mode=["LDAP_ONLY"]' -s 'config."user.roles.retrieve.strategy"=["LOAD_GROUPS_BY_MEMBER_ATTRIBUTE"]' -s 'config."mapped.group.attributes"=["admins-group"]' -s 'config."drop.non.existing.groups.during.sync"=["false"]' -s 'config.roles=["admins"]' -s 'config.groups=["admins-group"]' -s 'config.group=[]' -s 'config.preserve=["true"]' -s 'config.membership=["member"]'
   ```

#### Adding a full name LDAP mapper {#Adding_a_full_name_LDAP_mapper}
1. Run the `create` command on the `components` endpoint.

2. Set the `providerType` attribute to `org.keycloak.storage.ldap.mappers.LDAPStorageMapper`.

3. Set the `parentId` attribute to the ID of the LDAP provider instance.

4. Set the `providerId` attribute to `full-name-ldap-mapper`.

   For example:

   ```
   $ kcadm.sh create components -r demorealm -s name=full-name-ldap-mapper -s providerId=full-name-ldap-mapper -s providerType=org.keycloak.storage.ldap.mappers.LDAPStorageMapper -s parentId=b7c63d02-b62a-4fc1-977c-947d6a09e1ea -s 'config."ldap.full.name.attribute"=["cn"]' -s 'config."read.only"=["false"]' -s 'config."write.only"=["true"]'
   ```

### 19.14. Authentication operations {#Authentication_operations}
#### Setting a password policy {#Setting_a_password_policy}
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

#### Getting the current password policy {#Getting_the_current_password_policy}
Get the current realm configuration and filter everything but the `passwordPolicy` attribute.

Use the following example to display `passwordPolicy` for `demorealm`.

```
$ kcadm.sh get realms/demorealm --fields passwordPolicy
```

#### Listing authentication flows {#Listing_authentication_flows}
Run the `get` command on the `authentication/flows` endpoint.

For example:

```
$ kcadm.sh get authentication/flows -r demorealm
```

#### Getting a specific authentication flow {#Getting_a_specific_authentication_flow}
Run the `get` command on the `authentication/flows/FLOW_ID` endpoint.

For example:

```
$ kcadm.sh get authentication/flows/febfd772-e1a1-42fb-b8ae-00c0566fafb8 -r demorealm
```

#### Listing executions for a flow {#Listing_executions_for_a_flow}
Run the `get` command on the `authentication/flows/FLOW_ALIAS/executions` endpoint.

For example:

```
$ kcadm.sh get authentication/flows/Copy%20of%20browser/executions -r demorealm
```

#### Adding configuration to an execution {#Adding_configuration_to_an_execution}
1. Get execution for a flow, and take note of its ID
2. Run the `create` command on the `authentication/executions/{executionId}/config` endpoint.

For example:

```
$ kcadm create "authentication/executions/a3147129-c402-4760-86d9-3f2345e401c7/config" -r examplerealm -b '{"config":{"x509-cert-auth.mapping-source-selection":"Match SubjectDN using regular expression","x509-cert-auth.regular-expression":"(.*?)(?:$)","x509-cert-auth.mapper-selection":"Custom Attribute Mapper","x509-cert-auth.mapper-selection.user-attribute-name":"usercertificate","x509-cert-auth.crl-checking-enabled":"","x509-cert-auth.crldp-checking-enabled":false,"x509-cert-auth.crl-relative-path":"crl.pem","x509-cert-auth.ocsp-checking-enabled":"","x509-cert-auth.ocsp-responder-uri":"","x509-cert-auth.keyusage":"","x509-cert-auth.extendedkeyusage":"","x509-cert-auth.confirmation-page-disallowed":""},"alias":"my_otp_config"}'
```

#### Getting configuration for an execution {#Getting_configuration_for_an_execution}
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `get` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm get "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm
```

#### Updating configuration for an execution {#Updating_configuration_for_an_execution}
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `update` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm update "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm -b '{"id":"dd91611a-d25c-421a-87e2-227c18421833","alias":"my_otp_config","config":{"x509-cert-auth.extendedkeyusage":"","x509-cert-auth.mapper-selection.user-attribute-name":"usercertificate","x509-cert-auth.ocsp-responder-uri":"","x509-cert-auth.regular-expression":"(.*?)(?:$)","x509-cert-auth.crl-checking-enabled":"true","x509-cert-auth.confirmation-page-disallowed":"","x509-cert-auth.keyusage":"","x509-cert-auth.mapper-selection":"Custom Attribute Mapper","x509-cert-auth.crl-relative-path":"crl.pem","x509-cert-auth.crldp-checking-enabled":"false","x509-cert-auth.mapping-source-selection":"Match SubjectDN using regular expression","x509-cert-auth.ocsp-checking-enabled":""}}'
```

#### Deleting configuration for an execution {#Deleting_configuration_for_an_execution}
1. Get execution for a flow, and get its `authenticationConfig` attribute, containing the config ID.
2. Run the `delete` command on the `authentication/config/ID` endpoint.

For example:

```
$ kcadm delete "authentication/config/dd91611a-d25c-421a-87e2-227c18421833" -r examplerealm
```

