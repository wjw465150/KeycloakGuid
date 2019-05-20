# 保护应用程序和服务指南 {#Securing_Applications_and_Services_Guide}



## 1. 概述 {#Overview}

Keycloak支持OpenID Connect（OAuth 2.0的扩展）和SAML 2.0。 在保护客户端和服务时，首先需要确定的是您要使用的两个中的哪一个。 如果您愿意，您也可以选择使用OpenID Connect和其他SAML安全保护。

To secure clients and services you are also going to need an adapter or library for the protocol you’ve selected. Keycloak comes with its own adapters for selected platforms, but it is also possible to use generic OpenID Connect Resource Provider and SAML Service Provider libraries.

### 1.1. 什么是客户端适配器? {#What_are_Client_Adapters}

Keycloak客户端适配器是使用Keycloak轻松保护应用程序和服务的库。 我们将它们称为适配器而不是库，因为它们提供了与底层平台和框架的紧密集成。 这使得我们的适配器易于使用，并且它们需要的库样板代码少于库通常所需的代码。

### 1.2. 支持的平台 {#Supported_Platforms}

#### 1.2.1. OpenID Connect {#OpenID_Connect}

##### Java {#Java}

- [JBoss EAP](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)
- [WildFly](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)
- [Fuse](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter)
- [Tomcat](https://www.keycloak.org/docs/latest/securing_apps/index.html#_tomcat_adapter)
- [Jetty 9](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty9_adapter)
- [Servlet Filter](https://www.keycloak.org/docs/latest/securing_apps/index.html#_servlet_filter_adapter)
- [Spring Boot](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_boot_adapter)
- [Spring Security](https://www.keycloak.org/docs/latest/securing_apps/index.html#_spring_security_adapter)

##### JavaScript (client-side) {#JavaScript_client_side}

- [JavaScript](https://www.keycloak.org/docs/latest/securing_apps/index.html#_javascript_adapter)

##### Node.js (server-side) {#Node_js_server_side}

- [Node.js](https://www.keycloak.org/docs/latest/securing_apps/index.html#_nodejs_adapter)

#### 1.2.2. C# {#C__}

- [OWIN](https://github.com/dylanplecki/KeycloakOwinAuthentication) (community)

#### 1.2.3. Python {#Python}

- [oidc](https://pypi.org/project/oic/) (generic)

#### 1.2.4. Android {#Android}

- [AppAuth](https://github.com/openid/AppAuth-Android) (generic)
- [AeroGear](https://github.com/aerogear/aerogear-android-authz) (generic)

#### 1.2.5. iOS {#iOS}

- [AppAuth](https://github.com/openid/AppAuth-iOS) (generic)
- [AeroGear](https://github.com/aerogear/aerogear-ios-oauth2) (generic)

##### Apache HTTP Server {#Apache_HTTP_Server}

- [mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc)

#### 1.2.6. SAML {#SAML}

##### Java {#Java}

- [JBoss EAP](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_jboss_adapter)
- [WildFly](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_jboss_adapter)
- [Tomcat](https://www.keycloak.org/docs/latest/securing_apps/index.html#_tomcat_adapter)
- [Jetty](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty_saml_adapter)

##### Apache HTTP Server {#Apache_HTTP_Server}

- [mod_auth_mellon](https://www.keycloak.org/docs/latest/securing_apps/index.html#_mod_auth_mellon)

### 1.3. 支持的协议 {#Supported_Protocols}

#### 1.3.1. OpenID Connect {#OpenID_Connect}

[OpenID 连接](https://openid.net/connect/) (OIDC)是一个身份验证协议，它是[OAuth 2.0](https://tools.ietf.org/html/rfc6749)的扩展。虽然OAuth 2.0只是一个构建授权协议的框架，而且主要是不完整的，但OIDC是一个完整的身份验证和授权协议。 OIDC还大量使用了[Json Web Token](https://jwt.io/) (JWT)标准集。这些标准定义了一种身份令牌JSON格式，以及以一种紧凑且web友好的方式对数据进行数字签名和加密的方法。

使用OIDC时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到*identity token(身份令牌)*和*access token(访问令牌)*。 *身份令牌*包含有关用户的信息，例如用户名，电子邮件和其他个人资料信息。 *访问令牌*由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取*访问令牌*，它可以代表用户在其他远程服务上调用。 Keycloak对用户进行身份验证，然后要求用户同意授予访问请求它的客户端的权限。 然后客户端接收*访问令牌*。 此*访问令牌*由领域进行数字签名。 客户端可以使用此*访问令牌*在远程服务上进行REST调用。 REST服务提取*访问令牌*，验证令牌的签名，然后根据令牌内的访问信息决定是否处理请求。

#### 1.3.2. SAML 2.0 {#SAML_2_0}

[SAML 2.0](http://saml.xml.org/saml-specifications) 是与OIDC类似的规范，但是更老，更成熟。 它的根源在于SOAP和过多的WS-*规范，所以它往往比OIDC更冗长。 SAML 2.0主要是一种身份验证协议，通过在身份验证服务器和应用程序之间交换XML文档来工作。 XML签名和加密用于验证请求和响应。

在Keycloak中，SAML提供两种用例：浏览器应用程序和REST调用。

使用SAML时，实际上有两种用例。 第一个是要求Keycloak服务器为用户验证用户的应用程序。 成功登录后，应用程序将收到一个XML文档，其中包含称为SAML断言的内容，该断言指定了有关用户的各种属性。 此XML文档由领域进行数字签名，并包含访问信息（如用户角色映射），应用程序可以使用该信息来确定允许用户在应用程序上访问哪些资源。

第二种用例是希望获得远程服务访问权限的客户端。 在这种情况下，客户端要求Keycloak获取可用于代表用户在其他远程服务上调用的SAML断言。

#### 1.3.3. OpenID Connect 与 SAML {#OpenID_Connect_vs_SAML}

在OpenID Connect和SAML之间进行选择不仅仅是使用更新的协议（OIDC）而不是旧的更成熟的协议（SAML）。

在大多数情况下，Keycloak建议使用OIDC。

SAML往往比OIDC更冗长。

除了交换数据的详细程度之外，如果您比较规范，您会发现OIDC旨在与Web一起工作，同时SAML被改装为在Web上运行。 例如，OIDC也更适合HTML5/JavaScript应用程序，因为它比SAML更容易在客户端实现。 由于令牌采用JSON格式，因此JavaScript更易于使用。 您还将找到一些很好的功能，可以更轻松地在Web应用程序中实现安全性。 例如，查看规范用于轻松确定用户是否仍在登录的[iframe技巧](https://openid.net/specs/openid-connect-session-1_0.html#ChangeNotification)。

SAML虽然有它的用途。 正如您所看到的，OIDC规范的发展，您会发现它们实现了SAML多年来所拥有的越来越多的功能。 我们经常看到人们选择SAML而不是OIDC，因为人们认为它更成熟，也因为他们已经有了现有的应用程序。

## 2. OpenID 连接器 {#OpenID_Connect}

本节介绍如何使用Keycloak适配器或通用OpenID Connect资源提供程序库通过OpenID Connect保护应用程序和服务。

### 2.1. Java 适配器 {#Java_Adapters}

Keycloak为Java应用程序提供了一系列不同的适配器。 选择正确的适配器取决于目标平台。

所有Java适配器共享[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 章节中描述的一组常用配置选项。

#### 2.1.1. Java适配器配置 {#Java_Adapter_Config}

Keycloak支持的每个Java适配器都可以通过简单的JSON文件进行配置。 这可能是这样的：

```json
{
  "realm" : "demo",
  "resource" : "customer-portal",
  "realm-public-key" : "MIGfMA0GCSqGSIb3D...31LwIDAQAB",
  "auth-server-url" : "https://localhost:8443/auth",
  "ssl-required" : "external",
  "use-resource-role-mappings" : false,
  "enable-cors" : true,
  "cors-max-age" : 1000,
  "cors-allowed-methods" : "POST, PUT, DELETE, GET",
  "cors-exposed-headers" : "WWW-Authenticate, My-custom-exposed-Header",
  "bearer-only" : false,
  "enable-basic-auth" : false,
  "expose-token" : true,
  "verify-token-audience" : true,
   "credentials" : {
      "secret" : "234234-234234-234234"
   },

   "connection-pool-size" : 20,
   "disable-trust-manager": false,
   "allow-any-hostname" : false,
   "truststore" : "path/to/truststore.jks",
   "truststore-password" : "geheim",
   "client-keystore" : "path/to/client-keystore.jks",
   "client-keystore-password" : "geheim",
   "client-key-password" : "geheim",
   "token-minimum-time-to-live" : 10,
   "min-time-between-jwks-requests" : 10,
   "public-key-cache-ttl": 86400,
   "redirect-rewrite-rules" : {
   "^/wsmaster/api/(.*)$" : "/api/$1"
   }
}
```

您可以使用`${…}`来替换系统属性。例如`${jboss.server.config.dir}`将替换为`/path/to/Keycloak`。环境变量的替换也通过`env`前缀得到支持，例如。 `${env.MY_ENVIRONMENT_VARIABLE}`。

可以从管理控制台获取初始配置文件。 这可以通过打开管理控制台，从菜单中选择`Clients`并单击相应的客户端来完成。 打开客户端页面后，单击`Installation`选项卡，然后选择`Keycloak OIDC JSON`。

以下是每个配置选项的说明：

- realm

  领域的名称。 这是*必需的*

- resource

  应用程序的客户端ID。 每个应用程序都有一个client-id，用于标识应用程序。 这是*必需的*

- realm-public-key

  领域公钥的PEM格式。 您可以从管理控制台获取此信息。 这是*可选的*，不建议设置它。 如果没有设置，适配器将从Keycloak下载它，它将在需要时总是重新下载（例如Keycloak旋转它的键）。 但是，如果设置了realm-public-key，那么适配器永远不会从Keycloak下载新密钥，所以当Keycloak旋转它的密钥时，适配器将会中断。

- auth-server-url

  Keycloak服务器的基本URL。 所有其他Keycloak页面和REST服务端点都源于此。 它的形式通常为`https://host:port/auth`。 这是*必需的*

- ssl-required

  确保与Keycloak服务器之间的所有通信均通过HTTPS进行。 在生产中，这应该设置为“all”。 这是*可选的*。 默认值为*external*表示默认情况下HTTPS是外部请求所必需的。 有效值为“all”，“external”和“none”。

- confidential-port

  Keycloak服务器用于通过SSL/TLS进行安全连接的机密端口。 这是*可选的*。 默认值为*8443*。

- use-resource-role-mappings

  如果设置为true，那么适配器将在令牌内查找用户的应用程序级别角色映射。 如果为false，它将查看用户角色映射的领域级别。 这是*可选的*。 默认值为* false*。

- public-client

  如果设置为true，则适配器将不会将客户端的凭据发送到Keycloak。 这是*可选的*。 默认值为*false*。

- enable-cors

  这使CORS支持成为可能。 它将处理CORS预检请求。 它还将查看访问令牌以确定有效的来源。 这是*可选的*。 默认值为*false*。

- cors-max-age

  如果启用了CORS，则设置`Access-Control-Max-Age`标头的值。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-allowed-methods

  如果启用了CORS，则设置`Access-Control-Allow-Methods`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-allowed-headers

  如果启用了CORS，则设置`Access-Control-Allow-Headers`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- cors-exposed-headers

  如果启用了CORS，则设置`Access-Control-Expose-Headers`标头的值。 这应该是逗号分隔的字符串。 这是*可选的*。 如果未设置，则在CORS响应中不返回此标头。

- bearer-only

  对于服务，应将其设置为*true*。 如果启用，适配器将不会尝试对用户进行身份验证，而只会验证承载令牌。 这是*可选的*。 默认值为*false*。

- autodetect-bearer-only

  如果您的应用程序同时提供Web应用程序和Web服务（例如SOAP或REST），则应将其设置为*true*。 它允许您将未经身份验证的Web应用程序用户重定向到Keycloak登录页面，但是将HTTP`401`状态代码发送给未经身份验证的SOAP或REST客户端，因为他们无法理解重定向到登录页面。 Keycloak基于典型的标题自动检测SOAP或REST客户端，如`X-Requested-With`，`SOAPAction` 或 `Accept`。 默认值为*false*。

- enable-basic-auth

  这告诉适配器也支持基本身份验证。 如果启用此选项，则还必须提供*secret*。 这是*选的*。 默认值为*false*。

- expose-token

  如果是`true`，则经过身份验证的浏览器客户端（通过JavaScript HTTP调用）可以通过URL `root/k_query_bearer_token`获取签名访问令牌。 这是*可选的*。 默认值为*false*。

- credentials

  指定应用程序的凭据。 这是一个对象表示法，其中键是凭证类型，值是凭证类型的值。 目前支持密码和jwt。 这是*必须的*仅适用于具有'Confidential(机密)'访问类型的客户。

- connection-pool-size

  适配器将对Keycloak服务器进行单独的HTTP调用，以将访问代码转换为访问令牌。 此配置选项定义应该合并到Keycloak服务器的连接数。 这是*可选的*。 默认值为`20`。

- disable-trust-manager

  如果Keycloak服务器需要HTTPS并且此配置选项设置为true，则不必指定信任库。 此设置仅应在开发期间使用，**永远**不要在生产中使用，因为它将禁用SSL证书的验证。 这是*可选的*。 默认值为`false`。

- allow-any-hostname

  如果Keycloak服务器需要HTTPS并且此配置选项设置为`true`，则通过信任库验证Keycloak服务器的证书，但不会进行主机名验证。 此设置仅应在开发期间使用，**永远**不要在生产中使用，因为它将禁用SSL证书的验证。 此测试在测试环境中可能很有用。这是*可选的*。 默认值为`false`。

- proxy-url

  HTTP代理的URL（如果使用）。

- truststore

  该值是信任库文件的文件路径。 如果在路径前加上`classpath:`，那么将从部署的类路径中获取信任库。 用于与Keycloak服务器的传出HTTPS通信。 发出HTTPS请求的客户端需要一种方法来验证他们正在与之通信的服务器的主机。 这就是委托人所做的。 密钥库包含一个或多个可信主机证书或证书颁发机构。 您可以通过提取Keycloak服务器的SSL密钥库的公共证书来创建此信任库。 这是* 必须的*，除非`ssl-required`是`none`或`disable-trust-manager` 是 `true`。

- truststore-password

  信任库的密码。 如果设置了`truststore`并且信任库需要密码，那么这是*必须的*。

- client-keystore

  这是密钥库文件的文件路径。 当适配器向Keycloak服务器发出HTTPS请求时，此密钥库包含双向SSL的客户端证书。 这是*可选的*。

- client-keystore-password

  客户端密钥库的密码。 如果设置了`client-keystore`，这是*必须的*。

- client-key-password

  客户密钥的密码。 如果设置了`client-keystore`，这是*必须的*。

- always-refresh-token

  如果*true*，则适配器将在每个请求中刷新令牌。

- register-node-at-startup

  如果*true*，则适配器将向Keycloak发送注册请求。 它默认为*false*，仅在应用程序集群时才有用。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- register-node-period

  重新注册适配器到Keycloak的期限。 应用程序集群时很有用。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- token-store

  可能的值是*session*和*cookie*。 默认为*session*，这意味着适配器在HTTP会话中存储帐户信息。 如果是*cookie*表示在cookie中存储信息。 有关详细信息，请参阅[应用程序群集](https://www.keycloak.org/docs/latest/securing_apps/index.html#_applicationclustering)

- token-cookie-path

  使用cookie存储时，此选项设置用于存储帐户信息的cookie的路径。 如果它是相对路径，则假定应用程序在上下文根中运行，并且相对于该上下文根进行解释。 如果它是绝对路径，则绝对路径用于设置cookie路径。 默认使用相对于上下文根的路径。

- principal-attribute

  使用OpenID Connect ID Token属性填充UserPrincipal名称。 如果token属性为null，则默认为`sub`。 可能的值是`sub`，`preferred_username`，`email`，`name`，`nickname`，`given_name`，`family_name`。

- turn-off-change-session-id-on-login

  默认情况下，在某些平台上成功登录时会更改会话ID以插入安全攻击向量。 如果要将其关闭，请将此更改为true这是*可选的*。 默认值为*false*。

- token-minimum-time-to-live

  在Keycloak服务器到期之前使用Keycloak服务器抢先刷新活动访问令牌的时间（以秒为单位）。 当访问令牌被发送到另一个REST客户端时，这在它可能在评估之前到期时特别有用。 该值不应超过领域的访问令牌寿命。 这是*可选的*。 默认值为`0`秒，因此适配器将刷新访问令牌，如果它已过期。

- min-time-between-jwks-requests

  指定Keycloak检索新公钥的两个请求之间的最小间隔的时间量（以秒为单位）。 默认为10秒。 当适配器识别带有未知`kid`的令牌时，它总是会尝试下载新的公钥。 但是，它不会每10秒尝试一次（默认情况下）。 这是为了避免DoS，当攻击者发送大量带有错误`kid`强制适配器的令牌向Keycloak发送大量请求时。

- public-key-cache-ttl

  指定Keycloak检索新公钥的两个请求之间的最大间隔的时间量（以秒为单位）。 默认为86400秒（1天）。 当适配器识别带有未知`kid`的令牌时，它总是会尝试下载新的公钥。 如果它识别已知`kid`的令牌，它将只使用先前下载的公钥。 但是，每个此配置的间隔（默认为1天）至少一次将是新的公钥，即使令牌的`kid`已知，也会一直下载。

- ignore-oauth-query-parameter

  默认为`false`，如果设置为`true`将关闭处理承载令牌处理的`access_token`查询参数。 如果用户只传入`access_token`，他们将无法进行身份验证

- redirect-rewrite-rules

  如果需要，请指定重定向URI重写规则。 这是一个对象表示法，其中键是要与Redirect URI匹配的正则表达式，值是替换String。 `$`字符可用于替换String中的反向引用。

- verify-token-audience

  如果设置为“true”，则在使用承载令牌进行身份验证期间，适配器将验证令牌是否包含此客户端名称（资源）作为受众。 该选项对于主要服务于由承载令牌验证的请求的服务特别有用。 默认设置为`false`，但为了提高安全性，建议启用此功能。 有关受众群体支持的详细信息，请参阅[受众群体支持](https://www.keycloak.org/docs/6.0/server_admin/#_audience)。

#### 2.1.2. JBoss EAP/WildFly 适配器 {#JBoss_EAP_WildFly_Adapter}

为了能够保护部署在JBoss EAP，WildFly或JBoss AS上的WAR应用程序，您必须安装和配置Keycloak适配器子系统。 然后，您有两个选项来保护您的WAR。

您可以在WAR中提供适配器配置文件，并在web.xml中将auth-method更改为KEYCLOAK。

或者，您根本不必修改WAR，并且可以通过配置文件中的Keycloak适配器子系统配置（例如`standalone.xml`）来保护它。 本节将介绍这两种方法。

##### 安装适配器 {#Installing_the_adapter}

根据您使用的服务器版本，适配器可作为单独的存档提供。

> 我们只测试和维护适配器，并在发布时提供最新版本的WildFly。 一旦发布了新版本的WildFly，当前的适配器将被弃用，并且在下一个WildFly版本发布后将删除对它们的支持。 另一种方法是将应用程序从WildFly切换到JBoss EAP，因为JBoss EAP适配器的支持时间更长。

安装WildFly 9或更新版本:

```bash
$ cd $WILDFLY_HOME
$ unzip keycloak-wildfly-adapter-dist-6.0.1.zip
```

安装WildFly 8:

```bash
$ cd $WILDFLY_HOME
$ unzip keycloak-wf8-adapter-dist-6.0.1.zip
```

安装JBoss EAP 7:

```bash
$ cd $EAP_HOME
$ unzip keycloak-eap7-adapter-dist-6.0.1.zip
```

安装JBoss EAP 6:

```bash
$ cd $EAP_HOME
$ unzip keycloak-eap6-adapter-dist-6.0.1.zip
```

安装JBoss AS 7.1:

```bash
$ cd $JBOSS_HOME
$ unzip keycloak-as7-adapter-dist-6.0.1.zip
```

此ZIP存档包含特定于Keycloak适配器的JBoss模块。 它还包含JBoss CLI脚本以配置适配器子系统。

要在服务器未运行时配置适配器子系统，请执行：

> 或者，您可以在从命令行安装适配器时指定`server.config`属性，以使用其他配置安装适配器，例如：`-Dserver.config=standalone-ha.xml`。

WildFly 11 或者 更新版本

```bash
$ ./bin/jboss-cli.sh --file=bin/adapter-elytron-install-offline.cli
```

WildFly 10 或者 更旧版本

```bash
$ ./bin/jboss-cli.sh --file=bin/adapter-install-offline.cli
```

> 可以在WildFly 11或更新版本上使用传统的非Elytron适配器，这意味着即使在这些版本上也可以使用`adapter-install-offline.cli`。 但是，我们建议使用较新的Elytron适配器。

或者，如果服务器正在运行，则执行：

WildFly 11 或者 更新版本

```bash
$ ./bin/jboss-cli.sh -c --file=bin/adapter-elytron-install.cli
```

WildFly 10 或者 更旧版本

```bash
$ ./bin/jboss-cli.sh -c --file=bin/adapter-install.cli
```

##### JBoss SSO {#JBoss_SSO}
WildFly内置支持部署到同一WildFly实例的Web应用程序的单点登录。 使用Keycloak时不应启用此功能。

##### 每个WAR配置必需 {#WAR}
本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

此配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#java_adapter_config) 部分中进行了描述。

接下来，您必须在`web.xml`中将`auth-method`设置为`KEYCLOAK`。 您还必须使用标准servlet安全性来指定URL上的角色基础约束。

这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

    <module-name>application</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Admins</web-resource-name>
            <url-pattern>/admin/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>admin</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/customers/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

##### 通过适配器子系统保护WAR {#WAR}
您不必修改WAR以使用Keycloak保护它。 相反，您可以通过Keycloak适配器子系统从外部保护它。 虽然您不必将KEYCLOAK指定为`auth-method`，但仍需要在`web.xml`中定义`security-constraints`。 但是，您不必创建`WEB-INF/keycloak.json`文件。 而是在Keycloak子系统定义中的服务器配置（即`standalone.xml`）中定义此元数据。

```xml
<extensions>
  <extension module="org.keycloak.keycloak-adapter-subsystem"/>
</extensions>

<profile>
  <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
     <secure-deployment name="WAR MODULE NAME.war">
        <realm>demo</realm>
        <auth-server-url>http://localhost:8081/auth</auth-server-url>
        <ssl-required>external</ssl-required>
        <resource>customer-portal</resource>
        <credential name="secret">password</credential>
     </secure-deployment>
  </subsystem>
</profile>
```

`secure-deployment``name`属性标识要保护的WAR。 它的值是`web.xml`中定义的`module-name`，附加了`.war`。 其余的配置几乎与[Java adapter configuration](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)中定义的`keycloak.json`配置选项一一对应。

例外是`credential`元素。

为了方便您，您可以转到Keycloak管理控制台并转到此WAR所对应的应用程序的客户端/安装选项卡。 它提供了一个可以剪切和粘贴的示例XML文件。

如果您有多个由同一领域保护的部署，则可以在单独的元素中共享领域配置。 例如：

```xml
<subsystem xmlns="urn:jboss:domain:keycloak:1.1">
    <realm name="demo">
        <auth-server-url>http://localhost:8080/auth</auth-server-url>
        <ssl-required>external</ssl-required>
    </realm>
    <secure-deployment name="customer-portal.war">
        <realm>demo</realm>
        <resource>customer-portal</resource>
        <credential name="secret">password</credential>
    </secure-deployment>
    <secure-deployment name="product-portal.war">
        <realm>demo</realm>
        <resource>product-portal</resource>
        <credential name="secret">password</credential>
    </secure-deployment>
    <secure-deployment name="database.war">
        <realm>demo</realm>
        <resource>database-service</resource>
        <bearer-only>true</bearer-only>
    </secure-deployment>
</subsystem>
```

##### 安全域

要将安全上下文传播到EJB层，您需要将其配置为使用“keycloak”安全域。 这可以通过@SecurityDomain注释来实现：

```java
import org.jboss.ejb3.annotation.SecurityDomain;
...

@Stateless
@SecurityDomain("keycloak")
public class CustomerService {

    @RolesAllowed("user")
    public List<String> getCustomers() {
        return db.getCustomers();
    }
}
```

#### 2.1.3. 从RPM安装JBoss EAP适配器 {#Installing_JBoss_EAP_Adapter_from_an_RPM}

从RPM安装EAP 7适配器：

> 在Red Hat Enterprise Linux 7中，`term channel`被替换为`term repository`。 在这些说明中，仅使用`term repository`。

您必须先订阅JBoss EAP 7.2存储库，然后才能从RPM安装EAP 7适配器。

先决条件

1. 确保您的Red Hat Enterprise Linux系统已使用Red Hat Subscription Manager注册到您的帐户。 有关更多信息，请参阅[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须先取消订阅该存储库。

使用Red Hat订阅管理器，使用以下命令订阅JBoss EAP 7.2存储库。根据您的Red Hat Enterprise Linux版本，将<rhel_version>替换为6或7。</rhel_version>

```bash
$ sudo subscription-manager repos --enable=jb-eap-7-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令安装OIDC的EAP 7适配器:

```bash
$ sudo yum install eap7-keycloak-adapter-sso7_3
```

> RPM安装的默认EAP_HOME路径是:/opt/rh/eap7/root/usr/share/wildfly。

运行适当的模块安装脚本。

对于OIDC模块，输入以下命令:

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install.cli
```

安装完成。

从RPM安装EAP 6适配器:

> 在Red Hat Enterprise Linux 7中，`term channel`被`term repository`替换。在这些指令中，只使用`term repository`。

在从RPM安装EAP 6适配器之前，必须订阅JBoss EAP 6.0存储库。

先决条件

1. 确保您的Red Hat Enterprise Linux系统使用Red Hat Subscription Manager注册到您的帐户。有关更多信息，请参见[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须首先从该存储库取消订阅。

使用Red Hat订阅管理器，使用以下命令订阅JBoss EAP 6.0存储库。根据您的Red Hat Enterprise Linux版本，将<rhel_version>替换为6或7。</rhel_version>

```bash
$ sudo subscription-manager repos --enable=jb-eap-6-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令安装OIDC的EAP 6适配器:

```bash
$ sudo yum install keycloak-adapter-sso7_3-eap6
```

> RPM安装的默认EAP_HOME路径是/opt/rh/eap6/root/usr/share/wildfly。 

运行适当的模块安装脚本。

对于OIDC模块，输入以下命令:

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install.cli
```

安装完成。

#### 2.1.4. JBoss Fuse 6 适配器 {#JBoss_Fuse_6_Adapter}

Keycloak支持保护在[JBoss Fuse 6](https://developers.redhat.com/products/fuse/overview/)中运行的web应用程序。

JBoss Fuse 6利用[Jetty 9适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jetty9_adapter)作为JBoss Fuse 6.3.0 Rollup 5与[Jetty 9.2服务器](http://www.eclipse.org/jetty/)捆绑在一起和Jetty用于运行各种Web应用程序。

> Fuse 6唯一受支持的版本是最新版本。 如果使用早期版本的Fuse 6，则某些功能可能无法正常工作。 特别是，[Hawtio](https://hawt.io/)集成不适用于早期版本的Fuse 6。

Fuse支持以下项目的安全性:

- 使用Pax Web WAR Extender在Fuse上部署经典的WAR应用程序
- 使用Pax Web白板扩展器将servlet作为OSGI服务部署在Fuse上
- [Apache Camel](http://camel.apache.org/)使用[Camel Jetty](http://camel.apache.org/jetty.html)组件运行的Jetty端点
- [Apache CXF](http://cxf.apache.org/)端点在自己独立的[Jetty引擎](http://cxf.apache.org/docs/jetty-configuration.html)上运行
- [Apache CXF](http://cxf.apache.org/)在CXF servlet提供的默认引擎上运行的端点
- SSH和JMX管理员访问权限
- [Hawtio管理控制台](https://hawt.io/)

##### 在Fuse 6中保护Web应用程序 {#Securing_Your_Web_Applications_Inside_Fuse_6}

您必须先安装Keycloak Karaf功能。 接下来，您需要根据要保护的应用程序类型执行这些步骤。 所有引用的Web应用程序都需要将Keycloak Jetty身份验证器注入底层Jetty服务器。 实现此目标的步骤取决于应用程序类型。 细节描述如下。

最好的起点是看看作为`fuse`目录中Keycloak示例的一部分捆绑的Fuse演示。 通过测试和理解演示，大多数步骤都应该是可以理解的。

##### 安装Keycloak功能 {#Installing_the_Keycloak_Feature}

您必须首先在JBoss Fuse环境中安装`keycloak`功能。 keycloak功能包括Fuse适配器和所有第三方依赖项。 您可以从Maven存储库或存档中安装它。

###### 从Maven存储库安装 {#Installing_from_the_Maven_Repository}

作为先决条件，您必须在线并且可以访问Maven存储库。

对于社区来说，只要在maven中央存储库中提供所有工件和第三方依赖项就足够了。

要使用Maven存储库安装keycloak功能，请完成以下步骤：

1. 启动JBoss Fuse 6.3.0 Rollup 5; 然后在Karaf终端类型：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

2. 您可能还需要安装Jetty 9功能：

   ```
   features:install keycloak-jetty9-adapter
   ```

3. 确保已安装功能：

   ```
   features:list | grep keycloak
   ```

###### 从ZIP捆绑包安装 {#Installing_from_the_ZIP_bundle}

如果您处于脱机状态或不想使用Maven获取JAR文件和其他工件，这将非常有用。

要从ZIP存档安装Fuse适配器，请完成以下步骤：

1. 下载Keycloak Fuse适配器ZIP存档文件。

2. 将其解压缩到JBoss Fuse的根目录中。 然后将依赖项安装在`system`目录下。 您可以覆盖所有现有的jar文件。

   将此用于JBoss Fuse 6.3.0汇总5：

   ```bash
   cd /path-to-fuse/jboss-fuse-6.3.0.redhat-254
   unzip -q /path-to-adapter-zip/keycloak-fuse-adapter-6.0.1.zip
   ```

3. 启动Fuse并在Fuse/karaf终端中运行以下命令:

   ```bash
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

4. 安装相应的Jetty适配器。 由于artifacts可直接在JBoss Fuse`system`目录中使用，因此您无需使用Maven存储库。

##### 保护经典WAR应用程序 {#Securing_a_Classic_WAR_Application}

保护WAR应用程序所需的步骤如下：

1. 在`/WEB-INF/web.xml`文件中，声明必要的：

   - <security-constraint>元素中的安全性约束

   - <login-config>元素中的登录配置

   - <security-role>元素中的安全角色。

     例如：

   ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <web-app xmlns="http://java.sun.com/xml/ns/javaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
              version="3.0">
     
         <module-name>customer-portal</module-name>
     
         <welcome-file-list>
             <welcome-file>index.html</welcome-file>
         </welcome-file-list>
     
         <security-constraint>
             <web-resource-collection>
                 <web-resource-name>Customers</web-resource-name>
                 <url-pattern>/customers/*</url-pattern>
             </web-resource-collection>
             <auth-constraint>
                 <role-name>user</role-name>
             </auth-constraint>
         </security-constraint>
     
         <login-config>
             <auth-method>BASIC</auth-method>
             <realm-name>does-not-matter</realm-name>
         </login-config>
     
         <security-role>
             <role-name>admin</role-name>
         </security-role>
         <security-role>
             <role-name>user</role-name>
         </security-role>
     </web-app>
   ```

2. 将带有验证器的`jetty-web.xml`文件添加到`/WEB-INF/jetty-web.xml`文件中。

   例如：

   ```xml
   <?xml version="1.0"?>
   <!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN"
    "http://www.eclipse.org/jetty/configure_9_0.dtd">
   <Configure class="org.eclipse.jetty.webapp.WebAppContext">
       <Get name="securityHandler">
           <Set name="authenticator">
               <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
               </New>
           </Set>
       </Get>
   </Configure>
   ```

3. 在WAR的`/WEB-INF/`目录中，创建一个新文件keycloak.json。 该配置文件的格式在[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 部分中进行了描述。 也可以按[配置外部适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#config_external_adapter)中的说明在外部使用此文件。

4. 确保您的WAR应用程序在`Import-Package`标题下的`META-INF/MANIFEST.MF`文件中导入`org.keycloak.adapters.jetty`和更多软件包。 在项目中使用`maven-bundle-plugin`可以在清单中正确生成OSGI头文件。 请注意，包的“*”解析不会导入`org.keycloak.adapters.jetty`包，因为它不是由应用程序或Blueprint或Spring描述符使用，而是在`jetty-web.xml`文件中使用。

   要导入的包的列表可能是这样的:

   ```
   org.keycloak.adapters.jetty;version="6.0.1",
   org.keycloak.adapters;version="6.0.1",
   org.keycloak.constants;version="6.0.1",
   org.keycloak.util;version="6.0.1",
   org.keycloak.*;version="6.0.1",
   *;resolution:=optional
   ```

###### 配置外部适配器 {#Configuring_the_External_Adapter}

如果您不希望将`keycloak.json`适配器配置文件捆绑在WAR应用程序中，而是根据命名约定在外部提供并加载，请使用此配置方法。

要启用该功能，请将此部分添加到`/WEB_INF/web.xml`文件中：

```xml
<context-param>
    <param-name>keycloak.config.resolver</param-name>
    <param-value>org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver</param-value>
</context-param>
```

该组件使用`keycloak.config`或`karaf.etc` java属性来搜索基本文件夹以找到配置。 然后在其中一个文件夹中搜索名为`<your_web_context> -keycloak.json`的文件。

因此，例如，如果您的Web应用程序具有上下文`my-portal`，那么您的适配器配置将从`$FUSE_HOME/etc/my-portal-keycloak.json`文件加载。

##### 保护部署为OSGI服务的Servlet {#Securing_a_Servlet_Deployed_as_an_OSGI_Service}

如果在OSGI捆绑项目中有一个未部署为经典WAR应用程序的servlet类，则可以使用此方法。 Fuse使用Pax Web Whiteboard Extender将这些servlet部署为Web应用程序。

要使用Keycloak保护您的servlet，请完成以下步骤：

1. Keycloak提供了PaxWebIntegrationService，它允许注入jetty-web.xml并为您的应用程序配置安全性约束。 您需要在应用程序内的`OSGI-INF/blueprint/blueprint.xml`文件中声明此类服务。 请注意，您的servlet需要依赖它。 配置示例：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0
              http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd">
   
       <!-- Using jetty bean just for the compatibility with other fuse services -->
       <bean id="servletConstraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
           <property name="constraint">
               <bean class="org.eclipse.jetty.util.security.Constraint">
                   <property name="name" value="cst1"/>
                   <property name="roles">
                       <list>
                           <value>user</value>
                       </list>
                   </property>
                   <property name="authenticate" value="true"/>
                   <property name="dataConstraint" value="0"/>
               </bean>
           </property>
           <property name="pathSpec" value="/product-portal/*"/>
       </bean>
   
       <bean id="keycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.PaxWebIntegrationService"
             init-method="start" destroy-method="stop">
           <property name="jettyWebXmlLocation" value="/WEB-INF/jetty-web.xml" />
           <property name="bundleContext" ref="blueprintBundleContext" />
           <property name="constraintMappings">
               <list>
                   <ref component-id="servletConstraintMapping" />
               </list>
           </property>
       </bean>
   
       <bean id="productServlet" class="org.keycloak.example.ProductPortalServlet" depends-on="keycloakPaxWebIntegration">
       </bean>
   
       <service ref="productServlet" interface="javax.servlet.Servlet">
           <service-properties>
               <entry key="alias" value="/product-portal" />
               <entry key="servlet-name" value="ProductServlet" />
               <entry key="keycloak.config.file" value="/keycloak.json" />
           </service-properties>
       </service>
   
   </blueprint>
   ```

   - 您可能需要在项目中包含`WEB-INF`目录（即使您的项目不是Web应用程序）并创建`/WEB-INF/jetty-web.xml`和`/WEB-INF/keycloak.json`文件，如[经典WAR应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_classic_war)部分。 请注意，您不需要`web.xml`文件，因为在蓝图配置文件中声明了安全性约束。

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须至少包含以下导入：

   ```properties
   org.keycloak.adapters.jetty;version="6.0.1",
   org.keycloak.adapters;version="6.0.1",
   org.keycloak.constants;version="6.0.1",
   org.keycloak.util;version="6.0.1",
   org.keycloak.*;version="6.0.1",
   *;resolution:=optional
   ```

##### 保护Apache Camel应用程序 {#Securing_an_Apache_Camel_Application}

您可以通过添加带有`KeycloakJettyAuthenticator`的securityHandler并注入适当的安全约束来保护使用[camel-jetty](http://camel.apache.org/jetty.html)组件实现的Apache Camel端点。 您可以使用类似以下配置将`OSGI-INF/blueprint/blueprint.xml`文件添加到Camel应用程序中。 角色，安全约束映射和Keycloak适配器配置可能略有不同，具体取决于您的环境和需求。

例如：

```xml
<?xml version="1.0" encoding="UTF-8"?>

<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:camel="http://camel.apache.org/schema/blueprint"
           xsi:schemaLocation="
       http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
       http://camel.apache.org/schema/blueprint http://camel.apache.org/schema/blueprint/camel-blueprint.xsd">

    <bean id="kcAdapterConfig" class="org.keycloak.representations.adapters.config.AdapterConfig">
        <property name="realm" value="demo"/>
        <property name="resource" value="admin-camel-endpoint"/>
        <property name="bearerOnly" value="true"/>
        <property name="authServerUrl" value="http://localhost:8080/auth" />
        <property name="sslRequired" value="EXTERNAL"/>
    </bean>

    <bean id="keycloakAuthenticator" class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
        <property name="adapterConfig" ref="kcAdapterConfig"/>
    </bean>

    <bean id="constraint" class="org.eclipse.jetty.util.security.Constraint">
        <property name="name" value="Customers"/>
        <property name="roles">
            <list>
                <value>admin</value>
            </list>
        </property>
        <property name="authenticate" value="true"/>
        <property name="dataConstraint" value="0"/>
    </bean>

    <bean id="constraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
        <property name="constraint" ref="constraint"/>
        <property name="pathSpec" value="/*"/>
    </bean>

    <bean id="securityHandler" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
        <property name="authenticator" ref="keycloakAuthenticator" />
        <property name="constraintMappings">
            <list>
                <ref component-id="constraintMapping" />
            </list>
        </property>
        <property name="authMethod" value="BASIC"/>
        <property name="realmName" value="does-not-matter"/>
    </bean>

    <bean id="sessionHandler" class="org.keycloak.adapters.jetty.spi.WrappingSessionHandler">
        <property name="handler" ref="securityHandler" />
    </bean>

    <bean id="helloProcessor" class="org.keycloak.example.CamelHelloProcessor" />

    <camelContext id="blueprintContext"
                  trace="false"
                  xmlns="http://camel.apache.org/schema/blueprint">
        <route id="httpBridge">
            <from uri="jetty:http://0.0.0.0:8383/admin-camel-endpoint?handlers=sessionHandler&amp;matchOnUriPrefix=true" />
            <process ref="helloProcessor" />
            <log message="The message from camel endpoint contains ${body}"/>
        </route>
    </camelContext>

</blueprint>
```

- `META-INF/MANIFEST.MF`中的`Import-Package`需要包含这些导入：

```properties
javax.servlet;version="[3,4)",
javax.servlet.http;version="[3,4)",
org.apache.camel.*,
org.apache.camel;version="[2.13,3)",
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.server.nio;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1",
org.osgi.service.blueprint,
org.osgi.service.blueprint.container,
org.osgi.service.event,
```

##### Camel RestDSL {#Camel_RestDSL}

Camel Rest DSL是一种Camel功能，用于以流畅的方式定义REST端点。 但您仍必须使用特定的实现类，并提供有关如何与Keycloak集成的说明。

配置集成机制的方法取决于您为其配置RestDSL定义的路由的Camel组件。

以下示例显示如何使用Jetty组件配置集成，以及对先前Blueprint示例中定义的某些bean的引用。

```xml
<bean id="securityHandlerRest" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
    <property name="authenticator" ref="keycloakAuthenticator" />
    <property name="constraintMappings">
        <list>
            <ref component-id="constraintMapping" />
        </list>
    </property>
    <property name="authMethod" value="BASIC"/>
    <property name="realmName" value="does-not-matter"/>
</bean>

<bean id="sessionHandlerRest" class="org.keycloak.adapters.jetty.spi.WrappingSessionHandler">
    <property name="handler" ref="securityHandlerRest" />
</bean>


<camelContext id="blueprintContext"
              trace="false"
              xmlns="http://camel.apache.org/schema/blueprint">

    <restConfiguration component="jetty" contextPath="/restdsl"
                       port="8484">
        <!--the link with Keycloak security handlers happens here-->
        <endpointProperty key="handlers" value="sessionHandlerRest"></endpointProperty>
        <endpointProperty key="matchOnUriPrefix" value="true"></endpointProperty>
    </restConfiguration>

    <rest path="/hello" >
        <description>Hello rest service</description>
        <get uri="/{id}" outType="java.lang.String">
            <description>Just an helllo</description>
            <to uri="direct:justDirect" />
        </get>

    </rest>

    <route id="justDirect">
        <from uri="direct:justDirect"/>
        <process ref="helloProcessor" />
        <log message="RestDSL correctly invoked ${body}"/>
        <setBody>
            <constant>(__This second sentence is returned from a Camel RestDSL endpoint__)</constant>
        </setBody>
    </route>

</camelContext>
```

##### 在单独的Jetty引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_a_Separate_Jetty_Engine}

要在单独的Jetty引擎上运行Keycloak保护的CXF端点，请完成以下步骤：

1. 将`META-INF/spring/beans.xml`添加到您的应用程序中，并在其中使用Jetty SecurityHandler声明`httpj：ngine-factory`，并注入`KeycloakJettyAuthenticator`。 CFX JAX-WS应用程序的配置可能类似于以下内容：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:jaxws="http://cxf.apache.org/jaxws"
          xmlns:httpj="http://cxf.apache.org/transports/http-jetty/configuration"
          xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
           http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd
           http://www.springframework.org/schema/osgi http://www.springframework.org/schema/osgi/spring-osgi.xsd
           http://cxf.apache.org/transports/http-jetty/configuration http://cxf.apache.org/schemas/configuration/http-jetty.xsd">
   
       <import resource="classpath:META-INF/cxf/cxf.xml" />
   
       <bean id="kcAdapterConfig" class="org.keycloak.representations.adapters.config.AdapterConfig">
           <property name="realm" value="demo"/>
           <property name="resource" value="custom-cxf-endpoint"/>
           <property name="bearerOnly" value="true"/>
           <property name="authServerUrl" value="http://localhost:8080/auth" />
           <property name="sslRequired" value="EXTERNAL"/>
       </bean>
   
       <bean id="keycloakAuthenticator" class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
           <property name="adapterConfig">
               <ref local="kcAdapterConfig" />
           </property>
       </bean>
   
       <bean id="constraint" class="org.eclipse.jetty.util.security.Constraint">
           <property name="name" value="Customers"/>
           <property name="roles">
               <list>
                   <value>user</value>
               </list>
           </property>
           <property name="authenticate" value="true"/>
           <property name="dataConstraint" value="0"/>
       </bean>
   
       <bean id="constraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
           <property name="constraint" ref="constraint"/>
           <property name="pathSpec" value="/*"/>
       </bean>
   
       <bean id="securityHandler" class="org.eclipse.jetty.security.ConstraintSecurityHandler">
           <property name="authenticator" ref="keycloakAuthenticator" />
           <property name="constraintMappings">
               <list>
                   <ref local="constraintMapping" />
               </list>
           </property>
           <property name="authMethod" value="BASIC"/>
           <property name="realmName" value="does-not-matter"/>
       </bean>
   
       <httpj:engine-factory bus="cxf" id="kc-cxf-endpoint">
           <httpj:engine port="8282">
               <httpj:handlers>
                   <ref local="securityHandler" />
               </httpj:handlers>
               <httpj:sessionSupport>true</httpj:sessionSupport>
           </httpj:engine>
       </httpj:engine-factory>
   
       <jaxws:endpoint
                       implementor="org.keycloak.example.ws.ProductImpl"
                       address="http://localhost:8282/ProductServiceCF" depends-on="kc-cxf-endpoint" />
   
   </beans>
   ```

   对于CXF JAX-RS应用程序，唯一的区别可能在于依赖于engine-factory的端点配置：

   ```xml
   <jaxrs:server serviceClass="org.keycloak.example.rs.CustomerService" address="http://localhost:8282/rest"
       depends-on="kc-cxf-endpoint">
       <jaxrs:providers>
           <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
       </jaxrs:providers>
   </jaxrs:server>
   ```

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须包含那些导入：

```properties
META-INF.cxf;version="[2.7,3.2)",
META-INF.cxf.osgi;version="[2.7,3.2)";resolution:=optional,
org.apache.cxf.bus;version="[2.7,3.2)",
org.apache.cxf.bus.spring;version="[2.7,3.2)",
org.apache.cxf.bus.resource;version="[2.7,3.2)",
org.apache.cxf.transport.http;version="[2.7,3.2)",
org.apache.cxf.*;version="[2.7,3.2)",
org.springframework.beans.factory.config,
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1"
```

##### 在默认Jetty引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_the_Default_Jetty_Engine}

某些服务会在启动时自动附带已部署的servlet。 一个这样的服务是在`http://localhost:8181/cxf`上下文中运行的CXF servlet。 保护这些端点可能很复杂。 Keycloak目前使用的一种方法是ServletReregistrationService，它在启动时取消部署内置servlet，使您能够在Keycloak保护的上下文中重新部署它。

应用程序中的配置文件`OSGI-INF/blueprint/blueprint.xml`可能类似于下面的那个。 请注意，它添加了JAX-RS`customerservice`端点，它是特定于端点的应用程序，但更重要的是，它保护整个`/cxf`上下文。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:jaxrs="http://cxf.apache.org/blueprint/jaxrs"
           xsi:schemaLocation="
                http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
                http://cxf.apache.org/blueprint/jaxrs http://cxf.apache.org/schemas/blueprint/jaxrs.xsd">

    <!-- JAXRS Application -->

    <bean id="customerBean" class="org.keycloak.example.rs.CxfCustomerService" />

    <jaxrs:server id="cxfJaxrsServer" address="/customerservice">
        <jaxrs:providers>
            <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
        </jaxrs:providers>
        <jaxrs:serviceBeans>
            <ref component-id="customerBean" />
        </jaxrs:serviceBeans>
    </jaxrs:server>


    <!-- Securing of whole /cxf context by unregister default cxf servlet from paxweb and re-register with applied security constraints -->

    <bean id="cxfConstraintMapping" class="org.eclipse.jetty.security.ConstraintMapping">
        <property name="constraint">
            <bean class="org.eclipse.jetty.util.security.Constraint">
                <property name="name" value="cst1"/>
                <property name="roles">
                    <list>
                        <value>user</value>
                    </list>
                </property>
                <property name="authenticate" value="true"/>
                <property name="dataConstraint" value="0"/>
            </bean>
        </property>
        <property name="pathSpec" value="/cxf/*"/>
    </bean>

    <bean id="cxfKeycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.PaxWebIntegrationService"
          init-method="start" destroy-method="stop">
        <property name="bundleContext" ref="blueprintBundleContext" />
        <property name="jettyWebXmlLocation" value="/WEB-INF/jetty-web.xml" />
        <property name="constraintMappings">
            <list>
                <ref component-id="cxfConstraintMapping" />
            </list>
        </property>
    </bean>

    <bean id="defaultCxfReregistration" class="org.keycloak.adapters.osgi.ServletReregistrationService" depends-on="cxfKeycloakPaxWebIntegration"
          init-method="start" destroy-method="stop">
        <property name="bundleContext" ref="blueprintBundleContext" />
        <property name="managedServiceReference">
            <reference interface="org.osgi.service.cm.ManagedService" filter="(service.pid=org.apache.cxf.osgi)" timeout="5000"  />
        </property>
    </bean>

</blueprint>
```

因此，在默认CXF HTTP目标上运行的所有其他CXF服务也是安全的。 类似地，当取消部署应用程序时，整个`/cxf`上下文也变得不安全。 因此，如[在单独的Jetty引擎上安全CXF应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_cxf_separate)中所述，为您的应用程序使用自己的Jetty引擎，然后为您提供 更好地控制每个应用程序的安全性。

- `WEB-INF`目录可能需要在您的项目中（即使您的项目不是Web应用程序）。 您可能还需要以与[经典WAR应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter_classic_war)类似的方式编辑`/WEB-INF/jetty-web.xml`和`/WEB-INF/keycloak.json`文件。 请注意，您不需要`web.xml`文件，因为蓝图配置文件中声明了安全性约束。
- `META-INF/MANIFEST.MF`中的`Import-Package`必须包含以下导入：

```properties
META-INF.cxf;version="[2.7,3.2)",
META-INF.cxf.osgi;version="[2.7,3.2)";resolution:=optional,
org.apache.cxf.transport.http;version="[2.7,3.2)",
org.apache.cxf.*;version="[2.7,3.2)",
com.fasterxml.jackson.jaxrs.json;version="[2.5,3)",
org.eclipse.jetty.security;version="[9,10)",
org.eclipse.jetty.util.security;version="[9,10)",
org.keycloak.*;version="6.0.1",
org.keycloak.adapters.jetty;version="6.0.1",
*;resolution:=optional
```

##### 安全Fuse管理服务 {#Securing_Fuse_Administration_Services}

###### 使用SSH身份验证来融合终端 {#Using_SSH_Authentication_to_Fuse_Terminal}

Keycloak主要处理用于Web应用程序身份验证的用例; 但是，如果您的其他Web服务和应用程序受Keycloak保护，则使用Keycloak凭据保护非Web管理服务（如SSH）是最佳实践。 您可以使用JAAS登录模块执行此操作，该模块允许远程连接到Keycloak并根据[资源所有者密码凭据](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)验证凭据。

要启用SSH身份验证，请完成以下步骤：

1. 在Keycloak中创建一个客户端（例如，`ssh-jmx-admin-client`），它将用于SSH身份验证。 此客户端需要将`Direct Access Grants Enabled`选为`On`。

2. 在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`文件中，更新或指定此属性：

   ```properties
   sshRealm=keycloak
   ```

3. 添加`$FUSE_HOME/etc/keycloak-direct-access.json`文件，其内容类似于以下内容（基于您的环境和Keycloak客户端设置）：

   ```json
   {
       "realm": "demo",
       "resource": "ssh-jmx-admin-client",
       "ssl-required" : "external",
       "auth-server-url" : "http://localhost:8080/auth",
       "credentials": {
           "secret": "password"
       }
   }
   ```

   此文件指定客户端应用程序配置，该命令由来自`keycloak` JAAS领域的JAAS DirectAccessGrantsLoginModule用于SSH身份验证。

4. 启动Fuse并安装`keycloak` JAAS领域。 最简单的方法是安装`keycloak-jaas`功能，它具有预定义的JAAS领域。 您可以使用自己的`keycloak` JAAS领域覆盖该功能的预定义领域。 有关详细信息，请参阅[JBoss Fuse文档](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html-single/security_guide/#ESBSecureContainer)。

   在Fuse终端中使用以下命令：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak-jaas
   ```

5. 通过在终端中键入以下内容，使用SSH作为`admin`用户登录：

   ```bash
   ssh -o PubkeyAuthentication=no -p 8101 admin@localhost
   ```

6. 使用密码`password`登录。

> 在某些更高版本的操作系统上，您可能还需要使用SSH命令的-o选项`-o HostKeyAlgorithms=+ssh-dss`，因为以后的SSH客户端默认情况下不允许使用`ssh-dss`算法。 但是，默认情况下，它目前用于JBoss Fuse 6.3.0 Rollup 5。

请注意，用户需要具有领域角色`admin`来执行所有操作或其他角色来执行操作子集（例如，**viewer**角色限制用户仅运行只读的Karaf命令）。 可用角色在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`或`$FUSE_HOME/etc/system.properties`中配置。

###### 使用JMX身份验证 {#Using_JMX_Authentication}

如果要使用jconsole或其他外部工具通过RMI远程连接到JMX，则可能需要JMX身份验证。 否则最好使用hawt.io/jolokia，因为jolokia代理默认安装在hawt.io中。 有关详细信息，请参阅[Hawtio管理控制台](https://www.keycloak.org/docs/latest/securing_apps/index.html#_hawtio)。

要使用JMX身份验证，请完成以下步骤：

1. 在`$FUSE_HOME/etc/org.apache.karaf.management.cfg`文件中，将jmxRealm属性更改为：

   ```properties
   jmxRealm=keycloak
   ```

2. 安装`keycloak-jaas`功能并配置`$FUSE_HOME/etc/keycloak-direct-access.json`文件，如上面的SSH部分所述。

3. 在jconsole中，您可以使用以下URL：

```
service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root
```

和凭据：admin/password（根据您的环境，具有管理员权限的用户）。

##### 保护Hawtio管理控制台 {#Securing_the_Hawtio_Administration_Console}

要使用Keycloak保护Hawtio管理控制台，请完成以下步骤:

1. 将这些属性添加到`$FUSE_HOME/etc/system.properties`文件中：

   ```properties
   hawtio.keycloakEnabled=true
   hawtio.realm=keycloak
   hawtio.keycloakClientConfig=file://${karaf.base}/etc/keycloak-hawtio-client.json
   hawtio.rolePrincipalClasses=org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
   ```

2. 在您的领域的Keycloak管理控制台中创建一个客户端。 例如，在Keycloak`demo`领域，创建一个客户端`hawtio-client`，指定`public`作为Access Type，并指定一个指向Hawtio的重定向URI：`http://localhost:8181/hawtio/*`。 您还必须配置相应的Web Origin（在本例中为`http://localhost:8181`）。

3. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc`目录中创建`keycloak-hawtio-client.json`文件。 根据您的Keycloak环境更改`realm`，`resource`和`auth-server-url`属性。 `resource`属性必须指向上一步中创建的客户端。 该文件由客户端（Hawtio JavaScript应用程序）使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "hawtio-client",
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "public-client" : true
   }
   ```

4. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc` 目录中创建`keycloak-hawtio.json`文件。 根据您的Keycloak环境更改`realm`和`auth-server-url`属性。 此文件由服务器（JAAS登录模块）端的适配器使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "jaas",
     "bearer-only" : true,
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "use-resource-role-mappings": false,
     "principal-attribute": "preferred_username"
   }
   ```

5. 启动JBoss Fuse 6.3.0 Rollup 5并安装keycloak功能（如果尚未安装）。 Karaf终端中的命令与此示例类似：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak
   ```

6. 转到<http://localhost:8181/hawtio>并以Keycloak领域的用户身份登录。

   请注意，用户需要具有适当的领域角色才能成功向Hawtio进行身份验证。 可用角色在`hawtio.roles`中的`$FUSE_HOME/etc/system.properties`文件中配置。

###### 保护JBoss EAP 6.4上的Hawtio {#Securing_Hawtio_on_JBoss_EAP_6_4}
要在JBoss EAP 6.4服务器上运行Hawtio，请完成以下步骤:

1. 按照上一节“保护Hawtio管理控制台”中的说明设置Keycloak。 假设：

   - 你有一个Keycloak领域`demo`和客户`hawtio-client`
   - 你的Keycloak在`localhost:8080`上运行
   - 部署了Hawtio的JBoss EAP 6.4服务器将在`localhost:8181`上运行。 具有此服务器的目录在后续步骤中称为`$EAP_HOME`。

2. 将`hawtio-wildfly-1.4.0.redhat-630254.war`存档复制到`$EAP_HOME/standalone/configuration`目录。 有关部署Hawtio的更多详细信息，请参阅[Fuse Hawtio文档](https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.3/html-single/deploying_into_a_web_server/)。

3. 将带有上述内容的`keycloak-hawtio.json`和`keycloak-hawtio-client.json`文件复制到`$EAP_HOME/standalone/configuration`目录。

4. 按照[JBoss适配器文档](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)中的说明，将Keycloak适配器子系统安装到JBoss EAP 6.4服务器上。

5. 在`$EAP_HOME/standalone/configuration/standalone.xml`文件中配置系统属性，如下例所示：

   ```xml
   <extensions>
   ...
   </extensions>
   
   <system-properties>
       <property name="hawtio.authenticationEnabled" value="true" />
       <property name="hawtio.realm" value="hawtio" />
       <property name="hawtio.roles" value="admin,viewer" />
       <property name="hawtio.rolePrincipalClasses" value="org.keycloak.adapters.jaas.RolePrincipal" />
       <property name="hawtio.keycloakEnabled" value="true" />
       <property name="hawtio.keycloakClientConfig" value="${jboss.server.config.dir}/keycloak-hawtio-client.json" />
       <property name="hawtio.keycloakServerConfig" value="${jboss.server.config.dir}/keycloak-hawtio.json" />
   </system-properties>
   ```

6. 将Hawtio域添加到`security-domains`部分中的同一文件：

   ```xml
   <security-domain name="hawtio" cache-type="default">
       <authentication>
           <login-module code="org.keycloak.adapters.jaas.BearerTokenLoginModule" flag="required">
               <module-option name="keycloak-config-file" value="${hawtio.keycloakServerConfig}"/>
           </login-module>
       </authentication>
   </security-domain>
   ```

7. 将`secure-deployment`部分`hawtio`添加到适配器子系统。 这可确保Hawtio WAR能够找到JAAS登录模块类。

   ```xml
   <subsystem xmlns="urn:jboss:domain:keycloak:1.1">
       <secure-deployment name="hawtio-wildfly-1.4.0.redhat-630254.war" />
   </subsystem>
   ```

8. 使用Hawtio重新启动JBoss EAP 6.4服务器：

   ```bash
   cd $EAP_HOME/bin
   ./standalone.sh -Djboss.socket.binding.port-offset=101
   ```

9. 在<http://localhost:8181/hawtio>访问Hawtio。 它由Keycloak保护。

#### 2.1.5. JBoss Fuse 7 适配器 {#JBoss_Fuse_7_Adapter}

Keycloak支持保护在[JBoss Fuse 7](https://developers.redhat.com/products/fuse/overview/)中运行的Web应用程序。

JBoss Fuse 7利用了Undertow适配器，它与[EAP 7 / WildFly适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_jboss_adapter)基本相同，因为捆绑了JBoss Fuse 7.2.0 使用[Undertow HTTP引擎](http://undertow.io/)，Undertow用于运行各种Web应用程序。

> 唯一受支持的Fuse 7版本是最新版本。 如果使用早期版本的Fuse 7，则某些功能可能无法正常工作。 特别是，对于低于7.0.1的Fuse 7版本，集成将完全不起作用。

Fuse支持以下项目的安全性:

- 使用Pax Web War Extender部署在Fuse上的经典WAR应用程序
- 使用Pax Web Whiteboard Extender作为OSGI服务部署在Fuse上的Servlet以及通过org.osgi.service.http.HttpService#registerServlet()注册的servlet，这是标准的OSGi Enterprise HTTP服务
- [Apache Camel](http://camel.apache.org/) 使用[Camel Undertow](http://camel.apache.org/undertow.html)组件运行的端点
- [Apache CXF](http://cxf.apache.org/) 端点在他们自己独立的Undertow引擎上运行
- [Apache CXF](http://cxf.apache.org/) 在CXF servlet提供的默认引擎上运行的端点
- SSH和JMX管理员访问权限
- [Hawtio管理控制台](https://hawt.io/)

##### 在Fuse 7中保护Web应用程序 {#Securing_Your_Web_Applications_Inside_Fuse_7}
您必须先安装Keycloak Karaf功能。 接下来，您需要根据要保护的应用程序类型执行这些步骤。 所有引用的Web应用程序都需要将Keycloak Undertow身份验证机制注入底层Web服务器。 实现此目标的步骤取决于应用程序类型。 细节描述如下。

最好的起点是看看作为`fuse`目录中Keycloak示例的一部分捆绑的Fuse演示。 通过测试和理解演示，大多数步骤都应该是可以理解的。

##### 安装Keycloak功能 {#Installing_the_Keycloak_Feature}

您必须首先在JBoss Fuse环境中安装`keycloak-pax-http-afow`和`keycloak-jaas`功能。 `keycloak-pax-http-undertow`功能包括Fuse适配器和所有第三方依赖项。 `keycloak-jaas`包含用于SSH和JMX身份验证的领域中的JAAS模块。 您可以从Maven存储库或存档中安装它。

###### 从Maven存储库安装 {#Installing_from_the_Maven_Repository}
作为先决条件，您必须在线并且可以访问Maven存储库。

对于社区来说，只要在maven中央存储库中提供所有工件和第三方依赖项就足够了。

要使用Maven存储库安装keycloak功能，请完成以下步骤：

1. 启动JBoss Fuse 7.2.0; 然后在Karaf终端类型：

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

2. 您可能还需要安装Undertow功能：

   ```
   feature:install pax-http-undertow
   ```

3. 确保已安装功能：

```
feature:list | grep keycloak
```

###### 从ZIP捆绑包安装 {#Installing_from_the_ZIP_bundle}
如果您处于脱机状态或不想使用Maven获取JAR文件和其他工件，这将非常有用。

要从ZIP存档安装Fuse适配器，请完成以下步骤：

1. 下载Keycloak Fuse适配器ZIP存档。

2. 将其解压缩到JBoss Fuse的根目录中。 然后将依赖项安装在`system`目录下。 您可以覆盖所有现有的jar文件。

   用于JBoss Fuse 7.2.0：

   ```bash
   cd /path-to-fuse/fuse-karaf-7.z
   unzip -q /path-to-adapter-zip/keycloak-fuse-adapter-6.0.1.zip
   ```

3. 启动Fuse并在Fuse /karaf终端中运行以下命令:

   ```
   feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   feature:install keycloak-pax-http-undertow keycloak-jaas
   ```

4. 安装相应的Undertow适配器。 由于工件可直接在JBoss Fuse`system`目录中使用，因此您无需使用Maven存储库。

##### 保护经典WAR应用程序 {#Securing_a_Classic_WAR_Application}

保护WAR应用程序所需的步骤如下：

1. 在`/WEB-INF/web.xml`文件中，声明必要的：

   - <security-constraint>元素中的安全性约束

   - <login-config>元素中的登录配置。 确保`<auth-method>`是`KEYCLOAK`。

   - <security-role>元素中的安全角色

     例如：

     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <web-app xmlns="http://java.sun.com/xml/ns/javaee"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
              version="3.0">
     
         <module-name>customer-portal</module-name>
     
         <welcome-file-list>
             <welcome-file>index.html</welcome-file>
         </welcome-file-list>
     
         <security-constraint>
             <web-resource-collection>
                 <web-resource-name>Customers</web-resource-name>
                 <url-pattern>/customers/*</url-pattern>
             </web-resource-collection>
             <auth-constraint>
                 <role-name>user</role-name>
             </auth-constraint>
         </security-constraint>
     
         <login-config>
             <auth-method>KEYCLOAK</auth-method>
             <realm-name>does-not-matter</realm-name>
         </login-config>
     
         <security-role>
             <role-name>admin</role-name>
         </security-role>
         <security-role>
             <role-name>user</role-name>
         </security-role>
     </web-app>
     ```

2. 在WAR的`/WEB-INF/`目录中，创建一个新文件keycloak.json。 该配置文件的格式在[Java Adapters Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)部分中进行了描述。 也可以按[配置外部适配器](https://www.keycloak.org/docs/latest/securing_apps/index.html#config_external_adapter)中的说明在外部使用此文件。

   例如：

   ```json
   {
       "realm": "demo",
       "resource": "customer-portal",
       "auth-server-url": "http://localhost:8080/auth",
       "ssl-required" : "external",
       "credentials": {
           "secret": "password"
       }
   }
   ```

3. 与Fuse 6适配器相反，MANIFEST.MF中不需要特殊的OSGi导入。

###### 配置解析器 {#Configuration_Resolvers}
`keycloak.json`适配器配置文件可以存储在捆绑包中，这是默认行为，也可以存储在文件系统的目录中。 要指定配置文件的实际源，请将`keycloak.config.resolver`部署参数设置为所需的配置解析程序类。 例如，在经典的WAR应用程序中，在`web.xml`文件中设置`keycloak.config.resolver`上下文参数，如下所示：

```xml
<context-param>
    <param-name>keycloak.config.resolver</param-name>
    <param-value>org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver</param-value>
</context-param>
```

以下解析器可用于`keycloak.config.resolver`：

- org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver

  这是默认的解析器。 预期配置文件位于受保护的OSGi包中。 默认情况下，它加载名为`WEB-INF/keycloak.json`的文件，但可以通过`configLocation`属性配置此文件名。

- org.keycloak.adapters.osgi.PathBasedKeycloakConfigResolver

  此解析程序在`keycloak.config`系统属性指定的文件夹中搜索名为`<your_web_context>-keycloak.json`的文件。 如果未设置`keycloak.config`，则使用`karaf.etc`系统属性。例如，如果您的Web应用程序部署到上下文`my-portal`中，那么您的适配器配置将从`${keycloak.config}/my-portal-keycloak.json`文件加载，或来自`${karaf.etc}/my-portal-keycloak.json`。

- org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

  这个解析器类似于上面的`PathBasedKeycloakConfigResolver`，对于给定的URI路径，配置位置会从最特定到最不特定地进行检查。例如，对于`/my/web-app/context`URI，将搜索以下配置位置，直到第一个配置位置存在:`${karaf.etc}/my-web-app-context-keycloak.json``${karaf.etc}/my-web-app-keycloak.json``${karaf.etc}/my-keycloak.json``${karaf.etc}/keycloak.json`

##### 保护部署为OSGI服务的Servlet {#Securing_a_Servlet_Deployed_as_an_OSGI_Service}

如果在OSGI捆绑项目中有一个未部署为经典WAR应用程序的servlet类，则可以使用此方法。 Fuse使用Pax Web Whiteboard Extender将这些servlet部署为Web应用程序。

要使用Keycloak保护您的servlet，请完成以下步骤：

1. Keycloak提供了`org.keycloak.adapters.osgi.undertow.PaxWebIntegrationService`，它允许为您的应用程序配置身份验证方法和安全性约束。 您需要在应用程序内的`OSGI-INF/blueprint/blueprint.xml`文件中声明此类服务。 请注意，您的servlet需要依赖它。 配置示例：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd">
   
       <bean id="servletConstraintMapping" class="org.keycloak.adapters.osgi.PaxWebSecurityConstraintMapping">
           <property name="roles">
               <list>
                   <value>user</value>
               </list>
           </property>
           <property name="authentication" value="true"/>
           <property name="url" value="/product-portal/*"/>
       </bean>
   
       <!-- This handles the integration and setting the login-config and security-constraints parameters -->
       <bean id="keycloakPaxWebIntegration" class="org.keycloak.adapters.osgi.undertow.PaxWebIntegrationService"
             init-method="start" destroy-method="stop">
           <property name="bundleContext" ref="blueprintBundleContext" />
           <property name="constraintMappings">
               <list>
                   <ref component-id="servletConstraintMapping" />
               </list>
           </property>
       </bean>
   
       <bean id="productServlet" class="org.keycloak.example.ProductPortalServlet" depends-on="keycloakPaxWebIntegration" />
   
       <service ref="productServlet" interface="javax.servlet.Servlet">
           <service-properties>
               <entry key="alias" value="/product-portal" />
               <entry key="servlet-name" value="ProductServlet" />
               <entry key="keycloak.config.file" value="/keycloak.json" />
           </service-properties>
       </service>
   </blueprint>
   ```

   - 您可能需要在项目中包含`WEB-INF`目录（即使您的项目不是Web应用程序）并创建`/WEB-INF/keycloak.json`文件，如[Classic WAR application](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_classic_war)中所述部分。 请注意，您不需要`web.xml`文件，因为在蓝图配置文件中声明了安全性约束。

2. 与Fuse 6适配器相反，MANIFEST.MF中不需要特殊的OSGi导入。

##### 保护Apache Camel应用程序 {#Securing_an_Apache_Camel_Application}

您可以通过蓝图注入适当的安全约束并将使用过的组件更新为`undertow-keycloak`来保护使用[camel-undertow](http://camel.apache.org/undertow.html)组件实现的Apache Camel端点。 您必须使用类似以下配置将`OSGI-INF/blueprint/blueprint.xml`文件添加到Camel应用程序中。 角色和安全性约束映射以及适配器配置可能略有不同，具体取决于您的环境和需求。

与标准的`undertow`组件相比，`undertow-keycloak`组件增加了两个新属性：

- `configResolver`是一个解析器bean，提供Keycloak适配器配置。 可用的解析器列在[配置解析器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter)部分中。
- `allowedRoles`是以逗号分隔的角色列表。 访问服务的用户必须至少具有一个允许访问的角色。

例如：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:camel="http://camel.apache.org/schema/blueprint"
           xsi:schemaLocation="
       http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
       http://camel.apache.org/schema/blueprint http://camel.apache.org/schema/blueprint/camel-blueprint-2.17.1.xsd">

    <bean id="keycloakConfigResolver" class="org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver" >
        <property name="bundleContext" ref="blueprintBundleContext" />
    </bean>

    <bean id="helloProcessor" class="org.keycloak.example.CamelHelloProcessor" />

    <camelContext id="blueprintContext"
                  trace="false"
                  xmlns="http://camel.apache.org/schema/blueprint">

        <route id="httpBridge">
            <from uri="undertow-keycloak:http://0.0.0.0:8383/admin-camel-endpoint?matchOnUriPrefix=true&amp;configResolver=#keycloakConfigResolver&amp;allowedRoles=admin" />
            <process ref="helloProcessor" />
            <log message="The message from camel endpoint contains ${body}"/>
        </route>

    </camelContext>

</blueprint>
```

- `META-INF/MANIFEST.MF`中的`Import-Package`需要包含这些导入：

```properties
javax.servlet;version="[3,4)",
javax.servlet.http;version="[3,4)",
javax.net.ssl,
org.apache.camel.*,
org.apache.camel;version="[2.13,3)",
io.undertow.*,
org.keycloak.*;version="6.0.1",
org.osgi.service.blueprint,
org.osgi.service.blueprint.container
```

##### Camel RestDSL {#Camel_RestDSL}
Camel Rest DSL是一种Camel功能，用于以流畅的方式定义REST端点。 但您仍必须使用特定的实现类，并提供有关如何与Keycloak集成的说明。

配置集成机制的方法取决于您为其配置RestDSL定义的路由的Camel组件。

以下示例显示如何使用`undertow-keycloak`组件配置集成，并引用前一个Blueprint示例中定义的一些bean。

```xml
<camelContext id="blueprintContext"
              trace="false"
              xmlns="http://camel.apache.org/schema/blueprint">

    <!--the link with Keycloak security handlers happens by using undertow-keycloak component -->
    <restConfiguration apiComponent="undertow-keycloak" contextPath="/restdsl" port="8484">
        <endpointProperty key="configResolver" value="#keycloakConfigResolver" />
        <endpointProperty key="allowedRoles" value="admin,superadmin" />
    </restConfiguration>

    <rest path="/hello" >
        <description>Hello rest service</description>
        <get uri="/{id}" outType="java.lang.String">
            <description>Just a hello</description>
            <to uri="direct:justDirect" />
        </get>

    </rest>

    <route id="justDirect">
        <from uri="direct:justDirect"/>
        <process ref="helloProcessor" />
        <log message="RestDSL correctly invoked ${body}"/>
        <setBody>
            <constant>(__This second sentence is returned from a Camel RestDSL endpoint__)</constant>
        </setBody>
    </route>

</camelContext>
```

##### 在单独的Undertow引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_a_Separate_Undertow_Engine}

要在单独的Undertow引擎上运行Keycloak保护的CXF端点，请完成以下步骤：

1. 将`OSGI-INF/blueprint/blueprint.xml`添加到您的应用程序中，并在其中添加与[Camel配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter_camel)类似的正确配置解析程序bean。 在`httpu:engine-factory`中使用该camel配置声明`org.keycloak.adapters.osgi.undertow.CxfKeycloakAuthHandler`处理程序。 CFX JAX-WS应用程序的配置可能类似于以下内容：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:jaxws="http://cxf.apache.org/blueprint/jaxws"
              xmlns:cxf="http://cxf.apache.org/blueprint/core"
              xmlns:httpu="http://cxf.apache.org/transports/http-undertow/configuration".
              xsi:schemaLocation="
         http://cxf.apache.org/transports/http-undertow/configuration http://cxf.apache.org/schemas/configuration/http-undertow.xsd
         http://cxf.apache.org/blueprint/core http://cxf.apache.org/schemas/blueprint/core.xsd
         http://cxf.apache.org/blueprint/jaxws http://cxf.apache.org/schemas/blueprint/jaxws.xsd">
   
       <bean id="keycloakConfigResolver" class="org.keycloak.adapters.osgi.BundleBasedKeycloakConfigResolver" >
           <property name="bundleContext" ref="blueprintBundleContext" />
       </bean>
   
       <httpu:engine-factory bus="cxf" id="kc-cxf-endpoint">
           <httpu:engine port="8282">
               <httpu:handlers>
                   <bean class="org.keycloak.adapters.osgi.undertow.CxfKeycloakAuthHandler">
                       <property name="configResolver" ref="keycloakConfigResolver" />
                   </bean>
               </httpu:handlers>
           </httpu:engine>
       </httpu:engine-factory>
   
       <jaxws:endpoint implementor="org.keycloak.example.ws.ProductImpl"
                       address="http://localhost:8282/ProductServiceCF" depends-on="kc-cxf-endpoint"/>
   
   </blueprint>
   ```

   For the CXF JAX-RS application, the only difference might be in the configuration of the endpoint dependent on engine-factory:

   ```
   <jaxrs:server serviceClass="org.keycloak.example.rs.CustomerService" address="http://localhost:8282/rest"
       depends-on="kc-cxf-endpoint">
       <jaxrs:providers>
           <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
       </jaxrs:providers>
   </jaxrs:server>
   ```

2. `META-INF/MANIFEST.MF`中的`Import-Package`必须包含那些导入：

```properties
META-INF.cxf;version="[2.7,3.3)",
META-INF.cxf.osgi;version="[2.7,3.3)";resolution:=optional,
org.apache.cxf.bus;version="[2.7,3.3)",
org.apache.cxf.bus.spring;version="[2.7,3.3)",
org.apache.cxf.bus.resource;version="[2.7,3.3)",
org.apache.cxf.transport.http;version="[2.7,3.3)",
org.apache.cxf.*;version="[2.7,3.3)",
org.springframework.beans.factory.config,
org.keycloak.*;version="6.0.1"
```

##### 在默认的Undertow引擎上保护Apache CXF端点 {#Securing_an_Apache_CXF_Endpoint_on_the_Default_Undertow_Engine}

某些服务会在启动时自动附带已部署的servlet。 一个这样的服务是在http:// localhost:8181/cxf上下文中运行的CXF servlet。 Fuse的Pax Web支持通过配置管理改变现有的上下文。 这可用于通过Keycloak保护端点。

应用程序中的配置文件`OSGI-INF/blueprint/blueprint.xml`可能类似于下面的那个。 请注意，它添加了JAX-RS `customerservice`端点，该端点是特定于端点的应用程序。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:jaxrs="http://cxf.apache.org/blueprint/jaxrs"
           xsi:schemaLocation="
                http://www.osgi.org/xmlns/blueprint/v1.0.0 http://www.osgi.org/xmlns/blueprint/v1.0.0/blueprint.xsd
                http://cxf.apache.org/blueprint/jaxrs http://cxf.apache.org/schemas/blueprint/jaxrs.xsd">

    <!-- JAXRS Application -->
    <bean id="customerBean" class="org.keycloak.example.rs.CxfCustomerService" />

    <jaxrs:server id="cxfJaxrsServer" address="/customerservice">
        <jaxrs:providers>
            <bean class="com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider" />
        </jaxrs:providers>
        <jaxrs:serviceBeans>
            <ref component-id="customerBean" />
        </jaxrs:serviceBeans>
    </jaxrs:server>
</blueprint>
```

此外，您必须创建`${karaf.etc}/org.ops4j.pax.web.context-*anyName*.cfg file`。 它将被视为工厂PID配置，由`pax-web-runtime`包跟踪。 此类配置可能包含以下属性，这些属性对应于标准`web.xml`的某些属性：

```properties
bundle.symbolicName = org.apache.cxf.cxf-rt-transports-http
context.id = default

context.param.keycloak.config.resolver = org.keycloak.adapters.osgi.HierarchicalPathBasedKeycloakConfigResolver

login.config.authMethod = KEYCLOAK

security.cxf.url = /cxf/customerservice/*
security.cxf.roles = admin, user
```

有关配置管理文件中可用属性的完整说明，请参阅Fuse文档。 上述属性具有以下含义：

- `bundle.symbolicName` 和 `context.id`

  在`org.ops4j.pax.web.service.WebContainer`中标识bundle及其部署上下文。

- `context.param.keycloak.config.resolver`

  为bundle提供`keycloak.config.resolver`上下文参数的值，与经典WAR中的`web.xml`相同。 可用的解析器在[配置解析器](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_config_external_adapter)部分中进行了描述。

- `login.config.authMethod`

  身份验证方法。 必须是`KEYCLOAK`。

- `security.*anyName*.url` 和 `security.*anyName*.roles`

  各个安全约束的属性值就像在`web.xml`中的`security-constraint/web-resource-collection/url-pattern` 和 `security-constraint/auth-constraint/role-name`中设置的那样， 分别。 角色由逗号和周围的空格分隔。 `* anyName*`标识符可以是任意的，但必须匹配相同安全约束的各个属性。一些Fuse版本包含一个错误，需要将角色分隔为`", "`（逗号和单个空格）。 确保使用这种表示法来分离角色。

`META-INF/MANIFEST.MF`中的`Import-Package`必须至少包含以下导入：

```properties
javax.ws.rs;version="[2,3)",
META-INF.cxf;version="[2.7,3.3)",
META-INF.cxf.osgi;version="[2.7,3.3)";resolution:=optional,
org.apache.cxf.transport.http;version="[2.7,3.3)",
org.apache.cxf.*;version="[2.7,3.3)",
com.fasterxml.jackson.jaxrs.json;version="${jackson.version}"
```

##### 保护Fuse管理服务 {#Securing_Fuse_Administration_Services}

###### 使用SSH身份验证来Fuse终端 {#Using_SSH_Authentication_to_Fuse_Terminal}
Keycloak主要处理用于Web应用程序身份验证的用例; 但是，如果您的其他Web服务和应用程序受Keycloak保护，则使用Keycloak凭据保护非Web管理服务（如SSH）是最佳实践。 您可以使用JAAS登录模块执行此操作，该模块允许远程连接到Keycloak并根据[资源所有者密码凭据](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)验证凭据。

要启用SSH身份验证，请完成以下步骤：

1. 在Keycloak中创建一个客户端（例如，`ssh-jmx-admin-client`），它将用于SSH身份验证。 此客户端需要将`Direct Access Grants Enabled`选为`On`。

2. 在`$FUSE_HOME/etc/org.apache.karaf.shell.cfg`文件中，更新或指定此属性：

   ```properties
   sshRealm=keycloak
   ```

3. 添加`$FUSE_HOME/etc/ keycloak-direct-access.json`文件，其内容类似于以下内容（基于您的环境和Keycloak客户端设置）：

   ```json
   {
       "realm": "demo",
       "resource": "ssh-jmx-admin-client",
       "ssl-required" : "external",
       "auth-server-url" : "http://localhost:8080/auth",
       "credentials": {
           "secret": "password"
       }
   }
   ```

   此文件指定客户端应用程序配置，该命令由来自`keycloak` JAAS领域的JAAS DirectAccessGrantsLoginModule用于SSH身份验证。

4. 启动Fuse并安装`keycloak` JAAS领域。 最简单的方法是安装`keycloak-jaas`功能，它具有预定义的JAAS领域。 您可以使用自己的`keycloak` JAAS领域覆盖该功能的预定义领域。 有关详细信息，请参阅[JBoss Fuse文档](https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/html-single/apache_karaf_security_guide/index#ESBSecureContainer)。

   在Fuse终端中使用以下命令：

   ```
   features:addurl mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
   features:install keycloak-jaas
   ```

5. 通过在终端中键入以下内容，使用SSH作为`admin`用户登录：

   ```bash
   ssh -o PubkeyAuthentication=no -p 8101 admin@localhost
   ```

6. 使用密码`password`登录。

> 在某些更高版本的操作系统上，您可能还需要使用SSH命令的-o选项`-o HostKeyAlgorithms=+ssh-dss`，因为以后的SSH客户端默认情况下不允许使用`ssh-dss`算法。 但是，默认情况下，它目前在JBoss Fuse 7.2.0中使用。

请注意，用户需要具有领域角色`admin`来执行所有操作或其他角色来执行操作子集（例如，**viewer**角色限制用户仅运行只读的Karaf命令）。 可用角色在`$FUSE_HOME/etc/ org.apache.karaf.shell.cfg`或`$FUSE_HOME/etc/system.properties`中配置。

###### 使用JMX身份验证 {#Using_JMX_Authentication}
如果要使用jconsole或其他外部工具通过RMI远程连接到JMX，则可能需要JMX身份验证。 否则最好使用hawt.io/jolokia，因为jolokia代理默认安装在hawt.io中。 有关详细信息，请参阅[Hawtio管理控制台](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_hawtio)。

要使用JMX身份验证，请完成以下步骤：

1. 在`$FUSE_HOME/etc/org.apache.karaf.management.cfg`文件中，将jmxRealm属性更改为：

   ```properties
   jmxRealm=keycloak
   ```

2. 安装`keycloak-jaas`功能并配置`$FUSE_HOME/etc/keycloak-direct-access.json`文件，如上面的SSH部分所述。

3. 在jconsole中，您可以使用以下URL：

```
service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root
```

和凭据：admin/password（根据您的环境，具有管理员权限的用户）。

##### 保护Hawtio管理控制台 {#Securing_the_Hawtio_Administration_Console}

要使用Keycloak保护Hawtio管理控制台，请完成以下步骤：

1. 在您的领域的Keycloak管理控制台中创建一个客户端。 例如，在Keycloak`demo`realm中，创建一个客户端`hawtio-client`，指定`public`作为Access Type，并指定一个指向Hawtio的重定向URI：http://localhost:8181/hawtio/*。 配置相应的Web Origin(在本例中为http://localhost:8181)。 设置客户端范围映射，在`hawtio-client`客户端详细信息中的*scope*选项卡中包含*view-profile* client角色*account* client。

2. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc`目录中创建`keycloak-hawtio-client.json`文件。 根据您的Keycloak环境更改`realm`，`resource`和`auth-server-url`属性。 `resource`属性必须指向上一步中创建的客户端。 该文件由客户端（Hawtio JavaScript应用程序）使用。

   ```json
   {
     "realm" : "demo",
     "clientId" : "hawtio-client",
     "url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "public-client" : true
   }
   ```

3. 使用类似于下面示例中所示的内容在`$ FUSE_HOME/etc`目录中创建`keycloak-direct-access.json`文件。 根据您的Keycloak环境更改`realm`和`url`属性。 该文件由JavaScript客户端使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "ssh-jmx-admin-client",
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "credentials": {
       "secret": "password"
     }
   }
   ```

4. 使用类似于下面示例中所示的内容在`$FUSE_HOME/etc` 目录中创建`keycloak-hawtio.json` 文件。 根据您的Keycloak环境更改`realm`和`auth-server-url`属性。 此文件由服务器（JAAS登录模块）端的适配器使用。

   ```json
   {
     "realm" : "demo",
     "resource" : "jaas",
     "bearer-only" : true,
     "auth-server-url" : "http://localhost:8080/auth",
     "ssl-required" : "external",
     "use-resource-role-mappings": false,
     "principal-attribute": "preferred_username"
   }
   ```

5. 启动JBoss Fuse 7.2.0，[安装Keycloak功能](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_install_feature)。 然后输入Karaf终端：

   ```properties
   system:property -p hawtio.keycloakEnabled true
   system:property -p hawtio.realm keycloak
   system:property -p hawtio.keycloakClientConfig file://\${karaf.base}/etc/keycloak-hawtio-client.json
   system:property -p hawtio.rolePrincipalClasses org.keycloak.adapters.jaas.RolePrincipal,org.apache.karaf.jaas.boot.principal.RolePrincipal
   restart io.hawt.hawtio-war
   ```

6. 转到<http://localhost:8181/hawtio>并以Keycloak领域的用户身份登录。

   请注意，用户需要具有适当的领域角色才能成功向Hawtio进行身份验证。 可用角色在`hawtio.roles`中的`$FUSE_HOME/etc/system.properties`文件中配置。

#### 2.1.6. Spring Boot 适配器 {#Spring_Boot_Adapter}

为了能够保护Spring Boot应用程序，您必须将Keycloak Spring Boot适配器JAR添加到您的应用程序。 然后，您必须通过正常的Spring Boot配置（`application.properties`）提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}

Keycloak Spring Boot适配器利用Spring Boot的自动配置，因此您只需将Keycloak Spring Boot启动器添加到您的项目中即可。 他们的Keycloak Spring Boot Starter也可以从[Spring Start Page](https://start.spring.io/)直接获得。 要使用Maven手动添加它，请将以下内容添加到依赖项中：

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
</dependency>
```

添加适配器BOM依赖项：

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.keycloak.bom</groupId>
      <artifactId>keycloak-adapter-bom</artifactId>
      <version>6.0.1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

目前支持以下嵌入式容器，如果使用Starter，则不需要任何额外的依赖项：

- Tomcat
- Undertow
- Jetty

##### 必需的Spring Boot Adapter配置 {#Required_Spring_Boot_Adapter_Configuration}

本节介绍如何配置Spring Boot应用程序以使用Keycloak。

不需要配置`keycloak.json`文件，您可以通过正常的Spring Boot配置为Spring Boot Keycloak适配器配置域。 例如：

```properties
keycloak.realm = demorealm
keycloak.auth-server-url = http://127.0.0.1:8080/auth
keycloak.ssl-required = external
keycloak.resource = demoapp
keycloak.credentials.secret = 11111111-1111-1111-1111-111111111111
keycloak.use-resource-role-mappings = true
```

您可以通过设置`keycloak.enabled=false`来禁用Keycloak Spring Boot Adapter（例如在测试中）。

要配置策略强制实施器，与keycloak.json不同，必须使用`policy-enforcer-config`而不仅仅是`policy-enforcer`。

您还需要指定通常位于`web.xml`中的Java EE安全配置。 Spring Boot Adapter将`login-method`设置为`KEYCLOAK`并在启动时配置`security-constraints`。 这是一个示例配置：

```properties
keycloak.securityConstraints[0].authRoles[0] = admin
keycloak.securityConstraints[0].authRoles[1] = user
keycloak.securityConstraints[0].securityCollections[0].name = insecure stuff
keycloak.securityConstraints[0].securityCollections[0].patterns[0] = /insecure

keycloak.securityConstraints[1].authRoles[0] = admin
keycloak.securityConstraints[1].securityCollections[0].name = admin stuff
keycloak.securityConstraints[1].securityCollections[0].patterns[0] = /admin
```

> 如果您计划将Spring应用程序部署为WAR，那么不应该使用Spring引导适配器，而应该为您正在使用的应用程序服务器或servlet容器使用专用适配器。您的Spring Boot还应该包含一个 `web.xml`文件。

#### 2.1.7. Tomcat 6, 7 and 8 适配器 {#Tomcat_6_7_and_8_Adapters}

为了能够保护部署在Tomcat 6,7和8上的WAR应用程序，您必须在Tomcat安装中安装Keycloak Tomcat 6,7或8适配器。 然后，您必须在部署到Tomcat的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}

适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将适配器发行版解压到Tomcat的`lib/`目录中。在WEB-INF/lib目录中包含适配器的jar将不起作用!Keycloak适配器实现为一个Valve，而Valve代码必须驻留在Tomcat的主lib/目录中。

```bash
$ cd $TOMCAT_HOME/lib
$ unzip keycloak-tomcat6-adapter-dist.zip
    or
$ unzip keycloak-tomcat7-adapter-dist.zip
    or
$ unzip keycloak-tomcat8-adapter-dist.zip
```

##### 每个WAR配置所需 {#Required_Per_WAR_Configuration}

本节描述如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`META-INF/context.xml`文件。 这是一个特定于Tomcat的配置文件，您必须定义一个Keycloak特定的Valve。

```xml
<Context path="/your-context-path">
    <Valve className="org.keycloak.adapters.tomcat.KeycloakAuthenticatorValve"/>
</Context>
```

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

该配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config)中进行了描述

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 2.1.8. Jetty 9.x 适配器 {#Jetty_9_x_Adapters}

Keycloak有一个单独的适配器，适用于Jetty 9.2.x，Jetty 9.3.x和Jetty 9.4.x，您必须安装到Jetty安装中。 然后，您必须在部署到Jetty的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}
适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将Jetty 9.x发行版解压缩到Jetty 9.x的[基本目录](https://www.eclipse.org/jetty/documentation/current/startup-base-and-home.html)。 在WEB-INF/lib目录中包含适配器的jar将不起作用！ 在下面的例子中，Jetty基本名为`your-base`：

```bash
$ cd your-base
$ unzip keycloak-jetty93-adapter-dist-2.5.0.Final.zip
```

接下来，您必须为Jetty基础启用`keycloak`模块：

```bash
$ java -jar $JETTY_HOME/start.jar --add-to-startd=keycloak
```

##### 每个WAR配置必需 {#Required_Per_WAR_Configuration}
本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`WEB-INF/jetty-web.xml`文件。 这是Jetty特定的配置文件，您必须在其中定义Keycloak特定的身份验证器。

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Get name="securityHandler">
        <Set name="authenticator">
            <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
            </New>
        </Set>
    </Get>
</Configure>
```

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak.json`适配器配置文件。

该配置文件的格式在[Java适配器配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_java_adapter_config) 部分中进行了描述。

> Jetty 9.x适配器将无法找到`keycloak.json`文件。 您必须在`jetty-web.xml`文件中定义所有适配器设置，如下所述。

您可以在`jetty-web.xml`中定义所有内容，而不是使用keycloak.json。 您只需要弄清楚json设置如何与`org.keycloak.representations.adapters.config.AdapterConfig`类匹配。

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
  <Get name="securityHandler">
    <Set name="authenticator">
        <New class="org.keycloak.adapters.jetty.KeycloakJettyAuthenticator">
            <Set name="adapterConfig">
                <New class="org.keycloak.representations.adapters.config.AdapterConfig">
                    <Set name="realm">tomcat</Set>
                    <Set name="resource">customer-portal</Set>
                    <Set name="authServerUrl">http://localhost:8081/auth</Set>
                    <Set name="sslRequired">external</Set>
                    <Set name="credentials">
                        <Map>
                            <Entry>
                                <Item>secret</Item>
                                <Item>password</Item>
                            </Entry>
                        </Map>
                    </Set>
                </New>
            </Set>
        </New>
    </Set>
  </Get>
</Configure>
```

您不必破解打开WAR以使用keycloak保护它。 而是使用yourwar.xml的名称在webapps目录中创建jetty-web.xml文件。 Jetty应该捡起来。 在此模式下，您必须直接在xml文件中声明keycloak.json配置。

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 2.1.9. Spring Security 适配器 {#Spring_Security_Adapter}

要使用Spring Security和Keycloak保护应用程序，请将此适配器添加为项目的依赖项。 然后，您必须在Spring Security配置文件中提供一些额外的bean，并将Keycloak安全过滤器添加到管道中。

与其他Keycloak Adapters不同，您不应在web.xml中配置安全性。 但是，仍然需要keycloak.json。

##### 适配器安装 {#Adapter_Installation}
添加Keycloak Spring Security适配器作为Maven POM或Gradle构建的依赖项。

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-security-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### Spring安全配置 {#Spring_Security_Configuration}
Keycloak Spring Security适配器利用Spring Security灵活的安全配置语法。

###### Java配置 {#Java_Configuration}
Keycloak提供了一个KeycloakWebSecurityConfigurerAdapter作为创建[WebSecurityConfigurer](https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/web/WebSecurityConfigurer.html)实例。 该实现允许通过重写方法进行自定义。 虽然不需要使用它，但它极大地简化了安全上下文配置。

```java
@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    /**
     * Defines the session authentication strategy.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/customers*").hasRole("USER")
                .antMatchers("/admin*").hasRole("ADMIN")
                .anyRequest().permitAll();
    }
}
```

您必须为公共或机密应用程序提供一个会话身份验证策略bean，其类型应为`RegisterSessionAuthenticationStrategy`，对于仅承载应用程序，您必须提供`NullAuthenticatedSessionStrategy`。

目前不支持Spring Security的`SessionFixationProtectionStrategy`，因为它在通过Keycloak登录后更改会话标识符。 如果会话标识符更改，则通用注销将不起作用，因为Keycloak不知道新的会话标识符。

> `@KeycloakConfiguration`注释是一个元数据注释，它定义了在Spring Security中集成Keycloak所需的所有注释。 如果你有一个复杂的Spring Security设置，你可以简单地看一下`@KeycloakConfiguration`注释的注释，并创建你自己的自定义元注释，或者只是为Keycloak适配器使用特定的Spring注释。

###### XML配置 {#XML_Configuration}
虽然Spring Security的XML命名空间简化了配置，但自定义配置可能有点冗长。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:security="http://www.springframework.org/schema/security"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/security
       http://www.springframework.org/schema/security/spring-security.xsd">

    <context:component-scan base-package="org.keycloak.adapters.springsecurity" />

    <security:authentication-manager alias="authenticationManager">
        <security:authentication-provider ref="keycloakAuthenticationProvider" />
    </security:authentication-manager>

    <bean id="adapterDeploymentContext" class="org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean">
        <constructor-arg value="/WEB-INF/keycloak.json" />
    </bean>

    <bean id="keycloakAuthenticationEntryPoint" class="org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint" />
    <bean id="keycloakAuthenticationProvider" class="org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider" />
    <bean id="keycloakPreAuthActionsFilter" class="org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter" />
    <bean id="keycloakAuthenticationProcessingFilter" class="org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter">
        <constructor-arg name="authenticationManager" ref="authenticationManager" />
    </bean>

    <bean id="keycloakLogoutHandler" class="org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler">
        <constructor-arg ref="adapterDeploymentContext" />
    </bean>

    <bean id="logoutFilter" class="org.springframework.security.web.authentication.logout.LogoutFilter">
        <constructor-arg name="logoutSuccessUrl" value="/" />
        <constructor-arg name="handlers">
            <list>
                <ref bean="keycloakLogoutHandler" />
                <bean class="org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler" />
            </list>
        </constructor-arg>
        <property name="logoutRequestMatcher">
            <bean class="org.springframework.security.web.util.matcher.AntPathRequestMatcher">
                <constructor-arg name="pattern" value="/sso/logout**" />
                <constructor-arg name="httpMethod" value="GET" />
            </bean>
        </property>
    </bean>

    <security:http auto-config="false" entry-point-ref="keycloakAuthenticationEntryPoint">
        <security:custom-filter ref="keycloakPreAuthActionsFilter" before="LOGOUT_FILTER" />
        <security:custom-filter ref="keycloakAuthenticationProcessingFilter" before="FORM_LOGIN_FILTER" />
        <security:intercept-url pattern="/customers**" access="ROLE_USER" />
        <security:intercept-url pattern="/admin**" access="ROLE_ADMIN" />
        <security:custom-filter ref="logoutFilter" position="LOGOUT_FILTER" />
    </security:http>

</beans>
```

##### 多租户 {#Multi_Tenancy}
Keycloak Spring Security适配器还支持多租户。 而不是使用`keycloak.json`的路径注入`AdapterDeploymentContextFactoryBean`，您可以注入`KeycloakConfigResolver`接口的实现。 有关如何实现`KeycloakConfigResolver`的更多详细信息，请参见[多租户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy)。

##### 命名安全角色 {#Naming_Security_Roles}
使用基于角色的身份验证时，Spring Security要求角色名称以`ROLE_`开头。 例如，必须在Keycloak中将管理员角色声明为`ROLE_ADMIN`或类似名称，而不仅仅是`ADMIN`。

类`org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider`支持可选的`org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper`，它可用于将来自Keycloak的角色映射到Spring Security认可的角色。 例如，使用`org.springframework.security.core.authority.mapping.SimpleAuthorityMapper`插入`ROLE_`前缀并将角色名称转换为大写。 该类是Spring Security Core模块的一部分。

##### 客户端到客户端的支持 {#Client_to_Client_Support}
为了简化客户端之间的通信，Keycloak提供了Spring的`RestTemplate`的扩展，为您处理承载令牌认证。 要启用此功能，您的安全配置必须添加`KeycloakRestTemplate`bean。 请注意，它必须作为原型确定范围才能正常运行。

对于Java配置：

```java
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    ...

    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public KeycloakRestTemplate keycloakRestTemplate() {
        return new KeycloakRestTemplate(keycloakClientRequestFactory);
    }

    ...
}
```

对于XML配置：

```xml
<bean id="keycloakRestTemplate" class="org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate" scope="prototype">
    <constructor-arg name="factory" ref="keycloakClientRequestFactory" />
</bean>
```

然后，只要需要调用另一个客户端，您的应用程序代码就可以使用`KeycloakRestTemplate`。 例如：

```java
@Service
public class RemoteProductService implements ProductService {

    @Autowired
    private KeycloakRestTemplate template;

    private String endpoint;

    @Override
    public List<String> getProducts() {
        ResponseEntity<String[]> response = template.getForEntity(endpoint, String[].class);
        return Arrays.asList(response.getBody());
    }
}
```

##### Spring Boot集成 {#Spring_Boot_Integration}
可以组合Spring Boot和Spring Security适配器。

如果您使用Keycloak Spring Boot Starter来使用Spring Security适配器，您只需添加Spring Security启动器：

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

###### 使用Spring Boot配置 {#Using_Spring_Boot_Configuration}
默认情况下，Spring Security Adapter会查找`keycloak.json`配置文件。 您可以通过添加此bean来确保它查看Spring Boot Adapter提供的配置：

```java
@Bean
public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
}
```

###### 避免Bean注册两次 {#Avoid_double_bean_registration}
Spring Boot尝试使用Web应用程序上下文急切地注册过滤器bean。 因此，在Spring Boot环境中运行Keycloak Spring Security适配器时，可能需要将`FilterRegistrationBean`添加到安全配置中，以防止Keycloak过滤器被注册两次。

Spring Boot 2.1默认情况下也会禁用`spring.main.allow-bean-definition-overriding`。 这可能意味着如果扩展`KeycloakWebSecurityConfigurerAdapter`的`Configuration`类注册了一个已被`@ComponentScan`检测到的bean，则会遇到`BeanDefinitionOverrideException`。 通过覆盖注册以使用特定于Boot的`@ConditionalOnMissingBean`注释可以避免这种情况，如下面的`HttpSessionManager`。

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter
{
    ...

    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticatedActionsFilterBean(
            KeycloakAuthenticatedActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakSecurityContextRequestFilterBean(
        KeycloakSecurityContextRequestFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }
    ...
}
```

#### 2.1.10. Java Servlet Filter 适配器 {#Java_Servlet_Filter_Adapter}

如果要在没有Keycloak适配器的平台上部署Java Servlet应用程序，则选择使用servlet过滤器适配器。 此适配器的工作方式与其他适配器略有不同。 您没有在web.xml中定义安全性约束。 而是使用Keycloak servlet过滤器适配器定义过滤器映射，以保护要保护的URL模式。

> Backchannel注销与标准适配器的工作方式略有不同。 它不会使HTTP会话无效，而是将会话ID标记为已注销。 没有标准方法可以根据会话ID使HTTP会话无效。

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>application</module-name>

    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.servlet.KeycloakOIDCFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Keycloak Filter</filter-name>
        <url-pattern>/keycloak/*</url-pattern>
        <url-pattern>/protected/*</url-pattern>
    </filter-mapping>
</web-app>
```

在上面的代码片段中有两个url模式。 `/protected/*` 是我们想要保护的文件，而`/keycloak/*` url-pattern处理来自Keycloak服务器的回调。

如果需要在配置的`url-patterns`下面排除一些路径，可以使用Filter init-param`keycloak.config.skipPattern`来配置一个正则表达式，该表达式描述了keycloak过滤器应立即委托给的路径模式。 过滤链。 默认情况下，不配置skipPattern。

模式与`requestURI`匹配，没有`context-path`。 给定context-path `/myapp`，对`/myapp/index.html`的请求将与`/index.html`匹配跳过模式。

```xml
<init-param>
    <param-name>keycloak.config.skipPattern</param-name>
    <param-value>^/(path1|path2|path3).*</param-value>
</init-param>
```

请注意，您应该在Keycloak管理控制台中使用管理URL配置客户端，该URL指向过滤器的url-pattern所涵盖的安全部分。

管理员URL将对管理员URL进行回调，以执行backchannel注销等操作。 因此，此示例中的Admin URL应为`http[s]://hostname/{context-root}/keycloak`。

Keycloak过滤器具有与其他适配器相同的配置参数，除非您必须将它们定义为过滤器init参数而不是上下文参数。

要使用此过滤器，请在WAR poms中包含此maven工件：

```xml
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-servlet-filter-adapter</artifactId>
    <version>6.0.1</version>
</dependency>
```

##### 在OSGi上使用 {#Using_on_OSGi}
servlet过滤器适配器打包为OSGi包，因此可以在具有HTTP服务和HTTP白板的通用OSGi环境（R6及更高版本）中使用。

###### 安装 {#Installation}
适配器及其依赖项作为Maven工件分发，因此您需要使用Internet连接来访问Maven Central，或者将工件缓存在本地Maven存储库中。

如果您使用的是Apache Karaf，则可以直接从Keycloak功能仓库安装一个功能：

```
karaf@root()> feature:repo-add mvn:org.keycloak/keycloak-osgi-features/6.0.1/xml/features
karaf@root()> feature:install keycloak-servlet-filter-adapter
```

对于其他OSGi运行时，请参阅运行时文档，了解如何安装适配器包及其依赖项。

> 如果你的OSGi平台是带有Pax Web的Apache Karaf，你应该考虑使用[JBoss Fuse 6](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse_adapter)或[JBoss Fuse 7](https://www.keycloak.org/docs/latest/securing_apps/index.html#_fuse7_adapter)适配器。

###### 配置 {#Configuration}
首先，需要使用OSGi HTTP服务将适配器注册为servlet过滤器。 最常见的方法是编程（例如通过bundle激活器）和声明（使用OSGi注释）。 我们建议使用后者，因为它简化了动态注册和取消注册过滤器的过程：

```java
package mypackage;

import javax.servlet.Filter;
import org.keycloak.adapters.servlet.KeycloakOIDCFilter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(
    immediate = true,
    service = Filter.class,
    property = {
        KeycloakOIDCFilter.CONFIG_FILE_PARAM + "=" + "keycloak.json",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=" +"/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "(osgi.http.whiteboard.context.name=mycontext)"
    }
)
public class KeycloakFilter extends KeycloakOIDCFilter {
  //
}
```

上面的代码片段使用OSGi声明性服务规范将过滤器公开为`javax.servlet.Filter`类下的OSGI服务。 一旦在OSGi服务注册表中发布了类，它将由OSGi HTTP服务实现获取并用于过滤对指定servlet上下文的请求。 这将为每个匹配servlet上下文路径+过滤器路径的请求触发Keycloak适配器。

由于组件处于OSGi Configuration Admin Service的控制之下，因此可以动态配置其属性。 为此，要在OSGi运行时的标准配置位置下创建一个`mypackage.KeycloakFilter.cfg`文件：

```properties
keycloak.config.file = /path/to/keycloak.json
osgi.http.whiteboard.filter.pattern = /secure/*
```

或者使用交互式控制台，如果您的运行时允许：

```
karaf@root()> config:edit mypackage.KeycloakFilter
karaf@root()> config:property-set keycloak.config.file '${karaf.etc}/keycloak.json'
karaf@root()> config:update
```

如果您需要更多控制权，例如 提供自定义`KeycloakConfigResolver`来实现[多租户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy)，您可以通过编程方式注册过滤器：

```java
public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  public void start(BundleContext context) throws Exception {
    Hashtable props = new Hashtable();
    props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN, "/secure/*");
    props.put(KeycloakOIDCFilter.CONFIG_RESOLVER_PARAM, new MyConfigResolver());

    this.registration = context.registerService(Filter.class.getName(), new KeycloakOIDCFilter(), props);
  }

  public void stop(BundleContext context) throws Exception {
    this.registration.unregister();
  }
}
```

有关程序化注册的更多信息，请参阅[Apache Felix HTTP服务](http://felix.apache.org/documentation/subprojects/apache-felix-http-service.html#using-the-osgi-http-whiteboard)。

#### 2.1.11. JAAS 插件 {#JAAS_plugin}

通常不需要在大多数应用程序中使用JAAS，特别是如果它们是基于HTTP的，并且您最有可能选择我们的其他适配器之一。 但是，某些应用程序和系统仍可能依赖纯粹的传统JAAS解决方案。 Keycloak提供了两个登录模块来帮助解决这些问题。

提供的登录模块是：

- org.keycloak.adapters.jaas.DirectAccessGrantsLoginModule

  此登录模块允许使用Keycloak中的用户名/密码进行身份验证。 它使用[资源所有者密码凭证](https://www.keycloak.org/docs/latest/securing_apps/index.html#_resource_owner_password_credentials_flow)流来验证提供的用户名/密码是否有效。 它对非基于Web的系统非常有用，它需要依赖JAAS并希望使用Keycloak，但由于其非Web性质而无法使用基于标准浏览器的流程。 此类应用程序的示例可以是消息传递或SSH。

- org.keycloak.adapters.jaas.BearerTokenLoginModule

  此登录模块允许通过CallbackHandler作为密码传递给它的Keycloak访问令牌进行身份验证。 例如，当您从基于标准的身份验证流程获得Keycloak访问令牌并且您的Web应用程序需要与依赖于JAAS的外部非基于Web的系统进行通信时，它可能很有用。 例如，消息传递系统。

两个模块都使用以下配置属性：

- keycloak-config-file

  `keycloak.json`配置文件的位置。 配置文件可以位于文件系统上，也可以位于类路径上。 如果它位于类路径上，则需要在该位置前加上`classpath:`（例如`classpath:/path/keycloak.json`）。 这是*REQUIRED*

- `role-principal-class`

  为附加到JAAS Subject的Role主体配置备用类。 默认值为`org.keycloak.adapters.jaas.RolePrincipal`。 注意：该类需要具有带有单个`String`参数的构造函数。

- `scope`

  此选项仅适用于`DirectAccessGrantsLoginModule`。 指定的值将用作资源所有者密码凭据授予请求中的OAuth2 `scope` 参数。

#### 2.1.12. CLI / Desktop 应用 {#CLI___Desktop_Applications}

Keycloak支持通过系统浏览器执行身份验证步骤，通过`KeycloakInstalled`adapter保护桌面（例如Swing，JavaFX）或CLI应用程序。

`KeycloakInstalled`适配器支持`desktop`和`manual`变体。 桌面版本使用系统浏览器来收集用户凭据。 手动变体从`STDIN`读取用户凭据。

提示：Google在[OAuth2InstalledApp](https://developers.google.com/identity/protocols/OAuth2InstalledApp)上提供了有关此方法的更多信息。

##### 它是如何工作的 {#How_it_works}
为了使用`desktop`变体对用户进行身份验证，`KeycloakInstalled`适配器打开一个桌面浏览器窗口，当在`KeycloakInstalled`对象上调用`loginDesktop()`方法时，用户使用常规的Keycloak登录页面进行登录。

使用redirect参数打开登录页面URL，该参数指向本地`ServerSocket`，该地址`ServerSocket`在由适配器启动的`localhost`上的空闲临时端口上进行侦听。

成功登录后，`KeycloakInstalled`从传入的HTTP请求接收授权代码并执行授权代码流。 一旦令牌交换的代码完成，`ServerSocket`就会关闭。

> 如果用户已经有一个活动的Keycloak会话，则不会显示登录表单，但会继续进行令牌交换的代码，这样可以实现基于Web的SSO平滑体验。

客户端最终会收到令牌（access_token，refresh_token，id_token），然后可以使用这些令牌来调用后端服务。

`KeycloakInstalled`适配器支持更新陈旧令牌。

##### 适配器安装 {#Adapter_Installation}
```xml
<dependency>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-installed-adapter</artifactId>
        <version>6.0.1</version>
</dependency>
```

##### 客户端配置 {#Client_Configuration}
应用程序需要配置为`public` OpenID Connect客户端，其中`Standard Flow Enabled`和 http://localhost:*作为允许的`Valid Redirect URI`。

##### 用法 {#Usage}
`KeycloakInstalled`适配器从类路径上的`META-INF/keycloak.json`读取它的配置。 可以通过`KeycloakInstalled`constructor为`InputStream`或`KeycloakDeployment`提供自定义配置。

在下面的示例中，`desktop-app`的客户端配置使用以下`keycloak.json`：

```json
{
  "realm": "desktop-app-auth",
  "auth-server-url": "http://localhost:8081/auth",
  "ssl-required": "external",
  "resource": "desktop-app",
  "public-client": true,
  "use-resource-role-mappings": true
}
```

下面的草图演示了如何使用`KeycloakInstalled`适配器：

```java
// reads the configuration from classpath: META-INF/keycloak.json
KeycloakInstalled keycloak = new KeycloakInstalled();

// opens desktop browser
keycloak.loginDesktop();

AccessToken token = keycloak.getToken();
// use token to send backend request

// ensure token is valid for at least 30 seconds
long minValidity = 30L;
String tokenString = keycloak.getTokenString(minValidity, TimeUnit.SECONDS);


 // when you want to logout the user.
keycloak.logout();
```

> `KeycloakInstalled`类支持通过`loginResponseWriter`和`logoutResponseWriter`属性自定义登录/注销请求返回的http响应。

##### 例子 {#Example}
以下提供了上述配置的示例。

```java
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.keycloak.adapters.installed.KeycloakInstalled;
import org.keycloak.representations.AccessToken;

public class DesktopApp {

        public static void main(String[] args) throws Exception {

                KeycloakInstalled keycloak = new KeycloakInstalled();
                keycloak.setLocale(Locale.ENGLISH);
                keycloak.loginDesktop();

                AccessToken token = keycloak.getToken();
                Executors.newSingleThreadExecutor().submit(() -> {

                        System.out.println("Logged in...");
                        System.out.println("Token: " + token.getSubject());
                        System.out.println("Username: " + token.getPreferredUsername());
                        try {
                                System.out.println("AccessToken: " + keycloak.getTokenString());
                        } catch (Exception ex) {
                                ex.printStackTrace();
                        }

                        int timeoutSeconds = 20;
                        System.out.printf("Logging out in...%d Seconds%n", timeoutSeconds);
                        try {
                                TimeUnit.SECONDS.sleep(timeoutSeconds);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        try {
                                keycloak.logout();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }

                        System.out.println("Exiting...");
                        System.exit(0);
                });
        }
}
```

#### 2.1.13. 安全上下文 {#Security_Context}

如果您需要直接访问令牌，则可以使用`KeycloakSecurityContext`接口。 如果要从令牌中检索其他详细信息（例如用户配置文件信息），或者要调用受Keycloak保护的RESTful服务，这可能很有用。

在servlet环境中，它在安全调用中可用作HttpServletRequest中的属性：

```java
httpServletRequest
    .getAttribute(KeycloakSecurityContext.class.getName());
```

或者，它在HttpSession中的不安全请求中可用：

```
httpServletRequest.getSession()
    .getAttribute(KeycloakSecurityContext.class.getName());
```

#### 2.1.14. 错误处理 {#Error_Handling}

Keycloak为基于servlet的客户端适配器提供了一些错误处理功能。 在身份验证中遇到错误时，Keycloak将调用`HttpServletResponse.sendError()`。 您可以在`web.xml`文件中设置一个错误页面来处理您想要的错误。 Keycloak可能会丢失400,401,403和500错误。

```xml
<error-page>
    <error-code>403</error-code>
    <location>/ErrorHandler</location>
</error-page>
```

Keycloak还设置了一个可以检索的`HttpServletRequest`属性。 属性名称是`org.keycloak.adapters.spi.AuthenticationError`，应该将其转换为`org.keycloak.adapters.OIDCAuthenticationError`。

例如：

```java
import org.keycloak.adapters.OIDCAuthenticationError;
import org.keycloak.adapters.OIDCAuthenticationError.Reason;
...

OIDCAuthenticationError error = (OIDCAuthenticationError) httpServletRequest
    .getAttribute('org.keycloak.adapters.spi.AuthenticationError');

Reason reason = error.getReason();
System.out.println(reason.name());
```

#### 2.1.15. 注销 {#Logout}

您可以通过多种方式注销Web应用程序。 对于Java EE servlet容器，可以调用`HttpServletRequest.logout()`。 对于其他浏览器应用程序，您可以将浏览器重定向到`http://auth-server/auth/realms/{realm-name}/protocol/openid-connect/logout?redirect_uri=encodedRedirectUri`，如果您有与您的浏览器进行SSO会话。

当使用`HttpServletRequest.logout()`选项时，适配器对传递刷新令牌的Keycloak服务器执行反向通道POST调用。 如果该方法是从未受保护的页面（不检查有效令牌的页面）执行的，则刷新令牌可能不可用，在这种情况下，适配器会跳过该调用。 因此，建议使用受保护的页面来执行`HttpServletRequest.logout()`，以便始终考虑当前令牌，并在需要时执行与Keycloak服务器的交互。

如果您希望在注销过程中避免注销外部身份提供程序，则可以提供参数`initiating_idp`，其值为相关身份提供程序的标识（别名）。 当注销端点作为外部身份提供程序启动的单一注销的一部分进行调用时，这非常有用。

#### 2.1.16. 参数转发 {#Parameters_Forwarding}

Keycloak初始授权端点请求支持各种参数。 大多数参数在[OIDC规范](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint)中描述。 某些参数由适配器根据适配器配置自动添加。 但是，还可以在每次调用的基础上添加一些参数。 打开受保护的应用程序URI时，特定参数将转发到Keycloak授权端点。

例如，如果您请求脱机令牌，则可以使用`scope`参数打开受保护的应用程序URI，如：

```
http://myappserver/mysecuredapp?scope=offline_access
```

并且参数`scope = offline_access`将自动转发到Keycloak授权端点。

支持的参数是：

- scope - 使用以空格分隔的范围列表。 以空格分隔的列表通常引用在特定客户端上定义的[客户端范围](https://www.keycloak.org/docs/6.0/server_admin/#_client_scopes)。 请注意，范围`openid`将始终由适配器添加到作用域列表中。 例如，如果输入范围选项`address phone`，则对Keycloak的请求将包含范围参数`scope=openid address phone`。
- prompt - Keycloak支持以下设置：
  - `login` - 即使用户已经过身份验证，也会忽略SSO并始终显示Keycloak登录页面
  - `consent` - 仅适用于`Consent Required(需要同意)`的客户。 如果使用它，即使用户先前已同意此客户端，也将始终显示“同意”页面。
  - `none` - 登录页面永远不会显示; 相反，如果用户尚未通过身份验证，则会将用户重定向到应用程序，并显示错误。 此设置允许您在应用程序端创建过滤器/拦截器，并向用户显示自定义错误页面。 请参阅规范中的更多详细信息。
- max_age - 仅在用户已经过身份验证时使用。 指定身份验证持续的最长允许时间（从用户进行身份验证时开始计算）。 如果用户的身份验证时间超过`maxAge`，则会忽略SSO并且必须重新进行身份验证。
- login_hint - 用于预填充登录表单上的用户名/电子邮件字段。
- kc_idp_hint - 用于告诉Keycloak跳过显示登录页面并自动重定向到指定的身份提供者。 [身份提供商文档](https://www.keycloak.org/docs/6.0/server_admin/#_client_suggested_idp)中的更多信息。

大多数参数在[OIDC规范](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint)中描述。 唯一的例外是参数`kc_idp_hint`，它特定于Keycloak并包含要自动使用的身份提供者的名称。 有关更多信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)中的`Identity Brokering`部分。

> 如果使用附加参数打开URL，则如果已在应用程序中进行了身份验证，则适配器不会将您重定向到Keycloak。 例如，如果您已经对应用程序`mysecredapp`进行了身份验证，则打开`http://myappserver/mysecuredapp?prompt=login`将不会自动将您重定向到Keycloak登录页面。 将来可能会更改此行为。

#### 2.1.17. 客户端身份验证 {#Client_Authentication}

当机密OIDC客户端需要发送反向信道请求（例如，交换令牌代码或刷新令牌）时，它需要针对Keycloak服务器进行身份验证。 默认情况下，有三种方法可以对客户端进行身份验证：客户端ID和客户端密钥，使用签名JWT的客户端身份验证，或使用客户端密钥的签名JWT进行客户端身份验证。

##### 客户端ID和客户端Secret {#Client_ID_and_Client_Secret}
这是OAuth2规范中描述的传统方法。 客户端有一个secret，需要知道适配器（应用程序）和Keycloak服务器。 您可以在Keycloak管理控制台中为特定客户端生成密码，然后将此密钥粘贴到应用程序端的`keycloak.json`文件中：

```java
"credentials": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
}
```

##### 使用签名JWT的客户端身份验证 {#Client_Authentication_with_Signed_JWT}
这基于[RFC7523](https://tools.ietf.org/html/rfc7523)规范。 它以这种方式工作：

- 客户端必须具有私钥和证书。 对于Keycloak，可以通过传统的`keystore`文件获得，该文件可以在客户端应用程序的类路径上或文件系统上的某个位置获得。
- 启动客户端应用程序后，它允许使用URL等[JWKS](https://self-issued.info/docs/draft-ietf-jose-json-web-key.html)格式下载其公钥。 作为http://myhost.com/myapp/k_jwks，假设http://myhost.com/myapp是客户端应用程序的基本URL。 Keycloak可以使用此URL（见下文）。
- 在身份验证期间，客户端生成JWT令牌并使用其私钥对其进行签名，并在`client_assertion`参数中的特定反向通道请求（例如，代码到令牌请求）中将其发送到Keycloak。
- Keycloak必须具有客户端的公钥或证书，以便它可以验证JWT上的签名。 在Keycloak中，您需要为客户端配置客户端凭据。 首先，您需要选择`Signed JWT`作为在管理控制台的“凭据”选项卡中验证客户端的方法。 然后你可以选择：
  - 配置JWKS URL，Keycloak可以在其中下载客户端的公钥。 这可以是诸如http://myhost.com/myapp/k_jwks之类的URL（参见上面的详细信息）。 此选项是最灵活的，因为客户端可以随时旋转其键，然后Keycloak总是在需要时下载新密钥而无需更改配置。 更准确地说，Keycloak在看到由未知的`kid`（密钥ID）签名的令牌时下载新密钥。
  - 以PEM格式，JWK格式或从密钥库上载客户端的公钥或证书。 使用此选项，公钥是硬编码的，必须在客户端生成新密钥对时进行更改。 如果您没有自己的密钥库，您甚至可以从Keycloak管理控制台生成自己的密钥库。 有关如何设置Keycloak管理控制台的更多详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。

要在适配器端进行设置，您需要在`keycloak.json`文件中使用这样的内容：

```json
"credentials": {
  "jwt": {
    "client-keystore-file": "classpath:keystore-client.jks",
    "client-keystore-type": "JKS",
    "client-keystore-password": "storepass",
    "client-key-password": "keypass",
    "client-key-alias": "clientkey",
    "token-expiration": 10
  }
}
```

使用此配置，密钥库文件`keystore-client.jks`必须在WAR中的类路径上可用。 如果不使用前缀`classpath:`，则可以指向运行客户端应用程序的文件系统上的任何文件。

为了获得灵感，您可以查看示例分发到主要演示示例中的`product-portal`应用程序。

##### 使用客户端密钥使用签名JWT进行客户端身份验证 {#Client_Authentication_with_Signed_JWT_using_Client_Secret}
这与使用签名JWT的客户端身份验证相同，除了使用客户端密钥而不是私钥和证书。

客户端有一个`secret(秘密)`，需要知道适配器（应用程序）和Keycloak服务器。 您需要选择`Signed JWT with Client Secret`作为在管理控制台的`Credentials凭据`选项卡中验证客户端的方法，然后将此`secret(秘密)`粘贴到应用程序端的`keycloak.json`文件中：

```json
"credentials": {
  "secret-jwt": {
    "secret": "19666a4f-32dd-4049-b082-684c74115f28"
  }
}
```

##### 添加自己的客户端身份验证方法 {#Add_Your_Own_Client_Authentication_Method}
您也可以添加自己的客户端身份验证方法。 您将需要实现客户端和服务器端提供程序。 有关更多详细信息，请参阅[服务器开发人员指南](https://www.keycloak.org/docs/6.0/server_development/)中的`Authentication SPI`部分。

#### 2.1.18. 多租户 {#Multi_Tenancy}

在我们的上下文中，多租户意味着可以使用多个Keycloak领域来保护单个目标应用程序（WAR）。 领域可以位于同一个Keycloak实例中，也可以位于不同的实例上。

实际上，这意味着应用程序需要有多个`keycloak.json`适配器配置文件。

您可以拥有多个WAR实例，并将不同的适配器配置文件部署到不同的上下文路径。 但是，这可能不方便，您可能还希望基于上下文之外的其他内容选择领域。

Keycloak可以使用自定义配置解析器，因此您可以选择每个请求使用的适配器配置。

要实现此目的，首先需要创建`org.keycloak.adapters.KeycloakConfigResolver`的实现。 例如：

```java
package example;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;

public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        if (path.startsWith("alternative")) {
            KeycloakDeployment deployment = cache.get(realm);
            if (null == deployment) {
                InputStream is = getClass().getResourceAsStream("/tenant1-keycloak.json");
                return KeycloakDeploymentBuilder.build(is);
            }
        } else {
            InputStream is = getClass().getResourceAsStream("/default-keycloak.json");
            return KeycloakDeploymentBuilder.build(is);
        }
    }

}
```

您还需要配置与`web.xml`中的`keycloak.config.resolver`context-param一起使用的`KeycloakConfigResolver`实现：

```xml
<web-app>
    ...
    <context-param>
        <param-name>keycloak.config.resolver</param-name>
        <param-value>example.PathBasedKeycloakConfigResolver</param-value>
    </context-param>
</web-app>
```

#### 2.1.19. 应用程序集群 {#Application_Clustering}

本章涉及支持部署到JBoss EAP，WildFly和JBoss AS的集群应用程序。

根据您的应用程序是否有以下几种选项：

- 无状态或有状态
- 可分发的(复制的http会话)或不可分发的
- 依赖负载均衡器提供的粘性会话
- 与Keycloak在同一个域名中托管

处理群集并不像普通应用程序那么简单。 主要是因为浏览器和服务器端应用程序都向Keycloak发送请求，因此它不像在负载均衡器上启用粘性会话那么简单。

##### 无状态令牌存储 {#Stateless_token_store}
默认情况下，Keycloak保护的Web应用程序使用HTTP会话来存储安全上下文。 这意味着您必须启用粘性会话或复制HTTP会话。

作为在HTTP会话中存储安全上下文的替代方法，可以将适配器配置为将其存储在cookie中。 如果要使应用程序无状态或者您不希望在HTTP会话中存储安全上下文，这将非常有用。

要使用cookie存储来保存安全上下文，请编辑应用程序`WEB-INF/keycloak.json`并添加：

```
"token-store": "cookie"
```

> `token-store`的默认值是`session`，它将安全上下文存储在HTTP会话中。

使用cookie存储的一个限制是整个安全上下文在cookie中传递给每个HTTP请求。 这可能会影响性能。

另一个小限制是对单点登出的有限支持。 如果您从应用程序本身初始化servlet注销（HttpServletRequest.logout），它将正常工作，因为适配器将删除KEYCLOAK_ADAPTER_STATE cookie。 但是，Keycloak不会将从其他应用程序初始化的反向通道注销传播到使用cookie存储的应用程序。 因此，建议对访问令牌超时使用短值（例如1分钟）。

> 某些负载均衡器不允许任何配置粘性会话cookie名称或内容，例如Amazon ALB。 对于这些，建议将`shouldAttachRoute`选项设置为`false`。

##### 相对URI优化 {#Relative_URI_optimization}
在Keycloak和应用程序托管在同一域（通过反向代理或负载平衡器）的部署方案中，在客户端配置中使用相对URI选项会很方便。

使用相对URI，URI将相对于用于访问Keycloak的URL进行解析。

例如，如果您的应用程序的URL是`https://acme.org/myapp`，而Keycloak的URL是`https://acme.org/auth`，那么您可以使用redirect-uri `/myapp` 而不是 `https://acme.org/myapp`。

##### 管理员URL配置 {#Admin_URL_configuration}
可以在Keycloak管理控制台中配置特定客户端的管理URL。 它由Keycloak服务器用于向应用程序发送后端请求以执行各种任务，例如注销用户或推送撤销策略。

例如，反向通道注销的工作方式是：

1. 用户从一个应用程序发送注销请求
2. 应用程序向Keycloak发送注销请求
3. Keycloak服务器使用户会话无效
4. 然后，Keycloak服务器向应用程序发送反向通道请求，其中包含与会话关联的管理URL
5. 当应用程序收到注销请求时，它会使相应的HTTP会话无效

如果admin URL包含 `${application.session.host}` ，它将被替换为与HTTP会话关联的节点的URL。

##### 注册应用程序节点 {#Registration_of_application_nodes}
上一节描述了Keycloak如何向与特定HTTP会话关联的节点发送注销请求。 但是，在某些情况下，管理员可能希望将管理任务传播到所有已注册的群集节点，而不仅仅是其中一个。 例如，将新的not before策略推送到应用程序或从应用程序注销所有用户。

在这种情况下，Keycloak需要知道所有应用程序集群节点，因此它可以将事件发送给所有这些节点。 为此，我们支持自动发现机制：

1. 当新的应用程序节点加入群集时，它会向Keycloak服务器发送注册请求
2. 该请求可以以配置的周期性间隔重新发送到Keycloak
3. 如果Keycloak服务器在指定的超时内未收到重新注册请求，则它会自动取消注册特定节点
4. 当Keycloak发送取消注册请求时，该节点也会在Keycloak中取消注册，这通常是在节点关闭或应用程序取消部署期间。 当未调用未部署侦听器时，这可能无法正常执行强制关闭，这导致需要自动取消注册

默认情况下禁用发送启动注册和定期重新注册，因为只有某些群集应用程序才需要它。

要启用该功能，请编辑应用程序的`WEB-INF/keycloak.json`文件并添加：

```
"register-node-at-startup": true,
"register-node-period": 600,
```

这意味着适配器将在启动时发送注册请求，并每10分钟重新注册一次。

在Keycloak管理控制台中，您可以指定最大节点重新注册超时（应该大于适配器配置中的*register-node-period*）。 您还可以通过管理控制台手动添加和删除群集节点，如果您不想依赖自动注册功能，或者如果您想要在不使用自动注销功能的情况下删除陈旧的应用程序节点，这将非常有用。

##### 刷新每个请求中的令牌 {#Refresh_token_in_each_request}
默认情况下，应用程序适配器仅在访问令牌过期时刷新。 但是，您还可以配置适配器以在每个请求上刷新令牌。 这可能会对性能产生影响，因为您的应用程序将向Keycloak服务器发送更多请求。

要启用该功能，请编辑应用程序的`WEB-INF/keycloak.json`文件并添加：

```
"always-refresh-token": true
```

> 这可能会对性能产生重大影响。 如果您不能依赖反向通道消息传播注销而不是策略之前，则仅启用此功能。 另一件需要考虑的事情是，默认情况下，访问令牌的到期时间很短，因此即使未传播注销，令牌也会在注销后的几分钟内到期。

### 2.2. JavaScript 适配器 {#JavaScript_Adapter}

Keycloak附带了一个客户端JavaScript库，可以用来保护HTML5/JavaScript应用程序。JavaScript适配器内置了对Cordova应用程序的支持。

该库可以直接从Keycloak服务器检索到`/auth/js/keycloak.js`，也可以作为ZIP存档分发。

最佳做法是直接从Keycloak Server加载JavaScript适配器，因为它会在升级服务器时自动更新。 如果将适配器复制到Web应用程序，请确保仅在升级服务器后升级适配器。

关于使用客户端应用程序的一个重要注意事项是客户端必须是公共客户端，因为没有安全的方法来在客户端应用程序中存储客户端凭据。 这使得确保为客户端配置的重定向URI正确且尽可能具体非常重要。

要使用JavaScript适配器，必须首先在Keycloak管理控制台中为您的应用程序创建客户端。 确保为`Access Type`选择`public`。

您还需要配置有效的重定向URI和有效的Web源。 尽可能具体，因为未能这样做可能会导致安全漏洞。

创建客户端后，单击`Installation(安装)`选项卡，选择`Format Option(格式选项)`的`Keycloak OIDC JSON`，然后单击`Download(下载)`。 下载的`keycloak.json`文件应该存放在您的Web服务器上与HTML页面相同的位置。

或者，您可以跳过配置文件并手动配置适配器。

以下示例显示如何初始化JavaScript适配器：

```html
<head>
    <script src="keycloak.js"></script>
    <script>
        var keycloak = Keycloak();
        keycloak.init().success(function(authenticated) {
            alert(authenticated ? 'authenticated' : 'not authenticated');
        }).error(function() {
            alert('failed to initialize');
        });
    </script>
</head>
```

如果`keycloak.json`文件位于不同的位置，您可以指定它：

```javascript
var keycloak = Keycloak('http://localhost:8080/myapp/keycloak.json');
```

或者，您可以使用所需的配置传入JavaScript对象：

```javascript
var keycloak = Keycloak({
    url: 'http://keycloak-server/auth',
    realm: 'myrealm',
    clientId: 'myapp'
});
```

默认情况下，您需要调用`login`函数进行身份验证。 但是，有两个选项可用于使适配器自动进行身份验证。 您可以将`login-required`或`check-sso`传递给init函数。 如果用户登录到Keycloak，则`login-required`将对客户端进行身份验证，否则将显示登录页面。 `check-sso`将仅在用户已登录时验证客户端，如果用户未登录，则浏览器将被重定向回应用程序并保持未经身份验证。

要启用`login-required`，请将`onLoad`设置为`login-required`并传递给init方法：

```javascript
keycloak.init({ onLoad: 'login-required' })
```

在对用户进行身份验证之后，应用程序可以通过在`Authorization`标头中包含承载令牌来向Keycloak保护对RESTful服务的请求。 例如：

```javascript
var loadData = function () {
    document.getElementById('username').innerText = keycloak.subject;

    var url = 'http://localhost:8080/restful-service';

    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.setRequestHeader('Accept', 'application/json');
    req.setRequestHeader('Authorization', 'Bearer ' + keycloak.token);

    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {
                alert('Success');
            } else if (req.status == 403) {
                alert('Forbidden');
            }
        }
    }

    req.send();
};
```

需要记住的一件事是，默认情况下，访问令牌的生命周期很短，因此您可能需要在发送请求之前刷新访问令牌。 您可以通过`updateToken`方法执行此操作。 `updateToken`方法返回一个promise对象，只有在成功刷新令牌时才能轻松调用服务，例如，如果不是，则向用户显示错误。 例如：

```javascript
keycloak.updateToken(30).success(function() {
    loadData();
}).error(function() {
    alert('Failed to refresh token');
});
```

#### 2.2.1. 会话状态iframe {#Session_Status_iframe}
默认情况下，JavaScript适配器会创建一个隐藏的iframe，用于检测是否已发生单一注销。 这不需要任何网络流量，而是通过查看特殊状态cookie来检索状态。 可以通过在传递给`init`方法的选项中设置`checkLoginIframe:false`来禁用此功能。

你不应该直接看这个cookie。 它的格式可以更改，它也与Keycloak服务器的URL相关联，而不是与您的应用程序相关联。

#### 2.2.2. 隐式和混合流 {#Implicit_and_Hybrid_Flow}
默认情况下，JavaScript适配器使用[授权代码](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) 流程。

通过此流程，Keycloak服务器向应用程序返回授权代码，而不是身份验证令牌。 在将浏览器重定向回应用程序后，JavaScript适配器会交换访问令牌和刷新令牌的`code`。

Keycloak还支持[Implicit](https://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth) 流程，其中在使用Keycloak成功进行身份验证后立即发送访问令牌。 这可能比标准流具有更好的性能，因为没有额外的请求来交换令牌的代码，但是当访问令牌到期时它会产生影响。

但是，在URL片段中发送访问令牌可能是一个安全漏洞。 例如，令牌可以通过Web服务器日志和/或浏览器历史记录泄露。

要启用隐式流，您需要在Keycloak管理控制台中为客户端启用`Implicit Flow Enabled`标志。 您还需要将值为`implicit`的参数`flow`传递给`init`方法：

```javascript
keycloak.init({ flow: 'implicit' })
```

需要注意的一点是，只提供了访问令牌，并且没有刷新令牌。 这意味着一旦访问令牌到期，应用程序必须再次重定向到Keycloak以获取新的访问令牌。

Keycloak还支持[Hybrid](https://openid.net/specs/openid-connect-core-1_0.html#HybridFlowAuth) 流程。

这要求客户端在管理控制台中启用`Standard Flow Enabled`和`Implicit Flow Enabled`标志。 然后Keycloak服务器将代码和令牌发送到您的应用程序。 可以立即使用访问令牌，同时可以交换代码以访问和刷新令牌。 与隐式流类似，混合流有利于提高性能，因为访问令牌可立即使用。 但是，令牌仍然在URL中发送，前面提到的安全漏洞可能仍然适用。

混合流程的一个优点是刷新令牌可供应用程序使用。

对于Hybrid流，您需要将值为`hybrid`的参数`flow`传递给`init`方法：

```javascript
keycloak.init({ flow: 'hybrid' })
```

#### 2.2.3. 与Cordova的混合应用程序   {#Hybrid_Apps_with_Cordova}
Keycloak支持使用[Apache Cordova](https://cordova.apache.org/)开发的混合移动应用程序。 Javascript适配器有两种模式：`cordova`和`cordova-native`：

默认值为cordova，如果未配置适配器类型且window.cordova存在，则适配器将自动选择。 登录时，它将打开[InApp浏览器](https://cordova.apache.org/docs/en/latest/reference/cordova-plugin-inappbrowser/)，让用户与Keycloak交互，然后返回到app通过重定向到`http://localhost`。 因此，您必须将此URL列入白名单，作为管理控制台的客户端配置部分中的有效redirect-uri。

虽然这种模式易于设置，但它也有一些缺点：*InApp-Browser是嵌入在应用程序中的浏览器，不是手机的默认浏览器。 因此，它将具有不同的设置，并且存储的凭据将不可用. * InApp-Browser可能也会更慢，尤其是在渲染更复杂的主题时。 * 在使用此模式之前，需要考虑安全问题，例如应用程序可以访问用户的凭据，因为它可以完全控制浏览器呈现登录页面，因此不要允许 在您不信任的应用中使用。

使用此示例应用程序可帮助您入门：<https://github.com/keycloak/keycloak/tree/master/examples/cordova>

替代模式`cordova-nativei`采用不同的方法。 它使用系统的浏览器打开登录页面。 用户通过身份验证后，浏览器会使用特殊URL重定向回应用程序。 从那里，Keycloak适配器可以通过从URL读取代码或令牌来完成登录。

您可以通过将适配器类型`cordova-native`传递给`init`方法来激活本机模式：

```javascript
keycloak.init({ adapter: 'cordova-native' })
```

此适配器需要两个额外的插件：

- [cordova-plugin-browsertab](https://github.com/google/cordova-plugin-browsertab): 允许该应用在系统浏览器中打开网页
- [cordova-plugin-deeplinks](https://github.com/e-imaxina/cordova-plugin-deeplinks): 允许浏览器通过特殊网址重定向回您的应用

链接到应用程序的技术细节在每个平台上都有所不同，需要进行特殊设置。 有关详细说明，请参阅[deeplinks插件文档](https://github.com/e-imaxina/cordova-plugin-deeplinks/blob/master/README.md)的Android和iOS部分。

打开应用程序有不同类型的链接:自定义方案(即`myapp://login`或`android-app://com.example.myapp/https/example.com/login`)和[Universal Links(iOS) )]((https://developer.apple.com/ios/universal-links/))/ [Deep Links(Android)](https://developer.android.com/training/app-links/deep-linking). 虽然前者更容易设置并且更容易工作,但后者提供了额外的安全性,因为它们是唯一的,只有域的所有者才能注册它们. iOS上不推荐使用自定义网址. 我们建议您使用通用链接,并在其上附带自定义网址链接的备用网站,以获得最佳可靠性.

此外，我们建议采用以下步骤来改善与Keycloak适配器的兼容性：

- 当`response-mode`设置为`query`时，iOS上的Universal Links似乎更可靠
- 为防止Android在重定向上打开您的应用的新实例，请将以下代码段添加到`config.xml`：

```xml
<preference name="AndroidLaunchMode" value="singleTask" />
```

有一个示例应用程序，显示如何使用本机模式：<https://github.com/keycloak/keycloak/tree/master/examples/cordova-native>

#### 2.2.4. 早期的浏览器 {#Earlier_Browsers}
JavaScript适配器依赖于Base64（window.btoa和window.atob），HTML5 History API和可选的Promise API。 如果您需要支持那些没有这些的浏览器（例如，IE9），则需要添加polyfillers。

示例polyfill库：

- Base64 - <https://github.com/davidchambers/Base64.js>
- HTML5 History - <https://github.com/devote/HTML5-History-API>
- Promise - <https://github.com/stefanpenner/es6-promise>

#### 2.2.5. JavaScript适配器参考 {#JavaScript_Adapter_Reference}
##### 构造函数 {#Constructor}
```javascript
new Keycloak();
new Keycloak('http://localhost/keycloak.json');
new Keycloak({ url: 'http://localhost/auth', realm: 'myrealm', clientId: 'myApp' });
```

##### 属性 {#Properties}
- authenticated

  如果用户通过身份验证，则为`true`，否则为`false`。

- token

  base64编码的令牌，可以在对服务的请求中的`Authorization`头中发送。

- tokenParsed

  解析后的令牌作为JavaScript对象。

- subject

  用户ID。

- idToken

  base64编码的ID令牌。

- idTokenParsed

  解析的id令牌作为JavaScript对象。

- realmAccess

  与令牌关联的域角色。

- resourceAccess

  与令牌关联的资源角色。

- refreshToken

  base64编码的刷新令牌，可用于检索新令牌。

- refreshTokenParsed

  解析后的刷新令牌作为JavaScript对象。

- timeSkew

  浏览器时间与Keycloak服务器之间的估计时间差，以秒为单位。 此值只是一个估计值，但在确定令牌是否已过期时足够准确。

- responseMode

  在init中传递的响应模式（默认值为fragment）。

- flow

  流程在init中传递。

- adapter

  允许您覆盖重定向的方式以及库处理其他与浏览器相关的函数。 可用选项：“default(默认)” - 库使用浏览器api进行重定向（这是默认设置）“cordova” - 库将尝试使用InAppBrowser cordova插件加载keycloak登录/注册页面（这在库时自动使用） 正在使用Cordova生态系统）“cordova-native” - 图书馆尝试使用BrowserTabs cordova插件使用手机的系统浏览器打开登录和注册页面。 这需要额外设置以重定向回应用程序(请参阅[使用Cordova的混合应用程序](https://www.keycloak.org/docs/latest/securing_apps/index.html#hybrid-apps-with-cordova)).custom - 允许您实现自定义适配器（仅适用于高级用例）

- responseType

  响应类型通过登录请求发送到Keycloak。 这是根据初始化期间使用的流量值确定的，但可以通过设置此值来覆盖。

##### 方法 {#Methods}
###### init（选项） {#init_options_}
调用初始化适配器。

选项是一个对象，其中：

- onLoad - 指定要在加载时执行的操作。 支持的值为`login-required`或`check-sso`。
- token - 设置令牌的初始值。
- refreshToken - 设置刷新令牌的初始值。
- idToken - 设置id令牌的初始值（仅与token或refreshToken一起）。
- timeSkew - 在几秒钟内设置本地时间和Keycloak服务器之间的偏差初始值（仅与token或refreshToken一起）。
- checkLoginIframe - 设置为启用/禁用监控登录状态（默认为true）。
- checkLoginIframeInterval - 设置检查登录状态的时间间隔（默认为5秒）。
- responseMode - 将OpenID Connect响应模式设置为在登录请求时发送到Keycloak服务器。 有效值是查询或片段。 默认值为fragment，这意味着在成功进行身份验证后，Keycloak将重定向到javascript应用程序，并在URL片段中添加OpenID Connect参数。 这通常比查询更安全和推荐。
- flow - 设置OpenID Connect流程。 有效值是standard(标准值)，implicit (隐式值)或hybrid(混合值)。
- promiseType - 如果设置为`native`，则返回promise的所有方法都将返回本机JavaScript promise。 如果未设置，将返回Keycloak特定的promise对象。

返回promise设置在成功或错误时调用的函数。

###### login(选项) {#login_options_}
重定向到登录表单（选项是带有redirectUri和/或提示字段的可选对象）。

选项是一个对象，其中：

- redirectUri - 指定登录后要重定向到的URI。
- prompt - 此参数允许稍微自定义Keycloak服务器端的登录流程。 例如，在值为`login`的情况下强制显示登录屏幕。 有关`prompt`参数的详细信息和所有可能值，请参见[参数转发部分](https://www.keycloak.org/docs/latest/securing_apps/index.html#_params_forwarding) 。
- maxAge - 仅在用户已经过身份验证时使用。 指定自用户身份验证以来的最长时间。 如果用户的身份验证时间比`maxAge`长，则会忽略SSO，并且需要重新进行身份验证。
- loginHint - 用于预填充登录表单上的用户名/电子邮件字段。
- scope - 用于将scope参数转发到Keycloak登录端点。 使用以空格分隔的范围列表。 这些通常引用在特定客户端上定义的[客户端范围](https://www.keycloak.org/docs/6.0/server_admin/#_client_scopes)。 请注意，范围`openid`将始终由适配器添加到作用域列表中。 例如，如果输入范围选项`address phone`，则对Keycloak的请求将包含范围参数`scope=openid address phone`。
- idpHint - 用于告诉Keycloak跳过显示登录页面并自动重定向到指定的身份提供者。 [身份提供商文档]中的更多信息(https://www.keycloak.org/docs/6.0/server_admin/#_client_suggested_idp)。
- action - 如果值为'register'，则将用户重定向到注册页面，否则重定向到登录页面。
- locale - 根据OIDC 1.0规范的3.1.2.1节设置'ui_locales'查询参数。
- kcLocale - 为UI指定所需的Keycloak语言环境。 这与locale param的不同之处在于它告诉Keycloak服务器设置cookie并将用户的配置文件更新为新的首选语言环境。
- cordovaOptions - 指定传递给Cordova应用程序内浏览器的参数（如果适用）。 选项`hidden`和`location`不受这些参数的影响。 所有可用选项均在<https://cordova.apache.org/docs/en/latest/reference/cordova-plugin-inappbrowser/>中定义。 使用示例：`{ zoom: "no", hardwareback: "yes" }`;

###### createLoginUrl(选项) {#createLoginUrl_options_}
返回登录表单的URL（选项是带有redirectUri和/或提示字段的可选对象）。

Options是一个Object，它支持相同的选项，如函数`login`。

###### logout(选项) {#logout_options_}
重定向到注销。

选项是一个对象，其中：

- redirectUri - 指定注销后要重定向到的URI。

###### createLogoutUrl(选项) {#createLogoutUrl_options_}
返回用于注销用户的URL。

选项是一个对象，其中：

- redirectUri - 指定注销后要重定向到的URI。

###### register(选项) {#register_options_}
重定向到注册表单。 使用选项 action = 'register'登录的快捷方式

选项与登录方法相同，但'action'设置为'register'

###### createRegisterUrl(选项) {#createRegisterUrl_options_}
返回注册页面的url。 带有选项action = 'register'的createLoginUrl的快捷方式

选项与createLoginUrl方法相同，但'action'设置为'register'

###### accountManagement() {#accountManagement__}
重定向到帐户管理控制台。

###### createAccountUrl() {#createAccountUrl__}
返回帐户管理控制台的URL。

###### hasRealmRole(role) {#hasRealmRole_role_}
如果令牌具有给定的域角色，则返回true。

###### hasResourceRole(role, resource) {#hasResourceRole_role,_resource_}
如果令牌具有资源的给定角色，则返回true（如果未使用指定的clientId，则资源是可选的）。

###### loadUserProfile() {#loadUserProfile__}
加载用户个人资料。

如果成功加载配置文件，或者无法加载配置文件，则返回promise设置要调用的函数。

例如：

```javascript
keycloak.loadUserProfile().success(function(profile) {
        alert(JSON.stringify(profile, null, "  "));
    }).error(function() {
        alert('Failed to load user profile');
    });
```

###### isTokenExpired(minValidity) {#isTokenExpired_minValidity_}
如果令牌在到期之前的剩余时间小于minValidity秒，则返回true（minValidity是可选的，如果未指定0则使用）。

###### updateToken(minValidity) {#updateToken_minValidity_}
如果令牌在minValidity秒内到期（minValidity是可选的，如果未指定5，则使用）令牌将被刷新。 如果启用了会话状态iframe，则还会检查会话状态。

如果令牌仍然有效，或者令牌不再有效，则返回承诺设置可以调用的函数。 例如：

```javascript
keycloak.updateToken(5).success(function(refreshed) {
        if (refreshed) {
            alert('Token was successfully refreshed');
        } else {
            alert('Token is still valid');
        }
    }).error(function() {
        alert('Failed to refresh the token, or the session has expired');
    });
```

###### clearToken() {#clearToken__}
清除身份验证状态，包括令牌。 如果应用程序检测到会话已过期，例如更新令牌失败，则此功能非常有用。

调用此结果会导致调用onAuthLogout回调侦听器。

##### Callback Events {#Callback_Events}
适配器支持为某些事件设置回调侦听器。

例如：

```javascript
keycloak.onAuthSuccess = function() { alert('authenticated'); }
```

可用的事件是：

- onReady(authenticated) - 初始化适配器时调用。
- onAuthSuccess - 在成功验证用户时调用。
- onAuthError - 如果在身份验证期间出错，则调用。
- onAuthRefreshSuccess - 刷新令牌时调用。
- onAuthRefreshError - 如果在尝试刷新令牌时出现错误，则调用此方法。
- onAuthLogout - 如果用户已注销，则调用（仅在启用会话状态iframe时或在Cordova模式下调用）。
- onTokenExpired - 访问令牌过期时调用。 如果刷新令牌可用，则可以使用updateToken刷新令牌，或者在不使用updateToken的情况下（即使用implicit flow<隐式流>），您可以重定向到登录屏幕以获取新的访问令牌。

### 2.3. Node.js 适配器 {#Node_js_Adapter}

Keycloak提供了一个构建在[Connect](https://github.com/senchalabs/connect)之上的Node.js适配器，以保护服务器端JavaScript应用程序 - 目标是足够灵活，可以与[Express.js](https://expressjs.com/)等框架集成。

该库可以直接从[Keycloak组织](https://www.npmjs.com/package/keycloak-connect)下载，源代码可以在[GitHub](https://github.com/keycloak/keycloak-nodejs-connect)上获得。

要使用Node.js适配器，首先必须在Keycloak管理控制台中为应用程序创建客户端。 适配器支持公共，机密和仅承载访问类型。 选择哪一个取决于用例场景。

创建客户端后，单击`Installation`选项卡，为`Format Option`选择`Keycloak OIDC JSON`，然后单击`Download`。 下载的`keycloak.json`文件应该位于项目的根文件夹中。

#### 2.3.1. 安装 {#Installation}
假设您已经安装了[Node.js](https://nodejs.org/)，请为您的应用程序创建一个文件夹：

```bash
mkdir myapp && cd myapp
```

使用`npm init`命令为您的应用程序创建`package.json`。 现在在依赖项列表中添加Keycloak连接适配器：

```json
    "dependencies": {
        "keycloak-connect": "6.0.1"
    }
```

#### 2.3.2. 用法 {#Usage}
- 实例化Keycloak类

  `Keycloak`类提供了与应用程序配置和集成的中心点。 最简单的创建不涉及任何参数。

```javascript
    var session = require('express-session');
    var Keycloak = require('keycloak-connect');

    var memoryStore = new session.MemoryStore();
    var keycloak = new Keycloak({ store: memoryStore });
```

默认情况下，这将在应用程序的主要可执行文件旁边找到名为`keycloak.json`的文件，以初始化特定于keycloak的设置（公钥，域名，各种URL）。 `keycloak.json`文件是从Keycloak管理控制台获得的。

使用此方法进行实例化会导致使用所有合理的默认值。 作为替代方案，也可以提供配置对象，而不是`keycloak.json`文件：

```javascript
    let kcConfig = {
        clientId: 'myclient',
        bearerOnly: true,
        serverUrl: 'http://localhost:8080/auth',
        realm: 'myrealm',
        realmPublicKey: 'MIIBIjANB...'
    };

    let keycloak = new Keycloak({ store: memoryStore }, kcConfig);
```

应用程序还可以使用以下方法将用户重定向到其首选身份提供程

```javascript
    let keycloak = new Keycloak({ store: memoryStore, idpHint: myIdP }, kcConfig);
```

- 配置Web会话存储

  如果要使用Web会话来管理服务器端状态以进行身份验证，则需要使用至少一个`store`参数初始化`Keycloak(...)`，传入`express-session`正在使用的实际会话存储。

```javascript
    var session = require('express-session');
    var memoryStore = new session.MemoryStore();

    var keycloak = new Keycloak({ store: memoryStore });
```

- 传递自定义范围值

  默认情况下，范围值`openid`作为查询参数传递给Keycloak的登录URL，但您可以添加其他自定义值：

```javascript
    var keycloak = new Keycloak({ scope: 'offline_access' });
```

#### 2.3.3. 安装中间件 {#Installing_Middleware}
实例化后，将中间件安装到支持connect的应用程序中：

```javascript
    var app = express();

    app.use( keycloak.middleware() );
```

#### 2.3.4. 检查身份验证 {#Checking_Authentication}
要在访问资源之前检查用户是否已通过身份验证，只需使用`keycloak.checkSso()`。 它只会在用户已登录时进行身份验证。 如果用户未登录，浏览器将重定向回原始请求的URL并保持未经身份验证：

```javascript
    app.get( '/check-sso', keycloak.checkSso(), checkSsoHandler );
```

#### 2.3.5. 保护资源 {#Protecting_Resources}
- 简单认证

  要强制在访问资源之前必须对用户进行身份验证，只需使用`keycloak.protect()`的无参数版本：

```javascript
    app.get( '/complain', keycloak.protect(), complaintHandler );
```

- 基于角色的授权

  要保护具有当前应用程序的应用程序角色的资源：

```javascript
    app.get( '/special', keycloak.protect('special'), specialHandler );
```

要保护具有**不同**应用程序的应用程序角色的资源：

```javascript
    app.get( '/extra-special', keycloak.protect('other-app:special'), extraSpecialHandler );
```

要保护具有领域角色的资源：

```javascript
    app.get( '/admin', keycloak.protect( 'realm:admin' ), adminHandler );
```

- 高级授权

  为了保护基于URL本身部分的资源，假设每个部分都存在一个角色：

```javascript
    function protectBySection(token, request) {
      return token.hasRole( request.params.section );
    }

    app.get( '/:section/:page', keycloak.protect( protectBySection ), sectionHandler );
```

#### 2.3.6. 其他URLs {#Additional_URLs}
- 显式用户触发的注销

  默认情况下，中间件捕获对`/logout`的调用，以通过以Keycloak为中心的注销工作流发送用户。 这可以通过为`middleware()`调用指定`logout`配置参数来改变：

```javascript
    app.use( keycloak.middleware( { logout: '/logoff' } ));
```

- Keycloak Admin Callbacks

  此外，中间件支持来自Keycloak控制台的回调，以注销单个会话或所有会话。 默认情况下，这些类型的管理回调相对于`/`的根URL发生，但可以通过向`middleware()`调用提供`admin`参数来更改：

```javascript
    app.use( keycloak.middleware( { admin: '/callbacks' } );
```

### 2.4. Keycloak 看门人 {#Keycloak_Gatekeeper}

Keycloak提供了一个Go编程语言适配器，用于OpenID Connect（OIDC），它支持浏览器cookie或承载令牌中的访问令牌。

本文档详细介绍了如何构建和配置keycloak-gatekeeper，以及如何使用其每个功能的详细信息。

有关详细信息，请参阅随附的帮助文件，其中包含命令和开关的完整列表。 通过在命令行输入以下内容来查看文件（修改位置以匹配安装keycloak-gatekeeper的位置）：

```bash
    $ bin/keycloak-gatekeeper help
```

#### 2.4.1. 构建 {#Building}
先决条件

- Golang 必须安装.
- Make 必须安装.

过程

- 运行`make dep-install`来安装所有需要的依赖项。
- 运行`make test`来运行包含的测试。
- 运行`make`来构建项目。 如果您希望构建一个包含所有必需依赖项的二进制文件，则可以使用`make static`。

> 您还可以通过docker容器构建：`make docker-build`。 Docker镜像可在<https://hub.docker.com/r/keycloak/keycloak-gatekeeper/>上找到。

#### 2.4.2. 配置选项 {#Configuration_options}
配置可以来自yaml/json文件或使用命令行选项。 这是一个选项列表。

```
# is the url for retrieve the OpenID configuration - normally the <server>/auth/realm/<realm_name>
discovery-url: https://keycloak.example.com/auth/realms/<REALM_NAME>
# the client id for the 'client' application
client-id: <CLIENT_ID>
# the secret associated to the 'client' application
client-secret: <CLIENT_SECRET>
# the interface definition you wish the proxy to listen, all interfaces is specified as ':<port>', unix sockets as unix://<REL_PATH>|</ABS PATH>
listen: 127.0.0.1:3000
# whether to enable refresh tokens
enable-refresh-tokens: true
# the location of a certificate you wish the proxy to use for TLS support
tls-cert:
# the location of a private key for TLS
tls-private-key:
# the redirection url, essentially the site url, note: /oauth/callback is added at the end
redirection-url: http://127.0.0.1:3000
# the encryption key used to encode the session state
encryption-key: <ENCRYPTION_KEY>
# the upstream endpoint which we should proxy request
upstream-url: http://127.0.0.1:80
# additional scopes to add to add to the default (openid+email+profile)
scopes:
- vpn-user
# a collection of resource i.e. urls that you wish to protect
resources:
- uri: /admin/test
  # the methods on this url that should be protected, if missing, we assuming all
  methods:
  - GET
  # a list of roles the user must have in order to access urls under the above
  # If all you want is authentication ONLY, simply remove the roles array - the user must be authenticated but
  # no roles are required
  roles:
  - openvpn:vpn-user
  - openvpn:prod-vpn
  - test
- uri: /admin/*
  methods:
  - GET
  roles:
  - openvpn:vpn-user
  - openvpn:commons-prod-vpn
```

在命令行发出的选项具有更高的优先级，并将覆盖或合并配置文件中引用的选项。 这里显示了每种样式的示例。

#### 2.4.3. 示例用法和配置 {#Example_usage_and_configuration}
假设您有一些Web服务，您希望受到Keycloak的保护：

- 使用Keycloak GUI或CLI创建**客户端**; 客户端协议是**'openid-connect'**，访问类型：**confidential**。
- 添加**http://127.0.0.1:3000/oauth/callback**的有效重定向URI。
- 抓住客户端ID和客户端密钥。
- 在客户端或现有客户端下创建各种角色以进行授权。

这是一个示例配置文件。

```
client-id: <CLIENT_ID>
client-secret: <CLIENT_SECRET> # require for access_type: confidential
# Note the redirection-url is optional, it will default to the X-Forwarded-Proto / X-Forwarded-Host r the URL scheme and host not found
discovery-url: https://keycloak.example.com/auth/realms/<REALM_NAME>
enable-default-deny: true
encryption_key: AgXa7xRcoClDEU0ZDSH4X0XhL5Qy2Z2j
listen: 127.0.0.1:3000
redirection-url: http://127.0.0.1:3000
upstream-url: http://127.0.0.1:80
resources:
- uri: /admin*
  methods:
  - GET
  roles:
  - client:test1
  - client:test2
  require-any-role: true
  groups:
  - admins
  - users
- uri: /backend*
  roles:
  - client:test1
- uri: /public/*
  white-listed: true
- uri: /favicon
  white-listed: true
- uri: /css/*
  white-listed: true
- uri: /img/*
  white-listed: true
headers:
  myheader1: value_1
  myheader2: value_2
```

也可以使用命令行选项配置配置文件中定义的任何内容，例如在此示例中。

```bash
bin/keycloak-gatekeeper \
    --discovery-url=https://keycloak.example.com/auth/realms/<REALM_NAME> \
    --client-id=<CLIENT_ID> \
    --client-secret=<SECRET> \
    --listen=127.0.0.1:3000 \ # unix sockets format unix://path
    --redirection-url=http://127.0.0.1:3000 \
    --enable-refresh-tokens=true \
    --encryption-key=AgXa7xRcoClDEU0ZDSH4X0XhL5Qy2Z2j \
    --upstream-url=http://127.0.0.1:80 \
    --enable-default-deny=true \
    --resources="uri=/admin*|roles=test1,test2" \
    --resources="uri=/backend*|roles=test1" \
    --resources="uri=/css/*|white-listed=true" \
    --resources="uri=/img/*|white-listed=true" \
    --resources="uri=/public/*|white-listed=true" \
    --headers="myheader1=value1" \
    --headers="myheader2=value2"
```

默认情况下，资源上定义的角色执行逻辑`AND`，因此指定的所有角色必须存在于声明中，但是，这种行为可以通过`require-any-role`选项进行更改，因此只要一个角色是 目前授予许可。

#### 2.4.4. OpenID提供商通信 {#OpenID_Provider_Communication}
默认情况下，与OpenID提供程序的通信是直接的。 如果您愿意，可以在配置文件中指定转发代理服务器：

```
openid-provider-proxy: http://proxy.example.com:8080
```

#### 2.4.5. HTTP路由 {#HTTP_routing}
默认情况下，所有请求都将代理到上游，如果您希望确保所有请求都是身份验证，您可以使用此命令：

```
--resource=uri=/* # note, unless specified the method is assumed to be 'any|ANY'
```

HTTP路由规则遵循[chi](https://github.com/go-chi/chi#router-design)的指导原则。 资源的排序无关紧要，路由器将为您处理。

#### 2.4.6. 仅限会话的cookie {#Session-only_cookies}
默认情况下，访问和刷新cookie仅限会话，并在浏览器关闭时处理; 您可以使用`--enable-session-cookies`选项禁用此功能。

#### 2.4.7. 转发签名代理 {#Forward_signing_proxy}
转发签名提供了一种使用IdP发出的令牌在服务之间进行身份验证和授权的机制。 在此模式下运行时，代理将自动获取访问令牌（代表您处理刷新或登录）并使用Authorization标头标记出站请求。 您可以使用--forwarding-domains选项控制标记哪些域。 注意，此选项在域上使用**contains**比较。 因此，如果您想匹配*.svc.cluster.local下的所有域，您可以使用： - forwarding-domain=svc.cluster.local。

目前，该服务使用oauth client_credentials授权类型执行登录，因此您的IdP服务必须支持直接（用户名/密码）登录。

示例设置：

您收集了允许彼此交谈的微服务; 您已经在Keycloak中设置了凭据，角色和客户端，为问题令牌提供了精细的角色控制。

```bash
- name: keycloak-gatekeeper
  image: quay.io/gambol99/keycloak-generic-adapter:latest
  args:
  - --enable-forwarding=true
  - --forwarding-username=projecta
  - --forwarding-password=some_password
  - --forwarding-domains=projecta.svc.cluster.local
  - --forwarding-domains=projectb.svc.cluster.local
  - --tls-ca-certificate=/etc/secrets/ca.pem
  - --tls-ca-key=/etc/secrets/ca-key.pem
  # Note: if you don't specify any forwarding domains, all domains will be signed; Also the code checks is the
  # domain 'contains' the value (it's not a regex) so if you wanted to sign all requests to svc.cluster.local, just use
  # svc.cluster.local
  volumeMounts:
  - name: keycloak-socket
    mountPoint: /var/run/keycloak
- name: projecta
  image: some_images

# test the forward proxy
$ curl -k --proxy http://127.0.0.1:3000 https://test.projesta.svc.cluster.local
```

在接收方，您可以设置Keycloak Gatekeeper（--no=redirects=true）并允许此项验证并处理您的入场许可。 或者，可以在请求中找到访问令牌作为承载令牌。

#### 2.4.8. 转发已签名的HTTPS连接 {#Forwarding_signed_HTTPS_connections}
处理HTTPS需要中间人进行TLS连接。 默认情况下，如果没有提供`--tls-ca-certificate`和`--tls-ca-key`，代理将使用默认证书。 如果您希望验证信任，则需要生成CA.

```bash
$ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout ca.key -out ca.pem
$ bin/keycloak-gatekeeper \
  --enable-forwarding \
  --forwarding-username=USERNAME \
  --forwarding-password=PASSWORD \
  --client-id=CLIENT_ID \
  --client-secret=SECRET \
  --discovery-url=https://keycloak.example.com/auth/realms/test \
  --tls-ca-certificate=ca.pem \
  --tls-ca-key=ca-key.pem
```

#### 2.4.9. HTTPS重定向 {#HTTPS_redirect}
代理支持HTTP侦听器，因此这里唯一真正的要求是执行HTTP→HTTPS重定向。 您可以启用以下选项：

```yaml
--listen-http=127.0.0.1:80
--enable-security-filter=true  # is required for the https redirect
--enable-https-redirection
```

#### 2.4.10. 我们加密配置 {#Lets_Encrypt_configuration}
以下是Let's Encrypt支持所需配置的示例：

```yaml
listen: 0.0.0.0:443
enable-https-redirection: true
enable-security-filter: true
use-letsencrypt: true
letsencrypt-cache-dir: ./cache/
redirection-url: https://domain.tld:443/
hostnames:
  - domain.tld
```

必须通过端口443进行侦听。

#### 2.4.11. 访问令牌加密 {#Access_token_encryption}
默认情况下，会话令牌以明文形式放入cookie中。 如果您更喜欢加密会话cookie，请使用`--enable-encrypted-token`和`--encryption-key`选项。 请注意，在X-Auth-Token标头中转发到上游的访问令牌不受影响。

#### 2.4.12. 上游标题 {#Upstream_headers}
在受保护的资源上，上游端点将接收代理添加的多个标头以及自定义声明，如下所示：

```javascript
# add the header to the upstream endpoint
id := user.(*userContext)
cx.Request().Header.Set("X-Auth-Email", id.email)
cx.Request().Header.Set("X-Auth-ExpiresIn", id.expiresAt.String())
cx.Request().Header.Set("X-Auth-Groups", strings.Join(id.groups, ","))
cx.Request().Header.Set("X-Auth-Roles", strings.Join(id.roles, ","))
cx.Request().Header.Set("X-Auth-Subject", id.id)
cx.Request().Header.Set("X-Auth-Token", id.token.Encode())
cx.Request().Header.Set("X-Auth-Userid", id.name)
cx.Request().Header.Set("X-Auth-Username", id.name)
// step: add the authorization header if requested
if r.config.EnableAuthorizationHeader {
        cx.Request().Header.Set("Authorization", fmt.Sprintf("Bearer %s", id.token.Encode()))
}
```

要控制`Authorization`头，请使用`enable-authorization-header` yaml配置或`--enable-authorization-header`命令行选项。 默认情况下，此选项设置为`true`。

#### 2.4.13. 自定义声明标头 {#Custom_claim_headers}
您可以使用`--add-claims`选项将访问令牌中的其他声明注入到授权标头中。 例如，来自Keycloak提供商的令牌可能包含以下声明：

```yaml
"resource_access": {},
"name": "Beloved User",
"preferred_username": "beloved.user",
"given_name": "Beloved",
"family_name": "User",
"email": "beloved@example.com"
```

为了请求您在身份验证标头中收到given_name，family_name和name，我们将添加`--add-claims=given_name` 和 `--add-claims=family_name`等等，或者我们可以在配置中执行此操作 文件，像这样：

```yaml
add-claims:
- given_name
- family_name
- name
```

这会将附加标头与标准请求一起添加到经过身份验证的请求中。

```yaml
X-Auth-Family-Name: User
X-Auth-Given-Name: Beloved
X-Auth-Name: Beloved User
```

#### 2.4.14. 自定义标题 {#Custom_headers}
您可以使用`--headers="name=value"`选项或配置文件注入自定义标头：

```yaml
headers:
  name: value
```

#### 2.4.15. 加密密钥 {#Encryption_key}
为了保持无状态而不必依赖中央缓存来持久化refresh_tokens，刷新令牌被加密并使用**crypto/aes **作为cookie添加。 如果您在负载均衡器后面运行，则密钥必须相同。 密钥长度应为16或32字节，具体取决于您是否需要AES-128或AES-256。

#### 2.4.16. 要求匹配 {#Claim_matching}
代理支持针对所呈现的令牌添加声明匹配的变量列表以用于附加访问控制。 您可以将'iss'或'aud'与令牌或自定义属性进行匹配; 每个匹配都是正则表达式。 例如，`--match-claims'aud=sso.*'`或`--claim iss = https//.*'`或通过配置文件，如下所示：

```yaml
match-claims:
  aud: openvpn
  iss: https://keycloak.example.com/auth/realms/commons
```

或者通过CLI，像这样：

```yaml
--match-claims=auth=openvpn
--match-claims=iss=http://keycloak.example.com/realms/commons
```

您可以限制允许的电子邮件域; 例如，如果您要仅限于example.com域上的用户：

```yaml
match-claims:
  email: ^.*@example.com$
```

适配器支持匹配多值字符串声明。 如果其中一个值匹配，匹配将成功，例如：

```yaml
match-claims:
  perms: perm1
```

将成功匹配

```json
{
  "iss": "https://sso.example.com",
  "sub": "",
  "perms": ["perm1", "perm2"]
}
```

#### 2.4.17. 组要求 {#Group_claims}
您可以通过资源中可用的`groups`参数匹配令牌中的组声明。 虽然隐含地要求角色，例如`roles=admin,user`，其中用户必须具有角色'admin'和'user'，组应用OR操作，因此`groups=users,testers`要求用户必须 在'users'或'testers'中。 声明名称被硬编码为`groups`，因此JWT标记看起来像这样：

```java
{
  "iss": "https://sso.example.com",
  "sub": "",
  "aud": "test",
  "exp": 1515269245,
  "iat": 1515182845,
  "email": "beloved@example.com",
  "groups": [
    "group_one",
    "group_two"
  ],
  "name": "Beloved"
}
```

#### 2.4.18. 自定义页面 {#Custom_pages}
默认情况下，Keycloak Gatekeeper会立即重定向您进行身份验证，并返回403以拒绝访问。 大多数用户可能希望向用户显示更友好的登录和访问被拒绝的页面。 您可以使用`--signin-page=PATH`将命令行选项（或通过配置文件）路径传递给文件。 登录页面将有一个“重定向”变量传递到作用域并保存oauth重定向URL。 如果你想将其他变量传递给模板，比如title，sitename等，你可以使用`--tags key=pair`选项，如下所示：`--tags title="This is my site"`并且可以从`{{title}}`访问该变量。

```html
<html>
<body>
<a href="{{ redirect }}">Sign-in</a>
</body>
</html>
```

#### 2.4.19. 白名单 URL's {#White_listed_URLs}
根据应用程序URL的布局方式，您可能需要保护根/ URL，但在路径列表中有例外，例如`/health`。 虽然通过调整路径可以最好地解决这个问题，但您可以向受保护资源添加例外，如下所示：

```yaml
  resources:
  - uri: /some_white_listed_url
    white-listed: true
  - uri: /*
    methods:
      - GET
    roles:
      - <CLIENT_APP_NAME>:<ROLE_NAME>
      - <CLIENT_APP_NAME>:<ROLE_NAME>
```

或者在命令行上

```bash
  --resources "uri=/some_white_listed_url|white-listed=true"
  --resources "uri=/*"  # requires authentication on the rest
  --resources "uri=/admin*|roles=admin,superuser|methods=POST,DELETE"
```

#### 2.4.20. 交互 TLS {#Mutual_TLS}
代理支持通过添加`--tls-ca-certificate`命令行选项或配置文件选项为客户端强制执行相互TLS。 连接的所有客户端必须提供由正在使用的CA签名的证书。

#### 2.4.21. 证书轮换 {#Certificate_rotation}
如果文件在磁盘上更改，代理将自动轮换服务器证书。 请注意，内联更改不会发生停机时间。 在证书轮换之前连接的客户端将不受影响，并且将继续正常使用新证书提供的所有新连接。

#### 2.4.22. 刷新 tokens {#Refresh_tokens}
如果对访问令牌的请求包含刷新令牌并且`--enable-refresh-tokens`设置为`true`，则代理将自动为您刷新访问令牌。 令牌本身保存为加密的 **(--encryption-key=KEY)** cookie **(cookie name: kc-state).**。或存储 **(still requires encryption key)**。

目前支持的唯一存储选项是[Redis](https://github.com/antirez/redis) 和[Boltdb](https://github.com/boltdb/bolt)。

要启用本地boltdb存储，请使用`--store-url boltdb:///PATH`或使用相对路径`boltdb://PATH`。

要启用本地redis存储，请使用`redis://[USER:PASSWORD@]HOST:PORT`。 在这两种情况下，刷新令牌在放入商店之前都会被加密。

#### 2.4.23. 注销端点 {#Logout_endpoint}
提供**/oauth/logout?redirect=url**作为帮助记录用户的帮助程序。 除了删除任何会话cookie之外，我们还尝试通过提供者撤销通过吊销URL(config **revocation-url** or **--revocation-url**)的访问权限。 对于Keycloak，其网址为<https://keycloak.example.com/auth/realms/REALM_NAME/protocol/openid-connect/logout>。 如果未指定url，我们将尝试从OpenID发现响应中获取url。

#### 2.4.24. 跨域资源共享 (CORS) {#Cross_origin_resource_sharing__CORS}
您可以使用这些配置选项通过`--cors-[method]`添加CORS头。

- Access-Control-Allow-Origin
- Access-Control-Allow-Methods
- Access-Control-Allow-Headers
- Access-Control-Expose-Headers
- Access-Control-Allow-Credentials
- Access-Control-Max-Age

您可以使用配置文件添加：

```yaml
cors-origins:
- '*'
cors-methods:
- GET
- POST
```

或通过命令行：

```
--cors-origins [--cors-origins option]                  一组要添加到CORS访问控制的起源 (Access-Control-Allow-Origin)
--cors-methods [--cors-methods option]                  访问控制中允许的方法 (Access-Control-Allow-Methods)
--cors-headers [--cors-headers option]                  要添加到CORS访问控制的一组标头 (Access-Control-Allow-Headers)
--cors-exposes-headers [--cors-exposes-headers option]  设置公开的cors标头访问控制 (Access-Control-Expose-Headers)
```

#### 2.4.25. 上游URL {#Upstream_URL}
您可以通过`--upstream-url`选项控制上游端点。 通过`--skip-upstream-tls-verify` / `--upstream-keepalives`选项配置TLS验证和保持活动支持，支持HTTP和HTTPS。 注意，代理也可以通过UNIX套接字上游，`--upstream-url unix://path/to/the/file.sock`。

#### 2.4.26. 端点 {#Endpoints}
- **/oauth/authorize** 是身份验证端点，它将生成OpenID重定向到提供程序
- **/oauth/callback** 是提供者OpenID回调端点
- **/oauth/expired** 是一个辅助端点，用于检查访问令牌是否已过期，200表示ok，401表示无令牌，401表示已过期
- **/oauth/health** 是代理的运行状况检查端点，您也可以从标头中获取版本
- **/oauth/login** 提供一个通过`grant_type=password`登录的中继端点，例如，POST /oauth/login`表单值为`username=USERNAME＆password=PASSWORD`（必须启用）
- **/oauth/logout** 提供了一个方便的端点来记录用户，它总是会尝试从脱机令牌中执行反向通道注销
- **/oauth/token** 是一个帮助端点，它将为您显示当前的访问令牌
- **/oauth/metrics** 是一个Prometheus指标处理程序

#### 2.4.27. 度量 {#Metrics}
假设已经设置了`--enable-metrics`，可以在**/oauth/metrics **上找到Prometheus端点; 目前，唯一公开的度量标准是每个HTTP代码的计数器。

#### 2.4.28. 限制 {#Limitations}
如果您在浏览器cookie中使用访问权限或刷新令牌，请记住[浏览器cookie限制]（http://browsercookielimits.squawky.net/）。 如果您的cookie超过4093字节，Keycloak-generic-adapter会自动划分cookie。 cookie的实际大小取决于发布的访问令牌的内容。 此外，加密可能会为cookie大小添加额外的字节。 如果您有大型Cookie（> 200 KB），则可能会达到浏览器Cookie限制。

所有cookie都是标头请求的一部分，因此您可能会发现基础结构中的最大标头大小限制存在问题（某些负载平衡器的此值非常低，例如8 KB）。 确保所有网络设备都有足够的标头大小限制。 否则，您的用户将无法获得访问令牌。

#### 2.4.29. 已知的问题 {#Known_Issues}
- Keycloak服务器4.7.0.Final存在一个已知问题，其中Gatekeeper无法在*aud*声明中找到*client_id*。 这是因为*client_id*不再在观众中。 解决方法是将“Audience(受众)”协议映射器添加到客户端，并将受众指向*client_id*。 有关详细信息，请参阅[KEYCLOAK-8954](https://issues.jboss.org/browse/KEYCLOAK-8954)。 ==== mod_auth_openidc Apache HTTPD模块

[mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc)是OpenID Connect的Apache HTTP插件。 如果您的语言/环境支持使用Apache HTTPD作为代理，那么您可以使用*mod_auth_openidc*来使用OpenID Connect保护您的Web应用程序。 此模块的配置超出了本文档的范围。 有关配置的更多详细信息，请参阅*mod_auth_openidc* GitHub 仓库。

要配置*mod_auth_openidc*，您需要

- The client_id.
- The client_secret.
- redirect_uri到您的应用程序。
- Keycloak openid配置网址
- *mod_auth_openidc*特定的Apache HTTPD模块配置。

示例配置如下所示。

```
LoadModule auth_openidc_module modules/mod_auth_openidc.so

ServerName ${HOSTIP}

<VirtualHost *:80>

    ServerAdmin webmaster@localhost
    DocumentRoot /var/www/html

    #this is required by mod_auth_openidc
    OIDCCryptoPassphrase a-random-secret-used-by-apache-oidc-and-balancer

    OIDCProviderMetadataURL ${KC_ADDR}/auth/realms/${KC_REALM}/.well-known/openid-configuration

    OIDCClientID ${CLIENT_ID}
    OIDCClientSecret ${CLIENT_SECRET}
    OIDCRedirectURI http://${HOSTIP}/${CLIENT_APP_NAME}/redirect_uri

    # maps the prefered_username claim to the REMOTE_USER environment variable
    OIDCRemoteUserClaim preferred_username

    <Location /${CLIENT_APP_NAME}/>
        AuthType openid-connect
        Require valid-user
    </Location>
</VirtualHost>
```

有关如何配置mod_auth_openidc的更多信息，请参见[mod_auth_openidc](https://github.com/zmartzone/mod_auth_openidc) 项目页面。

### 2.5. 其他OpenID连接库 {#Other_OpenID_Connect_Libraries}

Keycloak可以通过提供的适配器进行保护，这些适配器通常更易于使用，并且可以更好地与Keycloak集成。 但是，如果适配器不适用于您的编程语言，框架或平台，则可以选择使用通用OpenID Connect资源提供程序（RP）库。 本章介绍了Keycloak的具体细节，但不包含特定的协议详细信息。 有关详细信息，请参阅[OpenID Connect规范](https://openid.net/connect/)和[OAuth2规范](https://tools.ietf.org/html/rfc6749)。

#### 2.5.1. 端点 {#Endpoints}
要了解的最重要的端点是`well-known(众所周知的)`配置端点。 它列出了与Keycloak中的OpenID Connect实现相关的端点和其他配置选项。 端点是：

```
/realms/{realm-name}/.well-known/openid-configuration
```

要获取完整的URL，请添加Keycloak的基本URL，并将`{realm-name}`替换为您的域的名称。 例如：

http://localhost:8080/auth/realms/master/.well-known/openid-configuration

某些RP库从此端点检索所有必需的端点，但对于其他端点，您可能需要单独列出端点。

##### 授权端点 {#Authorization_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/auth
```

授权端点执行最终用户的身份验证。 这是通过将用户代理重定向到此端点来完成的。

有关更多详细信息，请参阅OpenID Connect规范中的[授权端点](https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint) 部分。

##### 令牌端点 {#Token_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/token
```

令牌端点用于获取令牌。 可以通过交换授权代码或直接提供凭证来获取令牌，具体取决于使用的流程。 令牌端点还用于在到期时获取新的访问令牌。

有关更多详细信息，请参阅OpenID Connect规范中的[令牌端点](https://openid.net/specs/openid-connect-core-1_0.html#TokenEndpoint)部分。

##### Userinfo端点 {#Userinfo_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/userinfo
```

userinfo端点返回有关经过身份验证的用户的标准声明，并受承载令牌保护。

有关更多详细信息，请参阅OpenID Connect规范中的[Userinfo端点](https://openid.net/specs/openid-connect-core-1_0.html#UserInfo)部分。

##### 注销端点 {#Logout_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/logout
```

注销端点注销经过身份验证的用户。

可以将用户代理重定向到端点，在这种情况下，将注销活动用户会话。 之后，用户代理将重定向回应用程序。

端点也可以由应用程序直接调用。 要直接调用此端点，需要包含刷新令牌以及验证客户端所需的凭据。

##### 证书端点 {#Certificate_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/certs
```

证书端点返回由领域启用的公钥，编码为JSON Web密钥（JWK）。 根据领域设置，可以启用一个或多个密钥来验证令牌。 有关详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/) 和[JSON Web密钥规范](https://tools.ietf.org/html/rfc7517)。

##### 内省端点 {#Introspection_Endpoint}
```
/realms/{realm-name}/protocol/openid-connect/token/introspect
```

内省端点用于检索令牌的活动状态。 换句话说，您可以使用它来验证访问或刷新令牌。 它只能由机密客户端调用。

有关如何在此端点上调用的更多详细信息，请参阅[OAuth 2.0 Token Introspection规范](https://tools.ietf.org/html/rfc7662)。

##### 动态客户端注册端点 {#Dynamic_Client_Registration_Endpoint}
```
/realms/{realm-name}/clients-registrations/openid-connect
```

动态客户端注册端点用于动态注册客户端。

有关详细信息，请参阅[客户端注册章节](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_registration)和[OpenID Connect动态客户端注册规范](https://openid.net/specs/openid-connect-registration-1_0.html)。

#### 2.5.2. 验证访问令牌 {#Validating_Access_Tokens}
如果您需要手动验证Keycloak发出的访问令牌，您可以调用[内省端点](https://www.keycloak.org/docs/latest/securing_apps/index.html#_token_introspection_endpoint)。 这种方法的缺点是您必须对Keycloak服务器进行网络调用。 如果您同时进行太多验证请求，这可能会很慢并且可能会使服务器过载。 Keycloak颁发的访问令牌是[JSON Web令牌（JWT）](https://tools.ietf.org/html/rfc7519)，使用[JSON Web签名（JWS）](https://www.rfc-editor.org/rfc/rfc7515.txt)进行数字签名和编码。 因为它们是以这种方式编码的，所以这允许您使用发布领域的公钥在本地验证访问令牌。 您可以在验证代码中对域的公钥进行硬编码，也可以使用[证书端点]查找和缓存公钥(https://www.keycloak.org/docs/latest/securing_apps/index.html#_certificate_endpoint)使用JWS中嵌入的密钥ID（KID）。 根据您编写的语言，有许多第三方库可以帮助您进行JWS验证。

#### 2.5.3. 流 {#Flows}
##### 授权码 {#Authorization_Code}
授权代码流将用户代理重定向到Keycloak。 一旦用户成功通过Keycloak进行身份验证，就会创建一个授权码，并将用户代理重定向回应用程序。 然后，应用程序使用授权代码及其凭据从Keycloak获取访问令牌，刷新令牌和ID令牌。

该流程针对Web应用程序，但也建议用于本机应用程序，包括可以嵌入用户代理的移动应用程序。

有关更多详细信息，请参阅OpenID Connect规范中的[授权代码流程](https://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth) 。

##### 隐式 {#Implicit}
隐式流重定向的工作方式与授权代码流类似，但不返回授权代码，而是返回访问令牌和ID令牌。 这减少了额外调用以交换访问令牌的授权码的需要。 但是，它不包括刷新令牌。 这导致需要允许具有长期到期的访问令牌，这是有问题的，因为很难使这些无效。 或者在初始访问令牌过期后需要新的重定向来获取新的访问令牌。 如果应用程序只想验证用户并处理注销本身，则隐式流非常有用。

还有一个混合流程，其中返回访问令牌和授权代码。

需要注意的一点是，隐式流和混合流都存在潜在的安全风险，因为访问令牌可能会通过Web服务器日志和浏览器历史泄漏。 通过使用访问令牌的短期到期，可以稍微减轻这种情况。

有关更多详细信息，请参阅OpenID Connect规范中的[Implicit Flow](https://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth)。

##### 资源所有者密码凭据 {#Resource_Owner_Password_Credentials}
资源所有者密码凭据（在Keycloak中称为直接授权）允许为令牌交换用户凭据。 除非您绝对需要，否则不建议使用此流程。 这可能有用的示例是遗留应用程序和命令行界面。

使用此流程有许多限制，包括：

- 用户凭据将公开给应用程序
- 应用需要登录页面
- 应用程序需要了解身份验证方案
- 对身份验证流程的更改需要更改应用程序
- 不支持身份代理或社交登录
- 不支持流程（用户自行注册，所需操作等）

要允许客户端使用资源所有者密码凭据授予，客户端必须启用`Direct Access Grants Enabled`选项。

此流程不包含在OpenID Connect中，但是是OAuth 2.0规范的一部分。

有关更多详细信息，请参阅OAuth 2.0规范中的[资源所有者密码凭据授权](https://tools.ietf.org/html/rfc6749#section-4.3)章节。

###### 使用CURL的示例 {#Example_using_CURL}
以下示例显示如何使用用户名`user`和密码`password`为领域`master`中的用户获取访问令牌。 示例使用机密客户端`myclient`：

```bash
curl \
  -d "client_id=myclient" \
  -d "client_secret=40cc097b-2a57-4c17-b36a-8fdf3fc2d578" \
  -d "username=user" \
  -d "password=password" \
  -d "grant_type=password" \
  "http://localhost:8080/auth/realms/master/protocol/openid-connect/token"
```

##### 客户端凭据 {#Client_Credentials}
客户端（应用程序和服务）希望代表自己而不是代表用户获取访问权限时使用客户端凭据。 例如，这对于通常而不是针对特定用户应用对系统的更改的后台服务是有用的。

Keycloak支持客户端使用密钥或公钥/私钥进行身份验证。

此流程不包含在OpenID Connect中，但是是OAuth 2.0规范的一部分。

有关更多详细信息，请参阅OAuth 2.0规范中的[Client Credentials Grant](https://tools.ietf.org/html/rfc6749#section-4.4)章节。

#### 2.5.4. 重定向URI {#Redirect_URIs}
使用基于重定向的流时，为您的客户使用有效的重定向uris很重要。 重定向uris应尽可能具体。 这尤其适用于客户端（公共客户端）应用程序。 如果不这样做可能会导致：

- Open redirects - 这可以允许攻击者创建看似他们来自您的域的欺骗链接
- Unauthorized entry - 当用户已经使用Keycloak进行身份验证时，攻击者可以使用公共客户端，其中未正确配置重定向uris以通过重定向用户而无需用户知识来获取访问权限

在Web应用程序的生产中，始终对所有重定向URI使用`https`。 不允许重定向到http。

还有一些特殊的重定向URI：

- `http://localhost`

  此重定向URI对本机应用程序很有用，并允许本机应用程序在随机端口上创建可用于获取授权代码的Web服务器。 这个重定向uri允许任何端口。

- `urn:ietf:wg:oauth:2.0:oob`

  如果无法在客户端启动Web服务器（或浏览器不可用），则可以使用特殊的`urn:ietf:wg:oauth:2.0:oob`重定向uri。 使用此重定向uri时，Keycloak会显示一个页面，其中包含标题中的代码和页面上的框。 应用程序可以检测到浏览器标题已更改，或者用户可以手动将代码复制/粘贴到应用程序。 通过此重定向uri，用户还可以使用不同的设备来获取要粘贴回应用程序的代码。

## 3. SAML {#SAML}

本节介绍如何使用Keycloak客户端适配器或通用SAML提供程序库使用SAML保护应用程序和服务。

### 3.1. Java 适配器 {#Java_Adapters}

Keycloak为Java应用程序提供了一系列不同的适配器。 选择正确的适配器取决于目标平台。

#### 3.1.1. 通用适配器配置 {#General_Adapter_Config}

Keycloak支持的每个SAML客户端适配器都可以通过简单的XML文本文件进行配置。 这可能是这样的：

```xml
<keycloak-saml-adapter xmlns="urn:keycloak:saml:adapter"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="urn:keycloak:saml:adapter https://www.keycloak.org/schema/keycloak_saml_adapter_1_10.xsd">
    <SP entityID="http://localhost:8081/sales-post-sig/"
        sslPolicy="EXTERNAL"
        nameIDPolicyFormat="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
        logoutPage="/logout.jsp"
        forceAuthentication="false"
        isPassive="false"
        turnOffChangeSessionIdOnLogin="false"
        autodetectBearerOnly="false">
        <Keys>
            <Key signing="true" >
                <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                    <PrivateKey alias="http://localhost:8080/sales-post-sig/" password="test123"/>
                    <Certificate alias="http://localhost:8080/sales-post-sig/"/>
                </KeyStore>
            </Key>
        </Keys>
        <PrincipalNameMapping policy="FROM_NAME_ID"/>
        <RoleIdentifiers>
            <Attribute name="Role"/>
        </RoleIdentifiers>
        <IDP entityID="idp"
             signaturesRequired="true">
        <SingleSignOnService requestBinding="POST"
                             bindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    />

            <SingleLogoutService
                    requestBinding="POST"
                    responseBinding="POST"
                    postBindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    redirectBindingUrl="http://localhost:8081/auth/realms/demo/protocol/saml"
                    />
            <Keys>
                <Key signing="true">
                    <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                        <Certificate alias="demo"/>
                    </KeyStore>
                </Key>
            </Keys>
        </IDP>
     </SP>
</keycloak-saml-adapter>
```

其中一些配置开关可能是适配器特定的，有些在所有适配器上都是通用的。 对于Java适配器，您可以使用`${…}`enclosure作为System属性替换。 例如`${jboss.server.config.dir}`。

##### SP元素 {#SP_Element}

以下是SP元素属性的说明：

```xml
<SP entityID="sp"
    sslPolicy="ssl"
    nameIDPolicyFormat="format"
    forceAuthentication="true"
    isPassive="false"
    autodetectBearerOnly="false">
...
</SP>
```

- entityID

  这是此客户端的标识符。 IdP需要此值来确定与之通信的客户端。 此设置为*REQUIRED*。

- sslPolicy

  这是适配器将强制执行的SSL策略。 有效值为：`ALL`，`EXTERNAL`和`NONE`。 对于`ALL`，所有请求必须通过HTTPS进入。 对于`EXTERNAL`，只有非私有IP地址必须通过HTTPS连接。 对于`NONE`，不需要通过HTTPS接收请求。 此设置为*OPTIONAL*。 默认值为`EXTERNAL`。

- nameIDPolicyFormat

  SAML客户端可以请求特定的NameID主题格式。 如果需要特定格式，请填写此值。 它必须是标准的SAML格式标识符：`urn:oasis:names:tc:SAML:2.0:nameid-format:transient`。 此设置为*OPTIONAL*。 默认情况下，不请求特殊格式。

- forceAuthentication

  SAML客户端可以请求用户重新进行身份验证，即使他们已经在IdP登录也是如此。 将其设置为`true`以启用。 此设置为*OPTIONAL*。 默认值为`false`。

- isPassive

  SAML客户端可以请求永远不会要求用户进行身份验证，即使他们未在IdP登录也是如此。 如果你想要这个，请将其设置为`true`。 不要与`forceAuthentication`一起使用，因为它们相反。 此设置为*OPTIONAL*。 默认值为`false`。

- turnOffChangeSessionIdOnLogin

  默认情况下，在某些平台上成功登录时会更改会话ID以插入安全攻击媒介。 将其更改为`true`以禁用此功能。 建议您不要将其关闭。 默认值为`false`。

- autodetectBearerOnly

  如果您的应用程序同时提供Web应用程序和Web服务（例如SOAP或REST），则应将其设置为*true*。 它允许您将未经身份验证的Web应用程序用户重定向到Keycloak登录页面，但是将HTTP`401`状态代码发送给未经身份验证的SOAP或REST客户端，因为他们无法理解重定向到登录页面。 Keycloak基于典型的标题自动检测SOAP或REST客户端，如`X-Requested-With`，`SOAPAction`或`Accept`。 默认值为*false*。

- logoutPage

  这会将页面设置为在注销后显示。 如果页面是完整的URL，例如`http://web.example.com/logout.html`，则在使用HTTP`302`状态代码注销到该页面后，将重定向用户。 如果指定了没有scheme部分的链接，例如`/ logout.jsp`，则在注销后会显示该页面， *根据web.xml中的安全性约束声明，它是否位于受保护区域中*，并且相对于部署上下文根解析页面。

##### 服务提供商密钥和关键元素 {#Service_Provider_Keys_and_Key_Elements}

如果IdP要求客户端应用程序（或SP）签署其所有请求和/或IdP将加密断言，则必须定义用于执行此操作的密钥。 对于客户端签名的文档，您必须定义用于签署文档的私钥和公钥或证书。 对于加密，您只需定义用于解密它的私钥。

有两种方法可以描述您的密钥。 它们可以存储在Java KeyStore中，也可以直接在PEM格式的`keycloak-saml.xml`中复制/粘贴密钥。

```xml
        <Keys>
            <Key signing="true" >
               ...
            </Key>
        </Keys>
```

`Key`元素有两个可选属性`signing`和`encryption`。 设置为true时，这些将告诉适配器密钥的用途。 如果两个属性都设置为true，则密钥将用于签名文档和解密加密断言。 您必须将这些属性中的至少一个设置为true。

###### KeyStore元素 {#KeyStore_element}

在`Key`元素中，您可以从Java Keystore加载密钥和证书。 这是在`KeyStore`元素中声明的。

```xml
        <Keys>
            <Key signing="true" >
                <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                    <PrivateKey alias="myPrivate" password="test123"/>
                    <Certificate alias="myCertAlias"/>
                </KeyStore>
            </Key>
        </Keys>
```

以下是使用`KeyStore`元素定义的XML配置属性。

- file

  密钥库的文件路径。 此选项为*OPTIONAL*。 必须设置文件或资源属性。

- resource

  KeyStore的WAR资源路径。 这是对ServletContext.getResourceAsStream()的方法调用中使用的路径。 此选项为*OPTIONAL*。 必须设置文件或资源属性。

- password

  KeyStore的密码。 此选项为*REQUIRED*。

如果要定义SP将用于签署文档的密钥，则还必须在Java KeyStore中指定对私钥和证书的引用。 上例中的`PrivateKey`和`Certificate`元素定义了一个指向密钥库中的密钥或证书的`alias`。 密钥库需要额外的密码才能访问私钥。 在`PrivateKey`元素中，您必须在`password`属性中定义此密码。

###### Key PEMS {#Key_PEMS}

在`Key`元素中，您使用子元素`PrivateKeyPem`，`PublicKeyPem`和`CertificatePem`直接声明您的键和证书。 这些元素中包含的值必须符合PEM密钥格式。 如果使用`openssl`或类似的命令行工具生成密钥，通常使用此选项。

```xml
<Keys>
   <Key signing="true">
      <PrivateKeyPem>
         2341251234AB31234==231BB998311222423522334
      </PrivateKeyPem>
      <CertificatePem>
         211111341251234AB31234==231BB998311222423522334
      </CertificatePem>
   </Key>
</Keys>
```

##### SP PrincipalNameMapping 元素 {#SP_PrincipalNameMapping_element}

此元素是可选的。 在创建从诸如`HttpServletRequest.getUserPrincipal()`之类的方法获得的Java Principal对象时，可以定义`Principal.getName()`方法返回的名称。

```xml
<SP ...>
  <PrincipalNameMapping policy="FROM_NAME_ID"/>
</SP>

<SP ...>
  <PrincipalNameMapping policy="FROM_ATTRIBUTE" attribute="email" />
</SP>
```

`policy`属性定义用于填充此值的策略。 此属性的可能值为：

- FROM_NAME_ID

  此策略仅使用SAML主题值。 这是默认设置

- FROM_ATTRIBUTE

  这将从服务器收到的SAML断言中声明的其中一个属性中提取值。 您需要指定要在`attribute` XML属性中使用的SAML断言属性的名称。

##### RoleIdentifiers 元素 {#RoleIdentifiers_Element}

`RoleIdentifiers`元素定义从用户接收的断言中的SAML属性应该用作用户的Java EE安全上下文中的角色标识符。

```xml
<RoleIdentifiers>
     <Attribute name="Role"/>
     <Attribute name="member"/>
     <Attribute name="memberOf"/>
</RoleIdentifiers>
```

默认情况下，`Role`属性值将转换为Java EE角色。 一些IdP使用`member`或`memberOf`attribute断言发送角色。 您可以定义一个或多个`Attribute`元素，以指定必须将哪些SAML属性转换为角色。

##### IDP 元素 {#IDP_Element}

IDP元素中的所有内容都描述了SP正在与之通信的身份提供者（身份验证服务器）的设置。

```xml
<IDP entityID="idp"
     signaturesRequired="true"
     signatureAlgorithm="RSA_SHA1"
     signatureCanonicalizationMethod="http://www.w3.org/2001/10/xml-exc-c14n#">
...
</IDP>
```

以下是您可以在`IDP`元素声明中指定的属性配置选项。

- entityID

  这是IDP的发行者ID。 此设置为*REQUIRED*。

- signaturesRequired

  如果设置为`true`，则客户端适配器将对它发送给IDP的每个文档进行签名。 此外，客户将期望IDP将签署发送给它的任何文件。 此开关设置所有请求和响应类型的默认值，但稍后您将看到您对此有一些细粒度控制。 此设置为*OPTIONAL*，默认为`false`。

- signatureAlgorithm

  这是IDP期望签名文档使用的签名算法。 允许的值为：`RSA_SHA1`，`RSA_SHA256`，`RSA_SHA512`和`DSA_SHA1`。 此设置为*OPTIONAL*，默认为`RSA_SHA256`。

- signatureCanonicalizationMethod

  这是IDP期望签名文档使用的签名规范化方法。 此设置为*OPTIONAL*。 默认值为`http://www.w3.org/2001/10/xml-exc-c14n#`，对大多数IPDs应该是好的。

- metadataUrl

  用于检索IDP元数据的URL，目前仅用于定期获取签名和加密密钥，允许在IDP上循环使用这些密钥，而无需在SP端手动更改。

##### IDP SingleSignOnService 子元素 {#IDP_SingleSignOnService_sub_element}

`SingleSignOnService`子元素定义IDP的登录SAML端点。 当客户端适配器要登录时，它将通过此元素中的设置向IDP发送请求。

```xml
<SingleSignOnService signRequest="true"
                     validateResponseSignature="true"
                     requestBinding="post"
                     bindingUrl="url"/>
```

以下是您可以在此元素上定义的配置属性：

- signRequest

  客户应该签署authn请求吗？ 此设置为*OPTIONAL*。 默认为IDP`ignaturesRequired`元素值。

- validateResponseSignature

  客户是否希望IDP签署从auhtn请求发回的断言响应文档？ 此设置*OPTIONAL*。 默认为IDP`validateResponseSignature`元素值。

- requestBinding

  这是用于与IDP通信的SAML绑定类型。 此设置为*OPTIONAL*。 默认值为`POST`，但您也可以将其设置为`REDIRECT`。

- responseBinding

  SAML允许客户端请求它想要authn响应使用的绑定类型。 这个值可以是`POST`或`REDIRECT`。 此设置为*OPTIONAL*。 默认情况下，客户端不会为响应请求特定的绑定类型。

- assertionConsumerServiceUrl

  IDP登录服务应向其发送响应的断言使用者服务（ACS）的URL。 此设置为*OPTIONAL*。 默认情况下，它取消设置，依赖于IdP中的配置。 设置时，它必须以`/saml`结尾，例如`http://sp.domain.com/my/endpoint/for/saml`。 此属性的值在SAML`AuthnRequest`消息的`AssertionConsumerServiceURL`属性中发送。 该属性通常伴随着`responseBinding`属性。

- bindingUrl

  这是客户端将请求发送到的IDP登录服务的URL。 此设置为*REQUIRED*。

##### IDP SingleLogoutService 子元素 {#IDP_SingleLogoutService_sub_element}

`SingleLogoutService`子元素定义IDP的注销SAML端点。 当客户端适配器要注销时，它将通过此元素中的设置向IDP发送请求。

```xml
<SingleLogoutService validateRequestSignature="true"
                     validateResponseSignature="true"
                     signRequest="true"
                     signResponse="true"
                     requestBinding="redirect"
                     responseBinding="post"
                     postBindingUrl="posturl"
                     redirectBindingUrl="redirecturl">
```

- signRequest

  客户应该签署注销请求吗？ 此设置为*OPTIONAL*。 默认为IDP`signaturesRequired`元素值。

- signResponse

  如果客户端签署注销响应，它会发送给IDP请求吗？ 此设置为*OPTIONAL*。 默认为IDP`signaturesRequired`元素值。

- validateRequestSignature

  客户是否应该期待来自IDP的签名退出请求文件？ 此设置为*OPTIONAL*。 默认为IDP`signaturesRequired`元素值。

- validateResponseSignature

  客户是否应该期待来自IDP的签名注销响应文档？ 此设置为*OPTIONAL*。 默认为IDP`signaturesRequired`元素值。

- requestBinding

  这是用于将SAML请求传递给IDP的SAML绑定类型。 此设置为*OPTIONAL*。 默认值为`POST`，但您也可以将其设置为REDIRECT。

- responseBinding

  这是用于将SAML响应传递给IDP的SAML绑定类型。 这个值可以是`POST`或`REDIRECT`。 此设置为*OPTIONAL*。 默认值为`POST`，但您也可以将其设置为`REDIRECT`。

- postBindingUrl

  这是使用POST绑定时IDP注销服务的URL。 如果使用`POST`绑定，则此设置为*REQUIRED*。

- redirectBindingUrl

  这是使用REDIRECT绑定时IDP注销服务的URL。 如果使用REDIRECT绑定，此设置为*REQUIRED*。

##### IDP Keys 子元素 {#IDP_Keys_sub_element}

IDP的Keys子元素仅用于定义用于验证IDP签名的文档的证书或公钥。 它的定义方式与[SP的Keys元素](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-sp-keys)相同。 但同样，您只需要定义一个证书或公钥引用。 请注意，如果IDP和SP分别由Keycloak服务器和适配器实现，则无需指定用于签名验证的密钥，请参阅下文。

如果SP和IDP都由Keycloak实现，则可以将SP配置为自动从已发布的证书获取IDP签名验证的公钥。 这是通过删除Keys子元素中签名验证密钥的所有声明来完成的。 如果Keys子元素将保持为空，则可以完全省略它。 然后，SP从SAML描述符自动获取密钥，其位置来自[IDP SingleSignOnService子元素](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-singlesignonservice)中指定的SAML端点URL。用于SAML描述符检索的HTTP客户端的设置通常不需要额外配置，但可以在[IDP HttpClient子元素](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-httpclient)中配置。

还可以为签名验证指定多个密钥。 这是通过在Keys子元素中声明`signed`属性设置为`true`的多个Key元素来完成的。 例如，在旋转IDP签名密钥的情况下，这很有用：通常有一个过渡期，当新的SAML协议消息和断言用新密钥签名但仍应接受由先前密钥签名的那些消息和断言。

无法将Keycloak配置为自动获取签名验证密钥并定义其他静态签名验证密钥。

```xml
       <IDP entityID="idp">
            ...
            <Keys>
                <Key signing="true">
                    <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
                        <Certificate alias="demo"/>
                    </KeyStore>
                </Key>
            </Keys>
        </IDP>
```

##### IDP HttpClient 子元素 {#IDP_HttpClient_sub_element}

`HttpClient`可选子元素定义HTTP客户端的属性，用于在[启用](https://www.keycloak.org/docs/latest/securing_apps/index.html#_sp-idp-keys-automatic)时通过IDP的SAML描述符自动获取包含用于IDP签名验证的公钥的证书。

```xml
<HttpClient connectionPoolSize="10"
            disableTrustManager="false"
            allowAnyHostname="false"
            clientKeystore="classpath:keystore.jks"
            clientKeystorePassword="pwd"
            truststore="classpath:truststore.jks"
            truststorePassword="pwd"
            proxyUrl="http://proxy/" />
```

- connectionPoolSize

  适配器将对Keycloak服务器进行单独的HTTP调用，以将访问代码转换为访问令牌。 此配置选项定义应该合并到Keycloak服务器的连接数。 这是*OPTIONAL*。 默认值为`10`。

- disableTrustManager

  如果Keycloak服务器需要HTTPS并且此配置选项设置为`true`，则不必指定信任库。 此设置仅应在开发期间使用，**永远**不会在生产中使用，因为它将禁用SSL证书的验证。 这是*OPTIONAL*。 默认值为`false`。

- allowAnyHostname

  如果Keycloak服务器需要HTTPS并且此配置选项设置为`true`，则通过信任库验证Keycloak服务器的证书，但不会进行主机名验证。 此设置应仅在开发期间使用，**永远**不会在生产中使用，因为它将部分禁用SSL证书的验证。 这种设置在测试环境中可能很有用。 这是*OPTIONAL*。 默认值为`false`。

- truststore

  该值是信任库文件的文件路径。 如果在路径前加上`classpath:`，那么将从部署的类路径中获取信任库。 用于与Keycloak服务器的传出HTTPS通信。 发出HTTPS请求的客户端需要一种方法来验证他们正在与之通信的服务器的主机。 这就是委托人所做的。 密钥库包含一个或多个可信主机证书或证书颁发机构。 您可以通过提取Keycloak服务器的SSL密钥库的公共证书来创建此信任库。 除非`disableTrustManager`为'true`，否则这是*REQUIRED*。

- truststorePassword

  信任库的密码。 如果设置了`truststore`并且信任库需要密码，那么这是*REQUIRED*。

- clientKeystore

  这是密钥库文件的文件路径。 当适配器向Keycloak服务器发出HTTPS请求时，此密钥库包含双向SSL的客户端证书。 这是*OPTIONAL*。

- clientKeystorePassword

  客户端密钥库和客户端密钥的密码。 如果设置了`clientKeystore`，这是*REQUIRED*。

- proxyUrl

  用于HTTP连接的HTTP代理的URL。 这是*OPTIONAL*。


#### 3.1.2. JBoss EAP/WildFly 适配器 {# JBoss_EAP_WildFly_Adapter}

为了能够保护部署在JBoss EAP或WildFly上的WAR应用程序，您必须安装和配置Keycloak SAML适配器子系统。

然后，在WAR中提供keycloak配置，`/WEB-INF/keycloak-saml.xml`文件，并在web.xml中将auth-method更改为KEYCLOAK-SAML。 本节将介绍这两种方法。

##### 适配器安装 {#Adapter_Installation}

每个适配器都是Keycloak下载站点上的单独下载。

> 我们只测试和维护适配器，并在发布时提供最新版本的WildFly。 一旦发布了新版本的WildFly，当前的适配器将被弃用，并且在下一个WildFly版本发布后将删除对它们的支持。 另一种方法是将应用程序从WildFly切换到JBoss EAP，因为JBoss EAP适配器的支持时间更长。

在WildFly 9或更高版本或JBoss EAP 7上安装：

```bash
$ cd $WILDFLY_HOME
$ unzip keycloak-saml-wildfly-adapter-dist.zip
```

在JBoss EAP 6.x上安装：

```bash
$ cd $JBOSS_HOME
$ unzip keycloak-saml-eap6-adapter-dist.zip
```

这些zip文件在WildFly或JBoss EAP发行版中创建特定于WildFly/JBoss EAP SAML适配器的新JBoss模块。

添加模块后，必须在应用服务器的服务器配置中启用Keycloak SAML子系统：`domain.xml`或`standalone.xml`。

有一个CLI脚本可以帮助您修改服务器配置。 启动服务器并从服务器的bin目录运行脚本：

WildFly 11或更新版本

```bash
$ cd $JBOSS_HOME
$ ./bin/jboss-cli.sh -c --file=bin/adapter-elytron-install-saml.cli
```

WildFly 10或更老版本

```bash
$ cd $JBOSS_HOME
$ /bin/boss-cli.sh -c --file=bin/adapter-install-saml.cli
```

> 可以在WildFly 11或更新版本上使用传统的非Elytron适配器，这意味着您甚至可以在这些版本上使用`adapter-install-saml.cli`。 但是，我们建议使用较新的Elytron适配器。

该脚本将添加扩展，子系统和可选的安全域，如下所述。

```xml
<server xmlns="urn:jboss:domain:1.4">

    <extensions>
        <extension module="org.keycloak.keycloak-saml-adapter-subsystem"/>
          ...
    </extensions>

    <profile>
        <subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1"/>
         ...
    </profile>
```

当您需要在安全Web层中创建的安全上下文传播到您正在调用的EJB（其他EE组件）时，`keycloak`安全域应该与EJB和其他组件一起使用。 否则此配置是可选的。

```xml
<server xmlns="urn:jboss:domain:1.4">
 <subsystem xmlns="urn:jboss:domain:security:1.2">
    <security-domains>
...
      <security-domain name="keycloak">
         <authentication>
           <login-module code="org.keycloak.adapters.jboss.KeycloakLoginModule"
                         flag="required"/>
          </authentication>
      </security-domain>
    </security-domains>
```

例如，如果您的WEB-INF/classes目录中有一个EJB的JAX-RS服务，则需要使用`@SecurityDomain`注释对其进行注释，如下所示：

```java
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("customers")
@Stateless
@SecurityDomain("keycloak")
public class CustomerService {

    @EJB
    CustomerDB db;

    @GET
    @Produces("application/json")
    @NoCache
    @RolesAllowed("db_user")
    public List<String> getCustomers() {
        return db.getCustomers();
    }
}
```

我们希望将来改进我们的集成，以便在您希望将keycloak安全上下文传播到EJB层时不必指定`@SecurityDomain`注释。

##### JBoss SSO {#JBoss_SSO}
WildFly内置支持部署到同一WildFly实例的Web应用程序的单点登录。 使用Keycloak时不应启用此功能。

#### 3.1.3. 从RPM安装JBoss EAP适配器 {#Installing_JBoss_EAP_Adapter_from_an_RPM}

从RPM安装EAP 7适配器：

> 在Red Hat Enterprise Linux 7中，术语channel被替换为术语库。 在这些说明中，仅使用术语库。

您必须先订阅JBoss EAP 7存储库，然后才能从RPM安装EAP 7适配器。

先决条件

1. 确保您的Red Hat Enterprise Linux系统已使用Red Hat Subscription Manager注册到您的帐户。 有关更多信息，请参阅[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须先取消订阅该存储库。

使用Red Hat Subscription Manager，使用以下命令订阅JBoss EAP 7存储库。 将<RHEL_VERSION>替换为6或7，具体取决于您的Red Hat Enterprise Linux版本。

```bash
$ sudo subscription-manager repos --enable=jb-eap-7-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令为SAML安装EAP 7适配器：

```bash
$ sudo yum install eap7-keycloak-saml-adapter-sso7_2
```

> RPM安装的默认EAP_HOME路径是`/opt/rh/eap7/root/usr/share/wildfly`。 

运行相应的模块安装脚本。

对于SAML模块，请输入以下命令：

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install-saml.cli
```

您的安装已完成。

从RPM安装EAP 6适配器：

> 在Red Hat Enterprise Linux 7中，术语channel被替换为术语库。 在这些说明中，仅使用术语库。

您必须先订阅JBoss EAP 6存储库，然后才能从RPM安装EAP 6适配器。

先决条件

1. 确保您的Red Hat Enterprise Linux系统已使用Red Hat Subscription Manager注册到您的帐户。 有关更多信息，请参阅[Red Hat订阅管理文档](https://access.redhat.com/documentation/en-us/red_hat_subscription_management/1/html-single/quick_registration_for_rhel/index)。
2. 如果您已经订阅了另一个JBoss EAP存储库，则必须先取消订阅该存储库。

使用Red Hat Subscription Manager，使用以下命令订阅JBoss EAP 6存储库。 将<RHEL_VERSION>替换为6或7，具体取决于您的Red Hat Enterprise Linux版本。

```bash
$ sudo subscription-manager repos --enable=jb-eap-6-for-rhel-<RHEL_VERSION>-server-rpms
```

使用以下命令为SAML安装EAP 6适配器：

```bash
$ sudo yum install keycloak-saml-adapter-sso7_2-eap6
```

> RPM安装的默认EAP_HOME路径是`/opt/rh/eap6/root/usr/share/wildfly`。 
运行相应的模块安装脚本。

对于SAML模块，请输入以下命令：

```bash
$ $EAP_HOME/bin/jboss-cli.sh -c --file=$EAP_HOME/bin/adapter-install-saml.cli
```

您的安装已完成。

##### 每个WAR配置 {#Per_WAR_Configuration}

本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR的`WEB-INF`目录中创建一个`keycloak-saml.xml`适配器配置文件。 该配置文件的格式在[General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config)部分中描述。

接下来，您必须在`web.xml`中将`auth-method`设置为`KEYCLOAK-SAML`。 您还必须使用标准servlet安全性来指定URL上的角色基础约束。 这是一个示例*web.xml*文件：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Admins</web-resource-name>
            <url-pattern>/admin/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>admin</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/customers/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK-SAML</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

除了`auth-method`设置之外的所有标准servlet设置。

##### 通过Keycloak SAML子系统保护WAR {#Securing_WARs_via_Keycloak_SAML_Subsystem}

您不必破解打开WAR以使用Keycloak保护它。 或者，您可以通过Keycloak SAML适配器子系统从外部保护它。 虽然您不必将KEYCLOAK-SAML指定为`auth-method`，但仍需要在`web.xml`中定义`security-constraints`。 但是，您不必创建`WEB-INF/keycloak-saml.xml`文件。 而是在服务器的`domain.xml`或`standalone.xml`子系统配置部分的XML中定义此元数据。

```xml
<extensions>
  <extension module="org.keycloak.keycloak-saml-adapter-subsystem"/>
</extensions>

<profile>
  <subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1">
    <secure-deployment name="WAR MODULE NAME.war">
      <SP entityID="APPLICATION URL">
        ...
      </SP>
    </secure-deployment>
  </subsystem>
</profile>
```

`secure-deployment``name`属性标识要保护的WAR。 它的值是`web.xml`中定义的`module-name`，附加了`.war`。 其余配置使用与[General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config)中定义的`keycloak-saml.xml`配置相同的XML语法。

配置示例：

```xml
<subsystem xmlns="urn:jboss:domain:keycloak-saml:1.1">
  <secure-deployment name="saml-post-encryption.war">
    <SP entityID="http://localhost:8080/sales-post-enc/"
        sslPolicy="EXTERNAL"
        nameIDPolicyFormat="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
        logoutPage="/logout.jsp"
        forceAuthentication="false">
      <Keys>
        <Key signing="true" encryption="true">
          <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
            <PrivateKey alias="http://localhost:8080/sales-post-enc/" password="test123"/>
            <Certificate alias="http://localhost:8080/sales-post-enc/"/>
          </KeyStore>
        </Key>
      </Keys>
      <PrincipalNameMapping policy="FROM_NAME_ID"/>
      <RoleIdentifiers>
        <Attribute name="Role"/>
      </RoleIdentifiers>
      <IDP entityID="idp">
        <SingleSignOnService signRequest="true"
            validateResponseSignature="true"
            requestBinding="POST"
            bindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"/>

        <SingleLogoutService
            validateRequestSignature="true"
            validateResponseSignature="true"
            signRequest="true"
            signResponse="true"
            requestBinding="POST"
            responseBinding="POST"
            postBindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"
            redirectBindingUrl="http://localhost:8080/auth/realms/saml-demo/protocol/saml"/>
        <Keys>
          <Key signing="true" >
            <KeyStore resource="/WEB-INF/keystore.jks" password="store123">
              <Certificate alias="saml-demo"/>
            </KeyStore>
          </Key>
        </Keys>
      </IDP>
    </SP>
   </secure-deployment>
</subsystem>
```

#### 3.1.4. Tomcat SAML 适配器 {#Tomcat_SAML_adapters}

为了能够保护部署在Tomcat 6,7和8上的WAR应用程序，您必须将Keycloak Tomcat 6,7或8 SAML适配器安装到Tomcat安装中。 然后，您必须在部署到Tomcat的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### 适配器安装 {#Adapter_Installation}

适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将适配器发行版解压缩到Tomcat的`lib/`目录中。 在WEB-INF/lib目录中包含适配器的jar将不起作用！ Keycloak SAML适配器实现为Valve，Valve代码必须驻留在Tomcat的主lib/目录中。

```bash
$ cd $TOMCAT_HOME/lib
$ unzip keycloak-saml-tomcat6-adapter-dist.zip
    or
$ unzip keycloak-saml-tomcat7-adapter-dist.zip
    or
$ unzip keycloak-saml-tomcat8-adapter-dist.zip
```

##### 每个 WAR 配置 {#Per_WAR_Configuration}

本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`META-INF/context.xml`文件。 这是一个特定于Tomcat的配置文件，您必须定义一个Keycloak特定的Valve。

```xml
<Context path="/your-context-path">
    <Valve className="org.keycloak.adapters.saml.tomcat.SamlAuthenticatorValve"/>
</Context>
```

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak-saml.xml`适配器配置文件。 该配置文件的格式在[General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) 部分中描述。

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 3.1.5. Jetty SAML 适配器 {#Jetty_SAML_Adapters}

为了能够保护部署在Jetty上的WAR应用程序，您必须将Keycloak Jetty 9.x SAML适配器安装到Jetty安装中。 然后，您必须在部署到Jetty的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

##### Jetty 9 适配器安装 {#Jetty_9_Adapter_Installation}

Keycloak为Jetty 9.x提供了一个单独的SAML适配器。 然后，您必须在部署到Jetty的每个WAR中提供一些额外的配置。 我们来看看这些步骤。

适配器不再包含在设备或war分发版中。每个适配器在Keycloak下载站点上都是单独的下载。它们也可以作为maven构件使用。

您必须将Jetty 9.x发行版解压缩到Jetty 9.x的根目录中。 在WEB-INF/lib目录中包含适配器的jar将不起作用！

```bash
$ cd $JETTY_HOME
$ unzip keycloak-saml-jetty92-adapter-dist.zip
```

接下来，您必须为jetty.base启用keycloak模块。

```bash
$ cd your-base
$ java -jar $JETTY_HOME/start.jar --add-to-startd=keycloak
```

##### Jetty 9 每个 WAR 配置 {#Jetty_9_Per_WAR_Configuration}

本节介绍如何通过在WAR包中添加配置和编辑文件来直接保护WAR。

您必须做的第一件事是在WAR包中创建一个`WEB-INF/jetty-web.xml`文件。 这是Jetty特定的配置文件，您必须在其中定义Keycloak特定的身份验证器。

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Get name="securityHandler">
        <Set name="authenticator">
            <New class="org.keycloak.adapters.saml.jetty.KeycloakSamlAuthenticator">
            </New>
        </Set>
    </Get>
</Configure>
```

接下来，您必须在WAR的`WEB-INF`目录中创建一个`keycloak-saml.xml`适配器配置文件。 该配置文件的格式在[General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config) 部分中描述。

最后，您必须同时指定`login-config`并使用标准servlet安全性来指定URL上的角色基础约束。 这是一个例子：

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Customers</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>

    <login-config>
        <auth-method>BASIC</auth-method>
        <realm-name>this is ignored currently</realm-name>
    </login-config>

    <security-role>
        <role-name>admin</role-name>
    </security-role>
    <security-role>
        <role-name>user</role-name>
    </security-role>
</web-app>
```

#### 3.1.6. Java Servlet 过滤器适配器 {#Java_Servlet_Filter_Adapter}

如果要将SAML与不具有该servlet平台适配器的Java servlet应用程序一起使用，则可以选择使用Keycloak具有的servlet过滤器适配器。 此适配器与其他适配器的工作方式略有不同。 您仍然必须指定[General Adapter Config](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml-general-config)中定义的`/WEB-INF/keycloak-saml.xml`文件部分，但是您没有在*web.xml*中定义安全性约束。 而是使用Keycloak servlet过滤器适配器定义过滤器映射，以保护要保护的URL模式。

> Backchannel注销与标准适配器的工作方式略有不同。 而不是使http会话无效，而是将会话ID标记为已注销。 根据会话ID，无法任意使http会话无效。

> 当您拥有使用SAML筛选器的群集应用程序时，Backchannel注销当前不起作用。

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
      version="3.0">

        <module-name>customer-portal</module-name>

    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.saml.servlet.SamlFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Keycloak Filter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

Keycloak过滤器具有与其他适配器相同的配置参数，除非您必须将它们定义为过滤器init参数而不是上下文参数。

如果您有各种不同的安全和不安全的URL模式，则可以定义多个过滤器映射。

> 您必须有一个覆盖`/saml`的过滤器映射。 此映射涵盖所有服务器回调。

当向IdP注册SPs时，必须将`http[s]://主机名/{context-root}/saml`注册为断言消费者服务URL和单个注销服务URL。

要使用此过滤器，请在WAR poms中包含此maven工件：

```xml
<dependency>
   <groupId>org.keycloak</groupId>
   <artifactId>keycloak-saml-servlet-filter-adapter</artifactId>
   <version>6.0.1</version>
</dependency>
```

为了使用[Multi Tenancy](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_multi_tenancy)，应将`keycloak.config.resolver`参数作为过滤器参数传递。

```xml
    <filter>
        <filter-name>Keycloak Filter</filter-name>
        <filter-class>org.keycloak.adapters.saml.servlet.SamlFilter</filter-class>
        <init-param>
            <param-name>keycloak.config.resolver</param-name>
            <param-value>example.SamlMultiTenantResolver</param-value>
        </init-param>
    </filter>
```

#### 3.1.7. 注册身份提供商 {#Registering_with_an_Identity_Provider}

对于每个基于servlet的适配器，您注册断言使用者服务URL和单个注销服务的端点必须是附加了`/saml`的servlet应用程序的基本URL，即`https://example.com/contextPath/saml`。

#### 3.1.8. 注销 {#Logout}

您可以通过多种方式从Web应用程序注销。 对于Java EE servlet容器，可以调用`HttpServletRequest.logout()`。 对于任何其他浏览器应用程序，您可以将浏览器指向具有安全约束的Web应用程序的任何URL，并传入查询参数GLO，即`http://myapp?GLO=true`。 如果您的浏览器有SSO会话，这将退出。

##### 在群集环境中注销 {#Logout_in_Clustered_Environment}
在内部，SAML适配器存储SAML会话索引，主体名称（已知）和HTTP会话ID之间的映射。 可以在群集中为可分发的应用程序在JBoss应用程序服务器系列（WildFly 10/11，EAP 6/7）中维护此映射。 作为前提条件，HTTP会话需要跨集群分布（即应用程序在应用程序的`web.xml`中标有`<distributable />`标签）。

要启用该功能，请将以下部分添加到`/WEB_INF/web.xml`文件中：

对于 EAP 7, WildFly 10/11:

```xml
<context-param>
    <param-name>keycloak.sessionIdMapperUpdater.classes</param-name>
    <param-value>org.keycloak.adapters.saml.wildfly.infinispan.InfinispanSessionCacheIdMapperUpdater</param-value>
</context-param>
```

对于 EAP 6:

```xml
<context-param>
    <param-name>keycloak.sessionIdMapperUpdater.classes</param-name>
    <param-value>org.keycloak.adapters.saml.jbossweb.infinispan.InfinispanSessionCacheIdMapperUpdater</param-value>
</context-param>
```

如果部署的会话缓存名为`*deployment-cache*`，则用于SAML映射的缓存将命名为`*deployment-cache*.ssoCache`。 缓存的名称可以被上下文参数`keycloak.sessionIdMapperUpdater.infinispan.cacheName`覆盖。 包含缓存的缓存容器将与包含部署会话缓存的缓存容器相同，但可以由上下文参数`keycloak.sessionIdMapperUpdater.infinispan.containerName`覆盖。

默认情况下，SAML映射缓存的配置将从会话缓存中派生。 可以在服务器的缓存配置部分中手动覆盖配置，就像其他缓存一样。

目前，为了提供可靠的服务，建议为SAML会话缓存使用复制缓存。 使用分布式缓存可能会导致SAML注销请求落到某个节点而无法访问SAM会话索引到HTTP会话映射的结果，从而导致注销失败。

##### 在Cross DC场景中注销 {#Logout_in_Cross_DC_Scenario}
交叉DC场景仅适用于WildFly 10及更高版本，以及EAP 7及更高版本。

处理跨多个数据中心的会话需要特殊处理。 想象一下以下场景：

1. 登录请求在数据中心1的集群内处理。
2. 管理员发出特定SAML会话的注销请求，请求登陆数据中心2。

数据中心2必须注销数据中心1中存在的所有会话（以及共享HTTP会话的所有其他数据中心）。

为了涵盖这种情况，[上面](https://www.keycloak.org/docs/latest/securing_apps/index.html#_saml_logout_in_cluster) 描述的SAML会话缓存,不仅需要在单个群集中复制，还需要在所有数据中复制 中心，例如 [通过独立的Infinispan/JDG服务器](https://access.redhat.com/documentation/en-us/red_hat_data_grid/6.6/html/administration_and_configuration_guide/chap-externalize_sessions#Externalize_HTTP_Session_from_JBoss_EAP_6.x_to_JBoss_Data_Grid)：

1. 必须将缓存添加到独立的Infinispan/JDG服务器。
2. 必须将先前项目的缓存添加为相应SAML会话缓存的远程存储。

在部署期间发现SAML会话高速缓存存在远程存储后，将监视其更改并相应地更新本地SAML会话高速缓存。

#### 3.1.9. 获取断言属性 {#Obtaining_Assertion_Attributes}

成功进行SAML登录后，您的应用程序代码可能希望获取通过SAML断言传递的属性值。 `HttpServletRequest.getUserPrincipal()`返回一个`Principal`对象，您可以将其转换为名为`org.keycloak.adapters.saml.SamlPrincipal`的Keycloak特定类。 此对象允许您查看原始断言，并具有查找属性值的便捷功能。

```java
package org.keycloak.adapters.saml;

public class SamlPrincipal implements Serializable, Principal {
    /**
     * Get full saml assertion
     *
     * @return
     */
    public AssertionType getAssertion() {
       ...
    }

    /**
     * Get SAML subject sent in assertion
     *
     * @return
     */
    public String getSamlSubject() {
        ...
    }

    /**
     * Subject nameID format
     *
     * @return
     */
    public String getNameIDFormat() {
        ...
    }

    @Override
    public String getName() {
        ...
    }

    /**
     * Convenience function that gets Attribute value by attribute name
     *
     * @param name
     * @return
     */
    public List<String> getAttributes(String name) {
        ...

    }

    /**
     * Convenience function that gets Attribute value by attribute friendly name
     *
     * @param friendlyName
     * @return
     */
    public List<String> getFriendlyAttributes(String friendlyName) {
        ...
    }

    /**
     * Convenience function that gets first  value of an attribute by attribute name
     *
     * @param name
     * @return
     */
    public String getAttribute(String name) {
        ...
    }

    /**
     * Convenience function that gets first  value of an attribute by attribute name
     *
     *
     * @param friendlyName
     * @return
     */
    public String getFriendlyAttribute(String friendlyName) {
        ...
    }

    /**
     * Get set of all assertion attribute names
     *
     * @return
     */
    public Set<String> getAttributeNames() {
        ...
    }

    /**
     * Get set of all assertion friendly attribute names
     *
     * @return
     */
    public Set<String> getFriendlyNames() {
        ...
    }
}
```

#### 3.1.10. 错误处理 {#Error_Handling}

Keycloak为基于servlet的客户端适配器提供了一些错误处理功能。 在身份验证中遇到错误时，客户端适配器将调用`HttpServletResponse.sendError()`。 您可以在`web.xml`文件中设置`error-page`来处理您想要的错误。 客户端适配器可能会抛出400,401,403和500错误。

```xml
<error-page>
    <error-code>403</error-code>
    <location>/ErrorHandler</location>
</error-page>
```

客户端适配器还设置了可以检索的`HttpServletRequest`属性。 属性名称是`org.keycloak.adapters.spi.AuthenticationError`。 Typecast这个对象：`org.keycloak.adapters.saml.SamlAuthenticationError`。 这个类可以告诉你到底发生了什么。 如果未设置此属性，则适配器不对错误代码负责。

```java
public class SamlAuthenticationError implements AuthenticationError {
    public static enum Reason {
        EXTRACTION_FAILURE,
        INVALID_SIGNATURE,
        ERROR_STATUS
    }

    public Reason getReason() {
        return reason;
    }
    public StatusResponseType getStatus() {
        return status;
    }
}
```

#### 3.1.11. 故障排除 {#Troubleshooting}

解决问题的最佳方法是在客户端适配器和Keycloak Server中打开SAML的调试。 使用您的日志记录框架，将日志级别设置为`org.keycloak.saml`package的`DEBUG`。 启用此选项可以查看发送到服务器和从服务器发送的SAML请求和响应文档。

#### 3.1.12. 多租户 {#Multi_Tenancy}

SAML为[多租户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_multi_tenancy)提供与OIDC相同的功能，这意味着可以使用多个安全保护单个目标应用程序（WAR） Keycloak领域。 领域可以位于同一个Keycloak实例上，也可以位于不同的实例上。

为此，应用程序必须具有多个`keycloak-saml.xml`适配器配置文件。

虽然您可以将WAR的多个实例与不同的适配器配置文件部署到不同的上下文路径，但这可能不方便，您可能还希望根据context-path之外的其他内容选择域。

Keycloak可以使用自定义配置解析器，因此您可以选择为每个请求使用哪个适配器配置。 在SAML中，配置仅在登录处理中很有意义; 一旦用户登录，会话就会被验证，并且返回的`keycloak-saml.xml`是不同的并不重要。 因此，为同一会话返回相同的配置是正确的方法。

为此，请创建`org.keycloak.adapters.saml.SamlConfigResolver`的实现。 下面的示例使用`Host`标头来定位正确的配置并加载它以及应用程序的Java类路径中的相关元素：

```java
package example;

import java.io.InputStream;
import org.keycloak.adapters.saml.SamlConfigResolver;
import org.keycloak.adapters.saml.SamlDeployment;
import org.keycloak.adapters.saml.config.parsers.DeploymentBuilder;
import org.keycloak.adapters.saml.config.parsers.ResourceLoader;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.saml.common.exceptions.ParsingException;

public class SamlMultiTenantResolver implements SamlConfigResolver {

    @Override
    public SamlDeployment resolve(HttpFacade.Request request) {
        String host = request.getHeader("Host");
        String realm = null;
        if (host.contains("tenant1")) {
            realm = "tenant1";
        } else if (host.contains("tenant2")) {
            realm = "tenant2";
        } else {
            throw new IllegalStateException("Not able to guess the keycloak-saml.xml to load");
        }

        InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak-saml.xml");
        if (is == null) {
            throw new IllegalStateException("Not able to find the file /" + realm + "-keycloak-saml.xml");
        }

        ResourceLoader loader = new ResourceLoader() {
            @Override
            public InputStream getResourceAsStream(String path) {
                return getClass().getResourceAsStream(path);
            }
        };

        try {
            return new DeploymentBuilder().build(is, loader);
        } catch (ParsingException e) {
            throw new IllegalStateException("Cannot load SAML deployment", e);
        }
    }
}
```

您还必须在`web.xml`中配置与`keycloak.config.resolver`context-param一起使用的`SamlConfigResolver`实现：

```xml
<web-app>
    ...
    <context-param>
        <param-name>keycloak.config.resolver</param-name>
        <param-value>example.SamlMultiTenantResolver</param-value>
    </context-param>
</web-app>
```

#### 3.1.13. 从旧版本迁移 {#Migration_from_older_versions}

##### 迁移到1.9.0 {#Migrating_to_1_9_0}
###### SAML SP 客户端 适配器更改 {#SAML_SP_Client_Adapter_Changes}
Keycloak SAML SP客户端适配器现在需要一个特定的端点，`/saml`将在您的IdP中注册。 除了它具有的任何其他绑定之外，SamlFilter还必须绑定到/saml。 必须这样做，因为SAML POST绑定会占用请求输入流，这对依赖它的客户来说真的很糟糕。

### 3.2. mod_auth_mellon Apache HTTPD 模块 {#mod_auth_mellon_Apache_HTTPD_module}

[mod_auth_mellon](https://github.com/UNINETT/mod_auth_mellon) 模块是SAML的Apache HTTPD插件。 如果您的语言/环境支持使用Apache HTTPD作为代理，那么您可以使用mod_auth_mellon通过SAML保护您的Web应用程序。 有关此模块的更多详细信息，请参阅*mod_auth_mellon* GitHub 仓库。

要配置mod_auth_mellon，您需要：

- 身份提供者（IdP）实体描述符XML文件，描述与Keycloak或其他SAML IdP的连接
- SP实体描述符XML文件，描述您正在保护的应用程序的SAML连接和配置。
- 私钥PEM文件，它是PEM格式的文本文件，用于定义应用程序用于签署文档的私钥。
- 证书PEM文件，它是一个文本文件，用于定义应用程序的证书。
- mod_auth_mellon 特定的Apache HTTPD模块配置。

如果您已经在Keycloak应用程序服务器的领域内定义并注册了客户端应用程序，则Keycloak可以生成除Apache HTTPD模块配置之外所需的所有文件。

要生成Apache HTTPD模块配置，请完成以下步骤：

1. 转到SAML客户端的“安装”页面，然后选择“Mod Auth Mellon files”选项。

   mod_auth_mellon 配置下载

   ![mod auth mellon config download](assets/mod-auth-mellon-config-download.png)

2. 单击**Download**以下载包含所需XML描述符和PEM文件的zip文件。

#### 3.2.1. 使用Keycloak配置mod_auth_mellon {#Use_Keycloak_Configuring_mod_auth_mellon}
涉及两个主机：

- 运行Keycloak的主机，将被称为$idp_host，因为Keycloak是SAML身份提供程序（IdP）。
- 运行Web应用程序的主机，将称为$sp_host。 在SAML中，使用IdP的应用程序称为服务提供者（SP）。

需要在具有root权限的$sp_host上执行以下所有步骤。

##### 安装包 {#Installing_the_Packages}
要安装必要的软件包，您需要：

- Apache Web服务器 (httpd)
- 用于Apache的Mellon SAML SP附加模块
- 用于创建X509证书的工具

要安装必要的软件包，请运行以下命令：

```bash
yum install httpd mod_auth_mellon mod_ssl openssl
```

##### 为Apache SAML创建配置目录 {#Creating_a_Configuration_Directory_for_Apache_SAML}
建议在一个位置保留与Apache使用SAML相关的配置文件。

在Apache配置root `/etc/httpd`下创建一个名为saml2的新目录：

```bash
mkdir /etc/httpd/saml2
```

##### 配置Mellon服务提供商 {#Configuring_the_Mellon_Service_Provider}
Apache附加模块的配置文件位于`/etc/httpd/conf.d`目录中，文件扩展名为.conf。 您需要创建`/etc/httpd/conf.d/mellon.conf`文件并将Mellon的配置指令放入其中。

`Mellon’s`配置指令大致可以分为两类信息：

- 使用SAML身份验证保护哪些URL
- 引用受保护的URL时将使用哪些SAML参数。

Apache配置指令通常遵循URL空间中的分层树结构，称为位置。 您需要为Mellon指定一个或多个URL位置以进行保护。 您可以灵活地添加适用于每个位置的配置参数。 您可以将所有必需参数添加到位置块，也可以将Mellon参数添加到特定受保护位置继承的URL位置层次结构中的公共位置（或两者的某种组合）。 由于无论哪个位置触发SAML操作，SP都以相同的方式操作，这里使用的示例配置将常用的Mellon配置指令放在层次结构的根中，然后可以使用Mellon保护的特定位置定义 最小指令。 此策略避免为每个受保护位置复制相同的参数。

此示例只有一个受保护的位置:https://$sp_host/protected。

要配置Mellon服务提供商，请完成以下步骤：

1. 使用以下内容创建文件`/etc/httpd/conf.d/mellon.conf`：

```xml
 <Location / >
    MellonEnable info
    MellonEndpointPath /mellon/
    MellonSPMetadataFile /etc/httpd/saml2/mellon_metadata.xml
    MellonSPPrivateKeyFile /etc/httpd/saml2/mellon.key
    MellonSPCertFile /etc/httpd/saml2/mellon.crt
    MellonIdPMetadataFile /etc/httpd/saml2/idp_metadata.xml
 </Location>
 <Location /private >
    AuthType Mellon
    MellonEnable auth
    Require valid-user
 </Location>
```

> 上面代码中引用的某些文件是在后面的步骤中创建的。

##### 创建服务提供者元数据 {#Creating_the_Service_Provider_Metadata}
在SAML中，IdP和SP交换SAML元数据，这是XML格式的。 元数据的模式是标准，因此确保参与的SAML实体可以消耗彼此的元数据。 你需要：

- SP使用的IdP的元数据
- 描述提供给IdP的SP的元数据

SAML元数据的一个组件是X509证书。 这些证书用于两个目的：

- 签署SAML消息，以便接收端可以证明消息来自预期的一方。
- 在传输过程中加密消息（很少使用，因为SAML消息通常发生在受TLS保护的传输上）

如果您已拥有证书颁发机构（CA），则可以使用自己的证书，也可以生成自签名证书。 为简单起见，在此示例中使用自签名证书。

因为Mellon的SP元数据必须反映已安装的mod_auth_mellon版本的功能，所以必须是有效的SP元数据XML，并且必须包含X509证书（除非您熟悉X509证书生成，否则其创建可能是钝的）是生成 SP元数据将使用mod_auth_mellon包中包含的工具（mellon_create_metadata.sh）。 生成的元数据总是可以在以后编辑，因为它是一个文本文件。 该工具还会创建您的X509密钥和证书。

SAML IdP和SP使用称为EntityID的唯一名称来标识自己。 要使用Mellon元数据创建工具，您需要：

- EntityID，通常是SP的URL，通常是可以检索SP元数据的SP的URL
- 将使用SP的SAML消息的URL，Mellon将其称为MellonEndPointPath。

要创建SP元数据，请完成以下步骤：

1. 创建一些辅助shell变量：

   ```properties
   fqdn=`hostname`
   mellon_endpoint_url="https://${fqdn}/mellon"
   mellon_entity_id="${mellon_endpoint_url}/metadata"
   file_prefix="$(echo "$mellon_entity_id" | sed 's/[^A-Za-z.]/_/g' | sed 's/__*/_/g')"
   ```

2. 通过运行以下命令调用Mellon元数据创建工具：

   ```bash
   /usr/libexec/mod_auth_mellon/mellon_create_metadata.sh $mellon_entity_id $mellon_endpoint_url
   ```

3. 将生成的文件移动到目标位置（在上面创建的/etc/httpd/conf.d/mellon.conf文件中引用）：

   ```bash
   mv ${file_prefix}.cert /etc/httpd/saml2/mellon.crt
   mv ${file_prefix}.key /etc/httpd/saml2/mellon.key
   mv ${file_prefix}.xml /etc/httpd/saml2/mellon_metadata.xml
   ```

##### 将Mellon服务提供商添加到Keycloak身份提供商 {#Adding_the_Mellon_Service_Provider_to_the_Keycloak_Identity_Provider}
假设：Keycloak IdP已经安装在$idp_host上。

Keycloak支持多租户，其中所有用户，客户等都被分组在所谓的领域中。 每个领域都独立于其他领域。 您可以在Keycloak中使用现有领域，但此示例显示如何创建名为test_realm的新领域并使用该领域。

所有这些操作都是使用Keycloak管理Web控制台执行的。 您必须拥有$idp_host的管理员用户名和密码。

要完成以下步骤：

1. 打开管理控制台，然后输入管理员用户名和密码登录。

   登录管理控制台后，将存在现有领域。 首次设置Keycloak时，默认情况下会创建一个根域master。 任何以前创建的域都在管理控制台的左上角列在下拉列表中。

2. 从领域下拉列表中选择**Add realm**。

3. 在Name字段中输入`test_realm`并单击**Create**。

###### 将Mellon服务提供程序添加为领域的客户端 {#Adding_the_Mellon_Service_Provider_as_a_Client_of_the_Realm}
在Keycloak中，SAML SP称为客户端。 要添加SP，我们必须位于领域的“客户端”部分。

1. 单击左侧的“客户端”菜单项，然后单击右上角的“**Create**”以创建新客户端。

###### 添加Mellon SP客户端 {#Adding_the_Mellon_SP_Client}
要添加Mellon SP客户端，请完成以下步骤：

1. 将客户端协议设置为SAML。 从客户端协议下拉列表中，选择**saml**。
2. 提供上面创建的Mellon SP元数据文件(/etc/httpd/saml2/mellon_metadata.xml)。 根据浏览器的运行位置，您可能必须将SP元数据从$sp_host复制到运行浏览器的计算机，以便浏览器可以找到该文件。
3. 点击 **Save**.

###### 编辑Mellon SP客户端 {#Editing_the_Mellon_SP_Client}
我们建议设置几个客户端配置参数：

- 确保"Force POST Binding"处于打开状态。
- 将paosResponse添加到Valid Redirect URIs列表中：
  1. 复制"Valid Redirect URIs"中的postResponse URL并将其粘贴到"+"下方的空添加文本字段中。
  2. 将“postResponse”更改为“paosResponse”。 （SAML ECP需要paosResponse URL。）
  3. 点击底部的**Save**。

许多SAML SP根据用户在组中的成员身份确定授权。 Keycloak IdP可以管理用户组信息，但它不提供用户的组，除非IdP配置为将其作为SAML属性提供。

要配置IdP以将用户组作为SAML属性提供，请完成以下步骤：

1. 单击客户端的Mappers选项卡。
2. 在Mappers页面的右上角，单击**Create**。
3. 从Mapper Type下拉列表中选择**Group list**。
4. 将名称设置为“group list”。
5. 将SAML属性名称设置为“groups”。
6. 点击 **Save**.

其余步骤在$sp_host上执行。

###### 检索身份提供程序元数据 {#Retrieving_the_Identity_Provider_Metadata}
现在您已经在IdP上创建了领域，您需要检索与其关联的IdP元数据，以便Mellon SP识别它。 在先前创建的`/etc/httpd/conf.d/mellon.conf`文件中，MellonIdPMetadataFile指定为`/etc/httpd/saml2/idp_metadata.xml`，但直到现在该文件在$ sp_host上不存在。 要获取该文件，我们将从IdP中检索它。

1. 通过用$idp_host替换正确的值来从IdP中检索文件：

   ```bash
   curl -k -o /etc/httpd/saml2/idp_metadata.xml \
   https://$idp_host/auth/realms/test_realm/protocol/saml/descriptor
   ```

   Mellon 现已完全配置。

2. 要运行Apache配置文件的语法检查：

   ```bash
   apachectl configtest
   ```

   > Configtest等同于apachectl的-t参数。 如果配置测试显示任何错误，请在继续之前更正它们。

3. 重启Apache服务器：

   ```bash
   systemctl restart httpd.service
   ```

您现在已将testcalm中的Keycloak设置为SAML IdP，将mod_auth_mellon设置为SAML SP，通过对`$idp_host`IdP进行身份验证来保护URL $sp_host/protected（及其下的所有内容）。

## 4. Docker注册表配置 {#Docker_Registry_Configuration}

> 默认情况下禁用Docker身份验证。 要启用，请参阅[Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles)。

本节介绍如何配置Docker注册表以将Keycloak用作其身份验证服务器。

有关如何设置和配置Docker注册表的更多信息，请参阅[Docker注册表配置指南](https://docs.docker.com/registry/configuration/)。

### 4.1. Docker注册表配置文件安装 {#Docker_Registry_Configuration_File_Installation}
对于具有更高级Docker注册表配置的用户，通常建议您提供自己的注册表配置文件。 Keycloak Docker提供程序通过*Registry Config File* 格式化选项支持此机制。 选择此选项将生成类似于以下内容的输出：

```
auth:
  token:
    realm: http://localhost:8080/auth/realms/master/protocol/docker-v2/auth
    service: docker-test
    issuer: http://localhost:8080/auth/realms/master
```

然后可以将此输出复制到任何现有的注册表配置文件中。 有关如何设置文件的更多信息，请参阅[注册表配置文件规范](https://docs.docker.com/registry/configuration/)，或者以[基本示例](https://github.com/docker/distribution/blob/master/cmd/registry/config-example.yml)开头。

> 不要忘记使用Keycloak领域的pulic证书的位置配置`rootcertbundle`字段。 没有此参数，auth配置将不起作用。

### 4.2. Docker注册表环境变量覆盖安装 {#Docker_Registry_Environment_Variable_Override_Installation}
通常，对开发或POC Docker注册表使用简单的环境变量覆盖是合适的。 虽然这种方法通常不建议用于生产用途，但是当需要快速而肮脏的方式来建立注册表时，它可能会有所帮助。 只需使用客户端安装选项卡中的*Variable Override* 格式选项，输出应如下所示：

```
REGISTRY_AUTH_TOKEN_REALM: http://localhost:8080/auth/realms/master/protocol/docker-v2/auth
REGISTRY_AUTH_TOKEN_SERVICE: docker-test
REGISTRY_AUTH_TOKEN_ISSUER: http://localhost:8080/auth/realms/master
```

> 不要忘记使用Keycloak领域的pulic证书的位置配置`REGISTRY_AUTH_TOKEN_ROOTCERTBUNDLE`覆盖。 没有此参数，auth配置将不起作用。

### 4.3. Docker撰写YAML文件 {#Docker_Compose_YAML_File}
> 此安装方法旨在让docker注册表针对Keycloak服务器进行身份验证。 它仅用于开发目的，不应在生产或类似生产的环境中使用。

zip文件安装机制为想要了解Keycloak服务器如何与Docker注册表进行交互的开发人员提供了快速入门。 为了配置：

1. 从所需的领域，创建客户端配置。 此时您将没有Docker注册表 - 快速入门将负责该部分。
2. 从安装选项卡中选择“Docker Compose YAML”选项并下载.zip文件
3. 将存档解压缩到所需位置，然后打开目录。
4. 使用`docker-compose up`启动Docker注册表

> 建议您在`master`以外的域中配置Docker注册表客户端，因为HTTP Basic身份验证流程不会显示表单。

完成上述配置，并且keycloak服务器和Docker注册表正在运行时，docker身份验证应该成功：

```bash
[user ~]#??docker login localhost:5000 -u $username
Password: *******
Login Succeeded
```

## 5. 客户端注册 {#Client_Registration}

为了使应用程序或服务能够使用Keycloak，它必须在Keycloak中注册客户端。 管理员可以通过管理控制台（或管理员REST端点）执行此操作，但客户端也可以通过Keycloak客户端注册服务注册自己。

客户注册服务为Keycloak客户端表示，OpenID Connect客户端元数据和SAML实体描述符提供内置支持。 客户端注册服务端点是`/auth/realms/<realm>/clients-registrations/<provider>`。

内置支持的`providers`是：

- default - Keycloak客户端表示 (JSON)
- install - Keycloak适配器配置 (JSON)
- openid-connect - OpenID Connect客户端元数据描述 (JSON)
- saml2-entity-descriptor - SAML实体描述符 (XML)

以下部分将介绍如何使用不同的提供程序。

### 5.1. 认证 {#Authentication}
要调用客户端注册服务，通常需要令牌。 令牌可以是承载令牌，初始访问令牌或注册访问令牌。 有一种替代方案可以在没有任何令牌的情况下注册新客户端，但是您需要配置客户端注册策略（见下文）。

#### 5.1.1. 承载令牌 {#Bearer_Token}
可以代表用户或服务帐户发出承载令牌。 调用端点需要以下权限（有关详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/) for more details)）：

- create-client或manage-client  - 创建客户端
- view-client或manage-client  - 查看客户端
- manage-client  - 更新或删除客户端

如果您使用不记名令牌来创建客户端，建议使用仅具有“create-client”角色的服务帐户中的令牌（请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)了解更多详情）。

#### 5.1.2. 初始访问令牌 {#Initial_Access_Token}
注册新客户端的推荐方法是使用初始访问令牌。 初始访问令牌只能用于创建客户端，并且具有可配置的到期时间以及可以创建的客户端数量的可配置限制。

可以通过管理控制台创建初始访问令牌。 要创建新的初始访问令牌，首先在管理控制台中选择域，然后单击左侧菜单中的`Realm Settings`，然后单击页面中显示的选项卡中的`Client Registration`。 然后最后点击`Initial Access Tokens`子标签。

您现在可以看到任何现有的初始访问令牌。 如果您有权访问权限，则可以删除不再需要的令牌。 您只能在创建令牌时检索令牌的值。 要创建新标记，请单击`Create`。 您现在可以选择添加令牌有效的时间，也可以使用令牌创建多少客户端。 单击`Save`后，将显示标记值。

现在复制/粘贴此令牌非常重要，因为以后您将无法检索它。 如果您忘记复制/粘贴它，则删除令牌并创建另一个令牌。

通过将客户端注册服务添加到请求中的Authorization标头，令牌值在调用客户端注册服务时用作标准承载令牌。 例如：

```
Authorization: bearer eyJhbGciOiJSUz...
```

#### 5.1.3. 注册访问令牌 {#Registration_Access_Token}
通过客户端注册服务创建客户端时，响应将包括注册访问令牌。 注册访问令牌提供访问权限以便稍后检索客户端配置，还可以更新或删除客户端。 注册访问令牌以与承载令牌或初始访问令牌相同的方式包含在请求中。 注册访问令牌仅有效一次，当使用时，响应将包含新令牌。

如果客户端是在客户端注册服务之外创建的，则它将没有与之关联的注册访问令牌。 您可以通过管理控制台创建一个。 如果您丢失特定客户端的令牌，这也很有用。 要创建新令牌，请在管理控制台中找到客户端，然后单击`Credentials`。 然后单击`Generate registration access token`。

### 5.2. Keycloak表示 {#Keycloak_Representations}
`default`客户端注册提供程序可用于创建，检索，更新和删除客户端。 它使用Keycloak客户端表示格式，该格式为通过管理控制台配置客户端提供了完全配置支持，包括配置协议映射器。

要创建客户端，请创建客户端表示（JSON），然后对`/auth/realms/<realm>/clients-registrations/default`执行HTTP POST请求。

它将返回一个客户端表示，其中还包括注册访问令牌。 如果要稍后检索配置，更新或删除客户端，则应将注册访问令牌保存在某处。

要检索客户端表示，请对`/auth/realms/<realm>/clients-registrations/default/<client id>`执行HTTP GET请求。

它还将返回一个新的注册访问令牌。

要更新客户端表示，请使用更新的客户端表示执行HTTP PUT请求：`/auth/realms/<realm>/clients-registrations/default/<client id>`。

它还将返回一个新的注册访问令牌。

要删除客户端表示，请执行HTTP DELETE请求：`/auth/realms/<realm>/clients-registrations/default/<client id>`

### 5.3. Keycloak适配器配置 {#Keycloak_Adapter_Configuration}
`installation`客户端注册提供程序可用于检索客户端的适配器配置。 除了令牌身份验证，您还可以使用HTTP基本身份验证对客户端凭据进行身份验证。 为此，请在请求中包含以下标头：

```
Authorization: basic BASE64(client-id + ':' + client-secret)
```

要检索适配器配置，然后对`/auth/realms/<realm>/clients-registrations/install/<client id>`执行HTTP GET请求。

公共客户端无需身份验证。 这意味着对于JavaScript适配器，您可以使用上述URL直接从Keycloak加载客户端配置。

### 5.4. OpenID连接动态客户端注册 {#OpenID_Connect_Dynamic_Client_Registration}
Keycloak实现了[OpenID Connect动态客户端注册](https://openid.net/specs/openid-connect-registration-1_0.html)，它扩展了[OAuth 2.0动态客户端注册协议](https://tools.ietf.org/html/rfc7591)和[OAuth 2.0动态客户端注册管理协议](https://tools.ietf.org/html/rfc7592)。

使用这些规范在Keycloak中注册客户端的端点是`/auth/realms/<realm>/clients-registrations/openid-connect[/<client id>]`。

此端点也可以在域的OpenID Connect Discovery端点中找到，`/auth/realms/<realm>/.well-known/openid-configuration`。

### 5.5. SAML实体描述符 {#SAML_Entity_Descriptors}
SAML实体描述符端点仅支持使用SAML v2实体描述符来创建客户端。 它不支持检索，更新或删除客户端。 对于这些操作，应使用Keycloak表示端点。 创建客户端时，将返回Keycloak Client Representation，其中包含有关已创建客户端的详细信息，包括注册访问令牌。

要创建客户端，请使用SAML实体描述符对`/auth/realms/<realm>/clients-registrations/saml2-entity-descriptor`执行HTTP POST请求。

### 5.6. 使用CURL的示例 {#Example_using_CURL}
以下示例使用CURL使用clientId`myclient`创建客户端。 您需要使用正确的初始访问令牌或承载令牌替换`eyJhbGciOiJSUz ...`。

```bash
curl -X POST \
    -d '{ "clientId": "myclient" }' \
    -H "Content-Type:application/json" \
    -H "Authorization: bearer eyJhbGciOiJSUz..." \
    http://localhost:8080/auth/realms/master/clients-registrations/default
```

### 5.7. 使用Java客户端注册API的示例 {#Example_using_Java_Client_Registration_API}
客户端注册Java API可以使用Java轻松使用客户端注册服务。 要使用包含来自Maven的依赖项`org.keycloak:keycloak-client-registration-api:>VERSION<`。

有关使用客户端注册的完整说明，请参阅JavaDocs。 以下是创建客户端的示例。 您需要使用正确的初始访问令牌或承载令牌替换`eyJhbGciOiJSUz ...`。

```java
String token = "eyJhbGciOiJSUz...";

ClientRepresentation client = new ClientRepresentation();
client.setClientId(CLIENT_ID);

ClientRegistration reg = ClientRegistration.create()
    .url("http://localhost:8080/auth", "myrealm")
    .build();

reg.auth(Auth.token(token));

client = reg.create(client);

String registrationAccessToken = client.getRegistrationAccessToken();
```

### 5.8. 客户端注册政策 {#Client_Registration_Policies}
Keycloak目前支持2种方式如何通过客户注册服务注册新客户。

- 经过身份验证的请求 - 注册新客户端的请求必须包含上面提到的`Initial Access Token(初始访问令牌)` 或 `Bearer Token(承载令牌)`。
- 匿名请求 - 注册新客户端的请求根本不需要包含任何令牌

匿名客户端注册请求是非常有趣和强大的功能，但是您通常不希望任何人能够无任何限制地注册新客户端。 因此，我们有“客户注册策略SPI”，它提供了一种限制谁可以注册新客户以及在何种条件下的方法。

在Keycloak管理控制台中，您可以单击`Client Registration(客户端注册)`选项卡，然后单击“客户端注册策略”子选项卡。 在这里，您将看到默认情况下为匿名请求配置了哪些策略，以及为经过身份验证的请求配置了哪些策略。

> 匿名请求（没有任何令牌的请求）仅允许创建（注册）新客户端。 因此，当您通过匿名请求注册新客户端时，响应将包含注册访问令牌，该令牌必须用于特定客户端的读取，更新或删除请求。 但是，使用匿名注册的注册访问令牌也将受到匿名策略的约束！ 这意味着，例如，如果您具有`Trusted Hosts(可信主机)`策略，则更新客户端的请求也需要来自可信主机。 另外，例如，在更新客户端以及存在`Consent Required(同意所需)`策略时，将不允许禁用`Consent Required(同意所需)`等。

目前我们有这些策略实施：

- Trusted Hosts Policy(可信主机策略) - 您可以配置可信主机和可信域的列表。 可以从这些主机或域发送对客户注册服务的请求。 某些不受信任的IP发送的请求将被拒绝。 新注册客户端的URL也必须仅使用那些可信主机或域。 例如，不允许将客户端的“重定向URI”设置为指向某个不受信任的主机。 默认情况下，没有任何列入白名单的主机，因此事实上禁用匿名客户端注册。
- Consent Required Policy(需要同意策略) - 新注册的客户端将启用`Consent Allowed(允许同意)`切换。 因此，在成功进行身份验证后，用户在需要批准权限（客户端范围）时将始终看到同意屏幕。 这意味着除非用户批准，否则客户端将无法访问任何个人信息或用户的许可。
- Protocol Mappers Policy(协议映射器策略) - 允许配置列入白名单的协议映射器实现。 如果新客户端包含一些非白名单的协议映射器，则无法注册或更新新客户端。 请注意，此策略也用于经过身份验证的请求，因此即使对于经过身份验证的请求，也存在可以使用协议映射器的一些限制。
- Client Scope Policy(客户端范围策略) - 允许将`Client Scopes(客户端范围)`列入白名单，可以与新注册或更新的客户端一起使用。 默认情况下，没有列入白名单的范围; 只有客户端作用域（定义为`Realm Default Client Scopes`）默认列入白名单。
- Full Scope Policy(全范围策略) - 新注册的客户端将禁用`Full Scope Allowed(允许全范围)`开关。 这意味着他们将不具有任何作用域领域角色或其他客户端的客户角色。
- Max Clients Policy(最大客户端策略) - 如果域中的当前客户端数量大于指定限制，则拒绝注册。 匿名注册默认为200。
- Client Disabled Policy(客户端已禁用策略) - 将禁用新注册的客户端。 这意味着管理员需要手动批准并启用所有新注册的客户端。 即使是匿名注册，也不会默认使用此策略。

## 6. 客户端注册 CLI {#Client_Registration_CLI}

客户端注册CLI是一个命令行界面（CLI）工具，供应用程序开发人员在与Keycloak集成时以自助服务方式配置新客户端。 它专门设计用于与Keycloak客户端注册REST端点交互。

必须为任何能够使用Keycloak的应用程序创建或获取客户端配置。 您通常为托管在唯一主机名上的每个新应用程序配置新客户端。 当应用程序与Keycloak交互时，应用程序使用客户端ID识别自身，因此Keycloak可以提供登录页面，单点登录（SSO）会话管理和其他服务。

您可以使用客户端注册CLI从命令行配置应用程序客户端，并且可以在shell脚本中使用它。

为了允许特定用户使用`客户端注册CLI`，Keycloak管理员通常使用管理控制台配置具有适当角色的新用户，或者配置新客户端和客户端密钥以授予对客户端注册REST API的访问权限。

### 6.1. 配置新常规用户以使用客户端注册CLI {#Configuring_a_new_regular_user_for_use_with_Client_Registration_CLI}
1. 以`admin`身份登录管理控制台（例如<http://localhost:8080/auth/admin>）。

2. 选择要管理的领域。

3. 如果要使用现有用户，请选择要编辑的用户; 否则，创建一个新用户。

4. 选择**Role Mappings > Client Roles > realm-management**。 如果您在主域中，请选择**NAME-realm**，其中`NAME`是目标域的名称。 您可以向主域中的用户授予对任何其他域的访问权限。

5. 选择**Available Roles>manage-client**以授予一整套客户端管理权限。 另一个选择是选择**view-clients**为只读或**create-client**来创建新客户。

   > 这些权限授予用户执行操作的能力，而无需使用[初始访问令牌](https://www.keycloak.org/docs/latest/securing_apps/index.html#_initial_access_token)或[注册访问令牌](https://www.keycloak.org/docs/latest/securing_apps/index.html#_registration_access_token)。

可以不向用户分配任何`realm-management`角色。 在这种情况下，用户仍然可以使用客户端注册CLI登录，但如果没有初始访问令牌，则无法使用它。 尝试在没有令牌的情况下执行任何操作会导致**403 Forbidden**错误。

管理员可以通过**Realm Settings > Client Registration > Initial Access Token**菜单从管理控制台发出初始访问令牌。

### 6.2. 配置客户端以与客户端注册CLI一起使用 {#Configuring_a_client_for_use_with_the_Client_Registration_CLI}
默认情况下，服务器将客户端注册CLI识别为`admin-cli`客户端，该客户端为每个新域自动配置。 使用用户名登录时无需其他客户端配置。

1. 如果要为客户端注册CLI使用单独的客户端配置，请创建新客户端（例如，`reg-cli`）。
2. 切换**Standard Flow Enabled**将其设置为**Off**。
3. 通过将客户端`Access Type`配置为`Confidential`并选择**Credentials > ClientId and Secret**来加强安全性。

您可以在**Credentials**选项卡下配置`Client Id and Secret`或`Signed JWT`。

1. 如果要使用与客户端关联的服务帐户，请通过在`Admin Console`的**Clients**部分中选择要编辑的客户端来启用服务帐户。
   1. 在**Settings**下，将**Access Type**更改为**Confidential**，将**Service Accounts Enabled**设置切换为**On**，然后单击**Save**。
   2. 单击**Service Account Roles**并选择所需角色以配置服务帐户的访问权限。 有关要选择的角色的详细信息，请参阅[配置用于客户端注册CLI的新常规用户](https://www.keycloak.org/docs/latest/securing_apps/index.html#_configuring_a_user_for_client_registration_cli)。
2. 切换**Direct Access Grants Enabled**将其设置为**On**，如果您要使用常规用户帐户而不是服务帐户。
3. 如果客户端配置为`Confidential`，则在使用`--secret`选项运行`kcreg config credentials`时提供已配置的机密。
4. 在运行`kcreg config credentials`时指定要使用的`clientId`（例如，`--client reg-cli`）。
5. 启用服务帐户后，您可以在运行`kcreg config credentials`时省略指定用户，并仅提供客户端密钥或密钥库信息。

### 6.3. 安装客户端注册CLI {#Installing_the_Client_Registration_CLI}
客户端注册CLI打包在Keycloak Server发行版中。 您可以在`bin`目录中找到执行脚本。 Linux脚本称为`kcreg.sh`，Windows脚本称为`kcreg.bat`。

在设置客户端以便从文件系统上的任何位置使用时，将Keycloak服务器目录添加到`PATH`。

例如，在：

- Linux:

```bash
$ export PATH=$PATH:$KEYCLOAK_HOME/bin
$ kcreg.sh
```

- Windows:

```bash
c:\> set PATH=%PATH%;%KEYCLOAK_HOME%\bin
c:\> kcreg
```

`KEYCLOAK_HOME`指的是解压缩Keycloak Server分发的目录。

### 6.4. 使用客户端注册CLI {#Using_the_Client_Registration_CLI}
1. 通过使用您的凭据登录来启动经过身份验证的会话。

2. 在`Client Registration REST`端点上运行命令。

   例如，在：

   - Linux:

     ```bash
     $ kcreg.sh config credentials --server http://localhost:8080/auth --realm demo --user user --client reg-cli
     $ kcreg.sh create -s clientId=my_client -s 'redirectUris=["http://localhost:8980/myapp/*"]'
     $ kcreg.sh get my_client
     ```

   - Windows:

     ```bash
     c:\> kcreg config credentials --server http://localhost:8080/auth --realm demo --user user --client reg-cli
     c:\> kcreg create -s clientId=my_client -s "redirectUris=[\"http://localhost:8980/myapp/*\"]"
     c:\> kcreg get my_client
     ```

     > 在生产环境中，必须使用`https:`访问Keycloak，以避免将令牌暴露给网络嗅探器。

3. 如果服务器的证书不是由Java的默认证书信任库中包含的受信任证书颁发机构（CA）之一颁发的，请准备`truststore.jks`文件并指示客户端注册CLI使用它。

   例如，在：

   - Linux:

     ```bash
     $ kcreg.sh config truststore --trustpass $PASSWORD ~/.keycloak/truststore.jks
     ```

   - Windows:

     ```bash
     c:\> kcreg config truststore --trustpass %PASSWORD% %HOMEPATH%\.keycloak\truststore.jks
     ```

#### 6.4.1. 登录 {#Logging_in}
1. 使用客户端注册CLI登录时，请指定服务器端点URL和域。
2. 指定用户名或客户端ID，这将导致使用特殊服务帐户。 使用用户名时，必须使用指定用户的密码。 使用客户端ID时，使用客户端密钥或`Signed JWT`而不是密码。

无论登录方法如何，登录的帐户都需要适当的权限才能执行客户端注册操作。 请记住，非主域中的任何帐户只能拥有管理同一域内的客户端的权限。 如果需要管理不同的域，可以在不同的域中配置多个用户，也可以在`master`域中创建单个用户，并添加角色以管理不同领域的客户端。

您无法使用客户端注册CLI配置用户。 使用管理控制台Web界面或Admin Client CLI配置用户。 有关详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。

当`kcreg`成功登录时，它会接收授权令牌并将其保存在私有配置文件中，因此令牌可用于后续调用。 有关配置文件的详细信息，请参阅[使用备用配置](https://www.keycloak.org/docs/latest/securing_apps/index.html#_working_with_alternative_configurations) 。

有关使用客户端注册CLI的详细信息，请参阅内置帮助。

例如，在：

- Linux:

```bash
$ kcreg.sh help
```

- Windows:

```bash
c:\> kcreg help
```

有关启动经过身份验证的会话的详细信息，请参阅`kcreg config credentials --help`。

#### 6.4.2. 使用其他配置 {#Working_with_alternative_configurations}
默认情况下，客户端注册CLI会自动将配置文件维护在用户主目录下的默认位置`./.keycloak/kcreg.config`中。 您可以使用`--config`选项指向不同的文件或位置，以并行保存多个经过身份验证的会话。 这是从单个线程执行绑定到单个配置文件的操作的最安全的方法。

> 不要使配置文件对系统上的其他用户可见。 配置文件包含应保密的访问令牌和秘密。

您可能希望通过在所有命令中使用`--no-config`选项来避免在配置文件中存储机密，即使它不太方便并且需要更多令牌请求。 使用每个`kcreg`调用指定所有身份验证信息。

#### 6.4.3. 初始访问和注册访问令牌 {#Initial_Access_and_Registration_Access_Tokens}
没有在他们想要使用的Keycloak服务器上配置帐户的开发人员可以使用客户端注册CLI。 仅当领域管理员向开发人员发出初始访问令牌时，才可能这样做。 由领域管理员决定如何以及何时发布和分发这些令牌。 领域管理员可以限制初始访问令牌的最长期限以及可以使用它创建的客户端总数。

一旦开发人员拥有初始访问令牌，开发人员就可以使用它来创建新客户端，而无需使用`kcreg config credentials`进行身份验证。 初始访问令牌可以存储在配置文件中，也可以作为`kcreg create`命令的一部分指定。

例如，在：

- Linux:

```bash
$ kcreg.sh config initial-token $TOKEN
$ kcreg.sh create -s clientId=myclient
```

或者

```bash
$ kcreg.sh create -s clientId=myclient -t $TOKEN
```

- Windows:

```bash
c:\> kcreg config initial-token %TOKEN%
c:\> kcreg create -s clientId=myclient
```

或者

```bash
c:\> kcreg create -s clientId=myclient -t %TOKEN%
```

使用初始访问令牌时，服务器响应包括新发布的注册访问令牌。 该客户端的任何后续操作都需要通过使用该令牌进行身份验证来执行，该令牌仅对该客户端有效。

客户端注册CLI自动使用其专用配置文件来保存此标记并将其与其关联的客户端一起使用。 只要将相同的配置文件用于所有客户端操作，开发人员就无需进行身份验证即可读取，更新或删除以这种方式创建的客户端。

有关初始访问和注册访问令牌的详细信息，请参阅[客户端注册](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_registration)。

运行`kcreg config initial-token --help`和`kcreg config registration-token --help`命令，以获取有关如何使用客户端注册CLI配置令牌的更多信息。

#### 6.4.4. 创建客户端配置 {#Creating_a_client_configuration}
使用凭据进行身份验证或配置初始访问令牌后的第一个任务通常是创建新客户端。 通常，您可能希望使用准备好的JSON文件作为模板，并设置或覆盖某些属性。

以下示例显示如何读取JSON文件，覆盖它可能包含的任何客户端ID，设置任何其他属性，以及在成功创建后将配置打印到标准输出。

- Linux:

```bash
$ kcreg.sh create -f client-template.json -s clientId=myclient -s baseUrl=/myclient -s 'redirectUris=["/myclient/*"]' -o
```

- Windows:

```bash
C:\> kcreg create -f client-template.json -s clientId=myclient -s baseUrl=/myclient -s "redirectUris=[\"/myclient/*\"]" -o
```

有关`kcreg create`命令的更多信息，请运行`kcreg create --help`。

您可以使用`kcreg attrs`列出可用属性。 请记住，未检查许多配置属性的有效性或一致性。 您可以指定正确的值。 请记住，模板中不应包含任何id字段，也不应将它们指定为`kcreg create`命令的参数。

#### 6.4.5. 检索客户端配置 {#Retrieving_a_client_configuration}
您可以使用`kcreg get`命令检索现有客户端。

例如，在：

- Linux:

```bash
$ kcreg.sh get myclient
```

- Windows:

```bash
C:\> kcreg get myclient
```

您还可以将客户端配置检索为适配器配置文件，您可以将其与Web应用程序打包在一起。

例如，在：

- Linux:

```bash
$ kcreg.sh get myclient -e install > keycloak.json
```

- Windows:

```bash
C:\> kcreg get myclient -e install > keycloak.json
```

有关`kcreg get`命令的更多信息，请运行`kcreg get --help`命令。

#### 6.4.6. 修改客户端配置 {#Modifying_a_client_configuration}
有两种更新客户端配置的方法。

一种方法是在获取当前配置，将其保存到文件，编辑它并将其发回服务器后，向服务器提交一个完整的新状态。

例如，在：

- Linux:

```bash
$ kcreg.sh get myclient > myclient.json
$ vi myclient.json
$ kcreg.sh update myclient -f myclient.json
```

- Windows:

```bash
C:\> kcreg get myclient > myclient.json
C:\> notepad myclient.json
C:\> kcreg update myclient -f myclient.json
```

第二种方法获取当前客户端，设置或删除其上的字段，并在一个步骤中将其发回。

例如，在：

- Linux:

```bash
$ kcreg.sh update myclient -s enabled=false -d redirectUris
```

- Windows:

```bash
C:\> kcreg update myclient -s enabled=false -d redirectUris
```

您还可以使用仅包含要应用的更改的文件，这样您就不必将太多值指定为参数。 在这种情况下，指定`--merge`告诉客户端注册CLI，而不是将JSON文件视为完整的新配置，它应将其视为要在现有配置上应用的一组属性。

例如，在：

- Linux:

```bash
$ kcreg.sh update myclient --merge -d redirectUris -f mychanges.json
```

- Windows:

```bash
C:\> kcreg update myclient --merge -d redirectUris -f mychanges.json
```

有关`kcreg update`命令的更多信息，请运行`kcreg update --help`命令。

#### 6.4.7. 删除客户端配置 {#Deleting_a_client_configuration}
使用以下示例删除客户端。

- Linux:

```bash
$ kcreg.sh delete myclient
```

- Windows:

```bash
C:\> kcreg delete myclient
```

有关`kcreg delete`命令的更多信息，请运行`kcreg delete --help`命令。

#### 6.4.8. 刷新无效的注册访问令牌 {#Refreshing_invalid_Registration_Access_Tokens}
使用`--no-config`模式执行创建，读取，更新和删除（CRUD）操作时，客户端注册CLI无法为您处理注册访问令牌。 在这种情况下，可能会丢失对客户端最近发布的注册访问令牌的跟踪，这使得无法在没有使用具有**manage-clients**权限的帐户进行身份验证的情况下在该客户端上执行任何进一步的CRUD操作。

如果您具有权限，则可以为客户端发出新的注册访问令牌，并将其打印到标准输出或保存到您选择的配置文件中。 否则，您必须要求领域管理员为您的客户发出新的注册访问令牌并将其发送给您。 然后，您可以通过`--token`选项将其传递给任何CRUD命令。 您还可以使用`kcreg config registration-token`命令将新令牌保存在配置文件中，并让客户端注册CLI从那时起自动为您处理。

有关`kcreg update-token`命令的更多信息，请运行`kcreg update-token --help`命令。

### 6.5. 故障排除 {#Troubleshooting}
- Q: 登录时，出现错误： *Parameter client_assertion_type is missing [invalid_client].

  A: 此错误意味着您的客户端配置了`Signed JWT(签名JWT)`令牌凭据，这意味着您必须在登录时使用`--keystore`参数。

## 7. 令牌交换 {#Token_Exchange}

> 令牌交换是**技术预览**，并不完全支持。 默认情况下禁用此功能。要启用`-Dkeycloak.profile=preview` 或 `-Dkeycloak.profile.feature.token_exchange=enabled`启动服务器。 有关详细信息，请参阅[Profiles](https://www.keycloak.org/docs/6.0/server_installation/#profiles)。

在Keycloak中，令牌交换是使用一组凭据或令牌来获取完全不同的令牌的过程。 客户端可能希望在不太受信任的应用程序上调用，因此它可能希望降级它具有的当前令牌。 客户端可能想要为存储用于链接的社交提供者帐户的令牌交换Keycloak令牌。 您可能希望信任由其他Keycloak领域或外国国内流离失所者铸造的外部令牌。 客户可能需要冒充用户。 以下是Keycloak围绕令牌交换的当前功能的简短摘要。

- 客户端可以交换为特定客户端创建的现有Keycloak令牌，以获取针对不同客户端的新令牌
- 客户端可以将现有的Keycloak令牌交换为外部令牌，即链接的Facebook帐户
- 客户端可以为Keycloak令牌交换外部令牌。
- 客户端可以冒充用户

Keycloak中的令牌交换是IETF中[OAuth令牌交换](https://tools.ietf.org/html/draft-ietf-oauth-token-exchange-16)规范的非常松散的实现。 我们已经扩展了一点，忽略了一些，并松散地解释了规范的其他部分。 它是领域的OpenID Connect令牌端点上的简单授权类型调用。

```bash
/auth/realms/{realm}/protocol/openid-connect/token
```

它接受表单参数（`application / x-www-form-urlencoded`）作为输入，输出取决于您请求交换的令牌类型。 令牌交换是客户端端点，因此请求必须为调用客户端提供身份验证信息。 公共客户端将其客户端标识符指定为表单参数。 机密客户端还可以使用表单参数来传递其客户端ID和密码，Basic Auth，或者您的管理员已在您的领域中配置了客户端身份验证流。 这是表单参数列表

- client_id

  *REQUIRED MAYBE.* 使用表单参数进行身份验证的客户端需要此参数。 如果您使用的是Basic Auth，客户端JWT令牌或客户端证书身份验证，则不要指定此参数。

- client_secret

  *REQUIRED MAYBE*. 对于使用表单参数进行身份验证并使用客户端机密作为凭据的客户端，此参数是必需的。 如果您的领域中的客户端调用通过其他方式进行身份验证，请不要指定此参数。

- grant_type

  *REQUIRED.* 参数的值必须是`urn:ietf:params:oauth:grant-type:token-exchange`。

- subject_token

  *OPTIONAL.* 代表正在进行请求的一方的身份的安全令牌。 如果您要为新的令牌交换现有令牌，则需要它。

- subject_issuer

  *OPTIONAL.* 标识`subject_token`的发行者。 如果令牌来自当前领域，或者如果可以从`subject_token_type`确定发行者，则可以将其留空。 否则需要指定。 有效值是为您的领域配置的“身份提供者”的别名。 或者由特定`Identity Provider`配置的发行者声明标识符。

- subject_token_type

  *OPTIONAL.* 此参数是使用`subject_token`参数传递的标记的类型。 如果`subject_token`来自领域并且是访问令牌，则默认为`urn:ietf:params:oauth:token-type:access_token`。 如果它是外部令牌，则可以根据`subject_issuer`的要求指定或不指定此参数。

- requested_token_type

  *OPTIONAL.* 此参数表示客户端要交换的令牌类型。 目前仅支持oauth和OpenID Connect令牌类型。 这个的默认值取决于是`urn:ietf:params:oauth:token-type:refresh_token`在这种情况下，您将在响应中返回访问令牌和刷新令牌。 其他合适的值是`urn:ietf:params:oauth:token-type:access_token` 和 `urn:ietf:params:oauth:token-type:id_token`

- audience

  *OPTIONAL.* 此参数指定要为其生成新令牌的目标客户端。

- requested_issuer

  *OPTIONAL.* 此参数指定客户端需要由外部提供者生成的令牌。它必须是在域中配置的`Identity Provider`的别名。

- requested_subject

  *OPTIONAL.* 如果您的客户想要模仿其他用户，则指定用户名或用户ID。

- scope

  *NOT IMPLEMENTED.* 此参数表示客户机请求的OAuth和OpenID连接范围的目标集。目前还没有实现，但是一旦Keycloak对范围有了更好的支持，它就会实现。

> 我们目前仅支持OpenID Connect和OAuth交换。 根据用户需求，将来可能会添加对基于SAML的客户端和身份提供商的支持。

来自交换调用的成功响应将返回HTTP 200响应代码，其内容类型取决于客户端要求的`requested-token-type`和`requested_issuer`。 OAuth请求的令牌类型将返回[OAuth令牌交换]（(https://tools.ietf.org/html/draft-ietf-oauth-token-exchange-16) 规范中所述的JSON文档。

```json
{
   "access_token" : ".....",
   "refresh_token" : ".....",
   "expires_in" : "...."
 }
```

请求刷新令牌的客户端将在响应中返回访问和刷新令牌。 仅请求访问令牌类型的客户端将仅在响应中获取访问令牌。 对于通过`requested_issuer`参数请求外部发行者的客户，可以包括或不包括到期信息。

错误响应通常属于400 HTTP响应代码类别，但可能会返回其他错误状态代码，具体取决于错误的严重性。 错误响应可能包括取决于`requested_issuer`的内容。 基于OAuth的交换可能会返回JSON文档，如下所示：

```json
{
   "error" : "...."
   "error_description" : "...."
}
```

可能会返回其他错误声明，具体取决于交换类型。 例如，如果用户没有指向身份提供者的链接，OAuth身份提供者可能会包含额外的`account-link-url`声明。 此链接可用于客户端发起的链接请求。

> 令牌交换设置需要了解细粒度管理员权限（有关详细信息，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)）。 您需要授予客户交换的权限。 这将在本章后面讨论。

本章的其余部分讨论了设置要求，并提供了不同交换方案的示例。 为简单起见，让我们将当前领域铸造的令牌称为*internal*令牌，并将外部域或身份提供者铸造的令牌称为*external*令牌。

### 7.1. 内部令牌到内部令牌交换 {#Internal_Token_to_Internal_Token_Exchange}
通过令牌交换的内部令牌，您可以将现有令牌添加到特定客户端，并且您希望将此令牌交换为为不同目标客户端创建的新令牌。 你为什么想做这个？ 这通常发生在客户端为自己创建一个令牌时，并且需要向访问令牌中需要不同声明和权限的其他应用程序发出其他请求。 可能需要此类交换的其他原因是，如果您需要执行“permission downgrade(权限降级)”，您的应用需要在不太受信任的应用上调用，并且您不希望传播当前的访问令牌。

#### 7.1.1. 授予Exchange权限 {#Granting_Permission_for_the_Exchange1}
想要为不同客户端交换令牌的客户端需要在管理控制台中进行授权才能这样做。 您需要在要获得权限交换的目标客户端中定义`token-exchange`细粒度权限。

目标客户端权限

![exchange target client permission unset](assets/exchange-target-client-permission-unset.png)

将`Permissions Enabled`开关切换为ON。

目标客户端权限

![exchange target client permission set](assets/exchange-target-client-permission-set.png)

您应该在页面上看到`token-exchange`链接。 单击该开始定义权限。 它会带你到这个页面。

目标客户端Exchange权限设置

![exchange target client permission setup](assets/exchange-target-client-permission-setup.png)

您必须为此权限定义策略。 单击`Authorization`链接，转到`Policies`选项卡并创建`Client`策略。

客户端策略创建

![exchange target client policy](assets/exchange-target-client-policy.png)

在此处输入起始客户端，即请求令牌交换的经过身份验证的客户端。 创建此策略后，返回目标客户端的`token-exchange`权限并添加刚刚定义的客户端策略。

应用客户端策略

![exchange target client exchange apply policy](assets/exchange-target-client-exchange-apply-policy.png)

您的客户现在有权调用。 如果您没有正确执行此操作，如果您尝试进行交换，则会收到403 Forbidden响应。

#### 7.1.2. 提出请求 {#Making_the_Request1}
当您的客户端为指向另一个客户端的令牌交换现有令牌时，您必须使用`audience`参数。 此参数必须是您在管理控制台中配置的目标客户端的客户端标识符。

```bash
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:refresh_token" \
    -d "audience=target-client" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

`subject_token`参数必须是目标领域的访问令牌。 如果您的`requested_token_type`参数是刷新令牌类型，则响应将包含访问令牌，刷新令牌和到期。 这是您从此调用中获得的示例JSON响应。

```bash
{
   "access_token" : "....",
   "refresh_token" : "....",
   "expires_in" : 3600
}
```

### 7.2. 外部令牌交换的内部令牌 {#Internal_Token_to_External_Token_Exchange}
您可以为外部身份提供者创建的外部令牌交换领域令牌。 必须在管理控制台的“身份提供程序”部分中配置此外部身份提供程序。 目前仅支持基于OAuth/OpenID Connect的外部身份提供商，这包括所有社交提供商。 Keycloak不会向外部提供商执行反向信道交换。 因此，如果帐户未链接，您将无法获得外部令牌。 为了能够获得外部令牌，必须满足以下条件之一：

- 用户必须至少使用外部身份提供商登录一次
- 用户必须通过用户帐户服务与外部身份提供商链接
- 用户帐户使用[客户端启动的帐户链接](https://www.keycloak.org/docs/6.0/server_development/)API通过外部身份提供商进行链接。

最后，外部身份提供程序必须已配置为存储令牌，或者，必须使用与要交换的内部令牌相同的用户会话执行上述操作之一。

如果帐户未链接，则交换响应将包含可用于建立帐户的链接。 这将在[发出请求](https://www.keycloak.org/docs/latest/securing_apps/index.html#_internal_external_making_request)部分中进行更多讨论。

#### 7.2.1. 授予Exchange权限 {#Granting_Permission_for_the_Exchange2}
在您授予呼叫客户端与外部身份提供商交换令牌的权限之前，将拒绝内部到外部令牌交换请求的403禁止响应。 要向客户端授予权限，您必须转到身份提供程序的配置页面中的`Permissions`选项卡。

身份提供商许可

![exchange idp permission unset](assets/exchange-idp-permission-unset.png)

将`Permissions Enabled`切换为true。

身份提供商许可

![exchange idp permission set](assets/exchange-idp-permission-set.png)

您应该在页面上看到`token-exchange`链接。 单击该开始定义权限。 它会带你到这个页面。

身份提供商Exchange权限设置

![exchange idp permission setup](assets/exchange-idp-permission-setup.png)

您必须为此权限定义策略。 单击`Authorization`链接，转到`Policies`选项卡并创建`Client`策略。

客户端策略创建

![exchange idp client policy](assets/exchange-idp-client-policy.png)

在此处输入起始客户端，即请求令牌交换的经过身份验证的客户端。 创建此策略后，返回到身份提供者的`token-exchange`权限并添加您刚定义的客户端策略。

应用客户端策略

![exchange idp apply policy](assets/exchange-idp-apply-policy.png)

您的客户现在有权调用。 如果您没有正确执行此操作，如果您尝试进行交换，则会收到403 Forbidden响应。

#### 7.2.2. 提出请求 {#Making_the_Request2}
当您的客户端将现有内部令牌交换到外部令牌时，您必须提供`requested_issuer`参数。 该参数必须是已配置的标识提供程序的别名。

```bash
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "requested_issuer=google" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

`subject_token`参数必须是目标领域的访问令牌。 `requested_token_type`参数必须是`urn:ietf:params:oauth:token-type:access_token`或留空。 目前不支持其他请求的令牌类型。 以下是您从此调用中获得的成功JSON响应示例。

```json
{
   "access_token" : "....",
   "expires_in" : 3600
   "account-link-url" : "https://...."
}
```

如果外部身份提供程序由于某种原因未链接，您将获得带有此JSON文档的HTTP 400响应代码：

```json
{
   "error" : "....",
   "error_description" : "..."
   "account-link-url" : "https://...."
}
```

`error`声明将是`token_expired`或`not_linked`。 提供了`account-link-url`声明，以便客户端可以执行[客户端发起的帐户链接](https://www.keycloak.org/docs/6.0/server_development/)。 大多数（所有？）提供商都需要通过浏览器OAuth协议进行链接。 使用`account-link-url`只需向其添加`redirect_uri`查询参数，您就可以转发浏览器来执行链接。

### 7.3. 外部令牌到内部令牌交换 {#External_Token_to_Internal_Token_Exchange}
您可以信任并交换外部身份提供商为内部令牌创建的外部令牌。 这可用于在领域之间架起桥梁，或仅用于信任来自社交提供商的令牌。 它与身份提供程序浏览器登录的工作方式类似，如果新用户不存在，则会将新用户导入您的领域。

> 外部令牌交换的当前限制是，如果外部令牌映射到现有用户，则不允许交换，除非现有用户已经具有到外部身份提供者的帐户链接。

交换完成后，将在领域内创建用户会话，您将根据`requested_token_type`参数值接收访问和/或刷新令牌。 您应该注意，此新用户会话将保持活动状态，直到它超时或直到您调用域的注销端点传递此新访问令牌为止。

这些类型的更改需要在管理控制台中配置身份提供程序。

> 目前不支持SAML身份提供商。 Twitter令牌也无法交换。

#### 7.3.1. 授予Exchange权限 {#Granting_Permission_for_the_Exchange3}
在进行外部令牌交换之前，您必须为调用客户端授予进行交换的权限。 此权限的授予方式与[授予外部权限的内部](https://www.keycloak.org/docs/latest/securing_apps/index.html#_grant_permission_external_exchange)相同。

如果还提供了一个`audience`参数，其值指向不同于客户端的客户端，则还必须授予调用客户端与`audience`参数中特定目标客户端交换的权限。 如何做到这一点[在前面已经讨论过](https://www.keycloak.org/docs/latest/securing_apps/index.html#_client_to_client_permission)。

#### 7.3.2. 提出请求 {#Making_the_Request3}
`subject_token_type`必须是`urn:ietf:params:oauth:token-type:access_token` 或 `urn:ietf:params:oauth:token-type:jwt`。 如果类型是`urn:ietf:params:oauth:token-type:access_token`，则必须指定`subject_issuer`参数，它必须是已配置的身份提供者的别名。 如果类型是 `urn:ietf:params:oauth:token-type:jwt`，则提供者将通过JWT中的`issuer`声明进行匹配，该声明必须是提供者的别名，或者提供者配置中的注册发布方。

对于验证，如果令牌是访问令牌，则将调用提供者的用户信息服务以验证令牌。 成功通话意味着访问令牌有效。 如果主题令牌是JWT并且如果提供者启用了签名验证，则将尝试进行，否则，它将默认也调用用户信息服务来验证令牌。

默认情况下，内部令牌铸造将使用调用客户端使用为调用客户端定义的协议映射器来确定令牌中的内容。 或者，您可以使用`audience`参数指定其他目标客户端。

```bash
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    -d "subject_issuer=myOidcProvider" \
    --data-urlencode "subject_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "audience=target-client" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

如果您的`requested_token_type`参数是刷新令牌类型，则响应将包含访问令牌，刷新令牌和到期。 这是您从此调用中获得的示例JSON响应。

```json
{
   "access_token" : "....",
   "refresh_token" : "....",
   "expires_in" : 3600
}
```

### 7.4. 模拟 {#Impersonation}
对于内部和外部令牌交换，客户端可以代表用户请求冒充不同的用户。 例如，您可能有一个需要模拟用户的管理应用程序，以便支持工程师可以调试问题。

#### 7.4.1. 授予Exchange权限 {#Granting_Permission_for_the_Exchange4}
主题令牌所代表的用户必须具有模仿其他用户的权限。 有关如何启用此权限，请参阅[服务器管理指南](https://www.keycloak.org/docs/6.0/server_admin/)。 它可以通过角色或细粒度管理员权限来完成。

#### 7.4.2. 提出请求 {#Making_the_Request4}
除了另外指定`request_subject`参数之外，按照其他章节中的描述发出请求。 此参数的值必须是用户名或用户ID。

```bash
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "subject_token=...." \
    --data-urlencode "requested_token_type=urn:ietf:params:oauth:token-type:access_token" \
    -d "audience=target-client" \
    -d "requested_subject=wburke" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

### 7.5. 直接赤裸裸的模拟 {#Direct_Naked_Impersonation}
您可以在不提供`subject_token`的情况下发出内部令牌交换请求。这称为直接裸模拟，因为它非常信任客户机，因为客户机可以模拟域中的任何用户。在无法获得要交换的主题令牌的应用程序中，您可能需要使用此桥接。例如，您可能正在集成一个直接使用LDAP执行登录的遗留应用程序。在这种情况下，遗留应用程序能够对用户进行身份验证，但无法获得令牌。

> 为客户端启用直接裸模拟是非常危险的。如果客户机的凭据被盗，则该客户机可以模拟系统中的任何用户。

#### 7.5.1. 授予Exchange权限 {#Granting_Permission_for_the_Exchange5}
如果提供了`audience`参数，则调用客户端必须具有与客户端交换的权限。 本章前面将讨论如何进行此设置。

此外，必须授予调用客户端模拟用户的权限。 在管理控制台中，转到`Users`屏幕，然后单击`Permissions`选项卡。

用户权限

![exchange users permission unset](assets/exchange-users-permission-unset.png)

将`Permissions Enabled`切换为true。

身份提供商许可

![exchange users permission set](assets/exchange-users-permission-set.png)

您应该在页面上看到`impersonation`链接。 单击该开始定义权限。 它会带你到这个页面。

用户模拟权限设置

![exchange users permission setup](assets/exchange-users-permission-setup.png)

您必须为此权限定义策略。 单击`Authorization`链接，转到`Policies`选项卡并创建`Client`策略。

客户端策略创建

![exchange users client policy](assets/exchange-users-client-policy.png)

在此处输入起始客户端，即请求令牌交换的经过身份验证的客户端。 创建此策略后，请返回用户的`impersonation`权限并添加刚刚定义的客户端策略。

应用客户端策略

![exchange users apply policy](assets/exchange-users-apply-policy.png)

您的客户机现在具有模仿用户的权限。如果您没有正确地执行此操作，如果您尝试进行这种类型的交换，您将得到403禁止响应。

> 公共客户端不允许直接进行裸模拟。

#### 7.5.2. 提出请求 {#Making_the_Request5}
要发出请求，只需指定`requested_subject`参数即可。 这必须是有效用户的用户名或用户ID。 如果您愿意，也可以指定`audience`参数。

```bash
curl -X POST \
    -d "client_id=starting-client" \
    -d "client_secret=geheim" \
    --data-urlencode "grant_type=urn:ietf:params:oauth:grant-type:token-exchange" \
    -d "requested_subject=wburke" \
    http://localhost:8080/auth/realms/myrealm/protocol/openid-connect/token
```

### 7.6. 使用服务帐户展开权限模型 {#Expand_Permission_Model_With_Service_Accounts}
授予客户端交换权限时，您不必为每个客户端手动启用这些权限。 如果客户端具有与之关联的服务帐户，则可以使用角色将权限组合在一起，并通过将角色分配给客户端的服务帐户来分配交换权限。 例如，您可以定义一个`naked-exchange`角色，任何具有该角色的服务帐户都可以进行裸交换。

### 7.7. 交换漏洞 {#Exchange_Vulnerabilities}
当你开始允许令牌交换时，你需要注意和注意各种各样的事情。

首先是公共客户。 公共客户端没有或需要客户端凭证才能执行交换。 任何拥有有效令牌的人都可以*impersonate*公共客户端并执行允许公共客户端执行的交换。 如果有任何由您的域管理的不值得信任的客户端，公共客户端可能会在您的权限模型中打开漏洞。 这就是为什么直接裸交换不允许公共客户端，并且如果主叫客户端是公共的，将中止错误。

可以交换由Facebook，Google等提供的社交令牌作为领域令牌。 请小心并警惕交换令牌可以做什么，因为在这些社交网站上创建虚假账户并不困难。 使用默认角色，组和身份提供程序映射器来控制为外部社交用户分配的属性和角色。

直接的裸交换非常危险。 您对调用客户端非常信任它永远不会泄露其客户端凭据。 如果这些凭据被泄露，那么小偷可以冒充你系统中的任何人。 这与具有现有令牌的机密客户形成鲜明对比。 您有两个身份验证因素，访问令牌和客户端凭据，而您只处理一个用户。 因此，请谨慎使用直接裸交换。

